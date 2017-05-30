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
import org.modelmapper.*;
import java.util.*;
import java.lang.reflect.*;
import com.kpisoft.user.dac.impl.entity.*;
import com.canopus.dac.utils.*;
import com.canopus.mw.dto.*;
import com.googlecode.genericdao.search.*;
import org.slf4j.*;

@Component
public class RoleDataServiceImpl extends BaseDataAccessService implements RoleDataService
{
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public RoleDataServiceImpl() {
        this.roleDao = null;
        this.genericDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)RoleData.class, (Class)Role.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)RoleOperationRelationshipData.class, (Class)RoleOperationRelationship.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    @Transactional(readOnly = true)
    public Response getRole(final Request request) {
        final Identifier id = (Identifier)request.get(UMSParams.ROLE_ID.name());
        Role role = null;
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_ROLE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            role = (Role)this.roleDao.find((Serializable)id.getId());
            if (role == null) {
                return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_ROLE_DOES_NOT_EXIST_001.name(), "Role id {0} does not exist.", new Object[] { id }));
            }
            final RoleData roleData = (RoleData)this.modelMapper.map((Object)role, (Class)RoleData.class);
            return this.OK(UMSParams.ROLE_DATA.name(), (BaseValueObject)roleData);
        }
        catch (Exception ex) {
            RoleDataServiceImpl.log.error("Exception in RoleDataServiceImpl - getRole() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_ROLE_UNABLE_TO_GET_004.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    @Transactional
    public Response saveRole(final Request request) {
        final RoleData roleData = (RoleData)request.get(UMSParams.ROLE_DATA.name());
        Role role = null;
        if (roleData == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_ROLE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            if (roleData.getId() != null && roleData.getId() > 0) {
                role = (Role)this.roleDao.find((Serializable)roleData.getId());
                if (roleData.getVersion() == null || role.getVersion() != (int)roleData.getVersion()) {
                    return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_ROLE_DIRTY_UPDATE_EXCEPTION_009.name(), "Invalid version of role data"));
                }
                final Integer nextVersion = new Integer(role.getVersion() + 1);
                roleData.setVersion(nextVersion);
            }
            else {
                role = new Role();
            }
            this.modelMapper.map((Object)roleData, (Object)role);
            if (this.roleDao.isAttached(role)) {
                this.roleDao.merge((Object)role);
            }
            else {
                this.roleDao.save(role);
            }
            if (role.getRoleOperations() != null) {
                for (final RoleOperationRelationship iterator : role.getRoleOperations()) {
                    iterator.setRoleId(role.getId());
                }
            }
            return this.OK(UMSParams.ROLE_ID.name(), (BaseValueObject)new Identifier(role.getId()));
        }
        catch (Exception ex) {
            RoleDataServiceImpl.log.error("Exception in RoleDataServiceImpl - saveRole() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_ROLE_UNABLE_TO_CREATE_003.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    @Transactional
    public Response deleteRole(final Request request) {
        final Identifier id = (Identifier)request.get(UMSParams.ROLE_ID.name());
        if (id == null || id.getId() == null || id.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_ROLE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final boolean status = this.roleDao.removeById((Serializable)id.getId());
            return this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            RoleDataServiceImpl.log.error("Exception in RoleDataServiceImpl - deleteRole() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_ROLE_UNABLE_TO_DELETE_006.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllRoles(final Request request) {
        try {
            final List<Role> result = (List<Role>)this.roleDao.findAll();
            final Type listType = new TypeToken<List<RoleData>>() {}.getType();
            final List<RoleData> roleDataList = (List<RoleData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)roleDataList);
            final Response response = this.OK(UMSParams.ROLE_DATA_LIST.name(), (BaseValueObject)list);
            return response;
        }
        catch (Exception e) {
            RoleDataServiceImpl.log.error("Exception in RoleDataServiceImpl - getAllRoles() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_ROLE_UNABLE_TO_GET_ALL_007.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchRole(final Request request) {
        final RoleData roleData = (RoleData)request.get(UMSParams.ROLE_DATA.name());
        if (roleData == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_ROLE_INVALID_INPUT_002.name(), " No data object in the request"));
        }
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "name";
        try {
            final Role role = (Role)this.modelMapper.map((Object)roleData, (Class)Role.class);
            final Search search = new Search((Class)RoleBase.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.genericDao.getFilterFromExample((Object)role, options);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.genericDao.count((ISearch)search), defaultField);
            final List<RoleBase> result = (List<RoleBase>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<RoleData>>() {}.getType();
            final List<RoleData> roleDataList = (List<RoleData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)roleDataList);
            final Response response = this.OK(UMSParams.ROLE_DATA_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            RoleDataServiceImpl.log.error("Exception in RoleDataServiceImpl - searchRole() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_ROLE_UNABLE_TO_SEARCH_008.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getRolesCount(final Request request) {
        try {
            final Search search = new Search((Class)Role.class);
            final Integer rolesCount = this.roleDao.count((ISearch)search);
            return this.OK(UMSParams.ROLES_COUNT.name(), (BaseValueObject)new Identifier(rolesCount));
        }
        catch (Exception e) {
            RoleDataServiceImpl.log.error("Exception in RoleDataServiceImpl - getRolesCount() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_OPER_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)RoleDataServiceImpl.class);
    }
}
