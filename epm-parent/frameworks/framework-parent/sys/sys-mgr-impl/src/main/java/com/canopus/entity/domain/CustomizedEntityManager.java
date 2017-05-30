package com.canopus.entity.domain;

import com.canopus.mw.*;
import org.springframework.stereotype.*;
import com.canopus.entity.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.cache.*;
import java.util.*;
import com.canopus.entity.vo.*;
import com.canopus.mw.dto.*;

@Component
public class CustomizedEntityManager extends BaseDomainManager
{
    @Autowired
    private CustomizedEntityDataService dataService;
    @Autowired
    @Qualifier("customEntityCache")
    private Cache<Integer, CustomEntityData> customEntityCache;
    @Autowired
    @Qualifier("dataTypeCache")
    private Cache<Integer, DataTypeBean> dataTypeCache;
    @Autowired
    @Qualifier("fieldTypeCache")
    private Cache<Integer, FieldTypeBean> fieldTypeCache;
    @Autowired
    @Qualifier("entityInstanceDataCache")
    private Cache<Integer, CustomEntityInstanceData> entityInstanceDataCache;
    
    public CustomizedEntityManager() {
        this.dataService = null;
        this.customEntityCache = null;
        this.dataTypeCache = null;
        this.fieldTypeCache = null;
        this.entityInstanceDataCache = null;
    }
    
    public CustomEntityData getCustomEntity(final Integer customEntityId) {
        final CustomEntityData data = (CustomEntityData)this.customEntityCache.get(customEntityId, (CacheLoader)new CustomEntityLoader());
        return data;
    }
    
    public List<CustomEntityData> getAllCustomEntity() {
        final Response response = this.dataService.getAllCustomEntity(new Request());
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(CustomEntityParams.CUSTOM_ENTITY_DATA_LIST.name());
        final List<CustomEntityData> data = (List<CustomEntityData>)bList.getValueObjectList();
        if (data != null && !data.isEmpty()) {
            for (final CustomEntityData iterator : data) {
                this.customEntityCache.put(iterator.getId(), iterator);
            }
        }
        return data;
    }
    
    public List<CustomEntityFieldsData> getCustomEntityFields(final Integer customEntityId) {
        final Request request = new Request();
        request.put(CustomEntityParams.CUSTOM_ENTITY_ID.name(), (BaseValueObject)new Identifier(customEntityId));
        final Response response = this.dataService.getCustomEntityFields(request);
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(CustomEntityParams.CUSTOM_ENTITY_FIELDS_LIST.name());
        final List<CustomEntityFieldsData> data = (List<CustomEntityFieldsData>)bList.getValueObjectList();
        return data;
    }
    
    public List<CustomEntityFieldsData> getCustomEntityFieldsByGroupId(final Integer groupId) {
        final Request request = new Request();
        request.put(CustomEntityParams.FIELD_GROUP_ID.name(), (BaseValueObject)new Identifier(groupId));
        final Response response = this.dataService.getCustomEntityFieldsByGroupId(request);
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(CustomEntityParams.CUSTOM_ENTITY_FIELDS_LIST.name());
        final List<CustomEntityFieldsData> data = (List<CustomEntityFieldsData>)bList.getValueObjectList();
        return data;
    }
    
    public List<CustomEntityFieldLangData> getCustomEntityFieldLangValues(final Integer customEntityId, final String locale) {
        final Request request = new Request();
        request.put(CustomEntityParams.CUSTOM_ENTITY_ID.name(), (BaseValueObject)new Identifier(customEntityId));
        final Name name = new Name();
        name.setName(locale);
        request.put(CustomEntityParams.LOCALE.name(), (BaseValueObject)name);
        final Response response = this.dataService.getCustomEntityFieldLangValues(request);
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(CustomEntityParams.ENTITY_FIELD_LANG_DATA_LIST.name());
        final List<CustomEntityFieldLangData> data = (List<CustomEntityFieldLangData>)bList.getValueObjectList();
        return data;
    }
    
    public DataTypeBean getDataType(final Integer dataTypeId) {
        final DataTypeBean dataType = (DataTypeBean)this.dataTypeCache.get(dataTypeId, (CacheLoader)new DataTypeLoader());
        return dataType;
    }
    
    public List<DataTypeBean> getAllDataTypes() {
        final Response response = this.dataService.getAllDataTypes(new Request());
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(CustomEntityParams.DATA_TYPE_LIST.name());
        final List<DataTypeBean> data = (List<DataTypeBean>)bList.getValueObjectList();
        if (data != null && !data.isEmpty()) {
            for (final DataTypeBean iterator : data) {
                this.dataTypeCache.put(iterator.getId(), iterator);
            }
        }
        return data;
    }
    
    public FieldTypeBean getFieldType(final Integer fieldType) {
        final FieldTypeBean data = (FieldTypeBean)this.fieldTypeCache.get(fieldType, (CacheLoader)new FieldTypeLoader());
        return data;
    }
    
    public List<FieldTypeBean> getAllFieldType() {
        final Response response = this.dataService.getAllFieldType(new Request());
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(CustomEntityParams.FIELD_TYPE_LIST.name());
        final List<FieldTypeBean> data = (List<FieldTypeBean>)bList.getValueObjectList();
        if (data != null && !data.isEmpty()) {
            for (final FieldTypeBean iterator : data) {
                this.fieldTypeCache.put(iterator.getId(), iterator);
            }
        }
        return data;
    }
    
