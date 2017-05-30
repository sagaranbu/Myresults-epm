package com.canopus.mw.interceptor;

import org.springframework.beans.factory.annotation.*;
import javax.interceptor.*;
import com.canopus.mw.utils.*;
import com.canopus.mw.manager.*;

public class SystemStateInterceptor implements IInterceptor
{
    @Autowired
    SystemStateManagerImpl systemStateManager;
    
    @Override
    public void start(final InvocationContext invContext, final Boolean isEntryPoint) {
        if (!this.systemStateManager.getSystemState().equals(SystemState.HEALTHY) && InterceptorHelper.getOperationMode(invContext).equals("WRITE")) {
            throw new SystemStateException("SYS-WRIT-00", "Write operations are not possible when the system is in unhealthy state.");
        }
    }
    
    @Override
    public void end(final InvocationContext invContext, final Boolean isEntryPoint, final String requestPath) {
    }
    
    @Override
    public void error(final InvocationContext invContext, final Boolean isEntryPoint, final String requestPath, final Exception exception) {
    }
    
    @Override
    public void saveState() {
    }
}
