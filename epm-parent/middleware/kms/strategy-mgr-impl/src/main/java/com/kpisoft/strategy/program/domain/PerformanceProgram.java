package com.kpisoft.strategy.program.domain;

import com.kpisoft.strategy.profram.dac.*;
import com.kpisoft.strategy.params.*;
import com.kpisoft.strategy.program.vo.*;
import com.kpisoft.strategy.utility.*;
import com.canopus.mw.*;
import com.canopus.entity.vo.*;
import com.kpisoft.org.vo.*;
import com.canopus.entity.*;
import com.canopus.entity.vo.params.*;
import java.util.*;
import com.kpisoft.org.*;
import com.kpisoft.user.*;
import com.kpisoft.org.params.*;
import com.kpisoft.user.vo.*;
import com.canopus.mw.dto.*;
import com.kpisoft.user.vo.param.*;
import com.canopus.mw.utils.*;
import javax.validation.*;

public class PerformanceProgram extends BaseDomainObject
{
    private PerformanceProgramManager performanceProgramManager;
    private ProgramDataService programDataService;
    private StrategyProgramBean strategyProgramBean;
    private ProgramZoneBean programZoneBean;
    private StrategyProgramPolicyRuleBean strategyProgramPolicyRuleBean;
    private IServiceLocator serviceLocator;
    
    public PerformanceProgram(final PerformanceProgramManager objPerformanceProgramManager, final ProgramDataService objProgramDataService) {
        this.performanceProgramManager = null;
        this.programDataService = null;
        this.strategyProgramBean = null;
        this.programZoneBean = null;
        this.strategyProgramPolicyRuleBean = null;
        this.serviceLocator = null;
        this.performanceProgramManager = objPerformanceProgramManager;
        this.programDataService = objProgramDataService;
    }
    
    public PerformanceProgram(final PerformanceProgramManager objPerformanceProgramManager, final ProgramDataService objProgramDataService, final IServiceLocator serviceLocator) {
        this.performanceProgramManager = null;
        this.programDataService = null;
        this.strategyProgramBean = null;
        this.programZoneBean = null;
        this.strategyProgramPolicyRuleBean = null;
        this.serviceLocator = null;
        this.performanceProgramManager = objPerformanceProgramManager;
        this.programDataService = objProgramDataService;
        this.serviceLocator = serviceLocator;
    }
    
    public Integer savePerformanceProgram(StrategyProgramBean objStrategyProgramBean) {
        this.validate(objStrategyProgramBean);
        final Request request = new Request();
        request.put(ProgramParams.STRATEGY_PROGRAM.name(), (BaseValueObject)objStrategyProgramBean);
        final Response response = this.programDataService.savePerformanceProgram(request);
        objStrategyProgramBean = (StrategyProgramBean)response.get(ProgramParams.STRATEGY_PROGRAM.name());
        return objStrategyProgramBean.getId();
    }
    
    public boolean removePerformanceProgram(final int id) {
        final Identifier objIdentifier = new Identifier();
        objIdentifier.setId(id);
        final Request request = new Request();
        request.put(ProgramParams.STRATEGY_PROGRAM_ID.name(), (BaseValueObject)objIdentifier);
        final Response response = this.programDataService.removePerformanceProgram(request);
        final BooleanResponse bResponse = (BooleanResponse)response.get(ProgramParams.STATUS.name());
        return bResponse.isResponse();
    }
    
    public ProgramZoneBean getProgramZoneByProgramId(final int id) {
        final Identifier objIdentifier = new Identifier();
        objIdentifier.setId(id);
        final Request request = new Request();
        request.put(ProgramParams.PROGRAM_ZONE_ID.name(), (BaseValueObject)objIdentifier);
        final Response response = this.programDataService.getProgramZoneByProgramId(request);
        return this.programZoneBean = (ProgramZoneBean)response.get(ProgramParams.PROGRAM_ZONE.name());
    }
    
    public ProgramZoneBean addOrgUnitToProgramZone(ProgramZoneBean objProgramZoneBean) {
        final Request request = new Request();
        request.put(ProgramParams.PROGRAM_ZONE.name(), (BaseValueObject)objProgramZoneBean);
        final Response response = this.programDataService.addOrgUnitToProgramZone(request);
        objProgramZoneBean = (ProgramZoneBean)response.get(ProgramParams.PROGRAM_ZONE.name());
        return objProgramZoneBean;
    }
    
