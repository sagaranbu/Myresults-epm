package com.kpisoft.emp.dac.impl;

import com.canopus.dac.*;
import java.util.*;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Immutable
@Table(name = "EMP_DET_EMPLOYEE", uniqueConstraints = { @UniqueConstraint(columnNames = { "EDE_AT_EMPCODE", "TENANT_ID" }) })
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class EmployeeBase extends BaseTenantEntity
{
    private static final long serialVersionUID = 4459317724806444257L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "EMP_ID_SEQ")
    @SequenceGenerator(name = "EMP_ID_SEQ", sequenceName = "EMP_ID_SEQ")
    @Column(name = "EDE_PK_EMPLOYEE_ID", length = 11)
    private Integer id;
    @Column(name = "EDE_AT_FIRST_NAME", length = 255)
    private String firstName;
    @Column(name = "EDE_AT_MIDDLE_NAME", length = 255)
    private String middleName;
    @Column(name = "EDE_AT_LAST_NAME", length = 255)
    private String lastName;
    @Column(name = "EDE_AT_SALUTATION", length = 255)
    private String salutation;
    @Column(name = "EDE_AT_JOIN_DATE", length = 45)
    @Temporal(TemporalType.TIMESTAMP)
    private Date joiningDate;
    @Index(name = "EDE_AT_EMPCODE_INDEX")
    @Column(name = "EDE_AT_EMPCODE", length = 255, nullable = false)
    private String empCode;
    @Index(name = "EDE_AT_USERNAME_INDEX")
    @Column(name = "EDE_AT_USERNAME", length = 255)
    private String userName;
    @Column(name = "EDE_AT_DISPLAYNAME", length = 255, nullable = false)
    private String displayName;
    @Column(name = "EDE_AT_GENDER", length = 255)
    private String gender;
    @Lob
    @Column(name = "IMAGE")
    private byte[] image;
    @Index(name = "EMP_EMAIL_INDEX")
    @Column(name = "EMAIL", length = 255)
    private String email;
    @Column(name = "FILE_EXTENSION", length = 45)
    private String fileExtension;
    @Column(name = "HEIGHT", length = 11)
    private Integer imageHeight;
    @Column(name = "EMP_TYPE", length = 11)
    private Integer empType;
    @Column(name = "WIDTH", length = 11)
    private Integer imageWidth;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    @Column(name = "GRADE_ID", length = 11)
    private Integer grade;
    @Column(name = "STATUS", length = 11)
    private Integer status;
    @Column(name = "USAGE_TYPE", length = 11)
    private Integer usageType;
    @Column(name = "START_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "SUPERVISOR_IDS", length = 1000)
    private String supervisorIds;
    @Column(name = "POSITION_IDS", length = 1000)
    private String positionIds;
    @Column(name = "ORG_IDS", length = 1000)
    private String orgIds;
    
    public EmployeeBase() {
        this.status = 1;
        this.startDate = new Date();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
    
    public String getMiddleName() {
        return this.middleName;
    }
    
    public void setMiddleName(final String middleName) {
        this.middleName = middleName;
    }
    
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
    
    public String getSalutation() {
        return this.salutation;
    }
    
    public void setSalutation(final String salutation) {
        this.salutation = salutation;
    }
    
    public Date getJoiningDate() {
        return this.joiningDate;
    }
    
    public void setJoiningDate(final Date joiningDate) {
        this.joiningDate = joiningDate;
    }
    
    public String getEmpCode() {
        return this.empCode;
    }
    
    public void setEmpCode(final String empCode) {
        this.empCode = empCode;
    }
    
    public String getUserName() {
        return this.userName;
    }
    
    public void setUserName(final String userName) {
        this.userName = userName;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    public String getGender() {
        return this.gender;
    }
    
    public void setGender(final String gender) {
        this.gender = gender;
    }
    
    public byte[] getImage() {
        return this.image;
    }
    
    public void setImage(final byte[] image) {
        this.image = image;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(final String email) {
        this.email = email;
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
    
    public Integer getEmpType() {
        return this.empType;
    }
    
    public void setEmpType(final Integer empType) {
        this.empType = empType;
    }
    
    public Integer getImageWidth() {
        return this.imageWidth;
    }
    
    public void setImageWidth(final Integer imageWidth) {
        this.imageWidth = imageWidth;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
    
    public Integer getGrade() {
        return this.grade;
    }
    
    public void setGrade(final Integer grade) {
        this.grade = grade;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
    }
    
    public Integer getUsageType() {
        return this.usageType;
    }
    
    public void setUsageType(final Integer usageType) {
        this.usageType = usageType;
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
    
    public String getSupervisorIds() {
        return this.supervisorIds;
    }
    
    public void setSupervisorIds(final String supervisorIds) {
        this.supervisorIds = supervisorIds;
    }
    
    public String getPositionIds() {
        return this.positionIds;
    }
    
    public void setPositionIds(final String positionIds) {
        this.positionIds = positionIds;
    }
    
    public String getOrgIds() {
        return this.orgIds;
    }
    
    public void setOrgIds(final String orgIds) {
        this.orgIds = orgIds;
    }
}
