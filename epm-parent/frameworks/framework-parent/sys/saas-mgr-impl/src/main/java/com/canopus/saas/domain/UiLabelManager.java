package com.canopus.saas.domain;

import com.canopus.mw.*;
import org.springframework.stereotype.*;
import com.canopus.saas.dac.*;
import com.canopus.saas.vo.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import org.perf4j.aop.*;
import javax.annotation.*;
import com.canopus.saas.utils.*;
import java.util.*;
import com.canopus.mw.dto.*;

@Component
public class UiLabelManager extends BaseDomainManager
{
    @Autowired
    private UiLabelDataService dataService;
    private Map<Integer, UiLabelBaseData> basicMap;
    private Map<String, List<UiLabelBaseData>> pageCodeMap;
    private Map<String, List<UiLabelLangData>> localeMap;
    private Map<String, List<UiLabelLangData>> errorMap;
    @Value("${cacheOnStartup:true}")
    private boolean cacheOnStartup;
    private static final Logger log;
    
    public UiLabelManager() {
        this.dataService = null;
        this.basicMap = null;
        this.pageCodeMap = null;
        this.localeMap = null;
        this.errorMap = null;
        this.cacheOnStartup = false;
        this.basicMap = new HashMap<Integer, UiLabelBaseData>();
        this.pageCodeMap = new HashMap<String, List<UiLabelBaseData>>();
        this.localeMap = new HashMap<String, List<UiLabelLangData>>();
        this.errorMap = new HashMap<String, List<UiLabelLangData>>();
    }
    
    @Profiled(tag = "UI Label Manager init")
    @PostConstruct
    public void loadToCache() {
        if (!this.cacheOnStartup) {
            return;
        }
        UiLabelManager.log.debug((Object)"UI label manager initializing");
        ExecutionContext.getCurrent().setCrossTenant();
        try {
            this.loadAllUiLabels();
        }
        catch (Exception ex) {}
        ExecutionContext.getCurrent().unSetCrossTenant();
    }
    
