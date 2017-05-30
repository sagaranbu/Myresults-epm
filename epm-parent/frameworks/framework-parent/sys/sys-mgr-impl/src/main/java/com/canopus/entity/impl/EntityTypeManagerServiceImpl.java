package com.canopus.entity.impl;

import com.canopus.entity.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import com.canopus.entity.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import com.canopus.entity.vo.params.*;
import com.canopus.mw.*;
import java.util.*;
import com.canopus.entity.vo.*;
import com.canopus.mw.dto.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ EntityTypeManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class EntityTypeManagerServiceImpl extends BaseMiddlewareBean implements EntityTypeManagerService
{
    @Autowired
    private EntityType entityType;
    private static final Logger log;
    
    public EntityTypeManagerServiceImpl() {
        this.entityType = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response createEntityField(final Request request) {
        final EntityFieldData data = (EntityFieldData)request.get(EntityTypeParams.ENTITY_FIELD_DATA.getParamName());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final int id = this.entityType.createOrUpdateEntityField(data);
            return this.OK(EntityTypeParams.ENTITY_FIELD_IDENTIFIER.getParamName(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            EntityTypeManagerServiceImpl.log.error((Object)"Exception in EntityTypeManagerServiceImpl - createEntityField() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_ENTITY_FIELD_UNABLE_TO_CREATE_003.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response updateEntityField(final Request request) {
        final EntityFieldData data = (EntityFieldData)request.get(EntityTypeParams.ENTITY_FIELD_DATA.getParamName());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final int id = this.entityType.createOrUpdateEntityField(data);
            return this.OK(EntityTypeParams.ENTITY_FIELD_IDENTIFIER.getParamName(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            EntityTypeManagerServiceImpl.log.error((Object)"Exception in EntityTypeManagerServiceImpl - updateEntityField() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_ENTITY_FIELD_UNABLE_TO_UPDATE_005.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getEntityField(final Request request) {
        final Identifier identifier = (Identifier)request.get(EntityTypeParams.ENTITY_FIELD_IDENTIFIER.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final EntityFieldData data = this.entityType.getEntityField(identifier.getId());
            return this.OK(EntityTypeParams.ENTITY_FIELD_DATA.getParamName(), (BaseValueObject)data);
        }
        catch (Exception e) {
            EntityTypeManagerServiceImpl.log.error((Object)"Exception in EntityTypeManagerServiceImpl - getEntityField() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_ENTITY_FIELD_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAllEntityFields(final Request request) {
        try {
            final List<EntityFieldData> result = this.entityType.getAllEntityFields();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(EntityTypeParams.ENTITY_FIELD_DATA_LIST.getParamName(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EntityTypeManagerServiceImpl.log.error((Object)"Exception in EntityTypeManagerServiceImpl - getAllEntityFields() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_ENTITY_FIELD_UNABLE_TO_GET_ALL_007.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getEntityFieldsForEntityType(final Request request) {
        final Identifier identifier = (Identifier)request.get(EntityTypeParams.ENTITY_TYPE_IDENTIFIER.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final List<EntityFieldData> result = this.entityType.getEntityFieldsForEntityType(identifier.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(EntityTypeParams.ENTITY_FIELD_DATA_LIST.getParamName(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EntityTypeManagerServiceImpl.log.error((Object)"Exception in EntityTypeManagerServiceImpl - getEntityFieldsForEntityType() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_ENTITY_FIELD_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response removeEntityField(final Request request) {
        final Identifier identifier = (Identifier)request.get(EntityTypeParams.ENTITY_FIELD_IDENTIFIER.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final boolean status = this.entityType.removeEntityField(identifier.getId());
            return this.OK(EntityTypeParams.STATUS.getParamName(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EntityTypeManagerServiceImpl.log.error((Object)"Exception in EntityTypeManagerServiceImpl - removeEntityField() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_ENTITY_FIELD_UNABLE_TO_DELETE_006.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getEntityFieldsForEntityTypeByLocale(final Request request) {
        final Identifier identifier = (Identifier)request.get(EntityTypeParams.ENTITY_TYPE_IDENTIFIER.getParamName());
        final StringIdentifier locale = (StringIdentifier)request.get(EntityTypeParams.LOCALE_NAME.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        if (locale == null || locale.getId() == null || locale.getId() == "") {
            locale.setId("en_US");
        }
        try {
            final List<EntityFieldLangData> result = this.entityType.getEntityFieldsForEntityTypeByLocale(identifier.getId(), locale.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(EntityTypeParams.ENTITY_FIELD_LANG_DATA.getParamName(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EntityTypeManagerServiceImpl.log.error((Object)"Exception in EntityTypeManagerServiceImpl - getEntityFieldsForEntityTypeByLocale() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_ENTITY_FIELD_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response searchOnEntityRelationship(final Request request) {
        final EntityRelationshipBean eRelBean = (EntityRelationshipBean)request.get(EntityTypeParams.ENTITY_REL_DATA.getParamName());
        if (eRelBean == null) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final List<EntityRelationshipBean> eRelList = this.entityType.searchOnEntityRelationship(eRelBean);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)eRelList);
            return this.OK(EntityTypeParams.ENTITY_REL_LIST.getParamName(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EntityTypeManagerServiceImpl.log.error((Object)"Exception in EntityTypeManagerServiceImpl - searchOnEntityRelationship() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_ENTITY_FIELD_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response searchOnEntity(final Request request) {
        final BaseEntityBean eBean = (BaseEntityBean)request.get(EntityTypeParams.ENTITY_DATA.getParamName());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (eBean == null) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final List<BaseEntityBean> eList = this.entityType.searchOnEntity(eBean, page, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)eList);
            return this.OK(EntityTypeParams.ENTITY_DATA_LIST.getParamName(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EntityTypeManagerServiceImpl.log.error((Object)"Exception in EntityTypeManagerServiceImpl - searchOnEntityRelationship() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_ENTITY_FIELD_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response createBaseEntity(final Request request) {
        final BaseEntityBean data = (BaseEntityBean)request.get(EntityTypeParams.ENTITY_DATA.getParamName());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_BASE_ENTITY_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final int id = this.entityType.createOrUpdateBaseEntity(data);
            return this.OK(EntityTypeParams.ENTITY_IDENTIFIER.getParamName(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            EntityTypeManagerServiceImpl.log.error((Object)"Exception in EntityTypeManagerServiceImpl - createBaseEntity() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_BASE_ENTITY_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response updateBaseEntity(final Request request) {
        final BaseEntityBean data = (BaseEntityBean)request.get(EntityTypeParams.ENTITY_DATA.getParamName());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_BASE_ENTITY_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final int id = this.entityType.createOrUpdateBaseEntity(data);
            return this.OK(EntityTypeParams.ENTITY_IDENTIFIER.getParamName(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            EntityTypeManagerServiceImpl.log.error((Object)"Exception in EntityTypeManagerServiceImpl - updateBaseEntity() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_BASE_ENTITY_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getBaseEntity(final Request request) {
        final Identifier identifier = (Identifier)request.get(EntityTypeParams.ENTITY_IDENTIFIER.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_BASE_ENTITY_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final BaseEntityBean data = this.entityType.getBaseEntity(identifier.getId());
            return this.OK(EntityTypeParams.ENTITY_DATA.getParamName(), (BaseValueObject)data);
        }
        catch (Exception e) {
            EntityTypeManagerServiceImpl.log.error((Object)"Exception in EntityTypeManagerServiceImpl - getBaseEntity() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_BASE_ENTITY_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response removeBaseEntity(final Request request) {
        final Identifier identifier = (Identifier)request.get(EntityTypeParams.ENTITY_IDENTIFIER.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_BASE_ENTITY_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final boolean status = this.entityType.removeBaseEntity(identifier.getId());
            return this.OK(EntityTypeParams.STATUS.getParamName(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EntityTypeManagerServiceImpl.log.error((Object)"Exception in EntityTypeManagerServiceImpl - removeBaseEntity() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_BASE_ENTITY_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        log = Logger.getLogger((Class)EntityTypeManagerServiceImpl.class);
    }
}
