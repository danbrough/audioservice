package danbroid.audioservice.app.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import danbroid.audioservice.app.R
import kotlinx.serialization.Serializable

@Serializable
enum class AppIcon {
  SETTINGS, SAVINGS, AUDIO_TRACK, PANORAMA, BROWSER, RNZ_NEWS, FOLDER, RADIO, PLAYLIST, CAST, CAST_CONNECTED;

  companion object {
    fun lookup(key: AppIcon): Any = when (key) {
      AppIcon.SAVINGS -> Icons.Default.Savings
      AppIcon.CAST -> Icons.Default.Cast
      AppIcon.CAST_CONNECTED -> Icons.Default.CastConnected
      AppIcon.SETTINGS -> Icons.Default.Settings
      AppIcon.AUDIO_TRACK -> R.drawable.ic_audio_track
      AppIcon.PANORAMA -> Icons.Default.Panorama
      AppIcon.BROWSER -> R.drawable.ic_web

      AppIcon.RNZ_NEWS -> "https://www.rnz.co.nz/brand-images/rnz-news.jpg"
      AppIcon.FOLDER -> R.drawable.ic_folder
      AppIcon.RADIO -> R.drawable.ic_radio
      AppIcon.PLAYLIST -> R.drawable.ic_playlist
    }
  }
}


//private val log = danbroid.logging.getLog(AppIcon::class)
