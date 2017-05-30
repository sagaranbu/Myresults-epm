package com.kpisoft.kpi.domain;

import com.canopus.mw.*;
import com.kpisoft.kpi.vo.param.*;
import com.canopus.mw.utils.*;
import com.kpisoft.kpi.utility.*;
import com.kpisoft.kpi.dac.*;
import javax.validation.*;
import com.canopus.mw.dto.*;
import com.kpisoft.kpi.vo.*;
import java.util.*;

public class Kpi extends BaseDomainObject
{
    private KpiManager manager;
    private KpiData kpiDetails;
    private KpiUomData kpiUomDetails;
    private KpiTypeData kpiTypeData;
    
    public Kpi(final KpiManager manager) {
        this.manager = manager;
    }
    
    public int save() {
        this.validate(this.kpiDetails);
        final Request request = new Request();
        request.put(KpiParams.KPI_DATA.name(), (BaseValueObject)this.kpiDetails);
        final Response response = this.getDataService().saveKpi(request);
        final Identifier id = (Identifier)response.get(KpiParams.KPI_ID.name());
        return id.getId();
    }
    
    public boolean deleteKpi(final Integer kpiId) {
        final Request request = new Request();
        request.put(KpiParams.KPI_ID.name(), (BaseValueObject)new Identifier(kpiId));
        final Response response = this.getDataService().deleteKpi(request);
        final BooleanResponse status = (BooleanResponse)response.get(KpiParams.STATUS_RESPONSE.name());
        return status.isResponse();
    }
    
    public boolean deleteKpis(final List<Integer> kpiIdList, final List<Integer> kpiRelIdList) {
        final IdentifierList resIdList = new IdentifierList((List)kpiIdList);
        final IdentifierList resGIdList = new IdentifierList((List)kpiRelIdList);
        final Request request = new Request();
        request.put(KpiParams.KPI_ID_LIST.name(), (BaseValueObject)resIdList);
        request.put(KpiParams.KPI_REL_ID_LIST.name(), (BaseValueObject)resGIdList);
        final Response response = this.getDataService().deleteKpis(request);
        final BooleanResponse status = (BooleanResponse)response.get(KpiParams.STATUS_RESPONSE.name());
        return status.isResponse();
    }
    
    public boolean deactivateKpis(final List<Integer> kpiIdList, final List<Integer> kpiRelIdList) {
        final IdentifierList resIdList = new IdentifierList((List)kpiIdList);
        final IdentifierList resGIdList = new IdentifierList((List)kpiRelIdList);
        final Request request = new Request();
        request.put(KpiParams.KPI_ID_LIST.name(), (BaseValueObject)resIdList);
        request.put(KpiParams.KPI_REL_ID_LIST.name(), (BaseValueObject)resGIdList);
        final Response response = this.getDataService().deactivateKpis(request);
        final BooleanResponse status = (BooleanResponse)response.get(KpiParams.STATUS_RESPONSE.name());
        return status.isResponse();
    }
    
    public Integer getKpiCountByScoreCard(final Integer scorecardId, final String kpiTypeName) {
        final Request request = new Request();
        request.put(KpiParams.SCORECARD_ID.name(), (BaseValueObject)new Identifier(scorecardId));
        request.put(KpiParams.KPI_TYPE_NAME.name(), (BaseValueObject)new StringIdentifier(kpiTypeName));
        final Response response = this.getDataService().getKpiCountByScoreCard(request);
        final Identifier identifier = (Identifier)response.get(KpiParams.KPI_COUNT.name());
        return identifier.getId();
    }
    
