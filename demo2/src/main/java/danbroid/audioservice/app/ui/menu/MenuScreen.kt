package danbroid.audioservice.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.google.accompanist.insets.statusBarsHeight
import danbroid.audioservice.app.DemoAudioClientModel
import danbroid.audioservice.app.Routes
import danbroid.audioservice.app.menu.MenuItem
import danbroid.audioservice.app.ui.AppIcon
import danbroid.audioservice.app.ui.components.DemoImage
import danbroid.audioservice.app.ui.menu.menuModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration


@Composable
private fun MenuScreen(content: LazyListScope.() -> Unit) {
  Column {
    Spacer(Modifier.fillMaxWidth().statusBarsHeight().background(MaterialTheme.colors.primary))
    LazyColumn {
      content()
    }
  }
}

@Composable
fun MenuScreen(menus: List<MenuItem>, menuItemClicked: (MenuItem) -> Unit) {
  MenuScreen {
    items(menus, { it.id }) { menu ->
      MenuListItem(menu.title, menu.subTitle, menu.iconURI, { menuItemClicked(menu) })
    }
  }
}

@Composable
fun LazyListScope.menu(menu: MenuItem, onClicked: (MenuItem) -> Unit, block: @Composable MenuItem.() -> Unit) {
  menu.block()
  item(menu.id) {
    MenuListItem(menu.title, menu.subTitle, menu.iconURI, { onClicked(menu) })
  }
}

fun LazyListScope.menu(menu: MenuItem, onClicked: (MenuItem) -> Unit) {
  item(menu.id) {
    MenuListItem(menu.title, menu.subTitle, menu.iconURI, { onClicked(menu) })
  }
}
/*
items(menus, { it.id }) { menu ->
        MenuListItem(menu.title, menu.subTitle, menu.iconURI, { menuItemClicked.invoke(menu) })
      }
 */

@Composable
fun MenuListItem(title: String, subTitle: String, icon: Any?, onClicked: () -> Unit) {
  Row(modifier = Modifier.height(62.dp).fillMaxWidth().clickable { onClicked() }, verticalAlignment = Alignment.CenterVertically) {

    val imageModifier = Modifier.size(52.dp).padding(start = 4.dp)
    var icon = icon

    if (icon is AppIcon)
      icon = AppIcon.lookup(icon)

    icon?.also {
      when (it) {
        is String ->
          DemoImage(
              imageUrl = it,
              title,
              modifier = imageModifier
          )
        is ImageVector ->
          Icon(
              it,
              title,
              modifier = imageModifier,
              tint = MaterialTheme.colors.primary,
          )
        is Int ->
          Icon(
              painterResource(it),
              title,
              modifier = imageModifier,
              tint = MaterialTheme.colors.primary,
          )
        else ->
          DemoImage(
              imageUrl = it.toString(),
              title,
              modifier = imageModifier
          )
      }
    } ?: Icon(
        Icons.Filled.Audiotrack,
        contentDescription = null,
        tint = MaterialTheme.colors.primary,
        modifier = Modifier.size(52.dp).padding(start = 4.dp)
    )
    /*  Image(
          Icons.Filled.Audiotrack, "",
          contentScale = ContentScale.Fit,
          //modifier = Modifier.size(42.dp),
          colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
      )*/
    Column(Modifier.padding(start = 16.dp).fillMaxWidth(), verticalArrangement = Arrangement.Bottom) {
      Text(title, style = MaterialTheme.typography.subtitle1)
      Text(
          subTitle,
          overflow = TextOverflow.Ellipsis, maxLines = 2,
          modifier = Modifier.alpha(ContentAlpha.medium),
          style = MaterialTheme.typography.body2
      )
    }
  }
  Divider()
}

@Composable
fun Menu2(menuID: String, navController: NavHostController, audioClientModel: DemoAudioClientModel) {
  val menuModel = menuModel(menuID)
  val menuState by menuModel.state.collectAsState()
  log.dtrace("title: ${menuState.menuItem.title}")

  val titleFlow = remember {
    flow {
      var count = 1
      while (true) {
        emit("Title: $count")
        count++
        delay(Duration.seconds(1))
      }
    }
  }

  val title: String by titleFlow.collectAsState("Initial Title")

  MenuScreen {
    menu(MenuItem("test", title, "live menu", iconURI = AppIcon.PANORAMA)) {
      log.debug("clicked $it")
    }
  }
}

@Composable
fun Menu(menuID: String, navController: NavHostController, audioClientModel: DemoAudioClientModel) {
  val menuModel = menuModel(menuID)
  val menuState by menuModel.state.collectAsState()
  log.dtrace("title: ${menuState.menuItem.title}")

/*  val menuNavOptions: NavOptionsBuilder.() -> Unit = {
    anim {
      enter = R.anim.menu_enter
      exit = R.anim.menu_exit
      popEnter = R.anim.menu_pop_enter
      popExit = R.anim.menu_pop_exit
    }
  }*/

  MenuScreen(menuState.children) { menuItem ->

    menuItem.onClicked?.also {
      it.invoke(menuItem)
      return@MenuScreen
    }

    navController.findDestination(menuItem.id)?.also {
      navController.navigate(menuItem.id)
      return@MenuScreen
    }

    if (navController.graph.hasDeepLink(menuItem.id.toUri())) {
      navController.navigate(menuItem.id.toUri())
    } else if (menuItem.isBrowsable) {
      navController.navigate(Routes.menuRoute(menuItem.id)) //, menuNavOptions)
    } else if (menuItem.isPlayable) {
      audioClientModel.play(menuItem.id)
    }
  }
}

private val log = danbroid.logging.getLog("danbroid.audioservice.app.ui.menu")