    public Map<String, String> getValidationRules(final StrategyProgramBean program, final Integer empId, final Integer positionId, final Integer gradeId, final Integer orgId) {
        final Map<String, String> validationRules = new HashMap<String, String>();
        List<KpiRuleBean> kpiRuleList = null;
        final Map<String, List<KpiRuleBean>> validationRulesMap = new HashMap<String, List<KpiRuleBean>>();
        final Map<String, List<KpiRuleBean>> orgRulesMap = new HashMap<String, List<KpiRuleBean>>();
        final List<StrategyProgramPolicyRuleBean> rules = (List<StrategyProgramPolicyRuleBean>)program.getStrategyProgramPolicyRuleMetaData();
        if (program.getExclusionsMetaData() != null && !program.getExclusionsMetaData().isEmpty()) {
            for (final ExclusionsBean iterator : program.getExclusionsMetaData()) {
                if (iterator.getEmployeeId() != null && empId != null && iterator.getEmployeeId() == (int)empId) {
                    return null;
                }
                if (iterator.getOrgPositionId() != null && positionId != null && iterator.getOrgPositionId() == (int)positionId) {
                    return null;
                }
                if (iterator.getEmployeeGradeId() != null && gradeId != null && iterator.getEmployeeGradeId() == (int)gradeId) {
                    return null;
                }
                if (iterator.getOrgUnitId() != null && orgId != null && iterator.getOrgUnitId() == (int)orgId) {
                    return null;
                }
            }
        }
        for (final StrategyProgramPolicyRuleBean iterator2 : rules) {
            if (iterator2.getEmployeeId() != null && empId != null && iterator2.getEmployeeId() == (int)empId) {
                validationRulesMap.put("Emp", iterator2.getKpiRuleConfigMetaData());
            }
            else if (iterator2.getOrgPositionId() != null && orgId != null && iterator2.getOrgPositionId() == (int)positionId) {
                validationRulesMap.put("Pos", iterator2.getKpiRuleConfigMetaData());
            }
            else if (iterator2.getEmployeeGradeId() != null && gradeId != null && iterator2.getEmployeeGradeId() == (int)gradeId) {
                validationRulesMap.put("Grade", iterator2.getKpiRuleConfigMetaData());
            }
            else if (iterator2.getOrgUnitId() != null && orgId != null && iterator2.getOrgUnitId() == (int)orgId) {
                validationRulesMap.put("Org", iterator2.getKpiRuleConfigMetaData());
            }
            else if (iterator2.getOrgUnitId() != null && iterator2.getOrgUnitId() > 0) {
                orgRulesMap.put(iterator2.getOrgUnitId() + "", iterator2.getKpiRuleConfigMetaData());
            }
            else {
                if (iterator2.getEmployeeId() != null || iterator2.getEmployeeGradeId() != null || iterator2.getOrgPositionId() != null || iterator2.getOrgUnitId() != null) {
                    continue;
                }
                validationRulesMap.put("Config", iterator2.getKpiRuleConfigMetaData());
            }
        }
        if (validationRulesMap.containsKey("Emp")) {
            kpiRuleList = validationRulesMap.get("Emp");
        }
        else if (validationRulesMap.containsKey("Grade")) {
            kpiRuleList = validationRulesMap.get("Grade");
        }
        else if (validationRulesMap.containsKey("Pos")) {
            kpiRuleList = validationRulesMap.get("Pos");
        }
        else if (validationRulesMap.containsKey("Org")) {
            kpiRuleList = validationRulesMap.get("Org");
        }
        if (!orgRulesMap.isEmpty() && orgId != null && (kpiRuleList == null || kpiRuleList.isEmpty())) {
            final OrganizationManagerService orgService = (OrganizationManagerService)this.serviceLocator.getService("OrganizationManagerServiceImpl");
            kpiRuleList = this.recursive(orgId, orgService, orgRulesMap);
        }
        if ((kpiRuleList == null || kpiRuleList.isEmpty()) && validationRulesMap.containsKey("Config")) {
            kpiRuleList = validationRulesMap.get("Config");
        }
        String sysMasterBaseIds = "";
        final Map<String, String> newValidationRules = new HashMap<String, String>();
        if (kpiRuleList != null && !kpiRuleList.isEmpty()) {
            for (final KpiRuleBean rule : kpiRuleList) {
                validationRules.put(rule.getSystemMasterBaseData().getId() + "", rule.getValue());
                sysMasterBaseIds = sysMasterBaseIds + rule.getSystemMasterBaseData().getId() + ",";
            }
            if (sysMasterBaseIds == null || sysMasterBaseIds.equals("") || sysMasterBaseIds.length() <= 0) {
                throw new MiddlewareException(StrategyErrorCodesEnum.ERR_PROG_POLICY_RULE_NO_KPI_CONFIG_RULES_011.name(), "Default kpi config rules not defined");
            }
            sysMasterBaseIds = sysMasterBaseIds.substring(0, sysMasterBaseIds.length() - 1);
            final List<SystemMasterBaseBean> sysMstrList = this.getSysMasterByIds(sysMasterBaseIds);
            for (final SystemMasterBaseBean sysMstrBean : sysMstrList) {
                if (validationRules.containsKey(sysMstrBean.getId() + "")) {
                    newValidationRules.put(sysMstrBean.getValue(), validationRules.get(sysMstrBean.getId() + ""));
                }
            }
        }
        return newValidationRules;
    }
    
