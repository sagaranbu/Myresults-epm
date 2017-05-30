package com.kpisoft.org.domain;

import org.springframework.stereotype.*;
import com.kpisoft.org.dac.*;
import javax.validation.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.org.vo.*;
import com.canopus.mw.*;
import com.kpisoft.org.params.*;
import java.util.*;
import com.canopus.mw.dto.*;

@Component
public class GradeManager extends BaseDomainManager implements CacheLoader<Integer, Grade>
{
    @Autowired
    private GradeDataService gradeDataService;
    @Autowired
    private Validator validator;
    @Autowired
    @Qualifier("gradeCache")
    private Cache<Integer, Grade> cache;
    
    public GradeManager() {
        this.gradeDataService = null;
        this.cache = null;
    }
    
    public Grade saveOrUpdateGrade(final GradeData gradeData) {
        GradeData data = null;
        final Grade grade = new Grade(this, this.gradeDataService);
        try {
            data = grade.save(gradeData);
            grade.setObjGradeData(data);
        }
        catch (Exception e) {
            throw new MiddlewareException("ERR_SAVE_UPDATE_GRADE", e.getMessage(), (Throwable)e);
        }
        finally {
            if (data != null && data.getId() != null && data.getId() > 0) {
                this.cache.remove(data.getId());
            }
        }
        return grade;
    }
    
    public Grade getGrade(final int id) {
        final Grade grade = (Grade)this.cache.get(id, (CacheLoader)this);
        return grade;
    }
    
    public Grade load(final Integer key) {
        final Request request = new Request();
        request.put(GradeParams.GRADE_ID.name(), (BaseValueObject)new Identifier(key));
        final Response response = this.gradeDataService.getGradeById(request);
        final GradeData gradeData = (GradeData)response.get(GradeParams.GRADE_DATA.name());
        final Grade grade = new Grade(this, this.gradeDataService);
        grade.setObjGradeData(gradeData);
        return grade;
    }
    
    public List<GradeData> getAllGrade(SortList sortList) {
        final Request request = new Request();
        request.setSortList(sortList);
        final Response response = this.gradeDataService.getAllGrade(request);
        final BaseValueObjectList object = (BaseValueObjectList)response.get(GradeParams.GRADE_DATA_LIST.name());
        final List<GradeData> gradeList = (List<GradeData>)object.getValueObjectList();
        sortList = response.getSortList();
        return gradeList;
    }
    
    public boolean removeById(final Integer id) {
        final Grade grade = new Grade(this, this.gradeDataService);
        final boolean status = grade.removeGrade(id);
        if (status) {
            this.cache.remove(id);
        }
        return status;
    }
    
    public List<GradeData> searchGrade(final GradeData gradeData, SortList sortList) {
        final Request request = new Request();
        request.put(GradeParams.GRADE_DATA.name(), (BaseValueObject)gradeData);
        request.setSortList(sortList);
        final Response response = this.gradeDataService.searchGrade(request);
        final BaseValueObjectList list = (BaseValueObjectList)response.get(GradeParams.GRADE_DATA_LIST.name());
        final List<GradeData> gradeList = (List<GradeData>)list.getValueObjectList();
        sortList = response.getSortList();
        return gradeList;
    }
    
    public Integer getGradeCount() {
        final Response response = this.gradeDataService.getGradeCount(new Request());
        final Identifier identifier = (Identifier)response.get(GradeParams.GRD_COUNT.name());
        return identifier.getId();
    }
    
    public Validator getValidator() {
        return this.validator;
    }
}
