package com.kpisoft.kpi.utility;

import org.springframework.stereotype.*;
import com.kpisoft.kpi.domain.*;
import org.springframework.beans.factory.annotation.*;
import java.util.*;
import org.perf4j.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.vo.param.*;

@Component
public class PeriodUtil
{
    @Autowired
    private KpiManager manager;
    
    public static Integer getCurrentPeriod(final List<PeriodMasterBean> recomputePeriods, final Date systemDate) {
        Integer currentPeriod = null;
        if (recomputePeriods != null && !recomputePeriods.isEmpty()) {
            currentPeriod = recomputePeriods.get(0).getId();
        }
        for (final PeriodMasterBean bean : recomputePeriods) {
            if (bean.getEndDate() != null) {
                if (bean.getEndDate().compareTo(systemDate) > 0) {
                    break;
                }
                currentPeriod = bean.getId();
            }
        }
        return currentPeriod;
    }
    
    public KpiScoreBean getCurrentPeriodScore(final List<KpiScoreBean> scoreList, final Date systemDate) {
        final List<Integer> periodList = new ArrayList<Integer>();
        final Map<Integer, KpiScoreBean> periodmap = new HashMap<Integer, KpiScoreBean>();
        for (final KpiScoreBean score : scoreList) {
            if (score.getPeriodMasterId() != null && score.getPeriodMasterId() > 0) {
                periodmap.put(score.getPeriodMasterId(), score);
                periodList.add(score.getPeriodMasterId());
            }
        }
        Collections.sort(periodList);
        Integer currentPeriod = null;
        if (periodList != null && !periodList.isEmpty()) {
            currentPeriod = periodList.get(0);
        }
        for (final Integer period : periodList) {
            final PeriodMasterBean bean = this.manager.getPeriodMasterById(period);
            if (bean != null && bean.getEndDate() != null) {
                if (bean.getEndDate().compareTo(systemDate) > 0) {
                    break;
                }
                currentPeriod = bean.getId();
            }
        }
        KpiScoreBean scoreBean = null;
        if (currentPeriod != null) {
            scoreBean = periodmap.get(currentPeriod);
        }
        return scoreBean;
    }
    
    public KpiScoreBean getCurrentPeriodScore(final List<KpiScoreBean> scoreList) {
        return this.getCurrentPeriodScore(scoreList, new Date());
    }
    
