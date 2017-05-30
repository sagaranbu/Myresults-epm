package com.kpisoft.kpi.impl;

import com.kpisoft.kpi.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import com.kpisoft.kpi.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import com.kpisoft.kpi.utility.*;
import com.canopus.mw.*;
import com.kpisoft.kpi.vo.param.*;
import java.util.*;
import com.kpisoft.kpi.vo.*;
import com.canopus.mw.dto.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ KpiTargetManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class KpiTargetManagerServiceImpl extends BaseMiddlewareBean implements KpiTargetManagerService
{
    @Autowired
    private KpiTagretManager kpiTargetManager;
    private static final Logger log;
    
    public KpiTargetManagerServiceImpl() {
        this.kpiTargetManager = null;
    }
    
    public StringIdentifier getServiceId() {
        return null;
    }
    
    public Response createKpiTarget(final Request request) {
        final KpiTargetBean kpiTrgBean = (KpiTargetBean)request.get(KpiParams.KPI_TARGET.name());
        if (kpiTrgBean == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final KpiTargetBean kpiTrgBeanRes = this.kpiTargetManager.createKpiTarget(kpiTrgBean);
            final Identifier identifier = new Identifier();
            identifier.setId(kpiTrgBeanRes.getId());
            return this.OK(KpiParams.KPI_TARGET_ID.name(), (BaseValueObject)identifier);
        }
        catch (Exception ex) {
            KpiTargetManagerServiceImpl.log.error((Object)"Exception in KpiTargetManagerServiceImpl - createKpiTarget() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_UNABLE_TO_CREATE_003.name(), "Failed to create kpi target", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getKpiTarget(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.KPI_TARGET_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final KpiTargetBean kpiTargetBean = this.kpiTargetManager.getKpiTarget(id.getId());
            return this.OK(KpiParams.KPI_TARGET.name(), (BaseValueObject)kpiTargetBean);
        }
        catch (Exception ex) {
            KpiTargetManagerServiceImpl.log.error((Object)"Exception in KpiTargetManagerServiceImpl - getKpiTarget() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_UNABLE_TO_GET_004.name(), "Failed to get kpi target", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response updateKpiTarget(final Request request) {
        KpiTargetBean kpiTrgBean = (KpiTargetBean)request.get(KpiParams.KPI_TARGET.name());
        if (kpiTrgBean == null) {
            return this.ERROR((Exception)new MiddlewareException("INVALID_KPI_TARGET_DATA", "No data object in the request"));
        }
        try {
            kpiTrgBean = this.kpiTargetManager.updateKpiTarget(kpiTrgBean);
            final Identifier identifier = new Identifier();
            identifier.setId(kpiTrgBean.getId());
            return this.OK(KpiParams.KPI_TARGET_ID.name(), (BaseValueObject)identifier);
        }
        catch (Exception ex) {
            KpiTargetManagerServiceImpl.log.error((Object)"Exception in KpiTargetManagerServiceImpl - updateKpiTarget() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_UNABLE_TO_UPDATE_005.name(), "Failed to update kpi target", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response removeKpiTarget(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.KPI_TARGET_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Boolean isRes = this.kpiTargetManager.removeKpiTarget(id.getId());
            final BooleanResponse res = new BooleanResponse((boolean)isRes);
            return this.OK(KpiParams.IS_KPI_TARGET.name(), (BaseValueObject)res);
        }
        catch (Exception ex) {
            KpiTargetManagerServiceImpl.log.error((Object)"Exception in KpiTargetManagerServiceImpl - removeKpiTarget() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_UNABLE_TO_DELETE_006.name(), "Failed to delete kpi target", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response createScaleValue(final Request request) {
        ScaleValueBean kpiMasterScaleBean = (ScaleValueBean)request.get(TargetParams.TARGET_SCALE_VALUE.name());
        if (kpiMasterScaleBean == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_SCALE_VALUE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            kpiMasterScaleBean = this.kpiTargetManager.createScaleValue(kpiMasterScaleBean);
            final Identifier identifier = new Identifier();
            identifier.setId(kpiMasterScaleBean.getId());
            return this.OK(TargetParams.TARGET_SCALE_VALUE_ID.name(), (BaseValueObject)identifier);
        }
        catch (Exception ex) {
            KpiTargetManagerServiceImpl.log.error((Object)"Exception in KpiTargetManagerServiceImpl - createScaleValue() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_SCALE_VALUE_UNABLE_TO_CREATE_003.name(), "Failed to create target scale", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getScaleValue(final Request request) {
        final Identifier id = (Identifier)request.get(TargetParams.TARGET_SCALE_VALUE_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_SCALE_VALUE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final ScaleValueBean kpiMasterScaleBean = this.kpiTargetManager.getScaleValue(id.getId());
            return this.OK(TargetParams.TARGET_SCALE_VALUE.name(), (BaseValueObject)kpiMasterScaleBean);
        }
        catch (Exception ex) {
            KpiTargetManagerServiceImpl.log.error((Object)"Exception in KpiTargetManagerServiceImpl - getScaleValue() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_SCALE_VALUE_UNABLE_TO_GET_004.name(), "Failed to get target scale value", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response updateScaleValue(final Request request) {
        ScaleValueBean kpiMasterScaleBean = (ScaleValueBean)request.get(TargetParams.TARGET_SCALE_VALUE.name());
        if (kpiMasterScaleBean == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_SCALE_VALUE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            kpiMasterScaleBean = this.kpiTargetManager.updateScaleValue(kpiMasterScaleBean);
            final Identifier identifier = new Identifier();
            identifier.setId(kpiMasterScaleBean.getId());
            return this.OK(TargetParams.TARGET_SCALE_VALUE_ID.name(), (BaseValueObject)identifier);
        }
        catch (Exception ex) {
            KpiTargetManagerServiceImpl.log.error((Object)"Exception in KpiTargetManagerServiceImpl - updateScaleValue() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_SCALE_VALUE_UNABLE_TO_UPDATE_005.name(), "Failed to update target scale value", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response removeScaleValue(final Request request) {
        final Identifier id = (Identifier)request.get(TargetParams.TARGET_SCALE_VALUE_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_SCALE_VALUE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Boolean isRes = this.kpiTargetManager.removeScaleValue(id.getId());
            final BooleanResponse res = new BooleanResponse((boolean)isRes);
            return this.OK(TargetParams.IS_TARGET_SCALE_VALUE.name(), (BaseValueObject)res);
        }
        catch (Exception ex) {
            KpiTargetManagerServiceImpl.log.error((Object)"Exception in KpiTargetManagerServiceImpl - removeScaleValue() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_SCALE_VALUE_UNABLE_TO_DELETE_006.name(), "Failed to delete target scale value", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response createMasterScale(final Request request) {
        KpiScaleBean kpiScaleBean = (KpiScaleBean)request.get(TargetParams.TARGET_SCALE.name());
        if (kpiScaleBean == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCALE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            kpiScaleBean = this.kpiTargetManager.createMasterScale(kpiScaleBean);
            final Identifier identifier = new Identifier();
            identifier.setId(kpiScaleBean.getId());
            return this.OK(TargetParams.TARGET_SCALE_ID.name(), (BaseValueObject)identifier);
        }
        catch (Exception ex) {
            KpiTargetManagerServiceImpl.log.error((Object)"Exception in KpiTargetManagerServiceImpl - createMasterScale() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCALE_UNABLE_TO_CREATE_003.name(), "Failed to create target scale", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getMasterScale(final Request request) {
        final Identifier id = (Identifier)request.get(TargetParams.TARGET_SCALE_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCALE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final KpiScaleBean kpiScaleBean = this.kpiTargetManager.getMasterScale(id.getId());
            return this.OK(TargetParams.TARGET_SCALE.name(), (BaseValueObject)kpiScaleBean);
        }
        catch (Exception ex) {
            KpiTargetManagerServiceImpl.log.error((Object)"Exception in KpiTargetManagerServiceImpl - () : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCALE_UNABLE_TO_GET_004.name(), "Failed to get target scale", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response updateMasterScale(final Request request) {
        KpiScaleBean kpiScaleBean = (KpiScaleBean)request.get(TargetParams.TARGET_SCALE.name());
        if (kpiScaleBean == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCALE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            kpiScaleBean = this.kpiTargetManager.updateMasterScale(kpiScaleBean);
            final Identifier identifier = new Identifier();
            identifier.setId(kpiScaleBean.getId());
            return this.OK(TargetParams.TARGET_SCALE_ID.name(), (BaseValueObject)identifier);
        }
        catch (Exception ex) {
            KpiTargetManagerServiceImpl.log.error((Object)"Exception in KpiTargetManagerServiceImpl - () : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCALE_UNABLE_TO_UPDATE_005.name(), "Failed to update target scale", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response removeMasterScale(final Request request) {
        final Identifier id = (Identifier)request.get(TargetParams.TARGET_SCALE_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCALE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Boolean isRes = this.kpiTargetManager.removeMasterScale(id.getId());
            final BooleanResponse res = new BooleanResponse((boolean)isRes);
            return this.OK(TargetParams.IS_TARGET_SCALE.name(), (BaseValueObject)res);
        }
        catch (Exception ex) {
            KpiTargetManagerServiceImpl.log.error((Object)"Exception in KpiTargetManagerServiceImpl - removeMasterScale() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCALE_UNABLE_TO_DELETE_006.name(), "Failed to delete target scale", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getAllMasterScales(final Request request) {
        try {
            final List<KpiScaleBean> data = this.kpiTargetManager.getAllMasterScales();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(TargetParams.TARGET_SCALE_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception ex) {
            KpiTargetManagerServiceImpl.log.error((Object)"Exception in KpiTargetManagerServiceImpl - getAllMasterScales() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCALE_UNABLE_TO_GET_ALL_007.name(), "Failed to delete target scale", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getScaleValueForTarget(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.KPI_TARGET_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_SCALE_VALUE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<ScaleValueBean> kpiMasterSacleList = this.kpiTargetManager.getScaleValueForTarget(id.getId());
            final BaseValueObjectList result = new BaseValueObjectList();
            result.setValueObjectList((List)kpiMasterSacleList);
            return this.OK(TargetParams.TARGET_SCALE_VALUE_LIST.name(), (BaseValueObject)result);
        }
        catch (Exception ex) {
            KpiTargetManagerServiceImpl.log.error((Object)"Exception in KpiTargetManagerServiceImpl - getScaleValueForTarget() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_SCALE_VALUE_UNABLE_TO_GET_FOR_TAR_009.name(), "Failed to get target scale list", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getKpiTargetForKpi(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.KPI_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final KpiTargetBean KpiTargetBean = this.kpiTargetManager.getKpiTargetForKpi(id.getId());
            return this.OK(KpiParams.KPI_TARGET_SCALE.name(), (BaseValueObject)KpiTargetBean);
        }
        catch (Exception ex) {
            KpiTargetManagerServiceImpl.log.error((Object)"Exception in KpiTargetManagerServiceImpl - getKpiTargetForKpi() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TARGET_UNABLE_TO_GET_FOR_KPI_009.name(), "Failed to get target list", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response searchKpiReviewFrequency(final Request request) {
        final KpiReviewFrequencyBean kpiReviewFrequencyBean = (KpiReviewFrequencyBean)request.get(KpiParams.KPI_FREQUENCY_DATA.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (kpiReviewFrequencyBean == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_REVIEW_FREQUENCY_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final List<KpiReviewFrequencyBean> kpiReviewFrequencyBeans = this.kpiTargetManager.searchKpiReviewFrequency(kpiReviewFrequencyBean, page, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiReviewFrequencyBeans);
            return this.OK(KpiParams.KPI_FREQUENCY_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception ex) {
            KpiTargetManagerServiceImpl.log.error((Object)"Exception in KpiTargetManagerServiceImpl - searchKpiReviewFrequency() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_REVIEW_FREQUENCY_UNABLE_TO_SEARCH_008.name(), "Failed to search kpi frequency", new Object[] { ex.getMessage() }));
        }
    }
    
    static {
        log = Logger.getLogger((Class)KpiTargetManagerServiceImpl.class);
    }
}
