package com.canopus.entity.impl;

import com.canopus.entity.*;
import org.springframework.stereotype.*;
import com.canopus.dac.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.aggregation.*;
import com.canopus.mw.*;
import com.canopus.event.mgr.vo.params.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import org.modelmapper.*;
import java.util.*;
import com.canopus.mw.dto.*;
import java.lang.reflect.*;

@Component
public class AggregationRuleDataServiceImpl extends BaseDataAccessService implements AggregationRuleDataService
{
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    
    public AggregationRuleDataServiceImpl() {
        this.genericDao = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)AggregationRule.class, (Class)com.canopus.entity.domain.AggregationRule.class);
    }
    
    @Transactional(readOnly = true)
    public Response getAggregationRule(final Request request) {
        final Identifier id = (Identifier)request.get(AggregationParams.AGG_RULE_IDENTIFIER.name());
        com.canopus.entity.domain.AggregationRule aggregationRule = null;
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException("agg-rule-err-001", "Invalid data in request"));
        }
        try {
            aggregationRule = (com.canopus.entity.domain.AggregationRule)this.genericDao.find((Class)com.canopus.entity.domain.AggregationRule.class, (Serializable)id.getId());
            final AggregationRule aggregationRuleVo = new AggregationRule();
            if (aggregationRule == null) {
                return this.ERROR((Exception)new DataAccessException("agg-rule-err-002", "Aggregation Rule Does not Exist"));
            }
            this.modelMapper.map((Object)aggregationRule, (Object)aggregationRuleVo);
            return this.OK(AggregationParams.AGGREGATION_RULE.name(), (BaseValueObject)aggregationRuleVo);
        }
        catch (Exception e) {
            return this.ERROR((Exception)new DataAccessException("agg-rule-err-002", "Unknown error while getting orgSummary", (Throwable)e));
        }
    }
    
    @Transactional
    public Response saveAggregationRule(final Request request) {
        AggregationRule aggregationRuleVo = null;
        aggregationRuleVo = (AggregationRule)request.get(AggregationParams.AGGREGATION_RULE.name());
        if (aggregationRuleVo == null) {
            return this.ERROR((Exception)new DataAccessException("agg-rule-err-001", "Invalid data in request"));
        }
        try {
            com.canopus.entity.domain.AggregationRule aggregationRuleDomain;
            if (aggregationRuleVo.getId() != null && aggregationRuleVo.getId() > 0) {
                aggregationRuleDomain = (com.canopus.entity.domain.AggregationRule)this.genericDao.find((Class)com.canopus.entity.domain.AggregationRule.class, (Serializable)aggregationRuleVo.getId());
            }
            else {
                aggregationRuleDomain = new com.canopus.entity.domain.AggregationRule();
            }
            this.modelMapper.map((Object)aggregationRuleVo, (Object)aggregationRuleDomain);
            this.genericDao.save((Object)aggregationRuleDomain);
            return this.OK(AggregationParams.AGG_RULE_IDENTIFIER.name(), (BaseValueObject)new Identifier(aggregationRuleVo.getId()));
        }
        catch (Exception e) {
            return this.ERROR((Exception)new DataAccessException("agg-rule-err-002", "Unknown error while getting orgSummary", (Throwable)e));
        }
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response deleteAggregationRule(final Request request) {
        return null;
    }
    
    @Transactional(readOnly = true)
    public Response getAllAggregationRules(final Request request) {
        try {
            final List<com.canopus.entity.domain.AggregationRule> ldomai = (List<com.canopus.entity.domain.AggregationRule>)this.genericDao.findAll((Class)com.canopus.entity.domain.AggregationRule.class);
            System.err.println(ldomai);
            final Type listType = new TypeToken<List<AggregationRule>>() {}.getType();
            final List<AggregationRule> lvo = (List<AggregationRule>)this.modelMapper.map((Object)ldomai, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)lvo);
            return this.OK(AggregationParams.AGGREGATION_RULES.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            e.printStackTrace();
            return this.ERROR((Exception)new DataAccessException("agg-rule-err-002", "Unknown error while getting orgSummary", (Throwable)e));
        }
    }
}
