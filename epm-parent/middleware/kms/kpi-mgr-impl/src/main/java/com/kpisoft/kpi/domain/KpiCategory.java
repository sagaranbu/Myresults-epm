package com.kpisoft.kpi.domain;

import com.canopus.mw.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.vo.param.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.utils.*;
import com.kpisoft.kpi.utility.*;
import com.kpisoft.kpi.dac.*;
import javax.validation.*;

public class KpiCategory extends BaseDomainObject
{
    private KpiCategoryManager manager;
    private KpiCategoryData kpiCategoryDetails;
    
    public KpiCategory(final KpiCategoryManager manager) {
        this.manager = manager;
    }
    
    public int save() {
        this.validate();
        final Request request = new Request();
        request.put(KpiParams.KPI_CATEGORY_DATA.name(), (BaseValueObject)this.kpiCategoryDetails);
        final Response response = this.getDataService().saveKpiCategory(request);
        final Identifier id = (Identifier)response.get(KpiParams.KPI_CATEGORY_ID.name());
        this.kpiCategoryDetails.setId(id.getId());
        return id.getId();
    }
    
    public Response delete() {
        final Request request = new Request();
        request.put(KpiParams.KPI_CATEGORY_ID.name(), (BaseValueObject)new Identifier(this.getKpiCategoryDetails().getId()));
        return this.getDataService().deleteKpiCategory(request);
    }
    
    public void validate() {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), (Object)this.kpiCategoryDetails, KpiErrorCodesEnum.ERR_KPI_CAT_UNKNOWN_EXCEPTION_000.name(), "Invalid kpiCategory details");
    }
    
    public KpiCategoryData getKpiCategoryDetails() {
        return this.kpiCategoryDetails;
    }
    
    public void setKpiCategoryDetails(final KpiCategoryData kpiCategoryDetails) {
        this.kpiCategoryDetails = kpiCategoryDetails;
    }
    
    private KpiCategoryDataService getDataService() {
        return this.manager.getDataService();
    }
    
    private Validator getValidator() {
        return this.manager.getValidator();
    }
}
