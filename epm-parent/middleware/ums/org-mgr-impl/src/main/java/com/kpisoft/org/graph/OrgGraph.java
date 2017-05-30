package com.kpisoft.org.graph;

import java.util.*;
import com.canopus.graph.*;
import com.tinkerpop.frames.*;
import com.tinkerpop.blueprints.*;

public class OrgGraph
{
    public interface Children extends EdgeFrame
    {
        @Domain
        Organization getParent();
        
        @Range
        Organization getChildren();
        
        @Property("dimensionId")
        Integer getDimensionId();
        
        @Property("dimensionId")
        void setDimensionId(final Integer p0);
        
        @Property("startDate")
        Date getStartDate();
        
        @Property("startDate")
        void setStartDate(final Date p0);
        
        @Property("endDate")
        Date getEndDate();
        
        @Property("endDate")
        void setEndDate(final Date p0);
    }
    
    public interface Organization extends NamedGraphObject
    {
        @Property("orgId")
        Integer getOrgId();
        
        @Property("orgId")
        void setOrgId(final Integer p0);
        
        @Property("orgType")
        Integer getOrgType();
        
        @Property("orgType")
        void setOrgType(final Integer p0);
        
        @Property("orgName")
        String getOrgName();
        
        @Property("orgName")
        void setOrgName(final String p0);
        
        @Property("orgCode")
        String getOrgCode();
        
        @Property("orgCode")
        void setOrgCode(final String p0);
        
        @Property("level")
        Integer getLevel();
        
        @Property("level")
        void setLevel(final Integer p0);
        
        @Property("orgStartDate")
        Date getOrgStartDate();
        
        @Property("orgStartDate")
        void setOrgStartDate(final Date p0);
        
        @Property("orgEndDate")
        Date getOrgEndDate();
        
        @Property("orgEndDate")
        void setOrgEndDate(final Date p0);
        
        @Property("status")
        Integer getStatus();
        
        @Property("status")
        void setStatus(final Integer p0);
        
        @Incidence(label = "parent")
        Iterable<Parent> getParents();
        
        @Incidence(label = "parent")
        Parent addParent(final Organization p0);
        
        @Incidence(label = "parent")
        void removeParent(final Parent p0);
        
        @Incidence(label = "parent", direction = Direction.IN)
        Iterable<Children> getChildren();
    }
    
    public interface Parent extends EdgeFrame
    {
        @Domain
        Organization getChildren();
        
        @Range
        Organization getParent();
        
        @Property("dimensionId")
        Integer getDimensionId();
        
        @Property("dimensionId")
        void setDimensionId(final Integer p0);
        
        @Property("startDate")
        Date getStartDate();
        
        @Property("startDate")
        void setStartDate(final Date p0);
        
        @Property("endDate")
        Date getEndDate();
        
        @Property("endDate")
        void setEndDate(final Date p0);
    }
}
