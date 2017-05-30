package com.kpisoft.emp.impl.domain;

import com.kpisoft.emp.dac.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import javax.annotation.*;
import com.kpisoft.emp.vo.param.*;
import com.kpisoft.emp.vo.*;
import com.kpisoft.emp.utility.*;
import com.canopus.mw.*;
import com.canopus.mw.dto.*;
import java.util.*;

public class DesignationManager extends BaseDomainManager implements CacheLoader<Integer, Designation>
{
    @Autowired
    private DesignationDataService designationDataService;
    @Autowired
    @Qualifier("designationCache")
    private Cache<Integer, Designation> cache;
    private static final Logger log;
    
    public DesignationManager() {
        this.designationDataService = null;
        this.cache = null;
    }
    
    @PostConstruct
    public void loadCacheOnStartUp() {
        ExecutionContext.getCurrent().setCrossTenant();
        this.getAllDesignations(new SortList());
        ExecutionContext.getCurrent().unSetCrossTenant();
    }
    
    public Designation load(final Integer key) {
        final Request request = new Request();
        request.put(EMPParams.DESG_ID.name(), (BaseValueObject)new Identifier(key));
        final Response response = this.designationDataService.getDesignation(request);
        final DesignationData data = (DesignationData)response.get(EMPParams.DESG_DATA.name());
        final Designation designation = new Designation(this);
        designation.setDesignationData(data);
        return designation;
    }
    
    public Cache<Integer, Designation> getCache() {
        return this.cache;
    }
    
    public void setCache(final Cache<Integer, Designation> cache) {
        this.cache = cache;
    }
    
    public DesignationDataService getDataService() {
        return this.designationDataService;
    }
    
    public Integer saveOrUpdateDesignation(final DesignationData data) {
        final Designation designation = new Designation(this);
        designation.setDesignationData(data);
        int id = 0;
        try {
            id = designation.save();
        }
        catch (Exception e) {
            DesignationManager.log.error((Object)"Exception in DesignationManager saveOrUpdateDesignation() : ", (Throwable)e);
            throw new MiddlewareException(EmpErrorCodeEnum.ERR_DESG_SAVE_UPDATE.name(), e.getMessage(), (Throwable)e);
        }
        finally {
            if (id > 0) {
                this.cache.remove(id);
            }
        }
        return id;
    }
    
    public Designation getDesignation(final int desgID) {
        final Designation designation = (Designation)this.getCache().get(desgID, (CacheLoader)this);
        return designation;
    }
    
    public boolean deleteDesignation(final Integer desgId) {
        final Request request = new Request();
        request.put(EMPParams.DESG_ID.name(), (BaseValueObject)new Identifier(desgId));
        final Response response = this.designationDataService.removeDesignation(request);
        final BooleanResponse status = (BooleanResponse)response.get(EMPParams.BOOLEAN_RESPONSE.name());
        if (status.isResponse()) {
            this.cache.remove(desgId);
        }
        return status.isResponse();
    }
    
    public List<DesignationData> getAllDesignations(SortList sortList) {
        final Request request = new Request();
        request.setSortList(sortList);
        final Response response = this.designationDataService.getAllDesignations(request);
        sortList = response.getSortList();
        final BaseValueObjectList baseValueObjectList = (BaseValueObjectList)response.get(EMPParams.DESG_DATA_LIST.name());
        final List<DesignationData> designationDatas = (List<DesignationData>)baseValueObjectList.getValueObjectList();
        if (designationDatas != null && !designationDatas.isEmpty()) {
            for (final DesignationData iterator : designationDatas) {
                final Designation designation = new Designation(this);
                designation.setDesignationData(iterator);
                this.cache.put(iterator.getId(), designation);
            }
        }
        return designationDatas;
    }
    
    public List<DesignationData> searchDesignations(final DesignationData designationData, SortList sortList) {
        final Request request = new Request();
        request.put(EMPParams.DESG_DATA.name(), (BaseValueObject)designationData);
        request.setSortList(sortList);
        final Response response = this.designationDataService.searchDesignation(request);
        sortList = response.getSortList();
        final BaseValueObjectList baseValueObjectList = (BaseValueObjectList)response.get(EMPParams.DESG_DATA_LIST.name());
        final List<DesignationData> designationDatas = (List<DesignationData>)baseValueObjectList.getValueObjectList();
        return designationDatas;
    }
    
    static {
        log = Logger.getLogger((Class)DesignationManager.class);
    }
}
