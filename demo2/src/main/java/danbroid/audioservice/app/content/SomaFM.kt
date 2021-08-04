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

  log.dtrace("SomaFM")
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

        /*item("somafm://${it.id.uriEncode()}_") {
          ListItem(
              Modifier.clickable {
                log.dwarn("CLICKED ${it.id}")
              },
              icon = {
                Icon(
                    painter = rememberImagePainter(it.image),
                    contentDescription = it.title,
                    modifier = Modifier.size(48.dp),
                    tint = Color.Unspecified,
                )
              },
              secondaryText = { Text(it.description) }
          ) { Text(it.title) }
          Divider()
        }*/
      }
    }

  }.exceptionOrNull()?.also {
    log.error(it.message, it)
  }
}