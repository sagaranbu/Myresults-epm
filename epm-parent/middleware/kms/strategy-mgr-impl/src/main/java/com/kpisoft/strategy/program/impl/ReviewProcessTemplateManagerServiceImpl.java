package com.kpisoft.strategy.program.impl;

import com.kpisoft.strategy.program.service.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import com.kpisoft.strategy.program.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import com.kpisoft.strategy.utility.*;
import com.canopus.mw.*;
import java.util.*;
import com.canopus.mw.dto.*;
import com.canopus.dac.*;
import com.kpisoft.strategy.program.vo.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ ReviewProcessTemplateManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class ReviewProcessTemplateManagerServiceImpl extends BaseMiddlewareBean implements ReviewProcessTemplateManagerService
{
    @Autowired
    private ReviewProcessManager objReviewProcessManager;
    private static final Logger log;
    
    public ReviewProcessTemplateManagerServiceImpl() {
        this.objReviewProcessManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response getReviewTemplate(final Request request) {
        try {
            final Identifier objIdentifier = (Identifier)request.get("REVIEW_TEMPATE");
            if (objIdentifier.getId() == null) {
                throw new MiddlewareException(StrategyErrorCodesEnum.ERR_REV_PROC_TMPL_INVALID_INPUT_002.name(), "Review Template Id is required");
            }
            final List<QuestionCategoryBean> alQuestionCategoryBean = this.objReviewProcessManager.getReviewTemplate(objIdentifier.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)alQuestionCategoryBean);
            return this.OK("REVIEW_TEMPATE", (BaseValueObject)list);
        }
        catch (Exception e) {
            ReviewProcessTemplateManagerServiceImpl.log.error((Object)"Exception in ReviewProcessTemplateManagerServiceImpl - getReviewTemplate() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_REV_PROC_TMPL_UNABLE_TO_GET_004.name(), "Failed to load ReviewTemplate data", new Object[] { e.getMessage() }));
        }
    }
    
    static {
        log = Logger.getLogger((Class)ReviewProcessTemplateManagerServiceImpl.class);
    }
}
