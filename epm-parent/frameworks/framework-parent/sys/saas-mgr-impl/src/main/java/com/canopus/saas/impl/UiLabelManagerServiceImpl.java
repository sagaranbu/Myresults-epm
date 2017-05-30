package com.canopus.saas.impl;

import com.canopus.saas.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import com.canopus.saas.domain.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.saas.utils.*;
import java.util.*;
import com.canopus.mw.*;
import com.canopus.saas.vo.*;
import com.canopus.mw.dto.*;
import org.slf4j.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ UiLabelManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class UiLabelManagerServiceImpl extends BaseMiddlewareBean implements UiLabelManagerService
{
    public static String LABEL_MGR_SERVICE;
    @Autowired
    private UiLabelManager labelManager;
    private static final Logger log;
    
    public UiLabelManagerServiceImpl() {
        this.labelManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response getAllUILables(final Request request) {
        try {
            final List<UiLabelBaseData> data = this.labelManager.getAllUILables();
            return this.OK(UiLabelParams.UI_LABEL_BASE_DATA_LIST.getParamName(), (BaseValueObject)new BaseValueObjectList((List)data));
        }
        catch (Exception e) {
            UiLabelManagerServiceImpl.log.error("Exception in UiLabelManagerServiceImpl - getAllUILables() : " + e);
            return this.ERROR((Exception)new MiddlewareException("LABEL-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getUILablesByLocale(final Request request) {
        final StringIdentifier localeName = (StringIdentifier)request.get(UiLabelParams.LOCALE_NAME.getParamName());
        if (localeName == null || localeName.getId() == null || localeName.getId().trim().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException("INVALID_DATA", "No input data in the request"));
        }
        try {
            final List<UiLabelLangData> data = this.labelManager.getUILablesByLocale(localeName.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(UiLabelParams.UI_LABEL_LANG_DATA_LIST.getParamName(), (BaseValueObject)list);
        }
        catch (Exception e) {
            UiLabelManagerServiceImpl.log.error("Exception in UiLabelManagerServiceImpl - getUILablesByLocale() : " + e);
            return this.ERROR((Exception)new MiddlewareException("LABEL-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getUiLabelByPageCodes(final Request request) {
        final BaseValueObjectList pageCodes = (BaseValueObjectList)request.get(UiLabelParams.UI_PAGE_CODES.getParamName());
        if (pageCodes == null || pageCodes.getValueObjectList() == null || pageCodes.getValueObjectList().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException("INVALID_DATA", "No data object in the request"));
        }
        try {
            final List<UiLabelBaseData> data = this.labelManager.getUiLabelByPageCodes(pageCodes);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(UiLabelParams.UI_LABEL_BASE_DATA_LIST.getParamName(), (BaseValueObject)list);
        }
        catch (Exception e) {
            UiLabelManagerServiceImpl.log.error("Exception in UiLabelManagerServiceImpl - getUiLabelByPageCodes() : " + e);
            return this.ERROR((Exception)new MiddlewareException("LABEL-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response searchLabelLang(final Request request) {
        final UiLabelLangData uiLabelLangData = (UiLabelLangData)request.get(UiLabelParams.UI_LABEL_LANG_DATA.getParamName());
        if (uiLabelLangData == null) {
            return this.ERROR((Exception)new MiddlewareException("INVALID_DATA", "No data object in the request"));
        }
        try {
            final List<UiLabelLangData> data = this.labelManager.searchLabelLang(uiLabelLangData);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(UiLabelParams.UI_LABEL_LANG_DATA_LIST.getParamName(), (BaseValueObject)list);
        }
        catch (Exception e) {
            UiLabelManagerServiceImpl.log.error("Exception in UiLabelManagerServiceImpl - searchLabelLang() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException("LABEL-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getUILableLangByErrorCode(final Request request) {
        final StringIdentifier errorCode = (StringIdentifier)request.get(UiLabelParams.ERROR_CODE.getParamName());
        final StringIdentifier locale = (StringIdentifier)request.get(UiLabelParams.LOCALE_NAME.getParamName());
        if (errorCode == null || errorCode.getId() == null || errorCode.getId().trim().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException("INVALID_DATA", "No input data in the request"));
        }
        try {
            final UiLabelLangData data = this.labelManager.getUiLabelLangByErrorCode(errorCode.getId(), locale.getId());
            return this.OK(UiLabelParams.UI_LABEL_LANG_DATA.getParamName(), (BaseValueObject)data);
        }
        catch (Exception e) {
            UiLabelManagerServiceImpl.log.error("Exception in UiLabelManagerServiceImpl - getUILableLangByErrorCode() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException("LABEL-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response saveLabelData(final Request request) {
        final UiLabelBaseData uiLabelData = (UiLabelBaseData)request.get(UiLabelParams.UI_BASE_DATA.getParamName());
        if (uiLabelData == null) {
            return this.ERROR((Exception)new MiddlewareException("INVALID_DATA", "No data object in the request"));
        }
        try {
            final Integer baseId = this.labelManager.saveLabelData(uiLabelData);
            return this.OK(UiLabelParams.UI_BASE_ID.getParamName(), (BaseValueObject)new Identifier(baseId));
        }
        catch (Exception e) {
            UiLabelManagerServiceImpl.log.error("Exception in UiLabelManagerServiceImpl - saveLabelData() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException("LABEL-000", e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        UiLabelManagerServiceImpl.LABEL_MGR_SERVICE = "UiLabelManagerService";
        log = LoggerFactory.getLogger((Class)UiLabelManagerServiceImpl.class);
    }
}
