package com.kpisoft.user.dac.impl.entity;

import com.canopus.dac.*;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "USR_DET_SESSION_INFO")
public class UserSessionInfo extends BaseTenantEntity
{
    private static final long serialVersionUID = 3347311718606782557L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UDS_ID_SEQ")
    @SequenceGenerator(name = "UDS_ID_SEQ", sequenceName = "UDS_ID_SEQ")
    @Column(name = "UDS_PK_ID", length = 11)
    private Integer id;
    @Column(name = "OPENAM_TOKEN", length = 125)
    private String token;
    @Column(name = "LOGIN_TIME", length = 45)
    @Temporal(TemporalType.TIMESTAMP)
    private Date loginTime;
    @Column(name = "LOGOUT_TIME", length = 45)
    @Temporal(TemporalType.TIMESTAMP)
    private Date logoutTime;
    @Column(name = "UDU_FK_ID", length = 11)
    private Integer userId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public void setToken(final String token) {
        this.token = token;
    }
    
    public Date getLoginTime() {
        return this.loginTime;
    }
    
    public void setLoginTime(final Date loginTime) {
        this.loginTime = loginTime;
    }
    
    public Date getLogoutTime() {
        return this.logoutTime;
    }
    
    public void setLogoutTime(final Date logoutTime) {
        this.logoutTime = logoutTime;
    }
    
    public Integer getUserId() {
        return this.userId;
    }
    
    public void setUserId(final Integer userId) {
        this.userId = userId;
    }
}
