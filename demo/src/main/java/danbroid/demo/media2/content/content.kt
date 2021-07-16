package danbroid.demo.media2.content


import android.content.Context
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danbroid.demo.content.ipfs_gateway
import danbroid.demo.content.somaFM
import danbroid.demo.content.testTracks
import danbroid.demo.media2.R
import danbroid.media.client.AudioClient
import danbroid.media.client.AudioClientModel
import danbroid.util.format.uriEncode
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.MenuItemClickContext
import danbroid.util.menu.menu
import danbroid.util.menu.rootMenu

//URI base for navigation deeplinks
const val URI_CONTENT_ROOT = "demo://content"


private val MenuItemClickContext.audioClient: AudioClient
  get() = fragment.activityViewModels<AudioClientModel>() {
    object : ViewModelProvider.NewInstanceFactory() {
      override fun <T : ViewModel?> create(modelClass: Class<T>) = AudioClientModel(fragment.requireContext()) as T
    }
  }.value.client


fun rootContent(context: Context) = context.rootMenu<MenuItemBuilder> {
  id = URI_CONTENT_ROOT
  titleID = R.string.app_name

  onCreate = {
    log.info("onCreate()")

    testTracks.testData.forEach { track ->
      log.info("track $track")
      menu {
        title = track.title
        subtitle = track.subTitle
        imageID = R.drawable.ic_music_note
        imageURI = track.iconURI

        onClick = {
          audioClient.playUri(track.id)
        }
      }
    }
  }


  menu {
    title = "Soma FM"
    subtitle = "Over 30 unique channels of listener-supported, commercial-free, underground/alternative radio broadcasting to the world"
    isBrowsable = true
    imageURI = "$ipfs_gateway/ipns/audienz.danbrough.org/media/somafm.png"
    onClick = {
      log.dinfo("Test1")
      this@menu.children?.clear()
      context.somaFM.channels().forEach {
        log.debug("channel: ${it.id}: ${it.description}")
        menu {

          id = "somafm://${it.id.uriEncode()}"
          title = it.title
          subtitle = it.description
          imageURI = it.image
          onClick = {
            log.info("SHOULD PLAY: $id")
            audioClient.playUri(id)
          }
        }
      }
      proceed()
    }
  }
}


private val log = danbroid.logging.getLog("danbroid.demo.media2.content")


