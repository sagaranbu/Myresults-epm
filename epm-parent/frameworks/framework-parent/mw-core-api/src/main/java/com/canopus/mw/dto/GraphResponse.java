package com.canopus.mw.dto;

import com.tinkerpop.blueprints.*;

public class GraphResponse extends BaseValueObject
{
    private static final long serialVersionUID = 6949501809057977905L;
    private Graph graph;
    
    public GraphResponse() {
        this.graph = null;
    }
    
    public Graph getGraph() {
        return this.graph;
    }
    
    public void setGraph(final Graph graph) {
        this.graph = graph;
    }
}
