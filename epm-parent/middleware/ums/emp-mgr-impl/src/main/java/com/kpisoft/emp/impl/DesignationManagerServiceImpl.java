package com.kpisoft.emp.impl;

import com.kpisoft.emp.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import com.kpisoft.emp.vo.*;
import com.kpisoft.emp.vo.param.*;
import com.kpisoft.emp.utility.*;
import com.canopus.mw.*;
import com.kpisoft.emp.impl.domain.*;
import java.util.*;
import com.canopus.mw.dto.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ DesignationManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class DesignationManagerServiceImpl extends BaseMiddlewareBean implements DesignationManagerService
{
    public static final String DESG_SERVICE_NAME = "DesignationManagerService";
    @Autowired
    private DesignationManager designationManager;
    @Autowired
    private IServiceLocator serviceLocator;
    private static final Logger log;
    
    public DesignationManagerServiceImpl() {
        this.designationManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public DesignationManager getDesignationManager() {
        return this.designationManager;
    }
    
    public void setDesignationManager(final DesignationManager designationManager) {
        this.designationManager = designationManager;
    }
    
    public Response saveDesignation(final Request request) {
        final DesignationData designationData = (DesignationData)request.get(EMPParams.DESG_DATA.name());
        if (designationData == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_DESG_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final Integer desgId = this.getDesignationManager().saveOrUpdateDesignation(designationData);
            return this.OK(EMPParams.DESG_ID.name(), (BaseValueObject)new Identifier(desgId));
        }
        catch (Exception e) {
            DesignationManagerServiceImpl.log.error((Object)"Exception in DesignationManagerServiceImpl saveDesignation() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_DESG_UNABLE_TO_CREATE_003.name(), "Failed to save designation data"));
        }
    }
    
    public Response getDesignation(final Request request) {
        final Identifier desgId = (Identifier)request.get(EMPParams.DESG_ID.name());
        if (desgId == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_DESG_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final Designation designation = this.getDesignationManager().getDesignation(desgId.getId());
            if (designation != null && designation.getDesignationData() != null) {
                return this.OK(EMPParams.DESG_DATA.name(), (BaseValueObject)designation.getDesignationData());
            }
            return this.OK(EMPParams.DESG_DATA.name(), (BaseValueObject)null);
        }
        catch (Exception e) {
            DesignationManagerServiceImpl.log.error((Object)"Exception in DesignationManagerServiceImpl getDesignation() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_DESG_UNABLE_TO_GET_004.name(), "Failed to load designation data"));
        }
    }
    
    public Response updateDesignation(final Request request) {
        final DesignationData designationData = (DesignationData)request.get(EMPParams.DESG_DATA.name());
        if (designationData == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_DESG_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final Integer desgId = this.getDesignationManager().saveOrUpdateDesignation(designationData);
            return this.OK(EMPParams.DESG_ID.name(), (BaseValueObject)new Identifier(desgId));
        }
        catch (Exception e) {
            DesignationManagerServiceImpl.log.error((Object)"Exception in DesignationManagerServiceImpl saveDesignation() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_DESG_UNABLE_TO_CREATE_003.name(), "Failed to save designation data"));
        }
    }
    
    public Response removeDesignation(final Request request) {
        final Identifier desgId = (Identifier)request.get(EMPParams.DESG_ID.name());
        if (desgId == null || desgId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_DESG_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final boolean result = this.getDesignationManager().deleteDesignation(desgId.getId());
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(result));
        }
        catch (Exception e) {
            DesignationManagerServiceImpl.log.error((Object)"Exception in DesignationManagerServiceImpl removeDesignation() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_DESG_UNABLE_TO_DELETE_006.name(), "Failed to remove designation data"));
        }
    }
    
    public Response getAllDesignations(final Request request) {
        final SortList sortList = request.getSortList();
        try {
            final List<DesignationData> designations = this.getDesignationManager().getAllDesignations(sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)designations);
            final Response response = this.OK(EMPParams.DESG_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            DesignationManagerServiceImpl.log.error((Object)"Exception in DesignationManagerServiceImpl getAllDesignations() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_DESG_UNABLE_TO_GET_ALL_007.name(), "Failed to load all designation data"));
        }
    }
    
    public Response searchDesignation(final Request request) {
        final DesignationData designationData = (DesignationData)request.get(EMPParams.DESG_DATA.name());
        final SortList sortList = request.getSortList();
        if (designationData == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_DESG_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final List<DesignationData> designations = this.getDesignationManager().searchDesignations(designationData, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)designations);
            final Response response = this.OK(EMPParams.DESG_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            DesignationManagerServiceImpl.log.error((Object)"Exception in DesignationManagerServiceImpl searchDesignations() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_DESG_UNABLE_TO_SEARCH_008.name(), "Failed to load all designation data"));
        }
    }
    
    static {
        log = Logger.getLogger((Class)DesignationManagerServiceImpl.class);
    }
}
