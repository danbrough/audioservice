package danbroid.audioservice.app

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import danbroid.audioservice.app.ui.navigation.NavGraph
import danbroid.audioservice.app.ui.theme.DemoTheme


@Composable
fun MainScaffold(
  modifier: Modifier = Modifier,
  title: String = "AudioService Demo",
  navController: NavHostController = rememberNavController(),
  scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
  Scaffold(
    modifier = Modifier,
    //  snackbarHost = { SnackBarHost(it) },
    topBar = {
      Row {
        Text(title)
      }
    },
/*    topBar = {
      Column {
        Spacer(
          modifier = Modifier.statusBarsHeight().fillMaxWidth()
            .background(MaterialTheme.colors.primaryVariant)
        )
        Button({}) {
          Text("Testing")
        }
        BrewAppBar(
          title = title,
          navController = navController
        )
      }
    },*/
    //  bottomBar = { BottomNavButtons(navController = navController) },
    scaffoldState = scaffoldState,
  ) { innerPaddingModifier ->
    //log.derror("innerPadding ${innerPaddingModifier}")
    NavGraph(Modifier.padding(innerPaddingModifier), navController)
  }
}


@Preview
@Composable
fun MainScaffoldPreview() {
  DemoTheme {
    MainScaffold()
  }
}