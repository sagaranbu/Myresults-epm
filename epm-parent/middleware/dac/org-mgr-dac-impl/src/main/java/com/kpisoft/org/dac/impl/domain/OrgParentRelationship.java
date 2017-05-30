package com.kpisoft.org.dac.impl.domain;

import com.canopus.dac.*;
import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "ORG_REL_OU_OU_RELATION")
@SQLDelete(sql = "UPDATE ORG_REL_OU_OU_RELATION SET IS_DELETED = 1 WHERE ORO_PK_ID = ?")
@Where(clause = "IS_DELETED = 0 OR IS_DELETED IS NULL")
public class OrgParentRelationship extends BaseTenantEntity
{
    private static final long serialVersionUID = 1692198876260147651L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ORG_PARENT_ID_SEQ")
    @SequenceGenerator(name = "ORG_PARENT_ID_SEQ", sequenceName = "ORG_PARENT_ID_SEQ")
    @Column(name = "ORO_PK_ID", length = 11)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "SOURCE_ID", referencedColumnName = "ODO_PK_ID", nullable = false)
    private OrgIdentity sourceIdentity;
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "DESTINATION_ID", referencedColumnName = "ODO_PK_ID", nullable = false)
    private OrgIdentity destinationIdentity;
    @Column(name = "DIMENSION_FK_ID", length = 11, nullable = false)
    private Integer dimensionId;
    @Column(name = "IS_DELETED", nullable = false)
    private boolean deleted;
    @Column(name = "START_DATE", length = 45, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE", length = 45)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    
    public OrgParentRelationship() {
        this.deleted = false;
        this.startDate = new Date();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public OrgIdentity getSourceIdentity() {
        return this.sourceIdentity;
    }
    
    public void setSourceIdentity(final OrgIdentity sourceIdentity) {
        this.sourceIdentity = sourceIdentity;
    }
    
    public OrgIdentity getDestinationIdentity() {
        return this.destinationIdentity;
    }
    
    public void setDestinationIdentity(final OrgIdentity destinationIdentity) {
        this.destinationIdentity = destinationIdentity;
    }
    
    public Integer getDimensionId() {
        return this.dimensionId;
    }
    
    public void setDimensionId(final Integer dimensionId) {
        this.dimensionId = dimensionId;
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
