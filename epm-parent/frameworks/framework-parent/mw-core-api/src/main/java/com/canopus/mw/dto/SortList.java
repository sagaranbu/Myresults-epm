package com.canopus.mw.dto;

import java.util.*;

public class SortList extends BaseValueObject
{
    private static final long serialVersionUID = -2597898331400134129L;
    private List<Sort> sortList;
    
    public SortList() {
        this.sortList = new ArrayList<Sort>();
    }
    
    public List<Sort> getSortList() {
        return this.sortList;
    }
    
    public void setSortList(final List<Sort> sortList) {
        this.sortList = sortList;
    }
}
