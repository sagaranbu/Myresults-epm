package com.kpisoft.org.impl;

import com.kpisoft.org.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.org.vo.*;
import com.kpisoft.org.params.*;
import com.canopus.mw.*;
import com.kpisoft.org.domain.*;
import com.canopus.mw.dto.*;
import java.util.*;
import org.slf4j.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ OrganizationStructureManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class OrganizationStructureManagerServiceImpl extends BaseMiddlewareBean implements OrganizationStructureManagerService
{
    @Autowired
    private OrganizationStructureManager orgStructureManager;
    private static final Logger logger;
    
    public OrganizationStructureManagerServiceImpl() {
        this.orgStructureManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response createOrganizationDimensionStructure(final Request request) {
        final OrganizationDimensionStructureData orgTypeData = (OrganizationDimensionStructureData)request.get(OrgDimensionStructureParams.ORG_DIM_STR_DATA.name());
        if (orgTypeData == null) {
            return this.ERROR((Exception)new MiddlewareException("STR-201", "No record found in request object"));
        }
        try {
            final OrganizationStructure orgStructure = this.orgStructureManager.createOrUpdateOrgStructure(orgTypeData);
            return this.OK(OrgDimensionStructureParams.ORG_DIM_STR_ID.name(), (BaseValueObject)new Identifier(orgStructure.getStructureData().getId()));
        }
        catch (Exception e) {
            OrganizationStructureManagerServiceImpl.logger.error("Exception in OrganizationStructureManagerServiceImpl - createOrganizationDimensionStructure() : " + e);
            return this.ERROR((Exception)new MiddlewareException("STR-201", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getOrganizationDimensionStructure(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrgDimensionStructureParams.ORG_DIM_STR_ID.name());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("STR-201", "Dimension Structure id is not found"));
        }
        try {
            final OrganizationStructure orgStructure = this.orgStructureManager.getOrganizationDimensionStructure(identifier.getId());
            return this.OK(OrgDimensionStructureParams.ORG_DIM_STR_DATA.name(), (BaseValueObject)orgStructure.getStructureData());
        }
        catch (Exception e) {
            OrganizationStructureManagerServiceImpl.logger.error("Exception in OrganizationStructureManagerServiceImpl - getOrganizationDimensionStructure() : " + e);
            return this.ERROR((Exception)new MiddlewareException("STR-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response updateOrganizationDimensionStructure(final Request request) {
        final OrganizationDimensionStructureData orgTypeData = (OrganizationDimensionStructureData)request.get(OrgDimensionStructureParams.ORG_DIM_STR_DATA.name());
        if (orgTypeData == null) {
            return this.ERROR((Exception)new MiddlewareException("STR-201", "No record found in request object"));
        }
        try {
            final OrganizationStructure orgStructure = this.orgStructureManager.createOrUpdateOrgStructure(orgTypeData);
            return this.OK(OrgDimensionStructureParams.ORG_DIM_STR_DATA.name(), (BaseValueObject)orgStructure.getStructureData());
        }
        catch (Exception e) {
            OrganizationStructureManagerServiceImpl.logger.error("Exception in OrganizationStructureManagerServiceImpl - updateOrganizationDimensionStructure() : " + e);
            return this.ERROR((Exception)new MiddlewareException("STR-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response removeOrganizationDimensionStructure(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrgDimensionStructureParams.ORG_DIM_STR_ID.name());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("STR-201", "Dimension Structure id is not found"));
        }
        try {
            final boolean status = this.orgStructureManager.removeOrganizationDimensionStructure(identifier.getId());
            return this.OK(OrgDimensionStructureParams.ORG_STR_STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            OrganizationStructureManagerServiceImpl.logger.error("Exception in OrganizationStructureManagerServiceImpl - removeOrganizationDimensionStructure() : " + e);
            return this.ERROR((Exception)new MiddlewareException("STR-000", e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAllOrganizationStructure(final Request request) {
        try {
            final List<OrganizationDimensionStructureData> orgDimensionStructureDatas = this.orgStructureManager.getAllOrganizationStructure();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)orgDimensionStructureDatas);
            return this.OK(OrgDimensionStructureParams.ORG_STR_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            OrganizationStructureManagerServiceImpl.logger.error("Exception in OrganizationStructureManagerServiceImpl - getAllOrganizationStructure() : " + e);
            return this.ERROR((Exception)new MiddlewareException("STR-000", e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        logger = LoggerFactory.getLogger((Class)OrganizationStructureManagerServiceImpl.class);
    }
}
