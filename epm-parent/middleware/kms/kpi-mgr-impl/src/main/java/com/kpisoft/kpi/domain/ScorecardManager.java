package com.kpisoft.kpi.domain;

import com.canopus.mw.*;
import org.springframework.stereotype.*;
import com.kpisoft.kpi.dac.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.kpi.vo.param.*;
import javax.annotation.*;
import java.util.*;
import com.kpisoft.kpi.vo.*;
import com.canopus.mw.dto.*;

@Component
public class ScorecardManager extends BaseDomainManager implements CacheLoader<Integer, ScorecardDomain>
{
    @Autowired
    private ScorecardDataService dataService;
    @Autowired
    @Qualifier("scorecardCache")
    private Cache<Integer, ScorecardDomain> cache;
    private ScorecardDomain scorecardDomain;
    @Value("${cache.scorecard.startup:true}")
    private boolean cacheOnStartup;
    
    public ScorecardManager() {
        this.scorecardDomain = null;
        this.cacheOnStartup = false;
    }
    
    @PostConstruct
    public void loadToCache() {
        if (!this.cacheOnStartup) {
            return;
        }
        ExecutionContext.getCurrent().setCrossTenant();
        try {
            final BaseValueObjectList bList = (BaseValueObjectList)this.dataService.getAllScorecards(new Request()).get(KpiParams.SCORECARD_DATA_LIST.name());
            final List<ScorecardBean> scorecardList = (List<ScorecardBean>)bList.getValueObjectList();
            for (final ScorecardBean scorecardBean : scorecardList) {
                (this.scorecardDomain = new ScorecardDomain(this, this.dataService)).setScorecardBean(scorecardBean);
                this.cache.put(scorecardBean.getId(), this.scorecardDomain);
            }
        }
        catch (Exception ex) {}
        ExecutionContext.getCurrent().unSetCrossTenant();
    }
    
    public ScorecardBean createScorecard(ScorecardBean scorecardBean) {
        this.scorecardDomain = new ScorecardDomain(this, this.dataService);
        scorecardBean = this.scorecardDomain.createScorecard(scorecardBean);
        final Request request = new Request();
        request.put(KpiParams.SCORECARD_ID.name(), (BaseValueObject)new Identifier(scorecardBean.getId()));
        final Response res = this.scorecardDomain.getScorecard(request);
        scorecardBean = (ScorecardBean)res.get(KpiParams.SCORECARD.name());
        this.scorecardDomain.setScorecardBean(scorecardBean);
        this.cache.put(scorecardBean.getId(), this.scorecardDomain);
        return scorecardBean;
    }
    
    public Response getScorecard(final Request req) {
        this.scorecardDomain = new ScorecardDomain(this, this.dataService);
        final Response res = this.scorecardDomain.getScorecard(req);
        final ScorecardBean scorecardBean = (ScorecardBean)res.get(KpiParams.SCORECARD.name());
        this.scorecardDomain.setScorecardBean(scorecardBean);
        this.cache.put(scorecardBean.getId(), this.scorecardDomain);
        return res;
    }
    
    public Response updateScorecard(final Request req) {
        this.scorecardDomain = new ScorecardDomain(this, this.dataService);
        Response res = this.scorecardDomain.updateScorecard(req);
        ScorecardBean scorecardBean = (ScorecardBean)res.get(KpiParams.SCORECARD.name());
        final Request request = new Request();
        request.put(KpiParams.SCORECARD_ID.name(), (BaseValueObject)new Identifier(scorecardBean.getId()));
        res = this.scorecardDomain.getScorecard(request);
        scorecardBean = (ScorecardBean)res.get(KpiParams.SCORECARD.name());
        this.scorecardDomain.setScorecardBean(scorecardBean);
        this.cache.put(scorecardBean.getId(), this.scorecardDomain);
        return res;
    }
    
    public BooleanResponse removeScorecard(final Request req) {
        this.scorecardDomain = new ScorecardDomain(this, this.dataService);
        return this.scorecardDomain.removeScorecard(req);
    }
    
