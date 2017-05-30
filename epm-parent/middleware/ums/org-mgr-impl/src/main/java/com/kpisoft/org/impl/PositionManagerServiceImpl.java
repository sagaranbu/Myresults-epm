package com.kpisoft.org.impl;

import com.kpisoft.org.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.dac.*;
import com.canopus.mw.*;
import com.kpisoft.org.domain.*;
import java.util.*;
import com.kpisoft.org.graph.*;
import com.kpisoft.org.vo.*;
import com.canopus.mw.dto.*;
import com.tinkerpop.blueprints.*;
import org.slf4j.*;

@Deprecated
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ PositionManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class PositionManagerServiceImpl extends BaseMiddlewareBean implements PositionManagerService
{
    public static String POS_MGR_SERVICE;
    @Autowired
    private PositionManager positionManager;
    private static final Logger log;
    
    public PositionManagerServiceImpl() {
        this.positionManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response createPosition(final Request request) {
        final PositionData data = (PositionData)request.get(PositionParams.POSITION_DATA.getParamName());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        try {
            final Position position = this.getPositionManager().createPosition(data);
            final Identifier identifier = new Identifier();
            identifier.setId(position.getPositionData().getId());
            final List<PositionRelationshipData> result = this.positionManager.getParentRelationshipsByPositionId(identifier.getId());
            if (result != null && !result.isEmpty()) {
                for (final PositionRelationshipData iterator : result) {
                    this.positionManager.addParentInGraph(iterator);
                }
            }
            return this.OK(PositionParams.POSITION_IDENTIFIER.getParamName(), (BaseValueObject)identifier);
        }
        catch (DataAccessException ex) {
            ex.printStackTrace(System.out);
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - createPosition() : " + ex.getMessage());
            return this.ERROR((Exception)ex);
        }
        catch (ValidationException ve) {
            ve.printStackTrace(System.out);
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - createPosition() : " + ve.getMessage());
            return this.ERROR((Exception)ve);
        }
        catch (Exception ex2) {
            ex2.printStackTrace(System.out);
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - createPosition() : " + ex2.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", "Failed to create position", (Throwable)ex2));
        }
    }
    
    public Response createPositions(final Request request) {
        final BaseValueObjectList objectList = (BaseValueObjectList)request.get(PositionParams.POSITION_DATA_LIST.getParamName());
        if (objectList == null || objectList.getValueObjectList() == null || objectList.getValueObjectList().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        final List<PositionData> data = new ArrayList<PositionData>();
        for (final BaseValueObject iterator : objectList.getValueObjectList()) {
            data.add((PositionData)iterator);
        }
        try {
            final List<Integer> positions = this.getPositionManager().createPositions(data);
            if (positions != null && !positions.isEmpty()) {
                for (final Integer identifier : positions) {
                    final List<PositionRelationshipData> result = this.positionManager.getParentRelationshipsByPositionId(identifier);
                    if (result != null && !result.isEmpty()) {
                        for (final PositionRelationshipData iterator2 : result) {
                            this.positionManager.addParentInGraph(iterator2);
                        }
                    }
                }
            }
            return this.OK(PositionParams.POSITION_IDENTIFIER_LIST.getParamName(), (BaseValueObject)new IdentifierList((List)positions));
        }
        catch (DataAccessException ex) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - createPositions() : " + ex.getMessage());
            return this.ERROR((Exception)ex);
        }
        catch (ValidationException ve) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - createPositions() : " + ve.getMessage());
            return this.ERROR((Exception)ve);
        }
        catch (Exception ex2) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - createPositions() : " + ex2.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", "Failed to create positions", (Throwable)ex2));
        }
    }
    
    public Response updatePosition(final Request request) {
        final PositionData data = (PositionData)request.get(PositionParams.POSITION_DATA.getParamName());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        try {
            final Position position = this.getPositionManager().getPosition(data.getId());
            position.setPositionData(data);
            position.save();
            final Identifier identifier = new Identifier();
            identifier.setId(position.getPositionData().getId());
            final Vertex vertex = this.positionManager.getFramedGraph().getBaseGraph().getVertex((Object)identifier.getId());
            if (vertex != null) {
                this.positionManager.getParentRelationship().removeVertex(identifier.getId(), (Class)PositionGraph.Position.class);
            }
            final List<PositionRelationshipData> result = this.positionManager.getParentRelationshipsByPositionId(identifier.getId());
            if (result != null && !result.isEmpty()) {
                for (final PositionRelationshipData iterator : result) {
                    this.positionManager.addParentInGraph(iterator);
                }
            }
            return this.OK(PositionParams.POSITION_IDENTIFIER.getParamName(), (BaseValueObject)identifier);
        }
        catch (DataAccessException ex) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - updatePosition() : " + ex.getMessage());
            return this.ERROR((Exception)ex);
        }
        catch (ValidationException ve) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - updatePosition() : " + ve.getMessage());
            return this.ERROR((Exception)ve);
        }
        catch (Exception ex2) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - updatePosition() : " + ex2.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", "Failed to create position", (Throwable)ex2));
        }
    }
    
    public Response getPosition(final Request request) {
        final Identifier identifier = (Identifier)request.get(PositionParams.POSITION_IDENTIFIER.getParamName());
        if (identifier == null) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        try {
            final Position position = this.getPositionManager().getPosition(identifier.getId());
            final PositionData data = position.getPositionData();
            return this.OK(PositionParams.POSITION_DATA.getParamName(), (BaseValueObject)data);
        }
        catch (DataAccessException ex) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - getPosition() : " + ex.getMessage());
            return this.ERROR((Exception)ex);
        }
        catch (Exception ex2) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - getPosition() : " + ex2.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", "Failed to load position", (Throwable)ex2));
        }
    }
    
    public Response getAllPositions(final Request request) {
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        try {
            final List<PositionData> positions = this.getPositionManager().getAllPositions(page, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)positions);
            final Response response = this.OK(PositionParams.POSITION_DATA_LIST.getParamName(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (DataAccessException ex) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - getAllPositions() : " + ex.getMessage());
            return this.ERROR((Exception)ex);
        }
        catch (Exception ex2) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - getAllPositions() : " + ex2.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", "Failed to load positions", (Throwable)ex2));
        }
    }
    
    public Response deletePosition(final Request request) {
        final Identifier identifier = (Identifier)request.get(PositionParams.POSITION_IDENTIFIER.getParamName());
        if (identifier == null) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        try {
            final boolean status = this.getPositionManager().deletePosition(identifier.getId());
            final Vertex vertex = this.positionManager.getFramedGraph().getBaseGraph().getVertex((Object)identifier.getId());
            if (vertex != null) {
                this.positionManager.getParentRelationship().removeVertex(identifier.getId(), (Class)PositionGraph.Position.class);
            }
            return this.OK(PositionParams.STATUS.getParamName(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (DataAccessException ex) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - deletePosition() : " + ex.getMessage());
            return this.ERROR((Exception)ex);
        }
        catch (Exception ex2) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - deletePosition() : " + ex2.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", "Failed to delete position", (Throwable)ex2));
        }
    }
    
    public Response mergePositions(Request request) {
        final IdentifierList idList = (IdentifierList)request.get(PositionParams.POSITION_IDENTIFIER_LIST.getParamName());
        final PositionData data = (PositionData)request.get(PositionParams.POSITION_DATA.getParamName());
        if (idList == null || data == null) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        try {
            for (final Integer id : idList.getIdsList()) {
                request = new Request();
                request.put(PositionParams.POSITION_IDENTIFIER.name(), (BaseValueObject)new Identifier(id));
                this.deletePosition(request);
            }
            final Position pos = this.getPositionManager().createPosition(data);
            return this.OK(PositionParams.POSITION_DATA.getParamName(), (BaseValueObject)pos.getPositionData());
        }
        catch (DataAccessException ex) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - mergePositions() : " + ex.getMessage());
            return this.ERROR((Exception)ex);
        }
        catch (Exception ex2) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - mergePositions() : " + ex2.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", "Failed to merge positions", (Throwable)ex2));
        }
    }
    
    public Response addParent(final Request request) {
        final PositionRelationshipData data = (PositionRelationshipData)request.get(PositionParams.POSITION_REL_DATA.getParamName());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        try {
            final Integer id = this.getPositionManager().addParent(data);
            data.setId(id);
            return this.OK(PositionParams.POSITION_REL_ID.getParamName(), (BaseValueObject)new Identifier(id));
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - addParent() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", e.getMessage()));
        }
    }
    
    public Response getParents(final Request request) {
        final Identifier identifier = (Identifier)request.get(PositionParams.POSITION_IDENTIFIER.getParamName());
        if (identifier == null) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        try {
            final Position position = this.getPositionManager().getPosition(identifier.getId());
            final List<PositionIdentityBean> result = position.getParents(identifier.getId());
            final List<Integer> idList = position.toIdList(result);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final Response response = this.OK();
            response.put(PositionParams.POSITION_IDENTIFIER_LIST.name(), (BaseValueObject)new IdentifierList((List)idList));
            response.put(PositionParams.POS_IDENTITY_REL_DATA_LIST.name(), (BaseValueObject)list);
            return response;
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - getParents() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", e.getMessage()));
        }
    }
    
    public Response getChildrens(final Request request) {
        final Identifier identifier = (Identifier)request.get(PositionParams.POSITION_IDENTIFIER.getParamName());
        if (identifier == null) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        try {
            final Position position = this.getPositionManager().getPosition(identifier.getId());
            final List<PositionIdentityBean> result = position.getChildrens(identifier.getId());
            final List<Integer> idList = position.toIdList(result);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final Response response = this.OK();
            response.put(PositionParams.POSITION_IDENTIFIER_LIST.name(), (BaseValueObject)new IdentifierList((List)idList));
            response.put(PositionParams.POS_IDENTITY_REL_DATA_LIST.name(), (BaseValueObject)list);
            return response;
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - getChildrens() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", e.getMessage()));
        }
    }
    
    public Response removeParent(final Request request) {
        final PositionRelationshipData data = (PositionRelationshipData)request.get(PositionParams.POSITION_REL_DATA.getParamName());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        try {
            final boolean status = this.getPositionManager().removeParent(data);
            return this.OK(PositionParams.STATUS.getParamName(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - removeParent() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", e.getMessage()));
        }
    }
    
    public Response getAscendants(final Request request) {
        final Identifier identifier = (Identifier)request.get(PositionParams.POSITION_IDENTIFIER.getParamName());
        Identifier dist = (Identifier)request.get(PositionParams.DISTANCE.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        if (dist == null || dist.getId() == null || dist.getId() <= 0) {
            dist = new Identifier(0);
        }
        try {
            final Position position = this.getPositionManager().getPosition(identifier.getId());
            final List<PositionIdentityBean> result = position.getAscendants(identifier.getId(), dist.getId());
            final List<Integer> idList = position.toIdList(result);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final Response response = this.OK();
            response.put(PositionParams.POSITION_IDENTIFIER_LIST.name(), (BaseValueObject)new IdentifierList((List)idList));
            response.put(PositionParams.POS_IDENTITY_REL_DATA_LIST.name(), (BaseValueObject)list);
            return response;
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - getAscendants() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", e.getMessage()));
        }
    }
    
    public Response getAscendantsByIdList(final Request request) {
        final IdentifierList identifier = (IdentifierList)request.get(PositionParams.POSITION_IDENTIFIER_LIST.getParamName());
        Identifier dist = (Identifier)request.get(PositionParams.DISTANCE.getParamName());
        if (identifier == null || identifier.getIdsList() == null || identifier.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        if (dist == null || dist.getId() == null || dist.getId() <= 0) {
            dist = new Identifier(0);
        }
        try {
            final List<PositionIdentityBean> result = this.positionManager.getAscendantsByIdList(identifier.getIdsList(), dist.getId());
            final List<Integer> idList = this.positionManager.toIdList(result);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final Response response = this.OK();
            response.put(PositionParams.POSITION_IDENTIFIER_LIST.name(), (BaseValueObject)new IdentifierList((List)idList));
            response.put(PositionParams.POS_IDENTITY_REL_DATA_LIST.name(), (BaseValueObject)list);
            return response;
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - getAscendants() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", e.getMessage()));
        }
    }
    
    public Response getDescendants(final Request request) {
        final Identifier identifier = (Identifier)request.get(PositionParams.POSITION_IDENTIFIER.getParamName());
        Identifier dist = (Identifier)request.get(PositionParams.DISTANCE.getParamName());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        if (dist == null || dist.getId() == null || dist.getId() <= 0) {
            dist = new Identifier(0);
        }
        try {
            final Position position = this.getPositionManager().getPosition(identifier.getId());
            final List<PositionIdentityBean> result = position.getDescendants(identifier.getId(), dist.getId());
            final List<Integer> idList = position.toIdList(result);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final Response response = this.OK();
            response.put(PositionParams.POSITION_IDENTIFIER_LIST.name(), (BaseValueObject)new IdentifierList((List)idList));
            response.put(PositionParams.POS_IDENTITY_REL_DATA_LIST.name(), (BaseValueObject)list);
            return response;
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - getDescendants() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", e.getMessage()));
        }
    }
    
    public Response getDescendantsByIdList(final Request request) {
        final IdentifierList identifier = (IdentifierList)request.get(PositionParams.POSITION_IDENTIFIER_LIST.getParamName());
        Identifier dist = (Identifier)request.get(PositionParams.DISTANCE.getParamName());
        if (identifier == null || identifier.getIdsList() == null || identifier.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        if (dist == null || dist.getId() == null || dist.getId() <= 0) {
            dist = new Identifier(0);
        }
        try {
            final List<PositionIdentityBean> result = this.positionManager.getDescendantsByIdList(identifier.getIdsList(), dist.getId());
            final List<Integer> idList = this.positionManager.toIdList(result);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final Response response = this.OK();
            response.put(PositionParams.POSITION_IDENTIFIER_LIST.name(), (BaseValueObject)new IdentifierList((List)idList));
            response.put(PositionParams.POS_IDENTITY_REL_DATA_LIST.name(), (BaseValueObject)list);
            return response;
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - getAscendants() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", e.getMessage()));
        }
    }
    
    public Response isAscendant(final Request request) {
        final Identifier parentId = (Identifier)request.get(PositionParams.PARENT_POSITION_ID.getParamName());
        final Identifier childId = (Identifier)request.get(PositionParams.CHILD_POSITION_ID.getParamName());
        if (parentId == null || childId == null) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        try {
            final Position position = this.getPositionManager().getPosition(parentId.getId());
            final boolean status = position.isAscendant(parentId.getId(), childId.getId());
            return this.OK(PositionParams.STATUS.getParamName(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - isAscendant() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", e.getMessage()));
        }
    }
    
    public Response isDescendant(final Request request) {
        final Identifier parentId = (Identifier)request.get(PositionParams.PARENT_POSITION_ID.getParamName());
        final Identifier childId = (Identifier)request.get(PositionParams.CHILD_POSITION_ID.getParamName());
        if (parentId == null || childId == null) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        try {
            final Position position = this.getPositionManager().getPosition(childId.getId());
            final boolean status = position.isDescendant(childId.getId(), parentId.getId());
            return this.OK(PositionParams.STATUS.getParamName(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - isDescendant() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", e.getMessage()));
        }
    }
    
    public Response isParent(final Request request) {
        final Identifier parentId = (Identifier)request.get(PositionParams.PARENT_POSITION_ID.getParamName());
        final Identifier childId = (Identifier)request.get(PositionParams.CHILD_POSITION_ID.getParamName());
        if (parentId == null || childId == null) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        try {
            final Position position = this.getPositionManager().getPosition(parentId.getId());
            final boolean status = position.isParent(parentId.getId(), childId.getId());
            return this.OK(PositionParams.STATUS.getParamName(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - isParent() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", e.getMessage()));
        }
    }
    
    public Response isChild(final Request request) {
        final Identifier parentId = (Identifier)request.get(PositionParams.PARENT_POSITION_ID.getParamName());
        final Identifier childId = (Identifier)request.get(PositionParams.CHILD_POSITION_ID.getParamName());
        if (parentId == null || childId == null) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        try {
            final Position position = this.getPositionManager().getPosition(childId.getId());
            final boolean status = position.isChild(childId.getId(), parentId.getId());
            return this.OK(PositionParams.STATUS.getParamName(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - isChild() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", e.getMessage()));
        }
    }
    
    public Response getParentRelationships(final Request request) {
        try {
            final List<PositionRelationshipData> result = this.positionManager.getParentRelationships();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(PositionParams.POSITION_REL_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - getParentRelationships() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", e.getMessage()));
        }
    }
    
    public Response getAllParentRelationships(final Request request) {
        try {
            final List<PositionRelationshipData> result = this.positionManager.getAllParentRelationships();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(PositionParams.POSITION_REL_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - getAllParentRelationships() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", e.getMessage()));
        }
    }
    
    public Response searchPosition(final Request request) {
        final PositionData objPositionData = (PositionData)request.get(PositionParams.POSITION_DATA.name());
        if (objPositionData == null) {
            throw new MiddlewareException("POS-201", "No Position data found in request");
        }
        try {
            final List<PositionData> alPositionData = this.getPositionManager().searchPosition(objPositionData);
            final BaseValueObjectList objectList = new BaseValueObjectList();
            objectList.setValueObjectList((List)alPositionData);
            return this.OK(PositionParams.POSITION_DATA_LIST.name(), (BaseValueObject)objectList);
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - serachPosition() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("OPERATION_FAILED", "Unable to fetch the position data", new Object[] { e.getMessage() }));
        }
    }
    
    public PositionManager getPositionManager() {
        return this.positionManager;
    }
    
    public void setPositionManager(final PositionManager positionManager) {
        this.positionManager = positionManager;
    }
    
    public Response getAscendantsGraph(final Request request) {
        final Identifier identifier = (Identifier)request.get(PositionParams.POSITION_IDENTIFIER.name());
        Identifier dist = (Identifier)request.get(PositionParams.DISTANCE.name());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "Invalid input in request"));
        }
        if (dist == null || dist.getId() == null || dist.getId() <= 0) {
            dist = new Identifier(0);
        }
        try {
            final Graph graph = this.positionManager.getAscendantsGraph(identifier.getId(), dist.getId());
            final GraphResponse graphResponse = new GraphResponse();
            graphResponse.setGraph(graph);
            return this.OK(PositionParams.POSITION_GRAPH.name(), (BaseValueObject)graphResponse);
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - getAscendantsGraph() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", e.getMessage()));
        }
    }
    
    public Response getDescendantsGraph(final Request request) {
        final Identifier identifier = (Identifier)request.get(PositionParams.POSITION_IDENTIFIER.name());
        Identifier dist = (Identifier)request.get(PositionParams.DISTANCE.name());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "Invalid input in request"));
        }
        if (dist == null || dist.getId() == null || dist.getId() <= 0) {
            dist = new Identifier(0);
        }
        try {
            final Graph graph = this.positionManager.getDescendantsGraph(identifier.getId(), dist.getId());
            final GraphResponse graphResponse = new GraphResponse();
            graphResponse.setGraph(graph);
            return this.OK(PositionParams.POSITION_GRAPH.name(), (BaseValueObject)graphResponse);
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - getDescendantsGraph() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", e.getMessage()));
        }
    }
    
    public Response getPositionsCount(final Request request) {
        try {
            final Integer positionsCount = this.positionManager.getPositionsCount();
            return this.OK(PositionParams.POSITIONS_COUNT.name(), (BaseValueObject)new Identifier(positionsCount));
        }
        catch (Exception ex) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - getPositionsCount() : " + ex.getMessage());
            return this.ERROR((Exception)new MiddlewareException("Failed to get Positions count", ex.getMessage()));
        }
    }
    
    public Response getAscendantsGraphByIdList(final Request request) {
        final IdentifierList identifier = (IdentifierList)request.get(PositionParams.POSITION_IDENTIFIER_LIST.getParamName());
        Identifier dist = (Identifier)request.get(PositionParams.DISTANCE.name());
        if (identifier == null || identifier.getIdsList() == null || identifier.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        if (dist == null || dist.getId() == null || dist.getId() <= 0) {
            dist = new Identifier(0);
        }
        try {
            final Graph graph = this.positionManager.getAscendantsGraphByIdList(identifier.getIdsList(), dist.getId());
            final GraphResponse graphResponse = new GraphResponse();
            graphResponse.setGraph(graph);
            return this.OK(PositionParams.POSITION_GRAPH.name(), (BaseValueObject)graphResponse);
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - getAscendantsGraphByIdList() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", e.getMessage()));
        }
    }
    
    public Response getDescendantsGraphByIdList(final Request request) {
        final IdentifierList identifier = (IdentifierList)request.get(PositionParams.POSITION_IDENTIFIER_LIST.getParamName());
        Identifier dist = (Identifier)request.get(PositionParams.DISTANCE.name());
        if (identifier == null || identifier.getIdsList() == null || identifier.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException("POS-001", "No input data in the request"));
        }
        if (dist == null || dist.getId() == null || dist.getId() <= 0) {
            dist = new Identifier(0);
        }
        try {
            final Graph graph = this.positionManager.getDescendantsGraphByIdList(identifier.getIdsList(), dist.getId());
            final GraphResponse graphResponse = new GraphResponse();
            graphResponse.setGraph(graph);
            return this.OK(PositionParams.POSITION_GRAPH.name(), (BaseValueObject)graphResponse);
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - getDescendantsGraphByIdList() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("POS-000", e.getMessage()));
        }
    }
    
    public Response search(final Request request) {
        final PositionData positionData = (PositionData)request.get(PositionParams.POSITION_DATA.name());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        if (positionData == null) {
            return this.ERROR((Exception)new MiddlewareException("POS-201", "No Position data found in request"));
        }
        try {
            final List<PositionData> positionsList = this.getPositionManager().search(positionData, page, sortList);
            final BaseValueObjectList baseValObjList = new BaseValueObjectList();
            baseValObjList.setValueObjectList((List)positionsList);
            final Response response = this.OK(PositionParams.POSITION_DATA_LIST.name(), (BaseValueObject)baseValObjList);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            PositionManagerServiceImpl.log.error("Exception in PositionManagerServiceImpl - search() : " + e.getMessage());
            return this.ERROR((Exception)new MiddlewareException("OPERATION_FAILED", "Unable to fetch the position data by search", new Object[] { e.getMessage() }));
        }
    }
    
    static {
        PositionManagerServiceImpl.POS_MGR_SERVICE = "PositionManagerService";
        log = LoggerFactory.getLogger((Class)PositionManagerServiceImpl.class);
    }
}
