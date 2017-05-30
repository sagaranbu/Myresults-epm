package com.kpisoft.user.impl.domain;

import com.canopus.mw.*;
import com.kpisoft.user.vo.*;
import com.kpisoft.user.vo.param.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.utils.*;
import com.kpisoft.user.utility.*;
import com.kpisoft.user.dac.*;
import javax.validation.*;

public class User extends BaseDomainObject
{
    private UserManager manager;
    private UserData userDetails;
    
    public User(final UserManager manager) {
        this.manager = manager;
    }
    
    public int save() {
        this.validate();
        final Request request = new Request();
        request.put(UMSParams.USER_DATA.name(), (BaseValueObject)this.userDetails);
        final Response response = this.getDataService().saveUser(request);
        final Identifier id = (Identifier)response.get(UMSParams.USER_ID.name());
        this.userDetails.setId(id.getId());
        return id.getId();
    }
    
    public boolean delete() {
        this.validate();
        final Request request = new Request();
        request.put(UMSParams.USER_ID.name(), (BaseValueObject)new Identifier(this.userDetails.getId()));
        final Response response = this.getDataService().deleteUser(request);
        final BooleanResponse status = (BooleanResponse)response.get(UMSParams.STATUS_RESPONSE.name());
        return status.isResponse();
    }
    
    public void validate() {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), (Object)this.userDetails, UserErrorCodesEnum.ERR_USER_GRP_INVALID_INPUT_002.name(), "Invalid user details");
    }
    
    public UserData getUserDetails() {
        return this.userDetails;
    }
    
    public void setUserDetails(final UserData userDetails) {
        this.userDetails = userDetails;
    }
    
    private UserDataService getDataService() {
        return this.manager.getDataService();
    }
    
    private Validator getValidator() {
        return this.manager.getValidator();
    }
}
