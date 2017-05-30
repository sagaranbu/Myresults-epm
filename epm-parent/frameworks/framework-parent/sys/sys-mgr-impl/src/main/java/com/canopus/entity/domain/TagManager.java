package com.canopus.entity.domain;

import org.springframework.stereotype.*;
import com.canopus.entity.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.dto.param.*;
import com.canopus.mw.*;
import java.util.*;
import com.canopus.mw.dto.*;

@Component
public class TagManager extends BaseDomainManager implements CacheLoader<Integer, DomainTag>
{
    @Autowired
    TagDataService dataService;
    @Autowired
    @Qualifier("tagCache")
    private Cache<Integer, DomainTag> cache;
    
    public TagManager() {
        this.dataService = null;
        this.cache = null;
    }
    
    public DomainTag load(final Integer key) {
        final Request request = new Request();
        request.put(TagParam.TAG_ID.name(), (BaseValueObject)new Identifier(key));
        final Response response = this.dataService.getTag(request);
        final TagData data = (TagData)response.get(TagParam.TAG.name());
        final DomainTag domainTag = new DomainTag(this);
        domainTag.setTagData(data);
        return domainTag;
    }
    
    public TagDataService getDataService() {
        return this.dataService;
    }
    
    public void setDataService(final TagDataService dataService) {
        this.dataService = dataService;
    }
    
    public DomainTag saveOrUpdateTag(final TagData tagData) {
        final DomainTag domainTag = new DomainTag(this);
        domainTag.setTagData(tagData);
        int id = 0;
        try {
            id = domainTag.save();
        }
        catch (Exception e) {
            throw new MiddlewareException("ERR_SAVE_UPDATE_TAG", e.getMessage(), (Throwable)e);
        }
        return domainTag;
    }
    
    public DomainTag getTag(final Integer tagID) {
        final DomainTag domainTag = (DomainTag)this.getCache().get(tagID, (CacheLoader)this);
        return domainTag;
    }
    
    public Cache<Integer, DomainTag> getCache() {
        return this.cache;
    }
    
    public void setCache(final Cache<Integer, DomainTag> cache) {
        this.cache = cache;
    }
    
    public List<TagData> getAllTags() {
        final Request request = new Request();
        final Response response = this.getDataService().getAllTags(request);
        final BaseValueObjectList bvList = (BaseValueObjectList)response.get(TagParam.TAG_LIST.name());
        final List<TagData> list = (List<TagData>)bvList.getValueObjectList();
        return list;
    }
    
    public boolean deleteTag(final Integer tagID) {
        final Request request = new Request();
        request.put(TagParam.TAG_ID.name(), (BaseValueObject)new Identifier(tagID));
        final Response response = this.getDataService().deleteTag(request);
        final BooleanResponse status = (BooleanResponse)response.get(TagParam.STATUS_RESPONSE.name());
        return status.isResponse();
    }
    
    public Integer saveTagSummary(final TagSummaryData tagSummaryData) {
        final Request request = new Request();
        request.put(TagParam.TAG_SUMMARY.name(), (BaseValueObject)tagSummaryData);
        final Response response = this.getDataService().saveTagSummary(request);
        final Identifier identifier = (Identifier)response.get(TagParam.TAG_SUMMARY_ID.name());
        tagSummaryData.setId(identifier.getId());
        return identifier.getId();
    }
    
    public TagSummaryData getTagSummary(final Integer tagSummaryID) {
        final Request request = new Request();
        request.put(TagParam.TAG_SUMMARY_ID.name(), (BaseValueObject)new Identifier(tagSummaryID));
        final Response response = this.getDataService().getTagSummary(request);
        final TagSummaryData tagSummaryData = (TagSummaryData)response.get(TagParam.TAG_SUMMARY.name());
        return tagSummaryData;
    }
    
    public boolean deleteTagSummary(final Integer tagSummaryID) {
        final Request request = new Request();
        request.put(TagParam.TAG_SUMMARY_ID.name(), (BaseValueObject)new Identifier(tagSummaryID));
        final Response response = this.getDataService().deleteTagSummary(request);
        final BooleanResponse status = (BooleanResponse)response.get(TagParam.STATUS_RESPONSE.name());
        return status.isResponse();
    }
}
