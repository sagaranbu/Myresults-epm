package com.kpisoft.emp.impl.domain;

import com.kpisoft.emp.dac.*;
import javax.validation.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.tinkerpop.frames.*;
import com.tinkerpop.blueprints.impls.tg.*;
import com.kpisoft.emp.vo.param.*;
import com.canopus.mw.*;
import com.kpisoft.emp.impl.graph.*;
import com.tinkerpop.blueprints.*;
import java.util.*;
import com.canopus.mw.events.*;
import com.kpisoft.emp.event.param.*;
import com.kpisoft.emp.vo.*;
import com.canopus.mw.dto.*;
import org.slf4j.*;

public class EmployeeManager extends BaseDomainManager implements CacheLoader<Integer, Employee>
{
    @Autowired
    private EmployeeDataService dataService;
    private Validator validator;
    @Autowired
    @Qualifier("employeeCache")
    private Cache<Integer, Employee> cache;
    @Autowired
    protected IMiddlewareEventClient middlewareEventClient;
    private Graph graph;
    private FramedGraph<Graph> framedGraph;
    private OrganizationRelationship orgRelationship;
    private PositionRelationship positionRelationship;
    private SupervisorRelationship supervisorRelationship;
    private static final Logger log;
    
    public EmployeeManager() {
        this.dataService = null;
        this.validator = null;
        this.cache = null;
        this.graph = null;
        this.framedGraph = null;
        this.orgRelationship = null;
        this.positionRelationship = null;
        this.supervisorRelationship = null;
        this.graph = (Graph)new TinkerGraph();
        this.framedGraph = (FramedGraph<Graph>)new FramedGraph(this.graph);
        this.orgRelationship = new OrganizationRelationship(this.framedGraph);
        this.positionRelationship = new PositionRelationship(this.framedGraph);
        this.supervisorRelationship = new SupervisorRelationship(this.framedGraph);
    }
    
    public void reloadAllGraphs() {
        ExecutionContext.getCurrent().setCrossTenant();
        this.loadFrequentEmployeesToCache();
        this.reloadSupervisorRelationship();
        this.reloadEmpOrgRelationships();
        this.reloadEmployeePositionRelationship();
        ExecutionContext.getCurrent().unSetCrossTenant();
    }
    
