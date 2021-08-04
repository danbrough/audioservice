package danbroid.audio.rnz

import android.content.Context
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.core.text.parseAsHtml
import androidx.media2.common.MediaItem
import androidx.media2.common.MediaMetadata
import androidx.media2.common.UriMediaItem
import danbroid.audio.library.AudioLibrary
import danbroid.audio.service.util.httpSupport
import danbroid.util.misc.SingletonHolder
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.CacheControl
import java.io.IOException
import java.util.concurrent.TimeUnit


class RNZLibrary(context: Context) : AudioLibrary {
  val httpSupport = context.httpSupport

  override suspend fun loadItem(mediaID: String): MediaItem? {
    log.dtrace("loadItem() $mediaID")
    val progID = if (mediaID == URI_RNZ_NEWS) rnzNewsProgrammeID() else
      getIDFromProgrammeURI(mediaID)

    val defaultIcon = if (mediaID == URI_RNZ_NEWS) rnzNewsIcon else rnzNationalIcon
    log.dtrace("progID: $progID")
    return if (progID != null) loadProgramme(progID).toMediaItem(defaultIcon).also {
      log.dtrace("MediaItem: $it")
    } else null
  }

  companion object : SingletonHolder<RNZLibrary, Context>(::RNZLibrary) {
    const val SCHEME_RNZ = "rnz"
    const val URI_PREFIX_RNZ_PROGRAMME = "$SCHEME_RNZ://programme"
    const val URI_RNZ_NEWS = "$SCHEME_RNZ://news"
    const val URL_RNZ = "https://www.rnz.co.nz/topics"
    const val URL_RNZ_NEWS = "https://www.rnz.co.nz/news"
    private const val rnzNationalIcon = "https://cloudflare-ipfs.com/ipns/audienz.danbrough.org/media/rnz-national.jpg"
    private const val rnzNewsIcon = "https://cloudflare-ipfs.com/ipns/audienz.danbrough.org/media/rnz_news.jpg"

    fun getProgrammeURI(id: Long) = "$URI_PREFIX_RNZ_PROGRAMME/$id"
    fun getIDFromProgrammeURI(uri: String): Long? =
        if (uri.startsWith(URI_PREFIX_RNZ_PROGRAMME))
          uri.substringAfter("$URI_PREFIX_RNZ_PROGRAMME/").toLong() else null
  }

  private val json = Json {

  }


  @Throws(IOException::class)
  suspend fun rnzNewsProgrammeID(): Long {
    val content = httpSupport.requestString(URL_RNZ_NEWS)

    content.lines().forEach { line ->
      if (line.contains("radio-new-zealand-news")) {
        line.substringAfter("href=\"").split('/').forEach { part ->
          try {
            return part.toLong()
          } catch (err: NumberFormatException) {
            //try again with next line
          }
        }
      }
    }

    throw IOException("Failed to parse $URL_RNZ_NEWS")
  }


  @Serializable
  private data class RNZItem(val item: RNZProgramme)

  @Throws(IOException::class)
  suspend fun loadProgramme(
      progID: Long
  ): RNZProgramme {
    log.trace("loadProgramme() $progID")

    val progURL = RNZProgramme.getProgrammeURL(progID)

    log.dtrace("progURL: $progURL")

    val response = httpSupport.requestString(
        progURL,
        CacheControl.Builder().maxStale(Int.MAX_VALUE, TimeUnit.SECONDS).build()
    )

    return json.decodeFromString<RNZItem>(response).item
  }
}


/*fun RNZProgramme.toMenuItem(): MenuItem = MenuItem(
    id = RNZLibrary.getProgrammeURI(id),
    title = programmeName.parseAsHtml(HtmlCompat.FROM_HTML_MODE_COMPACT).toString(),
    subTitle = body.parseAsHtml(HtmlCompat.FROM_HTML_MODE_COMPACT).toString(),
    iconURI = if (thumbnail != null) "http://www.rnz.co.nz$thumbnail" else null,
    isPlayable = true
)*/

fun RNZProgramme.toMediaItem(defaultIcon: String): MediaItem =
    getMetadata(defaultIcon).build().let {
      UriMediaItem.Builder(it.getString(MediaMetadata.METADATA_KEY_MEDIA_URI)!!.toUri())
          .setStartPosition(0L).setEndPosition(-1L)
          .setMetadata(it)
          .build()
    }


fun RNZProgramme.getMetadata(defaultIcon:String): MediaMetadata.Builder = MediaMetadata.Builder()
    .putString(MediaMetadata.METADATA_KEY_MEDIA_ID, RNZLibrary.getProgrammeURI(id))
    .putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE, programmeName.parseAsHtml(HtmlCompat.FROM_HTML_MODE_COMPACT).toString())
    .putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE, body.parseAsHtml(HtmlCompat.FROM_HTML_MODE_COMPACT).toString())
    .putString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI, if (thumbnail != null) "http://www.rnz.co.nz$thumbnail" else defaultIcon)
    .putLong(MediaMetadata.METADATA_KEY_PLAYABLE, 1)
    .putString(MediaMetadata.METADATA_KEY_MEDIA_URI, audio.mp3?.url ?: audio.ogg!!.url)

val Context.rnz: RNZLibrary
  get() = RNZLibrary.getInstance(this)

private val log = danbroid.logging.getLog(RNZLibrary::class)