    public CustomEntityInstanceData getEntityInstance(final Integer instanceId) {
        final CustomEntityInstanceData data = (CustomEntityInstanceData)this.entityInstanceDataCache.get(instanceId, (CacheLoader)new CustomEntityInstanceDataLoader());
        return data;
    }
    
    public List<CustomEntityInstanceData> getAllEntityInstanceByEntityID(final Integer entityId) {
        final Request request = new Request();
        request.put(CustomEntityParams.CUSTOM_ENTITY_ID.name(), (BaseValueObject)new Identifier(entityId));
        final Response response = this.dataService.getAllEntityInstanceByEntityID(request);
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_LIST.name());
        final List<CustomEntityInstanceData> data = (List<CustomEntityInstanceData>)bList.getValueObjectList();
        return data;
    }
    
    public Integer saveEntityInstanceFieldData(final CustomEntityInstanceFieldData data) {
        final Request request = new Request();
        request.put(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_FIELD_DATA.name(), (BaseValueObject)data);
        final Response response = this.dataService.saveEntityInstanceFieldData(request);
        final Identifier id = (Identifier)response.get(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_FIELD_ID.name());
        return id.getId();
    }
    
    public List<CustomEntityInstanceFieldData> getAllEntityInstanceFieldData(final Integer entityInstanceId) {
        final Request request = new Request();
        request.put(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_ID.name(), (BaseValueObject)new Identifier(entityInstanceId));
        final Response response = this.dataService.getAllEntityInstanceFieldData(new Request());
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_FIELD_LIST.name());
        final List<CustomEntityInstanceFieldData> data = (List<CustomEntityInstanceFieldData>)bList.getValueObjectList();
        return data;
    }
    
    public Integer createCustomEntityField(final CustomEntityFieldsData customEntityFieldsData) {
        final Request request = new Request();
        request.put(CustomEntityParams.CUSTOM_ENTITY_FIELD_DATA.name(), (BaseValueObject)customEntityFieldsData);
        final Response response = this.dataService.createCustomEntity(request);
        final Identifier entityFieldId = (Identifier)response.get(CustomEntityParams.CUSTOM_ENTITY_FIELD_ID.name());
        return entityFieldId.getId();
    }
    
    public Boolean removeCustomEntityField(final Integer id) {
        final Request request = new Request();
        request.put(CustomEntityParams.CUSTOM_ENTITY_FIELD_ID.name(), (BaseValueObject)new Identifier(id));
        final Response response = this.dataService.removeCustomEntityField(request);
        final BooleanResponse value = (BooleanResponse)response.get(CustomEntityParams.CUSTOM_ENTITY_FIELD_STATUS.name());
        return value.isResponse();
    }
    
    public Integer createCustomEntityInstance(final CustomEntityInstanceData customEntityInstance) {
        final Request request = new Request();
        request.put(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_DATA.name(), (BaseValueObject)customEntityInstance);
        final Response response = this.dataService.createCustomEntityInstance(request);
        final Identifier id = (Identifier)response.get(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_ID.name());
        return id.getId();
    }
    
    private class CustomEntityLoader implements CacheLoader<Integer, CustomEntityData>
    {
        public CustomEntityData load(final Integer key) {
            final Request request = new Request();
            request.put(CustomEntityParams.CUSTOM_ENTITY_ID.name(), (BaseValueObject)new Identifier(key));
            final Response response = CustomizedEntityManager.this.dataService.getCustomEntity(request);
            final CustomEntityData data = (CustomEntityData)response.get(CustomEntityParams.CUSTOM_ENTITY_DATA.name());
            return data;
        }
    }
    
    private class DataTypeLoader implements CacheLoader<Integer, DataTypeBean>
    {
        public DataTypeBean load(final Integer key) {
            final Request request = new Request();
            request.put(CustomEntityParams.DATA_TYPE_ID.name(), (BaseValueObject)new Identifier(key));
            final Response response = CustomizedEntityManager.this.dataService.getDataType(request);
            final DataTypeBean data = (DataTypeBean)response.get(CustomEntityParams.DATA_TYPE.name());
            return data;
        }
    }
    
    private class FieldTypeLoader implements CacheLoader<Integer, FieldTypeBean>
    {
        public FieldTypeBean load(final Integer key) {
            final Request request = new Request();
            request.put(CustomEntityParams.FIELD_TYPE_ID.name(), (BaseValueObject)new Identifier(key));
            final Response response = CustomizedEntityManager.this.dataService.getFieldType(request);
            final FieldTypeBean data = (FieldTypeBean)response.get(CustomEntityParams.FIELD_TYPE.name());
            return data;
        }
    }
    
    private class CustomEntityInstanceDataLoader implements CacheLoader<Integer, CustomEntityInstanceData>
    {
        public CustomEntityInstanceData load(final Integer key) {
            final Request request = new Request();
            request.put(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_ID.name(), (BaseValueObject)new Identifier(key));
            final Response response = CustomizedEntityManager.this.dataService.getEntityInstance(request);
            final CustomEntityInstanceData data = (CustomEntityInstanceData)response.get(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_DATA.name());
            return data;
        }
    }
}
