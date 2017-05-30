package com.kpisoft.strategy.program.domain;

import com.canopus.mw.*;
import org.springframework.stereotype.*;
import com.kpisoft.strategy.profram.dac.*;
import org.springframework.beans.factory.annotation.*;
import javax.validation.*;
import java.util.*;
import com.kpisoft.strategy.program.vo.*;
import com.canopus.mw.dto.*;
import com.canopus.dac.*;

@Component
public class ReviewProcessManager extends BaseDomainManager
{
    @Autowired
    public ReviewProcessTemplateDataService objReviewProcessTemplateDataService;
    @Autowired
    private Validator validator;
    private ReviewProcessTemplate objReviewProcessTemplate;
    
    public ReviewProcessManager() {
        this.objReviewProcessTemplateDataService = null;
        this.objReviewProcessTemplate = null;
    }
    
    public List<QuestionCategoryBean> getReviewTemplate(final int id) throws DataAccessException, Exception {
        final Identifier objIdentifier = new Identifier();
        objIdentifier.setId(id);
        final Request request = new Request();
        request.put("REVIEW_TEMPATE", (BaseValueObject)objIdentifier);
        final Response response = this.objReviewProcessTemplateDataService.getReviewTemplate(request);
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get("REVIEW_TEMPATE");
        final List<QuestionCategoryBean> alQuestionCategoryBean = (List<QuestionCategoryBean>)objectList.getValueObjectList();
        return alQuestionCategoryBean;
    }
    
    public Validator getValidator() {
        return this.validator;
    }
}
