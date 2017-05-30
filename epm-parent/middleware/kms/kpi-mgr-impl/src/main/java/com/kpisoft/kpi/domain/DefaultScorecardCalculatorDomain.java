package com.kpisoft.kpi.domain;

import com.canopus.mw.*;
import com.kpisoft.kpi.utility.*;
import com.kpisoft.kpi.vo.*;
import java.util.*;

public class DefaultScorecardCalculatorDomain extends BaseDomainObject
{
    public Double calcScorecardRatingLevel(final List<KpiData> kpiDataList) {
        Double scorecardRatingLevel = 0.0;
        final Map<Integer, ScorecardCalculatorUtility> ratingLevelMap = new HashMap<Integer, ScorecardCalculatorUtility>();
        final Map<Integer, List<ScorecardCalculatorUtility>> groupRatingLevelMap = new HashMap<Integer, List<ScorecardCalculatorUtility>>();
        ScorecardCalculatorUtility scorecardCalculatorUtility = null;
        List<ScorecardCalculatorUtility> groupRatingList = null;
        for (final KpiData kpiData : kpiDataList) {
            scorecardCalculatorUtility = new ScorecardCalculatorUtility();
            Double kpiAvgRating = 0.0;
            if (!kpiData.getGroupKpi()) {
                int ratingCount = 0;
                for (final KpiScoreBean kpiScore : kpiData.getKpiScore()) {
                    kpiAvgRating += kpiScore.getRatingLevel();
                    ++ratingCount;
                }
                if (ratingCount > 0) {
                    kpiAvgRating /= ratingCount;
                }
            }
            scorecardCalculatorUtility.setKpiId(kpiData.getId());
            scorecardCalculatorUtility.setWeightage(kpiData.getWeightage());
            scorecardCalculatorUtility.setAvgRatingLevel(kpiAvgRating);
            if (kpiData.getGroupId() != null) {
                if (groupRatingLevelMap.containsKey(kpiData.getGroupId())) {
                    groupRatingList = groupRatingLevelMap.get(kpiData.getGroupId());
                }
                else {
                    groupRatingList = new ArrayList<ScorecardCalculatorUtility>();
                }
                groupRatingList.add(scorecardCalculatorUtility);
                groupRatingLevelMap.put(kpiData.getGroupId(), groupRatingList);
            }
            else {
                ratingLevelMap.put(kpiData.getId(), scorecardCalculatorUtility);
            }
        }
        for (final Map.Entry<Integer, List<ScorecardCalculatorUtility>> entry : groupRatingLevelMap.entrySet()) {
            final Integer key = entry.getKey();
            if (ratingLevelMap.containsKey(key)) {
                final ScorecardCalculatorUtility scorecardCalculator = ratingLevelMap.get(key);
                scorecardCalculator.setAvgRatingLevel(this.calcGroupRatingLevel(entry.getValue()));
                ratingLevelMap.put(key, scorecardCalculator);
            }
        }
        scorecardRatingLevel = this.calcRatingLevel(ratingLevelMap);
        return scorecardRatingLevel;
    }
    
    public Double calcGroupRatingLevel(final List<ScorecardCalculatorUtility> groupRatingLevelList) {
        Double groupRatingLevel = 0.0;
        Float totWeightage = 0.0f;
        for (final ScorecardCalculatorUtility scorecardCalcUtility : groupRatingLevelList) {
            totWeightage += scorecardCalcUtility.getWeightage();
            groupRatingLevel += (scorecardCalcUtility.getWeightage() * scorecardCalcUtility.getRatingLevel());
        }
        if (totWeightage > 0.0f) {
            groupRatingLevel /= totWeightage;
        }
        return groupRatingLevel;
    }
    