    public void loadFrequentEmployeesToCache() {
        try {
            final EmployeeData employeeData = new EmployeeData();
            employeeData.setStartDate((Date)null);
            employeeData.setUsageType(1);
            employeeData.setStatus(1);
            final Request request = new Request();
            request.put(EMPParams.EMP_DATA.name(), (BaseValueObject)employeeData);
            final Response response = this.dataService.loadFrequentEmployeesToCache(request);
            final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.BASEVALUE_LIST.name());
            if (list != null && list.getValueObjectList() != null && !list.getValueObjectList().isEmpty()) {
                final List<EmployeeData> data = (List<EmployeeData>)list.getValueObjectList();
                for (final EmployeeData iterator : data) {
                    final Employee employee = new Employee(this);
                    employee.setEmployeeDetails(iterator);
                    this.cache.put(iterator.getId(), employee);
                }
            }
        }
        catch (Exception e) {
            EmployeeManager.log.error("Exception in EmployeeManager - loadFrequentEmployeesToCache() : " + e.getMessage());
        }
    }
    
    public Employee saveOrUpdateEmployee(final EmployeeData data) {
        final Employee emp = new Employee(this);
        emp.setEmployeeDetails(data);
        int id = 0;
        try {
            id = emp.save();
        }
        catch (Exception e) {
            throw new MiddlewareException("ERR_SAVE_UPDATE_EMPLOYEE", e.getMessage(), (Throwable)e);
        }
        finally {
            if (id > 0) {
                this.cache.remove(id);
            }
        }
        return emp;
    }
    
    public Employee getEmployee(final int empID) {
        final Employee emp = (Employee)this.getCache().get(empID, (CacheLoader)this);
        return emp;
    }
    
    public boolean deleteEmployee(final Integer empId) {
        final Request request = new Request();
        request.put(EMPParams.EMP_ID.name(), (BaseValueObject)new Identifier(empId));
        final Response response = this.dataService.deleteEmployee(request);
        final BooleanResponse status = (BooleanResponse)response.get(EMPParams.BOOLEAN_RESPONSE.name());
        if (status.isResponse()) {
            this.cache.remove(empId);
        }
        return status.isResponse();
    }
    
    public boolean deleteEmployeesByIds(final List<Integer> empIds) {
        final Request request = new Request();
        request.put(EMPParams.EMP_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)empIds));
        final Response response = this.dataService.deleteEmployeesByIds(request);
        final BooleanResponse status = (BooleanResponse)response.get(EMPParams.BOOLEAN_RESPONSE.name());
        if (status.isResponse()) {
            for (final Integer iterator : empIds) {
                this.cache.remove(iterator);
            }
        }
        return status.isResponse();
    }
    
    public boolean suspendEmployeesByIds(final List<Integer> empIds, final Date endDate) {
        final DateResponse date = new DateResponse();
        date.setDate(endDate);
        final Request request = new Request();
        request.put(EMPParams.EMP_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)empIds));
        request.put(EMPParams.END_DATE.name(), (BaseValueObject)date);
        final Response response = this.dataService.suspendEmployeesByIds(request);
        final BooleanResponse status = (BooleanResponse)response.get(EMPParams.BOOLEAN_RESPONSE.name());
        return status.isResponse();
    }
    
    public Employee load(final Integer key) {
        final Request request = new Request();
        request.put(EMPParams.EMP_ID.name(), (BaseValueObject)new Identifier(key));
        final Response response = this.dataService.getEmployee(request);
        final EmployeeData data = (EmployeeData)response.get(EMPParams.EMP_DATA.name());
        final Employee emp = new Employee(this);
        emp.setEmployeeDetails(data);
        return emp;
    }
    
    public List<EmployeeData> search(final EmployeeData employeeData, Page page, SortList sortList) {
        final Request request = new Request();
        request.put(EMPParams.EMP_DATA.name(), (BaseValueObject)employeeData);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.search(request);
        page = response.getPage();
        sortList = response.getSortList();
        final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.BASEVALUE_LIST.name());
        final List<EmployeeData> result = (List<EmployeeData>)list.getValueObjectList();
        return result;
    }
    
    public List<EmployeeData> searchAll(final EmployeeData employeeData, Page page, SortList sortList) {
        final Request request = new Request();
        request.put(EMPParams.EMP_DATA.name(), (BaseValueObject)employeeData);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.searchAll(request);
        page = response.getPage();
        sortList = response.getSortList();
        final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.BASEVALUE_LIST.name());
        final List<EmployeeData> result = (List<EmployeeData>)list.getValueObjectList();
        return result;
    }
    
    public void reloadSupervisorRelationship() {
        try {
            final Response response = this.dataService.getSupervisorRelationships(new Request());
            final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.EMP_SUP_REL_DATA_LIST.name());
            if (list != null) {
                final List<EmployeeSupervisorRelationshipData> result = (List<EmployeeSupervisorRelationshipData>)list.getValueObjectList();
                if (result != null && result.size() != 0) {
                    for (final EmployeeSupervisorRelationshipData iterator : result) {
                        final EmployeeGraph.Employee emp = (EmployeeGraph.Employee)this.supervisorRelationship.add(iterator.getEmployeeId(), (Class)EmployeeGraph.Employee.class);
                        emp.setEmployeeId(iterator.getEmployeeId());
                        final EmployeeGraph.Employee sup = (EmployeeGraph.Employee)this.supervisorRelationship.add(iterator.getSupervisorId(), (Class)EmployeeGraph.Employee.class);
                        sup.setEmployeeId(iterator.getSupervisorId());
                        this.supervisorRelationship.addSupervisor(iterator.getEmployeeId(), iterator.getSupervisorId(), iterator.getPrimary());
                    }
                }
            }
        }
        catch (Exception e) {
            EmployeeManager.log.error("Exception in EmployeeManager - reloadSupervisorRelationship() : " + e.getMessage());
        }
    }
    
    public List<EmployeeSupervisorRelationshipData> getSupervisorRelationships() {
        final Response response = this.dataService.getSupervisorRelationships(new Request());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.EMP_SUP_REL_DATA_LIST.name());
        final List<EmployeeSupervisorRelationshipData> result = (List<EmployeeSupervisorRelationshipData>)list.getValueObjectList();
        return result;
    }
    
    public List<EmployeeSupervisorRelationshipData> getAllSupervisorRelationships() {
        final Response response = this.dataService.getAllSupervisorRelationships(new Request());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.EMP_SUP_REL_DATA_LIST.name());
        final List<EmployeeSupervisorRelationshipData> result = (List<EmployeeSupervisorRelationshipData>)list.getValueObjectList();
        return result;
    }
    
    public void addSupervisorInGraph(final EmployeeSupervisorRelationshipData data) {
        final EmployeeGraph.Employee emp = (EmployeeGraph.Employee)this.supervisorRelationship.add(data.getEmployeeId(), (Class)EmployeeGraph.Employee.class);
        emp.setEmployeeId(data.getEmployeeId());
        final EmployeeGraph.Employee sup = (EmployeeGraph.Employee)this.supervisorRelationship.add(data.getSupervisorId(), (Class)EmployeeGraph.Employee.class);
        sup.setEmployeeId(data.getSupervisorId());
        this.supervisorRelationship.addSupervisor(data.getEmployeeId(), data.getSupervisorId(), data.getPrimary());
    }
    
    public Integer addSupervisor(final EmployeeSupervisorRelationshipData data) {
        try {
            final Request request = new Request();
            request.put(EMPParams.EMP_SUP_REL_DATA.name(), (BaseValueObject)data);
            final Response response = this.dataService.addSupervisor(request);
            final Identifier identifier = (Identifier)response.get(EMPParams.EMP_SUP_REL_ID.name());
            this.addSupervisorInGraph(data);
            return identifier.getId();
        }
        catch (Exception ex) {
            EmployeeManager.log.error("Exception in EmployeeManager - addSupervisor() : " + ex);
            throw new MiddlewareException("", "Failed to add employee supervisor relationship", (Throwable)ex);
        }
    }
    
    public void removeSupervisorFromGraph(final Integer employeeId, final Integer supervisorId) {
        final Vertex vertex1 = this.framedGraph.getBaseGraph().getVertex((Object)employeeId);
        final Vertex vertex2 = this.framedGraph.getBaseGraph().getVertex((Object)supervisorId);
        if (vertex1 != null && vertex2 != null) {
            this.supervisorRelationship.removeSupervisor(employeeId, supervisorId);
        }
    }
    
    public boolean removeSupervisor(final EmployeeSupervisorRelationshipData data) {
        try {
            final Request request = new Request();
            request.put(EMPParams.EMP_SUP_REL_DATA.name(), (BaseValueObject)data);
            final Response response = this.dataService.removeSupervisor(request);
            final BooleanResponse status = (BooleanResponse)response.get(EMPParams.BOOLEAN_RESPONSE.name());
            if (status.isResponse()) {
                this.removeSupervisorFromGraph(data.getEmployeeId(), data.getSupervisorId());
            }
            return status.isResponse();
        }
        catch (Exception ex) {
            EmployeeManager.log.error("Exception in EmployeeManager - removeSupervisor() : " + ex.getMessage());
            throw new MiddlewareException("Failed to remove employee position relationship", ex.getMessage());
        }
    }
    
    public boolean suspendSupervisorRelationships(final List<EmployeeSupervisorRelationshipData> relList) {
        final BaseValueObjectList list = new BaseValueObjectList();
        list.setValueObjectList((List)relList);
        final Request request = new Request();
        request.put(EMPParams.EMP_SUP_REL_DATA_LIST.name(), (BaseValueObject)list);
        final Response response = this.dataService.suspendSupervisorRelationships(request);
        final BooleanResponse status = (BooleanResponse)response.get(EMPParams.STATUS_RESPONSE.name());
        return status.isResponse();
    }
    
    public boolean suspendSupervisorRelationshipsByEmployeeIds(final List<Integer> empIds, final Date endDate, final boolean delete) {
        final DateResponse date = new DateResponse();
        date.setDate(endDate);
        final Request request = new Request();
        request.put(EMPParams.EMP_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)empIds));
        request.put(EMPParams.END_DATE.name(), (BaseValueObject)date);
        request.put(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(delete));
        final Response response = this.dataService.suspendSupervisorRelationshipsByEmployeeIds(request);
        final BooleanResponse status = (BooleanResponse)response.get(EMPParams.STATUS_RESPONSE.name());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.EMP_SUP_REL_DATA_LIST.name());
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
                final List<EmployeeSupervisorRelationshipData> relations = (List<EmployeeSupervisorRelationshipData>)list.getValueObjectList();
                if (relations != null && !relations.isEmpty()) {
                    for (final EmployeeSupervisorRelationshipData iterator : relations) {
                        this.removeSupervisorFromGraph(iterator.getEmployeeId(), iterator.getSupervisorId());
                    }
                }
            }
            else {
                final MiddlewareEvent event = new MiddlewareEvent();
                event.setEventType(EmployeeGraphEvents.SUSPEND_SUP_REL_EVENT.getEventId());
                event.setPayLoad(EMPParams.EMP_SUP_REL_DATA_LIST.name(), (BaseValueObject)list);
                event.setScheduleDate(endDateCalendar.getTime());
                this.middlewareEventClient.sendToScheduler(event);
            }
        }
        return status.isResponse();
    }
    
    public void reloadEmployeePositionRelationship() {
        try {
            final Response response = this.dataService.getEmployeePositionRelationships(new Request());
            final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.EMP_POSITION_DATA_LIST.name());
            if (list != null) {
                final List<EmployeePositionData> result = (List<EmployeePositionData>)list.getValueObjectList();
                if (result != null && !result.isEmpty()) {
                    for (final EmployeePositionData iterator : result) {
                        final EmployeeGraph.Employee emp = (EmployeeGraph.Employee)this.positionRelationship.add(iterator.getEmployeeId(), (Class)EmployeeGraph.Employee.class);
                        emp.setEmployeeId(iterator.getEmployeeId());
                        final EmployeeGraph.Position position = (EmployeeGraph.Position)this.positionRelationship.add(iterator.getPositionId(), (Class)EmployeeGraph.Position.class);
                        position.setPositionId(iterator.getPositionId());
                        this.positionRelationship.addEmployeePosition(iterator.getEmployeeId(), iterator.getPositionId());
                    }
                }
            }
        }
        catch (Exception e) {
            EmployeeManager.log.error("Exception in EmployeeManager - reloadEmployeePositionRelationship() : " + e.getMessage());
        }
    }
    
    public void addEmployeePositionInGraph(final EmployeePositionData data) {
        final EmployeeGraph.Employee emp = (EmployeeGraph.Employee)this.positionRelationship.add(data.getEmployeeId(), (Class)EmployeeGraph.Employee.class);
        emp.setEmployeeId(data.getEmployeeId());
        final EmployeeGraph.Position position = (EmployeeGraph.Position)this.positionRelationship.add(data.getPositionId(), (Class)EmployeeGraph.Position.class);
        position.setPositionId(data.getPositionId());
        this.positionRelationship.addEmployeePosition(data.getEmployeeId(), data.getPositionId());
    }
    
    public Integer addEmployeePosition(final EmployeePositionData data) {
        try {
            final Request request = new Request();
            request.put(EMPParams.EMP_POSITION_DATA.name(), (BaseValueObject)data);
            final Response response = this.dataService.addEmployeePosition(request);
            final Identifier identifier = (Identifier)response.get(EMPParams.EMP_POSITION_ID.name());
            this.addEmployeePositionInGraph(data);
            return identifier.getId();
        }
        catch (Exception ex) {
            EmployeeManager.log.error("Exception in EmployeeManager - addEmployeePosition() : " + ex.getMessage());
            throw new MiddlewareException("Failed to add employee position relationship", ex.getMessage());
        }
    }
    
    public void removeEmployeePositionFromGraph(final Integer employeeId, final Integer positionId) {
        final Vertex vertex = this.framedGraph.getBaseGraph().getVertex((Object)employeeId);
        if (vertex != null) {
            this.positionRelationship.removeEmployeePosition(employeeId, positionId);
        }
    }
    
    public boolean removeEmployeePosition(final EmployeePositionData data) {
        try {
            final Request request = new Request();
            request.put(EMPParams.EMP_POSITION_DATA.name(), (BaseValueObject)data);
            final Response response = this.dataService.removeEmployeePosition(request);
            final BooleanResponse status = (BooleanResponse)response.get(EMPParams.BOOLEAN_RESPONSE.name());
            if (status.isResponse()) {
                this.removeEmployeePositionFromGraph(data.getEmployeeId(), data.getPositionId());
            }
            return status.isResponse();
        }
        catch (Exception ex) {
            EmployeeManager.log.error("Exception in EmployeeManager - removeEmployeePosition() : " + ex.getMessage());
            throw new MiddlewareException("Failed to remove employee position relationship", ex.getMessage());
        }
    }
    
    public List<EmployeePositionData> getEmployeePositionRelationships() {
        final Response response = this.dataService.getEmployeePositionRelationships(new Request());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.EMP_POSITION_DATA_LIST.name());
        final List<EmployeePositionData> result = (List<EmployeePositionData>)list.getValueObjectList();
        return result;
    }
    
    public List<EmployeePositionData> getAllEmployeePositionRelationships() {
        final Response response = this.dataService.getAllEmployeePositionRelationships(new Request());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.EMP_POSITION_DATA_LIST.name());
        final List<EmployeePositionData> result = (List<EmployeePositionData>)list.getValueObjectList();
        return result;
    }
    
    public boolean suspendEmpPositionRelationships(final List<EmployeePositionData> data) {
        final BaseValueObjectList list = new BaseValueObjectList();
        list.setValueObjectList((List)data);
        final Request request = new Request();
        request.put(EMPParams.EMP_POSITION_DATA_LIST.name(), (BaseValueObject)list);
        final Response response = this.dataService.suspendEmpPositionRelationships(request);
        final BooleanResponse status = (BooleanResponse)response.get(EMPParams.STATUS_RESPONSE.name());
        return status.isResponse();
    }
    
    public boolean suspendEmpPositionRelationshipsByEmployeeIds(final List<Integer> empIds, final Date endDate, final boolean delete) {
        final DateResponse date = new DateResponse();
        date.setDate(endDate);
        final Request request = new Request();
        request.put(EMPParams.EMP_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)empIds));
        request.put(EMPParams.END_DATE.name(), (BaseValueObject)date);
        request.put(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(delete));
        final Response response = this.dataService.suspendEmpPositionRelationshipsByEmployeeIds(request);
        final BooleanResponse status = (BooleanResponse)response.get(EMPParams.STATUS_RESPONSE.name());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.EMP_POSITION_DATA_LIST.name());
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
                final List<EmployeePositionData> relations = (List<EmployeePositionData>)list.getValueObjectList();
                if (relations != null && !relations.isEmpty()) {
                    for (final EmployeePositionData iterator : relations) {
                        this.removeEmployeePositionFromGraph(iterator.getEmployeeId(), iterator.getPositionId());
                    }
                }
            }
            else {
                final MiddlewareEvent event = new MiddlewareEvent();
                event.setEventType(EmployeeGraphEvents.SUSPEND_EMP_POS_REL_EVENT.getEventId());
                event.setPayLoad(EMPParams.EMP_POSITION_DATA_LIST.name(), (BaseValueObject)list);
                event.setScheduleDate(endDateCalendar.getTime());
                this.middlewareEventClient.sendToScheduler(event);
            }
        }
        return status.isResponse();
    }
    
    public void reloadEmpOrgRelationships() {
        try {
            final Response response = this.dataService.getEmployeeOrganizationRelationships(new Request());
            final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.EMP_ORG_REL_DATA_LIST.name());
            if (list != null) {
                final List<EmployeeOrgRelationshipData> result = (List<EmployeeOrgRelationshipData>)list.getValueObjectList();
                if (result != null && result.size() != 0) {
                    for (final EmployeeOrgRelationshipData iterator : result) {
                        final EmployeeGraph.Employee emp = (EmployeeGraph.Employee)this.orgRelationship.add(iterator.getEmployeeId(), (Class)EmployeeGraph.Employee.class);
                        emp.setEmployeeId(iterator.getEmployeeId());
                        final EmployeeGraph.Organization org = (EmployeeGraph.Organization)this.orgRelationship.add(iterator.getOrganizationId(), (Class)EmployeeGraph.Organization.class);
                        org.setOrganizationId(iterator.getOrganizationId());
                        org.setHod(iterator.getHod());
                        this.orgRelationship.addEmployeeOrganization(iterator.getEmployeeId(), iterator.getOrganizationId());
                    }
                }
            }
        }
        catch (Exception e) {
            EmployeeManager.log.error("Exception in EmployeeManager - reloadEmpOrgRelationships() : " + e.getMessage());
        }
    }
    
    public void addEmployeeOrganizationInGraph(final EmployeeOrgRelationshipData data) {
        final EmployeeGraph.Employee emp = (EmployeeGraph.Employee)this.orgRelationship.add(data.getEmployeeId(), (Class)EmployeeGraph.Employee.class);
        emp.setEmployeeId(data.getEmployeeId());
        final EmployeeGraph.Organization org = (EmployeeGraph.Organization)this.orgRelationship.add(data.getOrganizationId(), (Class)EmployeeGraph.Organization.class);
        org.setOrganizationId(data.getOrganizationId());
        org.setHod(data.getHod());
        this.orgRelationship.addEmployeeOrganization(data.getEmployeeId(), data.getOrganizationId());
    }
    
    public Integer addEmployeeOrganization(final EmployeeOrgRelationshipData data) {
        try {
            final Request request = new Request();
            request.put(EMPParams.EMP_ORG_REL_DATA.name(), (BaseValueObject)data);
            final Response response = this.dataService.addEmployeeOrganization(request);
            final Identifier identifier = (Identifier)response.get(EMPParams.EMP_ORG_REL_ID.name());
            this.addEmployeeOrganizationInGraph(data);
            return identifier.getId();
        }
        catch (Exception ex) {
            EmployeeManager.log.error("Exception in EmployeeManager - addEmployeeOrganization() : " + ex.getMessage());
            throw new MiddlewareException("Failed to add employee organization relationship", ex.getMessage());
        }
    }
    
    public void removeEmployeeOrganizationFromGraph(final Integer employeeId, final Integer orgId) {
        final Vertex vertex = this.framedGraph.getBaseGraph().getVertex((Object)employeeId);
        if (vertex != null) {
            this.orgRelationship.removeEmployeeOrganization(employeeId, orgId);
        }
    }
    
    public boolean removeEmployeeOrganization(final EmployeeOrgRelationshipData data) {
        try {
            final Request request = new Request();
            request.put(EMPParams.EMP_ORG_REL_DATA.name(), (BaseValueObject)data);
            final Response response = this.dataService.removeEmployeeOrganization(request);
            final BooleanResponse status = (BooleanResponse)response.get(EMPParams.BOOLEAN_RESPONSE.name());
            if (status.isResponse()) {
                this.removeEmployeeOrganizationFromGraph(data.getEmployeeId(), data.getOrganizationId());
            }
            return status.isResponse();
        }
        catch (Exception ex) {
            EmployeeManager.log.error("Exception in EmployeeManager - removeEmployeeOrganization() : " + ex.getMessage());
            throw new MiddlewareException("", "Failed to remove employee organization relationship", (Throwable)ex);
        }
    }
    
    public List<EmployeeOrgRelationshipData> getEmployeeOrganizationRelationships() {
        final Response response = this.dataService.getEmployeeOrganizationRelationships(new Request());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.EMP_ORG_REL_DATA_LIST.name());
        final List<EmployeeOrgRelationshipData> result = (List<EmployeeOrgRelationshipData>)list.getValueObjectList();
        return result;
    }
    
    public List<EmployeeOrgRelationshipData> getAllEmployeeOrganizationRelationships() {
        final Response response = this.dataService.getAllEmployeeOrganizationRelationships(new Request());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.EMP_ORG_REL_DATA_LIST.name());
        final List<EmployeeOrgRelationshipData> result = (List<EmployeeOrgRelationshipData>)list.getValueObjectList();
        return result;
    }
    
    public boolean suspendEmpOrgRelationships(final List<EmployeeOrgRelationshipData> data) {
        final BaseValueObjectList list = new BaseValueObjectList();
        list.setValueObjectList((List)data);
        final Request request = new Request();
        request.put(EMPParams.EMP_ORG_REL_DATA_LIST.name(), (BaseValueObject)list);
        final Response response = this.dataService.suspendEmpOrgRelationships(request);
        final BooleanResponse status = (BooleanResponse)response.get(EMPParams.STATUS_RESPONSE.name());
        return status.isResponse();
    }
    
    public boolean suspendEmpOrgRelationshipsByEmployeeIds(final List<Integer> empIds, final Date endDate, final boolean delete) {
        final DateResponse date = new DateResponse();
        date.setDate(endDate);
        final Request request = new Request();
        request.put(EMPParams.EMP_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)empIds));
        request.put(EMPParams.END_DATE.name(), (BaseValueObject)date);
        request.put(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(delete));
        final Response response = this.dataService.suspendEmpOrgRelationshipsByEmployeeIds(request);
        final BooleanResponse status = (BooleanResponse)response.get(EMPParams.STATUS_RESPONSE.name());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.EMP_ORG_REL_DATA_LIST.name());
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
                final List<EmployeeOrgRelationshipData> relations = (List<EmployeeOrgRelationshipData>)list.getValueObjectList();
                if (relations != null && !relations.isEmpty()) {
                    for (final EmployeeOrgRelationshipData iterator : relations) {
                        this.removeEmployeeOrganizationFromGraph(iterator.getEmployeeId(), iterator.getOrganizationId());
                    }
                }
            }
            else {
                final MiddlewareEvent event = new MiddlewareEvent();
                event.setEventType(EmployeeGraphEvents.SUSPEND_EMP_ORG_REL_EVENT.getEventId());
                event.setPayLoad(EMPParams.EMP_ORG_REL_DATA_LIST.name(), (BaseValueObject)list);
                event.setScheduleDate(endDateCalendar.getTime());
                this.middlewareEventClient.sendToScheduler(event);
            }
        }
        return status.isResponse();
    }
    
    public List<Integer> getOrganizationListEmployees(final List<Integer> orgIdList) {
        final List<Integer> result = this.orgRelationship.getOrganizationListEmployees(orgIdList);
        return result;
    }
    
    public List<Integer> getOrganizationEmployees(final Integer orgId) {
        final List<Integer> result = this.orgRelationship.getOrganizationEmployees(orgId);
        return result;
    }
    
    public boolean employeeResignation(final int empPositionId, final Date endDate) {
        final DateResponse date = new DateResponse();
        date.setDate(endDate);
        final Request request = new Request();
        request.put(EMPParams.EMP_POSITION_ID.name(), (BaseValueObject)new Identifier(empPositionId));
        request.put(EMPParams.POSITION_END_DATE.name(), (BaseValueObject)date);
        final Response response = this.dataService.employeeResignation(request);
        final BooleanResponse status = (BooleanResponse)response.get(EMPParams.BOOLEAN_RESPONSE.name());
        return status.isResponse();
    }
    
    public boolean transferEmployee(final EmployeePositionData oldPosition, final EmployeePositionData newPosition) {
        final Request request = new Request();
        request.put(EMPParams.EMP_OLD_POSITION_DATA.name(), (BaseValueObject)oldPosition);
        request.put(EMPParams.EMP_POSITION_DATA.name(), (BaseValueObject)newPosition);
        final Response response = this.dataService.transferEmployee(request);
        final BooleanResponse status = (BooleanResponse)response.get(EMPParams.BOOLEAN_RESPONSE.name());
        return status.isResponse();
    }
    
    public EmployeeDataService getDataService() {
        return this.dataService;
    }
    
    public void setDataService(final EmployeeDataService svc) {
        this.dataService = svc;
    }
    
    public Validator getValidator() {
        return this.validator;
    }
    
    public void setValidator(final Validator validator) {
        this.validator = validator;
    }
    
    public Cache<Integer, Employee> getCache() {
        return this.cache;
    }
    
    public void setCache(final Cache<Integer, Employee> cache) {
        this.cache = cache;
    }
    
    public FramedGraph<Graph> getFramedGraph() {
        return this.framedGraph;
    }
    
    public void setFramedGraph(final FramedGraph<Graph> framedGraph) {
        this.framedGraph = framedGraph;
    }
    
    public OrganizationRelationship getOrgRelationship() {
        return this.orgRelationship;
    }
    
    public void setOrgRelationship(final OrganizationRelationship orgRelationship) {
        this.orgRelationship = orgRelationship;
    }
    
    public PositionRelationship getPositionRelationship() {
        return this.positionRelationship;
    }
    
    public void setPositionRelationship(final PositionRelationship positionRelationship) {
        this.positionRelationship = positionRelationship;
    }
    
    public SupervisorRelationship getSupervisorRelationship() {
        return this.supervisorRelationship;
    }
    
    public void setSupervisorRelationship(final SupervisorRelationship supervisorRelationship) {
        this.supervisorRelationship = supervisorRelationship;
    }
    
    public CategoryData getEmpCategory(final Integer EmpCategoryId) {
        final Request request = new Request();
        final Identifier id = new Identifier(EmpCategoryId);
        request.put(EMPParams.CATEGORY_ID.name(), (BaseValueObject)id);
        final Response response = this.dataService.getCategory(request);
        final CategoryData data = (CategoryData)response.get(EMPParams.CATEGORY_DATA.name());
        return data;
    }
    
    public List<CategoryData> getAllEmpCategories(SortList sortList) {
        final Request request = new Request();
        request.setSortList(sortList);
        final Response response = this.dataService.getAllCategories(request);
        sortList = response.getSortList();
        final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.CATEGORY_TYPE_DATA_LIST.name());
        final List<CategoryData> empcategorylist = (List<CategoryData>)list.getValueObjectList();
        return empcategorylist;
    }
    
    public Integer createEmployeeCategory(final CategoryData objCategoryData) {
        final Employee objEmployee = new Employee(this);
        final Integer employmentCategoryId = objEmployee.createEmployeeCategory(objCategoryData);
        return employmentCategoryId;
    }
    
    public boolean updateEmployeeCategory(final CategoryData objCategoryData) {
        final Employee objEmployee = new Employee(this);
        final boolean isUpdate = objEmployee.updateEmployeeCategory(objCategoryData);
        return isUpdate;
    }
    
    public boolean deleteCategory(final Integer id) {
        final Employee objEmployee = new Employee(this);
        final boolean isUpdate = objEmployee.deleteCategory(id);
        return isUpdate;
    }
    
    public Double getEmployeesCount() {
        final Response response = this.dataService.getEmployeesCount(new Request());
        final DoubleIdentifier doubleIdentifier = (DoubleIdentifier)response.get(EMPParams.EMP_COUNT.name());
        return doubleIdentifier.getId();
    }
    
    public BaseValueObjectMap getUsersIdEmailFromEmployeeIdList(final IdentifierList idList) {
        final Request request = new Request();
        request.put(EMPParams.EMP_ID_LIST.name(), (BaseValueObject)idList);
        final Response response = this.getDataService().getUsersIdEmailFromEmployeeIdList(request);
        final BaseValueObjectMap map = (BaseValueObjectMap)response.get(EMPParams.USER_DATA_ID_EMAIL.name());
        return map;
    }
    
    public List<EmployeeData> getEmployeesByIds(final IdentifierList idList) {
        final Request request = new Request();
        request.put(EMPParams.EMP_ID_LIST.name(), (BaseValueObject)idList);
        final Response response = this.dataService.getEmployeesByIds(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.EMP_DATA_LIST.name());
        final List<EmployeeData> empList = (List<EmployeeData>)list.getValueObjectList();
        return empList;
    }
    
    public List<EmployeeData> getEmployeesForOrgId(final List<Integer> orgIds) {
        final Request request = new Request();
        request.put(EMPParams.ORG_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)orgIds));
        final Response response = this.dataService.getEmployeesForOrgId(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(EMPParams.EMP_DATA_LIST.name());
        final List<EmployeeData> result = (List<EmployeeData>)list.getValueObjectList();
        return result;
    }
    
    static {
        log = LoggerFactory.getLogger((Class)EmployeeManager.class);
    }
}
