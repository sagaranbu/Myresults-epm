package com.kpisoft.strategy.dac.impl;

import com.kpisoft.strategy.dac.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.strategy.dac.dao.*;
import com.canopus.dac.hibernate.*;
import org.apache.log4j.*;
import org.modelmapper.convention.*;
import com.canopus.mw.*;
import com.kpisoft.strategy.vo.*;
import com.kpisoft.strategy.params.*;
import com.kpisoft.strategy.utility.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import com.canopus.dac.utils.*;
import org.modelmapper.*;
import java.util.*;
import com.canopus.mw.dto.*;
import com.googlecode.genericdao.search.*;
import java.lang.reflect.*;
import com.kpisoft.strategy.dac.impl.domain.*;

@Service
public class StrategyTemplateDataServiceImpl extends BaseDataAccessService implements StrategyTemplateDataService
{
    @Autowired
    private StrategyDao objStrategyDao;
    @Autowired
    private StrategyTemplateDao strategyTemplateDao;
    @Autowired
    private GenericHibernateDao genericDao;
    private static final Logger logger;
    private ModelMapper modelMapper;
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public StrategyTemplateDataServiceImpl() {
        this.objStrategyDao = null;
        this.strategyTemplateDao = null;
        this.genericDao = null;
        this.modelMapper = null;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)StrategyTemplateVo.class, (Class)StrategyTemplate.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)StrategyVo.class, (Class)Strategy.class);
    }
    
    @Transactional
    public Response saveStrategyTemplate(final Request request) {
        final Response response = new Response();
        try {
            final StrategyTemplateVo strategyTemplate = (StrategyTemplateVo)request.get(StrategyParams.STRATEGY_TEMPLATE.getParamName());
            if (strategyTemplate == null) {
                return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_STR_TMPL_INVALID_INPUT_002.name(), "Invalid data"));
            }
            StrategyTemplate strTemplate = null;
            if (strategyTemplate.getId() != null && strategyTemplate.getId() > 0) {
                strTemplate = (StrategyTemplate)this.strategyTemplateDao.find((Serializable)strategyTemplate.getId());
            }
            else {
                strTemplate = new StrategyTemplate();
            }
            this.modelMapper.map((Object)strategyTemplate, (Object)strTemplate);
            this.strategyTemplateDao.merge(strTemplate);
            if (strTemplate.getStrategyLevelTemplate() != null && !strTemplate.getStrategyLevelTemplate().isEmpty()) {
                for (final StrategyLevelTemplate iterator : strTemplate.getStrategyLevelTemplate()) {
                    iterator.setStrTempId(strTemplate.getId());
                    if (iterator.getStrategyNodeTemplate() != null && !iterator.getStrategyNodeTemplate().isEmpty()) {
                        for (final StrategyNodeTemplate nodeTemplate : iterator.getStrategyNodeTemplate()) {
                            nodeTemplate.setStrTmpLvlId(iterator.getId());
                        }
                    }
                }
                final Identifier identifier = new Identifier(strTemplate.getId());
                response.put(StrategyParams.STRATEGY_TEMPLATE_ID.getParamName(), (BaseValueObject)identifier);
            }
        }
        catch (Exception ex) {
            StrategyTemplateDataServiceImpl.logger.error((Object)("Exception in StrategyTemplateDataServiceImpl - saveStrategyTemplate() : " + ex));
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_STR_TMPL_UNABLE_TO_CREATE_003.name(), "Failed to create strategy template", new Object[] { ex.getMessage() }));
        }
        return response;
    }
    
    @Transactional(readOnly = true)
    public Response getStrategyTemplate(final Request request) {
        Response response = null;
        StrategyTemplateVo objStrategyTemplateVo = null;
        final Identifier objIdentifier = (Identifier)request.get(StrategyParams.STRATEGY_TEMPLATE.getParamName());
        if (objIdentifier.getId() == null) {
            throw new DataAccessException(StrategyErrorCodesEnum.ERR_STR_TMPL_INVALID_INPUT_002.name(), "Template Id Required");
        }
        StrategyTemplate objStrategyTemplate = null;
        try {
            if (objIdentifier.getId() > 0) {
                objStrategyTemplate = (StrategyTemplate)this.strategyTemplateDao.find((Serializable)objIdentifier.getId());
                if (objStrategyTemplate == null) {
                    throw new DataAccessException(StrategyErrorCodesEnum.ERR_STR_TMPL_DOES_NOT_EXIST_001.name(), "strategyTemplate id does not exist");
                }
                objStrategyTemplateVo = (StrategyTemplateVo)this.modelMapper.map((Object)objStrategyTemplate, (Class)StrategyTemplateVo.class);
            }
            response = new Response();
            response.put(StrategyParams.STRATEGY_TEMPLATE.getParamName(), (BaseValueObject)objStrategyTemplateVo);
        }
        catch (Exception e) {
            StrategyTemplateDataServiceImpl.logger.error((Object)("Exception in StrategyTemplateDataServiceImpl - getStrategyTemplate() : " + e));
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_STR_TMPL_UNABLE_TO_GET_004.name(), "Failed to load strategy template", new Object[] { e.getMessage() }));
        }
        return response;
    }
    
    @Transactional
    public Response deleteStrategyTemplate(final Request request) {
        Response response = null;
        final Identifier objIdentifier = (Identifier)request.get(StrategyParams.STRATEGY_TEMPLATE.getParamName());
        if (objIdentifier.getId() == null) {
            throw new DataAccessException(StrategyErrorCodesEnum.ERR_STR_TMPL_INVALID_INPUT_002.name(), "Template Id Required");
        }
        try {
            if (objIdentifier.getId() > 0) {
                final boolean isRemove = this.strategyTemplateDao.removeById((Serializable)objIdentifier.getId());
                response = new Response();
                response.put(StrategyParams.STRATEGY_TEMPLATE.getParamName(), (BaseValueObject)new BooleanResponse(isRemove));
            }
        }
        catch (Exception e) {
            StrategyTemplateDataServiceImpl.logger.error((Object)("Exception in StrategyTemplateDataServiceImpl - deleteStrategyTemplate() : " + e));
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_STR_TMPL_UNABLE_TO_DELETE_006.name(), "Failed to delete strategyTemplate", new Object[] { e.getMessage() }));
        }
        return response;
    }
    
    @Transactional(readOnly = true)
    public Response getAllStrategyTemplate(final Request request) {
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        try {
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.strategyTemplateDao.getFilterFromExample(new StrategyTemplate(), options);
            final Search search = new Search((Class)StrategyTemplate.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<StrategyTemplate> result = (List<StrategyTemplate>)this.strategyTemplateDao.search((ISearch)search);
            final Type listType = new TypeToken<List<StrategyTemplateVo>>() {}.getType();
            final List<StrategyTemplateVo> strategyTemplateVos = (List<StrategyTemplateVo>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)strategyTemplateVos);
            final Response response = this.OK(StrategyParams.STRATEGY_TEMPLATE_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            StrategyTemplateDataServiceImpl.logger.error((Object)("Exception in StrategyTemplateDataServiceImpl - getAllStrategyTemplate(request) : " + e));
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_STR_TMPL_UNABLE_TO_GET_ALL_007.name(), "Failed to load all strategy templates data", new Object[] { e.getMessage() }));
        }
    }
    
    @Transactional
    public Response createStrategyMap(final Request request) {
        Response response = null;
        final StrategyVo strategyMap = (StrategyVo)request.get(StrategyParams.STRATEGY_MAP.getParamName());
        if (strategyMap == null) {
            throw new DataAccessException(StrategyErrorCodesEnum.ERR_STR_MAP_INVALID_INPUT_002.name(), "Invalid input in request");
        }
        Strategy objStrategyMapTemplateMetaData = null;
        try {
            if (strategyMap.getId() != null && strategyMap.getId() > 0) {
                objStrategyMapTemplateMetaData = (Strategy)this.objStrategyDao.find((Serializable)strategyMap.getId());
                if (objStrategyMapTemplateMetaData == null) {
                    throw new DataAccessException(StrategyErrorCodesEnum.ERR_STR_MAP_DOES_NOT_EXIST_001.name(), "strategyMap id does not exist");
                }
            }
            else {
                objStrategyMapTemplateMetaData = new Strategy();
                this.modelMapper.map((Object)strategyMap, (Object)objStrategyMapTemplateMetaData);
            }
            this.objStrategyDao.save(objStrategyMapTemplateMetaData);
            if (objStrategyMapTemplateMetaData.getStrategyLevel() != null) {
                for (final StrategyLevel iterator : objStrategyMapTemplateMetaData.getStrategyLevel()) {
                    iterator.setStrId(objStrategyMapTemplateMetaData.getId());
                    if (iterator.getStrategyNode() != null) {
                        for (final StrategyNode node : iterator.getStrategyNode()) {
                            node.setStrLvlId(iterator.getId());
                        }
                    }
                }
            }
            strategyMap.setId(objStrategyMapTemplateMetaData.getId());
            response = new Response();
            response.put(StrategyParams.STRATEGY_MAP.getParamName(), (BaseValueObject)strategyMap);
        }
        catch (Exception e) {
            StrategyTemplateDataServiceImpl.logger.error((Object)("Exception in StrategyTemplateDataServiceImpl - createStrategyMap() : " + e));
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_STR_MAP_UNABLE_TO_CREATE_003.name(), "Failted to create strategyMap", new Object[] { e.getMessage() }));
        }
        return response;
    }
    
    @Transactional(readOnly = true)
    public Response getStrategyMap(final Request request) {
        Response response = null;
        StrategyVo objStrategyVo = null;
        final Identifier objIdentifier = (Identifier)request.get(StrategyParams.STRATEGY_MAP.getParamName());
        if (objIdentifier == null) {
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_STR_MAP_INVALID_INPUT_002.name(), "Strategy Template Id is required"));
        }
        try {
            Strategy objStrategy = null;
            if (objIdentifier.getId() > 0) {
                objStrategy = (Strategy)this.objStrategyDao.find((Serializable)objIdentifier.getId());
                if (objStrategy == null) {
                    throw new DataAccessException(StrategyErrorCodesEnum.ERR_STR_MAP_DOES_NOT_EXIST_001.name(), "Data does not found");
                }
                objStrategyVo = (StrategyVo)this.modelMapper.map((Object)objStrategy, (Class)StrategyVo.class);
            }
            response = new Response();
            response.put(StrategyParams.STRATEGY_MAP.getParamName(), (BaseValueObject)objStrategyVo);
        }
        catch (Exception e) {
            StrategyTemplateDataServiceImpl.logger.error((Object)("Exception in StrategyTemplateDataServiceImpl - getStrategyMap() : " + e));
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_STR_MAP_UNABLE_TO_GET_004.name(), "Failed to load strategyMap data", new Object[] { e.getMessage() }));
        }
        return response;
    }
    
    @Transactional
    public Response deleteStrategyMap(final Request request) {
        Response response = null;
        final Identifier objIdentifier = (Identifier)request.get(StrategyParams.STRATEGY_MAP.getParamName());
        if (objIdentifier == null) {
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_STR_MAP_INVALID_INPUT_002.name(), "Strategy  Id is required"));
        }
        try {
            if (objIdentifier.getId() > 0) {
                final boolean isRemove = this.objStrategyDao.removeById((Serializable)objIdentifier.getId());
                response = new Response();
                response.put(StrategyParams.STRATEGY_MAP.getParamName(), (BaseValueObject)new BooleanResponse(isRemove));
            }
        }
        catch (Exception e) {
            StrategyTemplateDataServiceImpl.logger.error((Object)("Exception in StrategyTemplateDataServiceImpl - deleteStrategyMap() : " + e));
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_STR_MAP_UNABLE_TO_DELETE_006.name(), "Failed to delete strategyMap", new Object[] { e.getMessage() }));
        }
        return response;
    }
    
    @Transactional(readOnly = true)
    public Response getAllStrategyMap(final Request request) {
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        try {
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.objStrategyDao.getFilterFromExample(new Strategy(), options);
            final Search search = new Search((Class)Strategy.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<Strategy> result = (List<Strategy>)this.objStrategyDao.search((ISearch)search);
            final Type listType = new TypeToken<List<StrategyVo>>() {}.getType();
            final List<StrategyVo> strategyVos = (List<StrategyVo>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)strategyVos);
            final Response response = this.OK(StrategyParams.STRATEGY_MAP_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            StrategyTemplateDataServiceImpl.logger.error((Object)("Exception in StrategyTemplateDataServiceImpl - getAllStrategyMap(request) : " + e));
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_STR_MAP_UNABLE_TO_GET_ALL_007.name(), "Failed to load all the strategyMaps data", new Object[] { e.getMessage() }));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchStrategyMap(final Request request) {
        final StrategyVo strategyMap = (StrategyVo)request.get(StrategyParams.STRATEGY_MAP.name());
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        BaseValueObjectList list = null;
        if (strategyMap == null) {
            throw new DataAccessException(StrategyErrorCodesEnum.ERR_STR_MAP_INVALID_INPUT_002.name(), "Strategy  Id is required");
        }
        try {
            final Search search = new Search((Class)Strategy.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Strategy strategy = (Strategy)this.modelMapper.map((Object)strategyMap, (Class)Strategy.class);
            final Filter filter = this.genericDao.getFilterFromExample((Object)strategy, options);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<Strategy> strategyList = (List<Strategy>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<StrategyVo>>() {}.getType();
            final List<? extends BaseValueObject> kpiTypeBVList = (List<? extends BaseValueObject>)this.modelMapper.map((Object)strategyList, listType);
            list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiTypeBVList);
            final Response response = this.OK(StrategyParams.STRATEGY_MAP_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            StrategyTemplateDataServiceImpl.logger.error((Object)("Exception in StrategyTemplateDataServiceImpl - search() : " + e));
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_STR_MAP_UNABLE_TO_SEARCH_008.name(), "Failed to search strategyMap data", new Object[] { e.getMessage() }));
        }
    }
    
    static {
        logger = Logger.getLogger((Class)StrategyTemplateDataServiceImpl.class);
    }
}
