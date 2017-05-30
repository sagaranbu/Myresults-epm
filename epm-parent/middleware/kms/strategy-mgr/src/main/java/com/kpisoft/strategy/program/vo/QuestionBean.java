package com.kpisoft.strategy.program.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class QuestionBean extends BaseValueObject
{
    private static final long serialVersionUID = -7986978864814494176L;
    private Integer id;
    private String heading;
    private String description;
    private Integer version;
    private Integer parentId;
    private Integer orgUnitId;
    private Integer questionCategoryId;
    private List<AnswerOptionBean> answerOptionMetaData;
    
    public QuestionBean() {
        this.answerOptionMetaData = new ArrayList<AnswerOptionBean>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getHeading() {
        return this.heading;
    }
    
    public void setHeading(final String heading) {
        this.heading = heading;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(final Integer version) {
        this.version = version;
    }
    
    public Integer getParentId() {
        return this.parentId;
    }
    
    public void setParentId(final Integer parentId) {
        this.parentId = parentId;
    }
    
    public Integer getOrgUnitId() {
        return this.orgUnitId;
    }
    
    public void setOrgUnitId(final Integer orgUnitId) {
        this.orgUnitId = orgUnitId;
    }
    
    public Integer getQuestionCategoryId() {
        return this.questionCategoryId;
    }
    
    public void setQuestionCategoryId(final Integer questionCategoryId) {
        this.questionCategoryId = questionCategoryId;
    }
    
    public List<AnswerOptionBean> getAnswerOptionMetaData() {
        return this.answerOptionMetaData;
    }
    
    public void setAnswerOptionMetaData(final List<AnswerOptionBean> answerOptionMetaData) {
        this.answerOptionMetaData = answerOptionMetaData;
    }
}
