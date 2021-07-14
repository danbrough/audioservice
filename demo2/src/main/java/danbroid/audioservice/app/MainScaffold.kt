package danbroid.audioservice.app

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun TestScaffold(
    title: String = "AudioService Demo",
    navController: NavHostController = rememberNavController(),
) {
  val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
  val coroutineScope = rememberCoroutineScope()

  val insets = LocalWindowInsets.current

  log.trace("navBarsTop: ${insets.navigationBars.layoutInsets.top} bottom: ${insets.navigationBars.layoutInsets.bottom}")
  log.trace("statusBarsTop: ${insets.statusBars.layoutInsets.top} bottom: ${insets.statusBars.layoutInsets.bottom}")
  BottomSheetScaffold(
      modifier = Modifier,
      topBar = {
        Row {
          Text(title)
        }
      },
      scaffoldState = bottomSheetScaffoldState,
      sheetBackgroundColor = MaterialTheme.colors.primary,
      sheetContent = {
        Box(Modifier.fillMaxWidth().fillMaxHeight().navigationBarsPadding(bottom = true).let {
          if (bottomSheetScaffoldState.bottomSheetState.isExpanded)
            it.statusBarsPadding()
          else it
        }) {
          Column {
            Text(text = "Hello from sheet",
                style = MaterialTheme.typography.subtitle2)


          }
        }

      },
      //sheetPeekHeight = if (bottomSheetScaffoldState.bottomSheetState.isExpanded) 0.dp else 56.dp
      sheetPeekHeight = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) 56.dp else 100.dp
  ) {

    DemoNavGraph(Modifier.padding(it), navController)

  }
}

@Composable
fun MainScaffold(
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
    DemoNavGraph(Modifier.padding(innerPaddingModifier), navController)
  }
}


