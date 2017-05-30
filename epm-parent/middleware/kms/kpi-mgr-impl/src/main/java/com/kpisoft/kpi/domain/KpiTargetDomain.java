package com.kpisoft.kpi.domain;

import com.canopus.mw.*;
import com.kpisoft.kpi.dac.*;
import com.kpisoft.kpi.vo.*;
import javax.validation.*;
import com.kpisoft.kpi.vo.param.*;
import java.util.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.utils.*;

public class KpiTargetDomain extends BaseDomainObject
{
    private KpiTagretManager kpiTargetManager;
    private KpiTargetBean kpiTargetBean;
    private KpiTargetDataService dataService;
    private ScaleValueBean kpiMasterScaleBean;
    private KpiScaleBean kpiScaleBean;
    
    public KpiScaleBean getKpiScaleBean() {
        return this.kpiScaleBean;
    }
    
    public void setKpiScaleBean(final KpiScaleBean kpiScaleBean) {
        this.kpiScaleBean = kpiScaleBean;
    }
    
    public ScaleValueBean getKpiMasterScaleBean() {
        return this.kpiMasterScaleBean;
    }
    
    public void setKpiMasterScaleBean(final ScaleValueBean kpiMasterScaleBean) {
        this.kpiMasterScaleBean = kpiMasterScaleBean;
    }
    
    public Validator getValidator() {
        return this.kpiTargetManager.getValidator();
    }
    
    public KpiTargetDomain(final KpiTagretManager kpiTargetManager, final KpiTargetDataService dataService) {
        this.dataService = dataService;
        this.kpiTargetManager = kpiTargetManager;
    }
    
    public KpiTargetBean getKpiTargetBean() {
        return this.kpiTargetBean;
    }
    
    public void setKpiTargetBean(final KpiTargetBean kpiTargetBean) {
        this.kpiTargetBean = kpiTargetBean;
    }
    
    public KpiTargetBean createKpiTarget(final KpiTargetBean kpiTargetBean) {
        final Request req = new Request();
        req.put(KpiParams.KPI_TARGET.name(), (BaseValueObject)kpiTargetBean);
        final Response res = this.dataService.saveKpiTarget(req);
        final KpiTargetBean kpiTargetBeanRes = (KpiTargetBean)res.get(KpiParams.KPI_TARGET.name());
        return kpiTargetBeanRes;
    }
    
    public KpiTargetBean getKpiTarget(final Integer kpiTargetId) {
        final Request req = new Request();
        final Identifier id = new Identifier();
        id.setId(kpiTargetId);
        req.put(KpiParams.KPI_TARGET_ID.name(), (BaseValueObject)id);
        final Response res = this.dataService.getKpiTarget(req);
        final KpiTargetBean kpiTargetBean = (KpiTargetBean)res.get(KpiParams.KPI_TARGET.name());
        return kpiTargetBean;
    }
    
    public KpiTargetBean updateKpiTarget(final KpiTargetBean kpiTargetBean) {
        final Request req = new Request();
        req.put(KpiParams.KPI_TARGET.name(), (BaseValueObject)kpiTargetBean);
        final Response res = this.dataService.updateKpiTarget(req);
        final KpiTargetBean kpiTrgBean = (KpiTargetBean)res.get(KpiParams.KPI_TARGET.name());
        return kpiTrgBean;
    }
    
    public Boolean removeKpiTarget(final Integer kpiTargetId) {
        final Request req = new Request();
        final Identifier id = new Identifier();
        id.setId(kpiTargetId);
        req.put(KpiParams.KPI_TARGET_ID.name(), (BaseValueObject)id);
        final Response response = this.dataService.deleteKpiTarget(req);
        final BooleanResponse status = (BooleanResponse)response.get(KpiParams.STATUS_RESPONSE.name());
        return status.isResponse();
    }
    
    public ScaleValueBean getScaleValue(final Integer TargetScaleValueId) {
        final Request req = new Request();
        final Identifier id = new Identifier();
        id.setId(TargetScaleValueId);
        req.put(TargetParams.TARGET_SCALE_VALUE_ID.name(), (BaseValueObject)id);
        final Response res = this.dataService.getScaleValue(req);
        final ScaleValueBean kpiMasterScaleBean = (ScaleValueBean)res.get(TargetParams.TARGET_SCALE_VALUE.name());
        return kpiMasterScaleBean;
    }
    
    public ScaleValueBean createScaleValue(final ScaleValueBean kpiMasterScaleBean) {
        final Request req = new Request();
        req.put(TargetParams.TARGET_SCALE_VALUE.name(), (BaseValueObject)kpiMasterScaleBean);
        final Response res = this.dataService.createScaleValue(req);
        final ScaleValueBean kpiMasterScaleBeanRes = (ScaleValueBean)res.get(TargetParams.TARGET_SCALE_VALUE.name());
        return kpiMasterScaleBeanRes;
    }
    