    public List<KpiRuleBean> recursive(final Integer orgId, final OrganizationManagerService orgService, final Map<String, List<KpiRuleBean>> orgRulesMap) {
        final Request request = new Request();
        request.put(OrganizationParams.ORG_IDENTIFIER.name(), (BaseValueObject)new Identifier(orgId));
        final Response response = orgService.getParents(request);
        final IdentifierList parentIds = (IdentifierList)response.get(OrganizationParams.ORG_IDENTIFIER_LIST.name());
        if (parentIds != null && parentIds.getIdsList() != null && !parentIds.getIdsList().isEmpty()) {
            for (final Integer id : parentIds.getIdsList()) {
                if (orgRulesMap.containsKey(id + "")) {
                    final List<KpiRuleBean> kpiRuleList = orgRulesMap.get(id + "");
                    return kpiRuleList;
                }
            }
            for (final Integer id : parentIds.getIdsList()) {
                final List<KpiRuleBean> kpiRuleList = this.recursive(id, orgService, orgRulesMap);
                if (kpiRuleList != null) {
                    return kpiRuleList;
                }
            }
        }
        return null;
    }
    
    public List<SystemMasterBaseBean> getSysMasterByIds(final String ids) {
        final SystemBaseManagerService sysMasterManagerService = (SystemBaseManagerService)this.serviceLocator.getService("SystemBaseManagerServiceImpl");
        final Request request = new Request();
        request.put(SysParams.SYS_BASE_IDS.name(), (BaseValueObject)new StringIdentifier(ids));
        final Response response = sysMasterManagerService.getSysMasterByIds(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(SysParams.SYS_BASE_DATA_LIST.name());
        List<SystemMasterBaseBean> sysMstrList = new ArrayList<SystemMasterBaseBean>();
        if (list != null && list.getValueObjectList() != null) {
            sysMstrList = (List<SystemMasterBaseBean>)list.getValueObjectList();
        }
        return sysMstrList;
    }
    
    public BaseValueObjectMap getWorkflowLevels(final BaseValueObjectMap map, final Integer empId) {
        String name = null;
        String expression = null;
        Map<String, String> levels = null;
        if (map != null && map.getBaseValueMap() != null && !map.getBaseValueMap().isEmpty()) {
            final Map<StringIdentifier, StringIdentifier> valRulesMap = (Map<StringIdentifier, StringIdentifier>)map.getBaseValueMap();
            for (final Map.Entry<StringIdentifier, StringIdentifier> entry : valRulesMap.entrySet()) {
                if (entry.getKey() != null && entry.getKey().getId() != null && entry.getKey().getId().equalsIgnoreCase(ProgramParams.NAME.name())) {
                    if (entry.getValue() == null) {
                        continue;
                    }
                    name = entry.getValue().getId();
                }
                else {
                    if (entry.getKey() == null || entry.getKey().getId() == null || !entry.getKey().getId().equalsIgnoreCase(ProgramParams.EXPRESSION.name()) || entry.getValue() == null) {
                        continue;
                    }
                    expression = entry.getValue().getId();
                }
            }
            if (name != null && expression != null) {
                levels = new LinkedHashMap<String, String>();
                final String[] nameArr = name.split("\\~");
                final String[] expressionArr = expression.split("\\~");
                for (int i = 0; i < nameArr.length; ++i) {
                    levels.put(nameArr[i].trim(), expressionArr[i].trim());
                }
            }
        }
        final BaseValueObjectMap bvom = this.resolveExpressions(levels, empId);
        return bvom;
    }
    
    public BaseValueObjectMap resolveExpressions(final Map<String, String> levels, final Integer empId) {
        final EmployeeResolverService empResolverService = (EmployeeResolverService)this.serviceLocator.getService("EmployeeResolverServiceImpl");
        final UserManagerService userManagerService = (UserManagerService)this.serviceLocator.getService("UserManagerServiceImpl");
        final Map<String, Object> empResolver = new LinkedHashMap<String, Object>();
        if (levels != null && !levels.isEmpty()) {
            for (final String key : levels.keySet()) {
                final Request req = new Request();
                req.put(EmpResolverParams.EMP_ID.name(), (BaseValueObject)new Identifier(empId));
                req.put(EmpResolverParams.EXPRESSION.name(), (BaseValueObject)new StringIdentifier((String)levels.get(key)));
                final Response response = empResolverService.getEmployeeBasedOnExpression(req);
                empResolver.put(key, response);
            }
        }
        final Integer userId = this.getUserStringIdentifier(empId, userManagerService);
        List<Integer> userIdList = new ArrayList<Integer>();
        userIdList.add(userId);
        IdentifierList list = new IdentifierList((List)userIdList);
        final Map<StringIdentifier, IdentifierList> baseMap = new LinkedHashMap<StringIdentifier, IdentifierList>();
        baseMap.put(new StringIdentifier(ProgramParams.SUBMIT.name()), list);
        for (final String key2 : empResolver.keySet()) {
            final Response response2 = (Response) empResolver.get(key2);
            userIdList = new ArrayList<Integer>();
            if (response2.get(EmpResolverParams.USER_ID.name()) != null) {
                final Identifier id = (Identifier)response2.get(EmpResolverParams.USER_ID.name());
                userIdList.add(id.getId());
            }
            else if (response2.get(EmpResolverParams.USER_ID_LIST.name()) != null) {
                final IdentifierList listTemp = (IdentifierList)response2.get(EmpResolverParams.USER_ID_LIST.name());
                for (final Integer iterator : listTemp.getIdsList()) {
                    userIdList.add(iterator);
                }
            }
            list = new IdentifierList((List)userIdList);
            baseMap.put(new StringIdentifier(key2), list);
        }
        final BaseValueObjectMap bvom = new BaseValueObjectMap();
        bvom.setBaseValueMap((Map)baseMap);
        return bvom;
    }
    
    private Integer getUserStringIdentifier(final Integer employeeId, final UserManagerService userManagerService) {
        Integer userId = null;
        UserData userData = new UserData();
        userData.setEmployeeId(employeeId);
        final SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setBaseValueObject((BaseValueObject)userData);
        final Request request = new Request();
        request.put(UMSParams.SEARCH_CRITERIA.name(), (BaseValueObject)searchCriteria);
        final Response response = userManagerService.search(request);
        final BaseValueObjectList bValObjList = (BaseValueObjectList) response.getResponseValueObjects().get(UMSParams.USER_DATA_LIST.name());
        if (bValObjList != null && bValObjList.getValueObjectList() != null && bValObjList.getValueObjectList().size() > 0) {
            userData = (UserData) bValObjList.getValueObjectList().get(0);
            userId = userData.getId();
        }
        return userId;
    }
    
    public ProgramZoneBean getProgramZoneById(final int id) {
        final Identifier objIdentifier = new Identifier();
        objIdentifier.setId(id);
        final Request request = new Request();
        request.put(ProgramParams.PROGRAM_ZONE_ID.name(), (BaseValueObject)objIdentifier);
        final Response response = this.programDataService.getProgramZoneById(request);
        return this.programZoneBean = (ProgramZoneBean)response.get(ProgramParams.PROGRAM_ZONE.name());
    }
    
    public void validate(final Object obj) {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), obj, "ERR_VAL_INVALID_INPUT", "Invalid data input");
    }
    
    private Validator getValidator() {
        return this.performanceProgramManager.getValidator();
    }
    
    public StrategyProgramBean getStrategyProgramBean() {
        return this.strategyProgramBean;
    }
    
    public void setStrategyProgramBean(final StrategyProgramBean strategyProgramBean) {
        this.strategyProgramBean = strategyProgramBean;
    }
    
    public ProgramZoneBean getProgramZoneBean() {
        return this.programZoneBean;
    }
    
    public void setProgramZoneBean(final ProgramZoneBean programZoneBean) {
        this.programZoneBean = programZoneBean;
    }
    
    public StrategyProgramPolicyRuleBean getStrategyProgramPolicyRuleBean() {
        return this.strategyProgramPolicyRuleBean;
    }
    
    public void setStrategyProgramPolicyRuleBean(final StrategyProgramPolicyRuleBean strategyProgramPolicyRuleBean) {
        this.strategyProgramPolicyRuleBean = strategyProgramPolicyRuleBean;
    }
}
