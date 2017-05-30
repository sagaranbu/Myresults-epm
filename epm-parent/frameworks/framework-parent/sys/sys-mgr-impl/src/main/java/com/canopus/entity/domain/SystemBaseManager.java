package com.canopus.entity.domain;

import org.springframework.stereotype.*;
import com.canopus.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import org.perf4j.aop.*;
import javax.annotation.*;
import com.canopus.entity.vo.params.*;
import com.canopus.mw.*;
import java.util.*;
import com.canopus.mw.dto.*;
import com.canopus.entity.vo.*;

@Component
public class SystemBaseManager extends BaseDomainManager
{
    @Autowired
    private SystemDataService dataService;
    private Map<Integer, SystemMasterBaseBean> basicMap;
    private Map<Integer, Set<Integer>> categoryMap;
    private static final Logger log;
    
    public SystemBaseManager() {
        this.dataService = null;
        this.basicMap = null;
        this.categoryMap = null;
        this.basicMap = new HashMap<Integer, SystemMasterBaseBean>();
        this.categoryMap = new HashMap<Integer, Set<Integer>>();
    }
    
    @Profiled(tag = "System Base Manager init")
    @PostConstruct
    public void init() {
        SystemBaseManager.log.debug((Object)"System base manager initializing");
        ExecutionContext.getCurrent().setCrossTenant();
        this.loadAllBaseData();
        ExecutionContext.getCurrent().unSetCrossTenant();
    }
    
