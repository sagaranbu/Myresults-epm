package com.canopus.dac.utils;

import com.canopus.mw.dto.*;
import org.hibernate.*;
import java.util.*;
import com.canopus.mw.dto.param.*;
import com.canopus.dac.*;

public class HibernateDAOHelper
{
    public static void setTenantFilter(final Session session) {
        if (!ExecutionContext.getCurrent().isCrossTenant()) {
            final int tenantId = getTenantId();
            if (tenantId != -1) {
                final Filter filter = session.enableFilter("tenant");
                if (filter != null) {
                    filter.setParameter("tenantId", (Object)tenantId);
                }
            }
        }
    }
    
    public static void setSystemData(final Object... entities) {
        if (!ExecutionContext.getCurrent().isCrossTenant()) {
            if (entities instanceof IBaseTenantEntity[]) {
                final int tenantId = getTenantId();
                if (tenantId != -1) {
                    for (final IBaseTenantEntity entity : (IBaseTenantEntity[])entities) {
                        entity.setTenantId(tenantId);
                    }
                }
            }
            if (entities instanceof BaseDataAccessEntity[]) {
                for (final BaseDataAccessEntity entity2 : (BaseDataAccessEntity[])entities) {
                    entity2.setLastModifiedOn(new Date());
                    entity2.setLastModifiedBy((String)ExecutionContext.getCurrent().getContextValues().get(HeaderParam.USER_NAME.getParamName()));
                }
            }
        }
    }
    
    public static void setSystemData(final Object entity) {
        if (!ExecutionContext.getCurrent().isCrossTenant()) {
            if (entity instanceof IBaseTenantEntity) {
                final int tenantId = getTenantId();
                if (tenantId != -1) {
                    ((IBaseTenantEntity)entity).setTenantId(tenantId);
                }
            }
            if (entity instanceof BaseDataAccessEntity) {
                ((BaseDataAccessEntity)entity).setLastModifiedOn(new Date());
                if (ExecutionContext.getCurrent().getContextValues() != null) {
                    ((BaseDataAccessEntity)entity).setLastModifiedBy((String)ExecutionContext.getCurrent().getContextValues().get(HeaderParam.USER_NAME.getParamName()));
                }
            }
        }
    }
    
    public static void validateEntityTenantId(final Object result) {
        if (!ExecutionContext.getCurrent().isCrossTenant() && result instanceof IBaseTenantEntity) {
            final int tenantId = getTenantId();
            if (tenantId != -1 && !((IBaseTenantEntity)result).getTenantId().equals(tenantId)) {
                throw new DataAccessException("", "No element exist with the specified id for tenant: " + tenantId);
            }
        }
    }
    
    public static void validateEntityTenantId(final Object... entities) {
        if (!ExecutionContext.getCurrent().isCrossTenant() && entities instanceof IBaseTenantEntity[]) {
            final int tenantId = getTenantId();
            if (tenantId != -1) {
                for (final IBaseTenantEntity entity : (IBaseTenantEntity[])entities) {
                    if (!entity.getTenantId().equals(tenantId)) {
                        throw new DataAccessException(DacCoreErrorCodes.INVALID_TENANT.name(), "No element exist with the specified id for tenant: " + tenantId);
                    }
                }
            }
        }
    }
    
    private static int getTenantId() {
        int tenantId = -1;
        if (ExecutionContext.getCurrent() != null && ExecutionContext.getCurrent().getContextValues() != null && ExecutionContext.getCurrent().getContextValues().containsKey(HeaderParam.TENANT_ID.getParamName())) {
            tenantId = (int) ExecutionContext.getCurrent().getContextValues().get(HeaderParam.TENANT_ID.getParamName());
        }
        return tenantId;
    }
}
