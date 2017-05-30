package com.kpisoft.emp.impl.graph;

import com.canopus.graph.*;
import com.tinkerpop.frames.*;
import com.tinkerpop.blueprints.*;
import java.util.*;

public class SupervisorRelationship extends BaseRelationship
{
    private static final int MAX_DEPTH_RECURSION = 100;
    
    public SupervisorRelationship(final FramedGraph<Graph> framedGraph) {
        super((FramedGraph)framedGraph);
    }
    
    public void addSupervisor(final Integer subID, final Integer supID) {
        if (subID == supID) {
            throw new IllegalArgumentException("Subordinate and supervisor can't be same");
        }
        if (this.isAvailable(subID) && this.isAvailable(supID)) {
            final EmployeeGraph.Employee sub = (EmployeeGraph.Employee)this.get(subID, (Class)EmployeeGraph.Employee.class);
            final EmployeeGraph.Employee sup = (EmployeeGraph.Employee)this.get(supID, (Class)EmployeeGraph.Employee.class);
            boolean primary = true;
            if (sub.getSupervisors().iterator().hasNext()) {
                primary = false;
            }
            final boolean cycle = this.isAscendant(subID, supID);
            if (cycle) {
                throw new IllegalArgumentException("Cyclic reference");
            }
            final EmployeeGraph.Supervisor srel = sub.addSupervisor(sup);
            srel.setPrimary(primary);
        }
    }
    
    public void addSupervisor(final Integer subID, final Integer supID, final Boolean primary) {
        if (subID == supID) {
            throw new IllegalArgumentException("Subordinate and supervisor can't be same");
        }
        if (this.isAvailable(subID) && this.isAvailable(supID)) {
            final EmployeeGraph.Employee sub = (EmployeeGraph.Employee)this.get(subID, (Class)EmployeeGraph.Employee.class);
            final EmployeeGraph.Employee sup = (EmployeeGraph.Employee)this.get(supID, (Class)EmployeeGraph.Employee.class);
            final boolean cycle = this.isAscendant(subID, supID);
            if (cycle) {
                throw new IllegalArgumentException("Cyclic reference");
            }
            final EmployeeGraph.Supervisor srel = sub.addSupervisor(sup);
            srel.setPrimary(primary);
        }
    }
    
    public void setPrimarySupervisor(final Integer subID, final Integer supID) {
        if (subID == supID) {
            throw new IllegalArgumentException("Subordinate and supervisor can't be same");
        }
        if (this.isAvailable(subID) && this.isAvailable(supID)) {
            final EmployeeGraph.Employee sub = (EmployeeGraph.Employee)this.get(subID, (Class)EmployeeGraph.Employee.class);
            final EmployeeGraph.Employee sup = (EmployeeGraph.Employee)this.get(supID, (Class)EmployeeGraph.Employee.class);
            for (final EmployeeGraph.Supervisor suprel : sub.getSupervisors()) {
                if (suprel.getSupervisor().getEmployeeId() != sup.getEmployeeId()) {
                    if (!suprel.isPrimary()) {
                        continue;
                    }
                    suprel.setPrimary(false);
                }
                else {
                    suprel.setPrimary(true);
                }
            }
        }
    }
    
    public void removeSupervisor(final Integer subID, final Integer supID) {
        if (subID == supID) {
            throw new IllegalArgumentException("Subordinate and supervisor can't be same");
        }
        if (this.isAvailable(subID) && this.isAvailable(supID)) {
            final EmployeeGraph.Employee sub = (EmployeeGraph.Employee)this.get(subID, (Class)EmployeeGraph.Employee.class);
            final EmployeeGraph.Employee sup = (EmployeeGraph.Employee)this.get(supID, (Class)EmployeeGraph.Employee.class);
            for (final EmployeeGraph.Supervisor suprel : sub.getSupervisors()) {
                if (suprel.getSupervisor().getEmployeeId() == sup.getEmployeeId()) {
                    sub.removeSupervisor(suprel);
                }
            }
        }
    }
    
    public EmployeeGraph.Employee getPrimarySupervisor(final Integer id) {
        if (this.isAvailable(id)) {
            final EmployeeGraph.Employee node = (EmployeeGraph.Employee)this.get(id, (Class)EmployeeGraph.Employee.class);
            for (final EmployeeGraph.Supervisor sup : node.getSupervisors()) {
                if (sup.isPrimary()) {
                    return sup.getSupervisor();
                }
            }
        }
        return null;
    }
    
