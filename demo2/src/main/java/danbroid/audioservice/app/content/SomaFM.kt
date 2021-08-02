package danbroid.audioservice.app.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import danbroid.audioservice.app.ui.menu.LocalMenuContext
import danbroid.audioservice.app.ui.menu.menu
import danbroid.audioservice.app.ui.menu.menuScreen
import danbroid.util.format.uriEncode

@Composable
fun TestIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {
  val colorFilter = if (tint == Color.Unspecified) null else ColorFilter.tint(tint)
  val semantics = if (contentDescription != null) {
    Modifier.semantics {
      this.contentDescription = contentDescription
      this.role = Role.Image
    }
  } else {
    Modifier
  }
  Box(
      modifier.size(24.dp)
          .paint(
              painter,
              //   colorFilter = colorFilter,
              contentScale = ContentScale.Fit
          )
          .then(semantics)
  )
}

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