    public void validate(final Object data) {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), data, KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Invalid kpi details");
    }
    
    private KpiDataService getDataService() {
        return this.manager.getDataService();
    }
    
    private Validator getValidator() {
        return this.manager.getValidator();
    }
    
    public int saveKpiUom() {
        this.validate(this.kpiUomDetails);
        final Request request = new Request();
        request.put(KpiParams.KPI_UOM_DATA.name(), (BaseValueObject)this.kpiUomDetails);
        final Response response = this.getDataService().saveKpiUom(request);
        final Identifier id = (Identifier)response.get(KpiParams.KPI_UOM_ID.name());
        this.kpiUomDetails.setId(id.getId());
        return id.getId();
    }
    
    public KpiUomData getKpiUom(final int kpiUomId) {
        final Request req = new Request();
        req.put(KpiParams.KPI_UOM_ID.name(), (BaseValueObject)new Identifier(kpiUomId));
        final Response res = this.getDataService().getKpiUom(req);
        final KpiUomData kpiUomData = (KpiUomData)res.get(KpiParams.KPI_UOM_DATA.name());
        return kpiUomData;
    }
    
    public List<KpiUomData> getAllKpiUom() {
        final Response response = this.getDataService().getAllKpiUom(new Request());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(KpiParams.KPI_UOM_DATA_LIST.name());
        final List<KpiUomData> kpiUomData = (List<KpiUomData>)list.getValueObjectList();
        return kpiUomData;
    }
    
    public boolean deleteKpiUom(final Integer kpiUomId) {
        final Request request = new Request();
        request.put(KpiParams.KPI_UOM_ID.name(), (BaseValueObject)new Identifier(kpiUomId));
        final Response response = this.getDataService().deleteKpiUom(request);
        final BooleanResponse status = (BooleanResponse)response.get(KpiParams.STATUS_RESPONSE.name());
        return status.isResponse();
    }
    
    public Integer saveKpiType() {
        this.validate(this.kpiTypeData);
        final Request request = new Request();
        request.put(KpiParams.KPI_TYPE_DATA.name(), (BaseValueObject)this.kpiTypeData);
        final Response response = this.getDataService().saveKpiType(request);
        final Identifier identifier = (Identifier)response.get(KpiParams.KPI_TYPE_ID.name());
        this.kpiTypeData.setId(identifier.getId());
        return identifier.getId();
    }
    
    public KpiTypeData getKpiType(final int kpiTypeId) {
        final Request req = new Request();
        req.put(KpiParams.KPI_TYPE_ID.name(), (BaseValueObject)new Identifier(kpiTypeId));
        final Response res = this.getDataService().getKpiType(req);
        final KpiTypeData kpiTypeData = (KpiTypeData)res.get(KpiParams.KPI_TYPE_DATA.name());
        return kpiTypeData;
    }
    
    public List<KpiTypeData> getAllKpiType() {
        final Response response = this.getDataService().getAllKpiType(new Request());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(KpiParams.KPI_TYPE_DATA_LIST.name());
        final List<KpiTypeData> kpiTypeData = (List<KpiTypeData>)list.getValueObjectList();
        return kpiTypeData;
    }
    
    public boolean deleteKpiType(final Integer kpiTypeId) {
        final Request request = new Request();
        request.put(KpiParams.KPI_TYPE_ID.name(), (BaseValueObject)new Identifier(kpiTypeId));
        final Response response = this.getDataService().deleteKpiType(request);
        final BooleanResponse status = (BooleanResponse)response.get(KpiParams.STATUS_RESPONSE.name());
        return status.isResponse();
    }
    
    public KpiData getKpiDetails() {
        return this.kpiDetails;
    }
    
    public void setKpiDetails(final KpiData kpiDetails) {
        this.kpiDetails = kpiDetails;
    }
    
    public KpiUomData getKpiUomDetails() {
        return this.kpiUomDetails;
    }
    
    public void setKpiUomDetails(final KpiUomData kpiUomDetails) {
        this.kpiUomDetails = kpiUomDetails;
    }
    
    public KpiTypeData getKpiTypeData() {
        return this.kpiTypeData;
    }
    
    public void setKpiTypeData(final KpiTypeData kpiTypeData) {
        this.kpiTypeData = kpiTypeData;
    }
    
    public List<KpiUomData> getAllKpiUom(SortList sortList) {
        final Request request = new Request();
        request.setSortList(sortList);
        final Response response = this.getDataService().getAllKpiUom(request);
        sortList = response.getSortList();
        final BaseValueObjectList list = (BaseValueObjectList)response.get(KpiParams.KPI_UOM_DATA_LIST.name());
        final List<KpiUomData> kpiUomData = (List<KpiUomData>)list.getValueObjectList();
        return kpiUomData;
    }
    
    public Integer updateKPIState(final IdentifierList idList, final Identifier state) {
        int count = 0;
        final Request request = new Request();
        request.put(KpiParams.KPI_ID_LIST.name(), (BaseValueObject)idList);
        request.put(KpiParams.KPI_STATE.name(), (BaseValueObject)state);
        final Response response = this.getDataService().updateKPIState(request);
        final Identifier id = (Identifier)response.get(KpiParams.COUNT.name());
        if (id != null && id.getId() != null && id.getId() >= 0) {
            count = id.getId();
        }
        return count;
    }
    
    public void removeParentRelationship(final Integer parentID) {
        final List<KpiKpiGraphRelationshipBean> parents = (List<KpiKpiGraphRelationshipBean>)this.getKpiDetails().getKpiKpiRelationship();
        if (parents != null) {
            final Iterator<KpiKpiGraphRelationshipBean> relItr = parents.iterator();
            while (relItr.hasNext()) {
                final KpiKpiGraphRelationshipBean rel = relItr.next();
                if (rel.getParent().getId().equals(parentID)) {
                    relItr.remove();
                }
            }
        }
    }
}
