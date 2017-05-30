package com.kpisoft.kpi.domain;

import com.canopus.mw.*;
import com.kpisoft.kpi.dac.*;
import javax.validation.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.vo.param.*;
import com.canopus.mw.dto.*;

public class KpiCategoryManager extends BaseDomainManager implements CacheLoader<Integer, KpiCategory>
{
    @Autowired
    private KpiCategoryDataService dataService;
    @Autowired
    private Validator validator;
    @Autowired
    @Qualifier("kpiCategoryCache")
    private Cache<Integer, KpiCategory> cache;
    
    public KpiCategoryManager() {
        this.dataService = null;
        this.cache = null;
    }
    
    public KpiCategory createKpiCategory(final KpiCategoryData data) {
        final KpiCategory kpiCategory = new KpiCategory(this);
        kpiCategory.setKpiCategoryDetails(data);
        final int id = kpiCategory.save();
        this.getCache().put(id, kpiCategory);
        return kpiCategory;
    }
    
    public KpiCategory getKpiCategory(final int kpiCategoryId) {
        final KpiCategory kpiCategory = (KpiCategory)this.getCache().get(kpiCategoryId, (CacheLoader)this);
        return kpiCategory;
    }
    
    public KpiCategory load(final Integer key) {
        final KpiCategoryDataService svc = this.getDataService();
        final Request request = new Request();
        request.put(KpiParams.KPI_CATEGORY_ID.name(), (BaseValueObject)new Identifier(key));
        final Response response = svc.getKpiCategory(request);
        final KpiCategoryData data = (KpiCategoryData)response.get(KpiParams.KPI_CATEGORY_DATA.name());
        final KpiCategory kpiCategory = new KpiCategory(this);
        kpiCategory.setKpiCategoryDetails(data);
        return kpiCategory;
    }
    
    public KpiCategoryDataService getDataService() {
        return this.dataService;
    }
    
    public void setDataService(final KpiCategoryDataService svc) {
        this.dataService = svc;
    }
    
    public Validator getValidator() {
        return this.validator;
    }
    
    public void setValidator(final Validator validator) {
        this.validator = validator;
    }
    
    public Cache<Integer, KpiCategory> getCache() {
        return this.cache;
    }
    
    public void setCache(final Cache<Integer, KpiCategory> cache) {
        this.cache = cache;
    }
}
