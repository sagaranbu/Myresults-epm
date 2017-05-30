package com.canopus.entity.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COR_MET_FIELD_LANG")
@SQLDelete(sql = "UPDATE COR_MET_FIELD_LANG SET IS_DELETED = 1 WHERE CML_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class EntityFieldLang extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 659015616778882543L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "COR_FIELD_LANG_ID_SEQ")
    @SequenceGenerator(name = "COR_FIELD_LANG_ID_SEQ", sequenceName = "COR_FIELD_LANG_ID_SEQ")
    @Column(name = "CML_PK_ID")
    private Integer id;
    @Column(name = "DISPLAY_NAME", length = 512)
    private String displayName;
    @Column(name = "LOCALE_NAME", length = 127)
    private String locale;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @Column(name = "ERROR_TEXT", length = 512)
    private String errorText;
    @Column(name = "HELP_TEXT", length = 512)
    private String helpText;
    @Column(name = "DESCRIPTION", length = 512)
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CMF_FK_ID", referencedColumnName = "CMF_PK_ID")
    private EntityField entityField;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
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
    
    public String getErrorText() {
        return this.errorText;
    }
    
    public void setErrorText(final String errorText) {
        this.errorText = errorText;
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
    
    public EntityField getEntityField() {
        return this.entityField;
    }
    
    public void setEntityField(final EntityField entityField) {
        this.entityField = entityField;
    }
}
