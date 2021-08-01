package danbroid.audioservice.app.ui.menu

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.insets.statusBarsHeight
import danbroid.audio.library.AudioClientViewModel
import danbroid.audio.library.menu.Menu
import danbroid.audio.library.menu.MenuDSL
import danbroid.audio.somafm.SomaFM
import danbroid.audioservice.app.content.*
import danbroid.audioservice.app.ui.AppIcon
import danbroid.audioservice.app.ui.components.DemoImage

@Composable
fun MenuListItemImpl(title: String, subTitle: String, _icon: Any?, onClicked: () -> Unit) {
  Row(modifier = Modifier.height(62.dp).fillMaxWidth().clickable { onClicked() }, verticalAlignment = Alignment.CenterVertically) {

    val imageModifier = Modifier.size(52.dp).padding(start = 4.dp)
    var icon = _icon

    if (icon is AppIcon)
      icon = AppIcon.lookup(icon)



    icon?.also {
      when (it) {
        is Bitmap ->
          Icon(it.asImageBitmap(), title, modifier = imageModifier)
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

typealias MenuScreenScope = LazyListScope


@ExperimentalFoundationApi
@MenuDSL
inline fun MenuScreenScope.menu(stickHeader: Boolean = false, crossinline onCreate: @Composable Menu.() -> Unit) {


  val menu = Menu("_${MenuContext.NEXT_ID++}", "Untitled")
  log.dinfo("MENU: $menu")

  val content: @Composable LazyItemScope.() -> Unit = {
    log.dtrace("MENU BLOCK: $menu")
    val context = LocalMenuContext.current!!
    menu.onCreate()
    if (!menu.isHidden) {
      MenuListItemImpl(menu.title, menu.subTitle, menu.icon) {
        context.onClicked(menu)
      }
    }
  }

  val id = {
    menu.id.also {
      log.derror("MENU ID: $it")
    }
  }

  if (stickHeader)
    stickyHeader(id, content)
  else
    item(id, content)

}

@Composable
@MenuDSL
fun MenuScreen(content: MenuScreenScope.() -> Unit) {
  Column {
    Spacer(Modifier.fillMaxWidth().statusBarsHeight().background(MaterialTheme.colors.primary))
    LazyColumn {
      content()
    }
  }
}

@Composable
fun MenuScreen(menuID: String, navController: NavHostController, audioClientModel: AudioClientViewModel) {
  val menuModel = menuModel(menuID)
  log.dtrace("MenuScreen() $menuID")
  val menuContext = MenuContext(menuID, menuModel, navController, audioClientModel)
  CompositionLocalProvider(LocalMenuContext provides menuContext) {
    when (menuID) {
      URI_CONTENT -> RootMenu()
      URI_SOMA_FM -> SomaFM()
      URI_PLAYLIST -> Playlist()
      URI_TEST -> TestContent()
      else -> log.error("Unhandled menuID: $menuID")
    }
  }
}


