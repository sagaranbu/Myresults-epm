package com.canopus.saas.domain;

import com.canopus.mw.*;
import org.springframework.stereotype.*;
import com.canopus.saas.dac.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.saas.vo.*;
import com.canopus.saas.vo.params.*;
import java.util.*;
import com.canopus.mw.dto.*;

@Component
public class TenantManager extends BaseDomainManager
{
    @Autowired
    private TenantBaseDataService dataService;
    
    public TenantManager() {
        this.dataService = null;
    }
    
    public Integer createTenant(final TenantBaseData data) {
        final Request request = new Request();
        request.put(TenantParams.TENANT_DATA.name(), (BaseValueObject)data);
        final Response response = this.dataService.createTenant(request);
        final Identifier identifier = (Identifier)response.get(TenantParams.TENANT_ID.name());
        return identifier.getId();
    }
    
    public boolean removeTenant(final Integer id) {
        final Request request = new Request();
        request.put(TenantParams.TENANT_ID.name(), (BaseValueObject)new Identifier(id));
        final Response response = this.dataService.removeTenant(request);
        final BooleanResponse bResponse = (BooleanResponse)response.get(TenantParams.STATUS_RESPONSE.name());
        return bResponse.isResponse();
    }
    
    public TenantBaseData getTenant(final Integer id) {
        final Request request = new Request();
        request.put(TenantParams.TENANT_ID.name(), (BaseValueObject)new Identifier(id));
        final Response response = this.dataService.getTenant(request);
        final TenantBaseData tenantBaseData = (TenantBaseData)response.get(TenantParams.TENANT_DATA.name());
        return tenantBaseData;
    }
    
    public List<TenantBaseData> getAllTenants() {
        final Request request = new Request();
        final Response response = this.dataService.getAllTeanats(request);
        final BaseValueObjectList blist = (BaseValueObjectList)response.get(TenantParams.TENANT_TYPE_LIST.name());
        final List<TenantBaseData> list = (List<TenantBaseData>)blist.getValueObjectList();
        return list;
    }
}
