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

public class KpiOrgTypeDataServiceImpl extends BaseMiddlewareBean implements KpiOrgTypeDataService
{
    @Autowired
    private KpiOrgTypeDao kpiOrgTypeeDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public KpiOrgTypeDataServiceImpl() {
        this.kpiOrgTypeeDao = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)KpiOrgTypeData.class, (Class)KpiOrgType.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    @Transactional(readOnly = true)
    public Response getKpiOrgType(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.KPI_ORG_TYPE_ID.name());
        KpiOrgType kpiOrgTypee = null;
        try {
            if (id == null) {
                throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_ORIG_TYPE_INVALID_INPUT_002.name(), "No data object in the request");
            }
            kpiOrgTypee = (KpiOrgType)this.kpiOrgTypeeDao.find((Serializable)id);
        }
        catch (Exception ex) {
            KpiOrgTypeDataServiceImpl.log.error((Object)"Exception in KpiOrgTypeDataServiceImpl - getKpiOrgType() : ", (Throwable)ex);
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_ORIG_TYPE_UNABLE_TO_GET_004.name(), "Failed to load kpiOrgType", (Throwable)ex);
        }
        if (kpiOrgTypee == null) {
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_ORIG_TYPE_DOES_NOT_EXIST_001.name(), "kpiOrgTypee id {0} does not exist.", new Object[] { id });
        }
        final KpiOrgTypeData kpiOrgTypeeData = (KpiOrgTypeData)this.modelMapper.map((Object)kpiOrgTypee, (Class)KpiOrgTypeData.class);
        return this.OK(KpiParams.KPI_ORG_TYPE_DATA.name(), (BaseValueObject)kpiOrgTypeeData);
    }
    
    @Transactional
    public Response saveKpiOrgType(final Request request) {
        final KpiOrgTypeData kpiOrgTypeeData = (KpiOrgTypeData)request.get(KpiParams.KPI_ORG_TYPE_DATA.name());
        final KpiOrgType kpiOrgTypee = new KpiOrgType();
        try {
            if (kpiOrgTypeeData == null) {
                throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_ORIG_TYPE_INVALID_INPUT_002.name(), "No data object in the request");
            }
            this.modelMapper.map((Object)kpiOrgTypeeData, (Object)kpiOrgTypee);
            this.kpiOrgTypeeDao.save(kpiOrgTypee);
            return this.OK(KpiParams.KPI_ORG_TYPE_ID.name(), (BaseValueObject)new Identifier(kpiOrgTypee.getId()));
        }
        catch (Exception ex) {
            KpiOrgTypeDataServiceImpl.log.error((Object)"Exception in KpiOrgTypeDataServiceImpl - saveKpiOrgType() : ", (Throwable)ex);
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_ORIG_TYPE_UNABLE_TO_CREATE_003.name(), "Failed to create kpiOrgType", (Throwable)ex);
        }
    }
    
    @Transactional
    public Response deleteKpiOrgType(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.KPI_ORG_TYPE_ID.name());
        try {
            if (id == null) {
                throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_ORIG_TYPE_INVALID_INPUT_002.name(), "No data object in the request");
            }
            final boolean status = this.kpiOrgTypeeDao.removeById((Serializable)id.getId());
            return this.OK(KpiParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            KpiOrgTypeDataServiceImpl.log.error((Object)"Exception in KpiOrgTypeDataServiceImpl - deleteKpiOrgType() : ", (Throwable)ex);
            throw new DataAccessException(KpiErrorCodesEnum.ERR_KPI_ORIG_TYPE_UNABLE_TO_DELETE_006.name(), "Failed to delete KpiOrgType data", (Throwable)ex);
        }
    }
    
    static {
        log = Logger.getLogger((Class)KpiOrgTypeDataServiceImpl.class);
    }
}
