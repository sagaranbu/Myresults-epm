package com.kpisoft.emp.impl.domain;

import com.canopus.mw.*;
import com.kpisoft.emp.vo.*;
import com.kpisoft.emp.vo.param.*;
import com.canopus.mw.dto.*;
import com.kpisoft.emp.dac.*;

public class Designation extends BaseDomainObject
{
    private DesignationManager designationManager;
    private DesignationData designationData;
    
    public DesignationData getDesignationData() {
        return this.designationData;
    }
    
    public void setDesignationData(final DesignationData designationData) {
        this.designationData = designationData;
    }
    
    public Designation(final DesignationManager designationManager) {
        this.designationManager = designationManager;
    }
    
    public int save() {
        final Request request = new Request();
        request.put(EMPParams.DESG_DATA.name(), (BaseValueObject)this.designationData);
        final Response response = this.getDataService().saveDesignation(request);
        final Identifier identifier = (Identifier)response.get(EMPParams.DESG_ID.name());
        this.designationData.setId(identifier.getId());
        return identifier.getId();
    }
    
    private DesignationDataService getDataService() {
        return this.designationManager.getDataService();
    }
}
