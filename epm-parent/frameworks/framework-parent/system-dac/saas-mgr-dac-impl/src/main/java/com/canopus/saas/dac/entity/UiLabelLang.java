package com.canopus.saas.dac.entity;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "SAS_DET_UI_LABEL_LANG")
public class UiLabelLang extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 8437129496804981325L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "LABEL_LANG_ID_SEQ")
    @SequenceGenerator(name = "LABEL_LANG_ID_SEQ", sequenceName = "LABEL_LANG_ID_SEQ")
    @Column(name = "SDL_PK_ID", length = 11)
    private Integer id;
    @Column(name = "LOCALE_NAME", length = 127)
    private String locale;
    @Column(name = "DISPLAY_NAME", length = 127)
    private String displayName;
    @Column(name = "HELP_TEXT", length = 127)
    private String helpText;
    @Column(name = "ERROR_TEXT", length = 127)
    private String errorText;
    @Column(name = "DESCRIPTION", length = 127)
    private String description;
    @Column(name = "SDB_FK_ID", length = 11)
    private Integer labelBaseId;
    
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
    
    public Integer getLabelBaseId() {
        return this.labelBaseId;
    }
    
    public void setLabelBaseId(final Integer labelBaseId) {
        this.labelBaseId = labelBaseId;
    }
    
    public String getErrorText() {
        return this.errorText;
    }
    
    public void setErrorText(final String errorText) {
        this.errorText = errorText;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
}
