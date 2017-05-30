package com.kpisoft.kpi.vo.param;

public class KpiValidationParams
{
    public enum KpiType
    {
        INITIATIVE, 
        MESUREMENT;
    }
    
    public enum Uom
    {
        DATE, 
        NUMERIC;
    }
    
    public enum KpiKpiRelationship
    {
        CASCADE, 
        TRANSLATE;
    }
    
    public enum ProgramConfig
    {
        MIN_NUM_KPI, 
        MAX_NUM_KPI, 
        CORE_KPI_PERCENTAGE, 
        INDIVIDUAL_KPI_PERCENTAGE, 
        MIN_KPI_WEIGHTAGE, 
        MAX_KPI_WEIGHTAGE, 
        IS_INITIATIVE_GROUP, 
        IS_INDIVIDUAL_GROUP, 
        ALL_KPIS_CASCADED, 
        PROGRAMCONFIG, 
        KPICONFIG, 
        PROGRAM_RULES, 
        PROGRAM_ERROR_MESSAGES, 
        CUSTOM_VALIDATOR;
    }
    
    public enum SysMaster
    {
        SYS_MASTER_BASE_LIST, 
        SYS_MASTER_BASE_STR;
    }
    
    public enum KpiAggregationType
    {
        AVERAGE, 
        CONSTANT, 
        SUMMATION;
    }
    
    public enum ScoreAggregationType
    {
        CONSTANT, 
        SUMMATION;
    }
}
