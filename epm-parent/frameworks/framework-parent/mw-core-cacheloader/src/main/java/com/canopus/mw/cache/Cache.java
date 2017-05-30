package com.canopus.mw.cache;

public interface Cache<K, T>
{
    void put(final K p0, final T p1);
    
    T get(final K p0, final CacheLoader<K, T> p1);
    
    T getIfPresent(final K p0);
    
    void remove(final K p0);
    
    void clear();
}
