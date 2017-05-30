package com.kpisoft.strategy.program.dac.impl.domain;

import com.canopus.dac.*;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "QBE_DET_QUESTION")
public class QuestionMetaData extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 5992129613774854223L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "QDQ_ID_SEQ")
    @SequenceGenerator(name = "QDQ_ID_SEQ", sequenceName = "QDQ_ID_SEQ")
    @Column(name = "QDQ_PK_ID", length = 11)
    private Integer id;
    @Column(name = "HEADING", length = 45)
    private String heading;
    @Column(name = "DESCRIPTION", length = 45)
    private String description;
    @Column(name = "VERSION", length = 11)
    private Integer version;
    @Column(name = "PARENT_ID", length = 11)
    private Integer parentId;
    @Column(name = "OU_TENANT_ID", length = 11)
    private Integer orgUnitId;
    @Column(name = "QDC_FK_ID", length = 11)
    private Integer questionCategoryId;
    @OneToMany(cascade = { CascadeType.ALL })
    @JoinColumn(name = "QDQ_FK_ID", referencedColumnName = "QDQ_PK_ID")
    private List<AnswerOptionMetaData> answerOptionMetaData;
    
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
    
    public List<AnswerOptionMetaData> getAnswerOptionMetaData() {
        return this.answerOptionMetaData;
    }
    
    public void setAnswerOptionMetaData(final List<AnswerOptionMetaData> answerOptionMetaData) {
        this.answerOptionMetaData = answerOptionMetaData;
    }
}
