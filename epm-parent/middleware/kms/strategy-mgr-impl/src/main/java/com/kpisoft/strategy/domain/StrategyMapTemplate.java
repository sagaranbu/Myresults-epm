package com.kpisoft.strategy.domain;

import com.canopus.mw.*;
import com.kpisoft.strategy.dac.*;
import com.kpisoft.strategy.vo.*;
import com.kpisoft.strategy.params.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.utils.*;
import javax.validation.*;

public class StrategyMapTemplate extends BaseDomainObject
{
    private StrategyMapTemplateManager objStrategyMapManager;
    private StrategyTemplateDataService objStrategyTemplateDataService;
    private StrategyTemplateVo objStrategyTemplateVo;
    
    public StrategyMapTemplate(final StrategyMapTemplateManager objStrategyMapManager, final StrategyTemplateDataService objSystemStrategyTemplateDataService) {
        this.objStrategyMapManager = objStrategyMapManager;
        this.objStrategyTemplateDataService = objSystemStrategyTemplateDataService;
    }
    
    public Identifier save(final StrategyTemplateVo strategyTemplateVo) {
        this.validate(strategyTemplateVo);
        final Request request = new Request();
        request.put(StrategyParams.STRATEGY_TEMPLATE.getParamName(), (BaseValueObject)strategyTemplateVo);
        final Response response = this.objStrategyTemplateDataService.saveStrategyTemplate(request);
        final Identifier identifier = (Identifier)response.get(StrategyParams.STRATEGY_TEMPLATE_ID.getParamName());
        return identifier;
    }
    
    public boolean deleteStrategyTemplate(final int id) {
        final Identifier objIdentifier = new Identifier();
        objIdentifier.setId(id);
        final Request request = new Request();
        request.put(StrategyParams.STRATEGY_TEMPLATE.getParamName(), (BaseValueObject)objIdentifier);
        final Response response = this.objStrategyTemplateDataService.deleteStrategyTemplate(request);
        final BooleanResponse booleanResponse = (BooleanResponse)response.get(StrategyParams.STRATEGY_TEMPLATE.getParamName());
        return booleanResponse.isResponse();
    }
    
    public void validate(final Object obj) {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), obj, "ERR_VAL_INVALID_INPUT", "Invalid strategy template details");
    }
    
    private Validator getValidator() {
        return this.objStrategyMapManager.getValidator();
    }
    
    public StrategyTemplateVo getObjStrategyTemplateVo() {
        return this.objStrategyTemplateVo;
    }
    
    public void setObjStrategyTemplateVo(final StrategyTemplateVo objStrategyTemplateVo) {
        this.objStrategyTemplateVo = objStrategyTemplateVo;
    }
}
