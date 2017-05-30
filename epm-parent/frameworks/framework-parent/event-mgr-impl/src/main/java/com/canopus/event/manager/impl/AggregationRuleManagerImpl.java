package com.canopus.event.manager.impl;

import com.canopus.mw.aggregation.*;
import org.springframework.stereotype.*;
import com.canopus.entity.*;
import com.canopus.mw.cache.*;
import com.canopus.event.mgr.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.*;
import java.util.*;
import javax.annotation.*;
import com.canopus.event.mgr.vo.params.*;
import com.canopus.mw.dto.*;

@Component
public class AggregationRuleManagerImpl extends BaseDomainManager implements AggregationRuleManager, CacheLoader<Integer, AggregationRule>
{
    @Autowired
    private AggregationRuleDataService aggregationRuleDataService;
    @Autowired
    @Qualifier("aggregationRuleCatche")
    private Cache<Integer, AggregationRule> cache;
    @Value("#{eventInvocationMap}")
    private Map<String, List<IEventListenerInvokationHandler>> eventInvocationMap;
    @Autowired
    IServiceLocator serviceLocator;
    
    public AggregationRuleManagerImpl() {
        this.cache = null;
    }
    
    @PostConstruct
    public void init() {
        ExecutionContext.getCurrent().setCrossTenant();
        final List<AggregationRule> aggregationRules = this.getAllAggregationRules();
        ExecutionContext.getCurrent().unSetCrossTenant();
        IEventListenerInvokationHandler handler = null;
        if (aggregationRules != null) {
            for (final AggregationRule aggregationRule : aggregationRules) {
                handler = (IEventListenerInvokationHandler)new AggregationEventListenerInvokationHandler(aggregationRule, this.serviceLocator, "aggregation", aggregationRule.getOriginMechanism());
                List<IEventListenerInvokationHandler> eventListenerInvokationHandlers = this.eventInvocationMap.get(aggregationRule.getOriginId().trim());
                if (eventListenerInvokationHandlers == null) {
                    eventListenerInvokationHandlers = new ArrayList<IEventListenerInvokationHandler>();
                    this.eventInvocationMap.put(aggregationRule.getOriginId().trim(), eventListenerInvokationHandlers);
                }
                eventListenerInvokationHandlers.add(handler);
            }
        }
    }
    
    public AggregationRule getAggregationRule(final Integer id) {
        AggregationRule aggregationRule = null;
        aggregationRule = (AggregationRule)this.cache.get(id, (CacheLoader)this);
        if (aggregationRule == null) {
            final Request request = new Request();
            request.put(AggregationParams.AGG_RULE_IDENTIFIER.name(), (BaseValueObject)new Identifier(id));
            final Response response = this.aggregationRuleDataService.getAggregationRule(request);
            aggregationRule = (AggregationRule)response.get(AggregationParams.AGGREGATION_RULE.name());
            return aggregationRule;
        }
        return aggregationRule;
    }
    
    public Integer saveAggregationRule(final AggregationRule aggregationRule) {
        final Request request = new Request();
        request.put(AggregationParams.AGGREGATION_RULE.name(), (BaseValueObject)aggregationRule);
        final Response response = this.aggregationRuleDataService.saveAggregationRule(request);
        final Identifier identifier = (Identifier)response.get(AggregationParams.AGG_RULE_IDENTIFIER.name());
        return identifier.getId();
    }
    
    public List<AggregationRule> getAllAggregationRules() {
        final Response response = this.aggregationRuleDataService.getAllAggregationRules(new Request());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(AggregationParams.AGGREGATION_RULES.name());
        if (list != null) {
            final List actualList = list.getValueObjectList();
            return (List<AggregationRule>)actualList;
        }
        return null;
    }
    
    public AggregationRule load(final Integer key) {
        return null;
    }
}
