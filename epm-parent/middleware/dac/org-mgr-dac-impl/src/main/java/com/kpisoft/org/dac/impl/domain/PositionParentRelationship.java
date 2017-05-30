package com.kpisoft.org.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Deprecated
@Audited
@Entity
@Table(name = "ORG_REL_POS_POS_RELATION")
@SQLDelete(sql = "UPDATE ORG_REL_POS_POS_RELATION SET IS_DELETED = 1 WHERE ORP_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class PositionParentRelationship extends BaseTenantEntity
{
    private static final long serialVersionUID = 7950459121230021546L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ORG_POS_PARENT_ID_SEQ")
    @SequenceGenerator(name = "ORG_POS_PARENT_ID_SEQ", sequenceName = "ORG_POS_PARENT_ID_SEQ")
    @Column(name = "ORP_PK_ID", length = 11)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "SOURCE_ID", nullable = false, referencedColumnName = "OMP_PK_ID")
    private PositionIdentity sourceId;
    @ManyToOne
    @JoinColumn(name = "DESTINATION_ID", nullable = false, referencedColumnName = "OMP_PK_ID")
    private PositionIdentity destinationId;
    @Column(name = "IS_DELETED")
    private boolean deleted;
    @Column(name = "START_DATE", length = 45, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE", length = 45)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    
    public PositionParentRelationship() {
        this.startDate = new Date();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public PositionIdentity getSourceId() {
        return this.sourceId;
    }
    
    public void setSourceId(final PositionIdentity sourceId) {
        this.sourceId = sourceId;
    }
    
    public PositionIdentity getDestinationId() {
        return this.destinationId;
    }
    
    public void setDestinationId(final PositionIdentity destinationId) {
        this.destinationId = destinationId;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
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
}
