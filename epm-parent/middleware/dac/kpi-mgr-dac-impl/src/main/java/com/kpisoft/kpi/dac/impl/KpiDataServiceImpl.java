package com.kpisoft.kpi.dac.impl;

import com.kpisoft.kpi.dac.*;
import org.springframework.stereotype.*;
import com.kpisoft.kpi.dac.dao.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.dac.hibernate.*;
import com.canopus.entity.*;
import org.apache.log4j.*;
import org.modelmapper.convention.*;
import com.canopus.mw.*;
import com.kpisoft.kpi.vo.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import com.canopus.dac.utils.*;
import org.modelmapper.*;
import java.lang.reflect.*;
import com.googlecode.genericdao.search.*;
import com.kpisoft.kpi.dac.impl.domain.*;
import java.util.*;
import org.perf4j.*;
import com.canopus.entity.vo.*;
import com.canopus.mw.dto.*;
import com.kpisoft.kpi.utility.*;
import com.kpisoft.kpi.vo.param.*;

@Component
public class KpiDataServiceImpl extends BaseDataAccessService implements KpiDataService
{
    @Autowired
    private KpiDao kpiDao;
    @Autowired
    private GenericHibernateDao genericDao;
    @Autowired
    private EntityTypeDataService entityService;
    private ModelMapper modelMapper;
    private ModelMapper searchModelMapper;
    private static final Logger log;
    
