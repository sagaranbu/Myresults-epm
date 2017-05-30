package com.kpisoft.org.dac.impl;

import com.kpisoft.org.dac.*;
import org.springframework.stereotype.*;
import com.kpisoft.org.dac.dao.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.org.vo.*;
import com.kpisoft.org.dac.impl.domain.*;
import com.canopus.mw.*;
import com.kpisoft.org.params.*;
import com.kpisoft.org.utility.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import com.canopus.dac.utils.*;
import org.modelmapper.*;
import java.util.*;
import com.canopus.mw.dto.*;
import com.googlecode.genericdao.search.*;
import java.lang.reflect.*;
import org.slf4j.*;

@Service
public class GradeDataServiceImpl extends BaseDataAccessService implements GradeDataService
{
    @Autowired
    private GradeDao gradeDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public GradeDataServiceImpl() {
        this.gradeDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)GradeData.class, (Class)Grade.class);
    }
    
    @Transactional(readOnly = true)
    public Response getGradeById(final Request request) {
        final Identifier id = (Identifier)request.get(GradeParams.GRADE_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException(OrgErrorCodesEnum.ERR_GRD_INVALID_INPUT_002.name(), "No data found in request"));
        }
        try {
            Grade grade = null;
            if (id.getId() != null && id.getId() > 0) {
                grade = (Grade)this.gradeDao.find((Serializable)id.getId());
            }
            if (grade == null) {
                return this.ERROR((Exception)new DataAccessException(OrgErrorCodesEnum.ERR_GRD_DOES_NOT_EXIST_001.name(), "Grade id {0} does not exist.", new Object[] { id.getId() }));
            }
            final GradeData gradeData = (GradeData)this.modelMapper.map((Object)grade, (Class)GradeData.class);
            return this.OK(GradeParams.GRADE_DATA.name(), (BaseValueObject)gradeData);
        }
        catch (Exception e) {
            GradeDataServiceImpl.log.error("Exception in GradeDataServiceImpl - getUser() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(OrgErrorCodesEnum.ERR_GRD_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response saveGrade(final Request request) {
        final GradeData gradeData = (GradeData)request.get(GradeParams.GRADE_DATA.name());
        if (gradeData == null) {
            return this.ERROR((Exception)new DataAccessException(OrgErrorCodesEnum.ERR_GRD_INVALID_INPUT_002.name(), "No data found in request"));
        }
        try {
            Grade grade = null;
            if (gradeData.getId() != null && gradeData.getId() > 0) {
                grade = (Grade)this.gradeDao.find((Serializable)gradeData.getId());
                if (grade == null) {
                    return this.ERROR((Exception)new DataAccessException(OrgErrorCodesEnum.ERR_GRD_DOES_NOT_EXIST_001.name(), " data does not exist"));
                }
            }
            else {
                grade = new Grade();
            }
            this.modelMapper.map((Object)gradeData, (Object)grade);
            this.gradeDao.save(grade);
            gradeData.setId(grade.getId());
            return this.OK(GradeParams.GRADE_DATA.name(), (BaseValueObject)gradeData);
        }
        catch (Exception e) {
            GradeDataServiceImpl.log.error("Exception in GradeDataServiceImpl - saveGrade() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(OrgErrorCodesEnum.ERR_GRD_UNABLE_TO_CREATE_003.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response removeGrade(final Request request) {
        final Identifier id = (Identifier)request.get(GradeParams.GRADE_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException(OrgErrorCodesEnum.ERR_GRD_INVALID_INPUT_002.name(), "No data found in request"));
        }
        boolean flag = false;
        try {
            if (id.getId() != null && id.getId() > 0) {
                flag = this.gradeDao.removeById((Serializable)id.getId());
            }
            return this.OK(GradeParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(flag));
        }
        catch (Exception e) {
            GradeDataServiceImpl.log.error("Exception in GradeDataServiceImpl - removeGrade() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(OrgErrorCodesEnum.ERR_GRD_UNABLE_TO_DELETE_006.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllGrade(final Request request) {
        final SortList sortList = request.getSortList();
        final String defaultField = "level";
        try {
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.gradeDao.getFilterFromExample(new Grade(), options);
            final Search search = new Search((Class)Grade.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<Grade> result = (List<Grade>)this.gradeDao.search((ISearch)search);
            final Type listType = new TypeToken<List<GradeData>>() {}.getType();
            final List<GradeData> gradeList = (List<GradeData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)gradeList);
            final Response response = this.OK(GradeParams.GRADE_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            GradeDataServiceImpl.log.error("Exception in GradeDataServiceImpl - getAllGrade() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(OrgErrorCodesEnum.ERR_GRD_UNABLE_TO_GET_ALL_007.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchGrade(final Request request) {
        final GradeData gradeData = (GradeData)request.get(GradeParams.GRADE_DATA.name());
        if (gradeData == null) {
            return this.ERROR((Exception)new DataAccessException(OrgErrorCodesEnum.ERR_GRD_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        final SortList sortList = request.getSortList();
        final String defaultField = "level";
        try {
            final Grade grade = (Grade)this.modelMapper.map((Object)gradeData, (Class)Grade.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.gradeDao.getFilterFromExample(grade, options);
            final Search search = new Search((Class)Grade.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<Grade> result = (List<Grade>)this.gradeDao.search((ISearch)search);
            final Type listType = new TypeToken<List<GradeData>>() {}.getType();
            final List<GradeData> gradeList = (List<GradeData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)gradeList);
            final Response response = this.OK(GradeParams.GRADE_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            GradeDataServiceImpl.log.error("Exception in GradeDataServiceImpl - searchGrade() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(OrgErrorCodesEnum.ERR_GRD_UNABLE_TO_SEARCH_008.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getGradeCount(final Request request) {
        try {
            final Search search = new Search((Class)Grade.class);
            final Integer totalGrds = this.gradeDao.count((ISearch)search);
            return this.OK(GradeParams.GRD_COUNT.name(), (BaseValueObject)new Identifier(totalGrds));
        }
        catch (Exception ex) {
            GradeDataServiceImpl.log.error("Exception in GradeDataServiceImpl - getGradeCount() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(OrgErrorCodesEnum.ERR_GRD_UNABLE_TO_GET_004.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)GradeDataServiceImpl.class);
    }
}
