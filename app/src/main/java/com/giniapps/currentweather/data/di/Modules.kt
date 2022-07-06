package com.giniapps.currentweather.data.di

import com.giniapps.currentweather.data.GeocoderUtil
import com.giniapps.currentweather.data.cache.Cache
import com.giniapps.currentweather.data.cache.WeatherCache
import com.giniapps.currentweather.data.cache.room.WeatherDatabase
import com.giniapps.currentweather.data.remote.RemoteDataSource
import com.giniapps.currentweather.data.remote.data_contracts.DataSource
import com.giniapps.currentweather.data.repository.Repository
import com.giniapps.currentweather.data.repository.WeatherDataRepository
import com.giniapps.currentweather.location.LocationManager
import com.giniapps.currentweather.screens.main.MainScreenViewModel
import com.giniapps.currentweather.screens.map.MapScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val remoteDataModule = module {
    factory {
        CoroutineScope(Dispatchers.Main + SupervisorJob())
    }

    factory {
        GeocoderUtil(androidContext())
    }

    factory<DataSource> {
        RemoteDataSource(
            scope = get(),
            geocoderUtil = get()
        )
    }
}

val cacheModule = module {
    single {
        WeatherDatabase.getDatabase(androidContext())
            .weatherDetailsDao()
    }

    single {
        WeatherDatabase.getDatabase(androidContext())
            .locationDao()
    }

    single {
        WeatherDatabase.getDatabase(androidContext())
            .currentLocationDao()
    }

    factory<Cache> {
        WeatherCache(
            detailsDao = get(),
            locationDao = get(),
            currentLocationDao = get()
        )
    }
}

val repositoryModule = module {
    factory<Repository> {
        WeatherDataRepository(
            dataSource = get(),
            cache = get(),
            geocoderUtil = get()
        )
    }
}

val viewModelModule = module {
    factory {
        LocationManager(androidContext())
    }

    factory {
        MainScreenViewModel(
            repository = get(),
            locationManager = get(),
            geocoderUtil = get()
        )
    }

    factory {
        MapScreenViewModel(
            repository = get(),
            locationManager = get(),
            geocoderUtil = get()
        )
    }
}