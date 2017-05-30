package com.kpisoft.user.impl.domain;

import com.canopus.mw.*;
import com.kpisoft.user.vo.*;
import com.kpisoft.user.vo.param.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.utils.*;
import com.kpisoft.user.utility.*;
import com.kpisoft.user.dac.*;
import javax.validation.*;

public class Role extends BaseDomainObject
{
    private RoleManager manager;
    private RoleData roleDetails;
    
    public Role(final RoleManager manager) {
        this.manager = manager;
    }
    
    public int save() {
        this.validate();
        final Request request = new Request();
        request.put(UMSParams.ROLE_DATA.name(), (BaseValueObject)this.roleDetails);
        final Response response = this.getDataService().saveRole(request);
        final Identifier id = (Identifier)response.get(UMSParams.ROLE_ID.name());
        this.roleDetails.setId(id.getId());
        return id.getId();
    }
    
    public boolean delete() {
        final Request request = new Request();
        request.put(UMSParams.ROLE_ID.name(), (BaseValueObject)new Identifier(this.getRoleDetails().getId()));
        final Response response = this.getDataService().deleteRole(request);
        final BooleanResponse status = (BooleanResponse)response.get(UMSParams.STATUS_RESPONSE.name());
        if (status.isResponse()) {
            this.manager.getMultiTenantCache().remove(this.getRoleDetails().getId());
        }
        return status.isResponse();
    }
    
    public void validate() {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), (Object)this.roleDetails, UserErrorCodesEnum.ERR_ROLE_INVALID_INPUT_002.name(), "Invalid role details");
    }
    
    public RoleData getRoleDetails() {
        return this.roleDetails;
    }
    
    public void setRoleDetails(final RoleData roleDetails) {
        this.roleDetails = roleDetails;
    }
    
    private RoleDataService getDataService() {
        return this.manager.getDataService();
    }
    
    private Validator getValidator() {
        return this.manager.getValidator();
    }
}
