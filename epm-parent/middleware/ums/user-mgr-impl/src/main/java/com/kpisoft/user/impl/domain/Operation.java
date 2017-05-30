package com.kpisoft.user.impl.domain;

import com.canopus.mw.*;
import com.kpisoft.user.vo.*;
import com.canopus.mw.utils.*;
import com.kpisoft.user.utility.*;
import com.kpisoft.user.dac.*;
import javax.validation.*;

public class Operation extends BaseDomainObject
{
    private OperationManager manager;
    private OperationData operDetails;
    
    public Operation(final OperationManager manager) {
        this.manager = manager;
    }
    
    public void validate() {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), (Object)this.operDetails, UserErrorCodesEnum.ERR_OPER_INVALID_INPUT_002.name(), "Invalid role details");
    }
    
    public OperationData getOperationDetails() {
        return this.operDetails;
    }
    
    public void setOperationDetails(final OperationData operDetails) {
        this.operDetails = operDetails;
    }
    
    private OperationDataService getDataService() {
        return this.manager.getDataService();
    }
    
    private Validator getValidator() {
        return this.manager.getValidator();
    }
}
