package danbroid.audioservice.app

import android.app.Application
import danbroid.audioservice.app.rnz.RNZSupport
import danbroid.demo.content.SomaFMLibrary
import danbroid.demo.content.TestDataLibrary
import danbroid.media.service.Config
import danbroid.media.service.audioServiceConfig
import danbroid.util.resource.toResourceColour


class App : Application() {
  override fun onCreate() {
    log.info("onCreate()")
    audioServiceConfig.library.register(TestDataLibrary(), SomaFMLibrary.getInstance(this), RNZSupport.getInstance(this))
    Config.Notifications.apply {
      notificationColour = R.color.colorPrimary.toResourceColour(applicationContext)
      notificationIconTint = R.color.colorPrimaryLight.toResourceColour(applicationContext)
    }


    super.onCreate()
  }
}


