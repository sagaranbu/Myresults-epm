package com.canopus.saas.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class TenantBaseData extends BaseValueObject
{
    private static final long serialVersionUID = 3497247349624570543L;
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String orgName;
    private String noOfEmps;
    private String masLang;
    private byte[] tenantImage;
    private String fileExtension;
    private Boolean isDeleted;
    private List<TenantContentData> tenantContentData;
    
    public TenantBaseData() {
        this.tenantContentData = new ArrayList<TenantContentData>();
    }
    
    public String getFileExtension() {
        return this.fileExtension;
    }
    
    public void setFileExtension(final String fileExtension) {
        this.fileExtension = fileExtension;
    }
    
    public String getMasLang() {
        return this.masLang;
    }
    
    public void setMasLang(final String masLang) {
        this.masLang = masLang;
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
    
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(final String email) {
        this.email = email;
    }
    
    public String getOrgName() {
        return this.orgName;
    }
    
    public void setOrgName(final String orgName) {
        this.orgName = orgName;
    }
    
    public String getNoOfEmps() {
        return this.noOfEmps;
    }
    
    public void setNoOfEmps(final String noOfEmps) {
        this.noOfEmps = noOfEmps;
    }
    
    public List<TenantContentData> getTenantContentData() {
        return this.tenantContentData;
    }
    
    public void setTenantContentData(final List<TenantContentData> tenantContentData) {
        this.tenantContentData = tenantContentData;
    }
    
    public byte[] getTenantImage() {
        return this.tenantImage;
    }
    
    public void setTenantImage(final byte[] tenantImage) {
        this.tenantImage = tenantImage;
    }
    
    public Boolean getIsDeleted() {
        return this.isDeleted;
    }
    
    public void setIsDeleted(final Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public String toString() {
        return "TenantBaseData [id=" + this.id + ", firstName=" + this.firstName + ", lastName=" + this.lastName + ", email=" + this.email + ", name=" + this.orgName + ", noOfEmps=" + this.noOfEmps + "]";
    }
}
