package com.kpisoft.strategy.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "STR_DET_STRATEGY_TREE")
@SQLDelete(sql = "UPDATE STR_DET_STRATEGY_TREE SET IS_DELETED = 1 WHERE STRN_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class StrategyNode extends BaseTenantEntity
{
    private static final long serialVersionUID = 2237951844192096439L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "STR_NODE_ID_SEQ")
    @SequenceGenerator(name = "STR_NODE_ID_SEQ", sequenceName = "STR_NODE_ID_SEQ")
    @Column(name = "STRN_PK_ID", length = 11)
    private Integer id;
    @Column(name = "DISPLAY_ORDER", length = 11)
    private Integer displayOrder;
    @Column(name = "PARENT_ID", length = 11)
    private Integer parentId;
    @Column(name = "NAME", length = 127)
    private String name;
    @Column(name = "IS_DELETED")
    private boolean isDeleted;
    @Lob
    @Column(name = "IMAGE_ICON")
    private byte[] image_icon;
    @Column(name = "CODE", length = 127)
    private String code;
    @Column(name = "STR_LVL_FK_ID", length = 11)
    private Integer strLvlId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getDisplayOrder() {
        return this.displayOrder;
    }
    
    public void setDisplayOrder(final Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public Integer getParentId() {
        return this.parentId;
    }
    
    public void setParentId(final Integer parentId) {
        this.parentId = parentId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public byte[] getImage_icon() {
        return this.image_icon;
    }
    
    public void setImage_icon(final byte[] image_icon) {
        this.image_icon = image_icon;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public void setCode(final String code) {
        this.code = code;
    }
    
    public Integer getStrLvlId() {
        return this.strLvlId;
    }
    
    public void setStrLvlId(final Integer strLvlId) {
        this.strLvlId = strLvlId;
    }
}
