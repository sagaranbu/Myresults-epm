package com.canopus.saas.dac.impl;

import com.canopus.saas.dac.*;
import org.springframework.stereotype.*;
import com.canopus.saas.dac.dao.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.dac.hibernate.*;
import com.canopus.mw.*;
import com.canopus.saas.vo.*;
import com.canopus.saas.dac.entity.*;
import org.modelmapper.*;
import com.canopus.saas.utils.*;
import com.canopus.dac.*;
import java.lang.reflect.*;
import org.springframework.transaction.annotation.*;
import com.googlecode.genericdao.search.*;
import java.util.*;
import com.canopus.mw.dto.*;
import java.io.*;
import org.slf4j.*;

@Component
public class UiLabelDataServiceImpl extends BaseDataAccessService implements UiLabelDataService
{
    @Autowired
    private UiLabelBaseDao labelBaseDao;
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public UiLabelDataServiceImpl() {
        this.labelBaseDao = null;
        this.genericDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)UiLabelBaseData.class, (Class)UiLabelBase.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)UiLabelLangData.class, (Class)UiLabelLang.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    @Transactional(readOnly = true)
    public Response getAllUILables(final Request request) {
        List<UiLabelBaseData> data = null;
        try {
            final List<UiLabelBase> result = (List<UiLabelBase>)this.labelBaseDao.findAll();
            final Type listType = new TypeToken<List<UiLabelBaseData>>() {}.getType();
            data = (List<UiLabelBaseData>)this.modelMapper.map((Object)result, listType);
            return this.OK(UiLabelParams.UI_LABEL_BASE_DATA_LIST.getParamName(), (BaseValueObject)new BaseValueObjectList((List)data));
        }
        catch (Exception e) {
            UiLabelDataServiceImpl.log.error("Exception in UiLabelDataServiceImpl - getAllUILables() : " + e);
            return this.ERROR((Exception)new DataAccessException("LABEL-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getUILablesByLocale(final Request request) {
        final StringIdentifier locale = (StringIdentifier)request.get(UiLabelParams.LOCALE_NAME.getParamName());
        if (locale == null || locale.getId() == null || locale.getId().trim().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException("INVALID_DATA", "No Locale found"));
        }
        final List<UiLabelLangData> data = new ArrayList<UiLabelLangData>();
        try {
            final Search search = new Search((Class)UiLabelLang.class);
            search.addFilterEqual("locale", (Object)locale.getId());
            final List<UiLabelLang> result = (List<UiLabelLang>)this.genericDao.search((ISearch)search);
            for (final UiLabelLang iterator : result) {
                data.add((UiLabelLangData)this.modelMapper.map((Object)iterator, (Class)UiLabelLangData.class));
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(UiLabelParams.UI_LABEL_LANG_DATA_LIST.getParamName(), (BaseValueObject)list);
        }
        catch (Exception e) {
            UiLabelDataServiceImpl.log.error("Exception in UiLabelDataServiceImpl - getUILablesByLocale() : " + e);
            return this.ERROR((Exception)new DataAccessException("LABEL-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getUiLabelByPageCodes(final Request request) {
        final BaseValueObjectList pageCodes = (BaseValueObjectList)request.get(UiLabelParams.UI_PAGE_CODES.getParamName());
        if (pageCodes == null || pageCodes.getValueObjectList() == null || pageCodes.getValueObjectList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException("INVALID_DATA", "No data object in the request"));
        }
        final List<UiLabelBaseData> data = new ArrayList<UiLabelBaseData>();
        final List<StringIdentifier> pagecodeList = (List<StringIdentifier>)pageCodes.getValueObjectList();
        final List<String> pcList = new ArrayList<String>();
        for (final StringIdentifier strId : pagecodeList) {
            pcList.add(strId.getId());
        }
        try {
            final Search search = new Search((Class)UiLabelBase.class);
            search.addFilterIn("pageCode", (Collection)pcList);
            final List<UiLabelBase> uiBaseList = (List<UiLabelBase>)this.genericDao.search((ISearch)search);
            for (final UiLabelBase iterator : uiBaseList) {
                data.add((UiLabelBaseData)this.modelMapper.map((Object)iterator, (Class)UiLabelBaseData.class));
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(UiLabelParams.UI_LABEL_BASE_DATA_LIST.getParamName(), (BaseValueObject)list);
        }
        catch (Exception e) {
            UiLabelDataServiceImpl.log.error("Exception in UiLabelDataServiceImpl - getUILablesByLocale() : " + e);
            return this.ERROR((Exception)new DataAccessException("LABEL-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchLabelLang(final Request request) {
        final UiLabelLangData uiLabelLangData = (UiLabelLangData)request.get(UiLabelParams.UI_LABEL_LANG_DATA.name());
        BaseValueObjectList list = null;
        if (uiLabelLangData == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_DATA", "No data object in the request"));
        }
        try {
            final Search search = new Search((Class)UiLabelLang.class);
            search.addFilterEqual("displayName", (Object)uiLabelLangData.getDisplayName());
            final List<UiLabelLang> labelLangs = (List<UiLabelLang>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<UiLabelLangData>>() {}.getType();
            final List<? extends BaseValueObject> langBVList = (List<? extends BaseValueObject>)this.modelMapper.map((Object)labelLangs, listType);
            list = new BaseValueObjectList();
            list.setValueObjectList((List)langBVList);
        }
        catch (Exception e) {
            return this.ERROR((Exception)new DataAccessException("LABEL-DAC-000", "Unknown error while searching the label lang ", (Throwable)e));
        }
        final Response response = this.OK(UiLabelParams.UI_LABEL_LANG_DATA_LIST.name(), (BaseValueObject)list);
        return response;
    }
    
    @Transactional
    public Response saveLabelData(final Request request) {
        final UiLabelBaseData uiLabelData = (UiLabelBaseData)request.get(UiLabelParams.UI_BASE_DATA.getParamName());
        if (uiLabelData == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_DATA", "No data object in the request"));
        }
        try {
            final UiLabelBase base = new UiLabelBase();
            this.modelMapper.map((Object)uiLabelData, (Object)base);
            this.labelBaseDao.save(base);
            return this.OK(UiLabelParams.UI_BASE_ID.getParamName(), (BaseValueObject)new Identifier(uiLabelData.getId()));
        }
        catch (Exception e) {
            UiLabelDataServiceImpl.log.error("Exception in UiLabelDataServiceImpl - saveLabelData() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException("LABEL-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getUILableById(final Request request) {
        final Identifier baseId = (Identifier)request.get(UiLabelParams.UI_BASE_ID.getParamName());
        if (baseId == null || baseId.getId() == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_DATA", "No data object in the request"));
        }
        try {
            final UiLabelBase base = (UiLabelBase)this.labelBaseDao.find((Serializable)baseId.getId());
            final UiLabelBaseData baseData = new UiLabelBaseData();
            this.modelMapper.map((Object)base, (Object)baseData);
            return this.OK(UiLabelParams.UI_BASE_DATA.getParamName(), (BaseValueObject)baseData);
        }
        catch (Exception e) {
            UiLabelDataServiceImpl.log.error("Exception in UiLabelDataServiceImpl - getUILableById() : " + e);
            return this.ERROR((Exception)new DataAccessException("LABEL-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)UiLabelDataServiceImpl.class);
    }
}
