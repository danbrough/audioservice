package danbroid.audioservice.app

import android.app.Application

class App : Application(){
  override fun onCreate() {
    super.onCreate()
    log.dwarn("onCreate()")
  }
}

