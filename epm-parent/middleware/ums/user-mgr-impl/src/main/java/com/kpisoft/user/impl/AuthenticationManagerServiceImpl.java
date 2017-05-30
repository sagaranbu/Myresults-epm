package com.kpisoft.user.impl;

import com.kpisoft.user.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.user.vo.param.*;
import com.kpisoft.user.utility.*;
import com.canopus.mw.*;
import com.kpisoft.user.openam.*;
import java.util.*;
import java.util.Timer;

import com.kpisoft.user.impl.domain.session.stats.*;
import com.kpisoft.user.vo.*;
import org.joda.time.*;
import com.canopus.mw.dto.*;
import com.kpisoft.user.impl.domain.*;
import com.kpisoft.user.impl.openam.commands.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ AuthenticationManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class AuthenticationManagerServiceImpl extends BaseMiddlewareBean implements AuthenticationManagerService
{
    private static final Logger logger;
    private static final String TOP_LEVEL_REALM = "/";
    @Autowired
    private SessionStatsManager sessionStatsManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private IServiceLocator serviceLocator;
    @Autowired
    private OpenAmConfiguration amConfig;
    Timer statsTimer;
    
    public AuthenticationManagerServiceImpl() {
        this.userManager = null;
        this.statsTimer = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response loginUser(final Request request) {
        final UserLoginData data = (UserLoginData)request.get(UMSParams.USER_LOGIN_DATA.name());
        try {
            final Login userLogin = new Login(data.getUserName(), data.getPassword(), this.amConfig);
            userLogin.run();
            final Session session = userLogin.getSession();
            return this.OK(UMSParams.USER_SESSION.name(), (BaseValueObject)userLogin.getSession());
        }
        catch (Exception ex) {
            AuthenticationManagerServiceImpl.logger.error((Object)"Exception while login: ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_AUTH_FAILURE_009.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    private void sessionCreated(final Session session) {
        if (this.statsTimer == null) {
            (this.statsTimer = new Timer()).schedule(new SessionStatisticsTimer(), 300000L, 300000L);
        }
        session.setCreatedAt(new DateTime());
        session.setLastAccessedAt(new DateTime());
        final SessionStatsSummary sessioStatsSummary = this.sessionStatsManager.getCurrentSessioStatsSummary();
        final SessionStatsSummaryData summaryData = sessioStatsSummary.getSessionStatsSummaryDetails();
        summaryData.setCurrentSessionCnt(summaryData.getCurrentSessionCnt() + 1);
        if (summaryData.getCurrentSessionCnt() > summaryData.getPeakSessionCnt()) {
            summaryData.setPeakSessionCnt(summaryData.getCurrentSessionCnt());
        }
        summaryData.setTotalSessionCount(summaryData.getTotalSessionCount() + 1);
    }
    
    private void sessionDestroyed(final Session session) {
        final SessionStatsSummary sessioStatsSummary = this.getSessionStatsManager().getCurrentSessioStatsSummary();
        final SessionStatsSummaryData summaryData = sessioStatsSummary.getSessionStatsSummaryDetails();
        summaryData.setCurrentSessionCnt(summaryData.getCurrentSessionCnt() - 1);
        final DateTime now = new DateTime();
        final Minutes minutes = Minutes.minutesBetween((ReadableInstant)session.getCreatedAt(), (ReadableInstant)now);
        summaryData.setTotalSessionDuration(summaryData.getTotalSessionDuration() + minutes.getMinutes());
        summaryData.setAvgSessionDuration(summaryData.getTotalSessionDuration() / summaryData.getTotalSessionCount());
        if (minutes.getMinutes() < summaryData.getMinSessionDuration()) {
            summaryData.setMinSessionDuration(minutes.getMinutes());
        }
        if (minutes.getMinutes() > summaryData.getMaxSessionDuration()) {
            summaryData.setMaxSessionDuration(minutes.getMinutes());
        }
    }
    
    public Response logoutUser(final Request request) {
        try {
            final Session session = (Session)request.get(UMSParams.USER_SESSION.name());
            final Logout userLogout = new Logout(session, this.amConfig);
            userLogout.run();
            return this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new StatusResponse("OK"));
        }
        catch (Exception ex) {
            AuthenticationManagerServiceImpl.logger.error((Object)"Exception while logout: ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_AUTH_FAILURE_009.name(), ex.getMessage()));
        }
    }
    
    public Response createUser(final Request request) {
        final UserLoginData data = (UserLoginData)request.get(UMSParams.USER_LOGIN_DATA.name());
        Session adminSession = (Session)request.get(UMSParams.USER_SESSION.name());
        if (adminSession == null) {
            adminSession = this.getAdminSession();
        }
        try {
            final Integer tenantId = data.getTenantId();
            final String realm = this.checkRealm(tenantId, adminSession);
            final CreateUser createUser = new CreateUser(data.getUserName(), data.getPassword(), realm, adminSession, this.amConfig);
            createUser.run();
            return this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new StatusResponse("OK"));
        }
        catch (Exception ex) {
            AuthenticationManagerServiceImpl.logger.error((Object)"Exception while creating user: ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_AUTH_UNABLE_TO_CREATE_003.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response deleteUser(final Request request) {
        final StringIdentifier user = (StringIdentifier)request.get(UMSParams.USER_NAME.name());
        Session adminSession = (Session)request.get(UMSParams.USER_SESSION.name());
        if (adminSession == null) {
            adminSession = this.getAdminSession();
        }
        try {
            final String realm = this.checkRealm(1, adminSession);
            final DeleteUser delUser = new DeleteUser(user.getId(), realm, adminSession, this.amConfig);
            delUser.run();
            return this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new StatusResponse("OK"));
        }
        catch (Exception ex) {
            AuthenticationManagerServiceImpl.logger.error((Object)"Exception while deleting user: ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_AUTH_TO_DELETE_006.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response updateUser(final Request request) {
        final UserLoginData data = (UserLoginData)request.get(UMSParams.USER_LOGIN_DATA.name());
        Session adminSession = (Session)request.get(UMSParams.USER_SESSION.name());
        if (adminSession == null) {
            adminSession = this.getAdminSession();
        }
        try {
            final Integer tenantId = data.getTenantId();
            final String realm = this.checkRealm(tenantId, adminSession);
            final UpdateUser updateUser = new UpdateUser(data.getUserName(), data.getPassword(), realm, adminSession, this.amConfig);
            updateUser.run();
            return this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new StatusResponse("OK"));
        }
        catch (Exception ex) {
            AuthenticationManagerServiceImpl.logger.error((Object)"Exception while updating user: ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_AUTH_UNABLE_TO_UPDATE_005.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response validateSession(final Request request) {
        final Session session = (Session)request.get(UMSParams.USER_SESSION.name());
        try {
            final ValidateSession validateSession = new ValidateSession(session, this.amConfig);
            final String status = validateSession.run();
            return this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse((boolean)Boolean.valueOf(status)));
        }
        catch (Exception ex) {
            AuthenticationManagerServiceImpl.logger.error((Object)"Exception while validating session: ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_AUTH_UNKNOWN_EXCEPTION_000.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response getUserProfile(final Request request) {
        final Session session = (Session)request.get(UMSParams.USER_SESSION.name());
        String userName = null;
        try {
            final GetUserProfile userProfile = new GetUserProfile(session, this.amConfig);
            final String result = userProfile.run();
            final String[] split;
            final String[] entries = split = result.split("\n");
            for (final String entry : split) {
                final String[] keyValue = entry.split("=");
                if (keyValue.length > 0 && keyValue[0].equals("userdetails.attribute.value")) {
                    userName = keyValue[1];
                }
            }
            if (userName == null) {
                AuthenticationManagerServiceImpl.logger.warn((Object)"Uer name not resolved from token. Assuming amadmin");
                userName = "amadmin";
            }
            final User user = this.userManager.getUserByLogin(userName);
            if (user != null) {
                return this.OK(UMSParams.USER_DATA.name(), (BaseValueObject)user.getUserDetails());
            }
            throw new MiddlewareException(UserErrorCodesEnum.ERR_USER_DOES_NOT_EXIST_001.name(), "User not found for given session token");
        }
        catch (Exception ex) {
            AuthenticationManagerServiceImpl.logger.error((Object)"Exception while loading user profile for session token: ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_DOES_NOT_EXIST_001.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    private String checkRealm(final Integer tenantId, final Session adminSession) {
        if (this.amConfig.isRealmEnabled()) {
            final String subrealm = tenantId.toString();
            final boolean userRealmStatus = this.getUserRealmStatus(subrealm, adminSession);
            if (!userRealmStatus) {
                this.createNewRealm(subrealm, adminSession);
            }
            return "/" + subrealm;
        }
        return "/";
    }
    
    private boolean getUserRealmStatus(final String realm, final Session adminSession) {
        try {
            final ReadRealms rr = new ReadRealms(realm, adminSession, this.amConfig);
            rr.run();
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    private boolean createNewRealm(final String realm, final Session adminSession) {
        try {
            final CreateRealm cr = new CreateRealm(realm, adminSession, this.amConfig);
            cr.run();
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public SessionStatsManager getSessionStatsManager() {
        return this.sessionStatsManager;
    }
    
    private Session getAdminSession() {
        try {
            final Login login = new Login(this.amConfig.getAdminLogin(), this.amConfig.getAdminPassword(), this.amConfig);
            login.run();
            final Session session = login.getSession();
            return session;
        }
        catch (Exception ex) {
            throw new MiddlewareException(UserErrorCodesEnum.ERR_AUTH_UNKNOWN_EXCEPTION_000.name(), "Failed to login to SSO service. Please check the URL and admin credentials.", (Throwable)ex);
        }
    }
    
    public Response getRedirectUrl(final Request request) {
        return this.OK(UMSParams.REDIRECT_URL.name(), (BaseValueObject)new StringIdentifier(this.amConfig.getRedirectUrl()));
    }
    
    static {
        logger = Logger.getLogger((Class)AuthenticationManagerServiceImpl.class);
    }
}
