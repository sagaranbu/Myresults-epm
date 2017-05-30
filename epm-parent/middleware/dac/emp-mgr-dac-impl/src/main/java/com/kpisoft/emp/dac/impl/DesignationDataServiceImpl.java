package com.kpisoft.emp.dac.impl;

import com.kpisoft.emp.dac.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import com.kpisoft.emp.vo.*;
import com.canopus.mw.*;
import com.kpisoft.emp.vo.param.*;
import com.kpisoft.emp.utility.*;
import com.canopus.dac.*;
import org.springframework.transaction.annotation.*;
import java.io.*;
import com.canopus.dac.utils.*;
import org.modelmapper.*;
import java.util.*;
import com.canopus.mw.dto.*;
import com.googlecode.genericdao.search.*;
import java.lang.reflect.*;

@Component
public class DesignationDataServiceImpl extends BaseDataAccessService implements DesignationDataService
{
    @Autowired
    private DesignationDao designationDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public DesignationDataServiceImpl() {
        this.designationDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)DesignationData.class, (Class)Designation.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    @Transactional
    public Response saveDesignation(final Request request) {
        final DesignationData designationData = (DesignationData)request.get(EMPParams.DESG_DATA.name());
        if (designationData == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_DESG_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        final Designation designation = new Designation();
        try {
            this.modelMapper.map((Object)designationData, (Object)designation);
            this.designationDao.save(designation);
            return this.OK(EMPParams.DESG_ID.name(), (BaseValueObject)new Identifier(designation.getId()));
        }
        catch (Exception e) {
            DesignationDataServiceImpl.log.error((Object)"Exception in DesignationDataServiceImpl saveDesignation() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_DESG_UNABLE_TO_CREATE_003.name(), "Failed to save designation data"));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getDesignation(final Request request) {
        final Identifier desigId = (Identifier)request.get(EMPParams.DESG_ID.name());
        if (desigId == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_DESG_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        Designation designation = null;
        try {
            designation = (Designation)this.designationDao.find((Serializable)desigId.getId());
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            DesignationDataServiceImpl.log.error((Object)"Exception in DesignationDataServiceImpl getDesignation() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_DESG_UNABLE_TO_GET_004.name(), "Failed to load designation data"));
        }
        if (designation == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_DESG_DOES_NOT_EXIST_001.name(), "Designation id {0} does not exist.", new Object[] { desigId.getId() }));
        }
        final DesignationData designationData = (DesignationData)this.modelMapper.map((Object)designation, (Class)DesignationData.class);
        return this.OK(EMPParams.DESG_DATA.name(), (BaseValueObject)designationData);
    }
    
    @Transactional
    public Response updateDesignation(final Request request) {
        final DesignationData designationData = (DesignationData)request.get(EMPParams.DESG_DATA.name());
        if (designationData == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_DESG_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        Designation designation = null;
        try {
            if (designationData.getId() != null && designationData.getId() > 0) {
                designation = (Designation)this.designationDao.find((Serializable)designationData.getId());
                if (designation == null) {
                    return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_DESG_DOES_NOT_EXIST_001.name(), "designation id {0} does not exist.", new Object[] { designationData.getId() }));
                }
                this.modelMapper.map((Object)designationData, (Object)designation);
            }
            this.designationDao.save(designation);
            return this.OK(EMPParams.DESG_ID.name(), (BaseValueObject)new Identifier(designation.getId()));
        }
        catch (Exception e) {
            DesignationDataServiceImpl.log.error((Object)"Exception in DesignationDataServiceImpl updateDesignation() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_DESG_UNABLE_TO_UPDATE_005.name(), "Failed to update designation data"));
        }
    }
    
    @Transactional
    public Response removeDesignation(final Request request) {
        final Identifier desigId = (Identifier)request.get(EMPParams.DESG_ID.name());
        if (desigId == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_DESG_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final boolean status = this.designationDao.removeById((Serializable)desigId.getId());
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            DesignationDataServiceImpl.log.error((Object)"Exception in DesignationDataServiceImpl removeDesignation() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_DESG_UNABLE_TO_DELETE_006.name(), "Failed to remove designation data"));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllDesignations(final Request request) {
        final SortList sortList = request.getSortList();
        final String defaultField = "name";
        try {
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.designationDao.getFilterFromExample(new Designation(), options);
            final Search search = new Search((Class)Designation.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<Designation> data = (List<Designation>)this.designationDao.search((ISearch)search);
            final Type listType = new TypeToken<List<DesignationData>>() {}.getType();
            final List<DesignationData> designationData = (List<DesignationData>)this.modelMapper.map((Object)data, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)designationData);
            final Response response = this.OK(EMPParams.DESG_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            DesignationDataServiceImpl.log.error((Object)"Exception in DesignationDataServiceImpl getAllDesignations() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_DESG_UNABLE_TO_GET_ALL_007.name(), "Failed to load all designation data"));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchDesignation(final Request request) {
        final DesignationData designationData = (DesignationData)request.get(EMPParams.DESG_DATA.name());
        final SortList sortList = request.getSortList();
        final String defaultField = "name";
        if (designationData == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_DESG_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Designation designation = (Designation)this.modelMapper.map((Object)designationData, (Class)Designation.class);
            final Filter filter = this.designationDao.getFilterFromExample(designation, options);
            final Search search = new Search((Class)Designation.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<Designation> data = (List<Designation>)this.designationDao.search((ISearch)search);
            final Type listType = new TypeToken<List<DesignationData>>() {}.getType();
            final List<DesignationData> designationDataList = (List<DesignationData>)this.modelMapper.map((Object)data, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)designationDataList);
            final Response response = this.OK(EMPParams.DESG_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            DesignationDataServiceImpl.log.error((Object)"Exception in DesignationDataServiceImpl searchDesignations() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_DESG_UNABLE_TO_SEARCH_008.name(), "Failed to search the designation data"));
        }
    }
    
    static {
        log = Logger.getLogger((Class)DesignationDataServiceImpl.class);
    }
}
