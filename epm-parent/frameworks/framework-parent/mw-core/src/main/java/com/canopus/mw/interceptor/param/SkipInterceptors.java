package com.canopus.mw.interceptor.param;

import java.lang.annotation.*;
import com.canopus.mw.interceptor.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SkipInterceptors {
    Class<? extends IInterceptor>[] value();
}
