package com.canopus.entity.domain;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "COR_MAS_FIELD_TYPE")
public class FieldType extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 2827618426616912086L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SMF_ID_SEQ")
    @SequenceGenerator(name = "SMF_ID_SEQ", sequenceName = "SMF_ID_SEQ")
    @Column(name = "CMF_PK_ID")
    private Integer id;
    @Column(name = "FIELD_TYPE", length = 512)
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
