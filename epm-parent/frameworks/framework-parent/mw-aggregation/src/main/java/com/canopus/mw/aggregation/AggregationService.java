package com.canopus.mw.aggregation;

import com.canopus.mw.*;
import com.canopus.mw.spel.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.expression.spel.standard.*;
import org.springframework.expression.spel.support.*;
import org.springframework.expression.*;
import com.canopus.mw.events.*;
import java.io.*;
import com.canopus.mw.dto.*;
import java.lang.reflect.*;
import java.util.*;

public abstract class AggregationService extends BaseMiddlewareBean implements IAggregationService
{
    @Value("#{summaryManagersMap}")
    private Map<String, ISummaryManager> summaryManagersMap;
    @Value("#{functoidMap}")
    private Map<String, IExprFunctoid> functoidMap;
    @Autowired
    private MethodResolver methodResolver;
    private Set<ISummaryData> aggregatedSummries;
    private Set<Integer> aggregatedSummriesIds;
    
    public void performGraphAggregation(final List<Integer> parentIds, final AggregationRule aggregationRule, final ISummaryManager service, final Set<ISummaryData> aggregatedSummries, final Set<Integer> aggregatedSummriesIds) {
        parentIds.removeAll(aggregatedSummriesIds);
        final List<ISummaryData> summaryDataList = (List<ISummaryData>)service.getSummaryEntities((List)parentIds);
        if (!summaryDataList.isEmpty()) {
            for (final ISummaryData summaryData : summaryDataList) {
                aggregatedSummries.add(this.aggregate(summaryData, aggregationRule));
                aggregatedSummriesIds.add(summaryData.getId());
            }
            final List<Integer> parentsListNextLevel = (List<Integer>)service.getParentIds((List)parentIds);
            this.performGraphAggregation(parentsListNextLevel, aggregationRule, service, aggregatedSummries, aggregatedSummriesIds);
        }
    }
    
    public ISummaryData aggregate(final ISummaryData summaryData, final AggregationRule aggregationRule) {
        final ExpressionParser parser = (ExpressionParser)new SpelExpressionParser();
        final Expression expr = parser.parseExpression(aggregationRule.getAggregationOperation());
        final StandardEvaluationContext context = new StandardEvaluationContext((Object)summaryData);
        context.setVariable("root", (Object)summaryData);
        final ReflectiveMethodResolver reflectiveMethodResolver = new ReflectiveMethodResolver();
        context.addMethodResolver((MethodResolver)reflectiveMethodResolver);
        context.addMethodResolver(this.methodResolver);
        expr.getValue((EvaluationContext)context);
        return summaryData;
    }
    
