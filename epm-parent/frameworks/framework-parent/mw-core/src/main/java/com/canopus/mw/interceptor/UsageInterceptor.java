package com.canopus.mw.interceptor;

import org.apache.log4j.*;
import com.codahale.metrics.*;
import com.codahale.metrics.Timer;
import com.canopus.interceptor.*;
import org.springframework.beans.factory.annotation.*;
import javax.interceptor.*;
import com.canopus.mw.utils.*;
import com.canopus.mw.dto.*;
import com.canopus.interceptor.vo.*;
import java.util.*;

public class UsageInterceptor implements IInterceptor
{
    final MetricRegistry metricRegistry;
    private final Logger logger;
    private Timer timer;
    LinkedHashMap<String, Integer> tenantIdQueue;
    @Autowired
    private InterceptorDataService interceptorDataService;
    private boolean dbLoggingEnabled;
    private static InheritableThreadLocal<Stack<Timer.Context>> timerContextStack;
    
    public UsageInterceptor() {
        this.metricRegistry = new MetricRegistry();
        this.logger = Logger.getLogger((Class)UsageInterceptor.class);
        this.tenantIdQueue = new LinkedHashMap<String, Integer>();
        this.interceptorDataService = null;
        this.dbLoggingEnabled = false;
    }
    
    @Override
    public void start(final InvocationContext invContext, final Boolean isEntryPoint) {
        if (this.dbLoggingEnabled) {
            synchronized (this) {
                final String key = InterceptorHelper.getOperationId(invContext);
                this.timer = this.metricRegistry.timer(MetricRegistry.name(key, new String[] { "" }));
                this.tenantIdQueue.put(key, ExecutionContext.getTenantId());
                final Timer.Context timerContext = this.timer.time();
                UsageInterceptor.timerContextStack.get().push(timerContext);
            }
        }
    }
    
    @Override
    public void end(final InvocationContext invContext, final Boolean isEntryPoint, final String requestPath) {
        if (this.dbLoggingEnabled) {
            final Timer.Context timerContext;
            synchronized (this) {
                timerContext = UsageInterceptor.timerContextStack.get().pop();
            }
            timerContext.stop();
        }
    }
    
    @Override
    public void error(final InvocationContext invContext, final Boolean isEntryPoint, final String requestPath, final Exception exception) {
    }
    
    @Override
    public void saveState() {
        final List<UsageInterceptorData> usageInterceptorList = new ArrayList<UsageInterceptorData>();
        synchronized (this) {
            final SortedMap<String, Timer> timers = (SortedMap<String, Timer>)this.metricRegistry.getTimers();
            for (final String key : timers.keySet()) {
                this.logger.info((Object)("Operation: " + key + " || Mean: " + timers.get(key).getSnapshot().getMean()));
                final UsageInterceptorData usageInterceptorData = new UsageInterceptorData((Integer)null, (Integer)this.tenantIdQueue.get(key), key, timers.get(key).getSnapshot().getMean(), timers.get(key).getSnapshot().getMin(), timers.get(key).getSnapshot().getMax(), timers.get(key).getCount(), new Date());
                usageInterceptorList.add(usageInterceptorData);
                this.metricRegistry.remove(key);
            }
        }
        this.interceptorDataService.saveUsageInterceptorList((List)usageInterceptorList);
    }
    
    static {
        UsageInterceptor.timerContextStack = new InheritableThreadLocal<Stack<Timer.Context>>() {
            @Override
            protected Stack<Timer.Context> initialValue() {
                final Stack<Timer.Context> exetimeStack = new Stack<Timer.Context>();
                return exetimeStack;
            }
        };
    }
}
