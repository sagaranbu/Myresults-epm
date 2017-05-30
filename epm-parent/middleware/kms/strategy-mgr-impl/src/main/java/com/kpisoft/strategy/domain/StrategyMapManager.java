package com.kpisoft.strategy.domain;

import org.springframework.stereotype.*;
import com.kpisoft.strategy.dac.*;
import javax.validation.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.strategy.vo.*;
import com.canopus.dac.*;
import com.canopus.mw.*;
import java.util.*;
import com.kpisoft.strategy.params.*;
import com.canopus.mw.dto.*;

@Component
public class StrategyMapManager extends BaseDomainManager implements CacheLoader<Integer, StrategyMap>
{
    @Autowired
    public StrategyTemplateDataService objStrategyTemplateDataService;
    @Autowired
    private Validator validator;
    @Autowired
    @Qualifier("strategyMapCache")
    private Cache<Integer, StrategyMap> cache;
    private StrategyMap objStrategyMap;
    
    public StrategyMapManager() {
        this.cache = null;
        this.objStrategyMap = null;
    }
    
    public StrategyMap createStrategyMap(final StrategyVo objStrategyVo) throws DataAccessException, Exception {
        this.objStrategyMap = new StrategyMap(this, this.objStrategyTemplateDataService);
        final StrategyVo objStrategyVo2 = this.objStrategyMap.save(objStrategyVo);
        this.objStrategyMap.setObjStrategyVo(objStrategyVo2);
        return this.objStrategyMap;
    }
    
    public StrategyMap getStrategyMap(final int id) throws DataAccessException, Exception {
        return this.objStrategyMap = (StrategyMap)this.cache.get(id, (CacheLoader)this);
    }
    
    public boolean deleteStrategyMap(final int id) throws DataAccessException, MiddlewareException, Exception {
        this.objStrategyMap = new StrategyMap(this, this.objStrategyTemplateDataService);
        return this.objStrategyMap.deleteStrategyMap(id);
    }
    
    public List<StrategyVo> getAllStrategyMap(SortList sortList) {
        final Request request = new Request();
        request.setSortList(sortList);
        final Response response = this.objStrategyTemplateDataService.getAllStrategyMap(request);
        sortList = response.getSortList();
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get(StrategyParams.STRATEGY_MAP_LIST.name());
        final List<StrategyVo> alStrategyVo = (List<StrategyVo>)objectList.getValueObjectList();
        return alStrategyVo;
    }
    
    public Validator getValidator() {
        return this.validator;
    }
    
    public StrategyMap load(final Integer key) {
        final Identifier objIdentifier = new Identifier();
        objIdentifier.setId(key);
        final Request request = new Request();
        request.put(StrategyParams.STRATEGY_MAP.getParamName(), (BaseValueObject)objIdentifier);
        final Response response = this.objStrategyTemplateDataService.getStrategyMap(request);
        final StrategyVo objStrategyVo = (StrategyVo)response.get(StrategyParams.STRATEGY_MAP.getParamName());
        (this.objStrategyMap = new StrategyMap(this, this.objStrategyTemplateDataService)).setObjStrategyVo(objStrategyVo);
        return this.objStrategyMap;
    }
    
    public List<StrategyVo> searchStrategyMap(final StrategyVo strategyMap, SortList sortList) {
        final Request request = new Request();
        request.put(StrategyParams.STRATEGY_MAP.name(), (BaseValueObject)strategyMap);
        request.setSortList(sortList);
        final Response response = this.objStrategyTemplateDataService.searchStrategyMap(request);
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get(StrategyParams.STRATEGY_MAP_LIST.name());
        final List<StrategyVo> strategyVoList = (List<StrategyVo>)objectList.getValueObjectList();
        sortList = response.getSortList();
        return strategyVoList;
    }
}
