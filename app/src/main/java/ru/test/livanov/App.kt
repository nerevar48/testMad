package ru.test.livanov

import android.app.Application
import ru.test.livanov.di.AppComponent
import ru.test.livanov.di.DaggerAppComponent


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.create()
        context = applicationContext
    }

    companion object {
        lateinit var component: AppComponent
        lateinit var context: android.content.Context
    }

}