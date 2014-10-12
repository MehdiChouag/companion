package com.anyfetch.companion.commons.android.requests;

import android.app.Application;
import android.content.Context;

import com.anyfetch.companion.commons.android.helpers.GenericObjectPersisterFactory;
import com.anyfetch.companion.commons.android.pojo.Event;
import com.anyfetch.companion.commons.android.pojo.EventsList;
import com.anyfetch.companion.commons.android.pojo.Person;
import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.networkstate.NetworkStateChecker;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;

/**
 * Provides a Service for local async tasks such as Android content providers queries
 * <p/>
 * https://github.com/stephanenicolas/robospice/wiki/Advanced-RoboSpice-Usages-and-FAQ#can-i-use-robospice-for-a-non-network-related-task-like-a-long-running-computation-
 */
public class AndroidSpiceService extends SpiceService {
    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cm = new CacheManager();
        GenericObjectPersisterFactory factory = new GenericObjectPersisterFactory(application);
        cm.addPersister(factory.createObjectPersister(EventsList.class));
        factory = new GenericObjectPersisterFactory(application, 256 * 1024); // 256KB
        cm.addPersister(factory.createObjectPersister(Event.class));
        cm.addPersister(factory.createObjectPersister(Person.class));
        return cm;
    }

    @Override
    protected NetworkStateChecker getNetworkStateChecker() {
        return new NetworkStateChecker() {
            @Override
            public boolean isNetworkAvailable(Context context) {
                return true;
            }

            @Override
            public void checkPermissions(Context context) {

            }
        };
    }
}
