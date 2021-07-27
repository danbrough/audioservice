package danbroid.audioservice.app

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.navDeepLink
import danbroid.audioservice.app.content.URI_BROWSER
import danbroid.audioservice.app.content.URI_CONTENT
import danbroid.audioservice.app.content.URI_SETTINGS
import danbroid.audioservice.app.rnz.RNZLibrary
import danbroid.audioservice.app.ui.browser.BrowserScreen
import danbroid.audioservice.app.ui.home.MenuScreen
import danbroid.audioservice.app.ui.menu.menuModel
import danbroid.audioservice.app.ui.settings.SettingsScreen
import danbroid.util.format.uriEncode

object Routes {
  const val HOME = "home"
  const val MENU = "menu"
  const val SETTINGS = "settings"
  const val BROWSER = "browser"
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DemoNavGraph(
    modifier: Modifier,
    navController: NavHostController,
    audioClientModel: DemoAudioClientModel
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
    Menu(URI_CONTENT, navController, audioClientModel)
  }

  composable("${Routes.MENU}?id={id}", arguments = listOf(
      navArgument("id") {
        defaultValue = URI_CONTENT
        type = NavType.StringType
      }
  )) { entry ->

    val menuID = entry.arguments?.getString("id")!!

    Menu(menuID, navController, audioClientModel)


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

  composable(
      Routes.BROWSER,
      deepLinks = listOf(navDeepLink {
        uriPattern = URI_BROWSER
      })) { entry ->
    BrowserScreen(RNZLibrary.URL_RNZ, audioClientModel)
  }
}


@Composable
private fun Menu(menuID: String, navController: NavHostController, audioClientModel: DemoAudioClientModel) {
  log.dwarn("Showing Menu screen id:${menuID}")
  val menuModel = menuModel(menuID)
  val menuState by menuModel.state.collectAsState()
  log.dtrace("title: ${menuState.menuItem.title}")

  val menuNavOptions: NavOptionsBuilder.() -> Unit = {
    anim {
      enter = R.anim.menu_enter
      exit = R.anim.menu_exit
      popEnter = R.anim.menu_pop_enter
      popExit = R.anim.menu_pop_exit
    }
  }

  MenuScreen(menuState.menuItem.title, menuState.children) { menuItem ->
    log.dtrace("clicked $menuItem")

    menuItem.onClicked?.also {
      log.dtrace("calling click handler for ${menuItem.id}")
      it.invoke(menuItem)
      return@MenuScreen
    }

    navController.findDestination(menuItem.id)?.also {
      log.dtrace("found destination $it")
      navController.navigate(menuItem.id)
      return@MenuScreen
    }

    if (navController.graph.hasDeepLink(menuItem.id.toUri())) {
      log.trace("Navigating to deeplink:${menuItem.id}")
      navController.navigate(menuItem.id.toUri())
    } else if (menuItem.isBrowsable) {
      log.trace("Opening menu ${menuItem.id}")
      navController.navigate("${Routes.MENU}?id=${menuItem.id.uriEncode()}", menuNavOptions)
    } else if (menuItem.isPlayable) {
      audioClientModel.play(menuItem.id)
    }
  }
}


