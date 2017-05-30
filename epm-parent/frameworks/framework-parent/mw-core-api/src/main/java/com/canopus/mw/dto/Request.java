package com.canopus.mw.dto;

import java.io.*;
import java.util.*;
import com.canopus.mw.dto.param.*;

public class Request implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected Map<String, Object> context;
    protected Map<String, BaseValueObject> requestValueObjects;
    
    public Request() {
        this.requestValueObjects = new HashMap<String, BaseValueObject>();
        final Map<String, Object> currContext = ExecutionContext.getCurrent().getContextValues();
        this.context = ((currContext == null) ? new HashMap<String, Object>() : new HashMap<String, Object>(currContext));
    }
    
    public Map<String, Object> getContext() {
        return this.context;
    }
    
    public void setContext(final Map<String, Object> context) {
        this.context = context;
    }
    
    public Map<String, BaseValueObject> getRequestValueObjects() {
        return this.requestValueObjects;
    }
    
    public BaseValueObject get(final String key) {
        return this.requestValueObjects.get(key);
    }
    
    public void put(final String key, final BaseValueObject vo) {
        this.requestValueObjects.put(key, vo);
    }
    
    public void setPage(final int pageNumber, final int maxResults) {
        Page page = (Page)this.get(HeaderParam.PAGE.name());
        if (page == null) {
            page = new Page();
        }
        page.setMaxResults(maxResults);
        page.setPageNumber(pageNumber);
        this.put(HeaderParam.PAGE.name(), page);
    }
    
    public Page getPage() {
        Page page = (Page)this.get(HeaderParam.PAGE.name());
        if (page == null) {
            page = new Page();
        }
        return page;
    }
    
    public void addSort(final String field) {
        final Sort sort = new Sort();
        sort.setField(field);
        SortList sortList = (SortList)this.get(HeaderParam.SORT_LIST.name());
        if (sortList == null) {
            sortList = new SortList();
        }
        sortList.getSortList().add(sort);
        this.put(HeaderParam.SORT.name(), sort);
        this.put(HeaderParam.SORT_LIST.name(), sortList);
    }
    
    public void addSort(final String field, final boolean isDesc) {
        final Sort sort = new Sort();
        sort.setField(field);
        sort.setDesc(isDesc);
        SortList sortList = (SortList)this.get(HeaderParam.SORT_LIST.name());
        if (sortList == null) {
            sortList = new SortList();
        }
        sortList.getSortList().add(sort);
        this.put(HeaderParam.SORT.name(), sort);
        this.put(HeaderParam.SORT_LIST.name(), sortList);
    }
    
    public void addSort(final String field, final boolean isDesc, final boolean ignoreCase) {
        final Sort sort = new Sort();
        sort.setField(field);
        sort.setDesc(isDesc);
        sort.setIgnoreCase(ignoreCase);
        SortList sortList = (SortList)this.get(HeaderParam.SORT_LIST.name());
        if (sortList == null) {
            sortList = new SortList();
        }
        sortList.getSortList().add(sort);
        this.put(HeaderParam.SORT.name(), sort);
        this.put(HeaderParam.SORT_LIST.name(), sortList);
    }
    
    public void setPage(final Page page) {
        this.put(HeaderParam.PAGE.name(), page);
    }
    
    public void setSortList(final SortList sortList) {
        this.put(HeaderParam.SORT_LIST.name(), sortList);
    }
    
    public SortList getSortList() {
        SortList sortList = (SortList)this.get(HeaderParam.SORT_LIST.name());
        if (sortList == null) {
            sortList = new SortList();
        }
        return sortList;
    }
}
