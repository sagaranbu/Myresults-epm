package com.kpisoft.kpi.domain;

import com.canopus.mw.*;
import org.springframework.stereotype.*;
import com.kpisoft.kpi.dac.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import javax.validation.*;
import com.canopus.dac.*;
import java.util.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.vo.param.*;
import com.canopus.mw.dto.*;

@Component
public class KpiTagretManager extends BaseDomainManager implements CacheLoader<Integer, KpiTargetDomain>
{
    @Autowired
    private KpiTargetDataService dataService;
    @Autowired
    @Qualifier("kpiTagretCache")
    private Cache<Integer, KpiTargetDomain> cache;
    @Autowired
    private Validator validator;
    private KpiTargetDomain kpiTargetDomain;
    
    public KpiTagretManager() {
        this.kpiTargetDomain = null;
    }
    
    public Validator getValidator() {
        return this.validator;
    }
    
    public void setValidator(final Validator validator) {
        this.validator = validator;
    }
    
    public KpiTargetBean createKpiTarget(final KpiTargetBean kpiTargetBean) {
        this.kpiTargetDomain = new KpiTargetDomain(this, this.dataService);
        final KpiTargetBean kpiTargetBeanRes = this.kpiTargetDomain.createKpiTarget(kpiTargetBean);
        this.kpiTargetDomain.setKpiTargetBean(kpiTargetBeanRes);
        this.cache.put(kpiTargetBean.getId(), this.kpiTargetDomain);
        return kpiTargetBeanRes;
    }
    
    public KpiTargetBean getKpiTarget(final Integer id) throws DataAccessException, Exception {
        this.kpiTargetDomain = new KpiTargetDomain(this, this.dataService);
        final KpiTargetBean kpiTargetBean = this.kpiTargetDomain.getKpiTarget(id);
        this.kpiTargetDomain.setKpiTargetBean(kpiTargetBean);
        this.cache.put(kpiTargetBean.getId(), this.kpiTargetDomain);
        return kpiTargetBean;
    }
    
    public KpiTargetBean updateKpiTarget(final KpiTargetBean kpiTargetBean) {
        this.kpiTargetDomain = new KpiTargetDomain(this, this.dataService);
        final KpiTargetBean kpiTargetBeanRes = this.kpiTargetDomain.updateKpiTarget(kpiTargetBean);
        this.kpiTargetDomain.setKpiTargetBean(kpiTargetBeanRes);
        this.cache.put(kpiTargetBeanRes.getId(), this.kpiTargetDomain);
        return kpiTargetBeanRes;
    }
    
    public Boolean removeKpiTarget(final Integer kpiTargetId) throws DataAccessException, Exception {
        this.kpiTargetDomain = new KpiTargetDomain(this, this.dataService);
        return this.kpiTargetDomain.removeKpiTarget(kpiTargetId);
    }
    
    public KpiTargetDomain load(final Integer key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public KpiTargetDomain getKpiTargetDomain() {
        return this.kpiTargetDomain;
    }
    
    public void setKpiTargetDomain(final KpiTargetDomain kpiTargetDomain) {
        this.kpiTargetDomain = kpiTargetDomain;
    }
    
    public ScaleValueBean createScaleValue(final ScaleValueBean kpiMasterScaleBean) {
        this.kpiTargetDomain = new KpiTargetDomain(this, this.dataService);
        return this.kpiTargetDomain.createScaleValue(kpiMasterScaleBean);
    }
    
    public ScaleValueBean getScaleValue(final Integer scaleValueId) {
        this.kpiTargetDomain = new KpiTargetDomain(this, this.dataService);
        final ScaleValueBean kpiMasterScaleBean = this.kpiTargetDomain.getScaleValue(scaleValueId);
        this.kpiTargetDomain.setKpiMasterScaleBean(kpiMasterScaleBean);
        this.cache.put(kpiMasterScaleBean.getId(), this.kpiTargetDomain);
        return kpiMasterScaleBean;
    }
    
    public ScaleValueBean updateScaleValue(final ScaleValueBean kpiMasterScaleBean) {
        this.kpiTargetDomain = new KpiTargetDomain(this, this.dataService);
        return this.kpiTargetDomain.updateScaleValue(kpiMasterScaleBean);
    }
    
    public Boolean removeScaleValue(final Integer scaleValueId) {
        this.kpiTargetDomain = new KpiTargetDomain(this, this.dataService);
        return this.kpiTargetDomain.removeScaleValue(scaleValueId);
    }
    
    public KpiScaleBean createMasterScale(final KpiScaleBean kpiScaleBean) {
        this.kpiTargetDomain = new KpiTargetDomain(this, this.dataService);
        return this.kpiTargetDomain.createMasterScale(kpiScaleBean);
    }
    
    public KpiScaleBean getMasterScale(final Integer targetScale) {
        this.kpiTargetDomain = new KpiTargetDomain(this, this.dataService);
        final KpiScaleBean kpiScaleBean = this.kpiTargetDomain.getMasterScale(targetScale);
        this.kpiTargetDomain.setKpiScaleBean(kpiScaleBean);
        this.cache.put(kpiScaleBean.getId(), this.kpiTargetDomain);
        return kpiScaleBean;
    }
    
    public KpiScaleBean updateMasterScale(final KpiScaleBean kpiScaleBean) {
        this.kpiTargetDomain = new KpiTargetDomain(this, this.dataService);
        return this.kpiTargetDomain.updateMasterScale(kpiScaleBean);
    }
    
    public Boolean removeMasterScale(final Integer scaleId) {
        this.kpiTargetDomain = new KpiTargetDomain(this, this.dataService);
        return this.kpiTargetDomain.removeMasterScale(scaleId);
    }
    
    public List<KpiScaleBean> getAllMasterScales() {
        this.kpiTargetDomain = new KpiTargetDomain(this, this.dataService);
        return this.kpiTargetDomain.getAllMasterScales();
    }
    
    public List<ScaleValueBean> getScaleValueForTarget(final Integer targetId) {
        this.kpiTargetDomain = new KpiTargetDomain(this, this.dataService);
        final List<ScaleValueBean> list = this.kpiTargetDomain.getScaleValueForTarget(targetId);
        return list;
    }
    
    public KpiTargetBean getKpiTargetForKpi(final Integer kpiId) {
        this.kpiTargetDomain = new KpiTargetDomain(this, this.dataService);
        final KpiTargetBean KpiTargetBean = this.kpiTargetDomain.getKpiTargetForKpi(kpiId);
        return KpiTargetBean;
    }
    
    public List<KpiReviewFrequencyBean> searchKpiReviewFrequency(final KpiReviewFrequencyBean reviewFrequencyBean, final Page page, final SortList sortList) {
        final Request request = new Request();
        request.put(KpiParams.KPI_FREQUENCY_DATA.name(), (BaseValueObject)reviewFrequencyBean);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.searchKpiReviewFrequency(request);
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(KpiParams.KPI_FREQUENCY_DATA_LIST.name());
        final List<KpiReviewFrequencyBean> reviewFrequencyBeans = (List<KpiReviewFrequencyBean>)bList.getValueObjectList();
        return reviewFrequencyBeans;
    }
}
