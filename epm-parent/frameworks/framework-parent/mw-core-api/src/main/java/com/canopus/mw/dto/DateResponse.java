package com.canopus.mw.dto;

import java.util.*;

public class DateResponse extends BaseValueObject
{
    private static final long serialVersionUID = -4553113072192237298L;
    private Date date;
    
    public DateResponse() {
        this.date = null;
    }
    
    public Date getDate() {
        return this.date;
    }
    
    public void setDate(final Date date) {
        this.date = date;
    }
}
