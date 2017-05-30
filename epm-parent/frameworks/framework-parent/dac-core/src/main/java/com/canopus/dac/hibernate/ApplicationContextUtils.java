package com.canopus.dac.hibernate;

import org.springframework.context.*;
import org.springframework.beans.*;

public class ApplicationContextUtils implements ApplicationContextAware
{
    private static ApplicationContext ctx;
    
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtils.ctx = applicationContext;
    }
    
    public static ApplicationContext getApplicationContext() {
        return ApplicationContextUtils.ctx;
    }
}
