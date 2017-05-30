package com.canopus.entity.domain;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "COR_MET_NOTIFICATION_RECPNT")
public class NotificationRecipient extends BaseTenantEntity
{
    private static final long serialVersionUID = -6181150301643000658L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "NTF_RECPNT_ID_SEQ")
    @SequenceGenerator(name = "NTF_RECPNT_ID_SEQ", sequenceName = "NTF_RECPNT_ID_SEQ")
    @Column(name = "NTF_RECPNT_PK_ID", length = 11)
    private Integer id;
    @Column(name = "RECIPIENT_TYPE", length = 20)
    private String recipientType;
    @Column(name = "RECIPIENT", length = 100)
    private String recipient;
    @Column(name = "NOTIFICATION_FK_ID")
    private Integer notificationId;
    @Column(name = "IS_EXCLUSION")
    private boolean isExclusion;
    @Column(name = "IS_DIGEST")
    private boolean isDigest;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getRecipientType() {
        return this.recipientType;
    }
    
    public void setRecipientType(final String recipientType) {
        this.recipientType = recipientType;
    }
    
    public String getRecipient() {
        return this.recipient;
    }
    
    public void setRecipient(final String recipient) {
        this.recipient = recipient;
    }
    
    public Integer getNotificationId() {
        return this.notificationId;
    }
    
    public void setNotificationId(final Integer notificationId) {
        this.notificationId = notificationId;
    }
    
    public boolean getIsExclusion() {
        return this.isExclusion;
    }
    
    public void setIsExclusion(final boolean isExclusion) {
        this.isExclusion = isExclusion;
    }
    
    public boolean getIsDigest() {
        return this.isDigest;
    }
    
    public void setIsDigest(final boolean isDigest) {
        this.isDigest = isDigest;
    }
}
