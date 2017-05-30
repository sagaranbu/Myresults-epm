package com.kpisoft.kpi.impl;

import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import com.kpisoft.kpi.*;
import com.kpisoft.kpi.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.vo.param.*;
import com.kpisoft.kpi.utility.*;
import com.canopus.mw.*;
import java.util.*;
import com.canopus.mw.dto.*;
import org.springframework.beans.factory.config.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ KpiValidationManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class KpiValidationManagerServiceImpl extends BaseMiddlewareBean implements KpiValidationManagerService
{
    private List<CustomValidator> validators;
    @Autowired
    KpiValidationManager kpiValidateManager;
    @Autowired
    private IServiceLocator serviceLocator;
    private static final Logger log;
    
    public KpiValidationManagerServiceImpl() {
        this.validators = null;
        this.kpiValidateManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response validateKpi(final Request request) {
        final KpiData kpiData = (KpiData)request.get(KpiParams.KPI_DATA.name());
        final BaseValueObjectMap map = (BaseValueObjectMap)request.get(KpiValidationParams.ProgramConfig.PROGRAM_RULES.name());
        if (kpiData == null && map == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_VALIDATE_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            List<StatusResponse> errorMsgs = new ArrayList<StatusResponse>();
            errorMsgs = this.kpiValidateManager.validateKpi(kpiData, map);
            if (this.validators != null && !this.validators.isEmpty()) {
                for (final CustomValidator iterator : this.validators) {
                    final List<StatusResponse> messages = (List<StatusResponse>)iterator.validateKpi(kpiData);
                    if (messages != null && !messages.isEmpty()) {
                        errorMsgs.addAll(messages);
                    }
                }
            }
            final BaseValueObjectList bvol = new BaseValueObjectList();
            bvol.setValueObjectList((List)errorMsgs);
            return this.OK(KpiValidationParams.ProgramConfig.PROGRAM_ERROR_MESSAGES.name(), (BaseValueObject)bvol);
        }
        catch (Exception ex) {
            KpiValidationManagerServiceImpl.log.error((Object)"Exception in KpiValidationManagerServiceImpl - validateKpi() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_VALIDATE_003.name(), "Failed to validate kpi", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response validateScorecard(final Request request) {
        final Identifier scoredCardId = (Identifier)request.get(KpiParams.SCORECARD_ID.name());
        final KpiData kpiData = (KpiData)request.get(KpiParams.KPI_DATA.name());
        final BaseValueObjectMap map = (BaseValueObjectMap)request.get(KpiValidationParams.ProgramConfig.PROGRAM_RULES.name());
        if (kpiData == null && map == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_VALIDATE_INVALID_INPUT_002.name(), "No data object in the request");
        }
        List<StatusResponse> errorMsgs = null;
        try {
            errorMsgs = this.kpiValidateManager.validateScorecard(kpiData, map, scoredCardId.getId());
            final BaseValueObjectList bvol = new BaseValueObjectList();
            bvol.setValueObjectList((List)errorMsgs);
            return this.OK(KpiValidationParams.ProgramConfig.PROGRAM_ERROR_MESSAGES.name(), (BaseValueObject)bvol);
        }
        catch (Exception ex) {
            KpiValidationManagerServiceImpl.log.error((Object)"Exception in KpiValidationManagerServiceImpl - validateScorecard() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_UNABLE_TO_VALIDATE_004.name(), "Failed to validate scorecard", (Throwable)ex));
        }
    }
    
    public Response addCustomValidator(final Request request) {
        final CustomValidator validator = (CustomValidator)request.get(KpiValidationParams.ProgramConfig.CUSTOM_VALIDATOR.name());
        if (validator == null) {
            return this.ERROR((Exception)new MiddlewareException("INVALID_SCORECARD_VALIDATE_DATA", "No data object in the request"));
        }
        if (this.validators == null) {
            this.validators = new ArrayList<CustomValidator>();
        }
        this.validators.add(validator);
        return this.OK();
    }
    
    public List<CustomValidator> getValidators() {
        return this.validators;
    }
    
    @Autowired
    public void setValidators(final ListFactoryBean validators) {
        if (this.validators == null) {
            try {
                this.validators = (List<CustomValidator>)validators.getObject();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    static {
        log = Logger.getLogger((Class)KpiValidationManagerServiceImpl.class);
    }
}
