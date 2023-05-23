package danbroid.audioservice.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import danbroid.audio.service.Config
import danbroid.util.resource.toResourceColour
import klog.KMessageFormatters
import klog.Level
import klog.colored
import klog.klog
import okhttp3.OkHttpClient
import java.io.File


class App : Application(), ImageLoaderFactory {
  private val testLog = klog("AUDIO_DEMO") {
    level = Level.TRACE
    messageFormatter = KMessageFormatters.verbose.colored
  }

  override fun newImageLoader(): ImageLoader {
    val diskCache = DiskCache.Builder().directory(
      File(applicationContext.cacheDir, "imageCache")
    ).build()

    return ImageLoader.Builder(applicationContext)
      .crossfade(true)
      .diskCache(diskCache)
      .okHttpClient {
        OkHttpClient.Builder()
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
