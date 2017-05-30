package com.kpisoft.org.vo;

import java.util.*;

public class OrganizationUnitData extends OrgIdentityBean
{
    private static final long serialVersionUID = 2365164103811261266L;
    private Integer orgType;
    private Integer parentId;
    private Integer houEmployeeId;
    private String description;
    private String mission;
    private String vision;
    private String sequenceNum;
    private byte[] image;
    private String fileExtension;
    private Integer imageHeight;
    private Integer imageWidth;
    private Integer status;
    private List<OrganizationFiledData> filedData;
    private List<OrganizationDimensionStructureData> orgTypeMappings;
    
    public OrganizationUnitData() {
        this.status = 1;
        this.filedData = new ArrayList<OrganizationFiledData>();
        this.orgTypeMappings = new ArrayList<OrganizationDimensionStructureData>();
    }
    
    @Override
    public Integer getOrgType() {
        return this.orgType;
    }
    
    @Override
    public void setOrgType(final Integer orgType) {
        this.orgType = orgType;
    }
    
    public Integer getParentId() {
        return this.parentId;
    }
    
    public void setParentId(final Integer parentId) {
        this.parentId = parentId;
    }
    
    public Integer getHouEmployeeId() {
        return this.houEmployeeId;
    }
    
    public void setHouEmployeeId(final Integer houEmployeeId) {
        this.houEmployeeId = houEmployeeId;
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
    
    public void setVision(final String vision) {
        this.vision = vision;
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
    
    public List<OrganizationFiledData> getFiledData() {
        return this.filedData;
    }
    
    public void setFiledData(final List<OrganizationFiledData> filedData) {
        this.filedData = filedData;
    }
    
    public List<OrganizationDimensionStructureData> getOrgTypeMappings() {
        return this.orgTypeMappings;
    }
    
    public void setOrgTypeMappings(final List<OrganizationDimensionStructureData> orgTypeMappings) {
        this.orgTypeMappings = orgTypeMappings;
    }
    
    @Override
    public Integer getStatus() {
        return this.status;
    }
    
    @Override
    public void setStatus(final Integer status) {
        this.status = status;
    }
    
    public String getSequenceNum() {
        return this.sequenceNum;
    }
    
    public void setSequenceNum(final String sequenceNum) {
        this.sequenceNum = sequenceNum;
    }
}
