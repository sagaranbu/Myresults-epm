package com.canopus.mw.cache;

import com.google.common.cache.*;
import java.util.concurrent.*;

public class LRUCache<K, T> implements Cache<K, T>
{
    private com.google.common.cache.Cache<K, T> guavaCache;
    
    public LRUCache() {
        this.guavaCache = (com.google.common.cache.Cache<K, T>)CacheBuilder.newBuilder().maximumSize(10000L).build();
    }
    
    public LRUCache(final int size) {
        this.guavaCache = (com.google.common.cache.Cache<K, T>)CacheBuilder.newBuilder().maximumSize((long)size).build();
    }
    
    @Override
    public void put(final K key, final T obj) {
        this.guavaCache.put(key, obj);
    }
    
    @Override
    public T get(final K key, final CacheLoader<K, T> loader) {
        try {
            final LoaderCaller lc = new LoaderCaller((CacheLoader<K, T>)loader, (K)key);
            final T obj = (T)this.guavaCache.get(key, (Callable)lc);
            return obj;
        }
        catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public T getIfPresent(final K key) {
        final T obj = (T)this.guavaCache.getIfPresent((Object)key);
        return obj;
    }
    
    @Override
    public void remove(final K key) {
        this.guavaCache.invalidate((Object)key);
    }
    
    @Override
    public void clear() {
        this.guavaCache.cleanUp();
    }
    
    public static class LoaderCaller<K, T> implements Callable
    {
        private CacheLoader<K, T> loader;
        private K key;
        
        public LoaderCaller(final CacheLoader<K, T> loader, final K key) {
            this.loader = null;
            this.key = null;
            this.loader = loader;
            this.key = key;
        }
        
        @Override
        public Object call() throws Exception {
            return this.loader.load(this.key);
        }
    }
}
