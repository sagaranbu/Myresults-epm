package com.canopus.mw.util;

import java.util.*;
import java.io.*;
import org.codehaus.jackson.map.*;
import java.text.*;
import org.codehaus.jackson.annotate.*;

public final class TransformerUtil
{
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
    private static final ObjectMapper om;
    
    private TransformerUtil() {
        throw new AssertionError((Object)"Not to be instantiated.");
    }
    
    public static Map<String, Object> convertToMap(final Object obj) {
        return (Map<String, Object>)TransformerUtil.om.convertValue(obj, (Class)Map.class);
    }
    
    public static String convertToString(final Map<String, Object> map) throws IOException {
        return TransformerUtil.om.writeValueAsString((Object)map);
    }
    
    static {
        (om = new ObjectMapper()).configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        TransformerUtil.om.setDateFormat((DateFormat)new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"));
        TransformerUtil.om.setVisibility(JsonMethod.ALL, JsonAutoDetect.Visibility.NONE);
        TransformerUtil.om.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
    }
}
