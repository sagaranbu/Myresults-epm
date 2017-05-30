package com.kpisoft.org.domain;

import org.springframework.stereotype.*;
import com.kpisoft.org.dac.*;
import javax.validation.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.tinkerpop.frames.*;
import org.apache.log4j.*;
import com.tinkerpop.blueprints.impls.tg.*;
import org.perf4j.aop.*;
import javax.annotation.*;
import com.canopus.mw.*;
import com.canopus.mw.dto.*;
import com.kpisoft.org.graph.*;
import com.tinkerpop.blueprints.*;
import java.util.*;
import com.canopus.mw.events.*;
import com.kpisoft.org.params.*;
import com.kpisoft.org.vo.*;

@Component
public class OrganizationManager extends BaseDomainManager implements CacheLoader<Integer, OrganizationUnit>
{
    @Autowired
    private OrganizationUnitDataService dataService;
    @Autowired
    private Validator validator;
    @Autowired
    @Qualifier("organizationCache")
    private Cache<Integer, OrganizationUnit> cache;
    @Autowired
    protected IMiddlewareEventClient middlewareEventClient;
    private Graph identityGraph;
    private FramedGraph<Graph> identityFramedGraph;
    private OrgIdentityParentRelationship identityParentRelationship;
    private static final Logger log;
    
    public OrganizationManager() {
        this.dataService = null;
        this.cache = null;
        this.identityGraph = null;
        this.identityFramedGraph = null;
        this.identityParentRelationship = null;
        this.identityGraph = (Graph)new TinkerGraph();
        this.identityFramedGraph = (FramedGraph<Graph>)new FramedGraph(this.identityGraph);
        this.identityParentRelationship = new OrgIdentityParentRelationship(this.identityFramedGraph);
    }
    
    @Profiled(tag = "Org manager init")
    @PostConstruct
    public void init() {
        OrganizationManager.log.debug((Object)"Org manager initializing");
        ExecutionContext.getCurrent().setCrossTenant();
        this.reloadOrgGraph();
        ExecutionContext.getCurrent().unSetCrossTenant();
    }
    
    public void loadOrgUnitsToCache() {
        try {
            final Response response = this.dataService.loadOrgUnitsToCache(new Request());
            final BaseValueObjectList list = (BaseValueObjectList)response.get(OrganizationParams.ORG_DATA_LIST.name());
            if (list != null && list.getValueObjectList() != null && !list.getValueObjectList().isEmpty()) {
                final List<OrganizationUnitData> orgUnits = (List<OrganizationUnitData>)list.getValueObjectList();
                for (final OrganizationUnitData iterator : orgUnits) {
                    final OrganizationUnit orgUnit = new OrganizationUnit(this);
                    orgUnit.setOrgUnitData(iterator);
                    this.cache.put(iterator.getId(), orgUnit);
                }
            }
        }
        catch (Exception e) {
            OrganizationManager.log.error((Object)("Exception in OrganizationManager - loadOrgUnitsToCache() : " + e));
        }
    }
    
