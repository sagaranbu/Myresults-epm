package com.kpisoft.user.impl;

import com.kpisoft.user.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.user.vo.*;
import com.kpisoft.user.vo.param.*;
import com.kpisoft.user.utility.*;
import com.canopus.mw.*;
import com.kpisoft.user.impl.domain.*;
import java.util.*;
import com.canopus.mw.dto.*;
import org.slf4j.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ UserGroupManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class UserGroupManagerServiceImpl extends BaseMiddlewareBean implements UserGroupManagerService
{
    public static String USER_GROUP_MGR_SERVICE;
    @Autowired
    private UserGroupManager userGroupManager;
    private static final Logger log;
    
    public UserGroupManagerServiceImpl() {
        this.userGroupManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response createUserGroup(final Request request) {
        final GroupData data = (GroupData)request.get(UMSParams.USER_GRP_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_GRP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final UserGroup userGroup = this.getUserGroupManager().saveOrUpdateUserGroup(data);
            final Identifier id = new Identifier();
            id.setId(userGroup.getUserGroupDetails().getId());
            return this.OK(UMSParams.USER_GRP_ID.name(), (BaseValueObject)id);
        }
        catch (Exception ex) {
            UserGroupManagerServiceImpl.log.error("Exception in UserGroupManagerServiceImpl - createUserGroup() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_GRP_UNABLE_TO_CREATE_003.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response updateUserGroup(final Request request) {
        final GroupData data = (GroupData)request.get(UMSParams.USER_GRP_DATA.name());
        try {
            if (data == null) {
                return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_GRP_INVALID_INPUT_002.name(), "No data object in the request"));
            }
            final UserGroup userGroup = this.getUserGroupManager().getUserGroup(data.getId());
            userGroup.setUserGroupDetails(data);
            userGroup.save();
            final Identifier id = new Identifier();
            id.setId(userGroup.getUserGroupDetails().getId());
            return this.OK(UMSParams.USER_GRP_ID.name(), (BaseValueObject)id);
        }
        catch (Exception ex) {
            UserGroupManagerServiceImpl.log.error("Exception in UserGroupManagerServiceImpl - updateUserGroup() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_GRP_UNABLE_TO_UPDATE_005.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response getUserGroup(final Request request) {
        final Identifier id = (Identifier)request.get(UMSParams.USER_GRP_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_GRP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final UserGroup userGroup = this.getUserGroupManager().getUserGroup(id.getId());
            final GroupData userGroupData = userGroup.getUserGroupDetails();
            return this.OK(UMSParams.USER_GRP_DATA.name(), (BaseValueObject)userGroupData);
        }
        catch (Exception ex) {
            UserGroupManagerServiceImpl.log.error("Exception in UserGroupManagerServiceImpl - getUserGroup() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_GRP_UNABLE_TO_GET_004.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response getAllGroups(final Request request) {
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        try {
            final List<GroupData> gList = this.getUserGroupManager().getAllGroups(page, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)gList);
            final Response response = this.OK(UMSParams.USER_GRP_DATA_LIST.name(), (BaseValueObject)list);
            return response;
        }
        catch (Exception ex) {
            UserGroupManagerServiceImpl.log.error("Exception in UserGroupManagerServiceImpl - getAllGroups() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_GRP_UNABLE_TO_GET_ALL_007.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response deleteUserGroup(final Request request) {
        final Identifier id = (Identifier)request.get(UMSParams.USER_GRP_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_GRP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final UserGroup userGroup = this.getUserGroupManager().getUserGroup(id.getId());
            final GroupData groupData = userGroup.getUserGroupDetails();
            if (groupData.getUserGroups() != null && groupData.getUserGroups().size() > 0) {
                return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_GRP_UNABLE_TO_DELETE_006.name(), "This Group is assigned to users"));
            }
            return userGroup.delete();
        }
        catch (Exception ex) {
            UserGroupManagerServiceImpl.log.error("Exception in UserGroupManagerServiceImpl - deleteUserGroup() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_GRP_UNABLE_TO_DELETE_006.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response search(final Request request) {
        final SearchCriteria searchCriteria = (SearchCriteria)request.get(UMSParams.SEARCH_CRITERIA.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        try {
            if (searchCriteria == null) {
                return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_GRP_INVALID_INPUT_002.name(), "No data object in the request"));
            }
            final GroupData groupData = (GroupData)searchCriteria.getBaseValueObject();
            final List<GroupData> gList = this.getUserGroupManager().search(groupData, page, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)gList);
            final Response response = this.OK(UMSParams.USER_GRP_DATA_LIST.name(), (BaseValueObject)list);
            return response;
        }
        catch (Exception ex) {
            UserGroupManagerServiceImpl.log.error("Exception in UserGroupManagerServiceImpl - search() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_GRP_UNABLE_TO_SEARCH_008.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public UserGroupManager getUserGroupManager() {
        return this.userGroupManager;
    }
    
    public void setUserGroupManager(final UserGroupManager userGroupManager) {
        this.userGroupManager = userGroupManager;
    }
    
    static {
        UserGroupManagerServiceImpl.USER_GROUP_MGR_SERVICE = "UserGroupManagerService";
        log = LoggerFactory.getLogger((Class)UserGroupManagerServiceImpl.class);
    }
}
