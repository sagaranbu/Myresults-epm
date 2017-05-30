package com.canopus.mw.audit;

import java.util.*;

public interface Auditable
{
    Map<String, Object> getAuditData();
}
