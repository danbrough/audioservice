package danbroid.audioservice.app.ui.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import danbroid.audio.library.AudioClientViewModel
import danbroid.audio.menu.MenuModel
import danbroid.audio.menu.menuModel
import danbroid.audioservice.app.content.*
import danbroid.audioservice.app.log


@Composable
fun MenuScreen(
  menuID: String,
  navController: NavHostController,
  audioClientModel: AudioClientViewModel
) {
  val menuModel = menuModel<MenuModel>(menuID)
  log.trace("MenuScreen() $menuID")
  val context =
    MenuContext(menuID, LocalContext.current, menuModel, audioClientModel, navController)
  CompositionLocalProvider(LocalMenuContext provides context) {
    when (menuID) {
      URI_CONTENT -> RootMenu()
      //TODO URI_SOMA_FM -> SomaFM()
      URI_PLAYLIST -> PlaylistMenu()
      URI_TEST -> TestContent()
      else -> error("Unhandled menuID: $menuID")
    }
  }
}


