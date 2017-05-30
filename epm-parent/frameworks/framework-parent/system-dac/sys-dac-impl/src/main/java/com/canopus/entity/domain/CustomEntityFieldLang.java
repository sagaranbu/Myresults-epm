package com.canopus.entity.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COR_CUSTOM_ENTITY_FIELD_LANG")
@SQLDelete(sql = "UPDATE COR_CUSTOM_ENTITY_FIELD_LANG SET IS_DELETED = 1 WHERE CFL_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class CustomEntityFieldLang extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 4703586595785605744L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "COR_ENTITY_FIELD_LANG_ID_SEQ")
    @SequenceGenerator(name = "COR_ENTITY_FIELD_LANG_ID_SEQ", sequenceName = "COR_ENTITY_FIELD_LANG_ID_SEQ")
    @Column(name = "CFL_PK_ID")
    private Integer id;
    @Column(name = "LOCALE", length = 127)
    private String locale;
    @Column(name = "DISPLAY_NAME", length = 512)
    private String displayName;
    @Column(name = "HELP_TEXT", length = 512)
    private String helpText;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @Column(name = "DESCRIPTION", length = 512)
    private String description;
    @Column(name = "ERROR_TEXT", length = 512)
    private String errorText;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CCF_FK_ID", referencedColumnName = "CCF_PK_ID")
    private CustomEntityFields entityFieldId;
    
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
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    public String getHelpText() {
        return this.helpText;
    }
    
    public void setHelpText(final String helpText) {
        this.helpText = helpText;
    }
    
    public CustomEntityFields getEntityFieldId() {
        return this.entityFieldId;
    }
    
    public void setEntityFieldId(final CustomEntityFields entityFieldId) {
        this.entityFieldId = entityFieldId;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public String getErrorText() {
        return this.errorText;
    }
    
    public void setErrorText(final String errorText) {
        this.errorText = errorText;
    }
}
