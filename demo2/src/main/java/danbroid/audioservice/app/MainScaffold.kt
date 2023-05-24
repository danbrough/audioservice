package danbroid.audioservice.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
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

@ExperimentalMaterialApi
@Composable
fun MainScaffold(
  audioClientModel: AudioClientViewModel
) {
  val navBottom = with(LocalDensity.current) { WindowInsets.navigationBars.getBottom(this).toDp() }
  val statusBarBottom =
    with(LocalDensity.current) { WindowInsets.statusBars.getTop(this).toDp() }
  val scaffoldState = rememberBottomSheetScaffoldState()
  val sheetHeight = 50.dp

  log.trace("statusBarBottom: $statusBarBottom")

  BottomSheetScaffold(
    sheetBackgroundColor = MaterialTheme.colors.primary,
    sheetPeekHeight = sheetHeight + navBottom,
    scaffoldState = scaffoldState,
    sheetContent = {
      Column(
        Modifier
          .fillMaxSize()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Bottom Controls")
          //  BottomControls(expanded = scaffoldState.bottomSheetState.isExpanded, audioClientModel)
        }
      }
    }) {
    Column(Modifier.fillMaxSize()) {
      //DemoNavGraph(audioClientModel = audioClientModel)
      Row(
        Modifier
          .background(MaterialTheme.colors.primary)
          .height(statusBarBottom)
      ) {
        Text(text = "Status Bar")

      }
      Row {
        Text(text = "Content")
      }

    }
  }
}



