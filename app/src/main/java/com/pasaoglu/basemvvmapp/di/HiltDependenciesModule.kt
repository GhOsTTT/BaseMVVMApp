package com.pasaoglu.basemvvmapp.di

import android.app.Application
import android.os.Build
import com.pasaoglu.basemvvmapp.BuildConfig

import com.pasaoglu.basemvvmapp.data.network.Service
import com.pasaoglu.basemvvmapp.util.isNetworkAvailable

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object HiltDependenciesModule {

    /**
     * Returns the [HttpLoggingInterceptor] instance with logging level set to body
     * @since 1.0.0
     */
    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    fun provideCustomInterceptor(application: Application) = Interceptor { chain ->
        var request = chain.request()
        val originalHttpUrl: HttpUrl = request.url
        //Language support and set default api key
        val url = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            originalHttpUrl.newBuilder()
             //   .addQueryParameter("api_key", BuildConfig.API_KEY)
                .addQueryParameter("language", Locale.getDefault().toLanguageTag())
                .build()
        } else {
            originalHttpUrl.newBuilder()
               // .addQueryParameter("api_key", BuildConfig.API_KEY)
                .build()
        }
        //Offline supports
        request = if (application.isNetworkAvailable())
            request.newBuilder().header("Cache-Control", "public, max-age=" + 5).url(url).build()
        else
            request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                .url(url).build()
        chain.proceed(request)

    }

    /**
     * Provides an [OkHttpClient]
     * @param loggingInterceptor [HttpLoggingInterceptor] instance
     * @since 1.0.0
     */
    @Provides
    fun provideOKHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        customInterceptor: Interceptor,
        application: Application
    ): OkHttpClient {
        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(application.cacheDir, cacheSize)
        return OkHttpClient.Builder()
            .cache(myCache)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(customInterceptor)
            .build()
    }

    /**
     * Returns a [GsonConverterFactory] instance
     * @since 1.0.0
     */
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    /**
     * Returns an instance of the [Service] interface for the retrofit class
     * @return [Service] impl
     * @since 1.0.0
     */
    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Service =
        Retrofit.Builder().run {
            baseUrl(BuildConfig.BASE_URL)
            addConverterFactory(gsonConverterFactory)
            client(client)
            build()
        }.run {
            create(Service::class.java)
        }
}