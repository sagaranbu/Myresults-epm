package com.kpisoft.strategy.impl;

import com.kpisoft.strategy.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.strategy.params.*;
import com.kpisoft.strategy.utility.*;
import com.canopus.mw.*;
import com.kpisoft.strategy.vo.*;
import com.kpisoft.strategy.domain.*;
import java.util.*;
import com.canopus.mw.dto.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ StrategyTemplateManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class StrategyTemplateManagerServiceImpl extends BaseMiddlewareBean implements StrategyTemplateManagerService
{
    private static final Logger logger;
    @Autowired
    private StrategyMapManager objStrategyMapManager;
    @Autowired
    private StrategyMapTemplateManager objStrategyMapTemplateManager;
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response createStrategyTemplate(final Request request) {
        try {
            final StrategyTemplateVo objStrategyTemplateVo = (StrategyTemplateVo)request.get(StrategyParams.STRATEGY_TEMPLATE.getParamName());
            if (objStrategyTemplateVo == null) {
                throw new MiddlewareException(StrategyErrorCodesEnum.ERR_STR_TMPL_INVALID_INPUT_002.name(), "Data does not found in request");
            }
            final Identifier identifier = this.objStrategyMapTemplateManager.createStrategyTemplate(objStrategyTemplateVo);
            return this.OK(StrategyParams.STRATEGY_TEMPLATE_ID.getParamName(), (BaseValueObject)identifier);
        }
        catch (Exception e) {
            StrategyTemplateManagerServiceImpl.logger.error((Object)("Exception in StrategyTemplateManagerServiceImpl - createStrategyTemplate() : " + e));
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_STR_TMPL_UNABLE_TO_CREATE_003.name(), "Failed to create strategy template", new Object[] { e.getMessage() }));
        }
    }
    
    public Response createStrategyMap(final Request request) {
        try {
            final StrategyVo objStrategyVo = (StrategyVo)request.get(StrategyParams.STRATEGY_MAP.getParamName());
            if (objStrategyVo == null) {
                throw new MiddlewareException(StrategyErrorCodesEnum.ERR_STR_MAP_INVALID_INPUT_002.name(), "Data does not found in request");
            }
            final StrategyMap objStrategyMap = this.objStrategyMapManager.createStrategyMap(objStrategyVo);
            return this.OK(StrategyParams.STRATEGY_MAP.getParamName(), (BaseValueObject)objStrategyMap.getObjStrategyVo());
        }
        catch (Exception e) {
            StrategyTemplateManagerServiceImpl.logger.error((Object)("Exception in StrategyTemplateManagerServiceImpl - createStrategyMap() : " + e));
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_STR_MAP_UNABLE_TO_CREATE_003.name(), "Failted to create strategyMap", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getStrategyTemplate(final Request request) {
        try {
            final Identifier objIdentifier = (Identifier)request.get(StrategyParams.STRATEGY_TEMPLATE.getParamName());
            if (objIdentifier.getId() == null) {
                throw new MiddlewareException(StrategyErrorCodesEnum.ERR_STR_TMPL_INVALID_INPUT_002.name(), "Template Id Required");
            }
            final StrategyMapTemplate objStrategyMapTemplate = this.objStrategyMapTemplateManager.getStrategyTemplate(objIdentifier.getId());
            return this.OK(StrategyParams.STRATEGY_TEMPLATE.getParamName(), (BaseValueObject)objStrategyMapTemplate.getObjStrategyTemplateVo());
        }
        catch (Exception e) {
            StrategyTemplateManagerServiceImpl.logger.error((Object)("Exception in StrategyTemplateManagerServiceImpl - getStrategyTemplate() : " + e));
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_STR_TMPL_UNABLE_TO_GET_004.name(), "Failed to load strategy template", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getAllStrategyTemplate(final Request request) {
        final SortList sortList = request.getSortList();
        try {
            final List<StrategyTemplateVo> result = this.objStrategyMapTemplateManager.getAllStrategyTemplate(sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final Response response = this.OK(StrategyParams.STRATEGY_TEMPLATE_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            StrategyTemplateManagerServiceImpl.logger.error((Object)("Exception in StrategyTemplateManagerServiceImpl - getAllStrategyTemplate(request) : " + e));
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_STR_TMPL_UNABLE_TO_GET_ALL_007.name(), "Failed to load all strategy templates data", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getAllStrategyMap(final Request request) {
        final SortList sortList = request.getSortList();
        try {
            final List<StrategyVo> result = this.objStrategyMapManager.getAllStrategyMap(sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final Response response = this.OK(StrategyParams.STRATEGY_MAP_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            StrategyTemplateManagerServiceImpl.logger.error((Object)("Exception in StrategyTemplateManagerServiceImpl - getAllStrategyMap(request) : " + e));
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_STR_MAP_UNABLE_TO_GET_ALL_007.name(), "Failed to load all the strategyMaps data", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getStrategyMap(final Request request) {
        try {
            final Identifier objIdentifier = (Identifier)request.get(StrategyParams.STRATEGY_MAP.getParamName());
            if (objIdentifier == null) {
                return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_STR_MAP_INVALID_INPUT_002.name(), "Strategy Template Id is required"));
            }
            final StrategyMap objStrategyMap = this.objStrategyMapManager.getStrategyMap(objIdentifier.getId());
            return this.OK(StrategyParams.STRATEGY_MAP.getParamName(), (BaseValueObject)objStrategyMap.getObjStrategyVo());
        }
        catch (Exception e) {
            StrategyTemplateManagerServiceImpl.logger.error((Object)("Exception in StrategyTemplateManagerServiceImpl - getStrategyMap() : " + e));
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_STR_MAP_UNABLE_TO_GET_004.name(), "Failed to load strategyMap data", new Object[] { e.getMessage() }));
        }
    }
    
    public Response deleteStrategyTemplate(final Request request) {
        try {
            final Identifier objIdentifier = (Identifier)request.get(StrategyParams.STRATEGY_TEMPLATE.getParamName());
            if (objIdentifier == null) {
                return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_STR_TMPL_INVALID_INPUT_002.name(), "Strategy Template Id is required"));
            }
            final boolean bResult = this.objStrategyMapTemplateManager.deleteStrategyTemplate(objIdentifier.getId());
            return this.OK(StrategyParams.STRATEGY_TEMPLATE.getParamName(), (BaseValueObject)new BooleanResponse(bResult));
        }
        catch (Exception e) {
            StrategyTemplateManagerServiceImpl.logger.error((Object)("Exception in StrategyTemplateManagerServiceImpl - deleteStrategyTemplate() : " + e));
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_STR_TMPL_UNABLE_TO_DELETE_006.name(), "Failed to delete strategyTemplate", new Object[] { e.getMessage() }));
        }
    }
    
    public Response deleteStrategyMap(final Request request) {
        try {
            final Identifier objIdentifier = (Identifier)request.get(StrategyParams.STRATEGY_MAP.getParamName());
            if (objIdentifier == null) {
                return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_STR_MAP_INVALID_INPUT_002.name(), "Strategy  Id is required"));
            }
            final boolean bResult = this.objStrategyMapManager.deleteStrategyMap(objIdentifier.getId());
            return this.OK(StrategyParams.STRATEGY_MAP.getParamName(), (BaseValueObject)new BooleanResponse(bResult));
        }
        catch (Exception e) {
            StrategyTemplateManagerServiceImpl.logger.error((Object)("Exception in StrategyTemplateManagerServiceImpl - deleteStrategyMap() : " + e));
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_STR_MAP_UNABLE_TO_DELETE_006.name(), "Failed to delete strategyMap", new Object[] { e.getMessage() }));
        }
    }
    
    public Response searchStrategyMap(final Request request) {
        final StrategyVo strategyMap = (StrategyVo)request.get(StrategyParams.STRATEGY_MAP.getParamName());
        final SortList sortList = request.getSortList();
        if (strategyMap == null) {
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_STR_MAP_INVALID_INPUT_002.name(), "Strategy  Id is required"));
        }
        try {
            final List<StrategyVo> result = this.objStrategyMapManager.searchStrategyMap(strategyMap, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final Response response = this.OK(StrategyParams.STRATEGY_MAP_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            StrategyTemplateManagerServiceImpl.logger.error((Object)("Exception in StrategyTemplateManagerServiceImpl - search() : " + e));
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_STR_MAP_UNABLE_TO_SEARCH_008.name(), "Failed to search strategyMap data", new Object[] { e.getMessage() }));
        }
    }
    
    static {
        logger = Logger.getLogger((Class)StrategyTemplateManagerServiceImpl.class);
    }
}
