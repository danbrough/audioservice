package danbroid.audioservice.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.CoilUtils
import danbroid.audio.service.Config
import danbroid.util.resource.toResourceColour
import klog.KMessageFormatters
import klog.Level
import klog.colored
import klog.klog
import okhttp3.OkHttpClient


class App : Application(), ImageLoaderFactory {
  private val testLog = klog("AUDIO_DEMO") {
    level = Level.TRACE
    messageFormatter = KMessageFormatters.verbose.colored
  }

  override fun newImageLoader(): ImageLoader {
    return ImageLoader.Builder(applicationContext)
      .crossfade(true)
      .okHttpClient {
        OkHttpClient.Builder()
          //.cache(CoilUtils.createDefaultCache(applicationContext))
          .build()
      }
      .build()
  }

  override fun onCreate() {
    testLog.info("onCreate()")

    Config.Notifications.apply {
      notificationColour = R.color.colorPrimary.toResourceColour(applicationContext)
      notificationIconTint = R.color.colorPrimaryLight.toResourceColour(applicationContext)
    }

    super.onCreate()
  }
}
