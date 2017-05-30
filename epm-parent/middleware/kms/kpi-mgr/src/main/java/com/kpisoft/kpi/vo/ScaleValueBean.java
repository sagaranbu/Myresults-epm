package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;
import java.text.*;
import java.util.*;

public class ScaleValueBean extends BaseValueObject implements Comparable<ScaleValueBean>
{
    private static final long serialVersionUID = -4590612254148837492L;
    DecimalFormat df;
    private Integer id;
    private Double toValue;
    private Double fromValue;
    private String color;
    private Integer scaleIndex;
    private Integer type;
    private Integer order;
    private String statusIcon;
    private Integer scaleId;
    private String title;
    private Boolean isDefault;
    private Double fromPoint;
    private Double toPoint;
    private Date fromDate;
    private Date toDate;
    
    public ScaleValueBean() {
        this.df = new DecimalFormat("#.##");
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Double getToValue() {
        if (this.toValue != null) {
            this.toValue = Double.valueOf(this.df.format(this.toValue));
        }
        return this.toValue;
    }
    
    public void setToValue(final Double toValue) {
        this.toValue = toValue;
    }
    
    public Double getFromValue() {
        if (this.fromValue != null) {
            this.fromValue = Double.valueOf(this.df.format(this.fromValue));
        }
        return this.fromValue;
    }
    
    public void setFromValue(final Double fromValue) {
        this.fromValue = fromValue;
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
    
    public Double getFromPoint() {
        if (this.fromPoint != null) {
            this.fromPoint = Double.valueOf(this.df.format(this.fromPoint));
        }
        return this.fromPoint;
    }
    
    public void setFromPoint(final Double fromPoint) {
        this.fromPoint = fromPoint;
    }
    
    public Double getToPoint() {
        if (this.toPoint != null) {
            this.toPoint = Double.valueOf(this.df.format(this.toPoint));
        }
        return this.toPoint;
    }
    
    public void setToPoint(final Double toPoint) {
        this.toPoint = toPoint;
    }
    
    public Date getFromDate() {
        return this.fromDate;
    }
    
    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }
    
    public Date getToDate() {
        return this.toDate;
    }
    
    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }
    
    public int compareTo(final ScaleValueBean data) {
        if (data.getOrder() != null && this.order != null) {
            return this.order - data.getOrder();
        }
        if (this.order != null) {
            return this.order;
        }
        if (data.getOrder() != null) {
            return -1;
        }
        return 0;
    }
}
