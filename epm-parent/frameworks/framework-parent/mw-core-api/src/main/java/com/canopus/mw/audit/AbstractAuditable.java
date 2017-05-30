package com.canopus.mw.audit;

import java.util.*;
import com.canopus.mw.util.*;

public abstract class AbstractAuditable implements Auditable
{
    @Override
    public Map<String, Object> getAuditData() {
        return TransformerUtil.convertToMap(this);
    }
}
