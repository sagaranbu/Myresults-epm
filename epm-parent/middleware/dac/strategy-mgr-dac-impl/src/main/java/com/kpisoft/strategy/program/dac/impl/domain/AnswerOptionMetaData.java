package com.kpisoft.strategy.program.dac.impl.domain;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "QBE_DET_OPTION")
public class AnswerOptionMetaData extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 7946886862145274388L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "QDO_ID_SEQ")
    @SequenceGenerator(name = "QDO_ID_SEQ", sequenceName = "QDO_ID_SEQ")
    @Column(name = "QDO_PK_ID", length = 11)
    private Integer id;
    @Column(name = "OPTION_NAME", length = 127)
    private String option;
    @Column(name = "WEIGHTAGE", length = 11)
    private Integer weightage;
    @Column(name = "QDQ_FK_ID", length = 11)
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
