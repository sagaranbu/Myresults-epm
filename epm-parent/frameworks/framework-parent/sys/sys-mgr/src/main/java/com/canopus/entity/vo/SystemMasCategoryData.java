package com.canopus.entity.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class SystemMasCategoryData extends BaseValueObject
{
    private static final long serialVersionUID = 6334860247917724370L;
    private Integer id;
    private String name;
    private List<SystemMasterBaseBean> sytems;
    
    public SystemMasCategoryData() {
        this.sytems = new ArrayList<SystemMasterBaseBean>();
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
    
    public List<SystemMasterBaseBean> getSytems() {
        return this.sytems;
    }
    
    public void setSytems(final List<SystemMasterBaseBean> sytems) {
        this.sytems = sytems;
    }
}
