package danbroid.audioservice.app.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.media2.common.MediaMetadata
import danbroid.audio.library.menu.MenuDSL
import danbroid.audioservice.app.ui.menu.LocalMenuContext
import danbroid.audioservice.app.ui.menu.MenuContext
import danbroid.audioservice.app.ui.menu.menu
import danbroid.audioservice.app.ui.menu.menuScreen
import kotlinx.coroutines.flow.map

const val URI_PLAYLIST = "$URI_PREFIX/playlist"


@MenuDSL
suspend fun MenuContext.playlistMenu() {

/*
  menu {
    val playlistSize by menuModel.playlist.map { it.size }.collectAsState(true)
    id = URI_PLAYLIST
    title = stringResource(R.string.playlist)
    subTitle = "$playlistSize items"
    isHidden = playlistSize == 0
    isBrowsable = true
  }
*/

}


@Composable
fun PlaylistMenu() {

  val context = LocalMenuContext.current
  val audioClient = context.audioClientModel.client
  val playList by audioClient.playlist.collectAsState(emptyList())
  val playlistPosition by audioClient.queueState.map { it.position }.collectAsState(-1)

  log.error("PLAYLIST POSITION $playlistPosition")
  menuScreen {
    playList.forEachIndexed { index, mediaItem ->
      val id = mediaItem.metadata?.mediaId ?: "_${MenuContext.NEXT_ID++}"
      menu(id, highLighted = playlistPosition == index) {
        title = mediaItem.metadata?.getString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE)
            ?: mediaItem.metadata?.getString(MediaMetadata.METADATA_KEY_TITLE) ?: ""
        subTitle = mediaItem.metadata?.getString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE)
            ?: mediaItem.metadata?.getString(MediaMetadata.METADATA_KEY_DISPLAY_DESCRIPTION) ?: ""
        isPlayable = true
        icon = mediaItem.metadata?.getString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI)
      }
    }
  }
}