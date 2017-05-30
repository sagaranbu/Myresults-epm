package com.kpisoft.user.impl;

import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.user.utility.*;
import com.canopus.mw.*;
import com.kpisoft.user.*;
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
public class RoleRecipientResolverImpl extends BaseMiddlewareBean implements IRecipientResolver
{
    private static final Logger log;
    @Autowired
    private IServiceLocator serviceLocator;
    
    public Response getRecipients(final Request request) {
        final List<BaseValueObject> recipientList = new ArrayList<BaseValueObject>();
        final List<Integer> roleIdList = new ArrayList<Integer>();
        final StringIdentifier notificationChannelIdentifier = (StringIdentifier)request.get(RecipientResolverParams.NFT_CHANNEL.name());
        final BaseValueObjectList recipientResolverInput = (BaseValueObjectList)request.get(RecipientResolverParams.RCPT_RESOLVER_INPUT.name());
        if (notificationChannelIdentifier == null || recipientResolverInput == null) {
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_RECIPIENT_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        final UserManagerService userManagerService = (UserManagerService)this.serviceLocator.getService("UserManagerServiceImpl");
        final String _notificationChannel = notificationChannelIdentifier.getId();
        final List<StringIdentifier> stringIdentifierRoleIds = (List<StringIdentifier>)recipientResolverInput.getValueObjectList();
        for (final StringIdentifier stringIdentifierRoleId : stringIdentifierRoleIds) {
            roleIdList.add(Integer.valueOf(stringIdentifierRoleId.getId()));
        }
        final IdentifierList roleIdIdentifierList = new IdentifierList((List)roleIdList);
        request.put(UMSParams.USER_ROLE_ID_LIST.name(), (BaseValueObject)roleIdIdentifierList);
        try {
            final Response responseFromUMSService = userManagerService.getUsersIdEmailFromRoleList(request);
            final BaseValueObjectMap baseValueObjectMap = (BaseValueObjectMap)responseFromUMSService.get(UMSParams.USER_DATA_ID_EMAIL.name());
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
            RoleRecipientResolverImpl.log.error("Exception in RoleResolverServiceImpl - getRecipients() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(UserErrorCodesEnum.ERR_RECIPIENT_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    static {
        log = LoggerFactory.getLogger((Class)RoleRecipientResolverImpl.class);
    }
}
