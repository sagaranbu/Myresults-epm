package com.kpisoft.emp.impl;

import com.kpisoft.emp.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.events.*;
import com.kpisoft.emp.vo.*;
import com.kpisoft.emp.vo.param.*;
import com.kpisoft.emp.utility.*;
import com.canopus.dac.*;
import com.canopus.mw.*;
import com.kpisoft.emp.impl.domain.*;
import java.util.*;
import com.canopus.mw.dto.*;
import org.slf4j.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ ManagerLevelService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class ManagerLevelServiceImpl extends BaseMiddlewareBean implements ManagerLevelService
{
    @Autowired
    private ManagerLevelManager managerLevelManager;
    @Autowired
    private IServiceLocator serviceLocator;
    @Autowired
    protected IMiddlewareEventClient middlewareEventClient;
    private static final Logger log;
    
    public ManagerLevelServiceImpl() {
        this.managerLevelManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response saveManagerLevel(final Request request) {
        final ManagerLevelData managerLevelData = (ManagerLevelData)request.get(EMPParams.EMP_MGR_LEVEL_DATA.name());
        Integer response = null;
        if (managerLevelData == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            response = this.managerLevelManager.saveManagerLevel(managerLevelData);
        }
        catch (Exception e) {
            ManagerLevelServiceImpl.log.error("Exception in ManagerLevelServiceImpl - saveManagerLevel() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_UNABLE_TO_CREATE_003.name(), e.getMessage(), (Throwable)e));
        }
        return this.OK(EMPParams.EMP_MGR_LEVEL_ID.name(), (BaseValueObject)new Identifier(response));
    }
    
    public Response getManagerLevel(final Request request) {
        final Identifier id = (Identifier)request.get(EMPParams.EMP_MGR_LEVEL_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final ManagerLevelDomain managerLevel = this.managerLevelManager.getManagerLevel(id.getId());
            if (managerLevel != null && managerLevel.getManagerLevelData() != null) {
                return this.OK(EMPParams.EMP_MGR_LEVEL_DATA.name(), (BaseValueObject)managerLevel.getManagerLevelData());
            }
            return this.OK(EMPParams.EMP_MGR_LEVEL_DATA.name(), (BaseValueObject)null);
        }
        catch (Exception ex) {
            ManagerLevelServiceImpl.log.error("Exception in ManagerLevelServiceImpl - getManagerLevel() : " + ex);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_MGR_LEVEL_UNABLE_TO_GET_004.name(), "Failed to get manager level", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response updateManagerLevel(final Request request) {
        final ManagerLevelData managerLevelData = (ManagerLevelData)request.get(EMPParams.EMP_MGR_LEVEL_DATA.name());
        if (managerLevelData == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final Response res = this.managerLevelManager.updateManagerLevel(request);
            final Identifier identifier = (Identifier)res.get(EMPParams.EMP_MGR_LEVEL_ID.name());
            return this.OK(EMPParams.EMP_MGR_LEVEL_ID.name(), (BaseValueObject)identifier);
        }
        catch (Exception e) {
            ManagerLevelServiceImpl.log.error("Exception in ManagerLevelServiceImpl - updateManagerLevel() : " + e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_UNABLE_TO_UPDATE_005.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response removeManagerLevel(final Request request) {
        final Identifier identifier = (Identifier)request.get(EMPParams.EMP_MGR_LEVEL_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final Response response = this.managerLevelManager.removeManagerLevel(request);
            final BooleanResponse res = (BooleanResponse)response.get(EMPParams.EMP_MGR_LEVEL_BOOLEAN_RESPONSE.name());
            return this.OK(EMPParams.EMP_MGR_LEVEL_BOOLEAN_RESPONSE.name(), (BaseValueObject)res);
        }
        catch (Exception ex) {
            ManagerLevelServiceImpl.log.error("Exception in ManagerLevelServiceImpl - removeManagerLevel() : ", (Throwable)ex);
            throw new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_UNABLE_TO_DELETE_006.name(), "Failed to delete managerLevel.", (Throwable)ex);
        }
    }
    
    public Response getAllManagerLevels(final Request request) {
        final SortList sortList = request.getSortList();
        try {
            final List<ManagerLevelData> data = this.managerLevelManager.getAllManagerLevels(sortList);
            final BaseValueObjectList result = new BaseValueObjectList();
            result.setValueObjectList((List)data);
            final Response response = this.OK(EMPParams.EMP_MGR_LEVEL_DATA_LIST.name(), (BaseValueObject)result);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            ManagerLevelServiceImpl.log.error("Exception in ManagerLevelServiceImpl - getAllManagerLevels() : " + e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_UNABLE_TO_GET_ALL_007.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response searchManagerLevel(final Request request) {
        final ManagerLevelData managerLevelData = (ManagerLevelData)request.get(EMPParams.EMP_MGR_LEVEL_DATA.name());
        if (managerLevelData == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        final SortList sortList = request.getSortList();
        try {
            final List<ManagerLevelData> managerLevelList = this.managerLevelManager.searchManagerLevel(managerLevelData, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)managerLevelList);
            final Response response = this.OK(EMPParams.EMP_MGR_LEVEL_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            ManagerLevelServiceImpl.log.error("Exception in ManagerLevelServiceImpl - searchManagerLevel() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_UNABLE_TO_SEARCH_008.name(), "Failed to load the managerLevel data", new Object[] { e.getMessage() }));
        }
    }
    
    public ManagerLevelManager getManagerLevelManager() {
        return this.managerLevelManager;
    }
    
    public void setManagerLevelManager(final ManagerLevelManager managerLevelManager) {
        this.managerLevelManager = managerLevelManager;
    }
    
    static {
        log = LoggerFactory.getLogger((Class)ManagerLevelServiceImpl.class);
    }
}
