package com.kpisoft.strategy.program.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class QuestionCategoryBean extends BaseValueObject
{
    private static final long serialVersionUID = 5263308868233510131L;
    private Integer id;
    private String name;
    private String type;
    private List<QuestionBean> questionMetaData;
    private Integer parentId;
    
    public QuestionCategoryBean() {
        this.questionMetaData = new ArrayList<QuestionBean>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public List<QuestionBean> getQuestionMetaData() {
        return this.questionMetaData;
    }
    
    public void setQuestionMetaData(final List<QuestionBean> questionMetaData) {
        this.questionMetaData = questionMetaData;
    }
    
    public Integer getParentId() {
        return this.parentId;
    }
    
    public void setParentId(final Integer parentId) {
        this.parentId = parentId;
    }
}