    public List<EmployeeGraph.Employee> getSupervisors(final Integer id) {
        final List<EmployeeGraph.Employee> result = new ArrayList<EmployeeGraph.Employee>();
        if (this.isAvailable(id)) {
            final EmployeeGraph.Employee node = (EmployeeGraph.Employee)this.get(id, (Class)EmployeeGraph.Employee.class);
            for (final EmployeeGraph.Supervisor sup : node.getSupervisors()) {
                result.add(sup.getSupervisor());
            }
        }
        return result;
    }
    
    public List<EmployeeGraph.Employee> getSubordinates(final Integer id) {
        final List<EmployeeGraph.Employee> result = new ArrayList<EmployeeGraph.Employee>();
        if (this.isAvailable(id)) {
            final EmployeeGraph.Employee node = (EmployeeGraph.Employee)this.get(id, (Class)EmployeeGraph.Employee.class);
            for (final EmployeeGraph.SupervisedBy sup : node.getSubordinates()) {
                result.add(sup.getSubordinate());
            }
        }
        return result;
    }
    
    public List<EmployeeGraph.Employee> getPrimarySubordinates(final Integer id) {
        final List<EmployeeGraph.Employee> result = new ArrayList<EmployeeGraph.Employee>();
        if (this.isAvailable(id)) {
            final EmployeeGraph.Employee node = (EmployeeGraph.Employee)this.get(id, (Class)EmployeeGraph.Employee.class);
            for (final EmployeeGraph.SupervisedBy sup : node.getSubordinates()) {
                if (sup.isPrimary()) {
                    result.add(sup.getSubordinate());
                }
            }
        }
        return result;
    }
    
    public List<EmployeeGraph.Employee> getPeers(final Integer id) {
        final List<EmployeeGraph.Employee> result = new ArrayList<EmployeeGraph.Employee>();
        if (this.isAvailable(id)) {
            final EmployeeGraph.Employee node = (EmployeeGraph.Employee)this.get(id, (Class)EmployeeGraph.Employee.class);
            for (final EmployeeGraph.Supervisor suprel : node.getSupervisors()) {
                final EmployeeGraph.Employee sup = suprel.getSupervisor();
                final Iterable<EmployeeGraph.SupervisedBy> peers = sup.getSubordinates();
                for (final EmployeeGraph.SupervisedBy peer : peers) {
                    if (!peer.getSubordinate().equals(node) && !result.contains(peer.getSubordinate())) {
                        result.add(peer.getSubordinate());
                    }
                }
            }
        }
        return result;
    }
    
    public List<EmployeeGraph.Employee> getPrimaryPeers(final Integer id) {
        final List<EmployeeGraph.Employee> result = new ArrayList<EmployeeGraph.Employee>();
        if (this.isAvailable(id)) {
            final EmployeeGraph.Employee node = (EmployeeGraph.Employee)this.get(id, (Class)EmployeeGraph.Employee.class);
            for (final EmployeeGraph.Supervisor suprel : node.getSupervisors()) {
                final EmployeeGraph.Employee sup = suprel.getSupervisor();
                final Iterable<EmployeeGraph.SupervisedBy> peers = sup.getSubordinates();
                for (final EmployeeGraph.SupervisedBy peer : peers) {
                    if (!peer.getSubordinate().equals(node) && !result.contains(peer.getSubordinate()) && peer.isPrimary()) {
                        result.add(peer.getSubordinate());
                    }
                }
            }
        }
        return result;
    }
    
    public List<EmployeeGraph.Employee> getDescendants(final Integer id, final int dist) {
        final List<EmployeeGraph.Employee> res = new ArrayList<EmployeeGraph.Employee>();
        if (this.isAvailable(id)) {
            final EmployeeGraph.Employee node = (EmployeeGraph.Employee)this.get(id, (Class)EmployeeGraph.Employee.class);
            this.getDescendants(node, res, dist, false);
        }
        return res;
    }
    
    public List<EmployeeGraph.Employee> getPrimaryDescendants(final Integer id, final int dist) {
        final List<EmployeeGraph.Employee> res = new ArrayList<EmployeeGraph.Employee>();
        if (this.isAvailable(id)) {
            final EmployeeGraph.Employee node = (EmployeeGraph.Employee)this.get(id, (Class)EmployeeGraph.Employee.class);
            this.getDescendants(node, res, dist, true);
        }
        return res;
    }
    
