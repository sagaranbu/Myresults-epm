package com.kpisoft.org.domain;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.utils.*;
import com.kpisoft.org.vo.*;
import com.kpisoft.org.graph.*;
import java.util.*;
import com.kpisoft.org.dac.*;
import javax.validation.*;

@Deprecated
public class Position extends BaseDomainObject
{
    private PositionData positionData;
    private PositionManager positionManager;
    
    public Position(final PositionManager positionManager) {
        this.positionData = null;
        this.positionManager = null;
        this.positionManager = positionManager;
    }
    
    public int save() {
        this.validate();
        final Request request = new Request();
        request.put(PositionParams.POSITION_DATA.name(), (BaseValueObject)this.positionData);
        final Response response = this.getDataService().createPosition(request);
        final Identifier identifier = (Identifier)response.get(PositionParams.POSITION_IDENTIFIER.name());
        this.positionData.setId(identifier.getId());
        return this.positionData.getId();
    }
    
    public void validate() {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), (Object)this.positionData, "POS-201", "Invalid position details");
    }
    
    public List<PositionIdentityBean> getParents(final int childId) {
        final List<PositionGraph.Position> parents = this.positionManager.getParentRelationship().getParents(childId);
        final List<PositionIdentityBean> identityList = new ArrayList<PositionIdentityBean>();
        if (parents != null && !parents.isEmpty()) {
            for (final PositionGraph.Position iterator : parents) {
                identityList.add(this.toIdentity(iterator));
            }
        }
        return identityList;
    }
    
    public List<PositionIdentityBean> getChildrens(final int parentId) {
        final List<PositionGraph.Position> childs = this.positionManager.getParentRelationship().getChildrens(parentId);
        final List<PositionIdentityBean> identityList = new ArrayList<PositionIdentityBean>();
        if (childs != null && !childs.isEmpty()) {
            for (final PositionGraph.Position iterator : childs) {
                identityList.add(this.toIdentity(iterator));
            }
        }
        return identityList;
    }
    
    public List<PositionIdentityBean> getAscendants(final int id, final int dist) {
        final List<PositionGraph.Position> result = this.positionManager.getParentRelationship().getAscendants(id, dist);
        final List<PositionIdentityBean> identityList = new ArrayList<PositionIdentityBean>();
        if (result != null && !result.isEmpty()) {
            for (final PositionGraph.Position iterator : result) {
                identityList.add(this.toIdentity(iterator));
            }
        }
        return identityList;
    }
    
    public List<PositionIdentityBean> getDescendants(final int id, final int dist) {
        final List<PositionGraph.Position> result = this.positionManager.getParentRelationship().getDescendants(id, dist);
        final List<PositionIdentityBean> identityList = new ArrayList<PositionIdentityBean>();
        if (result != null && !result.isEmpty()) {
            for (final PositionGraph.Position iterator : result) {
                identityList.add(this.toIdentity(iterator));
            }
        }
        return identityList;
    }
    
    public boolean isAscendant(final int parentId, final int childId) {
        final boolean status = this.positionManager.getParentRelationship().isAscendant(parentId, childId);
        return status;
    }
    
    public boolean isDescendant(final int childId, final int parentId) {
        final boolean status = this.positionManager.getParentRelationship().isDescendant(childId, parentId);
        return status;
    }
    
    public boolean isParent(final int parentId, final int childId) {
        final boolean status = this.positionManager.getParentRelationship().isParent(parentId, childId);
        return status;
    }
    
    public boolean isChild(final int childId, final int parentId) {
        final boolean status = this.positionManager.getParentRelationship().isChild(childId, parentId);
        return status;
    }
    
    private PositionDataService getDataService() {
        return this.positionManager.getDataService();
    }
    
    private Validator getValidator() {
        return this.positionManager.getValidator();
    }
    
    public PositionData getPositionData() {
        return this.positionData;
    }
    
    public void setPositionData(final PositionData positionData) {
        this.positionData = positionData;
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
}
