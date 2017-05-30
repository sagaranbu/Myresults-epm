package com.kpisoft.org.domain;

import com.canopus.mw.*;
import com.canopus.mw.spel.*;
import com.kpisoft.emp.*;
import com.kpisoft.emp.vo.param.*;
import com.kpisoft.emp.vo.*;
import java.util.*;
import com.kpisoft.user.*;
import com.kpisoft.user.vo.*;
import com.canopus.mw.dto.*;
import com.kpisoft.user.vo.param.*;
import org.springframework.expression.spel.standard.*;
import org.springframework.expression.spel.support.*;
import org.springframework.expression.*;

public class EmployeeResolver
{
    private Integer employeeId;
    private IServiceLocator serviceLocator;
    private Map<String, IExprFunctoid> functoidMap;
    private FunctoidMethodResolver resolver;
    private static final String ROOT = "root";
    
    public EmployeeResolver(final Integer employeeId, final IServiceLocator serviceLocator) {
        this.employeeId = null;
        this.serviceLocator = null;
        this.functoidMap = new HashMap<String, IExprFunctoid>();
        this.resolver = new FunctoidMethodResolver((Map)this.functoidMap);
        this.employeeId = employeeId;
        this.serviceLocator = serviceLocator;
    }
    
    public void addFunctoid(final IExprFunctoid functoid) {
        this.functoidMap.put(functoid.getFunctionName(), functoid);
    }
    
    public void removeFunctoid(final IExprFunctoid functoid) {
        if (this.functoidMap.containsKey(functoid.getFunctionName())) {
            this.functoidMap.remove(functoid);
        }
    }
    
    public List<Integer> sup() {
        final EmployeeManagerService empService = (EmployeeManagerService)this.serviceLocator.getService("EmployeeManagerServiceImpl");
        final List<Integer> empIds = new ArrayList<Integer>();
        final Request request = new Request();
        request.put(EMPParams.EMP_ID.name(), (BaseValueObject)new Identifier(this.employeeId));
        final Response response = empService.getSupervisors(request);
        final BaseValueObjectList result = (BaseValueObjectList)response.get(EMPParams.BASEVALUE_LIST.name());
        if (result != null && result.getValueObjectList() != null && !result.getValueObjectList().isEmpty()) {
            final List<EmployeeIdentity> emps = (List<EmployeeIdentity>)result.getValueObjectList();
            for (final EmployeeIdentity iterator : emps) {
                empIds.add(iterator.getId());
            }
        }
        return empIds;
    }
    
    public List<Integer> sup(final Integer employeeId) {
        final EmployeeManagerService empService = (EmployeeManagerService)this.serviceLocator.getService("EmployeeManagerServiceImpl");
        final List<Integer> empIds = new ArrayList<Integer>();
        final Request request = new Request();
        request.put(EMPParams.EMP_ID.name(), (BaseValueObject)new Identifier(employeeId));
        final Response response = empService.getSupervisors(request);
        final BaseValueObjectList result = (BaseValueObjectList)response.get(EMPParams.BASEVALUE_LIST.name());
        if (result != null && result.getValueObjectList() != null && !result.getValueObjectList().isEmpty()) {
            final List<EmployeeIdentity> emps = (List<EmployeeIdentity>)result.getValueObjectList();
            for (final EmployeeIdentity iterator : emps) {
                empIds.add(iterator.getId());
            }
        }
        return empIds;
    }
    
    public Integer psup() {
        final EmployeeManagerService empService = (EmployeeManagerService)this.serviceLocator.getService("EmployeeManagerServiceImpl");
        final Request request = new Request();
        request.put(EMPParams.EMP_ID.name(), (BaseValueObject)new Identifier(this.employeeId));
        final Response response = empService.getPrimarySupervisor(request);
        final EmployeeIdentity employee = (EmployeeIdentity)response.get(EMPParams.EMP_DATA.name());
        if (employee != null) {
            return employee.getId();
        }
        return null;
    }
    
    public Integer psup(final Integer employeeId) {
        final EmployeeManagerService empService = (EmployeeManagerService)this.serviceLocator.getService("EmployeeManagerServiceImpl");
        final Request request = new Request();
        request.put(EMPParams.EMP_ID.name(), (BaseValueObject)new Identifier(employeeId));
        final Response response = empService.getPrimarySupervisor(request);
        final EmployeeIdentity employee = (EmployeeIdentity)response.get(EMPParams.EMP_DATA.name());
        if (employee != null) {
            return employee.getId();
        }
        return null;
    }
    
    public Integer pos(final Integer positionId) {
        final EmployeeManagerService empService = (EmployeeManagerService)this.serviceLocator.getService("EmployeeManagerServiceImpl");
        final Integer empId = this.positionRecursion(this.employeeId, positionId, empService);
        return empId;
    }
    
    public Integer pos(final Integer positionId, final Integer orgUnitId) {
        final EmployeeManagerService empService = (EmployeeManagerService)this.serviceLocator.getService("EmployeeManagerServiceImpl");
        final Integer empId = this.positionRecursion(this.employeeId, positionId, orgUnitId, empService);
        return empId;
    }
    
