package com.kpisoft.org.impl;

import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.*;
import com.kpisoft.emp.*;
import com.kpisoft.emp.vo.param.*;
import com.kpisoft.user.vo.param.*;
import com.canopus.mw.dto.param.*;
import com.canopus.mw.dto.*;
import java.util.*;
import org.slf4j.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ IRecipientResolver.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class OrganizationUnitRecipientResolverImpl extends BaseMiddlewareBean implements IRecipientResolver
{
    @Autowired
    private IServiceLocator serviceLocator;
    private static final Logger log;
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response getRecipients(final Request request) {
        final List<BaseValueObject> recipientList = new ArrayList<BaseValueObject>();
        final List<Integer> orgIdList = new ArrayList<Integer>();
        final StringIdentifier notificationChannelIdentifier = (StringIdentifier)request.get(RecipientResolverParams.NFT_CHANNEL.name());
        final BaseValueObjectList recipientResolverInput = (BaseValueObjectList)request.get(RecipientResolverParams.RCPT_RESOLVER_INPUT.name());
        if (notificationChannelIdentifier == null || recipientResolverInput == null) {
            return this.ERROR((Exception)new MiddlewareException("RES-MW-001", "No data object in the request"));
        }
        final EmployeeManagerService employeeManagerService = (EmployeeManagerService)this.serviceLocator.getService("EmployeeManagerServiceImpl");
        final String _notificationChannel = notificationChannelIdentifier.getId();
        final List<StringIdentifier> stringIdentifierOrgIds = (List<StringIdentifier>)recipientResolverInput.getValueObjectList();
        for (final StringIdentifier stringIdentifierOrgId : stringIdentifierOrgIds) {
            orgIdList.add(Integer.valueOf(stringIdentifierOrgId.getId()));
        }
        final IdentifierList orgIdIdentifierList = new IdentifierList((List)orgIdList);
        request.put(EMPParams.EMP_ORG_ID_LIST.name(), (BaseValueObject)orgIdIdentifierList);
        try {
            Response responseFromEmpService = employeeManagerService.getOrganizationListEmployees(request);
            final IdentifierList empIdList = (IdentifierList)responseFromEmpService.get(EMPParams.EMP_ID_LIST.name());
            request.put(EMPParams.EMP_ID_LIST.name(), (BaseValueObject)empIdList);
            responseFromEmpService = employeeManagerService.getUsersIdEmailFromEmployeeIdList(request);
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
            final Response response = this.OK(RecipientResolverParams.RCPT_RESP_DATA.name(), (BaseValueObject)userIDorEmailListWrapper);
            return response;
        }
        catch (Exception e) {
            OrganizationUnitRecipientResolverImpl.log.error("Exception in OrganizationUnitRecipientResolverImpl - getRecipients() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException("Exception in OrganizationUnitRecipientResolverImpl - getRecipients()", "Un known error in OrganizationUnitRecipientResolverImpl", new Object[] { e.getMessage() }));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)OrganizationUnitRecipientResolverImpl.class);
    }
}
