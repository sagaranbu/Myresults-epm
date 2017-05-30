package com.canopus.entity.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COR_SUM_TAGCOUNT")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class TagSummary extends BaseDataAccessEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CST_ID_SEQ")
    @SequenceGenerator(name = "CST_ID_SEQ", sequenceName = "CST_ID_SEQ")
    @Column(name = "CST_PK_ID", length = 11)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "PDI_FK_ID")
    private Tag tag;
    @Column(name = "CONTEXT", length = 255)
    private String context;
    @Column(name = "COUNT")
    private Integer count;
    @Column(name = "IS_DELETED", length = 11)
    private boolean deleted;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Tag getTag() {
        return this.tag;
    }
    
    public void setTag(final Tag tag) {
        this.tag = tag;
    }
    
    public String getContext() {
        return this.context;
    }
    
    public void setContext(final String context) {
        this.context = context;
    }
    
    public Integer getCount() {
        return this.count;
    }
    
    public void setCount(final Integer count) {
        this.count = count;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}
