package danbroid.audioservice.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.navDeepLink
import danbroid.audioservice.app.content.URI_CONTENT
import danbroid.audioservice.app.content.URI_SETTINGS
import danbroid.audioservice.app.ui.home.MenuScreen
import danbroid.audioservice.app.ui.menu.menuModel
import danbroid.audioservice.app.ui.settings.SettingsScreen
import danbroid.util.format.uriEncode

object Routes {
  const val HOME = "home"
  const val MENU = "menu"
  const val SETTINGS = "settings"
}

@Composable
fun DemoNavGraph(
    modifier: Modifier,
    navController: NavHostController
) = NavHost(
    navController,
    modifier = modifier,
    startDestination = Routes.HOME
) {


  /*log.dinfo("screenContext: $screenContext")
  Screen.values().forEach {
    it.composable(this, screenContext)
  }*/

  composable(Routes.HOME) {
    Menu(URI_CONTENT, navController)
  }


  composable("${Routes.MENU}?id={id}", arguments = listOf(
      navArgument("id") {
        defaultValue = URI_CONTENT
        type = NavType.StringType
      }
  )) { entry ->

    val menuID = entry.arguments?.getString("id")!!
    Menu(menuID, navController)

/*
    val backstackEntry = navController.currentBackStackEntryAsState()
    log.info("CURRENT ROUTE: ${backstackEntry.value?.destination?.route}")
*/


  }

  composable(
      Routes.SETTINGS,
      deepLinks = listOf(navDeepLink {
        uriPattern = URI_SETTINGS
      })) { entry ->
    SettingsScreen()
  }
}


@Composable
private fun Menu(menuID: String, navController: NavHostController) {
  log.dwarn("Showing Menu screen id:${menuID}")
  val menuModel = menuModel(menuID)
  val menuState by menuModel.state.collectAsState()
  log.dtrace("title: ${menuState.menuItem.title}")
  NavPage {
    MenuScreen(menuState.menuItem.title, menuState.children) { menuItem ->
      log.ddebug("CLICKED $menuItem")

      navController.findDestination(menuItem.id)?.also {
        log.dtrace("found destination $it")
        navController.navigate(menuItem.id)
        return@MenuScreen
      }

      if (navController.graph.hasDeepLink(menuItem.id.toUri())) {
        log.debug("FOUND DEEPLINK ${menuItem.id}")
        navController.navigate(menuItem.id.toUri())
      } else if (menuItem.isBrowsable) {
        log.debug("BROWING TO ${menuItem.id}")
        navController.navigate("${Routes.MENU}?id=${menuItem.id.uriEncode()}")
      } else if (menuItem.isPlayable) {

      }
    }
  }
}


@Composable
fun NavPage(content: @Composable () -> Unit) =
    Box(Modifier.fillMaxWidth().padding(8.dp)) {
      content()
    }

