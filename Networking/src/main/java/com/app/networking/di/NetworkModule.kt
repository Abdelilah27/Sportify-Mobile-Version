package com.app.networking.di

import com.app.networking.api.RetrofitServiceInterface
import com.app.networking.api.AuthRetrofitServiceInterface
import com.app.networking.model.network.AuthInterceptor
import com.app.networking.utils.ConstUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun createRetrofitInstanceApp(retrofitBuilder: Retrofit.Builder):
            RetrofitServiceInterface =
        retrofitBuilder.build().create(RetrofitServiceInterface::class.java)

    @Singleton
    @Provides
    fun getRetrofitInstanceApp(): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(ConstUtil.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())

    @Singleton
    @Provides
    fun provideOkHHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Singleton
    @Provides
    fun getRetrofitInstanceAuthApp(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(ConstUtil.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun createRetrofitInstanceAuthApp(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ):
            AuthRetrofitServiceInterface =
        retrofitBuilder.client(okHttpClient).build()
            .create(AuthRetrofitServiceInterface::class.java)


    @ApplicationScopeApp
    @Provides
    fun provideApplicationScopeApp() = CoroutineScope(SupervisorJob())

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScopeApp