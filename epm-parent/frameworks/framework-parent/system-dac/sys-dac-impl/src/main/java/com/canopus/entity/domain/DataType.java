package com.canopus.entity.domain;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "COR_MAS_DATA_TYPE")
public class DataType extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 3894164721920719205L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SMD_ID_SEQ")
    @SequenceGenerator(name = "SMD_ID_SEQ", sequenceName = "SMD_ID_SEQ")
    @Column(name = "CMD_PK_ID")
    private Integer id;
    @Column(name = "DATA_TYPE", length = 512)
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
