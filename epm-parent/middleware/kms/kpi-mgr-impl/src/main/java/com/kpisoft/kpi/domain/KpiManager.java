package com.kpisoft.kpi.domain;

import org.springframework.stereotype.*;
import org.apache.log4j.*;
import com.kpisoft.kpi.dac.*;
import javax.validation.*;
import com.canopus.mw.cache.*;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.frames.*;
import org.springframework.beans.factory.annotation.*;
import com.tinkerpop.blueprints.impls.tg.*;
import org.perf4j.aop.*;
import javax.annotation.*;
import com.kpisoft.kpi.vo.param.*;
import com.canopus.mw.*;
import com.kpisoft.kpi.utility.*;
import com.kpisoft.kpi.vo.*;
import org.perf4j.*;
import com.kpisoft.kpi.impl.graph.*;
import com.canopus.mw.dto.*;
import java.util.*;

@Component
public class KpiManager extends BaseDomainManager implements CacheLoader<Integer, Kpi>
{
    private static final Logger log;
    @Autowired
    private KpiDataService dataService;
    @Autowired
    private Validator validator;
    @Autowired
    @Qualifier("kpiCache")
    private Cache<Integer, Kpi> cache;
    private Graph graph;
    private FramedGraph<Graph> framedGraph;
    private KpiRelationship kpiRelationship;
    private Map<Integer, PeriodMasterBean> periodMasterMap;
    @Value("${cache.masterdata.startup:true}")
    private boolean masterDataCacheOnStartup;
    @Value("${cache.kpi.size:10000}")
    private Integer kpiCacheSize;
    @Value("${cache.kpi.startup:true}")
    private boolean kpiCacheOnStartup;
    @Value("${graph.kpi.startup:true}")
    private boolean kpiGraphOnStartup;
    
    KpiManager() {
        this.dataService = null;
        this.validator = null;
        this.cache = null;
        this.graph = null;
        this.framedGraph = null;
        this.kpiRelationship = null;
        this.periodMasterMap = null;
        this.masterDataCacheOnStartup = false;
        this.kpiCacheSize = 10000;
        this.kpiCacheOnStartup = false;
        this.kpiGraphOnStartup = false;
        this.graph = (Graph)new TinkerGraph();
        this.framedGraph = (FramedGraph<Graph>)new FramedGraph(this.graph);
        this.kpiRelationship = new KpiRelationship(this.framedGraph);
        this.periodMasterMap = new HashMap<Integer, PeriodMasterBean>();
    }
    
    @Profiled(tag = "KPI Masters Init")
    @PostConstruct
    public void loadToCache() {
        System.out.println("---------masterDataCacheOnStartup----------" + this.masterDataCacheOnStartup);
        System.out.println("---------kpiCacheSize----------" + this.kpiCacheSize);
        if (!this.masterDataCacheOnStartup) {
            return;
        }
        KpiManager.log.debug((Object)"KPI Masters initializing");
        ExecutionContext.getCurrent().setCrossTenant();
        try {
            this.loadAllKpiDependencies();
        }
        catch (Exception ex) {}
        ExecutionContext.getCurrent().unSetCrossTenant();
    }
    
    @PostConstruct
    public void loadKpiToCache() {
        if (!this.kpiCacheOnStartup) {
            return;
        }
        ExecutionContext.getCurrent().setCrossTenant();
        try {
            final Request request = new Request();
            request.put(KpiParams.KPI_COUNT.name(), (BaseValueObject)new Identifier(this.kpiCacheSize));
            final Response response = this.dataService.getKpisByCount(request);
            final BaseValueObjectList bList = (BaseValueObjectList)response.get(KpiParams.KPI_DATA_LIST.name());
            final List<KpiData> kpiList = (List<KpiData>)bList.getValueObjectList();
            for (final KpiData kpi : kpiList) {
                final Kpi kpiDomain = new Kpi(this);
                kpiDomain.setKpiDetails(kpi);
                this.getCache().put(kpi.getId(), kpiDomain);
            }
        }
        catch (Exception e) {
            KpiManager.log.error((Object)("Exception in KpiManager - loadKpiToCache() : " + e));
        }
        ExecutionContext.getCurrent().unSetCrossTenant();
    }
    
    public void loadAllKpiDependencies() {
        try {
            this.periodMasterMap.clear();
            final Response response = this.dataService.getAllPeriodMsaters(new Request());
            final BaseValueObjectList list = (BaseValueObjectList)response.get(KpiParams.PERIOD_MASTER_DATA_LIST.name());
            if (list != null && list.getValueObjectList() != null && !list.getValueObjectList().isEmpty()) {
                final List<PeriodMasterBean> result = (List<PeriodMasterBean>)list.getValueObjectList();
                for (final PeriodMasterBean bean : result) {
                    this.periodMasterMap.put(bean.getId(), bean);
                }
            }
        }
        catch (Exception e) {
            KpiManager.log.error((Object)("Exception in KpiManager - loadAllKpiDependencies() : " + e));
        }
    }
    
    public List<PeriodMasterBean> getAllPeriodMsaters() {
        final List<PeriodMasterBean> result = new ArrayList<PeriodMasterBean>();
        if (this.periodMasterMap.isEmpty()) {
            this.loadAllKpiDependencies();
        }
        for (final Integer key : this.periodMasterMap.keySet()) {
            result.add(this.periodMasterMap.get(key));
        }
        return result;
    }
    
    public PeriodMasterBean getPeriodMasterById(final Integer periodMasterId) {
        if (this.periodMasterMap.isEmpty()) {
            this.loadAllKpiDependencies();
        }
        if (this.periodMasterMap.containsKey(periodMasterId)) {
            return this.periodMasterMap.get(periodMasterId);
        }
        return null;
    }
    
