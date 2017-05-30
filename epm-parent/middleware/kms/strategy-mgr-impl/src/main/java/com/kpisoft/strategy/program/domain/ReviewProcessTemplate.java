package com.kpisoft.strategy.program.domain;

import com.canopus.mw.*;
import com.kpisoft.strategy.profram.dac.*;
import com.kpisoft.strategy.program.vo.*;
import com.canopus.mw.utils.*;
import javax.validation.*;

public class ReviewProcessTemplate extends BaseDomainObject
{
    private ReviewProcessManager objReviewProcessManager;
    public ReviewProcessTemplateDataService objReviewProcessTemplateDataService;
    public QuestionCategoryBean objQuestionCategoryBean;
    
    public ReviewProcessTemplate(final ReviewProcessManager objReviewProcessManager, final ReviewProcessTemplateDataService objReviewProcessTemplateDataService) {
        this.objReviewProcessManager = objReviewProcessManager;
        this.objReviewProcessTemplateDataService = objReviewProcessTemplateDataService;
    }
    
    public void validate(final Object obj) {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), obj, "ERR_VAL_INVALID_INPUT", "Invalid data input");
    }
    
    private Validator getValidator() {
        return this.objReviewProcessManager.getValidator();
    }
}