    public KpiData updateCurrentScoreDetails(final KpiData data) {
        final StopWatch sw = new StopWatch();
        sw.start();
        final Date date = new Date();
        final List<Integer> periodList = new ArrayList<Integer>();
        final Map<Integer, KpiScoreBean> periodmap = new HashMap<Integer, KpiScoreBean>();
        final Map<Integer, KpiTargetDataBean> targetmap = new HashMap<Integer, KpiTargetDataBean>();
        if (data.getKpiScore() != null) {
            for (final KpiScoreBean score : data.getKpiScore()) {
                if (score.getPeriodMasterId() != null && score.getPeriodMasterId() > 0) {
                    periodmap.put(score.getPeriodMasterId(), score);
                    periodList.add(score.getPeriodMasterId());
                }
            }
        }
        if (data.getKpiTarget() != null && data.getKpiTarget().getTargetData() != null) {
            for (final KpiTargetDataBean target : data.getKpiTarget().getTargetData()) {
                if (!periodmap.containsKey(target.getPeriodMasterId())) {
                    periodList.add(target.getPeriodMasterId());
                }
                targetmap.put(target.getPeriodMasterId(), target);
            }
        }
        Collections.sort(periodList);
        Integer currentPeriod = null;
        if (periodList != null && !periodList.isEmpty()) {
            currentPeriod = periodList.get(0);
            final PeriodMasterBean bean = this.manager.getPeriodMasterById(currentPeriod);
            if (bean != null && bean.getEndPeriodMasterId() != null) {
                final Integer endPeriod = bean.getEndPeriodMasterId();
                periodList.clear();
                for (Integer i = currentPeriod; i <= endPeriod; ++i) {
                    periodList.add(i);
                }
            }
        }
        double cumulativeScore = 0.0;
        double cumulativeTarget = 0.0;
        Date dateScore = null;
        boolean isAgg = false;
        Date targetDate = null;
        if (data.getKpiTarget() != null) {
            targetDate = data.getKpiTarget().getDateTarget();
        }
        if (data != null && data.getAggregationType() != null && data.getAggregationType().getType() != null) {
            isAgg = data.getAggregationType().getType().equalsIgnoreCase(KpiValidationParams.ScoreAggregationType.SUMMATION.name());
        }
        for (final Integer period : periodList) {
            final PeriodMasterBean bean2 = this.manager.getPeriodMasterById(period);
            if (bean2 != null && bean2.getEndDate() != null) {
                if (bean2.getEndDate().compareTo(date) > 0) {
                    break;
                }
                currentPeriod = bean2.getId();
            }
            final KpiTargetDataBean targetBean = targetmap.get(period);
            if (targetBean != null) {
                if (isAgg) {
                    cumulativeTarget += targetBean.getTargetDataNum();
                }
                else {
                    cumulativeTarget = targetBean.getTargetDataNum();
                }
            }
            final KpiScoreBean scoreBean = periodmap.get(period);
            if (scoreBean != null) {
                if (isAgg) {
                    if (scoreBean.getNumScore_mtd() != null) {
                        cumulativeScore += scoreBean.getNumScore_mtd();
                    }
                    else if (scoreBean.getNumScore() != null) {
                        cumulativeScore = scoreBean.getNumScore();
                    }
                }
                else if (scoreBean.getNumScore() != null) {
                    cumulativeScore = scoreBean.getNumScore();
                }
                else {
                    cumulativeScore = 0.0;
                }
                if (dateScore != null) {
                    continue;
                }
                dateScore = scoreBean.getDateScore();
            }
            else {
                if (isAgg) {
                    continue;
                }
                cumulativeScore = 0.0;
            }
        }
        KpiScoreBean scoreBean2 = null;
        if (currentPeriod != null) {
            scoreBean2 = periodmap.get(currentPeriod);
            data.setCurrScore(cumulativeScore);
            data.setNumTarget(cumulativeTarget);
            data.setDateTarget(targetDate);
            data.setCurrPeriodId(currentPeriod);
            data.setCurrDateScore(dateScore);
            double ach = 0.0;
            int level = 0;
            if (scoreBean2 != null) {
                if (scoreBean2.getAchievement() != null) {
                    ach = scoreBean2.getAchievement();
                }
                if (scoreBean2.getRatingLevel() != null) {
                    level = scoreBean2.getRatingLevel();
                }
            }
            data.setCurrAchievement(ach);
            data.setCurrRatingLevel(level);
        }
        sw.stop();
        return data;
    }
    
    public List<KpiData> updateCurrentScoreDetails(final List<KpiData> data) {
        final List<KpiData> updatedList = new ArrayList<KpiData>();
        for (KpiData existing : data) {
            existing = this.updateCurrentScoreDetails(existing);
            updatedList.add(existing);
        }
        return updatedList;
    }
    
    public double getCumulativeTarget(final Map<Integer, KpiTargetDataBean> targetmap, final List<Integer> periodList, final Integer currentPeriodID, final boolean isAgg) {
        double cumulativeTarget = 0.0;
        for (final Integer period : periodList) {
            final KpiTargetDataBean targetBean = targetmap.get(period);
            if (targetBean != null) {
                if (isAgg) {
                    if (targetBean.getTargetDataNum() == null) {
                        continue;
                    }
                    cumulativeTarget += targetBean.getTargetDataNum();
                }
                else {
                    if (targetBean.getTargetDataNum() == null) {
                        continue;
                    }
                    cumulativeTarget = targetBean.getTargetDataNum();
                }
            }
        }
        return cumulativeTarget;
    }
    
    public double getCumulativeActuals(final Map<Integer, KpiScoreBean> periodmap, final List<Integer> periodList, final Integer currentPeriodID, final boolean isAgg) {
        double cumulativeScore = 0.0;
        for (final Integer period : periodList) {
            final KpiScoreBean scoreBean = periodmap.get(period);
            if (scoreBean != null) {
                if (isAgg) {
                    if (scoreBean.getNumScore_mtd() == null) {
                        continue;
                    }
                    cumulativeScore += scoreBean.getNumScore_mtd();
                }
                else {
                    if (scoreBean.getNumScore() == null) {
                        continue;
                    }
                    cumulativeScore = scoreBean.getNumScore();
                }
            }
        }
        return cumulativeScore;
    }
    
    public Integer getCurrentPeriodID(final List<Integer> periodList, final Date date) {
        int currentPeriod = periodList.get(0);
        for (final Integer period : periodList) {
            final PeriodMasterBean bean = this.manager.getPeriodMasterById(period);
            if (bean != null && bean.getEndDate() != null) {
                if (bean.getEndDate().compareTo(date) > 0) {
                    break;
                }
                currentPeriod = bean.getId();
            }
        }
        return currentPeriod;
    }
}
