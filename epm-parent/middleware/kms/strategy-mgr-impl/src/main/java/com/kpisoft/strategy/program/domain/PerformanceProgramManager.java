package com.kpisoft.strategy.program.domain;

import org.springframework.stereotype.*;
import com.kpisoft.strategy.profram.dac.*;
import com.kpisoft.emp.dac.*;
import javax.validation.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.strategy.utility.*;
import com.canopus.mw.*;
import com.kpisoft.strategy.params.*;
import com.kpisoft.emp.vo.param.*;
import com.kpisoft.emp.vo.*;
import com.kpisoft.org.*;
import com.kpisoft.org.vo.*;
import java.util.*;
import com.canopus.mw.dto.*;
import com.kpisoft.strategy.program.vo.*;
import org.slf4j.*;

@Component
public class PerformanceProgramManager extends BaseDomainManager implements CacheLoader<Integer, PerformanceProgram>
{
    private static final Logger logger;
    @Autowired
    public ProgramDataService programDataService;
    @Autowired
    public EmployeeDataService employeeDataService;
    @Autowired
    private Validator validator;
    @Autowired
    @Qualifier("performanceProgramCache")
    private Cache<Integer, PerformanceProgram> cache;
    @Autowired
    private IServiceLocator serviceLocator;
    
    public PerformanceProgramManager() {
        this.cache = null;
        this.serviceLocator = null;
    }
    
