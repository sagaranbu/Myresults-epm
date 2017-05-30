package com.canopus.entity.domain;

import com.canopus.dac.*;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "COR_MET_DSHBRDMSGUSR")
public class DashboardMessageUser extends BaseTenantEntity
{
    private static final long serialVersionUID = -7468154891598852131L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "DSHBRDMSGUSR_ID_SEQ")
    @SequenceGenerator(name = "DSHBRDMSGUSR_ID_SEQ", sequenceName = "DSHBRDMSGUSR_ID_SEQ")
    @Column(name = "DSHBRDMSGUSR_PK_ID", length = 11)
    private Integer id;
    @Column(name = "DASHBOARDMSG_FK_ID")
    private Integer dashboardMessageId;
    @Column(name = "RECIP_USER_FK_ID")
    private Integer recipientUserId;
    @Column(name = "IS_DISABLED")
    private boolean isDisabled;
    @Column(name = "DISABLED_BY")
    private String disabledBy;
    @Column(name = "DISABLED_ON")
    private Date disabledOn;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getDashboardMessageId() {
        return this.dashboardMessageId;
    }
    
    public void setDashboardMessageId(final Integer dashboardMessageId) {
        this.dashboardMessageId = dashboardMessageId;
    }
    
    public Integer getRecipientUserId() {
        return this.recipientUserId;
    }
    
    public void setRecipientUserId(final Integer recipientUserId) {
        this.recipientUserId = recipientUserId;
    }
    
    public boolean getIsDisabled() {
        return this.isDisabled;
    }
    
    public void setIsDisabled(final boolean isDisabled) {
        this.isDisabled = isDisabled;
    }
    
    public String getDisabledBy() {
        return this.disabledBy;
    }
    
    public void setDisabledBy(final String disabledBy) {
        this.disabledBy = disabledBy;
    }
    
    public Date getDisabledOn() {
        return this.disabledOn;
    }
    
    public void setDisabledOn(final Date disabledOn) {
        this.disabledOn = disabledOn;
    }
}
