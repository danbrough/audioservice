package danbroid.audioservice.app.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import danbroid.audioservice.app.menu.MenuItem
import danbroid.audioservice.app.ui.components.DemoImage


@ExperimentalMaterialApi
@Composable
fun MenuScreen(
    title: String,
    menus: List<MenuItem>,
    menuItemClicked: (MenuItem) -> Unit = {},
) {
  log.dtrace("MenuScreen()")


/*  val audioClientModel = viewModel<AudioClientModel>(factory = AudioClientModelFactory(LocalContext.current))
  log.ddebug("audioClient: ${audioClientModel.client}")*/
  Column {

    Text(title, style = MaterialTheme.typography.caption)

    val imageModifier = Modifier.size(40.dp)
    LazyColumn {
      items(menus, { it.id }) { menu ->
        Row {
          ListItem(
              icon = {
                menu.imageURI?.also {
                  DemoImage(
                      imageUrl = it,
                      menu.title,
                      modifier = imageModifier,
                  )
                } ?: Icon(
                    Icons.Filled.Audiotrack,
                    contentDescription = null,
                    modifier = imageModifier,
                    tint = MaterialTheme.colors.primary
                )
              },
              text = { Text(menu.title) },
              secondaryText = { Text(menu.subTitle) },
              modifier = Modifier.clickable {
                menuItemClicked.invoke(menu)
              }
          )
        }
        Divider()
      }
    }
  }

}


private val log = danbroid.logging.getLog("danbroid.audioservice.app.ui.menu")
