package com.kpisoft.strategy.program.dac.impl.domain;

import com.canopus.dac.*;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "QBE_DET_QUESTION_CATEGORY")
public class QuestionCategoryMetaData extends BaseDataAccessEntity
{
    private static final long serialVersionUID = 5005755204115031973L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "QDC_ID_SEQ")
    @SequenceGenerator(name = "QDC_ID_SEQ", sequenceName = "QDC_ID_SEQ")
    @Column(name = "QDC_PK_ID", length = 11)
    private Integer id;
    @Column(name = "NAME", length = 45)
    private String name;
    @Column(name = "TYPE", length = 45)
    private String type;
    @Column(name = "PARENTID", length = 11)
    private Integer parentId;
    @OneToMany(cascade = { CascadeType.ALL })
    @JoinColumn(name = "QDC_FK_ID", referencedColumnName = "QDC_PK_ID")
    private List<QuestionMetaData> questionMetaData;
    
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
    
    public Integer getParentId() {
        return this.parentId;
    }
    
    public void setParentId(final Integer parentId) {
        this.parentId = parentId;
    }
    
    public List<QuestionMetaData> getQuestionMetaData() {
        return this.questionMetaData;
    }
    
    public void setQuestionMetaData(final List<QuestionMetaData> questionMetaData) {
        this.questionMetaData = questionMetaData;
    }
}
