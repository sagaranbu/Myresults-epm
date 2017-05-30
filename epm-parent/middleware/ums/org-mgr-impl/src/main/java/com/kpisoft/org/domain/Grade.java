package com.kpisoft.org.domain;

import com.canopus.mw.*;
import com.kpisoft.org.vo.*;
import com.kpisoft.org.dac.*;
import com.kpisoft.org.params.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.utils.*;
import com.kpisoft.org.utility.*;
import javax.validation.*;

public class Grade extends BaseDomainObject
{
    private GradeManager gradeManager;
    private GradeData gradeData;
    private GradeDataService gradeDataService;
    
    public Grade(final GradeManager gradeManager, final GradeDataService gradeDataService) {
        this.gradeManager = null;
        this.gradeData = null;
        this.gradeDataService = null;
        this.gradeManager = gradeManager;
        this.gradeDataService = gradeDataService;
    }
    
    public GradeData save(final GradeData gradeData) {
        this.validate(gradeData);
        final Request request = new Request();
        request.put(GradeParams.GRADE_DATA.name(), (BaseValueObject)gradeData);
        final Response response = this.gradeDataService.saveGrade(request);
        final GradeData gradeData2 = (GradeData)response.get(GradeParams.GRADE_DATA.name());
        return gradeData2;
    }
    
    public boolean removeGrade(final Integer id) {
        final Request request = new Request();
        request.put(GradeParams.GRADE_ID.name(), (BaseValueObject)new Identifier(id));
        final Response response = this.gradeDataService.removeGrade(request);
        final BooleanResponse bResponse = (BooleanResponse)response.get(GradeParams.STATUS_RESPONSE.name());
        return bResponse.isResponse();
    }
    
    public void validate(final Object object) {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), object, OrgErrorCodesEnum.ERR_GRD_INVALID_INPUT_002.name(), "Invalid grade details");
    }
    
    public GradeData getObjGradeData() {
        return this.gradeData;
    }
    
    public void setObjGradeData(final GradeData objGradeData) {
        this.gradeData = objGradeData;
    }
    
    private Validator getValidator() {
        return this.gradeManager.getValidator();
    }
}
