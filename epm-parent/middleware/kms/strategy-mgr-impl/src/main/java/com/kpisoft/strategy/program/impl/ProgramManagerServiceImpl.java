package com.kpisoft.strategy.program.impl;

import com.kpisoft.strategy.program.service.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.strategy.params.*;
import com.kpisoft.strategy.utility.*;
import com.canopus.mw.*;
import com.kpisoft.strategy.program.domain.*;
import com.kpisoft.strategy.program.vo.*;
import java.util.*;
import com.kpisoft.org.*;
import com.canopus.mw.dto.*;
import com.canopus.entity.*;
import com.canopus.entity.vo.params.*;
import com.canopus.entity.vo.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ ProgramManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class ProgramManagerServiceImpl extends BaseMiddlewareBean implements ProgramManagerService
{
    private static final Logger logger;
    @Autowired
    private PerformanceProgramManager performanceProgramManager;
    @Autowired
    private IServiceLocator serviceLocator;
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response createPerformanceProgram(final Request request) {
        try {
            final StrategyProgramBean strategyProgramBean = (StrategyProgramBean)request.get(ProgramParams.STRATEGY_PROGRAM.name());
            if (strategyProgramBean == null) {
                return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_INVALID_INPUT_002.name(), "No data found in request"));
            }
            final PerformanceProgram performanceProgram = this.performanceProgramManager.createPerformanceProgram(strategyProgramBean);
            return this.OK(ProgramParams.STRATEGY_PROGRAM_ID.name(), (BaseValueObject)new Identifier(performanceProgram.getStrategyProgramBean().getId()));
        }
        catch (Exception e) {
            ProgramManagerServiceImpl.logger.error((Object)"Exception in ProgramManagerServiceImpl - createPerformanceProgram() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_CREATE_003.name(), "Failed to create creatin Program", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getPerformanceProgram(final Request request) {
        try {
            final Identifier identifier = (Identifier)request.get(ProgramParams.STRATEGY_PROGRAM_ID.name());
            if (identifier == null || identifier.getId() == null) {
                return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_INVALID_INPUT_002.name(), "Program Id is required"));
            }
            final PerformanceProgram objPerformanceProgram = this.performanceProgramManager.getPerformanceProgram(identifier.getId());
            return this.OK(ProgramParams.STRATEGY_PROGRAM.name(), (BaseValueObject)objPerformanceProgram.getStrategyProgramBean());
        }
        catch (Exception e) {
            ProgramManagerServiceImpl.logger.error((Object)"Exception in ProgramManagerServiceImpl - getPerformanceProgram() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_GET_004.name(), "Unknown error while fetching performance program", (Throwable)e));
        }
    }
    
    public Response getProgramRules(final Request request) {
        try {
            final StrategyProgramPolicyRuleBean objStrategyProgramPolicyRuleBean = (StrategyProgramPolicyRuleBean)request.get(ProgramParams.PROGRAM_POLICY_RULE.name());
            if (objStrategyProgramPolicyRuleBean.getOrgPositionId() == null && objStrategyProgramPolicyRuleBean.getEmployeeGradeId() == null && objStrategyProgramPolicyRuleBean.getEmployeeId() == null && objStrategyProgramPolicyRuleBean.getOrgUnitId() == null && objStrategyProgramPolicyRuleBean.getEndDate() == null && objStrategyProgramPolicyRuleBean.getStartDate() == null) {
                return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PROG_POLICY_RULE_INVALID_INPUT_002.name(), "Atleast one value is required for search"));
            }
            final List<StrategyProgramPolicyRuleBean> alStrategyProgramPolicyRuleBean = this.performanceProgramManager.getProgramRules(objStrategyProgramPolicyRuleBean.getEmployeeGradeId(), objStrategyProgramPolicyRuleBean.getEmployeeId(), objStrategyProgramPolicyRuleBean.getOrgPositionId(), objStrategyProgramPolicyRuleBean.getOrgUnitId());
            final BaseValueObjectList alBaseValueObjectList = new BaseValueObjectList();
            alBaseValueObjectList.setValueObjectList((List)alStrategyProgramPolicyRuleBean);
            return this.OK(ProgramParams.PROGRAM_POLICY_RULE_LIST.name(), (BaseValueObject)alBaseValueObjectList);
        }
        catch (Exception e) {
            ProgramManagerServiceImpl.logger.error((Object)"Exception in ProgramManagerServiceImpl - getProgramRules() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PROG_POLICY_RULE_UNABLE_TO_GET_004.name(), "Unknown error while fetching performance program rules", (Throwable)e));
        }
    }
    
    public Response updatePerformanceProgram(final Request request) {
        try {
            final StrategyProgramBean objStrategyProgramBean = (StrategyProgramBean)request.get(ProgramParams.STRATEGY_PROGRAM.name());
            if (objStrategyProgramBean == null) {
                return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_INVALID_INPUT_002.name(), "Program Id is required"));
            }
            final PerformanceProgram performanceProgram = this.performanceProgramManager.createPerformanceProgram(objStrategyProgramBean);
            final Identifier id = new Identifier(performanceProgram.getStrategyProgramBean().getId());
            return this.OK(ProgramParams.STRATEGY_PROGRAM_ID.name(), (BaseValueObject)id);
        }
        catch (Exception e) {
            ProgramManagerServiceImpl.logger.error((Object)"Exception in ProgramManagerServiceImpl - updatePerformanceProgram() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_UPDATE_005.name(), "Unknown error while updating performance program", (Throwable)e));
        }
    }
    
    public Response removePerformanceProgram(final Request request) {
        try {
            final Identifier objIdentifier = (Identifier)request.get(ProgramParams.STRATEGY_PROGRAM_ID.name());
            if (objIdentifier == null || objIdentifier.getId() == null) {
                return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_INVALID_INPUT_002.name(), "Program Id is required"));
            }
            final boolean isRemove = this.performanceProgramManager.removePerformanceProgram(objIdentifier.getId());
            return this.OK(ProgramParams.STATUS.name(), (BaseValueObject)new BooleanResponse(isRemove));
        }
        catch (Exception e) {
            ProgramManagerServiceImpl.logger.error((Object)"Exception in ProgramManagerServiceImpl - removePerformanceProgram() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_DELETE_006.name(), "Unknown error while removing performance program", (Throwable)e));
        }
    }
    
    public Response getProgramZoneByProgramId(final Request request) {
        try {
            final Identifier objIdentifier = (Identifier)request.get(ProgramParams.PROGRAM_ZONE_ID.name());
            if (objIdentifier == null) {
                return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PROG_ZONE_INVALID_INPUT_002.name(), "Program Zone Id is required"));
            }
            final ProgramZoneBean performanceZone = this.performanceProgramManager.getProgramZoneByProgramId(objIdentifier.getId());
            return this.OK(ProgramParams.PROGRAM_ZONE.name(), (BaseValueObject)performanceZone);
        }
        catch (Exception e) {
            ProgramManagerServiceImpl.logger.error((Object)"Exception in ProgramManagerServiceImpl - getProgramZone() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PROG_ZONE_UNABLE_TO_GET_004.name(), "Unknown error while getProgramZoneByProgramId", (Throwable)e));
        }
    }
    
    public Response addOrgUnitToProgramZone(final Request request) {
        ProgramZoneBean programZoneBean = (ProgramZoneBean)request.get(ProgramParams.PROGRAM_ZONE.name());
        if (programZoneBean == null) {
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PROG_ZONE_INVALID_INPUT_002.name(), "Data not found"));
        }
        try {
            programZoneBean = this.performanceProgramManager.addOrgUnitToProgramZone(programZoneBean);
            final Identifier programZoneId = new Identifier(programZoneBean.getId());
            return this.OK(ProgramParams.PROGRAM_ZONE.name(), (BaseValueObject)programZoneId);
        }
        catch (Exception e) {
            ProgramManagerServiceImpl.logger.error((Object)"Exception in ProgramManagerServiceImpl - addOrgUnitToProgramZone() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PROG_ZONE_UNABLE_TO_ADD_ORG_009.name(), "Unknown error while adding org unit to program zone", (Throwable)e));
        }
    }
    
    public Response getAllProgram(final Request request) {
        final SortList sortList = request.getSortList();
        try {
            final List<StrategyProgramBean> alStrategyProgramBean = this.performanceProgramManager.getAllProgram(sortList);
            final BaseValueObjectList alBaseValueObjectList = new BaseValueObjectList();
            alBaseValueObjectList.setValueObjectList((List)alStrategyProgramBean);
            final Response response = this.OK(ProgramParams.STRATEGY_PROGRAM_LIST.name(), (BaseValueObject)alBaseValueObjectList);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            ProgramManagerServiceImpl.logger.error((Object)"Exception in ProgramManagerServiceImpl - getAllProgram() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_GET_ALL_007.name(), "Unknown error while fetching all programs", (Throwable)e));
        }
    }
    
    public Response searchProgram(final Request request) {
        final StrategyProgramBean strProgram = (StrategyProgramBean)request.get(ProgramParams.STRATEGY_PROGRAM.name());
        final SortList sortList = request.getSortList();
        BaseValueObjectList list = null;
        if (strProgram == null) {
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<StrategyProgramBean> strPrgList = this.performanceProgramManager.searchProgram(strProgram, sortList);
            list = new BaseValueObjectList();
            list.setValueObjectList((List)strPrgList);
            final Response response = this.OK(ProgramParams.STRATEGY_PROGRAM_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            ProgramManagerServiceImpl.logger.error((Object)"Exception in ProgramManagerServiceImpl - searchProgram() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_SEARCH_008.name(), "Unknown error while searchProgram", (Throwable)e));
        }
    }
    
    public Response getProgramsByPeriodType(final Request request) {
        final Identifier id = (Identifier)request.get(ProgramParams.PERIOD_TYPE_ID.name());
        final SortList sortList = request.getSortList();
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_INVALID_INPUT_002.name(), "Program Id is required"));
        }
        try {
            BaseValueObjectList programList = new BaseValueObjectList();
            programList = this.performanceProgramManager.getProgramsByPeriodType(id, sortList);
            final Response response = this.OK(ProgramParams.STRATEGY_PROGRAM_LIST.name(), (BaseValueObject)programList);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            ProgramManagerServiceImpl.logger.error((Object)"Exception in ProgramManagerServiceImpl - getProgramZone() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_GET_BY_PRD_TYPE_009.name(), "Unknown error while fetching program zone", (Throwable)e));
        }
    }
    
    public Response getValidationRules(final Request request) {
        final Identifier progId = (Identifier)request.get(ProgramParams.STRATEGY_PROGRAM_ID.name());
        Identifier empId = (Identifier)request.get(ProgramParams.EMP_ID.name());
        Identifier positionId = (Identifier)request.get(ProgramParams.POSITION_ID.name());
        Identifier gradeId = (Identifier)request.get(ProgramParams.GRADE_ID.name());
        Identifier orgId = (Identifier)request.get(ProgramParams.ORG_ID.name());
        if (progId == null || progId.getId() == null || progId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PROG_POLICY_RULE_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        if (empId == null || empId.getId() == null || empId.getId() <= 0) {
            empId = new Identifier((Integer)null);
        }
        if (positionId == null || positionId.getId() == null || positionId.getId() <= 0) {
            positionId = new Identifier((Integer)null);
        }
        if (gradeId == null || gradeId.getId() == null || gradeId.getId() <= 0) {
            gradeId = new Identifier((Integer)null);
        }
        if (orgId == null || orgId.getId() == null || orgId.getId() <= 0) {
            orgId = new Identifier((Integer)null);
        }
        try {
            final PerformanceProgram program = this.performanceProgramManager.getPerformanceProgram(progId.getId());
            final Map<String, String> validationRules = this.performanceProgramManager.getValidationRules(program.getStrategyProgramBean(), empId.getId(), positionId.getId(), gradeId.getId(), orgId.getId());
            final Map<StringIdentifier, StringIdentifier> result = new HashMap<StringIdentifier, StringIdentifier>();
            if (validationRules != null && !validationRules.isEmpty()) {
                for (final Map.Entry<String, String> e : validationRules.entrySet()) {
                    result.put(new StringIdentifier((String)e.getKey()), new StringIdentifier((String)e.getValue()));
                }
            }
            final BaseValueObjectMap map = new BaseValueObjectMap();
            map.setBaseValueMap((Map)result);
            return this.OK(ProgramParams.MAP_RESULT.name(), (BaseValueObject)map);
        }
        catch (Exception e2) {
            ProgramManagerServiceImpl.logger.error((Object)"Exception in ProgramManagerServiceImpl - getValidationRules() : ", (Throwable)e2);
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PROG_POLICY_RULE_VALIDATE_009.name(), "Unknown error while validating program policy rules", (Throwable)e2));
        }
    }
    
    public Response getWorkflowLevels(final Request request) {
        final Identifier progId = (Identifier)request.get(ProgramParams.STRATEGY_PROGRAM_ID.name());
        final Identifier empId = (Identifier)request.get(ProgramParams.EMP_ID.name());
        final BaseValueObjectMap map = (BaseValueObjectMap)request.get(ProgramParams.MAP_RESULT.name());
        if (progId == null || progId.getId() == null || progId.getId() <= 0 || empId == null || empId.getId() == null || empId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_WORK_FLOW_LEVELS_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final BaseValueObjectMap workflowLevels = this.performanceProgramManager.getWorkflowLevels(map, empId.getId());
            return this.OK(ProgramParams.WORKFLOW_LEVELS.name(), (BaseValueObject)workflowLevels);
        }
        catch (Exception e) {
            ProgramManagerServiceImpl.logger.error((Object)"Exception in ProgramManagerServiceImpl - getWorkflowLevels() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_WORK_FLOW_LEVELS_UNABLE_TO_GET_004.name(), "Unknown error while fetching workflow levels", (Throwable)e));
        }
    }
    
    public Response getProgramsForEmployee(final Request request) {
        Identifier empId = (Identifier)request.get(ProgramParams.EMP_ID.name());
        Identifier orgId = (Identifier)request.get(ProgramParams.ORG_ID.name());
        Identifier grdId = (Identifier)request.get(ProgramParams.GRADE_ID.name());
        Identifier posId = (Identifier)request.get(ProgramParams.POSITION_ID.name());
        IdentifierList idList = null;
        try {
            if (empId == null) {
                empId = new Identifier();
            }
            if (orgId == null) {
                orgId = new Identifier();
            }
            if (grdId == null) {
                grdId = new Identifier();
            }
            if (posId == null) {
                posId = new Identifier();
            }
            final OrganizationManagerService orgService = (OrganizationManagerService)this.serviceLocator.getService("OrganizationManagerServiceImpl");
            if (orgService != null) {
                final List<Integer> ruleBean = this.performanceProgramManager.getProgramsForEmployee(orgId.getId(), posId.getId(), empId.getId(), grdId.getId(), orgService);
                idList = new IdentifierList((List)ruleBean);
            }
            return this.OK(ProgramParams.PROGRAM_POLICY_RULE_LIST.name(), (BaseValueObject)idList);
        }
        catch (Exception e) {
            ProgramManagerServiceImpl.logger.error((Object)"Exception in ProgramManagerServiceImpl - getProgramsForEmployee() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PROG_POLICY_RULE_UNABLE_TO_GET_FOR_EMP_010.name(), "Unknown error while fetching programs by employee", (Throwable)e));
        }
    }
    
    public Response getProgramsByIds(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(ProgramParams.STRATEGY_PROGRAM_ID_LIST.name());
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().size() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_INVALID_INPUT_002.name(), "No Data found"));
        }
        try {
            final List<StrategyProgramBean> prgList = this.performanceProgramManager.getProgramsByIds(idList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)prgList);
            return this.OK(ProgramParams.STRATEGY_PROGRAM_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            ProgramManagerServiceImpl.logger.error((Object)"Exception in ProgramManagerServiceImpl - getProgramsByIds() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_GET_BY_IDS_10.name(), "Unknown error while fetching program objects by given id list", (Throwable)e));
        }
    }
    
    public Response getProgramZoneById(final Request request) {
        try {
            final Identifier identifier = (Identifier)request.get(ProgramParams.PROGRAM_ZONE_ID.name());
            if (identifier == null) {
                return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PROG_ZONE_INVALID_INPUT_002.name(), "Program Zone Id is required"));
            }
            final ProgramZoneBean performanceZone = this.performanceProgramManager.getProgramZoneById(identifier.getId());
            return this.OK(ProgramParams.PROGRAM_ZONE.name(), (BaseValueObject)performanceZone);
        }
        catch (Exception e) {
            ProgramManagerServiceImpl.logger.error((Object)"Exception in ProgramManagerServiceImpl - getProgramZoneById() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PROG_ZONE_UNABLE_TO_GET_004.name(), "Unknown error while getProgramZoneByProgramId", (Throwable)e));
        }
    }
    
    public Response getProgramsByZoneIds(final Request request) {
        try {
            final IdentifierList identifierList = (IdentifierList)request.get(ProgramParams.PROGRAM_ZONE_ID_LIST.name());
            if (identifierList == null) {
                return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_INVALID_INPUT_002.name(), "Program Zone Ids is required"));
            }
            final List<Integer> progIds = this.performanceProgramManager.getProgramsByZoneIds(identifierList.getIdsList());
            return this.OK(ProgramParams.PROG_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)progIds));
        }
        catch (Exception e) {
            ProgramManagerServiceImpl.logger.error((Object)"Exception in ProgramManagerServiceImpl - getProgramsByZoneIds() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_GET_BY_ZONE_IDS_11.name(), "Unknown error while getProgramZoneByProgramId", (Throwable)e));
        }
    }
    
    public Response getProgramDetails(final Request request) {
        try {
            final SystemBaseManagerService systemBaseManagerService = (SystemBaseManagerService)this.serviceLocator.getService("SystemBaseManagerServiceImpl");
            final Identifier identifier = (Identifier)request.get(ProgramParams.STRATEGY_PROGRAM_ID.name());
            if (identifier.getId() == null) {
                throw new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_INVALID_INPUT_002.name(), "Program Id is required");
            }
            final PerformanceProgram objPerformanceProgram = this.performanceProgramManager.getPerformanceProgram(identifier.getId());
            final Integer cascadeType = objPerformanceProgram.getStrategyProgramBean().getCascadeType();
            final Integer modeType = objPerformanceProgram.getStrategyProgramBean().getMode();
            request.put(SysParams.SYS_BASE_ID.name(), (BaseValueObject)new Identifier(cascadeType));
            Response response = systemBaseManagerService.getMasterBaseById(request);
            SystemMasterBaseBean systemMasterBaseBean2 = (SystemMasterBaseBean)response.get(SysParams.SYS_BASE_DATA.name());
            response = this.OK(ProgramParams.CASCADE_TYPE.name(), (BaseValueObject)new StringIdentifier(systemMasterBaseBean2.getValue()));
            request.put(SysParams.SYS_BASE_ID.name(), (BaseValueObject)new Identifier(modeType));
            response = systemBaseManagerService.getMasterBaseById(request);
            systemMasterBaseBean2 = (SystemMasterBaseBean)response.get(SysParams.SYS_BASE_DATA.name());
            response = this.OK(ProgramParams.MODE_TYPE.name(), (BaseValueObject)new StringIdentifier(systemMasterBaseBean2.getValue()));
            return response;
        }
        catch (Exception e) {
            ProgramManagerServiceImpl.logger.error((Object)"Exception in ProgramManagerServiceImpl - getProgramDetails() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_GET_DET_012.name(), "Unknown error while getProgramDetails", (Throwable)e));
        }
    }
    
    static {
        logger = Logger.getLogger((Class)ProgramManagerServiceImpl.class);
    }
}
