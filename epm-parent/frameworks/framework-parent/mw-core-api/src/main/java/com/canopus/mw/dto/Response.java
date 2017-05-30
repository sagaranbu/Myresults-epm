package com.canopus.mw.dto;

import java.io.*;
import java.util.*;
import com.canopus.mw.*;
import com.canopus.mw.dto.param.*;

public class Response implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected Map<String, BaseValueObject> responseValueObjects;
    protected List<Exception> errors;
    
    public Response() {
        this.responseValueObjects = new HashMap<String, BaseValueObject>();
        this.errors = new ArrayList<Exception>();
    }
    
    public Map<String, BaseValueObject> getResponseValueObjects() {
        return this.responseValueObjects;
    }
    
    public BaseValueObject get(final String key) {
        if (this.getErrors().size() > 0) {
            final Exception e = this.getErrors().get(0);
            e.printStackTrace();
            throw new MiddlewareException("DACERROR", e.getMessage(), e);
        }
        return this.responseValueObjects.get(key);
    }
    
    public void put(final String key, final BaseValueObject vo) {
        this.responseValueObjects.put(key, vo);
    }
    
    public void addError(final Exception e) {
        this.errors.add(e);
    }
    
    public List<Exception> getErrors() {
        return this.errors;
    }
    
    public void setPage(final int pageNumber, final int totalCount) {
        Page page = (Page)this.get(HeaderParam.PAGE.name());
        if (page == null) {
            page = new Page();
        }
        page.setPageNumber(pageNumber);
        page.setTotalCount(totalCount);
        this.put(HeaderParam.PAGE.name(), page);
    }
    
    public Page getPage() {
        final Page page = (Page)this.get(HeaderParam.PAGE.name());
        return page;
    }
    
    public SortList getSortList() {
        return (SortList)this.get(HeaderParam.SORT_LIST.name());
    }
    
    public void setSortList(final SortList sortList) {
        this.put(HeaderParam.SORT_LIST.name(), sortList);
    }
}
