package com.kpisoft.emp.impl.graph;

import com.canopus.graph.*;
import com.tinkerpop.frames.*;
import com.tinkerpop.blueprints.*;
import java.util.*;

public class PositionRelationship extends BaseRelationship
{
    public PositionRelationship(final FramedGraph<Graph> framedGraph) {
        super((FramedGraph)framedGraph);
    }
    
    public void addEmployeePosition(final Integer empID, final Integer positionID) {
        if (this.isEmpAvailable(empID) && this.isPosAvailable(positionID)) {
            final EmployeeGraph.Employee employee = (EmployeeGraph.Employee)this.get(empID, (Class)EmployeeGraph.Employee.class);
            final EmployeeGraph.Position position = (EmployeeGraph.Position)this.get(positionID, (Class)EmployeeGraph.Position.class);
            employee.addPosition(position);
        }
    }
    
    public List<Integer> getEmployeePositions(final Integer empID) {
        final List<Integer> result = new ArrayList<Integer>();
        if (this.isEmpAvailable(empID)) {
            final EmployeeGraph.Employee employee = (EmployeeGraph.Employee)this.get(empID, (Class)EmployeeGraph.Employee.class);
            for (final EmployeeGraph.PositionAssignment iterator : employee.getPositions()) {
                result.add(iterator.getPosition().getPositionId());
            }
        }
        return result;
    }
    
    public void removeEmployeePosition(final Integer empID, final Integer positionID) {
        if (this.isEmpAvailable(empID)) {
            final EmployeeGraph.Employee employee = (EmployeeGraph.Employee)this.get(empID, (Class)EmployeeGraph.Employee.class);
            for (final EmployeeGraph.PositionAssignment iterator : employee.getPositions()) {
                if (iterator.getPosition().getPositionId().equals(positionID)) {
                    employee.removePositionAssignment(iterator);
                }
            }
        }
    }
    
    public boolean isEmpAvailable(final Integer id) {
        try {
            final EmployeeGraph.Employee employee = (EmployeeGraph.Employee)this.get(id, (Class)EmployeeGraph.Employee.class);
            return id.equals(employee.getEmployeeId());
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public boolean isPosAvailable(final Integer id) {
        try {
            final EmployeeGraph.Position position = (EmployeeGraph.Position)this.get(id, (Class)EmployeeGraph.Position.class);
            return id.equals(position.getPositionId());
        }
        catch (Exception e) {
            return false;
        }
    }
}
