package com.kpisoft.org.domain;

import org.springframework.stereotype.*;
import com.kpisoft.org.dac.*;
import javax.validation.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.frames.*;
import com.tinkerpop.blueprints.impls.tg.*;
import com.kpisoft.org.graph.*;
import javax.annotation.*;
import com.canopus.mw.*;
import java.util.*;
import com.canopus.mw.dto.*;
import com.kpisoft.org.vo.*;
import org.slf4j.*;

@Component
@Deprecated
public class PositionManager extends BaseDomainManager implements CacheLoader<Integer, Position>
{
    @Autowired
    private PositionDataService dataService;
    @Autowired
    private Validator validator;
    @Autowired
    @Qualifier("positionCache")
    private Cache<Integer, Position> cache;
    private Graph graph;
    private FramedGraph<Graph> framedGraph;
    private OrgPositionParentRelationship parentRelationship;
    private static final Logger log;
    
    public PositionManager() {
        this.dataService = null;
        this.validator = null;
        this.cache = null;
        this.graph = (Graph)new TinkerGraph();
        this.framedGraph = (FramedGraph<Graph>)new FramedGraph(this.graph);
        this.parentRelationship = new OrgPositionParentRelationship(this.framedGraph);
    }
    
    @PostConstruct
    public void reloadPositionGraph() {
        ExecutionContext.getCurrent().setCrossTenant();
        try {
            final Response response = this.dataService.getParentRelationships(new Request());
            final BaseValueObjectList list = (BaseValueObjectList)response.get(PositionParams.POSITION_REL_DATA_LIST.name());
            if (list != null && list.getValueObjectList() != null && !list.getValueObjectList().isEmpty()) {
                final List<PositionRelationshipData> result = (List<PositionRelationshipData>)list.getValueObjectList();
                if (result != null && !result.isEmpty()) {
                    for (final PositionRelationshipData iterator : result) {
                        final PositionGraph.Position child = (PositionGraph.Position)this.parentRelationship.add(iterator.getSourceId().getId(), (Class)PositionGraph.Position.class);
                        child.setPositionId(iterator.getSourceId().getId());
                        final PositionGraph.Position parent = (PositionGraph.Position)this.parentRelationship.add(iterator.getDestinationId().getId(), (Class)PositionGraph.Position.class);
                        parent.setPositionId(iterator.getDestinationId().getId());
                        this.parentRelationship.addParent(iterator);
                    }
                }
            }
        }
        catch (Exception e) {
            PositionManager.log.error("Exception in PositionManager - reloadPositionGraph() : " + e.getMessage());
        }
        ExecutionContext.getCurrent().unSetCrossTenant();
    }
    
    public Position createPosition(final PositionData data) {
        final Position position = new Position(this);
        position.setPositionData(data);
        final int id = position.save();
        final Position pos = (Position)this.cache.get(id, (CacheLoader)this);
        if (pos != null) {
            this.cache.remove(id);
        }
        return position;
    }
    
    public List<Integer> createPositions(final List<PositionData> data) {
        if (data == null) {
            throw new MiddlewareException("POS-000", "Failed to create positions");
        }
        final BaseValueObjectList list = new BaseValueObjectList();
        list.setValueObjectList((List)data);
        final Request request = new Request();
        request.put(PositionParams.POSITION_DATA_LIST.name(), (BaseValueObject)list);
        final Response response = this.dataService.createPositions(request);
        final IdentifierList result = (IdentifierList)response.get(PositionParams.POSITION_IDENTIFIER_LIST.name());
        return (List<Integer>)result.getIdsList();
    }
    
    public Position getPosition(final int id) {
        final Position position = (Position)this.getCache().get(id, (CacheLoader)this);
        return position;
    }
    
    public List<PositionData> getAllPositions(Page page, SortList sortList) {
        final Request request = new Request();
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.getAllPositions(request);
        page = response.getPage();
        sortList = response.getSortList();
        List<PositionData> result = new ArrayList<PositionData>();
        final BaseValueObjectList list = (BaseValueObjectList)response.get(PositionParams.POSITION_DATA_LIST.name());
        if (list != null && list.getValueObjectList() != null && !list.getValueObjectList().isEmpty()) {
            result = (List<PositionData>)list.getValueObjectList();
        }
        return result;
    }
    
