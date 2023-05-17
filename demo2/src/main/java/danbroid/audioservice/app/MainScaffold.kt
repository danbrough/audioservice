package danbroid.audioservice.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import danbroid.audio.library.AudioClientViewModel

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
      Column(Modifier.fillMaxSize()) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Current song title", modifier = Modifier.weight(1f))
          IconButton(onClick = { /*TODO play song*/ }) {
            Icon(
              Icons.Rounded.PlayArrow,
              contentDescription = "Play"
            )
          }
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



