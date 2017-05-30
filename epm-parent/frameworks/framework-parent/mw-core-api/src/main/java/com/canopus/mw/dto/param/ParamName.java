package com.canopus.mw.dto.param;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ParamName {
    String value();
}
