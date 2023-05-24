package danbroid.audioservice.app.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import danbroid.audioservice.app.ui.menu.LocalMenuContext
import danbroid.audioservice.app.ui.menu.menu
import danbroid.audioservice.app.ui.menu.menuScreen

const val URI_PLAYLIST = "$URI_PREFIX/playlist"


@Composable
fun PlaylistMenu() {

  val context = LocalMenuContext.current
  val audioClient = context.audioClientModel.client
  val playList by audioClient.playlist.collectAsState(emptyList())
  val queueState by audioClient.queueState.collectAsState()

  menuScreen {
    playList.forEachIndexed { index, mediaItem ->
      val id = mediaItem.mediaId
      menu(id, highLighted = queueState.position == index) {
        title = mediaItem.mediaMetadata.displayTitle ?: mediaItem.mediaMetadata.title!!
        subTitle = mediaItem.mediaMetadata.subtitle ?: mediaItem.mediaMetadata.description ?: ""
        isPlayable = true
        icon = mediaItem.mediaMetadata.artworkUri
      }
    }
  }
}