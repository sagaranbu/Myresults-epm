package com.canopus.mw.dto;

import java.util.*;
import org.codehaus.jackson.annotate.*;
import com.canopus.mw.dto.param.*;

public class MultiLangDomainObject<T extends MultiLangDataExtension>
{
    public static Class langDataClass;
    private Integer id;
    protected List<T> langTokens;
    
    public MultiLangDomainObject() {
        this.langTokens = new ArrayList<T>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    @JsonIgnore
    public List<T> getLangTokens() {
        return this.langTokens;
    }
    
    @JsonIgnore
    public void setLangTokens(final List<T> langTokens) {
        this.langTokens = langTokens;
    }
    
    @JsonProperty
    public String getName() {
        final String locale = getLocale();
        String name = null;
        for (final T langToken : this.langTokens) {
            final String curLocale = langToken.getLocale();
            if (curLocale != null && curLocale.equals(locale)) {
                name = langToken.getName();
            }
        }
        return name;
    }
    
    @JsonProperty
    public String getName(final String locale) {
        String name = null;
        for (final T langToken : this.langTokens) {
            final String curLocale = langToken.getLocale();
            if (curLocale != null && curLocale.equals(locale)) {
                name = langToken.getName();
            }
        }
        return name;
    }
    
    public void setName(final String value) {
        final String locale = getLocale();
        for (final T langToken : this.langTokens) {
            final String curLocale = langToken.getLocale();
            if (curLocale != null && curLocale.equals(locale)) {
                langToken.setName(value);
                return;
            }
        }
        try {
            final T data = (T) MultiLangDomainObject.langDataClass.newInstance();
            data.setName(value);
            data.setLocale(locale);
            data.setMainId(this.id);
            this.langTokens.add(data);
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }
    
    public void setName(final String localeName, final String value) {
        for (final T langToken : this.langTokens) {
            if (langToken.getLocale().equals(localeName)) {
                langToken.setName(value);
                return;
            }
        }
        try {
            final T data = (T) MultiLangDomainObject.langDataClass.newInstance();
            data.setName(value);
            data.setLocale(localeName);
            data.setMainId(this.id);
            this.langTokens.add(data);
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }
    
    @JsonProperty
    public String getDescription() {
        final String locale = getLocale();
        String desc = null;
        for (final T langToken : this.langTokens) {
            final String curLocale = langToken.getLocale();
            if (curLocale != null && curLocale.equals(locale)) {
                desc = langToken.getDescription();
            }
        }
        return desc;
    }
    
    @JsonProperty
    public String getDescription(final String locale) {
        String desc = null;
        for (final T langToken : this.langTokens) {
            if (langToken.getLocale().equals(locale)) {
                desc = langToken.getDescription();
            }
        }
        return desc;
    }
    
    public void setDescription(final String value) {
        final String locale = getLocale();
        for (final T langToken : this.langTokens) {
            final String curLocale = langToken.getLocale();
            if (curLocale != null && curLocale.equals(locale)) {
                langToken.setDescription(value);
                return;
            }
        }
        try {
            final T data = (T) MultiLangDomainObject.langDataClass.newInstance();
            data.setDescription(value);
            data.setLocale(locale);
            data.setMainId(this.id);
            this.langTokens.add(data);
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }
    
    public void setDescription(final String localeName, final String value) {
        for (final T langToken : this.langTokens) {
            if (langToken.getLocale().equals(localeName)) {
                langToken.setDescription(value);
                return;
            }
        }
        try {
            final T data = (T) MultiLangDomainObject.langDataClass.newInstance();
            data.setDescription(value);
            data.setLocale(localeName);
            data.setMainId(this.id);
            this.langTokens.add(data);
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }
    
    public static String getLocale() {
        String locale;
        if (ExecutionContext.getCurrent().getContextValues().get(HeaderParam.USER_LOCALE.getParamName()) != null) {
            locale = (String) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.USER_LOCALE.getParamName());
        }
        else if (ExecutionContext.getCurrent().getContextValues().get(HeaderParam.TENANT_LOCALE.getParamName()) != null) {
            locale = (String) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.TENANT_LOCALE.getParamName());
        }
        else {
            locale = (String) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.SYSTEM_LOCALE.getParamName());
        }
        return locale;
    }
}
