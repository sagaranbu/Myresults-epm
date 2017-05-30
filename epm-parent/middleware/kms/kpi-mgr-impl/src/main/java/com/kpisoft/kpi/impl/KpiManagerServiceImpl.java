package com.kpisoft.kpi.impl;

import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.strategy.program.domain.*;
import org.apache.log4j.*;
import com.canopus.mw.*;
import com.kpisoft.kpi.domain.*;
import com.kpisoft.strategy.params.*;
import com.kpisoft.strategy.program.service.*;
import com.kpisoft.kpi.impl.graph.*;
import com.kpisoft.kpi.vo.param.*;
import com.kpisoft.kpi.*;
import com.kpisoft.kpi.utility.*;
import java.util.*;
import com.kpisoft.kpi.vo.*;
import com.canopus.mw.dto.*;
import com.tinkerpop.blueprints.*;
import org.springframework.transaction.annotation.*;
import com.google.common.math.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ KpiManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class KpiManagerServiceImpl extends BaseMiddlewareBean implements KpiManagerService
{
    @Autowired
    private KpiManager kpiManager;
    @Autowired
    private PeriodUtil periodUtil;
    @Autowired
    private KpiValidationManager validationManager;
    @Autowired
    private PerformanceProgramManager programManager;
    @Autowired
    private IServiceLocator serviceLocator;
    private static final int MAX_DEPTH_RECURSION = 100;
    private static final Logger log;
    
    public KpiManagerServiceImpl() {
        this.kpiManager = null;
        this.validationManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response getKpi(final Request request) {
        final Identifier identifier = (Identifier)request.get(KpiParams.KPI_ID.name());
        if (identifier == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final Kpi kpi = this.getKpiManager().getKpi(identifier.getId());
            if (kpi != null && kpi.getKpiDetails() != null) {
                KpiData data = kpi.getKpiDetails();
                data = this.periodUtil.updateCurrentScoreDetails(data);
                return this.OK(KpiParams.KPI_DATA.name(), (BaseValueObject)data);
            }
            return this.OK(KpiParams.KPI_DATA.name(), (BaseValueObject)null);
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getKpi() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_004.name(), "Failed to load kpi", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response createKpi(Request request) {
        KpiData data = (KpiData)request.get(KpiParams.KPI_DATA.name());
        final Identifier empId = (Identifier)request.get(KpiParams.EMP_ID.name());
        final Identifier orgId = (Identifier)request.get(KpiParams.ORG_ID.name());
        final Identifier progId = (Identifier)request.get(ProgramParams.STRATEGY_PROGRAM_ID.name());
        if (data == null || progId == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request");
        }
        Response response = this.OK();
        try {
            final ProgramManagerService programService = (ProgramManagerService)this.serviceLocator.getService("ProgramManagerServiceImpl");
            request = new Request();
            request.put(ProgramParams.STRATEGY_PROGRAM_ID.name(), (BaseValueObject)progId);
            request.put(ProgramParams.EMP_ID.name(), (BaseValueObject)empId);
            request.put(ProgramParams.ORG_ID.name(), (BaseValueObject)orgId);
            response = programService.getValidationRules(request);
            final BaseValueObjectMap map = (BaseValueObjectMap)response.get(ProgramParams.MAP_RESULT.name());
            final List<StatusResponse> statusList = this.validationManager.validateKpi(data, map);
            if (statusList.size() > 0) {
                final BaseValueObjectList list = new BaseValueObjectList();
                list.setValueObjectList((List)statusList);
                response = this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_VALIDATE_INVALID_INPUT_002.name(), "Invalid input"));
                response.getResponseValueObjects().put(KpiParams.VALIDATION_ERROR_MESSAGES.name(), list);
                return response;
            }
            if (data.getCoreKpi() != null && data.getCoreKpi()) {
                data.setKpiEmpRelationship((List)null);
            }
            if (data.getIndividualKpi() != null && data.getIndividualKpi()) {
                data.setKpiPositionRelationship((List)null);
            }
            if (data.getGroupKpi() != null && data.getGroupKpi() && data.getCoreKpi() != null && !data.getCoreKpi() && data.getIndividualKpi() != null && !data.getIndividualKpi()) {
                data.setKpiEmpRelationship((List)null);
                data.setKpiPositionRelationship((List)null);
            }
            if (data.getKpiTarget() != null) {
                data.setNumTarget(data.getKpiTarget().getNumTarget());
                data.setDateTarget(data.getKpiTarget().getDateTarget());
            }
            final Kpi kpi = this.getKpiManager().createKpi(data);
            data = kpi.getKpiDetails();
            this.kpiManager.updateGraph(data);
            response.put(KpiParams.KPI_ID.name(), (BaseValueObject)new Identifier(kpi.getKpiDetails().getId()));
            return response;
        }
        catch (Exception ex) {
            ex.printStackTrace(System.out);
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - createKpi() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_CREATE_003.name(), "Failed to create kpi", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response updateKpi(final Request request) {
        KpiData data = (KpiData)request.get(KpiParams.KPI_DATA.name());
        if (data == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request");
        }
        if (data.getCoreKpi() != null && data.getCoreKpi()) {
            data.setKpiEmpRelationship((List)null);
        }
        if (data.getIndividualKpi() != null && data.getIndividualKpi()) {
            data.setKpiPositionRelationship((List)null);
        }
        if (data.getGroupKpi() != null && data.getGroupKpi() && data.getCoreKpi() != null && !data.getCoreKpi() && data.getIndividualKpi() != null && !data.getIndividualKpi()) {
            data.setKpiEmpRelationship((List)null);
            data.setKpiPositionRelationship((List)null);
        }
        if (data.getKpiTarget() != null) {
            data.setNumTarget(data.getKpiTarget().getNumTarget());
            data.setDateTarget(data.getKpiTarget().getDateTarget());
        }
        try {
            final Kpi kpi = this.kpiManager.createKpi(data);
            data = kpi.getKpiDetails();
            final Integer kpiId = data.getId();
            this.kpiManager.updateGraph(data);
            if (kpi != null && kpi.getKpiDetails() != null) {
                return this.OK(KpiParams.KPI_ID.name(), (BaseValueObject)new Identifier(kpi.getKpiDetails().getId()));
            }
            return this.OK(KpiParams.KPI_ID.name(), (BaseValueObject)new Identifier((Integer)null));
        }
        catch (Exception ex) {
            ex.printStackTrace(System.out);
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - updateKpi() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_UPDATE_005.name(), "Failed to update kpi", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response updateKpiDetailsForMultiple(final Request request) {
        final BaseValueObjectList kpiUtility = (BaseValueObjectList)request.get(KpiParams.KPI_UTILITY_LIST.name());
        if (kpiUtility == null || kpiUtility.getValueObjectList() == null || kpiUtility.getValueObjectList().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final int count = this.kpiManager.updateKpiDetailsForMultiple(kpiUtility);
            return this.OK(KpiParams.COUNT.name(), (BaseValueObject)new Identifier(count));
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiDataServiceImpl - updateKpiDetailsForMultiple : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_UPDATE_005.name(), "Failed to update list of kpi details ", (Throwable)e));
        }
    }
    
    public Response deactivateKpi(final Request request) {
        final Identifier identifier = (Identifier)request.get(KpiParams.KPI_ID.name());
        final BooleanResponse isDelete = (BooleanResponse)request.get(KpiParams.DELETE_CASCADE_CHILDS.name());
        List<KpiIdentityBean> childIdentityList = null;
        List<Integer> finalIdList = new ArrayList<Integer>();
        if (identifier == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request");
        }
        childIdentityList = this.kpiManager.getDescendantsByType(identifier.getId(), KpiRelationshipParams.CASCADE.name(), 100);
        if (childIdentityList != null && childIdentityList.size() > 0) {
            if (isDelete == null || !isDelete.isResponse()) {
                throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_DEL_REF_EXISTS_009.name(), "Cannot delete since cascade childs exists");
            }
        }
        else {
            childIdentityList = new ArrayList<KpiIdentityBean>();
        }
        final List<KpiIdentityBean> childGIdentityList = this.kpiManager.getDescendantsByType(identifier.getId(), KpiRelationshipParams.GROUP.name(), 100);
        if (childGIdentityList != null && childGIdentityList.size() > 0) {
            childIdentityList.addAll(childGIdentityList);
        }
        finalIdList = this.kpiManager.getIdListFromIdObjectList(childIdentityList);
        finalIdList.add(identifier.getId());
        try {
            final boolean status = true;
            this.kpiManager.deactivateKpis(finalIdList, identifier.getId());
            if (status) {
                for (final Integer vId : finalIdList) {
                    this.kpiManager.getKpiRelationship().removeVertex(vId, (Class)KpiGraph.Kpi.class);
                }
            }
            return this.OK(KpiParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - deleteKpi() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_DELETE_006.name(), "Failed to delete kpi", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response deleteKpi(final Request request) {
        final Identifier identifier = (Identifier)request.get(KpiParams.KPI_ID.name());
        final BooleanResponse isDelete = (BooleanResponse)request.get(KpiParams.DELETE_CASCADE_CHILDS.name());
        final BooleanResponse isGDelete = (BooleanResponse)request.get(KpiParams.DELETE_GROUP_CHILDS.name());
        List<KpiIdentityBean> childIdentityList = null;
        List<Integer> finalIdList = new ArrayList<Integer>();
        if (identifier == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request");
        }
        childIdentityList = this.kpiManager.getDescendantsByType(identifier.getId(), KpiRelationshipParams.CASCADE.name(), 100);
        if (childIdentityList != null && childIdentityList.size() > 0) {
            if (isDelete == null || !isDelete.isResponse()) {
                throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_DEL_REF_EXISTS_009.name(), "Cannot delete since cascade childs exists");
            }
        }
        else {
            childIdentityList = new ArrayList<KpiIdentityBean>();
        }
        final List<KpiIdentityBean> childGIdentityList = this.kpiManager.getDescendantsByType(identifier.getId(), KpiRelationshipParams.GROUP.name(), 100);
        if (childGIdentityList != null && childGIdentityList.size() > 0) {
            if (isGDelete == null || !isGDelete.isResponse()) {
                throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_DEL_REF_EXISTS_009.name(), "Cannot delete since group childs exists");
            }
            childIdentityList.addAll(childGIdentityList);
        }
        finalIdList = this.kpiManager.getIdListFromIdObjectList(childIdentityList);
        finalIdList.add(identifier.getId());
        try {
            final boolean status = this.kpiManager.deleteKpis(finalIdList, identifier.getId());
            if (status) {
                for (final Integer vId : finalIdList) {
                    this.kpiManager.getKpiRelationship().removeVertex(vId, (Class)KpiGraph.Kpi.class);
                }
            }
            return this.OK(KpiParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - deleteKpi() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_DELETE_006.name(), "Failed to delete kpi", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getKpiCountByScoreCard(final Request request) {
        final Identifier scorecardId = (Identifier)request.get(KpiParams.SCORECARD_ID.name());
        final StringIdentifier kpiTypeName = (StringIdentifier)request.get(KpiParams.KPI_TYPE_NAME.name());
        if (scorecardId == null || kpiTypeName == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Integer count = this.kpiManager.getKpiCountByScoreCard(scorecardId.getId(), kpiTypeName.getId());
            return this.OK(KpiParams.KPI_COUNT.name(), (BaseValueObject)new Identifier(count));
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getKpiCountByScoreCard() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_GET_COUNT_BY_SCORECARD_010.name(), "Failed to get count by scorecard", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getKpisForEmployee(final Request request) {
        final Identifier ownerId = (Identifier)request.get(KpiParams.KPI_OWNER_ID.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (ownerId == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<KpiData> data = this.kpiManager.getKpisForEmployee(ownerId.getId(), page, sortList);
            final BaseValueObjectList result = new BaseValueObjectList();
            result.setValueObjectList((List)data);
            final Response response = this.OK(KpiParams.KPI_DATA_LIST.name(), (BaseValueObject)result);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getKpisForEmployee() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_BY_EMP_011.name(), "Unable to get the Kpis for the given employee", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getKpisByStrategyNode(final Request request) {
        final Identifier strategyNodeId = (Identifier)request.get(KpiParams.STRATEGY_NODE_ID.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (strategyNodeId == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<KpiData> data = this.kpiManager.getKpisByStrategyNode(strategyNodeId.getId(), page, sortList);
            final BaseValueObjectList result = new BaseValueObjectList();
            result.setValueObjectList((List)data);
            final Response response = this.OK(KpiParams.KPI_DATA_LIST.name(), (BaseValueObject)result);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getKpisByStrategyNode() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_BY_STR_NODE_012.name(), "Unable to get the Kpis with strategy node", new Object[] { e.getMessage() }));
        }
    }
    
    public Response validateKpiWithRootcode(final Request request) {
        final Identifier ownerId = (Identifier)request.get(KpiParams.KPI_OWNER_ID.name());
        final StringIdentifier kpiRootCode = (StringIdentifier)request.get(KpiParams.KPI_ROOT_CODE.name());
        if (ownerId == null || kpiRootCode == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final boolean status = this.kpiManager.validateKpiWithRootcode(ownerId.getId(), kpiRootCode.getId());
            return this.OK(KpiParams.BOOL_VALID_KPI.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - validateKpiWithRootcode() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_VALIDATE_ROOT_CODE_013.name(), "Failed to validate kpi root code", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getKpiUom(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.KPI_UOM_ID.name());
        if (id == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UOM_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final KpiUomData kpiUomData = this.getKpiManager().getKpiUom(id.getId());
            return this.OK(KpiParams.KPI_UOM_DATA.name(), (BaseValueObject)kpiUomData);
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getKpiUom() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UOM_UNABLE_TO_GET_004.name(), "Failed to load kpiUom", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getAllKpiUom(final Request request) {
        try {
            final SortList allKpiUomSortedList = request.getSortList();
            final List<KpiUomData> kpiUomDataList = this.getKpiManager().getAllKpiUom(allKpiUomSortedList);
            final BaseValueObjectList kpiUomDataListWrapper = new BaseValueObjectList();
            kpiUomDataListWrapper.setValueObjectList((List)kpiUomDataList);
            final Response response = this.OK(KpiParams.KPI_UOM_DATA_LIST.name(), (BaseValueObject)kpiUomDataListWrapper);
            response.setSortList(allKpiUomSortedList);
            return response;
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getAllKpiUom() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UOM_UNABLE_TO_GET_ALL_007.name(), "Failed to get all kpiUoms", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response createKpiUom(final Request request) {
        final KpiUomData data = (KpiUomData)request.get(KpiParams.KPI_UOM_DATA.name());
        if (data == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UOM_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final Integer kpiUomId = this.getKpiManager().createKpiUom(data);
            return this.OK(KpiParams.KPI_UOM_ID.name(), (BaseValueObject)new Identifier(kpiUomId));
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - createKpiUom() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UOM_UNABLE_TO_CREATE_003.name(), "Failed to create kpiUom", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response updateKpiUom(final Request request) {
        final KpiUomData data = (KpiUomData)request.get(KpiParams.KPI_UOM_DATA.name());
        if (data == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UOM_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final Integer uomId = this.kpiManager.createKpiUom(data);
            return this.OK(KpiParams.KPI_UOM_ID.name(), (BaseValueObject)new Identifier(uomId));
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - updateKpiUom() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UOM_UNABLE_TO_UPDATE_005.name(), "Failed to update kpiUom", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response deleteKpiUom(final Request request) {
        final Identifier identifier = (Identifier)request.get(KpiParams.KPI_UOM_ID.name());
        if (identifier == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UOM_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final boolean status = this.kpiManager.deleteKpiUom(identifier.getId());
            return this.OK(KpiParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - deleteKpiUom() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UOM_UNABLE_TO_DELETE_006.name(), "Failed to delete kpiUom", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getKpiType(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.KPI_TYPE_ID.name());
        if (id == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TYPE_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final KpiTypeData data = this.getKpiManager().getKpiType(id.getId());
            return this.OK(KpiParams.KPI_TYPE_DATA.name(), (BaseValueObject)data);
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getKpiType() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TYPE_UNABLE_TO_GET_004.name(), "Failed to load kpi type", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getAllKpiType(final Request request) {
        final SortList sortList = request.getSortList();
        try {
            final List<KpiTypeData> data = this.kpiManager.getAllKpiType(sortList);
            final BaseValueObjectList result = new BaseValueObjectList();
            result.setValueObjectList((List)data);
            final Response response = this.OK(KpiParams.KPI_TYPE_DATA_LIST.name(), (BaseValueObject)result);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getAllKpiTypes() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TYPE_UNABLE_TO_GET_ALL_007.name(), "Unable to load all the kpi types", new Object[] { e.getMessage() }));
        }
    }
    
    public Response createKpiType(final Request request) {
        final KpiTypeData data = (KpiTypeData)request.get(KpiParams.KPI_TYPE_DATA.name());
        if (data == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TYPE_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final Integer kpiTypeId = this.getKpiManager().createKpiType(data);
            return this.OK(KpiParams.KPI_TYPE_ID.name(), (BaseValueObject)new Identifier(kpiTypeId));
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - createKpiType() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TYPE_UNABLE_TO_CREATE_003.name(), "Failed to create kpi type", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response updateKpiType(final Request request) {
        final KpiTypeData data = (KpiTypeData)request.get(KpiParams.KPI_TYPE_DATA.name());
        if (data == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TYPE_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final Integer typeId = this.kpiManager.createKpiType(data);
            return this.OK(KpiParams.KPI_TYPE_ID.name(), (BaseValueObject)new Identifier(typeId));
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - updateKpiType() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TYPE_UNABLE_TO_UPDATE_005.name(), "Failed to update kpi type", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response deleteKpiType(final Request request) {
        final Identifier identifier = (Identifier)request.get(KpiParams.KPI_TYPE_ID.name());
        if (identifier == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TYPE_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final boolean status = this.kpiManager.deleteKpiType(identifier.getId());
            return this.OK(KpiParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - deleteKpiType() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TYPE_UNABLE_TO_DELETE_006.name(), "Failed to delete kpi type", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getKpiAggregationTypeById(final Request request) {
        final Identifier aggregationId = (Identifier)request.get(KpiParams.KPI_AGGR_TYPE_ID.name());
        if (aggregationId == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_AGGR_TYPE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final KpiAggregationTypeBean data = this.kpiManager.getKpiAggregationTypeById(aggregationId.getId());
            return this.OK(KpiParams.KPI_AGGR_TYPE_DATA.name(), (BaseValueObject)data);
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getKpiAggregationTypeById() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_AGGR_TYPE_UNABLE_TO_GET_004.name(), "Failed to load kpi aggregation type bean", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getAllKpiAggregationTypes(final Request request) {
        final SortList sortList = request.getSortList();
        try {
            final List<KpiAggregationTypeBean> data = this.kpiManager.getAllKpiAggregationTypes(sortList);
            final BaseValueObjectList result = new BaseValueObjectList();
            result.setValueObjectList((List)data);
            final Response response = this.OK(KpiParams.KPI_AGGR_TYPE_DATA_LIST.name(), (BaseValueObject)result);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getAllKpiAggregationTypes() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_AGGR_TYPE_UNABLE_TO_GET_ALL_007.name(), "unable to load all the kpi aggregation types", new Object[] { e.getMessage() }));
        }
    }
    
    public Response saveKpiScore(Request request) {
        final KpiScoreBean data = (KpiScoreBean)request.get(KpiParams.KPI_SCORE_DATA.name());
        final Identifier periodId = (Identifier)request.get(KpiParams.PERIOD_MASTER_ID.name());
        final StringIdentifier scoreAggregationType = (StringIdentifier)request.get(KpiParams.SCORE_AGGREGATION_TYPE.name());
        final DateResponse sysDate = (DateResponse)request.get(KpiParams.SYSTEM_DATE.name());
        if (data == null || periodId == null || periodId.getId() == null || periodId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCORE_INVALID_INPUT_002.name(), "Invalid data in the request"));
        }
        try {
            int inputPeriodMasterId = periodId.getId();
            final PeriodMasterBean inputPeriodMasterBean = this.getKpiManager().getPeriodMasterById(inputPeriodMasterId);
            if (inputPeriodMasterBean == null || inputPeriodMasterBean.getEndPeriodMasterId() == null || inputPeriodMasterBean.getEndPeriodMasterId() <= 0) {
                return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_PRD_MASTER_END_PERIOD_MASTER_ID_NULL_009.name(), "End Period master id is null."));
            }
            boolean isKpiAggTypeSum = false;
            boolean isScoreAggTypeSum = false;
            if (scoreAggregationType != null && scoreAggregationType.getId() != null && scoreAggregationType.getId().equalsIgnoreCase(KpiValidationParams.ScoreAggregationType.SUMMATION.name())) {
                isScoreAggTypeSum = true;
            }
            final Kpi kpi = this.getKpiManager().getKpi(data.getKpiId());
            final KpiData kpiData = kpi.getKpiDetails();
            if (kpiData == null || kpiData.getId() == null || kpiData.getId() <= 0) {
                return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_004.name(), "Unable to get KPI."));
            }
            KpiManagerServiceImpl.log.debug((Object)("SCORELOG Saving score for: " + kpiData + ": period: " + periodId));
            if (kpiData.getAggregationType() != null && kpiData.getAggregationType().getType().equalsIgnoreCase(KpiValidationParams.KpiAggregationType.SUMMATION.name())) {
                isKpiAggTypeSum = true;
            }
            boolean isInitiative = false;
            if (kpiData.getKpiType() != null && kpiData.getKpiType().getType() != null && kpiData.getKpiType().getType().equalsIgnoreCase(KpiValidationParams.KpiType.INITIATIVE.name())) {
                isInitiative = true;
            }
            final boolean isScoreChanged = this.checkScoreChange(data, periodId, kpiData.getKpiScore(), isScoreAggTypeSum, isInitiative);
            if (!isScoreChanged) {
                KpiManagerServiceImpl.log.debug((Object)"SCORELOG No change in score. Returning without doing anything");
                final Kpi kpiDomain = (Kpi)this.getKpiManager().getCache().getIfPresent(kpiData.getId());
                if (kpiDomain != null && kpiDomain.getKpiDetails() != null) {
                    kpiData.getKpiScore().add(data);
                    kpiDomain.getKpiDetails().setKpiScore(kpiData.getKpiScore());
                    this.getKpiManager().getCache().put(kpiData.getId(), kpiDomain);
                }
                return this.OK(KpiParams.KPI_ID.name(), (BaseValueObject)new Identifier(kpiData.getId()));
            }
            final List<Integer> recomputePeriodIds = new ArrayList<Integer>();
            final List<PeriodMasterBean> recomputePeriods = new ArrayList<PeriodMasterBean>();
            for (int j = inputPeriodMasterId; j <= inputPeriodMasterBean.getEndPeriodMasterId(); ++j) {
                recomputePeriodIds.add(j);
                if (this.getKpiManager().getPeriodMasterById(j) != null) {
                    recomputePeriods.add(this.getKpiManager().getPeriodMasterById(j));
                }
                if (recomputePeriodIds.size() == 12) {
                    break;
                }
            }
            if (!isKpiAggTypeSum && !isInitiative) {
                recomputePeriodIds.clear();
                recomputePeriodIds.add(inputPeriodMasterId);
            }
            Date systemDate = new Date();
            if (sysDate != null && sysDate.getDate() != null) {
                systemDate = sysDate.getDate();
            }
            final Integer currentPeriodId = PeriodUtil.getCurrentPeriod(recomputePeriods, systemDate);
            if (currentPeriodId == null) {
                return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCORE_UNABLE_TO_GET_CURR_PERIOD_011.name(), "Invalid current period."));
            }
            boolean haveBreakups = false;
            if (kpiData.getKpiTarget() != null && kpiData.getKpiTarget() != null && kpiData.getKpiTarget().getTargetData() != null && !kpiData.getKpiTarget().getTargetData().isEmpty()) {
                haveBreakups = true;
            }
            final KpiCalculatorManagerService calculatorService = (KpiCalculatorManagerService)this.serviceLocator.getService("DefaultKpiCalculatorManagerServiceImpl");
            List<KpiScoreBean> scoreBeansToSave = new ArrayList<KpiScoreBean>();
            List<KpiScoreBean> kpisToSave = new ArrayList<KpiScoreBean>();
            KpiScoreBean kpiScoreutil = null;
            boolean breakIteration = false;
            Double lastAchievement = null;
            Double lastNumTarget = null;
            Date lastDateTarget = null;
            Integer ratingLevel = null;
            for (final int periodIds : recomputePeriodIds) {
                KpiManagerServiceImpl.log.debug((Object)("SCORELOG Computing score for: " + kpiData + ", period: " + periodIds));
                KpiScoreBean scoreBean = new KpiScoreBean();
                double applicableTarget = 0.0;
                double applicableActual = 0.0;
                final DoubleIdentifier mtd = new DoubleIdentifier();
                if (!breakIteration) {
                    if (haveBreakups) {
                        applicableTarget = this.getAggregateApplicableTarget(kpiData.getKpiTarget().getTargetData(), isKpiAggTypeSum, periodIds, kpiData.getStartDate(), mtd);
                    }
                    else if (kpiData.getKpiTarget().getNumTarget() != null) {
                        applicableTarget = kpiData.getKpiTarget().getNumTarget();
                    }
                    applicableActual = this.getAggregateApplicableActual(kpiData.getKpiScore(), isScoreAggTypeSum, periodIds, inputPeriodMasterId);
                    if (periodIds >= inputPeriodMasterId) {
                        if (data.getNumScore_mtd() != null && isScoreAggTypeSum) {
                            applicableActual += data.getNumScore_mtd();
                        }
                        else if (data.getNumScore() != null) {
                            applicableActual += data.getNumScore();
                        }
                    }
                    KpiManagerServiceImpl.log.debug((Object)("SCORELOG applicable act: " + applicableActual + ", tgt: " + applicableTarget));
                    request = new Request();
                    request.put(KpiParams.KPI_ACIEVEMENT_TYPE.name(), (BaseValueObject)new StringIdentifier(kpiData.getKpiTarget().getAchievementType().name()));
                    request.put(KpiParams.KPI_TYPE_NAME.name(), (BaseValueObject)new StringIdentifier(kpiData.getKpiType().getType()));
                    request.put(KpiParams.KPI_TARGET_SCALE.name(), (BaseValueObject)kpiData.getKpiTarget().getScale());
                    request.put(KpiParams.KPI_SCORE_ACTUAL.name(), (BaseValueObject)new DoubleIdentifier(applicableActual));
                    request.put(KpiParams.KPI_TARGET.name(), (BaseValueObject)new DoubleIdentifier(kpiData.getKpiTarget().getNumTarget()));
                    request.put(KpiParams.KPI_TARGET_CUMULATIVE.name(), (BaseValueObject)new DoubleIdentifier(applicableTarget));
                    final DateResponse dateActual = new DateResponse();
                    dateActual.setDate(data.getDateScore());
                    request.put(KpiParams.KPI_DATE_SCORE_ACTUAL.name(), (BaseValueObject)dateActual);
                    final DateResponse dateTarget = new DateResponse();
                    if (kpiData.getKpiTarget() != null) {
                        dateTarget.setDate(kpiData.getKpiTarget().getDateTarget());
                    }
                    request.put(KpiParams.KPI_DATE_TARGET.name(), (BaseValueObject)dateTarget);
                    final Response response = calculatorService.calculateKpiPerformancePoints(request);
                    final KpiCalcUtility identifier = (KpiCalcUtility)response.get(KpiParams.KPI_CALC_UTILITY.name());
                    boolean isActualNotNull = false;
                    for (final KpiScoreBean iterator : kpiData.getKpiScore()) {
                        if (periodIds == iterator.getPeriodMasterId() && iterator.getNumScore() != null) {
                            isActualNotNull = true;
                            break;
                        }
                    }
                    for (final KpiScoreBean score : kpiData.getKpiScore()) {
                        if (score.getKpiId() != null && data.getKpiId() != null && score.getKpiId() == (int)data.getKpiId() && score.getPeriodMasterId() != null && score.getPeriodMasterId() == periodIds) {
                            kpiData.getKpiScore().remove(score);
                            scoreBean = score;
                            break;
                        }
                    }
                    if (periodIds == inputPeriodMasterId) {
                        final Integer id = scoreBean.getId();
                        scoreBean = data;
                        scoreBean.setId(id);
                    }
                    if (!isActualNotNull && periodIds != inputPeriodMasterId) {
                        scoreBean.setNumScore((Double)null);
                        scoreBean.setNumScore_mtd((Double)null);
                    }
                    else if (dateTarget.getDate() == null) {
                        scoreBean.setNumScore(applicableActual);
                    }
                    scoreBean.setRatingLevel(identifier.getRatingLevel());
                    scoreBean.setAchievement(identifier.getAchievement());
                    if (dateTarget.getDate() == null) {
                        scoreBean.setNumTarget(applicableTarget);
                    }
                    scoreBean.setNumTarget_mtd(mtd.getId());
                    scoreBean.setDateScore(data.getDateScore());
                    scoreBean.setDateTarget(dateTarget.getDate());
                    scoreBean.setPeriodMasterId(periodIds);
                    scoreBean.setKpiId(kpiData.getId());
                    if (!haveBreakups) {
                        breakIteration = true;
                        lastAchievement = scoreBean.getAchievement();
                        ratingLevel = scoreBean.getRatingLevel();
                        lastNumTarget = scoreBean.getNumTarget();
                        lastDateTarget = dateTarget.getDate();
                    }
                }
                else {
                    KpiManagerServiceImpl.log.debug((Object)"Kpi have no break ups");
                    for (final KpiScoreBean score2 : kpiData.getKpiScore()) {
                        if (score2.getKpiId() != null && data.getKpiId() != null && score2.getKpiId() == (int)data.getKpiId() && score2.getPeriodMasterId() != null && score2.getPeriodMasterId() == periodIds) {
                            kpiData.getKpiScore().remove(score2);
                            scoreBean = score2;
                            break;
                        }
                    }
                    if (periodIds == inputPeriodMasterId) {
                        final Integer id2 = scoreBean.getId();
                        scoreBean = data;
                        scoreBean.setId(id2);
                    }
                    scoreBean.setAchievement(lastAchievement);
                    scoreBean.setRatingLevel(ratingLevel);
                    scoreBean.setNumTarget(lastNumTarget);
                    scoreBean.setDateTarget(lastDateTarget);
                    scoreBean.setPeriodMasterId(periodIds);
                    scoreBean.setKpiId(kpiData.getId());
                }
                scoreBeansToSave.add(scoreBean);
                kpiData.getKpiScore().add(scoreBean);
                if (currentPeriodId == periodIds) {
                    kpiScoreutil = new KpiScoreBean();
                    kpiScoreutil = scoreBean;
                    kpiData.setCurrRatingLevel(scoreBean.getRatingLevel());
                    kpiData.setNumTarget(scoreBean.getNumTarget());
                    kpiData.setDateTarget(scoreBean.getDateTarget());
                    kpiData.setCurrAchievement(scoreBean.getAchievement());
                    kpiData.setCurrDateScore(scoreBean.getDateScore());
                    kpiData.setCurrScore(scoreBean.getNumScore());
                    kpiData.setCurrPeriodId(periodIds);
                }
            }
            if (!scoreBeansToSave.isEmpty()) {
                final BaseValueObjectList scoreBvol = new BaseValueObjectList();
                scoreBvol.setValueObjectList((List)scoreBeansToSave);
                final int count = this.kpiManager.saveKpiScoreForMultiple(scoreBvol);
                KpiManagerServiceImpl.log.info((Object)("Number of Scores updated " + count + " for the kpi " + kpiData.getId()));
            }
            this.getKpiManager().getCache().remove(kpiData.getId());
            if (kpiScoreutil != null) {
                final BaseValueObjectList scoreBvol = new BaseValueObjectList();
                kpisToSave.add(kpiScoreutil);
                scoreBvol.setValueObjectList((List)kpisToSave);
                final int count = this.kpiManager.saveScoreDetailsInKpiForMultiple(scoreBvol);
                KpiManagerServiceImpl.log.info((Object)("Number of kpis updated " + count + " for the kpi " + kpiData.getId()));
            }
            if (kpiData.getGroupId() != null && kpiData.getGroupId() > 0 && kpiData.getKpiScorecardRelationships() != null && !kpiData.getKpiScorecardRelationships().isEmpty()) {
                KpiManagerServiceImpl.log.debug((Object)("SCORELOG Has parent: " + kpiData.getGroupId()));
                final Map<Integer, KpiScoreBean> parentKpiListToSave = new HashMap<Integer, KpiScoreBean>();
                scoreBeansToSave = new ArrayList<KpiScoreBean>();
                kpisToSave = new ArrayList<KpiScoreBean>();
                request = new Request();
                final Integer scorecardId = kpiData.getKpiScorecardRelationships().get(0).getEmpScorecardId();
                request.put(KpiParams.SCORECARD_ID.name(), (BaseValueObject)new Identifier(scorecardId));
                final Response response2 = this.getKpisByScorecardId(request);
                final BaseValueObjectList kpiList = (BaseValueObjectList)response2.get(KpiParams.KPI_DATA_LIST.name());
                final List<KpiData> kpiDataList = (List<KpiData>)kpiList.getValueObjectList();
                final Map<Integer, KpiData> groupkpisMap = new HashMap<Integer, KpiData>();
                final Map<Integer, List<KpiData>> groupChildkpisMap = new HashMap<Integer, List<KpiData>>();
                List<KpiData> kpiDataListTemp = null;
                for (final KpiData kpiTemp : kpiDataList) {
                    KpiManagerServiceImpl.log.debug((Object)("SCORELOG Analyzing list " + kpiTemp));
                    if (kpiTemp.getStatus() != null && kpiTemp.getStatus() != 0) {
                        if (kpiTemp.getGroupKpi() != null && kpiTemp.getGroupKpi() && !groupkpisMap.containsKey(kpiTemp.getId())) {
                            groupkpisMap.put(kpiTemp.getId(), kpiTemp);
                        }
                        if (kpiTemp.getGroupId() == null) {
                            continue;
                        }
                        if (groupChildkpisMap.containsKey(kpiTemp.getGroupId())) {
                            kpiDataListTemp = groupChildkpisMap.get(kpiTemp.getGroupId());
                        }
                        else {
                            kpiDataListTemp = new ArrayList<KpiData>();
                        }
                        kpiDataListTemp.add(kpiTemp);
                        groupChildkpisMap.put(kpiTemp.getGroupId(), kpiDataListTemp);
                        KpiManagerServiceImpl.log.debug((Object)("SCORELOG Added to map " + kpiTemp.getGroupId() + "->" + kpiTemp.getId()));
                    }
                }
                final Set<Integer> relaodKpis = new HashSet<Integer>();
                PeriodMasterBean periodMasterBean = this.getKpiManager().getPeriodMasterById(inputPeriodMasterId);
                if (periodMasterBean.getApplicablePeriodIds() != null) {
                    final String applicablePeriodIds = periodMasterBean.getApplicablePeriodIds();
                    final String[] str = applicablePeriodIds.trim().split(",");
                    final int[] subArr = new int[str.length];
                    for (int i = 0; i < str.length; ++i) {
                        subArr[i] = Integer.parseInt(str[i]);
                    }
                    Arrays.sort(subArr);
                    final int leastPeriod = subArr[0];
                    Integer endPeriod = null;
                    if (!isKpiAggTypeSum) {
                        endPeriod = subArr[subArr.length - 1];
                    }
                    else {
                        periodMasterBean = this.getKpiManager().getPeriodMasterById(leastPeriod);
                        if (periodMasterBean != null && periodMasterBean.getEndPeriodMasterId() != null && periodMasterBean.getEndPeriodMasterId() > 0) {
                            endPeriod = periodMasterBean.getEndPeriodMasterId();
                        }
                    }
                    if (endPeriod == null || endPeriod <= 0) {
                        return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_PRD_MASTER_UNABLE_TO_GET_004.name(), "Period master is null." + periodId.getId()));
                    }
                    for (int periods = leastPeriod; periods <= endPeriod; ++periods) {
                        KpiManagerServiceImpl.log.debug((Object)("Group score calc applicable period " + periods));
                        Date startDate = null;
                        periodMasterBean = this.getKpiManager().getPeriodMasterById(periods);
                        if (periodMasterBean != null && periodMasterBean.getStartDate() != null) {
                            startDate = periodMasterBean.getStartDate();
                        }
                        if (startDate == null) {
                            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCORE_PRD_START_DATE_NOT_FOUND_009.name(), "Period msater start date should be mandatory for the given period " + periodId.getId()));
                        }
                        inputPeriodMasterId = periods;
                        this.recursive(kpiData, inputPeriodMasterId, startDate, groupkpisMap, groupChildkpisMap, parentKpiListToSave, scoreBeansToSave, inputPeriodMasterId, relaodKpis);
                    }
                }
                else {
                    KpiManagerServiceImpl.log.debug((Object)"No applicable periods");
                    for (final int periods2 : recomputePeriodIds) {
                        KpiManagerServiceImpl.log.debug((Object)("Group score calc period " + periods2));
                        Date startDate2 = null;
                        periodMasterBean = this.getKpiManager().getPeriodMasterById(periods2);
                        if (periodMasterBean != null && periodMasterBean.getStartDate() != null) {
                            startDate2 = periodMasterBean.getStartDate();
                        }
                        if (startDate2 == null) {
                            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCORE_PRD_START_DATE_NOT_FOUND_009.name(), "Period msater start date should be mandatory for the given period " + periodId.getId()));
                        }
                        inputPeriodMasterId = periods2;
                        this.recursive(kpiData, inputPeriodMasterId, startDate2, groupkpisMap, groupChildkpisMap, parentKpiListToSave, scoreBeansToSave, currentPeriodId, relaodKpis);
                    }
                }
                if (!scoreBeansToSave.isEmpty()) {
                    final BaseValueObjectList scoreBvol2 = new BaseValueObjectList();
                    scoreBvol2.setValueObjectList((List)scoreBeansToSave);
                    final int count2 = this.kpiManager.saveKpiScoreForMultiple(scoreBvol2);
                    KpiManagerServiceImpl.log.info((Object)("Number of group Scores updated " + count2 + " for the kpi"));
                }
                if (parentKpiListToSave != null && !parentKpiListToSave.isEmpty()) {
                    final BaseValueObjectList scoreBvol2 = new BaseValueObjectList();
                    for (final Map.Entry<Integer, KpiScoreBean> entry : parentKpiListToSave.entrySet()) {
                        kpisToSave.add(entry.getValue());
                        relaodKpis.add(entry.getValue().getKpiId());
                    }
                    scoreBvol2.setValueObjectList((List)kpisToSave);
                    final int count2 = this.kpiManager.saveScoreDetailsInKpiForMultiple(scoreBvol2);
                    KpiManagerServiceImpl.log.info((Object)("Number of group kpis updated " + count2 + " for the kpi"));
                }
                for (final Integer removeId : relaodKpis) {
                    if (removeId != null) {
                        this.getKpiManager().getCache().remove(removeId);
                    }
                }
            }
            return this.OK(KpiParams.KPI_ID.name(), (BaseValueObject)new Identifier(kpiData.getId()));
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - saveKpiScore : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCORE_UNABLE_TO_CREATE_003.name(), "Failed to save kpi score", new Object[] { e.getMessage() }));
        }
    }
    
    private double getAggregateApplicableActual(final List<KpiScoreBean> scoreData, final boolean isScoreAggTypeSum, final int periodMasterId, final int inputPeriodId) {
        double actual_ytd = 0.0;
        if (isScoreAggTypeSum) {
            for (final KpiScoreBean iterator : scoreData) {
                if (iterator.getPeriodMasterId() != null && iterator.getPeriodMasterId() <= periodMasterId && iterator.getPeriodMasterId() != inputPeriodId && iterator.getNumScore_mtd() != null) {
                    actual_ytd += iterator.getNumScore_mtd();
                }
            }
        }
        else {
            final Iterator<KpiScoreBean> iterator3 = scoreData.iterator();
            if (iterator3.hasNext()) {
                final KpiScoreBean iterator = iterator3.next();
                if (periodMasterId == iterator.getPeriodMasterId() && iterator.getPeriodMasterId() != inputPeriodId && iterator.getNumScore() != null) {
                    actual_ytd = iterator.getNumScore();
                }
            }
        }
        return actual_ytd;
    }
    
    private double getAggregateApplicableTarget(final List<KpiTargetDataBean> targetData, final boolean isKpiAggTypeSum, final int periodMasterId, final Date startDate, final DoubleIdentifier mtd) {
        double target_ytd = 0.0;
        double target_mtd = 0.0;
        if (isKpiAggTypeSum) {
            KpiManagerServiceImpl.log.debug((Object)"SCORELOG: Aggregating target");
            for (final KpiTargetDataBean iterator : targetData) {
                KpiManagerServiceImpl.log.debug((Object)("SCORELOG: Aggregating target with " + iterator.getTargetDataNum() + ", target p =" + iterator.getPeriodMasterId() + ", calc p =" + periodMasterId));
                if (iterator.getTargetDataNum() != null && iterator.getTargetDataNum() > 0.0 && iterator.getPeriodMasterId() != null && iterator.getPeriodMasterId() <= periodMasterId && iterator.getTargetDataNum() != null) {
                    final PeriodMasterBean period = this.getKpiManager().getPeriodMasterById(iterator.getPeriodMasterId());
                    final Date periodEndDate = period.getEndDate();
                    if (startDate != null && periodEndDate != null) {
                        if (periodEndDate.after(startDate)) {
                            target_ytd += iterator.getTargetDataNum();
                        }
                    }
                    else {
                        target_ytd += iterator.getTargetDataNum();
                    }
                    KpiManagerServiceImpl.log.debug((Object)("TGTLOG " + startDate + " -> " + periodEndDate + " : " + target_ytd));
                    if (!iterator.getPeriodMasterId().equals(periodMasterId)) {
                        continue;
                    }
                    target_mtd = iterator.getTargetDataNum();
                }
            }
        }
        else {
            for (final KpiTargetDataBean iterator : targetData) {
                if (iterator.getTargetDataNum() != null && iterator.getTargetDataNum() > 0.0 && iterator.getTargetDataNum() != null && periodMasterId == iterator.getPeriodMasterId()) {
                    target_ytd = iterator.getTargetDataNum();
                    break;
                }
            }
        }
        mtd.setId(new Double(target_mtd));
        return target_ytd;
    }
    
    private void recursive(KpiData kpiData, final int periodId, final Date startDate, final Map<Integer, KpiData> groupkpisMap, final Map<Integer, List<KpiData>> groupChildkpisMap, final Map<Integer, KpiScoreBean> parentKpiListToSave, final List<KpiScoreBean> parentKpiScoreListToSave, final Integer currentPeriodId, final Set<Integer> relaodKpis) {
        KpiManagerServiceImpl.log.debug((Object)("SCORELOG Computing score for parent of " + kpiData + ", period: " + periodId));
        if (!groupChildkpisMap.containsKey(kpiData.getGroupId())) {
            return;
        }
        final List<KpiData> kpiList = groupChildkpisMap.get(kpiData.getGroupId());
        KpiManagerServiceImpl.log.debug((Object)"SCORELOG Computing score for GK - found in map");
        final double groupAchievement = this.parentScoreCalc(kpiList, periodId, startDate, groupkpisMap);
        final KpiScoreBean scoreBean = new KpiScoreBean();
        final KpiData kpiDataGroup = groupkpisMap.get(kpiData.getGroupId());
        scoreBean.setAchievement(groupAchievement);
        scoreBean.setKpiId(kpiDataGroup.getId());
        scoreBean.setPeriodMasterId(periodId);
        final Iterator<KpiScoreBean> it = kpiDataGroup.getKpiScore().iterator();
        while (it.hasNext()) {
            final KpiScoreBean score = it.next();
            if (score.getKpiId() != null && scoreBean.getKpiId() != null && score.getKpiId() == (int)scoreBean.getKpiId() && score.getPeriodMasterId() != null && scoreBean.getPeriodMasterId() != null && score.getPeriodMasterId() == (int)scoreBean.getPeriodMasterId()) {
                it.remove();
                scoreBean.setId(score.getId());
                break;
            }
        }
        kpiDataGroup.getKpiScore().add(scoreBean);
        if (currentPeriodId == periodId) {
            kpiDataGroup.setCurrPeriodId(periodId);
            kpiDataGroup.setCurrRatingLevel(scoreBean.getRatingLevel());
            kpiDataGroup.setNumTarget(scoreBean.getNumTarget());
            kpiDataGroup.setDateTarget(scoreBean.getDateTarget());
            kpiDataGroup.setCurrAchievement(scoreBean.getAchievement());
            kpiDataGroup.setCurrDateScore(scoreBean.getDateScore());
            kpiDataGroup.setCurrScore(scoreBean.getNumScore());
            parentKpiListToSave.put(kpiDataGroup.getId(), scoreBean);
        }
        parentKpiScoreListToSave.add(scoreBean);
        groupkpisMap.put(kpiDataGroup.getId(), kpiDataGroup);
        relaodKpis.add(kpiDataGroup.getId());
        kpiData = kpiDataGroup;
        if (kpiData.getGroupId() != null && kpiData.getGroupId() > 0 && kpiData.getKpiScorecardRelationships() != null && !kpiData.getKpiScorecardRelationships().isEmpty()) {
            this.recursive(kpiData, periodId, startDate, groupkpisMap, groupChildkpisMap, parentKpiListToSave, parentKpiScoreListToSave, currentPeriodId, relaodKpis);
        }
    }
    
    private double parentScoreCalc(final List<KpiData> kpiList, final int periodId, final Date startDate, final Map<Integer, KpiData> groupkpisMap) {
        double parentScore = 0.0;
        float weightage = 0.0f;
        Double ytdAchivement = 0.0;
        Double achivement = 0.0;
        KpiManagerServiceImpl.log.debug((Object)("SCORELOG Computing score for parent: " + kpiList.size() + " children, period: " + periodId + " start date: " + startDate));
        for (KpiData kpi : kpiList) {
            if (groupkpisMap.containsKey(kpi.getId())) {
                kpi = groupkpisMap.get(kpi.getId());
            }
            ytdAchivement = 0.0;
            achivement = 0.0;
            KpiManagerServiceImpl.log.debug((Object)("SCORELOG Child : " + kpi + ", period: " + periodId));
            if (kpi.getStartDate() != null && kpi.getStartDate().compareTo(startDate) <= 0) {
                if (kpi.getKpiType() != null && kpi.getKpiType().getType() != null && kpi.getKpiType().getType().equalsIgnoreCase(KpiValidationParams.KpiType.INITIATIVE.name())) {
                    KpiManagerServiceImpl.log.debug((Object)("SCORELOG Initiative Child : " + kpi + ", period: " + periodId + " is started: " + kpi.getStartDate()));
                    if (kpi.getKpiScore() != null && !kpi.getKpiScore().isEmpty()) {
                        for (final KpiScoreBean score : kpi.getKpiScore()) {
                            if (score.getPeriodMasterId() != null && score.getPeriodMasterId() <= periodId && score.getAchievement() != null) {
                                achivement = (ytdAchivement = score.getAchievement());
                            }
                        }
                    }
                }
                else if (kpi.getKpiScore() != null && !kpi.getKpiScore().isEmpty()) {
                    boolean isapplicable = false;
                    boolean breakIteration = false;
                    for (final KpiScoreBean score2 : kpi.getKpiScore()) {
                        KpiManagerServiceImpl.log.debug((Object)("SCORELOG Child : " + kpi + ", score period: " + score2.getPeriodMasterId() + ", compute period: " + periodId + ": score count: " + kpi.getKpiScore().size()));
                        if (score2.getPeriodMasterId() != null) {
                            final PeriodMasterBean periodMasterBean = this.getKpiManager().getPeriodMasterById(score2.getPeriodMasterId());
                            if (periodMasterBean.getApplicablePeriodIds() != null) {
                                final String applicablePeriodIds = periodMasterBean.getApplicablePeriodIds();
                                final String[] split;
                                final String[] str = split = applicablePeriodIds.trim().split(",");
                                for (final String periods : split) {
                                    final int applPeriod = Integer.parseInt(periods.trim());
                                    if (applPeriod == periodId) {
                                        isapplicable = true;
                                        breakIteration = true;
                                    }
                                    else if (applPeriod > periodId) {
                                        breakIteration = true;
                                    }
                                }
                            }
                            else if (score2.getPeriodMasterId() == periodId) {
                                isapplicable = true;
                                breakIteration = true;
                            }
                        }
                        ytdAchivement = score2.getAchievement();
                        if (score2.getAchievement() != null && isapplicable) {
                            achivement = score2.getAchievement();
                            breakIteration = true;
                        }
                        if (breakIteration) {
                            break;
                        }
                    }
                }
                KpiManagerServiceImpl.log.debug((Object)("SCORELOG Child : " + kpi + ", period: " + periodId + ", achievement: " + achivement + ", ytd ach: " + ytdAchivement + ", w: " + kpi.getWeightage()));
                if (kpi.getAggregationType() != null && kpi.getAggregationType().getType() != null && kpi.getAggregationType().getType().equalsIgnoreCase(KpiValidationParams.KpiAggregationType.SUMMATION.name())) {
                    achivement = ytdAchivement;
                    KpiManagerServiceImpl.log.debug((Object)("SCORELOG Child : " + kpi + " ytd score carried forward"));
                }
                weightage += kpi.getWeightage();
                parentScore += achivement * kpi.getWeightage();
            }
        }
        KpiManagerServiceImpl.log.debug((Object)("SCORELOG p=" + parentScore + "/ w=" + weightage));
        if (weightage != 0.0f) {
            parentScore /= weightage;
        }
        else {
            parentScore = 0.0;
        }
        return parentScore;
    }
    
    public Response updateKpiScore(final Request request) {
        final KpiScoreBean data = (KpiScoreBean)request.get(KpiParams.KPI_SCORE_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCORE_INVALID_INPUT_002.name(), "Invalid data in the request"));
        }
        try {
            final Integer id = this.kpiManager.saveKpiScore(data);
            final Kpi kpi = this.getKpiManager().getKpi(data.getId());
            final KpiData kpiData = kpi.getKpiDetails();
            if (kpiData != null && kpiData.getId() != null) {
                if (data.getRatingLevel() != null) {
                    kpiData.setCurrRatingLevel(data.getRatingLevel());
                }
                if (data.getAchievement() != null) {
                    kpiData.setCurrAchievement(data.getAchievement());
                }
                if (data.getDateScore() != null) {
                    kpiData.setCurrDateScore(data.getDateScore());
                }
                if (data.getNumScore() != null) {
                    kpiData.setCurrScore(data.getNumScore());
                }
                kpi.setKpiDetails(kpiData);
                kpi.save();
            }
            return this.OK(KpiParams.KPI_SCORE_DATA_ID.name(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - updateKpiScore : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCORE_UNABLE_TO_UPDATE_005.name(), "Failed to update kpi score", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getKpiScoreById(final Request request) {
        final Identifier identifier = (Identifier)request.get(KpiParams.KPI_SCORE_DATA_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCORE_INVALID_INPUT_002.name(), "Invalid data in the request"));
        }
        try {
            final KpiScoreBean scoreBean = this.kpiManager.getKpiScoreById(identifier.getId());
            return this.OK(KpiParams.KPI_SCORE_DATA.name(), (BaseValueObject)scoreBean);
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getKpiScoreById : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCORE_UNABLE_TO_GET_004.name(), "Failed to load kpi score", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getAllPeriodTypes(final Request request) {
        final SortList sortList = request.getSortList();
        try {
            final List<PeriodTypeBean> data = this.kpiManager.getAllPeriodTypes(sortList);
            final BaseValueObjectList result = new BaseValueObjectList();
            result.setValueObjectList((List)data);
            final Response response = this.OK(KpiParams.PERIOD_TYPE_LIST.name(), (BaseValueObject)result);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getAllPeriodTypes() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_PRD_TYPE_UNABLE_TO_GET_ALL_007.name(), "Failed to load all period types", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getPositionKpiEmployeeRelations(final Request request) {
        final Identifier identifier = (Identifier)request.get(KpiParams.POSITION_ID.name());
        if (identifier == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final List<KpiData> alKpiData = this.kpiManager.getPositionKpiEmployeeRelations(identifier.getId());
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)alKpiData);
            return this.OK(KpiParams.KPI_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getPositionKpiEmployeeRelations() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_POS_KPI_EMP_REL_020.name(), "Failed to get kpi list", new Object[] { ex.getMessage() }));
        }
    }
    
    public KpiManager getKpiManager() {
        return this.kpiManager;
    }
    
    public void setKpiManager(final KpiManager kpiManager) {
        this.kpiManager = kpiManager;
    }
    
    public Response searchKpi(final Request request) {
        final KpiData kpiData = (KpiData)request.get(KpiParams.KPI_DATA.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (kpiData == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final List<KpiData> kpiList = this.kpiManager.searchKpi(kpiData, page, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiList);
            return this.OK(KpiParams.KPI_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - searchKpi() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_SEARCH_008.name(), "Failed to get kpi list", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response searchKpiScore(final Request request) {
        final KpiScoreBean kpiScoreBean = (KpiScoreBean)request.get(KpiParams.KPI_SCORE_DATA.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (kpiScoreBean == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCORE_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final List<KpiScoreBean> kpiList = this.kpiManager.searchKpiScore(kpiScoreBean, page, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiList);
            return this.OK(KpiParams.KPI_SCORE_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - searchKpiScore() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCORE_UNABLE_TO_SEARCH_008.name(), "Failed to get kpi list", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getPeriodMasterByPeriodType(final Request request) {
        final Identifier id = (Identifier)request.get(KpiParams.PERIOD_TYPE_ID.name());
        final SortList sortList = request.getSortList();
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_PRD_MASTER_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<PeriodMasterBean> data = this.kpiManager.getPeriodMasterByPeriodType(id.getId(), sortList);
            final BaseValueObjectList result = new BaseValueObjectList();
            result.setValueObjectList((List)data);
            final Response response = this.OK(KpiParams.PERIOD_MASTER_DATA_LIST.name(), (BaseValueObject)result);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getPeriodMasterByPeriodType() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_PRD_MASTER_UNABLE_TO_GET_004.name(), "failed to load PeriodMaster By PeriodType() : ", new Object[] { e.getMessage() }));
        }
    }
    
    public Response searchPeriodType(final Request request) {
        final PeriodTypeBean periodType = (PeriodTypeBean)request.get(KpiParams.PERIOD_TYPE_DATA.name());
        final SortList sortList = request.getSortList();
        if (periodType == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_PRD_TYPE_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final List<PeriodTypeBean> periodTypeList = this.kpiManager.searchPeriodType(periodType, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)periodTypeList);
            final Response response = this.OK(KpiParams.PERIOD_TYPE_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl.searchPeriodType() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_PRD_TYPE_UNABLE_TO_SEARCH_008.name(), "Failed to search period type", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response searchKpiTag(final Request request) {
        final KpiTagBean kpiTag = (KpiTagBean)request.get(KpiParams.KPI_TAG_DATA.name());
        final SortList sortList = request.getSortList();
        if (kpiTag == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TAG_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final List<KpiTagBean> kpiTagList = this.kpiManager.searchKpiTag(kpiTag, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiTagList);
            final Response response = this.OK(KpiParams.KPI_TAG_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - searchKpiTag() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TAG_UNABLE_TO_SEARCH_008.name(), "Failed to search kpi tag", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getKpiRelationshipTypeNameById(final Request request) {
        final Identifier typeId = (Identifier)request.get(KpiParams.KPI_REL_TYPE_ID.name());
        StringIdentifier typeName = null;
        if (typeId == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        try {
            typeName = this.kpiManager.getKpiRelationshipTypeNameById(typeId);
            return this.OK(KpiParams.KPI_REL_TYPE_NAME.name(), (BaseValueObject)typeName);
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getKpiRelationshipTypeNameById() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_REL_TYPE_019.name(), "Failed to Load KpiRelationshipTypeName By Id", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getKpisByScorecardIdAndKpiTagId(final Request request) {
        final Identifier scorecardId = (Identifier)request.get(KpiParams.SCORECARD_ID.name());
        final Identifier kpiTagId = (Identifier)request.get(KpiParams.KPI_TAG_ID.name());
        if (kpiTagId == null || scorecardId == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        try {
            List<KpiData> scList = this.kpiManager.getKpisByScorecardIdAndKpiTagId(scorecardId, kpiTagId, page, sortList);
            scList = this.periodUtil.updateCurrentScoreDetails(scList);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)scList);
            final Response response = this.OK(KpiParams.KPI_DATA_LIST.name(), (BaseValueObject)bList);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getKpisByScorecardIdAndKpiTagId() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_BY_SID_TID_014.name(), "Failed to load kpis by scorecardId and kpiTagId", new Object[] { e.getMessage() }));
        }
    }
    
    public Response searchPeriodMaster(final Request request) {
        final PeriodMasterBean periodMasterBean = (PeriodMasterBean)request.get(KpiParams.PERIOD_MASTER_DATA.name());
        final SortList sortList = request.getSortList();
        if (periodMasterBean == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_PRD_MASTER_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final List<PeriodMasterBean> periodMasterList = this.kpiManager.searchPeriodMaster(periodMasterBean, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)periodMasterList);
            final Response response = this.OK(KpiParams.PERIOD_MASTER_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - searchPeriodMaster() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_PRD_MASTER_UNABLE_TO_SEARCH_008.name(), "Failed to get period master ", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getKpiKpiRelationships(final Request request) {
        final KpiKpiGraphRelationshipBean graphBean = (KpiKpiGraphRelationshipBean)request.get(KpiParams.KPI_REL_DATA.name());
        if (graphBean == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        List<KpiKpiGraphRelationshipBean> graphBeanList = new ArrayList<KpiKpiGraphRelationshipBean>();
        try {
            graphBeanList = this.kpiManager.getKpiKpiRelationships(graphBean);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)graphBeanList);
            return this.OK(KpiParams.KPI_KPI_REL_LIST.name(), (BaseValueObject)bList);
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getKpiKpiRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_KPI_KPI_REL_042.name(), "Failed to load kpi-kpi relationships", (Throwable)e));
        }
    }
    
    public Response getChildren(final Request request) {
        final Identifier kpiId = (Identifier)request.get(KpiParams.KPI_ID.name());
        if (kpiId == null || kpiId.getId() == null || kpiId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        try {
            final List<KpiIdentityBean> identityList = this.kpiManager.getChildren(kpiId.getId());
            final List<Integer> idList = this.kpiManager.getIdListFromIdObjectList(identityList);
            final IdentifierList idsList = new IdentifierList((List)idList);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)identityList);
            final Response response = this.OK();
            response.put(KpiParams.KPI_ID_LIST.name(), (BaseValueObject)idsList);
            response.put(KpiParams.KPI_IDENTITY_LIST.name(), (BaseValueObject)bList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getChildren() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_CHILDS_015.name(), "Failed to get Children", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getChildrenByType(final Request request) {
        final Identifier kpiId = (Identifier)request.get(KpiParams.KPI_ID.name());
        if (kpiId == null || kpiId.getId() == null || kpiId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "ID data not found"));
        }
        final StringIdentifier relTypeName = (StringIdentifier)request.get(KpiParams.KPI_REL_TYPE_NAME.name());
        if (relTypeName == null || relTypeName.getId() == null || relTypeName.getId() == "") {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Relation name data not found"));
        }
        try {
            final List<KpiIdentityBean> identityList = this.kpiManager.getChildrenByType(kpiId.getId(), relTypeName.getId());
            final List<Integer> idList = this.kpiManager.getIdListFromIdObjectList(identityList);
            final IdentifierList idsList = new IdentifierList((List)idList);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)identityList);
            final Response response = this.OK();
            response.put(KpiParams.KPI_ID_LIST.name(), (BaseValueObject)idsList);
            response.put(KpiParams.KPI_IDENTITY_LIST.name(), (BaseValueObject)bList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getChildrenByType() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_CHILDS_BY_TYPE_016.name(), "Failed to load the children", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getParents(final Request request) {
        final Identifier kpiId = (Identifier)request.get(KpiParams.KPI_ID.name());
        if (kpiId == null || kpiId.getId() == null || kpiId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        try {
            final List<KpiIdentityBean> identityList = this.kpiManager.getParents(kpiId.getId());
            final List<Integer> idList = this.kpiManager.getIdListFromIdObjectList(identityList);
            final IdentifierList idsList = new IdentifierList((List)idList);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)identityList);
            final Response response = this.OK();
            response.put(KpiParams.KPI_ID_LIST.name(), (BaseValueObject)idsList);
            response.put(KpiParams.KPI_IDENTITY_LIST.name(), (BaseValueObject)bList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getParents() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_PARENTS_017.name(), "failed to load Parents", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getParentsByType(final Request request) {
        final Identifier kpiId = (Identifier)request.get(KpiParams.KPI_ID.name());
        final StringIdentifier relTypeName = (StringIdentifier)request.get(KpiParams.KPI_REL_TYPE_NAME.name());
        if (kpiId == null || kpiId.getId() == null || kpiId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "ID data not found"));
        }
        if (relTypeName == null || relTypeName.getId() == null || relTypeName.getId() == "") {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Relation name data not found"));
        }
        try {
            final List<KpiIdentityBean> identityList = this.kpiManager.getParentsByType(kpiId.getId(), relTypeName.getId());
            final List<Integer> idList = this.kpiManager.getIdListFromIdObjectList(identityList);
            final IdentifierList idsList = new IdentifierList((List)idList);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)identityList);
            final Response response = this.OK();
            response.put(KpiParams.KPI_ID_LIST.name(), (BaseValueObject)idsList);
            response.put(KpiParams.KPI_IDENTITY_LIST.name(), (BaseValueObject)bList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getParentsByType() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_PARENTS_BY_TYPE_018.name(), "Failed to load Parents By Type", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getAscendants(final Request request) {
        final Identifier kpiId = (Identifier)request.get(KpiParams.KPI_ID.name());
        if (kpiId == null || kpiId.getId() == null || kpiId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        Identifier depth = (Identifier)request.get(KpiParams.DEPTH.name());
        if (depth == null || depth.getId() == null || depth.getId() <= 0) {
            depth = new Identifier(-1);
        }
        try {
            final List<KpiIdentityBean> identityList = this.kpiManager.getAscendants(kpiId.getId(), depth.getId());
            final List<Integer> idList = this.kpiManager.getIdListFromIdObjectList(identityList);
            final IdentifierList idsList = new IdentifierList((List)idList);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)identityList);
            final Response response = this.OK();
            response.put(KpiParams.KPI_ID_LIST.name(), (BaseValueObject)idsList);
            response.put(KpiParams.KPI_IDENTITY_LIST.name(), (BaseValueObject)bList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getAscendants() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_ASCENDANTS_021.name(), "Failed to load Ascendants Kpi", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getAscendantsByType(final Request request) {
        final Identifier kpiId = (Identifier)request.get(KpiParams.KPI_ID.name());
        Identifier depth = (Identifier)request.get(KpiParams.DEPTH.name());
        final StringIdentifier relTypeName = (StringIdentifier)request.get(KpiParams.KPI_REL_TYPE_NAME.name());
        if (kpiId == null || kpiId.getId() == null || kpiId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "ID data not found"));
        }
        if (relTypeName == null || relTypeName.getId() == null || relTypeName.getId() == "") {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Relation name data not found"));
        }
        if (depth == null || depth.getId() == null || depth.getId() <= 0) {
            depth = new Identifier(-1);
        }
        try {
            final List<KpiIdentityBean> identityList = this.kpiManager.getAscendantsByType(kpiId.getId(), relTypeName.getId(), depth.getId());
            final List<Integer> idList = this.kpiManager.getIdListFromIdObjectList(identityList);
            final IdentifierList idsList = new IdentifierList((List)idList);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)identityList);
            final Response response = this.OK();
            response.put(KpiParams.KPI_ID_LIST.name(), (BaseValueObject)idsList);
            response.put(KpiParams.KPI_IDENTITY_LIST.name(), (BaseValueObject)bList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getAscendantsByType() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_ASCENDANTS_BY_TYPE_022.name(), "Failed to load Ascendants Kpi", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getAscendantsByIdList(final Request request) {
        final IdentifierList kpiIdList = (IdentifierList)request.get(KpiParams.KPI_ID_LIST.name());
        if (kpiIdList == null || kpiIdList.getIdsList() == null || kpiIdList.getIdsList().size() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        final List<Integer> inputList = (List<Integer>)kpiIdList.getIdsList();
        Identifier depth = (Identifier)request.get(KpiParams.DEPTH.name());
        if (depth == null || depth.getId() == null || depth.getId() <= 0) {
            depth = new Identifier(-1);
        }
        try {
            final Set<KpiIdentityBean> identitySet = new HashSet<KpiIdentityBean>();
            for (final Integer id : inputList) {
                identitySet.addAll(this.kpiManager.getAscendants(id, depth.getId()));
            }
            final List<Integer> idList = this.kpiManager.getIdListFromIdObjectList(identitySet);
            final IdentifierList idsList = new IdentifierList((List)idList);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)this.kpiManager.getListFromSet(identitySet));
            final Response response = this.OK();
            response.put(KpiParams.KPI_ID_LIST.name(), (BaseValueObject)idsList);
            response.put(KpiParams.KPI_IDENTITY_LIST.name(), (BaseValueObject)bList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getAscendantsByIdList() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_ASCENDANTS_BY_ID_LIST_023.name(), "Failed to load Ascendants Kpi", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getAscendantsByIdListAndType(final Request request) {
        final IdentifierList kpiIdList = (IdentifierList)request.get(KpiParams.KPI_ID_LIST.name());
        if (kpiIdList == null || kpiIdList.getIdsList() == null || kpiIdList.getIdsList().size() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        Identifier depth = (Identifier)request.get(KpiParams.DEPTH.name());
        if (depth == null || depth.getId() == null || depth.getId() <= 0) {
            depth = new Identifier(-1);
        }
        final StringIdentifier relTypeName = (StringIdentifier)request.get(KpiParams.KPI_REL_TYPE_NAME.name());
        if (relTypeName == null || relTypeName.getId() == null || relTypeName.getId() == "") {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Relation name data not found"));
        }
        try {
            final Set<KpiIdentityBean> identitySet = new HashSet<KpiIdentityBean>();
            final List<Integer> inputList = (List<Integer>)kpiIdList.getIdsList();
            for (final Integer id : inputList) {
                identitySet.addAll(this.kpiManager.getAscendantsByType(id, relTypeName.getId(), depth.getId()));
            }
            final List<Integer> idList = this.kpiManager.getIdListFromIdObjectList(identitySet);
            final IdentifierList idsList = new IdentifierList((List)idList);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)this.kpiManager.getListFromSet(identitySet));
            final Response response = this.OK();
            response.put(KpiParams.KPI_ID_LIST.name(), (BaseValueObject)idsList);
            response.put(KpiParams.KPI_IDENTITY_LIST.name(), (BaseValueObject)bList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getAscendantsByIdListAndType() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_ASCENDANTS_BY_TYPE_ID_LIST_024.name(), "Failed to load Ascendants Kpi", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getAscendantsGraph(final Request request) {
        final Identifier kpiId = (Identifier)request.get(KpiParams.KPI_ID.name());
        if (kpiId == null || kpiId.getId() == null || kpiId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        Identifier depth = (Identifier)request.get(KpiParams.DEPTH.name());
        if (depth == null || depth.getId() == null || depth.getId() <= 0) {
            depth = new Identifier(-1);
        }
        try {
            final Graph graph = this.kpiManager.getAscendantsGraph(kpiId.getId(), depth.getId());
            final GraphResponse response = new GraphResponse();
            response.setGraph(graph);
            return this.OK(KpiParams.KPI_GRAPH.name(), (BaseValueObject)response);
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getAscendantsGraph() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_ASCENDANT_GRAPH_032.name(), "Failed to load Ascendants Graph", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getAscendantsGraphByType(final Request request) {
        final Identifier kpiId = (Identifier)request.get(KpiParams.KPI_ID.name());
        if (kpiId == null || kpiId.getId() == null || kpiId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        final StringIdentifier relTypeName = (StringIdentifier)request.get(KpiParams.KPI_REL_TYPE_NAME.name());
        if (relTypeName == null || relTypeName.getId() == null || relTypeName.getId() == "") {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Relation name data not found"));
        }
        Identifier depth = (Identifier)request.get(KpiParams.DEPTH.name());
        if (depth == null || depth.getId() == null || depth.getId() <= 0) {
            depth = new Identifier(-1);
        }
        try {
            final Graph graph = this.kpiManager.getAscendantsGraphByType(kpiId.getId(), relTypeName.getId(), depth.getId());
            final GraphResponse response = new GraphResponse();
            response.setGraph(graph);
            return this.OK(KpiParams.KPI_GRAPH.name(), (BaseValueObject)response);
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getAscendantsGraphByType() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_ASCENDANT_GRAPH_BY_TYPE_033.name(), "Failed to load Ascendants Graph", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getAscendantsGraphByIdList(final Request request) {
        final IdentifierList kpiIdList = (IdentifierList)request.get(KpiParams.KPI_ID_LIST.name());
        if (kpiIdList == null || kpiIdList.getIdsList() == null || kpiIdList.getIdsList().size() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        Identifier depth = (Identifier)request.get(KpiParams.DEPTH.name());
        if (depth == null || depth.getId() == null || depth.getId() <= 0) {
            depth = new Identifier(-1);
        }
        try {
            final List<Integer> idList = (List<Integer>)kpiIdList.getIdsList();
            final Graph graph = this.kpiManager.getAscendantsGraphByIdList(idList, depth.getId());
            final GraphResponse response = new GraphResponse();
            response.setGraph(graph);
            return this.OK(KpiParams.KPI_GRAPH.name(), (BaseValueObject)response);
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getAscendantsGraphByIdList() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_ASCENDANT_GRAPH_BY_ID_LIST_034.name(), "Failed to load Ascendants Graph", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getAscendantsGraphByIdListAndType(final Request request) {
        final IdentifierList kpiIdList = (IdentifierList)request.get(KpiParams.KPI_ID_LIST.name());
        if (kpiIdList == null || kpiIdList.getIdsList() == null || kpiIdList.getIdsList().size() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        final StringIdentifier relTypeName = (StringIdentifier)request.get(KpiParams.KPI_REL_TYPE_NAME.name());
        if (relTypeName == null || relTypeName.getId() == null || relTypeName.getId() == "") {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Relation name data not found"));
        }
        Identifier depth = (Identifier)request.get(KpiParams.DEPTH.name());
        if (depth == null || depth.getId() == null || depth.getId() <= 0) {
            depth = new Identifier(-1);
        }
        try {
            final List<Integer> idList = (List<Integer>)kpiIdList.getIdsList();
            final Graph graph = this.kpiManager.getAscendantsGraphByIdListAndType(idList, relTypeName.getId(), depth.getId());
            final GraphResponse response = new GraphResponse();
            response.setGraph(graph);
            return this.OK(KpiParams.KPI_GRAPH.name(), (BaseValueObject)response);
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getAscendantsGraphByIdListAndType() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_ASCENDANT_GRAPH_BY_TYPE_ID_LIST_035.name(), "Failed to load Ascendants Graph", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getDescendants(final Request request) {
        final Identifier kpiId = (Identifier)request.get(KpiParams.KPI_ID.name());
        if (kpiId == null || kpiId.getId() == null || kpiId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        Identifier depth = (Identifier)request.get(KpiParams.DEPTH.name());
        if (depth == null || depth.getId() == null || depth.getId() <= 0) {
            depth = new Identifier(-1);
        }
        try {
            final List<KpiIdentityBean> identityList = this.kpiManager.getDescendants(kpiId.getId(), depth.getId());
            final List<Integer> idList = this.kpiManager.getIdListFromIdObjectList(identityList);
            final IdentifierList idsList = new IdentifierList((List)idList);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)identityList);
            final Response response = this.OK();
            response.put(KpiParams.KPI_ID_LIST.name(), (BaseValueObject)idsList);
            response.put(KpiParams.KPI_IDENTITY_LIST.name(), (BaseValueObject)bList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getDescendants() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_DESCENDANTS_028.name(), "Failed to load Descendants Kpi", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getDescendantsByType(final Request request) {
        final Identifier kpiId = (Identifier)request.get(KpiParams.KPI_ID.name());
        if (kpiId == null || kpiId.getId() == null || kpiId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "ID data not found"));
        }
        final StringIdentifier relTypeName = (StringIdentifier)request.get(KpiParams.KPI_REL_TYPE_NAME.name());
        if (relTypeName == null || relTypeName.getId() == null || relTypeName.getId() == "") {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Relation name data not found"));
        }
        Identifier depth = (Identifier)request.get(KpiParams.DEPTH.name());
        if (depth == null || depth.getId() == null || depth.getId() <= 0) {
            depth = new Identifier(-1);
        }
        try {
            final List<KpiIdentityBean> identityList = this.kpiManager.getDescendantsByType(kpiId.getId(), relTypeName.getId(), depth.getId());
            final List<Integer> idList = this.kpiManager.getIdListFromIdObjectList(identityList);
            final IdentifierList idsList = new IdentifierList((List)idList);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)identityList);
            final Response response = this.OK();
            response.put(KpiParams.KPI_ID_LIST.name(), (BaseValueObject)idsList);
            response.put(KpiParams.KPI_IDENTITY_LIST.name(), (BaseValueObject)bList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getDescendantsByType() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_DESCENDANTS_BY_TYPE_029.name(), "Failed to load Descendants Kpi", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getDescendantsByIdList(final Request request) {
        final IdentifierList kpiIdList = (IdentifierList)request.get(KpiParams.KPI_ID_LIST.name());
        if (kpiIdList == null || kpiIdList.getIdsList() == null || kpiIdList.getIdsList().size() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        final List<Integer> inputList = (List<Integer>)kpiIdList.getIdsList();
        Identifier depth = (Identifier)request.get(KpiParams.DEPTH.name());
        if (depth == null || depth.getId() == null || depth.getId() <= 0) {
            depth = new Identifier(-1);
        }
        try {
            final Set<KpiIdentityBean> identitySet = new HashSet<KpiIdentityBean>();
            for (final Integer id : inputList) {
                identitySet.addAll(this.kpiManager.getDescendants(id, depth.getId()));
            }
            final List<Integer> idList = this.kpiManager.getIdListFromIdObjectList(identitySet);
            final IdentifierList idsList = new IdentifierList((List)idList);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)this.kpiManager.getListFromSet(identitySet));
            final Response response = this.OK();
            response.put(KpiParams.KPI_ID_LIST.name(), (BaseValueObject)idsList);
            response.put(KpiParams.KPI_IDENTITY_LIST.name(), (BaseValueObject)bList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getDescendantsByIdList() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_DESCENDANTS_BY_ID_LIST_030.name(), "Failed to load Descendants Kpi", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getDescendantsByIdListAndType(final Request request) {
        final IdentifierList kpiIdList = (IdentifierList)request.get(KpiParams.KPI_ID_LIST.name());
        if (kpiIdList == null || kpiIdList.getIdsList() == null || kpiIdList.getIdsList().size() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        final List<Integer> inputList = (List<Integer>)kpiIdList.getIdsList();
        Identifier depth = (Identifier)request.get(KpiParams.DEPTH.name());
        if (depth == null || depth.getId() == null || depth.getId() <= 0) {
            depth = new Identifier(-1);
        }
        final StringIdentifier relTypeName = (StringIdentifier)request.get(KpiParams.KPI_REL_TYPE_NAME.name());
        if (relTypeName == null || relTypeName.getId() == null || relTypeName.getId() == "") {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Relation name data not found"));
        }
        try {
            final Set<KpiIdentityBean> identitySet = new HashSet<KpiIdentityBean>();
            for (final Integer id : inputList) {
                identitySet.addAll(this.kpiManager.getDescendantsByType(id, relTypeName.getId(), depth.getId()));
            }
            final List<Integer> idList = this.kpiManager.getIdListFromIdObjectList(identitySet);
            final IdentifierList resList = new IdentifierList((List)idList);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)this.kpiManager.getListFromSet(identitySet));
            final Response response = this.OK();
            response.put(KpiParams.KPI_ID_LIST.name(), (BaseValueObject)resList);
            response.put(KpiParams.KPI_IDENTITY_LIST.name(), (BaseValueObject)bList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getDescendantsByIdListAndType() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_DESCENDANTS_BY_TYPE_ID_LIST_031.name(), "Failed to load Descendants Kpi", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getDescendantsGraph(final Request request) {
        final Identifier kpiId = (Identifier)request.get(KpiParams.KPI_ID.name());
        if (kpiId == null || kpiId.getId() == null || kpiId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        Identifier depth = (Identifier)request.get(KpiParams.DEPTH.name());
        if (depth == null || depth.getId() == null || depth.getId() <= 0) {
            depth = new Identifier(-1);
        }
        try {
            final Graph graph = this.kpiManager.getDescendantsGraph(kpiId.getId(), depth.getId());
            final GraphResponse response = new GraphResponse();
            response.setGraph(graph);
            return this.OK(KpiParams.KPI_GRAPH.name(), (BaseValueObject)response);
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getDescendantsGraph() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_DESCENDANT_GRAPH_036.name(), "Failed to Load DescendantsGraph", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getDescendantsGraphByType(final Request request) {
        final Identifier kpiId = (Identifier)request.get(KpiParams.KPI_ID.name());
        if (kpiId == null || kpiId.getId() == null || kpiId.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        Identifier depth = (Identifier)request.get(KpiParams.DEPTH.name());
        if (depth == null || depth.getId() == null || depth.getId() <= 0) {
            depth = new Identifier(-1);
        }
        final StringIdentifier relTypeName = (StringIdentifier)request.get(KpiParams.KPI_REL_TYPE_NAME.name());
        if (relTypeName == null || relTypeName.getId() == null || relTypeName.getId() == "") {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Relationship type data not found"));
        }
        try {
            final Graph graph = this.kpiManager.getDescendantsGraphByType(kpiId.getId(), relTypeName.getId(), depth.getId());
            final GraphResponse response = new GraphResponse();
            response.setGraph(graph);
            return this.OK(KpiParams.KPI_GRAPH.name(), (BaseValueObject)response);
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getDescendantsGraphByType() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_DESCENDANT_GRAPH_BY_TYPE_037.name(), "Failed to Load DescendantsGraph", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getDescendantsGraphByIdList(final Request request) {
        final IdentifierList kpiIdList = (IdentifierList)request.get(KpiParams.KPI_ID_LIST.name());
        if (kpiIdList == null || kpiIdList.getIdsList() == null || kpiIdList.getIdsList().size() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        Identifier depth = (Identifier)request.get(KpiParams.DEPTH.name());
        if (depth == null || depth.getId() == null || depth.getId() <= 0) {
            depth = new Identifier(-1);
        }
        try {
            final List<Integer> idList = (List<Integer>)kpiIdList.getIdsList();
            final Graph graph = this.kpiManager.getDescendantsGraphByIdList(idList, depth.getId());
            final GraphResponse response = new GraphResponse();
            response.setGraph(graph);
            return this.OK(KpiParams.KPI_GRAPH.name(), (BaseValueObject)response);
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getDescendantsGraphByIdList() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_DESCENDANT_GRAPH_BY_ID_LIST_038.name(), "Failed to Load DescendantsGraph", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getDescendantsGraphByIdListAndType(final Request request) {
        final IdentifierList kpiIdList = (IdentifierList)request.get(KpiParams.KPI_ID_LIST.name());
        if (kpiIdList == null || kpiIdList.getIdsList() == null || kpiIdList.getIdsList().size() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        Identifier depth = (Identifier)request.get(KpiParams.DEPTH.name());
        if (depth == null || depth.getId() == null || depth.getId() <= 0) {
            depth = new Identifier(-1);
        }
        final StringIdentifier relTypeName = (StringIdentifier)request.get(KpiParams.KPI_REL_TYPE_NAME.name());
        if (relTypeName == null || relTypeName.getId() == null || relTypeName.getId() == "") {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Relationship type data not found"));
        }
        try {
            final List<Integer> idList = (List<Integer>)kpiIdList.getIdsList();
            final Graph graph = this.kpiManager.getDescendantsGraphByIdListAndType(idList, relTypeName.getId(), depth.getId());
            final GraphResponse response = new GraphResponse();
            response.setGraph(graph);
            return this.OK(KpiParams.KPI_GRAPH.name(), (BaseValueObject)response);
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getDescendantsGraphByIdListAndType() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_DESCENDANT_GRAPH_BY_TYPE_ID_LIST_039.name(), "Failed to Load DescendantsGraph", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getKpisByIds(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(KpiParams.KPI_ID_LIST.name());
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().size() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final BaseValueObjectList bList = this.getKpiManager().getKpisByIds(idList.getIdsList());
            if (bList != null && !bList.getValueObjectList().isEmpty()) {
                final List<KpiData> data = this.periodUtil.updateCurrentScoreDetails((List<KpiData>) bList.getValueObjectList());
                bList.setValueObjectList((List)data);
            }
            final Response response = this.OK(KpiParams.KPI_DATA_LIST.name(), (BaseValueObject)bList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getKpisByIds() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_BY_ID_LIST_027.name(), "Failed to load kpis", new Object[] { e.getMessage() }));
        }
    }
    
    public Response updateKPIState(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(KpiParams.KPI_ID_LIST.name());
        final Identifier state = (Identifier)request.get(KpiParams.KPI_STATE.name());
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().isEmpty() || state == null || state.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final int count = this.getKpiManager().updateKPIState(idList, state);
            return this.OK(KpiParams.COUNT.name(), (BaseValueObject)new Identifier(count));
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl -  : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_UPDATE_STATUS_026.name(), "Unknown error while updating kpi status by ids", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getKpiDataToCalcScore(final Request request) {
        final Identifier groupId = (Identifier)request.get(KpiParams.GROUP_ID.name());
        final Identifier periodMasterId = (Identifier)request.get(KpiParams.PERIOD_MASTER_ID.name());
        final DateResponse startDate = (DateResponse)request.get(KpiParams.START_DATE.name());
        if (startDate == null || startDate.getDate() == null || groupId == null || groupId.getId() == null || groupId.getId() < 0 || periodMasterId == null || periodMasterId.getId() == null || periodMasterId.getId() < 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final BaseValueObjectList list = this.kpiManager.getKpiDataToCalcScore(groupId, periodMasterId, startDate);
            return this.OK(KpiParams.SCORE_UTILITY_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getKpiDataToCalcScore() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_TO_CALC_SCORE_025.name(), "Failed to get KpiData To Calculate Score ", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response searchKpiAggregationType(final Request request) {
        final KpiAggregationTypeBean kpiAggregationType = (KpiAggregationTypeBean)request.get(KpiParams.KPI_AGGR_TYPE_DATA.name());
        final SortList sortList = request.getSortList();
        if (kpiAggregationType == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_AGGR_TYPE_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final List<KpiAggregationTypeBean> kpiAggregationTypeList = this.kpiManager.searchKpiAggregationType(kpiAggregationType, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiAggregationTypeList);
            final Response response = this.OK(KpiParams.KPI_AGGR_TYPE_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - searchKpiAggregationType() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_AGGR_TYPE_UNABLE_TO_SEARCH_008.name(), "Failed to search kpi AggregationType", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response searchKpiUom(final Request request) {
        final KpiUomData kpiUom = (KpiUomData)request.get(KpiParams.KPI_UOM_DATA.name());
        final SortList sortList = request.getSortList();
        if (kpiUom == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UOM_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final List<KpiUomData> kpiUomList = this.kpiManager.searchKpiUom(kpiUom, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)kpiUomList);
            final Response response = this.OK(KpiParams.KPI_UOM_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - searchKpiUom() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UOM_UNABLE_TO_SEARCH_008.name(), "Failed to search kpi uom", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response searchKpiType(final Request request) {
        final KpiTypeData kpiTypeData = (KpiTypeData)request.get(KpiParams.KPI_TYPE_DATA.name());
        final SortList sortList = request.getSortList();
        if (kpiTypeData == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TYPE_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final List<KpiTypeData> KpiTypeDataList = this.kpiManager.searchKpiType(kpiTypeData, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)KpiTypeDataList);
            final Response response = this.OK(KpiParams.KPI_TYPE_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - searchKpiType() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_TYPE_UNABLE_TO_SEARCH_008.name(), "Failed to search kpi Type", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response getAllKpiScoreByKpiIds(final Request request) {
        final IdentifierList kpiIdList = (IdentifierList)request.get(KpiParams.KPI_ID_LIST.name());
        if (kpiIdList == null || kpiIdList.getIdsList() == null || kpiIdList.getIdsList().size() <= 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCORE_INVALID_INPUT_002.name(), "Invalid data in the request"));
        }
        try {
            final BaseValueObjectList valueObjectList = this.kpiManager.getAllKpiScoreByKpiIds(kpiIdList.getIdsList());
            final Response response = this.OK(KpiParams.KPI_SCORE_DATA_LIST.name(), (BaseValueObject)valueObjectList);
            return response;
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getAllKpiScoreByKpiIds() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCORE_UNABLE_TO_GET_ALL_BY_ID_LIST_010.name(), "Failed to load kpi scores", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getKpisByScorecardId(final Request request) {
        final Identifier scorecardId = (Identifier)request.get(KpiParams.SCORECARD_ID.name());
        if (scorecardId == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "Data not found"));
        }
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        try {
            List<KpiData> scList = this.kpiManager.getKpisByScorecardId(scorecardId, page, sortList);
            scList = this.periodUtil.updateCurrentScoreDetails(scList);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)scList);
            final Response response = this.OK(KpiParams.KPI_DATA_LIST.name(), (BaseValueObject)bList);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getKpisByScorecardId() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_BY_SCORECARD_ID_040.name(), "Failed to load Kpis By scorecardId", (Throwable)e));
        }
    }
    
    @Transactional
    public Response getKpiDataToCalcScorecardScore(final Request request) {
        final Identifier scoreCardId = (Identifier)request.get(KpiParams.SCORECARD_ID.name());
        final Identifier periodMasterId = (Identifier)request.get(KpiParams.PERIOD_MASTER_ID.name());
        final DateResponse startDate = (DateResponse)request.get(KpiParams.START_DATE.name());
        if (startDate == null || startDate.getDate() == null || scoreCardId == null || scoreCardId.getId() == null || scoreCardId.getId() < 0 || periodMasterId == null || periodMasterId.getId() == null || periodMasterId.getId() < 0) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final BaseValueObjectList list = this.kpiManager.getKpiDataToCalcScorecardScore(scoreCardId, periodMasterId, startDate);
            return this.OK(KpiParams.SCORE_UTILITY_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getKpiDataToCalcScorecardScore() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_UNABLE_TO_GET_TO_CALC_SCORECARD_SCORE_041.name(), "Failed to load KpiData To Calculate ScorecardScore ", (Throwable)ex));
        }
    }
    
    public Response getPeriodMasterById(final Request request) {
        final Identifier identifier = (Identifier)request.get(KpiParams.PERIOD_MASTER_ID.name());
        if (identifier == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_PRD_MASTER_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            final PeriodMasterBean periodMaster = this.getKpiManager().getPeriodMasterById(identifier.getId());
            return this.OK(KpiParams.PERIOD_MASTER_DATA.name(), (BaseValueObject)periodMaster);
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getPeriodMasterById() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_PRD_MASTER_UNABLE_TO_GET_004.name(), "Failed to get Period master", (Throwable)ex));
        }
    }
    
    private boolean checkScoreChange(final KpiScoreBean data, final Identifier periodId, final List<KpiScoreBean> kpiScore, final boolean isScoreAggTypeSum, final boolean isInitiative) {
        final Iterator<KpiScoreBean> it = kpiScore.iterator();
        while (it.hasNext()) {
            final KpiScoreBean existing = it.next();
            if (existing.getPeriodMasterId() != null && existing.getPeriodMasterId() == (int)periodId.getId()) {
                boolean isSame = false;
                if (isInitiative) {
                    final Date existDate = existing.getDateScore();
                    final Date newDate = data.getDateScore();
                    isSame = this.isSameDate(existDate, newDate);
                    KpiManagerServiceImpl.log.debug((Object)("SCORELOG period id = " + periodId + " existing = " + existDate + ", new = " + newDate));
                }
                else {
                    final Double existingScore = isScoreAggTypeSum ? existing.getNumScore_mtd() : existing.getNumScore();
                    final Double newScore = isScoreAggTypeSum ? data.getNumScore_mtd() : data.getNumScore();
                    KpiManagerServiceImpl.log.debug((Object)("SCORELOG period id = " + periodId + " existing = " + existingScore + ", new = " + newScore));
                    isSame = this.isSame(existingScore, newScore);
                }
                if (isSame) {
                    data.setId(existing.getId());
                    data.setPeriodMasterId(existing.getPeriodMasterId());
                    data.setAchievement(existing.getAchievement());
                    data.setDateScore(existing.getDateScore());
                    data.setDateTarget(existing.getDateTarget());
                    data.setEndDate(existing.getEndDate());
                    data.setNumScore(existing.getNumScore());
                    data.setNumScore_mtd(existing.getNumScore_mtd());
                    data.setNumTarget(existing.getNumTarget());
                    data.setNumTarget_mtd(existing.getNumTarget_mtd());
                    data.setRatingLevel(existing.getRatingLevel());
                    data.setStartDate(existing.getStartDate());
                    final Integer id = this.getKpiManager().saveKpiScore(data);
                    data.setId(id);
                    it.remove();
                    return false;
                }
                return true;
            }
        }
        return true;
    }
    
    private boolean isSame(final Double d1, final Double d2) {
        boolean result = false;
        if (d1 == null && d2 != null) {
            return result;
        }
        final double dd1 = (d1 == null) ? 0.0 : d1;
        final double dd2 = (d2 == null) ? 0.0 : d2;
        result = DoubleMath.fuzzyEquals(dd1, dd2, 1.0E-5);
        return result;
    }
    
    private boolean isSameDate(final Date d1, final Date d2) {
        boolean result = false;
        if (d1 == null && d2 != null) {
            return result;
        }
        if (d2 == null) {
            result = true;
        }
        result = d1.equals(d2);
        return result;
    }
    
    public Response getCurrentPeriodScore(final Request request) {
        final BaseValueObjectList list = (BaseValueObjectList)request.get(KpiParams.KPI_SCORE_LIST.name());
        final DateResponse dateResponse = (DateResponse)request.get(KpiParams.DATE.name());
        if (list == null || list.getValueObjectList() == null || list.getValueObjectList().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCORE_INVALID_INPUT_002.name(), "No data in the request"));
        }
        final List<KpiScoreBean> scoreList = (List<KpiScoreBean>)list.getValueObjectList();
        try {
            KpiScoreBean kpiScoreBean = null;
            if (dateResponse != null && dateResponse.getDate() != null) {
                final Date systemDate = dateResponse.getDate();
                kpiScoreBean = this.kpiManager.getCurrentPeriodScore(scoreList, systemDate);
            }
            else {
                kpiScoreBean = this.kpiManager.getCurrentPeriodScore(scoreList, new Date());
            }
            return this.OK(KpiParams.KPI_SCORE_DATA.name(), (BaseValueObject)kpiScoreBean);
        }
        catch (Exception e) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - getCurrentPeriodScore() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_SCORE_UNABLE_TO_GET_004.name(), "Failed to get KpiScore", (Throwable)e));
        }
    }
    
    public Response refreshPerformanceDataCache(final Request request) {
        try {
            this.kpiManager.loadToCache();
            return this.OK();
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - refreshPerformanceDataCache() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CACHE_REFRESH.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response refreshKpiCacheandGraph(final Request request) {
        try {
            this.kpiManager.loadKpiToCache();
            this.kpiManager.reloadKpiKpiRelationshipGraph();
            return this.OK();
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiManagerServiceImpl - refreshKpiCacheandGraph() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CACHE_REFRESH.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response updateKpiGraph(final Request request) {
        final BaseValueObjectList bList = (BaseValueObjectList)request.get(KpiParams.KPI_KPI_REL_LIST.name());
        try {
            final List<KpiKpiGraphRelationshipBean> graphList = (List<KpiKpiGraphRelationshipBean>)bList.getValueObjectList();
            final boolean status = this.kpiManager.updateKpiGraph(graphList);
            return this.OK(KpiParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiTargetManagerServiceImpl - getEntityVolumeSummary() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CACHE_REFRESH.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response reloadKpiKpiReationshipGraph(final Request request) {
        try {
            this.kpiManager.reloadKpiKpiRelationshipGraph();
            return this.OK(KpiParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception ex) {
            KpiManagerServiceImpl.log.error((Object)"Exception in KpiTargetManagerServiceImpl - getEntityVolumeSummary() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CACHE_REFRESH.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    static {
        log = Logger.getLogger((Class)KpiManagerServiceImpl.class);
    }
}
