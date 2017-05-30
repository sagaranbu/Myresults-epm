package com.kpisoft.user.impl.domain;

import org.springframework.stereotype.*;
import com.kpisoft.user.dac.*;
import javax.validation.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.user.vo.*;
import javax.annotation.*;
import com.canopus.mw.*;
import com.kpisoft.user.utility.*;
import com.kpisoft.user.*;
import com.kpisoft.user.vo.param.*;
import java.util.*;
import com.canopus.mw.dto.*;
import org.slf4j.*;

@Component
public class UserManager extends BaseDomainManager implements CacheLoader<Integer, User>
{
    @Autowired
    private UserDataService dataService;
    @Autowired
    private UserGroupDataService userGroupDataService;
    @Autowired
    private RoleDataService roleDataServiceImpl;
    @Autowired
    private IServiceLocator serviceLocator;
    private Validator validator;
    @Autowired
    @Qualifier("userCache")
    private Cache<Integer, User> userCache;
    @Value("${cacheOnStartup:true}")
    private boolean cacheOnStartup;
    private static final Logger log;
    
    public UserManager() {
        this.dataService = null;
        this.userGroupDataService = null;
        this.roleDataServiceImpl = null;
        this.userCache = null;
        this.cacheOnStartup = false;
    }
    
    @PostConstruct
    public void loadUsersToCache() {
        if (!this.cacheOnStartup) {
            return;
        }
        ExecutionContext.getCurrent().setCrossTenant();
        try {
            final Response response = this.dataService.loadUsersToCache(new Request());
            final BaseValueObjectList list = (BaseValueObjectList)response.get(UMSParams.USER_DATA_LIST.name());
            if (list != null && list.getValueObjectList() != null && !list.getValueObjectList().isEmpty()) {
                final List<UserData> users = (List<UserData>)list.getValueObjectList();
                for (final UserData iterator : users) {
                    final User user = new User(this);
                    user.setUserDetails(iterator);
                    this.userCache.put(iterator.getId(), user);
                }
            }
        }
        catch (Exception e) {
            UserManager.log.error("Exception in UserManager - loadUsersToCache() : ", (Throwable)e);
        }
        ExecutionContext.getCurrent().unSetCrossTenant();
    }
    
    public void loadFrequentUsersToCache() {
        ExecutionContext.getCurrent().setCrossTenant();
        try {
            final UserData data = new UserData();
            data.setUsageType(1);
            final Request request = new Request();
            request.put(UMSParams.USER_DATA.name(), (BaseValueObject)data);
            final Response response = this.dataService.loadFrequentUsersToCache(request);
            final BaseValueObjectList list = (BaseValueObjectList)response.get(UMSParams.USER_DATA_LIST.name());
            if (list != null && list.getValueObjectList() != null && !list.getValueObjectList().isEmpty()) {
                final List<UserData> users = (List<UserData>)list.getValueObjectList();
                for (final UserData iterator : users) {
                    final User user = new User(this);
                    user.setUserDetails(iterator);
                    this.userCache.put(iterator.getId(), user);
                }
            }
        }
        catch (Exception e) {
            UserManager.log.error("Exception in UserManager - loadFrequentUsersToCache() : " + e);
        }
        ExecutionContext.getCurrent().unSetCrossTenant();
    }
    
    public User saveOrUpdateUser(final UserData data) {
        final User user = new User(this);
        user.setUserDetails(data);
        int id = 0;
        try {
            id = user.save();
        }
        catch (Exception e) {
            throw new MiddlewareException("SAVE_UPDATE_USER", e.getMessage(), (Throwable)e);
        }
        finally {
            if (id > 0) {
                this.userCache.remove(id);
            }
        }
        return user;
    }
    
    public User getUser(final int userID) {
        final User user = (User)this.userCache.get(userID, (CacheLoader)this);
        return user;
    }
    
    public User getUserByLogin(final String userName) {
        if (userName == null) {
            throw new MiddlewareException(UserErrorCodesEnum.ERR_AUTH_INVALID_INPUT_002.name(), "Invalid user name - null");
        }
        final UserDataService svc = this.getDataService();
        final Request request = new Request();
        request.put(UMSParams.USER_NAME.name(), (BaseValueObject)new StringIdentifier(userName));
        final Response response = svc.getUserByLogin(request);
        final UserData data = (UserData)response.get(UMSParams.USER_DATA.name());
        final User user = new User(this);
        user.setUserDetails(data);
        return user;
    }
    
    public User getUserByExtCode(final String userName) {
        if (userName == null) {
            throw new MiddlewareException(UserErrorCodesEnum.ERR_AUTH_INVALID_INPUT_002.name(), "Invalid user external code - null");
        }
        final UserDataService svc = this.getDataService();
        final Request request = new Request();
        request.put(UMSParams.USER_EXT_CODE.name(), (BaseValueObject)new StringIdentifier(userName));
        final Response response = svc.getUserByExtCode(request);
        final UserData data = (UserData)response.get(UMSParams.USER_DATA.name());
        final User user = new User(this);
        user.setUserDetails(data);
        return user;
    }
    
    public User load(final Integer key) {
        final UserDataService svc = this.getDataService();
        final Request request = new Request();
        request.put(UMSParams.USER_ID.name(), (BaseValueObject)new Identifier(key));
        final Response response = svc.getUser(request);
        final UserData data = (UserData)response.get(UMSParams.USER_DATA.name());
        final User user = new User(this);
        user.setUserDetails(data);
        return user;
    }
    
