package com.canopus.entity.impl;

import com.canopus.entity.*;
import org.springframework.stereotype.*;
import com.canopus.dac.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import com.canopus.mw.*;
import com.canopus.entity.domain.*;
import com.canopus.mw.dto.param.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import com.googlecode.genericdao.search.*;
import org.modelmapper.*;
import java.lang.reflect.*;
import com.canopus.entity.vo.params.*;
import com.canopus.mw.dto.*;
import java.util.*;

@Component
public class DashboardMessageDataServiceImpl extends BaseDataAccessService implements DashboardMessageDataService
{
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    private static final Logger LOGGER;
    
    public DashboardMessageDataServiceImpl() {
        this.genericDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)DashboardMessage.class, (Class)DashboardMessageData.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)DashboardMessageUser.class, (Class)DashboardMessageUserData.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    @Transactional(rollbackFor = { Exception.class })
    public Response saveDashboardMessage(final Request request) {
        final DashboardMessageData dashboardMessageData = (DashboardMessageData)request.get(DashboardMessageParam.DASHBOARD_MESSAGE.name());
        if (dashboardMessageData == null) {
            return this.ERROR((Exception)new DataAccessException("err-invalid-data", "Invalid data in request"));
        }
        try {
            final DashboardMessage dashboardMessage = new DashboardMessage();
            this.modelMapper.map((Object)dashboardMessageData, (Object)dashboardMessage);
            this.genericDao.save((Object)dashboardMessage);
            final List<Integer> recipientUserIds = (List<Integer>)dashboardMessageData.getRecipientUserIds();
            if (dashboardMessageData == null || dashboardMessageData.getId() == null || dashboardMessageData.getId() <= 0) {
                if (recipientUserIds != null && recipientUserIds.size() > 0) {
                    for (final Integer recipientUserId : recipientUserIds) {
                        final DashboardMessageUser msgUser = new DashboardMessageUser();
                        msgUser.setDashboardMessageId(dashboardMessage.getId());
                        msgUser.setRecipientUserId(recipientUserId);
                        msgUser.setIsDisabled(false);
                        this.genericDao.save((Object)msgUser);
                    }
                }
            }
            else {
                final Map<Integer, DashboardMessageUserData> existingRecipientUserIds = new HashMap<Integer, DashboardMessageUserData>();
                final List<Integer> addRecipientUserIds = new ArrayList<Integer>();
                request.put(DashboardMessageParam.DASHBOARDMESSAGE_ID.name(), (BaseValueObject)new Identifier(dashboardMessage.getId()));
                final BaseValueObjectList bList = (BaseValueObjectList)this.searchDUMByDId(request).get(DashboardMessageParam.DASHBOARD_USR_MSGS.name());
                if (bList != null) {
                    final List<DashboardMessageUserData> dumList = (List<DashboardMessageUserData>)bList.getValueObjectList();
                    if (!dumList.isEmpty()) {
                        for (final DashboardMessageUserData rData : dumList) {
                            existingRecipientUserIds.put(rData.getRecipientUserId(), rData);
                            addRecipientUserIds.add(rData.getRecipientUserId());
                        }
                    }
                }
                for (final Integer rId : recipientUserIds) {
                    if (!addRecipientUserIds.contains(rId)) {
                        final DashboardMessageUser msgUser2 = new DashboardMessageUser();
                        msgUser2.setDashboardMessageId(dashboardMessage.getId());
                        msgUser2.setRecipientUserId(rId);
                        msgUser2.setIsDisabled(false);
                        this.genericDao.save((Object)msgUser2);
                    }
                    else {
                        addRecipientUserIds.remove(rId);
                    }
                }
                for (final Integer rId : addRecipientUserIds) {
                    final DashboardMessageUserData data = existingRecipientUserIds.get(rId);
                    if (data != null && data.getId() != null) {
                        this.genericDao.removeById((Class)DashboardMessageUser.class, (Serializable)data.getId());
                    }
                }
            }
            return this.OK(DashboardMessageParam.DASHBOARDMESSAGE_ID.name(), (BaseValueObject)new Identifier(dashboardMessage.getId()));
        }
        catch (Exception e) {
            DashboardMessageDataServiceImpl.LOGGER.error((Object)"Exception in DashboardMessageDataServiceImpl - saveDashboardMessage(): ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException("err-unknown-exp", "Unknown error while saving Dashboard Message.", (Throwable)e));
        }
    }
    
    @Transactional
    public Response getDashboardMessages(final Request request) {
        final Identifier userId = (Identifier)request.get(DashboardMessageParam.USER_ID.name());
        if (userId.getId() == null) {
            return this.ERROR((Exception)new DataAccessException("err-invalid-data", "Invalid data in request"));
        }
        try {
            final Search search = new Search((Class)DashboardMessageUser.class);
            search.addFilterEqual("recipientUserId", (Object)userId.getId());
            search.addFilterEqual("isDisabled", (Object)false);
            search.addFilterNotNull("dashboardMessageId");
            final List<DashboardMessageUser> dashboardMessageUsers = (List<DashboardMessageUser>)this.genericDao.search((ISearch)search);
            final BaseValueObjectList dashboardMsgDataList = new BaseValueObjectList();
            if (dashboardMessageUsers != null) {
                final List<DashboardMessage> dashboardMsgList = new ArrayList<DashboardMessage>();
                for (final DashboardMessageUser dashboardMessageUser : dashboardMessageUsers) {
                    final DashboardMessage msg = (DashboardMessage)this.genericDao.find((Class)DashboardMessage.class, (Serializable)dashboardMessageUser.getDashboardMessageId());
                    dashboardMsgList.add(msg);
                }
                final Type listType = new TypeToken<List<DashboardMessageData>>() {}.getType();
                final List<DashboardMessageData> lvo = (List<DashboardMessageData>)this.modelMapper.map((Object)dashboardMsgList, listType);
                dashboardMsgDataList.setValueObjectList((List)lvo);
            }
            return this.OK(DashboardMessageParam.DASHBOARD_MSGS.name(), (BaseValueObject)dashboardMsgDataList);
        }
        catch (Exception e) {
            DashboardMessageDataServiceImpl.LOGGER.error((Object)"Exception in DashboardMessageDataServiceImpl - getDashboardMessages(): ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException("err-unknown-exp", "Unknown error while getting Dashboard Message.", (Throwable)e));
        }
    }
    
    @Transactional
    public Response cancelUserDashboardMessage(final Request request) {
        final Identifier userId = (Identifier)request.get(DashboardMessageParam.USER_ID.name());
        final Identifier dashboardMessageId = (Identifier)request.get(DashboardMessageParam.DASHBOARDMESSAGE_ID.name());
        if (userId.getId() == null || dashboardMessageId.getId() == null) {
            return this.ERROR((Exception)new DataAccessException("err-invalid-data", "Invalid data in request"));
        }
        final Search search = new Search((Class)DashboardMessageUser.class);
        search.addFilterEqual("recipientUserId", (Object)userId.getId());
        search.addFilterEqual("isDisabled", (Object)false);
        search.addFilterEqual("dashboardMessageId", (Object)dashboardMessageId.getId());
        final List<DashboardMessageUser> dashboardMessageUsers = (List<DashboardMessageUser>)this.genericDao.search((ISearch)search);
        if (dashboardMessageUsers == null) {
            return this.ERROR((Exception)new DataAccessException("err-invalid-data", "No record found for given userID and messageID."));
        }
        if (dashboardMessageUsers.size() == 0) {
            return this.ERROR((Exception)new DataAccessException("err-invalid-data", "No record found for given userID and messageID."));
        }
        final DashboardMessageUser dashboardMessageUser = dashboardMessageUsers.get(0);
        dashboardMessageUser.setIsDisabled(true);
        this.genericDao.save((Object)dashboardMessageUser);
        final DashboardMessageUserData msgUserData = new DashboardMessageUserData();
        this.modelMapper.map((Object)dashboardMessageUser, (Object)msgUserData);
        return this.OK(DashboardMessageParam.DASHBOARD_MSGUSR.name(), (BaseValueObject)msgUserData);
    }
    
    @Transactional(readOnly = true)
    public Response getAllDashboardMessages(final Request request) {
        final List<DashboardMessage> dashboardMsgList = (List<DashboardMessage>)this.genericDao.findAll((Class)DashboardMessage.class);
        final Type listType = new TypeToken<List<DashboardMessageData>>() {}.getType();
        final List<DashboardMessageData> dmList = (List<DashboardMessageData>)this.modelMapper.map((Object)dashboardMsgList, listType);
        final BaseValueObjectList bList = new BaseValueObjectList();
        bList.setValueObjectList((List)dmList);
        return this.OK(DashboardMessageParam.DASHBOARD_MSGS.name(), (BaseValueObject)bList);
    }
    
    @Transactional(readOnly = true)
    public Response getAllDashboardUserMessages(final Request request) {
        final List<DashboardMessageUser> dashboardMsgList = (List<DashboardMessageUser>)this.genericDao.findAll((Class)DashboardMessageUser.class);
        final Type listType = new TypeToken<List<DashboardMessageUserData>>() {}.getType();
        final List<DashboardMessageUserData> dmList = (List<DashboardMessageUserData>)this.modelMapper.map((Object)dashboardMsgList, listType);
        final BaseValueObjectList bList = new BaseValueObjectList();
        bList.setValueObjectList((List)dmList);
        return this.OK(DashboardMessageParam.DASHBOARD_MSGS.name(), (BaseValueObject)bList);
    }
    
    @Transactional(readOnly = true)
    public Response getDashboardMessage(final Request request) {
        final Identifier dashboardId = (Identifier)request.get(DashboardMessageParam.DASHBOARDMESSAGE_ID.name());
        if (dashboardId == null || dashboardId.getId() == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_DASHBOARD_ID.name(), "Invalid data"));
        }
        try {
            final DashboardMessage message = (DashboardMessage)this.genericDao.find((Class)DashboardMessage.class, (Serializable)dashboardId.getId());
            final DashboardMessageData messageData = (DashboardMessageData)this.modelMapper.map((Object)message, (Class)DashboardMessageData.class);
            return this.OK(DashboardMessageParam.DASHBOARD_MESSAGE.name(), (BaseValueObject)messageData);
        }
        catch (Exception e) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_DASHBOARD_ID.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchDUMByDId(final Request request) {
        final Identifier dashboardId = (Identifier)request.get(DashboardMessageParam.DASHBOARDMESSAGE_ID.name());
        final Identifier userId = (Identifier)request.get(DashboardMessageParam.USER_ID.name());
        if (dashboardId == null || dashboardId.getId() == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_DASHBOARD_ID.name(), "Invalid data"));
        }
        try {
            final Search search = new Search((Class)DashboardMessageUser.class);
            search.addFilterEqual("dashboardMessageId", (Object)dashboardId.getId());
            if (userId != null && userId.getId() != null && userId.getId() > 0) {
                search.addFilterEqual("recipientUserId", (Object)userId.getId());
            }
            final List<DashboardMessageUser> dashboardUserMsgList = (List<DashboardMessageUser>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<DashboardMessageUserData>>() {}.getType();
            final List<DashboardMessageUserData> dmList = (List<DashboardMessageUserData>)this.modelMapper.map((Object)dashboardUserMsgList, listType);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)dmList);
            return this.OK(DashboardMessageParam.DASHBOARD_USR_MSGS.name(), (BaseValueObject)bList);
        }
        catch (Exception e) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_DASHBOARD_ID.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getDashboardMessageByIds(final Request request) {
        final IdentifierList dashboardIds = (IdentifierList)request.get(DashboardMessageParam.DASHBOARD_MESSAGE_IDS.name());
        if (dashboardIds == null || dashboardIds.getIdsList() == null || dashboardIds.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_DASHBOARD_ID.name(), "Invalid data"));
        }
        try {
            final Search search = new Search((Class)DashboardMessage.class);
            search.addFilterIn("id", (Collection)dashboardIds.getIdsList());
            final List<DashboardMessage> dashboardMsgList = (List<DashboardMessage>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<DashboardMessageData>>() {}.getType();
            final List<DashboardMessageData> dmList = (List<DashboardMessageData>)this.modelMapper.map((Object)dashboardMsgList, listType);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)dmList);
            return this.OK(DashboardMessageParam.DASHBOARD_MSGS.name(), (BaseValueObject)bList);
        }
        catch (Exception e) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_DASHBOARD_ID.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        LOGGER = Logger.getLogger((Class)DashboardMessageDataServiceImpl.class);
    }
}
