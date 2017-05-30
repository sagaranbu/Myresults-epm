package com.kpisoft.emp.impl.domain;

import org.springframework.stereotype.*;
import com.kpisoft.emp.dac.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import org.perf4j.aop.*;
import javax.annotation.*;
import com.kpisoft.emp.vo.*;
import com.kpisoft.emp.utility.*;
import com.canopus.mw.*;
import com.kpisoft.emp.vo.param.*;
import java.util.*;
import com.canopus.mw.dto.*;

@Component
public class ManagerLevelManager extends BaseDomainManager implements CacheLoader<Integer, ManagerLevelDomain>
{
    @Autowired
    private ManagerLevelDataService dataService;
    @Autowired
    @Qualifier("managerLevelCache")
    private Cache<Integer, ManagerLevelDomain> cache;
    private ManagerLevelDomain managerLevelDomain;
    private static final Logger log;
    
    public ManagerLevelManager() {
        this.dataService = null;
        this.cache = null;
        this.managerLevelDomain = null;
    }
    
    @Profiled(tag = "ManagerLevel Manager init")
    @PostConstruct
    public void loadCacheOnStartUp() {
        ManagerLevelManager.log.debug((Object)"ManagerLevel manager initializing");
        ExecutionContext.getCurrent().setCrossTenant();
        this.getAllManagerLevels(new SortList());
        ExecutionContext.getCurrent().unSetCrossTenant();
    }
    
    public Integer saveManagerLevel(final ManagerLevelData managerLevelData) {
        this.managerLevelDomain = new ManagerLevelDomain(this);
        Integer result = null;
        try {
            result = this.managerLevelDomain.saveManagerLevel(managerLevelData);
        }
        catch (Exception e) {
            throw new MiddlewareException(EmpErrorCodeEnum.ERR_MGR_LEVEL_UNABLE_TO_CREATE_003.name(), e.getMessage());
        }
        finally {
            if (managerLevelData.getId() != null && managerLevelData.getId() > 0) {
                this.cache.remove(managerLevelData.getId());
            }
        }
        return result;
    }
    
    public ManagerLevelDomain getManagerLevel(final Integer id) {
        final ManagerLevelDomain managerLevel = (ManagerLevelDomain)this.getCache().get(id, (CacheLoader)this);
        return managerLevel;
    }
    
    public Response updateManagerLevel(final Request req) {
        this.managerLevelDomain = new ManagerLevelDomain(this);
        Response res = null;
        Identifier id = null;
        try {
            res = this.managerLevelDomain.updateManagerLevel(req);
            id = (Identifier)res.get(EMPParams.EMP_MGR_LEVEL_ID.name());
        }
        catch (Exception e) {
            throw new MiddlewareException(EmpErrorCodeEnum.ERR_MGR_LEVEL_UNABLE_TO_UPDATE_005.name(), e.getMessage());
        }
        finally {
            if (id.getId() != null && id.getId() > 0) {
                this.cache.remove(id.getId());
            }
        }
        return res;
    }
    
    public Response removeManagerLevel(final Request req) {
        this.managerLevelDomain = new ManagerLevelDomain(this);
        final Response response = this.managerLevelDomain.removeManagerLevel(req);
        final BooleanResponse res = (BooleanResponse)response.get(EMPParams.EMP_MGR_LEVEL_BOOLEAN_RESPONSE.name());
        if (res.isResponse()) {
            final Identifier identifier = (Identifier)req.get(EMPParams.EMP_MGR_LEVEL_ID.name());
            this.cache.remove(identifier.getId());
        }
        return response;
    }
    
    public List<ManagerLevelData> getAllManagerLevels(SortList sortList) {
        final Request request = new Request();
        request.setSortList(sortList);
        List<ManagerLevelData> result = new ArrayList<ManagerLevelData>();
        try {
            final Response response = this.dataService.getAllManagerLevels(request);
            final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.EMP_MGR_LEVEL_DATA_LIST.name());
            result = (List<ManagerLevelData>)list.getValueObjectList();
            sortList = response.getSortList();
            for (final ManagerLevelData managerLevelData : result) {
                final ManagerLevelDomain managerLevelDomain = new ManagerLevelDomain(this);
                managerLevelDomain.setManagerLevelData(managerLevelData);
                this.cache.put(managerLevelData.getId(), managerLevelDomain);
            }
        }
        catch (Exception ex) {}
        return result;
    }
    
    public List<ManagerLevelData> searchManagerLevel(final ManagerLevelData managerLevelData, SortList sortList) {
        final Request request = new Request();
        request.put(EMPParams.EMP_MGR_LEVEL_DATA.name(), (BaseValueObject)managerLevelData);
        request.setSortList(sortList);
        final Response response = this.dataService.searchManagerLevel(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.EMP_MGR_LEVEL_DATA_LIST.name());
        final List<ManagerLevelData> result = (List<ManagerLevelData>)list.getValueObjectList();
        sortList = response.getSortList();
        return result;
    }
    
    public ManagerLevelDataService getDataService() {
        return this.dataService;
    }
    
    public void setDataService(final ManagerLevelDataService dataService) {
        this.dataService = dataService;
    }
    
    public Cache<Integer, ManagerLevelDomain> getCache() {
        return this.cache;
    }
    
    public void setCache(final Cache<Integer, ManagerLevelDomain> cache) {
        this.cache = cache;
    }
    
    public ManagerLevelDomain load(final Integer key) {
        final Request request = new Request();
        final ManagerLevelDataService svc = this.getDataService();
        request.put(EMPParams.EMP_MGR_LEVEL_ID.name(), (BaseValueObject)new Identifier(key));
        final Response response = svc.getManagerLevel(request);
        final ManagerLevelData data = (ManagerLevelData)response.get(EMPParams.EMP_MGR_LEVEL_DATA.name());
        final ManagerLevelDomain managerLevelDomain = new ManagerLevelDomain(this);
        managerLevelDomain.setManagerLevelData(data);
        return managerLevelDomain;
    }
    
    static {
        log = Logger.getLogger((Class)ManagerLevelManager.class);
    }
}