    public void loadAllUiLabels() {
        this.basicMap.clear();
        this.pageCodeMap.clear();
        this.localeMap.clear();
        this.errorMap.clear();
        try {
            final Response response = this.dataService.getAllUILables(new Request());
            final BaseValueObjectList list = (BaseValueObjectList)response.get(UiLabelParams.UI_LABEL_BASE_DATA_LIST.name());
            if (list != null && list.getValueObjectList() != null && !list.getValueObjectList().isEmpty()) {
                final List<UiLabelBaseData> result = (List<UiLabelBaseData>)list.getValueObjectList();
                for (final UiLabelBaseData iterator : result) {
                    this.basicMap.put(iterator.getId(), iterator);
                    if (this.pageCodeMap.containsKey(iterator.getPageCode())) {
                        final List<UiLabelBaseData> data = this.pageCodeMap.get(iterator.getPageCode());
                        data.add(iterator);
                    }
                    else {
                        final List<UiLabelBaseData> data = new ArrayList<UiLabelBaseData>();
                        data.add(iterator);
                        this.pageCodeMap.put(iterator.getPageCode(), data);
                    }
                    for (final UiLabelLangData langIterator : iterator.getLabelLangData()) {
                        if (this.localeMap.containsKey(langIterator.getLocale())) {
                            final List<UiLabelLangData> data2 = this.localeMap.get(langIterator.getLocale());
                            data2.add(langIterator);
                        }
                        else {
                            final List<UiLabelLangData> data2 = new ArrayList<UiLabelLangData>();
                            data2.add(langIterator);
                            this.localeMap.put(langIterator.getLocale(), data2);
                        }
                    }
                }
                final List<UiLabelBaseData> data3 = this.pageCodeMap.get("error999");
                if (data3 != null && !data3.isEmpty()) {
                    final List<UiLabelLangData> langList = (List<UiLabelLangData>)data3.get(0).getLabelLangData();
                    if (langList != null && !langList.isEmpty()) {
                        for (final UiLabelLangData uiLabelLangData : langList) {
                            if (this.errorMap.containsKey(uiLabelLangData.getDisplayName())) {
                                final List<UiLabelLangData> langData = this.errorMap.get(uiLabelLangData.getDisplayName());
                                langData.add(uiLabelLangData);
                            }
                            else {
                                final List<UiLabelLangData> langData = new ArrayList<UiLabelLangData>();
                                langData.add(uiLabelLangData);
                                this.errorMap.put(uiLabelLangData.getDisplayName(), langData);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            UiLabelManager.log.error((Object)("Exception in UiLabelManager - loadAllUiLabels() : " + e));
        }
    }
    
    public List<UiLabelBaseData> getAllUILables() {
        final List<UiLabelBaseData> result = new ArrayList<UiLabelBaseData>();
        if (this.basicMap.isEmpty()) {
            this.loadAllUiLabels();
        }
        for (final Integer key : this.basicMap.keySet()) {
            result.add(this.basicMap.get(key));
        }
        return result;
    }
    
    public List<UiLabelLangData> getUILablesByLocale(final String locale) {
        final List<UiLabelLangData> data = this.localeMap.get(locale);
        return data;
    }
    
    public List<UiLabelBaseData> getUiLabelByPageCodes(final BaseValueObjectList baseValObjList) {
        final List<UiLabelBaseData> data = new ArrayList<UiLabelBaseData>();
        final List<StringIdentifier> pagecodeList = (List<StringIdentifier>)baseValObjList.getValueObjectList();
        if (pagecodeList != null && !pagecodeList.isEmpty()) {
            for (final StringIdentifier iterator : pagecodeList) {
                final List<UiLabelBaseData> baseData = this.pageCodeMap.get(iterator.getId());
                if (baseData != null && !baseData.isEmpty()) {
                    data.addAll(baseData);
                }
            }
        }
        return data;
    }
    
    public List<UiLabelLangData> searchLabelLang(final UiLabelLangData labelLangData) {
        final Request request = new Request();
        request.put(UiLabelParams.UI_LABEL_LANG_DATA.name(), (BaseValueObject)labelLangData);
        final Response response = this.dataService.searchLabelLang(request);
        final BaseValueObjectList bList = (BaseValueObjectList)response.get(UiLabelParams.UI_LABEL_LANG_DATA_LIST.name());
        final List<UiLabelLangData> labelLangDatas = (List<UiLabelLangData>)bList.getValueObjectList();
        return labelLangDatas;
    }
    
    public UiLabelLangData getUiLabelLangByErrorCode(final String errorCode, final String locale) {
        UiLabelLangData langData = new UiLabelLangData();
        final List<UiLabelLangData> langList = this.errorMap.get(errorCode);
        if (langList != null && langList.size() > 0) {
            for (final UiLabelLangData uiLabelLangData : langList) {
                if (uiLabelLangData.getLocale().equals(locale)) {
                    langData = uiLabelLangData;
                    break;
                }
            }
        }
        return langData;
    }
    
    public UiLabelBaseData getUILableById(final Integer baseDataId) {
        UiLabelBaseData baseData = null;
        if (this.basicMap.containsKey(baseDataId) && this.basicMap.get(baseDataId) != null) {
            baseData = this.basicMap.get(baseDataId);
        }
        else {
            final Request request = new Request();
            request.put(UiLabelParams.UI_BASE_ID.name(), (BaseValueObject)new Identifier(baseDataId));
            baseData = (UiLabelBaseData)this.dataService.getUILableById(request).get(UiLabelParams.UI_BASE_DATA.name());
        }
        return baseData;
    }
    
    public Integer saveLabelData(UiLabelBaseData baseData) {
        final Request request = new Request();
        request.put(UiLabelParams.UI_BASE_DATA.name(), (BaseValueObject)baseData);
        final Response response = this.dataService.saveLabelData(request);
        final Identifier baseId = (Identifier)response.get(UiLabelParams.UI_BASE_ID.getParamName());
        if (baseId != null && baseId.getId() != null) {
            this.basicMap.put(baseId.getId(), null);
            baseData = this.getUILableById(baseId.getId());
            this.basicMap.put(baseId.getId(), baseData);
            if (this.pageCodeMap.containsKey(baseData.getPageCode())) {
                final List<UiLabelBaseData> data = this.pageCodeMap.get(baseData.getPageCode());
                data.add(baseData);
            }
            else {
                final List<UiLabelBaseData> data = new ArrayList<UiLabelBaseData>();
                data.add(baseData);
                this.pageCodeMap.put(baseData.getPageCode(), data);
            }
            for (final UiLabelLangData langIterator : baseData.getLabelLangData()) {
                if (this.localeMap.containsKey(langIterator.getLocale())) {
                    final List<UiLabelLangData> data2 = this.localeMap.get(langIterator.getLocale());
                    data2.add(langIterator);
                }
                else {
                    final List<UiLabelLangData> data2 = new ArrayList<UiLabelLangData>();
                    data2.add(langIterator);
                    this.localeMap.put(langIterator.getLocale(), data2);
                }
            }
            return baseId.getId();
        }
        return null;
    }
    
    static {
        log = Logger.getLogger((Class)UiLabelManager.class);
    }
}
