package danbroid.audioservice.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import danbroid.audio.content.RNZLibrary
import danbroid.audio.library.AudioClientViewModel
import danbroid.audioservice.app.content.*
import danbroid.audioservice.app.ui.browser.BrowserScreen
import danbroid.audioservice.app.ui.menu.MenuScreen
import danbroid.audioservice.app.ui.settings.SettingsScreen
import danbroid.util.format.uriEncode




@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DemoNavGraphOld(
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

  composable(Routes.SOMAFM, deepLinks = listOf(navDeepLink {
    uriPattern = URI_SOMA_FM
  })) {
    MenuScreen(URI_SOMA_FM, navController, audioClientModel)
  }

  composable(Routes.PLAYLIST, deepLinks = listOf(navDeepLink {
    uriPattern = URI_PLAYLIST
  })) {
    MenuScreen(URI_PLAYLIST, navController, audioClientModel)
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

  //URI_PLAYLIST

  composable(
      Routes.BROWSER,
      deepLinks = listOf(navDeepLink {
        uriPattern = URI_BROWSER
      })) { _ ->
    BrowserScreen(RNZLibrary.URL_RNZ, audioClientModel)
  }
}




