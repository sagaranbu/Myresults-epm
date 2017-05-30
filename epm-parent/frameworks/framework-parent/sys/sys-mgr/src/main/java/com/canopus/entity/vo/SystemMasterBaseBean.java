package com.canopus.entity.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class SystemMasterBaseBean extends BaseValueObject
{
    private static final long serialVersionUID = -8138897474092314725L;
    private Integer id;
    private String value;
    private boolean isDeleted;
    private SystemMasCategoryData category;
    private SystemMasCategoryData subCategory;
    private List<SystemMasterBaseLangData> langs;
    
    public SystemMasterBaseBean() {
        this.langs = new ArrayList<SystemMasterBaseLangData>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public SystemMasCategoryData getCategory() {
        return this.category;
    }
    
    public void setCategory(final SystemMasCategoryData category) {
        this.category = category;
    }
    
    public SystemMasCategoryData getSubCategory() {
        return this.subCategory;
    }
    
    public void setSubCategory(final SystemMasCategoryData subCategory) {
        this.subCategory = subCategory;
    }
    
    public List<SystemMasterBaseLangData> getLangs() {
        return this.langs;
    }
    
    public void setLangs(final List<SystemMasterBaseLangData> langs) {
        this.langs = langs;
    }
}
