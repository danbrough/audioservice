package danbroid.audio.content

import androidx.media2.common.MediaItem
import danbroid.audio.http.HttpSupport2
import danbroid.audio.library.AudioLibrary
import danbroid.audio.library.MenuState
import danbroid.audio.log
import danbroid.audio.menu.Menus
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class IPFSLibrary(private val httpSupport2: HttpSupport2) : AudioLibrary {

  companion object {
    const val IPFS_PREFIX = "ipfs://"
    const val IPNS_PREFIX = "ipns://"

    val gateways = listOf("https://h1.danbrough.org", "https://cloudflare-ipfs.com")
  }

  override suspend fun loadItem(mediaID: String): MediaItem? {
    return null
  }

  override fun loadMenus(menuID: String): Flow<MenuState>? {
    val useIPFS = menuID.startsWith(IPFS_PREFIX)
    val useIPNS = menuID.startsWith(IPNS_PREFIX)
    if (!useIPFS && !useIPNS) return null
    return flow {
      val cid = if (useIPFS) "ipfs/${menuID.substring(IPFS_PREFIX.length)}"
      else "ipns/${menuID.substring(IPNS_PREFIX.length)}"
      val url = "${gateways[0]}/$cid"

      runCatching {
        log.trace("getting ... $url")

        httpSupport2.client.get(url) {
          headers {

            append(HttpHeaders.CacheControl, "max-stale=${7.toDuration(DurationUnit.DAYS).inWholeSeconds}")
          }
        }.body<Menus>().also {
          emit(MenuState.LOADED(it.title, it.menus))
        }

      }.exceptionOrNull()?.also {
        log.error(it.message, it)
        emit(MenuState.ERROR(it.message ?: "Error loading $menuID"))
      }
    }
  }
}

