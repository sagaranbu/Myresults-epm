package com.kpisoft.kpi.domain;

import com.canopus.mw.*;
import com.kpisoft.kpi.dac.*;
import javax.validation.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.vo.param.*;
import com.canopus.mw.dto.*;

public class KpiOrgTypeManager extends BaseDomainManager implements CacheLoader<Integer, KpiOrgType>
{
    @Autowired
    private KpiOrgTypeDataService dataService;
    @Autowired
    private Validator validator;
    @Autowired
    @Qualifier("kpiOrgTypeCache")
    private Cache<Integer, KpiOrgType> cache;
    
    public KpiOrgTypeManager() {
        this.dataService = null;
        this.cache = null;
    }
    
    public KpiOrgType createKpiOrgType(final KpiOrgTypeData data) {
        final KpiOrgType kpiOrgType = new KpiOrgType(this);
        kpiOrgType.setKpiOrgTypeDetails(data);
        final int id = kpiOrgType.save();
        this.getCache().put(id, kpiOrgType);
        return kpiOrgType;
    }
    
    public KpiOrgType getKpiOrgType(final int kpiOrgTypeId) {
        final KpiOrgType kpiOrgType = (KpiOrgType)this.getCache().get(kpiOrgTypeId, (CacheLoader)this);
        return kpiOrgType;
    }
    
    public KpiOrgType load(final Integer key) {
        final KpiOrgTypeDataService svc = this.getDataService();
        final Request request = new Request();
        request.put(KpiParams.KPI_ORG_TYPE_ID.name(), (BaseValueObject)new Identifier(key));
        final Response response = svc.getKpiOrgType(request);
        final KpiOrgTypeData data = (KpiOrgTypeData)response.get(KpiParams.KPI_ORG_TYPE_DATA.name());
        final KpiOrgType kpiOrgType = new KpiOrgType(this);
        kpiOrgType.setKpiOrgTypeDetails(data);
        return kpiOrgType;
    }
    
    public KpiOrgTypeDataService getDataService() {
        return this.dataService;
    }
    
    public void setDataService(final KpiOrgTypeDataService svc) {
        this.dataService = svc;
    }
    
    public Validator getValidator() {
        return this.validator;
    }
    
    public void setValidator(final Validator validator) {
        this.validator = validator;
    }
    
    public Cache<Integer, KpiOrgType> getCache() {
        return this.cache;
    }
    
    public void setCache(final Cache<Integer, KpiOrgType> cache) {
        this.cache = cache;
    }
}
