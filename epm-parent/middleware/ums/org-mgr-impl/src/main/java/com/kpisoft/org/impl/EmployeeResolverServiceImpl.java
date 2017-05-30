package com.kpisoft.org.impl;

import com.kpisoft.org.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.org.params.*;
import com.canopus.mw.*;
import com.kpisoft.org.domain.*;
import java.util.*;
import com.canopus.mw.dto.*;
import org.slf4j.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ EmployeeResolverService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class EmployeeResolverServiceImpl extends BaseMiddlewareBean implements EmployeeResolverService
{
    @Autowired
    private IServiceLocator serviceLocator;
    private static final Logger log;
    
    public EmployeeResolverServiceImpl() {
        this.serviceLocator = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response getEmployeeBasedOnExpression(final Request request) {
        final Identifier empId = (Identifier)request.get(EmpResolverParams.EMP_ID.name());
        final StringIdentifier expression = (StringIdentifier)request.get(EmpResolverParams.EXPRESSION.name());
        if (empId == null || expression == null) {
            return this.ERROR((Exception)new MiddlewareException("Invalid_Input", "No data object in the request"));
        }
        try {
            final Integer employeeId = empId.getId();
            final String expressionValue = expression.getId();
            final EmployeeResolver resolver = new EmployeeResolver(employeeId, this.serviceLocator);
            final Object result = resolver.resolve(expressionValue);
            if (result == null) {
                return this.ERROR((Exception)new MiddlewareException("RESOLVER-101", "Resolver returned null value"));
            }
            if (result instanceof Integer) {
                return this.OK(EmpResolverParams.USER_ID.name(), (BaseValueObject)new Identifier((Integer)result));
            }
            if (result instanceof List) {
                return this.OK(EmpResolverParams.USER_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)result));
            }
        }
        catch (Exception e) {
            EmployeeResolverServiceImpl.log.error("Exception in EmployeeResolverServiceImpl - getEmployeeBasedOnExpression() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("RESOLVER-001", e.getMessage()));
        }
        return this.ERROR((Exception)new MiddlewareException("RESOLVER-101", "Resolver returned null value"));
    }
    
    static {
        log = LoggerFactory.getLogger((Class)EmployeeResolverServiceImpl.class);
    }
}
