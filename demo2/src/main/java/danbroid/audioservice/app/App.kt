package danbroid.audioservice.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.CoilUtils
import danbroid.audio.service.Config
import danbroid.util.resource.toResourceColour
import okhttp3.OkHttpClient


class App : Application(), ImageLoaderFactory {

  override fun newImageLoader(): ImageLoader {
    return ImageLoader.Builder(applicationContext)
        .crossfade(true)
        .okHttpClient {
          OkHttpClient.Builder()
              //TODO
              //.cache(CoilUtils.createDefaultCache(applicationContext))
              .build()
        }
        .build()
  }

  override fun onCreate() {
    log.info("onCreate()")

    Config.Notifications.apply {
      notificationColour = R.color.colorPrimary.toResourceColour(applicationContext)
      notificationIconTint = R.color.colorPrimaryLight.toResourceColour(applicationContext)
    }

    super.onCreate()
  }
}
