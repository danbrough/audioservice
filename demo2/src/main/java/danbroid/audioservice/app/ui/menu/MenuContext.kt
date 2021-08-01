package danbroid.audioservice.app.ui.menu

import androidx.compose.runtime.compositionLocalOf
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import danbroid.audio.library.AudioClientViewModel
import danbroid.audio.library.menu.Menu
import danbroid.audioservice.app.Routes

class MenuContext(
    val id: String,
    val menuModel: MenuModel,
    val navController: NavHostController,
    val audioClientModel: AudioClientViewModel) {

  companion object {
    var NEXT_ID = 0
  }

  fun onClicked(menu: Menu) {
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
/*  val menuNavOptions: NavOptionsBuilder.() -> Unit = {
    anim {
      enter = R.anim.menu_enter
      exit = R.anim.menu_exit
      popEnter = R.anim.menu_pop_enter
      popExit = R.anim.menu_pop_exit
    }
  }*/
  }

}

val log = danbroid.logging.getLog("danbroid.audioservice.app.ui.menu")

val LocalMenuContext = compositionLocalOf<MenuContext?> { null }
