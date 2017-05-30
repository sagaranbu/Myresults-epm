package com.kpisoft.emp.vo;

import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import java.util.*;

public class EmployeeData extends EmployeeIdentity
{
    private static final long serialVersionUID = -6916055866928900427L;
    @NotNull
    private String firstName;
    private String middleName;
    private String lastName;
    private String displayName;
    @NotNull
    @Email
    private String email;
    private String salutation;
    private String gender;
    private String userName;
    private Date joiningDate;
    private byte[] image;
    private String fileExtension;
    private Integer imageHeight;
    private Integer imageWidth;
    private Integer grade;
    private Integer empType;
    private Integer status;
    private Integer usageType;
    @NotNull
    private Date startDate;
    private Date endDate;
    private String supervisorIds;
    private String positionIds;
    private String orgIds;
    private List<EmployeeFieldData> fieldData;
    private List<EmployeePositionData> posData;
    private List<EmployeeOrgRelationshipData> empOrgRelData;
    private List<EmployeeSupervisorRelationshipData> empSupData;
    
    public EmployeeData() {
        this.status = 1;
        this.startDate = new Date();
        this.fieldData = new ArrayList<EmployeeFieldData>();
        this.posData = new ArrayList<EmployeePositionData>();
        this.empOrgRelData = new ArrayList<EmployeeOrgRelationshipData>();
        this.empSupData = new ArrayList<EmployeeSupervisorRelationshipData>();
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
    
    public String getGender() {
        return this.gender;
    }
    
    public void setGender(final String gender) {
        this.gender = gender;
    }
    
    public String getUserName() {
        return this.userName;
    }
    
    public void setUserName(final String userName) {
        this.userName = userName;
    }
    
    public Date getJoiningDate() {
        return this.joiningDate;
    }
    
    public void setJoiningDate(final Date joiningDate) {
        this.joiningDate = joiningDate;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
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
    
    public Integer getGrade() {
        return this.grade;
    }
    
    public void setGrade(final Integer grade) {
        this.grade = grade;
    }
    
    public Integer getEmpType() {
        return this.empType;
    }
    
    public void setEmpType(final Integer empType) {
        this.empType = empType;
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
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(final String email) {
        this.email = email;
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
    
    public List<EmployeeFieldData> getFieldData() {
        return this.fieldData;
    }
    
    public void setFieldData(final List<EmployeeFieldData> fieldData) {
        this.fieldData = fieldData;
    }
    
    public List<EmployeePositionData> getPosData() {
        return this.posData;
    }
    
    public void setPosData(final List<EmployeePositionData> posData) {
        this.posData = posData;
    }
    
    public List<EmployeeOrgRelationshipData> getEmpOrgRelData() {
        return this.empOrgRelData;
    }
    
    public void setEmpOrgRelData(final List<EmployeeOrgRelationshipData> empOrgRelData) {
        this.empOrgRelData = empOrgRelData;
    }
    
    public List<EmployeeSupervisorRelationshipData> getEmpSupData() {
        return this.empSupData;
    }
    
    public void setEmpSupData(final List<EmployeeSupervisorRelationshipData> empSupData) {
        this.empSupData = empSupData;
    }
}
