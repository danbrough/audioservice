package danbroid.audioservice.app.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NetworkWifi
import androidx.compose.material.icons.filled.Panorama
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import danbroid.audioservice.app.R
import kotlinx.serialization.Serializable

@Serializable
enum class AppIcon {
  SETTINGS, SAVINGS, AUDIO_TRACK, PANORAMA, BROWSER, RNZ_NEWS, FOLDER, RADIO,PLAYLIST;

  companion object {
    fun lookup(key: AppIcon) = when (key) {
      AppIcon.SAVINGS -> Icons.Default.Savings
      AppIcon.SETTINGS -> Icons.Default.Settings
      AppIcon.AUDIO_TRACK -> R.drawable.ic_audio_track
      AppIcon.PANORAMA -> Icons.Default.Panorama
      AppIcon.BROWSER -> Icons.Default.NetworkWifi

      AppIcon.RNZ_NEWS -> "https://www.rnz.co.nz/brand-images/rnz-news.jpg"
      AppIcon.FOLDER -> R.drawable.ic_folder
      AppIcon.RADIO -> R.drawable.ic_radio
      AppIcon.PLAYLIST -> R.drawable.ic_playlist
    }
  }
}


//private val log = danbroid.logging.getLog(AppIcon::class)
