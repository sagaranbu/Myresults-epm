package com.canopus.entity.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class CustomEntityFieldsData extends BaseValueObject
{
    private static final long serialVersionUID = 6805249356746273182L;
    private Integer id;
    private Integer fieldDataType;
    private Integer fieldType;
    private Integer position;
    private Integer groupId;
    private Integer height;
    private Integer displayOrderId;
    private boolean visible;
    private boolean summaryVisible;
    private boolean summaryEditable;
    private boolean detailVisible;
    private boolean detailEditable;
    private boolean summaryGridVisible;
    private boolean summaryGridEditable;
    private boolean detailGridVisible;
    private boolean detailGridEditable;
    private String fieldName;
    private String referenceValue;
    private String eventHandler;
    private Integer customEntityId;
    private Integer width;
    private Integer maxLength;
    private Integer minLength;
    private Boolean mandatory;
    private String displayName;
    private boolean isDeleted;
    private String renderingHints;
    private List<CustomEntityFieldLangData> customLangData;
    
    public CustomEntityFieldsData() {
        this.customLangData = new ArrayList<CustomEntityFieldLangData>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getFieldDataType() {
        return this.fieldDataType;
    }
    
    public void setFieldDataType(final Integer fieldDataType) {
        this.fieldDataType = fieldDataType;
    }
    
    public Integer getFieldType() {
        return this.fieldType;
    }
    
    public void setFieldType(final Integer fieldType) {
        this.fieldType = fieldType;
    }
    
    public Integer getPosition() {
        return this.position;
    }
    
    public void setPosition(final Integer position) {
        this.position = position;
    }
    
    public Integer getGroupId() {
        return this.groupId;
    }
    
    public void setGroupId(final Integer groupId) {
        this.groupId = groupId;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
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
    
    public String getFieldName() {
        return this.fieldName;
    }
    
    public void setFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }
    
    public String getReferenceValue() {
        return this.referenceValue;
    }
    
    public void setReferenceValue(final String referenceValue) {
        this.referenceValue = referenceValue;
    }
    
    public String getEventHandler() {
        return this.eventHandler;
    }
    
    public void setEventHandler(final String eventHandler) {
        this.eventHandler = eventHandler;
    }
    
    public Integer getCustomEntityId() {
        return this.customEntityId;
    }
    
    public void setCustomEntityId(final Integer customEntityId) {
        this.customEntityId = customEntityId;
    }
    
    public List<CustomEntityFieldLangData> getCustomLangData() {
        return this.customLangData;
    }
    
    public void setCustomLangData(final List<CustomEntityFieldLangData> customLangData) {
        this.customLangData = customLangData;
    }
    
    public Integer getWidth() {
        return this.width;
    }
    
    public void setWidth(final Integer width) {
        this.width = width;
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
    
    public Boolean getMandatory() {
        return this.mandatory;
    }
    
    public void setMandatory(final Boolean mandatory) {
        this.mandatory = mandatory;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
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
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }
}
