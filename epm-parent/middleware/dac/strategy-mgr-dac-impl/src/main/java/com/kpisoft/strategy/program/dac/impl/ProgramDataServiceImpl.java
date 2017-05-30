package com.kpisoft.strategy.program.dac.impl;

import com.kpisoft.strategy.profram.dac.*;
import org.springframework.stereotype.*;
import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.dac.hibernate.*;
import com.kpisoft.strategy.program.dac.dao.*;
import org.modelmapper.convention.*;
import com.kpisoft.strategy.program.vo.*;
import com.kpisoft.strategy.params.*;
import com.kpisoft.strategy.utility.*;
import com.canopus.dac.*;
import org.springframework.transaction.annotation.*;
import java.io.*;
import java.text.*;
import org.modelmapper.*;
import java.lang.reflect.*;
import com.canopus.mw.dto.param.*;
import com.canopus.dac.utils.*;
import com.kpisoft.strategy.program.dac.impl.domain.*;
import com.googlecode.genericdao.search.*;
import com.canopus.mw.*;
import com.canopus.mw.dto.*;
import java.util.*;

@Service
public class ProgramDataServiceImpl extends BaseDataAccessService implements ProgramDataService
{
    private static final Logger logger;
    @Autowired
    private PerformanceProgramDao performanceProgramDao;
    @Autowired
    private ProgramZoneDao programZoneDao;
    @Autowired
    private GenericHibernateDao genericDao;
    @Autowired
    private StrategyProgramPolicyRuleDao strategyProgramPolicyRuleDao;
    private ModelMapper modelMapper;
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public ProgramDataServiceImpl() {
        this.performanceProgramDao = null;
        this.programZoneDao = null;
        this.genericDao = null;
        this.strategyProgramPolicyRuleDao = null;
        this.modelMapper = null;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)StrategyProgramMetaData.class, (Class)StrategyProgramBean.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)ProgramZoneMetaData.class, (Class)ProgramZoneBean.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)StrategyProgramPolicyRuleMetaData.class, (Class)StrategyProgramPolicyRuleBean.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)ExclusionsBean.class, (Class)ExclusionsMetaData.class);
    }
    
    @Transactional
    public Response savePerformanceProgram(final Request request) {
        final StrategyProgramBean strategyProgramBean = (StrategyProgramBean)request.get(ProgramParams.STRATEGY_PROGRAM.name());
        if (strategyProgramBean == null) {
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PERF_PROG_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        final StrategyProgramMetaData strategyProgramMetaData = new StrategyProgramMetaData();
        try {
            this.modelMapper.map((Object)strategyProgramBean, (Object)strategyProgramMetaData);
            if (this.performanceProgramDao.isAttached(strategyProgramMetaData)) {
                this.performanceProgramDao.merge((Object)strategyProgramMetaData);
            }
            else {
                this.performanceProgramDao.save(strategyProgramMetaData);
            }
            if (strategyProgramMetaData.getExclusionsMetaData() != null && !strategyProgramMetaData.getExclusionsMetaData().isEmpty()) {
                for (final ExclusionsMetaData iterator : strategyProgramMetaData.getExclusionsMetaData()) {
                    iterator.setProgramId(strategyProgramMetaData.getId());
                }
            }
            if (strategyProgramMetaData.getProgramZoneMetaData() != null && !strategyProgramMetaData.getProgramZoneMetaData().isEmpty()) {
                for (final ProgramZoneMetaData iterator2 : strategyProgramMetaData.getProgramZoneMetaData()) {
                    iterator2.setProgramId(strategyProgramMetaData.getId());
                }
            }
            if (strategyProgramMetaData.getStrategyProgramPolicyRuleMetaData() != null && !strategyProgramMetaData.getStrategyProgramPolicyRuleMetaData().isEmpty()) {
                for (final StrategyProgramPolicyRuleMetaData iterator3 : strategyProgramMetaData.getStrategyProgramPolicyRuleMetaData()) {
                    iterator3.setProgramId(strategyProgramMetaData.getId());
                    if (iterator3.getKpiRuleConfigMetaData() != null && !iterator3.getKpiRuleConfigMetaData().isEmpty()) {
                        for (final KpiRuleConfigMetaData iterator4 : iterator3.getKpiRuleConfigMetaData()) {
                            iterator4.setProgramPolicyId(iterator3.getId());
                        }
                    }
                }
            }
            strategyProgramBean.setId(strategyProgramMetaData.getId());
            return this.OK(ProgramParams.STRATEGY_PROGRAM.name(), (BaseValueObject)strategyProgramBean);
        }
        catch (Exception ex) {
            ProgramDataServiceImpl.logger.error((Object)"Exception in ProgramDataServiceImpl - savePerformanceProgram() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_CREATE_003.name(), "Unknown error while creating program", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getPerformanceProgram(final Request request) {
        Response response = null;
        StrategyProgramBean objStrategyProgramBean = null;
        final Identifier identifier = (Identifier)request.get(ProgramParams.STRATEGY_PROGRAM_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PERF_PROG_INVALID_INPUT_002.name(), "No data found in request"));
        }
        try {
            StrategyProgramMetaData objStrategyProgramMetaData = null;
            if (identifier.getId() != null && identifier.getId() > 0) {
                objStrategyProgramMetaData = (StrategyProgramMetaData)this.performanceProgramDao.find((Serializable)identifier.getId());
                if (objStrategyProgramMetaData == null) {
                    return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PERF_PROG_DOES_NOT_EXIST_001.name(), "Invalid Program ID"));
                }
                objStrategyProgramBean = (StrategyProgramBean)this.modelMapper.map((Object)objStrategyProgramMetaData, (Class)StrategyProgramBean.class);
                response = new Response();
                response.put(ProgramParams.STRATEGY_PROGRAM.name(), (BaseValueObject)objStrategyProgramBean);
            }
        }
        catch (Exception ex) {
            ProgramDataServiceImpl.logger.error((Object)"Exception in ProgramDataServiceImpl - getPerformanceProgram() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_GET_004.name(), "Unknown error while fetching program", (Throwable)ex));
        }
        return response;
    }
    
    @Transactional
    public Response removePerformanceProgram(final Request request) {
        final Identifier identifier = (Identifier)request.get(ProgramParams.STRATEGY_PROGRAM_ID.name());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PERF_PROG_INVALID_INPUT_002.name(), "Program Id is required"));
        }
        try {
            final boolean isRemove = this.performanceProgramDao.removeById((Serializable)identifier.getId());
            return this.OK(ProgramParams.STATUS.name(), (BaseValueObject)new BooleanResponse(isRemove));
        }
        catch (Exception ex) {
            ProgramDataServiceImpl.logger.error((Object)"Exception in ProgramDataServiceImpl - removePerformanceProgram() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_DELETE_006.name(), "Unknown error while removing program", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getProgramZoneByProgramId(final Request request) {
        final Identifier progId = (Identifier)request.get(ProgramParams.STRATEGY_PROGRAM_ID.name());
        if (progId == null || progId.getId() == null || progId.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PROG_ZONE_INVALID_INPUT_002.name(), "No data found"));
        }
        ProgramZoneMetaData objProgramZoneMetaData = null;
        try {
            final Search objSearch = new Search((Class)ProgramZoneMetaData.class);
            objSearch.addFilterEqual("programId", (Object)progId.getId());
            objProgramZoneMetaData = (ProgramZoneMetaData)this.programZoneDao.searchUnique((ISearch)objSearch);
            final ProgramZoneBean programZoneBean = (ProgramZoneBean)this.modelMapper.map((Object)objProgramZoneMetaData, (Class)ProgramZoneBean.class);
            return this.OK(ProgramParams.PROGRAM_ZONE.name(), (BaseValueObject)programZoneBean);
        }
        catch (Exception ex) {
            ProgramDataServiceImpl.logger.error((Object)"Exception in ProgramDataServiceImpl - getProgramZoneByProgramId() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PROG_ZONE_UNABLE_TO_GET_004.name(), "Unknown error while fetching zone by program id", (Throwable)ex));
        }
    }
    
    @Transactional
    public Response addOrgUnitToProgramZone(final Request request) {
        ProgramZoneBean programZoneBean = (ProgramZoneBean)request.get(ProgramParams.PROGRAM_ZONE.name());
        if (programZoneBean == null) {
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PROG_ZONE_INVALID_INPUT_002.name(), "No Data Found in request"));
        }
        if (programZoneBean.getProgramId() == null || programZoneBean.getProgramId() <= 0 || programZoneBean.getOrgUnitId() == null || programZoneBean.getOrgUnitId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PROG_ZONE_INVALID_INPUT_002.name(), "Invalid data"));
        }
        ProgramZoneMetaData programZoneMetaData = null;
        try {
            programZoneMetaData = (ProgramZoneMetaData)this.modelMapper.map((Object)programZoneBean, (Class)ProgramZoneMetaData.class);
            this.programZoneDao.save(programZoneMetaData);
            programZoneBean = (ProgramZoneBean)this.modelMapper.map((Object)programZoneMetaData, (Class)ProgramZoneBean.class);
            return this.OK(ProgramParams.PROGRAM_ZONE.name(), (BaseValueObject)programZoneBean);
        }
        catch (Exception ex) {
            ProgramDataServiceImpl.logger.error((Object)"Exception in ProgramDataServiceImpl - addOrgUnitToProgramZone() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PROG_ZONE_UNABLE_TO_ADD_ORG_009.name(), "Unknown error while adding organization unit to program zone", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getProgramRules(final Request request) {
        Response response = null;
        final StrategyProgramPolicyRuleBean strategyProgramPolicyRuleBean = (StrategyProgramPolicyRuleBean)request.get(ProgramParams.PROGRAM_POLICY_RULE.name());
        if (strategyProgramPolicyRuleBean == null) {
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PROG_POLICY_RULE_INVALID_INPUT_002.name(), "No record found in request"));
        }
        String str = "";
        try {
            if (strategyProgramPolicyRuleBean.getOrgPositionId() != null && strategyProgramPolicyRuleBean.getOrgPositionId() > 0) {
                str = str + "orgPositionId = " + strategyProgramPolicyRuleBean.getOrgPositionId() + " OR ";
            }
            if (strategyProgramPolicyRuleBean.getEmployeeGradeId() != null && strategyProgramPolicyRuleBean.getEmployeeGradeId() > 0) {
                str = str + "employeeGradeId = " + strategyProgramPolicyRuleBean.getEmployeeGradeId() + " OR ";
            }
            if (strategyProgramPolicyRuleBean.getEmployeeId() != null && strategyProgramPolicyRuleBean.getEmployeeId() > 0) {
                str = str + "employeeId = " + strategyProgramPolicyRuleBean.getEmployeeId() + " OR ";
            }
            if (strategyProgramPolicyRuleBean.getOrgUnitId() != null && strategyProgramPolicyRuleBean.getOrgUnitId() > 0) {
                str = str + "orgUnitId = " + strategyProgramPolicyRuleBean.getOrgUnitId() + " OR ";
            }
            if (strategyProgramPolicyRuleBean.getStartDate() != null) {
                final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
                final String s = formatter.format(strategyProgramPolicyRuleBean.getStartDate());
                str = str + "startDate = '" + s + "' OR ";
            }
            if (strategyProgramPolicyRuleBean.getEndDate() != null) {
                final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
                final String s = formatter.format(strategyProgramPolicyRuleBean.getStartDate());
                str = str + "endDate = '" + s + "' OR ";
            }
            str = str.substring(0, str.length() - 3);
            final Search search = new Search((Class)StrategyProgramPolicyRuleMetaData.class);
            search.addFilterCustom(str);
            final List<StrategyProgramPolicyRuleMetaData> alStrategyProgramPolicyRuleMetaData = (List<StrategyProgramPolicyRuleMetaData>)this.strategyProgramPolicyRuleDao.search((ISearch)search);
            final Type listType = new TypeToken<List<StrategyProgramPolicyRuleBean>>() {}.getType();
            final List<? extends BaseValueObject> programBVList = (List<? extends BaseValueObject>)this.modelMapper.map((Object)alStrategyProgramPolicyRuleMetaData, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)programBVList);
            response = new Response();
            response.put(ProgramParams.PROGRAM_POLICY_RULE_LIST.name(), (BaseValueObject)list);
            return response;
        }
        catch (Exception e) {
            ProgramDataServiceImpl.logger.error((Object)"Exception in ProgramDataServiceImpl - getProgramRules() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PROG_POLICY_RULE_UNABLE_TO_GET_004.name(), "Unknown error while fetchinf the program rules", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllProgram(final Request request) {
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        BaseValueObjectList list = null;
        try {
            final Search search = new Search((Class)StrategyProgramMetaData.class);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField, HeaderParam.DESC.name());
            final List<StrategyProgramMetaData> alStrategyProgramMetaData = (List<StrategyProgramMetaData>)this.performanceProgramDao.search((ISearch)search);
            if (alStrategyProgramMetaData == null) {
                return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PERF_PROG_DOES_NOT_EXIST_001.name(), "No data found"));
            }
            final Type listType = new TypeToken<List<StrategyProgramBean>>() {}.getType();
            final List<? extends BaseValueObject> programBVList = (List<? extends BaseValueObject>)this.modelMapper.map((Object)alStrategyProgramMetaData, listType);
            list = new BaseValueObjectList();
            list.setValueObjectList((List)programBVList);
            final Response response = this.OK(ProgramParams.STRATEGY_PROGRAM_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            ProgramDataServiceImpl.logger.error((Object)"Exception in ProgramDataServiceImpl - getAllProgram() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_GET_ALL_007.name(), e.getMessage()));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchProgram(final Request request) {
        final StrategyProgramBean strProgram = (StrategyProgramBean)request.get(ProgramParams.STRATEGY_PROGRAM.name());
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        BaseValueObjectList list = null;
        if (strProgram == null) {
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PERF_PROG_INVALID_INPUT_002.name(), "No data found"));
        }
        try {
            final Search search = new Search((Class)StrategyProgramMetaDataBase.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final StrategyProgramMetaDataBase prgBase = (StrategyProgramMetaDataBase)this.modelMapper.map((Object)strProgram, (Class)StrategyProgramMetaDataBase.class);
            final Filter filter = this.genericDao.getFilterFromExample((Object)prgBase, options);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField, HeaderParam.DESC.name());
            final List<StrategyProgramMetaDataBase> prgList = (List<StrategyProgramMetaDataBase>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<StrategyProgramBean>>() {}.getType();
            final List<? extends BaseValueObject> prgDataList = (List<? extends BaseValueObject>)this.modelMapper.map((Object)prgList, listType);
            list = new BaseValueObjectList();
            list.setValueObjectList((List)prgDataList);
            final Response response = this.OK(ProgramParams.STRATEGY_PROGRAM_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            ProgramDataServiceImpl.logger.error((Object)"Exception in ProgramDataServiceImpl - searchProgram() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_SEARCH_008.name(), "Unknown error while loading program", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getProgramsByPeriodType(final Request request) {
        final Identifier id = (Identifier)request.get(ProgramParams.PERIOD_TYPE_ID.name());
        final SortList sortList = request.getSortList();
        final String defaultField = "displayOrderId";
        if (id == null) {
            return this.ERROR((Exception)new MiddlewareException(StrategyErrorCodesEnum.ERR_PERF_PROG_INVALID_INPUT_002.name(), "Period Type Id is required"));
        }
        try {
            final Search search = new Search((Class)StrategyProgramMetaData.class);
            search.addFilterEqual("periodTypeId", (Object)id.getId());
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField, HeaderParam.DESC.name());
            final List<StrategyProgramMetaData> programs = (List<StrategyProgramMetaData>)this.performanceProgramDao.search((ISearch)search);
            final List<StrategyProgramBean> programList = new ArrayList<StrategyProgramBean>();
            for (final StrategyProgramMetaData iterator : programs) {
                programList.add((StrategyProgramBean)this.modelMapper.map((Object)iterator, (Class)StrategyProgramBean.class));
            }
            final BaseValueObjectList bvol = new BaseValueObjectList();
            bvol.setValueObjectList((List)programList);
            final Response response = this.OK(ProgramParams.STRATEGY_PROGRAM_LIST.name(), (BaseValueObject)bvol);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            ProgramDataServiceImpl.logger.error((Object)"Exception in ProgramDataServiceImpl - getProgramsByPeriodType() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_GET_BY_PRD_TYPE_009.name(), "Unknown error while saving kpi", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getProgramsByIds(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(ProgramParams.STRATEGY_PROGRAM_ID_LIST.name());
        if (idList == null) {
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PERF_PROG_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        final List<Integer> prgIdList = new ArrayList<Integer>();
        for (final Integer prgId : idList.getIdsList()) {
            prgIdList.add(prgId);
        }
        try {
            BaseValueObjectList bvol = null;
            final Search search = new Search((Class)StrategyProgramMetaData.class);
            search.addFilterIn("id", (Collection)prgIdList);
            final List<StrategyProgramMetaData> list = (List<StrategyProgramMetaData>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<StrategyProgramBean>>() {}.getType();
            final List<StrategyProgramBean> kpiList = (List<StrategyProgramBean>)this.modelMapper.map((Object)list, listType);
            bvol = new BaseValueObjectList();
            bvol.setValueObjectList((List)kpiList);
            final Response response = this.OK(ProgramParams.STRATEGY_PROGRAM_LIST.name(), (BaseValueObject)bvol);
            return response;
        }
        catch (Exception ex) {
            ProgramDataServiceImpl.logger.error((Object)"Exception in ProgramDataServiceImpl - getProgramsByIds() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_GET_BY_IDS_10.name(), "Unknown error while getting program by ids", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getProgramsForEmployee(final Request request) {
        final Identifier empId = (Identifier)request.get(ProgramParams.EMP_ID.name());
        final Identifier orgId = (Identifier)request.get(ProgramParams.ORG_ID.name());
        final Identifier grdId = (Identifier)request.get(ProgramParams.GRADE_ID.name());
        final Identifier progId = (Identifier)request.get(ProgramParams.POSITION_ID.name());
        String str = "";
        try {
            if (empId.getId() != null && empId.getId() > 0) {
                str = str + "employeeId = " + empId.getId() + " OR ";
            }
            if (orgId.getId() != null && orgId.getId() > 0) {
                str = str + "orgUnitId = " + orgId.getId() + " OR ";
            }
            if (grdId.getId() != null && grdId.getId() > 0) {
                str = str + "employeeGradeId = " + grdId.getId() + " OR ";
            }
            if (progId.getId() != null && progId.getId() > 0) {
                str = str + "orgPositionId = " + progId.getId() + " OR ";
            }
            str = str.substring(0, str.length() - 3);
            final Search search = new Search((Class)StrategyProgramPolicyRuleMetaData.class);
            search.addFilterCustom(str);
            final List<StrategyProgramPolicyRuleMetaData> alStrategyProgramPolicyRuleMetaData = (List<StrategyProgramPolicyRuleMetaData>)this.strategyProgramPolicyRuleDao.search((ISearch)search);
            final Type listType = new TypeToken<List<StrategyProgramPolicyRuleBean>>() {}.getType();
            final List<StrategyProgramPolicyRuleBean> programBVList = (List<StrategyProgramPolicyRuleBean>)this.modelMapper.map((Object)alStrategyProgramPolicyRuleMetaData, listType);
            final List<Integer> pIdList = new ArrayList<Integer>();
            for (final StrategyProgramPolicyRuleBean bean : programBVList) {
                pIdList.add(bean.getProgramId());
            }
            final IdentifierList idList = new IdentifierList((List)pIdList);
            return this.OK(ProgramParams.PROGRAM_POLICY_RULE_LIST.name(), (BaseValueObject)idList);
        }
        catch (Exception e) {
            ProgramDataServiceImpl.logger.error((Object)"Exception in ProgramDataServiceImpl - getProgramsForEmployee() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PROG_POLICY_RULE_UNABLE_TO_GET_FOR_EMP_010.name(), "Unknown error while fetching program for given employee", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchProgramPolicyRule(final Request request) {
        final Identifier id = (Identifier)request.get(ProgramParams.ORG_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PROG_POLICY_RULE_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final StrategyProgramPolicyRuleBean ruleBean = new StrategyProgramPolicyRuleBean();
            ruleBean.setOrgUnitId(id.getId());
            final StrategyProgramPolicyRuleMetaData ruleMetaData = (StrategyProgramPolicyRuleMetaData)this.modelMapper.map((Object)ruleBean, (Class)StrategyProgramPolicyRuleMetaData.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.genericDao.getFilterFromExample((Object)ruleMetaData, options);
            final Search search = new Search((Class)StrategyProgramPolicyRuleMetaData.class);
            search.addFilter(filter);
            final List<StrategyProgramPolicyRuleMetaData> result = (List<StrategyProgramPolicyRuleMetaData>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<StrategyProgramPolicyRuleBean>>() {}.getType();
            final List<StrategyProgramPolicyRuleBean> progList = (List<StrategyProgramPolicyRuleBean>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList bList = new BaseValueObjectList();
            bList.setValueObjectList((List)progList);
            return this.OK(ProgramParams.PROGRAM_POLICY_RULE_LIST.name(), (BaseValueObject)bList);
        }
        catch (Exception e) {
            ProgramDataServiceImpl.logger.error((Object)"Exception in ProgramDataServiceImpl - searchProgramPolicyRule() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PROG_POLICY_RULE_UNABLE_TO_SEARCH_008.name(), "Unknown error while searching the program rules for given org unit", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getProgramZoneById(final Request request) {
        final Identifier programZoneIdentifier = (Identifier)request.get(ProgramParams.PROGRAM_ZONE_ID.name());
        if (programZoneIdentifier == null || programZoneIdentifier.getId() == null || programZoneIdentifier.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PROG_ZONE_INVALID_INPUT_002.name(), "No data found"));
        }
        ProgramZoneMetaData programZone = null;
        try {
            programZone = (ProgramZoneMetaData)this.programZoneDao.find((Serializable)programZoneIdentifier.getId());
            final ProgramZoneBean programZoneVO = (ProgramZoneBean)this.modelMapper.map((Object)programZone, (Class)ProgramZoneBean.class);
            return this.OK(ProgramParams.PROGRAM_ZONE.name(), (BaseValueObject)programZoneVO);
        }
        catch (Exception ex) {
            ProgramDataServiceImpl.logger.error((Object)"Exception in ProgramDataServiceImpl - getProgramZoneById() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PROG_ZONE_UNABLE_TO_GET_004.name(), "Unknown error while fetching zone by ProgramZone", (Throwable)ex));
        }
    }
    
    @Transactional
    public Response searchExclusionMetaData(final Request request) {
        final ExclusionsBean exclusionsBean = (ExclusionsBean)request.get(ProgramParams.EXCLUSION_DATA.name());
        BaseValueObjectList list = null;
        if (exclusionsBean == null) {
            throw new DataAccessException(StrategyErrorCodesEnum.ERR_EXCLISIONS_INVALID_INPUT_002.name(), "No data object in the request");
        }
        try {
            String str = "";
            if (exclusionsBean.getOrgPositionId() != null && exclusionsBean.getOrgPositionId() > 0) {
                str = str + "orgPositionId = " + exclusionsBean.getOrgPositionId() + " OR ";
            }
            if (exclusionsBean.getEmployeeGradeId() != null && exclusionsBean.getEmployeeGradeId() > 0) {
                str = str + "employeeGradeId = " + exclusionsBean.getEmployeeGradeId() + " OR ";
            }
            if (exclusionsBean.getEmployeeId() != null && exclusionsBean.getEmployeeId() > 0) {
                str = str + "employeeId = " + exclusionsBean.getEmployeeId() + " OR ";
            }
            if (exclusionsBean.getOrgUnitId() != null && exclusionsBean.getOrgUnitId() > 0) {
                str = str + "orgUnitId = " + exclusionsBean.getOrgUnitId() + " OR ";
            }
            if (str != "") {
                str = str.substring(0, str.length() - 3);
            }
            final Search search = new Search((Class)ExclusionsMetaData.class);
            search.addFilterCustom(str);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final ExclusionsMetaData exclusionsMetaData = (ExclusionsMetaData)this.modelMapper.map((Object)exclusionsBean, (Class)ExclusionsMetaData.class);
            final Filter filter = this.genericDao.getFilterFromExample((Object)exclusionsMetaData, options);
            search.addFilter(filter);
            final List<ExclusionsMetaData> exclusionsBeans = (List<ExclusionsMetaData>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<ExclusionsBean>>() {}.getType();
            final List<? extends BaseValueObject> exBvList = (List<? extends BaseValueObject>)this.modelMapper.map((Object)exclusionsBeans, listType);
            list = new BaseValueObjectList();
            list.setValueObjectList((List)exBvList);
        }
        catch (Exception e) {
            ProgramDataServiceImpl.logger.error((Object)"Exception in ProgramDataServiceImpl - searchExclusionMetaData() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_EXCLISIONS_UNABLE_TO_SEARCH_004.name(), "Unknown error while searching the exclusion", (Throwable)e));
        }
        final Response response = this.OK(ProgramParams.EXCLUSION_DATA_LIST.name(), (BaseValueObject)list);
        return response;
    }
    
    @Transactional(readOnly = true)
    public Response getProgramsByZoneIds(final Request request) {
        final IdentifierList zoneIds = (IdentifierList)request.get(ProgramParams.PROGRAM_ZONE_ID_LIST.name());
        List<Integer> progIdList = null;
        if (zoneIds == null || zoneIds.getIdsList().size() < 0) {
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PERF_PROG_INVALID_INPUT_002.name(), "No data found"));
        }
        try {
            final List<Integer> progZoneIds = (List<Integer>)zoneIds.getIdsList();
            progIdList = this.programZoneDao.getProgsByProgZoneIdList(progZoneIds);
            return this.OK(ProgramParams.PROG_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)progIdList));
        }
        catch (Exception ex) {
            ProgramDataServiceImpl.logger.error((Object)"Exception in ProgramDataServiceImpl - getProgramsByZoneIds() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(StrategyErrorCodesEnum.ERR_PERF_PROG_UNABLE_TO_GET_BY_ZONE_IDS_11.name(), "Unknown error while fetching progs by ProgramZone ids", (Throwable)ex));
        }
    }
    
    static {
        logger = Logger.getLogger((Class)ProgramDataServiceImpl.class);
    }
}
