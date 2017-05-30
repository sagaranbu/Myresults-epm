package com.canopus.mw.utils;

import javax.validation.*;
import com.canopus.mw.ValidationException;

import java.util.*;

public class ValidationHelper
{
    public <T> void validate(final Validator val, final T data, final String errCode, final String message) {
        if (val != null) {
            final Set<ConstraintViolation<T>> violations = (Set<ConstraintViolation<T>>)val.validate(data, new Class[0]);
            if (!violations.isEmpty()) {
                final Set<String> messages = new HashSet<String>();
                for (final ConstraintViolation<T> violation : violations) {
                    messages.add(violation.getMessage());
                }
                throw new ValidationException(errCode, message, (Set)messages);
            }
        }
    }
}