    public boolean deletePosition(final int id) {
        final Request request = new Request();
        request.put(PositionParams.POSITION_IDENTIFIER.name(), (BaseValueObject)new Identifier(id));
        final Response response = this.dataService.deletePosition(request);
        final BooleanResponse status = (BooleanResponse)response.get(PositionParams.STATUS.name());
        if (status.isResponse()) {
            final Position pos = (Position)this.cache.get(id, (CacheLoader)this);
            if (pos != null) {
                this.cache.remove(id);
            }
        }
        return status.isResponse();
    }
    
    public Position load(final Integer key) {
        PositionData data = null;
        final Request request = new Request();
        request.put(PositionParams.POSITION_IDENTIFIER.name(), (BaseValueObject)new Identifier(key));
        try {
            final Response response = this.dataService.getPosition(request);
            data = (PositionData)response.get(PositionParams.POSITION_DATA.name());
        }
        catch (Exception e) {
            PositionManager.log.error("Exception in PositionManager - load() : " + e.getMessage());
        }
        final Position position = new Position(this);
        position.setPositionData(data);
        return position;
    }
    
    public List<PositionRelationshipData> getParentRelationshipsByPositionId(final Integer positionId) {
        final Request request = new Request();
        request.put(PositionParams.POSITION_IDENTIFIER.name(), (BaseValueObject)new Identifier(positionId));
        final Response response = this.dataService.getParentRelationshipsByPositionId(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(PositionParams.POSITION_REL_DATA_LIST.name());
        final List<PositionRelationshipData> result = (List<PositionRelationshipData>)list.getValueObjectList();
        return result;
    }
    
    public List<PositionRelationshipData> getParentRelationships() {
        final Response response = this.dataService.getParentRelationships(new Request());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(PositionParams.POSITION_REL_DATA_LIST.name());
        final List<PositionRelationshipData> result = (List<PositionRelationshipData>)list.getValueObjectList();
        return result;
    }
    
    public List<PositionRelationshipData> getAllParentRelationships() {
        final Response response = this.dataService.getAllParentRelationships(new Request());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(PositionParams.POSITION_REL_DATA_LIST.name());
        final List<PositionRelationshipData> result = (List<PositionRelationshipData>)list.getValueObjectList();
        return result;
    }
    
    public void addParentInGraph(final PositionRelationshipData data) {
        final PositionGraph.Position child = (PositionGraph.Position)this.parentRelationship.add(data.getSourceId().getId(), (Class)PositionGraph.Position.class);
        child.setPositionId(data.getSourceId().getId());
        final PositionGraph.Position parent = (PositionGraph.Position)this.parentRelationship.add(data.getDestinationId().getId(), (Class)PositionGraph.Position.class);
        parent.setPositionId(data.getDestinationId().getId());
        this.parentRelationship.addParent(data);
    }
    
    public Integer addParent(PositionRelationshipData data) {
        try {
            final Request request = new Request();
            request.put(PositionParams.POSITION_REL_DATA.name(), (BaseValueObject)data);
            final Response response = this.dataService.addParent(request);
            final Identifier identifier = (Identifier)response.get(PositionParams.POSITION_REL_ID.name());
            data.setId(identifier.getId());
            data = (PositionRelationshipData)response.get(PositionParams.POSITION_REL_DATA.name());
            this.addParentInGraph(data);
            return identifier.getId();
        }
        catch (Exception ex) {
            PositionManager.log.error("Exception in PositionManager - addParent() : " + ex.getMessage());
            throw new MiddlewareException("", "Failed to add parent relationship", (Throwable)ex);
        }
    }
    
    public boolean removeParent(final PositionRelationshipData data) {
        try {
            final Request request = new Request();
            request.put(PositionParams.POSITION_REL_DATA.name(), (BaseValueObject)data);
            final Response response = this.dataService.removeParent(request);
            final BooleanResponse status = (BooleanResponse)response.get(PositionParams.STATUS.name());
            if (status.isResponse()) {
                this.parentRelationship.removeParent(data.getSourceId().getId(), data.getDestinationId().getId());
            }
            return status.isResponse();
        }
        catch (Exception ex) {
            PositionManager.log.error("Exception in PositionManager - removeParent() : " + ex.getMessage());
            throw new MiddlewareException("", "Failed to remove parent relationship", (Throwable)ex);
        }
    }
    
    public List<PositionData> searchPosition(final PositionData objPositionData) {
        final Request request = new Request();
        request.put(PositionParams.POSITION_DATA.name(), (BaseValueObject)objPositionData);
        final Response response = this.dataService.searchPosition(request);
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get(PositionParams.POSITION_DATA_LIST.name());
        final List<PositionData> alPositionData = (List<PositionData>)objectList.getValueObjectList();
        return alPositionData;
    }
    
    public PositionDataService getDataService() {
        return this.dataService;
    }
    
    public void setDataService(final PositionDataService dataService) {
        this.dataService = dataService;
    }
    
    public Validator getValidator() {
        return this.validator;
    }
    
    public void setValidator(final Validator validator) {
        this.validator = validator;
    }
    
    public Cache<Integer, Position> getCache() {
        return this.cache;
    }
    
    public void setCache(final Cache<Integer, Position> cache) {
        this.cache = cache;
    }
    
    public FramedGraph<Graph> getFramedGraph() {
        return this.framedGraph;
    }
    
    public void setFramedGraph(final FramedGraph<Graph> framedGraph) {
        this.framedGraph = framedGraph;
    }
    
    public OrgPositionParentRelationship getParentRelationship() {
        return this.parentRelationship;
    }
    
    public void setParentRelationship(final OrgPositionParentRelationship parentRelationship) {
        this.parentRelationship = parentRelationship;
    }
    
    public List<PositionIdentityBean> getAscendantsByIdList(final List<Integer> ids, final int dist) {
        final List<PositionGraph.Position> result = this.parentRelationship.getAscendantsByIdList(ids, dist);
        final List<PositionIdentityBean> identityList = new ArrayList<PositionIdentityBean>();
        if (result != null && !result.isEmpty()) {
            for (final PositionGraph.Position iterator : result) {
                identityList.add(this.toIdentity(iterator));
            }
        }
        return identityList;
    }
    
    public List<PositionIdentityBean> getDescendantsByIdList(final List<Integer> ids, final int dist) {
        final List<PositionGraph.Position> result = this.parentRelationship.getDescendantsByIdList(ids, dist);
        final List<PositionIdentityBean> identityList = new ArrayList<PositionIdentityBean>();
        if (result != null && !result.isEmpty()) {
            for (final PositionGraph.Position iterator : result) {
                identityList.add(this.toIdentity(iterator));
            }
        }
        return identityList;
    }
    
    public PositionIdentityBean toIdentity(final PositionGraph.Position position) {
        final PositionIdentityBean identityBean = new PositionIdentityBean();
        identityBean.setId(position.getPositionId());
        identityBean.setName(position.getName());
        identityBean.setPositionCode(position.getPositionCode());
        return identityBean;
    }
    
    public List<Integer> toIdList(final List<PositionIdentityBean> identityList) {
        final List<Integer> idList = new ArrayList<Integer>();
        if (identityList != null && !identityList.isEmpty()) {
            for (final PositionIdentityBean iterator : identityList) {
                idList.add(iterator.getId());
            }
        }
        return idList;
    }
    
    public Graph getAscendantsGraph(final Integer id, final int distance) {
        final Graph graph = this.parentRelationship.getAscendantsGraph(id, distance);
        return graph;
    }
    
    public Graph getDescendantsGraph(final Integer id, final int distance) {
        final Graph graph = this.parentRelationship.getDescendantsGraph(id, distance);
        return graph;
    }
    
    public Integer getPositionsCount() {
        final Response response = this.dataService.getPositionsCount(new Request());
        final Identifier identifier = (Identifier)response.get(PositionParams.POSITIONS_COUNT.name());
        return identifier.getId();
    }
    
    public Graph getAscendantsGraphByIdList(final List<Integer> ids, final int distance) {
        final Graph graph = this.parentRelationship.getAscendantsGraphByIdList(ids, distance);
        return graph;
    }
    
    public Graph getDescendantsGraphByIdList(final List<Integer> ids, final int distance) {
        final Graph graph = this.parentRelationship.getDescendantsGraphByIdList(ids, distance);
        return graph;
    }
    
    public List<PositionData> search(final PositionData positionData, Page page, SortList sortList) {
        final Request request = new Request();
        request.put(PositionParams.POSITION_DATA.name(), (BaseValueObject)positionData);
        request.setPage(page);
        request.setSortList(sortList);
        final Response response = this.dataService.search(request);
        page = response.getPage();
        sortList = response.getSortList();
        final BaseValueObjectList objectList = (BaseValueObjectList)response.get(PositionParams.POSITION_DATA_LIST.name());
        final List<PositionData> alPositionData = (List<PositionData>)objectList.getValueObjectList();
        return alPositionData;
    }
    
    static {
        log = LoggerFactory.getLogger((Class)PositionManager.class);
    }
}
