package com.canopus.mw;

import java.util.*;

public class ValidationException extends MiddlewareException
{
    private String violations;
    
    public ValidationException(final String errCode, final String message, final Set<String> violations) {
        super(errCode, message);
        this.violations = null;
        final StringBuilder builder = new StringBuilder();
        for (final String violation : violations) {
            builder.append(violation).append("\n");
        }
    }
    
    @Override
    public String toString() {
        final String core = super.toString();
        return core + this.violations;
    }
}
