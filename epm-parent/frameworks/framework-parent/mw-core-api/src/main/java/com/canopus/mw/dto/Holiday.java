package com.canopus.mw.dto;

import java.util.*;

public class Holiday extends BaseValueObject
{
    private Date holidayDate;
    
    public Date getHolidayDate() {
        return this.holidayDate;
    }
    
    public void setHolidayDate(final Date holidayDate) {
        this.holidayDate = holidayDate;
    }
}
