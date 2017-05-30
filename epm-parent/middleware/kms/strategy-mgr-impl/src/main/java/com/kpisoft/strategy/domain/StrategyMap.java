package com.kpisoft.strategy.domain;

import com.kpisoft.strategy.dac.*;
import com.kpisoft.strategy.vo.*;
import com.kpisoft.strategy.params.*;
import com.canopus.dac.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.*;
import com.canopus.mw.utils.*;
import javax.validation.*;

public class StrategyMap extends BaseDomainObject
{
    private StrategyMapManager objStrategyMapManager;
    public StrategyTemplateDataService objStrategyTemplateDataService;
    public StrategyVo objStrategyVo;
    
    public StrategyMap(final StrategyMapManager objStrategyMapManager, final StrategyTemplateDataService objStrategyTemplateDataService) {
        this.objStrategyMapManager = objStrategyMapManager;
        this.objStrategyTemplateDataService = objStrategyTemplateDataService;
    }
    
    public StrategyVo save(StrategyVo objStrategyVo) throws DataAccessException, Exception {
        this.validate(objStrategyVo);
        final Request request = new Request();
        request.put(StrategyParams.STRATEGY_MAP.getParamName(), (BaseValueObject)objStrategyVo);
        final Response response = this.objStrategyTemplateDataService.createStrategyMap(request);
        objStrategyVo = (StrategyVo)response.get(StrategyParams.STRATEGY_MAP.getParamName());
        return objStrategyVo;
    }
    
    public boolean deleteStrategyMap(final int id) throws DataAccessException, MiddlewareException, Exception {
        final Identifier objIdentifier = new Identifier();
        objIdentifier.setId(id);
        final Request request = new Request();
        request.put(StrategyParams.STRATEGY_MAP.getParamName(), (BaseValueObject)objIdentifier);
        final Response response = this.objStrategyTemplateDataService.deleteStrategyMap(request);
        final BooleanResponse booleanResponse = (BooleanResponse)response.get(StrategyParams.STRATEGY_MAP.getParamName());
        return booleanResponse.isResponse();
    }
    
    public void validate(final Object obj) {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), obj, "ERR_VAL_INVALID_INPUT", "Invalid template details");
    }
    
    private Validator getValidator() {
        return this.objStrategyMapManager.getValidator();
    }
    
    public StrategyVo getObjStrategyVo() {
        return this.objStrategyVo;
    }
    
    public void setObjStrategyVo(final StrategyVo objStrategyVo) {
        this.objStrategyVo = objStrategyVo;
    }
}
