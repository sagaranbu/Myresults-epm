package com.canopus.entity.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.*;

@Entity
@Table(name = "COR_MAS_MASTER_BASE")
@SQLDelete(sql = "UPDATE COR_MAS_MASTER_BASE SET IS_DELETED = 1 WHERE CMM_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class SystemMasterBaseMetaData extends BaseDataAccessEntity
{
    private static final long serialVersionUID = -5422106847423377848L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SMM_ID_SEQ")
    @SequenceGenerator(name = "SMM_ID_SEQ", sequenceName = "SMM_ID_SEQ")
    @Column(name = "CMM_PK_ID", length = 11)
    private Integer id;
    @Column(name = "VALUE", length = 512)
    private String value;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @ManyToOne
    @JoinColumn(name = "CATEGORY", referencedColumnName = "CMC_PK_ID")
    private SystemMasCategory category;
    @ManyToOne
    @JoinColumn(name = "SUB_CATEGORY", referencedColumnName = "CMC_PK_ID")
    private SystemMasCategory subCategory;
    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @JoinColumn(name = "CMM_FK_ID", referencedColumnName = "CMM_PK_ID")
    private List<SystemMasterBaseLang> langs;
    
    public SystemMasterBaseMetaData() {
        this.langs = new ArrayList<SystemMasterBaseLang>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public SystemMasCategory getCategory() {
        return this.category;
    }
    
    public void setCategory(final SystemMasCategory category) {
        this.category = category;
    }
    
    public SystemMasCategory getSubCategory() {
        return this.subCategory;
    }
    
    public void setSubCategory(final SystemMasCategory subCategory) {
        this.subCategory = subCategory;
    }
    
    public List<SystemMasterBaseLang> getLangs() {
        return this.langs;
    }
    
    public void setLangs(final List<SystemMasterBaseLang> langs) {
        this.langs = langs;
    }
}
