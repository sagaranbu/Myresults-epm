package com.canopus.entity.impl;

import com.canopus.entity.*;
import org.springframework.stereotype.*;
import com.canopus.dac.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.entity.dao.*;
import org.modelmapper.*;
import org.apache.log4j.*;
import com.canopus.mw.*;
import com.canopus.entity.vo.params.*;
import com.canopus.dac.*;
import org.springframework.transaction.annotation.*;
import java.io.*;
import java.util.*;
import com.googlecode.genericdao.search.*;
import com.canopus.dac.utils.*;
import com.canopus.mw.dto.*;
import com.canopus.entity.domain.*;
import com.canopus.entity.vo.*;

@Component
public class EntityTypeDataServiceImpl extends BaseDataAccessService implements EntityTypeDataService
{
    @Autowired
    private GenericHibernateDao genericDao;
    @Autowired
    private EntityTypeDao entityTypeDao;
    @Autowired
    private BaseEntityDao baseEntityDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public EntityTypeDataServiceImpl() {
        this.genericDao = null;
        this.entityTypeDao = null;
        this.baseEntityDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)EntityFieldData.class, (Class)EntityField.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)EntityFieldLangData.class, (Class)EntityFieldLang.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)EntityRelationshipBean.class, (Class)EntityRelationship.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)BaseEntityBean.class, (Class)BaseEntity.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    @Transactional
    public Response createEntityField(final Request request) {
        final EntityFieldData data = (EntityFieldData)request.get(EntityTypeParams.ENTITY_FIELD_DATA.getParamName());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        EntityField entityField = null;
        try {
            entityField = new EntityField();
            this.modelMapper.map((Object)data, (Object)entityField);
            entityField.setTenantId(ExecutionContext.getTenantId());
            this.entityTypeDao.merge(entityField);
            return this.OK(EntityTypeParams.ENTITY_FIELD_IDENTIFIER.getParamName(), (BaseValueObject)new Identifier(entityField.getId()));
        }
        catch (Exception ex) {
            EntityTypeDataServiceImpl.log.error((Object)"Exception in EntityTypeDataServiceImpl - createEntityField() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_ENTITY_FIELD_UNABLE_TO_CREATE_003.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getEntityField(final Request request) {
        final Identifier fieldId = (Identifier)request.get(EntityTypeParams.ENTITY_FIELD_IDENTIFIER.name());
        EntityField entityField = null;
        if (fieldId == null || fieldId.getId() == null || fieldId.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            entityField = (EntityField)this.entityTypeDao.find((Serializable)fieldId.getId());
        }
        catch (Exception ex) {
            EntityTypeDataServiceImpl.log.error((Object)"Exception in EntityTypeDataServiceImpl - getEntityField() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_ENTITY_FIELD_INVALID_INPUT_002.name(), ex.getMessage(), (Throwable)ex));
        }
        if (entityField == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_ENTITY_FIELD_DOES_NOT_EXIST_001.name(), "Entity field does not exists with id = {0}", new Object[] { fieldId.getId() }));
        }
        final EntityFieldData data = (EntityFieldData)this.modelMapper.map((Object)entityField, (Class)EntityFieldData.class);
        return this.OK(EntityTypeParams.ENTITY_FIELD_DATA.getParamName(), (BaseValueObject)data);
    }
    
    @Transactional(readOnly = true)
    public Response getAllEntityFields(final Request request) {
        List<EntityField> entityFields = null;
        try {
            entityFields = (List<EntityField>)this.entityTypeDao.findAll();
        }
        catch (Exception ex) {
            EntityTypeDataServiceImpl.log.error((Object)"Exception in EntityTypeDataServiceImpl - getAllEntityFields() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_ENTITY_FIELD_UNABLE_TO_GET_ALL_007.name(), ex.getMessage(), (Throwable)ex));
        }
        if (entityFields == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_ENTITY_FIELD_DOES_NOT_EXIST_001.name(), "Entity fields does not exists"));
        }
        final List<EntityFieldData> data = new ArrayList<EntityFieldData>();
        for (final EntityField iterator : entityFields) {
            data.add(this.convertEntityToVO(iterator));
        }
        final BaseValueObjectList bList = new BaseValueObjectList();
        bList.setValueObjectList((List)data);
        return this.OK(EntityTypeParams.ENTITY_FIELD_DATA_LIST.getParamName(), (BaseValueObject)bList);
    }
    
    @Transactional(readOnly = true)
    public Response getEntityFieldsForEntityType(final Request request) {
        final Identifier entityTypeId = (Identifier)request.get(EntityTypeParams.ENTITY_TYPE_IDENTIFIER.getParamName());
        if (entityTypeId == null || entityTypeId.getId() == null || entityTypeId.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        List<EntityField> entityFields = null;
        final Search search = new Search((Class)EntityField.class);
        search.addFilterEqual("entityType", (Object)entityTypeId.getId());
        search.addFetch("rules");
        search.addFetch("langs");
        search.addFetch("dataType");
        try {
            entityFields = (List<EntityField>)this.entityTypeDao.search((ISearch)search);
        }
        catch (Exception ex) {
            EntityTypeDataServiceImpl.log.error((Object)"Exception in EntityTypeDataServiceImpl - getEntityFieldsForEntityType() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_ENTITY_FIELD_UNABLE_TO_GET_004.name(), ex.getMessage(), (Throwable)ex));
        }
        if (entityFields == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_ENTITY_FIELD_UNABLE_TO_GET_004.name(), "Entity fields does not exists"));
        }
        final List<EntityFieldData> data = new ArrayList<EntityFieldData>();
        for (final EntityField iterator : entityFields) {
            data.add(this.convertEntityToVO(iterator));
        }
        final BaseValueObjectList bList = new BaseValueObjectList();
        bList.setValueObjectList((List)data);
        return this.OK(EntityTypeParams.ENTITY_FIELD_DATA_LIST.getParamName(), (BaseValueObject)bList);
    }
    
    @Transactional(readOnly = true)
    public Response getEntityFieldsForEntityTypeByLocale(final Request request) {
        final Identifier entityTypeId = (Identifier)request.get(EntityTypeParams.ENTITY_TYPE_IDENTIFIER.getParamName());
        final StringIdentifier locale = (StringIdentifier)request.get(EntityTypeParams.LOCALE_NAME.getParamName());
        if (entityTypeId == null || entityTypeId.getId() == null || entityTypeId.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        List<EntityFieldLang> entityFields = null;
        final Search search = new Search((Class)EntityFieldLang.class);
        search.addFilterEqual("locale", (Object)locale.getId());
        search.addFilterEqual("entityField.entityType", (Object)entityTypeId.getId());
        try {
            entityFields = (List<EntityFieldLang>)this.genericDao.search((ISearch)search);
        }
        catch (Exception ex) {
            EntityTypeDataServiceImpl.log.error((Object)"Exception in EntityTypeDataServiceImpl - getEntityFieldsForEntityTypeByLocale() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_ENTITY_FIELD_UNABLE_TO_GET_004.name(), ex.getMessage(), (Throwable)ex));
        }
        if (entityFields == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_ENTITY_FIELD_DOES_NOT_EXIST_001.name(), "Entity fields does not exists"));
        }
        final List<EntityFieldLangData> data = new ArrayList<EntityFieldLangData>();
        for (final EntityFieldLang iterator : entityFields) {
            data.add((EntityFieldLangData)this.modelMapper.map((Object)iterator, (Class)EntityFieldLangData.class));
        }
        final BaseValueObjectList bList = new BaseValueObjectList();
        bList.setValueObjectList((List)data);
        return this.OK(EntityTypeParams.ENTITY_FIELD_LANG_DATA.getParamName(), (BaseValueObject)bList);
    }
    
    @Transactional
    public Response removeEntityField(final Request request) {
        final Identifier fieldId = (Identifier)request.get(EntityTypeParams.ENTITY_FIELD_IDENTIFIER.getParamName());
        if (fieldId == null || fieldId.getId() == null || fieldId.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final boolean status = this.entityTypeDao.removeById((Serializable)fieldId.getId());
            return this.OK(EntityTypeParams.STATUS.getParamName(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EntityTypeDataServiceImpl.log.error((Object)"Exception in EntityTypeDataServiceImpl - removeEntityField() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_ENTITY_FIELD_UNABLE_TO_DELETE_006.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchOnEntityRelationship(final Request request) {
        final List<EntityRelationshipBean> eRelBList = new ArrayList<EntityRelationshipBean>();
        final EntityRelationshipBean relBean = (EntityRelationshipBean)request.get(EntityTypeParams.ENTITY_REL_DATA.name());
        if (relBean == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final EntityRelationship bObj = (EntityRelationship)this.modelMapper.map((Object)relBean, (Class)EntityRelationship.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.genericDao.getFilterFromExample((Object)bObj, options);
            final Search search = new Search((Class)EntityRelationship.class);
            search.addFilter(filter);
            final List<EntityRelationship> eRelList = (List<EntityRelationship>)this.genericDao.search((ISearch)search);
            for (final EntityRelationship iterator : eRelList) {
                final EntityRelationshipBean kpiRelObj = (EntityRelationshipBean)this.modelMapper.map((Object)iterator, (Class)EntityRelationshipBean.class);
                eRelBList.add(kpiRelObj);
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)eRelBList);
            return this.OK(EntityTypeParams.ENTITY_REL_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EntityTypeDataServiceImpl.log.error((Object)"Exception in EntityTypeDataServiceImpl - searchOnEntityRelationship() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_ENTITY_FIELD_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchOnEntity(final Request request) {
        final List<BaseEntityBean> eRelBList = new ArrayList<BaseEntityBean>();
        BaseEntityBean obj = (BaseEntityBean)request.get(EntityTypeParams.ENTITY_DATA.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "name";
        if (obj == null) {
            obj = new BaseEntityBean();
        }
        try {
            final BaseEntity bObj = (BaseEntity)this.modelMapper.map((Object)obj, (Class)BaseEntity.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.genericDao.getFilterFromExample((Object)bObj, options);
            final Search search = new Search((Class)BaseEntity.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.genericDao.count((ISearch)search), defaultField);
            final List<BaseEntity> eRelList = (List<BaseEntity>)this.genericDao.search((ISearch)search);
            for (final BaseEntity iterator : eRelList) {
                final BaseEntityBean kpiRelObj = (BaseEntityBean)this.modelMapper.map((Object)iterator, (Class)BaseEntityBean.class);
                eRelBList.add(kpiRelObj);
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)eRelBList);
            return this.OK(EntityTypeParams.ENTITY_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EntityTypeDataServiceImpl.log.error((Object)"Exception in EntityTypeDataServiceImpl - searchOnEntity() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_ENTITY_FIELD_UNKNOWN_EXCEPTION_000.name(), e.getMessage()));
        }
    }
    
    @Transactional
    public Response createBaseEntity(final Request request) {
        final BaseEntityBean data = (BaseEntityBean)request.get(EntityTypeParams.ENTITY_DATA.getParamName());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_BASE_ENTITY_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        BaseEntity baseEntity = null;
        try {
            baseEntity = new BaseEntity();
            this.modelMapper.map((Object)data, (Object)baseEntity);
            this.baseEntityDao.merge(baseEntity);
            return this.OK(EntityTypeParams.ENTITY_IDENTIFIER.getParamName(), (BaseValueObject)new Identifier(baseEntity.getId()));
        }
        catch (Exception ex) {
            EntityTypeDataServiceImpl.log.error((Object)"Exception in EntityTypeDataServiceImpl - createBaseEntity() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_BASE_ENTITY_UNKNOWN_EXCEPTION_000.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response getBaseEntity(final Request request) {
        final Identifier entityId = (Identifier)request.get(EntityTypeParams.ENTITY_IDENTIFIER.name());
        BaseEntity baseEntity = null;
        if (entityId == null || entityId.getId() == null || entityId.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_BASE_ENTITY_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            baseEntity = (BaseEntity)this.baseEntityDao.find((Serializable)entityId.getId());
        }
        catch (Exception ex) {
            EntityTypeDataServiceImpl.log.error((Object)"Exception in EntityTypeDataServiceImpl - getBaseEntity() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_BASE_ENTITY_UNKNOWN_EXCEPTION_000.name(), ex.getMessage(), (Throwable)ex));
        }
        if (baseEntity == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_BASE_ENTITY_UNKNOWN_EXCEPTION_000.name(), "Entity field does not exists with id = {0}", new Object[] { entityId.getId() }));
        }
        final BaseEntityBean data = (BaseEntityBean)this.modelMapper.map((Object)baseEntity, (Class)BaseEntityBean.class);
        return this.OK(EntityTypeParams.ENTITY_DATA.getParamName(), (BaseValueObject)data);
    }
    
    @Transactional
    public Response removeBaseEntity(final Request request) {
        final Identifier entityId = (Identifier)request.get(EntityTypeParams.ENTITY_IDENTIFIER.getParamName());
        if (entityId == null || entityId.getId() == null || entityId.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_BASE_ENTITY_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final boolean status = this.baseEntityDao.removeById((Serializable)entityId.getId());
            return this.OK(EntityTypeParams.STATUS.getParamName(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EntityTypeDataServiceImpl.log.error((Object)"Exception in EntityTypeDataServiceImpl - removeBaseEntity() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_BASE_ENTITY_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    private EntityFieldData convertEntityToVO(final EntityField field) {
        final EntityFieldData data = new EntityFieldData();
        data.setCore(field.isCore());
        data.setDeleted(field.isDeleted());
        data.setDescription(field.getDescription());
        data.setDetailEditable(field.isDetailEditable());
        data.setDetailGridEditable(field.isDetailGridEditable());
        data.setDetailGridVisible(field.isDetailGridVisible());
        data.setDetailVisible(field.isDetailVisible());
        data.setDisplayOrderId(field.getDisplayOrderId());
        data.setEntityType(field.getEntityType());
        data.setEventHandler(field.getEventHandler());
        data.setFieldType(field.getFieldType());
        data.setGroupId(field.getGroupId());
        data.setHeight(field.getHeight());
        data.setId(field.getId());
        data.setSearchable(field.isSearchable());
        data.setMandatory(field.isMandatory());
        data.setMaxLength(field.getMaxLength());
        data.setMinLength(field.getMinLength());
        data.setModifiable(field.isModifiable());
        data.setName(field.getName());
        data.setParentId(field.getParentId());
        data.setRenderingHints(field.getRenderingHints());
        data.setSourceUrl(field.getSourceUrl());
        data.setStatus(field.getStatus());
        data.setSummaryEditable(field.isSummaryEditable());
        data.setSummaryGridEditable(field.isSummaryGridEditable());
        data.setSummaryGridVisible(field.isSummaryGridVisible());
        data.setSummaryVisible(field.isSummaryVisible());
        data.setVisible(field.isVisible());
        data.setWidth(field.getWidth());
        if (field.getDataType() != null) {
            final DataTypeBean dataTypeBean = new DataTypeBean();
            dataTypeBean.setId(field.getDataType().getId());
            dataTypeBean.setName(field.getDataType().getName());
            data.setDataType(dataTypeBean);
        }
        data.setLangs((List)new ArrayList());
        data.setRules((List)new ArrayList());
        if (field.getLangs() != null && !field.getLangs().isEmpty()) {
            for (final EntityFieldLang iterator : field.getLangs()) {
                final EntityFieldLangData langData = new EntityFieldLangData();
                langData.setDescription(iterator.getDescription());
                langData.setDisplayName(iterator.getDisplayName());
                langData.setErrorText(iterator.getErrorText());
                langData.setHelpText(iterator.getHelpText());
                langData.setId(iterator.getId());
                langData.setLocale(iterator.getLocale());
                langData.setEntityField(data);
                data.getLangs().add(langData);
            }
        }
        if (field.getRules() != null && !field.getRules().isEmpty()) {
            for (final FieldValidationRule iterator2 : field.getRules()) {
                final FieldValidationRuleData ruleData = new FieldValidationRuleData();
                ruleData.setDeleted(iterator2.isDeleted());
                ruleData.setId(iterator2.getId());
                ruleData.setRule(iterator2.getRule());
                ruleData.setStatus(iterator2.getStatus());
                ruleData.setEntityField(data);
                data.getRules().add(ruleData);
            }
        }
        return data;
    }
    
    static {
        log = Logger.getLogger((Class)EntityTypeDataServiceImpl.class);
    }
}
