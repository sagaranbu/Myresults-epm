package com.kpisoft.kpi.impl;

import com.kpisoft.kpi.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import com.kpisoft.kpi.vo.param.*;
import com.kpisoft.kpi.utility.*;
import com.canopus.mw.*;
import com.canopus.mw.dto.*;
import com.kpisoft.kpi.domain.*;
import com.kpisoft.kpi.vo.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ KpiOrgTypeManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class KpiOrgTypeManagerServiceImpl extends BaseMiddlewareBean implements KpiOrgTypeManagerService
{
    @Autowired
    private KpiOrgTypeManager kpiOrgTypeManager;
    private static final Logger log;
    
    public KpiOrgTypeManagerServiceImpl() {
        this.kpiOrgTypeManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response getKpiOrgType(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.KPI_ORG_TYPE_ID.name());
        try {
            if (id == null) {
                throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_ORIG_TYPE_INVALID_INPUT_002.name(), "No data object in the request");
            }
            final KpiOrgType kpiOrgType = this.getKpiOrgTypeManager().getKpiOrgType(id.getId());
            final KpiOrgTypeData kpiOrgTypeData = kpiOrgType.getKpiOrgTypeDetails();
            return this.OK(KpiParams.KPI_ORG_TYPE_DATA.name(), (BaseValueObject)kpiOrgTypeData);
        }
        catch (Exception ex) {
            KpiOrgTypeManagerServiceImpl.log.error((Object)"Exception in KpiOrgTypeManagerServiceImpl - getKpiOrgType() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_ORIG_TYPE_UNABLE_TO_GET_004.name(), "Failed to load kpiOrgType", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response createKpiOrgType(final Request request) {
        final KpiOrgTypeData data = (KpiOrgTypeData)request.get(KpiParams.KPI_ORG_TYPE_DATA.name());
        try {
            if (data == null) {
                throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_ORIG_TYPE_INVALID_INPUT_002.name(), "No data object in the request");
            }
            final KpiOrgType kpiOrgType = this.getKpiOrgTypeManager().createKpiOrgType(data);
            final Identifier id = new Identifier();
            id.setId(kpiOrgType.getKpiOrgTypeDetails().getId());
            return this.OK(KpiParams.KPI_ORG_TYPE_ID.name(), (BaseValueObject)id);
        }
        catch (Exception ex) {
            KpiOrgTypeManagerServiceImpl.log.error((Object)"Exception in KpiOrgTypeManagerServiceImpl - createKpiOrgType() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_ORIG_TYPE_UNABLE_TO_CREATE_003.name(), "Failed to create kpiOrgType", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response updateKpiOrgType(final Request request) {
        final KpiOrgTypeData data = (KpiOrgTypeData)request.get(KpiParams.KPI_ORG_TYPE_DATA.name());
        try {
            if (data == null) {
                throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_ORIG_TYPE_INVALID_INPUT_002.name(), "No data object in the request");
            }
            final KpiOrgType kpiOrgType = this.getKpiOrgTypeManager().getKpiOrgType(data.getId());
            kpiOrgType.setKpiOrgTypeDetails(data);
            kpiOrgType.save();
            final Identifier id = new Identifier();
            id.setId(kpiOrgType.getKpiOrgTypeDetails().getId());
            return this.OK(KpiParams.KPI_ORG_TYPE_ID.name(), (BaseValueObject)id);
        }
        catch (Exception ex) {
            KpiOrgTypeManagerServiceImpl.log.error((Object)"Exception in KpiOrgTypeManagerServiceImpl - updateKpiOrgType() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_ORIG_TYPE_UNABLE_TO_UPDATE_005.name(), "Failed to update kpiOrgType", new Object[] { ex.getMessage() }));
        }
    }
    
    public KpiOrgTypeManager getKpiOrgTypeManager() {
        return this.kpiOrgTypeManager;
    }
    
    public void setKpiOrgTypeManager(final KpiOrgTypeManager kpiOrgTypeManager) {
        this.kpiOrgTypeManager = kpiOrgTypeManager;
    }
    
    static {
        log = Logger.getLogger((Class)KpiOrgTypeManagerServiceImpl.class);
    }
}
