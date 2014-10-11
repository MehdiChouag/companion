package com.anyfetch.companion.commons.api;

import android.app.Application;

import com.anyfetch.companion.commons.android.GenericObjectPersisterFactory;
import com.anyfetch.companion.commons.api.pojo.Document;
import com.anyfetch.companion.commons.api.pojo.DocumentsList;
import com.octo.android.robospice.okhttp.OkHttpSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;

/**
 * Provides a cached service for HTTP Requests
 */
public class HttpSpiceService extends OkHttpSpiceService {
    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cm = new CacheManager();
        GenericObjectPersisterFactory factory = new GenericObjectPersisterFactory(application);
        cm.addPersister(factory.createObjectPersister(DocumentsList.class));
        factory = new GenericObjectPersisterFactory(application, 256 * 1024);
        cm.addPersister(factory.createObjectPersister(Document.class));
        return cm;
    }
}
