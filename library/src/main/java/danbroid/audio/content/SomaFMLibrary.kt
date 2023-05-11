package danbroid.audio.content

import android.content.Context
import androidx.core.net.toUri
import androidx.media2.common.MediaItem
import androidx.media2.common.MediaMetadata
import androidx.media2.common.UriMediaItem
import danbroid.audio.http.httpSupport
import danbroid.audio.http.httpSupport2
import danbroid.audio.library.AudioLibrary
import danbroid.audio.library.MenuState
import danbroid.audio.menu.Menu
import danbroid.audio.utils.parsePlaylistURL
import danbroid.util.format.uriEncode
import danbroid.util.misc.SingletonHolder
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.CacheControl
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

const val SOMA_CHANNELS_URL = "https://somafm.com/channels.json"

const val URI_SOMA_PREFIX = "somafm:/"

const val URI_SOMA_CHANNELS = "$URI_SOMA_PREFIX/channels"

internal val log = danbroid.logging.getLog("danbroid.audio.content")

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
  suspend fun channels2(): List<SomaChannel> =
      context.httpSupport.requestString(SOMA_CHANNELS_URL, CacheControl.Builder().maxStale(7, TimeUnit.DAYS).build(), true).let {
        json.decodeFromString<SomaChannels>(it).channels
      }

  suspend fun channels(): List<SomaChannel> = context.httpSupport2.client.get(SOMA_CHANNELS_URL) {
    headers {
      append(HttpHeaders.CacheControl, "max-stale=${7.toDuration(DurationUnit.DAYS).inWholeSeconds}")
    }
  }.body<SomaChannels>().channels

  override fun loadMenus(menuID: String): Flow<MenuState>? =
      if (menuID == URI_SOMA_CHANNELS)
        flow {
          emit(MenuState.LOADED("Soma FM", channels().map { it.toMenu() }))
        }
      else null


  override suspend fun loadItem(mediaID: String): MediaItem? {
    log.trace("loadItem() $mediaID")
    val somaID = mediaID.substringAfterLast('/')
    log.trace("somaID: $somaID")

    return channels().firstOrNull {
      it.id == somaID
    }?.let { somaChannel ->

      val metadata = somaChannel.mediaMetadata
      val playlistURL = somaChannel.playlists.first { it.format == SomaChannel.Playlist.Format.AAC }.url

      val audioURL = parsePlaylistURL(context, playlistURL) ?: run {
        log.error("failed to find audio url in $playlistURL")
        return null
      }
      log.dtrace("playlist: $playlistURL -> $audioURL")
      UriMediaItem.Builder(mediaID.toUri())
          .setStartPosition(0L).setEndPosition(-1L)
          .setMetadata(metadata
              .putString(MediaMetadata.METADATA_KEY_MEDIA_URI, audioURL)
              .build())
          .build()
    }
  }
}

val Context.somaFM: SomaFMLibrary
  get() = SomaFMLibrary.getInstance(this)


val SomaChannel.mediaMetadata: MediaMetadata.Builder
  get() = MediaMetadata.Builder()
      .putString(MediaMetadata.METADATA_KEY_MEDIA_ID, "$URI_SOMA_PREFIX/${id.uriEncode()}")
      .putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE, title)
      .putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE, description)
      .putString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI, image)
      .putLong(MediaMetadata.METADATA_KEY_PLAYABLE, 1)
      .putString(MediaMetadata.METADATA_KEY_MEDIA_URI, playlists.first { it.format == SomaChannel.Playlist.Format.AAC }.url)


fun SomaChannel.toMenu(): Menu = Menu("somafm://${id.uriEncode()}", title, description, image, isPlayable = true)