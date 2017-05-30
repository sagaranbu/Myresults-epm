package com.kpisoft.org.graph;

import com.canopus.graph.*;
import com.tinkerpop.frames.*;
import com.kpisoft.org.vo.*;
import java.util.*;
import com.tinkerpop.blueprints.impls.tg.*;
import com.tinkerpop.blueprints.*;

@Deprecated
public class OrgPositionParentRelationship extends BaseRelationship
{
    private static final int MAX_DEPTH_RECURSION = 100;
    
    public OrgPositionParentRelationship(final FramedGraph<Graph> framedGraph) {
        super((FramedGraph)framedGraph);
    }
    
    public void addParent(final PositionRelationshipData data) {
        if (data.getSourceId().getId() == data.getDestinationId().getId()) {
            throw new IllegalArgumentException("Child and parent cannot be same");
        }
        if (this.isAvailable(data.getSourceId().getId()) && this.isAvailable(data.getDestinationId().getId())) {
            final PositionGraph.Position child = (PositionGraph.Position)this.get(data.getSourceId().getId(), (Class)PositionGraph.Position.class);
            final PositionGraph.Position parent = (PositionGraph.Position)this.get(data.getDestinationId().getId(), (Class)PositionGraph.Position.class);
            final PositionGraph.Parent parentEdge = child.addParent(parent);
            parentEdge.setStartDate(data.getStartDate());
            if (data.getEndDate() != null) {
                parentEdge.setEndDate(data.getEndDate());
            }
        }
    }
    
    public List<PositionGraph.Position> getParents(final int childId) {
        final List<PositionGraph.Position> result = new ArrayList<PositionGraph.Position>();
        if (this.isAvailable(childId)) {
            final PositionGraph.Position node = (PositionGraph.Position)this.get(childId, (Class)PositionGraph.Position.class);
            for (final PositionGraph.Parent iterator : node.getParents()) {
                result.add(iterator.getParent());
            }
        }
        return result;
    }
    
    public List<PositionGraph.Position> getChildrens(final int parentId) {
        final List<PositionGraph.Position> result = new ArrayList<PositionGraph.Position>();
        if (this.isAvailable(parentId)) {
            final PositionGraph.Position node = (PositionGraph.Position)this.get(parentId, (Class)PositionGraph.Position.class);
            for (final PositionGraph.Children iterator : node.getChildren()) {
                result.add(iterator.getChildren());
            }
        }
        return result;
    }
    
    public void removeParent(final int childId, final int parentId) {
        if (parentId == childId) {
            throw new IllegalArgumentException("Parent and child cannot be same");
        }
        if (this.isAvailable(childId) && this.isAvailable(parentId)) {
            final PositionGraph.Position child = (PositionGraph.Position)this.get(childId, (Class)PositionGraph.Position.class);
            final PositionGraph.Position parent = (PositionGraph.Position)this.get(parentId, (Class)PositionGraph.Position.class);
            for (final PositionGraph.Parent iterator : child.getParents()) {
                if (iterator.getParent().getPositionId() == parent.getPositionId()) {
                    child.removeParent(iterator);
                }
            }
        }
    }
    
    public List<PositionGraph.Position> getAscendants(final int id, int dist) {
        if (dist == 0) {
            dist = 100;
        }
        final Set<PositionGraph.Position> result = new HashSet<PositionGraph.Position>();
        if (this.isAvailable(id)) {
            final PositionGraph.Position node = (PositionGraph.Position)this.get(id, (Class)PositionGraph.Position.class);
            this.getAscendants(node, result, dist);
        }
        return new ArrayList<PositionGraph.Position>(result);
    }
    
    public List<PositionGraph.Position> getAscendantsByIdList(final List<Integer> ids, int dist) {
        if (dist == 0) {
            dist = 100;
        }
        final Set<PositionGraph.Position> result = new HashSet<PositionGraph.Position>();
        if (ids != null && !ids.isEmpty()) {
            for (final Integer id : ids) {
                if (this.isAvailable(id)) {
                    final PositionGraph.Position node = (PositionGraph.Position)this.get(id, (Class)PositionGraph.Position.class);
                    this.getAscendants(node, result, dist);
                }
            }
        }
        return new ArrayList<PositionGraph.Position>(result);
    }
    
