package danbroid.audioservice.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import danbroid.audio.library.AudioClientViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScaffold(
  audioClientModel: AudioClientViewModel
) {
  BottomSheetScaffold(
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
        .padding(16.dp)) {
      Text("Main Screen Content")
    }
  }
}



