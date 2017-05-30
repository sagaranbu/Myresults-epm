package com.canopus.entity.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COR_MAS_MASTER_LANG")
@SQLDelete(sql = "UPDATE COR_MAS_MASTER_LANG SET IS_DELETED = 1 WHERE CML_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class SystemMasterBaseLang extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "COR_MAS_BASE_LANG_ID_SEQ")
    @SequenceGenerator(name = "COR_MAS_BASE_LANG_ID_SEQ", sequenceName = "COR_MAS_BASE_LANG_ID_SEQ")
    @Column(name = "CML_PK_ID")
    private Integer id;
    @Column(name = "HELP_TEXT", length = 512)
    private String helpText;
    @Column(name = "DESCRIPTION", length = 512)
    private String description;
    @Column(name = "DISPLAY_NAME", length = 512)
    private String displayName;
    @Column(name = "ERROR_TEXT", length = 512)
    private String errorText;
    @Column(name = "LOCALE_NAME", length = 127)
    private String locale;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @Column(name = "CMM_FK_ID")
    private Integer sysMasId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getHelpText() {
        return this.helpText;
    }
    
    public void setHelpText(final String helpText) {
        this.helpText = helpText;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    public String getErrorText() {
        return this.errorText;
    }
    
    public void setErrorText(final String errorText) {
        this.errorText = errorText;
    }
    
    public String getLocale() {
        return this.locale;
    }
    
    public void setLocale(final String locale) {
        this.locale = locale;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public Integer getSysMasId() {
        return this.sysMasId;
    }
    
    public void setSysMasId(final Integer sysMasId) {
        this.sysMasId = sysMasId;
    }
}
