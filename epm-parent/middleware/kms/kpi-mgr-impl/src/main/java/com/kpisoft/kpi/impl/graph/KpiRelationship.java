package com.kpisoft.kpi.impl.graph;

import com.canopus.graph.*;
import com.tinkerpop.frames.*;
import com.kpisoft.kpi.vo.*;
import java.util.*;
import com.tinkerpop.blueprints.impls.tg.*;
import com.tinkerpop.blueprints.*;

public class KpiRelationship extends BaseRelationship
{
    private static final int MAX_DEPTH_RECURSION = 100;
    
    public KpiRelationship(final FramedGraph<Graph> framedGraph) {
        super((FramedGraph)framedGraph);
    }
    
    public void addParent(final KpiKpiGraphRelationshipBean graphBean) {
        if (graphBean.getChild().getId() == graphBean.getParent().getId()) {
            throw new IllegalArgumentException("Parent and Child can't be same");
        }
        final KpiGraph.Kpi parent = (KpiGraph.Kpi)this.get(graphBean.getParent().getId(), (Class)KpiGraph.Kpi.class);
        final KpiGraph.Kpi child = (KpiGraph.Kpi)this.get(graphBean.getChild().getId(), (Class)KpiGraph.Kpi.class);
        final String type = graphBean.getType();
        Float weightage = graphBean.getWeightage();
        weightage = ((weightage == null) ? 0.0f : weightage);
        final boolean isCyclic = this.isAcendant(graphBean.getChild().getId(), graphBean.getParent().getId());
        if (isCyclic) {
            throw new IllegalArgumentException("Cyclic reference");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        if (type.equalsIgnoreCase(KpiRelationshipParams.CASCADE.name())) {
            for (final KpiGraph.CascadedFrom obj : child.getCascadeParent()) {
                final KpiGraph.Kpi pObj = obj.getParent();
                if (pObj != null && pObj.getKpiId() != parent.getKpiId()) {
                    child.removeCascadeParent(obj);
                }
            }
            final KpiGraph.CascadedFrom cPar = child.addCascadeParent(parent);
            cPar.setWeightage(weightage);
            cPar.setType(type);
        }
        else if (type.equalsIgnoreCase(KpiRelationshipParams.TRANSLATE.name())) {
            final KpiGraph.TranslatedFrom tPar = child.addTranslateParent(parent);
            tPar.setType(type);
        }
        else if (type.equalsIgnoreCase(KpiRelationshipParams.ORIGIN.name())) {
            final KpiGraph.OriginatedFrom oPar = child.addOriginParent(parent);
            oPar.setType(type);
        }
        else {
            if (!type.equalsIgnoreCase(KpiRelationshipParams.GROUP.name())) {
                throw new IllegalArgumentException("Illegal type");
            }
            for (final KpiGraph.GroupParent obj2 : child.getGroupParent()) {
                final KpiGraph.Kpi pObj = obj2.getParent();
                if (pObj != null && pObj.getKpiId() != parent.getKpiId()) {
                    child.removeGroupParent(obj2);
                }
            }
            final KpiGraph.GroupParent oPar2 = child.addGroupParent(parent);
            oPar2.setType(type);
            oPar2.setWeightage(weightage);
        }
    }
    
    public void removeParent(final Integer pId, final Integer chId) {
        this.removeParent(pId, chId, KpiRelationshipParams.CASCADE.name());
        this.removeParent(pId, chId, KpiRelationshipParams.TRANSLATE.name());
        this.removeParent(pId, chId, KpiRelationshipParams.ORIGIN.name());
        this.removeParent(pId, chId, KpiRelationshipParams.GROUP.name());
    }
    
    public void removeParent(final Integer pId, final Integer chId, final String type) {
        if (pId == chId) {
            throw new IllegalArgumentException("Parent and Child can't be same");
        }
        final KpiGraph.Kpi child = (KpiGraph.Kpi)this.get(chId, (Class)KpiGraph.Kpi.class);
        final KpiGraph.Kpi parent = (KpiGraph.Kpi)this.get(pId, (Class)KpiGraph.Kpi.class);
        if (type.equalsIgnoreCase(KpiRelationshipParams.CASCADE.name())) {
            for (final KpiGraph.CascadedFrom obj : child.getCascadeParent()) {
                child.removeCascadeParent(obj);
            }
        }
        else if (type.equalsIgnoreCase(KpiRelationshipParams.TRANSLATE.name())) {
            for (final KpiGraph.TranslatedFrom obj2 : child.getTranslateParents()) {
                if (obj2.getParent().getKpiId() == parent.getKpiId()) {
                    child.removeTranslateParent(obj2);
                }
            }
        }
        else if (type.equalsIgnoreCase(KpiRelationshipParams.ORIGIN.name())) {
            for (final KpiGraph.OriginatedFrom obj3 : child.getOriginParents()) {
                if (obj3.getParent().getKpiId() == parent.getKpiId()) {
                    child.removeOriginParent(obj3);
                }
            }
        }
        else {
            if (!type.equalsIgnoreCase(KpiRelationshipParams.GROUP.name())) {
                throw new IllegalArgumentException("Illegal type");
            }
            for (final KpiGraph.GroupParent obj4 : child.getGroupParent()) {
                if (obj4.getParent().getKpiId() == parent.getKpiId()) {
                    child.removeGroupParent(obj4);
                }
            }
        }
    }
    
    public List<KpiGraph.Kpi> getAscendants(final Integer id, int depth) {
        if (depth == -1) {
            depth = 100;
        }
        final KpiGraph.Kpi node = (KpiGraph.Kpi)this.get(id, (Class)KpiGraph.Kpi.class);
        final List<KpiGraph.Kpi> result = new ArrayList<KpiGraph.Kpi>();
        this.getAscendants(node, result, depth, KpiRelationshipParams.CASCADE.name());
        this.getAscendants(node, result, depth, KpiRelationshipParams.TRANSLATE.name());
        this.getAscendants(node, result, depth, KpiRelationshipParams.ORIGIN.name());
        this.getAscendants(node, result, depth, KpiRelationshipParams.GROUP.name());
        return result;
    }
    
    public List<KpiGraph.Kpi> getAscendants(final Integer id, final String type, int depth) {
        if (depth == -1) {
            depth = 100;
        }
        final KpiGraph.Kpi node = (KpiGraph.Kpi)this.get(id, (Class)KpiGraph.Kpi.class);
        final List<KpiGraph.Kpi> result = new ArrayList<KpiGraph.Kpi>();
        this.getAscendants(node, result, depth, type);
        return result;
    }
    
    public void getAscendants(final KpiGraph.Kpi node, final List<KpiGraph.Kpi> result, int depth, final String type) {
        if (depth == -1) {
            depth = 100;
        }
        final List<KpiGraph.Kpi> recurse = new ArrayList<KpiGraph.Kpi>();
        if (type.equalsIgnoreCase(KpiRelationshipParams.CASCADE.name())) {
            for (final KpiGraph.CascadedFrom obj : node.getCascadeParent()) {
                if (!result.contains(obj.getParent())) {
                    result.add(obj.getParent());
                    recurse.add(obj.getParent());
                }
            }
        }
        else if (type.equalsIgnoreCase(KpiRelationshipParams.TRANSLATE.name())) {
            for (final KpiGraph.TranslatedFrom obj2 : node.getTranslateParents()) {
                if (!result.contains(obj2.getParent())) {
                    result.add(obj2.getParent());
                    recurse.add(obj2.getParent());
                }
            }
        }
        else if (type.equalsIgnoreCase(KpiRelationshipParams.ORIGIN.name())) {
            for (final KpiGraph.OriginatedFrom obj3 : node.getOriginParents()) {
                if (!result.contains(obj3.getParent())) {
                    result.add(obj3.getParent());
                    recurse.add(obj3.getParent());
                }
            }
        }
        else {
            if (!type.equalsIgnoreCase(KpiRelationshipParams.GROUP.name())) {
                throw new IllegalArgumentException("Illegal type");
            }
            for (final KpiGraph.GroupParent obj4 : node.getGroupParent()) {
                if (!result.contains(obj4.getParent())) {
                    result.add(obj4.getParent());
                    recurse.add(obj4.getParent());
                }
            }
        }
        if (depth > 1) {
            --depth;
            for (final KpiGraph.Kpi kpi : recurse) {
                this.getAscendants(kpi, result, depth, type);
            }
        }
    }
    
    public Graph getAscendantsGraph(final Integer id, int depth) {
        if (depth == -1) {
            depth = 100;
        }
        final KpiGraph.Kpi node = (KpiGraph.Kpi)this.get(id, (Class)KpiGraph.Kpi.class);
        final Graph graph = (Graph)new TinkerGraph();
        this.getAscendantsGraph(node, graph, depth, KpiRelationshipParams.CASCADE.name());
        this.getAscendantsGraph(node, graph, depth, KpiRelationshipParams.TRANSLATE.name());
        this.getAscendantsGraph(node, graph, depth, KpiRelationshipParams.ORIGIN.name());
        this.getAscendantsGraph(node, graph, depth, KpiRelationshipParams.GROUP.name());
        return graph;
    }
    
    public Graph getAscendantsGraph(final Integer id, final String type, int depth) {
        if (depth == -1) {
            depth = 100;
        }
        final KpiGraph.Kpi node = (KpiGraph.Kpi)this.get(id, (Class)KpiGraph.Kpi.class);
        final Graph graph = (Graph)new TinkerGraph();
        this.getAscendantsGraph(node, graph, depth, type);
        return graph;
    }
    
    public void getAscendantsGraph(final KpiGraph.Kpi node, final Graph graph, int depth, final String type) {
        if (depth == -1) {
            depth = 100;
        }
        Vertex child = graph.getVertex((Object)node.getKpiId());
        Vertex parent = null;
        if (child == null) {
            child = graph.addVertex((Object)node.getKpiId());
            child.setProperty("kid", (Object)node.getKpiId());
            child.setProperty("kname", (Object)node.getKpiName());
            child.setProperty("kcode", (Object)node.getKpiCode());
            child.setProperty("kweightage", (Object)node.getKpiWeightage());
        }
        final List<KpiGraph.Kpi> recurse = new ArrayList<KpiGraph.Kpi>();
        if (type.equalsIgnoreCase(KpiRelationshipParams.CASCADE.name())) {
            for (final KpiGraph.CascadedFrom obj : node.getCascadeParent()) {
                parent = graph.getVertex((Object)obj.getParent().getKpiId());
                if (parent == null) {
                    parent = graph.addVertex((Object)obj.getParent().getKpiId());
                    final KpiGraph.Kpi kpi = (KpiGraph.Kpi)this.get(obj.getParent().getKpiId(), (Class)KpiGraph.Kpi.class);
                    parent.setProperty("kid", (Object)kpi.getKpiId());
                    parent.setProperty("kname", (Object)kpi.getKpiName());
                    parent.setProperty("kcode", (Object)kpi.getKpiCode());
                    parent.setProperty("kweightage", (Object)kpi.getKpiWeightage());
                }
                final Edge rel = graph.addEdge((Object)null, child, parent, type);
                rel.setProperty("type", (Object)obj.getType());
                rel.setProperty("weightage", (Object)obj.getWeightage());
                recurse.add(obj.getParent());
            }
        }
        else if (type.equalsIgnoreCase(KpiRelationshipParams.TRANSLATE.name())) {
            for (final KpiGraph.TranslatedFrom obj2 : node.getTranslateParents()) {
                parent = graph.getVertex((Object)obj2.getParent().getKpiId());
                if (parent == null) {
                    parent = graph.addVertex((Object)obj2.getParent().getKpiId());
                    final KpiGraph.Kpi kpi = (KpiGraph.Kpi)this.get(obj2.getParent().getKpiId(), (Class)KpiGraph.Kpi.class);
                    parent.setProperty("kid", (Object)kpi.getKpiId());
                    parent.setProperty("kname", (Object)kpi.getKpiName());
                    parent.setProperty("kcode", (Object)kpi.getKpiCode());
                    parent.setProperty("kweightage", (Object)kpi.getKpiWeightage());
                }
                final Edge rel = graph.addEdge((Object)null, child, parent, type);
                rel.setProperty("type", (Object)obj2.getType());
                recurse.add(obj2.getParent());
            }
        }
        else if (type.equalsIgnoreCase(KpiRelationshipParams.ORIGIN.name())) {
            for (final KpiGraph.OriginatedFrom obj3 : node.getOriginParents()) {
                parent = graph.getVertex((Object)obj3.getParent().getKpiId());
                if (parent == null) {
                    parent = graph.addVertex((Object)obj3.getParent().getKpiId());
                    final KpiGraph.Kpi kpi = (KpiGraph.Kpi)this.get(obj3.getParent().getKpiId(), (Class)KpiGraph.Kpi.class);
                    parent.setProperty("kid", (Object)kpi.getKpiId());
                    parent.setProperty("kname", (Object)kpi.getKpiName());
                    parent.setProperty("kcode", (Object)kpi.getKpiCode());
                    parent.setProperty("kweightage", (Object)kpi.getKpiWeightage());
                }
                final Edge rel = graph.addEdge((Object)null, child, parent, type);
                rel.setProperty("type", (Object)obj3.getType());
                recurse.add(obj3.getParent());
            }
        }
        else {
            if (!type.equalsIgnoreCase(KpiRelationshipParams.GROUP.name())) {
                throw new IllegalArgumentException("Illegal type");
            }
            for (final KpiGraph.GroupParent obj4 : node.getGroupParent()) {
                parent = graph.getVertex((Object)obj4.getParent().getKpiId());
                if (parent == null) {
                    parent = graph.addVertex((Object)obj4.getParent().getKpiId());
                    final KpiGraph.Kpi kpi = (KpiGraph.Kpi)this.get(obj4.getParent().getKpiId(), (Class)KpiGraph.Kpi.class);
                    parent.setProperty("kid", (Object)kpi.getKpiId());
                    parent.setProperty("kname", (Object)kpi.getKpiName());
                    parent.setProperty("kcode", (Object)kpi.getKpiCode());
                    parent.setProperty("kweightage", (Object)kpi.getKpiWeightage());
                }
                final Edge rel = graph.addEdge((Object)null, child, parent, type);
                rel.setProperty("type", (Object)obj4.getType());
                rel.setProperty("weightage", (Object)obj4.getWeightage());
                recurse.add(obj4.getParent());
            }
        }
        if (depth > 1) {
            --depth;
            for (final KpiGraph.Kpi kpi2 : recurse) {
                this.getAscendantsGraph(kpi2, graph, depth, type);
            }
        }
    }
    
    public List<KpiGraph.Kpi> getDescendants(final Integer id, int depth) {
        if (depth == -1) {
            depth = 100;
        }
        final KpiGraph.Kpi node = (KpiGraph.Kpi)this.get(id, (Class)KpiGraph.Kpi.class);
        final List<KpiGraph.Kpi> result = new ArrayList<KpiGraph.Kpi>();
        this.getDescendants(node, result, KpiRelationshipParams.CASCADE.name(), depth);
        this.getDescendants(node, result, KpiRelationshipParams.TRANSLATE.name(), depth);
        this.getDescendants(node, result, KpiRelationshipParams.ORIGIN.name(), depth);
        this.getDescendants(node, result, KpiRelationshipParams.GROUP.name(), depth);
        return result;
    }
    
    public List<KpiGraph.Kpi> getDescendants(final Integer id, final String type, int depth) {
        if (depth == -1) {
            depth = 100;
        }
        final KpiGraph.Kpi node = (KpiGraph.Kpi)this.get(id, (Class)KpiGraph.Kpi.class);
        final List<KpiGraph.Kpi> result = new ArrayList<KpiGraph.Kpi>();
        this.getDescendants(node, result, type, depth);
        return result;
    }
    
    public void getDescendants(final KpiGraph.Kpi node, final List<KpiGraph.Kpi> result, final String type, int depth) {
        if (depth == -1) {
            depth = 100;
        }
        final List<KpiGraph.Kpi> recurse = new ArrayList<KpiGraph.Kpi>();
        if (type.equalsIgnoreCase(KpiRelationshipParams.CASCADE.name())) {
            for (final KpiGraph.CascadedTo obj : node.getCascadedChildren()) {
                if (!result.contains(obj.getChildren())) {
                    result.add(obj.getChildren());
                    recurse.add(obj.getChildren());
                }
            }
        }
        else if (type.equalsIgnoreCase(KpiRelationshipParams.TRANSLATE.name())) {
            for (final KpiGraph.TranslatedTo obj2 : node.getTranslatedChildren()) {
                if (!result.contains(obj2.getChildren())) {
                    result.add(obj2.getChildren());
                    recurse.add(obj2.getChildren());
                }
            }
        }
        else if (type.equalsIgnoreCase(KpiRelationshipParams.ORIGIN.name())) {
            for (final KpiGraph.OriginatedTo obj3 : node.getOriginatedChildren()) {
                if (!result.contains(obj3.getChildren())) {
                    result.add(obj3.getChildren());
                    recurse.add(obj3.getChildren());
                }
            }
        }
        else {
            if (!type.equalsIgnoreCase(KpiRelationshipParams.GROUP.name())) {
                throw new IllegalArgumentException("Illegal type");
            }
            for (final KpiGraph.GroupParentTo obj4 : node.getGroupChildren()) {
                if (!result.contains(obj4.getChildren())) {
                    result.add(obj4.getChildren());
                    recurse.add(obj4.getChildren());
                }
            }
        }
        if (depth > 1) {
            --depth;
            for (final KpiGraph.Kpi kpi : recurse) {
                this.getDescendants(kpi, result, type, depth);
            }
        }
    }
    
    public Graph getDescendantsGraph(final Integer id, int depth) {
        if (depth == -1) {
            depth = 100;
        }
        final KpiGraph.Kpi node = (KpiGraph.Kpi)this.get(id, (Class)KpiGraph.Kpi.class);
        final Graph graph = (Graph)new TinkerGraph();
        this.getDescendantsGraph(node, graph, depth, KpiRelationshipParams.CASCADE.name());
        this.getDescendantsGraph(node, graph, depth, KpiRelationshipParams.TRANSLATE.name());
        this.getDescendantsGraph(node, graph, depth, KpiRelationshipParams.ORIGIN.name());
        this.getDescendantsGraph(node, graph, depth, KpiRelationshipParams.GROUP.name());
        return graph;
    }
    
    public Graph getDescendantsGraph(final Integer id, final String type, int depth) {
        if (depth == -1) {
            depth = 100;
        }
        final KpiGraph.Kpi node = (KpiGraph.Kpi)this.get(id, (Class)KpiGraph.Kpi.class);
        final Graph graph = (Graph)new TinkerGraph();
        this.getDescendantsGraph(node, graph, depth, type);
        return graph;
    }
    
    public void getDescendantsGraph(final KpiGraph.Kpi node, final Graph graph, int depth, final String type) {
        if (depth == -1) {
            depth = 100;
        }
        Vertex parent = graph.getVertex((Object)node.getKpiId());
        Vertex child = null;
        if (parent == null) {
            parent = graph.addVertex((Object)node.getKpiId());
            parent.setProperty("kid", (Object)node.getKpiId());
            parent.setProperty("kname", (Object)node.getKpiName());
            parent.setProperty("kcode", (Object)node.getKpiCode());
            parent.setProperty("kweightage", (Object)node.getKpiWeightage());
        }
        final List<KpiGraph.Kpi> recurse = new ArrayList<KpiGraph.Kpi>();
        if (type.equalsIgnoreCase(KpiRelationshipParams.CASCADE.name())) {
            for (final KpiGraph.CascadedTo obj : node.getCascadedChildren()) {
                child = graph.getVertex((Object)obj.getChildren().getKpiId());
                if (child == null) {
                    child = graph.addVertex((Object)obj.getChildren().getKpiId());
                    final KpiGraph.Kpi kpi = (KpiGraph.Kpi)this.get(obj.getChildren().getKpiId(), (Class)KpiGraph.Kpi.class);
                    child.setProperty("kid", (Object)kpi.getKpiId());
                    child.setProperty("kname", (Object)kpi.getKpiName());
                    child.setProperty("kcode", (Object)kpi.getKpiCode());
                    child.setProperty("kweightage", (Object)kpi.getKpiWeightage());
                }
                final Edge rel = graph.addEdge((Object)null, parent, child, type);
                rel.setProperty("type", (Object)obj.getType());
                rel.setProperty("weightage", (Object)obj.getWeightage());
                recurse.add(obj.getChildren());
            }
        }
        else if (type.equalsIgnoreCase(KpiRelationshipParams.TRANSLATE.name())) {
            for (final KpiGraph.TranslatedTo obj2 : node.getTranslatedChildren()) {
                child = graph.getVertex((Object)obj2.getChildren().getKpiId());
                if (child == null) {
                    child = graph.addVertex((Object)obj2.getChildren().getKpiId());
                    final KpiGraph.Kpi kpi = (KpiGraph.Kpi)this.get(obj2.getChildren().getKpiId(), (Class)KpiGraph.Kpi.class);
                    child.setProperty("kid", (Object)kpi.getKpiId());
                    child.setProperty("kname", (Object)kpi.getKpiName());
                    child.setProperty("kcode", (Object)kpi.getKpiCode());
                    child.setProperty("kweightage", (Object)kpi.getKpiWeightage());
                }
                final Edge rel = graph.addEdge((Object)null, parent, child, type);
                rel.setProperty("type", (Object)obj2.getType());
                recurse.add(obj2.getChildren());
            }
        }
        else if (type.equalsIgnoreCase(KpiRelationshipParams.ORIGIN.name())) {
            for (final KpiGraph.OriginatedTo obj3 : node.getOriginatedChildren()) {
                child = graph.getVertex((Object)obj3.getChildren().getKpiId());
                if (child == null) {
                    child = graph.addVertex((Object)obj3.getChildren().getKpiId());
                    final KpiGraph.Kpi kpi = (KpiGraph.Kpi)this.get(obj3.getChildren().getKpiId(), (Class)KpiGraph.Kpi.class);
                    child.setProperty("kid", (Object)kpi.getKpiId());
                    child.setProperty("kname", (Object)kpi.getKpiName());
                    child.setProperty("kcode", (Object)kpi.getKpiCode());
                    child.setProperty("kweightage", (Object)kpi.getKpiWeightage());
                }
                final Edge rel = graph.addEdge((Object)null, parent, child, type);
                rel.setProperty("type", (Object)obj3.getType());
                recurse.add(obj3.getChildren());
            }
        }
        else {
            if (!type.equalsIgnoreCase(KpiRelationshipParams.GROUP.name())) {
                throw new IllegalArgumentException("Illegal type");
            }
            for (final KpiGraph.GroupParentTo obj4 : node.getGroupChildren()) {
                child = graph.getVertex((Object)obj4.getChildren().getKpiId());
                if (child == null) {
                    child = graph.addVertex((Object)obj4.getChildren().getKpiId());
                    final KpiGraph.Kpi kpi = (KpiGraph.Kpi)this.get(obj4.getChildren().getKpiId(), (Class)KpiGraph.Kpi.class);
                    child.setProperty("kid", (Object)kpi.getKpiId());
                    child.setProperty("kname", (Object)kpi.getKpiName());
                    child.setProperty("kcode", (Object)kpi.getKpiCode());
                    child.setProperty("kweightage", (Object)kpi.getKpiWeightage());
                }
                final Edge rel = graph.addEdge((Object)null, parent, child, type);
                rel.setProperty("type", (Object)obj4.getType());
                rel.setProperty("weightage", (Object)obj4.getWeightage());
                recurse.add(obj4.getChildren());
            }
        }
        if (depth > 1) {
            --depth;
            for (final KpiGraph.Kpi kpi2 : recurse) {
                this.getDescendantsGraph(kpi2, graph, depth, type);
            }
        }
    }
    
    public void getParents(final Integer id, List<KpiGraph.Kpi> result, final String type) {
        final KpiGraph.Kpi node = (KpiGraph.Kpi)this.get(id, (Class)KpiGraph.Kpi.class);
        if (result == null) {
            result = new ArrayList<KpiGraph.Kpi>();
        }
        if (type.equalsIgnoreCase(KpiRelationshipParams.CASCADE.name())) {
            for (final KpiGraph.CascadedFrom obj : node.getCascadeParent()) {
                if (!result.contains(obj.getParent())) {
                    result.add(obj.getParent());
                }
            }
        }
        else if (type.equalsIgnoreCase(KpiRelationshipParams.TRANSLATE.name())) {
            for (final KpiGraph.TranslatedFrom obj2 : node.getTranslateParents()) {
                if (!result.contains(obj2.getParent())) {
                    result.add(obj2.getParent());
                }
            }
        }
        else if (type.equalsIgnoreCase(KpiRelationshipParams.ORIGIN.name())) {
            for (final KpiGraph.OriginatedFrom obj3 : node.getOriginParents()) {
                if (!result.contains(obj3.getParent())) {
                    result.add(obj3.getParent());
                }
            }
        }
        else {
            if (!type.equalsIgnoreCase(KpiRelationshipParams.GROUP.name())) {
                throw new IllegalArgumentException("Illegal type");
            }
            for (final KpiGraph.GroupParent obj4 : node.getGroupParent()) {
                if (!result.contains(obj4.getParent())) {
                    result.add(obj4.getParent());
                }
            }
        }
    }
    
    public List<KpiGraph.Kpi> getParents(final Integer id, final String type) {
        final List<KpiGraph.Kpi> result = new ArrayList<KpiGraph.Kpi>();
        this.getParents(id, result, type);
        return result;
    }
    
    public List<KpiGraph.Kpi> getParents(final Integer id) {
        final List<KpiGraph.Kpi> result = new ArrayList<KpiGraph.Kpi>();
        this.getParents(id, result, KpiRelationshipParams.CASCADE.name());
        this.getParents(id, result, KpiRelationshipParams.TRANSLATE.name());
        this.getParents(id, result, KpiRelationshipParams.ORIGIN.name());
        this.getParents(id, result, KpiRelationshipParams.GROUP.name());
        return result;
    }
    
    public void getChildren(final Integer id, List<KpiGraph.Kpi> result, final String type) {
        final KpiGraph.Kpi node = (KpiGraph.Kpi)this.get(id, (Class)KpiGraph.Kpi.class);
        if (result == null) {
            result = new ArrayList<KpiGraph.Kpi>();
        }
        if (type.equalsIgnoreCase(KpiRelationshipParams.CASCADE.name())) {
            for (final KpiGraph.CascadedTo obj : node.getCascadedChildren()) {
                if (!result.contains(obj.getChildren())) {
                    result.add(obj.getChildren());
                }
            }
        }
        else if (type.equalsIgnoreCase(KpiRelationshipParams.TRANSLATE.name())) {
            for (final KpiGraph.TranslatedTo obj2 : node.getTranslatedChildren()) {
                if (!result.contains(obj2.getChildren())) {
                    result.add(obj2.getChildren());
                }
            }
        }
        else if (type.equalsIgnoreCase(KpiRelationshipParams.ORIGIN.name())) {
            for (final KpiGraph.OriginatedTo obj3 : node.getOriginatedChildren()) {
                if (!result.contains(obj3.getChildren())) {
                    result.add(obj3.getChildren());
                }
            }
        }
        else {
            if (!type.equalsIgnoreCase(KpiRelationshipParams.GROUP.name())) {
                throw new IllegalArgumentException("Illegal type");
            }
            for (final KpiGraph.GroupParentTo obj4 : node.getGroupChildren()) {
                if (!result.contains(obj4.getChildren())) {
                    result.add(obj4.getChildren());
                }
            }
        }
    }
    
    public List<KpiGraph.Kpi> getChildren(final Integer id, final String type) {
        final List<KpiGraph.Kpi> result = new ArrayList<KpiGraph.Kpi>();
        this.getChildren(id, result, type);
        return result;
    }
    
    public List<KpiGraph.Kpi> getChildren(final Integer id) {
        final List<KpiGraph.Kpi> result = new ArrayList<KpiGraph.Kpi>();
        this.getChildren(id, result, KpiRelationshipParams.CASCADE.name());
        this.getChildren(id, result, KpiRelationshipParams.TRANSLATE.name());
        this.getChildren(id, result, KpiRelationshipParams.ORIGIN.name());
        this.getChildren(id, result, KpiRelationshipParams.GROUP.name());
        return result;
    }
    
    public boolean isAcendant(final Integer pId, final Integer cId, final String type) {
        final List<KpiGraph.Kpi> ascs = this.getAscendants(cId, type, 100);
        final KpiGraph.Kpi kpi = (KpiGraph.Kpi)this.get(pId, (Class)KpiGraph.Kpi.class);
        return ascs.contains(kpi);
    }
    
    public boolean isDescendant(final Integer pId, final Integer cId, final String type) {
        final List<KpiGraph.Kpi> descs = this.getDescendants(pId, type, 100);
        final KpiGraph.Kpi subKpi = (KpiGraph.Kpi)this.get(cId, (Class)KpiGraph.Kpi.class);
        return descs.contains(subKpi);
    }
    
    public boolean isAcendant(final Integer pId, final Integer cId) {
        final List<KpiGraph.Kpi> ascs = this.getAscendants(cId, 100);
        final KpiGraph.Kpi kpi = (KpiGraph.Kpi)this.get(pId, (Class)KpiGraph.Kpi.class);
        return ascs.contains(kpi);
    }
    
    public boolean isDescendant(final Integer pId, final Integer cId) {
        final List<KpiGraph.Kpi> descs = this.getDescendants(pId, 100);
        final KpiGraph.Kpi subKpi = (KpiGraph.Kpi)this.get(cId, (Class)KpiGraph.Kpi.class);
        return descs.contains(subKpi);
    }
    
    public boolean isAvailable(final Integer id) {
        try {
            final KpiGraph.Kpi subKpi = (KpiGraph.Kpi)this.get(id, (Class)KpiGraph.Kpi.class);
            return id.equals(subKpi.getKpiId());
        }
        catch (Exception e) {
            return false;
        }
    }
}
