package com.kpisoft.org.graph;

import com.canopus.graph.*;
import com.tinkerpop.frames.*;
import com.kpisoft.org.vo.*;
import java.util.*;
import com.tinkerpop.blueprints.impls.tg.*;
import com.tinkerpop.blueprints.*;

public class OrgIdentityParentRelationship extends BaseRelationship
{
    private static final int MAX_DEPTH_RECURSION = 100;
    
    public OrgIdentityParentRelationship(final FramedGraph<Graph> framedGraph) {
        super((FramedGraph)framedGraph);
    }
    
    public void addParent(final OrgParentRelationshipBean data) {
        if (data.getSourceIdentity().getId() == data.getDestinationIdentity().getId()) {
            throw new IllegalArgumentException("Child and parent cannot be same");
        }
        if (this.isAvailable(data.getSourceIdentity().getId()) && this.isAvailable(data.getDestinationIdentity().getId())) {
            final OrgGraph.Organization child = (OrgGraph.Organization)this.get(data.getSourceIdentity().getId(), (Class)OrgGraph.Organization.class);
            final OrgGraph.Organization parent = (OrgGraph.Organization)this.get(data.getDestinationIdentity().getId(), (Class)OrgGraph.Organization.class);
            final OrgGraph.Parent parentEdge = child.addParent(parent);
            parentEdge.setDimensionId(data.getDimensionId());
            parentEdge.setStartDate(data.getStartDate());
            if (data.getEndDate() != null) {
                parentEdge.setEndDate(data.getEndDate());
            }
        }
    }
    
    public List<OrgGraph.Organization> getParents(final Integer childId) {
        final List<OrgGraph.Organization> result = new ArrayList<OrgGraph.Organization>();
        if (this.isAvailable(childId)) {
            final OrgGraph.Organization node = (OrgGraph.Organization)this.get(childId, (Class)OrgGraph.Organization.class);
            for (final OrgGraph.Parent iterator : node.getParents()) {
                result.add(iterator.getParent());
            }
        }
        return result;
    }
    
