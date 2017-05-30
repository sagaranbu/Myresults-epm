package com.kpisoft.user.dac.impl;

import com.kpisoft.user.dac.*;
import org.springframework.stereotype.*;
import com.kpisoft.user.dac.impl.dao.*;
import org.springframework.beans.factory.annotation.*;
import org.modelmapper.*;
import com.kpisoft.user.vo.*;
import com.kpisoft.user.dac.impl.entity.*;
import com.canopus.mw.*;
import com.kpisoft.user.vo.param.*;
import com.kpisoft.user.utility.*;
import com.canopus.dac.*;
import com.googlecode.genericdao.search.*;
import com.canopus.mw.dto.*;
import java.util.*;
import org.springframework.transaction.annotation.*;
import java.io.*;
import org.slf4j.*;

@Component
public class SessionStatsSummaryDataServiceImpl extends BaseDataAccessService implements SessionStatsSummaryDataService
{
    @Autowired
    private SessionStatsSummaryDao sessionStatsSummaryDao;
    private static final Logger log;
    private ModelMapper modelMapper;
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public SessionStatsSummaryDataServiceImpl() {
        this.sessionStatsSummaryDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)SessionStatsSummaryData.class, (Class)SessionStatsSummary.class);
    }
    
    @Transactional(readOnly = true)
    public Response getSessionSummary(Request request) {
        final Identifier agrPeriod = (Identifier)request.get(UMSParams.SESSION_STATS_AGGR_PERIOD.name());
        final Identifier userType = (Identifier)request.get(UMSParams.USER_TYPE.name());
        SessionStatsSummary sessionSummary = null;
        List<SessionStatsSummary> sessionStats = null;
        try {
            if (agrPeriod == null || userType == null) {
                return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_SESSION_INVALID_INPUT_002.name(), "No data object in the request"));
            }
            final Search search = new Search((Class)SessionStatsSummary.class);
            search.addFilterEqual("agrPeriod", (Object)agrPeriod.getId());
            search.addFilterEqual("userType", (Object)userType.getId());
            sessionStats = (List<SessionStatsSummary>)this.sessionStatsSummaryDao.search((ISearch)search);
            if (sessionStats == null || sessionStats.size() == 0) {
                sessionSummary = new SessionStatsSummary();
                sessionSummary.setAgrPeriod(agrPeriod.getId());
                sessionSummary.setUserType(userType.getId());
                request = new Request();
                final SessionStatsSummaryData sessionStatsSummaryData = (SessionStatsSummaryData)this.modelMapper.map((Object)sessionSummary, (Class)SessionStatsSummaryData.class);
                request.put(UMSParams.SESSION_STATS_SUMMARY_DATA.name(), (BaseValueObject)sessionStatsSummaryData);
                final Response response = this.saveSessionSummary(request);
                final Identifier id = (Identifier)response.get(UMSParams.SESSION_STATS_SUMMARY_ID.name());
                sessionStatsSummaryData.setId(id.getId());
                return this.OK(UMSParams.SESSION_STATS_SUMMARY_DATA.name(), (BaseValueObject)sessionStatsSummaryData);
            }
            sessionSummary = sessionStats.get(0);
            final SessionStatsSummaryData sessionStatsSummaryData = (SessionStatsSummaryData)this.modelMapper.map((Object)sessionSummary, (Class)SessionStatsSummaryData.class);
            return this.OK(UMSParams.SESSION_STATS_SUMMARY_DATA.name(), (BaseValueObject)sessionStatsSummaryData);
        }
        catch (Exception ex) {
            SessionStatsSummaryDataServiceImpl.log.error("Exception in SessionStatsSummaryDataServiceImpl - getSessionSummary() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_SESSION_UNABLE_TO_GET_004.name(), "Unknown error while loading Session Summary", new Object[] { ex.getMessage(), ex }));
        }
    }
    
    @Transactional
    public Response saveSessionSummary(final Request request) {
        final SessionStatsSummaryData sessionStatsSummaryData = (SessionStatsSummaryData)request.get(UMSParams.SESSION_STATS_SUMMARY_DATA.name());
        SessionStatsSummary sessionSummary = null;
        try {
            if (sessionStatsSummaryData == null && sessionStatsSummaryData == null) {
                return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_SESSION_INVALID_INPUT_002.name(), "No data object in the request"));
            }
            if (sessionStatsSummaryData.getId() != null) {
                sessionSummary = (SessionStatsSummary)this.sessionStatsSummaryDao.find((Serializable)sessionStatsSummaryData.getId());
            }
            else {
                sessionSummary = new SessionStatsSummary();
            }
            this.modelMapper.map((Object)sessionStatsSummaryData, (Object)sessionSummary);
            this.sessionStatsSummaryDao.save(sessionSummary);
            return this.OK(UMSParams.SESSION_STATS_SUMMARY_ID.name(), (BaseValueObject)new Identifier(sessionSummary.getId()));
        }
        catch (Exception ex) {
            SessionStatsSummaryDataServiceImpl.log.error("Exception in SessionStatsSummaryDataServiceImpl - saveSessionSummary() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(UserErrorCodesEnum.ERR_SESSION_UNABLE_TO_CREATE_003.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)SessionStatsSummaryDataServiceImpl.class);
    }
}
