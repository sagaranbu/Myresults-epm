package com.canopus.entity.impl;

import com.canopus.entity.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.dto.param.*;
import com.canopus.mw.*;
import com.canopus.entity.domain.*;
import java.util.*;
import com.canopus.mw.dto.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ TagManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class TagManagerServiceImpl extends BaseMiddlewareBean implements TagManagerService
{
    @Autowired
    TagManager tagManager;
    
    public TagManagerServiceImpl() {
        this.tagManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response createTag(final Request request) {
        final TagData tagData = (TagData)request.get(TagParam.TAG.name());
        if (tagData == null) {
            return this.ERROR((Exception)new MiddlewareException("TAG_INVALID_INPUT_ERROR", "No data object in the request"));
        }
        try {
            final DomainTag domainTag = this.getTagManager().saveOrUpdateTag(tagData);
            final Identifier identifier = new Identifier();
            identifier.setId(domainTag.getTagData().getId());
            return this.OK(TagParam.TAG_ID.name(), (BaseValueObject)identifier);
        }
        catch (Exception e) {
            return this.ERROR((Exception)new MiddlewareException("TAG_SAVE_PDI_ERROR", e.getMessage(), (Throwable)e));
        }
    }
    
    public TagManager getTagManager() {
        return this.tagManager;
    }
    
    public void setTagManager(final TagManager tagManager) {
        this.tagManager = tagManager;
    }
    
    public Response getTag(final Request request) {
        final Identifier id = (Identifier)request.get(TagParam.TAG_ID.name());
        if (id == null || id.getId() == null || id.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("TAG_INVALID_INPUT_ERROR", "No data object in the request"));
        }
        try {
            final DomainTag domainTag = this.getTagManager().getTag(id.getId());
            final TagData tagData = domainTag.getTagData();
            return this.OK(TagParam.TAG.name(), (BaseValueObject)tagData);
        }
        catch (Exception e) {
            return this.ERROR((Exception)new MiddlewareException(TagManagerService.TAG_GET_ERROR, e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAllTags(final Request request) {
        try {
            final List<TagData> resultList = this.getTagManager().getAllTags();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)resultList);
            return this.OK(TagParam.TAG_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            return this.ERROR((Exception)new MiddlewareException(TagManagerService.TAG_GET_ERROR, e.getMessage(), (Throwable)e));
        }
    }
    
    public Response deleteTag(final Request request) {
        final Identifier id = (Identifier)request.get(TagParam.TAG_ID.name());
        if (id == null || id.getId() == null || id.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("TAG_INVALID_INPUT_ERROR", "No data object in the request"));
        }
        try {
            final boolean status = this.getTagManager().deleteTag(id.getId());
            return this.OK(TagParam.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            return this.ERROR((Exception)new MiddlewareException("TAG_DELETE_ERROR", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response createTagSummary(final Request request) {
        final TagSummaryData tagSummaryData = (TagSummaryData)request.get(TagParam.TAG_SUMMARY.name());
        if (tagSummaryData == null) {
            return this.ERROR((Exception)new MiddlewareException("TAGSUM_INVALID_INPUT_ERROR", "No data object in the request"));
        }
        try {
            final Integer id = this.getTagManager().saveTagSummary(tagSummaryData);
            final Identifier identifier = new Identifier(id);
            return this.OK(TagParam.TAG_SUMMARY_ID.name(), (BaseValueObject)identifier);
        }
        catch (Exception e) {
            return this.ERROR((Exception)new MiddlewareException("TAGSUM_SAVE_ERROR", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getTagSummary(final Request request) {
        final Identifier id = (Identifier)request.get(TagParam.TAG_SUMMARY_ID.name());
        if (id == null || id.getId() == null || id.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("TAGSUM_INVALID_INPUT_ERROR", "No data object in the request"));
        }
        try {
            final TagSummaryData tagSummaryData = this.getTagManager().getTagSummary(id.getId());
            return this.OK(TagParam.TAG_SUMMARY.name(), (BaseValueObject)tagSummaryData);
        }
        catch (Exception e) {
            return this.ERROR((Exception)new MiddlewareException("TAGSUM_GET_ERROR", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response deleteTagSummary(final Request request) {
        final Identifier id = (Identifier)request.get(TagParam.TAG_SUMMARY_ID.name());
        if (id == null || id.getId() == null || id.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("TAGSUM_INVALID_INPUT_ERROR", "No data object in the request"));
        }
        try {
            final boolean status = this.getTagManager().deleteTagSummary(id.getId());
            return this.OK(TagParam.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            return this.ERROR((Exception)new MiddlewareException("TAGSUM_DELETE_ERROR", e.getMessage(), (Throwable)e));
        }
    }
}
