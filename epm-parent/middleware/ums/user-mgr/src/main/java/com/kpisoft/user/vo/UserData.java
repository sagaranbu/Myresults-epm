package com.kpisoft.user.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;
import java.util.*;

public class UserData extends BaseValueObject
{
    private static final long serialVersionUID = 6971972930578454073L;
    private Integer id;
    @NotNull
    private String userName;
    @NotNull
    private String password;
    private String txPassword;
    private String extCode;
    private String encIdentity;
    @NotNull
    private Integer type;
    private Date startDate;
    private Date endDate;
    @NotNull
    private Integer tenantId;
    @NotNull
    private Integer employeeId;
    private Integer status;
    private Integer version;
    private List<UserGroupRelationshipData> userGroups;
    private List<UserRoleRelationshipData> userRoles;
    private Integer usageType;
    private String userCode;
    private String firstName;
    private String middleName;
    private String lastName;
    private String salutation;
    private String displayName;
    private String gender;
    private byte[] image;
    private String fileExtension;
    private Integer imageHeight;
    private Integer imageWidth;
    private String email;
    private String phone;
    private String fax;
    private String passwordResetToken;
    private Date passwordResetTokenExpiryDate;
    private Date passwordResetDate;
    private String txPasswordResetToken;
    private Date txPasswordResetTokenExpiryDate;
    private Date txPasswordResetDate;
    
    public UserData() {
        this.userGroups = new ArrayList<UserGroupRelationshipData>();
        this.userRoles = new ArrayList<UserRoleRelationshipData>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getUserName() {
        return this.userName;
    }
    
    public void setUserName(final String userName) {
        this.userName = userName;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public void setType(final Integer type) {
        this.type = type;
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
    
    public Integer getEmployeeId() {
        return this.employeeId;
    }
    
    public void setEmployeeId(final Integer employeeId) {
        this.employeeId = employeeId;
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
    
    public Integer getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final Integer tenantId) {
        this.tenantId = tenantId;
    }
    
    public void setVersion(final Integer version) {
        this.version = version;
    }
    
    public Integer getUsageType() {
        return this.usageType;
    }
    
    public void setUsageType(final Integer usageType) {
        this.usageType = usageType;
    }
    
    public List<UserGroupRelationshipData> getUserGroups() {
        return this.userGroups;
    }
    
    public void setUserGroups(final List<UserGroupRelationshipData> userGroups) {
        this.userGroups = userGroups;
    }
    
    public List<UserRoleRelationshipData> getUserRoles() {
        return this.userRoles;
    }
    
    public void setUserRoles(final List<UserRoleRelationshipData> userRoles) {
        this.userRoles = userRoles;
    }
    
    public String getUserCode() {
        return this.userCode;
    }
    
    public void setUserCode(final String userCode) {
        this.userCode = userCode;
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
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(final String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return this.phone;
    }
    
    public void setPhone(final String phone) {
        this.phone = phone;
    }
    
    public String getFax() {
        return this.fax;
    }
    
    public void setFax(final String fax) {
        this.fax = fax;
    }
    
    public String getTxPassword() {
        return this.txPassword;
    }
    
    public void setTxPassword(final String txPassword) {
        this.txPassword = txPassword;
    }
    
    public String getExtCode() {
        return this.extCode;
    }
    
    public void setExtCode(final String extCode) {
        this.extCode = extCode;
    }
    
    public String getEncIdentity() {
        return this.encIdentity;
    }
    
    public void setEncIdentity(final String encIdentity) {
        this.encIdentity = encIdentity;
    }
    
    public String getPasswordResetToken() {
        return this.passwordResetToken;
    }
    
    public void setPasswordResetToken(final String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }
    
    public Date getPasswordResetTokenExpiryDate() {
        return this.passwordResetTokenExpiryDate;
    }
    
    public void setPasswordResetTokenExpiryDate(final Date passwordResetTokenExpiryDate) {
        this.passwordResetTokenExpiryDate = passwordResetTokenExpiryDate;
    }
    
    public Date getPasswordResetDate() {
        return this.passwordResetDate;
    }
    
    public void setPasswordResetDate(final Date passwordResetDate) {
        this.passwordResetDate = passwordResetDate;
    }
    
    public String getTxPasswordResetToken() {
        return this.txPasswordResetToken;
    }
    
    public void setTxPasswordResetToken(final String txPasswordResetToken) {
        this.txPasswordResetToken = txPasswordResetToken;
    }
    
    public Date getTxPasswordResetTokenExpiryDate() {
        return this.txPasswordResetTokenExpiryDate;
    }
    
    public void setTxPasswordResetTokenExpiryDate(final Date txPasswordResetTokenExpiryDate) {
        this.txPasswordResetTokenExpiryDate = txPasswordResetTokenExpiryDate;
    }
    
    public Date getTxPasswordResetDate() {
        return this.txPasswordResetDate;
    }
    
    public void setTxPasswordResetDate(final Date txPasswordResetDate) {
        this.txPasswordResetDate = txPasswordResetDate;
    }
}
