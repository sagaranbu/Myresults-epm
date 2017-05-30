package com.canopus.graph;

import com.tinkerpop.frames.*;

public interface NamedGraphObject extends VertexFrame
{
    @Property("name")
    String getName();
    
    @Property("name")
    void setName(final String p0);
}
