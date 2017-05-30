package com.canopus.saas.dac.entity;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SAS_DET_TENANT")
@SQLDelete(sql = "UPDATE SAS_DET_TENANT SET IS_DELETED = 1 WHERE SMT_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class TenantBase extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 7575035901004219991L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "TENANT_BASE_ID_SEQ")
    @SequenceGenerator(name = "TENANT_BASE_ID_SEQ", sequenceName = "TENANT_BASE_ID_SEQ")
    @Column(name = "SMT_PK_ID", length = 11)
    private Integer id;
    @Column(name = "ORG_NAME", length = 127)
    private String orgName;
    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "NUM_OF_EMPLOYEE")
    private String noOfEmps;
    @Column(name = "FILE_EXTENSION")
    private String fileExtension;
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;
    @Lob
    @Column(name = "TENANT_IMAGE")
    private byte[] tenantImage;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinColumn(name = "SMT_PK_ID")
    private List<TenantContent> tenantContentData;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getOrgName() {
        return this.orgName;
    }
    
    public void setOrgName(final String orgName) {
        this.orgName = orgName;
    }
    
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }
    
    public String getNoOfEmps() {
        return this.noOfEmps;
    }
    
    public void setNoOfEmps(final String noOfEmps) {
        this.noOfEmps = noOfEmps;
    }
    
    public List<TenantContent> getTenantContentData() {
        return this.tenantContentData;
    }
    
    public void setTenantContentData(final List<TenantContent> tenantContentData) {
        this.tenantContentData = tenantContentData;
    }
    
    public byte[] getTenantImage() {
        return this.tenantImage;
    }
    
    public void setTenantImage(final byte[] tenantImage) {
        this.tenantImage = tenantImage;
    }
    
    public String getFileExtension() {
        return this.fileExtension;
    }
    
    public void setFileExtension(final String fileExtension) {
        this.fileExtension = fileExtension;
    }
    
    public Boolean getIsDeleted() {
        return this.isDeleted;
    }
    
    public void setIsDeleted(final Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public String toString() {
        return "TenantBase [id=" + this.id + ", orgName=" + this.orgName + ", startDate=" + this.startDate + ", endDate=" + this.endDate + ", noOfEmps=" + this.noOfEmps + "]";
    }
}
