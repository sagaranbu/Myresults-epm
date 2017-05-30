package com.kpisoft.org.impl;

import com.kpisoft.org.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.org.params.*;
import com.canopus.mw.*;
import com.kpisoft.org.domain.*;
import com.canopus.mw.dto.*;
import com.kpisoft.org.vo.*;
import java.util.*;
import org.slf4j.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ OrganizationDimensionManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class OrganizationDimensionManagerServiceImpl extends BaseMiddlewareBean implements OrganizationDimensionManagerService
{
    private static final Logger logger;
    @Autowired
    private OrganizationDimensionManager objOrganizationDimensionManager;
    
    public OrganizationDimensionManagerServiceImpl() {
        this.objOrganizationDimensionManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response createOrganizationDimension(final Request request) {
        try {
            final OrganizationDimensionData objOrganizationDimensionData = (OrganizationDimensionData)request.get(OrgDimensionParams.ORG_DIM_DATA.name());
            if (objOrganizationDimensionData == null) {
                throw new MiddlewareException("DIM-101", "No Data found in the request");
            }
            final OrganizationDimension objOrganizationDimension = this.objOrganizationDimensionManager.createOrganizationDimension(objOrganizationDimensionData);
            return this.OK(OrgDimensionParams.ORG_DIM_DATA.name(), (BaseValueObject)objOrganizationDimension.getObjOrganizationDimensionData());
        }
        catch (Exception e) {
            OrganizationDimensionManagerServiceImpl.logger.error("Exception in OrganizationDimensionManagerServiceImpl - createOrganizationDimension() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("DIM-201", "Failed to load dimension", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getOrganizationDimension(final Request request) {
        try {
            final Identifier identifier = (Identifier)request.get(OrgDimensionParams.ORG_DIM_DATA_ID.name());
            if (identifier == null) {
                throw new MiddlewareException("DIM-101", "Dimension Id is required");
            }
            final OrganizationDimension organizationDimension = this.objOrganizationDimensionManager.getOrganizationDimension(identifier.getId());
            return this.OK(OrgDimensionParams.ORG_DIM_DATA.name(), (BaseValueObject)organizationDimension.getObjOrganizationDimensionData());
        }
        catch (Exception e) {
            OrganizationDimensionManagerServiceImpl.logger.error("Exception in OrganizationDimensionManagerServiceImpl - getOrganizationDimension() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("DIM-201", "Failed to load dimension", new Object[] { e.getMessage() }));
        }
    }
    
    public Response updateOrganizationDimension(final Request request) {
        try {
            final OrganizationDimensionData objOrganizationDimensionData = (OrganizationDimensionData)request.get(OrgDimensionParams.ORG_DIM_DATA.name());
            if (objOrganizationDimensionData == null) {
                throw new MiddlewareException("DIM-201", "No Data found in request for update");
            }
            this.validateOrganizationDimension(objOrganizationDimensionData);
            final OrganizationDimension objOrganizationDimension = this.objOrganizationDimensionManager.updateOrganizationDimension(objOrganizationDimensionData);
            return this.OK(OrgDimensionParams.ORG_DIM_DATA.name(), (BaseValueObject)objOrganizationDimension.getObjOrganizationDimensionData());
        }
        catch (Exception e) {
            OrganizationDimensionManagerServiceImpl.logger.error("Exception in OrganizationDimensionManagerServiceImpl - updateOrganizationDimension() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("DIM-201", "Failed to load dimension", new Object[] { e.getMessage() }));
        }
    }
    
    public Response getAllOrganizationDimension(final Request request) {
        try {
            final List<OrganizationDimensionData> alOrganizationDimension = this.objOrganizationDimensionManager.getAllOrganizationDimension();
            final BaseValueObjectList objectList = new BaseValueObjectList();
            objectList.setValueObjectList((List)alOrganizationDimension);
            return this.OK(OrgDimensionParams.ORG_DIM_LIST.name(), (BaseValueObject)objectList);
        }
        catch (Exception e) {
            OrganizationDimensionManagerServiceImpl.logger.error("Exception in OrganizationDimensionManagerServiceImpl - getAllOrganizationDimension() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("DIM-201", "Failed to load dimension", new Object[] { e.getMessage() }));
        }
    }
    
    public Response removeOrganizationDimension(final Request request) {
        try {
            final Identifier identifier = (Identifier)request.get(OrgDimensionParams.ORG_DIM_DATA_ID.name());
            if (identifier.getId() == 0) {
                throw new MiddlewareException("GRD-201", "Dimension id is required in request");
            }
            final boolean bResponse = this.objOrganizationDimensionManager.removeOrganizationDimension(identifier.getId());
            return this.OK(OrgDimensionParams.ORG_DIM_STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(bResponse));
        }
        catch (Exception e) {
            OrganizationDimensionManagerServiceImpl.logger.error("Exception in OrganizationDimensionManagerServiceImpl - removeOrganizationDimension() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("DIM-201", "Failed to load dimension", new Object[] { e.getMessage() }));
        }
    }
    
    private void validateOrganizationDimension(final OrganizationDimensionData objOrganizationDimension) {
        final List<OrganizationDimensionStructureData> alTemp = (List<OrganizationDimensionStructureData>)objOrganizationDimension.getOrganizationStructure();
        if (alTemp != null && alTemp.size() > 0) {
            for (final OrganizationDimensionStructureData objOrganizationStructure : alTemp) {
                if (objOrganizationStructure.getId() < 1) {
                    throw new MiddlewareException("DIM-201", "Structure Id is Mandatory");
                }
            }
        }
    }
    
    public Response searchOrgDimension(final Request request) {
        final OrganizationDimensionData dimData = (OrganizationDimensionData)request.get(OrgDimensionParams.ORG_DIM_DATA.name());
        if (dimData == null) {
            return this.ERROR((Exception)new MiddlewareException("DIM-201", "No data object in the request"));
        }
        try {
            final List<OrganizationDimensionData> result = this.objOrganizationDimensionManager.searchOrgDimension(dimData);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(OrgDimensionParams.ORG_DIM_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            return this.ERROR((Exception)new MiddlewareException("DIM-000", e.getMessage()));
        }
    }
    
    public Response searchOrgStructure(final Request request) {
        final OrganizationDimensionStructureData strData = (OrganizationDimensionStructureData)request.get(OrgDimensionParams.ORG_STR_DATA.name());
        if (strData == null) {
            return this.ERROR((Exception)new MiddlewareException("DIM-201", "No data object in the request"));
        }
        try {
            final List<OrganizationDimensionStructureData> result = this.objOrganizationDimensionManager.searchOrgStructure(strData);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(OrgDimensionParams.ORG_DIM_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            return this.ERROR((Exception)new MiddlewareException("DIM-000", e.getMessage()));
        }
    }
    
    static {
        logger = LoggerFactory.getLogger((Class)OrganizationDimensionManagerServiceImpl.class);
    }
}