    public Kpi createKpi(final KpiData data) {
        final Kpi kpi = new Kpi(this);
        kpi.setKpiDetails(data);
        try {
            final int id = kpi.save();
            kpi.getKpiDetails().setId(id);
        }
        catch (Exception e) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_CREATE_003.name(), e.getMessage());
        }
        finally {
            if (data.getId() != null && data.getId() > 0) {
                this.cache.remove(data.getId());
            }
        }
        return kpi;
    }
    
    public Integer updateKpiDetailsForMultiple(final BaseValueObjectList kpiUtilList) {
        final Request request = new Request();
        request.put(KpiParams.KPI_UTILITY_LIST.name(), (BaseValueObject)kpiUtilList);
        final Response response = this.dataService.updateKpiDetailsForMultiple(request);
        final Identifier identifier = (Identifier)response.get(KpiParams.COUNT.name());
        if (kpiUtilList != null && kpiUtilList.getValueObjectList() != null) {
            for (final KpiUtility updateKpi : (List<KpiUtility>)kpiUtilList.getValueObjectList()) {
                final Kpi kpi = (Kpi)this.getCache().getIfPresent(updateKpi.getId());
                if (kpi != null && kpi.getKpiDetails() != null) {
                    kpi.getKpiDetails().setWeightage(updateKpi.getWeightage());
                    this.getCache().put(kpi.getKpiDetails().getId(), kpi);
                }
            }
        }
        return identifier.getId();
    }
    
    public Integer saveScoreDetailsInKpiForMultiple(final BaseValueObjectList kpiUtilList) {
        final Request request = new Request();
        request.put(KpiParams.KPI_SCORE_DATA_LIST.name(), (BaseValueObject)kpiUtilList);
        final Response response = this.dataService.saveScoreDetailsInKpiForMultiple(request);
        final Identifier identifier = (Identifier)response.get(KpiParams.COUNT.name());
        return identifier.getId();
    }
    
    public Kpi getKpi(final Integer kpiId) {
        final Kpi kpi = (Kpi)this.getCache().get(kpiId, (CacheLoader)this);
        return kpi;
    }
    
    public boolean deleteKpi(final Integer kpiId) {
        final Kpi kpi = new Kpi(this);
        final boolean status = kpi.deleteKpi(kpiId);
        if (status) {
            this.getCache().remove(kpiId);
        }
        return status;
    }
    
    public boolean deleteKpis(final List<Integer> kpiIdList, final Integer kpiId) {
        final Kpi kpi = new Kpi(this);
        final List<Integer> kpiRelIdList = new ArrayList<Integer>();
        List<KpiKpiGraphRelationshipBean> kpiRelBeanList = new ArrayList<KpiKpiGraphRelationshipBean>();
        final KpiKpiGraphRelationshipBean bean = new KpiKpiGraphRelationshipBean();
        final KpiIdentityBean idBean = new KpiIdentityBean();
        idBean.setId(kpiId);
        bean.setParent(idBean);
        kpiRelBeanList = this.getKpiKpiRelationships(bean);
        if (kpiRelBeanList != null) {
            for (final KpiKpiGraphRelationshipBean iterator : kpiRelBeanList) {
                kpiRelIdList.add(iterator.getId());
            }
        }
        final boolean status = kpi.deleteKpis(kpiIdList, kpiRelIdList);
        if (status) {
            for (final Integer kpiIds : kpiIdList) {
                this.getCache().remove(kpiIds);
            }
            if (kpiRelBeanList != null) {
                for (final KpiKpiGraphRelationshipBean iterator2 : kpiRelBeanList) {
                    final Kpi child = (Kpi)this.getCache().getIfPresent(iterator2.getChild().getId());
                    if (child != null) {
                        child.removeParentRelationship(kpiId);
                    }
                }
            }
        }
        return status;
    }
    
    public boolean deactivateKpis(final List<Integer> kpiIdList, final Integer kpiId) {
        final Kpi kpi = new Kpi(this);
        final List<Integer> kpiRelIdList = new ArrayList<Integer>();
        List<KpiKpiGraphRelationshipBean> kpiRelBeanList = new ArrayList<KpiKpiGraphRelationshipBean>();
        final KpiKpiGraphRelationshipBean bean = new KpiKpiGraphRelationshipBean();
        final KpiIdentityBean idBean = new KpiIdentityBean();
        idBean.setId(kpiId);
        bean.setParent(idBean);
        kpiRelBeanList = this.getKpiKpiRelationships(bean);
        if (kpiRelBeanList != null) {
            for (final KpiKpiGraphRelationshipBean iterator : kpiRelBeanList) {
                kpiRelIdList.add(iterator.getId());
            }
        }
        final boolean status = kpi.deactivateKpis(kpiIdList, kpiRelIdList);
        if (status) {
            if (kpiIdList != null && !kpiIdList.isEmpty()) {
                for (final Integer id : kpiIdList) {
                    final Kpi kpid = (Kpi)this.getCache().getIfPresent(id);
                    if (kpid != null && kpid.getKpiDetails() != null) {
                        kpid.getKpiDetails().setStatus(0);
                        this.getCache().put(kpid.getKpiDetails().getId(), kpid);
                    }
                }
            }
            if (kpiRelBeanList != null) {
                for (final KpiKpiGraphRelationshipBean iterator2 : kpiRelBeanList) {
                    final Kpi child = (Kpi)this.getCache().getIfPresent(iterator2.getChild().getId());
                    if (child != null) {
                        child.removeParentRelationship(kpiId);
                    }
                }
            }
        }
        return status;
    }
    
    public Integer getKpiCountByScoreCard(final Integer scorecardId, final String kpiTypeName) {
        final Kpi kpi = new Kpi(this);
        final Integer count = kpi.getKpiCountByScoreCard(scorecardId, kpiTypeName);
        return count;
    }
    
    public List<KpiData> getKpisForEmployee(final Integer ownerId, Page page, SortList sortList) {
        final Request request = new Request();
        request.put(KpiParams.KPI_OWNER_ID.name(), (BaseValueObject)new Identifier(ownerId));
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.getKpisForEmployee(request);
        final BaseValueObjectList result = (BaseValueObjectList)response.get(KpiParams.KPI_DATA_LIST.name());
        final List<KpiData> data = (List<KpiData>)result.getValueObjectList();
        page = response.getPage();
        sortList = response.getSortList();
        return data;
    }
    
    public List<KpiData> getKpisByStrategyNode(final Integer strategyNodeId, Page page, SortList sortList) {
        final Request request = new Request();
        request.put(KpiParams.STRATEGY_NODE_ID.name(), (BaseValueObject)new Identifier(strategyNodeId));
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.getKpisByStrategyNode(request);
        final BaseValueObjectList result = (BaseValueObjectList)response.get(KpiParams.KPI_DATA_LIST.name());
        final List<KpiData> data = (List<KpiData>)result.getValueObjectList();
        page = response.getPage();
        sortList = response.getSortList();
        return data;
    }
    
    public boolean validateKpiWithRootcode(final Integer ownerId, final String kpiRootCode) {
        final Request request = new Request();
        request.put(KpiParams.KPI_OWNER_ID.name(), (BaseValueObject)new Identifier(ownerId));
        request.put(KpiParams.KPI_ROOT_CODE.name(), (BaseValueObject)new StringIdentifier(kpiRootCode));
        final Response response = this.dataService.validateKpiWithRootcode(request);
        final BooleanResponse status = (BooleanResponse)response.get(KpiParams.BOOL_VALID_KPI.name());
        return status.isResponse();
    }
    
    public Kpi load(final Integer key) {
        final KpiDataService svc = this.getDataService();
        final Request request = new Request();
        request.put(KpiParams.KPI_ID.name(), (BaseValueObject)new Identifier(key));
        final Response response = svc.getKpi(request);
        final KpiData data = (KpiData)response.get(KpiParams.KPI_DATA.name());
        final Kpi kpi = new Kpi(this);
        kpi.setKpiDetails(data);
        return kpi;
    }
    
    public Integer createKpiUom(final KpiUomData data) {
        final Kpi kpi = new Kpi(this);
        kpi.setKpiUomDetails(data);
        final int id = kpi.saveKpiUom();
        return id;
    }
    
    public KpiUomData getKpiUom(final Integer kpiUomId) {
        final Kpi kpi = new Kpi(this);
        final KpiUomData kpiUomData = kpi.getKpiUom(kpiUomId);
        return kpiUomData;
    }
    
    public List<KpiUomData> getAllKpiUom() {
        final Kpi kpi = new Kpi(this);
        final List<KpiUomData> kpiUomData = kpi.getAllKpiUom();
        return kpiUomData;
    }
    
    public List<KpiUomData> getAllKpiUom(final SortList sortList) {
        final Kpi kpi = new Kpi(this);
        final List<KpiUomData> kpiUomData = kpi.getAllKpiUom(sortList);
        return kpiUomData;
    }
    
    public boolean deleteKpiUom(final Integer kpiUomId) {
        final Kpi kpi = new Kpi(this);
        final boolean status = kpi.deleteKpiUom(kpiUomId);
        return status;
    }
    
    public Integer createKpiType(final KpiTypeData data) {
        final Kpi kpi = new Kpi(this);
        kpi.setKpiTypeData(data);
        final Integer id = kpi.saveKpiType();
        return id;
    }
    
    public KpiTypeData getKpiType(final Integer kpiTypeId) {
        final Kpi kpi = new Kpi(this);
        final KpiTypeData data = kpi.getKpiType(kpiTypeId);
        return data;
    }
    
    public List<KpiTypeData> getAllKpiType(SortList sortList) {
        final Request request = new Request();
        request.setSortList(sortList);
        final Response response = this.dataService.getAllKpiType(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(KpiParams.KPI_TYPE_DATA_LIST.name());
        final List<KpiTypeData> result = (List<KpiTypeData>)list.getValueObjectList();
        sortList = response.getSortList();
        return result;
    }
    
    public boolean deleteKpiType(final Integer kpiTypeId) {
        final Kpi kpi = new Kpi(this);
        final boolean status = kpi.deleteKpiType(kpiTypeId);
        return status;
    }
    
    public KpiAggregationTypeBean getKpiAggregationTypeById(final Integer aggregationTypeId) {
        final Request request = new Request();
        request.put(KpiParams.KPI_AGGR_TYPE_ID.name(), (BaseValueObject)new Identifier(aggregationTypeId));
        final Response response = this.dataService.getKpiAggregationTypeById(request);
        final KpiAggregationTypeBean data = (KpiAggregationTypeBean)response.get(KpiParams.KPI_AGGR_TYPE_DATA.name());
        return data;
    }
    
    public List<KpiAggregationTypeBean> getAllKpiAggregationTypes(SortList sortList) {
        final Request request = new Request();
        request.setSortList(sortList);
        final Response response = this.dataService.getAllKpiAggregationTypes(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(KpiParams.KPI_AGGR_TYPE_DATA_LIST.name());
        final List<KpiAggregationTypeBean> result = (List<KpiAggregationTypeBean>)list.getValueObjectList();
        sortList = response.getSortList();
        return result;
    }
    
    public Integer saveKpiScore(final KpiScoreBean data) {
        final Request request = new Request();
        request.put(KpiParams.KPI_SCORE_DATA.name(), (BaseValueObject)data);
        final Response response = this.dataService.saveKpiScore(request);
        final Identifier identifier = (Identifier)response.get(KpiParams.KPI_SCORE_DATA_ID.name());
        return identifier.getId();
    }
    
    public Integer saveKpiScoreForMultiple(final BaseValueObjectList scoreList) {
        final Request request = new Request();
        request.put(KpiParams.KPI_SCORE_DATA_LIST.name(), (BaseValueObject)scoreList);
        final Response response = this.dataService.saveKpiScoreForMultiple(request);
        final Identifier identifier = (Identifier)response.get(KpiParams.COUNT.name());
        return identifier.getId();
    }
    
    public KpiScoreBean getKpiScoreById(final Integer id) {
        final Request request = new Request();
        request.put(KpiParams.KPI_SCORE_DATA_ID.name(), (BaseValueObject)new Identifier(id));
        final Response response = this.dataService.getKpiScoreById(request);
        final KpiScoreBean scoreBean = (KpiScoreBean)response.get(KpiParams.KPI_SCORE_DATA.name());
        return scoreBean;
    }
    
    public List<PeriodTypeBean> getAllPeriodTypes(SortList sortList) {
        final Request request = new Request();
        request.setSortList(sortList);
        final Response response = this.dataService.getAllPeriodTypes(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(KpiParams.PERIOD_TYPE_LIST.name());
        final List<PeriodTypeBean> result = (List<PeriodTypeBean>)list.getValueObjectList();
        sortList = response.getSortList();
        return result;
    }
    
    public List<KpiData> getPositionKpiEmployeeRelations(final Integer id) {
        final Identifier objIdentifier = new Identifier();
        objIdentifier.setId(id);
        final Request request = new Request();
        request.put(KpiParams.POSITION_ID.name(), (BaseValueObject)objIdentifier);
        final Response response = this.dataService.getPositionKpiEmployeeRelations(request);
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get(KpiParams.KPI_DATA_LIST.name());
        final List<KpiData> alKpiData = (List<KpiData>)objectList.getValueObjectList();
        return alKpiData;
    }
    
    public List<KpiData> searchKpi(final KpiData kpiData, final Page page, final SortList sortList) {
        final Request request = new Request();
        request.put(KpiParams.KPI_DATA.name(), (BaseValueObject)kpiData);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.searchKpi(request);
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get(KpiParams.KPI_DATA_LIST.name());
        final List<KpiData> alKpiData = (List<KpiData>)objectList.getValueObjectList();
        return alKpiData;
    }
    
    public List<KpiScoreBean> searchKpiScore(final KpiScoreBean kpiScoreBean, final Page page, final SortList sortList) {
        final Request request = new Request();
        request.put(KpiParams.KPI_SCORE_DATA.name(), (BaseValueObject)kpiScoreBean);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.searchKpiScore(request);
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get(KpiParams.KPI_SCORE_DATA_LIST.name());
        final List<KpiScoreBean> alKpiScore = (List<KpiScoreBean>)objectList.getValueObjectList();
        return alKpiScore;
    }
    
    public List<PeriodTypeBean> searchPeriodType(final PeriodTypeBean periodType, SortList sortList) {
        final Request request = new Request();
        request.setSortList(sortList);
        request.put(KpiParams.PERIOD_TYPE_DATA.name(), (BaseValueObject)periodType);
        final Response response = this.dataService.searchPeriodType(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(KpiParams.PERIOD_TYPE_LIST.name());
        final List<PeriodTypeBean> periodTypeList = (List<PeriodTypeBean>)list.getValueObjectList();
        sortList = response.getSortList();
        return periodTypeList;
    }
    
    public List<PeriodMasterBean> getPeriodMasterByPeriodType(final Integer id, SortList sortList) {
        final Request request = new Request();
        request.put(KpiParams.PERIOD_TYPE_ID.name(), (BaseValueObject)new Identifier(id));
        request.setSortList(sortList);
        final Response response = this.dataService.getPeriodMasterByPeriodType(request);
        final BaseValueObjectList result = (BaseValueObjectList)response.get(KpiParams.PERIOD_MASTER_DATA_LIST.name());
        final List<PeriodMasterBean> data = (List<PeriodMasterBean>)result.getValueObjectList();
        sortList = response.getSortList();
        return data;
    }
    
    public List<KpiTagBean> searchKpiTag(final KpiTagBean kpiTag, SortList sortList) {
        final Request request = new Request();
        request.put(KpiParams.KPI_TAG_DATA.name(), (BaseValueObject)kpiTag);
        request.setSortList(sortList);
        final Response response = this.dataService.searchKpiTag(request);
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get(KpiParams.KPI_TAG_DATA_LIST.name());
        final List<KpiTagBean> allKpiTag = (List<KpiTagBean>)objectList.getValueObjectList();
        sortList = response.getSortList();
        return allKpiTag;
    }
    
    public void addKpiKpiRelationshipInGraph(final KpiKpiGraphRelationshipBean data) {
        final KpiGraph.Kpi chKpi = (KpiGraph.Kpi)this.kpiRelationship.add(data.getChild().getId(), (Class)KpiGraph.Kpi.class);
        chKpi.setKpiId(data.getChild().getId());
        chKpi.setKpiCode(data.getChild().getCode());
        chKpi.setKpiName(data.getChild().getName());
        chKpi.setKpiWeightage(data.getChild().getWeightage());
        final KpiGraph.Kpi parKpi = (KpiGraph.Kpi)this.kpiRelationship.add(data.getParent().getId(), (Class)KpiGraph.Kpi.class);
        parKpi.setKpiId(data.getParent().getId());
        parKpi.setKpiCode(data.getParent().getCode());
        parKpi.setKpiName(data.getParent().getName());
        parKpi.setKpiWeightage(data.getParent().getWeightage());
        this.kpiRelationship.addParent(data);
    }
    
    public void updateGraph(final KpiData kpi) {
        final Integer kpiId = kpi.getId();
        final List<KpiIdentityBean> parents = this.getParents(kpiId);
        if (parents != null && parents.size() > 0) {
            for (final KpiIdentityBean parent : parents) {
                this.kpiRelationship.removeParent(parent.getId(), kpiId);
            }
        }
        final List<KpiKpiGraphRelationshipBean> kpiRelList = (List<KpiKpiGraphRelationshipBean>)kpi.getKpiKpiRelationship();
        if (kpiRelList != null && kpiRelList.size() > 0) {
            for (final KpiKpiGraphRelationshipBean relBean : kpiRelList) {
                relBean.getChild().setId(kpiId);
                this.addKpiKpiRelationshipInGraph(relBean);
            }
        }
    }
    
    public boolean updateKpiGraph(final List<KpiKpiGraphRelationshipBean> kpiRelList) {
        try {
            if (kpiRelList != null && kpiRelList.size() > 0) {
                for (final KpiKpiGraphRelationshipBean relBean : kpiRelList) {
                    this.addKpiKpiRelationshipInGraph(relBean);
                }
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public void removeParent(final Integer pId, final Integer chId, final String type) {
        if (this.kpiRelationship.isAvailable(pId) && this.kpiRelationship.isAvailable(chId)) {
            this.kpiRelationship.removeParent(pId, chId, type);
        }
    }
    
    public void removeParent(final Integer pId, final Integer chId) {
        if (this.kpiRelationship.isAvailable(pId) && this.kpiRelationship.isAvailable(chId)) {
            this.kpiRelationship.removeParent(pId, chId);
        }
    }
    
    public KpiRelationship getKpiRelationship() {
        return this.kpiRelationship;
    }
    
    @Profiled(tag = "KPI Graph Init")
    @PostConstruct
    public void reloadKpiKpiRelationshipGraph() {
        if (!this.kpiCacheOnStartup) {
            return;
        }
        try {
            KpiManager.log.debug((Object)"KPI graph initializing");
            final StopWatch sw = new StopWatch("get-kpigraph");
            sw.start();
            final Request request = new Request();
            final KpiKpiGraphRelationshipBean gBean = new KpiKpiGraphRelationshipBean();
            request.put(KpiParams.KPI_REL_DATA.name(), (BaseValueObject)gBean);
            final Response response = this.dataService.getAllKpiRelationships(request);
            final BaseValueObjectList list = (BaseValueObjectList)response.get(KpiParams.KPI_KPI_REL_LIST.name());
            if (list != null) {
                final List<KpiKpiGraphRelationshipBean> graphList = (List<KpiKpiGraphRelationshipBean>)list.getValueObjectList();
                if (graphList != null && graphList.size() > 0) {
                    for (final KpiKpiGraphRelationshipBean data : graphList) {
                        final KpiGraph.Kpi chKpi = (KpiGraph.Kpi)this.kpiRelationship.add(data.getChild().getId(), (Class)KpiGraph.Kpi.class);
                        chKpi.setKpiId(data.getChild().getId());
                        chKpi.setKpiCode(data.getChild().getCode());
                        chKpi.setKpiName(data.getChild().getName());
                        chKpi.setKpiWeightage(data.getChild().getWeightage());
                        final KpiGraph.Kpi parKpi = (KpiGraph.Kpi)this.kpiRelationship.add(data.getParent().getId(), (Class)KpiGraph.Kpi.class);
                        parKpi.setKpiId(data.getParent().getId());
                        parKpi.setKpiCode(data.getParent().getCode());
                        parKpi.setKpiName(data.getParent().getName());
                        parKpi.setKpiWeightage(data.getParent().getWeightage());
                        this.kpiRelationship.addParent(data);
                    }
                }
            }
            sw.stop();
            KpiManager.log.debug((Object)("Manager - Kpi Kpi Graph Load time:" + sw.getElapsedTime()));
        }
        catch (Exception ex) {}
    }
    
    public List<KpiKpiGraphRelationshipBean> getKpiKpiRelationships(final KpiKpiGraphRelationshipBean bean) {
        final Request request = new Request();
        request.put(KpiParams.KPI_REL_DATA.name(), (BaseValueObject)bean);
        final BaseValueObjectList list = (BaseValueObjectList)this.dataService.getKpiKpiRelationships(request).get(KpiParams.KPI_KPI_REL_LIST.name());
        final List<KpiKpiGraphRelationshipBean> graphRel = (List<KpiKpiGraphRelationshipBean>)list.getValueObjectList();
        return graphRel;
    }
    
    public StringIdentifier getKpiRelationshipTypeNameById(final Identifier identifier) {
        final Request request = new Request();
        request.put(KpiParams.KPI_REL_TYPE_ID.name(), (BaseValueObject)identifier);
        final Response response = this.dataService.getKpiRelationshipTypeNameById(request);
        final StringIdentifier strIden = (StringIdentifier)response.get(KpiParams.KPI_REL_TYPE_NAME.name());
        return strIden;
    }
    
    public List<KpiData> getKpisByScorecardIdAndKpiTagId(final Identifier scoreCardId, final Identifier tagId, Page page, SortList sortList) {
        final Request request = new Request();
        request.put(KpiParams.KPI_TAG_ID.name(), (BaseValueObject)tagId);
        request.put(KpiParams.SCORECARD_ID.name(), (BaseValueObject)scoreCardId);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.getKpisByScorecardIdAndKpiTagId(request);
        page = response.getPage();
        sortList = response.getSortList();
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(KpiParams.KPI_DATA_LIST.name());
        final List<KpiData> scList = (List<KpiData>)bList.getValueObjectList();
        return scList;
    }
    
    public KpiIdentityBean getKpiIdentity(final KpiGraph.Kpi kpi) {
        final KpiIdentityBean kpiIdentityBean = new KpiIdentityBean();
        kpiIdentityBean.setCode(kpi.getKpiCode());
        kpiIdentityBean.setId(kpi.getKpiId());
        kpiIdentityBean.setName(kpi.getName());
        kpiIdentityBean.setWeightage(kpi.getKpiWeightage());
        return kpiIdentityBean;
    }
    
    public List<Integer> getIdListFromIdObjectList(final List<KpiIdentityBean> childIdList) {
        final List<Integer> finalIdList = new ArrayList<Integer>();
        for (final KpiIdentityBean id : childIdList) {
            finalIdList.add(id.getId());
        }
        return finalIdList;
    }
    
    public List<Integer> getIdListFromIdObjectList(final Set<KpiIdentityBean> childIdList) {
        final List<Integer> finalIdList = new ArrayList<Integer>();
        for (final KpiIdentityBean id : childIdList) {
            finalIdList.add(id.getId());
        }
        return finalIdList;
    }
    
    public List<KpiIdentityBean> getListFromSet(final Set<KpiIdentityBean> childIdList) {
        final List<KpiIdentityBean> finalIdList = new ArrayList<KpiIdentityBean>();
        for (final KpiIdentityBean id : childIdList) {
            finalIdList.add(id);
        }
        return finalIdList;
    }
    
    public List<KpiIdentityBean> getParentsByType(final Integer id, final String type) {
        final List<KpiIdentityBean> idObjList = new ArrayList<KpiIdentityBean>();
        if (!this.kpiRelationship.isAvailable(id)) {
            return idObjList;
        }
        final List<KpiGraph.Kpi> kpiList = this.kpiRelationship.getParents(id, type);
        for (final KpiGraph.Kpi kpi : kpiList) {
            idObjList.add(this.getKpiIdentity(kpi));
        }
        return idObjList;
    }
    
    public List<KpiIdentityBean> getParents(final Integer id) {
        final List<KpiIdentityBean> idObjList = new ArrayList<KpiIdentityBean>();
        if (!this.kpiRelationship.isAvailable(id)) {
            return idObjList;
        }
        final List<KpiGraph.Kpi> kpiList = this.kpiRelationship.getParents(id);
        for (final KpiGraph.Kpi kpi : kpiList) {
            idObjList.add(this.getKpiIdentity(kpi));
        }
        return idObjList;
    }
    
    public List<KpiIdentityBean> getAscendants(final Integer id, final Integer depth) {
        final List<KpiIdentityBean> idObjList = new ArrayList<KpiIdentityBean>();
        if (!this.kpiRelationship.isAvailable(id)) {
            return idObjList;
        }
        final List<KpiGraph.Kpi> kpiList = this.kpiRelationship.getAscendants(id, depth);
        for (final KpiGraph.Kpi kpi : kpiList) {
            idObjList.add(this.getKpiIdentity(kpi));
        }
        return idObjList;
    }
    
    public List<KpiIdentityBean> getAscendantsByType(final Integer id, final String type, final Integer depth) {
        final List<KpiIdentityBean> idObjList = new ArrayList<KpiIdentityBean>();
        if (!this.kpiRelationship.isAvailable(id)) {
            return idObjList;
        }
        final List<KpiGraph.Kpi> kpiList = this.kpiRelationship.getAscendants(id, type, depth);
        for (final KpiGraph.Kpi kpi : kpiList) {
            idObjList.add(this.getKpiIdentity(kpi));
        }
        return idObjList;
    }
    
    public Graph getAscendantsGraph(final Integer id, final Integer depth) {
        if (!this.kpiRelationship.isAvailable(id)) {
            return null;
        }
        final Graph graph = this.kpiRelationship.getAscendantsGraph(id, depth);
        return graph;
    }
    
    public Graph getAscendantsGraphByType(final Integer id, final String type, final Integer depth) {
        if (!this.kpiRelationship.isAvailable(id)) {
            return null;
        }
        final Graph graph = this.kpiRelationship.getAscendantsGraph(id, type, depth);
        return graph;
    }
    
    public Graph getAscendantsGraphByIdList(final List<Integer> idList, final Integer depth) {
        final Graph graph = (Graph)new TinkerGraph();
        if (idList != null) {
            for (final Integer id : idList) {
                if (id != null) {
                    if (!this.kpiRelationship.isAvailable(id)) {
                        continue;
                    }
                    final KpiGraph.Kpi node = (KpiGraph.Kpi)this.kpiRelationship.get(id, (Class)KpiGraph.Kpi.class);
                    this.kpiRelationship.getAscendantsGraph(node, graph, depth, KpiRelationshipParams.CASCADE.name());
                    this.kpiRelationship.getAscendantsGraph(node, graph, depth, KpiRelationshipParams.TRANSLATE.name());
                    this.kpiRelationship.getAscendantsGraph(node, graph, depth, KpiRelationshipParams.ORIGIN.name());
                    this.kpiRelationship.getAscendantsGraph(node, graph, depth, KpiRelationshipParams.GROUP.name());
                }
            }
        }
        return graph;
    }
    
    public Graph getAscendantsGraphByIdListAndType(final List<Integer> idList, final String type, final Integer depth) {
        final Graph graph = (Graph)new TinkerGraph();
        if (idList != null) {
            for (final Integer id : idList) {
                if (id != null) {
                    if (!this.kpiRelationship.isAvailable(id)) {
                        continue;
                    }
                    final KpiGraph.Kpi node = (KpiGraph.Kpi)this.kpiRelationship.get(id, (Class)KpiGraph.Kpi.class);
                    this.kpiRelationship.getAscendantsGraph(node, graph, depth, type);
                }
            }
        }
        return graph;
    }
    
    public List<KpiIdentityBean> getDescendants(final Integer id, final Integer depth) {
        final List<KpiIdentityBean> idObjList = new ArrayList<KpiIdentityBean>();
        if (!this.kpiRelationship.isAvailable(id)) {
            return idObjList;
        }
        final List<KpiGraph.Kpi> kpiList = this.kpiRelationship.getDescendants(id, depth);
        for (final KpiGraph.Kpi kpi : kpiList) {
            idObjList.add(this.getKpiIdentity(kpi));
        }
        return idObjList;
    }
    
    public List<KpiIdentityBean> getDescendantsByType(final Integer id, final String type, final Integer depth) {
        final List<KpiIdentityBean> idObjList = new ArrayList<KpiIdentityBean>();
        if (!this.kpiRelationship.isAvailable(id)) {
            return idObjList;
        }
        final List<KpiGraph.Kpi> kpiList = this.kpiRelationship.getDescendants(id, type, depth);
        for (final KpiGraph.Kpi kpi : kpiList) {
            idObjList.add(this.getKpiIdentity(kpi));
        }
        return idObjList;
    }
    
    public Graph getDescendantsGraph(final Integer id, final Integer depth) {
        if (!this.kpiRelationship.isAvailable(id)) {
            return null;
        }
        final Graph graph = this.kpiRelationship.getDescendantsGraph(id, depth);
        return graph;
    }
    
    public Graph getDescendantsGraphByType(final Integer id, final String type, final Integer depth) {
        if (!this.kpiRelationship.isAvailable(id)) {
            return null;
        }
        final Graph graph = this.kpiRelationship.getDescendantsGraph(id, type, depth);
        return graph;
    }
    
    public Graph getDescendantsGraphByIdList(final List<Integer> idList, final Integer depth) {
        final Graph graph = (Graph)new TinkerGraph();
        if (idList != null) {
            for (final Integer id : idList) {
                if (id != null) {
                    if (!this.kpiRelationship.isAvailable(id)) {
                        continue;
                    }
                    final KpiGraph.Kpi node = (KpiGraph.Kpi)this.kpiRelationship.get(id, (Class)KpiGraph.Kpi.class);
                    this.kpiRelationship.getDescendantsGraph(node, graph, depth, KpiRelationshipParams.CASCADE.name());
                    this.kpiRelationship.getDescendantsGraph(node, graph, depth, KpiRelationshipParams.TRANSLATE.name());
                    this.kpiRelationship.getDescendantsGraph(node, graph, depth, KpiRelationshipParams.ORIGIN.name());
                    this.kpiRelationship.getDescendantsGraph(node, graph, depth, KpiRelationshipParams.GROUP.name());
                }
            }
        }
        return graph;
    }
    
    public Graph getDescendantsGraphByIdListAndType(final List<Integer> idList, final String type, final Integer depth) {
        final Graph graph = (Graph)new TinkerGraph();
        if (idList != null) {
            for (final Integer id : idList) {
                if (id != null) {
                    if (!this.kpiRelationship.isAvailable(id)) {
                        continue;
                    }
                    final KpiGraph.Kpi node = (KpiGraph.Kpi)this.kpiRelationship.get(id, (Class)KpiGraph.Kpi.class);
                    this.kpiRelationship.getDescendantsGraph(node, graph, depth, type);
                }
            }
        }
        return graph;
    }
    
    public List<KpiIdentityBean> getChildrenByType(final Integer id, final String type) {
        final List<KpiIdentityBean> idObjList = new ArrayList<KpiIdentityBean>();
        if (!this.kpiRelationship.isAvailable(id)) {
            return idObjList;
        }
        final List<KpiGraph.Kpi> kpiList = this.kpiRelationship.getChildren(id, type);
        for (final KpiGraph.Kpi kpi : kpiList) {
            idObjList.add(this.getKpiIdentity(kpi));
        }
        return idObjList;
    }
    
    public List<KpiIdentityBean> getChildren(final Integer id) {
        final List<KpiIdentityBean> idObjList = new ArrayList<KpiIdentityBean>();
        if (!this.kpiRelationship.isAvailable(id)) {
            return idObjList;
        }
        final List<KpiGraph.Kpi> kpiList = this.kpiRelationship.getChildren(id);
        for (final KpiGraph.Kpi kpi : kpiList) {
            idObjList.add(this.getKpiIdentity(kpi));
        }
        return idObjList;
    }
    
    public List<PeriodMasterBean> searchPeriodMaster(final PeriodMasterBean periodMasterBean, SortList sortList) {
        final Request request = new Request();
        request.put(KpiParams.PERIOD_MASTER_DATA.name(), (BaseValueObject)periodMasterBean);
        request.setSortList(sortList);
        final Response response = this.dataService.searchPeriodMaster(request);
        sortList = response.getSortList();
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get(KpiParams.PERIOD_MASTER_DATA_LIST.name());
        final List<PeriodMasterBean> allPeriodMaster = (List<PeriodMasterBean>)objectList.getValueObjectList();
        return allPeriodMaster;
    }
    
    public BaseValueObjectList getKpisByIds(final List<Integer> kpiIdList) {
        final List<KpiData> kpiList = new ArrayList<KpiData>();
        final List<Integer> dbIdList = new ArrayList<Integer>();
        for (final Integer id : kpiIdList) {
            final Kpi kpi = (Kpi)this.getCache().getIfPresent(id);
            if (kpi != null && kpi.getKpiDetails() != null) {
                kpiList.add(kpi.getKpiDetails());
            }
            else {
                dbIdList.add(id);
            }
        }
        Response response = null;
        BaseValueObjectList bList = null;
        if (!dbIdList.isEmpty()) {
            final IdentifierList idList = new IdentifierList((List)dbIdList);
            final Request request = new Request();
            request.put(KpiParams.KPI_ID_LIST.name(), (BaseValueObject)idList);
            response = this.dataService.getKpisByIds(request);
            bList = (BaseValueObjectList)response.get(KpiParams.KPI_DATA_LIST.name());
        }
        if (bList != null && bList.getValueObjectList() != null) {
            for (final KpiData data : (List<KpiData>) bList.getValueObjectList()) {
                kpiList.add(data);
                final Kpi kpi2 = new Kpi(this);
                kpi2.setKpiDetails(data);
                this.getCache().put(data.getId(), kpi2);
            }
        }
        Collections.sort(kpiList);
        if (bList == null) {
            bList = new BaseValueObjectList();
        }
        bList.setValueObjectList((List)kpiList);
        return bList;
    }
    
    public Integer updateKPIState(final IdentifierList idList, final Identifier state) {
        final Kpi kpi = new Kpi(this);
        final Integer count = kpi.updateKPIState(idList, state);
        if (count != null && count > 0 && idList != null && idList.getIdsList() != null && !idList.getIdsList().isEmpty()) {
            for (final Integer id : idList.getIdsList()) {
                final Kpi kpid = (Kpi)this.getCache().getIfPresent(id);
                if (kpid != null && kpid.getKpiDetails() != null) {
                    kpid.getKpiDetails().setState(state.getId());
                    this.getCache().put(kpid.getKpiDetails().getId(), kpid);
                }
            }
        }
        return count;
    }
    
    public KpiDataService getDataService() {
        return this.dataService;
    }
    
    public void setDataService(final KpiDataService svc) {
        this.dataService = svc;
    }
    
    public Validator getValidator() {
        return this.validator;
    }
    
    public void setValidator(final Validator validator) {
        this.validator = validator;
    }
    
    public Cache<Integer, Kpi> getCache() {
        return this.cache;
    }
    
    public void setCache(final Cache<Integer, Kpi> cache) {
        this.cache = cache;
    }
    
    public BaseValueObjectList getKpiDataToCalcScore(final Identifier groupId, final Identifier periodMasterId, final DateResponse startDate) {
        final Request request = new Request();
        request.put(KpiParams.GROUP_ID.name(), (BaseValueObject)groupId);
        request.put(KpiParams.PERIOD_MASTER_ID.name(), (BaseValueObject)periodMasterId);
        request.put(KpiParams.START_DATE.name(), (BaseValueObject)startDate);
        final Response response = this.dataService.getKpiDataToCalcScore(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(KpiParams.SCORE_UTILITY_LIST.name());
        return list;
    }
    
    public List<KpiAggregationTypeBean> searchKpiAggregationType(final KpiAggregationTypeBean kpiAggregationType, SortList sortList) {
        final Request request = new Request();
        request.put(KpiParams.KPI_AGGR_TYPE_DATA.name(), (BaseValueObject)kpiAggregationType);
        request.setSortList(sortList);
        final Response response = this.dataService.searchKpiAggregationType(request);
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get(KpiParams.KPI_AGGR_TYPE_DATA_LIST.name());
        final List<KpiAggregationTypeBean> kpiAggregationTypeList = (List<KpiAggregationTypeBean>)objectList.getValueObjectList();
        sortList = response.getSortList();
        return kpiAggregationTypeList;
    }
    
    public List<KpiUomData> searchKpiUom(final KpiUomData kpiUom, SortList sortList) {
        final Request request = new Request();
        request.put(KpiParams.KPI_UOM_DATA.name(), (BaseValueObject)kpiUom);
        request.setSortList(sortList);
        final Response response = this.dataService.searchKpiUom(request);
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get(KpiParams.KPI_UOM_DATA_LIST.name());
        final List<KpiUomData> kpiUomList = (List<KpiUomData>)objectList.getValueObjectList();
        sortList = response.getSortList();
        return kpiUomList;
    }
    
    public List<KpiTypeData> searchKpiType(final KpiTypeData kpiTypeData, SortList sortList) {
        final Request request = new Request();
        request.put(KpiParams.KPI_TYPE_DATA.name(), (BaseValueObject)kpiTypeData);
        request.setSortList(sortList);
        final Response response = this.dataService.searchKpiType(request);
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get(KpiParams.KPI_TYPE_DATA_LIST.name());
        final List<KpiTypeData> kpiTypeList = (List<KpiTypeData>)objectList.getValueObjectList();
        sortList = response.getSortList();
        return kpiTypeList;
    }
    
    public BaseValueObjectList getAllKpiScoreByKpiIds(final List<Integer> kpiIdList) {
        final List<KpiScoreBean> kpiScoreList = new ArrayList<KpiScoreBean>();
        final List<Integer> dbIdList = new ArrayList<Integer>();
        for (final Integer id : kpiIdList) {
            final Kpi kpi = (Kpi)this.getCache().getIfPresent(id);
            if (kpi != null && kpi.getKpiDetails() != null && kpi.getKpiDetails().getKpiScore() != null) {
                for (final KpiScoreBean scoreBean : kpi.getKpiDetails().getKpiScore()) {
                    kpiScoreList.add(scoreBean);
                }
            }
            else {
                dbIdList.add(id);
            }
        }
        Response response = null;
        BaseValueObjectList bList = null;
        if (!dbIdList.isEmpty()) {
            final Request request = new Request();
            request.put(KpiParams.KPI_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)dbIdList));
            response = this.dataService.getAllKpiScoreByKpiIds(request);
            bList = (BaseValueObjectList)response.get(KpiParams.KPI_SCORE_DATA_LIST.name());
        }
        if (bList != null && bList.getValueObjectList() != null) {
            for (final KpiScoreBean scoreBean2 : (List<KpiScoreBean>)bList.getValueObjectList()) {
                kpiScoreList.add(scoreBean2);
            }
        }
        if (bList == null) {
            bList = new BaseValueObjectList();
        }
        bList.setValueObjectList((List)kpiScoreList);
        return bList;
    }
    
    public List<KpiData> getKpisByScorecardId(final Identifier scoreCardId, Page page, SortList sortList) {
        final Request request = new Request();
        request.put(KpiParams.SCORECARD_ID.name(), (BaseValueObject)scoreCardId);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.getKpisByScorecardId(request);
        page = response.getPage();
        sortList = response.getSortList();
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(KpiParams.KPI_DATA_LIST.name());
        final List<KpiData> scList = (List<KpiData>)bList.getValueObjectList();
        return scList;
    }
    
    public BaseValueObjectList getKpiDataToCalcScorecardScore(final Identifier scoreCardId, final Identifier periodMasterId, final DateResponse startDate) {
        final Request request = new Request();
        request.put(KpiParams.SCORECARD_ID.name(), (BaseValueObject)scoreCardId);
        request.put(KpiParams.PERIOD_MASTER_ID.name(), (BaseValueObject)periodMasterId);
        request.put(KpiParams.START_DATE.name(), (BaseValueObject)startDate);
        final Response response = this.dataService.getKpiDataToCalcScorecardScore(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(KpiParams.SCORE_UTILITY_LIST.name());
        return list;
    }
    
    public KpiScoreBean getCurrentPeriodScore(final List<KpiScoreBean> scoreList, final Date systemDate) {
        final List<Integer> periodList = new ArrayList<Integer>();
        final Map<Integer, KpiScoreBean> periodmap = new HashMap<Integer, KpiScoreBean>();
        for (final KpiScoreBean score : scoreList) {
            if (score.getPeriodMasterId() != null && score.getPeriodMasterId() > 0) {
                periodmap.put(score.getPeriodMasterId(), score);
                periodList.add(score.getPeriodMasterId());
            }
        }
        Collections.sort(periodList);
        Integer currentPeriod = null;
        if (periodList != null && !periodList.isEmpty()) {
            currentPeriod = periodList.get(0);
        }
        for (final Integer period : periodList) {
            final PeriodMasterBean bean = this.getPeriodMasterById(period);
            if (bean != null && bean.getEndDate() != null) {
                if (bean.getEndDate().compareTo(systemDate) > 0) {
                    break;
                }
                currentPeriod = bean.getId();
            }
        }
        KpiScoreBean scoreBean = null;
        if (currentPeriod != null) {
            scoreBean = periodmap.get(currentPeriod);
        }
        return scoreBean;
    }
    
    static {
        log = Logger.getLogger((Class)KpiManager.class);
    }
}
