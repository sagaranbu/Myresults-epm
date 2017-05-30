package com.kpisoft.org.domain;

import org.springframework.stereotype.*;
import com.kpisoft.org.dac.*;
import javax.validation.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.org.vo.*;
import com.canopus.mw.*;
import com.kpisoft.org.params.*;
import java.util.*;
import com.canopus.mw.dto.*;

@Component
public class OrganizationStructureManager extends BaseDomainManager implements CacheLoader<Integer, OrganizationStructure>
{
    @Autowired
    private OrganizationStructureDataService dataService;
    @Autowired
    private Validator validator;
    @Autowired
    @Qualifier("organizationDimensionStructureCache")
    private Cache<Integer, OrganizationStructure> cache;
    
    public OrganizationStructureManager() {
        this.dataService = null;
        this.cache = null;
    }
    
    public OrganizationStructure createOrUpdateOrgStructure(final OrganizationDimensionStructureData objOrganizationDimensionStructureData) {
        final OrganizationStructure orgStructure = new OrganizationStructure(this, this.dataService);
        orgStructure.setStructureData(objOrganizationDimensionStructureData);
        OrganizationDimensionStructureData orgTypeData = null;
        try {
            orgTypeData = orgStructure.save(objOrganizationDimensionStructureData);
        }
        catch (Exception e) {
            throw new MiddlewareException("ERR_SAVE_UPDATE_ORG_STRUCTURE", e.getMessage(), (Throwable)e);
        }
        finally {
            if (orgTypeData != null && orgTypeData.getId() != null && orgTypeData.getId() > 0) {
                this.cache.remove(orgTypeData.getId());
            }
        }
        return orgStructure;
    }
    
    public OrganizationStructure getOrganizationDimensionStructure(final int id) {
        final OrganizationStructure orgStructure = (OrganizationStructure)this.cache.get(id, (CacheLoader)this);
        return orgStructure;
    }
    
    public OrganizationStructure load(final Integer key) {
        final Request request = new Request();
        request.put(OrgDimensionStructureParams.ORG_DIM_STR_ID.name(), (BaseValueObject)new Identifier(key));
        final Response response = this.dataService.getOrganizationDimensionStructure(request);
        final OrganizationDimensionStructureData orgTypeData = (OrganizationDimensionStructureData)response.get(OrgDimensionStructureParams.ORG_DIM_STR_DATA.name());
        final OrganizationStructure orgStructure = new OrganizationStructure(this, this.dataService);
        orgStructure.setStructureData(orgTypeData);
        return orgStructure;
    }
    
    public boolean removeOrganizationDimensionStructure(final int id) {
        final Request request = new Request();
        request.put(OrgDimensionStructureParams.ORG_DIM_STR_ID.name(), (BaseValueObject)new Identifier(id));
        final Response response = this.dataService.removeOrganizationDimensionStructure(request);
        final BooleanResponse status = (BooleanResponse)response.get(OrgDimensionStructureParams.ORG_STR_STATUS_RESPONSE.name());
        if (status.isResponse()) {
            this.cache.remove(id);
        }
        return status.isResponse();
    }
    
    public List<OrganizationDimensionStructureData> getAllOrganizationStructure() {
        final Response response = this.dataService.getAllOrganizationStructure(new Request());
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get(OrgDimensionStructureParams.ORG_STR_LIST.name());
        final List<OrganizationDimensionStructureData> alOrganizationStrData = (List<OrganizationDimensionStructureData>)objectList.getValueObjectList();
        return alOrganizationStrData;
    }
    
    public Validator getValidator() {
        return this.validator;
    }
    
    public void setValidator(final Validator validator) {
        this.validator = validator;
    }
}
