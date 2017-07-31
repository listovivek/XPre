package com.quad.xpress.webservice;

import android.content.Context;

import com.quad.xpress.utills.helpers.StaticConfig;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;


/**
 * Created by admin on 29-03-16.
 */
public class RestClient {
    private static RxApi REST_CLIENT;
    private static String ROOT = StaticConfig.ROOT_URL;
    static Context mcontext;

    static {
        setupRestClient();
    }

   /* private RestClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Cache cache = new Cache(getCacheDir(), 1024);
    okHttpClient.setCache(cache);
    Log.v("","");
}*/

    public static RxApi get(Context mcontext1) {
        return REST_CLIENT;
    }

    private static void setupRestClient() {
       /* OkHttpClient okHttpClient = new OkHttpClient();
        Cache cache = null;
        try {
            cache = new Cache(mcontext.getCacheDir(), 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        okHttpClient.setCache(cache);*/

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ROOT)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);
        RestAdapter restAdapter = builder.build();
        REST_CLIENT = restAdapter.create(RxApi.class);
    }
}