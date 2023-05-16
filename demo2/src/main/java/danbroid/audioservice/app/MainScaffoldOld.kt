package danbroid.audioservice.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import danbroid.audio.library.AudioClientViewModel
import danbroid.audio.ui.BackButtonHandler
import danbroid.audioservice.app.ui.controls.BottomControls
import kotlinx.coroutines.launch

@Composable
fun MainScaffoldOld(
    navController: NavHostController,
    audioClientModel: AudioClientViewModel
) {
  val coroutineScope = rememberCoroutineScope()
  //val insets = WindowInsets.current

  val scaffoldState = rememberBottomSheetScaffoldState()

  //val navBottom = with(LocalDensity.current) { insets.navigationBars.bottom.toDp() }
  //log.trace("NAV BAR BOTTOM: $navBottom")
  val sheetHeight = 50.dp

  BottomSheetScaffold(
      modifier = Modifier,
      /*      topBar = {
              Row {
                Text(title)
              }
            },*/
      scaffoldState = scaffoldState,
      sheetBackgroundColor = MaterialTheme.colors.primary,
      sheetContent = {
        Box(Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
          BottomControls(expanded = scaffoldState.bottomSheetState.isExpanded, audioClientModel)
        }

      },
      //sheetPeekHeight = if (bottomSheetScaffoldState.bottomSheetState.isExpanded) 0.dp else 56.dp
      sheetPeekHeight = sheetHeight
  ) {
    DemoNavGraph(
        Modifier,
        //Modifier.navigationBarsPadding(end = false).padding(bottom = sheetHeight),
        navController,
        audioClientModel
    )

    BackButtonHandler(scaffoldState.bottomSheetState.isExpanded) {
      coroutineScope.launch {
        scaffoldState.bottomSheetState.collapse()
      }
    }
  }


}



