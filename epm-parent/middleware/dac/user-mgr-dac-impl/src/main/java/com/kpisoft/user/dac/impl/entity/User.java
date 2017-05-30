package com.kpisoft.user.dac.impl.entity;

import com.canopus.dac.*;
import org.jasypt.hibernate4.type.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.*;
import javax.persistence.*;
import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "USR_DET_USER")
@SQLDelete(sql = "UPDATE USR_DET_USER SET IS_DELETED = 1 WHERE UDU_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
@TypeDef(name = "encryptedString", typeClass = EncryptedStringType.class, parameters = { @Parameter(name = "encryptorRegisteredName", value = "hibernateStringEncryptor") })
public class User extends BaseTenantEntity
{
    private static final long serialVersionUID = 2444050005570023514L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "USER_ID_SEQ")
    @SequenceGenerator(name = "USER_ID_SEQ", sequenceName = "USER_ID_SEQ")
    @Column(name = "UDU_PK_ID", length = 11)
    private Integer id;
    @Index(name = "USERNAME_INDEX")
    @Column(name = "USERNAME", length = 255)
    private String userName;
    @Type(type = "encryptedString")
    @Column(name = "PASSWORD", length = 255)
    private String password;
    @Type(type = "encryptedString")
    @Column(name = "TX_PASSWORD", length = 255)
    private String txPassword;
    @Index(name = "EXT_USER_CODE_INDEX")
    @Column(name = "EXT_USER_CODE", length = 255)
    private String extCode;
    @Type(type = "encryptedString")
    @Column(name = "ENC_IDENTITY", length = 255)
    private String encIdentity;
    @Column(name = "UMU_FK_ID", length = 11)
    private Integer type;
    @Column(name = "START_DATE", length = 45)
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "END_DATE", length = 45)
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(name = "EDE_FK_ID", length = 11)
    private Integer employeeId;
    @Column(name = "VERSION_NO", length = 11)
    private Integer version;
    @Column(name = "IS_DELETED", length = 11)
    private Boolean deleted;
    @Column(name = "STATUS", length = 11)
    private Integer status;
    @Column(name = "USAGE_TYPE", length = 11)
    private Integer usageType;
    @Index(name = "USERCODE_INDEX")
    @Column(name = "USERCODE", length = 255, nullable = false)
    private String userCode;
    @Column(name = "FIRST_NAME", length = 255)
    private String firstName;
    @Column(name = "MIDDLE_NAME", length = 255)
    private String middleName;
    @Column(name = "LAST_NAME", length = 255)
    private String lastName;
    @Column(name = "SALUTATION", length = 255)
    private String salutation;
    @Column(name = "DISPLAYNAME", length = 255)
    private String displayName;
    @Column(name = "GENDER", length = 255)
    private String gender;
    @Lob
    @Column(name = "IMAGE")
    private byte[] image;
    @Column(name = "FILE_EXTENSION", length = 45)
    private String fileExtension;
    @Column(name = "HEIGHT", length = 11)
    private Integer imageHeight;
    @Column(name = "WIDTH", length = 11)
    private Integer imageWidth;
    @Index(name = "EMAIL_INDEX")
    @Column(name = "EMAIL", length = 255)
    private String email;
    @Column(name = "PHONE", length = 45)
    private String phone;
    @Column(name = "FAX", length = 45)
    private String fax;
    @Column(name = "PASSWORD_RESET_TOKEN", length = 255)
    private String passwordResetToken;
    @Column(name = "PASSWORD_RESET_TOKEN_EXPIRY", length = 45)
    @Temporal(TemporalType.DATE)
    private Date passwordResetTokenExpiryDate;
    @Column(name = "PASSWORD_RESET_DATE", length = 45)
    @Temporal(TemporalType.DATE)
    private Date passwordResetDate;
    @Column(name = "TXPASSWORD_RESET_TOKEN", length = 255)
    private String txPasswordResetToken;
    @Column(name = "TXPASSWORD_RESET_TOKEN_EXPIRY", length = 45)
    @Temporal(TemporalType.DATE)
    private Date txPasswordResetTokenExpiryDate;
    @Column(name = "TXPASSWORD_RESET_DATE", length = 45)
    @Temporal(TemporalType.DATE)
    private Date txPasswordResetDate;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
    @AuditMappedBy(mappedBy = "userId")
    @JoinColumn(name = "UDU_PK_ID")
    @Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
    private List<UserRoleRelationship> userRoles;
    
    public User() {
        this.deleted = false;
        this.userRoles = new ArrayList<UserRoleRelationship>();
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
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(final Integer version) {
        this.version = version;
    }
    
    public Boolean getDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final Boolean deleted) {
        this.deleted = deleted;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public Integer getUsageType() {
        return this.usageType;
    }
    
    public void setUsageType(final Integer usageType) {
        this.usageType = usageType;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
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
    
    public List<UserRoleRelationship> getUserRoles() {
        return Collections.unmodifiableList((List<? extends UserRoleRelationship>)this.userRoles);
    }
    
    public void setUserRoles(final List<UserRoleRelationship> userRoles) {
        this.userRoles.clear();
        if (userRoles != null && !userRoles.isEmpty()) {
            for (final UserRoleRelationship iterator : userRoles) {
                this.userRoles.add(iterator);
            }
        }
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
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
