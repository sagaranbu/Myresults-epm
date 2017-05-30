package com.canopus.entity.impl;

import com.canopus.entity.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.entity.domain.*;
import org.apache.log4j.*;
import com.canopus.entity.vo.params.*;
import com.canopus.mw.*;
import com.canopus.mw.dto.*;
import com.canopus.entity.vo.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ AttachmentManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class AttachmentManagerServiceImpl extends BaseMiddlewareBean implements AttachmentManagerService
{
    @Autowired
    private IServiceLocator serviceLocator;
    @Autowired
    private AttachmentManager attachmentManager;
    private static final Logger log;
    
    public AttachmentManagerServiceImpl() {
        this.attachmentManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response getSystemAttachmentDetails(final Request request) {
        final Identifier sysAttId = (Identifier)request.get(SysParams.SYS_ATTACHMENT_ID.name());
        if (sysAttId == null) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_SYS_ATT_DET_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final SystemAttachmentDetailsBean attachmentDetailsBean = this.attachmentManager.getAttachmentDetails(sysAttId.getId());
            return this.OK(SysParams.SYS_ATTACHMENT_DATA.name(), (BaseValueObject)attachmentDetailsBean);
        }
        catch (Exception e) {
            AttachmentManagerServiceImpl.log.error((Object)"Exception in AttachmentManagerServiceImpl getSystemAttachmentDetails() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_SYS_ATT_DET_UNABLE_TO_GET_004.name(), "Failed to load SystemAttachmentDetails data"));
        }
    }
    
    public Response saveSystemAttachmentDetails(final Request request) {
        final SystemAttachmentDetailsBean attachmentDetailsBean = (SystemAttachmentDetailsBean)request.get(SysParams.SYS_ATTACHMENT_DATA.name());
        if (attachmentDetailsBean == null) {
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_SYS_ATT_DET_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final SystemAttachmentBean attachmentBean = this.attachmentManager.saveAttachmentDetails(attachmentDetailsBean);
            return this.OK(SysParams.SYS_ATTACHMENT_DATA.name(), (BaseValueObject)attachmentBean);
        }
        catch (Exception e) {
            AttachmentManagerServiceImpl.log.error((Object)"Exception in AttachmentManagerServiceImpl saveSystemAttachmentDetails() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(SysErrorCodesEnum.ERR_SYS_ATT_DET_UNABLE_TO_CREATE_003.name(), "Failed to save SystemAttachmentDetails data"));
        }
    }
    
    static {
        log = Logger.getLogger((Class)AttachmentDataServiceImpl.class);
    }
}
