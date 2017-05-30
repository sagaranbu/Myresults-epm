package com.kpisoft.kpi.dac.impl;

import com.kpisoft.kpi.dac.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.dac.hibernate.*;
import com.kpisoft.kpi.dac.dao.*;
import org.apache.log4j.*;
import org.modelmapper.convention.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.utility.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import com.kpisoft.kpi.vo.param.*;
import org.modelmapper.*;
import java.util.*;
import java.lang.reflect.*;
import com.canopus.mw.*;
import com.kpisoft.kpi.dac.impl.domain.*;
import com.canopus.dac.utils.*;
import com.canopus.mw.dto.*;
import com.googlecode.genericdao.search.*;

@Component
public class KpiTargetDataServiceImpl extends BaseDataAccessService implements KpiTargetDataService
{
    @Autowired
    private KpiTargetDao kpiTargetDao;
    @Autowired
    private KpiScaleDao objKpiScaleDao;
    @Autowired
    private GenericHibernateDao genericDao;
    @Autowired
    private KpiDao objKpiDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public KpiTargetDataServiceImpl() {
        this.kpiTargetDao = null;
        this.objKpiScaleDao = null;
        this.genericDao = null;
        this.objKpiDao = null;
        this.modelMapper = null;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)KpiTargetBean.class, (Class)KpiTargetMetaData.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)ScaleValueBean.class, (Class)ScaleValue.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)KpiScaleBean.class, (Class)Scale.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)KpiReviewFrequencyBean.class, (Class)KpiReviewFrequency.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    @Transactional
    public Response saveKpiTarget(final Request req) {
        final KpiTargetBean kpiTargetBean = (KpiTargetBean)req.get(KpiParams.KPI_TARGET.name());
        if (kpiTargetBean == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TARGET_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        KpiTargetMetaData kpiTarget = null;
        if (kpiTargetBean.getId() != null) {
            kpiTarget = (KpiTargetMetaData)this.kpiTargetDao.find((Serializable)kpiTargetBean.getId());
        }
        else {
            kpiTarget = new KpiTargetMetaData();
        }
        try {
            this.modelMapper.map((Object)kpiTargetBean, (Object)kpiTarget);
            this.kpiTargetDao.save(kpiTarget);
            kpiTargetBean.setId(kpiTarget.getId());
            if (kpiTarget.getScale() != null) {
                kpiTarget.getScale().setTenantId(ExecutionContext.getTenantId());
            }
            if (kpiTarget.getScale().getAlkpiMasterScaleValue() != null && !kpiTarget.getScale().getAlkpiMasterScaleValue().isEmpty()) {
                for (int size = kpiTarget.getScale().getAlkpiMasterScaleValue().size(), i = 0; i < size; ++i) {
                    kpiTarget.getScale().getAlkpiMasterScaleValue().get(i).setTenantId(ExecutionContext.getTenantId());
                }
            }
            return this.OK(KpiParams.KPI_TARGET.name(), (BaseValueObject)kpiTargetBean);
        }
        catch (Exception ex) {
            KpiTargetDataServiceImpl.log.error((Object)"Exception in KpiTargetDataServiceImpl - saveKpiTarget() :", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TARGET_UNABLE_TO_CREATE_003.name(), "Failed to create kpi target", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getKpiTarget(final Request req) {
        final Identifier id = (Identifier)req.get(KpiParams.KPI_TARGET_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TARGET_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        KpiTargetMetaData kpitarget = null;
        try {
            kpitarget = (KpiTargetMetaData)this.kpiTargetDao.find((Serializable)id.getId());
        }
        catch (Exception ex) {
            KpiTargetDataServiceImpl.log.error((Object)"Exception in KpiTargetDataServiceImpl - getKpiTarget() :", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TARGET_UNABLE_TO_GET_004.name(), "Failed to get kpi target", (Throwable)ex));
        }
        if (kpitarget == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TARGET_DOES_NOT_EXIST_001.name(), "Kpi target id {0} does not exist.", new Object[] { id.getId() }));
        }
        final KpiTargetBean kpiTargetBean = (KpiTargetBean)this.modelMapper.map((Object)kpitarget, (Class)KpiTargetBean.class);
        return this.OK(KpiParams.KPI_TARGET.name(), (BaseValueObject)kpiTargetBean);
    }
    
    @Transactional
    public Response updateKpiTarget(final Request req) {
        final KpiTargetBean kpiTargetBean = (KpiTargetBean)req.get(KpiParams.KPI_TARGET.name());
        if (kpiTargetBean == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TARGET_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        KpiTargetMetaData kpiTarget = null;
        if (kpiTargetBean.getId() != null) {
            kpiTarget = (KpiTargetMetaData)this.kpiTargetDao.find((Serializable)kpiTargetBean.getId());
        }
        else {
            kpiTarget = new KpiTargetMetaData();
        }
        try {
            this.modelMapper.map((Object)kpiTargetBean, (Object)kpiTarget);
            this.kpiTargetDao.save(kpiTarget);
            kpiTargetBean.setId(kpiTarget.getId());
            return this.OK(KpiParams.KPI_TARGET.name(), (BaseValueObject)kpiTargetBean);
        }
        catch (Exception ex) {
            KpiTargetDataServiceImpl.log.error((Object)"Exception in KpiTargetDataServiceImpl - updateKpiTarget() :", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TARGET_UNABLE_TO_UPDATE_005.name(), "Failed to update kpi target", (Throwable)ex));
        }
    }
    
    @Transactional
    public Response deleteKpiTarget(final Request req) {
        final Identifier id = (Identifier)req.get(KpiParams.KPI_TARGET_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TARGET_INVALID_INPUT_002.name(), "Invalid kpi target"));
        }
        try {
            final boolean isKpiTarget = this.kpiTargetDao.removeById((Serializable)id.getId());
            return this.OK(KpiParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(isKpiTarget));
        }
        catch (Exception ex) {
            KpiTargetDataServiceImpl.log.error((Object)"Exception in KpiTargetDataServiceImpl - deleteKpiTarget() :", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TARGET_UNABLE_TO_DELETE_006.name(), "Failed to delete kpi target", (Throwable)ex));
        }
    }
    
    @Transactional
    public Response createScaleValue(final Request request) {
        final ScaleValueBean scaleValueBean = (ScaleValueBean)request.get(TargetParams.TARGET_SCALE_VALUE.name());
        ScaleValue scaleValue = null;
        if (scaleValueBean.getId() != null && scaleValueBean.getId() > 0) {
            scaleValue = (ScaleValue)this.genericDao.find((Class)ScaleValue.class, (Serializable)scaleValueBean.getId());
            if (scaleValue == null) {
                throw new DataAccessException("INVALID_OPERATION", "Invalid data");
            }
        }
        else {
            scaleValue = new ScaleValue();
            this.modelMapper.map((Object)scaleValueBean, (Object)scaleValue);
            if (scaleValueBean.getScaleId() == null || scaleValueBean.getScaleId() <= 0) {
                throw new DataAccessException("DB_OPERATION_FAILED", "scale id is required");
            }
            final Scale scale = new Scale();
            scale.setId(scaleValueBean.getScaleId());
            scaleValue.setScale(scale);
        }
        this.genericDao.save((Object)scaleValue);
        scaleValueBean.setId(scaleValue.getId());
        return this.OK(TargetParams.TARGET_SCALE_VALUE.name(), (BaseValueObject)scaleValueBean);
    }
    
    @Transactional(readOnly = true)
    public Response getScaleValue(final Request request) {
        Response response = null;
        final ScaleValueBean objKpiMasterScaleBean = (ScaleValueBean)request.get(TargetParams.TARGET_SCALE_VALUE_ID.name());
        ScaleValue objKpiMasterScaleMetaData = null;
        if (objKpiMasterScaleBean.getId() > 0) {
            objKpiMasterScaleMetaData = (ScaleValue)this.genericDao.find((Class)ScaleValue.class, (Serializable)objKpiMasterScaleBean.getId());
            if (objKpiMasterScaleMetaData == null) {
                throw new DataAccessException("INVALID_OPERATION", "Invalid data");
            }
            final ScaleValueBean objKpiMasterScaleBean2 = (ScaleValueBean)this.modelMapper.map((Object)objKpiMasterScaleMetaData, (Class)ScaleValueBean.class);
            response = new Response();
            response.put(TargetParams.TARGET_SCALE_VALUE.name(), (BaseValueObject)objKpiMasterScaleBean2);
        }
        return response;
    }
    
    @Transactional
    public Response updateScaleValue(final Request request) {
        Response response = null;
        final ScaleValueBean objKpiMasterScaleBean = (ScaleValueBean)request.get(TargetParams.TARGET_SCALE_VALUE.name());
        ScaleValue objKpiMasterScaleMetaData = null;
        if (objKpiMasterScaleBean.getId() > 0) {
            objKpiMasterScaleMetaData = (ScaleValue)this.genericDao.find((Class)ScaleValue.class, (Serializable)objKpiMasterScaleBean.getId());
            if (objKpiMasterScaleMetaData == null) {
                throw new DataAccessException("INVALID_OPERATION", "Invalid data");
            }
            this.modelMapper.map((Object)objKpiMasterScaleBean, (Object)objKpiMasterScaleMetaData);
            final boolean save = this.genericDao.save((Object)objKpiMasterScaleMetaData);
            objKpiMasterScaleBean.setId(objKpiMasterScaleMetaData.getId());
            response = new Response();
            response.put(TargetParams.TARGET_SCALE_VALUE.name(), (BaseValueObject)objKpiMasterScaleBean);
        }
        return response;
    }
    
    @Transactional
    public Response removeScaleValue(final Request request) {
        try {
            final Identifier id = (Identifier)request.get(TargetParams.TARGET_SCALE_VALUE_ID.name());
            if (id == null) {
                throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TARGET_SCALE_VALUE_INVALID_INPUT_002.name(), "Invalid target scale value");
            }
            final boolean isKpiTarget = this.genericDao.removeById((Class)ScaleValue.class, (Serializable)id.getId());
            return this.OK(KpiParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(isKpiTarget));
        }
        catch (Exception ex) {
            KpiTargetDataServiceImpl.log.error((Object)"Exception in KpiTargetDataServiceImpl - removeScaleValue() :", (Throwable)ex);
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_TARGET_SCALE_VALUE_UNABLE_TO_DELETE_006.name(), "Failed to delete target scale value", (Throwable)ex);
        }
    }
    
    @Transactional
    public Response createMasterScale(final Request request) {
        try {
            final KpiScaleBean objKpiScaleBean = (KpiScaleBean)request.get(TargetParams.TARGET_SCALE.name());
            Scale objKpiScaleMetaData = null;
            if (objKpiScaleBean.getId() != null && objKpiScaleBean.getId() > 0) {
                objKpiScaleMetaData = (Scale)this.objKpiScaleDao.find((Serializable)objKpiScaleBean.getId());
                if (objKpiScaleMetaData == null) {
                    throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCALE_INVALID_INPUT_002.name(), "Invalid data");
                }
            }
            else {
                objKpiScaleMetaData = new Scale();
            }
            this.modelMapper.map((Object)objKpiScaleBean, (Object)objKpiScaleMetaData);
            for (final ScaleValue sValue : objKpiScaleMetaData.getAlkpiMasterScaleValue()) {
                sValue.setScaleId(objKpiScaleMetaData.getId());
            }
            this.objKpiScaleDao.save(objKpiScaleMetaData);
            objKpiScaleBean.setId(objKpiScaleMetaData.getId());
            if (objKpiScaleMetaData.getAlkpiMasterScaleValue() != null && !objKpiScaleMetaData.getAlkpiMasterScaleValue().isEmpty()) {
                for (int size = objKpiScaleMetaData.getAlkpiMasterScaleValue().size(), i = 0; i < size; ++i) {
                    objKpiScaleMetaData.getAlkpiMasterScaleValue().get(i).setTenantId(ExecutionContext.getTenantId());
                }
            }
            return this.OK(TargetParams.TARGET_SCALE.name(), (BaseValueObject)objKpiScaleBean);
        }
        catch (Exception ex) {
            KpiTargetDataServiceImpl.log.error((Object)"Exception in KpiTargetDataServiceImpl - createMasterScale() :", (Throwable)ex);
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCALE_UNABLE_TO_CREATE_003.name(), "Failed to create target scale", new Object[] { ex.getMessage() });
        }
    }
    
    @Transactional(readOnly = true)
    public Response getMasterScale(final Request request) {
        Scale scale = null;
        final Identifier identifier = (Identifier)request.get(TargetParams.TARGET_SCALE_ID.name());
        try {
            if (identifier == null || identifier.getId() == 0) {
                throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCALE_INVALID_INPUT_002.name(), "Invalid Data");
            }
            scale = (Scale)this.objKpiScaleDao.find((Serializable)identifier.getId());
        }
        catch (Exception ex) {
            KpiTargetDataServiceImpl.log.error((Object)"Exception in KpiTargetDataServiceImpl - getMasterScale() :", (Throwable)ex);
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCALE_UNABLE_TO_GET_004.name(), "Failed to get target scale", new Object[] { ex.getMessage() });
        }
        if (scale == null) {
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCALE_DOES_NOT_EXIST_001.name(), "Scale Id doesn't exist" + identifier.getId());
        }
        final KpiScaleBean scaleBean = (KpiScaleBean)this.modelMapper.map((Object)scale, (Class)KpiScaleBean.class);
        return this.OK(TargetParams.TARGET_SCALE.name(), (BaseValueObject)scaleBean);
    }
    
    @Transactional
    public Response updateMasterScale(final Request request) {
        final KpiScaleBean objKpiScaleBean = (KpiScaleBean)request.get(TargetParams.TARGET_SCALE.name());
        Scale objKpiScaleMetaData = null;
        if (objKpiScaleBean.getId() > 0) {
            objKpiScaleMetaData = (Scale)this.objKpiScaleDao.find((Serializable)objKpiScaleBean.getId());
            if (objKpiScaleMetaData == null) {
                throw new DataAccessException("INVALID_OPERATION", "Invalid data");
            }
            this.modelMapper.map((Object)objKpiScaleBean, (Object)objKpiScaleMetaData);
        }
        this.objKpiScaleDao.save(objKpiScaleMetaData);
        objKpiScaleBean.setId(objKpiScaleMetaData.getId());
        return this.OK(TargetParams.TARGET_SCALE.name(), (BaseValueObject)objKpiScaleBean);
    }
    
    @Transactional
    public Response removeMasterScale(final Request request) {
        final Identifier id = (Identifier)request.get(TargetParams.TARGET_SCALE_ID.name());
        if (id == null) {
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCALE_INVALID_INPUT_002.name(), "Invalid target scale");
        }
        try {
            final boolean isKpiTarget = this.objKpiScaleDao.removeById((Serializable)id.getId());
            return this.OK(TargetParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(isKpiTarget));
        }
        catch (Exception ex) {
            KpiTargetDataServiceImpl.log.error((Object)"Exception in KpiTargetDataServiceImpl - updateMasterScale() :", (Throwable)ex);
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCALE_UNABLE_TO_DELETE_006.name(), "Failed to delete target scale", (Throwable)ex);
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllMasterScales(final Request request) {
        try {
            final List<Scale> data = (List<Scale>)this.genericDao.findAll((Class)Scale.class);
            final Type listType = new TypeToken<List<KpiScaleBean>>() {}.getType();
            final List<KpiScaleBean> kpiList = (List<KpiScaleBean>)this.modelMapper.map((Object)data, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiList);
            return this.OK(TargetParams.TARGET_SCALE_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            KpiTargetDataServiceImpl.log.error((Object)"Exception in KpiTargetDataServiceImpl - getAllMasterScales() :", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_SCALE_UNABLE_TO_GET_ALL_007.name(), "Failed to delete target scale", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getScaleValueForTarget(final Request request) {
        Response response = null;
        final Identifier id = (Identifier)request.get(KpiParams.KPI_TARGET_ID.name());
        KpiTargetMetaData objKpiTargetMetaData = null;
        if (id.getId() > 0) {
            objKpiTargetMetaData = (KpiTargetMetaData)this.kpiTargetDao.find((Serializable)id.getId());
            if (objKpiTargetMetaData == null) {
                throw new DataAccessException("INVALID_OPERATION", "Invalid data");
            }
            final Search objSearch = new Search((Class)ScaleValue.class);
            objSearch.addFilterEqual("scale.id", (Object)objKpiTargetMetaData.getScale().getId());
            final List<ScaleValue> alKpiMasterScaleMetaData = (List<ScaleValue>)this.genericDao.search((ISearch)objSearch);
            if (alKpiMasterScaleMetaData == null) {
                throw new MiddlewareException("ERR_VAL_INVALID_INPUT", "Invalid Given Id");
            }
            final Type listType = new TypeToken<List<ScaleValueBean>>() {}.getType();
            final List<? extends BaseValueObject> kpiMasterScaleBVList = (List<? extends BaseValueObject>)this.modelMapper.map((Object)alKpiMasterScaleMetaData, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiMasterScaleBVList);
            response = new Response();
            response.put(TargetParams.TARGET_SCALE_VALUE_LIST.name(), (BaseValueObject)list);
        }
        return response;
    }
    
    @Transactional(readOnly = true)
    public Response getKpiTargetForKpi(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.KPI_ID.name());
        KpiTargetBean kpiTargetBean = null;
        Kpi objKpi = null;
        if (id.getId() > 0) {
            objKpi = (Kpi)this.objKpiDao.find((Serializable)id.getId());
            if (objKpi == null) {
                throw new DataAccessException("INVALID_OPERATION", "Invalid data");
            }
            KpiTargetMetaData kpiTargetMetaData = null;
            if (objKpi.getKpiTarget() != null) {
                kpiTargetMetaData = objKpi.getKpiTarget();
            }
            if (kpiTargetMetaData == null) {
                throw new MiddlewareException("INVALID_OPERATION", "No data ");
            }
            kpiTargetBean = (KpiTargetBean)this.modelMapper.map((Object)kpiTargetMetaData, (Class)KpiTargetBean.class);
        }
        return this.OK(KpiParams.KPI_TARGET_SCALE.name(), (BaseValueObject)kpiTargetBean);
    }
    
    @Transactional(readOnly = true)
    public Response searchKpiReviewFrequency(final Request request) {
        final KpiReviewFrequencyBean kpiReviewFrequencyBean = (KpiReviewFrequencyBean)request.get(KpiParams.KPI_FREQUENCY_DATA.name());
        final SortList sortList = request.getSortList();
        final String defaultField = "id";
        BaseValueObjectList list = null;
        if (kpiReviewFrequencyBean == null) {
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_REVIEW_FREQUENCY_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final Search search = new Search((Class)KpiReviewFrequency.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final ModelMapper modelMapper = new ModelMapper();
            final KpiReviewFrequency kpiReviewFrequency = (KpiReviewFrequency)modelMapper.map((Object)kpiReviewFrequencyBean, (Class)KpiReviewFrequency.class);
            final Filter filter = this.genericDao.getFilterFromExample((Object)kpiReviewFrequency, options);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<KpiReviewFrequency> kpiReviewFrequencies = (List<KpiReviewFrequency>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<KpiReviewFrequencyBean>>() {}.getType();
            final List<KpiReviewFrequencyBean> kpiFreqBVList = (List<KpiReviewFrequencyBean>)modelMapper.map((Object)kpiReviewFrequencies, listType);
            list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiFreqBVList);
            final Response response = this.OK(KpiParams.KPI_FREQUENCY_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiTargetDataServiceImpl.log.error((Object)"Exception in KpiTargetDataServiceImpl - searchKpiReviewFrequency() :", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(KpiErrorCodesEnum.ERR_KPI_REVIEW_FREQUENCY_UNABLE_TO_SEARCH_008.name(), "Failed to search kpi frequency", (Throwable)e));
        }
    }
    
    static {
        log = Logger.getLogger((Class)KpiTargetDataServiceImpl.class);
    }
}
