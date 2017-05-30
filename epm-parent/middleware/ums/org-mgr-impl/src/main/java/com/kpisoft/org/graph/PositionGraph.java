package com.kpisoft.org.graph;

import java.util.*;
import com.canopus.graph.*;
import com.tinkerpop.frames.*;
import com.tinkerpop.blueprints.*;

@Deprecated
public class PositionGraph
{
    public interface Children extends EdgeFrame
    {
        @Domain
        Position getParent();
        
        @Range
        Position getChildren();
        
        @Property("startDate")
        Date getStartDate();
        
        @Property("startDate")
        void setStartDate(final Date p0);
        
        @Property("endDate")
        Date getEndDate();
        
        @Property("endDate")
        void setEndDate(final Date p0);
    }
    
    public interface Position extends NamedGraphObject
    {
        @Property("positionId")
        Integer getPositionId();
        
        @Property("positionId")
        void setPositionId(final Integer p0);
        
        @Property("positionName")
        String getPositionName();
        
        @Property("positionName")
        void setPositionName(final String p0);
        
        @Property("positionCode")
        String getPositionCode();
        
        @Property("positionCode")
        void setPositionCode(final String p0);
        
        @Incidence(label = "parent")
        Iterable<Parent> getParents();
        
        @Incidence(label = "parent")
        Parent addParent(final Position p0);
        
        @Incidence(label = "parent")
        void removeParent(final Parent p0);
        
        @Incidence(label = "parent", direction = Direction.IN)
        Iterable<Children> getChildren();
    }
    
    public interface Parent extends EdgeFrame
    {
        @Domain
        Position getChildren();
        
        @Range
        Position getParent();
        
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
