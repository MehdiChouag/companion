package com.anyfetch.companion.android;

import android.app.Application;
import android.content.Context;

import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.networkstate.NetworkStateChecker;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;

/**
 * Provides a Service for local async tasks such as Android content providers queries
 *
 * https://github.com/stephanenicolas/robospice/wiki/Advanced-RoboSpice-Usages-and-FAQ#can-i-use-robospice-for-a-non-network-related-task-like-a-long-running-computation-
 */
public class AndroidSpiceService extends SpiceService {
    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        return new CacheManager();
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
