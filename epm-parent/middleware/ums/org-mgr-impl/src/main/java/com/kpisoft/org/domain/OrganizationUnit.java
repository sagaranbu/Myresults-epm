package com.kpisoft.org.domain;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.cache.*;
import com.kpisoft.org.graph.*;
import java.util.*;
import com.kpisoft.org.vo.*;
import com.canopus.mw.utils.*;
import com.kpisoft.org.dac.*;
import javax.validation.*;

public class OrganizationUnit extends BaseDomainObject
{
    private OrganizationUnitData orgUnitData;
    private OrganizationManager orgManager;
    
    public OrganizationUnit(final OrganizationManager orgManager) {
        this.orgUnitData = null;
        this.orgManager = null;
        this.orgManager = orgManager;
    }
    
    public int save() {
        this.validate();
        final Request request = new Request();
        request.put(OrganizationParams.ORG_DATA.name(), (BaseValueObject)this.orgUnitData);
        final Response response = this.getDataService().saveOrganizationUnit(request);
        final Identifier identifier = (Identifier)response.get(OrganizationParams.ORG_IDENTIFIER.name());
        this.orgUnitData.setId(identifier.getId());
        return this.orgUnitData.getId();
    }
    
    public int save(final Integer dimensionId) {
        this.validate();
        final Request request = new Request();
        request.put(OrganizationParams.ORG_DATA.name(), (BaseValueObject)this.orgUnitData);
        request.put(OrganizationParams.DIMENSION_ID.name(), (BaseValueObject)new Identifier(dimensionId));
        final Response response = this.getDataService().saveOrganizationUnit(request);
        final Identifier identifier = (Identifier)response.get(OrganizationParams.ORG_IDENTIFIER.name());
        this.orgUnitData.setId(identifier.getId());
        final OrganizationUnit org = (OrganizationUnit)this.orgManager.getCache().get(this.orgUnitData.getId(), (CacheLoader)this.orgManager);
        if (org != null) {
            this.orgManager.getCache().remove(this.orgUnitData.getId());
        }
        return this.orgUnitData.getId();
    }
    
    public List<OrgIdentityBean> getParents(final int childId) {
        final List<OrgGraph.Organization> parents = this.orgManager.getIdentityParentRelationship().getParents(childId);
        final List<OrgIdentityBean> parentIds = new ArrayList<OrgIdentityBean>();
        if (parents != null && !parents.isEmpty()) {
            for (final OrgGraph.Organization iterator : parents) {
                parentIds.add(this.toIdentity(iterator));
            }
        }
        return parentIds;
    }
    
    public List<OrgIdentityBean> getChildrens(final int parentId) {
        final List<OrgGraph.Organization> childs = this.orgManager.getIdentityParentRelationship().getChildrens(parentId);
        final List<OrgIdentityBean> childrens = new ArrayList<OrgIdentityBean>();
        if (childs != null && !childs.isEmpty()) {
            for (final OrgGraph.Organization iterator : childs) {
                childrens.add(this.toIdentity(iterator));
            }
        }
        return childrens;
    }
    
    public List<OrgIdentityBean> getAscendants(final int id, final int dist) {
        final List<OrgGraph.Organization> result = this.orgManager.getIdentityParentRelationship().getAscendants(id, dist);
        final List<OrgIdentityBean> ascendants = new ArrayList<OrgIdentityBean>();
        if (result != null && !result.isEmpty()) {
            for (final OrgGraph.Organization iterator : result) {
                ascendants.add(this.toIdentity(iterator));
            }
        }
        return ascendants;
    }
    
    public List<OrgIdentityBean> getDescendants(final int id, final int dist) {
        final List<OrgGraph.Organization> result = this.orgManager.getIdentityParentRelationship().getDescendants(id, dist);
        final List<OrgIdentityBean> descendants = new ArrayList<OrgIdentityBean>();
        if (result != null && !result.isEmpty()) {
            for (final OrgGraph.Organization iterator : result) {
                descendants.add(this.toIdentity(iterator));
            }
        }
        return descendants;
    }
    
    public boolean isAscendant(final int parentId, final int childId) {
        final boolean status = this.orgManager.getIdentityParentRelationship().isAscendant(parentId, childId);
        return status;
    }
    
