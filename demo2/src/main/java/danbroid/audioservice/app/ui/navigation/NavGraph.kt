package danbroid.audioservice.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import danbroid.audioservice.app.ui.home.Home

object Routes {
  const val HOME = "home"
}

@Composable
fun NavGraph(
  modifier: Modifier = Modifier,
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
    NavPage {
      Home(it)
    }
  }
}


@Composable
fun NavPage(content: @Composable () -> Unit) =
  Box(Modifier.fillMaxWidth().padding(8.dp)) {
    content()
  }

