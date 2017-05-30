package com.canopus.entity.impl;

import com.canopus.entity.*;
import org.springframework.stereotype.*;
import com.canopus.dac.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.entity.domain.*;
import com.canopus.mw.*;
import com.canopus.mw.dto.param.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import org.modelmapper.*;
import java.util.*;
import com.canopus.mw.dto.*;
import java.lang.reflect.*;

@Component
public class NotificationRuleDataServiceImpl extends BaseDataAccessService implements NotificationRuleDataService
{
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    
    public NotificationRuleDataServiceImpl() {
        this.genericDao = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)NotificationRuleData.class, (Class)Notification.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    @Transactional
    public Response saveNotificationRule(final Request request) {
        final NotificationRuleData notificationRuleData = (NotificationRuleData)request.get(NotificationRuleParam.NOTIFICATION_RULE.name());
        if (notificationRuleData == null) {
            return this.ERROR((Exception)new DataAccessException("ntf-err-000", "Invalid data in request"));
        }
        try {
            Notification notification;
            if (notificationRuleData.getId() != null) {
                notification = (Notification)this.genericDao.find((Class)Notification.class, (Serializable)notificationRuleData.getId());
            }
            else {
                notification = new Notification();
            }
            this.modelMapper.map((Object)notificationRuleData, (Object)notification);
            this.genericDao.save((Object)notification);
            notificationRuleData.setId(notification.getId());
            return this.OK(NotificationRuleParam.NOTIFICATION_RULEID.name(), (BaseValueObject)new Identifier(notificationRuleData.getId()));
        }
        catch (Exception e) {
            e.printStackTrace();
            return this.ERROR((Exception)new DataAccessException("ntf-err-001", "Unknown error while saving Notification Rule.", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllNotificationRules(final Request request) {
        try {
            final List<Notification> notificationRules = (List<Notification>)this.genericDao.findAll((Class)Notification.class);
            final Type listType = new TypeToken<List<NotificationRuleData>>() {}.getType();
            final List<NotificationRuleData> notificationRuleDataList = (List<NotificationRuleData>)this.modelMapper.map((Object)notificationRules, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)notificationRuleDataList);
            return this.OK(NotificationRuleParam.NOTIFICATIONRULES.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            e.printStackTrace();
            return this.ERROR((Exception)new DataAccessException("ntf-err-001", "Unknown error while getting Notification Rules.", (Throwable)e));
        }
    }
}
