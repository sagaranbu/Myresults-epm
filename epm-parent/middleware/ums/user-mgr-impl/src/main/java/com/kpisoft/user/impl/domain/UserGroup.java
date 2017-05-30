package com.kpisoft.user.impl.domain;

import com.canopus.mw.*;
import com.kpisoft.user.vo.*;
import com.kpisoft.user.vo.param.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.utils.*;
import com.kpisoft.user.utility.*;
import com.kpisoft.user.dac.*;
import javax.validation.*;

public class UserGroup extends BaseDomainObject
{
    private UserGroupManager manager;
    private GroupData userGroupDetails;
    
    public UserGroup(final UserGroupManager manager) {
        this.manager = manager;
    }
    
    public int save() {
        this.validate();
        final Request request = new Request();
        request.put(UMSParams.USER_GRP_DATA.name(), (BaseValueObject)this.userGroupDetails);
        final Response response = this.getDataService().saveUserGroup(request);
        final Identifier id = (Identifier)response.get(UMSParams.USER_GRP_ID.name());
        this.userGroupDetails.setId(id.getId());
        return id.getId();
    }
    
    public Response delete() {
        final Request request = new Request();
        request.put(UMSParams.USER_GRP_ID.name(), (BaseValueObject)new Identifier(this.getUserGroupDetails().getId()));
        return this.getDataService().deleteUserGroup(request);
    }
    
    public void validate() {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), (Object)this.userGroupDetails, UserErrorCodesEnum.ERR_USER_GRP_INVALID_INPUT_002.name(), "Invalid userGroup details");
    }
    
    public GroupData getUserGroupDetails() {
        return this.userGroupDetails;
    }
    
    public void setUserGroupDetails(final GroupData userGroupDetails) {
        this.userGroupDetails = userGroupDetails;
    }
    
    private UserGroupDataService getDataService() {
        return this.manager.getDataService();
    }
    
    private Validator getValidator() {
        return this.manager.getValidator();
    }
}
