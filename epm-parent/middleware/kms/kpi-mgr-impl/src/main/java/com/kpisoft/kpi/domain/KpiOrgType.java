package com.kpisoft.kpi.domain;

import com.canopus.mw.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.vo.param.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.utils.*;
import com.kpisoft.kpi.utility.*;
import com.kpisoft.kpi.dac.*;
import javax.validation.*;

public class KpiOrgType extends BaseDomainObject
{
    private KpiOrgTypeManager manager;
    private KpiOrgTypeData kpiOrgTypeDetails;
    
    public KpiOrgType(final KpiOrgTypeManager manager) {
        this.manager = manager;
    }
    
    public int save() {
        this.validate();
        final Request request = new Request();
        request.put(KpiParams.KPI_ORG_TYPE_DATA.name(), (BaseValueObject)this.kpiOrgTypeDetails);
        final Response response = this.getDataService().saveKpiOrgType(request);
        final Identifier id = (Identifier)response.get(KpiParams.KPI_ORG_TYPE_ID.name());
        this.kpiOrgTypeDetails.setId(id.getId());
        return id.getId();
    }
    
    public Response delete() {
        final Request request = new Request();
        request.put(KpiParams.KPI_ORG_TYPE_ID.name(), (BaseValueObject)new Identifier(this.getKpiOrgTypeDetails().getId()));
        return this.getDataService().deleteKpiOrgType(request);
    }
    
    public void validate() {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), (Object)this.kpiOrgTypeDetails, KpiErrorCodesEnum.ERR_KPI_ORIG_TYPE_INVALID_INPUT_002.name(), "Invalid kpiOrgType details");
    }
    
    public KpiOrgTypeData getKpiOrgTypeDetails() {
        return this.kpiOrgTypeDetails;
    }
    
    public void setKpiOrgTypeDetails(final KpiOrgTypeData kpiOrgTypeDetails) {
        this.kpiOrgTypeDetails = kpiOrgTypeDetails;
    }
    
    private KpiOrgTypeDataService getDataService() {
        return this.manager.getDataService();
    }
    
    private Validator getValidator() {
        return this.manager.getValidator();
    }
}