    public void loadAllBaseData() {
        this.basicMap.clear();
        this.categoryMap.clear();
        try {
            final Response response = this.dataService.getAllBaseData(new Request());
            final BaseValueObjectList list = (BaseValueObjectList)response.get(SysParams.SYS_BASE_DATA_LIST.name());
            if (list != null && list.getValueObjectList() != null && !list.getValueObjectList().isEmpty()) {
                final List<SystemMasterBaseBean> baseBean = (List<SystemMasterBaseBean>)list.getValueObjectList();
                for (final SystemMasterBaseBean iterator : baseBean) {
                    this.basicMap.put(iterator.getId(), iterator);
                    if (iterator.getCategory() != null && iterator.getCategory().getId() != null) {
                        if (this.categoryMap.containsKey(iterator.getCategory().getId())) {
                            final Set<Integer> idList = this.categoryMap.get(iterator.getCategory().getId());
                            idList.add(iterator.getId());
                            this.categoryMap.put(iterator.getCategory().getId(), idList);
                        }
                        else {
                            final Set<Integer> idList = new HashSet<Integer>();
                            idList.add(iterator.getId());
                            this.categoryMap.put(iterator.getCategory().getId(), idList);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            SystemBaseManager.log.error((Object)("Exception in SystemBaseManager - loadAllBaseData() : " + e.getMessage()));
        }
    }
    
    public Integer saveOrUpdateBaseData(final SystemMasterBaseBean systemMasterBaseBean) {
        Identifier id = null;
        final Request request = new Request();
        request.put(SysParams.SYS_BASE_DATA.name(), (BaseValueObject)systemMasterBaseBean);
        try {
            final Response response = this.dataService.createBaseData(request);
            id = (Identifier)response.get(SysParams.SYS_BASE_ID.name());
            this.basicMap.put(id.getId(), null);
            this.getBaseById(id.getId());
        }
        catch (Exception e) {
            throw new MiddlewareException("ERR_SAVE_UPDATE_BASEDATA", e.getMessage(), (Throwable)e);
        }
        return id.getId();
    }
    
    public SystemMasterBaseBean getBaseById(final Integer id) {
        SystemMasterBaseBean baseBean = this.basicMap.get(id);
        if (baseBean == null) {
            final Request request = new Request();
            request.put(SysParams.SYS_BASE_ID.name(), (BaseValueObject)new Identifier(id));
            final Response response = this.dataService.getBaseDataById(request);
            baseBean = (SystemMasterBaseBean)response.get(SysParams.SYS_BASE_DATA.name());
            this.basicMap.put(baseBean.getId(), baseBean);
            if (baseBean.getCategory() != null && baseBean.getCategory().getId() != null) {
                if (this.categoryMap.containsKey(baseBean.getCategory().getId())) {
                    final Set<Integer> idList = this.categoryMap.get(baseBean.getCategory().getId());
                    idList.add(baseBean.getId());
                    this.categoryMap.put(baseBean.getCategory().getId(), idList);
                }
                else {
                    final Set<Integer> idList = new HashSet<Integer>();
                    idList.add(baseBean.getId());
                    this.categoryMap.put(baseBean.getCategory().getId(), idList);
                }
            }
        }
        return baseBean;
    }
    
    public List<SystemMasterBaseBean> getAllBaseData() {
        if (this.basicMap.isEmpty()) {
            this.loadAllBaseData();
        }
        final List<SystemMasterBaseBean> result = new ArrayList<SystemMasterBaseBean>();
        for (final Integer id : this.basicMap.keySet()) {
            result.add(this.basicMap.get(id));
        }
        return result;
    }
    
    public List<SystemMasterBaseBean> getBaseByCategory(final Integer id) {
        List<SystemMasterBaseBean> result = new ArrayList<SystemMasterBaseBean>();
        final Set<Integer> idList = this.categoryMap.get(id);
        if (idList == null || idList.isEmpty()) {
            final Request request = new Request();
            request.put(SysParams.SYS_CATEGORY_ID.name(), (BaseValueObject)new Identifier(id));
            final Response response = this.dataService.getBaseDataForCategory(request);
            final BaseValueObjectList list = (BaseValueObjectList)response.get(SysParams.SYS_BASE_DATA_LIST.name());
            result = (List<SystemMasterBaseBean>)list.getValueObjectList();
        }
        else {
            for (final Integer ids : idList) {
                result.add(this.basicMap.get(ids));
            }
        }
        return result;
    }
    
    public List<SystemMasterBaseBean> getBaseByCategories(final Integer category, final Integer subCategory) {
        final Request request = new Request();
        request.put(SysParams.SYS_CATEGORY_ID.name(), (BaseValueObject)new Identifier(category));
        request.put(SysParams.SYS_SUB_CATEGORY_ID.name(), (BaseValueObject)new Identifier(subCategory));
        final Response response = this.dataService.getBaseDataForBothCategories(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(SysParams.SYS_BASE_DATA_LIST.name());
        final List<SystemMasterBaseBean> result = (List<SystemMasterBaseBean>)list.getValueObjectList();
        return result;
    }
    
    public boolean deleteBaseSystem(final Integer id) {
        final SystemMasterBaseBean baseBean = this.getBaseById(id);
        final Request request = new Request();
        request.put(SysParams.SYS_BASE_ID.name(), (BaseValueObject)new Identifier(id));
        final Response response = this.dataService.removeBaseDataById(request);
        final BooleanResponse booleanResponse = (BooleanResponse)response.get(SysParams.STATUS_RESPONSE.name());
        if (booleanResponse != null && booleanResponse.isResponse()) {
            this.basicMap.remove(id);
            if (baseBean.getCategory() != null && baseBean.getCategory().getId() != null && this.categoryMap.containsKey(baseBean.getCategory().getId())) {
                final Set<Integer> idList = this.categoryMap.get(baseBean.getCategory().getId());
                idList.remove(baseBean.getId());
                this.categoryMap.put(baseBean.getCategory().getId(), idList);
            }
        }
        return booleanResponse.isResponse();
    }
    
    public List<SystemMasterBaseBean> getSysMasterByIds(final String ids) {
        final String[] array = ids.split(",");
        final List<SystemMasterBaseBean> sysMasterList = new ArrayList<SystemMasterBaseBean>();
        for (int index = 0; index < array.length; ++index) {
            final SystemMasterBaseBean baseBean = this.getBaseById(Integer.parseInt(array[index].trim()));
            sysMasterList.add(baseBean);
        }
        return sysMasterList;
    }
    
    public List<SystemMasCategoryData> searchCatagory(final SystemMasCategoryData categoryData) {
        final Request req = new Request();
        req.put(SysParams.SYS_CATEGORY_DATA.name(), (BaseValueObject)categoryData);
        final Response res = this.dataService.searchCatagory(req);
        final BaseValueObjectList sysBVList = (BaseValueObjectList)res.get(SysParams.SYS_CATEGORY_DATA_LIST.name());
        final List<SystemMasCategoryData> categoryList = (List<SystemMasCategoryData>)sysBVList.getValueObjectList();
        return categoryList;
    }
    
    public List<SystemMasterBaseBean> searchMasterBase(final SystemMasterBaseBean masterBaseData) {
        final Request requset = new Request();
        requset.put(SysParams.SYS_BASE_DATA.name(), (BaseValueObject)masterBaseData);
        final Response res = this.dataService.searchMasterBase(requset);
        final BaseValueObjectList baseValObjList = (BaseValueObjectList)res.get(SysParams.SYS_BASE_DATA_LIST.name());
        final List<SystemMasterBaseBean> masterBaseList = (List<SystemMasterBaseBean>)baseValObjList.getValueObjectList();
        return masterBaseList;
    }
    
    static {
        log = Logger.getLogger((Class)SystemBaseManager.class);
    }
}
