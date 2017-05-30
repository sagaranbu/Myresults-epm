package com.canopus.entity.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class EntityFieldData extends BaseValueObject
{
    private static final long serialVersionUID = 666868703213479339L;
    private Integer id;
    private Integer maxLength;
    private Integer minLength;
    private Integer fieldType;
    private Integer status;
    private Integer entityType;
    private Integer displayOrderId;
    private Integer parentId;
    private DataTypeBean dataType;
    private Integer width;
    private Integer height;
    private boolean summaryVisible;
    private boolean summaryEditable;
    private boolean detailVisible;
    private boolean detailEditable;
    private boolean summaryGridVisible;
    private boolean summaryGridEditable;
    private boolean detailGridVisible;
    private boolean detailGridEditable;
    private boolean mandatory;
    private boolean visible;
    private boolean modifiable;
    private boolean searchable;
    private boolean deleted;
    private boolean isCore;
    private String groupId;
    private String name;
    private String description;
    private String eventHandler;
    private String sourceUrl;
    private String renderingHints;
    private List<FieldValidationRuleData> rules;
    private List<EntityFieldLangData> langs;
    
    public EntityFieldData() {
        this.rules = new ArrayList<FieldValidationRuleData>();
        this.langs = new ArrayList<EntityFieldLangData>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getMaxLength() {
        return this.maxLength;
    }
    
    public void setMaxLength(final Integer maxLength) {
        this.maxLength = maxLength;
    }
    
    public Integer getMinLength() {
        return this.minLength;
    }
    
    public void setMinLength(final Integer minLength) {
        this.minLength = minLength;
    }
    
    public Integer getFieldType() {
        return this.fieldType;
    }
    
    public void setFieldType(final Integer fieldType) {
        this.fieldType = fieldType;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
    }
    
    public Integer getEntityType() {
        return this.entityType;
    }
    
    public void setEntityType(final Integer entityType) {
        this.entityType = entityType;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public DataTypeBean getDataType() {
        return this.dataType;
    }
    
    public void setDataType(final DataTypeBean dataType) {
        this.dataType = dataType;
    }
    
    public boolean isSummaryVisible() {
        return this.summaryVisible;
    }
    
    public void setSummaryVisible(final boolean summaryVisible) {
        this.summaryVisible = summaryVisible;
    }
    
    public boolean isSummaryEditable() {
        return this.summaryEditable;
    }
    
    public void setSummaryEditable(final boolean summaryEditable) {
        this.summaryEditable = summaryEditable;
    }
    
    public boolean isDetailVisible() {
        return this.detailVisible;
    }
    
    public void setDetailVisible(final boolean detailVisible) {
        this.detailVisible = detailVisible;
    }
    
    public boolean isDetailEditable() {
        return this.detailEditable;
    }
    
    public void setDetailEditable(final boolean detailEditable) {
        this.detailEditable = detailEditable;
    }
    
    public boolean isSummaryGridVisible() {
        return this.summaryGridVisible;
    }
    
    public void setSummaryGridVisible(final boolean summaryGridVisible) {
        this.summaryGridVisible = summaryGridVisible;
    }
    
    public boolean isSummaryGridEditable() {
        return this.summaryGridEditable;
    }
    
    public void setSummaryGridEditable(final boolean summaryGridEditable) {
        this.summaryGridEditable = summaryGridEditable;
    }
    
    public boolean isDetailGridVisible() {
        return this.detailGridVisible;
    }
    
    public void setDetailGridVisible(final boolean detailGridVisible) {
        this.detailGridVisible = detailGridVisible;
    }
    
    public boolean isDetailGridEditable() {
        return this.detailGridEditable;
    }
    
    public void setDetailGridEditable(final boolean detailGridEditable) {
        this.detailGridEditable = detailGridEditable;
    }
    
    public boolean isCore() {
        return this.isCore;
    }
    
    public void setCore(final boolean isCore) {
        this.isCore = isCore;
    }
    
    public String getEventHandler() {
        return this.eventHandler;
    }
    
    public void setEventHandler(final String eventHandler) {
        this.eventHandler = eventHandler;
    }
    
    public String getSourceUrl() {
        return this.sourceUrl;
    }
    
    public void setSourceUrl(final String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
    
    public boolean isMandatory() {
        return this.mandatory;
    }
    
    public void setMandatory(final boolean mandatory) {
        this.mandatory = mandatory;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
    
    public boolean isModifiable() {
        return this.modifiable;
    }
    
    public void setModifiable(final boolean modifiable) {
        this.modifiable = modifiable;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }
    
    public List<FieldValidationRuleData> getRules() {
        return this.rules;
    }
    
    public void setRules(final List<FieldValidationRuleData> rules) {
        this.rules = rules;
    }
    
    public List<EntityFieldLangData> getLangs() {
        return this.langs;
    }
    
    public void setLangs(final List<EntityFieldLangData> langs) {
        this.langs = langs;
    }
    
    public Integer getWidth() {
        return this.width;
    }
    
    public void setWidth(final Integer width) {
        this.width = width;
    }
    
    public Integer getHeight() {
        return this.height;
    }
    
    public void setHeight(final Integer height) {
        this.height = height;
    }
    
    public String getRenderingHints() {
        return this.renderingHints;
    }
    
    public void setRenderingHints(final String renderingHints) {
        this.renderingHints = renderingHints;
    }
    
    public Integer getParentId() {
        return this.parentId;
    }
    
    public void setParentId(final Integer parentId) {
        this.parentId = parentId;
    }
    
    public String getGroupId() {
        return this.groupId;
    }
    
    public void setGroupId(final String groupId) {
        this.groupId = groupId;
    }
    
    public boolean isSearchable() {
        return this.searchable;
    }
    
    public void setSearchable(final boolean searchable) {
        this.searchable = searchable;
    }
}
