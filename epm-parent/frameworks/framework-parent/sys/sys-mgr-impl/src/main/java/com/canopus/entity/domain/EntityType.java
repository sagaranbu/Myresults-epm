package com.canopus.entity.domain;

import com.canopus.mw.*;
import org.springframework.stereotype.*;
import com.canopus.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import org.perf4j.*;
import javax.annotation.*;
import java.util.*;
import com.canopus.entity.vo.*;
import com.canopus.mw.dto.*;

@Component
public class EntityType extends BaseDomainObject
{
    @Autowired
    private EntityTypeDataService dataService;
    private Map<Integer, EntityFieldData> entityFieldCacheMap;
    private Map<Integer, BaseEntityBean> baseEntityCacheMap;
    private static final Logger log;
    
    public EntityType() {
        this.dataService = null;
        this.entityFieldCacheMap = null;
        this.baseEntityCacheMap = null;
        this.entityFieldCacheMap = new HashMap<Integer, EntityFieldData>();
        this.baseEntityCacheMap = new HashMap<Integer, BaseEntityBean>();
    }
    
    @PostConstruct
    public void loadAllEntities() {
        EntityType.log.info((Object)"Initializing entity field metadata");
        final StopWatch sw = new StopWatch();
        ExecutionContext.getCurrent().setCrossTenant();
        try {
            this.entityFieldCacheMap.clear();
            this.baseEntityCacheMap.clear();
            sw.start();
            final Response response = this.dataService.getAllEntityFields(new Request());
            sw.stop();
            EntityType.log.debug((Object)("Initialization took " + sw.getElapsedTime() + " ms"));
            final BaseValueObjectList bList = (BaseValueObjectList)response.get(EntityTypeParams.ENTITY_FIELD_DATA_LIST.name());
            final List<EntityFieldData> result = (List<EntityFieldData>)bList.getValueObjectList();
            if (result != null && !result.isEmpty()) {
                for (final EntityFieldData iterator : result) {
                    this.entityFieldCacheMap.put(iterator.getId(), iterator);
                }
            }
        }
        catch (Exception e) {
            EntityType.log.error((Object)("Exception in EntityType - loadAllEntities() : " + e));
        }
        ExecutionContext.getCurrent().unSetCrossTenant();
    }
    
    public int createOrUpdateEntityField(final EntityFieldData data) {
        final Request request = new Request();
        request.put(EntityTypeParams.ENTITY_FIELD_DATA.name(), (BaseValueObject)data);
        final Response response = this.dataService.createEntityField(request);
        final Identifier id = (Identifier)response.get(EntityTypeParams.ENTITY_FIELD_IDENTIFIER.name());
        final EntityFieldData fieldData = this.load(id.getId());
        this.entityFieldCacheMap.put(fieldData.getId(), fieldData);
        return id.getId();
    }
    
    public EntityFieldData getEntityField(final int fieldId) {
        EntityFieldData data = this.entityFieldCacheMap.get(fieldId);
        if (data == null) {
            data = this.load(fieldId);
            this.entityFieldCacheMap.put(data.getId(), data);
        }
        return data;
    }
    
    public List<EntityFieldData> getAllEntityFields() {
        final List<EntityFieldData> result = new ArrayList<EntityFieldData>(this.entityFieldCacheMap.values());
        return result;
    }
    
    public List<EntityFieldData> getEntityFieldsForEntityType(final int entityTypeId) {
        final List<EntityFieldData> result = new ArrayList<EntityFieldData>();
        final List<EntityFieldData> data = new ArrayList<EntityFieldData>(this.entityFieldCacheMap.values());
        for (final EntityFieldData iterator : data) {
            if (iterator.getEntityType() == entityTypeId) {
                result.add(iterator);
            }
        }
        return result;
    }
    
    public boolean removeEntityField(final int fieldId) {
        final Request request = new Request();
        request.put(EntityTypeParams.ENTITY_FIELD_IDENTIFIER.name(), (BaseValueObject)new Identifier(fieldId));
        final Response response = this.dataService.removeEntityField(request);
        final BooleanResponse bResponse = (BooleanResponse)response.get(EntityTypeParams.STATUS.name());
        final boolean status = bResponse.isResponse();
        if (status) {
            this.entityFieldCacheMap.remove(fieldId);
        }
        return status;
    }
    
