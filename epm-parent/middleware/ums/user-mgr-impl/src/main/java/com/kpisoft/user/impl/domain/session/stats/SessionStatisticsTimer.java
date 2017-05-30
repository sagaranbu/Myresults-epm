package com.kpisoft.user.impl.domain.session.stats;

import java.util.*;
import org.springframework.beans.factory.annotation.*;

public class SessionStatisticsTimer extends TimerTask
{
    @Autowired
    private SessionStatsManager statsManager;
    
    public SessionStatisticsTimer() {
        this.statsManager = null;
    }
    
    @Override
    public void run() {
        this.statsManager.getCurrentSessioStatsSummary().save();
    }
}
