package com.canopus.dac.utils;

import com.googlecode.genericdao.search.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.dto.Sort;

import java.util.*;
import com.canopus.mw.dto.param.*;

public class PaginationSortDAOHelper
{
    public static void addSortAndPaginationToSearch(final Search search, final Page page, final SortList sortList, final Integer maxCount, final String defaultField) {
        if (page.getPageNumber() == null) {
            page.setPageNumber(0);
        }
        if (page.getMaxResults() == null || page.getMaxResults() == 0) {
            page.setMaxResults(maxCount);
        }
        page.setTotalCount(maxCount);
        search.setFirstResult((int)page.getPageNumber());
        search.setMaxResults((int)page.getMaxResults());
        if (sortList.getSortList() != null && sortList.getSortList().size() > 0) {
            for (final Sort sort : sortList.getSortList()) {
                if (sort.isDesc()) {
                    search.addSort(com.googlecode.genericdao.search.Sort.desc(sort.getField()));
                }
                else {
                    search.addSort(com.googlecode.genericdao.search.Sort.asc(sort.getField()));
                }
            }
        }
        else {
            final Sort sort2 = new Sort();
            sort2.setField(defaultField);
            sortList.getSortList().add(sort2);
            search.addSort(com.googlecode.genericdao.search.Sort.asc(defaultField));
        }
    }
    
    public static void addSortToSearch(final Search search, final SortList sortList, final String defaultField) {
        if (sortList.getSortList() != null && sortList.getSortList().size() > 0) {
            for (final Sort sort : sortList.getSortList()) {
                if (sort.isDesc()) {
                    search.addSort(com.googlecode.genericdao.search.Sort.desc(sort.getField()));
                }
                else {
                    search.addSort(com.googlecode.genericdao.search.Sort.asc(sort.getField()));
                }
            }
        }
        else {
            final Sort sort2 = new Sort();
            sort2.setField(defaultField);
            sortList.getSortList().add(sort2);
            search.addSort(com.googlecode.genericdao.search.Sort.asc(defaultField));
        }
    }
    
    public static void addSortAndPaginationToSearch(final Search search, final Page page, final SortList sortList, final Integer maxCount, final String defaultField, final String defaultOrder) {
        if (page.getPageNumber() == null) {
            page.setPageNumber(0);
        }
        if (page.getMaxResults() == null || page.getMaxResults() == 0) {
            page.setMaxResults(maxCount);
        }
        page.setTotalCount(maxCount);
        search.setFirstResult((int)page.getPageNumber());
        search.setMaxResults((int)page.getMaxResults());
        if (sortList.getSortList() != null && sortList.getSortList().size() > 0) {
            for (final Sort sort : sortList.getSortList()) {
                if (sort.isDesc()) {
                    search.addSort(com.googlecode.genericdao.search.Sort.desc(sort.getField()));
                }
                else {
                    search.addSort(com.googlecode.genericdao.search.Sort.asc(sort.getField()));
                }
            }
        }
        else {
            final Sort sort2 = new Sort();
            sort2.setField(defaultField);
            if (defaultOrder != null && !defaultOrder.isEmpty() && defaultOrder.equalsIgnoreCase(HeaderParam.DESC.name())) {
                search.addSort(com.googlecode.genericdao.search.Sort.desc(defaultField));
                sort2.setDesc(true);
            }
            else {
                search.addSort(com.googlecode.genericdao.search.Sort.asc(defaultField));
            }
            sortList.getSortList().add(sort2);
        }
    }
    
    public static void addSortToSearch(final Search search, final SortList sortList, final String defaultField, final String defaultOrder) {
        if (sortList.getSortList() != null && sortList.getSortList().size() > 0) {
            for (final Sort sort : sortList.getSortList()) {
                if (sort.isDesc()) {
                    search.addSort(com.googlecode.genericdao.search.Sort.desc(sort.getField()));
                }
                else {
                    search.addSort(com.googlecode.genericdao.search.Sort.asc(sort.getField()));
                }
            }
        }
        else {
            final Sort sort2 = new Sort();
            sort2.setField(defaultField);
            if (defaultOrder != null && !defaultOrder.isEmpty() && defaultOrder.equalsIgnoreCase(HeaderParam.DESC.name())) {
                search.addSort(com.googlecode.genericdao.search.Sort.desc(defaultField));
                sort2.setDesc(true);
            }
            else {
                search.addSort(com.googlecode.genericdao.search.Sort.asc(defaultField));
            }
            sortList.getSortList().add(sort2);
        }
    }
}
