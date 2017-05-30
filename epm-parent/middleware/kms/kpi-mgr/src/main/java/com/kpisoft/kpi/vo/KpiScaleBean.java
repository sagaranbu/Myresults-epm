package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class KpiScaleBean extends BaseValueObject
{
    private static final long serialVersionUID = -3269006960994723199L;
    private Integer id;
    private String code;
    private Integer level;
    private List<ScaleValueBean> alkpiMasterScaleValue;
    
    public KpiScaleBean() {
        this.alkpiMasterScaleValue = new ArrayList<ScaleValueBean>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public void setCode(final String code) {
        this.code = code;
    }
    
    public Integer getLevel() {
        return this.level;
    }
    
    public void setLevel(final Integer level) {
        this.level = level;
    }
    
    public List<ScaleValueBean> getAlkpiMasterScaleValue() {
        Collections.sort(this.alkpiMasterScaleValue);
        return this.alkpiMasterScaleValue;
    }
    
    public void setAlkpiMasterScaleValue(final List<ScaleValueBean> alkpiMasterScaleValue) {
        this.alkpiMasterScaleValue = alkpiMasterScaleValue;
    }
}
