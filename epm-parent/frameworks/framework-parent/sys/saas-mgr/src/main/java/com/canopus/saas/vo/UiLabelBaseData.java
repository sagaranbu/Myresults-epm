package com.canopus.saas.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class UiLabelBaseData extends BaseValueObject implements Comparable<UiLabelBaseData>
{
    private static final long serialVersionUID = 3497247349624570543L;
    private Integer id;
    private Integer tenantId;
    private Integer height;
    private Integer displayOrderId;
    private String name;
    private String pageCode;
    private boolean visible;
    private List<UiLabelLangData> labelLangData;
    
    public UiLabelBaseData() {
        this.labelLangData = new ArrayList<UiLabelLangData>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final Integer tenantId) {
        this.tenantId = tenantId;
    }
    
    public Integer getHeight() {
        return this.height;
    }
    
    public void setHeight(final Integer height) {
        this.height = height;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
    
    public List<UiLabelLangData> getLabelLangData() {
        return this.labelLangData;
    }
    
    public void setLabelLangData(final List<UiLabelLangData> labelLangData) {
        this.labelLangData = labelLangData;
    }
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }
    
    public String getPageCode() {
        return this.pageCode;
    }
    
    public void setPageCode(final String pageCode) {
        this.pageCode = pageCode;
    }
    
    public int compareTo(final UiLabelBaseData data) {
        if (data.getDisplayOrderId() != null && this.displayOrderId != null) {
            return this.displayOrderId - data.getDisplayOrderId();
        }
        if (this.displayOrderId != null) {
            return this.displayOrderId;
        }
        if (data.getDisplayOrderId() != null) {
            return -1;
        }
        return 0;
    }
}
