package com.canopus.entity.impl;

import com.canopus.entity.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.entity.dao.*;
import org.modelmapper.*;
import com.canopus.mw.*;
import com.canopus.entity.domain.*;
import com.canopus.mw.dto.param.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import com.canopus.mw.dto.*;
import java.util.*;
import org.slf4j.*;

@Component
public class TagDataServiceImpl extends BaseDataAccessService implements TagDataService
{
    @Autowired
    TagDao tagDao;
    @Autowired
    private TagSummaryDao tagSummaryDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public TagDataServiceImpl() {
        this.tagDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)TagData.class, (Class)Tag.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)TagSummaryData.class, (Class)TagSummary.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    @Transactional
    public Response saveTag(final Request request) {
        final TagData tagData = (TagData)request.get(TagParam.TAG.name());
        if (tagData == null) {
            return this.ERROR((Exception)new DataAccessException("OBPDAC_INVALID_INPUT_ERROR", "No data object in the request"));
        }
        try {
            Tag tag = null;
            if (tagData.getId() != null && tagData.getId() != 0) {
                tag = (Tag)this.tagDao.find((Serializable)tagData.getId());
            }
            else {
                tag = new Tag();
            }
            this.modelMapper.map((Object)tagData, (Object)tag);
            this.tagDao.save(tag);
            tagData.setId(tag.getId());
            return this.OK(TagParam.TAG_ID.name(), (BaseValueObject)new Identifier(tagData.getId()));
        }
        catch (Exception e) {
            TagDataServiceImpl.log.error("Exception in TagDataServiceImpl - saveTag():", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException("OBPTAGDAC_SAVE_ERROR", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getTag(final Request request) {
        final Identifier identifier = (Identifier)request.get(TagParam.TAG_ID.name());
        Integer id = null;
        Tag tag = null;
        try {
            if (identifier == null || identifier.getId() == 0) {
                return this.ERROR((Exception)new DataAccessException("OBPDAC_INVALID_INPUT_ERROR", "No data object in the request"));
            }
            id = identifier.getId();
            tag = (Tag)this.tagDao.find((Serializable)id);
        }
        catch (Exception e) {
            TagDataServiceImpl.log.error("Unknown error while loading Tag: ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException("ERR_OBPDAC_UNKNOWN_EXCEPTION", "Unknown error while loading Tag.", new Object[] { e.getMessage(), e }));
        }
        if (tag == null) {
            return this.ERROR((Exception)new DataAccessException("ERR_CMT_UNABLE_TO_GET", "Tag id {0} does not exist.", new Object[] { id }));
        }
        final TagData tagData = (TagData)this.modelMapper.map((Object)tag, (Class)TagData.class);
        return this.OK(TagParam.TAG.name(), (BaseValueObject)tagData);
    }
    
    @Transactional
    public Response saveTagSummary(final Request request) {
        final TagSummaryData tagSummaryData = (TagSummaryData)request.get(TagParam.TAG_SUMMARY.name());
        if (tagSummaryData == null) {
            return this.ERROR((Exception)new DataAccessException("OBPDAC_INVALID_INPUT_ERROR", "No data object in the request"));
        }
        try {
            TagSummary tagSummary = null;
            if (tagSummaryData.getId() != null && tagSummaryData.getId() != 0) {
                tagSummary = (TagSummary)this.tagSummaryDao.find((Serializable)tagSummaryData.getId());
            }
            else {
                tagSummary = new TagSummary();
            }
            this.modelMapper.map((Object)tagSummaryData, (Object)tagSummary);
            this.tagSummaryDao.save(tagSummary);
            tagSummaryData.setId(tagSummary.getId());
            return this.OK(TagParam.TAG_SUMMARY_ID.name(), (BaseValueObject)new Identifier(tagSummaryData.getId()));
        }
        catch (Exception e) {
            TagDataServiceImpl.log.error("Exception in TagDataServiceImpl - saveTagSummary():", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException("OBPTAGSUMDAC_SAVE_ERROR", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getTagSummary(final Request request) {
        final Identifier identifier = (Identifier)request.get(TagParam.TAG_SUMMARY_ID.name());
        Integer id = null;
        TagSummary tagSummary = null;
        try {
            if (identifier.getId() == null && identifier.getId() == 0) {
                return this.ERROR((Exception)new DataAccessException("OBPDAC_INVALID_INPUT_ERROR", "No data object in the request"));
            }
            id = identifier.getId();
            tagSummary = (TagSummary)this.tagSummaryDao.find((Serializable)id);
        }
        catch (Exception e) {
            TagDataServiceImpl.log.error("Exception at TagDataServiceImpl - getTagSummary(): ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException("ERR_OBPDAC_UNKNOWN_EXCEPTION", "Unknown error while loading Tag Summary.", new Object[] { e.getMessage(), e }));
        }
        if (tagSummary == null) {
            return this.ERROR((Exception)new DataAccessException("ERR_CST_UNABLE_TO_GET", "TagSummary id {0} does not exist.", new Object[] { id }));
        }
        final TagSummaryData tagSummaryData = (TagSummaryData)this.modelMapper.map((Object)tagSummary, (Class)TagSummaryData.class);
        return this.OK(TagParam.TAG_SUMMARY.name(), (BaseValueObject)tagSummaryData);
    }
    
    @Transactional
    public Response deleteTag(final Request request) {
        final Identifier identifer = (Identifier)request.get(TagParam.TAG_ID.name());
        if (identifer == null) {
            return this.ERROR((Exception)new DataAccessException("OBPDAC_INVALID_INPUT_ERROR", "No data object in the request"));
        }
        try {
            final boolean status = this.tagDao.removeById((Serializable)identifer.getId());
            return this.OK(TagParam.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            TagDataServiceImpl.log.error("Exception in TagDataServiceImpl - deleteTag() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException("ERR_TAG_UNABLE_TO_DELETE", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response deleteTagSummary(final Request request) {
        final Identifier identifier = (Identifier)request.get(TagParam.TAG_SUMMARY_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException("OBPDAC_INVALID_INPUT_ERROR", "No data object in the request"));
        }
        try {
            final boolean status = this.tagSummaryDao.removeById((Serializable)identifier.getId());
            return this.OK(TagParam.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            TagDataServiceImpl.log.error("Exception in TagDataServiceImpl - deleteTagSummary() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException("ERR_TAG_SUM_UNABLE_TO_DELETE", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllTags(final Request request) {
        final List<TagData> result = new ArrayList<TagData>();
        try {
            final List<Tag> data = (List<Tag>)this.tagDao.findAll();
            for (final Tag iterator : data) {
                result.add((TagData)this.modelMapper.map((Object)iterator, (Class)TagData.class));
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(TagParam.TAG_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            TagDataServiceImpl.log.error("Exception in OnboardingProcessDataServiceImpl - getAllProcessInstances() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException("ERR_CMT_UNABLE_TO_GET_ALL", e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)TagDataServiceImpl.class);
    }
}
