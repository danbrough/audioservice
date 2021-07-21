package danbroid.audioservice.app

import androidx.compose.animation.*
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
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.insets.statusBarsPadding
import danbroid.audioservice.app.content.URI_BROWSER
import danbroid.audioservice.app.content.URI_CONTENT
import danbroid.audioservice.app.content.URI_SETTINGS
import danbroid.audioservice.app.ui.browser.BrowserScreen
import danbroid.audioservice.app.ui.home.MenuScreen
import danbroid.audioservice.app.ui.menu.menuModel
import danbroid.audioservice.app.ui.settings.SettingsScreen
import danbroid.media.client.AudioClientModel
import danbroid.util.format.uriEncode

object Routes {
  const val HOME = "home"
  const val MENU = "menu"
  const val SETTINGS = "settings"
  const val BROWSER = "browser"
}


@ExperimentalAnimationApi
@Composable
fun EnterAnimation(content: @Composable () -> Unit) {
  AnimatedVisibility(
      visible = true,
      enter = slideInHorizontally(
          initialOffsetX = { -40 }
      ) + fadeIn(initialAlpha = 0.3f),
      exit = slideOutHorizontally() + fadeOut(),
      content = content,
      initiallyVisible = false
  )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DemoNavGraph(
    modifier: Modifier,
    navController: NavHostController,
    audioClientModel: AudioClientModel
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

    EnterAnimation {
      Menu(menuID, navController, audioClientModel)
    }

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
    BrowserScreen(audioClientModel)
  }
}


@Composable
private fun Menu(menuID: String, navController: NavHostController, audioClientModel: AudioClientModel) {
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
        navController.navigate("${Routes.MENU}?id=${menuItem.id.uriEncode()}", menuNavOptions)
      } else if (menuItem.isPlayable) {
        audioClientModel.client.playUri(menuItem.id)
      }
    }
  }
}


@Composable
fun NavPage(content: @Composable () -> Unit) =
    Box(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp).statusBarsPadding()) {
      content()
    }

