package danbroid.audio.content

import android.content.Context
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import danbroid.audio.library.AudioLibrary
import danbroid.audio.log
import danbroid.audio.service.util.httpSupport
import danbroid.util.format.uriEncode
import danbroid.util.misc.SingletonHolder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.CacheControl
import java.util.concurrent.TimeUnit

const val SOMA_CHANNELS_URL = "https://somafm.com/channels.json"


@Serializable
data class SomaChannel(
  val id: String,
  val title: String,
  val description: String,
  val dj: String,
  val twitter: String,
  @SerialName("djmail")
  val djMail: String,
  val genre: String,
  val image: String,
  @SerialName("largeimage")
  val largeImage: String,
  @SerialName("xlimage")
  val extraLargeImage: String,
  val listeners: Int,
  val lastPlaying: String,
  val playlists: List<Playlist>,
  @SerialName("preroll")
  val preRoll: List<String> = emptyList()
) {
  @Serializable
  data class Playlist(val url: String, val format: Format, val quality: Quality) {

    @Serializable
    enum class Quality {
      @SerialName("high")
      HIGH,

      @SerialName("highest")
      HIGHEST,

      @SerialName("low")
      LOW,
    }

    @Serializable
    enum class Format {
      @SerialName("aac")
      AAC,

      @SerialName("aacp")
      AACP,

      @SerialName("mp3")
      MP3
    }
  }
}

@Serializable
data class SomaChannels(val channels: List<SomaChannel>)


class SomaFMLibrary(val context: Context) : AudioLibrary {

  companion object : SingletonHolder<SomaFMLibrary, Context>(::SomaFMLibrary)

  val json = Json {
    ignoreUnknownKeys = true
  }


  @Suppress("BlockingMethodInNonBlockingContext")
  suspend fun channels(): List<SomaChannel> =
    context.httpSupport.requestString(
      SOMA_CHANNELS_URL,
      CacheControl.Builder().maxStale(7, TimeUnit.DAYS).build(),
      true
    ).let {
      json.decodeFromString<SomaChannels>(it).channels
    }

  override suspend fun loadItem(mediaID: String): MediaItem? {
    log.trace("loadItem() $mediaID")
    val somaID = mediaID.substringAfterLast('/')
    log.trace("somaID: $somaID")

    return channels().firstOrNull {
      it.id == somaID
    }?.mediaItem
  }
}

val Context.somaFM: SomaFMLibrary
  get() = SomaFMLibrary.getInstance(this)


val SomaChannel.mediaItem: MediaItem
  get() = MediaItem.Builder()
    .setMediaId("somafm://${id.uriEncode()}")
    .setMediaMetadata(mediaMetadata.build())
    .setUri(playlists.first { it.format == SomaChannel.Playlist.Format.AAC }.url)
    .build()

val SomaChannel.mediaMetadata: MediaMetadata.Builder
  get() = MediaMetadata.Builder()
    .setDisplayTitle(title)
    .setSubtitle(description)
    .setArtworkUri(image.toUri())
    .setIsPlayable(true)


