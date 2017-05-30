package com.kpisoft.emp.impl.domain;

import com.canopus.mw.*;
import com.kpisoft.emp.vo.*;
import com.kpisoft.emp.vo.param.*;
import com.canopus.mw.dto.*;
import com.kpisoft.emp.dac.*;

public class ManagerLevelDomain extends BaseDomainObject
{
    private ManagerLevelManager managerLevelManager;
    private ManagerLevelData managerLevelData;
    
    public ManagerLevelDomain(final ManagerLevelManager manager) {
        this.managerLevelManager = manager;
    }
    
    public Integer saveManagerLevel(final ManagerLevelData data) {
        final Request request = new Request();
        request.put(EMPParams.EMP_MGR_LEVEL_DATA.name(), (BaseValueObject)data);
        final Response response = this.getDataService().saveManagerLevel(request);
        final Identifier identifier = (Identifier)response.get(EMPParams.EMP_MGR_LEVEL_ID.name());
        return identifier.getId();
    }
    
    public Response updateManagerLevel(final Request req) {
        return this.getDataService().updateManagerLevel(req);
    }
    
    public Response removeManagerLevel(final Request req) {
        return this.getDataService().removeManagerLevel(req);
    }
    
    private ManagerLevelDataService getDataService() {
        return this.managerLevelManager.getDataService();
    }
    
    public ManagerLevelData getManagerLevelData() {
        return this.managerLevelData;
    }
    
    public void setManagerLevelData(final ManagerLevelData managerLevelData) {
        this.managerLevelData = managerLevelData;
    }
}
