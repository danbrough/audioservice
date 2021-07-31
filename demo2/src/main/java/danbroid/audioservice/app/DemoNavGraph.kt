package danbroid.audioservice.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.navDeepLink
import danbroid.audio.library.AudioClientViewModel
import danbroid.audioservice.app.content.URI_BROWSER
import danbroid.audioservice.app.content.URI_CONTENT
import danbroid.audioservice.app.content.URI_SETTINGS
import danbroid.audioservice.app.rnz.RNZLibrary
import danbroid.audioservice.app.ui.browser.BrowserScreen
import danbroid.audioservice.app.ui.menu.MenuScreen
import danbroid.audioservice.app.ui.settings.SettingsScreen
import danbroid.util.format.uriEncode

object Routes {
  const val HOME = "home"
  const val MENU = "menu"
  const val SETTINGS = "settings"
  const val BROWSER = "browser"

  fun menuRoute(menuID: String) = "${MENU}?id=${menuID.uriEncode()}"
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DemoNavGraph(
    modifier: Modifier,
    navController: NavHostController,
    audioClientModel: AudioClientViewModel
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
    MenuScreen(URI_CONTENT, navController, audioClientModel)
  }

  composable("${Routes.MENU}?id={id}", arguments = listOf(
      navArgument("id") {
        defaultValue = URI_CONTENT
        type = NavType.StringType
      }
  )) { entry ->

    val menuID = entry.arguments?.getString("id")!!

    MenuScreen(menuID, navController, audioClientModel)
  }

  composable(
      Routes.SETTINGS,
      deepLinks = listOf(navDeepLink {
        uriPattern = URI_SETTINGS
      })) { _ ->
    SettingsScreen()
  }

  composable(
      Routes.BROWSER,
      deepLinks = listOf(navDeepLink {
        uriPattern = URI_BROWSER
      })) { _ ->
    BrowserScreen(RNZLibrary.URL_RNZ, audioClientModel)
  }
}




