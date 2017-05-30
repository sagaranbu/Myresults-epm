package com.canopus.mw.interceptor;

import org.springframework.beans.factory.annotation.*;
import com.canopus.event.mgr.vo.*;
import javax.interceptor.*;
import com.canopus.mw.utils.*;
import java.util.*;
import com.canopus.mw.*;
import com.canopus.event.mgr.*;
import com.canopus.event.mgr.vo.params.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.events.*;

public class EventInterceptor implements IInterceptor
{
    @Value("${event.enabled:false}")
    private boolean enabled;
    @Autowired
    private IMiddlewareEventClient mwEventClient;
    @Autowired
    private IServiceLocator serviceLocator;
    private static Map<String, List<EventOriginData>> eventOriginData;
    
    public EventInterceptor() {
        this.enabled = false;
    }
    
    @Override
    public void start(final InvocationContext invContext, final Boolean isEntryPoint) {
        if (!this.isEnabled(invContext)) {
            return;
        }
        final List<EventOriginData> data = this.getEventOriginData(invContext);
        final Object[] params = invContext.getParameters();
        Request request = null;
        if (params.length > 0 && params[0] instanceof Request) {
            request = (Request)params[0];
        }
        if (null != data && null != request) {
            final String operationId = InterceptorHelper.getOperationId(invContext);
            for (final EventOriginData origData : data) {
                if (null != origData.getOperationId() && origData.getOperationId().equals(operationId) && "Pre_Operation_Req".equals(origData.getOperationType())) {
                    this.fireEvent(request, null, origData);
                }
            }
        }
    }
    
    private boolean isEnabled(final InvocationContext invContext) {
        return this.enabled && !invContext.getTarget().getClass().getSimpleName().equals("EventManagerServiceImple") && !invContext.getTarget().getClass().getSimpleName().equals("AsyncEventManager");
    }
    
    @Override
    public void end(final InvocationContext invContext, final Boolean isEntryPoint, final String requestPath) {
        if (!this.isEnabled(invContext)) {
            return;
        }
        final List<EventOriginData> data = this.getEventOriginData(invContext);
        final Object[] params = invContext.getParameters();
        Request request = null;
        if (params.length > 0 && params[0] instanceof Request) {
            request = (Request)params[0];
        }
        final Response response = ExecutionContext.getCurrent().getResponse();
        if (null != data) {
            final String operationId = InterceptorHelper.getOperationId(invContext);
            for (final EventOriginData origData : data) {
                if (null != origData.getOperationId() && origData.getOperationId().equals(operationId)) {
                    if ("Post_Operation_Req".equals(origData.getOperationType()) && null != request) {
                        this.fireEvent(request, null, origData);
                    }
                    if (!"Post_Operation_Res".equals(origData.getOperationType()) || null == response) {
                        continue;
                    }
                    this.fireEvent(null, response, origData);
                }
            }
        }
    }
    
    @Override
    public void error(final InvocationContext invContext, final Boolean isEntryPoint, final String requestPath, final Exception exception) {
        if (!this.isEnabled(invContext)) {
            return;
        }
        final List<EventOriginData> data = this.getEventOriginData(invContext);
        if (null != data) {}
    }
    
    @Override
    public void saveState() {
    }
    
    public static void refreshEventOriginData() {
        EventInterceptor.eventOriginData = new HashMap<String, List<EventOriginData>>();
    }
    
    private List<EventOriginData> getEventOriginData(final InvocationContext ctx) {
        final StringIdentifier serviceId = ((MiddlewareService)ctx.getTarget()).getServiceId();
        List<EventOriginData> originData = EventInterceptor.eventOriginData.get(serviceId.getId());
        if (null == originData) {
            final IEventManager eventManager = (IEventManager)this.serviceLocator.getService("EventManagerServiceImple");
            final Request request = new Request();
            request.getRequestValueObjects().put(EventOriginDataParams.SERVICEID.name(), serviceId);
            final Response response = eventManager.getMWServiceEventOriginators(request);
            if (null != response) {
                final BaseValueObjectList list = (BaseValueObjectList)response.get(EventOriginDataParams.ORIGINDATA.name());
                if (null != list) {
                    originData = (List<EventOriginData>)list.getValueObjectList();
                    EventInterceptor.eventOriginData.put(serviceId.getId(), originData);
                }
            }
        }
        return originData;
    }
    
    private void fireEvent(final Request request, final Response response, final EventOriginData originData) {
        final MiddlewareEvent event = new MiddlewareEvent();
        if (null != request) {
            event.setPayLoadMap(request.getRequestValueObjects());
        }
        if (null != response) {
            event.setPayLoadMap(response.getResponseValueObjects());
        }
        event.setEventType(originData.getOperationId());
        this.mwEventClient.fireEvent2(event);
    }
    
    static {
        EventInterceptor.eventOriginData = new HashMap<String, List<EventOriginData>>();
    }
}