    public List<EntityFieldLangData> getEntityFieldsForEntityTypeByLocale(final int entityTypeId, final String locale) {
        final Request request = new Request();
        request.put(EntityTypeParams.ENTITY_TYPE_IDENTIFIER.name(), (BaseValueObject)new Identifier(entityTypeId));
        request.put(EntityTypeParams.LOCALE_NAME.name(), (BaseValueObject)new StringIdentifier(locale));
        final Response response = this.dataService.getEntityFieldsForEntityTypeByLocale(request);
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(EntityTypeParams.ENTITY_FIELD_LANG_DATA.name());
        final List<EntityFieldLangData> result = (List<EntityFieldLangData>)bList.getValueObjectList();
        return result;
    }
    
    public List<EntityRelationshipBean> searchOnEntityRelationship(final EntityRelationshipBean entityRelBean) {
        final Request request = new Request();
        request.put(EntityTypeParams.ENTITY_REL_DATA.name(), (BaseValueObject)entityRelBean);
        final Response response = this.dataService.searchOnEntityRelationship(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(EntityTypeParams.ENTITY_REL_LIST.name());
        final List<EntityRelationshipBean> eRelList = (List<EntityRelationshipBean>)list.getValueObjectList();
        return eRelList;
    }
    
    public List<BaseEntityBean> searchOnEntity(final BaseEntityBean bEBean, final Page page, final SortList sortList) {
        final Request request = new Request();
        request.put(EntityTypeParams.ENTITY_DATA.name(), (BaseValueObject)bEBean);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.searchOnEntity(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(EntityTypeParams.ENTITY_DATA_LIST.name());
        final List<BaseEntityBean> eRelList = (List<BaseEntityBean>)list.getValueObjectList();
        return eRelList;
    }
    
    public EntityFieldData load(final Integer key) {
        final Request request = new Request();
        request.put(EntityTypeParams.ENTITY_FIELD_IDENTIFIER.name(), (BaseValueObject)new Identifier(key));
        final Response response = this.dataService.getEntityField(request);
        final EntityFieldData data = (EntityFieldData)response.get(EntityTypeParams.ENTITY_FIELD_DATA.name());
        return data;
    }
    
    public BaseEntityBean loadBaseEntity(final Integer key) {
        final Request request = new Request();
        request.put(EntityTypeParams.ENTITY_IDENTIFIER.name(), (BaseValueObject)new Identifier(key));
        final Response response = this.dataService.getBaseEntity(request);
        final BaseEntityBean data = (BaseEntityBean)response.get(EntityTypeParams.ENTITY_DATA.name());
        return data;
    }
    
    public int createOrUpdateBaseEntity(final BaseEntityBean data) {
        final Request request = new Request();
        request.put(EntityTypeParams.ENTITY_DATA.name(), (BaseValueObject)data);
        final Response response = this.dataService.createBaseEntity(request);
        final Identifier id = (Identifier)response.get(EntityTypeParams.ENTITY_IDENTIFIER.name());
        return id.getId();
    }
    
    public BaseEntityBean getBaseEntity(final int entityId) {
        BaseEntityBean baseEntityBean = this.baseEntityCacheMap.get(entityId);
        if (baseEntityBean == null) {
            baseEntityBean = this.loadBaseEntity(entityId);
            this.baseEntityCacheMap.put(baseEntityBean.getId(), baseEntityBean);
        }
        return baseEntityBean;
    }
    
    public boolean removeBaseEntity(final int entityId) {
        final Request request = new Request();
        request.put(EntityTypeParams.ENTITY_IDENTIFIER.name(), (BaseValueObject)new Identifier(entityId));
        final Response response = this.dataService.removeBaseEntity(request);
        final BooleanResponse bResponse = (BooleanResponse)response.get(EntityTypeParams.STATUS.name());
        final boolean status = bResponse.isResponse();
        if (status) {
            this.baseEntityCacheMap.remove(entityId);
        }
        return status;
    }
    
    static {
        log = Logger.getLogger((Class)EntityType.class);
    }
}
