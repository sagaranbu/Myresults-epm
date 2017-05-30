package com.kpisoft.kpi.dac.impl;

import com.kpisoft.kpi.dac.*;
import org.springframework.stereotype.*;
import com.canopus.dac.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import org.modelmapper.convention.*;
import com.canopus.mw.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.dac.impl.domain.*;
import com.kpisoft.kpi.vo.param.*;
import com.kpisoft.kpi.utility.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import org.modelmapper.*;
import java.util.*;
import com.canopus.mw.dto.*;
import java.lang.reflect.*;
import com.googlecode.genericdao.search.*;

@Component
public class WorkflowDataServiceImpl extends BaseDataAccessService implements WorkflowDataService
{
    @Autowired
    private GenericHibernateDao genericDao;
    private static final Logger log;
    private ModelMapper modelMapper;
    
    public WorkflowDataServiceImpl() {
        this.genericDao = null;
        this.modelMapper = null;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)GoalSettingWorkflowCommentsBean.class, (Class)GoalSettingWorkflowComments.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)PerformanceReviewCommentsBean.class, (Class)PerformanceReviewComments.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    @Transactional
    public Response saveGoalSettingComments(final Request request) {
        final GoalSettingWorkflowCommentsBean data = (GoalSettingWorkflowCommentsBean)request.get(WorkflowParams.GOAL_COMMENTS_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_GSWC_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            GoalSettingWorkflowComments comments = null;
            if (data.getId() != null && data.getId() > 0) {
                comments = (GoalSettingWorkflowComments)this.genericDao.find((Class)GoalSettingWorkflowComments.class, (Serializable)data.getId());
            }
            else {
                comments = new GoalSettingWorkflowComments();
            }
            this.modelMapper.map((Object)data, (Object)comments);
            this.genericDao.save((Object)comments);
            return this.OK(WorkflowParams.GOAL_COMMENTS_ID.name(), (BaseValueObject)new Identifier(comments.getId()));
        }
        catch (Exception ex) {
            WorkflowDataServiceImpl.log.error((Object)"Exception in WorkflowDataServiceImpl - saveGoalSettingComments() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_GSWC_UNABLE_TO_CREATE_003.name(), "Failed to save goalSettingWorkflowComments data", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getGoalSettingCommentsById(final Request request) {
        final Identifier identifier = (Identifier)request.get(WorkflowParams.GOAL_COMMENTS_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_GSWC_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final GoalSettingWorkflowComments comments = (GoalSettingWorkflowComments)this.genericDao.find((Class)GoalSettingWorkflowComments.class, (Serializable)identifier.getId());
            final GoalSettingWorkflowCommentsBean data = (GoalSettingWorkflowCommentsBean)this.modelMapper.map((Object)comments, (Class)GoalSettingWorkflowCommentsBean.class);
            return this.OK(WorkflowParams.GOAL_COMMENTS_DATA.name(), (BaseValueObject)data);
        }
        catch (Exception ex) {
            WorkflowDataServiceImpl.log.error((Object)"Exception in WorkflowDataServiceImpl - getGoalSettingCommentsById() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_GSWC_UNABLE_TO_GET_004.name(), "Failed to load GoalSettingWorkflowComments", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchGoalSettingComments(final Request request) {
        final GoalSettingWorkflowCommentsBean data = (GoalSettingWorkflowCommentsBean)request.get(WorkflowParams.GOAL_COMMENTS_DATA.name());
        try {
            final GoalSettingWorkflowComments comments = (GoalSettingWorkflowComments)this.modelMapper.map((Object)data, (Class)GoalSettingWorkflowComments.class);
            final Filter filter = this.genericDao.getFilterFromExample((Object)comments);
            final Search search = new Search((Class)GoalSettingWorkflowComments.class);
            search.addFilter(filter);
            final List<GoalSettingWorkflowComments> result = (List<GoalSettingWorkflowComments>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<GoalSettingWorkflowCommentsBean>>() {}.getType();
            final List<GoalSettingWorkflowCommentsBean> commentsList = (List<GoalSettingWorkflowCommentsBean>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)commentsList);
            return this.OK(WorkflowParams.GOAL_COMMENTS_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception ex) {
            WorkflowDataServiceImpl.log.error((Object)"Exception in WorkflowDataServiceImpl - searchGoalSettingComments() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_GSWC_UNABLE_TO_SEARCH_008.name(), "Failed to search GoalSettingWorkflowComments", (Throwable)ex));
        }
    }
    
    @Transactional
    public Response savePerformanceComments(final Request request) {
        final PerformanceReviewCommentsBean data = (PerformanceReviewCommentsBean)request.get(WorkflowParams.PERFORMANCE_COMMENTS_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_PRC_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            PerformanceReviewComments comments = null;
            if (data.getId() != null && data.getId() > 0) {
                comments = (PerformanceReviewComments)this.genericDao.find((Class)PerformanceReviewComments.class, (Serializable)data.getId());
            }
            else {
                comments = new PerformanceReviewComments();
            }
            this.modelMapper.map((Object)data, (Object)comments);
            this.genericDao.save((Object)comments);
            return this.OK(WorkflowParams.PERFORMANCE_COMMENTS_ID.name(), (BaseValueObject)new Identifier(comments.getId()));
        }
        catch (Exception ex) {
            WorkflowDataServiceImpl.log.error((Object)"Exception in WorkflowDataServiceImpl - savePerformanceComments() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_PRC_UNABLE_TO_CREATE_003.name(), "Unknown error while saving performance review comments", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getPerformanceCommentsById(final Request request) {
        final Identifier identifier = (Identifier)request.get(WorkflowParams.PERFORMANCE_COMMENTS_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_PRC_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final PerformanceReviewComments comments = (PerformanceReviewComments)this.genericDao.find((Class)PerformanceReviewComments.class, (Serializable)identifier.getId());
            final PerformanceReviewCommentsBean data = (PerformanceReviewCommentsBean)this.modelMapper.map((Object)comments, (Class)PerformanceReviewCommentsBean.class);
            return this.OK(WorkflowParams.PERFORMANCE_COMMENTS_DATA.name(), (BaseValueObject)data);
        }
        catch (Exception ex) {
            WorkflowDataServiceImpl.log.error((Object)"Exception in WorkflowDataServiceImpl - getPerformanceCommentsById() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_PRC_UNABLE_TO_GET_004.name(), "Unknown error while loading performance review comments", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchPerformanceComments(final Request request) {
        final PerformanceReviewCommentsBean data = (PerformanceReviewCommentsBean)request.get(WorkflowParams.PERFORMANCE_COMMENTS_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_PRC_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final PerformanceReviewComments comments = (PerformanceReviewComments)this.modelMapper.map((Object)data, (Class)PerformanceReviewComments.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            final Filter filter = this.genericDao.getFilterFromExample((Object)comments, options);
            final Search search = new Search((Class)PerformanceReviewComments.class);
            search.addFilter(filter);
            final List<PerformanceReviewComments> result = (List<PerformanceReviewComments>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<GoalSettingWorkflowCommentsBean>>() {}.getType();
            final List<PerformanceReviewCommentsBean> commentsList = (List<PerformanceReviewCommentsBean>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)commentsList);
            return this.OK(WorkflowParams.PERFORMANCE_COMMENTS_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception ex) {
            WorkflowDataServiceImpl.log.error((Object)"Exception in WorkflowDataServiceImpl - searchPerformanceComments() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_PRC_UNABLE_TO_SEARCH_008.name(), "Failed while searching goal setting comments", (Throwable)ex));
        }
    }
    
    @Transactional
    public Response saveAllGoalSettingComments(final Request request) {
        final BaseValueObjectList objectList = (BaseValueObjectList)request.get(WorkflowParams.GOAL_COMMENTS_DATA_LIST.name());
        final List<GoalSettingWorkflowCommentsBean> data = (List<GoalSettingWorkflowCommentsBean>)objectList.getValueObjectList();
        if (data == null || data.isEmpty()) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_GSWC_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Type listType = new TypeToken<List<GoalSettingWorkflowComments>>() {}.getType();
            final List<GoalSettingWorkflowComments> beans = (List<GoalSettingWorkflowComments>)this.modelMapper.map((Object)data, listType);
            final boolean[] id = this.genericDao.save(beans.toArray());
            return this.OK(WorkflowParams.COUNT.name(), (BaseValueObject)new Identifier(id.length));
        }
        catch (Exception ex) {
            WorkflowDataServiceImpl.log.error((Object)"Exception in WorkflowDataServiceImpl - saveAllGoalSettingComments() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_GSWC_UNABLE_TO_SAVE_ALL_009.name(), "Failed to save list of GoalSettingWorkflowComments data", (Throwable)ex));
        }
    }
    
    @Transactional
    public Response saveAllPerformanceReviewComments(final Request request) {
        final BaseValueObjectList objectList = (BaseValueObjectList)request.get(WorkflowParams.PERFORMANCE_COMMENTS_DATA_LIST.name());
        final List<PerformanceReviewCommentsBean> data = (List<PerformanceReviewCommentsBean>)objectList.getValueObjectList();
        if (data == null || data.isEmpty()) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_PRC_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Type listType = new TypeToken<List<PerformanceReviewComments>>() {}.getType();
            final List<PerformanceReviewComments> beans = (List<PerformanceReviewComments>)this.modelMapper.map((Object)data, listType);
            final boolean[] id = this.genericDao.save(beans.toArray());
            return this.OK(WorkflowParams.COUNT.name(), (BaseValueObject)new Identifier(id.length));
        }
        catch (Exception ex) {
            WorkflowDataServiceImpl.log.error((Object)"Exception in WorkflowDataServiceImpl - saveAllPerformanceReviewComments() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_PRC_UNABLE_TO_SAVE_ALL_009.name(), "Failed to save list of PerformanceReviewComments data", (Throwable)ex));
        }
    }
    
    static {
        log = Logger.getLogger((Class)WorkflowDataServiceImpl.class);
    }
}
