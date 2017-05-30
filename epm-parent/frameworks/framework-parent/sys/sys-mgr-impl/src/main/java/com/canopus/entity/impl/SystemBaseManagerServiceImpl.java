package com.canopus.entity.impl;

import com.canopus.entity.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import com.canopus.entity.domain.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.entity.vo.params.*;
import com.canopus.mw.*;
import com.canopus.mw.dto.*;
import java.util.*;
import com.canopus.entity.vo.*;
import com.canopus.dac.*;
import org.slf4j.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ SystemBaseManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class SystemBaseManagerServiceImpl extends BaseMiddlewareBean implements SystemBaseManagerService
{
    @Autowired
    private SystemBaseManager manager;
    private static final Logger log;
    
    public SystemBaseManagerServiceImpl() {
        this.manager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response createMasterBase(final Request request) {
        final SystemMasterBaseBean systemMasterBaseBean = (SystemMasterBaseBean)request.get(SysParams.SYS_BASE_DATA.name());
        if (systemMasterBaseBean == null) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_MASTER_BASE_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final Integer id = this.manager.saveOrUpdateBaseData(systemMasterBaseBean);
            return this.OK(SysParams.SYS_BASE_ID.name(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            SystemBaseManagerServiceImpl.log.error("Exception in SystemBaseManagerServiceImpl - createMasterBase() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_MASTER_BASE_UNABLE_TO_CREATE_003.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response deleteMasterBase(final Request request) {
        final Identifier id = (Identifier)request.get(SysParams.SYS_BASE_ID.name());
        if (id == null || id.getId() == null || id.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_MASTER_BASE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final boolean value = this.manager.deleteBaseSystem(id.getId());
            return this.OK(SysParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(value));
        }
        catch (Exception e) {
            SystemBaseManagerServiceImpl.log.error("Exception in SystemBaseManagerServiceImpl - updateMasterBase() : " + e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_MASTER_BASE_UNABLE_TO_DELETE_006.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response updateMasterBase(final Request request) {
        final SystemMasterBaseBean systemMasterBaseBean = (SystemMasterBaseBean)request.get(SysParams.SYS_BASE_DATA.name());
        if (systemMasterBaseBean == null) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_MASTER_BASE_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final Integer id = this.manager.saveOrUpdateBaseData(systemMasterBaseBean);
            return this.OK(SysParams.SYS_BASE_ID.name(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            SystemBaseManagerServiceImpl.log.error("Exception in SystemBaseManagerServiceImpl - updateMasterBase() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_MASTER_BASE_UNABLE_TO_UPDATE_005.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getMasterBaseById(final Request request) {
        final Identifier identifier = (Identifier)request.get(SysParams.SYS_BASE_ID.name());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_MASTER_BASE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final SystemMasterBaseBean systemMasterBaseBean = this.manager.getBaseById(identifier.getId());
            return this.OK(SysParams.SYS_BASE_DATA.name(), (BaseValueObject)systemMasterBaseBean);
        }
        catch (Exception e) {
            SystemBaseManagerServiceImpl.log.error("Exception in SystemBaseManagerServiceImpl - getMasterBaseById() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_MASTER_BASE_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getBaseByCategory(final Request request) {
        final Identifier identifier = (Identifier)request.get(SysParams.SYS_CATEGORY_ID.name());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_MASTER_BASE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<SystemMasterBaseBean> systemMasterBaseBeans = this.manager.getBaseByCategory(identifier.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)systemMasterBaseBeans);
            return this.OK(SysParams.SYS_BASE_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            SystemBaseManagerServiceImpl.log.error("Exception in SystemBaseManagerServiceImpl - getBaseByCategory() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_MASTER_BASE_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getBaseByCatagories(final Request request) {
        final Identifier categoryId = (Identifier)request.get(SysParams.SYS_CATEGORY_ID.name());
        final Identifier subcategoryId = (Identifier)request.get(SysParams.SYS_SUB_CATEGORY_ID.name());
        if (categoryId == null || categoryId.getId() == null || categoryId.getId() <= 0 || subcategoryId == null || subcategoryId.getId() == null || subcategoryId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_MASTER_BASE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<SystemMasterBaseBean> systemMasterBaseBeans = this.manager.getBaseByCategories(categoryId.getId(), subcategoryId.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)systemMasterBaseBeans);
            return this.OK(SysParams.SYS_BASE_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            SystemBaseManagerServiceImpl.log.error("Exception in SystemBaseManagerServiceImpl - getBaseByCatagories() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_MASTER_BASE_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getSysMasterByIds(final Request request) {
        final StringIdentifier identifier = (StringIdentifier)request.get(SysParams.SYS_BASE_IDS.name());
        if (identifier == null || identifier.getId() == null || identifier.getId().trim().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_MASTER_BASE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<SystemMasterBaseBean> systemMasterBaseBeans = this.manager.getSysMasterByIds(identifier.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)systemMasterBaseBeans);
            return this.OK(SysParams.SYS_BASE_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            SystemBaseManagerServiceImpl.log.error("Exception in SystemBaseManagerServiceImpl - getSysMasterByIds() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_MASTER_BASE_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response searchCatagory(final Request request) {
        final SystemMasCategoryData categoryData = (SystemMasCategoryData)request.get(SysParams.SYS_CATEGORY_DATA.name());
        if (categoryData == null) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_MASTER_BASE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<SystemMasCategoryData> systemMasterBaseBeans = this.manager.searchCatagory(categoryData);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)systemMasterBaseBeans);
            return this.OK(SysParams.SYS_BASE_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            SystemBaseManagerServiceImpl.log.error("Exception in SystemBaseManagerServiceImpl - searchCatagory() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_MASTER_BASE_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response searchMasterBase(final Request request) {
        final SystemMasterBaseBean masterBaseData = (SystemMasterBaseBean)request.get(SysParams.SYS_BASE_DATA.name());
        if (masterBaseData == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<SystemMasterBaseBean> systemMasterBaseList = this.manager.searchMasterBase(masterBaseData);
            final BaseValueObjectList baseValueObjList = new BaseValueObjectList();
            baseValueObjList.setValueObjectList((List)systemMasterBaseList);
            return this.OK(SysParams.SYS_BASE_DATA_LIST.name(), (BaseValueObject)baseValueObjList);
        }
        catch (Exception e) {
            SystemBaseManagerServiceImpl.log.error("Exception in SystemBaseManagerServiceImpl - searchMasterBase() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_MASTER_BASE_UNABLE_TO_SEARCH_008.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response reloadCache(final Request request) {
        try {
            this.manager.init();
            return this.OK();
        }
        catch (Exception e) {
            SystemBaseManagerServiceImpl.log.error("Exception in SystemBaseManagerServiceImpl - reloadCache() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_UNABLE_TO_LAOD_SYSTEM_MASTER_BASE_CACHE.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)SystemBaseManagerServiceImpl.class);
    }
}
