package com.kpisoft.kpi.dac.impl.domain;

import com.canopus.dac.*;
import javax.persistence.*;

public class KpiLang extends BaseTenantEntity
{
    private static final long serialVersionUID = 6976451055374507122L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "KPI_LANG_ID_SEQ")
    @SequenceGenerator(name = "KPI_LANG_ID_SEQ", sequenceName = "KPI_LANG_ID_SEQ")
    @Column(name = "KL_PK_ID", length = 11)
    private Integer id;
    @Column(name = "LOCALE_NAME", length = 127)
    private String locale;
    @Column(name = "NAME", length = 127)
    private String name;
    @Column(name = "DESCRIPTION", length = 250)
    private String description;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getLocale() {
        return this.locale;
    }
    
    public void setLocale(final String locale) {
        this.locale = locale;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}