    public Response aggregate(final Request request) {
        try {
            final AggregationRule aggregationRule = (AggregationRule)request.get("aggregationRule");
            final Object aggregationdataObject = request.get("aggregationData");
            MiddlewareEvent event = null;
            if (aggregationdataObject instanceof MiddlewareEventBean) {
                final MiddlewareEventBean eventBean = (MiddlewareEventBean)aggregationdataObject;
                event = (MiddlewareEvent)new ObjectInputStream(new ByteArrayInputStream(eventBean.getEventData())).readObject();
            }
            else if (aggregationdataObject instanceof MiddlewareEvent) {
                event = (MiddlewareEvent)aggregationdataObject;
            }
            if (event != null) {
                final Map<String, BaseValueObject> m = (Map<String, BaseValueObject>)event.getPayLoadMap();
                final Object data = m.get(aggregationRule.getConnectorKey());
                final Class<?> c = data.getClass();
                final Class<?>[] parms = (Class<?>[])new Class[0];
                final Method method = c.getDeclaredMethod(aggregationRule.getConnector(), parms);
                final Object summaryObjectList = method.invoke(data, (Object[])null);
                final ISummaryManager service = this.summaryManagersMap.get(aggregationRule.getTargetEntity());
                final ExpressionParser parser = (ExpressionParser)new SpelExpressionParser();
                final Expression expr = parser.parseExpression(aggregationRule.getAggregationOperation());
                if (summaryObjectList instanceof Collection) {
                    if (aggregationRule.isGraphOperation()) {
                        final List<ISummaryData> summaryDataList = (List<ISummaryData>)service.getSummaryEntities((List)summaryObjectList);
                        for (final ISummaryData summaryData : summaryDataList) {
                            final Set<ISummaryData> aggregatedSummries = new TreeSet<ISummaryData>();
                            final Set<Integer> aggregatedSummriesIds = new TreeSet<Integer>();
                            this.aggregate(summaryData, aggregationRule);
                            final List<Integer> ids = new ArrayList<Integer>();
                            ids.add(summaryData.getId());
                            final List<Integer> parentsListNextLevel = (List<Integer>)service.getParentIds((List)ids);
                            aggregatedSummries.add(summaryData);
                            aggregatedSummriesIds.add(summaryData.getId());
                            this.performGraphAggregation(parentsListNextLevel, aggregationRule, service, aggregatedSummries, aggregatedSummriesIds);
                            service.saveSummaryEntities((List)new ArrayList(aggregatedSummries));
                        }
                        System.out.println("saving aggregated data compleate---------");
                    }
                    else {
                        final List<ISummaryData> summaryEntities = (List<ISummaryData>)service.getSummaryEntities((List)summaryObjectList);
                        for (final ISummaryData summaryEntity : summaryEntities) {
                            final StandardEvaluationContext context = new StandardEvaluationContext((Object)summaryEntity);
                            context.setVariable("root", (Object)summaryEntity);
                            final ReflectiveMethodResolver reflectiveMethodResolver = new ReflectiveMethodResolver();
                            context.addMethodResolver((MethodResolver)reflectiveMethodResolver);
                            context.addMethodResolver(this.methodResolver);
                            expr.getValue((EvaluationContext)context);
                        }
                        service.saveSummaryEntities((List)summaryEntities);
                    }
                }
                else if (summaryObjectList instanceof Integer) {
                    final ISummaryData summaryEntity2 = service.getSummaryEntity((int)summaryObjectList);
                    final StandardEvaluationContext context2 = new StandardEvaluationContext((Object)summaryEntity2);
                    context2.setVariable("root", (Object)summaryEntity2);
                    final ReflectiveMethodResolver reflectiveMethodResolver2 = new ReflectiveMethodResolver();
                    context2.addMethodResolver((MethodResolver)reflectiveMethodResolver2);
                    context2.addMethodResolver(this.methodResolver);
                    expr.getValue((EvaluationContext)context2);
                    if (aggregationRule.isGraphOperation()) {
                        final Set<ISummaryData> aggregatedSummries = new TreeSet<ISummaryData>();
                        final Set<Integer> aggregatedSummriesIds = new TreeSet<Integer>();
                        final List<Integer> summrayIdList = new ArrayList<Integer>();
                        summrayIdList.add(summaryEntity2.getId());
                        final List<Integer> parentIds = (List<Integer>)service.getParentIds((List)summrayIdList);
                        this.performGraphAggregation(parentIds, aggregationRule, service, aggregatedSummries, aggregatedSummriesIds);
                        service.saveSummaryEntities((List)new ArrayList(aggregatedSummries));
                    }
                    else {
                        service.saveSummaryEntity(summaryEntity2);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return this.ERROR(e);
        }
        return this.OK();
    }
    
    public Response addSummaryDataService(final Request request) {
        final ISummaryManager summaryManager = (ISummaryManager)request.get("summaryDataService");
        if (this.summaryManagersMap == null) {
            this.summaryManagersMap = new HashMap<String, ISummaryManager>();
        }
        this.summaryManagersMap.put(summaryManager.getSummaryEntityName(), summaryManager);
        return this.OK();
    }
    
    public MethodResolver getMethodResolver() {
        return this.methodResolver;
    }
    
    public void setMethodResolver(final MethodResolver methodResolver) {
        this.methodResolver = methodResolver;
    }
    
    public Map<String, ISummaryManager> getSummaryDataServiceMap() {
        return this.summaryManagersMap;
    }
    
    public void setSummaryDataServiceMap(final Map<String, ISummaryManager> summaryDataServiceMap) {
        this.summaryManagersMap = summaryDataServiceMap;
    }
    
    public Map<String, IExprFunctoid> getFunctoidMap() {
        return this.functoidMap;
    }
    
    public void setFunctoidMap(final Map<String, IExprFunctoid> functoidMap) {
        this.functoidMap = functoidMap;
    }
}
