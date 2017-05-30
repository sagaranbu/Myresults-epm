package com.canopus.dac;

import java.util.*;
import javax.persistence.*;

@MappedSuperclass
public abstract class BaseMultiLangEntity<T extends MultiLangExtension> extends BaseDataAccessEntity implements IBaseMultiLangEntity<T>
{
    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @JoinColumn(name = "MAIN_ID")
    List<T> langTokens;
    
    @Override
    public List<T> getLangTokens() {
        return this.langTokens;
    }
    
    @Override
    public void setLangTokens(final List<T> langTokens) {
        this.langTokens = langTokens;
    }
}
