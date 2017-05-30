package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;
import com.canopus.entity.vo.*;

public class KpiAttachmentBean extends BaseValueObject
{
    private static final long serialVersionUID = -3958358880876620914L;
    private Integer id;
    private Integer kpiId;
    private SystemAttachmentBean attachment;
    private String context1;
    private String context2;
    private String context3;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getKpiId() {
        return this.kpiId;
    }
    
    public void setKpiId(final Integer kpiId) {
        this.kpiId = kpiId;
    }
    
    public SystemAttachmentBean getAttachment() {
        return this.attachment;
    }
    
    public void setAttachment(final SystemAttachmentBean attachment) {
        this.attachment = attachment;
    }
    
    public String getContext1() {
        return this.context1;
    }
    
    public void setContext1(final String context1) {
        this.context1 = context1;
    }
    
    public String getContext2() {
        return this.context2;
    }
    
    public void setContext2(final String context2) {
        this.context2 = context2;
    }
    
    public String getContext3() {
        return this.context3;
    }
    
    public void setContext3(final String context3) {
        this.context3 = context3;
    }
}
