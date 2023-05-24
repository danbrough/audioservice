package danbroid.audio.content

import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import danbroid.audio.service.AudioService
import kotlinx.serialization.Serializable

@DslMarker
annotation class TestDataDSL

@TestDataDSL
class TestData {
  val testData = mutableListOf<AudioTrack>()
}

@TestDataDSL
fun testData(block: TestData.() -> Unit) = TestData().apply {
  block()
}

@TestDataDSL
fun TestData.item(block: AudioTrack.() -> Unit) = AudioTrack("").also {
  it.block()
  testData.add(it)
}

@Serializable
data class AudioTrack(
  var id: String,
  var title: String = "Untitled",
  var subTitle: String = "",
  var iconURI: String? = null,
  var bitrate: Int = -1
) {
  @OptIn(UnstableApi::class)
  constructor(id: String, md: MediaMetadata) : this(id) {
    title = md.displayTitle?.toString() ?: "Untitled"
    subTitle = md.subtitle?.toString() ?: ""
    bitrate = md.extras?.getInt(AudioService.MEDIA_METADATA_KEY_BITRATE) ?: -1
    iconURI = md.artworkUri?.toString()
  }
}

@OptIn(UnstableApi::class)
fun AudioTrack.toMediaMetadata(): MediaMetadata.Builder = MediaMetadata.Builder().apply {
  setDisplayTitle(title)
  setSubtitle(subTitle)
  setArtworkUri(iconURI?.toUri())
  setIsPlayable(true)
  setExtras(bundleOf(AudioService.MEDIA_METADATA_KEY_BITRATE to bitrate))
}

fun MediaItem.toAudioTrack(): AudioTrack = AudioTrack(mediaId, mediaMetadata)


/*
//.putString(MediaMetadata.METADATA_KEY_MEDIA_ID, id)
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
*/


