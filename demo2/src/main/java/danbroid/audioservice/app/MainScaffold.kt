package danbroid.audioservice.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import danbroid.audio.library.AudioClientViewModel
import danbroid.audioservice.app.ui.controls.BottomControls

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScaffold(
  audioClientModel: AudioClientViewModel
) {
  val navBottom = with(LocalDensity.current) { WindowInsets.navigationBars.getBottom(this).toDp() }

  val scaffoldState = rememberBottomSheetScaffoldState()
  val sheetHeight = 50.dp

  BottomSheetScaffold(
    sheetBackgroundColor = MaterialTheme.colors.primary,
    sheetPeekHeight = sheetHeight + navBottom,

    sheetContent = {
      Column(Modifier.fillMaxSize()) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          BottomControls(expanded = scaffoldState.bottomSheetState.isExpanded, audioClientModel)
        }
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



