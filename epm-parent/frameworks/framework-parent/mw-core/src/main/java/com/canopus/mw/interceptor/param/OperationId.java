package com.canopus.mw.interceptor.param;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface OperationId {
    String name();
    
    String mode();
}