    public ScaleValueBean updateScaleValue(final ScaleValueBean kpiMasterScaleBean) {
        final Request req = new Request();
        req.put(TargetParams.TARGET_SCALE_VALUE.name(), (BaseValueObject)kpiMasterScaleBean);
        final Response res = this.dataService.updateScaleValue(req);
        final ScaleValueBean kpiMasterScaleBeanRes = (ScaleValueBean)res.get(TargetParams.TARGET_SCALE_VALUE.name());
        return kpiMasterScaleBeanRes;
    }
    
    public Boolean removeScaleValue(final Integer scaleValueId) {
        final Request req = new Request();
        final Identifier id = new Identifier();
        id.setId(scaleValueId);
        req.put(TargetParams.TARGET_SCALE_VALUE_ID.name(), (BaseValueObject)id);
        final Response response = this.dataService.removeScaleValue(req);
        final BooleanResponse status = (BooleanResponse)response.get(KpiParams.STATUS_RESPONSE.name());
        return status.isResponse();
    }
    
    public KpiScaleBean createMasterScale(final KpiScaleBean kpiScaleBean) {
        final Request req = new Request();
        req.put(TargetParams.TARGET_SCALE.name(), (BaseValueObject)kpiScaleBean);
        final Response res = this.dataService.createMasterScale(req);
        final KpiScaleBean kpiScaleBeanRes = (KpiScaleBean)res.get(TargetParams.TARGET_SCALE.name());
        return kpiScaleBeanRes;
    }
    
    public KpiScaleBean updateMasterScale(final KpiScaleBean kpiScaleBean) {
        final Request req = new Request();
        req.put(TargetParams.TARGET_SCALE.name(), (BaseValueObject)kpiScaleBean);
        final Response res = this.dataService.updateMasterScale(req);
        final KpiScaleBean kpiScaleBeanRes = (KpiScaleBean)res.get(TargetParams.TARGET_SCALE.name());
        return kpiScaleBeanRes;
    }
    
    public KpiScaleBean getMasterScale(final Integer TargetScaleValueId) {
        final Request req = new Request();
        final Identifier id = new Identifier();
        id.setId(TargetScaleValueId);
        req.put(TargetParams.TARGET_SCALE_ID.name(), (BaseValueObject)id);
        final Response res = this.dataService.getMasterScale(req);
        final KpiScaleBean kpiScaleBean = (KpiScaleBean)res.get(TargetParams.TARGET_SCALE.name());
        return kpiScaleBean;
    }
    
    public Boolean removeMasterScale(final Integer scaleId) {
        final Request req = new Request();
        final Identifier id = new Identifier();
        id.setId(scaleId);
        req.put(TargetParams.TARGET_SCALE_ID.name(), (BaseValueObject)id);
        final Response response = this.dataService.removeMasterScale(req);
        final BooleanResponse status = (BooleanResponse)response.get(TargetParams.STATUS_RESPONSE.name());
        return status.isResponse();
    }
    
    public List<KpiScaleBean> getAllMasterScales() {
        final Response response = this.dataService.getAllMasterScales(new Request());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(TargetParams.TARGET_SCALE_LIST.name());
        final List<KpiScaleBean> kpiScaleBeanList = (List<KpiScaleBean>)list.getValueObjectList();
        return kpiScaleBeanList;
    }
    
    public void validate(final Object obj) {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), obj, "ERR_VAL_INVALID_INPUT", "Invalid template details");
    }
    
    public List<ScaleValueBean> getScaleValueForTarget(final Integer targetId) {
        final Request req = new Request();
        final Identifier id = new Identifier();
        id.setId(targetId);
        req.put(KpiParams.KPI_TARGET_ID.name(), (BaseValueObject)id);
        final Response res = this.dataService.getScaleValueForTarget(req);
        final BaseValueObjectList list = (BaseValueObjectList)res.get(TargetParams.TARGET_SCALE_VALUE_LIST.name());
        final List<ScaleValueBean> kpiMasterScaleBeanList = (List<ScaleValueBean>)list.getValueObjectList();
        return kpiMasterScaleBeanList;
    }
    
    public KpiTargetBean getKpiTargetForKpi(final Integer kpiId) {
        final Request req = new Request();
        final Identifier id = new Identifier();
        id.setId(kpiId);
        req.put(KpiParams.KPI_ID.name(), (BaseValueObject)id);
        final Response res = this.dataService.getKpiTargetForKpi(req);
        final KpiTargetBean kpiTargetBean = (KpiTargetBean)res.get(KpiParams.KPI_TARGET_SCALE.name());
        return kpiTargetBean;
    }
}
