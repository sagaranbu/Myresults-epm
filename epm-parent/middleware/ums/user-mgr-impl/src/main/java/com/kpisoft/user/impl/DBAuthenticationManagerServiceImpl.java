package com.kpisoft.user.impl;

import com.kpisoft.user.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.apache.log4j.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.user.vo.*;
import com.kpisoft.user.vo.param.*;
import com.kpisoft.user.utility.*;
import com.canopus.mw.*;
import java.util.*;
import com.kpisoft.user.openam.*;
import org.joda.time.*;
import com.kpisoft.user.impl.domain.*;
import com.canopus.mw.dto.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ AuthenticationManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class DBAuthenticationManagerServiceImpl extends BaseMiddlewareBean implements AuthenticationManagerService, CacheLoader<String, String>
{
    private static final Logger logger;
    @Autowired
    @Qualifier("dbSessionCache")
    private Cache<String, String> sessionCache;
    @Autowired
    private UserManager userManager;
    
    public DBAuthenticationManagerServiceImpl() {
        this.sessionCache = null;
    }
    
    public Response loginUser(final Request request) {
        final UserLoginData data = (UserLoginData)request.get(UMSParams.USER_LOGIN_DATA.name());
        final String userName = data.getUserName();
        final String password = data.getPassword();
        User user = null;
        try {
            user = this.userManager.getUserByLogin(userName);
        }
        catch (Exception ex) {
            DBAuthenticationManagerServiceImpl.logger.error((Object)"Failed to validate login credentials. ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_UNKNOWN_EXCEPTION_000.name(), ex.getMessage(), (Throwable)ex));
        }
        if (user == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_UNKNOWN_EXCEPTION_000.name(), "Invalid user name"));
        }
        boolean valid = false;
        if (user.getUserDetails().getPassword() != null && password != null) {
            valid = user.getUserDetails().getPassword().equals(password);
        }
        if (!valid) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_UNKNOWN_EXCEPTION_000.name(), "Invalid password"));
        }
        final String uuid = UUID.randomUUID().toString();
        final Session session = new Session(uuid);
        session.setCreatedAt(DateTime.now());
        session.setLastAccessedAt(DateTime.now());
        this.sessionCache.put(uuid, userName);
        DBAuthenticationManagerServiceImpl.logger.debug((Object)("Created session " + uuid));
        return this.OK(UMSParams.USER_SESSION.name(), (BaseValueObject)session);
    }
    
    public Response logoutUser(final Request request) {
        final Session session = (Session)request.get(UMSParams.USER_SESSION.name());
        final String userName = (String)this.sessionCache.get(session.getToken(), (CacheLoader)this);
        if (!userName.isEmpty()) {
            this.sessionCache.remove(session.getToken());
        }
        DBAuthenticationManagerServiceImpl.logger.debug((Object)("Logged out session " + session.getToken()));
        return this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new StatusResponse("OK"));
    }
    
    public Response validateSession(final Request request) {
        final Session session = (Session)request.get(UMSParams.USER_SESSION.name());
        final String userName = (String)this.sessionCache.get(session.getToken(), (CacheLoader)this);
        final boolean status = !userName.isEmpty();
        DBAuthenticationManagerServiceImpl.logger.debug((Object)("Validated session " + session.getToken()));
        return this.OK(UMSParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
    }
    
    public Response getUserProfile(final Request request) {
        final Session session = (Session)request.get(UMSParams.USER_SESSION.name());
        final String userName = (String)this.sessionCache.get(session.getToken(), (CacheLoader)this);
        if (!userName.isEmpty()) {
            final User user = this.userManager.getUserByLogin(userName);
            return this.OK(UMSParams.USER_DATA.name(), (BaseValueObject)user.getUserDetails());
        }
        return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_USER_UNKNOWN_EXCEPTION_000.name(), "Invalid session"));
    }
    
    public Response createUser(final Request request) {
        return this.OK();
    }
    
    public Response updateUser(final Request request) {
        return this.OK();
    }
    
    public Response deleteUser(final Request request) {
        return this.OK();
    }
    
    public Response getRedirectUrl(final Request request) {
        return this.OK(UMSParams.REDIRECT_URL.name(), (BaseValueObject)new StringIdentifier("ok"));
    }
    
    public StringIdentifier getServiceId() {
        return new StringIdentifier("DBAuthenticationManager");
    }
    
    public String load(final String k) {
        return "";
    }
    
    static {
        logger = Logger.getLogger((Class)AuthenticationManagerServiceImpl.class);
    }
}
