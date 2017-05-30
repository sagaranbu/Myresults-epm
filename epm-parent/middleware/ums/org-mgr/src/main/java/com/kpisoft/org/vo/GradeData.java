package com.kpisoft.org.vo;

import com.canopus.mw.dto.*;
import java.io.*;
import javax.validation.constraints.*;

public class GradeData extends BaseValueObject implements Serializable
{
    private static final long serialVersionUID = 6174410298403058764L;
    private Integer id;
    private String name;
    private Integer level;
    private String category;
    @NotNull
    private String gradeCode;
    
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
    
    public Integer getLevel() {
        return this.level;
    }
    
    public void setLevel(final Integer level) {
        this.level = level;
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public void setCategory(final String category) {
        this.category = category;
    }
    
    public String getGradeCode() {
        return this.gradeCode;
    }
    
    public void setGradeCode(final String gradeCode) {
        this.gradeCode = gradeCode;
    }
}
