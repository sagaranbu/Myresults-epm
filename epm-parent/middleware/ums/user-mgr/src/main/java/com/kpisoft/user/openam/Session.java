package com.kpisoft.user.openam;

import com.canopus.mw.dto.*;
import org.joda.time.*;

public class Session extends BaseValueObject
{
    private String token;
    private DateTime createdAt;
    private DateTime lastAccessedAt;
    
    public Session(final String token) {
        this.token = "";
        if (token.startsWith("token.id")) {
            this.token = token.substring(9);
        }
        else {
            this.token = token;
        }
    }
    
    public String getToken() {
        return this.token;
    }
    
    public String toString() {
        return this.getToken();
    }
    
    public DateTime getCreatedAt() {
        return this.createdAt;
    }
    
    public void setCreatedAt(final DateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public DateTime getLastAccessedAt() {
        return this.lastAccessedAt;
    }
    
    public void setLastAccessedAt(final DateTime lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }
}
