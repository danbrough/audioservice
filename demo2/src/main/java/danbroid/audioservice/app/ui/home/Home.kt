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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import danbroid.audioservice.app.menu.MenuItem
import danbroid.audioservice.app.ui.components.DemoImage
import danbroid.audioservice.app.ui.menu.MenuModel
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@Composable
fun Home(
    navBackStackEntry: NavBackStackEntry? = null,
    menuModel: MenuModel,
    itemClicked: (MenuItem) -> Unit = {},
) {
  log.info("Home()")
  val scope = rememberCoroutineScope()


  val menus: List<MenuItem> by menuModel.children.collectAsState()


/*  val audioClientModel = viewModel<AudioClientModel>(factory = AudioClientModelFactory(LocalContext.current))
  log.ddebug("audioClient: ${audioClientModel.client}")*/
  Column {
    Button({
      scope.launch {
        menuModel.test()
      }
    }) {
      Text("Home Page", style = MaterialTheme.typography.caption)
    }
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
                log.info("clicked: $menu")

                scope.launch {
                  menuModel.test()
                }
              }
          )
        }
        Divider()
      }
    }
  }

}


private val log = danbroid.logging.getLog("danbroid.audioservice.app.ui.home")
