package com.kpisoft.strategy.program.vo;

import com.canopus.mw.dto.*;

public class AnswerOptionBean extends BaseValueObject
{
    private static final long serialVersionUID = -8924674641885711247L;
    private Integer id;
    private String option;
    private Integer weightage;
    private Integer questionId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getOption() {
        return this.option;
    }
    
    public void setOption(final String option) {
        this.option = option;
    }
    
    public Integer getWeightage() {
        return this.weightage;
    }
    
    public void setWeightage(final Integer weightage) {
        this.weightage = weightage;
    }
    
    public Integer getQuestionId() {
        return this.questionId;
    }
    
    public void setQuestionId(final Integer questionId) {
        this.questionId = questionId;
    }
}
