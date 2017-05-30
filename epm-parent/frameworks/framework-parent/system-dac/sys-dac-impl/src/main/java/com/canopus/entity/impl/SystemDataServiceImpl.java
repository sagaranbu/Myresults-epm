package com.canopus.entity.impl;

import com.canopus.entity.*;
import org.springframework.stereotype.*;
import com.canopus.entity.dao.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.dac.hibernate.*;
import org.modelmapper.*;
import com.canopus.mw.*;
import com.canopus.entity.vo.*;
import com.canopus.entity.domain.*;
import com.canopus.entity.vo.params.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import com.canopus.mw.dto.*;
import java.util.*;
import com.google.common.reflect.*;
import com.google.common.reflect.TypeToken;

import java.lang.reflect.*;
import com.googlecode.genericdao.search.*;
import org.slf4j.*;

@Service
public class SystemDataServiceImpl extends BaseDataAccessService implements SystemDataService
{
    @Autowired
    private SystemMasterBaseDao systemMasterBaseDao;
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public SystemDataServiceImpl() {
        this.systemMasterBaseDao = null;
        this.genericDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)SystemMasterBaseMetaData.class, (Class)SystemMasterBaseBean.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)SystemMasCategoryData.class, (Class)SystemMasCategory.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    @Transactional
    public Response createBaseData(final Request request) {
        final SystemMasterBaseBean systemMasterBaseBean = (SystemMasterBaseBean)request.get(SysParams.SYS_BASE_DATA.name());
        if (systemMasterBaseBean == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        SystemMasterBaseMetaData systemMasterBaseMetaData = null;
        if (systemMasterBaseBean.getId() != null && systemMasterBaseBean.getId() > 0) {
            systemMasterBaseMetaData = (SystemMasterBaseMetaData)this.systemMasterBaseDao.find((Serializable)systemMasterBaseBean.getId());
        }
        else {
            systemMasterBaseMetaData = new SystemMasterBaseMetaData();
        }
        try {
            this.modelMapper.map((Object)systemMasterBaseBean, (Object)systemMasterBaseMetaData);
            this.systemMasterBaseDao.save(systemMasterBaseMetaData);
            return this.OK(SysParams.SYS_BASE_ID.name(), (BaseValueObject)new Identifier(systemMasterBaseMetaData.getId()));
        }
        catch (Exception e) {
            SystemDataServiceImpl.log.error("Exception in SystemDataServiceImpl - createBaseData() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_UNABLE_TO_CREATE_003.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getBaseDataById(final Request request) {
        final Identifier identifier = (Identifier)request.get(SysParams.SYS_BASE_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        SystemMasterBaseMetaData systemMasterBaseMetaData = null;
        try {
            systemMasterBaseMetaData = (SystemMasterBaseMetaData)this.systemMasterBaseDao.find((Serializable)identifier.getId());
        }
        catch (Exception e) {
            SystemDataServiceImpl.log.error("Exception in SystemDataServiceImpl - getBaseDataById() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
        if (systemMasterBaseMetaData == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_DOES_NOT_EXIST_001.name(), "system id {0} does not exist.", new Object[] { identifier }));
        }
        final SystemMasterBaseBean systemMasterBaseBean = (SystemMasterBaseBean)this.modelMapper.map((Object)systemMasterBaseMetaData, (Class)SystemMasterBaseBean.class);
        return this.OK(SysParams.SYS_BASE_DATA.name(), (BaseValueObject)systemMasterBaseBean);
    }
    
    @Transactional(readOnly = true)
    public Response getAllBaseData(final Request request) {
        final List<SystemMasterBaseBean> result = new ArrayList<SystemMasterBaseBean>();
        try {
            final List<SystemMasterBaseMetaData> data = (List<SystemMasterBaseMetaData>)this.systemMasterBaseDao.findAll();
            for (final SystemMasterBaseMetaData iterator : data) {
                result.add((SystemMasterBaseBean)this.modelMapper.map((Object)iterator, (Class)SystemMasterBaseBean.class));
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(SysParams.SYS_BASE_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            SystemDataServiceImpl.log.error("Exception in SystemDataServiceImpl - getAllBaseData() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_UNABLE_TO_GET_ALL_007.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response removeBaseDataById(final Request request) {
        final Identifier id = (Identifier)request.get(SysParams.SYS_BASE_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final boolean status = this.genericDao.removeById((Class)SystemMasterBaseMetaData.class, (Serializable)id.getId());
            return this.OK(SysParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            SystemDataServiceImpl.log.error("Exception in SystemDataServiceImpl - removeBaseDataById() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_UNABLE_TO_DELETE_006.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getBaseDataForCategory(final Request request) {
        final Identifier identifier = (Identifier)request.get(SysParams.SYS_CATEGORY_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        final Search search = new Search();
        search.addFilterEqual("category.id", (Object)identifier.getId());
        search.addFetch("langs");
        search.addFetch("category");
        try {
            final List<SystemMasterBaseMetaData> result = (List<SystemMasterBaseMetaData>)this.systemMasterBaseDao.search((ISearch)search);
            final List<SystemMasterBaseBean> data = new ArrayList<SystemMasterBaseBean>();
            for (final SystemMasterBaseMetaData iterator : result) {
                data.add((SystemMasterBaseBean)this.modelMapper.map((Object)iterator, (Class)SystemMasterBaseBean.class));
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(SysParams.SYS_BASE_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            SystemDataServiceImpl.log.error("Exception in SystemDataServiceImpl - getBaseDataForCategory() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getBaseDataForBothCategories(final Request request) {
        final Identifier categoryidentifier = (Identifier)request.get(SysParams.SYS_CATEGORY_ID.name());
        final Identifier subcatidegoryentifier = (Identifier)request.get(SysParams.SYS_SUB_CATEGORY_ID.name());
        if (categoryidentifier == null || subcatidegoryentifier == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Search search = new Search();
            search.addFilterEqual("category.id", (Object)categoryidentifier.getId());
            search.addFilterEqual("subCategory.id", (Object)subcatidegoryentifier.getId());
            final List<SystemMasterBaseMetaData> result = (List<SystemMasterBaseMetaData>)this.systemMasterBaseDao.search((ISearch)search);
            final List<SystemMasterBaseBean> data = new ArrayList<SystemMasterBaseBean>();
            for (final SystemMasterBaseMetaData iterator : result) {
                data.add((SystemMasterBaseBean)this.modelMapper.map((Object)iterator, (Class)SystemMasterBaseBean.class));
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(SysParams.SYS_BASE_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            SystemDataServiceImpl.log.error("Exception in SystemDataServiceImpl - getBaseDataForBothCategories() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getSysMasterByIds(final Request req) {
        final StringIdentifier ids = (StringIdentifier)req.get(SysParams.SYS_BASE_IDS.name());
        if (ids == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final String[] arr = ids.getId().split(",");
            final List<Integer> idsList = new ArrayList<Integer>();
            for (final String id : arr) {
                idsList.add(Integer.parseInt(id));
            }
            final Search search = new Search((Class)SystemMasterBaseMetaData.class);
            search.addFilterIn("id", (Collection)idsList);
            final List<SystemMasterBaseMetaData> SysMstrList = (List<SystemMasterBaseMetaData>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<SystemMasterBaseBean>>() {}.getType();
            final List<SystemMasterBaseBean> sysMasterList = (List<SystemMasterBaseBean>)this.modelMapper.map((Object)SysMstrList, listType);
            final BaseValueObjectList bvol = new BaseValueObjectList();
            bvol.setValueObjectList((List)sysMasterList);
            return this.OK(SysParams.SYS_BASE_DATA_LIST.name(), (BaseValueObject)bvol);
        }
        catch (Exception e) {
            SystemDataServiceImpl.log.error("Exception in SystemDataServiceImpl - getSysMasterByIds() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchCatagory(final Request req) {
        final SystemMasCategoryData categoryData = (SystemMasCategoryData)req.get(SysParams.SYS_CATEGORY_DATA.name());
        if (categoryData == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final SystemMasCategory category = (SystemMasCategory)this.modelMapper.map((Object)categoryData, (Class)SystemMasCategory.class);
            final Filter filter = this.genericDao.getFilterFromExample((Object)category);
            final Search search = new Search((Class)SystemMasCategory.class);
            search.addFilter(filter);
            final List<SystemMasCategory> result = (List<SystemMasCategory>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<SystemMasCategoryData>>() {}.getType();
            final List<SystemMasCategoryData> categoryList = (List<SystemMasCategoryData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList bvol = new BaseValueObjectList();
            bvol.setValueObjectList((List)categoryList);
            return this.OK(SysParams.SYS_CATEGORY_DATA_LIST.name(), (BaseValueObject)bvol);
        }
        catch (Exception e) {
            SystemDataServiceImpl.log.error("Exception in SystemDataServiceImpl - searchCatagory() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchMasterBase(final Request request) {
        final SystemMasterBaseBean masterBaseData = (SystemMasterBaseBean)request.get(SysParams.SYS_BASE_DATA.name());
        if (masterBaseData == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final SystemMasterBaseMetaData masterBaseMetaData = (SystemMasterBaseMetaData)this.modelMapper.map((Object)masterBaseData, (Class)SystemMasterBaseMetaData.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.genericDao.getFilterFromExample((Object)masterBaseMetaData, options);
            final Search search = new Search((Class)SystemMasterBaseMetaData.class);
            search.addFilter(filter);
            final List<SystemMasterBaseMetaData> result = (List<SystemMasterBaseMetaData>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<SystemMasterBaseBean>>() {}.getType();
            final List<SystemMasterBaseBean> masterBaseDataList = (List<SystemMasterBaseBean>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList baseValueObjList = new BaseValueObjectList();
            baseValueObjList.setValueObjectList((List)masterBaseDataList);
            return this.OK(SysParams.SYS_BASE_DATA_LIST.name(), (BaseValueObject)baseValueObjList);
        }
        catch (Exception e) {
            SystemDataServiceImpl.log.error("Exception in SystemDataServiceImpl - searchMasterBase() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_MASTER_BASE_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)SystemDataServiceImpl.class);
    }
}
