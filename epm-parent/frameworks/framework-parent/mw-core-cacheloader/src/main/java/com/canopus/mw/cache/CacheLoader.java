package com.canopus.mw.cache;

public interface CacheLoader<K, T>
{
    T load(final K p0);
}
