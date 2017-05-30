package com.kpisoft.org.domain;

import org.springframework.stereotype.*;
import com.kpisoft.org.dac.*;
import javax.validation.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.org.params.*;
import com.kpisoft.org.exception.*;
import com.kpisoft.org.vo.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.*;
import java.util.*;
import org.slf4j.*;

@Component
public class OrganizationDimensionManager extends BaseDomainManager implements CacheLoader<Integer, OrganizationDimension>
{
    private static final Logger logger;
    @Autowired
    private OrganizationDimensionDataService objDimensionDataService;
    @Autowired
    private Validator validator;
    @Autowired
    @Qualifier("organizationDimensionCache")
    private Cache<Integer, OrganizationDimension> cache;
    private OrganizationDimension objOrgDimension;
    
    public OrganizationDimensionManager() {
        this.cache = null;
        this.objOrgDimension = null;
    }
    
    public OrganizationDimension createOrganizationDimension(OrganizationDimensionData objOrganizationDimensionData) {
        this.objOrgDimension = new OrganizationDimension(this, this.objDimensionDataService);
        objOrganizationDimensionData = this.objOrgDimension.save(objOrganizationDimensionData);
        this.objOrgDimension.setObjOrganizationDimensionData(objOrganizationDimensionData);
        return this.objOrgDimension;
    }
    
    public OrganizationDimension getOrganizationDimension(final int id) {
        return this.objOrgDimension = (OrganizationDimension)this.cache.get(id, (CacheLoader)this);
    }
    
    public OrganizationDimension load(final Integer key) {
        try {
            final Request request = new Request();
            request.put(OrgDimensionParams.ORG_DIM_DATA_ID.name(), (BaseValueObject)new Identifier(key));
            final Response response = this.objDimensionDataService.getOrganizationDimension(request);
            final OrganizationDimensionData objDimensionData = (OrganizationDimensionData)response.get(OrgDimensionParams.ORG_DIM_DATA.name());
            (this.objOrgDimension = new OrganizationDimension(this, this.objDimensionDataService)).setObjOrganizationDimensionData(objDimensionData);
            return this.objOrgDimension;
        }
        catch (Exception e) {
            OrganizationDimensionManager.logger.error("Exception in OrganizationDimensionManager - load() : " + e.getMessage());
            APIUtility.handleException(e);
            return null;
        }
    }
    
    public OrganizationDimension updateOrganizationDimension(final OrganizationDimensionData objOrganizationDimensionData) {
        this.objOrgDimension = new OrganizationDimension(this, this.objDimensionDataService);
        final OrganizationDimensionData objOrganizationDimensionData2 = this.objOrgDimension.updateOrganizationDimension(objOrganizationDimensionData);
        this.objOrgDimension.setObjOrganizationDimensionData(objOrganizationDimensionData2);
        return this.objOrgDimension;
    }
    
    public boolean removeOrganizationDimension(final int id) {
        final OrganizationDimensionStructureData objOrganizationDimensionStructureData = new OrganizationDimensionStructureData();
        objOrganizationDimensionStructureData.setId(id);
        final Request request = new Request();
        request.put(OrgDimensionParams.ORG_STR_DATA.name(), (BaseValueObject)objOrganizationDimensionStructureData);
        final Response response = this.objDimensionDataService.searchOrgStructure(request);
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get(OrgDimensionParams.ORG_DIM_LIST.name());
        final List<OrganizationDimensionStructureData> alOrganizationDimensionStructureData = (List<OrganizationDimensionStructureData>)objectList.getValueObjectList();
        if (alOrganizationDimensionStructureData.isEmpty()) {
            throw new MiddlewareException("DB_OPERATION_FAILED", "Unable to Delete dimension as it is in used in Organization Structure");
        }
        this.objOrgDimension = new OrganizationDimension(this, this.objDimensionDataService);
        return this.objOrgDimension.removeOrganizationDimension(id);
    }
    
    public List<OrganizationDimensionData> getAllOrganizationDimension() {
        final Response response = this.objDimensionDataService.getAllOrganizationDimension(new Request());
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get(OrgDimensionParams.ORG_DIM_LIST.name());
        final List<OrganizationDimensionData> alOrganizationDimensionData = (List<OrganizationDimensionData>)objectList.getValueObjectList();
        return alOrganizationDimensionData;
    }
    
    public Validator getValidator() {
        return this.validator;
    }
    
    public List<OrganizationDimensionData> searchOrgDimension(final OrganizationDimensionData dimData) {
        final Request request = new Request();
        request.put(OrgDimensionParams.ORG_DIM_DATA.name(), (BaseValueObject)dimData);
        final Response response = this.objDimensionDataService.searchOrgDimension(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(OrgDimensionParams.ORG_DIM_LIST.name());
        final List<OrganizationDimensionData> result = (List<OrganizationDimensionData>)list.getValueObjectList();
        return result;
    }
    
    public List<OrganizationDimensionStructureData> searchOrgStructure(final OrganizationDimensionStructureData strData) {
        final Request request = new Request();
        request.put(OrgDimensionParams.ORG_STR_DATA.name(), (BaseValueObject)strData);
        final Response response = this.objDimensionDataService.searchOrgStructure(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(OrgDimensionParams.ORG_DIM_LIST.name());
        final List<OrganizationDimensionStructureData> result = (List<OrganizationDimensionStructureData>)list.getValueObjectList();
        return result;
    }
    
    static {
        logger = LoggerFactory.getLogger((Class)OrganizationDimensionManager.class);
    }
}
