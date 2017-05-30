package com.kpisoft.user.dac.impl.entity;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import javax.persistence.*;

@Entity
@Table(name = "COR_MAS_OPERATIONS")
@Audited
public class Operation extends BaseTenantEntity
{
    private static final long serialVersionUID = -2014274061188455353L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "OPER_ID_SEQ")
    @SequenceGenerator(name = "OPER_ID_SEQ", sequenceName = "OPER_ID_SEQ")
    @Column(name = "CMO_PK_ID", length = 11)
    private Integer id;
    @Column(name = "NAME", length = 127)
    private String name;
    @Column(name = "CODE", length = 127)
    private String code;
    @Column(name = "STATUS", length = 1)
    private Integer status;
    @Column(name = "VERSION_NO", length = 11)
    private Integer version;
    @Column(name = "DISPLAY_ORDER_ID", length = 11)
    private Integer displayOrderId;
    @Column(name = "OPERATION_GROUP", length = 11)
    private String operationGroup;
    @Column(name = "BASE_URL", length = 127)
    private String baseUrl;
    @Lob
    @Column(name = "IMAGE")
    private byte[] image;
    @Column(name = "IMAGE_URL", length = 127)
    private String imageURL;
    
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
    
    public String getCode() {
        return this.code;
    }
    
    public void setCode(final String code) {
        this.code = code;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(final Integer version) {
        this.version = version;
    }
    
    public Integer getDisplayOrderId() {
        return this.displayOrderId;
    }
    
    public void setDisplayOrderId(final Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }
    
    public String getOperationGroup() {
        return this.operationGroup;
    }
    
    public void setOperationGroup(final String operationGroup) {
        this.operationGroup = operationGroup;
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
    
    public String getImageURL() {
        return this.imageURL;
    }
    
    public void setImageURL(final String imageURL) {
        this.imageURL = imageURL;
    }
}
