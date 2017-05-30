package com.canopus.entity.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COR_MET_FIELD")
@SQLDelete(sql = "UPDATE COR_MET_FIELD SET IS_DELETED = 1 WHERE CMF_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class EntityField extends BaseTenantEntity
{
    private static final long serialVersionUID = 4947904248692042484L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "COR_FIELD_ID_SEQ")
    @SequenceGenerator(name = "COR_FIELD_ID_SEQ", sequenceName = "COR_FIELD_ID_SEQ")
    @Column(name = "CMF_PK_ID")
    private Integer id;
    @Column(name = "MAX_LENGTH", length = 11)
    private Integer maxLength;
    @Column(name = "MIN_LENGTH", length = 11)
    private Integer minLength;
    @Column(name = "FIELD_TYPE", length = 11)
    private Integer fieldType;
    @Column(name = "STATUS", length = 11)
    private Integer status;
    @Column(name = "CME_FK_ID", length = 11)
    private Integer entityType;
    @Column(name = "WIDTH", length = 11)
    private Integer width;
    @Column(name = "HEIGHT", length = 11)
    private Integer height;
    @Column(name = "GROUP_ID", length = 11)
    private String groupId;
    @Column(name = "PARENT_ID", length = 11)
    private Integer parentId;
    @Column(name = "FIELD_NAME", length = 512)
    private String name;
    @Column(name = "DESCRIPTION", length = 512)
    private String description;
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
    @Column(name = "IS_CORE")
    private boolean isCore;
    @Column(name = "EVENT_HANDLER")
    private String eventHandler;
    @Column(name = "SOURCE_URL", length = 250)
    private String sourceUrl;
    @Column(name = "IS_MANDATORY")
    private boolean mandatory;
    @Column(name = "IS_VISIBLE")
    private boolean visible;
    @Column(name = "IS_SEARCHABLE")
    private boolean searchable;
    @Column(name = "IS_MODIFIABLE")
    private boolean modifiable;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    @Column(name = "DISPLAY_ORDER_ID", length = 11)
    private Integer displayOrderId;
    @Column(name = "RENDERING_HINTS", length = 512)
    private String renderingHints;
    @ManyToOne
    @JoinColumn(name = "CMD_FK_ID", referencedColumnName = "CMD_PK_ID")
    private DataType dataType;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, mappedBy = "entityField")
    @Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
    private Set<FieldValidationRule> rules;
    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "entityField")
    @Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
    private Set<EntityFieldLang> langs;
    
    public EntityField() {
        this.rules = new HashSet<FieldValidationRule>();
        this.langs = new HashSet<EntityFieldLang>();
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
    
    public Set<FieldValidationRule> getRules() {
        return this.rules;
    }
    
    public void setRules(final Set<FieldValidationRule> rules) {
        this.rules = rules;
    }
    
    public Set<EntityFieldLang> getLangs() {
        return this.langs;
    }
    
    public void setLangs(final Set<EntityFieldLang> langs) {
        this.langs = langs;
    }
    
    public DataType getDataType() {
        return this.dataType;
    }
    
    public void setDataType(final DataType dataType) {
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
    
    public boolean isSearchable() {
        return this.searchable;
    }
    
    public void setSearchable(final boolean searchable) {
        this.searchable = searchable;
    }
}
