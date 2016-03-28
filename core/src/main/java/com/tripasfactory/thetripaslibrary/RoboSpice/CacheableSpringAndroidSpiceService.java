
package com.tripasfactory.thetripaslibrary.RoboSpice;

import android.app.Application;
import android.content.Context;

import com.octo.android.robospice.SpringAndroidSpiceService;
import com.octo.android.robospice.networkstate.NetworkStateChecker;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.tripasfactory.thetripaslibrary.ConfigData;
import com.tripasfactory.thetripaslibrary.Configs;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class CacheableSpringAndroidSpiceService extends
        SpringAndroidSpiceService {

    @Override
    public RestTemplate createRestTemplate() {

        RestTemplate restTemplate = new RestTemplate();

        // Setup connection and read timeouts
        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory())
                .setReadTimeout(Configs.TIMEOUT_CONNECTION);
        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory())
                .setConnectTimeout(Configs.TIMEOUT_CONNECTION);

        // ** Create message converters

        // To convert http payload to Json Objects
        // GsonHttpMessageConverter jsonConverter = new
        // GsonHttpMessageConverter();

        // To handle form data, including multipart form data
        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();

        // To read and write strings
        // By default, this converter supports all media types (*/*), and writes
        // with a Content-Type of text/plain.
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();

        // To read and write byte arrays.
        // By default, this converter supports all media types (*/*), and writes
        // with a Content-Type of application/octet-stream
        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();

        final List<HttpMessageConverter<?>> listHttpMessageConverters = restTemplate
                .getMessageConverters();

        // ** Insert converters in restTemplate
        // listHttpMessageConverters.add(jsonConverter);
        listHttpMessageConverters.add(formHttpMessageConverter);
        listHttpMessageConverters.add(stringHttpMessageConverter);
        listHttpMessageConverters.add(byteArrayHttpMessageConverter);
        restTemplate.setMessageConverters(listHttpMessageConverters);

        if (ConfigData.getEndpoint() != null
                && ConfigData.getEndpoint().startsWith("https")) {
            restTemplate.setRequestFactory(new SimpleClientHttpSSLPinningRequestFactory());
        }

        return restTemplate;
    }

    @Override
    public CacheManager createCacheManager(Application application)
            throws CacheCreationException {

        // Setup cache manager to cache Json Objects, with gsonPersister
        CacheManager cacheManager = new CacheManager();
        MemoryGsonObjectPersisterFactory gsonObjectPersisterFactory = new MemoryGsonObjectPersisterFactory(
                application);
        cacheManager.addPersister(gsonObjectPersisterFactory);
        return cacheManager;

    }

    @Override
    public int getThreadCount() {
        return Configs.NBR_OF_THREADS_TO_PROCESS_REQUESTS;
    }

}
