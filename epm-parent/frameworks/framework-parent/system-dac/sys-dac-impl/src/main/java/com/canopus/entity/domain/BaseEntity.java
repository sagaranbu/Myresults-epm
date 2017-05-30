package com.canopus.entity.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COR_MAS_ENTITY")
@SQLDelete(sql = "UPDATE COR_MAS_ENTITY SET IS_DELETED = 1 WHERE CME_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class BaseEntity extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 7756626739569519960L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "COR_ENTITY_ID_SEQ")
    @SequenceGenerator(name = "COR_ENTITY_ID_SEQ", sequenceName = "COR_ENTITY_ID_SEQ")
    @Column(name = "CME_PK_ID")
    private Integer id;
    @Column(name = "NAME", length = 127)
    private String name;
    @Column(name = "TYPE", length = 127)
    private Integer type;
    @Column(name = "DESCRIPTION", length = 127)
    private String description;
    @Column(name = "IMAGE", length = 127)
    private byte[] image;
    @Column(name = "FILE_EXTENSION", length = 127)
    private String fileExtension;
    @Column(name = "IS_DELETED")
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
    
    public Integer getType() {
        return this.type;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public byte[] getImage() {
        return this.image;
    }
    
    public void setImage(final byte[] image) {
        this.image = image;
    }
    
    public String getFileExtension() {
        return this.fileExtension;
    }
    
    public void setFileExtension(final String fileExtension) {
        this.fileExtension = fileExtension;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}
