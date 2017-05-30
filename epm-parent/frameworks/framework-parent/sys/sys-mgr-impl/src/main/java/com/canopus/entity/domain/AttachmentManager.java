package com.canopus.entity.domain;

import com.canopus.mw.*;
import org.springframework.stereotype.*;
import com.canopus.entity.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.entity.vo.*;
import com.canopus.entity.vo.params.*;
import com.canopus.mw.dto.*;

@Component
public class AttachmentManager extends BaseDomainManager
{
    @Autowired
    private AttachmentDataService attachmentDataService;
    
    public AttachmentManager() {
        this.attachmentDataService = null;
    }
    
    public SystemAttachmentBean saveAttachmentDetails(final SystemAttachmentDetailsBean attachmentDetailsBean) {
        final Request request = new Request();
        request.put(SysParams.SYS_ATTACHMENT_DATA.name(), (BaseValueObject)attachmentDetailsBean);
        final Response response = this.attachmentDataService.saveSystemAttachmentDetails(request);
        final SystemAttachmentBean attachmentBean = (SystemAttachmentBean)response.get(SysParams.SYS_ATTACHMENT_DATA.name());
        return attachmentBean;
    }
    
    public SystemAttachmentDetailsBean getAttachmentDetails(final Integer attachmentId) {
        final Request request = new Request();
        request.put(SysParams.SYS_ATTACHMENT_ID.name(), (BaseValueObject)new Identifier(attachmentId));
        final Response response = this.attachmentDataService.getSystemAttachmentDetails(request);
        final SystemAttachmentDetailsBean attachmentDetailsBean = (SystemAttachmentDetailsBean)response.get(SysParams.SYS_ATTACHMENT_DATA.name());
        return attachmentDetailsBean;
    }
}
