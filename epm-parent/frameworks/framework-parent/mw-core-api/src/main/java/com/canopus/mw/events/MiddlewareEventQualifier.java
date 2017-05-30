package com.canopus.mw.events;

import java.lang.annotation.*;
import javax.inject.*;

@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier
public @interface MiddlewareEventQualifier {
    String value();
}
