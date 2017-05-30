package com.canopus.dac;

import java.io.*;
import java.util.*;

public interface DataAccessEntity extends Serializable
{
    Integer getId();
    
    void setId(final Integer p0);
    
    String getLastModifiedBy();
    
    void setLastModifiedBy(final String p0);
    
    Date getLastModifiedOn();
    
    void setLastModifiedOn(final Date p0);
}
