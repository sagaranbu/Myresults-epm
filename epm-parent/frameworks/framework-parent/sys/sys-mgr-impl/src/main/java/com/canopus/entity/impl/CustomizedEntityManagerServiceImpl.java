package com.canopus.entity.impl;

import com.canopus.entity.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import com.canopus.entity.domain.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.entity.vo.params.*;
import com.canopus.mw.*;
import java.util.*;
import com.canopus.entity.vo.*;
import com.canopus.mw.dto.*;
import org.slf4j.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ CustomizedEntityManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class CustomizedEntityManagerServiceImpl extends BaseMiddlewareBean implements CustomizedEntityManagerService
{
    @Autowired
    private CustomizedEntityManager manager;
    private static final Logger log;
    
    public CustomizedEntityManagerServiceImpl() {
        this.manager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response getCustomEntity(final Request request) {
        final Identifier identifier = (Identifier)request.get(CustomEntityParams.CUSTOM_ENTITY_ID.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final CustomEntityData data = this.manager.getCustomEntity(identifier.getId());
            return this.OK(CustomEntityParams.CUSTOM_ENTITY_DATA.getParamName(), (BaseValueObject)data);
        }
        catch (Exception e) {
            CustomizedEntityManagerServiceImpl.log.error("Exception in CustomizedEntityManagerServiceImpl - getCustomEntity() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAllCustomEntity(final Request request) {
        try {
            final List<CustomEntityData> data = this.manager.getAllCustomEntity();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(CustomEntityParams.CUSTOM_ENTITY_DATA_LIST.getParamName(), (BaseValueObject)list);
        }
        catch (Exception e) {
            CustomizedEntityManagerServiceImpl.log.error("Exception in CustomizedEntityManagerServiceImpl - getAllCustomEntity() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_UNABLE_TO_GET_ALL_007.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getCustomEntityFields(final Request request) {
        final Identifier identifier = (Identifier)request.get(CustomEntityParams.CUSTOM_ENTITY_ID.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final List<CustomEntityFieldsData> data = this.manager.getCustomEntityFields(identifier.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(CustomEntityParams.CUSTOM_ENTITY_FIELDS_LIST.getParamName(), (BaseValueObject)list);
        }
        catch (Exception e) {
            CustomizedEntityManagerServiceImpl.log.error("Exception in CustomizedEntityManagerServiceImpl - getCustomEntityFields() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getCustomEntityFieldsByGroupId(final Request request) {
        final Identifier identifier = (Identifier)request.get(CustomEntityParams.FIELD_GROUP_ID.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final List<CustomEntityFieldsData> data = this.manager.getCustomEntityFieldsByGroupId(identifier.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(CustomEntityParams.CUSTOM_ENTITY_FIELDS_LIST.getParamName(), (BaseValueObject)list);
        }
        catch (Exception e) {
            CustomizedEntityManagerServiceImpl.log.error("Exception in CustomizedEntityManagerServiceImpl - getCustomEntityFieldsByGroupId() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getCustomEntityFieldLangValues(final Request request) {
        final Identifier identifier = (Identifier)request.get(CustomEntityParams.CUSTOM_ENTITY_ID.getParamName());
        final Name locale = (Name)request.get(CustomEntityParams.LOCALE.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0 || locale == null || locale.getName() == null || locale.getName().trim().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final List<CustomEntityFieldLangData> data = this.manager.getCustomEntityFieldLangValues(identifier.getId(), locale.getName());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(CustomEntityParams.ENTITY_FIELD_LANG_DATA_LIST.getParamName(), (BaseValueObject)list);
        }
        catch (Exception e) {
            CustomizedEntityManagerServiceImpl.log.error("Exception in CustomizedEntityManagerServiceImpl - getCustomEntityFieldLangValues() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getDataType(final Request request) {
        final Identifier identifier = (Identifier)request.get(CustomEntityParams.DATA_TYPE_ID.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_DATA_TYPE_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final DataTypeBean data = this.manager.getDataType(identifier.getId());
            final Name value = new Name();
            value.setName(data.getName());
            final Response response = this.OK(CustomEntityParams.VALUE.getParamName(), (BaseValueObject)value);
            response.put(CustomEntityParams.DATA_TYPE.getParamName(), (BaseValueObject)data);
            return response;
        }
        catch (Exception e) {
            CustomizedEntityManagerServiceImpl.log.error("Exception in CustomizedEntityManagerServiceImpl - getDataType() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_DATA_TYPE_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAllDataTypes(final Request request) {
        try {
            final List<DataTypeBean> data = this.manager.getAllDataTypes();
            final BaseValueObjectList result = new BaseValueObjectList();
            result.setValueObjectList((List)data);
            return this.OK(CustomEntityParams.DATA_TYPE_LIST.getParamName(), (BaseValueObject)result);
        }
        catch (Exception e) {
            CustomizedEntityManagerServiceImpl.log.error("Exception in CustomizedEntityManagerServiceImpl - getAllDataTypes() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_DATA_TYPE_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getFieldType(final Request request) {
        final Identifier identifier = (Identifier)request.get(CustomEntityParams.FIELD_TYPE_ID.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_FIELD_TYPE_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final FieldTypeBean data = this.manager.getFieldType(identifier.getId());
            final Name value = new Name();
            value.setName(data.getName());
            final Response response = this.OK(CustomEntityParams.VALUE.getParamName(), (BaseValueObject)value);
            response.put(CustomEntityParams.FIELD_TYPE.getParamName(), (BaseValueObject)data);
            return response;
        }
        catch (Exception e) {
            CustomizedEntityManagerServiceImpl.log.error("Exception in CustomizedEntityManagerServiceImpl - getFieldType() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_FIELD_TYPE_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAllFieldType(final Request request) {
        try {
            final List<FieldTypeBean> data = this.manager.getAllFieldType();
            final BaseValueObjectList result = new BaseValueObjectList();
            result.setValueObjectList((List)data);
            return this.OK(CustomEntityParams.FIELD_TYPE_LIST.getParamName(), (BaseValueObject)result);
        }
        catch (Exception e) {
            CustomizedEntityManagerServiceImpl.log.error("Exception in CustomizedEntityManagerServiceImpl - getAllFieldType() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_FIELD_TYPE_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getEntityInstance(final Request request) {
        final Identifier identifier = (Identifier)request.get(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_ID.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final CustomEntityInstanceData data = this.manager.getEntityInstance(identifier.getId());
            return this.OK(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_DATA.getParamName(), (BaseValueObject)data);
        }
        catch (Exception e) {
            CustomizedEntityManagerServiceImpl.log.error("Exception in CustomizedEntityManagerServiceImpl - getEntityInstance() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_FIELD_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAllEntityInstanceByEntityID(final Request request) {
        final Identifier identifier = (Identifier)request.get(CustomEntityParams.CUSTOM_ENTITY_ID.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final List<CustomEntityInstanceData> data = this.manager.getAllEntityInstanceByEntityID(identifier.getId());
            final BaseValueObjectList result = new BaseValueObjectList();
            result.setValueObjectList((List)data);
            return this.OK(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_LIST.getParamName(), (BaseValueObject)result);
        }
        catch (Exception e) {
            CustomizedEntityManagerServiceImpl.log.error("Exception in CustomizedEntityManagerServiceImpl - getAllEntityInstanceByEntityID() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_UNABLE_TO_GET_ALL_007.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response createEntityInstanceFieldData(final Request request) {
        final CustomEntityInstanceFieldData data = (CustomEntityInstanceFieldData)request.get(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_FIELD_DATA.getParamName());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final Integer id = this.manager.saveEntityInstanceFieldData(data);
            return this.OK(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_FIELD_ID.getParamName(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            CustomizedEntityManagerServiceImpl.log.error("Exception in CustomizedEntityManagerServiceImpl - createEntityInstanceFieldData() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_FIELD_UNABLE_TO_CREATE_003.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response updateEntityInstanceFieldData(final Request request) {
        final CustomEntityInstanceFieldData data = (CustomEntityInstanceFieldData)request.get(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_FIELD_DATA.getParamName());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final Integer id = this.manager.saveEntityInstanceFieldData(data);
            return this.OK(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_FIELD_ID.getParamName(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            CustomizedEntityManagerServiceImpl.log.error("Exception in CustomizedEntityManagerServiceImpl - updateEntityInstanceFieldData() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_FIELD_UNABLE_TO_UPDATE_005.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAllEntityInstanceFieldData(final Request request) {
        final Identifier identifier = (Identifier)request.get(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_ID.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final List<CustomEntityInstanceFieldData> data = this.manager.getAllEntityInstanceFieldData(identifier.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_FIELD_LIST.getParamName(), (BaseValueObject)list);
        }
        catch (Exception e) {
            CustomizedEntityManagerServiceImpl.log.error("Exception in CustomizedEntityManagerServiceImpl - getAllEntityInstanceFieldData() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_FIELD_UNABLE_TO_GET_ALL_007.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response createEntityFieldData(final Request request) {
        final CustomEntityFieldsData customEntityFieldsData = (CustomEntityFieldsData)request.get(CustomEntityParams.CUSTOM_ENTITY_FIELD_DATA.getParamName());
        if (customEntityFieldsData == null) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final Integer id = this.manager.createCustomEntityField(customEntityFieldsData);
            return this.OK(CustomEntityParams.CUSTOM_ENTITY_FIELD_ID.getParamName(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            CustomizedEntityManagerServiceImpl.log.error("Exception in CustomizedEntityManagerServiceImpl - createEntityFieldData() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_UNABLE_TO_CREATE_003.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response updateEntityFieldData(final Request request) {
        final CustomEntityFieldsData data = (CustomEntityFieldsData)request.get(CustomEntityParams.CUSTOM_ENTITY_FIELD_DATA.getParamName());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Integer id = this.manager.createCustomEntityField(data);
            return this.OK(CustomEntityParams.CUSTOM_ENTITY_FIELD_ID.getParamName(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            CustomizedEntityManagerServiceImpl.log.error("Exception in CustomizedEntityManagerServiceImpl - updateEntityFieldData() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_UNABLE_TO_UPDATE_005.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response removeEntityFieldData(final Request request) {
        final Identifier id = (Identifier)request.get(CustomEntityParams.CUSTOM_ENTITY_FIELD_ID.getParamName());
        if (id == null || id.getId() == null || id.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final boolean value = this.manager.removeCustomEntityField(id.getId());
            return this.OK(CustomEntityParams.CUSTOM_ENTITY_FIELD_STATUS.name(), (BaseValueObject)new BooleanResponse(value));
        }
        catch (Exception e) {
            CustomizedEntityManagerServiceImpl.log.error("Exception in CustomizedEntityManagerServiceImpl - removeEntityFieldData() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_FIELD_UNABLE_TO_DELETE_006.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response createEntityInstance(final Request request) {
        final CustomEntityInstanceData data = (CustomEntityInstanceData)request.get(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_DATA.getParamName());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_INVALID_INPUT_002.name(), "No input data in the request"));
        }
        try {
            final Integer id = this.manager.createCustomEntityInstance(data);
            return this.OK(CustomEntityParams.CUSTOM_ENTITY_INSTANCE_ID.getParamName(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            CustomizedEntityManagerServiceImpl.log.error("Exception in CustomizedEntityManagerServiceImpl - createEntityInstance() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_CUST_ENTITY_INSTANCE_UNABLE_TO_CREATE_003.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)CustomizedEntityManagerServiceImpl.class);
    }
}
