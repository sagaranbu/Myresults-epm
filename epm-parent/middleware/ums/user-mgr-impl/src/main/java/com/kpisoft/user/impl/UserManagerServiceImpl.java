package com.kpisoft.user.impl;

import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import com.kpisoft.user.impl.domain.session.stats.*;
import com.canopus.mw.utils.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.user.utility.*;
import com.canopus.mw.*;
import com.kpisoft.user.*;
import com.kpisoft.user.vo.param.*;
import com.kpisoft.user.impl.domain.*;
import java.util.*;
import com.kpisoft.user.vo.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.dto.param.*;
import org.slf4j.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ UserManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class UserManagerServiceImpl extends BaseMiddlewareBean implements UserManagerService
{
    public static String USER_MGR_SERVICE;
    @Autowired
    private UserManager userManager;
    @Autowired
    private OperationManager operManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private SessionStatsManager statsManager;
    @Autowired
    private EncryptionHelper encryptionUtil;
    @Value("${license}")
    private String license;
    @Autowired
    private IServiceLocator serviceLocator;
    private static final Logger log;
    
    public UserManagerServiceImpl() {
        this.userManager = null;
        this.operManager = null;
        this.roleManager = null;
        this.statsManager = null;
        this.encryptionUtil = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response createUser(final Request request) {
        final UserData data = (UserData)request.get(UMSParams.USER_DATA.name());
        BooleanResponse isTenantUser = (BooleanResponse)request.get(UMSParams.IS_TENANT_USER.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Long activeUsersCount = this.userManager.getActiveUserCount();
            Integer userCount = 0;
            final String mLicense = this.encryptionUtil.decrypt(this.license);
            final String[] propArr = mLicense.split("epm.user.count=");
            if (propArr.length > 1 && !propArr[1].isEmpty()) {
                userCount = Integer.parseInt(propArr[1]);
            }
            UserManagerServiceImpl.log.debug("User Count: " + userCount + "  Original Count: " + activeUsersCount);
            if (activeUsersCount != null && (int)(Object)activeUsersCount >= userCount) {
                return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_EXCEEDED.name(), "Create user limit exceeded"));
            }
            if (isTenantUser == null) {
                isTenantUser = new BooleanResponse(false);
            }
            RoleData roleData = new RoleData();
            if (isTenantUser.isResponse()) {
                roleData.setName("Admin");
            }
            else {
                roleData.setName("Employee");
            }
            final List<RoleData> roleList = this.roleManager.searchRole(roleData, null, null);
            if (roleList != null && roleList.size() > 0) {
                roleData = roleList.get(0);
                final UserRoleRelationshipData userRoleRel = new UserRoleRelationshipData();
                userRoleRel.setRoleId(roleData.getId());
                data.getUserRoles().add(userRoleRel);
            }
            final User user = this.getUserManager().saveOrUpdateUser(data);
            final Identifier id = new Identifier();
            id.setId(user.getUserDetails().getId());
            final UserLoginData userLoginData = new UserLoginData();
            userLoginData.setUserName(data.getUserName());
            userLoginData.setPassword(data.getPassword());
            final Integer tenantId = (Integer) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.TENANT_ID.getParamName());
            userLoginData.setTenantId(tenantId);
            request.put(UMSParams.USER_LOGIN_DATA.name(), (BaseValueObject)userLoginData);
            final AuthenticationManagerService authManagerService = (AuthenticationManagerService)this.serviceLocator.getService("AuthenticationManagerServiceImpl");
            authManagerService.createUser(request);
            return this.OK(UMSParams.USER_ID.name(), (BaseValueObject)id);
        }
        catch (Exception ex) {
            UserManagerServiceImpl.log.error("Exception in UserManagerServiceImpl - createUser() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_UNABLE_TO_CREATE_003.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response updateUser(final Request request) {
        final UserData data = (UserData)request.get(UMSParams.USER_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final User user = this.getUserManager().saveOrUpdateUser(data);
            final Identifier id = new Identifier();
            id.setId(user.getUserDetails().getId());
            final UserLoginData userLoginData = new UserLoginData();
            userLoginData.setUserName(data.getUserName());
            userLoginData.setPassword(data.getPassword());
            final Integer tenantId = (Integer) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.TENANT_ID.getParamName());
            userLoginData.setTenantId(tenantId);
            request.put(UMSParams.USER_LOGIN_DATA.name(), (BaseValueObject)userLoginData);
            final AuthenticationManagerService authManagerService = (AuthenticationManagerService)this.serviceLocator.getService("AuthenticationManagerServiceImpl");
            authManagerService.updateUser(request);
            return this.OK(UMSParams.USER_ID.name(), (BaseValueObject)id);
        }
        catch (Exception ex) {
            UserManagerServiceImpl.log.error("Exception in UserManagerServiceImpl - updateUser() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_UNABLE_TO_UPDATE_005.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response getUser(final Request request) {
        final Identifier id = (Identifier)request.get(UMSParams.USER_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final User user = this.getUserManager().getUser(id.getId());
            final UserData userData = user.getUserDetails();
            return this.OK(UMSParams.USER_DATA.name(), (BaseValueObject)userData);
        }
        catch (Exception ex) {
            UserManagerServiceImpl.log.error("Exception in UserManagerServiceImpl - getUser() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_UNABLE_TO_GET_004.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response getUserByLogin(final Request request) {
        final StringIdentifier login = (StringIdentifier)request.get(UMSParams.USER_NAME.name());
        if (login == null) {
            throw new MiddlewareException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "User login name must be specified in the input.");
        }
        try {
            final User user = this.getUserManager().getUserByLogin(login.getId());
            final UserData userData = user.getUserDetails();
            return this.OK(UMSParams.USER_DATA.name(), (BaseValueObject)userData);
        }
        catch (Exception ex) {
            UserManagerServiceImpl.log.error("Exception in UserManagerServiceImpl - getUserByLogin() : " + ex.getMessage());
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_UNKNOWN_EXCEPTION_000.name(), ex.getMessage()));
        }
    }
    
    public Response getUserByExtCode(final Request request) {
        final StringIdentifier extCode = (StringIdentifier)request.get(UMSParams.USER_EXT_CODE.name());
        if (extCode == null) {
            throw new MiddlewareException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "User external code must be specified in the input.");
        }
        try {
            final User user = this.getUserManager().getUserByExtCode(extCode.getId());
            final UserData userData = user.getUserDetails();
            return this.OK(UMSParams.USER_DATA.name(), (BaseValueObject)userData);
        }
        catch (Exception ex) {
            UserManagerServiceImpl.log.error("Exception in UserManagerServiceImpl - getUserByExtCode() : " + ex.getMessage());
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_UNKNOWN_EXCEPTION_000.name(), ex.getMessage()));
        }
    }
    
    public Response deleteUser(Request request) {
        final Identifier id = (Identifier)request.get(UMSParams.USER_ID.name());
        if (id == null || id.getId() == null || id.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final User user = this.getUserManager().getUser(id.getId());
            final boolean result = user.delete();
            request.put(UMSParams.USER_NAME.name(), (BaseValueObject)new StringIdentifier(user.getUserDetails().getUserName()));
            final AuthenticationManagerService authManagerService = (AuthenticationManagerService)this.serviceLocator.getService("AuthenticationManagerServiceImpl");
            authManagerService.deleteUser(request);
            if (result) {
                final OrgUserRoleManagerService orgUserRoleService = (OrgUserRoleManagerService)this.serviceLocator.getService("OrgUserRoleManagerServiceImpl");
                final List<Integer> ids = new ArrayList<Integer>();
                ids.add(id.getId());
                request = new Request();
                request.put(OrgUserRoleParams.USER_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)ids));
                final Response response = orgUserRoleService.removeRelationForUserIds(request);
                response.get(OrgUserRoleParams.STATUS.name());
            }
            return this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(result));
        }
        catch (Exception ex) {
            UserManagerServiceImpl.log.error("Exception in UserManagerServiceImpl - deleteUser() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_UNABLE_TO_DELETE_006.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response getUserAuthorization(final Request request) {
        final Identifier userId = (Identifier)request.get(UMSParams.USER_ID.name());
        final Identifier operId = (Identifier)request.get(UMSParams.OPER_ID.name());
        if (userId == null || operId == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Operation operation = this.operManager.getOperation(operId.getId());
            final StringTokenizer operCodes = new StringTokenizer(operation.getOperationDetails().getCode(), ".");
            final User user = this.userManager.getUser(userId.getId());
            final List<UserRoleRelationshipData> userRoles = (List<UserRoleRelationshipData>)user.getUserDetails().getUserRoles();
            while (operCodes.hasMoreElements()) {
                for (final UserRoleRelationshipData iterator : userRoles) {
                    final RoleData roleData = this.roleManager.getRole(iterator.getRoleId()).getRoleDetails();
                    final List<RoleOperationRelationshipData> operationRel = (List<RoleOperationRelationshipData>)roleData.getRoleOperations();
                    for (final RoleOperationRelationshipData roleOperationRel : operationRel) {
                        final OperationData roleOperations = this.operManager.getOperation(roleOperationRel.getOperationId()).getOperationDetails();
                        if (roleOperations.getCode().contains(operCodes.nextToken())) {
                            return this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(true));
                        }
                    }
                }
            }
            return this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(false));
        }
        catch (Exception ex) {
            UserManagerServiceImpl.log.error("Exception in UserManagerServiceImpl - getUserAuthorization() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_UNKNOWN_EXCEPTION_000.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response search(final Request request) {
        final SearchCriteria searchCriteria = (SearchCriteria)request.get(UMSParams.SEARCH_CRITERIA.name());
        if (searchCriteria == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final UserData userData = (UserData)searchCriteria.getBaseValueObject();
            final Integer maxResults = searchCriteria.getMaxResults();
            final Integer firstResult = searchCriteria.getFirstResult();
            return this.getUserManager().search(userData, maxResults, firstResult);
        }
        catch (Exception ex) {
            UserManagerServiceImpl.log.error("Exception in UserManagerServiceImpl - search() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_UNABLE_TO_SEARCH_008.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response deleteUsersByEmployeeIds(final Request request) {
        final IdentifierList employeeIds = (IdentifierList)request.get(UMSParams.EMPLOYEE_ID_LIST.name());
        if (employeeIds == null || employeeIds.getIdsList() == null || employeeIds.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final boolean status = this.userManager.deleteUsersByEmployeeIds(employeeIds.getIdsList());
            return this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            UserManagerServiceImpl.log.error("Exception in UserManagerServiceImpl - deleteUsersByEmployeeIds() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_UNABLE_TO_DELETE_BY_EMP_IDS_011.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response suspendUsersByEmployeeIds(final Request request) {
        final IdentifierList employeeIds = (IdentifierList)request.get(UMSParams.EMPLOYEE_ID_LIST.name());
        final DateResponse endDate = (DateResponse)request.get(UMSParams.END_DATE.name());
        if (employeeIds == null || endDate == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final boolean status = this.userManager.suspendUsersByEmployeeIds(employeeIds.getIdsList(), endDate.getDate());
            return this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            UserManagerServiceImpl.log.error("Exception in UserManagerServiceImpl - suspendUsersByEmployeeIds() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_SUSPEND_BY_EMP_IDS_012.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getEmployeeIdsByRoles(final Request request) {
        final IdentifierList roleList = (IdentifierList)request.get(UMSParams.USER_ROLE_ID_LIST.name());
        if (roleList == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final BaseValueObjectMap map = this.getUserManager().getEmployeeIdsByRoles(roleList);
            return this.OK(UMSParams.EMPLOYEE_ID_LIST.name(), (BaseValueObject)map);
        }
        catch (Exception ex) {
            UserManagerServiceImpl.log.error("Exception in UserManagerServiceImpl - getEmployeeIdsByRoles() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_GET_EMP_IDS_BY_ROLES_013.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public UserManager getUserManager() {
        return this.userManager;
    }
    
    public void setUserManager(final UserManager userManager) {
        this.userManager = userManager;
    }
    
    public OperationManager getOperManager() {
        return this.operManager;
    }
    
    public void setOperManager(final OperationManager operManager) {
        this.operManager = operManager;
    }
    
    public SessionStatsManager getStatsManager() {
        return this.statsManager;
    }
    
    public void setStatsManager(final SessionStatsManager statsManager) {
        this.statsManager = statsManager;
    }
    
    public Response getUserMapByRoles(final Request request) {
        final IdentifierList roleList = (IdentifierList)request.get(UMSParams.USER_ROLE_ID_LIST.name());
        if (roleList == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final BaseValueObjectMap baseValueObjectMap = this.userManager.getUserMapByRoles(roleList);
            return this.OK(UMSParams.USER_ID_LIST.name(), (BaseValueObject)baseValueObjectMap);
        }
        catch (Exception ex) {
            UserManagerServiceImpl.log.error("Exception in UserManagerServiceImpl - getUserMapByRoles() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_GET_BY_ROLES_010.name(), "Unknown error while getUserMapByRoles", (Throwable)ex));
        }
    }
    
    public Response getUserByEmployeeId(final Request request) {
        final Identifier employeeId = (Identifier)request.get(RecipientParams.RCPT_EMP_ID.name());
        if (employeeId == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final User user = this.userManager.getUserByEmployeeId(employeeId.getId());
            return this.OK(UMSParams.USER_DATA.name(), (BaseValueObject)user.getUserDetails());
        }
        catch (Exception e) {
            UserManagerServiceImpl.log.error("Exception in UserManagerServiceImpl - getUserByEmployeeId() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_GET_USER_BY_EMP_IDS_014.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAllUsersByRoleId(final Request request) {
        final Identifier roleId = (Identifier)request.get(UMSParams.ROLE_ID.name());
        if (roleId == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final User user = null;
            return this.OK(UMSParams.USER_DATA.name(), (BaseValueObject)user.getUserDetails());
        }
        catch (Exception e) {
            UserManagerServiceImpl.log.error("Exception in UserManagerServiceImpl - getAllUsersByRoleId() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_GET_BY_ROLES_010.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getUsersIdEmailFromRoleList(final Request request) {
        try {
            System.out.println(this.license);
            return this.OK(UMSParams.USER_DATA_ID_EMAIL.name(), (BaseValueObject)null);
        }
        catch (Exception ex) {
            UserManagerServiceImpl.log.error("Exception in UserManagerServiceImpl - getUsersIdEmailFromRoleList() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_EMAIL_IDS_ROLE_LIST_015.name(), "Unknown error while getUsersIdEmailFromRoleList", new Object[] { ex.getMessage(), ex }));
        }
    }
    
    static {
        UserManagerServiceImpl.USER_MGR_SERVICE = "UserManagerService";
        log = LoggerFactory.getLogger((Class)UserManagerServiceImpl.class);
    }
}
