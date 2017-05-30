package com.kpisoft.org.impl;

import com.kpisoft.org.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.org.params.*;
import com.kpisoft.org.utility.*;
import com.canopus.mw.*;
import com.kpisoft.org.domain.*;
import java.util.*;
import com.kpisoft.org.vo.*;
import com.canopus.mw.dto.*;
import org.slf4j.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ GradeManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class GradeManagerServiceImpl extends BaseMiddlewareBean implements GradeManagerService
{
    private static final Logger log;
    @Autowired
    private GradeManager gradeManager;
    
    public GradeManagerServiceImpl() {
        this.gradeManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response getGradeById(final Request request) {
        final Identifier identifier = (Identifier)request.get(GradeParams.GRADE_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new MiddlewareException(OrgErrorCodesEnum.ERR_GRD_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            Grade grade = null;
            if (identifier.getId() != null && identifier.getId() > 0) {
                grade = this.gradeManager.getGrade(identifier.getId());
            }
            return this.OK(GradeParams.GRADE_DATA.name(), (BaseValueObject)grade.getObjGradeData());
        }
        catch (Exception e) {
            GradeManagerServiceImpl.log.error("Exception in GradeManagerServiceImpl - getGradeById() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(OrgErrorCodesEnum.ERR_GRD_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getAllGrade(final Request request) {
        try {
            final SortList sortList = request.getSortList();
            final List<GradeData> data = this.gradeManager.getAllGrade(sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            final Response response = this.OK(GradeParams.GRADE_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception ex) {
            GradeManagerServiceImpl.log.error("Exception in GradeManagerServiceImpl - getAllGrade() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(OrgErrorCodesEnum.ERR_GRD_UNABLE_TO_GET_ALL_007.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response createGrade(final Request request) {
        final GradeData data = (GradeData)request.get(GradeParams.GRADE_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(OrgErrorCodesEnum.ERR_GRD_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Grade grade = this.gradeManager.saveOrUpdateGrade(data);
            return this.OK(GradeParams.GRADE_ID.name(), (BaseValueObject)new Identifier(grade.getObjGradeData().getId()));
        }
        catch (Exception ex) {
            GradeManagerServiceImpl.log.error("Exception in GradeManagerServiceImpl - createGrade() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(OrgErrorCodesEnum.ERR_GRD_UNABLE_TO_CREATE_003.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response updateGrade(final Request request) {
        final GradeData data = (GradeData)request.get(GradeParams.GRADE_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(OrgErrorCodesEnum.ERR_GRD_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final Grade grade = this.gradeManager.saveOrUpdateGrade(data);
            return this.OK(GradeParams.GRADE_ID.name(), (BaseValueObject)new Identifier(grade.getObjGradeData().getId()));
        }
        catch (Exception ex) {
            GradeManagerServiceImpl.log.error("Exception in GradeManagerServiceImpl - updateGrade() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(OrgErrorCodesEnum.ERR_GRD_UNABLE_TO_UPDATE_005.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response removeGrade(final Request request) {
        final Identifier identifier = (Identifier)request.get(GradeParams.GRADE_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new MiddlewareException(OrgErrorCodesEnum.ERR_GRD_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final boolean status = this.gradeManager.removeById(identifier.getId());
            return this.OK(GradeParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            GradeManagerServiceImpl.log.error("Exception in GradeManagerServiceImpl - removeGrade() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(OrgErrorCodesEnum.ERR_GRD_UNABLE_TO_DELETE_006.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    public Response searchGrade(final Request request) {
        final GradeData data = (GradeData)request.get(GradeParams.GRADE_DATA.name());
        final SortList sortList = request.getSortList();
        if (data == null) {
            return this.ERROR((Exception)new MiddlewareException(OrgErrorCodesEnum.ERR_GRD_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final List<GradeData> result = this.gradeManager.searchGrade(data, sortList);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            final Response response = this.OK(GradeParams.GRADE_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            GradeManagerServiceImpl.log.error("Exception in GradeManagerServiceImpl - searchGrade() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(OrgErrorCodesEnum.ERR_GRD_UNABLE_TO_SEARCH_008.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    public Response getGradeCount(final Request request) {
        try {
            final Integer count = this.gradeManager.getGradeCount();
            return this.OK(GradeParams.GRD_COUNT.name(), (BaseValueObject)new Identifier(count));
        }
        catch (Exception e) {
            GradeManagerServiceImpl.log.error("Exception in GradeManagerServiceImpl - getGradeCount() : ", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(OrgErrorCodesEnum.ERR_GRD_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)GradeManagerServiceImpl.class);
    }
}
