package com.canopus.entity.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COR_MAS_TAG")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class Tag extends BaseDataAccessEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CMT_ID_SEQ")
    @SequenceGenerator(name = "CMT_ID_SEQ", sequenceName = "CMT_ID_SEQ")
    @Column(name = "CMT_PK_ID", length = 11)
    private Integer id;
    @Column(name = "NAME", length = 255)
    private String name;
    @Column(name = "CATEGORY", length = 255)
    private String category;
    @Column(name = "TYPE")
    private Integer type;
    @Column(name = "IS_DELETED", length = 11)
    private boolean deleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public void setCategory(final String category) {
        this.category = category;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}
