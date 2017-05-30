package com.kpisoft.kpi.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class ScorecardSnapshotVo extends BaseValueObject
{
    private static final long serialVersionUID = -2052972290621682261L;
    private int id;
    private Date date;
    private ScorecardScoreBean scorecardScore;
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public Date getDate() {
        return this.date;
    }
    
    public void setDate(final Date date) {
        this.date = date;
    }
    
    public ScorecardScoreBean getScorecardScore() {
        return this.scorecardScore;
    }
    
    public void setScorecardScore(final ScorecardScoreBean scorecardScore) {
        this.scorecardScore = scorecardScore;
    }
}
