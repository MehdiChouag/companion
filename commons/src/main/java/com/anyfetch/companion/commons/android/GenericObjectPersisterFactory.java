package com.anyfetch.companion.commons.android;


import android.app.Application;

import com.octo.android.robospice.persistence.ObjectPersister;
import com.octo.android.robospice.persistence.ObjectPersisterFactory;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.memory.CacheItem;
import com.octo.android.robospice.persistence.memory.LruCache;
import com.octo.android.robospice.persistence.memory.LruCacheObjectPersister;

/**
 * Stores generic objects in the spice cache
 */
public class GenericObjectPersisterFactory extends ObjectPersisterFactory {
    private static final int DEFAULT_MAX_SIZE = 1 * 1024 * 1024; // 1MB
    private final int mMaxSize;

    public GenericObjectPersisterFactory(Application application) {
        super(application);
        mMaxSize = DEFAULT_MAX_SIZE;
    }

    public GenericObjectPersisterFactory(Application application, int maxSize) {
        super(application);
        mMaxSize = maxSize;
    }

    @Override
    public <DATA> ObjectPersister<DATA> createObjectPersister(Class<DATA> clazz) throws CacheCreationException {
        LruCache<Object, CacheItem<DATA>> lru = new LruCache<Object, CacheItem<DATA>>(mMaxSize);

        ObjectPersister<DATA> op = new LruCacheObjectPersister<DATA>(clazz, lru);

        return op;
    }
}
