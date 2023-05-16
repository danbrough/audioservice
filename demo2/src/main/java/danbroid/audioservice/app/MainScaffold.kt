package danbroid.audioservice.app


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import danbroid.audio.library.AudioClientViewModel
import danbroid.audioservice.app.ui.controls.BottomControls

@Composable
fun MainScaffold(
    navController: NavHostController,
    audioClientModel: AudioClientViewModel
) {
  val scaffoldState = rememberBottomSheetScaffoldState()

  BottomSheetScaffold(
      scaffoldState = scaffoldState,
      sheetBackgroundColor = MaterialTheme.colors.primary,
      sheetPeekHeight = 50.dp,
      sheetContent = {
        BottomControls(expanded = scaffoldState.bottomSheetState.isExpanded, audioClientModel)

        /*Column(Modifier.fillMaxSize()) {
          Row(
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 16.dp),
              verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Current song title", modifier = Modifier.weight(1f))
            IconButton(onClick = { *//*TODO play song*//* }) {
              Icon(
                  Icons.Rounded.PlayArrow,
                  contentDescription = "Play"
              )
            }
          }
        }*/
      }) {
    Box(Modifier
        .fillMaxSize()
        .padding(16.dp)) {
      Text("Main Screen Content")
    }
  }
}



