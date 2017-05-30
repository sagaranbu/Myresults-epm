package com.canopus.entity.domain;

import com.canopus.mw.*;
import com.canopus.mw.dto.param.*;
import com.canopus.mw.dto.*;
import com.canopus.entity.*;

public class DomainTag extends BaseDomainObject
{
    private TagManager tagManager;
    private TagData tagData;
    
    public DomainTag(final TagManager tagManager) {
        this.tagManager = tagManager;
    }
    
    public int save() {
        final Request request = new Request();
        request.put(TagParam.TAG.name(), (BaseValueObject)this.tagData);
        final Response response = this.getDataService().saveTag(request);
        final Identifier identifier = (Identifier)response.get(TagParam.TAG_ID.name());
        this.tagData.setId(identifier.getId());
        return identifier.getId();
    }
    
    private TagDataService getDataService() {
        return this.tagManager.getDataService();
    }
    
    public TagData getTagData() {
        return this.tagData;
    }
    
    public void setTagData(final TagData tagData) {
        this.tagData = tagData;
    }
}