    public List<EmployeeGraph.Employee> getAscendants(final Integer id, final int dist) {
        final List<EmployeeGraph.Employee> result = new ArrayList<EmployeeGraph.Employee>();
        if (this.isAvailable(id)) {
            final EmployeeGraph.Employee node = (EmployeeGraph.Employee)this.get(id, (Class)EmployeeGraph.Employee.class);
            this.getAscendants(node, result, dist, false);
        }
        return result;
    }
    
    public List<EmployeeGraph.Employee> getPrimaryAscendants(final Integer id, final int dist) {
        final List<EmployeeGraph.Employee> result = new ArrayList<EmployeeGraph.Employee>();
        if (this.isAvailable(id)) {
            final EmployeeGraph.Employee node = (EmployeeGraph.Employee)this.get(id, (Class)EmployeeGraph.Employee.class);
            this.getAscendants(node, result, dist, true);
        }
        return result;
    }
    
    public boolean isSupervisor(final Integer supID, final Integer subID) {
        if (this.isAvailable(supID) && this.isAvailable(subID)) {
            final EmployeeGraph.Employee sub = (EmployeeGraph.Employee)this.get(subID, (Class)EmployeeGraph.Employee.class);
            final EmployeeGraph.Employee sup = (EmployeeGraph.Employee)this.get(supID, (Class)EmployeeGraph.Employee.class);
            for (final EmployeeGraph.Supervisor suprel : sub.getSupervisors()) {
                if (suprel.getSupervisor().equals(sup)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isAscendant(final Integer supID, final Integer subID) {
        if (this.isAvailable(supID) && this.isAvailable(subID)) {
            final EmployeeGraph.Employee sup = (EmployeeGraph.Employee)this.get(supID, (Class)EmployeeGraph.Employee.class);
            final List<EmployeeGraph.Employee> ascs = this.getAscendants(subID, 100);
            return ascs.contains(sup);
        }
        return false;
    }
    
    public boolean isSubordinate(final Integer subID, final Integer supID) {
        if (this.isAvailable(subID) && this.isAvailable(supID)) {
            final EmployeeGraph.Employee sub = (EmployeeGraph.Employee)this.get(subID, (Class)EmployeeGraph.Employee.class);
            final EmployeeGraph.Employee sup = (EmployeeGraph.Employee)this.get(supID, (Class)EmployeeGraph.Employee.class);
            for (final EmployeeGraph.SupervisedBy suprel : sup.getSubordinates()) {
                if (suprel.getSubordinate().equals(sub)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isDescendant(final Integer subID, final Integer supID) {
        if (this.isAvailable(subID) && this.isAvailable(supID)) {
            final EmployeeGraph.Employee sub = (EmployeeGraph.Employee)this.get(subID, (Class)EmployeeGraph.Employee.class);
            final List<EmployeeGraph.Employee> descs = this.getDescendants(supID, 100);
            return descs.contains(sub);
        }
        return false;
    }
    
    private void getDescendants(final EmployeeGraph.Employee node, final List<EmployeeGraph.Employee> result, int depth, final boolean primOnly) {
        final List<EmployeeGraph.Employee> recurse = new ArrayList<EmployeeGraph.Employee>();
        for (final EmployeeGraph.SupervisedBy sup : node.getSubordinates()) {
            final boolean add = !primOnly || sup.isPrimary();
            if (add && !result.contains(sup.getSubordinate())) {
                result.add(sup.getSubordinate());
                recurse.add(sup.getSubordinate());
            }
        }
        if (depth > 1) {
            --depth;
            for (final EmployeeGraph.Employee emp : recurse) {
                this.getDescendants(emp, result, depth, primOnly);
            }
        }
    }
    
    private void getAscendants(final EmployeeGraph.Employee node, final List<EmployeeGraph.Employee> result, int depth, final boolean primOnly) {
        final List<EmployeeGraph.Employee> recurse = new ArrayList<EmployeeGraph.Employee>();
        for (final EmployeeGraph.Supervisor sup : node.getSupervisors()) {
            final boolean add = !primOnly || sup.isPrimary();
            if (add && !result.contains(sup.getSupervisor())) {
                result.add(sup.getSupervisor());
                recurse.add(sup.getSupervisor());
            }
        }
        if (depth > 1) {
            --depth;
            for (final EmployeeGraph.Employee emp : recurse) {
                this.getAscendants(emp, result, depth, primOnly);
            }
        }
    }
    
    public boolean isAvailable(final Integer id) {
        try {
            final EmployeeGraph.Employee employee = (EmployeeGraph.Employee)this.get(id, (Class)EmployeeGraph.Employee.class);
            return id.equals(employee.getEmployeeId());
        }
        catch (Exception e) {
            return false;
        }
    }
}
