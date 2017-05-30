package com.canopus.interceptor.domain;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "TEST_ENTITY_TWO")
public class TestEntity2 extends BaseDataAccessEntity
{
    private static final long serialVersionUID = -1689078456004555412L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "TE2_ENTITY_ID_SEQ")
    @SequenceGenerator(name = "TE2_ENTITY_ID_SEQ", sequenceName = "TE2_ENTITY_ID_SEQ")
    @Column(name = "TE2_PK_ID")
    private Integer id;
    @Column(name = "NAME", length = 127)
    private String name;
    @Column(name = "AGE")
    private Integer age;
    
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
    
    public Integer getAge() {
        return this.age;
    }
    
    public void setAge(final Integer age) {
        this.age = age;
    }
}
