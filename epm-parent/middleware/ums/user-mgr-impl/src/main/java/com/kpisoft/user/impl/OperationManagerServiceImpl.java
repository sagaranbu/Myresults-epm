package com.kpisoft.user.impl;

import com.kpisoft.user.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.user.vo.param.*;
import com.kpisoft.user.utility.*;
import com.canopus.mw.*;
import com.kpisoft.user.impl.domain.*;
import java.util.*;
import com.kpisoft.user.vo.*;
import com.canopus.mw.dto.*;
import org.slf4j.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ OperationManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class OperationManagerServiceImpl extends BaseMiddlewareBean implements OperationManagerService
{
    public static String OPER_MGR_SERVICE;
    @Autowired
    private OperationManager operationManager;
    private static final Logger log;
    
    public OperationManagerServiceImpl() {
        this.operationManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response getOperation(final Request request) {
        final Identifier id = (Identifier)request.get(UMSParams.OPER_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_OPER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Operation operation = this.getOperationManager().getOperation(id.getId());
            final OperationData operationData = operation.getOperationDetails();
            return this.OK(UMSParams.OPERATION_DATA.name(), (BaseValueObject)operationData);
        }
        catch (Exception ex) {
            OperationManagerServiceImpl.log.error("Exception in OperationManagerServiceImpl - getOperation() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_OPER_UNABLE_TO_GET_004.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response getAllOperations(final Request request) {
        try {
            final List<OperationData> operList = this.getOperationManager().getAllOperations();
            Collections.sort(operList, new Comparator<OperationData>() {
                @Override
                public int compare(final OperationData arg0, final OperationData arg1) {
                    return arg0.getDisplayOrderId() - arg1.getDisplayOrderId();
                }
            });
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)operList);
            return this.OK(UMSParams.OPERATION_DATA_LIST.name(), (BaseValueObject)bList);
        }
        catch (Exception ex) {
            OperationManagerServiceImpl.log.error("Exception in OperationManagerServiceImpl - getAllOperations() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_OPER_UNABLE_TO_GET_ALL_007.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response createOperationHierarchy(final Request request) {
        final OperationHierarchyData data = (OperationHierarchyData)request.get(UMSParams.OPER_HIERARCHY_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_OPER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            return this.getOperationManager().createOperationHierarchy(data);
        }
        catch (Exception ex) {
            OperationManagerServiceImpl.log.error("Exception in OperationManagerServiceImpl - createOperationHierarchy() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_OPER_UNKNOWN_EXCEPTION_000.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response deleteOperationHierarchy(final Request request) {
        final Identifier id = (Identifier)request.get(UMSParams.OPER_HIERARCHY_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_OPER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            return this.getOperationManager().deleteOperationHierarchy(id.getId());
        }
        catch (Exception ex) {
            OperationManagerServiceImpl.log.error("Exception in OperationManagerServiceImpl - deleteOperationHierarchy() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_OPER_UNABLE_TO_DELETE_006.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public OperationManager getOperationManager() {
        return this.operationManager;
    }
    
    public void setOperationManager(final OperationManager operationManager) {
        this.operationManager = operationManager;
    }
    
    public Response search(final Request request) {
        final SearchCriteria searchCriteria = (SearchCriteria)request.get(UMSParams.SEARCH_CRITERIA.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (searchCriteria == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_OPER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final OperationData operationData = (OperationData)searchCriteria.getBaseValueObject();
            final List<OperationData> result = this.getOperationManager().search(operationData, page, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(UMSParams.BASEVALUE_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            OperationManagerServiceImpl.log.error("Exception in OpearationManagerServiceImpl - search() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_OPER_UNABLE_TO_SEARCH_008.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response searchOperationByIdList(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(UMSParams.OPERATION_ID_LIST.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (idList == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_OPER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<OperationData> opData = this.getOperationManager().getoperationList(idList, page, sortList);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)opData);
            return this.OK(UMSParams.OPERATION_DATA_LIST.name(), (BaseValueObject)bList);
        }
        catch (Exception e) {
            OperationManagerServiceImpl.log.error("Exception in OpearationManagerServiceImpl - searchOperationByIdList() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_OPER_UNABLE_TO_SEARCH_008.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getOperationsByPageCodes(final Request request) {
        final BaseValueObjectList baseList = (BaseValueObjectList)request.get(UMSParams.OPERATION_PAGE_CODE_LIST.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (baseList == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_OPER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<OperationData> opData = this.getOperationManager().getOperationsByPageCodes(baseList, page, sortList);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)opData);
            return this.OK(UMSParams.OPERATION_DATA_LIST.name(), (BaseValueObject)bList);
        }
        catch (Exception e) {
            OperationManagerServiceImpl.log.error("Exception in OpearationManagerServiceImpl - getOperationsByPageCodes() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_OPER_UNABLE_TO_SEARCH_008.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getChildOperations(final Request request) {
        final OperationData operationData = (OperationData)request.get(UMSParams.OPERATION_DATA.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (operationData == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_OPER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<OperationData> result = this.getOperationManager().getChildOperations(operationData, page, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(UMSParams.BASEVALUE_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            OperationManagerServiceImpl.log.error("Exception in OpearationManagerServiceImpl - getChildOperations() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_OPER_UNABLE_TO_SEARCH_008.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        OperationManagerServiceImpl.OPER_MGR_SERVICE = "OperationManagerService";
        log = LoggerFactory.getLogger((Class)OperationManagerServiceImpl.class);
    }
}
