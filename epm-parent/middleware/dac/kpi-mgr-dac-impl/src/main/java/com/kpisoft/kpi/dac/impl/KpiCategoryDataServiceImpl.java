package com.kpisoft.kpi.dac.impl;

import com.kpisoft.kpi.dac.*;
import com.kpisoft.kpi.dac.dao.*;
import org.springframework.beans.factory.annotation.*;
import org.modelmapper.*;
import org.apache.log4j.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.dac.impl.domain.*;
import com.canopus.mw.*;
import com.kpisoft.kpi.vo.param.*;
import com.kpisoft.kpi.utility.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import com.canopus.mw.dto.*;

public class KpiCategoryDataServiceImpl extends BaseMiddlewareBean implements KpiCategoryDataService
{
    @Autowired
    private KpiCategoryDao kpiCategoryDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public KpiCategoryDataServiceImpl() {
        this.kpiCategoryDao = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)KpiCategoryData.class, (Class)KpiCategory.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    @Transactional(readOnly = true)
    public Response getKpiCategory(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.KPI_CATEGORY_ID.name());
        KpiCategory kpiCategory = null;
        try {
            if (id == null) {
                throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_CAT_INVALID_INPUT_002.name(), "No data object in the request");
            }
            kpiCategory = (KpiCategory)this.kpiCategoryDao.find((Serializable)id);
        }
        catch (Exception ex) {
            KpiCategoryDataServiceImpl.log.error((Object)"Exception in KpiCategoryDataServiceImpl - getKpiCategory() : ", (Throwable)ex);
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_CAT_UNABLE_TO_GET_004.name(), "Failed to load KpiCategory", (Throwable)ex);
        }
        if (kpiCategory == null) {
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_CAT_DOES_NOT_EXIST_001.name(), "kpiCategory id {0} does not exist.", new Object[] { id });
        }
        final KpiCategoryData kpiCategoryData = (KpiCategoryData)this.modelMapper.map((Object)kpiCategory, (Class)KpiCategoryData.class);
        return this.OK(KpiParams.KPI_CATEGORY_DATA.name(), (BaseValueObject)kpiCategoryData);
    }
    
    @Transactional
    public Response saveKpiCategory(final Request request) {
        final KpiCategoryData kpiCategoryData = (KpiCategoryData)request.get(KpiParams.KPI_CATEGORY_DATA.name());
        final KpiCategory kpiCategory = new KpiCategory();
        try {
            if (kpiCategoryData == null) {
                throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_CAT_INVALID_INPUT_002.name(), "No data object in the request");
            }
            this.modelMapper.map((Object)kpiCategoryData, (Object)kpiCategory);
            this.kpiCategoryDao.save(kpiCategory);
            return this.OK(KpiParams.KPI_CATEGORY_ID.name(), (BaseValueObject)new Identifier(kpiCategory.getId()));
        }
        catch (Exception ex) {
            KpiCategoryDataServiceImpl.log.error((Object)"Exception in KpiCategoryDataServiceImpl - saveKpiCategory() : ", (Throwable)ex);
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_CAT_UNABLE_TO_CREATE_003.name(), "Failed to save kpiCategory", (Throwable)ex);
        }
    }
    
    @Transactional
    public Response deleteKpiCategory(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.KPI_CATEGORY_ID.name());
        try {
            if (id == null) {
                throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_CAT_INVALID_INPUT_002.name(), "No data object in the request");
            }
            final boolean status = this.kpiCategoryDao.removeById((Serializable)id.getId());
            return this.OK(KpiParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            KpiCategoryDataServiceImpl.log.error((Object)"Exception in KpiCategoryDataServiceImpl - deleteKpiCategory() : ", (Throwable)ex);
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_CAT_UNABLE_TO_DELETE_006.name(), "Unknown error while deleting kpiCategory", (Throwable)ex);
        }
    }
    
    static {
        log = Logger.getLogger((Class)KpiCategoryDataServiceImpl.class);
    }
}
