package com.canopus.saas.dac.entity;

import com.canopus.dac.*;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "SAS_DET_UI_LABEL_BASE")
public class UiLabelBase extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 3497247349624570543L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "LABEL_BASE_ID_SEQ")
    @SequenceGenerator(name = "LABEL_BASE_ID_SEQ", sequenceName = "LABEL_BASE_ID_SEQ")
    @Column(name = "SDB_PK_ID", length = 11)
    private Integer id;
    @Column(name = "SDT_FK_ID", length = 11)
    private Integer tenantId;
    @Column(name = "HEIGHT", length = 11)
    private Integer height;
    @Column(name = "DISPLAY_ORDER_ID", length = 11)
    private Integer displayOrderId;
    @Column(name = "FIELD_NAME", length = 45)
    private String name;
    @Column(name = "IS_VISIBLE")
    private boolean visible;
    @Column(name = "PAGE_CODE")
    private String pageCode;
    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "labelBaseId")
    private List<UiLabelLang> labelLangData;
    
    public UiLabelBase() {
        this.labelLangData = new ArrayList<UiLabelLang>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final Integer tenantId) {
        this.tenantId = tenantId;
    }
    
    public Integer getHeight() {
        return this.height;
    }
    
    public void setHeight(final Integer height) {
        this.height = height;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
    
    public List<UiLabelLang> getLabelLangData() {
        return this.labelLangData;
    }
    
    public void setLabelLangData(final List<UiLabelLang> labelLangData) {
        this.labelLangData = labelLangData;
    }
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }
    
    public String getPageCode() {
        return this.pageCode;
    }
    
    public void setPageCode(final String pageCode) {
        this.pageCode = pageCode;
    }
}
