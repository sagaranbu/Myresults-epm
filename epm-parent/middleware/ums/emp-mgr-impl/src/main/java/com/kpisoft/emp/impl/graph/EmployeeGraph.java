package com.kpisoft.emp.impl.graph;

import java.util.*;
import com.canopus.graph.*;
import com.tinkerpop.frames.*;
import com.tinkerpop.blueprints.*;

public class EmployeeGraph
{
    public interface AssignedOrganization extends EdgeFrame
    {
        @Domain
        Organization getOrganization();
        
        @Range
        Employee getEmployee();
        
        @Property("startdate")
        Date getStartdate();
        
        @Property("startdate")
        void setStartdate(final Date p0);
        
        @Property("enddate")
        Date getEnddate();
        
        @Property("enddate")
        void setEnddate(final Date p0);
    }
    
    public interface Organization extends NamedGraphObject
    {
        @Incidence(label = "assignedto")
        OrganizationAssignment addEmployee(final Organization p0);
        
        @Incidence(label = "assignedto")
        Iterable<OrganizationAssignment> getEmployee();
        
        @Incidence(label = "assignedto")
        void removeOrganizationAssignment(final OrganizationAssignment p0);
        
        @Incidence(label = "assignedto", direction = Direction.IN)
        Iterable<AssignedOrganization> getAssignedOrganization();
        
        @Property("organizationId")
        Integer getOrganizationId();
        
        @Property("organizationId")
        void setOrganizationId(final Integer p0);
        
        @Property("hod")
        boolean isHod();
        
        @Property("hod")
        void setHod(final boolean p0);
    }
    
    public interface OrganizationAssignment extends EdgeFrame
    {
        @Domain
        Employee getEmployee();
        
        @Range
        Organization getOrganization();
        
        @Property("startdate")
        Date getStartdate();
        
        @Property("startdate")
        void setStartdate(final Date p0);
        
        @Property("enddate")
        Date getEnddate();
        
        @Property("enddate")
        void setEnddate(final Date p0);
    }
    
    public interface Employee extends NamedGraphObject
    {
        @Property("eid")
        Integer getEmployeeId();
        
        @Property("eid")
        void setEmployeeId(final Integer p0);
        
        @Incidence(label = "sup")
        Iterable<Supervisor> getSupervisors();
        
        @Incidence(label = "sup")
        Supervisor addSupervisor(final Employee p0);
        
        @Incidence(label = "sup")
        void removeSupervisor(final Supervisor p0);
        
        @Incidence(label = "sup", direction = Direction.IN)
        Iterable<SupervisedBy> getSubordinates();
        
        @Incidence(label = "pos")
        PositionAssignment addPosition(final Position p0);
        
        @Incidence(label = "pos")
        Iterable<PositionAssignment> getPositions();
        
        @Incidence(label = "pos")
        void removePositionAssignment(final PositionAssignment p0);
        
        @Incidence(label = "pos", direction = Direction.IN)
        Iterable<PositionOccupied> getPositionOccupied();
        
        @Incidence(label = "assignedto")
        OrganizationAssignment addOrganization(final Organization p0);
        
        @Incidence(label = "assignedto")
        Iterable<OrganizationAssignment> getOrganization();
        
        @Incidence(label = "assignedto")
        void removeOrganizationAssignment(final OrganizationAssignment p0);
    }
    
    public interface Supervisor extends EdgeFrame
    {
        @Domain
        Employee getSubordinate();
        
        @Range
        Employee getSupervisor();
        
        @Property("primary")
        boolean isPrimary();
        
        @Property("primary")
        void setPrimary(final boolean p0);
    }
    
    public interface SupervisedBy extends EdgeFrame
    {
        @Domain
        Employee getSupervisor();
        
        @Range
        Employee getSubordinate();
        
        @Property("primary")
        boolean isPrimary();
        
        @Property("primary")
        void setPrimary(final boolean p0);
    }
    
    public interface Position extends NamedGraphObject
    {
        @Incidence(label = "pos")
        PositionAssignment addEmployee(final Position p0);
        
        @Incidence(label = "pos")
        Iterable<PositionAssignment> getEmployee();
        
        @Incidence(label = "pos")
        void removePositionAssignment(final PositionAssignment p0);
        
        @Property("positionId")
        Integer getPositionId();
        
        @Property("positionId")
        void setPositionId(final Integer p0);
    }
    
    public interface PositionAssignment extends EdgeFrame
    {
        @Domain
        Employee getEmployee();
        
        @Range
        Position getPosition();
        
        @Property("startdate")
        Date getStartdate();
        
        @Property("startdate")
        void setStartdate(final Date p0);
        
        @Property("enddate")
        Date getEnddate();
        
        @Property("enddate")
        void setEnddate(final Date p0);
    }
    
    public interface PositionOccupied extends EdgeFrame
    {
        @Domain
        Position getPosition();
        
        @Range
        Employee getEmployee();
        
        @Property("startdate")
        Date getStartdate();
        
        @Property("startdate")
        void setStartdate(final Date p0);
        
        @Property("enddate")
        Date getEnddate();
        
        @Property("enddate")
        void setEnddate(final Date p0);
    }
}
