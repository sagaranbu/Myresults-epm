package com.canopus.entity.impl;

import com.canopus.entity.*;
import org.springframework.stereotype.*;
import com.canopus.dac.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import org.modelmapper.*;
import com.canopus.mw.*;
import com.canopus.entity.domain.*;
import com.canopus.entity.vo.*;
import com.canopus.entity.vo.params.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import java.util.*;
import com.googlecode.genericdao.search.*;
import com.canopus.mw.dto.*;

@Component
public class CustomizedEntityDataServiceImpl extends BaseDataAccessService implements CustomizedEntityDataService
{
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    
    public CustomizedEntityDataServiceImpl() {
        this.genericDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)CustomEntity.class, (Class)CustomEntityData.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)CustomEntityFields.class, (Class)CustomEntityFieldsData.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)CustomEntityFieldLang.class, (Class)CustomEntityFieldLangData.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)DataType.class, (Class)DataTypeBean.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)FieldType.class, (Class)FieldTypeBean.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)CustomEntityInstance.class, (Class)CustomEntityInstanceData.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)CustomEntityInstanceFieldData.class, (Class)CustomEntityInstanceFieldValue.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    @Transactional(readOnly = true)
    public Response getCustomEntity(final Request request) {
        final Identifier identifier = (Identifier)request.get(CustomEntityParams.CUSTOM_ENTITY_ID.getParamName());
        CustomEntity customEntity = null;
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            customEntity = (CustomEntity)this.genericDao.find((Class)CustomEntity.class, (Serializable)identifier.getId());
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_UNABLE_TO_GET_004.name(), ex.getMessage(), (Throwable)ex));
        }
        if (customEntity == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_DOES_NOT_EXIST_001.name(), "Custom entity does not exists with id = {0}", new Object[] { identifier.getId() }));
        }
        final CustomEntityData data = (CustomEntityData)this.modelMapper.map((Object)customEntity, (Class)CustomEntityData.class);
        return this.OK(CustomEntityParams.CUSTOM_ENTITY_DATA.name(), (BaseValueObject)data);
    }
    
    @Transactional(readOnly = true)
    public Response getAllCustomEntity(final Request request) {
        List<CustomEntity> data = null;
        final List<CustomEntityData> result = new ArrayList<CustomEntityData>();
        try {
            data = (List<CustomEntity>)this.genericDao.findAll((Class)CustomEntity.class);
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_UNABLE_TO_GET_ALL_007.name(), ex.getMessage(), (Throwable)ex));
        }
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_DOES_NOT_EXIST_001.name(), "Custom entity does not exists"));
        }
        for (final CustomEntity iterator : data) {
            result.add((CustomEntityData)this.modelMapper.map((Object)iterator, (Class)CustomEntityData.class));
        }
        final BaseValueObjectList bList = new BaseValueObjectList();
        bList.setValueObjectList((List)result);
        return this.OK(CustomEntityParams.CUSTOM_ENTITY_DATA_LIST.name(), (BaseValueObject)bList);
    }
    
    @Transactional(readOnly = true)
    public Response getCustomEntityFields(final Request request) {
        final Identifier identifier = (Identifier)request.get(CustomEntityParams.CUSTOM_ENTITY_ID.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        List<CustomEntityFields> fields = null;
        final List<CustomEntityFieldsData> result = new ArrayList<CustomEntityFieldsData>();
        try {
            final Search search = new Search((Class)CustomEntityFields.class);
            search.addFilterEqual("customEntityId", (Object)identifier.getId());
            search.addSortAsc("displayOrderId");
            search.addFetch("customLangData");
            fields = (List<CustomEntityFields>)this.genericDao.search((ISearch)search);
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_UNABLE_TO_GET_004.name(), ex.getMessage(), (Throwable)ex));
        }
        if (fields == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_DOES_NOT_EXIST_001.name(), "Custom entity fields does not exists with id = {0}", new Object[] { identifier.getId() }));
        }
        for (final CustomEntityFields iterator : fields) {
            result.add((CustomEntityFieldsData)this.modelMapper.map((Object)iterator, (Class)CustomEntityFieldsData.class));
        }
        final BaseValueObjectList bList = new BaseValueObjectList();
        bList.setValueObjectList((List)result);
        return this.OK(CustomEntityParams.CUSTOM_ENTITY_FIELDS_LIST.name(), (BaseValueObject)bList);
    }
    
    @Transactional(readOnly = true)
    public Response getCustomEntityFieldsByGroupId(final Request request) {
        final Identifier identifier = (Identifier)request.get(CustomEntityParams.FIELD_GROUP_ID.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        List<CustomEntityFields> fields = null;
        final List<CustomEntityFieldsData> result = new ArrayList<CustomEntityFieldsData>();
        try {
            final Search search = new Search((Class)CustomEntityFields.class);
            search.addFilterEqual("groupId", (Object)identifier.getId());
            search.addSortAsc("displayOrderId");
            search.addFetch("customLangData");
            fields = (List<CustomEntityFields>)this.genericDao.search((ISearch)search);
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_UNKNOWN_EXCEPTION_000.name(), ex.getMessage(), (Throwable)ex));
        }
        if (fields == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_DOES_NOT_EXIST_001.name(), "Custom entity fields does not exists with group id = {0}", new Object[] { identifier.getId() }));
        }
        for (final CustomEntityFields iterator : fields) {
            result.add((CustomEntityFieldsData)this.modelMapper.map((Object)iterator, (Class)CustomEntityFieldsData.class));
        }
        final BaseValueObjectList bList = new BaseValueObjectList();
        bList.setValueObjectList((List)result);
        return this.OK(CustomEntityParams.CUSTOM_ENTITY_FIELDS_LIST.name(), (BaseValueObject)bList);
    }
    
    @Transactional(readOnly = true)
    public Response getCustomEntityFieldLangValues(final Request request) {
        final Identifier customEntityId = (Identifier)request.get(CustomEntityParams.CUSTOM_ENTITY_ID.getParamName());
        final Name locale = (Name)request.get(CustomEntityParams.LOCALE.getParamName());
        if (customEntityId == null || customEntityId.getId() == null || customEntityId.getId() <= 0 || locale == null || locale.getName() == null || locale.getName().trim().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        List<CustomEntityFieldLang> fields = null;
        final List<CustomEntityFieldLangData> result = new ArrayList<CustomEntityFieldLangData>();
        try {
            final Search search = new Search((Class)CustomEntityFieldLang.class);
            search.addFilterEqual("locale", (Object)locale);
            search.addFilterEqual("entityFieldId.id", (Object)customEntityId.getId());
            search.addSortAsc("entityFieldId.displayOrderId");
            fields = (List<CustomEntityFieldLang>)this.genericDao.search((ISearch)search);
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_UNKNOWN_EXCEPTION_000.name(), ex.getMessage(), (Throwable)ex));
        }
        if (fields == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_DOES_NOT_EXIST_001.name(), "Custom entity field lang values does not exists"));
        }
        for (final CustomEntityFieldLang iterator : fields) {
            result.add((CustomEntityFieldLangData)this.modelMapper.map((Object)iterator, (Class)CustomEntityFieldLangData.class));
        }
        final BaseValueObjectList bList = new BaseValueObjectList();
        bList.setValueObjectList((List)result);
        return this.OK(CustomEntityParams.ENTITY_FIELD_LANG_DATA_LIST.name(), (BaseValueObject)bList);
    }
    
    @Transactional(readOnly = true)
    public Response getDataType(final Request request) {
        final Identifier dataTypeId = (Identifier)request.get(CustomEntityParams.DATA_TYPE_ID.getParamName());
        DataType value = null;
        if (dataTypeId == null || dataTypeId.getId() == null || dataTypeId.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_DATA_TYPE_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            value = (DataType)this.genericDao.find((Class)DataType.class, (Serializable)dataTypeId.getId());
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_DATA_TYPE_UNKNOWN_EXCEPTION_000.name(), "Custom entity field lang values does not exists"));
        }
        final DataTypeBean dataTypeBean = (DataTypeBean)this.modelMapper.map((Object)value, (Class)DataTypeBean.class);
        return this.OK(CustomEntityParams.DATA_TYPE.name(), (BaseValueObject)dataTypeBean);
    }
    
    @Transactional(readOnly = true)
    public Response getAllDataTypes(final Request request) {
        List<DataType> data = null;
        final List<DataTypeBean> result = new ArrayList<DataTypeBean>();
        try {
            data = (List<DataType>)this.genericDao.findAll((Class)DataType.class);
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_DATA_TYPE_UNKNOWN_EXCEPTION_000.name(), ex.getMessage(), (Throwable)ex));
        }
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_DATA_TYPE_DOES_NOT_EXIST_001.name(), "data types does not exist"));
        }
        for (final DataType iterator : data) {
            result.add((DataTypeBean)this.modelMapper.map((Object)iterator, (Class)DataTypeBean.class));
        }
        final BaseValueObjectList bList = new BaseValueObjectList();
        bList.setValueObjectList((List)result);
        return this.OK(CustomEntityParams.DATA_TYPE_LIST.name(), (BaseValueObject)bList);
    }
    
    @Transactional(readOnly = true)
    public Response getFieldType(final Request request) {
        final Identifier fieldTypeId = (Identifier)request.get(CustomEntityParams.FIELD_TYPE_ID.getParamName());
        FieldType value = null;
        if (fieldTypeId == null || fieldTypeId.getId() == null || fieldTypeId.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_FIELD_TYPE_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            value = (FieldType)this.genericDao.find((Class)FieldType.class, (Serializable)fieldTypeId.getId());
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_FIELD_TYPE_UNKNOWN_EXCEPTION_000.name(), ex.getMessage(), (Throwable)ex));
        }
        final FieldTypeBean fieldTypeBean = (FieldTypeBean)this.modelMapper.map((Object)value, (Class)FieldTypeBean.class);
        return this.OK(CustomEntityParams.FIELD_TYPE.name(), (BaseValueObject)fieldTypeBean);
    }
    
    @Transactional(readOnly = true)
    public Response getAllFieldType(final Request request) {
        List<FieldType> data = null;
        final List<FieldTypeBean> result = new ArrayList<FieldTypeBean>();
        try {
            data = (List<FieldType>)this.genericDao.findAll((Class)FieldType.class);
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_FIELD_TYPE_UNKNOWN_EXCEPTION_000.name(), ex.getMessage(), (Throwable)ex));
        }
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_FIELD_TYPE_DOES_NOT_EXIST_001.name(), "data types doesnot exists"));
        }
        for (final FieldType iterator : data) {
            result.add((FieldTypeBean)this.modelMapper.map((Object)iterator, (Class)FieldTypeBean.class));
        }
        final BaseValueObjectList bList = new BaseValueObjectList();
        bList.setValueObjectList((List)result);
        return this.OK(CustomEntityParams.FIELD_TYPE_LIST.name(), (BaseValueObject)bList);
    }
    
    @Transactional(readOnly = true)
    public Response getEntityInstance(final Request request) {
        final Identifier instanceId = (Identifier)request.get(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_ID.getParamName());
        CustomEntityInstance data = null;
        if (instanceId == null || instanceId.getId() == null || instanceId.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            data = (CustomEntityInstance)this.genericDao.find((Class)CustomEntityInstance.class, (Serializable)instanceId.getId());
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_UNABLE_TO_GET_004.name(), ex.getMessage(), (Throwable)ex));
        }
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_DOES_NOT_EXIST_001.name(), "Custom entity instance does not exists with id = {0}", new Object[] { instanceId.getId() }));
        }
        final CustomEntityInstanceData result = (CustomEntityInstanceData)this.modelMapper.map((Object)data, (Class)CustomEntityInstanceData.class);
        return this.OK(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_DATA.name(), (BaseValueObject)result);
    }
    
    @Transactional(readOnly = true)
    public Response getAllEntityInstanceByEntityID(final Request request) {
        final Identifier entityId = (Identifier)request.get(CustomEntityParams.CUSTOM_ENTITY_ID.getParamName());
        if (entityId == null || entityId.getId() == null || entityId.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        List<CustomEntityInstance> data = null;
        final List<CustomEntityInstanceData> result = new ArrayList<CustomEntityInstanceData>();
        try {
            final Search search = new Search((Class)CustomEntityInstance.class);
            search.addFilterEqual("entityId", (Object)entityId.getId());
            search.addFetch("fields");
            data = (List<CustomEntityInstance>)this.genericDao.search((ISearch)search);
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_UNABLE_TO_GET_ALL_007.name(), ex.getMessage(), (Throwable)ex));
        }
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_DOES_NOT_EXIST_001.name(), "Custom entity instance does not exists"));
        }
        for (final CustomEntityInstance iterator : data) {
            result.add((CustomEntityInstanceData)this.modelMapper.map((Object)iterator, (Class)CustomEntityInstanceData.class));
        }
        final BaseValueObjectList bList = new BaseValueObjectList();
        bList.setValueObjectList((List)result);
        return this.OK(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_LIST.name(), (BaseValueObject)bList);
    }
    
    @Transactional
    public Response saveEntityInstanceFieldData(final Request request) {
        final CustomEntityInstanceFieldData data = (CustomEntityInstanceFieldData)request.get(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_FIELD_DATA.getParamName());
        CustomEntityInstanceFieldValue field = null;
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            if (data.getId() != null) {
                field = (CustomEntityInstanceFieldValue)this.genericDao.find((Class)CustomEntityInstanceFieldValue.class, (Serializable)data.getId());
            }
            else {
                field = new CustomEntityInstanceFieldValue();
            }
            this.modelMapper.map((Object)data, (Object)field);
            this.genericDao.save((Object)field);
            return this.OK(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_FIELD_ID.name(), (BaseValueObject)new Identifier(field.getId()));
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_FIELD_UNABLE_TO_CREATE_003.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllEntityInstanceFieldData(final Request request) {
        final Identifier identifier = (Identifier)request.get(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_ID.getParamName());
        List<CustomEntityInstanceFieldValue> field = null;
        final List<CustomEntityInstanceFieldData> result = new ArrayList<CustomEntityInstanceFieldData>();
        try {
            field = (List<CustomEntityInstanceFieldValue>)this.genericDao.findAll((Class)CustomEntityInstanceFieldValue.class);
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_FIELD_UNABLE_TO_GET_ALL_007.name(), ex.getMessage(), (Throwable)ex));
        }
        if (field == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_FIELD_DOES_NOT_EXIST_001.name(), "Entity instance fields doesnot exists for entity instance {0}", new Object[] { identifier.getId() }));
        }
        for (final CustomEntityInstanceFieldValue iterator : field) {
            result.add((CustomEntityInstanceFieldData)this.modelMapper.map((Object)iterator, (Class)CustomEntityInstanceFieldData.class));
        }
        final BaseValueObjectList bList = new BaseValueObjectList();
        bList.setValueObjectList((List)result);
        return this.OK(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_FIELD_LIST.name(), (BaseValueObject)bList);
    }
    
    @Transactional
    public Response createCustomEntity(final Request request) {
        final CustomEntityFieldsData customEntityFieldsData = (CustomEntityFieldsData)request.get(CustomEntityParams.CUSTOM_ENTITY_FIELD_DATA.getParamName());
        CustomEntityFields entityFields = null;
        if (customEntityFieldsData == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            entityFields = new CustomEntityFields();
            this.modelMapper.map((Object)customEntityFieldsData, (Object)entityFields);
            this.genericDao.save((Object)entityFields);
            return this.OK(CustomEntityParams.CUSTOM_ENTITY_FIELD_ID.name(), (BaseValueObject)new Identifier(entityFields.getId()));
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_UNABLE_TO_CREATE_003.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response removeCustomEntityField(final Request request) {
        final Identifier id = (Identifier)request.get(CustomEntityParams.CUSTOM_ENTITY_FIELD_ID.getParamName());
        if (id == null || id.getId() == null || id.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        boolean status = false;
        try {
            status = this.genericDao.removeById((Class)CustomEntityFields.class, (Serializable)id.getId());
        }
        catch (Exception e) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_UNABLE_TO_DELETE_006.name(), e.getMessage(), (Throwable)e));
        }
        return this.OK(CustomEntityParams.CUSTOM_ENTITY_FIELD_STATUS.name(), (BaseValueObject)new BooleanResponse(status));
    }
    
    @Transactional
    public Response createCustomEntityInstance(final Request request) {
        final CustomEntityInstanceData data = (CustomEntityInstanceData)request.get(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        CustomEntityInstance entityInstance = null;
        if (data.getId() != null) {
            entityInstance = (CustomEntityInstance)this.genericDao.find((Class)CustomEntityInstance.class, (Serializable)data.getId());
        }
        else {
            entityInstance = new CustomEntityInstance();
        }
        try {
            this.modelMapper.map((Object)data, (Object)entityInstance);
            this.genericDao.save((Object)entityInstance);
            return this.OK(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_ID.getParamName(), (BaseValueObject)new Identifier(entityInstance.getId()));
        }
        catch (Exception e) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_UNABLE_TO_CREATE_003.name(), e.getMessage(), (Throwable)e));
        }
    }
}
