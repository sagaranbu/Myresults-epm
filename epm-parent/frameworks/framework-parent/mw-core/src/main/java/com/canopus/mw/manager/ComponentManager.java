package com.canopus.mw.manager;

import com.canopus.mw.dto.*;
import org.springframework.beans.factory.annotation.*;

public class ComponentManager
{
    @Autowired
    private ComponentInfo componentInfo;
    
    public void setComponentInfo(final ComponentInfo componentInfo) {
        this.componentInfo = componentInfo;
    }
    
    public void start() {
    }
    
    public void stop() {
    }
    
    public ComponentInfo getComponentInfo() {
        return this.componentInfo;
    }
}
