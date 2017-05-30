package com.kpisoft.org.dac.impl;

import com.kpisoft.org.dac.*;
import org.springframework.stereotype.*;
import com.kpisoft.org.dac.dao.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.dac.hibernate.*;
import com.canopus.dac.*;
import java.io.*;
import com.kpisoft.org.dac.impl.domain.*;
import org.springframework.transaction.annotation.*;
import java.util.*;
import com.canopus.dac.utils.*;
import com.canopus.mw.dto.*;
import org.modelmapper.*;
import com.googlecode.genericdao.search.*;
import java.lang.reflect.*;
import com.kpisoft.org.vo.*;
import com.canopus.mw.*;
import org.slf4j.*;

@Component
@Deprecated
public class PositionDataServiceImpl extends BaseDataAccessService implements PositionDataService
{
    @Autowired
    private PositionDao positionDao;
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public PositionDataServiceImpl() {
        this.positionDao = null;
        this.genericDao = null;
        this.modelMapper = null;
        this.modelMapper = new ModelMapper();
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    @Transactional
    public Response createPosition(final Request request) {
        final PositionData data = (PositionData)request.get(PositionParams.POSITION_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_DATA", "No input data in the request"));
        }
        int oldParentId = 0;
        Position position = null;
        if (data.getId() != null && data.getId() > 0) {
            position = (Position)this.positionDao.find((Serializable)data.getId());
            if (position.getParentId() != null && position.getParentId() > 0) {
                oldParentId = position.getParentId();
            }
        }
        else {
            position = new Position();
        }
        try {
            this.modelMapper.map((Object)data, (Object)position);
            if (position.getPosStrData() != null && !position.getPosStrData().isEmpty()) {
                for (final PositionStructureRelationship iterator : position.getPosStrData()) {
                    iterator.setPosition(position);
                }
            }
            if (this.positionDao.isAttached(position)) {
                this.positionDao.merge((Object)position);
            }
            else {
                this.positionDao.save(position);
            }
            if (position.getParentId() != null && position.getParentId() > 0) {
                final Position parentPosition = (Position)this.positionDao.find((Serializable)position.getParentId());
                if (data.getId() == null || data.getId() == 0) {
                    final PositionIdentity sourceIdentity = new PositionIdentity();
                    sourceIdentity.setId(position.getId());
                    sourceIdentity.setName(position.getName());
                    sourceIdentity.setPositionCode(position.getPositionCode());
                    final PositionIdentity destinationIdentity = new PositionIdentity();
                    destinationIdentity.setId(parentPosition.getId());
                    destinationIdentity.setName(parentPosition.getName());
                    destinationIdentity.setPositionCode(parentPosition.getPositionCode());
                    final PositionParentRelationship relData = new PositionParentRelationship();
                    relData.setSourceId(sourceIdentity);
                    relData.setDestinationId(destinationIdentity);
                    this.genericDao.save((Object)relData);
                }
                else if (oldParentId != 0) {
                    final Search search = new Search((Class)PositionParentRelationship.class);
                    search.addFilterEqual("sourceId.id", (Object)position.getId());
                    final List<PositionParentRelationship> result = (List<PositionParentRelationship>)this.genericDao.search((ISearch)search);
                    PositionParentRelationship relData = null;
                    if (result != null && !result.isEmpty()) {
                        for (final PositionParentRelationship iterator2 : result) {
                            if (iterator2.getDestinationId().getId() == oldParentId) {
                                relData = iterator2;
                                break;
                            }
                        }
                        if (relData != null) {
                            relData.setEndDate(new Date());
                            relData.setDeleted(true);
                            this.genericDao.save((Object)relData);
                        }
                    }
                    final PositionIdentity sourceIdentity2 = new PositionIdentity();
                    sourceIdentity2.setId(position.getId());
                    sourceIdentity2.setName(position.getName());
                    sourceIdentity2.setPositionCode(position.getPositionCode());
                    final PositionIdentity destinationIdentity2 = new PositionIdentity();
                    destinationIdentity2.setId(parentPosition.getId());
                    destinationIdentity2.setName(parentPosition.getName());
                    destinationIdentity2.setPositionCode(parentPosition.getPositionCode());
                    relData = new PositionParentRelationship();
                    relData.setSourceId(sourceIdentity2);
                    relData.setDestinationId(destinationIdentity2);
                    this.genericDao.save((Object)relData);
                }
            }
            return this.OK(PositionParams.POSITION_IDENTIFIER.name(), (BaseValueObject)new Identifier(position.getId()));
        }
        catch (Exception ex) {
            PositionDataServiceImpl.log.error("Exception in PositionDataServiceImpl - createPosition() : " + ex.getMessage());
            return this.ERROR((Exception)new DataAccessException("POS-DAC-000", "Unknown error while creating position", (Throwable)ex));
        }
    }
    
    @Transactional
    public Response createPositions(Request request) {
        final BaseValueObjectList list = (BaseValueObjectList)request.get(PositionParams.POSITION_DATA_LIST.name());
        if (list == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_DATA", "No input data in the request"));
        }
        final List<PositionData> data = (List<PositionData>)list.getValueObjectList();
        final List<Integer> positions = new ArrayList<Integer>();
        try {
            for (final PositionData iterator : data) {
                request = new Request();
                request.put(PositionParams.POSITION_DATA.name(), (BaseValueObject)iterator);
                final Response response = this.createPosition(request);
                final Identifier posId = (Identifier)response.get(PositionParams.POSITION_IDENTIFIER.name());
                positions.add(posId.getId());
            }
        }
        catch (Exception e) {
            PositionDataServiceImpl.log.error("Exception in PositionDataServiceImpl - createPositions() : " + e.getMessage());
            return this.ERROR((Exception)new DataAccessException("POS-DAC-000", e.getMessage()));
        }
        return this.OK(PositionParams.POSITION_IDENTIFIER_LIST.name(), (BaseValueObject)new IdentifierList((List)positions));
    }
    
    @Transactional(readOnly = true)
    public Response getPosition(final Request request) {
        final Identifier identifier = (Identifier)request.get(PositionParams.POSITION_IDENTIFIER.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_DATA", "No input data in the request"));
        }
        Position position = null;
        try {
            position = (Position)this.positionDao.find((Serializable)identifier.getId());
        }
        catch (Exception ex) {
            PositionDataServiceImpl.log.error("Exception in PositionDataServiceImpl - getPosition() : " + ex.getMessage());
            return this.ERROR((Exception)new DataAccessException("POS-DAC-000", "Unknown error while loading position", (Throwable)ex));
        }
        if (position == null) {
            return this.ERROR((Exception)new DataAccessException("POS-DAC-101", "Position with id {0} does not exist.", new Object[] { identifier.getId() }));
        }
        final PositionData positionData = (PositionData)this.modelMapper.map((Object)position, (Class)PositionData.class);
        return this.OK(PositionParams.POSITION_DATA.name(), (BaseValueObject)positionData);
    }
    
    @Transactional(readOnly = true)
    public Response getAllPositions(final Request request) {
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "startDate";
        final List<PositionData> positions = new ArrayList<PositionData>();
        List<Position> allPositions = null;
        try {
            final Search search = new Search((Class)Position.class);
            search.addFilterEqual("status", (Object)1);
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.positionDao.count((ISearch)search), defaultField);
            allPositions = (List<Position>)this.positionDao.search((ISearch)search);
        }
        catch (Exception ex) {
            PositionDataServiceImpl.log.error("Exception in PositionDataServiceImpl - getAllPositions() : " + ex.getMessage());
            return this.ERROR((Exception)new DataAccessException("POS-DAC-000", "Unknown error while loading positions", (Throwable)ex));
        }
        if (allPositions == null || allPositions.isEmpty()) {
            return this.ERROR((Exception)new DataAccessException("POS-DAC-101", "positions does not exist."));
        }
        for (final Position iterator : allPositions) {
            positions.add((PositionData)this.modelMapper.map((Object)iterator, (Class)PositionData.class));
        }
        final BaseValueObjectList list = new BaseValueObjectList();
        list.setValueObjectList((List)positions);
        final Response response = this.OK(PositionParams.POSITION_DATA_LIST.name(), (BaseValueObject)list);
        response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
        response.setSortList(sortList);
        return response;
    }
    
