package com.canopus.graph;

import com.tinkerpop.frames.*;
import com.tinkerpop.blueprints.*;

public class BaseRelationship
{
    private FramedGraph<Graph> framedGraph;
    
    public BaseRelationship(final FramedGraph<Graph> framedGraph) {
        this.framedGraph = framedGraph;
    }
    
    public FramedGraph<Graph> getFramedGraph() {
        return this.framedGraph;
    }
    
    public Graph getBaseGraph() {
        return this.framedGraph.getBaseGraph();
    }
    
    public <T> T get(final Integer id, final Class<T> clz) {
        if (id == null) {
            throw new IllegalArgumentException("Node ID can't be null");
        }
        final Vertex v = this.framedGraph.getBaseGraph().getVertex((Object)id);
        T node = null;
        if (v == null) {
            throw new IllegalArgumentException("Node not found");
        }
        node = (T)this.framedGraph.frame(v, (Class)clz);
        return node;
    }
    
    public <T> T add(final Integer id, final Class<T> clz) {
        Vertex v = this.framedGraph.getBaseGraph().getVertex((Object)id);
        T node = null;
        if (v == null) {
            v = this.framedGraph.getBaseGraph().addVertex((Object)new Integer(id));
        }
        node = (T)this.framedGraph.frame(v, (Class)clz);
        return node;
    }
    
    public <T> void removeVertex(final Integer id, final Class<T> clz) {
        final Vertex vertex = this.framedGraph.getBaseGraph().getVertex((Object)id);
        if (vertex != null) {
            this.framedGraph.getBaseGraph().removeVertex(vertex);
        }
    }
}
