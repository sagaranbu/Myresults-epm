package com.kpisoft.strategy.program.dac.impl;

import com.kpisoft.strategy.profram.dac.*;
import org.springframework.stereotype.*;
import com.kpisoft.strategy.program.dac.dao.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import org.modelmapper.convention.*;
import com.kpisoft.strategy.program.vo.*;
import com.kpisoft.strategy.program.dac.impl.domain.*;
import com.canopus.mw.*;
import com.kpisoft.strategy.utility.*;
import com.canopus.dac.*;
import java.io.*;
import com.googlecode.genericdao.search.*;
import org.modelmapper.*;
import java.util.*;
import com.canopus.mw.dto.*;
import java.lang.reflect.*;
import org.springframework.transaction.annotation.*;

@Service
public class ReviewProcessTemplateDataServiceImpl extends BaseDataAccessService implements ReviewProcessTemplateDataService
{
    @Autowired
    private QuestionCategoryDao objQuestionCategoryDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public ReviewProcessTemplateDataServiceImpl() {
        this.objQuestionCategoryDao = null;
        this.modelMapper = null;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)QuestionCategoryBean.class, (Class)QuestionCategoryMetaData.class);
    }
    
    @Transactional(readOnly = true)
    public Response getReviewTemplate(final Request request) throws DataAccessException, Exception {
        List<QuestionCategoryMetaData> alCategoryMetaData = null;
        Response response = null;
        final Identifier objIdentifier = (Identifier)request.get("REVIEW_TEMPATE");
        if (objIdentifier.getId() == null) {
            throw new DataAccessException(StrategyErrorCodesEnum.ERR_REV_PROC_TMPL_INVALID_INPUT_002.name(), "Review Template Id is required");
        }
        QuestionCategoryMetaData objQuestionCategoryMetaData = null;
        try {
            if (objIdentifier.getId() > 0) {
                objQuestionCategoryMetaData = (QuestionCategoryMetaData)this.objQuestionCategoryDao.find((Serializable)objIdentifier.getId());
                if (objQuestionCategoryMetaData == null) {
                    throw new DataAccessException(StrategyErrorCodesEnum.ERR_REV_PROC_TMPL_DOES_NOT_EXIST_001.name(), "Data not found in database with given id");
                }
                final Search objSearch = new Search((Class)QuestionCategoryMetaData.class);
                objSearch.addFilter(Filter.equal("parentId", (Object)objQuestionCategoryMetaData.getId()));
                alCategoryMetaData = (List<QuestionCategoryMetaData>)this.objQuestionCategoryDao.search((ISearch)objSearch);
                final Type listType = new TypeToken<List<QuestionCategoryBean>>() {}.getType();
                final List<? extends BaseValueObject> questionCategoryBVList = (List<? extends BaseValueObject>)this.modelMapper.map((Object)alCategoryMetaData, listType);
                final BaseValueObjectList list = new BaseValueObjectList();
                list.setValueObjectList((List)questionCategoryBVList);
                response = new Response();
                response.put("REVIEW_TEMPATE", (BaseValueObject)list);
            }
        }
        catch (Exception e) {
            ReviewProcessTemplateDataServiceImpl.log.error((Object)"Exception in ReviewProcessTemplateDataServiceImpl - getReviewTemplate() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_REV_PROC_TMPL_UNABLE_TO_GET_004.name(), "Failed to load ReviewTemplate data", new Object[] { e.getMessage() }));
        }
        return response;
    }
    
    static {
        log = Logger.getLogger((Class)ReviewProcessTemplateDataServiceImpl.class);
    }
}
