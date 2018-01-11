package com.example.ready.stepruler.utils;

import com.franmontiel.persistentcookiejar.BuildConfig;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ready on 2017/12/28.
 */

public class RetrofitFactory {
    private static final Object object = new Object();
    private volatile static Retrofit retrofit;
    static String Host = "http://39.108.173.192:80/";

    /**
     * 缓存机制
     * 在响应请求之后在 data/data/<包名>/cache 下建立一个response 文件夹，保持缓存数据。
     * 这样我们就可以在请求的时候，如果判断到没有网络，自动读取缓存的数据。
     * 同样这也可以实现，在我们没有网络的情况下，重新打开App可以浏览的之前显示过的内容。
     * 也就是：判断网络，有网络，则从网络获取，并保存到缓存中，无网络，则从缓存中获取。
     */
    private static final Interceptor cacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if(!NetWorkUtil.isNetworkConnected(AppManager.getAppManager().currentActivity())){
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }

            Response response = chain.proceed(request);
            if(NetWorkUtil.isNetworkConnected(AppManager.getAppManager().currentActivity())){
                //有网络时，设置缓存为默认值
                String cacheControl = request.cacheControl().toString();
                return response.newBuilder()
                        .header("Cache_Control", cacheControl)
                        .removeHeader("Progma")
                        .build();
            }else {
                //无网络时，设置超时时间为一周
                int maxStale = 60 * 60 * 24 * 7;
                return response.newBuilder()
                        .header("Cache_Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Progma")
                        .build();
            }
        }
    };

    public static Retrofit getRetrofit(){
        synchronized (object){
            if(retrofit == null){
                //指定缓存路径，缓存大小50Mb
                Cache cache = new Cache(new File(AppManager.getAppManager().currentActivity().getCacheDir(),"HttpCache"), 1024 * 1024 * 50);
                //cookie持久化
                ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(AppManager.getAppManager().currentActivity()));
                //okhttp
                OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                        .cache(cache)
//                        .cookieJar(cookieJar)
//                        .addInterceptor(cacheControlInterceptor)
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .writeTimeout(15, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true);
                //log拦截器
                if(BuildConfig.DEBUG){
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    builder.addInterceptor(interceptor);
                }
                //retrofit
                retrofit = new Retrofit.Builder()
                        .baseUrl(Host)
                        .client(builder.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
            }
        }
        return retrofit;
    }
}
