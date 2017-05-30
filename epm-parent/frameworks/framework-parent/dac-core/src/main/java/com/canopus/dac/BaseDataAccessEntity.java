package com.canopus.dac;

import org.hibernate.envers.*;
import java.util.*;
import javax.persistence.*;

@Audited
@MappedSuperclass
public abstract class BaseDataAccessEntity implements DataAccessEntity
{
    @Column(name = "LAST_MODIFIED_BY", length = 45)
    private String lastModifiedBy;
    @Column(name = "LAST_MODIFIED_ON", length = 45)
    @Temporal(TemporalType.DATE)
    private Date lastModifiedOn;
    
    @Override
    public int hashCode() {
        if (this.getId() == null) {
            return super.hashCode();
        }
        return this.getId();
    }
    
    @Override
    public String toString() {
        return this.getClass().getName() + ":" + this.getId();
    }
    
    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass().equals(this.getClass())) {
            final BaseDataAccessEntity otherEntity = (BaseDataAccessEntity)other;
            return this.getId() == otherEntity.getId();
        }
        return true;
    }
    
    @Override
    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }
    
    @Override
    public void setLastModifiedBy(final String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
    
    @Override
    public Date getLastModifiedOn() {
        return this.lastModifiedOn;
    }
    
    @Override
    public void setLastModifiedOn(final Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }
}
