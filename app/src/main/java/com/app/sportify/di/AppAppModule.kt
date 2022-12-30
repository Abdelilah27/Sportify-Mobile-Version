package com.app.sportify.di

import com.app.sportify.network.AppRetrofitServiceInterface
import com.app.sportify.network.AuthAppRetrofitServiceInterface
import com.app.sportify.network.AuthInterceptor
import com.app.sportify.utils.ConstUtil
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
class AppModule {
    @Provides
    @Singleton
    fun createRetrofitInstanceApp(retrofitBuilder: Retrofit.Builder):
            AppRetrofitServiceInterface =
        retrofitBuilder.build().create(AppRetrofitServiceInterface::class.java)

    @Provides
    @Singleton
    fun getRetrofitInstanceApp(): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(ConstUtil.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())


    @Provides
    @Singleton
    fun provideOkHHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Provides
    @Singleton
    fun getRetrofitInstanceAuthApp(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(ConstUtil.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun createRetrofitInstanceAuthApp(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ):
            AuthAppRetrofitServiceInterface =
        retrofitBuilder.client(okHttpClient).build()
            .create(AuthAppRetrofitServiceInterface::class.java)

    @ApplicationScopeApp
    @Provides
    @Singleton
    fun provideApplicationScopeApp() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ApplicationScopeApp