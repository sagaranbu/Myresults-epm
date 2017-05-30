package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class KpiTagBean extends BaseValueObject
{
    private static final long serialVersionUID = -6997571324424115424L;
    private Integer id;
    private String name;
    private String category;
    private Integer displayOrderId;
    private List<KpiTagRelationshipBean> kpiTagRelationship;
    
    public KpiTagBean() {
        this.kpiTagRelationship = new ArrayList<KpiTagRelationshipBean>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public void setCategory(final String category) {
        this.category = category;
    }
    
    public List<KpiTagRelationshipBean> getKpiTagRelationship() {
        return this.kpiTagRelationship;
    }
    
    public void setKpiTagRelationship(final List<KpiTagRelationshipBean> kpiTagRelationship) {
        this.kpiTagRelationship = kpiTagRelationship;
    }
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }
}
