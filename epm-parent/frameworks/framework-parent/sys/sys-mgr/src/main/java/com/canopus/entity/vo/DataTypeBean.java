package com.canopus.entity.vo;

import com.canopus.mw.dto.*;

public class DataTypeBean extends BaseValueObject
{
    private static final long serialVersionUID = 5411888144365478228L;
    private Integer id;
    private String name;
    
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
}