    private Integer positionRecursion(final Integer employeeId, final Integer positionId, final EmployeeManagerService empService) {
        Request request = new Request();
        request.put(EMPParams.EMP_ID.name(), (BaseValueObject)new Identifier(employeeId));
        Response response = empService.getPrimarySupervisor(request);
        final EmployeeIdentity employee = (EmployeeIdentity)response.get(EMPParams.EMP_DATA.name());
        if (employee != null && employee.getId() != null) {
            request = new Request();
            request.put(EMPParams.EMP_ID.name(), (BaseValueObject)new Identifier(employee.getId()));
            response = empService.getEmployeePositions(request);
            final IdentifierList posIds = (IdentifierList)response.get(EMPParams.EMP_POSITION_ID_LIST.name());
            if (posIds != null && posIds.getIdsList() != null && !posIds.getIdsList().isEmpty()) {
                for (final Integer pos : posIds.getIdsList()) {
                    if (positionId == (int)pos) {
                        return employee.getId();
                    }
                }
            }
            final Integer empId = this.positionRecursion(employee.getId(), positionId, empService);
            if (empId != null) {
                return empId;
            }
        }
        return null;
    }
    
    private Integer positionRecursion(final Integer employeeId, final Integer positionId, final Integer orgUnitId, final EmployeeManagerService empService) {
        Request request = new Request();
        request.put(EMPParams.EMP_ID.name(), (BaseValueObject)new Identifier(employeeId));
        Response response = empService.getPrimarySupervisor(request);
        final EmployeeIdentity employee = (EmployeeIdentity)response.get(EMPParams.EMP_DATA.name());
        if (employee != null && employee.getId() != null) {
            boolean inOrgUnit = false;
            request = new Request();
            request.put(EMPParams.EMP_ID.name(), (BaseValueObject)new Identifier(employee.getId()));
            response = empService.getEmployeeOrganizations(request);
            final IdentifierList orgIds = (IdentifierList)response.get(EMPParams.EMP_ID_LIST.name());
            if (orgIds != null && orgIds.getIdsList() != null && !orgIds.getIdsList().isEmpty()) {
                for (final Integer iterator : orgIds.getIdsList()) {
                    if (orgUnitId == (int)iterator) {
                        inOrgUnit = true;
                        break;
                    }
                }
            }
            if (inOrgUnit) {
                request = new Request();
                request.put(EMPParams.EMP_ID.name(), (BaseValueObject)new Identifier(employee.getId()));
                response = empService.getEmployeePositions(request);
                final IdentifierList posIds = (IdentifierList)response.get(EMPParams.EMP_POSITION_ID_LIST.name());
                if (posIds != null && posIds.getIdsList() != null && !posIds.getIdsList().isEmpty()) {
                    for (final Integer pos : posIds.getIdsList()) {
                        if (positionId == (int)pos) {
                            return employee.getId();
                        }
                    }
                }
            }
            final Integer empId = this.positionRecursion(employee.getId(), positionId, orgUnitId, empService);
            if (empId != null) {
                return empId;
            }
        }
        return null;
    }
    
    public Integer empid() {
        return this.employeeId;
    }
    
    public Integer empid(final Integer employeeId) {
        return employeeId;
    }
    
    public Integer userid(final Integer userId) {
        return userId;
    }
    
    public Integer orgid(final Integer orgId) {
        return orgId;
    }
    
    public Integer user(final Integer employeeId) {
        final UserManagerService userService = (UserManagerService)this.serviceLocator.getService("UserManagerServiceImpl");
        if (employeeId != null) {
            final UserData userData = new UserData();
            userData.setEmployeeId(employeeId);
            final SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setBaseValueObject((BaseValueObject)userData);
            final Request request = new Request();
            request.put(UMSParams.SEARCH_CRITERIA.name(), (BaseValueObject)searchCriteria);
            final Response response = userService.search(request);
            final BaseValueObjectList list = (BaseValueObjectList)response.get(UMSParams.USER_DATA_LIST.name());
            if (list != null && list.getValueObjectList() != null && !list.getValueObjectList().isEmpty()) {
                final List<UserData> users = (List<UserData>)list.getValueObjectList();
                return users.get(0).getId();
            }
        }
        return null;
    }
    
    public List<Integer> user(final List<Integer> employeeIds) {
        final UserManagerService userService = (UserManagerService)this.serviceLocator.getService("UserManagerServiceImpl");
        final List<Integer> userIds = new ArrayList<Integer>();
        if (employeeIds != null && !employeeIds.isEmpty()) {
            for (final Integer iterator : employeeIds) {
                final UserData userData = new UserData();
                userData.setEmployeeId(iterator);
                final SearchCriteria searchCriteria = new SearchCriteria();
                searchCriteria.setBaseValueObject((BaseValueObject)userData);
                final Request request = new Request();
                request.put(UMSParams.SEARCH_CRITERIA.name(), (BaseValueObject)searchCriteria);
                final Response response = userService.search(request);
                final BaseValueObjectList list = (BaseValueObjectList)response.get(UMSParams.USER_DATA_LIST.name());
                if (list != null && list.getValueObjectList() != null && !list.getValueObjectList().isEmpty()) {
                    final List<UserData> users = (List<UserData>)list.getValueObjectList();
                    userIds.add(users.get(0).getId());
                }
            }
            return userIds;
        }
        return null;
    }
    
    public Object resolve(String expression) {
        expression = expression.toLowerCase();
        final ExpressionParser parser = (ExpressionParser)new SpelExpressionParser();
        final Expression expr = parser.parseExpression(expression);
        final StandardEvaluationContext context = new StandardEvaluationContext((Object)this);
        context.setVariable("root", (Object)this);
        final ReflectiveMethodResolver reflectiveMethodResolver = new ReflectiveMethodResolver();
        context.addMethodResolver((MethodResolver)reflectiveMethodResolver);
        context.addMethodResolver((MethodResolver)this.resolver);
        return expr.getValue((EvaluationContext)context);
    }
}
