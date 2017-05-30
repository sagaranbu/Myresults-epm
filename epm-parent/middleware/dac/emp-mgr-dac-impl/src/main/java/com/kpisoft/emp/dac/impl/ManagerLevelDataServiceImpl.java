package com.kpisoft.emp.dac.impl;

import com.kpisoft.emp.dac.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.dac.hibernate.*;
import com.kpisoft.emp.vo.*;
import com.canopus.mw.*;
import com.kpisoft.emp.vo.param.*;
import com.kpisoft.emp.utility.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import com.canopus.dac.utils.*;
import org.modelmapper.*;
import java.util.*;
import com.canopus.mw.dto.*;
import com.googlecode.genericdao.search.*;
import java.lang.reflect.*;
import org.slf4j.*;

@Component
public class ManagerLevelDataServiceImpl extends BaseDataAccessService implements ManagerLevelDataService
{
    @Autowired
    private ManagerLevelDao managerLevelDao;
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public ManagerLevelDataServiceImpl() {
        this.managerLevelDao = null;
        this.genericDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)ManagerLevelData.class, (Class)ManagerLevel.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    @Transactional
    public Response saveManagerLevel(final Request request) {
        final ManagerLevelData managerLevelData = (ManagerLevelData)request.get(EMPParams.EMP_MGR_LEVEL_DATA.name());
        if (managerLevelData == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        ManagerLevel managerLevel = null;
        if (managerLevelData.getId() != null && managerLevelData.getId() > 0) {
            managerLevel = (ManagerLevel)this.managerLevelDao.find((Serializable)managerLevelData.getId());
        }
        else {
            managerLevel = new ManagerLevel();
        }
        try {
            this.modelMapper.map((Object)managerLevelData, (Object)managerLevel);
            this.managerLevelDao.save(managerLevel);
            return this.OK(EMPParams.EMP_MGR_LEVEL_ID.name(), (BaseValueObject)new Identifier(managerLevel.getId()));
        }
        catch (Exception e) {
            ManagerLevelDataServiceImpl.log.error("Exception in ManagerLevelDataServiceImpl - saveManagerLevel() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_UNABLE_TO_CREATE_003.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getManagerLevel(final Request request) {
        final Identifier identifier = (Identifier)request.get(EMPParams.EMP_MGR_LEVEL_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        ManagerLevel managerLevel = null;
        try {
            managerLevel = (ManagerLevel)this.managerLevelDao.find((Serializable)identifier.getId());
        }
        catch (Exception e) {
            ManagerLevelDataServiceImpl.log.error("Exception in ManagerLevelDataServiceImpl - getManagerLevel() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
        if (managerLevel == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_DOES_NOT_EXIST_001.name(), "managerLevel id {0} does not exist.", new Object[] { identifier.getId() }));
        }
        final ManagerLevelData managerLevelData = (ManagerLevelData)this.modelMapper.map((Object)managerLevel, (Class)ManagerLevelData.class);
        return this.OK(EMPParams.EMP_MGR_LEVEL_DATA.name(), (BaseValueObject)managerLevelData);
    }
    
    @Transactional
    public Response updateManagerLevel(final Request request) {
        final ManagerLevelData managerLevelData = (ManagerLevelData)request.get(EMPParams.EMP_MGR_LEVEL_DATA.name());
        if (managerLevelData == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        ManagerLevel managerLevel = null;
        try {
            if (managerLevelData.getId() != null && managerLevelData.getId() > 0) {
                managerLevel = (ManagerLevel)this.managerLevelDao.find((Serializable)managerLevelData.getId());
                if (managerLevel == null) {
                    return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_DOES_NOT_EXIST_001.name(), "managerLevel id {0} does not exist.", new Object[] { managerLevelData.getId() }));
                }
                this.modelMapper.map((Object)managerLevelData, (Object)managerLevel);
            }
            this.managerLevelDao.save(managerLevel);
            return this.OK(EMPParams.EMP_MGR_LEVEL_ID.name(), (BaseValueObject)new Identifier(managerLevel.getId()));
        }
        catch (Exception e) {
            ManagerLevelDataServiceImpl.log.error("Exception in ManagerLevelDataServiceImpl - updateManagerLevel() : " + e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_UNABLE_TO_UPDATE_005.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response removeManagerLevel(final Request request) {
        final Identifier identifier = (Identifier)request.get(EMPParams.EMP_MGR_LEVEL_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final boolean isManagerLevel = this.managerLevelDao.removeById((Serializable)identifier.getId());
            final BooleanResponse res = new BooleanResponse(isManagerLevel);
            return this.OK(EMPParams.EMP_MGR_LEVEL_BOOLEAN_RESPONSE.name(), (BaseValueObject)res);
        }
        catch (Exception ex) {
            ManagerLevelDataServiceImpl.log.error("Exception in ManagerLevelDataServiceImpl - removeManagerLevel() : ", (Throwable)ex);
            throw new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_UNABLE_TO_DELETE_006.name(), "Failed to delete managerLevel.", (Throwable)ex);
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllManagerLevels(final Request request) {
        final SortList sortList = request.getSortList();
        final String defaultField = "name";
        try {
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.genericDao.getFilterFromExample((Object)new ManagerLevel(), options);
            final Search search = new Search((Class)ManagerLevel.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<ManagerLevel> result = (List<ManagerLevel>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<ManagerLevelData>>() {}.getType();
            final List<ManagerLevelData> managerLevelList = (List<ManagerLevelData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)managerLevelList);
            final Response response = this.OK(EMPParams.EMP_MGR_LEVEL_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            ManagerLevelDataServiceImpl.log.error("Exception in ManagerLevelDataServiceImpl - getAllManagerLevels() : " + e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_UNABLE_TO_GET_ALL_007.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchManagerLevel(final Request request) {
        final ManagerLevelData managerLevelData = (ManagerLevelData)request.get(EMPParams.EMP_MGR_LEVEL_DATA.name());
        if (managerLevelData == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        final SortList sortList = request.getSortList();
        final String defaultField = "name";
        try {
            final ManagerLevel managerLevel = (ManagerLevel)this.modelMapper.map((Object)managerLevelData, (Class)ManagerLevel.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.managerLevelDao.getFilterFromExample(managerLevel, options);
            final Search search = new Search((Class)ManagerLevel.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<ManagerLevel> result = (List<ManagerLevel>)this.managerLevelDao.search((ISearch)search);
            final Type listType = new TypeToken<List<ManagerLevelData>>() {}.getType();
            final List<ManagerLevelData> managerLevelList = (List<ManagerLevelData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)managerLevelList);
            final Response response = this.OK(EMPParams.EMP_MGR_LEVEL_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            ManagerLevelDataServiceImpl.log.error("Exception in ManagerLevelDataServiceImpl - searchManagerLevel() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_MGR_LEVEL_UNABLE_TO_SEARCH_008.name(), "Failed to load the managerLevel data", new Object[] { e.getMessage() }));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)ManagerLevelDataServiceImpl.class);
    }
}
