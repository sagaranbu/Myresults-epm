package com.canopus.mw.sys;

import org.springframework.stereotype.*;
import com.canopus.mw.sys.domain.*;
import org.codehaus.jackson.map.*;
import org.springframework.beans.factory.annotation.*;
import org.quartz.impl.*;
import com.canopus.mw.*;
import javax.annotation.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.dto.MiddlewareEventBean;
import com.canopus.entity.vo.*;
import java.io.*;
import com.canopus.mw.events.*;
import com.canopus.dac.*;
import org.quartz.*;
import java.util.*;

@Component
public class EventScheduler
{
    @Autowired
    private IMiddlewareEventClient middlewareEventClient;
    @Autowired
    private ScheduledEventDomainManager scheduledEventDomainManager;
    ObjectMapper objectMapper;
    private Scheduler scheduler;
    @Autowired
    @Qualifier("eventSchedulerCron")
    private String eventSchedulerCron;
    
    public EventScheduler() {
        this.objectMapper = new ObjectMapper();
    }
    
    @PostConstruct
    private void init() {
        try {
            final Properties props = new Properties();
            props.put("org.quartz.scheduler.instanceName", "EventScheduler");
            props.put("org.quartz.threadPool.threadCount", "3");
            props.put("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
            final StdSchedulerFactory factory = new StdSchedulerFactory(props);
            this.scheduler = StdSchedulerFactory.getDefaultScheduler();
            final JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("middlewareEventClient", (Object)this.middlewareEventClient);
            jobDataMap.put("scheduledEventDomainManager", (Object)this.scheduledEventDomainManager);
            jobDataMap.put("objectMapper", (Object)this.objectMapper);
            final JobDetail job = JobBuilder.newJob((Class)EventSchedulerJob.class).usingJobData(jobDataMap).withIdentity("job1", "group1").build();
            final CronTrigger trigger = (CronTrigger)TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").withSchedule((ScheduleBuilder)CronScheduleBuilder.cronSchedule(this.eventSchedulerCron)).build();
            this.scheduler.scheduleJob(job, (Trigger)trigger);
            this.scheduler.start();
        }
        catch (SchedulerException e) {
            throw new MiddlewareException("", "Unable to start Scheduler", (Throwable)e);
        }
    }
    
    public void addScheduleJob(final JobDetail job, final Trigger trigger) {
        try {
            (this.scheduler = StdSchedulerFactory.getDefaultScheduler()).scheduleJob(job, trigger);
            this.scheduler.start();
        }
        catch (ObjectAlreadyExistsException e) {
            e.printStackTrace();
        }
        catch (SchedulerException e2) {
            throw new MiddlewareException("", "Error while add job", (Throwable)e2);
        }
    }
    
    @PreDestroy
    private void destroy() {
        try {
            this.scheduler.shutdown();
        }
        catch (SchedulerException e) {
            throw new MiddlewareException("", "Unable to shutdown Scheduler", (Throwable)e);
        }
    }
    
    public static class EventSchedulerJob implements Job
    {
        private IMiddlewareEventClient middlewareEventClient;
        private ScheduledEventDomainManager scheduledEventDomainManager;
        private ObjectMapper objectMapper;
        
        public EventSchedulerJob() {
            this.objectMapper = null;
        }
        
        public void execute(final JobExecutionContext context) throws JobExecutionException {
            ExecutionContext.getCurrent().setCrossTenant();
            final List<MiddlewareEventBean> eventBeans = this.scheduledEventDomainManager.getEventsToFire();
            for (final MiddlewareEventBean eventBean : eventBeans) {
                MiddlewareEvent event = null;
                try {
                    final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(eventBean.getEventData()));
                    event = (MiddlewareEvent)in.readObject();
                }
                catch (Exception e) {
                    throw new DataAccessException("INVALID_SYSTEM_DATA", "Error deserializing the payload", (Throwable)e);
                }
                this.middlewareEventClient.fireEvent(event);
                this.scheduledEventDomainManager.setProcessed(eventBean.getId(), true);
            }
            ExecutionContext.getCurrent().unSetCrossTenant();
        }
        
        public void setMiddlewareEventClient(final IMiddlewareEventClient middlewareEventClient) {
            this.middlewareEventClient = middlewareEventClient;
        }
        
        public void setScheduledEventDomainManager(final ScheduledEventDomainManager scheduledEventDomainManager) {
            this.scheduledEventDomainManager = scheduledEventDomainManager;
        }
        
        public void setObjectMapper(final ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }
    }
}
