package com.canopus.entity.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COR_MET_ATTACHMENT")
@SQLDelete(sql = "UPDATE COR_MET_ATTACHMENT SET IS_DELETED = 1 WHERE CMA_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class SystemAttachmentDetails extends BaseDataAccessEntity
{
    private static final long serialVersionUID = -7360485981964771272L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "COR_ATTACH_ID_SEQ")
    @SequenceGenerator(name = "COR_ATTACH_ID_SEQ", sequenceName = "COR_ATTACH_ID_SEQ")
    @Column(name = "CMA_PK_ID", length = 11)
    private Integer id;
    @Column(name = "DOC_NAME", length = 127)
    private String docName;
    @Column(name = "DOC_TYPE", length = 127)
    private String docType;
    @Column(name = "DOC_SIZE", length = 11)
    private String size;
    @Column(name = "DOC_URL", length = 255)
    private String url;
    @Lob
    @Column(name = "DOCUMENT")
    private byte[] document;
    @ManyToOne
    @JoinColumn(name = "SME_PK_ID")
    private BaseEntity baseEntity;
    @Column(name = "REFERENCE_ID", length = 11)
    private Integer referenceId;
    @Column(name = "REFERENCE_KEY", length = 255)
    private String referenceKey;
    @Column(name = "is_deleted")
    private Boolean deleted;
    
    public SystemAttachmentDetails() {
        this.deleted = false;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getDocName() {
        return this.docName;
    }
    
    public void setDocName(final String docName) {
        this.docName = docName;
    }
    
    public String getDocType() {
        return this.docType;
    }
    
    public void setDocType(final String docType) {
        this.docType = docType;
    }
    
    public byte[] getDocument() {
        return this.document;
    }
    
    public void setDocument(final byte[] document) {
        this.document = document;
    }
    
    public BaseEntity getBaseEntity() {
        return this.baseEntity;
    }
    
    public void setBaseEntity(final BaseEntity baseEntity) {
        this.baseEntity = baseEntity;
    }
    
    public Integer getReferenceId() {
        return this.referenceId;
    }
    
    public void setReferenceId(final Integer referenceId) {
        this.referenceId = referenceId;
    }
    
    public Boolean getDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final Boolean deleted) {
        this.deleted = deleted;
    }
    
    public String getSize() {
        return this.size;
    }
    
    public void setSize(final String size) {
        this.size = size;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    public String getReferenceKey() {
        return this.referenceKey;
    }
    
    public void setReferenceKey(final String referenceKey) {
        this.referenceKey = referenceKey;
    }
}
