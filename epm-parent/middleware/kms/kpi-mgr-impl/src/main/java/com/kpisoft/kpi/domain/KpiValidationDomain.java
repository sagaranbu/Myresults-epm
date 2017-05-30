package com.kpisoft.kpi.domain;

import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.utility.*;
import java.util.*;
import com.kpisoft.kpi.vo.param.*;
import com.kpisoft.strategy.program.vo.*;
import com.canopus.mw.*;
import com.canopus.entity.vo.*;
import com.canopus.entity.*;
import com.canopus.entity.vo.params.*;
import com.canopus.mw.dto.*;

public class KpiValidationDomain extends BaseDomainObject
{
    private IServiceLocator serviceLocator;
    private KpiManager kpiManager;
    
    public KpiValidationDomain() {
    }
    
    public KpiValidationDomain(final KpiManager kpiManager, final IServiceLocator serviceLocator) {
        this.kpiManager = kpiManager;
        this.serviceLocator = serviceLocator;
    }
    
    public List<StatusResponse> validateKpi(final KpiData kpi, final BaseValueObjectMap map) {
        final List<StatusResponse> errorMessages = new ArrayList<StatusResponse>();
        String key = null;
        String value = null;
        final Map<StringIdentifier, StringIdentifier> programRules = (Map<StringIdentifier, StringIdentifier>)map.getBaseValueMap();
        final Map<String, String> validationRulesMap = new HashMap<String, String>();
        for (final Map.Entry<StringIdentifier, StringIdentifier> entry : programRules.entrySet()) {
            if (entry.getKey() != null) {
                key = entry.getKey().getId();
            }
            if (entry.getValue() != null) {
                value = entry.getValue().getId();
            }
            validationRulesMap.put(key, value);
        }
        if (validationRulesMap.containsKey(KpiValidationParams.ProgramConfig.MIN_KPI_WEIGHTAGE.name())) {
            final String minWeightage = validationRulesMap.get(KpiValidationParams.ProgramConfig.MIN_KPI_WEIGHTAGE.name());
            if (minWeightage == null) {
                errorMessages.add(new StatusResponse(KpiErrorCodesEnum.ERR_VALIDATE_KPI_MIN_WEIGHTAGE_001.name(), "Minimum kpi weightage configuration was not settted."));
            }
            else if (kpi.getWeightage() < Float.parseFloat(minWeightage)) {
                errorMessages.add(new StatusResponse(KpiErrorCodesEnum.ERR_VALIDATE_LESS_KPI_WEIGHTAGE_002.name(), "Kpi weightage should not less than " + Float.parseFloat(minWeightage) + "."));
            }
        }
        if (validationRulesMap.containsKey(KpiValidationParams.ProgramConfig.MAX_KPI_WEIGHTAGE.name())) {
            final String maxWeightage = validationRulesMap.get(KpiValidationParams.ProgramConfig.MAX_KPI_WEIGHTAGE.name());
            if (maxWeightage == null) {
                errorMessages.add(new StatusResponse(KpiErrorCodesEnum.ERR_VALIDATE_KPI_MAX_WEIGHTAGE_003.name(), "Maximum kpi weightage configuration was not settted."));
            }
            else if (kpi.getWeightage() > Float.parseFloat(maxWeightage)) {
                errorMessages.add(new StatusResponse(KpiErrorCodesEnum.ERR_VALIDATE_MORE_KPI_WEIGHTAGE_004.name(), "Kpi weightage should not greater than " + Float.parseFloat(maxWeightage) + "."));
            }
        }
        if (kpi.getGroupKpi() != null && kpi.getGroupKpi() && kpi.getKpiType() != null && kpi.getKpiType().getType().equalsIgnoreCase(KpiValidationParams.KpiType.INITIATIVE.name()) && validationRulesMap.containsKey(KpiValidationParams.ProgramConfig.IS_INITIATIVE_GROUP.name())) {
            final String isInitiativeGroup = validationRulesMap.get(KpiValidationParams.ProgramConfig.IS_INITIATIVE_GROUP.name());
            if (isInitiativeGroup == null) {
                errorMessages.add(new StatusResponse(KpiErrorCodesEnum.ERR_VALIDATE_KPI_IS_INITIATIVE_GROUP_005.name(), "Is initiative as group configuration was not settted."));
            }
            else if (kpi.getGroupId() != null && kpi.getGroupId() > 0 && !Boolean.parseBoolean(isInitiativeGroup)) {
                errorMessages.add(new StatusResponse(KpiErrorCodesEnum.ERR_VALIDATE_UNABLE_TO_ADD_INITIATIVE_GROUP_006.name(), "Initiative Kpi can not add into group."));
            }
        }
        return errorMessages;
    }
    
