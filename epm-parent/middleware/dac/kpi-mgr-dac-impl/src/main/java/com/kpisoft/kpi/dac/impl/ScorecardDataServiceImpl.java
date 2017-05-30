package com.kpisoft.kpi.dac.impl;

import com.kpisoft.kpi.dac.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.kpi.dac.dao.*;
import com.canopus.dac.hibernate.*;
import org.apache.log4j.*;
import org.modelmapper.convention.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.dac.impl.domain.*;
import com.kpisoft.kpi.vo.param.*;
import java.io.*;
import com.kpisoft.kpi.utility.*;
import com.canopus.dac.*;
import org.springframework.transaction.annotation.*;
import com.canopus.mw.dto.param.*;
import com.canopus.dac.utils.*;
import org.modelmapper.*;
import com.googlecode.genericdao.search.*;
import java.lang.reflect.*;
import java.text.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.*;
import java.util.*;

@Component
public class ScorecardDataServiceImpl extends BaseDataAccessService implements ScorecardDataService
{
    @Autowired
    private ScorecardDao scorecardDao;
    @Autowired
    private ScorecardSnapshotDao objScorecardSnapshotDao;
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public ScorecardDataServiceImpl() {
        this.scorecardDao = null;
        this.objScorecardSnapshotDao = null;
        this.genericDao = null;
        this.modelMapper = null;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)ScorecardMetaData.class, (Class)ScorecardBean.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)ScorecardScoreBean.class, (Class)ScorecardScore.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)ScorecardSnapshotVo.class, (Class)ScorecardSnapshot.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    @Transactional
    public Response saveScorecard(final Request req) {
        final ScorecardBean scorecardBean = (ScorecardBean)req.get(KpiParams.SCORECARD.name());
        ScorecardMetaData scorecard = null;
        Response res = null;
        if (scorecardBean != null && scorecardBean.getId() != null && scorecardBean.getId() > 0) {
            scorecard = (ScorecardMetaData)this.scorecardDao.find((Serializable)scorecardBean.getId());
        }
        else {
            scorecard = new ScorecardMetaData();
        }
        try {
            this.modelMapper.map((Object)scorecardBean, (Object)scorecard);
            this.scorecardDao.save(scorecard);
            scorecardBean.setId(scorecard.getId());
            res = new Response();
            res.put(KpiParams.SCORECARD.name(), (BaseValueObject)scorecardBean);
            return res;
        }
        catch (Exception ex) {
            ScorecardDataServiceImpl.log.error((Object)"Exception in ScorecardDataServiceImpl - saveScorecard() : ", (Throwable)ex);
            throw new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_UNABLE_TO_CREATE_003.name(), "Failed to create scorecard", (Throwable)ex);
        }
    }
    
    @Transactional(readOnly = true)
    public Response getScorecard(final Request req) {
        final Identifier id = (Identifier)req.get(KpiParams.SCORECARD_ID.name());
        ScorecardMetaData scorecard = null;
        try {
            scorecard = (ScorecardMetaData)this.scorecardDao.find((Serializable)id.getId());
        }
        catch (Exception ex) {
            ScorecardDataServiceImpl.log.error((Object)"Exception in ScorecardDataServiceImpl - () : ", (Throwable)ex);
            throw new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_UNABLE_TO_GET_004.name(), "Failed to get scorecard", (Throwable)ex);
        }
        if (scorecard == null) {
            throw new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_DOES_NOT_EXIST_001.name(), "scorecard id {0} does not exist.", new Object[] { id.getId() });
        }
        final ScorecardBean scorecardBean = (ScorecardBean)this.modelMapper.map((Object)scorecard, (Class)ScorecardBean.class);
        final Response res = new Response();
        res.put(KpiParams.SCORECARD.name(), (BaseValueObject)scorecardBean);
        return res;
    }
    
    @Transactional
    public Response updateScorecard(final Request req) {
        final ScorecardBean scorecardBean = (ScorecardBean)req.get(KpiParams.SCORECARD.name());
        ScorecardMetaData scorecard = null;
        Response res = null;
        if (scorecardBean != null && scorecardBean.getId() != null && scorecardBean.getId() > 0) {
            scorecard = (ScorecardMetaData)this.scorecardDao.find((Serializable)scorecardBean.getId());
        }
        else {
            if (scorecardBean == null) {
                throw new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_INVALID_INPUT_002.name(), "scorecard does not exist");
            }
            scorecard = new ScorecardMetaData();
        }
        try {
            this.modelMapper.map((Object)scorecardBean, (Object)scorecard);
            this.scorecardDao.save(scorecard);
            scorecardBean.setId(scorecard.getId());
            res = new Response();
            res.put(KpiParams.SCORECARD.name(), (BaseValueObject)scorecardBean);
            return res;
        }
        catch (Exception ex) {
            ScorecardDataServiceImpl.log.error((Object)"Exception in ScorecardDataServiceImpl - updateScorecard() : ", (Throwable)ex);
            throw new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_UNABLE_TO_UPDATE_005.name(), "Failed to update scorecard", (Throwable)ex);
        }
    }
    
    @Transactional
    public BooleanResponse deleteScorecard(final Request req) {
        try {
            final Identifier id = (Identifier)req.get(KpiParams.SCORECARD_ID.name());
            if (id == null) {
                throw new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_INVALID_INPUT_002.name(), "Invalid scorecard");
            }
            final boolean isScorecard = this.scorecardDao.removeById((Serializable)id.getId());
            final BooleanResponse res = new BooleanResponse(isScorecard);
            return res;
        }
        catch (Exception ex) {
            ScorecardDataServiceImpl.log.error((Object)"Exception in ScorecardDataServiceImpl - deleteScorecard() : ", (Throwable)ex);
            throw new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_UNABLE_TO_DELETE_006.name(), "Failed to delete scorecard.", (Throwable)ex);
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchScorecard(final Request request) {
        final ScorecardBean scorecardBean = (ScorecardBean)request.get(KpiParams.SCORECARD.name());
        if (scorecardBean == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "startDate";
        try {
            final ScorecardMetaData scorecard = (ScorecardMetaData)this.modelMapper.map((Object)scorecardBean, (Class)ScorecardMetaData.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.scorecardDao.getFilterFromExample(scorecard, options);
            final Search search = new Search((Class)ScorecardMetaData.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.genericDao.count((ISearch)search), defaultField, HeaderParam.DESC.name());
            final List<ScorecardMetaData> result = (List<ScorecardMetaData>)this.scorecardDao.search((ISearch)search);
            final Type listType = new TypeToken<List<ScorecardBean>>() {}.getType();
            final List<ScorecardBean> scorecardList = (List<ScorecardBean>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)scorecardList);
            final Response response = this.OK(KpiParams.SCORECARD_DATA_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            ScorecardDataServiceImpl.log.error((Object)"Exception in ScorecardDataServiceImpl - searchScorecard() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_UNABLE_TO_SEARCH_008.name(), "Failed to load the scorecards data", new Object[] { e.getMessage() }));
        }
    }
    
    @Transactional
    public Response saveScorecardScore(final Request request) {
        final ScorecardScoreBean data = (ScorecardScoreBean)request.get(KpiParams.SCORECARD_SCORE_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            ScorecardScore score = null;
            if (data.getId() != null && data.getId() != null && data.getId() > 0) {
                score = (ScorecardScore)this.genericDao.find((Class)ScorecardScore.class, (Serializable)data.getId());
            }
            else {
                score = new ScorecardScore();
            }
            this.modelMapper.map((Object)data, (Object)score);
            this.genericDao.save((Object)score);
            return this.OK(KpiParams.SCORECARD_SCORE_ID.name(), (BaseValueObject)new Identifier(score.getId()));
        }
        catch (Exception e) {
            ScorecardDataServiceImpl.log.error((Object)"Exception in ScorecardDataServiceImpl - saveScorecardScore() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_UNABLE_TO_CREATE_003.name(), "Failed to save scorecard score", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getScorecardScore(final Request request) {
        final Identifier identifier = (Identifier)request.get(KpiParams.SCORECARD_SCORE_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final ScorecardScore score = (ScorecardScore)this.genericDao.find((Class)ScorecardScore.class, (Serializable)identifier.getId());
            final ScorecardScoreBean data = (ScorecardScoreBean)this.modelMapper.map((Object)score, (Class)ScorecardScoreBean.class);
            return this.OK(KpiParams.SCORECARD_SCORE_DATA.name(), (BaseValueObject)data);
        }
        catch (Exception e) {
            ScorecardDataServiceImpl.log.error((Object)"Exception in ScorecardDataServiceImpl - getScorecardScore() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_UNABLE_TO_GET_004.name(), "Failed to load scoecardScore", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchScorecardScore(final Request request) {
        final ScorecardScoreBean data = (ScorecardScoreBean)request.get(KpiParams.SCORECARD_SCORE_DATA.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "reviewDate";
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final ScorecardScore score = (ScorecardScore)this.modelMapper.map((Object)data, (Class)ScorecardScore.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.genericDao.getFilterFromExample((Object)score, options);
            final Search search = new Search((Class)ScorecardScore.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.genericDao.count((ISearch)search), defaultField, HeaderParam.DESC.name());
            final List<ScorecardScore> result = (List<ScorecardScore>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<ScorecardScoreBean>>() {}.getType();
            final List<ScorecardScoreBean> scoreList = (List<ScorecardScoreBean>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)scoreList);
            final Response response = this.OK(KpiParams.SCORECARD_SCORE_DATA_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            ScorecardDataServiceImpl.log.error((Object)"Exception in ScorecardDataServiceImpl - searchScorecardScore() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_UNABLE_TO_SEARCH_008.name(), "Failed to search for scorecardScores", (Throwable)e));
        }
    }
    
    @Transactional
    public Response takeSnapshot(final Request request) {
        final ScorecardSnapshotVo objScorecardSnapshotVo = (ScorecardSnapshotVo)request.get(KpiParams.SCORECARD_ID.name());
        if (objScorecardSnapshotVo == null) {
            throw new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_SNAPSHOT_INVALID_INPUT_002.name(), "Scorecard id is required");
        }
        ScorecardSnapshot objScorecardSnapshot = null;
        try {
            if (objScorecardSnapshotVo.getId() > 0) {
                objScorecardSnapshot = (ScorecardSnapshot)this.objScorecardSnapshotDao.find((Serializable)objScorecardSnapshotVo.getId());
            }
            else {
                objScorecardSnapshot = new ScorecardSnapshot();
            }
            this.modelMapper.map((Object)objScorecardSnapshotVo, (Object)objScorecardSnapshot);
            this.objScorecardSnapshotDao.save(objScorecardSnapshot);
        }
        catch (Exception ex) {
            ScorecardDataServiceImpl.log.error((Object)"Exception in ScorecardDataServiceImpl - takeSnapshot() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_SNAPSHOT_UNABLE_TO_TAKE_009.name(), "Failed to take scorecard.", (Throwable)ex));
        }
        return this.OK(KpiParams.SCORECARD_SNAPSHOT.name(), (BaseValueObject)new Identifier(objScorecardSnapshot.getId()));
    }
    
    @Transactional(readOnly = true)
    public Response getSnapshot(final Request request) {
        final DateResponse date = (DateResponse)request.get(KpiParams.DATE.name());
        final Identifier objIdentifier = (Identifier)request.get(KpiParams.SCORECARD_ID.name());
        if (date == null && objIdentifier.getId() == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_SNAPSHOT_INVALID_INPUT_002.name(), "Date and Scorecard id is required"));
        }
        ScorecardSnapshotVo objScorecardSnapshotVo = null;
        try {
            if (objIdentifier.getId() != null) {
                final Search objSearch = new Search((Class)ScorecardSnapshot.class);
                final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
                final String date2 = formatter.format(date.getDate());
                objSearch.addFilterCustom("rowid=(select max(rowid) from _it where SNAPSHOTDATE <= '" + date2 + "' and SDE_PK_ID = '" + objIdentifier.getId() + "')");
                final ScorecardSnapshot objScorecardSnapshot = (ScorecardSnapshot)this.objScorecardSnapshotDao.searchUnique((ISearch)objSearch);
                if (objScorecardSnapshot == null) {
                    return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_SNAPSHOT_DOES_NOT_EXIST_001.name(), "No data found"));
                }
                objScorecardSnapshotVo = (ScorecardSnapshotVo)this.modelMapper.map((Object)objScorecardSnapshot, (Class)ScorecardSnapshotVo.class);
            }
        }
        catch (Exception e) {
            ScorecardDataServiceImpl.log.error((Object)"Exception in ScorecardDataServiceImpl - getSnapshot() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_SNAPSHOT_UNABLE_TO_GET_004.name(), "Failed to get snapshot", (Throwable)e));
        }
        return this.OK(KpiParams.SCORECARD_SNAPSHOT.name(), (BaseValueObject)objScorecardSnapshotVo);
    }
    
    @Transactional
    public Response updateScorecardStatus(final Request request) {
        IdentifierList idList = (IdentifierList)request.get(KpiParams.SCORECARD_ID_LIST.name());
        final Identifier id = (Identifier)request.get(KpiParams.SCORECARD_ID.name());
        final Identifier state = (Identifier)request.get(KpiParams.SCORECARD_STATE.name());
        if (((id == null || id.getId() == null || id.getId() <= 0) && (idList == null || idList.getIdsList() == null || idList.getIdsList().isEmpty())) || state == null || state.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            if (id != null && id.getId() != null && id.getId() > 0) {
                if (idList != null && idList.getIdsList() != null) {
                    idList.getIdsList().add(id.getId());
                }
                else {
                    idList = new IdentifierList((List)new ArrayList());
                    idList.getIdsList().add(id.getId());
                }
            }
            final int count = this.scorecardDao.updateScorecardStatus(idList.getIdsList(), state.getId());
            return this.OK(KpiParams.COUNT.name(), (BaseValueObject)new Identifier(count));
        }
        catch (Exception ex) {
            ScorecardDataServiceImpl.log.error((Object)"Exception in ScorecardDataServiceImpl - updateScorecardStatus() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_SCORECARD_UNABLE_TO_UPDATE_STATUS_009.name(), "Failed to update scorecard state by scorecard id", (Throwable)ex));
        }
    }
    
    @Transactional
    public Response updateScorecardScoreStatus(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(KpiParams.SCORECARD_SCORE_ID_LIST.name());
        final Identifier state = (Identifier)request.get(KpiParams.SCORECARD_SCORE_STATE.name());
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().isEmpty() || state == null || state.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final int count = this.scorecardDao.updateScorecardScoreStatus(idList.getIdsList(), state.getId());
            return this.OK(KpiParams.COUNT.name(), (BaseValueObject)new Identifier(count));
        }
        catch (Exception e) {
            ScorecardDataServiceImpl.log.error((Object)("Exception in ScorecardDataServiceImpl - updateScorecardScoreStatus() : " + e));
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_UNABLE_TO_UPDATE_STATUS_009.name(), "Failed to update scorecard score state", new Object[] { e.getMessage() }));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllScorecards(final Request request) {
        try {
            final List<ScorecardMetaData> scorecardList = (List<ScorecardMetaData>)this.scorecardDao.findAll();
            final Type listType = new TypeToken<List<ScorecardBean>>() {}.getType();
            final List<ScorecardBean> scorecardBeanList = (List<ScorecardBean>)this.modelMapper.map((Object)scorecardList, listType);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)scorecardBeanList);
            return this.OK(KpiParams.SCORECARD_DATA_LIST.name(), (BaseValueObject)bList);
        }
        catch (Exception e) {
            ScorecardDataServiceImpl.log.error((Object)("Exception in ScorecardDataServiceImpl - getAllScorecards() : " + e));
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_UNABLE_TO_GET_004.name(), "Failed to fetch scorecards", new Object[] { e.getMessage() }));
        }
    }
    
    static {
        log = Logger.getLogger((Class)ScorecardDataServiceImpl.class);
    }
}
