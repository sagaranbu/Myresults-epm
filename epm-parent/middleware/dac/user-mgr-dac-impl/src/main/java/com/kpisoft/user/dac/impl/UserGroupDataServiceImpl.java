package com.kpisoft.user.dac.impl;

import com.kpisoft.user.dac.*;
import org.springframework.stereotype.*;
import com.kpisoft.user.dac.impl.dao.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.user.vo.*;
import com.canopus.mw.*;
import com.kpisoft.user.vo.param.*;
import com.kpisoft.user.utility.*;
import com.canopus.dac.*;
import java.io.*;
import com.kpisoft.user.dac.impl.entity.*;
import org.springframework.transaction.annotation.*;
import com.canopus.dac.utils.*;
import org.modelmapper.*;
import java.util.*;
import java.lang.reflect.*;
import com.canopus.mw.dto.*;
import com.googlecode.genericdao.search.*;
import org.slf4j.*;

@Component
public class UserGroupDataServiceImpl extends BaseDataAccessService implements UserGroupDataService
{
    @Autowired
    UserGroupDao userGroupDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public UserGroupDataServiceImpl() {
        this.userGroupDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)GroupData.class, (Class)UserGroup.class);
    }
    
    @Transactional
    public Response saveUserGroup(final Request request) {
        final GroupData data = (GroupData)request.get(UMSParams.USER_GRP_DATA.name());
        UserGroup userGroup = null;
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_GRP_UNKNOWN_EXCEPTION_000.name(), "No data object in the request"));
        }
        try {
            if (data.getId() != null) {
                userGroup = (UserGroup)this.userGroupDao.find((Serializable)data.getId());
                if (data.getVersion() == null || data.getVersion() != (int)userGroup.getVersion()) {
                    return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_GRP_DIRTY_UPDATE_EXCEPTION_009.name(), "Invalid version of user group data"));
                }
                final Integer nextVersion = userGroup.getVersion() + 1;
                data.setVersion(nextVersion);
            }
            else {
                userGroup = new UserGroup();
            }
            this.modelMapper.map((Object)data, (Object)userGroup);
            if (this.userGroupDao.isAttached(userGroup)) {
                this.userGroupDao.merge((Object)userGroup);
            }
            else {
                this.userGroupDao.save(userGroup);
            }
            if (userGroup.getUserGroups() != null) {
                for (final UserGroupRelationship iterator : userGroup.getUserGroups()) {
                    iterator.setGroupId(userGroup.getId());
                }
            }
            if (userGroup.getRoleGroups() != null) {
                for (final RoleGroupRelationship iterator2 : userGroup.getRoleGroups()) {
                    iterator2.setGroupId(userGroup.getId());
                }
            }
            return this.OK(UMSParams.USER_GRP_ID.name(), (BaseValueObject)new Identifier(userGroup.getId()));
        }
        catch (Exception ex) {
            UserGroupDataServiceImpl.log.error("Exception in UserGroupDataServiceImpl - saveUserGroup() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_GRP_UNABLE_TO_CREATE_003.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getUserGroup(final Request request) {
        final Identifier id = (Identifier)request.get(UMSParams.USER_GRP_ID.name());
        UserGroup userGroup = null;
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_GRP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            userGroup = (UserGroup)this.userGroupDao.find((Serializable)id.getId());
        }
        catch (Exception ex) {
            UserGroupDataServiceImpl.log.error("Exception in UserGroupDataServiceImpl - getUserGroup() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_GRP_UNABLE_TO_GET_004.name(), ex.getMessage(), (Throwable)ex));
        }
        if (userGroup == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_GRP_UNABLE_TO_GET_004.name(), "User group with id {0} does not exist.", new Object[] { id }));
        }
        final GroupData userGroupData = (GroupData)this.modelMapper.map((Object)userGroup, (Class)GroupData.class);
        return this.OK(UMSParams.USER_GRP_DATA.name(), (BaseValueObject)userGroupData);
    }
    
    @Transactional(readOnly = true)
    public Response getAllGroups(final Request request) {
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        try {
            final Search search = new Search((Class)UserGroup.class);
            search.addFetch("roleGroups");
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.userGroupDao.count((ISearch)search), "name");
            List<UserGroup> userGroups = (List<UserGroup>)this.userGroupDao.search((ISearch)search);
            if (userGroups == null || userGroups.isEmpty()) {
                userGroups = new ArrayList<UserGroup>();
            }
            final Type listType = new TypeToken<List<GroupData>>() {}.getType();
            final List<GroupData> usrGrpsBVList = (List<GroupData>)this.modelMapper.map((Object)userGroups, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)usrGrpsBVList);
            final Response response = this.OK(UMSParams.USER_GRP_DATA_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception ex) {
            UserGroupDataServiceImpl.log.error("Exception in UserGroupDataServiceImpl - getAllGroups() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_GRP_UNABLE_TO_GET_ALL_007.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    @Transactional
    public Response deleteUserGroup(final Request request) {
        final Identifier id = (Identifier)request.get(UMSParams.USER_GRP_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_GRP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final boolean status = this.userGroupDao.removeById((Serializable)id.getId());
            return this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            UserGroupDataServiceImpl.log.error("Exception in UserGroupDataServiceImpl - deleteUserGroup() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_GRP_UNABLE_TO_DELETE_006.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response search(final Request request) {
        final GroupData groupData = (GroupData)request.get(UMSParams.USER_GRP_DATA.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        try {
            if (groupData == null) {
                return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_GRP_INVALID_INPUT_002.name(), "No data object in the request"));
            }
            final UserGroup userGroup = new UserGroup();
            this.modelMapper.map((Object)groupData, (Object)userGroup);
            final Search search = new Search((Class)UserGroup.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.userGroupDao.getFilterFromExample(userGroup, options);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.userGroupDao.count((ISearch)search), "name");
            final List<UserGroup> allUserGroups = (List<UserGroup>)this.userGroupDao.search((ISearch)search);
            final Type listType = new TypeToken<List<GroupData>>() {}.getType();
            final List<? extends BaseValueObject> userGroups = (List<? extends BaseValueObject>)this.modelMapper.map((Object)allUserGroups, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)userGroups);
            final Response response = this.OK(UMSParams.USER_GRP_DATA_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception ex) {
            UserGroupDataServiceImpl.log.error("Exception in UserGroupDataServiceImpl - search() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_USER_GRP_UNABLE_TO_SEARCH_008.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)UserGroupDataServiceImpl.class);
    }
}
