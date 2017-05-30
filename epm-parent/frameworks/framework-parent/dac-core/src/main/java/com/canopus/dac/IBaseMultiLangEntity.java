package com.canopus.dac;

import java.util.*;

public interface IBaseMultiLangEntity<T extends MultiLangExtension>
{
    List<T> getLangTokens();
    
    void setLangTokens(final List<T> p0);
}
