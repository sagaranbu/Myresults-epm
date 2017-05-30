package com.kpisoft.user.impl.domain;

import org.springframework.stereotype.*;
import com.kpisoft.user.dac.*;
import javax.validation.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.user.vo.param.*;
import javax.annotation.*;
import com.kpisoft.user.vo.*;
import com.kpisoft.user.utility.*;
import com.canopus.mw.*;
import com.canopus.mw.ValidationException;

import java.util.*;
import com.canopus.mw.dto.*;

@Component
public class OperationManager extends BaseDomainManager implements CacheLoader<Integer, Operation>
{
    @Autowired
    private OperationDataService dataService;
    @Autowired
    private Validator validator;
    @Autowired
    @Qualifier("operationCache")
    private Cache<Integer, Operation> multiTenantCache;
    
    public OperationManager() {
        this.dataService = null;
        this.multiTenantCache = null;
    }
    
    public Operation getOperation(final int operationID) {
        final Operation operation = (Operation)this.multiTenantCache.get(operationID, (CacheLoader)this);
        return operation;
    }
    
    @PostConstruct
    public void loadOperationsToCache() {
        List<OperationData> operList = null;
        final OperationDataService svc = this.getDataService();
        final Response response = svc.getAllOperations(new Request());
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(UMSParams.OPERATION_DATA_LIST.name());
        if (bList != null && bList.getValueObjectList() != null) {
            operList = (List<OperationData>)bList.getValueObjectList();
        }
        if (operList != null) {
            for (final OperationData oper : operList) {
                final Operation operation = new Operation(this);
                operation.setOperationDetails(oper);
                this.multiTenantCache.put(oper.getId(), operation);
            }
        }
    }
    
    public List<OperationData> getAllOperations() {
        List<OperationData> operList = null;
        final OperationDataService svc = this.getDataService();
        final Response response = svc.getAllOperations(new Request());
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(UMSParams.OPERATION_DATA_LIST.name());
        if (bList != null && bList.getValueObjectList() != null) {
            operList = (List<OperationData>)bList.getValueObjectList();
        }
        return operList;
    }
    
    public Operation load(final Integer key) {
        final OperationDataService svc = this.getDataService();
        final Request request = new Request();
        request.put(UMSParams.OPER_ID.name(), (BaseValueObject)new Identifier(key));
        final Response response = svc.getOperation(request);
        final OperationData data = (OperationData)response.get(UMSParams.OPERATION_DATA.name());
        final Operation operation = new Operation(this);
        operation.setOperationDetails(data);
        return operation;
    }
    
    public Response createOperationHierarchy(final OperationHierarchyData hierarchy) {
        if (hierarchy.getParentOperId() == hierarchy.getChildOperId()) {
            throw new ValidationException(UserErrorCodesEnum.ERR_OPER_INVALID_INPUT_002.name(), "Invalid operation details", (Set)null);
        }
        final Request request = new Request();
        request.put(UMSParams.OPER_HIERARCHY_DATA.name(), (BaseValueObject)hierarchy);
        final OperationDataService svc = this.getDataService();
        return svc.createOperationHierarchy(request);
    }
    
    public Response deleteOperationHierarchy(final int id) {
        final OperationDataService svc = this.getDataService();
        final Request request = new Request();
        request.put(UMSParams.OPER_HIERARCHY_ID.name(), (BaseValueObject)new Identifier(id));
        return svc.deleteOperationHierarchy(request);
    }
    
    public OperationDataService getDataService() {
        return this.dataService;
    }
    
    public Validator getValidator() {
        return this.validator;
    }
    
    public Cache<Integer, Operation> getMultiTenantCache() {
        return this.multiTenantCache;
    }
    
    public List<OperationData> search(final OperationData operationData, final Page page, final SortList sortList) {
        final SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setBaseValueObject((BaseValueObject)operationData);
        final Request request = new Request();
        request.put(UMSParams.SEARCH_CRITERIA.name(), (BaseValueObject)searchCriteria);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.search(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(UMSParams.BASEVALUE_LIST.name());
        final List<OperationData> result = (List<OperationData>)list.getValueObjectList();
        return result;
    }
    
    public List<OperationData> getoperationList(final IdentifierList operationData, final Page page, final SortList sortList) {
        final Request request = new Request();
        request.put(UMSParams.OPERATION_ID_LIST.name(), (BaseValueObject)operationData);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.searchOperationByIdList(request);
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(UMSParams.OPERATION_DATA_LIST.name());
        final List<OperationData> opData = (List<OperationData>)bList.getValueObjectList();
        return opData;
    }
    
    public List<OperationData> getOperationsByPageCodes(final BaseValueObjectList baseList, final Page page, final SortList sortList) {
        final Request request = new Request();
        request.put(UMSParams.OPERATION_PAGE_CODE_LIST.name(), (BaseValueObject)baseList);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.getOperationsByPageCodes(request);
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(UMSParams.OPERATION_DATA_LIST.name());
        final List<OperationData> opData = (List<OperationData>)bList.getValueObjectList();
        return opData;
    }
    
    public List<OperationData> getChildOperations(final OperationData operationData, final Page page, final SortList sortList) {
        final Request request = new Request();
        request.put(UMSParams.OPERATION_DATA.name(), (BaseValueObject)operationData);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.getChildOperations(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(UMSParams.BASEVALUE_LIST.name());
        final List<OperationData> result = (List<OperationData>)list.getValueObjectList();
        return result;
    }
}
