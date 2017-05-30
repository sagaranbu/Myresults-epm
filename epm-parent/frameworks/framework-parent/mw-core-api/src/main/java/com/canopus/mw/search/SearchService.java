package com.canopus.mw.search;

public interface SearchService<E>
{
     <V, W> E getSearchResults(final SearchParams<V, W> p0);
}