    public List<ScorecardBean> searchScorecard(final ScorecardBean scorecardBean, Page page, SortList sortList) {
        final Request request = new Request();
        request.put(KpiParams.SCORECARD.name(), (BaseValueObject)scorecardBean);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.searchScorecard(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(KpiParams.SCORECARD_DATA_LIST.name());
        final List<ScorecardBean> result = (List<ScorecardBean>)list.getValueObjectList();
        page = response.getPage();
        sortList = response.getSortList();
        return result;
    }
    
    public ScorecardDomain load(final Integer key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Integer saveScorecardScore(final ScorecardScoreBean data) {
        final Request request = new Request();
        request.put(KpiParams.SCORECARD_SCORE_DATA.name(), (BaseValueObject)data);
        final Response response = this.dataService.saveScorecardScore(request);
        final Identifier identifier = (Identifier)response.get(KpiParams.SCORECARD_SCORE_ID.name());
        return identifier.getId();
    }
    
    public ScorecardScoreBean getScorecardScore(final Integer id) {
        final Request request = new Request();
        request.put(KpiParams.SCORECARD_SCORE_ID.name(), (BaseValueObject)new Identifier(id));
        final Response response = this.dataService.getScorecardScore(request);
        final ScorecardScoreBean data = (ScorecardScoreBean)response.get(KpiParams.SCORECARD_SCORE_DATA.name());
        return data;
    }
    
    public List<ScorecardScoreBean> searchScorecardScore(final ScorecardScoreBean data, Page page, SortList sortList) {
        final Request request = new Request();
        request.put(KpiParams.SCORECARD_SCORE_DATA.name(), (BaseValueObject)data);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.searchScorecardScore(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(KpiParams.SCORECARD_SCORE_DATA_LIST.name());
        final List<ScorecardScoreBean> scoreList = (List<ScorecardScoreBean>)list.getValueObjectList();
        page = response.getPage();
        sortList = response.getSortList();
        return scoreList;
    }
    
    public Integer takeSnapshot(final Integer scorecardId, final Date date) {
        final ScorecardSnapshotVo objScorecardSnapshotVo = new ScorecardSnapshotVo();
        final ScorecardScoreBean objScorecardScoreBean = new ScorecardScoreBean();
        objScorecardScoreBean.setId(scorecardId);
        objScorecardSnapshotVo.setScorecardScore(objScorecardScoreBean);
        objScorecardSnapshotVo.setDate(date);
        final Request request = new Request();
        request.put(KpiParams.SCORECARD_ID.name(), (BaseValueObject)objScorecardSnapshotVo);
        final Response response = this.dataService.takeSnapshot(request);
        final Identifier objIdentifier = (Identifier)response.get(KpiParams.SCORECARD_SNAPSHOT.name());
        return objIdentifier.getId();
    }
    
    public ScorecardSnapshotVo getSnapshot(final Integer id, final Date date) {
        final DateResponse objDateIdentifier = new DateResponse();
        objDateIdentifier.setDate(date);
        final Identifier objIdentifier = new Identifier();
        objIdentifier.setId(id);
        final Request request = new Request();
        request.put(KpiParams.SCORECARD_ID.name(), (BaseValueObject)objIdentifier);
        request.put(KpiParams.DATE.name(), (BaseValueObject)objDateIdentifier);
        final Response response = this.dataService.getSnapshot(request);
        final ScorecardSnapshotVo objScorecardSnapshotVo = (ScorecardSnapshotVo)response.get(KpiParams.SCORECARD_SNAPSHOT.name());
        return objScorecardSnapshotVo;
    }
    
    public Integer updateScorecardStatus(final IdentifierList idList, final Integer state) {
        final Request request = new Request();
        request.put(KpiParams.SCORECARD_ID_LIST.name(), (BaseValueObject)idList);
        request.put(KpiParams.SCORECARD_STATE.name(), (BaseValueObject)new Identifier(state));
        final Response response = this.dataService.updateScorecardStatus(request);
        for (final Integer i : idList.getIdsList()) {
            final ScorecardDomain sDomain = (ScorecardDomain)this.cache.getIfPresent(i);
            final ScorecardBean scard = sDomain.getScorecardBean();
            scard.setState(state);
            sDomain.setScorecardBean(scard);
            this.cache.put(i, sDomain);
        }
        int count = 0;
        final Identifier idCount = (Identifier)response.get(KpiParams.COUNT.name());
        if (idCount != null && idCount.getId() != null && idCount.getId() > 0) {
            count = idCount.getId();
        }
        return count;
    }
    
    public Integer updateScorecardScoreStatus(final IdentifierList idList, final Integer state) {
        final Request request = new Request();
        request.put(KpiParams.SCORECARD_SCORE_ID_LIST.name(), (BaseValueObject)idList);
        request.put(KpiParams.SCORECARD_SCORE_STATE.name(), (BaseValueObject)new Identifier(state));
        final Response response = this.dataService.updateScorecardScoreStatus(request);
        for (final Integer i : idList.getIdsList()) {
            final ScorecardDomain sDomain = (ScorecardDomain)this.cache.getIfPresent(i);
            final ScorecardBean scard = sDomain.getScorecardBean();
            scard.setState(state);
            sDomain.setScorecardBean(scard);
            this.cache.put(i, sDomain);
        }
        int count = 0;
        final Identifier idCount = (Identifier)response.get(KpiParams.COUNT.name());
        if (idCount != null && idCount.getId() != null && idCount.getId() > 0) {
            count = idCount.getId();
        }
        return count;
    }
}