    @Transactional
    public Response deletePosition(final Request request) {
        final Identifier identifier = (Identifier)request.get(PositionParams.POSITION_IDENTIFIER.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_DATA", "No input data in the request"));
        }
        try {
            final boolean status = this.positionDao.removeById((Serializable)identifier.getId());
            return this.OK(PositionParams.STATUS.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            PositionDataServiceImpl.log.error("Exception in PositionDataServiceImpl - deletePosition() : " + ex.getMessage());
            return this.ERROR((Exception)new DataAccessException("POS-DAC-000", "Unknown error while deleting position", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response search(final Request request) {
        final PositionData data = (PositionData)request.get(PositionParams.POSITION_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_PARAM", "No input data in the request"));
        }
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "startDate";
        try {
            final Position position = (Position)this.modelMapper.map((Object)data, (Class)Position.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.positionDao.getFilterFromExample(position, options);
            final Search search = new Search((Class)Position.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.positionDao.count((ISearch)search), defaultField);
            final List<Position> result = (List<Position>)this.positionDao.search((ISearch)search);
            final Type listType = new TypeToken<List<PositionData>>() {}.getType();
            final List<PositionData> positions = (List<PositionData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)positions);
            final Response response = this.OK(PositionParams.POSITION_DATA_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            PositionDataServiceImpl.log.error("Exception in PositionDataServiceImpl - search() : " + e.getMessage());
            return this.ERROR((Exception)new DataAccessException("POS-DAC-000", e.getMessage()));
        }
    }
    
    @Transactional
    public Response addParent(final Request request) {
        PositionRelationshipData data = (PositionRelationshipData)request.get(PositionParams.POSITION_REL_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_DATA", "No input data in the request"));
        }
        try {
            final PositionParentRelationship rel = (PositionParentRelationship)this.modelMapper.map((Object)data, (Class)PositionParentRelationship.class);
            this.genericDao.save((Object)rel);
            final PositionParentRelationship relData = (PositionParentRelationship)this.genericDao.find((Class)PositionParentRelationship.class, (Serializable)rel.getId());
            data = (PositionRelationshipData)this.modelMapper.map((Object)relData, (Class)PositionRelationshipData.class);
            final Response response = this.OK();
            response.put(PositionParams.POSITION_REL_DATA.name(), (BaseValueObject)data);
            response.put(PositionParams.POSITION_REL_ID.name(), (BaseValueObject)new Identifier(rel.getId()));
            return response;
        }
        catch (Exception e) {
            PositionDataServiceImpl.log.error("Exception in PositionDataServiceImpl - addParent() : " + e.getMessage());
            return this.ERROR((Exception)new DataAccessException("POS-DAC-000", e.getMessage()));
        }
    }
    
    @Transactional
    public Response removeParent(final Request request) {
        final PositionRelationshipData data = (PositionRelationshipData)request.get(PositionParams.POSITION_REL_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_DATA", "No input data in the request"));
        }
        try {
            final PositionParentRelationship rel = (PositionParentRelationship)this.modelMapper.map((Object)data, (Class)PositionParentRelationship.class);
            final boolean status = this.genericDao.removeById((Class)PositionParentRelationship.class, (Serializable)rel.getId());
            return this.OK(PositionParams.STATUS.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            PositionDataServiceImpl.log.error("Exception in PositionDataServiceImpl - removeParent() : " + e.getMessage());
            return this.ERROR((Exception)new DataAccessException("POS-DAC-000", e.getMessage()));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getParentRelationshipsByPositionId(final Request request) {
        final Identifier positionId = (Identifier)request.get(PositionParams.POSITION_IDENTIFIER.name());
        if (positionId == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_PARAM", "No input data in the request"));
        }
        try {
            final List<PositionRelationshipData> data = new ArrayList<PositionRelationshipData>();
            final Search search = new Search((Class)PositionParentRelationship.class);
            search.addFilterEqual("sourceId.id", (Object)positionId.getId());
            search.addFilterLessOrEqual("startDate", (Object)new Date());
            search.addFilterOr(new Filter[] { Filter.greaterOrEqual("endDate", (Object)new Date()), Filter.or(new Filter[] { Filter.isNull("endDate") }) });
            final List<PositionParentRelationship> result = (List<PositionParentRelationship>)this.genericDao.search((ISearch)search);
            if (result != null && !result.isEmpty()) {
                for (final PositionParentRelationship iterator : result) {
                    data.add((PositionRelationshipData)this.modelMapper.map((Object)iterator, (Class)PositionRelationshipData.class));
                }
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(PositionParams.POSITION_REL_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            return this.ERROR((Exception)new DataAccessException("POS-DAC-000", e.getMessage()));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getParentRelationships(final Request request) {
        try {
            final List<PositionRelationshipData> data = new ArrayList<PositionRelationshipData>();
            final Search search = new Search((Class)PositionParentRelationship.class);
            search.addFilterLessOrEqual("startDate", (Object)new Date());
            search.addFilterOr(new Filter[] { Filter.greaterOrEqual("endDate", (Object)new Date()), Filter.or(new Filter[] { Filter.isNull("endDate") }) });
            search.addFetch("sourceId");
            search.addFetch("destinationId");
            final List<PositionParentRelationship> result = (List<PositionParentRelationship>)this.genericDao.search((ISearch)search);
            for (final PositionParentRelationship iterator : result) {
                data.add((PositionRelationshipData)this.modelMapper.map((Object)iterator, (Class)PositionRelationshipData.class));
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(PositionParams.POSITION_REL_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            PositionDataServiceImpl.log.error("Exception in PositionDataServiceImpl - getParentRelationships() : " + e.getMessage());
            return this.ERROR((Exception)new DataAccessException("POS-DAC-000", e.getMessage()));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllParentRelationships(final Request request) {
        try {
            final List<PositionRelationshipData> data = new ArrayList<PositionRelationshipData>();
            final List<PositionParentRelationship> result = (List<PositionParentRelationship>)this.genericDao.findAll((Class)PositionParentRelationship.class);
            for (final PositionParentRelationship iterator : result) {
                data.add((PositionRelationshipData)this.modelMapper.map((Object)iterator, (Class)PositionRelationshipData.class));
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(PositionParams.POSITION_REL_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            PositionDataServiceImpl.log.error("Exception in PositionDataServiceImpl - getAllParentRelationships() : " + e.getMessage());
            return this.ERROR((Exception)new DataAccessException("POS-DAC-000", e.getMessage()));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchPosition(final Request request) {
        final PositionData objPositionData = (PositionData)request.get(PositionParams.POSITION_DATA.name());
        if (objPositionData == null) {
            return this.ERROR((Exception)new DataAccessException("POS-DAC-102", "No Position data found in request"));
        }
        try {
            String str = "";
            str = str + "name like '" + objPositionData.getName() + "%' ";
            final Search search = new Search((Class)Position.class);
            search.addFilterCustom(str);
            final List<Position> result = (List<Position>)this.positionDao.search((ISearch)search);
            final Type listType = new TypeToken<List<PositionData>>() {}.getType();
            final List<PositionData> positionDataList = (List<PositionData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)positionDataList);
            return this.OK(PositionParams.POSITION_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            PositionDataServiceImpl.log.error("Exception in PositionDataServiceImpl - serachPosition() : " + e.getMessage());
            return this.ERROR((Exception)new DataAccessException("POS-DAC-000", e.getMessage()));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getPositionsCount(final Request request) {
        try {
            final Search search = new Search((Class)Position.class);
            final Integer positionsCount = this.positionDao.count((ISearch)search);
            return this.OK(PositionParams.POSITIONS_COUNT.name(), (BaseValueObject)new Identifier(positionsCount));
        }
        catch (Exception ex) {
            PositionDataServiceImpl.log.error("Exception in PositionDataServiceImpl - getPositionsCount() : " + ex.getMessage());
            return this.ERROR((Exception)new MiddlewareException("Failed to get Positions count", ex.getMessage()));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)PositionDataServiceImpl.class);
    }
}