    public boolean isDescendant(final int childId, final int parentId) {
        final boolean status = this.orgManager.getIdentityParentRelationship().isDescendant(childId, parentId);
        return status;
    }
    
    public boolean isParent(final int parentId, final int childId) {
        final boolean status = this.orgManager.getIdentityParentRelationship().isParent(parentId, childId);
        return status;
    }
    
    public boolean isChild(final int childId, final int parentId) {
        final boolean status = this.orgManager.getIdentityParentRelationship().isChild(childId, parentId);
        return status;
    }
    
    public List<OrgIdentityBean> getParentsByDimension(final int childId, final int dimensionId) {
        final List<OrgGraph.Organization> result = this.orgManager.getIdentityParentRelationship().getParentsByDimension(childId, dimensionId);
        final List<OrgIdentityBean> parents = new ArrayList<OrgIdentityBean>();
        if (result != null && !result.isEmpty()) {
            for (final OrgGraph.Organization iterator : result) {
                parents.add(this.toIdentity(iterator));
            }
        }
        return parents;
    }
    
    public List<OrgIdentityBean> getChildrensByDimension(final int parentId, final int dimensionId) {
        final List<OrgGraph.Organization> childs = this.orgManager.getIdentityParentRelationship().getChildrensByDimension(parentId, dimensionId);
        final List<OrgIdentityBean> childrens = new ArrayList<OrgIdentityBean>();
        if (childs != null && !childs.isEmpty()) {
            for (final OrgGraph.Organization iterator : childs) {
                childrens.add(this.toIdentity(iterator));
            }
        }
        return childrens;
    }
    
    public List<OrgIdentityBean> getAscendantsByDimension(final int id, final int dist, final int dimensionId) {
        final List<OrgGraph.Organization> result = this.orgManager.getIdentityParentRelationship().getAscendantsByDimension(id, dist, dimensionId);
        final List<OrgIdentityBean> ascendants = new ArrayList<OrgIdentityBean>();
        if (result != null && !result.isEmpty()) {
            for (final OrgGraph.Organization iterator : result) {
                ascendants.add(this.toIdentity(iterator));
            }
        }
        return ascendants;
    }
    
    public List<OrgIdentityBean> getDescendantsByDimension(final int id, final int dist, final int dimensionId) {
        final List<OrgGraph.Organization> result = this.orgManager.getIdentityParentRelationship().getDescendantsByDimension(id, dist, dimensionId);
        final List<OrgIdentityBean> descendants = new ArrayList<OrgIdentityBean>();
        if (result != null && !result.isEmpty()) {
            for (final OrgGraph.Organization iterator : result) {
                descendants.add(this.toIdentity(iterator));
            }
        }
        return descendants;
    }
    
    public PasswordPolicyData getOrgPasswordPolicy() {
        final Request request = new Request();
        request.put(OrganizationParams.ORG_IDENTIFIER.name(), (BaseValueObject)new Identifier(this.orgUnitData.getId()));
        final Response response = this.orgManager.getDataService().getOrgPasswordPolicy(request);
        final PasswordPolicyData data = (PasswordPolicyData)response.get(OrganizationParams.PASSWORD_POLICY_DATA.name());
        return data;
    }
    
    public void validate() {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), (Object)this.orgUnitData, "ORG-201", "Invalid organization unit details");
    }
    
    public OrganizationUnitData getOrgUnitData() {
        return this.orgUnitData;
    }
    
    public void setOrgUnitData(final OrganizationUnitData orgUnitData) {
        this.orgUnitData = orgUnitData;
    }
    
    private OrganizationUnitDataService getDataService() {
        return this.orgManager.getDataService();
    }
    
    private Validator getValidator() {
        return this.orgManager.getValidator();
    }
    
    public OrgIdentityBean toIdentity(final OrgGraph.Organization org) {
        final OrgIdentityBean identityBean = new OrgIdentityBean();
        identityBean.setId(org.getOrgId());
        identityBean.setOrgUnitCode(org.getOrgCode());
        identityBean.setOrgName(org.getOrgName());
        identityBean.setLevel(org.getLevel());
        identityBean.setStartDate(org.getOrgStartDate());
        identityBean.setEndDate(org.getOrgEndDate());
        identityBean.setOrgType(org.getOrgType());
        identityBean.setStatus(org.getStatus());
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
}
