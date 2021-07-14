package danbroid.audioservice.app.ui.controls

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import danbroid.audioservice.app.R
import danbroid.audioservice.app.audioClientModel
import danbroid.audioservice.app.ui.theme.DemoTheme
import danbroid.media.client.AudioClient


@Composable
private fun BottomControls(hasPrevious: Boolean, isPlaying: Boolean, hasNext: Boolean,
                           skipToPrev: () -> Unit = {}, togglePlay: () -> Unit = {}, skipToNext: () -> Unit = {}) {

  val buttonModifier = Modifier.padding(2.dp).then(Modifier.size(42.dp))
  Row {
    if (hasPrevious)
      IconButton(skipToPrev, modifier = buttonModifier) {
        Icon(imageVector = Icons.Default.SkipPrevious, contentDescription = stringResource(R.string.lbl_skip_prev), tint = MaterialTheme.colors.secondary)
      }

    IconButton(togglePlay, modifier = buttonModifier) {
      if (isPlaying)
        Icon(imageVector = Icons.Default.Pause, contentDescription = stringResource(R.string.pause), tint = MaterialTheme.colors.secondary)
      else
        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = stringResource(R.string.play), tint = MaterialTheme.colors.secondary)
    }

    if (hasNext)
      IconButton(skipToNext, modifier = buttonModifier) {
        Icon(imageVector = Icons.Default.SkipNext, contentDescription = stringResource(R.string.lbl_skip_next), tint = MaterialTheme.colors.secondary)
      }
  }
}

@Preview
@Composable
private fun Preview1() {
  DemoTheme {
    BottomControls(true, true, true)
  }

}

@Composable
fun BottomControls() {
  val audioClientModel = audioClientModel()
  val player = audioClientModel.client
  val playerState by player.playState.collectAsState()
  val queueState by player.queueState.collectAsState()
  BottomControls(
      queueState.hasPrevious, playerState == AudioClient.PlayerState.PLAYING, queueState.hasNext,
      player::skipToPrev, player::togglePause, player::skipToNext
  )

}

private val log = danbroid.logging.getLog("danbroid.audioservice.app.ui.controls")
