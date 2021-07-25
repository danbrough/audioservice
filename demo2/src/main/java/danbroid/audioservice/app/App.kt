package danbroid.audioservice.app

import android.app.Application
import danbroid.audio.service.Config
import danbroid.util.resource.toResourceColour


class App : Application() {


  override fun onCreate() {
    log.info("onCreate()")

    Config.Notifications.apply {
      notificationColour = R.color.colorPrimary.toResourceColour(applicationContext)
      notificationIconTint = R.color.colorPrimaryLight.toResourceColour(applicationContext)
    }

    super.onCreate()
  }
}
