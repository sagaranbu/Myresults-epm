package com.canopus.entity.domain;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "COR_CUSTOM_ENTITY")
public class CustomEntity extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 4703586595785605744L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "COR_ENTITY_ID_SEQ")
    @SequenceGenerator(name = "COR_ENTITY_ID_SEQ", sequenceName = "COR_ENTITY_ID_SEQ")
    @Column(name = "CCE_PK_ID")
    private Integer id;
    @Column(name = "ENTITY_NAME", length = 512)
    private String name;
    @Column(name = "BASE_URL", length = 127)
    private String baseUrl;
    @Lob
    @Column(name = "IMAGE")
    private byte[] image;
    
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
    
    public String getBaseUrl() {
        return this.baseUrl;
    }
    
    public void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public byte[] getImage() {
        return this.image;
    }
    
    public void setImage(final byte[] image) {
        this.image = image;
    }
}
