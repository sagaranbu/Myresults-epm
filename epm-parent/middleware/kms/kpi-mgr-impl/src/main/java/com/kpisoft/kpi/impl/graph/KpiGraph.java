package com.kpisoft.kpi.impl.graph;

import com.canopus.graph.*;
import com.tinkerpop.frames.*;
import com.tinkerpop.blueprints.*;

public class KpiGraph
{
    public interface GroupParentTo extends EdgeFrame
    {
        @Range
        Kpi getChildren();
        
        @Domain
        Kpi getParent();
        
        @Property("weightage")
        void setWeightage(final Float p0);
        
        @Property("weightage")
        Float getWeightage();
        
        @Property("type")
        String getType();
        
        @Property("type")
        void setType(final String p0);
    }
    
    public interface Kpi extends NamedGraphObject
    {
        @Property("kid")
        Integer getKpiId();
        
        @Property("kid")
        void setKpiId(final Integer p0);
        
        @Property("kname")
        String getKpiName();
        
        @Property("kname")
        void setKpiName(final String p0);
        
        @Property("kcode")
        String getKpiCode();
        
        @Property("kcode")
        void setKpiCode(final String p0);
        
        @Property("kweightage")
        Float getKpiWeightage();
        
        @Property("kweightage")
        void setKpiWeightage(final Float p0);
        
        @Incidence(label = "cascade")
        CascadedFrom addCascadeParent(final Kpi p0);
        
        @Incidence(label = "cascade")
        void removeCascadeParent(final CascadedFrom p0);
        
        @Incidence(label = "cascade", direction = Direction.IN)
        Iterable<CascadedTo> getCascadedChildren();
        
        @Incidence(label = "cascade")
        Iterable<CascadedFrom> getCascadeParent();
        
        @Incidence(label = "translate")
        TranslatedFrom addTranslateParent(final Kpi p0);
        
        @Incidence(label = "translate")
        void removeTranslateParent(final TranslatedFrom p0);
        
        @Incidence(label = "translate", direction = Direction.IN)
        Iterable<TranslatedTo> getTranslatedChildren();
        
        @Incidence(label = "translate")
        Iterable<TranslatedFrom> getTranslateParents();
        
        @Incidence(label = "origin")
        OriginatedFrom addOriginParent(final Kpi p0);
        
        @Incidence(label = "origin")
        void removeOriginParent(final OriginatedFrom p0);
        
        @Incidence(label = "origin", direction = Direction.IN)
        Iterable<OriginatedTo> getOriginatedChildren();
        
        @Incidence(label = "origin")
        Iterable<OriginatedFrom> getOriginParents();
        
        @Incidence(label = "group")
        GroupParent addGroupParent(final Kpi p0);
        
        @Incidence(label = "group")
        void removeGroupParent(final GroupParent p0);
        
        @Incidence(label = "group", direction = Direction.IN)
        Iterable<GroupParentTo> getGroupChildren();
        
        @Incidence(label = "group")
        Iterable<GroupParent> getGroupParent();
    }
    
    public interface CascadedFrom extends EdgeFrame
    {
        @Domain
        Kpi getChildren();
        
        @Range
        Kpi getParent();
        
        @Property("weightage")
        void setWeightage(final Float p0);
        
        @Property("weightage")
        Float getWeightage();
        
        @Property("type")
        String getType();
        
        @Property("type")
        void setType(final String p0);
    }
    
    public interface CascadedTo extends EdgeFrame
    {
        @Range
        Kpi getChildren();
        
        @Domain
        Kpi getParent();
        
        @Property("weightage")
        void setWeightage(final Float p0);
        
        @Property("weightage")
        Float getWeightage();
        
        @Property("type")
        String getType();
        
        @Property("type")
        void setType(final String p0);
    }
    
    public interface TranslatedFrom extends EdgeFrame
    {
        @Domain
        Kpi getChildren();
        
        @Range
        Kpi getParent();
        
        @Property("type")
        String getType();
        
        @Property("type")
        void setType(final String p0);
    }
    
    public interface TranslatedTo extends EdgeFrame
    {
        @Range
        Kpi getChildren();
        
        @Domain
        Kpi getParent();
        
        @Property("type")
        String getType();
        
        @Property("type")
        void setType(final String p0);
    }
    
    public interface OriginatedFrom extends EdgeFrame
    {
        @Domain
        Kpi getChildren();
        
        @Range
        Kpi getParent();
        
        @Property("type")
        String getType();
        
        @Property("type")
        void setType(final String p0);
    }
    
    public interface OriginatedTo extends EdgeFrame
    {
        @Range
        Kpi getChildren();
        
        @Domain
        Kpi getParent();
        
        @Property("type")
        String getType();
        
        @Property("type")
        void setType(final String p0);
    }
    
    public interface GroupParent extends EdgeFrame
    {
        @Domain
        Kpi getChildren();
        
        @Range
        Kpi getParent();
        
        @Property("weightage")
        void setWeightage(final Float p0);
        
        @Property("weightage")
        Float getWeightage();
        
        @Property("type")
        String getType();
        
        @Property("type")
        void setType(final String p0);
    }
}
