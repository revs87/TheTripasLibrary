package com.tripasfactory.thetripaslibrary.RoboSpice;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.octo.android.robospice.persistence.ObjectPersister;
import com.octo.android.robospice.persistence.exception.CacheLoadingException;
import com.octo.android.robospice.persistence.exception.CacheSavingException;
import com.tripasfactory.thetripaslibrary.Security.SecurityUtils;

import org.springframework.util.support.Base64;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MemoryGsonObjectPersister<T> extends ObjectPersister<T> {

    private final Gson mGson;
    private final String mSeed;
    private Map<Object, String> mValuesMap;
    private Map<Object, Long> mDatesMap;

    public MemoryGsonObjectPersister(Application application, Class<T> clazz, String seed) {
        super(application, clazz);

        this.mGson = new Gson();
        this.mValuesMap = new HashMap<Object, String>();
        this.mDatesMap = new HashMap<Object, Long>();
        this.mSeed = seed;
    }

    @Override
    public
    @Nullable
    T loadDataFromCache(Object cacheKey, long maxTimeInCache)
            throws CacheLoadingException {
        boolean isInCache = mValuesMap.containsKey(cacheKey);
        if (isInCache) {
            Long lastModified = mDatesMap.get(cacheKey);
            final boolean isOk = isCachedAndNotExpired(maxTimeInCache, lastModified);
            if (isOk) {
                final String json = mValuesMap.get(cacheKey);
                final T obj = decodeEncryptedJson(json);
                return obj;
            }
        }

        return null;
    }

    private T decodeEncryptedJson(@NonNull String json) throws CacheLoadingException {
        if (mSeed != null) {
            try {
                final byte[] decoded = Base64.decode(json);
                final byte[] decrypted = SecurityUtils.decrypt(decoded, mSeed);
                json = new String(decrypted);
            } catch (Exception e) {
                throw new CacheLoadingException(e);
            }
        }

        return mGson.fromJson(json, getHandledClass());
    }


    @Override
    public List<T> loadAllDataFromCache() throws CacheLoadingException {
        Collection<String> values = mValuesMap.values();
        List<String> jsonList = new ArrayList<String>();
        jsonList.addAll(values);

        List<T> returnList = new ArrayList<T>(jsonList.size());
        for (int i = 0; i < jsonList.size(); i++) {
            final T obj = decodeEncryptedJson(jsonList.get(i));
            returnList.add(obj);
        }

        return returnList;
    }

    @Override
    public List<Object> getAllCacheKeys() {
        Set<Object> keys = mValuesMap.keySet();
        ArrayList returnList = new ArrayList();
        returnList.addAll(keys);
        return returnList;
    }

    @Override
    public T saveDataToCacheAndReturnData(@NonNull T data, @NonNull Object cacheKey) throws CacheSavingException {

        // transform the content in json to store it in the cache
        String resultJson = mGson.toJson(data);

        if (mSeed != null) {
            try {
                byte[] encrypted = SecurityUtils.encrypt(resultJson, mSeed);
                resultJson = Base64.encodeBytes(encrypted);
            } catch (Exception e) {
                throw new CacheSavingException(e);
            }
        }

        mValuesMap.put(cacheKey, resultJson);
        long lastModification = System.currentTimeMillis();
        mDatesMap.put(cacheKey, lastModification);
        return data;
    }

    @Override
    public boolean removeDataFromCache(@NonNull Object cacheKey) {
        mValuesMap.remove(cacheKey);
        mDatesMap.remove(cacheKey);
        return false;
    }

    @Override
    public void removeAllDataFromCache() {
        mValuesMap.clear();
        mDatesMap.clear();
    }

    @Override
    public long getCreationDateInCache(@NonNull Object cacheKey) throws CacheLoadingException {
        return mDatesMap.get(cacheKey);
    }

    @Override
    public boolean isDataInCache(@NonNull Object cacheKey, long maxTimeInCacheBeforeExpiry) {
        boolean isInCache = mValuesMap.containsKey(cacheKey);
        if (isInCache) {
            Long lastModified = mDatesMap.get(cacheKey);
            return isCachedAndNotExpired(maxTimeInCacheBeforeExpiry, lastModified);
        } else {
            return false;
        }
    }

    protected boolean isCachedAndNotExpired(long maxTimeInCacheBeforeExpiry, long lastModified) {
        long timeInCache = System.currentTimeMillis() - lastModified;
        if (maxTimeInCacheBeforeExpiry == 0L || timeInCache <= maxTimeInCacheBeforeExpiry) {
            return true;
        }

        return false;
    }

}
