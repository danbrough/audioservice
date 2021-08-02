package danbroid.audioservice.app.ui.menu

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.RemoveFromQueue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.google.accompanist.insets.statusBarsHeight
import danbroid.audio.library.AudioClientViewModel
import danbroid.audio.library.menu.Menu
import danbroid.audio.library.menu.MenuDSL
import danbroid.audioservice.app.Routes
import danbroid.audioservice.app.content.*
import danbroid.audioservice.app.ui.AppIcon
import danbroid.audioservice.app.ui.components.DemoImage
import danbroid.audioservice.app.ui.theme.DemoTheme

@Composable
private fun MenuListItemRow(title: String, subTitle: String, _icon: Any?, onClicked: () -> Unit) {
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
}

@Composable
fun MenuListItemImpl(title: String, subTitle: String, _icon: Any?, highLighted: Boolean = false, onClicked: () -> Unit) {
  if (highLighted) {
    Box {
      MenuListItemRow(title, subTitle, _icon, onClicked)
      Surface(Modifier.height(62.dp).fillMaxWidth().background(Color.Green.copy(alpha = 0.2f))){

      }
    }
  } else
    MenuListItemRow(title, subTitle, _icon, onClicked)
  Divider()
}

@Preview(showBackground = true)
@Composable
private fun MenuItemPreview() {
  DemoTheme {
    Column {
      MenuListItemImpl("The title", "The Subtitle", Icons.Default.QueueMusic) {
      }
      Divider()
      MenuListItemImpl("The title2", "The Subtitle which is a lot longer and so will probably over flow the text display thingy still typing ", Icons.Default.RemoveFromQueue, highLighted = true) {
      }
      Divider()
    }
  }
}

@MenuDSL
inline fun LazyListScope.menu(
    id: String = "_${MenuContext.NEXT_ID++}", stickyHeader: Boolean = false,
    highLighted: Boolean = false,
    crossinline onCreate: @Composable Menu.() -> Unit) {

  val menu = Menu(id, "Untitled")
  log.dtrace("menuID: ${menu.id}")
  item(menu.id) {
    menu.onCreate()
    val context = LocalMenuContext.current

    MenuListItemImpl(menu.title, menu.subTitle, menu.icon) {
      onClicked(menu, context.navHostController, context.audioClientModel)
    }
  }


/*  val content: @Composable LazyItemScope.() -> Unit = {
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
    item(id, content)*/

}

/*
@Composable
@MenuDSL
fun MenuScreen2(content: MenuScreenScope.() -> Unit) {
  Column {
    Spacer(Modifier.fillMaxWidth().statusBarsHeight().background(MaterialTheme.colors.primary))
    LazyColumn {
      content()
    }
  }
}
*/




fun onClicked(menu: Menu, navController: NavHostController, audioClientModel: AudioClientViewModel) {
  log.debug("clicked: $menu")
  menu.onClicked?.also {
    it.invoke()
    return
  }

  navController.findDestination(menu.id)?.also {
    navController.navigate(menu.id)
    return
  }

  if (navController.graph.hasDeepLink(menu.id.toUri())) {
    navController.navigate(menu.id.toUri())
  } else if (menu.isBrowsable) {
    navController.navigate(Routes.menuRoute(menu.id)) //, menuNavOptions)
  } else if (menu.isPlayable) {
    audioClientModel.play(menu.id)
  }

  /*val menuNavOptions: NavOptionsBuilder.() -> Unit = {
    anim {
      enter = R.anim.menu_enter
      exit = R.anim.menu_exit
      popEnter = R.anim.menu_pop_enter
      popExit = R.anim.menu_pop_exit
    }
  }*/
}


@MenuDSL
@Composable
fun menuScreen(block: LazyListScope.() -> Unit) {
  Column {
    Spacer(Modifier.fillMaxWidth().statusBarsHeight().background(MaterialTheme.colors.primary))
    LazyColumn {
      block()
    }
  }
}

@Composable
fun MenuScreen(menuID: String, navController: NavHostController, audioClientModel: AudioClientViewModel) {
  val menuModel = menuModel(menuID)
  log.dtrace("MenuScreen() $menuID")
  val context = MenuContext(menuID, LocalContext.current, menuModel, audioClientModel, navController)
  CompositionLocalProvider(LocalMenuContext provides context) {
    when (menuID) {
      URI_CONTENT -> RootMenu()
      URI_SOMA_FM -> SomaFM()
      URI_PLAYLIST -> PlaylistMenu()
      else -> error("Unhandled menuID: $menuID")
    }
  }
}


