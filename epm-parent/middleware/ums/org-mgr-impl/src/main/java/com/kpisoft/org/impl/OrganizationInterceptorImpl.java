package com.kpisoft.org.impl;

import com.canopus.mw.*;
import com.kpisoft.org.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import com.canopus.mw.dto.*;
import com.kpisoft.org.vo.*;
import java.util.*;
import org.slf4j.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ OrganizationInterceptorService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class OrganizationInterceptorImpl extends BaseMiddlewareBean implements OrganizationInterceptorService
{
    private static final Logger log;
    private static final Integer OU_TYPE_FIELD_ID1;
    private static final Integer OU_TYPE_FIELD_ID2;
    private static final Integer OU_TYPE_FIELD_ID3;
    private static final Integer OU_TYPE_FIELD_ID4;
    private static final Integer OU_TYPE_FIELD_ID5;
    
    public Response beforeCreate(final Request request) {
        OrganizationInterceptorImpl.log.debug("Before creating OU");
        this.updateOrgType(request);
        return this.copyToResponse(request);
    }
    
    public Response beforeUpdate(final Request request) {
        OrganizationInterceptorImpl.log.debug("Before updating OU");
        this.updateOrgType(request);
        return this.copyToResponse(request);
    }
    
    public Response beforeDelete(final Request request) {
        OrganizationInterceptorImpl.log.debug("Before deleting OU");
        return this.copyToResponse(request);
    }
    
    public Response beforeSuspend(final Request request) {
        OrganizationInterceptorImpl.log.debug("Before suspending OU");
        return this.copyToResponse(request);
    }
    
    public StringIdentifier getServiceId() {
        return new StringIdentifier("OrganizationInterceptorImpl");
    }
    
    protected Response copyToResponse(final Request request) {
        final Map<String, BaseValueObject> params = (Map<String, BaseValueObject>)request.getRequestValueObjects();
        final Response response = new Response();
        response.getResponseValueObjects().putAll(params);
        return response;
    }
    
    protected void updateOrgType(final Request request) {
        final OrganizationUnitData data = (OrganizationUnitData)request.get(OrganizationParams.ORG_DATA.getParamName());
        if (data != null) {
            final List<OrganizationFiledData> fields = (List<OrganizationFiledData>)data.getFiledData();
            if (fields != null && !fields.isEmpty()) {
                String type = null;
                for (final OrganizationFiledData field : fields) {
                    final Integer id = field.getFieldId();
                    if (OrganizationInterceptorImpl.OU_TYPE_FIELD_ID1.equals(id) || OrganizationInterceptorImpl.OU_TYPE_FIELD_ID2.equals(id) || OrganizationInterceptorImpl.OU_TYPE_FIELD_ID3.equals(id) || OrganizationInterceptorImpl.OU_TYPE_FIELD_ID4.equals(id) || OrganizationInterceptorImpl.OU_TYPE_FIELD_ID5.equals(id)) {
                        type = field.getData();
                    }
                }
                if (type != null) {
                    try {
                        final Integer typeI = Integer.parseInt(type.trim());
                        data.setOrgType(typeI);
                    }
                    catch (Exception ex) {
                        OrganizationInterceptorImpl.log.debug("Invalid type found in the OU type field data + " + ex);
                    }
                }
            }
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)OrganizationInterceptorImpl.class);
        OU_TYPE_FIELD_ID1 = new Integer(9);
        OU_TYPE_FIELD_ID2 = new Integer(33);
        OU_TYPE_FIELD_ID3 = new Integer(40);
        OU_TYPE_FIELD_ID4 = new Integer(151);
        OU_TYPE_FIELD_ID5 = new Integer(133);
    }
}
