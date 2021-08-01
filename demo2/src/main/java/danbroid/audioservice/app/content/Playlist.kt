package danbroid.audioservice.app.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.media2.common.MediaMetadata
import danbroid.audio.library.menu.MenuDSL
import danbroid.audioservice.app.R
import danbroid.audioservice.app.ui.menu.LocalMenuContext
import danbroid.audioservice.app.ui.menu.MenuScreen
import danbroid.audioservice.app.ui.menu.MenuScreenScope
import danbroid.audioservice.app.ui.menu.menu
import kotlinx.coroutines.flow.map

const val URI_PLAYLIST = "$URI_PREFIX/playlist"

@MenuDSL
fun MenuScreenScope.PlaylistMenu() {

  menu {
    val playlistSize by LocalMenuContext.current!!.menuModel.playlist.map { it.size }.collectAsState(true)
    id = URI_PLAYLIST
    title = stringResource(R.string.playlist)
    subTitle = "$playlistSize items"
    isHidden = playlistSize == 0
    isBrowsable = true
  }

}


@Composable
fun Playlist() {

  val audioClient = LocalMenuContext.current!!.audioClientModel.client
  val playList by audioClient.playlist.collectAsState(emptyList())
  MenuScreen {
    playList.forEach {
      menu {
        id = it.metadata?.mediaId ?: ""
        title = it.metadata?.getString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE)
            ?: it.metadata?.getString(MediaMetadata.METADATA_KEY_TITLE) ?: ""
        subTitle = it.metadata?.getString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE)
            ?: it.metadata?.getString(MediaMetadata.METADATA_KEY_DISPLAY_DESCRIPTION) ?: ""
        isPlayable = true
        icon = it.metadata?.getString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI)
      }
    }
  }
}