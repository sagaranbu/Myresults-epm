package com.canopus.mw;

import org.springframework.test.context.testng.*;
import org.springframework.test.context.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.events.*;
import javax.ejb.embeddable.*;
import com.canopus.mw.dto.param.*;
import com.canopus.mw.dto.*;
import java.util.*;
import org.apache.openejb.core.ivm.naming.*;
import org.testng.annotations.*;

@ContextConfiguration({ "classpath*:mw-spring/mw-core-test-context.xml" })
public class BaseEmbeddedContainerTestNG extends AbstractTestNGSpringContextTests
{
    @Autowired
    protected IServiceLocator serviceLocator;
    @Autowired
    protected IMiddlewareEventClient middlewareEventClient;
    public static EJBContainer ejbContainer;
    
    @BeforeClass
    public static void startTheContainer() {
        BaseEmbeddedContainerTestNG.ejbContainer = EJBContainer.createEJBContainer();
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put(HeaderParam.TENANT_ID.getParamName(), 1);
        context.put(HeaderParam.USER_ID.getParamName(), "TESTNG");
        context.put(HeaderParam.SYSTEM_LOCALE.getParamName(), "en-us");
        context.put(HeaderParam.TENANT_LOCALE.getParamName(), "en-nz");
        context.put(HeaderParam.USER_LOCALE.getParamName(), "en-us");
        ExecutionContext.getCurrent().setContextValues((Map)context);
    }
    
    @AfterClass
    public static void stopTheContainer() {
        if (BaseEmbeddedContainerTestNG.ejbContainer != null) {
            BaseEmbeddedContainerTestNG.ejbContainer.close();
        }
    }
    
    @BeforeMethod
    public void lookupABean() throws NamingException {
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put(HeaderParam.TENANT_ID.getParamName(), 1);
        context.put(HeaderParam.USER_ID.getParamName(), "TESTNG");
        context.put(HeaderParam.SYSTEM_LOCALE.getParamName(), "en-us");
        context.put(HeaderParam.TENANT_LOCALE.getParamName(), "en-nz");
        context.put(HeaderParam.USER_LOCALE.getParamName(), "en-us");
        ExecutionContext.getCurrent().setContextValues((Map)context);
        final EmbeddedContainerServiceLocator embeddedContainerServiceLocator = (EmbeddedContainerServiceLocator)this.serviceLocator;
        EmbeddedContainerServiceLocator.setEjbContext(BaseEmbeddedContainerTestNG.ejbContainer.getContext());
    }
}
