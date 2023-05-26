package danbroid.audio

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import danbroid.audio.library.R
import kotlinx.serialization.Serializable


interface AppIcon<T : Enum<T>> {
  val uri: String
    get() = "icon://${this::class.qualifiedName}/${toString()}"
  val data: Any

  companion object {
    private val ICON_MAP = mutableMapOf<String, Any>()

    fun register(vararg icons: AppIcon<*>) {
      icons.forEach {
        ICON_MAP[it.uri] = it.data
      }
    }

    fun lookupIcon(key: Any?): Any? {
      val lookupKey: String? = when (key) {
        is String -> key
        is AppIcon<*> -> key.uri
        else -> key?.toString()
      }
      lookupKey ?: return key
      return ICON_MAP[lookupKey] ?: key
    }

  }

}





@Serializable
enum class LibraryIcon : AppIcon<LibraryIcon> {
  SETTINGS, SAVINGS, AUDIO_TRACK, PANORAMA, BROWSER, FOLDER, RADIO, PLAYLIST, CAST, CAST_CONNECTED, SOMAFM;


  companion object {
    init {
      AppIcon.register(*values())
    }
  }

  override val data: Any
    get() = when (this) {
      SAVINGS -> Icons.Default.Savings
      CAST -> Icons.Default.Cast
      CAST_CONNECTED -> Icons.Default.CastConnected
      SETTINGS -> Icons.Default.Settings
      AUDIO_TRACK -> R.drawable.ic_audio_track
      PANORAMA -> Icons.Default.Panorama
      BROWSER -> R.drawable.ic_web
      SOMAFM -> "https://cloudflare-ipfs.com/ipns/audienz.danbrough.org/media/somafm.png"
      FOLDER -> R.drawable.ic_folder
      RADIO -> R.drawable.ic_radio
      PLAYLIST -> R.drawable.ic_playlist
    }


}


//private val log = danbroid.logging.getLog(AppIcon::class)
