package com.kpisoft.user.dac.impl;

import com.kpisoft.user.dac.*;
import org.springframework.stereotype.*;
import com.kpisoft.user.dac.impl.dao.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.dac.hibernate.*;
import com.canopus.mw.*;
import com.kpisoft.user.vo.*;
import com.kpisoft.user.vo.param.*;
import com.kpisoft.user.utility.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import com.kpisoft.user.dac.impl.entity.*;
import com.googlecode.genericdao.search.*;
import org.modelmapper.*;
import java.lang.reflect.*;
import java.util.*;
import com.canopus.mw.dto.*;
import org.slf4j.*;

@Component
public class UserDataServiceImpl extends BaseDataAccessService implements UserDataService
{
    @Autowired
    private UserDao userDao;
    @Autowired
    private GenericHibernateDao genericDao;
    private static final Logger log;
    private ModelMapper modelMapper;
    private ModelMapper searchModelMapper;
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public UserDataServiceImpl() {
        this.userDao = null;
        this.genericDao = null;
        this.modelMapper = null;
        this.searchModelMapper = null;
        this.modelMapper = new ModelMapper();
        (this.searchModelMapper = new ModelMapper()).addMappings((PropertyMap)new UserSearchMap());
        TransformationHelper.createTypeMap(this.modelMapper, (Class)UserData.class, (Class)User.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)UserGroupRelationshipData.class, (Class)UserGroupRelationship.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)UserRoleRelationshipData.class, (Class)UserRoleRelationship.class);
        TransformationHelper.createTypeMap(this.searchModelMapper, (Class)UserData.class, (Class)User.class);
        TransformationHelper.createTypeMap(this.searchModelMapper, (Class)UserGroupRelationshipData.class, (Class)UserGroupRelationship.class);
        TransformationHelper.createTypeMap(this.searchModelMapper, (Class)UserRoleRelationshipData.class, (Class)UserRoleRelationship.class);
    }
    
    @Transactional(readOnly = true)
    public Response getUser(final Request request) {
        final Identifier identifier = (Identifier)request.get(UMSParams.USER_ID.name());
        User user = null;
        Integer id = null;
        try {
            if (identifier == null || identifier.getId() == 0) {
                return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "No data object in the request"));
            }
            id = identifier.getId();
            user = (User)this.userDao.find((Serializable)id);
        }
        catch (Exception ex) {
            UserDataServiceImpl.log.error("Exception in UserDataServiceImpl - getUser() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_UNKNOWN_EXCEPTION_000.name(), "Unknown error while loading USER", new Object[] { ex.getMessage(), ex }));
        }
        if (user == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_UNABLE_TO_GET_004.name(), "User id {0} does not exist.", new Object[] { id }));
        }
        final UserData userData = (UserData)this.modelMapper.map((Object)user, (Class)UserData.class);
        return this.OK(UMSParams.USER_DATA.name(), (BaseValueObject)userData);
    }
    
    @Transactional(readOnly = true)
    public Response getUserByLogin(final Request request) {
        final StringIdentifier identifier = (StringIdentifier)request.get(UMSParams.USER_NAME.name());
        User user = null;
        String id = null;
        if (identifier == null) {
            throw new DataAccessException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            id = identifier.getId();
            final Search search = new Search((Class)User.class);
            final Filter f = Filter.equal("userName", (Object)id);
            search.addFilter(f);
            user = (User)this.userDao.searchUnique((ISearch)search);
        }
        catch (Exception ex) {
            UserDataServiceImpl.log.error("Exception in UserDataServiceImpl - getUserByName(): ", (Object)ex.getMessage());
            throw new DataAccessException(UserErrorCodesEnum.ERR_USER_UNKNOWN_EXCEPTION_000.name(), "Error while loading user: " + ex.getMessage(), (Throwable)ex);
        }
        if (user == null) {
            throw new DataAccessException(UserErrorCodesEnum.ERR_USER_DOES_NOT_EXIST_001.name(), "User {0} does not exist.", new Object[] { id });
        }
        final UserData userData = (UserData)this.searchModelMapper.map((Object)user, (Class)UserData.class);
        return this.OK(UMSParams.USER_DATA.name(), (BaseValueObject)userData);
    }
    
    @Transactional(readOnly = true)
    public Response getUserByExtCode(final Request request) {
        final StringIdentifier identifier = (StringIdentifier)request.get(UMSParams.USER_EXT_CODE.name());
        User user = null;
        String id = null;
        if (identifier == null) {
            throw new DataAccessException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            id = identifier.getId();
            UserDataServiceImpl.log.info("Loading user by ext code(): " + id);
            final Search search = new Search((Class)User.class);
            final Filter f = Filter.equal("extCode", (Object)id);
            search.addFilter(f);
            user = (User)this.userDao.searchUnique((ISearch)search);
        }
        catch (Exception ex) {
            UserDataServiceImpl.log.error("Exception in UserDataServiceImpl - getUserByExtCode(): ", (Object)ex.getMessage());
            throw new DataAccessException(UserErrorCodesEnum.ERR_USER_UNKNOWN_EXCEPTION_000.name(), "Error while loading user: " + ex.getMessage(), (Throwable)ex);
        }
        if (user == null) {
            throw new DataAccessException(UserErrorCodesEnum.ERR_USER_DOES_NOT_EXIST_001.name(), "User {0} does not exist.", new Object[] { id });
        }
        final UserData userData = (UserData)this.searchModelMapper.map((Object)user, (Class)UserData.class);
        return this.OK(UMSParams.USER_DATA.name(), (BaseValueObject)userData);
    }
    
    @Transactional
    public Response saveUser(final Request request) {
        User user = null;
        final UserData userData = (UserData)request.get(UMSParams.USER_DATA.name());
        try {
            if (userData == null) {
                return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "No data object in the request"));
            }
            if (userData.getId() != null && userData.getId() != 0) {
                user = (User)this.userDao.find((Serializable)userData.getId());
                if (userData.getVersion() == null || user.getVersion() != (int)userData.getVersion()) {
                    return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_DIRTY_UPDATE_EXCEPTION_009.name(), "Invalid version of user data"));
                }
                final Integer nextVersion = new Integer(user.getVersion() + 1);
                userData.setVersion(nextVersion);
            }
            else {
                final Integer tenantId = ExecutionContext.getTenantId();
                final List<User> userList = this.userDao.searchByUserName(userData.getUserName(), tenantId, true);
                if (userList.size() > 0) {
                    return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_AUTH_INVALID_INPUT_002.name(), "Username already in use"));
                }
                user = new User();
            }
            this.modelMapper.map((Object)userData, (Object)user);
            if (this.userDao.isAttached(user)) {
                this.userDao.merge((Object)user);
            }
            else {
                this.userDao.save(user);
            }
            if (user.getUserRoles() != null && user.getUserRoles().size() > 0) {
                for (final UserRoleRelationship userRoleRel : user.getUserRoles()) {
                    userRoleRel.setUserId(user.getId());
                }
            }
            return this.OK(UMSParams.USER_ID.name(), (BaseValueObject)new Identifier(user.getId()));
        }
        catch (Exception ex) {
            UserDataServiceImpl.log.error("Exception in UserDataServiceImpl - saveUser() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_UNABLE_TO_CREATE_003.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response search(final Request request) {
        final UserData userData = (UserData)request.get(UMSParams.USER_DATA.name());
        final Identifier maxResults = (Identifier)request.get(UMSParams.SEARCH_MAX_RESULTS.name());
        final Identifier firstResult = (Identifier)request.get(UMSParams.SEARCH_FIRST_RESULT.name());
        try {
            if (userData == null) {
                return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "No data object in the request"));
            }
            final UserBase user = new UserBase();
            this.searchModelMapper.map((Object)userData, (Object)user);
            final Search search = new Search((Class)UserBase.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.genericDao.getFilterFromExample((Object)user, options);
            search.addFilter(filter);
            if (maxResults != null && maxResults.getId() != null) {
                search.setMaxResults((int)maxResults.getId());
            }
            if (firstResult != null && firstResult.getId() != null) {
                search.setPage((int)firstResult.getId());
            }
            final List<User> allUsers = (List<User>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<UserData>>() {}.getType();
            final List<? extends BaseValueObject> users = (List<? extends BaseValueObject>)this.modelMapper.map((Object)allUsers, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)users);
            return this.OK(UMSParams.USER_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception ex) {
            UserDataServiceImpl.log.error("Exception in UserDataServiceImpl - search() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_UNABLE_TO_SEARCH_008.name(), "Unknown error while searching users", new Object[] { ex.getMessage(), ex }));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getUserMapByRoles(final Request request) {
        final IdentifierList roleList = (IdentifierList)request.get(UMSParams.USER_ROLE_ID_LIST.name());
        try {
            if (roleList == null) {
                return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "No data object in the request"));
            }
            final Search search = new Search((Class)UserRoleRelationship.class);
            final List<Integer> idList = (List<Integer>)roleList.getIdsList();
            search.addFilterIn("roleId", (Collection)idList);
            final List<UserRoleRelationship> users = (List<UserRoleRelationship>)this.genericDao.search((ISearch)search);
            final Map<Integer, List<Integer>> userRolesTemp = new HashMap<Integer, List<Integer>>();
            for (final Integer role : idList) {
                final List<Integer> userList = new ArrayList<Integer>();
                for (final UserRoleRelationship userRoleRelationship : users) {
                    if (role.equals(userRoleRelationship.getRoleId())) {
                        userList.add(userRoleRelationship.getUserId());
                    }
                }
                userRolesTemp.put(role, userList);
            }
            final Map<Identifier, IdentifierList> roleAndUsers = new HashMap<Identifier, IdentifierList>();
            for (final Map.Entry<Integer, List<Integer>> userRoles : userRolesTemp.entrySet()) {
                roleAndUsers.put(new Identifier((Integer)userRoles.getKey()), new IdentifierList((List)userRoles.getValue()));
            }
            final BaseValueObjectMap map = new BaseValueObjectMap();
            map.setBaseValueMap((Map)roleAndUsers);
            return this.OK(UMSParams.USER_ID_LIST.name(), (BaseValueObject)map);
        }
        catch (Exception ex) {
            UserDataServiceImpl.log.error("Exception in UserDataServiceImpl - getUserMapByRoles() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_GET_BY_ROLES_010.name(), "Unknown error while getting the users", new Object[] { ex.getMessage(), ex }));
        }
    }
    
    @Transactional
    public Response deleteUser(final Request request) {
        try {
            final Identifier id = (Identifier)request.get(UMSParams.USER_ID.name());
            final boolean status = this.userDao.removeById((Serializable)id.getId());
            return this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            UserDataServiceImpl.log.error("Exception in UserDataServiceImpl - deleteUser() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_UNABLE_TO_DELETE_006.name(), "Unknown error while deleting users", new Object[] { ex.getMessage(), ex }));
        }
    }
    
    @Transactional
    public Response deleteUsersByEmployeeIds(final Request request) {
        final IdentifierList employeeIds = (IdentifierList)request.get(UMSParams.EMPLOYEE_ID_LIST.name());
        if (employeeIds == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final Search search = new Search((Class)User.class);
            search.addFilterIn("employeeId", (Collection)employeeIds.getIdsList());
            final List<User> users = (List<User>)this.userDao.search((ISearch)search);
            if (users != null && !users.isEmpty()) {
                this.genericDao.remove(users.toArray());
            }
            final List<Integer> idList = new ArrayList<Integer>();
            for (final User iterator : users) {
                idList.add(iterator.getId());
            }
            final Response response = this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(true));
            response.put(UMSParams.USER_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)idList));
            return response;
        }
        catch (Exception ex) {
            UserDataServiceImpl.log.error("Exception in UserDataServiceImpl - deleteUsersByEmployeeIds() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_UNABLE_TO_DELETE_BY_EMP_IDS_011.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    @Transactional
    public Response suspendUsersByEmployeeIds(final Request request) {
        final IdentifierList employeeIds = (IdentifierList)request.get(UMSParams.EMPLOYEE_ID_LIST.name());
        final DateResponse endDate = (DateResponse)request.get(UMSParams.END_DATE.name());
        if (employeeIds == null || endDate == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        final List<Integer> idList = new ArrayList<Integer>();
        try {
            final Search search = new Search((Class)User.class);
            search.addFilterIn("employeeId", (Collection)employeeIds.getIdsList());
            final List<User> users = (List<User>)this.userDao.search((ISearch)search);
            if (users != null && !users.isEmpty()) {
                for (final User iterator : users) {
                    iterator.setEndDate(endDate.getDate());
                }
                final Calendar endDateCalendar = Calendar.getInstance();
                endDateCalendar.setTime(endDate.getDate());
                endDateCalendar.set(10, 0);
                endDateCalendar.set(12, 0);
                endDateCalendar.set(13, 0);
                endDateCalendar.set(14, 0);
                final Calendar currentCalendar = Calendar.getInstance();
                currentCalendar.set(10, 0);
                currentCalendar.set(12, 0);
                currentCalendar.set(13, 0);
                currentCalendar.set(14, 0);
                if (currentCalendar.getTime().equals(endDateCalendar.getTime())) {
                    for (final User iterator2 : users) {
                        iterator2.setStatus(0);
                        idList.add(iterator2.getId());
                    }
                }
                this.genericDao.save(users.toArray());
            }
            final Response response = this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(true));
            response.put(UMSParams.USER_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)idList));
            return response;
        }
        catch (Exception ex) {
            UserDataServiceImpl.log.error("Exception in UserDataServiceImpl - suspendUsersByEmployeeIds() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_SUSPEND_BY_EMP_IDS_012.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getEmployeeIdsByRoles(final Request request) {
        final IdentifierList roleList = (IdentifierList)request.get(UMSParams.USER_ROLE_ID_LIST.name());
        if (roleList == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "No roles in the request"));
        }
        try {
            final List<Integer> roleIds = (List<Integer>)roleList.getIdsList();
            final List<Object[]> results = this.userDao.getEmployeeIdsByRoles(roleIds);
            final Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
            List<Integer> empList = null;
            for (final Object[] objects : results) {
                if (objects[0] != null && objects[1] != null) {
                    if (map.containsKey(objects[0])) {
                        empList = map.get(objects[0]);
                        empList.add(Integer.parseInt(objects[1].toString()));
                    }
                    else {
                        empList = new ArrayList<Integer>();
                        empList.add(Integer.parseInt(objects[1].toString()));
                    }
                    map.put(Integer.parseInt(objects[0].toString()), empList);
                }
            }
            final Map<Identifier, IdentifierList> roleAndEmps = new HashMap<Identifier, IdentifierList>();
            Identifier role = null;
            IdentifierList employees = null;
            for (final Integer key : map.keySet()) {
                role = new Identifier(key);
                employees = new IdentifierList((List)map.get(key));
                roleAndEmps.put(role, employees);
            }
            final BaseValueObjectMap baseValueObjectMap = new BaseValueObjectMap();
            baseValueObjectMap.setBaseValueMap((Map)roleAndEmps);
            return this.OK(UMSParams.EMPLOYEE_ID_LIST.name(), (BaseValueObject)baseValueObjectMap);
        }
        catch (Exception ex) {
            UserDataServiceImpl.log.error("Exception in UserDataServiceImpl - getEmployeeIdsByRoles() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_GET_EMP_IDS_BY_ROLES_013.name(), "Unknown error while getting  employee ids", new Object[] { ex.getMessage(), ex }));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getUserByEmployeeId(final Request request) {
        final Identifier employeeId = (Identifier)request.get(UMSParams.EMPLOYEE_ID.name());
        if (employeeId == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final Search search = new Search((Class)User.class);
            search.addFilterIn("employeeId", new Object[] { employeeId.getId() });
            final User user = (User)this.userDao.searchUnique((ISearch)search);
            final UserData userData = (UserData)this.modelMapper.map((Object)user, (Class)UserData.class);
            return this.OK(UMSParams.USER_DATA.name(), (BaseValueObject)userData);
        }
        catch (Exception ex) {
            UserDataServiceImpl.log.error("Exception in UserDataServiceImpl - getUserByEmployeeId() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_GET_USER_BY_EMP_IDS_014.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getUsersIdEmailFromRoleList(final Request request) {
        final IdentifierList roleIdentifierList = (IdentifierList)request.get(UMSParams.USER_ROLE_ID_LIST.name());
        if (roleIdentifierList == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "No roles in the request"));
        }
        try {
            final List<Integer> roleIds = (List<Integer>)roleIdentifierList.getIdsList();
            final List<Object[]> results = this.userDao.getUsersIdEmailFromRoleList(roleIds);
            final Map<IdentifierList, BaseValueObjectList> userIdEmailMap = new HashMap<IdentifierList, BaseValueObjectList>();
            final List<Integer> userIdList = new ArrayList<Integer>();
            final List<StringIdentifier> userEmailList = new ArrayList<StringIdentifier>();
            for (final Object[] objects : results) {
                if (objects[0] != null && objects[1] != null) {
                    userIdList.add(Integer.parseInt(objects[1].toString()));
                    userEmailList.add(new StringIdentifier((objects[3] == null) ? "" : objects[3].toString()));
                }
            }
            final BaseValueObjectList userEmailBaseObjList = new BaseValueObjectList();
            userEmailBaseObjList.setValueObjectList((List)userEmailList);
            userIdEmailMap.put(new IdentifierList((List)userIdList), userEmailBaseObjList);
            final BaseValueObjectMap baseValueObjectMap = new BaseValueObjectMap();
            baseValueObjectMap.setBaseValueMap((Map)userIdEmailMap);
            return this.OK(UMSParams.USER_DATA_ID_EMAIL.name(), (BaseValueObject)baseValueObjectMap);
        }
        catch (Exception ex) {
            UserDataServiceImpl.log.error("Exception in UserDataServiceImpl - getUserIdEmailFromRoleList() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_EMAIL_IDS_ROLE_LIST_015.name(), "Unknown error while getting user ids and email", new Object[] { ex.getMessage(), ex }));
        }
    }
    
    @Transactional(readOnly = true)
    public Response loadFrequentUsersToCache(final Request request) {
        final UserData userData = (UserData)request.get(UMSParams.USER_DATA.name());
        if (userData == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final User user = new User();
            final ModelMapper modelMapper = new ModelMapper();
            modelMapper.map((Object)userData, (Object)user);
            final Filter filter = this.userDao.getFilterFromExample(user);
            final Search search = new Search((Class)User.class);
            search.addFilter(filter);
            final List<User> allUsers = (List<User>)this.userDao.search((ISearch)search);
            final Type listType = new TypeToken<List<UserData>>() {}.getType();
            final List<UserData> users = (List<UserData>)modelMapper.map((Object)allUsers, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)users);
            return this.OK(UMSParams.USER_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            UserDataServiceImpl.log.error("Exception in UserDataServiceImpl - loadFrequentUsersToCache() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_LOAD_USERS_TO_CACHE_016.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response loadUsersToCache(final Request request) {
        try {
            final List<User> allUsers = (List<User>)this.userDao.findAll();
            final Type listType = new TypeToken<List<UserData>>() {}.getType();
            final List<UserData> users = (List<UserData>)this.modelMapper.map((Object)allUsers, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)users);
            return this.OK(UMSParams.USER_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            UserDataServiceImpl.log.error("Exception in UserDataServiceImpl - loadUsersToCache() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_LOAD_USERS_TO_CACHE_016.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getActiveUserCount(final Request request) {
        try {
            final Long count = this.userDao.getActiveUserCount();
            return this.OK(UMSParams.USER_COUNT.name(), (BaseValueObject)new LongIdentifier(count));
        }
        catch (Exception e) {
            UserDataServiceImpl.log.error("Exception in UserDataServiceImpl - getActiveUserCount() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)UserDataServiceImpl.class);
    }
    
    public static class UserSearchMap extends PropertyMap<User, UserData>
    {
        protected void configure() {
            ((UserData)this.skip()).setUserGroups((List)null);
        }
    }
}
