package com.kpisoft.strategy.domain;

import com.canopus.mw.*;
import org.springframework.stereotype.*;
import com.kpisoft.strategy.dac.*;
import javax.validation.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.strategy.vo.*;
import com.kpisoft.strategy.params.*;
import com.canopus.dac.*;
import java.util.*;
import com.canopus.mw.dto.*;

@Component
public class StrategyMapTemplateManager extends BaseDomainManager implements CacheLoader<Integer, StrategyMapTemplate>
{
    @Autowired
    public StrategyTemplateDataService objStrategyTemplateDataService;
    @Autowired
    private Validator validator;
    @Autowired
    @Qualifier("strategyMapTemplateCache")
    private Cache<Integer, StrategyMapTemplate> cache;
    private StrategyMapTemplate objStrategyMapTemplate;
    
    public StrategyMapTemplateManager() {
        this.cache = null;
        this.objStrategyMapTemplate = null;
    }
    
    public Identifier createStrategyTemplate(final StrategyTemplateVo objStrategyTemplateVo) throws DataAccessException, Exception {
        final Request request = new Request();
        request.put(StrategyParams.STRATEGY_TEMPLATE.getParamName(), (BaseValueObject)objStrategyTemplateVo);
        this.objStrategyMapTemplate = new StrategyMapTemplate(this, this.objStrategyTemplateDataService);
        final Identifier identifier = this.objStrategyMapTemplate.save(objStrategyTemplateVo);
        return identifier;
    }
    
    public StrategyMapTemplate getStrategyTemplate(final int id) throws DataAccessException, Exception {
        return this.objStrategyMapTemplate = (StrategyMapTemplate)this.cache.get(id, (CacheLoader)this);
    }
    
    public List<StrategyTemplateVo> getAllStrategyTemplate(SortList sortList) {
        final Request request = new Request();
        request.setSortList(sortList);
        final Response response = this.objStrategyTemplateDataService.getAllStrategyTemplate(request);
        sortList = response.getSortList();
        final BaseValueObjectList list = (BaseValueObjectList)response.get(StrategyParams.STRATEGY_TEMPLATE_LIST.name());
        final List<StrategyTemplateVo> alStrategyTemplateVo = (List<StrategyTemplateVo>)list.getValueObjectList();
        return alStrategyTemplateVo;
    }
    
    public boolean deleteStrategyTemplate(final int id) throws DataAccessException, Exception {
        this.objStrategyMapTemplate = new StrategyMapTemplate(this, this.objStrategyTemplateDataService);
        return this.objStrategyMapTemplate.deleteStrategyTemplate(id);
    }
    
    public Validator getValidator() {
        return this.validator;
    }
    
    public StrategyMapTemplate load(final Integer key) {
        final Identifier objIdentifier = new Identifier();
        objIdentifier.setId(key);
        final Request request = new Request();
        request.put(StrategyParams.STRATEGY_TEMPLATE.getParamName(), (BaseValueObject)objIdentifier);
        final Response response = this.objStrategyTemplateDataService.getStrategyTemplate(request);
        final StrategyTemplateVo objStrategyTemplateVo1 = (StrategyTemplateVo)response.get(StrategyParams.STRATEGY_TEMPLATE.getParamName());
        (this.objStrategyMapTemplate = new StrategyMapTemplate(this, this.objStrategyTemplateDataService)).setObjStrategyTemplateVo(objStrategyTemplateVo1);
        return this.objStrategyMapTemplate;
    }
}
