package com.canopus.mw.search;

import java.util.*;
import com.canopus.mw.audit.*;

public interface SearchClient<E>
{
    SearchClient<E> dateRange(final Date p0, final Date p1);
    
    SearchClient<E> searchText(final String p0);
    
    SearchClient<E> searchCustom(final String p0, final Object p1);
    
    SearchClient<E> searchStatus(final AuditStatus p0);
    
    SearchClient<E> searchOperation(final AuditOperation p0);
    
    SearchClient<E> searchCategory(final AuditCategory p0);
    
     <V> SearchClient<E> searchOrg(final V p0);
    
     <W> SearchClient<E> searchUser(final W p0);
    
    SearchClient<E> searchEntryFrom(final int p0);
    
    SearchClient<E> entrySize(final int p0);
    
    E execute(final String p0);
}