    private Double calcRatingLevel(final Map<Integer, ScorecardCalculatorUtility> ratingLevelMap) {
        Double ratingLevel = 0.0;
        Float totWeightage = 0.0f;
        for (final Map.Entry<Integer, ScorecardCalculatorUtility> entry : ratingLevelMap.entrySet()) {
            totWeightage += entry.getValue().getWeightage();
            ratingLevel += (entry.getValue().getWeightage() * entry.getValue().getRatingLevel());
        }
        if (totWeightage > 0.0f) {
            ratingLevel /= totWeightage;
        }
        return ratingLevel;
    }
    
    public Double calcScorecardAcievement(final List<ScorecardCalculatorUtility> scorecardUtilityList) {
        Double achievement = 0.0;
        Float totWeightage = 0.0f;
        Double kpiAchievement = 0.0;
        for (final ScorecardCalculatorUtility kpiData : scorecardUtilityList) {
            kpiAchievement += kpiData.getAchievement();
            totWeightage += kpiData.getWeightage();
        }
        if (totWeightage != 0.0f) {
            achievement /= totWeightage;
        }
        return achievement;
    }
    
    public Double calcGroupAcievement(final List<ScorecardCalculatorUtility> ratingLevelMap) {
        Double achievement = 0.0;
        final Map<Integer, List<ScorecardCalculatorUtility>> groupkpisMap = new HashMap<Integer, List<ScorecardCalculatorUtility>>();
        List<ScorecardCalculatorUtility> groupRatingList = null;
        for (final ScorecardCalculatorUtility scorecardCalcUtility : ratingLevelMap) {
            if (scorecardCalcUtility.getGroupId() != null && scorecardCalcUtility.getAchievement() != null) {
                if (groupkpisMap.containsKey(scorecardCalcUtility.getKpiId())) {
                    groupRatingList = groupkpisMap.get(scorecardCalcUtility.getKpiId());
                }
                else {
                    groupRatingList = new ArrayList<ScorecardCalculatorUtility>();
                }
                groupRatingList.add(scorecardCalcUtility);
                groupkpisMap.put(scorecardCalcUtility.getKpiId(), groupRatingList);
            }
        }
        final Map<Integer, ScorecardCalculatorUtility> groupkpis = this.calcGroupKpisAchievement(groupkpisMap);
        if (!groupkpis.isEmpty()) {
            achievement = this.calcAchivement(groupkpis);
        }
        return achievement;
    }
    
    private Map<Integer, ScorecardCalculatorUtility> calcGroupKpisAchievement(final Map<Integer, List<ScorecardCalculatorUtility>> groupkpisMap) {
        final Map<Integer, ScorecardCalculatorUtility> groupkpis = new HashMap<Integer, ScorecardCalculatorUtility>();
        for (final Map.Entry<Integer, List<ScorecardCalculatorUtility>> entry : groupkpisMap.entrySet()) {
            final ScorecardCalculatorUtility utility = new ScorecardCalculatorUtility();
            double kpiAvgAchivement = 0.0;
            int ratingCount = 0;
            float weightage = 0.0f;
            for (final ScorecardCalculatorUtility kpis : entry.getValue()) {
                kpiAvgAchivement += kpis.getAchievement();
                ++ratingCount;
                weightage = kpis.getWeightage();
            }
            if (ratingCount > 0) {
                kpiAvgAchivement /= ratingCount;
            }
            utility.setKpiId((Integer)entry.getKey());
            utility.setWeightage(weightage);
            utility.setAchievement(kpiAvgAchivement);
            groupkpis.put(entry.getKey(), utility);
        }
        return groupkpis;
    }
    
    private Double calcAchivement(final Map<Integer, ScorecardCalculatorUtility> groupKpisMap) {
        Double achivement = 0.0;
        Float totWeightage = 0.0f;
        for (final Map.Entry<Integer, ScorecardCalculatorUtility> entry : groupKpisMap.entrySet()) {
            totWeightage += entry.getValue().getWeightage();
            achivement += entry.getValue().getWeightage() * entry.getValue().getAchievement();
        }
        if (totWeightage > 0.0f) {
            achivement /= totWeightage;
        }
        return achivement;
    }
}
