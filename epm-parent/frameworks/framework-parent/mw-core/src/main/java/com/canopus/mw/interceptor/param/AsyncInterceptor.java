package com.canopus.mw.interceptor.param;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AsyncInterceptor {
}
