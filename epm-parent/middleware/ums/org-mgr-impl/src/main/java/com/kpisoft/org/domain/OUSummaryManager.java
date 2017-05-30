package com.kpisoft.org.domain;

import com.canopus.mw.*;
import com.canopus.mw.aggregation.*;
import org.springframework.stereotype.*;
import com.kpisoft.org.dac.*;
import org.springframework.beans.factory.annotation.*;
import javax.validation.*;
import com.canopus.mw.cache.*;
import javax.annotation.*;
import com.kpisoft.org.params.*;
import com.kpisoft.org.dac.impl.domain.*;
import com.canopus.mw.dto.*;
import java.util.*;
import com.kpisoft.org.vo.*;

@Component
public class OUSummaryManager extends BaseDomainObject implements ISummaryManager, CacheLoader<Integer, ISummaryData>
{
    @Autowired
    private OrganizationSummaryDataService organizationSummaryDataService;
    @Autowired
    private OrganizationManager organizationManager;
    @Value("#{summaryManagersMap}")
    private Map<String, ISummaryManager> summaryManagersMap;
    @Autowired
    private Validator validator;
    private Cache<Integer, ISummaryData> cache;
    
    public OUSummaryManager() {
        this.cache = null;
    }
    
    public String getSummaryEntityName() {
        return "OUSummary";
    }
    
    public ISummaryData load(final Integer key) {
        return null;
    }
    
    @PostConstruct
    public void init() {
        this.summaryManagersMap.put(this.getSummaryEntityName(), (ISummaryManager)this);
    }
    
    public ISummaryData getSummaryEntity(final int id) {
        final Request req = new Request();
        req.put(OUSummaryParms.OUSUMMARY_ID.name(), (BaseValueObject)new Identifier(1));
        final Response response = this.organizationSummaryDataService.getOUSummary(req);
        final OUSummaryEntity oe = new OUSummaryEntity();
        final OUSummaryData ous = (OUSummaryData)response.get(OUSummaryParms.OUSUMMARY.name());
        return (ISummaryData)ous;
    }
    
    public List<ISummaryData> getSummaryEntities(final List<Integer> ids) {
        final Request req = new Request();
        final IdentifierList idlist = new IdentifierList((List)ids);
        req.put(OUSummaryParms.OUSUMMARY_ID_LIST.name(), (BaseValueObject)idlist);
        final Response response = this.organizationSummaryDataService.getOUSummaries(req);
        final BaseValueObjectList bvlist1 = (BaseValueObjectList)response.get(OUSummaryParms.OUSUMMARY_LIST.name());
        final List<OUSummaryData> list2 = (List<OUSummaryData>)bvlist1.getValueObjectList();
        final List<ISummaryData> l1 = new ArrayList<ISummaryData>();
        for (final OUSummaryData data : list2) {
            l1.add((ISummaryData)data);
        }
        return l1;
    }
    
    public int saveSummaryEntity(final ISummaryData summaryEntity) {
        final Request req = new Request();
        req.put(OUSummaryParms.OUSUMMARY.name(), (BaseValueObject)summaryEntity);
        final Response response = this.organizationSummaryDataService.saveOUSummary(req);
        final OUSummaryEntity oe = new OUSummaryEntity();
        final Identifier id = (Identifier)response.get(OUSummaryParms.OUSUMMARY_ID.name());
        return id.getId();
    }
    
    public int saveSummaryEntities(final List<ISummaryData> summaryEntities) {
        final Request request = new Request();
        final List<OUSummaryData> listBefore = new ArrayList<OUSummaryData>();
        for (final ISummaryData i : summaryEntities) {
            listBefore.add((OUSummaryData)i);
        }
        final BaseValueObjectList baseValueObjectList = new BaseValueObjectList();
        baseValueObjectList.setValueObjectList((List)listBefore);
        request.put(OUSummaryParms.OUSUMMARY_LIST.name(), (BaseValueObject)baseValueObjectList);
        final Response response = this.organizationSummaryDataService.saveOUSummaries(request);
        return 1;
    }
    
    public List<ISummaryData> getParentSummaryEntities(final List<Integer> ids) {
        final List<OrgIdentityBean> parentIds = this.organizationManager.getAscendantsByIdList(ids, 1);
        return this.getSummaryEntitiesByListOfIds(parentIds);
    }
    
    public List<ISummaryData> getSummaryEntitiesByListOfIds(final List<OrgIdentityBean> ids) {
        final List<Integer> ids2 = new ArrayList<Integer>();
        for (final OrgIdentityBean i : ids) {
            ids2.add(i.getId());
        }
        return this.getParentSummaryEntities(ids2);
    }
    
    public List<Integer> getParentIds(final List<Integer> ids) {
        final List<OrgIdentityBean> parentIds = this.organizationManager.getAscendantsByIdList(ids, 1);
        final List<Integer> ids2 = new ArrayList<Integer>();
        for (final OrgIdentityBean i : parentIds) {
            ids2.add(i.getId());
        }
        return ids2;
    }
}
