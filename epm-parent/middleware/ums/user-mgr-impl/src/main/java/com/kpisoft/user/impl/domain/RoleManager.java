package com.kpisoft.user.impl.domain;

import org.springframework.stereotype.*;
import com.kpisoft.user.dac.*;
import javax.validation.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.user.vo.param.*;
import com.kpisoft.user.vo.*;
import java.util.*;
import javax.annotation.*;
import com.canopus.mw.*;
import com.canopus.mw.dto.*;

@Component
public class RoleManager extends BaseDomainManager implements CacheLoader<Integer, Role>
{
    @Autowired
    private RoleDataService dataService;
    @Autowired
    private Validator validator;
    @Autowired
    @Qualifier("roleCache")
    private Cache<Integer, Role> multiTenantCache;
    
    public RoleManager() {
        this.dataService = null;
        this.multiTenantCache = null;
    }
    
    @PostConstruct
    public void loadRolesToCache() {
        List<RoleData> roleList = null;
        final BaseValueObjectList bList = (BaseValueObjectList)this.getDataService().getAllRoles(new Request()).get(UMSParams.ROLE_DATA_LIST.name());
        if (bList != null && bList.getValueObjectList() != null) {
            roleList = (List<RoleData>)bList.getValueObjectList();
            if (roleList != null && !roleList.isEmpty()) {
                for (final RoleData iterator : roleList) {
                    final Role role = new Role(this);
                    role.setRoleDetails(iterator);
                    this.multiTenantCache.put(iterator.getId(), role);
                }
            }
        }
    }
    
    public Role saveOrUpdateRole(final RoleData data) {
        final Role role = new Role(this);
        role.setRoleDetails(data);
        int id = 0;
        try {
            id = role.save();
        }
        catch (Exception e) {
            throw new MiddlewareException("ERR_SAVE_UPDATE_ROLE", e.getMessage(), (Throwable)e);
        }
        finally {
            if (id > 0) {
                this.multiTenantCache.remove(id);
            }
        }
        return role;
    }
    
    public List<RoleData> getAllRoles() {
        final RoleDataService svc = this.getDataService();
        final Request request = new Request();
        final Response response = svc.getAllRoles(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(UMSParams.ROLE_DATA_LIST.name());
        final List<RoleData> roleList = (List<RoleData>)list.getValueObjectList();
        if (roleList != null && !roleList.isEmpty()) {
            for (final RoleData iterator : roleList) {
                final Role role = new Role(this);
                role.setRoleDetails(iterator);
                this.multiTenantCache.put(iterator.getId(), role);
            }
        }
        return roleList;
    }
    
    public Role getRole(final int roleID) {
        final Role role = (Role)this.multiTenantCache.get(roleID, (CacheLoader)this);
        return role;
    }
    
    public Role load(final Integer key) {
        final Request request = new Request();
        request.put(UMSParams.ROLE_ID.name(), (BaseValueObject)new Identifier(key));
        final Response response = this.dataService.getRole(request);
        final RoleData data = (RoleData)response.get(UMSParams.ROLE_DATA.name());
        final Role role = new Role(this);
        role.setRoleDetails(data);
        return role;
    }
    
    public List<RoleData> searchRole(final RoleData roleData, Page page, SortList sortList) {
        final Request request = new Request();
        request.put(UMSParams.ROLE_DATA.name(), (BaseValueObject)roleData);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.getDataService().searchRole(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(UMSParams.ROLE_DATA_LIST.name());
        final List<RoleData> roleList = (List<RoleData>)list.getValueObjectList();
        page = response.getPage();
        sortList = response.getSortList();
        return roleList;
    }
    
    public RoleDataService getDataService() {
        return this.dataService;
    }
    
    public Validator getValidator() {
        return this.validator;
    }
    
    public Cache<Integer, Role> getMultiTenantCache() {
        return this.multiTenantCache;
    }
    
    public Integer getRolesCount() {
        final Response response = this.getDataService().getRolesCount(new Request());
        final Identifier identifier = (Identifier)response.get(UMSParams.ROLES_COUNT.name());
        return identifier.getId();
    }
}
