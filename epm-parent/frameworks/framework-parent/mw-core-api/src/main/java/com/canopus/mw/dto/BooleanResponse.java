package com.canopus.mw.dto;

import java.io.*;

public class BooleanResponse extends BaseValueObject implements Serializable
{
    private static final long serialVersionUID = 2L;
    private boolean response;
    
    public BooleanResponse(final boolean response) {
        this.response = response;
    }
    
    public boolean isResponse() {
        return this.response;
    }
    
    public void setResponse(final boolean response) {
        this.response = response;
    }
}