    public void reloadOrgGraph() {
        try {
            final Response response = this.dataService.getAllParentRelationships(new Request());
            final BaseValueObjectList list = (BaseValueObjectList)response.get(OrganizationParams.ORG_REL_DATA_LIST.name());
            if (list != null && list.getValueObjectList() != null && !list.getValueObjectList().isEmpty()) {
                final List<OrgParentRelationshipBean> result = (List<OrgParentRelationshipBean>)list.getValueObjectList();
                if (result != null && !result.isEmpty()) {
                    synchronized (OrganizationManager.class) {
                        final TinkerGraph tempGraph = (TinkerGraph)this.identityGraph;
                        tempGraph.clear();
                        for (final OrgParentRelationshipBean iterator : result) {
                            this.addParentInGraph(iterator);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            OrganizationManager.log.error((Object)"Exception in OrganizationManager - reloadOrgGraph() : ", (Throwable)e);
        }
    }
    
    public OrganizationUnit createOrganizationUnit(final OrganizationUnitData data, final Integer dimensionId) {
        final OrganizationUnit orgUnit = new OrganizationUnit(this);
        orgUnit.setOrgUnitData(data);
        try {
            final int id = orgUnit.save(dimensionId);
            orgUnit.getOrgUnitData().setId(id);
        }
        catch (Exception e) {
            throw new MiddlewareException("SAVE_ORG_UNIT", e.getMessage(), (Throwable)e);
        }
        finally {
            if (orgUnit.getOrgUnitData().getId() != null && orgUnit.getOrgUnitData().getId() > 0) {
                this.cache.remove(orgUnit.getOrgUnitData().getId());
            }
        }
        return orgUnit;
    }
    
    public OrganizationUnit getOrganizationUnit(final int id) {
        final OrganizationUnit orgUnit = (OrganizationUnit)this.getCache().get(id, (CacheLoader)this);
        return orgUnit;
    }
    
    public Boolean updateOrganizationUnitInCache(final List<Integer> orgIdList, final Integer status, final Date startDate, final Date endDate) {
        for (final Integer orgId : orgIdList) {
            final OrganizationUnit orgUnit = (OrganizationUnit)this.getCache().get(orgId, (CacheLoader)this);
            final OrganizationUnitData data = orgUnit.getOrgUnitData();
            data.setStatus(status);
            if (startDate != null) {
                data.setStartDate(startDate);
            }
            if (endDate != null) {
                data.setEndDate(endDate);
            }
            orgUnit.setOrgUnitData(data);
            this.getCache().put(orgId, orgUnit);
        }
        return true;
    }
    
    public List<OrganizationUnitData> getOrganizationUnits() {
        final Response response = this.dataService.getOrganizationUnits(new Request());
        final BaseValueObjectList baseValueObjectList = (BaseValueObjectList)response.get(OrganizationParams.ORG_DATA_LIST.name());
        return (List<OrganizationUnitData>)baseValueObjectList.getValueObjectList();
    }
    
    public List<OrganizationUnitData> searchOrganizationUnitByName(final String orgUnitName, final Page page, final SortList sortList) {
        final Name name = new Name();
        name.setName(orgUnitName);
        final Request request = new Request();
        request.put(OrganizationParams.ORG_UNIT_NAME.name(), (BaseValueObject)name);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.searchOrganizationUnitByName(request);
        final BaseValueObjectList result = (BaseValueObjectList)response.get(OrganizationParams.ORG_DATA_LIST.name());
        List<OrganizationUnitData> orgData = new ArrayList<OrganizationUnitData>();
        if (result != null && result.getValueObjectList() != null && !result.getValueObjectList().isEmpty()) {
            orgData = (List<OrganizationUnitData>)result.getValueObjectList();
        }
        return orgData;
    }
    
    public List<OrganizationUnitData> search(final OrganizationUnitData data, Page page, SortList sortList) {
        final Request request = new Request();
        request.put(OrganizationParams.ORG_DATA.name(), (BaseValueObject)data);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.search(request);
        page = response.getPage();
        sortList = response.getSortList();
        final BaseValueObjectList list = (BaseValueObjectList)response.get(OrganizationParams.ORG_DATA_LIST.name());
        List<OrganizationUnitData> result = new ArrayList<OrganizationUnitData>();
        if (list != null && list.getValueObjectList() != null && !list.getValueObjectList().isEmpty()) {
            result = (List<OrganizationUnitData>)list.getValueObjectList();
        }
        return result;
    }
    
    public List<OrganizationUnitData> searchAll(final OrganizationUnitData data, Page page, SortList sortList) {
        final Request request = new Request();
        request.put(OrganizationParams.ORG_DATA.name(), (BaseValueObject)data);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.searchAll(request);
        page = response.getPage();
        sortList = response.getSortList();
        final BaseValueObjectList list = (BaseValueObjectList)response.get(OrganizationParams.ORG_DATA_LIST.name());
        List<OrganizationUnitData> result = new ArrayList<OrganizationUnitData>();
        if (list != null && list.getValueObjectList() != null && !list.getValueObjectList().isEmpty()) {
            result = (List<OrganizationUnitData>)list.getValueObjectList();
        }
        return result;
    }
    
    public boolean deleteOrganizationUnit(final Integer orgId) {
        final Request request = new Request();
        request.put(OrganizationParams.ORG_IDENTIFIER.name(), (BaseValueObject)new Identifier(orgId));
        final Response response = this.dataService.deleteOrganizationUnit(request);
        final BooleanResponse status = (BooleanResponse)response.get(OrganizationParams.STATUS.name());
        if (status.isResponse()) {
            this.cache.remove(orgId);
        }
        return status.isResponse();
    }
    
    public boolean suspendOrganizationUnit(final Integer orgId, final Date endDate) {
        final DateResponse date = new DateResponse();
        date.setDate(endDate);
        final Request request = new Request();
        request.put(OrganizationParams.ORG_IDENTIFIER.name(), (BaseValueObject)new Identifier(orgId));
        request.put(OrganizationParams.END_DATE.name(), (BaseValueObject)date);
        final Response response = this.dataService.suspendOrganizationUnit(request);
        final BooleanResponse status = (BooleanResponse)response.get(OrganizationParams.STATUS.name());
        return status.isResponse();
    }
    
    public boolean deleteOrganizationsByIds(final List<Integer> orgIds) {
        final Request request = new Request();
        request.put(OrganizationParams.ORG_IDENTIFIER_LIST.name(), (BaseValueObject)new IdentifierList((List)orgIds));
        final Response response = this.dataService.deleteOrganizationsByIds(request);
        final BooleanResponse status = (BooleanResponse)response.get(OrganizationParams.STATUS.name());
        if (status.isResponse()) {
            for (final Integer id : orgIds) {
                this.cache.remove(id);
            }
        }
        return status.isResponse();
    }
    
    public boolean suspendOrganizationsByIds(final List<Integer> orgIds, final Date endDate) {
        final DateResponse date = new DateResponse();
        date.setDate(endDate);
        final Request request = new Request();
        request.put(OrganizationParams.ORG_IDENTIFIER_LIST.name(), (BaseValueObject)new IdentifierList((List)orgIds));
        request.put(OrganizationParams.END_DATE.name(), (BaseValueObject)date);
        final Response response = this.dataService.suspendOrganizationsByIds(request);
        final BooleanResponse status = (BooleanResponse)response.get(OrganizationParams.STATUS.name());
        return status.isResponse();
    }
    
    public Integer addParent(final OrgParentRelationshipBean data) {
        try {
            Request request = new Request();
            request.put(OrganizationParams.ORG_REL_DATA.name(), (BaseValueObject)data);
            Response response = this.dataService.addParent(request);
            final Identifier identifier = (Identifier)response.get(OrganizationParams.ORG_REL_ID.name());
            if (identifier != null && identifier.getId() != null && identifier.getId() > 0) {
                request = new Request();
                request.put(OrganizationParams.ORG_REL_ID.name(), (BaseValueObject)identifier);
                response = this.dataService.getParentRelById(request);
                final OrgParentRelationshipBean result = (OrgParentRelationshipBean)response.get(OrganizationParams.ORG_REL_DATA.name());
                this.addParentInGraph(result);
            }
            return identifier.getId();
        }
        catch (Exception ex) {
            OrganizationManager.log.error((Object)("Exception in OrganizationManager - addParent() : " + ex));
            throw new MiddlewareException("", "Failed to add parent relationship", new Object[] { ex.getMessage(), ex });
        }
    }
    
    public void addParentInGraph(final OrgParentRelationshipBean data) {
        try {
            final OrgGraph.Organization child = (OrgGraph.Organization)this.identityParentRelationship.add(data.getSourceIdentity().getId(), (Class)OrgGraph.Organization.class);
            child.setOrgId(data.getSourceIdentity().getId());
            child.setOrgCode(data.getSourceIdentity().getOrgUnitCode());
            child.setOrgName(data.getSourceIdentity().getOrgName());
            child.setOrgStartDate(data.getSourceIdentity().getStartDate());
            if (data.getSourceIdentity().getEndDate() != null) {
                child.setOrgEndDate(data.getSourceIdentity().getEndDate());
            }
            if (data.getSourceIdentity().getLevel() != null) {
                child.setLevel(data.getSourceIdentity().getLevel());
            }
            if (data.getSourceIdentity().getOrgType() != null) {
                child.setOrgType(data.getSourceIdentity().getOrgType());
            }
            if (data.getSourceIdentity().getStatus() != null) {
                child.setStatus(data.getSourceIdentity().getStatus());
            }
            final OrgGraph.Organization parent = (OrgGraph.Organization)this.identityParentRelationship.add(data.getDestinationIdentity().getId(), (Class)OrgGraph.Organization.class);
            parent.setOrgId(data.getDestinationIdentity().getId());
            parent.setOrgCode(data.getDestinationIdentity().getOrgUnitCode());
            parent.setOrgName(data.getDestinationIdentity().getOrgName());
            parent.setOrgStartDate(data.getDestinationIdentity().getStartDate());
            if (data.getDestinationIdentity().getEndDate() != null) {
                parent.setOrgEndDate(data.getDestinationIdentity().getEndDate());
            }
            if (data.getDestinationIdentity().getLevel() != null) {
                parent.setLevel(data.getDestinationIdentity().getLevel());
            }
            if (data.getDestinationIdentity().getOrgType() != null) {
                parent.setOrgType(data.getDestinationIdentity().getOrgType());
            }
            if (data.getDestinationIdentity().getStatus() != null) {
                parent.setStatus(data.getDestinationIdentity().getStatus());
            }
            this.identityParentRelationship.addParent(data);
        }
        catch (Exception e) {
            OrganizationManager.log.error((Object)("Exception in OrganizationManager - addParentInGraph() : " + e));
        }
    }
    
    public void removeParentFromGraph(final Integer sourceId, final Integer destinationId) {
        final Vertex vertex1 = this.identityFramedGraph.getBaseGraph().getVertex((Object)sourceId);
        final Vertex vertex2 = this.identityFramedGraph.getBaseGraph().getVertex((Object)destinationId);
        if (vertex1 != null && vertex2 != null) {
            this.identityParentRelationship.removeParent(sourceId, destinationId);
        }
    }
    
    public boolean removeParent(final OrgParentRelationshipBean data) {
        try {
            final Request request = new Request();
            request.put(OrganizationParams.ORG_REL_DATA.name(), (BaseValueObject)data);
            final Response response = this.dataService.removeParent(request);
            final BooleanResponse status = (BooleanResponse)response.get(OrganizationParams.STATUS.name());
            if (status.isResponse()) {
                this.removeParentFromGraph(data.getSourceIdentity().getId(), data.getDestinationIdentity().getId());
            }
            return status.isResponse();
        }
        catch (Exception ex) {
            OrganizationManager.log.error((Object)("Exception in OrganizationManager - removeParent() : " + ex.getMessage()));
            throw new MiddlewareException("", "Failed to remove parent relationship", (Throwable)ex);
        }
    }
    
    public List<OrgParentRelationshipBean> getParentRelationshipsByOrgId(final Integer orgId) {
        final Request request = new Request();
        request.put(OrganizationParams.ORG_IDENTIFIER.name(), (BaseValueObject)new Identifier(orgId));
        final Response response = this.dataService.getParentRelationshipsByOrgId(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(OrganizationParams.ORG_REL_DATA_LIST.name());
        final List<OrgParentRelationshipBean> data = (List<OrgParentRelationshipBean>)list.getValueObjectList();
        return data;
    }
    
    public List<OrgParentRelationshipBean> getParentRelationships() {
        final Response response = this.dataService.getParentRelationships(new Request());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(OrganizationParams.ORG_REL_DATA_LIST.name());
        final List<OrgParentRelationshipBean> data = (List<OrgParentRelationshipBean>)list.getValueObjectList();
        return data;
    }
    
    public List<OrgParentRelationshipBean> getAllParentRelationships() {
        final Response response = this.dataService.getAllParentRelationships(new Request());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(OrganizationParams.ORG_REL_DATA_LIST.name());
        final List<OrgParentRelationshipBean> data = (List<OrgParentRelationshipBean>)list.getValueObjectList();
        return data;
    }
    
    public boolean suspendParentRelationshipsByIds(final List<Integer> orgIds, final Date endDate, final boolean delete) {
        final DateResponse date = new DateResponse();
        date.setDate(endDate);
        final Request request = new Request();
        request.put(OrganizationParams.ORG_IDENTIFIER_LIST.name(), (BaseValueObject)new IdentifierList((List)orgIds));
        request.put(OrganizationParams.END_DATE.name(), (BaseValueObject)date);
        request.put(OrganizationParams.DELETE.name(), (BaseValueObject)new BooleanResponse(delete));
        final Response response = this.dataService.suspendParentRelationshipsByIds(request);
        final BooleanResponse status = (BooleanResponse)response.get(OrganizationParams.STATUS.name());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(OrganizationParams.ORG_REL_DATA_LIST.name());
        if (list != null && list.getValueObjectList() != null && !list.getValueObjectList().isEmpty()) {
            final Calendar endDateCalendar = Calendar.getInstance();
            endDateCalendar.setTime(endDate);
            endDateCalendar.set(10, 0);
            endDateCalendar.set(12, 0);
            endDateCalendar.set(13, 0);
            endDateCalendar.set(14, 0);
            final Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.set(10, 0);
            currentCalendar.set(12, 0);
            currentCalendar.set(13, 0);
            currentCalendar.set(14, 0);
            if (delete || currentCalendar.getTime().equals(endDateCalendar.getTime())) {
                final List<OrgParentRelationshipBean> relations = (List<OrgParentRelationshipBean>)list.getValueObjectList();
                if (relations != null && !relations.isEmpty()) {
                    for (final OrgParentRelationshipBean iterator : relations) {
                        this.removeParentFromGraph(iterator.getSourceIdentity().getId(), iterator.getDestinationIdentity().getId());
                    }
                }
            }
            else {
                MiddlewareEvent event = new MiddlewareEvent();
                event.setEventType(OrgGraphEvents.SUSPEND_ORG_REL_EVENT.getEventId());
                event.setScheduleDate(endDateCalendar.getTime());
                this.middlewareEventClient.sendToScheduler(event);
                final IdentifierList idsList = new IdentifierList((List)orgIds);
                event = new MiddlewareEvent();
                event.setEventType(OrgGraphEvents.SUSPEND_ORG_EVENT.getEventId());
                event.setScheduleDate(endDateCalendar.getTime());
                this.middlewareEventClient.sendToScheduler(event);
            }
        }
        return status.isResponse();
    }
    
    public Integer updateOrgPasswordPolicy(final PasswordPolicyData data) {
        final Request request = new Request();
        request.put(OrganizationParams.PASSWORD_POLICY_DATA.name(), (BaseValueObject)data);
        final Response response = this.dataService.updateOrgPasswordPolicy(request);
        final Identifier identifier = (Identifier)response.get(OrganizationParams.PASSWORD_POLICY_ID.name());
        return identifier.getId();
    }
    
    public PasswordPolicyData getDefaultPasswordPolicy() {
        final Response response = this.dataService.getDefaultPasswordPolicy(new Request());
        final PasswordPolicyData data = (PasswordPolicyData)response.get(OrganizationParams.PASSWORD_POLICY_DATA.name());
        return data;
    }
    
    public OrganizationUnit load(final Integer key) {
        final Request request = new Request();
        request.put(OrganizationParams.ORG_IDENTIFIER.name(), (BaseValueObject)new Identifier(key));
        OrganizationUnitData data = null;
        try {
            final Response response = this.dataService.getOrganizationUnitData(request);
            data = (OrganizationUnitData)response.get(OrganizationParams.ORG_DATA.name());
        }
        catch (Exception e) {
            OrganizationManager.log.error((Object)("Exception in OrganizationManager - load() : " + e.getMessage()));
        }
        final OrganizationUnit orgUnit = new OrganizationUnit(this);
        orgUnit.setOrgUnitData(data);
        return orgUnit;
    }
    
    public List<OrganizationUnitData> searchDepartment(final OrganizationUnitData objOrganizationUnitData) {
        final Request request = new Request();
        request.put(OrganizationParams.ORG_DATA.name(), (BaseValueObject)objOrganizationUnitData);
        final Response response = this.dataService.searchDepartment(request);
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get(OrganizationParams.ORG_DATA_LIST.name());
        final List<OrganizationUnitData> alOrganizationUnitData = (List<OrganizationUnitData>)objectList.getValueObjectList();
        return alOrganizationUnitData;
    }
    
    public List<OrganizationUnitData> searchOrgByFieldData(final OrganizationUnitData objOrganizationUnitData, final Page page, final SortList sortList) {
        final Request request = new Request();
        request.put(OrganizationParams.ORG_DATA.name(), (BaseValueObject)objOrganizationUnitData);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.searchOrgByFieldData(request);
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get(OrganizationParams.ORG_DATA_LIST.name());
        final List<OrganizationUnitData> alOrganizationUnitData = (List<OrganizationUnitData>)objectList.getValueObjectList();
        return alOrganizationUnitData;
    }
    
    public OrganizationUnitDataService getDataService() {
        return this.dataService;
    }
    
    public void setDataService(final OrganizationUnitDataService dataService) {
        this.dataService = dataService;
    }
    
    public Validator getValidator() {
        return this.validator;
    }
    
    public void setValidator(final Validator validator) {
        this.validator = validator;
    }
    
    public Cache<Integer, OrganizationUnit> getCache() {
        return this.cache;
    }
    
    public void setCache(final Cache<Integer, OrganizationUnit> cache) {
        this.cache = cache;
    }
    
    public List<OrganizationUnitData> getOrganizationsByIdList(final IdentifierList idList, Page page, SortList sortList) {
        final Request request = new Request();
        request.put(OrganizationParams.ORG_IDENTIFIER_LIST.name(), (BaseValueObject)idList);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.getOrganizationsByIdList(request);
        page = response.getPage();
        sortList = response.getSortList();
        final BaseValueObjectList list = (BaseValueObjectList)response.get(OrganizationParams.ORG_DATA_LIST.name());
        List<OrganizationUnitData> result = new ArrayList<OrganizationUnitData>();
        if (list != null && list.getValueObjectList() != null && !list.getValueObjectList().isEmpty()) {
            result = (List<OrganizationUnitData>)list.getValueObjectList();
            for (final OrganizationUnitData iterator : result) {
                final OrganizationUnit orgUnit = new OrganizationUnit(this);
                orgUnit.setOrgUnitData(iterator);
                this.cache.put(iterator.getId(), orgUnit);
            }
        }
        return result;
    }
    
    public List<OrganizationUnitData> getOrgBaseByIds(final IdentifierList idList, Page page, SortList sortList) {
        final Request request = new Request();
        request.put(OrganizationParams.ORG_IDENTIFIER_LIST.name(), (BaseValueObject)idList);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.getOrgBaseByIds(request);
        page = response.getPage();
        sortList = response.getSortList();
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(OrganizationParams.ORG_DATA_LIST.name());
        List<OrganizationUnitData> orgData = new ArrayList<OrganizationUnitData>();
        if (bList != null && bList.getValueObjectList() != null && !bList.getValueObjectList().isEmpty()) {
            orgData = (List<OrganizationUnitData>)bList.getValueObjectList();
        }
        return orgData;
    }
    
    public FramedGraph<Graph> getIdentityFramedGraph() {
        return this.identityFramedGraph;
    }
    
    public void setIdentityFramedGraph(final FramedGraph<Graph> identityFramedGraph) {
        this.identityFramedGraph = identityFramedGraph;
    }
    
    public OrgIdentityParentRelationship getIdentityParentRelationship() {
        return this.identityParentRelationship;
    }
    
    public void setIdentityParentRelationship(final OrgIdentityParentRelationship identityParentRelationship) {
        this.identityParentRelationship = identityParentRelationship;
    }
    
    public OrgIdentityBean toIdentity(final OrgGraph.Organization org) {
        final OrgIdentityBean identityBean = new OrgIdentityBean();
        identityBean.setId(org.getOrgId());
        identityBean.setOrgUnitCode(org.getOrgCode());
        identityBean.setOrgName(org.getOrgName());
        identityBean.setLevel(org.getLevel());
        identityBean.setStartDate(org.getOrgStartDate());
        identityBean.setEndDate(org.getOrgEndDate());
        return identityBean;
    }
    
    public List<Integer> toIdList(final List<OrgIdentityBean> identity) {
        final List<Integer> idList = new ArrayList<Integer>();
        if (identity != null && !identity.isEmpty()) {
            for (final OrgIdentityBean iterator : identity) {
                idList.add(iterator.getId());
            }
        }
        return idList;
    }
    
    public List<OrgIdentityBean> getAscendantsByIdList(final List<Integer> ids, final int distance) {
        final List<OrgGraph.Organization> result = this.identityParentRelationship.getAscendantsByIdList(ids, distance);
        final List<OrgIdentityBean> ascendants = new ArrayList<OrgIdentityBean>();
        if (result != null && !result.isEmpty()) {
            for (final OrgGraph.Organization iterator : result) {
                ascendants.add(this.toIdentity(iterator));
            }
        }
        return ascendants;
    }
    
    public List<OrgIdentityBean> getDescendantsByIdList(final List<Integer> ids, final int distance) {
        final List<OrgGraph.Organization> result = this.identityParentRelationship.getDescendantsByIdList(ids, distance);
        final List<OrgIdentityBean> descendants = new ArrayList<OrgIdentityBean>();
        if (result != null && !result.isEmpty()) {
            for (final OrgGraph.Organization iterator : result) {
                descendants.add(this.toIdentity(iterator));
            }
        }
        return descendants;
    }
    
    public Graph getAscendantsGraph(final Integer id, final int distance) {
        final Graph graph = this.identityParentRelationship.getAscendantsGraph(id, distance);
        return graph;
    }
    
    public Graph getDescendantsGraph(final Integer id, final int distance) {
        final Graph graph = this.identityParentRelationship.getDescendantsGraph(id, distance);
        return graph;
    }
    
    public Graph getAscendantsGraphByDimension(final Integer id, final int distance, final int dimensionId) {
        final Graph graph = this.identityParentRelationship.getAscendantsGraphByDimension(id, distance, dimensionId);
        return graph;
    }
    
    public Graph getDescendantsGraphByDimension(final Integer id, final int distance, final int dimensionId) {
        final Graph graph = this.identityParentRelationship.getDescendantsGraphByDimension(id, distance, dimensionId);
        return graph;
    }
    
    public Graph getAscendantsGraphByIdList(final List<Integer> ids, final int distance) {
        final Graph graph = this.identityParentRelationship.getAscendantsGraphByIdList(ids, distance);
        return graph;
    }
    
    public Graph getDescendantsGraphByIdList(final List<Integer> ids, final int distance) {
        final Graph graph = this.identityParentRelationship.getDescendantsGraphByIdList2(ids, distance);
        return graph;
    }
    
    static {
        log = Logger.getLogger((Class)OrganizationManager.class);
    }
}
