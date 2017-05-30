package com.canopus.saas.dac.entity;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "SAS_DET_TENANT_LANGUAGE")
public class TenantLangBase extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "TENANT_BASE_LANG_ID_SEQ")
    @SequenceGenerator(name = "TENANT_BASE_LANG_ID_SEQ", sequenceName = "TENANT_BASE_LANG_ID_SEQ")
    @Column(name = "SML_PK_ID", length = 11)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SMT_PK_ID")
    private TenantBase tenantBase;
    @Column(name = "SML_LANGUAGE")
    private String maslang;
    
    public TenantBase getTenantBase() {
        return this.tenantBase;
    }
    
    public void setTenantBase(final TenantBase tenantBase) {
        this.tenantBase = tenantBase;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getMaslang() {
        return this.maslang;
    }
    
    public void setMaslang(final String maslang) {
        this.maslang = maslang;
    }
}
