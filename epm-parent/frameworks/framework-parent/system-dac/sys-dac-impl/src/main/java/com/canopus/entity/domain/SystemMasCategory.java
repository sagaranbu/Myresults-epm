package com.canopus.entity.domain;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "COR_MAS_CATEGORY")
public class SystemMasCategory extends BaseDataAccessEntity
{
    private static final long serialVersionUID = -6336613831745884640L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "COR_ID_SEQ")
    @SequenceGenerator(name = "COR_ID_SEQ", sequenceName = "COR_ID_SEQ")
    @Column(name = "CMC_PK_ID", length = 11)
    private Integer id;
    @Column(name = "CATEGORY_NAME", length = 512)
    private String name;
    
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
}
