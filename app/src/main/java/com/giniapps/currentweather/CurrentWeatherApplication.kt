package com.giniapps.currentweather

import android.app.Application
import com.giniapps.currentweather.data.di.cacheModule
import com.giniapps.currentweather.data.di.remoteDataModule
import com.giniapps.currentweather.data.di.repositoryModule
import com.giniapps.currentweather.data.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CurrentWeatherApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@CurrentWeatherApplication)
            modules(
                remoteDataModule,
                cacheModule,
                repositoryModule,
                viewModelModule
            )
        }
    }
}