package com.canopus.dac;

import org.modelmapper.*;
import com.canopus.mw.dto.*;
import java.util.*;

public class MultiLangConverter<S extends MultiLangDomainObject, D extends BaseMultiLangEntity>
{
    public void map(final S source, final D destination) {
        final List<MultiLangExtension> destLangTokens = destination.getLangTokens();
        final List<MultiLangDataExtension> srcLangTokens = (List<MultiLangDataExtension>)source.getLangTokens();
        final Map<String, MultiLangExtension> origDestLocalesMap = new LinkedHashMap<String, MultiLangExtension>();
        for (final MultiLangExtension destLangToken : destLangTokens) {
            origDestLocalesMap.put(destLangToken.getLocale(), destLangToken);
        }
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(source, destination);
        final Map<String, MultiLangExtension> newDestLocalesMap = new LinkedHashMap<String, MultiLangExtension>();
        for (final MultiLangExtension  destLangToken2 : (List<MultiLangExtension>) destination.getLangTokens()) {
            newDestLocalesMap.put(destLangToken2.getLocale(), destLangToken2);
        }
        if (destination.getLangTokens() != null) {
            if (destination.getLangTokens().size() != 0) {
                for (final MultiLangDataExtension sourceLangToken : srcLangTokens) {
                    if (origDestLocalesMap.containsKey(sourceLangToken.getLocale())) {
                        final MultiLangExtension origEnt = origDestLocalesMap.get(sourceLangToken.getLocale());
                        final Integer id = origEnt.getId();
                        final Integer mainId = origEnt.getMainId();
                        modelMapper.map((Object)sourceLangToken, (Object)origEnt);
                        origEnt.setId(id);
                        origEnt.setMainId(mainId);
                    }
                    else {
                        origDestLocalesMap.put(sourceLangToken.getLocale(), newDestLocalesMap.get(sourceLangToken.getLocale()));
                    }
                }
            }
        }
        destination.getLangTokens().clear();
        destination.setLangTokens(new ArrayList<MultiLangExtension>(origDestLocalesMap.values()));
    }
}