    public Response search(final UserData userData, final Integer maxResults, final Integer firstResult) {
        final Request request = new Request();
        request.put(UMSParams.USER_DATA.name(), (BaseValueObject)userData);
        request.put(UMSParams.SEARCH_MAX_RESULTS.name(), (BaseValueObject)new Identifier(maxResults));
        request.put(UMSParams.SEARCH_FIRST_RESULT.name(), (BaseValueObject)new Identifier(firstResult));
        final UserDataService svc = this.getDataService();
        return svc.search(request);
    }
    
    public boolean deleteUsersByEmployeeIds(final List<Integer> employeeIds) {
        Request request = new Request();
        request.put(UMSParams.EMPLOYEE_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)employeeIds));
        Response response = this.dataService.deleteUsersByEmployeeIds(request);
        final BooleanResponse status = (BooleanResponse)response.get(UMSParams.STATUS_RESPONSE.name());
        if (status.isResponse()) {
            final IdentifierList idList = (IdentifierList)response.get(UMSParams.USER_ID_LIST.name());
            if (idList != null && idList.getIdsList() != null && !idList.getIdsList().isEmpty()) {
                final OrgUserRoleManagerService orgUserRoleService = (OrgUserRoleManagerService)this.serviceLocator.getService("OrgUserRoleManagerServiceImpl");
                for (final Integer id : idList.getIdsList()) {
                    this.userCache.remove(id);
                }
                request = new Request();
                request.put(OrgUserRoleParams.USER_ID_LIST.name(), (BaseValueObject)new IdentifierList(idList.getIdsList()));
                response = orgUserRoleService.removeRelationForUserIds(request);
                response.get(OrgUserRoleParams.STATUS.name());
            }
        }
        return status.isResponse();
    }
    
    public boolean suspendUsersByEmployeeIds(final List<Integer> employeeIds, final Date endDate) {
        final DateResponse date = new DateResponse();
        date.setDate(endDate);
        final Request request = new Request();
        request.put(UMSParams.EMPLOYEE_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)employeeIds));
        request.put(UMSParams.END_DATE.name(), (BaseValueObject)date);
        final Response response = this.dataService.suspendUsersByEmployeeIds(request);
        final BooleanResponse status = (BooleanResponse)response.get(UMSParams.STATUS_RESPONSE.name());
        if (status.isResponse()) {
            final IdentifierList idList = (IdentifierList)response.get(UMSParams.USER_ID_LIST.name());
            if (idList != null && idList.getIdsList() != null && !idList.getIdsList().isEmpty()) {
                for (final Integer id : idList.getIdsList()) {
                    this.userCache.remove(id);
                }
            }
        }
        return status.isResponse();
    }
    
    public BaseValueObjectMap getUserMapByRoles(final IdentifierList idList) {
        final Request request = new Request();
        request.put(UMSParams.USER_ROLE_ID_LIST.name(), (BaseValueObject)idList);
        final Response response = this.getDataService().getUserMapByRoles(request);
        final BaseValueObjectMap map = (BaseValueObjectMap)response.get(UMSParams.USER_ID_LIST.name());
        return map;
    }
    
    public User getUserByEmployeeId(final Integer employeeId) {
        final Request request = new Request();
        request.put(UMSParams.EMPLOYEE_ID.name(), (BaseValueObject)new Identifier(employeeId));
        final Response response = this.dataService.getUserByEmployeeId(request);
        final UserData data = (UserData)response.get(UMSParams.USER_DATA.name());
        final User user = new User(this);
        user.setUserDetails(data);
        return user;
    }
    
    public BaseValueObjectMap getEmployeeIdsByRoles(final IdentifierList roleList) {
        final Request request = new Request();
        request.put(UMSParams.USER_ROLE_ID_LIST.name(), (BaseValueObject)roleList);
        final Response response = this.dataService.getEmployeeIdsByRoles(request);
        final BaseValueObjectMap map = (BaseValueObjectMap)response.get(UMSParams.EMPLOYEE_ID_LIST.name());
        return map;
    }
    
    public BaseValueObjectMap getUsersIdEmailFromRoleList(final IdentifierList idList) {
        final Request request = new Request();
        request.put(UMSParams.USER_ROLE_ID_LIST.name(), (BaseValueObject)idList);
        final Response response = this.getDataService().getUsersIdEmailFromRoleList(request);
        final BaseValueObjectMap map = (BaseValueObjectMap)response.get(UMSParams.USER_DATA_ID_EMAIL.name());
        return map;
    }
    
    public UserDataService getDataService() {
        return this.dataService;
    }
    
    public Validator getValidator() {
        return this.validator;
    }
    
    public void setValidator(final Validator validator) {
        this.validator = validator;
    }
    
    public Cache<Integer, User> getUserCache() {
        return this.userCache;
    }
    
    public void setUserCache(final Cache<Integer, User> userCache) {
        this.userCache = userCache;
    }
    
    public Long getActiveUserCount() {
        final Response response = this.getDataService().getActiveUserCount(new Request());
        final LongIdentifier count = (LongIdentifier)response.get(UMSParams.USER_COUNT.name());
        return count.getId();
    }
    
    static {
        log = LoggerFactory.getLogger((Class)UserManager.class);
    }
}
