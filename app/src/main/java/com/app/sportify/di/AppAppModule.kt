package com.app.sportify.di

import com.app.sportify.network.AppRetrofitServiceInterface
import com.app.sportify.utils.ConstUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun createRetrofitInstanceApp(@Named("App") retrofit: Retrofit): AppRetrofitServiceInterface =
        retrofit.create(AppRetrofitServiceInterface::class.java)

    @Provides
    @Singleton
    @Named("App")
    fun getRetrofitInstanceApp(): Retrofit = Retrofit.Builder()
        .baseUrl(ConstUtil.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @ApplicationScopeApp
    @Provides
    @Singleton
    fun provideApplicationScopeApp() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ApplicationScopeApp