package com.canopus.mw.dto;

import java.util.*;

public class IdentifierList extends BaseValueObject
{
    private static final long serialVersionUID = 1L;
    private List<Integer> idsList;
    
    public IdentifierList(final List<Integer> idsList) {
        this.idsList = idsList;
    }
    
    public List<Integer> getIdsList() {
        return this.idsList;
    }
    
    public void setIdsList(final List<Integer> idsList) {
        this.idsList = idsList;
    }
}
