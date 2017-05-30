package com.canopus.entity.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COR_CUSTOM_ENTITY_FIELD", uniqueConstraints = { @UniqueConstraint(columnNames = { "FIELD_NAME", "CCF_PK_ID" }) })
@SQLDelete(sql = "UPDATE COR_CUSTOM_ENTITY_FIELD SET IS_DELETED = 1 WHERE CCF_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class CustomEntityFields extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 113928261302424851L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "COR_ENTITY_FIELD_ID_SEQ")
    @SequenceGenerator(name = "COR_ENTITY_FIELD_ID_SEQ", sequenceName = "COR_ENTITY_FIELD_ID_SEQ")
    @Column(name = "CCF_PK_ID")
    private Integer id;
    @Column(name = "FIELD_DATA_TYPE", length = 11)
    private Integer fieldDataType;
    @Column(name = "FIELD_TYPE", length = 11)
    private Integer fieldType;
    @Column(name = "POSITION", length = 11)
    private Integer position;
    @Column(name = "GROUP_ID", length = 11)
    private String groupId;
    @Column(name = "PARENT_ID", length = 11)
    private Integer parentId;
    @Column(name = "HEIGHT", length = 11)
    private Integer height;
    @Column(name = "IS_VISIBLE")
    private boolean visible;
    @Column(name = "IS_SUMMARY_VISIBLE")
    private boolean summaryVisible;
    @Column(name = "IS_SUMMARY_EDITABLE")
    private boolean summaryEditable;
    @Column(name = "IS_DETAIL_VISIBLE")
    private boolean detailVisible;
    @Column(name = "IS_DETAIL_EDITABLE")
    private boolean detailEditable;
    @Column(name = "IS_SUMMARYGRID_VISIBLE")
    private boolean summaryGridVisible;
    @Column(name = "IS_SUMMARYGRID_EDITABLE")
    private boolean summaryGridEditable;
    @Column(name = "IS_DETAILGRID_VISIBLE")
    private boolean detailGridVisible;
    @Column(name = "IS_DETAILGRID_EDITABLE")
    private boolean detailGridEditable;
    @Column(name = "FIELD_NAME", length = 512)
    private String fieldName;
    @Column(name = "REFERENCE_VALUE", length = 512)
    private String referenceValue;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @Column(name = "DISPLAY_ORDER_ID", length = 11)
    private Integer displayOrderId;
    @Column(name = "CCE_FK_ID")
    private Integer customEntityId;
    @Column(name = "WIDTH", length = 11)
    private Integer width;
    @Column(name = "MAX_LENGTH", length = 11)
    private Integer maxLength;
    @Column(name = "MIN_LENGTH", length = 11)
    private Integer minLength;
    @Column(name = "IS_MANDATORY")
    private Boolean mandatory;
    @Column(name = "EVENT_HANDLER", length = 512)
    private String eventHandler;
    @Column(name = "RENDERING_HINTS", length = 512)
    private String renderingHints;
    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "entityFieldId")
    private List<CustomEntityFieldLang> customLangData;
    
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
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }
    
    public Integer getCustomEntityId() {
        return this.customEntityId;
    }
    
    public void setCustomEntityId(final Integer customEntityId) {
        this.customEntityId = customEntityId;
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
    
    public List<CustomEntityFieldLang> getCustomLangData() {
        return this.customLangData;
    }
    
    public void setCustomLangData(final List<CustomEntityFieldLang> customLangData) {
        this.customLangData = customLangData;
    }
    
    public String getEventHandler() {
        return this.eventHandler;
    }
    
    public void setEventHandler(final String eventHandler) {
        this.eventHandler = eventHandler;
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
    
    public String getGroupId() {
        return this.groupId;
    }
    
    public void setGroupId(final String groupId) {
        this.groupId = groupId;
    }
    
    public Integer getParentId() {
        return this.parentId;
    }
    
    public void setParentId(final Integer parentId) {
        this.parentId = parentId;
    }
}
