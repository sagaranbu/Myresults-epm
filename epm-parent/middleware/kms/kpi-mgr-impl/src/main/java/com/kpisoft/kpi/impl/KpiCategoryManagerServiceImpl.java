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
@Remote({ KpiCategoryManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class KpiCategoryManagerServiceImpl extends BaseMiddlewareBean implements KpiCategoryManagerService
{
    @Autowired
    private KpiCategoryManager kpiCategoryManager;
    private static final Logger log;
    
    public KpiCategoryManagerServiceImpl() {
        this.kpiCategoryManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response getKpiCategory(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.KPI_CATEGORY_ID.name());
        try {
            if (id == null) {
                throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CAT_INVALID_INPUT_002.name(), "No data object in the request");
            }
            final KpiCategory kpiCategory = this.getKpiCategoryManager().getKpiCategory(id.getId());
            final KpiCategoryData kpiCategoryData = kpiCategory.getKpiCategoryDetails();
            return this.OK(KpiParams.KPI_CATEGORY_DATA.name(), (BaseValueObject)kpiCategoryData);
        }
        catch (Exception ex) {
            KpiCategoryManagerServiceImpl.log.error((Object)"Exception in KpiCategoryManagerServiceImpl - getKpiCategory() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CAT_UNABLE_TO_GET_004.name(), "Failed to load kpiCategory", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response createKpiCategory(final Request request) {
        final KpiCategoryData data = (KpiCategoryData)request.get(KpiParams.KPI_CATEGORY_DATA.name());
        try {
            if (data == null) {
                throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CAT_INVALID_INPUT_002.name(), "No data object in the request");
            }
            final KpiCategory kpiCategory = this.getKpiCategoryManager().createKpiCategory(data);
            final Identifier id = new Identifier();
            id.setId(kpiCategory.getKpiCategoryDetails().getId());
            return this.OK(KpiParams.KPI_CATEGORY_ID.name(), (BaseValueObject)id);
        }
        catch (Exception ex) {
            KpiCategoryManagerServiceImpl.log.error((Object)"Exception in KpiCategoryManagerServiceImpl - createKpiCategory() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CAT_UNABLE_TO_CREATE_003.name(), "Failed to create kpiCategory", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response updateKpiCategory(final Request request) {
        final KpiCategoryData data = (KpiCategoryData)request.get(KpiParams.KPI_CATEGORY_DATA.name());
        try {
            if (data == null) {
                throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CAT_INVALID_INPUT_002.name(), "No data object in the request");
            }
            final KpiCategory kpiCategory = this.getKpiCategoryManager().getKpiCategory(data.getId());
            kpiCategory.setKpiCategoryDetails(data);
            kpiCategory.save();
            final Identifier id = new Identifier();
            id.setId(kpiCategory.getKpiCategoryDetails().getId());
            return this.OK(KpiParams.KPI_CATEGORY_ID.name(), (BaseValueObject)id);
        }
        catch (Exception ex) {
            KpiCategoryManagerServiceImpl.log.error((Object)"Exception in KpiCategoryManagerServiceImpl - updateKpiCategory() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CAT_UNABLE_TO_UPDATE_005.name(), "Failed to update kpiCategory", new Object[] { ex.getMessage() }));
        }
    }
    
    public KpiCategoryManager getKpiCategoryManager() {
        return this.kpiCategoryManager;
    }
    
    public void setKpiCategoryManager(final KpiCategoryManager kpiCategoryManager) {
        this.kpiCategoryManager = kpiCategoryManager;
    }
    
    static {
        log = Logger.getLogger((Class)KpiCategoryManagerServiceImpl.class);
    }
}
