package com.canopus.entity.impl;

import com.canopus.entity.*;
import org.springframework.stereotype.*;
import com.canopus.dac.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import org.modelmapper.*;
import org.apache.log4j.*;
import com.canopus.entity.domain.*;
import com.canopus.mw.*;
import com.canopus.entity.vo.params.*;
import com.canopus.dac.*;
import java.io.*;
import com.canopus.mw.dto.*;
import org.springframework.transaction.annotation.*;
import com.canopus.entity.vo.*;

@Component
public class AttachmentDataServiceImpl extends BaseDataAccessService implements AttachmentDataService
{
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public AttachmentDataServiceImpl() {
        this.genericDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)SystemAttachmentDetailsBean.class, (Class)SystemAttachmentDetails.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    @Transactional(readOnly = true)
    public Response getSystemAttachmentDetails(final Request request) {
        final Identifier identifier = (Identifier)request.get(SysParams.SYS_ATTACHMENT_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_SYS_ATT_DET_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        SystemAttachmentDetails systemAttachmentDetails = null;
        try {
            systemAttachmentDetails = (SystemAttachmentDetails)this.genericDao.find((Class)SystemAttachmentDetails.class, (Serializable)identifier.getId());
        }
        catch (Exception e) {
            AttachmentDataServiceImpl.log.error((Object)"Exception in SystemAttachmentDetailsDataServiceImpl - getSystemAttachmentDetails() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_SYS_ATT_DET_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
        if (systemAttachmentDetails == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_SYS_ATT_DET_DOES_NOT_EXIST_001.name(), "system id {0} does not exist.", new Object[] { identifier }));
        }
        final SystemAttachmentDetailsBean systemAttachmentDetailsBean = (SystemAttachmentDetailsBean)this.modelMapper.map((Object)systemAttachmentDetails, (Class)SystemAttachmentDetailsBean.class);
        return this.OK(SysParams.SYS_ATTACHMENT_DATA.name(), (BaseValueObject)systemAttachmentDetailsBean);
    }
    
    @Transactional
    public Response saveSystemAttachmentDetails(final Request request) {
        final SystemAttachmentDetailsBean attachmentDetailsBean = (SystemAttachmentDetailsBean)request.get(SysParams.SYS_ATTACHMENT_DATA.name());
        if (attachmentDetailsBean == null) {
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_SYS_ATT_DET_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        final SystemAttachmentDetails attachmentDetails = new SystemAttachmentDetails();
        SystemAttachmentBean attachmentBean = null;
        try {
            this.modelMapper.map((Object)attachmentDetailsBean, (Object)attachmentDetails);
            this.genericDao.save((Object)attachmentDetails);
            attachmentBean = new SystemAttachmentBean();
            this.modelMapper.map((Object)attachmentDetails, (Object)attachmentBean);
            return this.OK(SysParams.SYS_ATTACHMENT_DATA.name(), (BaseValueObject)attachmentBean);
        }
        catch (Exception e) {
            AttachmentDataServiceImpl.log.error((Object)"Exception in SystemAttachmentDetailsDataServiceImpl saveSystemAttachmentDetails() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(SysErrorCodesEnum.ERR_SYS_ATT_DET_UNABLE_TO_CREATE_003.name(), "Failed to save system attachment details data"));
        }
    }
    
    static {
        log = Logger.getLogger((Class)AttachmentDataServiceImpl.class);
    }
}
