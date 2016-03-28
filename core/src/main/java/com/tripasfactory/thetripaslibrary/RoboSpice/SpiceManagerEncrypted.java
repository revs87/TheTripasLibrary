package com.tripasfactory.thetripaslibrary.RoboSpice;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;
import com.tripasfactory.thetripaslibrary.Configs;
import com.tripasfactory.thetripaslibrary.Security.EncryptionUtils;

import java.util.concurrent.Future;

/**
 * Created by Rui on 17/09/2015.
 */
public class SpiceManagerEncrypted extends SpiceManager {
    /**
     * Creates a {@link SpiceManager}. Typically this occurs in the construction
     * of an Activity or Fragment. This method will check if the service to bind
     * to has been properly declared in AndroidManifest.
     *
     * @param spiceServiceClass the service class to bind to.
     */
    public SpiceManagerEncrypted(Class<? extends SpiceService> spiceServiceClass) {
        super(spiceServiceClass);
    }

    @Override
    public <T> void execute(SpiceRequest<T> request, Object requestCacheKey, long cacheExpiryDuration, RequestListener<T> requestListener) {
        if (Configs.ENCRYPT_CACHE) {
            super.execute(
                    request,
                    EncryptionUtils.encryptDataString((String) requestCacheKey),
                    cacheExpiryDuration,
                    requestListener
            );
        } else {
            super.execute(
                    request,
                    requestCacheKey,
                    cacheExpiryDuration,
                    requestListener
            );
        }
    }

    @Override
    public <T> Future<?> removeDataFromCache(Class<T> clazz, Object cacheKey) {
        if (Configs.ENCRYPT_CACHE) {
            return super.removeDataFromCache(clazz, EncryptionUtils.encryptDataString((String) cacheKey));
        }
        return super.removeDataFromCache(clazz, cacheKey);
    }
}