    public List<PositionGraph.Position> getDescendants(final int id, int dist) {
        if (dist == 0) {
            dist = 100;
        }
        final Set<PositionGraph.Position> result = new HashSet<PositionGraph.Position>();
        if (this.isAvailable(id)) {
            final PositionGraph.Position node = (PositionGraph.Position)this.get(id, (Class)PositionGraph.Position.class);
            this.getDescendants(node, result, dist);
        }
        return new ArrayList<PositionGraph.Position>(result);
    }
    
    public List<PositionGraph.Position> getDescendantsByIdList(final List<Integer> ids, int dist) {
        if (dist == 0) {
            dist = 100;
        }
        final Set<PositionGraph.Position> result = new HashSet<PositionGraph.Position>();
        if (ids != null && !ids.isEmpty()) {
            for (final Integer id : ids) {
                if (this.isAvailable(id)) {
                    final PositionGraph.Position node = (PositionGraph.Position)this.get(id, (Class)PositionGraph.Position.class);
                    this.getDescendants(node, result, dist);
                }
            }
        }
        return new ArrayList<PositionGraph.Position>(result);
    }
    
    public boolean isAscendant(final int parentId, final int childId) {
        if (this.isAvailable(parentId) && this.isAvailable(childId)) {
            final List<PositionGraph.Position> ascs = this.getAscendants(childId, 100);
            final List<Integer> ids = new ArrayList<Integer>();
            final PositionGraph.Position parent = (PositionGraph.Position)this.get(parentId, (Class)PositionGraph.Position.class);
            for (final PositionGraph.Position iterator : ascs) {
                ids.add(iterator.getPositionId());
            }
            return ids.contains(parent.getPositionId());
        }
        return false;
    }
    
    public boolean isDescendant(final int childId, final int parentId) {
        if (this.isAvailable(childId) && this.isAvailable(parentId)) {
            final List<PositionGraph.Position> descs = this.getDescendants(parentId, 100);
            final PositionGraph.Position child = (PositionGraph.Position)this.get(childId, (Class)PositionGraph.Position.class);
            final List<Integer> ids = new ArrayList<Integer>();
            for (final PositionGraph.Position iterator : descs) {
                ids.add(iterator.getPositionId());
            }
            return ids.contains(child.getPositionId());
        }
        return false;
    }
    
