package com.canopus.entity.vo;

import com.canopus.mw.dto.*;

public class SystemAttachmentDetailsBean extends BaseValueObject
{
    private static final long serialVersionUID = -4318472795140212244L;
    private Integer id;
    private String docName;
    private String docType;
    private String size;
    private String url;
    private Integer referenceId;
    private String referenceKey;
    private BaseEntityBean baseEntity;
    private byte[] document;
    
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
    
    public Integer getReferenceId() {
        return this.referenceId;
    }
    
    public void setReferenceId(final Integer referenceId) {
        this.referenceId = referenceId;
    }
    
    public BaseEntityBean getBaseEntity() {
        return this.baseEntity;
    }
    
    public void setBaseEntity(final BaseEntityBean baseEntity) {
        this.baseEntity = baseEntity;
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
