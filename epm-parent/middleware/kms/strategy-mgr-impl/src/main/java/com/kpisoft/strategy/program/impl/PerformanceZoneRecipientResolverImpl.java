package com.kpisoft.strategy.program.impl;

import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.strategy.utility.*;
import com.canopus.mw.*;
import com.kpisoft.strategy.program.service.*;
import com.kpisoft.emp.*;
import com.kpisoft.user.*;
import com.kpisoft.strategy.params.*;
import com.kpisoft.strategy.program.vo.*;
import com.kpisoft.emp.vo.param.*;
import com.kpisoft.user.vo.param.*;
import com.canopus.mw.dto.param.*;
import com.canopus.mw.dto.*;
import java.util.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ IRecipientResolver.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class PerformanceZoneRecipientResolverImpl extends BaseMiddlewareBean implements IRecipientResolver
{
    public static final String NOTIFICATION_DASHBOARD = "Dashboard";
    public static final String NOTIFICATION_EMAIL = "Email";
    public static final String NOTIFICATION_SMS = "SMS";
    private static final Logger log;
    @Autowired
    private IServiceLocator serviceLocator;
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response getRecipients(Request request) {
        final List<BaseValueObject> recipientList = new ArrayList<BaseValueObject>();
        final List<Integer> progZoneList = new ArrayList<Integer>();
        final List<Integer> empIdList = new ArrayList<Integer>();
        final StringIdentifier notificationChannelIdentifier = (StringIdentifier)request.get(RecipientResolverParams.NFT_CHANNEL.name());
        final BaseValueObjectList recipientResolverInput = (BaseValueObjectList)request.get(RecipientResolverParams.RCPT_RESOLVER_INPUT.name());
        if (notificationChannelIdentifier == null || recipientResolverInput == null) {
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_RECIPIENT_RESOLVER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final ProgramManagerService performanceProgramManagerService = (ProgramManagerService)this.serviceLocator.getService("ProgramManagerServiceImpl");
            final EmployeeManagerService employeeManagerService = (EmployeeManagerService)this.serviceLocator.getService("EmployeeManagerServiceImpl");
            final UserManagerService userManagerService = (UserManagerService)this.serviceLocator.getService("UserManagerServiceImpl");
            final String _notificationChannel = notificationChannelIdentifier.getId();
            final List<StringIdentifier> stringIdentifierProgZoneIds = (List<StringIdentifier>)recipientResolverInput.getValueObjectList();
            for (final StringIdentifier stringIdentifierProgZoneId : stringIdentifierProgZoneIds) {
                progZoneList.add(Integer.valueOf(stringIdentifierProgZoneId.getId()));
            }
            final IdentifierList zoneIdentifierList = new IdentifierList((List)progZoneList);
            request.put(ProgramParams.PROGRAM_ZONE_ID_LIST.name(), (BaseValueObject)zoneIdentifierList);
            Response response = performanceProgramManagerService.getProgramsByZoneIds(request);
            final IdentifierList identifierList = (IdentifierList)response.get(ProgramParams.PROG_ID_LIST.name());
            request = new Request();
            request.put(ProgramParams.STRATEGY_PROGRAM_ID_LIST.name(), (BaseValueObject)identifierList);
            response = performanceProgramManagerService.getProgramsByIds(request);
            final BaseValueObjectList bList = (BaseValueObjectList)response.get(ProgramParams.STRATEGY_PROGRAM_LIST.name());
            final List<StrategyProgramBean> prgList = (List<StrategyProgramBean>)bList.getValueObjectList();
            if (prgList != null && prgList.size() > 0) {
                for (final StrategyProgramBean strategyProgramBean2 : prgList) {
                    final List<StrategyProgramPolicyRuleBean> strategyProgramPolicyRuleBeanList = (List<StrategyProgramPolicyRuleBean>)strategyProgramBean2.getStrategyProgramPolicyRuleMetaData();
                    for (final StrategyProgramPolicyRuleBean strategyProgramPolicyRuleBean : strategyProgramPolicyRuleBeanList) {
                        empIdList.add(strategyProgramPolicyRuleBean.getEmployeeId());
                        request.put(EMPParams.EMP_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)empIdList));
                        final Response responseFromEmpService = employeeManagerService.getUsersIdEmailFromEmployeeIdList(request);
                        final BaseValueObjectMap baseValueObjectMap = (BaseValueObjectMap)responseFromEmpService.get(UMSParams.USER_DATA_ID_EMAIL.name());
                        final Map<IdentifierList, BaseValueObjectList> map = (Map<IdentifierList, BaseValueObjectList>)baseValueObjectMap.getBaseValueMap();
                        IdentifierList userIdentifierList = null;
                        BaseValueObjectList userEmailStringIdentifierList = null;
                        final List<Integer> userIds = new ArrayList<Integer>();
                        final List<BaseValueObject> usersEmail = new ArrayList<BaseValueObject>();
                        for (final Map.Entry<IdentifierList, BaseValueObjectList> entry : map.entrySet()) {
                            userIdentifierList = entry.getKey();
                            userEmailStringIdentifierList = entry.getValue();
                            userIds.addAll(userIdentifierList.getIdsList());
                            usersEmail.addAll(userEmailStringIdentifierList.getValueObjectList());
                        }
                        if (_notificationChannel.equals(NotificationChannelParams.EMAIL.name())) {
                            recipientList.addAll(usersEmail);
                        }
                        else if (_notificationChannel.equals(NotificationChannelParams.DASHBOARD.name())) {
                            for (final Integer userId : userIds) {
                                recipientList.add((BaseValueObject)new Identifier(userId));
                            }
                        }
                        final BaseValueObjectList userIDorEmailListWrapper = new BaseValueObjectList();
                        userIDorEmailListWrapper.setValueObjectList((List)recipientList);
                        response = this.OK(RecipientResolverParams.RCPT_RESP_DATA.name(), (BaseValueObject)userIDorEmailListWrapper);
                    }
                }
            }
            return response;
        }
        catch (Exception e) {
            PerformanceZoneRecipientResolverImpl.log.error((Object)"Exception in PerformanceZoneRecipientResolverImpl - getRecipients() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_RECIPIENT_RESOLVER_UNABLE_TO_GET_004.name(), "Failed to load the recipients data", new Object[] { e.getMessage() }));
        }
    }
    
    static {
        log = Logger.getLogger((Class)PerformanceZoneRecipientResolverImpl.class);
    }
}