    public boolean isParent(final int parentId, final int childId) {
        if (this.isAvailable(parentId) && this.isAvailable(childId)) {
            final PositionGraph.Position child = (PositionGraph.Position)this.get(childId, (Class)PositionGraph.Position.class);
            final PositionGraph.Position parent = (PositionGraph.Position)this.get(parentId, (Class)PositionGraph.Position.class);
            for (final PositionGraph.Parent iterator : child.getParents()) {
                if (iterator.getParent().equals(parent)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isChild(final int childId, final int parentId) {
        if (this.isAvailable(childId) && this.isAvailable(parentId)) {
            final PositionGraph.Position child = (PositionGraph.Position)this.get(childId, (Class)PositionGraph.Position.class);
            final PositionGraph.Position parent = (PositionGraph.Position)this.get(parentId, (Class)PositionGraph.Position.class);
            for (final PositionGraph.Children iterator : parent.getChildren()) {
                if (iterator.getChildren().equals(child)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void getAscendants(final PositionGraph.Position node, final Set<PositionGraph.Position> result, int depth) {
        final Set<PositionGraph.Position> recurse = new HashSet<PositionGraph.Position>();
        for (final PositionGraph.Parent iterator : node.getParents()) {
            if (!result.contains(iterator.getParent().getPositionId())) {
                result.add(iterator.getParent());
                recurse.add(iterator.getParent());
            }
        }
        if (depth > 1) {
            --depth;
            for (final PositionGraph.Position iterator2 : recurse) {
                this.getAscendants(iterator2, result, depth);
            }
        }
    }
    
    private void getDescendants(final PositionGraph.Position node, final Set<PositionGraph.Position> result, int depth) {
        final Set<PositionGraph.Position> recurse = new HashSet<PositionGraph.Position>();
        for (final PositionGraph.Children iterator : node.getChildren()) {
            if (!result.contains(iterator.getChildren().getPositionId())) {
                result.add(iterator.getChildren());
                recurse.add(iterator.getChildren());
            }
        }
        if (depth > 1) {
            --depth;
            for (final PositionGraph.Position iterator2 : recurse) {
                this.getDescendants(iterator2, result, depth);
            }
        }
    }
    
    public Graph getAscendantsGraph(final int id, int dist) {
        if (dist == 0) {
            dist = 100;
        }
        final Graph graph = (Graph)new TinkerGraph();
        if (this.isAvailable(id)) {
            final PositionGraph.Position node = (PositionGraph.Position)this.get(id, (Class)PositionGraph.Position.class);
            this.getAscendantsGraph(node, graph, dist);
        }
        return graph;
    }
    
    public Graph getAscendantsGraphByIdList(final List<Integer> ids, int dist) {
        if (dist == 0) {
            dist = 100;
        }
        final Graph graph = (Graph)new TinkerGraph();
        for (final Integer id : ids) {
            if (this.isAvailable(id)) {
                final PositionGraph.Position node = (PositionGraph.Position)this.get(id, (Class)PositionGraph.Position.class);
                this.getAscendantsGraph(node, graph, dist);
            }
        }
        return graph;
    }
    
    public Graph getDescendantsGraph(final int id, int dist) {
        if (dist == 0) {
            dist = 100;
        }
        final Graph graph = (Graph)new TinkerGraph();
        if (this.isAvailable(id)) {
            final PositionGraph.Position node = (PositionGraph.Position)this.get(id, (Class)PositionGraph.Position.class);
            this.getDescendantsGraph(node, graph, dist);
        }
        return graph;
    }
    
    public Graph getDescendantsGraphByIdList(final List<Integer> ids, int dist) {
        if (dist == 0) {
            dist = 100;
        }
        final Graph graph = (Graph)new TinkerGraph();
        for (final Integer id : ids) {
            if (this.isAvailable(id)) {
                final PositionGraph.Position node = (PositionGraph.Position)this.get(id, (Class)PositionGraph.Position.class);
                this.getDescendantsGraph(node, graph, dist);
            }
        }
        return graph;
    }
    
    private void getAscendantsGraph(final PositionGraph.Position node, final Graph graph, int dist) {
        Vertex parent = null;
        Vertex child = graph.getVertex((Object)node.getPositionId());
        final List<PositionGraph.Position> recurse = new ArrayList<PositionGraph.Position>();
        if (child == null) {
            child = graph.addVertex((Object)node.getPositionId());
        }
        for (final PositionGraph.Parent iterator : node.getParents()) {
            parent = graph.getVertex((Object)iterator.getParent().getPositionId());
            if (parent == null) {
                parent = graph.addVertex((Object)iterator.getParent().getPositionId());
            }
            final Edge edge = graph.addEdge((Object)null, child, parent, "parent");
            edge.setProperty("startDate", (Object)iterator.getStartDate());
            if (iterator.getEndDate() != null) {
                edge.setProperty("endDate", (Object)iterator.getStartDate());
            }
            recurse.add(iterator.getParent());
        }
        if (dist > 1) {
            --dist;
            for (final PositionGraph.Position iterator2 : recurse) {
                this.getAscendantsGraph(iterator2, graph, dist);
            }
        }
    }
    
    private void getDescendantsGraph(final PositionGraph.Position node, final Graph graph, int dist) {
        Vertex parent = graph.getVertex((Object)node.getPositionId());
        Vertex child = null;
        final List<PositionGraph.Position> recurse = new ArrayList<PositionGraph.Position>();
        if (parent == null) {
            parent = graph.addVertex((Object)node.getPositionId());
        }
        for (final PositionGraph.Children iterator : node.getChildren()) {
            child = graph.getVertex((Object)iterator.getChildren().getPositionId());
            if (child == null) {
                child = graph.addVertex((Object)iterator.getChildren().getPositionId());
            }
            final Edge edge = graph.addEdge((Object)null, parent, child, "parent");
            edge.setProperty("startDate", (Object)iterator.getStartDate());
            if (iterator.getEndDate() != null) {
                edge.setProperty("endDate", (Object)iterator.getStartDate());
            }
            recurse.add(iterator.getChildren());
        }
        if (dist > 1) {
            --dist;
            for (final PositionGraph.Position iterator2 : recurse) {
                this.getDescendantsGraph(iterator2, graph, dist);
            }
        }
    }
    
    public boolean isAvailable(final Integer id) {
        try {
            final PositionGraph.Position position = (PositionGraph.Position)this.get(id, (Class)PositionGraph.Position.class);
            return id.equals(position.getPositionId());
        }
        catch (Exception e) {
            return false;
        }
    }
}
