package danbroid.audioservice.app.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import danbroid.audioservice.app.ui.menu.LocalMenuContext
import danbroid.audioservice.app.ui.menu.menu
import danbroid.audioservice.app.ui.menu.menuScreen
import danbroid.util.format.uriEncode


@Composable
fun SomaFM() {
  val context = LocalMenuContext.current

  log.dtrace("SOMAFM")
  runCatching {
    val somaChannels by context.menuModel.somaChannels.collectAsState()

    menuScreen {
      somaChannels.forEach {
        menu("somafm://${it.id.uriEncode()}") {
          title = it.title
          subTitle = it.description
          icon = it.image
          isPlayable = true
        }
      }
    }

  }.exceptionOrNull()?.also {
    log.error(it.message, it)
  }
}