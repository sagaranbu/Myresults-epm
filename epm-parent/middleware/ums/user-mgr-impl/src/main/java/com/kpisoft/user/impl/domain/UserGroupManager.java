package com.kpisoft.user.impl.domain;

import org.springframework.stereotype.*;
import com.kpisoft.user.dac.*;
import javax.validation.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.user.vo.*;
import com.canopus.mw.*;
import com.kpisoft.user.vo.param.*;
import java.util.*;
import com.canopus.mw.dto.*;

@Component
public class UserGroupManager extends BaseDomainManager implements CacheLoader<Integer, UserGroup>
{
    @Autowired
    private UserGroupDataService dataService;
    @Autowired
    private Validator validator;
    @Autowired
    @Qualifier("userGroupCache")
    private Cache<Integer, UserGroup> multiTenantCache;
    
    public UserGroupManager() {
        this.dataService = null;
        this.multiTenantCache = null;
    }
    
    public UserGroup saveOrUpdateUserGroup(final GroupData data) {
        final UserGroup userGroup = new UserGroup(this);
        userGroup.setUserGroupDetails(data);
        int id = 0;
        try {
            id = userGroup.save();
        }
        catch (Exception e) {
            throw new MiddlewareException("ERR_SAVE_UPDATE_USER_GROUP", e.getMessage(), (Throwable)e);
        }
        finally {
            if (id > 0) {
                this.multiTenantCache.remove(id);
            }
        }
        return userGroup;
    }
    
    public UserGroup getUserGroup(final int userGroupID) {
        final UserGroup userGroup = (UserGroup)this.multiTenantCache.get(userGroupID, (CacheLoader)this);
        return userGroup;
    }
    
    public UserGroup load(final Integer key) {
        final UserGroupDataService svc = this.getDataService();
        final Request request = new Request();
        request.put(UMSParams.USER_GRP_ID.name(), (BaseValueObject)new Identifier(key));
        final Response response = svc.getUserGroup(request);
        final GroupData data = (GroupData)response.get(UMSParams.USER_GRP_DATA.name());
        final UserGroup userGroup = new UserGroup(this);
        userGroup.setUserGroupDetails(data);
        return userGroup;
    }
    
    public List<GroupData> getAllGroups(Page page, SortList sortList) {
        final Response response = this.getDataService().getAllGroups(new Request());
        page = response.getPage();
        sortList = response.getSortList();
        final BaseValueObjectList list = (BaseValueObjectList)response.get(UMSParams.USER_GRP_DATA_LIST.name());
        final List<GroupData> gList = (List<GroupData>)list.getValueObjectList();
        return gList;
    }
    
    public List<GroupData> search(final GroupData groupData, Page page, SortList sortList) {
        final Request request = new Request();
        request.put(UMSParams.USER_GRP_DATA.name(), (BaseValueObject)groupData);
        final Response response = this.getDataService().search(request);
        page = response.getPage();
        sortList = response.getSortList();
        final BaseValueObjectList list = (BaseValueObjectList)response.get(UMSParams.USER_GRP_DATA_LIST.name());
        final List<GroupData> gList = (List<GroupData>)list.getValueObjectList();
        return gList;
    }
    
    public UserGroupDataService getDataService() {
        return this.dataService;
    }
    
    public Validator getValidator() {
        return this.validator;
    }
    
    public Cache<Integer, UserGroup> getMultiTenantCache() {
        return this.multiTenantCache;
    }
}
