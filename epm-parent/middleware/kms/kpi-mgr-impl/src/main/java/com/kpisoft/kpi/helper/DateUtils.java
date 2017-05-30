package com.kpisoft.kpi.helper;

import java.util.*;
import java.text.*;

public class DateUtils
{
    private static final SimpleDateFormat monthFormatter;
    private static final SimpleDateFormat dateFormatter;
    
    public static Date toDate(final Integer date) {
        return toDate(date, DateUtils.dateFormatter);
    }
    
    public static Integer fromDate(final Date date) {
        return fromDate(date, DateUtils.dateFormatter);
    }
    
    public static Date toMonth(final Integer month) {
        return toDate(month, DateUtils.monthFormatter);
    }
    
    public static Integer fromMonth(final Date date) {
        return fromDate(date, DateUtils.monthFormatter);
    }
    
    private static Date toDate(final Integer date, final SimpleDateFormat formatter) {
        if (date != null) {
            try {
                return formatter.parse(String.valueOf(date));
            }
            catch (ParseException ex) {}
        }
        return null;
    }
    
    private static Integer fromDate(final Date date, final SimpleDateFormat formatter) {
        if (date != null) {
            return new Integer(formatter.format(date));
        }
        return new Integer(0);
    }
    
    static {
        monthFormatter = new SimpleDateFormat("yyyyMM");
        dateFormatter = new SimpleDateFormat("yyyyMMdd");
    }
}