    public PerformanceProgram createPerformanceProgram(final StrategyProgramBean strategyProgramBean) {
        final PerformanceProgram program = new PerformanceProgram(this, this.programDataService);
        program.setStrategyProgramBean(strategyProgramBean);
        try {
            final Integer id = program.savePerformanceProgram(strategyProgramBean);
            program.getStrategyProgramBean().setId(id);
        }
        catch (Exception e) {
            PerformanceProgramManager.logger.error("Exception in PerformanceProgramManager - createPerformanceProgram() : ", (Throwable)e);
            throw new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_CREATE_003.name(), "Error while saving program", (Throwable)e);
        }
        finally {
            if (strategyProgramBean.getId() != null && strategyProgramBean.getId() > 0) {
                this.cache.remove(strategyProgramBean.getId());
            }
        }
        return program;
    }
    
    public PerformanceProgram getPerformanceProgram(final int id) {
        final PerformanceProgram performanceProgram = (PerformanceProgram)this.cache.get(id, (CacheLoader)this);
        return performanceProgram;
    }
    
    public List<StrategyProgramPolicyRuleBean> getProgramRules(final Integer employeeGradeId, final Integer employeeId, final Integer orgPositionId, final Integer orgUnitId) {
        final StrategyProgramPolicyRuleBean objStrategyProgramPolicyRuleBean = new StrategyProgramPolicyRuleBean();
        objStrategyProgramPolicyRuleBean.setEmployeeGradeId(employeeGradeId);
        objStrategyProgramPolicyRuleBean.setEmployeeId(employeeId);
        objStrategyProgramPolicyRuleBean.setOrgPositionId(orgPositionId);
        objStrategyProgramPolicyRuleBean.setOrgUnitId(orgUnitId);
        final Request request = new Request();
        request.put(ProgramParams.PROGRAM_POLICY_RULE.name(), (BaseValueObject)objStrategyProgramPolicyRuleBean);
        final Response response = this.programDataService.getProgramRules(request);
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get(ProgramParams.PROGRAM_POLICY_RULE_LIST.name());
        final List<StrategyProgramPolicyRuleBean> alStrategyProgramPolicyRuleBean = (List<StrategyProgramPolicyRuleBean>)objectList.getValueObjectList();
        return alStrategyProgramPolicyRuleBean;
    }
    
    public boolean removePerformanceProgram(final int id) {
        final PerformanceProgram performanceProgram = new PerformanceProgram(this, this.programDataService);
        return performanceProgram.removePerformanceProgram(id);
    }
    
    public ProgramZoneBean getProgramZoneByProgramId(final int id) {
        final PerformanceProgram program = new PerformanceProgram(this, this.programDataService);
        final ProgramZoneBean objProgramZoneBean = program.getProgramZoneByProgramId(id);
        return objProgramZoneBean;
    }
    
    public ProgramZoneBean addOrgUnitToProgramZone(ProgramZoneBean objProgramZoneBean) {
        final PerformanceProgram program = new PerformanceProgram(this, this.programDataService);
        objProgramZoneBean = program.addOrgUnitToProgramZone(objProgramZoneBean);
        return objProgramZoneBean;
    }
    
    public List<StrategyProgramBean> getAllProgram(SortList sortList) {
        final Request request = new Request();
        request.setSortList(sortList);
        final Response response = this.programDataService.getAllProgram(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(ProgramParams.STRATEGY_PROGRAM_LIST.name());
        final List<StrategyProgramBean> alStrategyProgramBean = (List<StrategyProgramBean>)list.getValueObjectList();
        sortList = response.getSortList();
        return alStrategyProgramBean;
    }
    
    public PerformanceProgram load(final Integer key) {
        final PerformanceProgram performanceProgram = new PerformanceProgram(this, this.programDataService);
        final Identifier identifier = new Identifier();
        identifier.setId(key);
        final Request request = new Request();
        request.put(ProgramParams.STRATEGY_PROGRAM_ID.name(), (BaseValueObject)identifier);
        final Response response = this.programDataService.getPerformanceProgram(request);
        final StrategyProgramBean strategyProgramBean = (StrategyProgramBean)response.get(ProgramParams.STRATEGY_PROGRAM.name());
        performanceProgram.setStrategyProgramBean(strategyProgramBean);
        return performanceProgram;
    }
    
    public List<StrategyProgramBean> searchProgram(final StrategyProgramBean strPrgBean, SortList sortList) {
        List<StrategyProgramBean> strList = null;
        final Request request = new Request();
        request.put(ProgramParams.STRATEGY_PROGRAM.name(), (BaseValueObject)strPrgBean);
        request.setSortList(sortList);
        final Response response = this.programDataService.searchProgram(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(ProgramParams.STRATEGY_PROGRAM_LIST.name());
        sortList = response.getSortList();
        strList = (List<StrategyProgramBean>)list.getValueObjectList();
        return strList;
    }
    
    public BaseValueObjectList getProgramsByPeriodType(final Identifier id, SortList sortList) {
        final Request req = new Request();
        req.setSortList(sortList);
        req.put(ProgramParams.PERIOD_TYPE_ID.name(), (BaseValueObject)id);
        final Response response = this.programDataService.getProgramsByPeriodType(req);
        sortList = response.getSortList();
        return (BaseValueObjectList)response.get(ProgramParams.STRATEGY_PROGRAM_LIST.name());
    }
    
    public Validator getValidator() {
        return this.validator;
    }
    
    public Response getEmployeeRelatedEntities(final Identifier employeeId) {
        final Request request = new Request();
        request.put(EMPParams.EMP_ID.name(), (BaseValueObject)employeeId);
        Response response = this.employeeDataService.getEmployee(request);
        final EmployeeData employeeData = (EmployeeData)response.get(EMPParams.EMP_DATA.name());
        response = new Response();
        if (employeeData != null) {
            response.put(EMPParams.EMP_GRADE_ID.name(), (BaseValueObject)new Identifier(employeeData.getGrade()));
            final List<EmployeePositionData> empPosList = (List<EmployeePositionData>)employeeData.getPosData();
            if (empPosList != null && empPosList.size() > 0) {
                response.put(EMPParams.EMP_POSITION_ID.name(), (BaseValueObject)new Identifier(empPosList.get(0).getPositionId()));
            }
            final List<EmployeeOrgRelationshipData> empOrgList = (List<EmployeeOrgRelationshipData>)employeeData.getEmpOrgRelData();
            if (empOrgList != null && empOrgList.size() > 0) {
                response.put(EMPParams.EMP_ORG_ID.name(), (BaseValueObject)new Identifier(empOrgList.get(0).getOrganizationId()));
            }
        }
        return response;
    }
    
    public List<Integer> getProgramsForEmployee(final Integer orgId, final Integer posId, final Integer empId, final Integer grdId, final OrganizationManagerService orgService) {
        final Request request = new Request();
        request.put(ProgramParams.ORG_ID.name(), (BaseValueObject)new Identifier(orgId));
        request.put(ProgramParams.EMP_ID.name(), (BaseValueObject)new Identifier(empId));
        request.put(ProgramParams.GRADE_ID.name(), (BaseValueObject)new Identifier(grdId));
        request.put(ProgramParams.POSITION_ID.name(), (BaseValueObject)new Identifier(posId));
        final Response response = this.programDataService.getProgramsForEmployee(request);
        final IdentifierList bList = (IdentifierList)response.get(ProgramParams.PROGRAM_POLICY_RULE_LIST.name());
        final List<Integer> inclPrgList = (List<Integer>)bList.getIdsList();
        final List<Integer> excProgIds = this.searchExclusionMetaData(orgId, posId, grdId, empId);
        if (excProgIds != null && excProgIds.size() > 0 && inclPrgList != null && inclPrgList.size() > 0) {
            for (int i = 0; i < excProgIds.size(); ++i) {
                final Integer inclPrgId = inclPrgList.get(i);
                if (excProgIds.contains(inclPrgId)) {
                    inclPrgList.remove(inclPrgId);
                }
            }
        }
        if (inclPrgList.size() == 0 && inclPrgList.isEmpty()) {
            final List<StrategyProgramPolicyRuleBean> ruleBeanList = this.getProgramByOrgId(orgId, orgService, excProgIds);
            if (ruleBeanList != null && ruleBeanList.size() > 0) {
                for (final StrategyProgramPolicyRuleBean bean : ruleBeanList) {
                    inclPrgList.add(bean.getProgramId());
                }
            }
        }
        return inclPrgList;
    }
    
    public List<StrategyProgramPolicyRuleBean> getProgramByOrgId(final Integer orgId, final OrganizationManagerService orgService, final List<Integer> excProgIds) {
        List<StrategyProgramPolicyRuleBean> progList = null;
        boolean isFound = false;
        final List<Integer> recurseList = new ArrayList<Integer>();
        final Request request = new Request();
        request.put(OrganizationParams.ORG_IDENTIFIER.name(), (BaseValueObject)new Identifier(orgId));
        Response response = orgService.getParents(request);
        final IdentifierList parentIds = (IdentifierList)response.get(OrganizationParams.ORG_IDENTIFIER_LIST.name());
        if (parentIds != null && parentIds.getIdsList() != null && !parentIds.getIdsList().isEmpty()) {
            for (final Integer id : parentIds.getIdsList()) {
                recurseList.add(id);
                final Identifier identifier = new Identifier();
                identifier.setId(id);
                request.put(ProgramParams.ORG_ID.name(), (BaseValueObject)identifier);
                response = this.programDataService.searchProgramPolicyRule(request);
                final BaseValueObjectList bList = (BaseValueObjectList)response.get(ProgramParams.PROGRAM_POLICY_RULE_LIST.name());
                progList = (List<StrategyProgramPolicyRuleBean>)bList.getValueObjectList();
                final Iterator<StrategyProgramPolicyRuleBean> iteratorList = progList.iterator();
                if (iteratorList != null && excProgIds != null && excProgIds.size() > 0 && iteratorList.hasNext() && excProgIds.contains(iteratorList.next().getProgramId())) {
                    iteratorList.remove();
                }
                if (progList != null && progList.size() > 0) {
                    isFound = true;
                    break;
                }
            }
        }
        if (!isFound && recurseList.size() > 0) {
            final Iterator<Integer> iterator2 = recurseList.iterator();
            if (iterator2.hasNext()) {
                final Integer i = iterator2.next();
                return this.getProgramByOrgId(i, orgService, excProgIds);
            }
        }
        return progList;
    }
    
    public Map<String, String> getValidationRules(final StrategyProgramBean program, final Integer empId, final Integer positionId, final Integer gradeId, final Integer orgId) {
        final PerformanceProgram performanceProgram = new PerformanceProgram(this, this.programDataService, this.serviceLocator);
        return performanceProgram.getValidationRules(program, empId, positionId, gradeId, orgId);
    }
    
    public BaseValueObjectMap getWorkflowLevels(final BaseValueObjectMap map, final Integer empId) {
        final PerformanceProgram performanceProgram = new PerformanceProgram(this, this.programDataService, this.serviceLocator);
        return performanceProgram.getWorkflowLevels(map, empId);
    }
    
    public List<StrategyProgramBean> getProgramsByIds(final IdentifierList idList) {
        final Request request = new Request();
        request.put(ProgramParams.STRATEGY_PROGRAM_ID_LIST.name(), (BaseValueObject)idList);
        final Response response = this.programDataService.getProgramsByIds(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(ProgramParams.STRATEGY_PROGRAM_LIST.name());
        final List<StrategyProgramBean> prgList = (List<StrategyProgramBean>)list.getValueObjectList();
        return prgList;
    }
    
    public ProgramZoneBean getProgramZoneById(final int id) {
        final PerformanceProgram performanceProgram = new PerformanceProgram(this, this.programDataService);
        final ProgramZoneBean objProgramZoneBean = performanceProgram.getProgramZoneById(id);
        return objProgramZoneBean;
    }
    
    public List<Integer> searchExclusionMetaData(final Integer orgId, final Integer PosId, final Integer grdId, final Integer empId) {
        final ExclusionsBean exclusionsBean = new ExclusionsBean();
        exclusionsBean.setOrgPositionId(PosId);
        exclusionsBean.setEmployeeId(empId);
        exclusionsBean.setEmployeeGradeId(grdId);
        exclusionsBean.setOrgUnitId(orgId);
        final List<Integer> progIds = new ArrayList<Integer>();
        final Request request = new Request();
        request.put(ProgramParams.EXCLUSION_DATA.name(), (BaseValueObject)exclusionsBean);
        final Response response = this.programDataService.searchExclusionMetaData(request);
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(ProgramParams.EXCLUSION_DATA_LIST.name());
        final List<ExclusionsBean> exclusionsBeans = (List<ExclusionsBean>)bList.getValueObjectList();
        for (final ExclusionsBean exclusionsBean2 : exclusionsBeans) {
            progIds.add(exclusionsBean2.getProgramId());
        }
        return progIds;
    }
    
    public List<Integer> getProgramsByZoneIds(final List<Integer> progZoneIds) {
        final Request request = new Request();
        request.put(ProgramParams.PROGRAM_ZONE_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)progZoneIds));
        final Response response = this.programDataService.getProgramsByZoneIds(request);
        final IdentifierList identifierList = (IdentifierList)response.get(ProgramParams.PROG_ID_LIST.name());
        final List<Integer> progIds = (List<Integer>)identifierList.getIdsList();
        return progIds;
    }
    
    static {
        logger = LoggerFactory.getLogger((Class)PerformanceProgramManager.class);
    }
}