    protected KpiDataServiceImpl() {
        this.kpiDao = null;
        this.genericDao = null;
        this.entityService = null;
        this.modelMapper = null;
        this.searchModelMapper = null;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        (this.searchModelMapper = new ModelMapper()).addMappings((PropertyMap)new KpiSearchMap());
        this.searchModelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)KpiData.class, (Class)Kpi.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)KpiUomData.class, (Class)KpiUom.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)KpiTypeData.class, (Class)KpiType.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)KpiAggregationTypeBean.class, (Class)KpiAggregationType.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)KpiScoreBean.class, (Class)KpiScore.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)KpiData.class, (Class)KpiBase.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)PeriodTypeBean.class, (Class)PeriodType.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)KpiTagBean.class, (Class)KpiTag.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)KpiKpiGraphRelationshipBean.class, (Class)KpiKpiGraphRelationship.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)PeriodMasterBean.class, (Class)PeriodMaster.class);
        TransformationHelper.createTypeMap(this.searchModelMapper, (Class)KpiData.class, (Class)Kpi.class);
        TransformationHelper.createTypeMap(this.searchModelMapper, (Class)KpiScoreBean.class, (Class)KpiScore.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    @Transactional(readOnly = true)
    public Response getKpi(final Request request) {
        final Identifier identifier = (Identifier)request.get(KpiParams.KPI_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        Kpi kpi = null;
        try {
            kpi = (Kpi)this.kpiDao.find((Serializable)identifier.getId());
            if (kpi == null) {
                return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_DOES_NOT_EXIST_001.name(), "kpi id {0} does not exist.", new Object[] { identifier.getId() }));
            }
            final KpiData kpiData = (KpiData)this.modelMapper.map((Object)kpi, (Class)KpiData.class);
            return this.OK(KpiParams.KPI_DATA.name(), (BaseValueObject)kpiData);
        }
        catch (Exception ex) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getKpi() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_004.name(), "Failed to load kpi", (Throwable)ex));
        }
    }
    
    @Transactional
    public Response saveKpi(final Request request) {
        final KpiData kpiData = (KpiData)request.get(KpiParams.KPI_DATA.name());
        if (kpiData == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        Kpi kpi = null;
        try {
            kpi = new Kpi();
            final Response response = this.OK();
            this.modelMapper.map((Object)kpiData, (Object)kpi);
            if (kpi.getKpiScorecardRelationships() != null) {
                for (final KpiScorecardRelationship iterator : kpi.getKpiScorecardRelationships()) {
                    iterator.setTenantId(ExecutionContext.getTenantId());
                }
            }
            if (kpi.getFieldData() != null) {
                for (final KpiFieldData iterator2 : kpi.getFieldData()) {
                    iterator2.setTenantId(ExecutionContext.getTenantId());
                }
            }
            if (kpi.getAttachmentList() != null) {
                for (final KpiAttachment iterator3 : kpi.getAttachmentList()) {
                    iterator3.setTenantId(ExecutionContext.getTenantId());
                }
            }
            if (kpi.getKpiTagRelationship() != null) {
                for (final KpiTagRelationship iterator4 : kpi.getKpiTagRelationship()) {
                    iterator4.setTenantId(ExecutionContext.getTenantId());
                }
            }
            if (kpi.getKpiScore() != null) {
                for (final KpiScore iterator5 : kpi.getKpiScore()) {
                    iterator5.setTenantId(ExecutionContext.getTenantId());
                }
            }
            if (kpi.getReviewFrequency() != null) {
                kpi.getReviewFrequency().setTenantId(ExecutionContext.getTenantId());
            }
            if (kpi.getKpiTagRelationship() != null) {
                for (final KpiTagRelationship iterator4 : kpi.getKpiTagRelationship()) {
                    iterator4.setTenantId(ExecutionContext.getTenantId());
                }
            }
            if (kpi.getKpiKpiRelationship() != null) {
                for (final KpiKpiGraphRelationship iterator6 : kpi.getKpiKpiRelationship()) {
                    iterator6.setTenantId(ExecutionContext.getTenantId());
                }
            }
            if (kpi.getKpiTarget() != null) {
                kpi.getKpiTarget().setTenantId(ExecutionContext.getTenantId());
                if (kpi.getKpiTarget().getTargetData() != null) {
                    for (final TargetData iterator7 : kpi.getKpiTarget().getTargetData()) {
                        iterator7.setTenantId(ExecutionContext.getTenantId());
                    }
                }
                if (kpi.getKpiTarget().getScale() != null) {
                    kpi.getKpiTarget().getScale().setTenantId(ExecutionContext.getTenantId());
                    if (kpi.getKpiTarget().getScale().getAlkpiMasterScaleValue() != null) {
                        for (final ScaleValue iterator8 : kpi.getKpiTarget().getScale().getAlkpiMasterScaleValue()) {
                            iterator8.setTenantId(ExecutionContext.getTenantId());
                        }
                    }
                }
            }
            kpi.setTenantId(ExecutionContext.getTenantId());
            this.kpiDao.merge(kpi);
            if (kpi.getKpiKpiRelationship() != null) {
                for (final KpiKpiGraphRelationship iterator6 : kpi.getKpiKpiRelationship()) {
                    if (iterator6.getChild() == null || iterator6.getChild().getId() == null || iterator6.getChild().getId() <= 0) {
                        final KpiIdentity obj = new KpiIdentity();
                        obj.setId(kpi.getId());
                        obj.setCode(kpi.getCode());
                        obj.setWeightage(kpi.getWeightage());
                        iterator6.setChild(obj);
                    }
                }
            }
            response.put(KpiParams.KPI_ID.name(), (BaseValueObject)new Identifier(kpi.getId()));
            return response;
        }
        catch (Exception ex) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - saveKpi() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_CREATE_003.name(), "Failed to save kpi", (Throwable)ex));
        }
    }
    
    @Transactional
    public Response saveScoreDetailsInKpiForMultiple(final Request request) {
        final BaseValueObjectList scoreList = (BaseValueObjectList)request.get(KpiParams.KPI_SCORE_DATA_LIST.name());
        if (scoreList == null || scoreList.getValueObjectList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<KpiScoreBean> utilList = (List<KpiScoreBean>)scoreList.getValueObjectList();
            final int count = this.kpiDao.saveScoreDetailsInKpiForMultiple(utilList);
            return this.OK(KpiParams.COUNT.name(), (BaseValueObject)new Identifier(count));
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - saveScoreDetailsInKpiForMultiple : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCORE_FAILED_TO_CALC_SCORE_011.name(), "Failed to save list of kpi scores ", (Throwable)e));
        }
    }
    
    @Transactional
    public Response updateKpiDetailsForMultiple(final Request request) {
        final BaseValueObjectList kpiUtility = (BaseValueObjectList)request.get(KpiParams.KPI_UTILITY_LIST.name());
        if (kpiUtility == null || kpiUtility.getValueObjectList() == null || kpiUtility.getValueObjectList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<KpiUtility> utilList = (List<KpiUtility>)kpiUtility.getValueObjectList();
            final int count = this.kpiDao.updateKpiDetailsForMultiple(utilList);
            return this.OK(KpiParams.COUNT.name(), (BaseValueObject)new Identifier(count));
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - updateKpiDetailsForMultiple : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_UPDATE_005.name(), "Failed to update list of kpi details ", (Throwable)e));
        }
    }
    
    @Transactional
    public Response deleteKpi(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.KPI_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final boolean status = this.kpiDao.removeById((Serializable)id.getId());
            return this.OK(KpiParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - deleteKpi() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_DELETE_006.name(), "Failed to delete kpi", (Throwable)ex));
        }
    }
    
    @Transactional
    public Response deleteKpis(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(KpiParams.KPI_ID_LIST.name());
        final IdentifierList idRelList = (IdentifierList)request.get(KpiParams.KPI_REL_ID_LIST.name());
        boolean status = false;
        if (idList == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            if (idList.getIdsList() != null && idList.getIdsList().size() > 0) {
                for (final Integer id : idList.getIdsList()) {
                    status = this.kpiDao.removeById((Serializable)id);
                    if (!status) {
                        return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_DELETE_006.name(), "Failed to delete Kpi"));
                    }
                }
            }
            if (idRelList != null && idRelList.getIdsList() != null && idRelList.getIdsList().size() > 0) {
                for (final Integer id : idRelList.getIdsList()) {
                    if (this.genericDao.find((Class)KpiKpiGraphRelationship.class, (Serializable)id) != null) {
                        status = this.genericDao.removeById((Class)KpiKpiGraphRelationship.class, (Serializable)id);
                        if (!status) {
                            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_DELETE_KPI_REL_044.name(), "Failed to delete kpi relationship"));
                        }
                        continue;
                    }
                }
            }
            return this.OK(KpiParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            KpiDataServiceImpl.log.error((Object)"Exception KpiDataServiceImpl - deleteKpis() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_DELETE_KPIS_045.name(), "Failed to delete list of kpis data", (Throwable)ex));
        }
    }
    
    @Transactional
    public Response deactivateKpis(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(KpiParams.KPI_ID_LIST.name());
        final IdentifierList idRelList = (IdentifierList)request.get(KpiParams.KPI_REL_ID_LIST.name());
        boolean status = false;
        Integer count = 0;
        if (idList == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            if (idList.getIdsList() != null && idList.getIdsList().size() > 0) {
                count = this.kpiDao.updateKpiStatus(idList.getIdsList(), 0);
                if (count != idList.getIdsList().size()) {
                    return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_CAT_UNABLE_TO_DEACTIVATE_006.name(), "Failed to deactivate kpi's"));
                }
            }
            if (idRelList != null && idRelList.getIdsList() != null && idRelList.getIdsList().size() > 0) {
                for (final Integer id : idRelList.getIdsList()) {
                    if (this.genericDao.find((Class)KpiKpiGraphRelationship.class, (Serializable)id) != null) {
                        status = this.genericDao.removeById((Class)KpiKpiGraphRelationship.class, (Serializable)id);
                        if (!status) {
                            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_DELETE_KPI_REL_044.name(), "Failed to delete kpi relationship"));
                        }
                        continue;
                    }
                }
            }
            else {
                status = true;
            }
            final Response response = this.OK(KpiParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
            response.put(KpiParams.COUNT.name(), (BaseValueObject)new Identifier(count));
            return response;
        }
        catch (Exception ex) {
            KpiDataServiceImpl.log.error((Object)"Exception KpiDataServiceImpl - deactivateKpis() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_DELETE_KPIS_045.name(), "Failed to deactivate list of kpis data", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getKpiCountByScoreCard(final Request request) {
        final Identifier scorecardId = (Identifier)request.get(KpiParams.SCORECARD_ID.name());
        final StringIdentifier kpiTypeName = (StringIdentifier)request.get(KpiParams.KPI_TYPE_NAME.name());
        if (scorecardId == null || kpiTypeName == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        Search search = new Search((Class)KpiScorecardRelationship.class);
        search.addFilterEqual("empScorecardId", (Object)scorecardId.getId());
        final List<KpiScorecardRelationship> kpiIds = (List<KpiScorecardRelationship>)this.genericDao.search((ISearch)search);
        int coreCount = 0;
        int individualCount = 0;
        int groupCoreCount = 0;
        int groupIndividualCount = 0;
        try {
            for (final KpiScorecardRelationship iterator : kpiIds) {
                final Request req = new Request();
                req.put(KpiParams.KPI_ID.name(), (BaseValueObject)new Identifier(iterator.getKpiId()));
                final KpiData kpiData = (KpiData)this.getKpi(req).get(KpiParams.KPI_DATA.name());
                if (kpiData.getCoreKpi() != null && kpiData.getCoreKpi()) {
                    ++coreCount;
                }
                else if (kpiData.getIndividualKpi() != null && kpiData.getIndividualKpi()) {
                    ++individualCount;
                }
                else {
                    if (kpiData.getGroupKpi() == null || !kpiData.getGroupKpi()) {
                        continue;
                    }
                    search = new Search((Class)Kpi.class);
                    search.addFilterEqual("groupId", (Object)kpiData.getId());
                    final List<Kpi> kpis = (List<Kpi>)this.genericDao.search((ISearch)search);
                    for (final Kpi kpi : kpis) {
                        if (kpi.getCoreKpi()) {
                            ++groupCoreCount;
                        }
                        else {
                            if (!kpi.getIndividualKpi()) {
                                continue;
                            }
                            ++groupIndividualCount;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getKpiCountByScoreCard() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_GET_COUNT_BY_SCORECARD_010.name(), "Failed to get count by scorecard", (Throwable)e));
        }
        int total = 0;
        if (KpiParams.NONE.name().equals(kpiTypeName.getId())) {
            total = coreCount + individualCount + groupCoreCount + groupIndividualCount;
        }
        else if (KpiParams.CORE.name().equals(kpiTypeName.getId())) {
            total = coreCount + groupCoreCount;
        }
        else if (KpiParams.INDIVIDUAL.name().equals(kpiTypeName.getId())) {
            total = individualCount + groupIndividualCount;
        }
        else if (KpiParams.GROUP.name().equals(kpiTypeName.getId())) {
            total = groupCoreCount + groupIndividualCount;
        }
        return this.OK(KpiParams.KPI_COUNT.name(), (BaseValueObject)new Identifier(total));
    }
    
    @Transactional(readOnly = true)
    public Response getKpisForEmployee(final Request request) {
        final Identifier ownerId = (Identifier)request.get(KpiParams.KPI_OWNER_ID.name());
        if (ownerId == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "startDate";
        try {
            final Search search = new Search((Class)Kpi.class);
            search.addFilterEqual("kpiOwnerId", (Object)ownerId.getId());
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.genericDao.count((ISearch)search), defaultField);
            final List<Kpi> result = (List<Kpi>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<KpiData>>() {}.getType();
            final List<KpiData> kpiList = (List<KpiData>)this.searchModelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiList);
            final Response response = this.OK(KpiParams.KPI_DATA_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getKpisForEmployee() ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_BY_EMP_011.name(), "Unable to get the Kpis for the given employee", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getKpisByStrategyNode(final Request request) {
        final Identifier nodeId = (Identifier)request.get(KpiParams.STRATEGY_NODE_ID.name());
        if (nodeId == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "startDate";
        try {
            final Search search = new Search((Class)Kpi.class);
            search.addFilterEqual("strategyTree", (Object)nodeId.getId());
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.genericDao.count((ISearch)search), defaultField);
            final List<Kpi> result = (List<Kpi>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<KpiData>>() {}.getType();
            final List<KpiData> kpiList = (List<KpiData>)this.searchModelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiList);
            final Response response = this.OK(KpiParams.KPI_DATA_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getKpisByStrategyNode() ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_BY_STR_NODE_012.name(), "Unable to get the Kpis with strategy node", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response validateKpiWithRootcode(final Request request) {
        final Identifier ownerId = (Identifier)request.get(KpiParams.KPI_OWNER_ID.name());
        final StringIdentifier kpiRootCode = (StringIdentifier)request.get(KpiParams.KPI_ROOT_CODE.name());
        if (ownerId == null || kpiRootCode == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        boolean valid = true;
        try {
            final Search search = new Search((Class)Kpi.class);
            search.addFilterEqual("kpiOwnerId", (Object)ownerId.getId());
            search.addFilterEqual("rootCode", (Object)kpiRootCode.getId());
            final List<Kpi> kpis = (List<Kpi>)this.kpiDao.search((ISearch)search);
            if (!kpis.isEmpty()) {
                valid = false;
            }
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - validateKpiWithRootcode() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_VALIDATE_ROOT_CODE_013.name(), "Failed to validate kpi root code", (Throwable)e));
        }
        return this.OK(KpiParams.BOOL_VALID_KPI.name(), (BaseValueObject)new BooleanResponse(valid));
    }
    
    @Transactional(readOnly = true)
    public Response getKpiUom(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.KPI_UOM_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UOM_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        KpiUom kpiUom = null;
        try {
            kpiUom = (KpiUom)this.genericDao.find((Class)KpiUom.class, (Serializable)id.getId());
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UOM_UNABLE_TO_GET_004.name(), "Failed to load kpiUom", (Throwable)ex));
        }
        if (kpiUom == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UOM_DOES_NOT_EXIST_001.name(), "kpiUom id {0} does not exist.", new Object[] { id.getId() }));
        }
        final KpiUomData kpiUomData = (KpiUomData)this.modelMapper.map((Object)kpiUom, (Class)KpiUomData.class);
        return this.OK(KpiParams.KPI_UOM_DATA.name(), (BaseValueObject)kpiUomData);
    }
    
    @Transactional
    public Response saveKpiUom(final Request request) {
        final KpiUomData kpiUomData = (KpiUomData)request.get(KpiParams.KPI_UOM_DATA.name());
        if (kpiUomData == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UOM_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        final KpiUom kpiUom = new KpiUom();
        try {
            this.modelMapper.map((Object)kpiUomData, (Object)kpiUom);
            this.genericDao.save((Object)kpiUom);
            return this.OK(KpiParams.KPI_UOM_ID.name(), (BaseValueObject)new Identifier(kpiUom.getId()));
        }
        catch (Exception ex) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - saveKpiUom() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UOM_UNABLE_TO_CREATE_003.name(), "Failed to create kpiUom", (Throwable)ex));
        }
    }
    
    @Transactional
    public Response deleteKpiUom(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.KPI_UOM_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UOM_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final boolean status = this.genericDao.removeById((Class)KpiUom.class, (Serializable)id.getId());
            return this.OK(KpiParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - deleteKpiUom() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UOM_UNABLE_TO_DELETE_006.name(), "Failed to delete kpiUom", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllKpiUom(final Request request) {
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        try {
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.genericDao.getFilterFromExample((Object)new KpiUom(), options);
            final Search search = new Search((Class)KpiUom.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<KpiUom> data = (List<KpiUom>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<KpiUomData>>() {}.getType();
            final List<KpiUomData> kpiUomDataList = (List<KpiUomData>)this.modelMapper.map((Object)data, listType);
            final BaseValueObjectList sortedKpiUomlistHolder = new BaseValueObjectList();
            sortedKpiUomlistHolder.setValueObjectList((List)kpiUomDataList);
            final Response response = this.OK(KpiParams.KPI_UOM_DATA_LIST.name(), (BaseValueObject)sortedKpiUomlistHolder);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getAllKpiUom() : ", (Throwable)e);
            e.printStackTrace();
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UOM_UNABLE_TO_GET_ALL_007.name(), "Failed to get all kpiUoms", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getKpiType(final Request request) {
        final Identifier identifier = (Identifier)request.get(KpiParams.KPI_TYPE_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TYPE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        KpiType kpiType = null;
        try {
            kpiType = (KpiType)this.genericDao.find((Class)KpiType.class, (Serializable)identifier.getId());
        }
        catch (Exception ex) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getKpiType() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TYPE_UNABLE_TO_GET_004.name(), "Failed to load kpi type", (Throwable)ex));
        }
        if (kpiType == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TYPE_DOES_NOT_EXIST_001.name(), "kpiType id {0} does not exist.", new Object[] { identifier.getId() }));
        }
        final KpiTypeData kpiTypeData = (KpiTypeData)this.modelMapper.map((Object)kpiType, (Class)KpiTypeData.class);
        return this.OK(KpiParams.KPI_TYPE_DATA.name(), (BaseValueObject)kpiTypeData);
    }
    
    @Transactional
    public Response saveKpiType(final Request request) {
        final KpiTypeData kpiTypeData = (KpiTypeData)request.get(KpiParams.KPI_TYPE_DATA.name());
        if (kpiTypeData == null) {
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TYPE_INVALID_INPUT_002.name(), "No data object in the request");
        }
        final KpiType kpiType = new KpiType();
        try {
            this.modelMapper.map((Object)kpiTypeData, (Object)kpiType);
            this.genericDao.save((Object)kpiType);
            return this.OK(KpiParams.KPI_TYPE_ID.name(), (BaseValueObject)new Identifier(kpiType.getId()));
        }
        catch (Exception ex) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - saveKpiType() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TYPE_UNABLE_TO_CREATE_003.name(), "Failed to save kpi type", (Throwable)ex));
        }
    }
    
    @Transactional
    public Response deleteKpiType(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.KPI_TYPE_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TYPE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final boolean status = this.genericDao.removeById((Class)KpiType.class, (Serializable)id.getId());
            return this.OK(KpiParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - deleteKpiType() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TYPE_UNABLE_TO_DELETE_006.name(), "Failed to delete kpi type", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllKpiType(final Request request) {
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        try {
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.genericDao.getFilterFromExample((Object)new KpiType(), options);
            final Search search = new Search((Class)KpiType.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<KpiType> data = (List<KpiType>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<KpiTypeData>>() {}.getType();
            final List<KpiTypeData> kpiTypeData = (List<KpiTypeData>)this.modelMapper.map((Object)data, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiTypeData);
            final Response response = this.OK(KpiParams.KPI_TYPE_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getAllKpiType() ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TYPE_UNABLE_TO_GET_ALL_007.name(), "Unable to load all the kpi types", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getKpiAggregationTypeById(final Request request) {
        final Identifier aggregationId = (Identifier)request.get(KpiParams.KPI_AGGR_TYPE_ID.name());
        if (aggregationId == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_AGGR_TYPE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        KpiAggregationType aggregationType = null;
        try {
            aggregationType = (KpiAggregationType)this.genericDao.find((Class)KpiAggregationType.class, (Serializable)aggregationId.getId());
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getKpiAggregationTypeById() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_AGGR_TYPE_UNABLE_TO_GET_004.name(), "Failed to load kpi aggregation type bean", (Throwable)e));
        }
        if (aggregationType == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_AGGR_TYPE_DOES_NOT_EXIST_001.name(), "kpi aggregation type id {0} does not exist.", new Object[] { aggregationId.getId() }));
        }
        final KpiAggregationTypeBean aggregationTypeBean = (KpiAggregationTypeBean)this.modelMapper.map((Object)aggregationType, (Class)KpiAggregationTypeBean.class);
        return this.OK(KpiParams.KPI_AGGR_TYPE_DATA.name(), (BaseValueObject)aggregationTypeBean);
    }
    
    @Transactional(readOnly = true)
    public Response getAllKpiAggregationTypes(final Request request) {
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        try {
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.genericDao.getFilterFromExample((Object)new KpiAggregationType(), options);
            final Search search = new Search((Class)KpiAggregationType.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<KpiAggregationType> data = (List<KpiAggregationType>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<KpiAggregationTypeBean>>() {}.getType();
            final List<KpiAggregationTypeBean> kpiAggregationTypeBeans = (List<KpiAggregationTypeBean>)this.modelMapper.map((Object)data, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiAggregationTypeBeans);
            final Response response = this.OK(KpiParams.KPI_AGGR_TYPE_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getKpisForEmployee() ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_AGGR_TYPE_UNABLE_TO_GET_ALL_007.name(), "unable to load all the kpi aggregation types", (Throwable)e));
        }
    }
    
    @Transactional
    public Response saveKpiScore(final Request request) {
        final KpiScoreBean scoreBean = (KpiScoreBean)request.get(KpiParams.KPI_SCORE_DATA.name());
        if (scoreBean == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCORE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        final KpiScore kpiScore = new KpiScore();
        try {
            this.modelMapper.map((Object)scoreBean, (Object)kpiScore);
            this.genericDao.save((Object)kpiScore);
            kpiScore.setTenantId(ExecutionContext.getTenantId());
            return this.OK(KpiParams.KPI_SCORE_DATA_ID.name(), (BaseValueObject)new Identifier(kpiScore.getId()));
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - saveKpiScore : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCORE_UNABLE_TO_CREATE_003.name(), "Failed to save kpi score", (Throwable)e));
        }
    }
    
    @Transactional
    public Response saveKpiScoreForMultiple(final Request request) {
        final BaseValueObjectList scoreList = (BaseValueObjectList)request.get(KpiParams.KPI_SCORE_DATA_LIST.name());
        if (scoreList == null || scoreList.getValueObjectList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCORE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<KpiScoreBean> scoreBeanList = (List<KpiScoreBean>)scoreList.getValueObjectList();
            final Type listType = new TypeToken<List<KpiScore>>() {}.getType();
            final List<KpiScore> beans = (List<KpiScore>)this.modelMapper.map((Object)scoreBeanList, listType);
            final boolean[] id = this.genericDao.save(beans.toArray());
            for (final KpiScore score : beans) {
                score.setTenantId(ExecutionContext.getTenantId());
            }
            return this.OK(KpiParams.COUNT.name(), (BaseValueObject)new Identifier(id.length));
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - saveKpiScore : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCORE_UNABLE_TO_CREATE_003.name(), "Failed to save list of kpi scores ", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getKpiScoreById(final Request request) {
        final Identifier kpiScoreId = (Identifier)request.get(KpiParams.KPI_SCORE_DATA_ID.name());
        if (kpiScoreId == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCORE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        KpiScore score = null;
        try {
            score = (KpiScore)this.genericDao.find((Class)KpiScore.class, (Serializable)kpiScoreId.getId());
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getKpiScoreById : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCORE_UNABLE_TO_GET_004.name(), "Failed to load kpi score", (Throwable)e));
        }
        if (score == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCORE_DOES_NOT_EXIST_001.name(), "kpi score with id {0} does not exist.", new Object[] { kpiScoreId.getId() }));
        }
        final KpiScoreBean scoreBean = (KpiScoreBean)this.modelMapper.map((Object)score, (Class)KpiScoreBean.class);
        return this.OK(KpiParams.KPI_SCORE_DATA.name(), (BaseValueObject)scoreBean);
    }
    
    @Transactional(readOnly = true)
    public Response getAllPeriodTypes(final Request request) {
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        try {
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.genericDao.getFilterFromExample((Object)new PeriodType(), options);
            final Search search = new Search((Class)PeriodType.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<PeriodType> data = (List<PeriodType>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<PeriodTypeBean>>() {}.getType();
            final List<PeriodTypeBean> periodTypeBeans = (List<PeriodTypeBean>)this.modelMapper.map((Object)data, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)periodTypeBeans);
            final Response response = this.OK(KpiParams.PERIOD_TYPE_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getAllPeriodTypes() ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_PRD_TYPE_UNABLE_TO_GET_ALL_007.name(), "Failed to load all period types", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllPeriodMsaters(final Request request) {
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        try {
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.genericDao.getFilterFromExample((Object)new PeriodMaster(), options);
            final Search search = new Search((Class)PeriodMaster.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<PeriodType> data = (List<PeriodType>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<PeriodMasterBean>>() {}.getType();
            final List<PeriodMasterBean> periodTypeBeans = (List<PeriodMasterBean>)this.modelMapper.map((Object)data, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)periodTypeBeans);
            final Response response = this.OK(KpiParams.PERIOD_MASTER_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getAllPeriodMsaters() ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_PRD_MASTER_UNABLE_TO_GET_ALL_007.name(), "Failed to load all period masters", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getPositionKpiEmployeeRelations(final Request request) {
        final Identifier objIdentifier = (Identifier)request.get(KpiParams.POSITION_ID.name());
        if (objIdentifier == null) {
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request");
        }
        BaseValueObjectList list = null;
        try {
            final Search objSearch = new Search((Class)KpiPositionRelationship.class);
            objSearch.addFilterEqual("positionId", (Object)objIdentifier.getId());
            final List<KpiPositionRelationship> alKpiPositionRelation = (List<KpiPositionRelationship>)this.genericDao.search((ISearch)objSearch);
            final List<Integer> integers = new ArrayList<Integer>();
            for (final KpiPositionRelationship objKpiPositionRelation : alKpiPositionRelation) {
                integers.add(objKpiPositionRelation.getKpiId());
            }
            final Search objSearch2 = new Search((Class)Kpi.class);
            objSearch2.addFilterIn("id", (Collection)integers);
            final List<Kpi> alKpi = (List<Kpi>)this.genericDao.search((ISearch)objSearch2);
            final Type listType = new TypeToken<List<KpiData>>() {}.getType();
            final List<? extends BaseValueObject> kpiBVList = (List<? extends BaseValueObject>)this.searchModelMapper.map((Object)alKpi, listType);
            list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiBVList);
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getPositionKpiEmployeeRelations() ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_POS_KPI_EMP_REL_020.name(), "Failed to get kpi list", (Throwable)e));
        }
        return this.OK(KpiParams.KPI_DATA_LIST.name(), (BaseValueObject)list);
    }
    
    @Transactional(readOnly = true)
    public Response searchKpi(final Request request) {
        final KpiData kpiData = (KpiData)request.get(KpiParams.KPI_DATA.name());
        BaseValueObjectList list = null;
        if (kpiData == null) {
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request");
        }
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "code";
        try {
            final Search search = new Search((Class)KpiBase.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final KpiBase kpi = (KpiBase)this.modelMapper.map((Object)kpiData, (Class)KpiBase.class);
            final Filter filter = this.genericDao.getFilterFromExample((Object)kpi, options);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.genericDao.count((ISearch)search), defaultField);
            final List<KpiBase> kpiList = (List<KpiBase>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<KpiData>>() {}.getType();
            final List<? extends BaseValueObject> kpiBVList = (List<? extends BaseValueObject>)this.modelMapper.map((Object)kpiList, listType);
            list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiBVList);
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - searchKpi() ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_SEARCH_008.name(), "Failed to get kpi list", (Throwable)e));
        }
        final Response response = this.OK(KpiParams.KPI_DATA_LIST.name(), (BaseValueObject)list);
        response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
        response.setSortList(sortList);
        return response;
    }
    
    @Transactional(readOnly = true)
    public Response searchKpiScore(final Request request) {
        final KpiScoreBean kpiScoreBean = (KpiScoreBean)request.get(KpiParams.KPI_SCORE_DATA.name());
        BaseValueObjectList list = null;
        if (kpiScoreBean == null) {
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCORE_INVALID_INPUT_002.name(), "No data object in the request");
        }
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "id";
        try {
            final Search search = new Search((Class)KpiScore.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final KpiScore kpiScore = (KpiScore)this.modelMapper.map((Object)kpiScoreBean, (Class)KpiScore.class);
            final Filter filter = this.genericDao.getFilterFromExample((Object)kpiScore, options);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.genericDao.count((ISearch)search), defaultField);
            final List<KpiScore> kpiScoreList = (List<KpiScore>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<KpiScoreBean>>() {}.getType();
            final List<? extends BaseValueObject> kpiBVList = (List<? extends BaseValueObject>)this.modelMapper.map((Object)kpiScoreList, listType);
            list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiBVList);
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - searchKpiScore() ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCORE_UNABLE_TO_SEARCH_008.name(), "Failed to get kpi list", (Throwable)e));
        }
        final Response response = this.OK(KpiParams.KPI_SCORE_DATA_LIST.name(), (BaseValueObject)list);
        response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
        response.setSortList(sortList);
        return response;
    }
    
    @Transactional(readOnly = true)
    public Response getPeriodMasterByPeriodType(final Request request) {
        final Identifier ownerId = (Identifier)request.get(KpiParams.PERIOD_TYPE_ID.name());
        if (ownerId == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_PRD_MASTER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        try {
            final Search search = new Search((Class)PeriodMaster.class);
            search.addFilterEqual("periodtype.id", (Object)ownerId.getId());
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<PeriodMaster> result = (List<PeriodMaster>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<KpiData>>() {}.getType();
            final List<PeriodMasterBean> periodMasterBeans = (List<PeriodMasterBean>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)periodMasterBeans);
            final Response response = this.OK(KpiParams.PERIOD_MASTER_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getPeriodMasterByPeriodType() ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_PRD_MASTER_UNABLE_TO_GET_004.name(), "failed to load PeriodMaster By PeriodType", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchPeriodType(final Request request) {
        final PeriodTypeBean periodType = (PeriodTypeBean)request.get(KpiParams.PERIOD_TYPE_DATA.name());
        final SortList sortList = request.getSortList();
        if (periodType == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_PRD_TYPE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Search search = new Search((Class)PeriodType.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final PeriodType periodTypeEntity = (PeriodType)this.modelMapper.map((Object)periodType, (Class)PeriodType.class);
            final Filter filter = this.genericDao.getFilterFromExample((Object)periodTypeEntity, options);
            search.addFilter(filter);
            final List<PeriodType> periodTypeList = (List<PeriodType>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<PeriodTypeBean>>() {}.getType();
            final List<? extends BaseValueObject> kpiTypeBVList = (List<? extends BaseValueObject>)this.modelMapper.map((Object)periodTypeList, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiTypeBVList);
            final Response response = this.OK(KpiParams.PERIOD_TYPE_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - searchPeriodType() ", (Throwable)e);
            e.printStackTrace();
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_PRD_TYPE_UNABLE_TO_SEARCH_008.name(), "Failed to search period type", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchKpiTag(final Request request) {
        final KpiTagBean kpiTag = (KpiTagBean)request.get(KpiParams.KPI_TAG_DATA.name());
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        BaseValueObjectList list = null;
        if (kpiTag == null) {
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TAG_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final Search search = new Search((Class)KpiTag.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final KpiTag kpiTag2 = (KpiTag)this.modelMapper.map((Object)kpiTag, (Class)KpiTag.class);
            final Filter filter = this.genericDao.getFilterFromExample((Object)kpiTag2, options);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<KpiTag> kpiTagList = (List<KpiTag>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<KpiTagBean>>() {}.getType();
            final List<? extends BaseValueObject> kpiTagBVList = (List<? extends BaseValueObject>)this.modelMapper.map((Object)kpiTagList, listType);
            list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiTagBVList);
            final Response response = this.OK(KpiParams.KPI_TAG_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - searchKpiTag() ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TAG_UNABLE_TO_SEARCH_008.name(), "Failed to search kpi tag", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getKpisByScorecardIdAndKpiTagId(final Request request) {
        final Identifier scorecardId = (Identifier)request.get(KpiParams.SCORECARD_ID.name());
        final Identifier kpiTagId = (Identifier)request.get(KpiParams.KPI_TAG_ID.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (scorecardId == null || kpiTagId == null || scorecardId.getId() <= 0 || kpiTagId.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in request"));
        }
        try {
            final Search search = new Search((Class)Kpi.class);
            search.addFilterEqual("kpiScorecardRelationships.empScorecardId", (Object)scorecardId.getId());
            search.addFilterEqual("kpiTagRelationship.kpiTagId", (Object)kpiTagId.getId());
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.genericDao.count((ISearch)search), "code");
            final List<Kpi> result = (List<Kpi>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<KpiData>>() {}.getType();
            final List<KpiData> kpiList = (List<KpiData>)this.searchModelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiList);
            final Response response = this.OK(KpiParams.KPI_DATA_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getKpisByScorecardIdAndKpiTagId() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_BY_SID_TID_014.name(), "Failed to load kpis by scorecardId and kpiTagId", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllKpiRelationships(final Request request) {
        final KpiKpiGraphRelationshipBean graphBean = (KpiKpiGraphRelationshipBean)request.get(KpiParams.KPI_REL_DATA.name());
        if (graphBean == null) {
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final List<KpiKpiGraphRelationshipBean> result = this.kpiDao.getAllKpiRelationships();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(KpiParams.KPI_KPI_REL_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getAllKpiRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_ALL_KPI_REL_043.name(), "Failed to load all kpi-kpi relationships", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getKpiKpiRelationships(final Request request) {
        final KpiKpiGraphRelationshipBean graphBean = (KpiKpiGraphRelationshipBean)request.get(KpiParams.KPI_REL_DATA.name());
        if (graphBean == null) {
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final Search search = new Search((Class)KpiKpiGraphRelationship.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            search.addFetch("parent");
            search.addFetch("child");
            final KpiKpiGraphRelationship kpiRel = (KpiKpiGraphRelationship)this.modelMapper.map((Object)graphBean, (Class)KpiKpiGraphRelationship.class);
            final Filter filter = this.genericDao.getFilterFromExample((Object)kpiRel, options);
            search.addFilter(filter);
            final StopWatch sw = new StopWatch("rel-query");
            sw.start();
            final List<KpiKpiGraphRelationship> data = (List<KpiKpiGraphRelationship>)this.genericDao.search((ISearch)search);
            sw.stop();
            KpiDataServiceImpl.log.info((Object)("Relationships search time " + sw));
            final List<KpiKpiGraphRelationshipBean> result = new ArrayList<KpiKpiGraphRelationshipBean>();
            for (final KpiKpiGraphRelationship iterator : data) {
                final KpiKpiGraphRelationshipBean kpiRelObj = (KpiKpiGraphRelationshipBean)this.modelMapper.map((Object)iterator, (Class)KpiKpiGraphRelationshipBean.class);
                result.add(kpiRelObj);
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(KpiParams.KPI_KPI_REL_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getKpiKpiRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_KPI_KPI_REL_042.name(), "Failed to load kpi-kpi relationships", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getKpiRelationshipTypeNameById(Request request) {
        final Identifier typeId = (Identifier)request.get(KpiParams.KPI_REL_TYPE_ID.name());
        StringIdentifier typeName = null;
        if (typeId == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        request = new Request();
        try {
            final EntityRelationshipBean eRel = new EntityRelationshipBean();
            eRel.setId(typeId.getId());
            request.put(EntityTypeParams.ENTITY_REL_DATA.name(), (BaseValueObject)eRel);
            final Response response = this.entityService.searchOnEntityRelationship(request);
            final BaseValueObjectList list = (BaseValueObjectList)response.get(EntityTypeParams.ENTITY_REL_LIST.name());
            if (list != null) {
                final List<EntityRelationshipBean> eRelList = (List<EntityRelationshipBean>)list.getValueObjectList();
                final EntityRelationshipBean data = eRelList.get(0);
                typeName = new StringIdentifier();
                typeName.setId(data.getName());
            }
            return this.OK(KpiParams.KPI_REL_TYPE_NAME.name(), (BaseValueObject)typeName);
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getKpiRelationshipTypeNameById() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_REL_TYPE_019.name(), "Failed to Load KpiRelationshipTypeName By Id", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchPeriodMaster(final Request request) {
        final PeriodMasterBean periodMasterBean = (PeriodMasterBean)request.get(KpiParams.PERIOD_MASTER_DATA.name());
        BaseValueObjectList list = null;
        if (periodMasterBean == null) {
            throw new DataAccessException(KpiErrorCodesEnum.ERR_PRD_MASTER_INVALID_INPUT_002.name(), "No data object in the request");
        }
        final SortList sortList = request.getSortList();
        final String defaultField = "id";
        try {
            final Search search = new Search((Class)PeriodMaster.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final PeriodMaster periodMstr = (PeriodMaster)this.modelMapper.map((Object)periodMasterBean, (Class)PeriodMaster.class);
            final Filter filter = this.genericDao.getFilterFromExample((Object)periodMstr, options);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<PeriodMaster> periodmstrList = (List<PeriodMaster>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<PeriodMasterBean>>() {}.getType();
            final List<? extends BaseValueObject> periodMstrBVList = (List<? extends BaseValueObject>)this.modelMapper.map((Object)periodmstrList, listType);
            list = new BaseValueObjectList();
            list.setValueObjectList((List)periodMstrBVList);
            final Response response = this.OK(KpiParams.PERIOD_MASTER_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - searchPeriodMaster() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_PRD_MASTER_UNABLE_TO_SEARCH_008.name(), "Failed to get period master ", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getKpisByIds(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(KpiParams.KPI_ID_LIST.name());
        if (idList == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        final List<Integer> kpiIdList = new ArrayList<Integer>();
        for (final Integer kpiId : idList.getIdsList()) {
            kpiIdList.add(kpiId);
        }
        try {
            BaseValueObjectList bvol = null;
            final Search search = new Search((Class)Kpi.class);
            search.addFilterIn("id", (Collection)kpiIdList);
            final List<Kpi> list = (List<Kpi>)this.kpiDao.search((ISearch)search);
            final Type listType = new TypeToken<List<KpiData>>() {}.getType();
            final List<KpiData> kpiList = (List<KpiData>)this.modelMapper.map((Object)list, listType);
            bvol = new BaseValueObjectList();
            bvol.setValueObjectList((List)kpiList);
            final Response response = this.OK(KpiParams.KPI_DATA_LIST.name(), (BaseValueObject)bvol);
            return response;
        }
        catch (Exception ex) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getKpisByIds() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_BY_ID_LIST_027.name(), "Failed to load kpis", (Throwable)ex));
        }
    }
    
    @Transactional
    public Response updateKPIState(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(KpiParams.KPI_ID_LIST.name());
        final Identifier state = (Identifier)request.get(KpiParams.KPI_STATE.name());
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().isEmpty() || state == null || state.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final int count = this.kpiDao.updateKpiState(idList.getIdsList(), state.getId());
            return this.OK(KpiParams.COUNT.name(), (BaseValueObject)new Identifier(count));
        }
        catch (Exception ex) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - updateKPIStatus() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_UPDATE_STATUS_026.name(), "Unknown error while updating kpi status by ids", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getKpiDataToCalcScore(final Request request) {
        final Identifier groupId = (Identifier)request.get(KpiParams.GROUP_ID.name());
        final Identifier periodMasterId = (Identifier)request.get(KpiParams.PERIOD_MASTER_ID.name());
        final DateResponse startDate = (DateResponse)request.get(KpiParams.START_DATE.name());
        if (startDate == null || startDate.getDate() == null || groupId == null || groupId.getId() == null || groupId.getId() < 0 || periodMasterId == null || periodMasterId.getId() == null || periodMasterId.getId() < 0) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Search search = new Search((Class)Kpi.class);
            search.addFilterIn("groupId", new Object[] { groupId.getId() });
            search.addFilterLessOrEqual("startDate", (Object)startDate.getDate());
            search.addFetch("kpiScore");
            final List<Kpi> list = (List<Kpi>)this.kpiDao.search((ISearch)search);
            final List<ScorecardCalculatorUtility> scorecardCalculatorUtilityList = new ArrayList<ScorecardCalculatorUtility>();
            for (final Kpi kpi : list) {
                Integer scoreId = null;
                Integer ratingLevel = 0;
                Double achivement = 0.0;
                final ScorecardCalculatorUtility scorecardCalculatorUtility = new ScorecardCalculatorUtility();
                scorecardCalculatorUtility.setKpiId(kpi.getId());
                scorecardCalculatorUtility.setWeightage(kpi.getWeightage());
                scorecardCalculatorUtility.setGroupId(kpi.getGroupId());
                scorecardCalculatorUtility.setIsGroupKpi(kpi.getGroupKpi());
                if (kpi.getKpiType() != null && kpi.getKpiType().getType() != null && kpi.getKpiType().getType().equalsIgnoreCase(KpiValidationParams.KpiType.INITIATIVE.name())) {
                    if (kpi.getKpiScore() != null && !kpi.getKpiScore().isEmpty()) {
                        for (final KpiScore score : kpi.getKpiScore()) {
                            if (score.getPeriodMasterId() != null && score.getPeriodMasterId() <= periodMasterId.getId()) {
                                scoreId = score.getId();
                                if (score.getRatingLevel() != null) {
                                    ratingLevel = score.getRatingLevel();
                                }
                                if (score.getAchievement() == null) {
                                    continue;
                                }
                                achivement = score.getAchievement();
                            }
                        }
                    }
                }
                else if (kpi.getKpiScore() != null && !kpi.getKpiScore().isEmpty()) {
                    for (final KpiScore score : kpi.getKpiScore()) {
                        if (score.getPeriodMasterId() != null && score.getPeriodMasterId() == (int)periodMasterId.getId()) {
                            scoreId = score.getId();
                            if (score.getRatingLevel() != null) {
                                ratingLevel = score.getRatingLevel();
                            }
                            if (score.getAchievement() == null) {
                                continue;
                            }
                            achivement = score.getAchievement();
                        }
                    }
                }
                scorecardCalculatorUtility.setScoreId(scoreId);
                scorecardCalculatorUtility.setRatingLevel(ratingLevel);
                scorecardCalculatorUtility.setAchievement(achivement);
                scorecardCalculatorUtilityList.add(scorecardCalculatorUtility);
            }
            final BaseValueObjectList bvolist = new BaseValueObjectList();
            bvolist.setValueObjectList((List)scorecardCalculatorUtilityList);
            final Response response = this.OK(KpiParams.SCORE_UTILITY_LIST.name(), (BaseValueObject)bvolist);
            return response;
        }
        catch (Exception ex) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getKpiDataToCalcScore() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_TO_CALC_SCORE_025.name(), "Failed to get KpiData To Calculate Score ", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchKpiAggregationType(final Request request) {
        final KpiAggregationTypeBean kpiAggregationType = (KpiAggregationTypeBean)request.get(KpiParams.KPI_AGGR_TYPE_DATA.name());
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        BaseValueObjectList list = null;
        if (kpiAggregationType == null) {
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_AGGR_TYPE_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final Search search = new Search((Class)KpiAggregationType.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final KpiAggregationType kpiAggregationType2 = (KpiAggregationType)this.modelMapper.map((Object)kpiAggregationType, (Class)KpiAggregationType.class);
            final Filter filter = this.genericDao.getFilterFromExample((Object)kpiAggregationType2, options);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<KpiAggregationType> kpiAggregationTypeList = (List<KpiAggregationType>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<KpiAggregationTypeBean>>() {}.getType();
            final List<? extends BaseValueObject> kpiAggBVList = (List<? extends BaseValueObject>)this.modelMapper.map((Object)kpiAggregationTypeList, listType);
            list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiAggBVList);
            final Response response = this.OK(KpiParams.KPI_AGGR_TYPE_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - searchKpiAggregationType() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_AGGR_TYPE_UNABLE_TO_SEARCH_008.name(), "Failed to search kpi AggregationType", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchKpiUom(final Request request) {
        final KpiUomData kpiUomData = (KpiUomData)request.get(KpiParams.KPI_UOM_DATA.name());
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        BaseValueObjectList list = null;
        if (kpiUomData == null) {
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UOM_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final Search search = new Search((Class)KpiUom.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final KpiUom kpiUom = (KpiUom)this.modelMapper.map((Object)kpiUomData, (Class)KpiUom.class);
            final Filter filter = this.genericDao.getFilterFromExample((Object)kpiUom, options);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<KpiUom> KpiUomList = (List<KpiUom>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<KpiUomData>>() {}.getType();
            final List<? extends BaseValueObject> kpiUomBVList = (List<? extends BaseValueObject>)this.modelMapper.map((Object)KpiUomList, listType);
            list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiUomBVList);
            final Response response = this.OK(KpiParams.KPI_UOM_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - searchKpiUom() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UOM_UNABLE_TO_SEARCH_008.name(), "Failed to search kpi uom", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchKpiType(final Request request) {
        final KpiTypeData kpiTypeData = (KpiTypeData)request.get(KpiParams.KPI_TYPE_DATA.name());
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        BaseValueObjectList list = null;
        if (kpiTypeData == null) {
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TYPE_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final Search search = new Search((Class)KpiType.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final KpiType kpiType = (KpiType)this.modelMapper.map((Object)kpiTypeData, (Class)KpiType.class);
            final Filter filter = this.genericDao.getFilterFromExample((Object)kpiType, options);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<KpiType> KpiTypeList = (List<KpiType>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<KpiTypeData>>() {}.getType();
            final List<? extends BaseValueObject> kpiTypeBVList = (List<? extends BaseValueObject>)this.modelMapper.map((Object)KpiTypeList, listType);
            list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiTypeBVList);
            final Response response = this.OK(KpiParams.KPI_TYPE_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - searchKpiType() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TYPE_UNABLE_TO_SEARCH_008.name(), "Failed to search kpi Type", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllKpiScoreByKpiIds(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(KpiParams.KPI_ID_LIST.name());
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().size() <= 0) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCORE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        final List<Integer> kpiIdList = new ArrayList<Integer>();
        for (final Integer kpiId : idList.getIdsList()) {
            kpiIdList.add(kpiId);
        }
        try {
            BaseValueObjectList bvol = null;
            final Search search = new Search((Class)KpiScore.class);
            search.addFilterIn("kpiId", (Collection)kpiIdList);
            final List<KpiScore> list = (List<KpiScore>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<KpiScoreBean>>() {}.getType();
            final List<KpiScoreBean> kpiScoreList = (List<KpiScoreBean>)this.searchModelMapper.map((Object)list, listType);
            bvol = new BaseValueObjectList();
            bvol.setValueObjectList((List)kpiScoreList);
            final Response response = this.OK(KpiParams.KPI_SCORE_DATA_LIST.name(), (BaseValueObject)bvol);
            return response;
        }
        catch (Exception ex) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getAllKpiScoreByKpiIds() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCORE_UNABLE_TO_GET_ALL_BY_ID_LIST_010.name(), "Failed to load kpi scores", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getKpiDataToCalcScorecardScore(final Request request) {
        final Identifier scorecardId = (Identifier)request.get(KpiParams.SCORECARD_ID.name());
        final Identifier periodMasterId = (Identifier)request.get(KpiParams.PERIOD_MASTER_ID.name());
        final DateResponse startDate = (DateResponse)request.get(KpiParams.START_DATE.name());
        if (startDate == null || startDate.getDate() == null || scorecardId == null || scorecardId.getId() == null || scorecardId.getId() < 0 || periodMasterId == null || periodMasterId.getId() == null || periodMasterId.getId() < 0) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Search search = new Search((Class)Kpi.class);
            search.addFilterIn("kpiScorecardRelationships.empScorecardId", new Object[] { scorecardId.getId() });
            search.addFilterLessOrEqual("startDate", (Object)startDate.getDate());
            search.addFetch("kpiScore");
            search.addFilterOr(new Filter[] { Filter.isNull("groupId"), Filter.or(new Filter[] { Filter.equal("groupKpi", (Object)true) }) });
            final List<Kpi> list = (List<Kpi>)this.kpiDao.search((ISearch)search);
            final List<ScorecardCalculatorUtility> scorecardCalculatorUtilityList = new ArrayList<ScorecardCalculatorUtility>();
            for (final Kpi kpi : list) {
                Integer scoreId = null;
                Integer ratingLevel = 0;
                Double achivement = 0.0;
                final ScorecardCalculatorUtility scorecardCalculatorUtility = new ScorecardCalculatorUtility();
                scorecardCalculatorUtility.setKpiId(kpi.getId());
                scorecardCalculatorUtility.setWeightage(kpi.getWeightage());
                scorecardCalculatorUtility.setScorecardId(scorecardId.getId());
                scorecardCalculatorUtility.setIsGroupKpi(kpi.getGroupKpi());
                if (kpi.getKpiType() != null && kpi.getKpiType().getType() != null && kpi.getKpiType().getType().equalsIgnoreCase(KpiValidationParams.KpiType.INITIATIVE.name())) {
                    if (kpi.getKpiScore() != null && !kpi.getKpiScore().isEmpty()) {
                        for (final KpiScore score : kpi.getKpiScore()) {
                            if (score.getPeriodMasterId() != null && score.getPeriodMasterId() <= periodMasterId.getId()) {
                                scoreId = score.getId();
                                if (score.getRatingLevel() != null) {
                                    ratingLevel = score.getRatingLevel();
                                }
                                if (score.getAchievement() == null) {
                                    continue;
                                }
                                achivement = score.getAchievement();
                            }
                        }
                    }
                }
                else if (kpi.getKpiScore() != null && !kpi.getKpiScore().isEmpty()) {
                    for (final KpiScore score : kpi.getKpiScore()) {
                        if (score.getPeriodMasterId() != null && score.getPeriodMasterId() == (int)periodMasterId.getId()) {
                            scoreId = score.getId();
                            if (score.getRatingLevel() != null) {
                                ratingLevel = score.getRatingLevel();
                            }
                            if (score.getAchievement() == null) {
                                continue;
                            }
                            achivement = score.getAchievement();
                        }
                    }
                }
                scorecardCalculatorUtility.setScoreId(scoreId);
                scorecardCalculatorUtility.setRatingLevel(ratingLevel);
                scorecardCalculatorUtility.setAchievement(achivement);
                scorecardCalculatorUtilityList.add(scorecardCalculatorUtility);
            }
            final BaseValueObjectList bvolist = new BaseValueObjectList();
            bvolist.setValueObjectList((List)scorecardCalculatorUtilityList);
            return this.OK(KpiParams.SCORE_UTILITY_LIST.name(), (BaseValueObject)bvolist);
        }
        catch (Exception ex) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getKpiDataToCalcScorecardScore() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_TO_CALC_SCORECARD_SCORE_041.name(), "Failed to load KpiData To Calculate ScorecardScore ", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getKpisByScorecardId(final Request request) {
        final Identifier scorecardId = (Identifier)request.get(KpiParams.SCORECARD_ID.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (scorecardId == null || scorecardId.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in request"));
        }
        try {
            final Search search = new Search((Class)Kpi.class);
            search.addFilterEqual("kpiScorecardRelationships.empScorecardId", (Object)scorecardId.getId());
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, 100, "code");
            final List<Kpi> result = (List<Kpi>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<KpiData>>() {}.getType();
            final List<KpiData> kpiList = (List<KpiData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiList);
            final Response response = this.OK(KpiParams.KPI_DATA_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getKpisByScorecardId() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_BY_SCORECARD_ID_040.name(), "Failed to load Kpis By scorecardId", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getKpisByCount(final Request request) {
        final Identifier count = (Identifier)request.get(KpiParams.KPI_COUNT.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        try {
            final Search search = new Search((Class)Kpi.class);
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, count.getId(), "code");
            final List<Kpi> result = (List<Kpi>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<KpiData>>() {}.getType();
            final List<KpiData> kpiList = (List<KpiData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiList);
            final Response response = this.OK(KpiParams.KPI_DATA_LIST.name(), (BaseValueObject)list);
            return response;
        }
        catch (Exception e) {
            KpiDataServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - getKpisByScorecardId() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_BY_SCORECARD_ID_040.name(), "Failed to load Kpis By scorecardId", (Throwable)e));
        }
    }
    
    static {
        log = Logger.getLogger((Class)KpiDataServiceImpl.class);
    }
    
    public class KpiSearchMap extends PropertyMap<Kpi, KpiData>
    {
        protected void configure() {
            ((KpiData)this.skip()).setKpiEmpRelationship((List)null);
            ((KpiData)this.skip()).setKpiPositionRelationship((List)null);
            ((KpiData)this.skip()).setKpiScore((List)null);
        }
    }
}
