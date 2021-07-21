package danbroid.audioservice.app.rnz

import android.content.Context
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.core.text.parseAsHtml
import androidx.media2.common.MediaItem
import androidx.media2.common.MediaMetadata
import androidx.media2.common.UriMediaItem
import danbroid.audioservice.app.menu.MenuItem
import danbroid.demo.rnz.RNZProgramme
import danbroid.media.service.MediaLibrary
import danbroid.media.service.util.httpSupport
import danbroid.util.misc.SingletonHolder
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.CacheControl
import java.io.IOException
import java.util.concurrent.TimeUnit


class RNZSupport(context: Context) : MediaLibrary {
  val httpSupport = context.httpSupport

  override suspend fun loadItem(mediaID: String): MediaItem? = getIDFromProgrammeURI(mediaID)?.let {
    loadProgramme(it).toMediaItem()
  }

  companion object : SingletonHolder<RNZSupport, Context>(::RNZSupport) {
    const val SCHEME_RNZ = "rnz"
    const val URI_PREFIX_RNZ_PROGRAMME = "$SCHEME_RNZ://programme"

    fun getProgrammeURI(id: Long) = "$URI_PREFIX_RNZ_PROGRAMME/$id"
    fun getIDFromProgrammeURI(uri: String): Long? =
        if (uri.startsWith(URI_PREFIX_RNZ_PROGRAMME))
          uri.substringAfter("$URI_PREFIX_RNZ_PROGRAMME/").toLong() else null
  }

  private val json = Json {

  }


  @Serializable
  private data class RNZItem(val item: RNZProgramme)

  @Throws(IOException::class)
  suspend fun loadProgramme(
      progID: Long
  ): RNZProgramme {
    log.trace("loadProgramme() $progID")

    val progURL = RNZProgramme.getProgrammeURL(progID)

    val response = httpSupport.requestString(
        progURL,
        CacheControl.Builder().maxStale(Int.MAX_VALUE, TimeUnit.SECONDS).build()
    )

    return json.decodeFromString<RNZItem>(response).item
  }
}


fun RNZProgramme.toMenuItem(): MenuItem = MenuItem(
    id = RNZSupport.getProgrammeURI(id),
    title = programmeName.parseAsHtml(HtmlCompat.FROM_HTML_MODE_COMPACT).toString(),
    subTitle = body.parseAsHtml(HtmlCompat.FROM_HTML_MODE_COMPACT).toString(),
    iconURI = if (thumbnail != null) "http://www.rnz.co.nz$thumbnail" else null,
    isPlayable = true
)

fun RNZProgramme.toMediaItem(): MediaItem =
    mediaMetadata.build().let {
      UriMediaItem.Builder(it.getString(MediaMetadata.METADATA_KEY_MEDIA_URI)!!.toUri())
          .setStartPosition(0L).setEndPosition(-1L)
          .setMetadata(it)
          .build()
    }


val RNZProgramme.mediaMetadata: MediaMetadata.Builder
  get() = MediaMetadata.Builder()
      .putString(MediaMetadata.METADATA_KEY_MEDIA_ID, RNZSupport.getProgrammeURI(id))
      .putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE, programmeName.parseAsHtml(HtmlCompat.FROM_HTML_MODE_COMPACT).toString())
      .putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE, body.parseAsHtml(HtmlCompat.FROM_HTML_MODE_COMPACT).toString())
      .putString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI, if (thumbnail != null) "http://www.rnz.co.nz$thumbnail" else null)
      .putLong(MediaMetadata.METADATA_KEY_PLAYABLE, 1)
      .putString(MediaMetadata.METADATA_KEY_MEDIA_URI, audio.mp3?.url ?: audio.ogg!!.url)

val Context.rnz: RNZSupport
  get() = RNZSupport.getInstance(this)

private val log = danbroid.logging.getLog(RNZSupport::class)
