package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "KPI_MAS_SCALE_VALUE")
@SQLDelete(sql = "UPDATE KPI_MAS_SCALE_VALUE SET IS_DELETED = 1 WHERE KMV_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class ScaleValue extends BaseTenantEntity
{
    private static final long serialVersionUID = -4235405340739649147L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCL_VAL_ID_SEQ")
    @SequenceGenerator(name = "SCL_VAL_ID_SEQ", sequenceName = "SCL_VAL_ID_SEQ", allocationSize = 100)
    @Column(name = "KMV_PK_ID", length = 11)
    private Integer id;
    @Column(name = "TO_VALUE", length = 11)
    private Double toValue;
    @Column(name = "FROM_VALUE", length = 11)
    private Double fromValue;
    @Column(name = "COLOR", length = 127)
    private String color;
    @Column(name = "SCALE_INDEX", length = 11)
    private Integer scaleIndex;
    @Column(name = "TYPED", length = 11)
    private Integer type;
    @Column(name = "ORDERED", length = 11)
    private Integer order;
    @Column(name = "STATUS_ICON", length = 127)
    private String statusIcon;
    @Column(name = "KMS_FK_ID", length = 11, insertable = false, updatable = false)
    private Integer scaleId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KMS_FK_ID", nullable = false)
    private Scale scale;
    @Column(name = "TITLE", length = 127)
    private String title;
    @Column(name = "IS_DEFAULT")
    private Boolean isDefault;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    @Column(name = "FROM_POINT")
    private Double fromPoint;
    @Column(name = "TO_POINT")
    private Double toPoint;
    @Column(name = "TO_DATE", length = 45)
    @Temporal(TemporalType.DATE)
    private Date toDate;
    @Column(name = "FROM_DATE", length = 45)
    @Temporal(TemporalType.DATE)
    private Date fromDate;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Double getToValue() {
        return this.toValue;
    }
    
    public void setToValue(final Double toValue) {
        this.toValue = toValue;
    }
    
    public Double getFromValue() {
        return this.fromValue;
    }
    
    public void setFromValue(final Double fromValue) {
        this.fromValue = fromValue;
    }
    
    public Date getToDate() {
        return this.toDate;
    }
    
    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }
    
    public Date getFromDate() {
        return this.fromDate;
    }
    
    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }
    
    public String getColor() {
        return this.color;
    }
    
    public void setColor(final String color) {
        this.color = color;
    }
    
    public Integer getScaleIndex() {
        return this.scaleIndex;
    }
    
    public void setScaleIndex(final Integer scaleIndex) {
        this.scaleIndex = scaleIndex;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public Integer getOrder() {
        return this.order;
    }
    
    public void setOrder(final Integer order) {
        this.order = order;
    }
    
    public String getStatusIcon() {
        return this.statusIcon;
    }
    
    public void setStatusIcon(final String statusIcon) {
        this.statusIcon = statusIcon;
    }
    
    public Integer getScaleId() {
        return this.scaleId;
    }
    
    public void setScaleId(final Integer scaleId) {
        this.scaleId = scaleId;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public Boolean getIsDefault() {
        return this.isDefault;
    }
    
    public void setIsDefault(final Boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    public Scale getScale() {
        return this.scale;
    }
    
    public void setScale(final Scale scale) {
        this.scale = scale;
    }
    
    public Double getFromPoint() {
        return this.fromPoint;
    }
    
    public void setFromPoint(final Double fromPoint) {
        this.fromPoint = fromPoint;
    }
    
    public Double getToPoint() {
        return this.toPoint;
    }
    
    public void setToPoint(final Double toPoint) {
        this.toPoint = toPoint;
    }
}
