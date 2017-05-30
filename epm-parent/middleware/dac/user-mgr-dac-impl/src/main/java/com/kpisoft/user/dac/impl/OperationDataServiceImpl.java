package com.kpisoft.user.dac.impl;

import com.kpisoft.user.dac.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.user.dac.impl.dao.*;
import com.canopus.mw.*;
import com.kpisoft.user.vo.*;
import com.kpisoft.user.dac.impl.entity.*;
import com.kpisoft.user.vo.param.*;
import com.kpisoft.user.utility.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import com.canopus.dac.utils.*;
import org.modelmapper.*;
import com.googlecode.genericdao.search.*;
import java.lang.reflect.*;
import com.canopus.mw.dto.*;
import java.util.*;
import org.slf4j.*;

@Component
public class OperationDataServiceImpl extends BaseDataAccessService implements OperationDataService
{
    @Autowired
    private OperationDao operationDao;
    @Autowired
    private OperationHierarchyDao operationHierarchyDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public OperationDataServiceImpl() {
        this.operationDao = null;
        this.operationHierarchyDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)OperationData.class, (Class)Operation.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)OperationHierarchyData.class, (Class)OperationHierarchy.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    @Transactional(readOnly = true)
    public Response getOperation(final Request request) {
        final Identifier id = (Identifier)request.get(UMSParams.OPER_ID.name());
        Operation operation = null;
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_OPER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            operation = (Operation)this.operationDao.find((Serializable)id.getId());
        }
        catch (Exception ex) {
            OperationDataServiceImpl.log.error("Exception in OperationDataServiceImpl - getOperation() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_OPER_UNABLE_TO_GET_004.name(), ex.getMessage(), (Throwable)ex));
        }
        if (operation == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_OPER_DOES_NOT_EXIST_010.name(), "Operation id {0} does not exist.", new Object[] { id }));
        }
        final OperationData operationData = (OperationData)this.modelMapper.map((Object)operation, (Class)OperationData.class);
        return this.OK(UMSParams.OPERATION_DATA.name(), (BaseValueObject)operationData);
    }
    
    @Transactional(readOnly = true)
    public Response getAllOperations(final Request request) {
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        try {
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.operationDao.getFilterFromExample(new Operation(), options);
            final Search search = new Search((Class)Operation.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.operationDao.count((ISearch)search), defaultField);
            final List<Operation> result = (List<Operation>)this.operationDao.search((ISearch)search);
            final Type listType = new TypeToken<List<OperationData>>() {}.getType();
            final List<OperationData> oprList = (List<OperationData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)oprList);
            final Response response = this.OK(UMSParams.OPERATION_DATA_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            OperationDataServiceImpl.log.error("Exception in OperationDataServiceImpl - getAllOperations() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_OPER_UNABLE_TO_GET_ALL_007.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response createOperationHierarchy(final Request request) {
        final OperationHierarchyData operHrachyData = (OperationHierarchyData)request.get(UMSParams.OPER_HIERARCHY_DATA.name());
        OperationHierarchy operationHierarchy = null;
        if (operHrachyData == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_OPER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            if (operHrachyData.getId() != null) {
                operationHierarchy = (OperationHierarchy)this.operationHierarchyDao.find((Serializable)operHrachyData.getId());
                if (operHrachyData.getVersion() == null || operHrachyData.getVersion() != operHrachyData.getVersion()) {
                    return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_OPER_UNKNOWN_EXCEPTION_000.name(), "No data object in the request"));
                }
                final Integer nextVersion = new Integer(operHrachyData.getVersion() + 1);
                operHrachyData.setVersion(nextVersion);
            }
            else {
                operationHierarchy = new OperationHierarchy();
            }
            this.modelMapper.map((Object)operHrachyData, (Object)operationHierarchy);
            this.operationHierarchyDao.save(operationHierarchy);
            return this.OK(UMSParams.OPER_HIERARCHY_ID.name(), (BaseValueObject)new Identifier(operationHierarchy.getId()));
        }
        catch (Exception ex) {
            OperationDataServiceImpl.log.error("Exception in OperationDataServiceImpl - createOperationHierarchy() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_OPER_UNKNOWN_EXCEPTION_000.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    @Transactional
    public Response deleteOperationHierarchy(final Request request) {
        final Identifier id = (Identifier)request.get(UMSParams.OPER_HIERARCHY_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_OPER_INVALID_INPUT_002.name(), "Invalid version of operation data"));
        }
        try {
            final boolean status = this.operationHierarchyDao.removeById((Serializable)id.getId());
            return this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            OperationDataServiceImpl.log.error("Exception in OperationDataServiceImpl - deleteOperationHierarchy() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_OPER_UNABLE_TO_DELETE_006.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response search(final Request request) {
        final SearchCriteria searchCriteria = (SearchCriteria)request.get(UMSParams.SEARCH_CRITERIA.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        if (searchCriteria == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_OPER_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final OperationData operationData = (OperationData)searchCriteria.getBaseValueObject();
            final Operation operation = (Operation)this.modelMapper.map((Object)operationData, (Class)Operation.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.operationDao.getFilterFromExample(operation, options);
            final Search search = new Search((Class)Operation.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.operationDao.count((ISearch)search), defaultField);
            final List<Operation> result = (List<Operation>)this.operationDao.search((ISearch)search);
            final Type listType = new TypeToken<List<OperationData>>() {}.getType();
            final List<OperationData> operationList = (List<OperationData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)operationList);
            final Response response = this.OK(UMSParams.BASEVALUE_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            OperationDataServiceImpl.log.error("Exception in OperationDataServiceImpl - search() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_OPER_UNABLE_TO_SEARCH_008.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchOperationByIdList(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(UMSParams.OPERATION_ID_LIST.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_OPER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            BaseValueObjectList bvol = null;
            final Search search = new Search((Class)Operation.class);
            search.addFilterIn("id", (Collection)idList.getIdsList());
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.operationDao.count((ISearch)search), defaultField);
            final List<Operation> list = (List<Operation>)this.operationDao.search((ISearch)search);
            final Type listType = new TypeToken<List<OperationData>>() {}.getType();
            final List<OperationData> oprList = (List<OperationData>)this.modelMapper.map((Object)list, listType);
            bvol = new BaseValueObjectList();
            bvol.setValueObjectList((List)oprList);
            return this.OK(UMSParams.OPERATION_DATA_LIST.name(), (BaseValueObject)bvol);
        }
        catch (Exception ex) {
            OperationDataServiceImpl.log.error("Exception in OperationDataServiceImpl - searchOperationByIdList() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_OPER_UNABLE_TO_SEARCH_008.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getOperationsByPageCodes(final Request request) {
        final BaseValueObjectList pageCodeList = (BaseValueObjectList)request.get(UMSParams.OPERATION_PAGE_CODE_LIST.name());
        final List<String> pageCodes = new ArrayList<String>();
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        if (pageCodeList == null || pageCodeList.getValueObjectList() == null || pageCodeList.getValueObjectList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_OPER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        if (pageCodeList != null && pageCodeList.getValueObjectList().size() > 0) {
            final List<StringIdentifier> stringIdentifiers = (List<StringIdentifier>)pageCodeList.getValueObjectList();
            for (int i = 0; i < stringIdentifiers.size(); ++i) {
                pageCodes.add(stringIdentifiers.get(i).getId());
            }
        }
        try {
            BaseValueObjectList bvol = null;
            final Search search = new Search((Class)Operation.class);
            search.addFilterIn("code", (Collection)pageCodes);
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.operationDao.count((ISearch)search), defaultField);
            final List<Operation> list = (List<Operation>)this.operationDao.search((ISearch)search);
            final Type listType = new TypeToken<List<OperationData>>() {}.getType();
            final List<OperationData> oprList = (List<OperationData>)this.modelMapper.map((Object)list, listType);
            bvol = new BaseValueObjectList();
            bvol.setValueObjectList((List)oprList);
            return this.OK(UMSParams.OPERATION_DATA_LIST.name(), (BaseValueObject)bvol);
        }
        catch (Exception ex) {
            OperationDataServiceImpl.log.error("Exception in OperationDataServiceImpl - getOperationsByPageCodes() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_OPER_UNABLE_TO_SEARCH_008.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getChildOperations(final Request request) {
        final OperationData operationData = (OperationData)request.get(UMSParams.OPERATION_DATA.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        if (operationData == null) {
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_OPER_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final Operation operation = (Operation)this.modelMapper.map((Object)operationData, (Class)Operation.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(1);
            options.setIgnoreCase(true);
            final Filter filter = this.operationDao.getFilterFromExample(operation, options);
            final Search search = new Search((Class)Operation.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.operationDao.count((ISearch)search), defaultField);
            final List<Operation> result = (List<Operation>)this.operationDao.search((ISearch)search);
            final Type listType = new TypeToken<List<OperationData>>() {}.getType();
            final List<OperationData> operationList = (List<OperationData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)operationList);
            final Response response = this.OK(UMSParams.BASEVALUE_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            OperationDataServiceImpl.log.error("Exception in OperationDataServiceImpl - getChildOperations() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_OPER_UNABLE_TO_SEARCH_008.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)OperationDataServiceImpl.class);
    }
}