    public List<StatusResponse> validateScorecard(final KpiData kpi, final BaseValueObjectMap map, final Integer scorecardId) {
        final List<StatusResponse> errorMessages = new ArrayList<StatusResponse>();
        String key = null;
        String value = null;
        final Map<StringIdentifier, StringIdentifier> programRules = (Map<StringIdentifier, StringIdentifier>)map.getBaseValueMap();
        final Map<String, String> validationRulesMap = new HashMap<String, String>();
        for (final Map.Entry<StringIdentifier, StringIdentifier> entry : programRules.entrySet()) {
            if (entry.getKey() != null) {
                key = entry.getKey().getId();
            }
            if (entry.getValue() != null) {
                value = entry.getValue().getId();
            }
            validationRulesMap.put(key, value);
        }
        final int numOfKpisAssigned = this.kpiManager.getKpiCountByScoreCard(scorecardId, KpiParams.NONE.name());
        if (validationRulesMap.containsKey(KpiValidationParams.ProgramConfig.MIN_NUM_KPI.name())) {
            final String minNumKpi = validationRulesMap.get(KpiValidationParams.ProgramConfig.MIN_NUM_KPI.name());
            if (minNumKpi == null) {
                errorMessages.add(new StatusResponse(KpiErrorCodesEnum.ERR_VALIDATE_KPI_MIN_COUNT_007.name(), "Minimum number of kpis configuration was not settted."));
            }
            else if (numOfKpisAssigned < Integer.parseInt(minNumKpi)) {
                errorMessages.add(new StatusResponse(KpiErrorCodesEnum.ERR_VALIDATE_LESS_KPI_COUNT_008.name(), "Minimum number of kpis should not less than " + Float.parseFloat(minNumKpi) + "."));
            }
        }
        if (validationRulesMap.containsKey(KpiValidationParams.ProgramConfig.MAX_NUM_KPI.name())) {
            final String maxNumKpi = validationRulesMap.get(KpiValidationParams.ProgramConfig.MAX_NUM_KPI.name());
            if (maxNumKpi == null) {
                errorMessages.add(new StatusResponse(KpiErrorCodesEnum.ERR_VALIDATE_KPI_MAX_COUNT_009.name(), "Maximum number of kpis configuration was not settted."));
            }
            else if (numOfKpisAssigned > Integer.parseInt(maxNumKpi)) {
                errorMessages.add(new StatusResponse(KpiErrorCodesEnum.ERR_VALIDATE_MORE_KPI_COUNT_010.name(), "Maximum number of kpis should not greater than " + Float.parseFloat(maxNumKpi) + "."));
            }
        }
        if (validationRulesMap.containsKey(KpiValidationParams.ProgramConfig.CORE_KPI_PERCENTAGE.name())) {
            final String coreKpiPercentage = validationRulesMap.get(KpiValidationParams.ProgramConfig.CORE_KPI_PERCENTAGE.name());
            final int numOfCoreKpisAssigned = this.kpiManager.getKpiCountByScoreCard(scorecardId, KpiParams.CORE.name());
            if (coreKpiPercentage == null) {
                errorMessages.add(new StatusResponse(KpiErrorCodesEnum.ERR_VALIDATE_CORE_KPI_PERCENTAGE_011.name(), "Core kpi's percentage configuration was not settted."));
            }
            else if (numOfCoreKpisAssigned > Integer.parseInt(coreKpiPercentage)) {
                errorMessages.add(new StatusResponse(KpiErrorCodesEnum.ERR_VALIDATE_MORE_CORE_KPI_PERCENTAGE_012.name(), "Exceeds the core kpi's percentage. "));
            }
        }
        if (validationRulesMap.containsKey(KpiValidationParams.ProgramConfig.INDIVIDUAL_KPI_PERCENTAGE.name())) {
            final String individualKpiPercentage = validationRulesMap.get(KpiValidationParams.ProgramConfig.INDIVIDUAL_KPI_PERCENTAGE.name());
            final int numOfIndividualKpisAssigned = this.kpiManager.getKpiCountByScoreCard(scorecardId, KpiParams.INDIVIDUAL.name());
            if (individualKpiPercentage == null) {
                errorMessages.add(new StatusResponse(KpiErrorCodesEnum.ERR_VALIDATE_INDIVIDUAL_KPI_PERCENTAGE_013.name(), "Individual kpi's percentage configuration was not settted."));
            }
            else if (numOfIndividualKpisAssigned > Integer.parseInt(individualKpiPercentage)) {
                errorMessages.add(new StatusResponse(KpiErrorCodesEnum.ERR_VALIDATE_MORE_INDIVIDUAL_KPI_PERCENTAGE_014.name(), "Exceeds the individual kpi's percentage. "));
            }
        }
        return errorMessages;
    }
    
