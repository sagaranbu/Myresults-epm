package com.kpisoft.org.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.*;
import java.util.*;

@Entity
@Immutable
@Table(name = "ORG_DET_ORG_UNIT", uniqueConstraints = { @UniqueConstraint(columnNames = { "ORGUNIT_CODE", "TENANT_ID" }) })
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class OrganizationUnitBase extends BaseTenantEntity
{
    private static final long serialVersionUID = 6092513642714152578L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ORG_ID_SEQ")
    @SequenceGenerator(name = "ORG_ID_SEQ", sequenceName = "ORG_ID_SEQ")
    @Column(name = "ODO_PK_ID", length = 11)
    private Integer id;
    @Column(name = "ODO_AT_ORG_TYPE", length = 11)
    private Integer orgType;
    @Column(name = "ODO_AT_LEVEL", length = 11)
    private Integer level;
    @Column(name = "ODO_FK_PARENT_ID", length = 11)
    private Integer parentId;
    @Column(name = "ODO_AT_NAME", length = 512, nullable = false)
    private String orgName;
    @Column(name = "SEQUENCE_NUM", length = 1024)
    private String sequenceNum;
    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "ODO_AT_DESCRIPTION", length = 512)
    private String description;
    @Column(name = "ODO_AT_MISSION", length = 512)
    private String mission;
    @Column(name = "ODO_AT_VISION", length = 512)
    private String vision;
    @Lob
    @Column(name = "IMAGE")
    private byte[] image;
    @Column(name = "FILE_EXTENSION", length = 45)
    private String fileExtension;
    @Column(name = "HEIGHT", length = 11)
    private Integer imageHeight;
    @Column(name = "WIDTH", length = 11)
    private Integer imageWidth;
    @Column(name = "EDE_HOD_FK_ID", length = 11)
    private Integer houEmployeeId;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    @Column(name = "ORGUNIT_CODE", length = 45, nullable = false)
    private String orgUnitCode;
    @Column(name = "START_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "STATUS", length = 11)
    private Integer status;
    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, orphanRemoval = true, mappedBy = "orgUnitId")
    @AuditMappedBy(mappedBy = "orgUnitId")
    @Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
    private List<OrganizationFieldData> filedData;
    
    public OrganizationUnitBase() {
        this.startDate = new Date();
        this.status = 1;
        this.filedData = new ArrayList<OrganizationFieldData>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getOrgType() {
        return this.orgType;
    }
    
    public void setOrgType(final Integer orgType) {
        this.orgType = orgType;
    }
    
    public Integer getLevel() {
        return this.level;
    }
    
    public void setLevel(final Integer level) {
        this.level = level;
    }
    
    public Integer getParentId() {
        return this.parentId;
    }
    
    public void setParentId(final Integer parentId) {
        this.parentId = parentId;
    }
    
    public String getOrgName() {
        return this.orgName;
    }
    
    public void setOrgName(final String orgName) {
        this.orgName = orgName;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public String getMission() {
        return this.mission;
    }
    
    public void setMission(final String mission) {
        this.mission = mission;
    }
    
    public String getVision() {
        return this.vision;
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
    
    public Integer getImageHeight() {
        return this.imageHeight;
    }
    
    public void setImageHeight(final Integer imageHeight) {
        this.imageHeight = imageHeight;
    }
    
    public Integer getImageWidth() {
        return this.imageWidth;
    }
    
    public void setImageWidth(final Integer imageWidth) {
        this.imageWidth = imageWidth;
    }
    
    public void setVision(final String vision) {
        this.vision = vision;
    }
    
    public Integer getHouEmployeeId() {
        return this.houEmployeeId;
    }
    
    public void setHouEmployeeId(final Integer houEmployeeId) {
        this.houEmployeeId = houEmployeeId;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
    
    public String getOrgUnitCode() {
        return this.orgUnitCode;
    }
    
    public void setOrgUnitCode(final String orgUnitCode) {
        this.orgUnitCode = orgUnitCode;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
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
    
    public List<OrganizationFieldData> getFiledData() {
        return this.filedData;
    }
    
    public void setFiledData(final List<OrganizationFieldData> filedData) {
        this.filedData = filedData;
    }
    
    public Date getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(final Date createDate) {
        this.createDate = createDate;
    }
    
    public String getSequenceNum() {
        return this.sequenceNum;
    }
    
    public void setSequenceNum(final String sequenceNum) {
        this.sequenceNum = sequenceNum;
    }
}
