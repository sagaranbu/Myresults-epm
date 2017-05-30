package com.kpisoft.kpi.impl;

import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import com.kpisoft.kpi.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import com.kpisoft.strategy.params.*;
import com.kpisoft.kpi.utility.*;
import com.canopus.mw.*;
import com.kpisoft.strategy.program.service.*;
import com.kpisoft.kpi.vo.param.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.*;
import com.canopus.mw.dto.*;
import java.util.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ ScorecardManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class ScorecardManagerServiceImpl extends BaseMiddlewareBean implements ScorecardManagerService
{
    @Autowired
    private ScorecardManager scorecardManager;
    @Autowired
    private IServiceLocator serviceLocator;
    private static final Logger log;
    
    public ScorecardManagerServiceImpl() {
        this.scorecardManager = null;
        this.serviceLocator = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response createScorecard(Request request) {
        ScorecardBean scorecardBean = (ScorecardBean)request.get(KpiParams.SCORECARD.name());
        final Identifier scorecardState = (Identifier)request.get(KpiParams.SCORECARD_STATE.name());
        final BaseValueObjectMap workflowLevels = (BaseValueObjectMap)request.get(ProgramParams.WORKFLOW_LEVELS.name());
        final Identifier progId = (Identifier)request.get(ProgramParams.STRATEGY_PROGRAM_ID.name());
        final Identifier gradeId = (Identifier)request.get(ProgramParams.GRADE_ID.name());
        if (scorecardBean == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        BaseValueObjectMap result = null;
        BaseValueObjectMap map = null;
        try {
            if (scorecardState != null && scorecardState.getId() != null && scorecardState.getId() > 0) {
                scorecardBean.setState(scorecardState.getId());
            }
            else {
                scorecardBean.setState(ScorecardState.CREATED.ordinal());
            }
            scorecardBean = this.scorecardManager.createScorecard(scorecardBean);
            final Identifier identifier = new Identifier();
            identifier.setId(scorecardBean.getId());
            final ProgramManagerService programService = (ProgramManagerService)this.serviceLocator.getService("ProgramManagerServiceImpl");
            if (workflowLevels == null || workflowLevels.getBaseValueMap() == null || workflowLevels.getBaseValueMap().isEmpty()) {
                request = new Request();
                request.put(ProgramParams.STRATEGY_PROGRAM_ID.name(), (BaseValueObject)progId);
                request.put(ProgramParams.EMP_ID.name(), (BaseValueObject)new Identifier(scorecardBean.getEmployeeId()));
                request.put(ProgramParams.POSITION_ID.name(), (BaseValueObject)new Identifier(scorecardBean.getPositionId()));
                request.put(ProgramParams.GRADE_ID.name(), (BaseValueObject)gradeId);
                request.put(ProgramParams.ORG_ID.name(), (BaseValueObject)new Identifier(scorecardBean.getOrgUnitId()));
                Response response = programService.getValidationRules(request);
                result = (BaseValueObjectMap)response.get(ProgramParams.MAP_RESULT.name());
                request = new Request();
                request.put(ProgramParams.STRATEGY_PROGRAM_ID.name(), (BaseValueObject)progId);
                request.put(ProgramParams.EMP_ID.name(), (BaseValueObject)new Identifier(scorecardBean.getEmployeeId()));
                request.put(ProgramParams.MAP_RESULT.name(), (BaseValueObject)result);
                response = programService.getWorkflowLevels(request);
                map = (BaseValueObjectMap)response.get(ProgramParams.WORKFLOW_LEVELS.name());
            }
            else {
                map = workflowLevels;
            }
            if (map != null && map.getBaseValueMap() != null && !map.getBaseValueMap().isEmpty()) {
                request = new Request();
                request.put(WorkflowParams.SCORECARD_ID.name(), (BaseValueObject)identifier);
                request.put(WorkflowParams.EMP_ID.name(), (BaseValueObject)new Identifier(scorecardBean.getEmployeeId()));
                request.put(WorkflowParams.SCORECARD_STATE.name(), (BaseValueObject)new StringIdentifier(scorecardBean.getState().toString()));
                request.put(WorkflowParams.WORKFLOW_LEVELS.name(), (BaseValueObject)map);
                this.getWorkFlowService().goalSettingTrigger(request);
            }
            return this.OK(KpiParams.SCORECARD_ID.name(), (BaseValueObject)identifier);
        }
        catch (Exception ex) {
            ScorecardManagerServiceImpl.log.error((Object)("Exception in ScorecardManagerServiceImpl - createScorecard() : " + ex));
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_UNABLE_TO_CREATE_003.name(), "Failed to create scorecard", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getScorecard(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.SCORECARD_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Response res = this.scorecardManager.getScorecard(request);
            final ScorecardBean scorecardBean = (ScorecardBean)res.get(KpiParams.SCORECARD.name());
            return this.OK(KpiParams.SCORECARD.name(), (BaseValueObject)scorecardBean);
        }
        catch (Exception ex) {
            ScorecardManagerServiceImpl.log.error((Object)("Exception in ScorecardManagerServiceImpl - getScorecard() : " + ex));
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_UNABLE_TO_GET_004.name(), "Failed to get scorecard", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response updateScorecard(final Request request) {
        ScorecardBean scorecardBean = (ScorecardBean)request.get(KpiParams.SCORECARD.name());
        if (scorecardBean == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Response res = this.scorecardManager.updateScorecard(request);
            scorecardBean = (ScorecardBean)res.get(KpiParams.SCORECARD.name());
            final Identifier identifier = new Identifier();
            identifier.setId(scorecardBean.getId());
            return this.OK(KpiParams.SCORECARD_ID.name(), (BaseValueObject)identifier);
        }
        catch (Exception ex) {
            ScorecardManagerServiceImpl.log.error((Object)("Exception in ScorecardManagerServiceImpl - updateScorecard() : " + ex));
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_UNABLE_TO_UPDATE_005.name(), "Failed to update scorecard", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response removeScorecard(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.SCORECARD_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final BooleanResponse res = this.scorecardManager.removeScorecard(request);
            return this.OK(KpiParams.IS_SCORECARD.name(), (BaseValueObject)res);
        }
        catch (Exception ex) {
            ScorecardManagerServiceImpl.log.error((Object)("Exception in ScorecardManagerServiceImpl - removeScorecard() : " + ex));
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_UNABLE_TO_DELETE_006.name(), "Failed to delete scorecard.", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response searchScorecard(final Request request) {
        final ScorecardBean scorecardBean = (ScorecardBean)request.get(KpiParams.SCORECARD.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (scorecardBean == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_INVALID_INPUT_002.name(), "No data object in request"));
        }
        try {
            final List<ScorecardBean> result = this.scorecardManager.searchScorecard(scorecardBean, page, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final Response response = this.OK(KpiParams.SCORECARD_DATA_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            ScorecardManagerServiceImpl.log.error((Object)("Exception in ScorecardManagerServiceImpl - searchScorecard() : " + e));
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_UNABLE_TO_SEARCH_008.name(), "Failed to load the scorecards data", new Object[] { e.getMessage() }));
        }
    }
    
    public Response saveScorecardScore(final Request request) {
        final ScorecardScoreBean data = (ScorecardScoreBean)request.get(KpiParams.SCORECARD_SCORE_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Integer id = this.scorecardManager.saveScorecardScore(data);
            return this.OK(KpiParams.SCORECARD_SCORE_ID.name(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            ScorecardManagerServiceImpl.log.error((Object)("Exception in ScorecardManagerServiceImpl - saveScorecardScore() : " + e));
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_UNABLE_TO_CREATE_003.name(), "Failed to save scorecard score", new Object[] { e.getMessage() }));
        }
    }
    
    public Response updateScorecardScore(final Request request) {
        final ScorecardScoreBean data = (ScorecardScoreBean)request.get(KpiParams.SCORECARD_SCORE_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Integer id = this.scorecardManager.saveScorecardScore(data);
            return this.OK(KpiParams.SCORECARD_SCORE_ID.name(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            ScorecardManagerServiceImpl.log.error((Object)("Exception in ScorecardManagerServiceImpl - updateScorecardScore() : " + e));
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_UNABLE_TO_UPDATE_005.name(), "Failed to update scorecard score", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getScorecardScore(final Request request) {
        final Identifier identifier = (Identifier)request.get(KpiParams.SCORECARD_SCORE_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final ScorecardScoreBean data = this.scorecardManager.getScorecardScore(identifier.getId());
            return this.OK(KpiParams.SCORECARD_SCORE_DATA.name(), (BaseValueObject)data);
        }
        catch (Exception e) {
            ScorecardManagerServiceImpl.log.error((Object)("Exception in ScorecardManagerServiceImpl - getScorecardScore() : " + e));
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_UNABLE_TO_GET_004.name(), "Failed to load scoecardScore", new Object[] { e.getMessage() }));
        }
    }
    
    public Response searchScorecardScore(final Request request) {
        final ScorecardScoreBean data = (ScorecardScoreBean)request.get(KpiParams.SCORECARD_SCORE_DATA.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<ScorecardScoreBean> result = this.scorecardManager.searchScorecardScore(data, page, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final Response response = this.OK(KpiParams.SCORECARD_SCORE_DATA_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            ScorecardManagerServiceImpl.log.error((Object)("Exception in ScorecardManagerServiceImpl - searchScorecardScore() : " + e));
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_UNABLE_TO_SEARCH_008.name(), "Failed to search for scorecardScores", new Object[] { e.getMessage() }));
        }
    }
    
    public Response takeSnapshot(final Request request) {
        final ScorecardSnapshotVo objScorecardSnapshotVo = (ScorecardSnapshotVo)request.get(KpiParams.SCORECARD_ID.name());
        if (objScorecardSnapshotVo == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_SNAPSHOT_INVALID_INPUT_002.name(), "Scorecard id is required");
        }
        try {
            final Integer id = this.scorecardManager.takeSnapshot(objScorecardSnapshotVo.getScorecardScore().getId(), objScorecardSnapshotVo.getDate());
            return this.OK(KpiParams.SCORECARD_SNAPSHOT.name(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception ex) {
            ScorecardManagerServiceImpl.log.error((Object)("Exception in ScorecardManagerServiceImpl - takeSnapshot() : " + ex.getMessage()));
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_SNAPSHOT_UNABLE_TO_TAKE_009.name(), "Failed to take scorecard.", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getSnapshot(final Request request) {
        final DateResponse date = (DateResponse)request.get(KpiParams.DATE.name());
        final Identifier scorecardId = (Identifier)request.get(KpiParams.SCORECARD_ID.name());
        if (date == null && scorecardId.getId() == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_SNAPSHOT_INVALID_INPUT_002.name(), "Date and Scorecard id is required"));
        }
        try {
            final ScorecardSnapshotVo objScorecardSnapshotVo = this.scorecardManager.getSnapshot(scorecardId.getId(), date.getDate());
            return this.OK(KpiParams.SCORECARD_SNAPSHOT.name(), (BaseValueObject)objScorecardSnapshotVo);
        }
        catch (Exception ex) {
            ScorecardManagerServiceImpl.log.error((Object)("Exception in ScorecardManagerServiceImpl - getSnapshot() : " + ex.getMessage()));
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_SNAPSHOT_UNABLE_TO_GET_004.name(), "Failed to get snapshot", new Object[] { ex.getMessage() }));
        }
    }
    
    private WorkflowManagerService getWorkFlowService() {
        return (WorkflowManagerService)this.serviceLocator.getService("WorkflowManagerServiceImpl");
    }
    
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
            final int count = this.scorecardManager.updateScorecardStatus(idList, state.getId());
            return this.OK(KpiParams.COUNT.name(), (BaseValueObject)new Identifier(count));
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_UNABLE_TO_UPDATE_STATUS_009.name(), "Unknown error while updating scorecard state by scorecard id", (Throwable)ex));
        }
    }
    
    public Response updateScorecardScoreStatus(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(KpiParams.SCORECARD_SCORE_ID_LIST.name());
        final Identifier state = (Identifier)request.get(KpiParams.SCORECARD_SCORE_STATE.name());
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().isEmpty() || state == null || state.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Integer count = this.scorecardManager.updateScorecardScoreStatus(idList, state.getId());
            return this.OK(KpiParams.COUNT.name(), (BaseValueObject)new Identifier(count));
        }
        catch (Exception e) {
            ScorecardManagerServiceImpl.log.error((Object)("Exception in ScorecardManagerServiceImpl - updateScorecardScoreStatus() : " + e));
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_SCORE_UNABLE_TO_UPDATE_STATUS_009.name(), "Failed to update scorecard score state", new Object[] { e.getMessage() }));
        }
    }
    
    static {
        log = Logger.getLogger((Class)ScorecardManagerServiceImpl.class);
    }
}
