package danbroid.demo.content

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.media2.common.MediaMetadata
import danbroid.audio.service.AudioService
import kotlinx.serialization.Serializable

@Serializable
data class AudioTrack(
    var id: String,
    var title: String = "Untitled",
    var subTitle: String = "",
    var iconURI: String? = null,
    var bitrate: Int = -1
) {
  constructor(md: MediaMetadata) : this(
      md.getString(MediaMetadata.METADATA_KEY_MEDIA_ID)!!
  ) {
    title = md.getText(MediaMetadata.METADATA_KEY_DISPLAY_TITLE)?.toString() ?: "Untitled"
    subTitle = md.getText(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE)?.toString() ?: ""
    bitrate = md.extras?.getInt(AudioService.MEDIA_METADATA_KEY_BITRATE) ?: -1
    iconURI = md.getString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI)
  }
}


fun AudioTrack.toMediaMetadata(): MediaMetadata.Builder = MediaMetadata.Builder()
    .putString(MediaMetadata.METADATA_KEY_MEDIA_ID, id)
    .putString(MediaMetadata.METADATA_KEY_MEDIA_URI, id)
    .putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE, title)
    .putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE, subTitle)
    .putLong(MediaMetadata.METADATA_KEY_PLAYABLE, 1)
    .putString(MediaMetadata.METADATA_KEY_ARTIST, subTitle)
    .setExtras(bundleOf())

    .also { builder ->
      if (iconURI != null) builder.putString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI, iconURI)

      var _bundle: Bundle? = null
      val bundle: () -> Bundle = {
        _bundle ?: Bundle().also {
          _bundle = it
          builder.setExtras(it)
        }
      }
      if (bitrate != -1)
        bundle().putInt(AudioService.MEDIA_METADATA_KEY_BITRATE, bitrate)
    }



