package danbroid.audioservice.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import danbroid.audioservice.app.ui.controls.BottomControls

@Composable
fun TestScaffold(
    title: String = "AudioService Demo",
    navController: NavHostController = rememberNavController(),
) {
  val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
  val coroutineScope = rememberCoroutineScope()
  val audioClientModel: DemoAudioClientModel = audioClientModel()
  val insets = LocalWindowInsets.current

  log.trace("navBarsTop: ${insets.navigationBars.layoutInsets.top} bottom: ${insets.navigationBars.layoutInsets.bottom}")
  log.trace("statusBarsTop: ${insets.statusBars.layoutInsets.top} bottom: ${insets.statusBars.layoutInsets.bottom}")


  val navBottom = with(LocalDensity.current) { insets.navigationBars.bottom.toDp() }
  log.trace("NAV BAR BOTTOM: ${navBottom}")

  BottomSheetScaffold(
      modifier = Modifier,
/*      topBar = {
        Row {
          Text(title)
        }
      },*/
      scaffoldState = bottomSheetScaffoldState,
      sheetBackgroundColor = MaterialTheme.colors.primary,
      sheetContent = {
        Box(Modifier.fillMaxWidth().fillMaxHeight()) {
          BottomControls(expanded = bottomSheetScaffoldState.bottomSheetState.isExpanded)
        }

      },
      //sheetPeekHeight = if (bottomSheetScaffoldState.bottomSheetState.isExpanded) 0.dp else 56.dp
      sheetPeekHeight = 56.dp + navBottom
  ) {

    DemoNavGraph(Modifier.navigationBarsPadding().padding(bottom = 56.dp), navController, audioClientModel)

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
    //DemoNavGraph(Modifier.padding(innerPaddingModifier), navController,audioClientModel: AudioClientModel = audioClientModel())
  }
}


