package com.felipefernandes.desafiousemobile.config

import android.app.Application
import com.felipefernandes.desafiousemobile.config.retrofit.DependenciesModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppDesafioMobile : Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AppDesafioMobile)

            modules(DependenciesModules.appModule)
        }
    }
}
