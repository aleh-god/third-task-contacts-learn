package by.godevelopment.thirdtask

import android.app.Application
import android.content.Context
import by.godevelopment.thirdtask.di.AppComponent
import by.godevelopment.thirdtask.di.DaggerAppComponent

//import dagger.hilt.android.HiltAndroidApp

//@HiltAndroidApp
class TaskApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is TaskApp -> appComponent
        else -> applicationContext.appComponent
    }