    public Map<String, String> getValidationRules(final List<StrategyProgramPolicyRuleBean> programRules) {
        final Map<String, String> validationRules = new HashMap<String, String>();
        final Map<String, List<KpiRuleBean>> validationRulesMap = new HashMap<String, List<KpiRuleBean>>();
        List<KpiRuleBean> kpiRuleList = null;
        String sysMasterBaseIdList = "";
        for (final StrategyProgramPolicyRuleBean prgmpolicyList : programRules) {
            if (prgmpolicyList.getEmployeeId() != null) {
                validationRulesMap.put("Emp", prgmpolicyList.getKpiRuleConfigMetaData());
            }
            else if (prgmpolicyList.getEmployeeGradeId() != null) {
                validationRulesMap.put("Grade", prgmpolicyList.getKpiRuleConfigMetaData());
            }
            else if (prgmpolicyList.getOrgPositionId() != null) {
                validationRulesMap.put("Pos", prgmpolicyList.getKpiRuleConfigMetaData());
            }
            else if (prgmpolicyList.getOrgUnitId() != null) {
                validationRulesMap.put("Org", prgmpolicyList.getKpiRuleConfigMetaData());
            }
            else {
                if (prgmpolicyList.getEmployeeId() != null || prgmpolicyList.getEmployeeGradeId() != null || prgmpolicyList.getOrgPositionId() != null || prgmpolicyList.getOrgUnitId() != null) {
                    continue;
                }
                validationRulesMap.put("Config", prgmpolicyList.getKpiRuleConfigMetaData());
            }
        }
        if (validationRulesMap.containsKey("Emp")) {
            kpiRuleList = validationRulesMap.get("Emp");
        }
        else if (validationRulesMap.containsKey("Grade")) {
            kpiRuleList = validationRulesMap.get("Grade");
        }
        else if (validationRulesMap.containsKey("Pos")) {
            kpiRuleList = validationRulesMap.get("Pos");
        }
        else if (validationRulesMap.containsKey("Org")) {
            kpiRuleList = validationRulesMap.get("Org");
        }
        else if (validationRulesMap.containsKey("Config")) {
            kpiRuleList = validationRulesMap.get("Config");
        }
        for (final KpiRuleBean rule : kpiRuleList) {
            validationRules.put(rule.getSystemMasterBaseData().getId() + "", rule.getValue());
            sysMasterBaseIdList = sysMasterBaseIdList + rule.getSystemMasterBaseData().getId() + ",";
        }
        if (sysMasterBaseIdList == null || sysMasterBaseIdList == "" || sysMasterBaseIdList.length() <= 0) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_VALIDATE_INVALID_INPUT_002.name(), "Default kpi config rules not defined");
        }
        sysMasterBaseIdList = sysMasterBaseIdList.substring(0, sysMasterBaseIdList.length() - 1);
        final List<SystemMasterBaseBean> sysMstrList = this.getSysMasterByIds(sysMasterBaseIdList);
        for (final SystemMasterBaseBean sysMstrBean : sysMstrList) {
            if (validationRules.containsKey(sysMstrBean.getId() + "")) {
                validationRules.put(sysMstrBean.getValue(), validationRules.get(sysMstrBean.getId() + ""));
                validationRules.remove(sysMstrBean.getId() + "");
            }
        }
        return validationRules;
    }
    
    public List<SystemMasterBaseBean> getSysMasterByIds(final String ids) {
        final SystemBaseManagerService sysMasterManagerService = (SystemBaseManagerService)this.serviceLocator.getService("SystemBaseManagerServiceImpl");
        final Request req = new Request();
        req.put(SysParams.SYS_BASE_IDS.name(), (BaseValueObject)new StringIdentifier(ids));
        final Response res = sysMasterManagerService.getSysMasterByIds(req);
        final BaseValueObjectList sysBVList = (BaseValueObjectList)res.get(SysParams.SYS_BASE_DATA_LIST.name());
        List<SystemMasterBaseBean> sysMstrList = new ArrayList<SystemMasterBaseBean>();
        if (sysBVList != null && sysBVList.getValueObjectList() != null) {
            sysMstrList = (List<SystemMasterBaseBean>)sysBVList.getValueObjectList();
        }
        return sysMstrList;
    }
}
