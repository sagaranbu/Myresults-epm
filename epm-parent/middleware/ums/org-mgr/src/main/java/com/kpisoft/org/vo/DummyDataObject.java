package com.kpisoft.org.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class DummyDataObject extends BaseValueObject
{
    private static final long serialVersionUID = 1L;
    private int id;
    private List<Integer> dummydata;
    private List<Integer> oUIds;
    private List<Integer> oldOUIds;
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public List<Integer> getDummydata() {
        return this.dummydata;
    }
    
    public void setDummydata(final List<Integer> dummydata) {
        this.dummydata = dummydata;
    }
    
    public List<Integer> getOldOUIds() {
        return this.oldOUIds;
    }
    
    public void setOldOUIds(final List<Integer> oldOUIds) {
        this.oldOUIds = oldOUIds;
    }
    
    public List<Integer> getOUIds() {
        return this.oUIds;
    }
    
    public void setOUIds(final List<Integer> oUIds) {
        this.oUIds = oUIds;
    }
}
