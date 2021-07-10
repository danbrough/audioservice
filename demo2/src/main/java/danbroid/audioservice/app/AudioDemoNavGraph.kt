package danbroid.audioservice.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController


object MainDestinations {
  const val HOME_ROUTE = "home"
}

@Composable
fun JetsnackNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainDestinations.HOME_ROUTE,
) {
  NavHost(
      navController = navController,
      startDestination = startDestination,
      modifier = modifier
  ) {

  }
}

