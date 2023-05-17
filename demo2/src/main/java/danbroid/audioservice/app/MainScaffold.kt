package danbroid.audioservice.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import danbroid.audio.library.AudioClientViewModel
import danbroid.audioservice.app.ui.controls.BottomControls

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScaffold(
  navController: NavHostController,
  audioClientModel: AudioClientViewModel
) {
  val coroutineScope = rememberCoroutineScope()
  val navBottom = with(LocalDensity.current) { WindowInsets.navigationBars.getBottom(this).toDp() }
  log.trace("NAV BAR BOTTOM: $navBottom")
  val scaffoldState = rememberBottomSheetScaffoldState()
  val sheetHeight = 50.dp


  BottomSheetScaffold(
    sheetBackgroundColor = MaterialTheme.colors.primary,
    sheetPeekHeight = sheetHeight + navBottom,
    sheetContent = {
      Box(
        Modifier
          .fillMaxWidth()
          .fillMaxHeight()
      ) {
        BottomControls(expanded = scaffoldState.bottomSheetState.isExpanded, audioClientModel)
      }
    }) {
    Box(
      Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
      Text("Main Screen Content")
    }
  }
}



