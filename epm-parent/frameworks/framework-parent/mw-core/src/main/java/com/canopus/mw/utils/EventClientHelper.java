package com.canopus.mw.utils;

import java.util.*;
import com.canopus.mw.dto.*;

public class EventClientHelper
{
    public static BaseValueObject getBaseValueObject(final String string) {
        return (BaseValueObject)new StringIdentifier(string);
    }
    
    public static BaseValueObject getBaseValueObject(final Integer integer) {
        return (BaseValueObject)new Identifier(integer);
    }
    
    public static BaseValueObject getBaseValueObject(final List<? extends BaseValueObject> list) {
        final BaseValueObjectList lst = new BaseValueObjectList();
        lst.setValueObjectList((List)list);
        return (BaseValueObject)lst;
    }
    
    public static BaseValueObject getBaseValueObject(final Map<? extends BaseValueObject, ? extends BaseValueObject> baseValueMap) {
        final BaseValueObjectMap map = new BaseValueObjectMap();
        map.setBaseValueMap((Map)baseValueMap);
        return (BaseValueObject)map;
    }
    
    public static BaseValueObject getBaseValueObject(final Set<? extends BaseValueObject> set) {
        final BaseValueObjectSet st = new BaseValueObjectSet();
        st.setValueObjectSet((Set)set);
        return (BaseValueObject)st;
    }
    
    public static BaseValueObject getBaseValueObject(final Object obj) {
        final BaseValueObjectData objData = new BaseValueObjectData();
        objData.setData(obj);
        return (BaseValueObject)objData;
    }
    
    public static BaseValueObject getBaseValueObject(final BaseValueObject bvo) {
        return bvo;
    }
}