    public List<OrgGraph.Organization> getChildrens(final Integer parentId) {
        final List<OrgGraph.Organization> result = new ArrayList<OrgGraph.Organization>();
        if (this.isAvailable(parentId)) {
            final OrgGraph.Organization node = (OrgGraph.Organization)this.get(parentId, (Class)OrgGraph.Organization.class);
            for (final OrgGraph.Children iterator : node.getChildren()) {
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
            final OrgGraph.Organization child = (OrgGraph.Organization)this.get(childId, (Class)OrgGraph.Organization.class);
            final OrgGraph.Organization parent = (OrgGraph.Organization)this.get(parentId, (Class)OrgGraph.Organization.class);
            for (final OrgGraph.Parent iterator : child.getParents()) {
                if (iterator.getParent().getOrgId() == parent.getOrgId()) {
                    child.removeParent(iterator);
                }
            }
        }
    }
    
    public List<OrgGraph.Organization> getAscendants(final int id, int dist) {
        if (dist == 0) {
            dist = 100;
        }
        final Set<OrgGraph.Organization> result = new HashSet<OrgGraph.Organization>();
        if (this.isAvailable(id)) {
            final OrgGraph.Organization node = (OrgGraph.Organization)this.get(id, (Class)OrgGraph.Organization.class);
            this.getAscendants(node, result, dist);
        }
        return new ArrayList<OrgGraph.Organization>(result);
    }
    
    public List<OrgGraph.Organization> getAscendantsByIdList(final List<Integer> ids, int dist) {
        if (dist == 0) {
            dist = 100;
        }
        final Set<OrgGraph.Organization> result = new HashSet<OrgGraph.Organization>();
        if (ids != null && !ids.isEmpty()) {
            for (final Integer id : ids) {
                if (this.isAvailable(id)) {
                    final OrgGraph.Organization node = (OrgGraph.Organization)this.get(id, (Class)OrgGraph.Organization.class);
                    this.getAscendants(node, result, dist);
                }
            }
        }
        return new ArrayList<OrgGraph.Organization>(result);
    }
    
    public List<OrgGraph.Organization> getDescendants(final int id, int dist) {
        if (dist == 0) {
            dist = 100;
        }
        final Set<OrgGraph.Organization> result = new HashSet<OrgGraph.Organization>();
        if (this.isAvailable(id)) {
            final OrgGraph.Organization node = (OrgGraph.Organization)this.get(id, (Class)OrgGraph.Organization.class);
            this.getDescendants(node, result, dist);
        }
        return new ArrayList<OrgGraph.Organization>(result);
    }
    
    public List<OrgGraph.Organization> getDescendantsByIdList(final List<Integer> ids, int dist) {
        if (dist == 0) {
            dist = 100;
        }
        final Set<OrgGraph.Organization> result = new HashSet<OrgGraph.Organization>();
        if (ids != null && !ids.isEmpty()) {
            for (final Integer id : ids) {
                if (this.isAvailable(id)) {
                    final OrgGraph.Organization node = (OrgGraph.Organization)this.get(id, (Class)OrgGraph.Organization.class);
                    this.getDescendants(node, result, dist);
                }
            }
        }
        return new ArrayList<OrgGraph.Organization>(result);
    }
    
    public boolean isAscendant(final int parentId, final int childId) {
        if (this.isAvailable(parentId) && this.isAvailable(childId)) {
            final OrgGraph.Organization parent = (OrgGraph.Organization)this.get(parentId, (Class)OrgGraph.Organization.class);
            final List<OrgGraph.Organization> ascs = this.getAscendants(childId, 100);
            final List<Integer> ids = new ArrayList<Integer>();
            for (final OrgGraph.Organization iterator : ascs) {
                ids.add(iterator.getOrgId());
            }
            return ids.contains(parent.getOrgId());
        }
        return false;
    }
    
    public boolean isDescendant(final int childId, final int parentId) {
        if (this.isAvailable(parentId) && this.isAvailable(childId)) {
            final OrgGraph.Organization child = (OrgGraph.Organization)this.get(childId, (Class)OrgGraph.Organization.class);
            final List<OrgGraph.Organization> descs = this.getDescendants(parentId, 100);
            final List<Integer> ids = new ArrayList<Integer>();
            for (final OrgGraph.Organization iterator : descs) {
                ids.add(iterator.getOrgId());
            }
            return ids.contains(child.getOrgId());
        }
        return false;
    }
    
    public boolean isParent(final int parentId, final int childId) {
        if (this.isAvailable(parentId) && this.isAvailable(childId)) {
            final OrgGraph.Organization child = (OrgGraph.Organization)this.get(childId, (Class)OrgGraph.Organization.class);
            final OrgGraph.Organization parent = (OrgGraph.Organization)this.get(parentId, (Class)OrgGraph.Organization.class);
            for (final OrgGraph.Parent iterator : child.getParents()) {
                if (iterator.getParent().equals(parent)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isChild(final int childId, final int parentId) {
        if (this.isAvailable(parentId) && this.isAvailable(childId)) {
            final OrgGraph.Organization child = (OrgGraph.Organization)this.get(childId, (Class)OrgGraph.Organization.class);
            final OrgGraph.Organization parent = (OrgGraph.Organization)this.get(parentId, (Class)OrgGraph.Organization.class);
            for (final OrgGraph.Children iterator : parent.getChildren()) {
                if (iterator.getChildren().equals(child)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void getAscendants(final OrgGraph.Organization node, final Set<OrgGraph.Organization> result, int depth) {
        final Set<OrgGraph.Organization> recurse = new HashSet<OrgGraph.Organization>();
        for (final OrgGraph.Parent iterator : node.getParents()) {
            result.add(iterator.getParent());
            recurse.add(iterator.getParent());
        }
        if (depth > 1) {
            --depth;
            for (final OrgGraph.Organization iterator2 : recurse) {
                this.getAscendants(iterator2, result, depth);
            }
        }
    }
    
    private void getDescendants(final OrgGraph.Organization node, final Set<OrgGraph.Organization> result, int depth) {
        final Set<OrgGraph.Organization> recurse = new HashSet<OrgGraph.Organization>();
        for (final OrgGraph.Children iterator : node.getChildren()) {
            result.add(iterator.getChildren());
            recurse.add(iterator.getChildren());
        }
        if (depth > 1) {
            --depth;
            for (final OrgGraph.Organization iterator2 : recurse) {
                this.getDescendants(iterator2, result, depth);
            }
        }
    }
    
    public List<OrgGraph.Organization> getParentsByDimension(final int childId, final int dimensionId) {
        final List<OrgGraph.Organization> result = new ArrayList<OrgGraph.Organization>();
        if (this.isAvailable(childId)) {
            final OrgGraph.Organization node = (OrgGraph.Organization)this.get(childId, (Class)OrgGraph.Organization.class);
            for (final OrgGraph.Parent iterator : node.getParents()) {
                if (iterator.getDimensionId() == dimensionId) {
                    result.add(iterator.getParent());
                }
            }
        }
        return result;
    }
    
    public List<OrgGraph.Organization> getChildrensByDimension(final int parentId, final int dimensionId) {
        final List<OrgGraph.Organization> result = new ArrayList<OrgGraph.Organization>();
        if (this.isAvailable(parentId)) {
            final OrgGraph.Organization node = (OrgGraph.Organization)this.get(parentId, (Class)OrgGraph.Organization.class);
            for (final OrgGraph.Children iterator : node.getChildren()) {
                if (iterator.getDimensionId() == dimensionId) {
                    result.add(iterator.getChildren());
                }
            }
        }
        return result;
    }
    
    public List<OrgGraph.Organization> getAscendantsByDimension(final int id, int dist, final int dimensionId) {
        if (dist == 0) {
            dist = 100;
        }
        final Set<OrgGraph.Organization> result = new HashSet<OrgGraph.Organization>();
        if (this.isAvailable(id)) {
            final OrgGraph.Organization node = (OrgGraph.Organization)this.get(id, (Class)OrgGraph.Organization.class);
            this.getAscendantsByDimension(node, result, dist, dimensionId);
        }
        return new ArrayList<OrgGraph.Organization>(result);
    }
    
    public List<OrgGraph.Organization> getDescendantsByDimension(final int id, int dist, final int dimensionId) {
        if (dist == 0) {
            dist = 100;
        }
        final Set<OrgGraph.Organization> result = new HashSet<OrgGraph.Organization>();
        if (this.isAvailable(id)) {
            final OrgGraph.Organization node = (OrgGraph.Organization)this.get(id, (Class)OrgGraph.Organization.class);
            this.getDescendantsByDimension(node, result, dist, dimensionId);
        }
        return new ArrayList<OrgGraph.Organization>(result);
    }
    
    private void getAscendantsByDimension(final OrgGraph.Organization node, final Set<OrgGraph.Organization> result, int depth, final int dimensionId) {
        final Set<OrgGraph.Organization> recurse = new HashSet<OrgGraph.Organization>();
        for (final OrgGraph.Parent iterator : node.getParents()) {
            if (iterator.getDimensionId() == dimensionId) {
                result.add(iterator.getParent());
                recurse.add(iterator.getParent());
            }
        }
        if (depth > 1) {
            --depth;
            for (final OrgGraph.Organization iterator2 : recurse) {
                this.getAscendants(iterator2, result, depth);
            }
        }
    }
    
    private void getDescendantsByDimension(final OrgGraph.Organization node, final Set<OrgGraph.Organization> result, int depth, final int dimensionId) {
        final Set<OrgGraph.Organization> recurse = new HashSet<OrgGraph.Organization>();
        for (final OrgGraph.Children iterator : node.getChildren()) {
            if (iterator.getDimensionId() == dimensionId) {
                result.add(iterator.getChildren());
                recurse.add(iterator.getChildren());
            }
        }
        if (depth > 1) {
            --depth;
            for (final OrgGraph.Organization iterator2 : recurse) {
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
            final OrgGraph.Organization node = (OrgGraph.Organization)this.get(id, (Class)OrgGraph.Organization.class);
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
                final OrgGraph.Organization node = (OrgGraph.Organization)this.get(id, (Class)OrgGraph.Organization.class);
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
            final OrgGraph.Organization node = (OrgGraph.Organization)this.get(id, (Class)OrgGraph.Organization.class);
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
                final OrgGraph.Organization node = (OrgGraph.Organization)this.get(id, (Class)OrgGraph.Organization.class);
                this.getDescendantsGraph(node, graph, dist);
            }
        }
        return graph;
    }
    
    public Graph getDescendantsGraphByIdList2(final List<Integer> ids, int dist) {
        if (dist == 0) {
            dist = 100;
        }
        final Graph graph = (Graph)new TinkerGraph();
        for (final Integer id : ids) {
            if (this.isAvailable(id)) {
                final Vertex node = this.getBaseGraph().getVertex((Object)id);
                this.getDescendantsGraph2(node, graph, dist);
            }
        }
        return graph;
    }
    
    private void getAscendantsGraph(final OrgGraph.Organization node, final Graph graph, int dist) {
        Vertex parent = null;
        Vertex child = graph.getVertex((Object)node.getOrgId());
        final List<OrgGraph.Organization> recurse = new ArrayList<OrgGraph.Organization>();
        if (child == null) {
            child = graph.addVertex((Object)node.getOrgId());
        }
        this.addVertexData(child, node);
        for (final OrgGraph.Parent iterator : node.getParents()) {
            parent = graph.getVertex((Object)iterator.getParent().getOrgId());
            if (parent == null) {
                parent = graph.addVertex((Object)iterator.getParent().getOrgId());
            }
            this.addVertexData(parent, iterator.getParent());
            final Edge edge = graph.addEdge((Object)null, child, parent, "parent");
            this.addEdgeData(edge, iterator);
            recurse.add(iterator.getParent());
        }
        if (dist > 1) {
            --dist;
            for (final OrgGraph.Organization iterator2 : recurse) {
                this.getAscendantsGraph(iterator2, graph, dist);
            }
        }
    }
    
    private void getDescendantsGraph(final OrgGraph.Organization node, final Graph graph, int dist) {
        Vertex parent = graph.getVertex((Object)node.getOrgId());
        Vertex child = null;
        final List<OrgGraph.Organization> recurse = new ArrayList<OrgGraph.Organization>();
        if (parent == null) {
            parent = graph.addVertex((Object)node.getOrgId());
        }
        this.addVertexData(parent, node);
        for (final OrgGraph.Children iterator : node.getChildren()) {
            child = graph.getVertex((Object)iterator.getChildren().getOrgId());
            if (child == null) {
                child = graph.addVertex((Object)iterator.getChildren().getOrgId());
            }
            this.addVertexData(child, iterator.getChildren());
            final Edge edge = graph.addEdge((Object)null, parent, child, "parent");
            this.addEdgeData(edge, iterator);
            recurse.add(iterator.getChildren());
        }
        if (dist > 1) {
            --dist;
            for (final OrgGraph.Organization iterator2 : recurse) {
                this.getDescendantsGraph(iterator2, graph, dist);
            }
        }
    }
    
    private void getDescendantsGraph2(final Vertex node, final Graph graph, int dist) {
        Vertex parent = graph.getVertex(node.getId());
        Vertex child = null;
        final List<Vertex> recurse = new ArrayList<Vertex>();
        if (parent == null) {
            parent = graph.addVertex(node.getId());
        }
        this.copyProperties((Element)parent, (Element)node);
        for (final Edge childEdge : node.getEdges(Direction.IN, new String[] { "parent" })) {
            final Vertex mainChild = childEdge.getVertex(Direction.OUT);
            child = graph.getVertex(mainChild.getId());
            if (child == null) {
                child = graph.addVertex(mainChild.getId());
            }
            this.copyProperties((Element)child, (Element)mainChild);
            final Edge edge = graph.addEdge((Object)null, parent, child, "parent");
            this.copyProperties((Element)edge, (Element)childEdge);
            recurse.add(mainChild);
        }
        if (dist > 1) {
            --dist;
            for (final Vertex iterator : recurse) {
                this.getDescendantsGraph2(iterator, graph, dist);
            }
        }
    }
    
    public Graph getAscendantsGraphByDimension(final int id, int dist, final int dimensionId) {
        if (dist == 0) {
            dist = 100;
        }
        final Graph graph = (Graph)new TinkerGraph();
        if (this.isAvailable(id)) {
            final OrgGraph.Organization node = (OrgGraph.Organization)this.get(id, (Class)OrgGraph.Organization.class);
            this.getAscendantsGraphByDimension(node, graph, dist, dimensionId);
        }
        return graph;
    }
    
    public Graph getDescendantsGraphByDimension(final int id, int dist, final int dimensionId) {
        if (dist == 0) {
            dist = 100;
        }
        final Graph graph = (Graph)new TinkerGraph();
        if (this.isAvailable(id)) {
            final OrgGraph.Organization node = (OrgGraph.Organization)this.get(id, (Class)OrgGraph.Organization.class);
            this.getDescendantsGraphByDimension(node, graph, dist, dimensionId);
        }
        return graph;
    }
    
    private void getAscendantsGraphByDimension(final OrgGraph.Organization node, final Graph graph, int dist, final int dimensionId) {
        Vertex parent = null;
        Vertex child = graph.getVertex((Object)node.getOrgId());
        final List<OrgGraph.Organization> recurse = new ArrayList<OrgGraph.Organization>();
        if (child == null) {
            child = graph.addVertex((Object)node.getOrgId());
        }
        this.addVertexData(child, node);
        for (final OrgGraph.Parent iterator : node.getParents()) {
            if (iterator.getDimensionId() == dimensionId) {
                parent = graph.getVertex((Object)iterator.getParent().getOrgId());
                if (parent == null) {
                    parent = graph.addVertex((Object)iterator.getParent().getOrgId());
                }
                this.addVertexData(parent, iterator.getParent());
                final Edge edge = graph.addEdge((Object)null, child, parent, "parent");
                this.addEdgeData(edge, iterator);
                recurse.add(iterator.getParent());
            }
        }
        if (dist > 1) {
            --dist;
            for (final OrgGraph.Organization iterator2 : recurse) {
                this.getAscendantsGraphByDimension(iterator2, graph, dist, dimensionId);
            }
        }
    }
    
    private void getDescendantsGraphByDimension(final OrgGraph.Organization node, final Graph graph, int dist, final int dimensionId) {
        Vertex parent = graph.getVertex((Object)node.getOrgId());
        Vertex child = null;
        final List<OrgGraph.Organization> recurse = new ArrayList<OrgGraph.Organization>();
        if (parent == null) {
            parent = graph.addVertex((Object)node.getOrgId());
        }
        this.addVertexData(parent, node);
        for (final OrgGraph.Children iterator : node.getChildren()) {
            if (iterator.getDimensionId() == dimensionId) {
                child = graph.getVertex((Object)iterator.getChildren().getOrgId());
                if (child == null) {
                    child = graph.addVertex((Object)iterator.getChildren().getOrgId());
                }
                this.addVertexData(child, iterator.getChildren());
                final Edge edge = graph.addEdge((Object)null, parent, child, "parent");
                this.addEdgeData(edge, iterator);
                recurse.add(iterator.getChildren());
            }
        }
        if (dist > 1) {
            --dist;
            for (final OrgGraph.Organization iterator2 : recurse) {
                this.getDescendantsGraphByDimension(iterator2, graph, dist, dimensionId);
            }
        }
    }
    
    private boolean isAvailable(final Integer id) {
        try {
            final OrgGraph.Organization org = (OrgGraph.Organization)this.get(id, (Class)OrgGraph.Organization.class);
            return id.equals(org.getOrgId());
        }
        catch (Exception e) {
            return false;
        }
    }
    
    private void addVertexData(final Vertex vertex, final OrgGraph.Organization node) {
        vertex.setProperty("orgId", (Object)node.getOrgId());
        vertex.setProperty("orgName", (Object)node.getOrgName());
        vertex.setProperty("orgCode", (Object)node.getOrgCode());
        vertex.setProperty("orgStartDate", (Object)node.getOrgStartDate());
        this.setNonNullProperty((Element)vertex, "orgType", node.getOrgType());
        this.setNonNullProperty((Element)vertex, "status", node.getStatus());
        this.setNonNullProperty((Element)vertex, "orgEndDate", node.getOrgEndDate());
        this.setNonNullProperty((Element)vertex, "level", node.getLevel());
    }
    
    private void copyProperties(final Element tgt, final Element src) {
        for (final String key : src.getPropertyKeys()) {
            final Object val = src.getProperty(key);
            this.setNonNullProperty(tgt, key, val);
        }
    }
    
    private void addEdgeData(final Edge edge, final OrgGraph.Parent node) {
        edge.setProperty("dimensionId", (Object)node.getDimensionId());
        edge.setProperty("startDate", (Object)node.getStartDate());
        this.setNonNullProperty((Element)edge, "endDate", node.getEndDate());
    }
    
    private void addEdgeData(final Edge edge, final OrgGraph.Children node) {
        edge.setProperty("dimensionId", (Object)node.getDimensionId());
        edge.setProperty("startDate", (Object)node.getStartDate());
        this.setNonNullProperty((Element)edge, "endDate", node.getEndDate());
    }
    
    private void setNonNullProperty(final Element element, final String property, final Object value) {
        if (element == null) {
            return;
        }
        if (property == null) {
            return;
        }
        if (value == null) {
            return;
        }
        element.setProperty(property, value);
    }
}
