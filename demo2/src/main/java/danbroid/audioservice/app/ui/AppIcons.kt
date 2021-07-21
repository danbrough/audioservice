package danbroid.audioservice.app.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Panorama
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Web
import danbroid.audioservice.app.R
import kotlinx.serialization.Serializable

@Serializable
enum class AppIcon {
  SETTINGS, SAVINGS, AUDIO_TRACK, PANORAMA, BROWSER;

  companion object {
    fun lookup(key: AppIcon) = when (key) {
      AppIcon.SAVINGS -> Icons.Default.Savings
      AppIcon.SETTINGS -> Icons.Default.Settings
      AppIcon.AUDIO_TRACK -> R.drawable.ic_audio_track
      AppIcon.PANORAMA -> Icons.Default.Panorama
      BROWSER -> Icons.Default.Web
    }
  }
}


//private val log = danbroid.logging.getLog(AppIcon::class)
