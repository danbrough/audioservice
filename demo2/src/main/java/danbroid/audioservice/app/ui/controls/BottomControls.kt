package danbroid.audioservice.app.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media2.common.MediaMetadata
import danbroid.audioservice.app.R
import danbroid.audioservice.app.audioClientModel
import danbroid.audioservice.app.ui.theme.DemoTheme
import danbroid.audioservice.app.ui.theme.LightThemeColors
import danbroid.demo.formatDurationFromSeconds
import danbroid.media.client.AudioClient

private val playerButtonSize = 46.dp

@Composable
private fun PlayerButton(imageVector: ImageVector, contentDescription: String = "", onClicked: () -> Unit = {}) {
  IconButton(onClicked, modifier = Modifier.size(playerButtonSize)) {
    Icon(imageVector = imageVector,
        contentDescription = contentDescription,
        tint = MaterialTheme.colors.secondary,
        modifier = Modifier.size(42.dp)
    )
  }
}


@Composable
private fun BottomControls(
    title: String? = "title", subTitle: String? = "subTitle",
    hasPrevious: Boolean, isPlaying: Boolean, hasNext: Boolean,
    skipToPrev: () -> Unit = {}, togglePlay: () -> Unit = {}, skipToNext: () -> Unit = {}) {

  Column {
    Row {
      if (hasPrevious)
        PlayerButton(Icons.Default.SkipPrevious, stringResource(R.string.lbl_skip_prev), skipToPrev)
      else
        Spacer(Modifier.width(playerButtonSize))

      if (isPlaying)
        PlayerButton(Icons.Default.Pause, stringResource(R.string.pause), togglePlay)
      else
        PlayerButton(Icons.Default.PlayArrow, stringResource(R.string.play), togglePlay)

      if (hasNext)
        PlayerButton(Icons.Default.SkipNext, stringResource(R.string.lbl_skip_next), skipToNext)
      else
        Spacer(Modifier.width(playerButtonSize))

      Column(Modifier.fillMaxWidth()) {
        Text(title ?: "", style = MaterialTheme.typography.subtitle1)
        Text(subTitle
            ?: "", style = MaterialTheme.typography.subtitle2, maxLines = 2, overflow = TextOverflow.Ellipsis)
      }
    }

  }
}

@Preview
@Composable()
private fun BottomControlsPreview() {
  DemoTheme {
    CompositionLocalProvider(LocalContentColor provides contentColorFor(MaterialTheme.colors.primary)) {
      Column(Modifier.background(MaterialTheme.colors.primary).width(300.dp)) {
        BottomControls("The Title", "The Subtitle", true, true, true)
      }
    }
  }
}

@Composable
fun ExtraControls(value: Float, durationInSeconds: Float, onValueChange: (Float) -> Unit, onValueChangeFinished: () -> Unit = {}) {
  Text("Extra controls")

  MaterialTheme(colors = LightThemeColors.copy(primary = LightThemeColors.onPrimary, onPrimary = LightThemeColors.primary)) {
    Slider(
        value,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        // steps = 5,
        valueRange = 0f..durationInSeconds,
        modifier = Modifier.fillMaxWidth(),
    )

  }

}

@Preview
@Composable()
private fun ExtraControlsPreview() {
  DemoTheme {
    Column(Modifier.background(MaterialTheme.colors.primary).width(300.dp)) {
      var value by remember { mutableStateOf(120f) }
      ExtraControls(value, 600f, {
        value = it
      })
    }

  }
}

@Composable
fun BottomControls(expanded: Boolean = false) {
  log.ddebug("BottomControls() expanded: $expanded")
  val audioClientModel = audioClientModel()
  val player = audioClientModel.client
  val playerState by player.playState.collectAsState()
  val queueState by player.queueState.collectAsState()
  val currentItem by player.currentItem.collectAsState()
  val playPosition by player.playPosition.collectAsState()

  val title = currentItem?.metadata?.getString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE) ?: ""
  val subTitle = currentItem?.metadata?.getString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE) ?: ""

  var dragging by remember { mutableStateOf(false) }
  var value by remember { mutableStateOf(playPosition.currentPos) }
  if (!dragging)
    value = playPosition.currentPos

  Column {
    BottomControls(
        title, subTitle,
        queueState.hasPrevious, playerState == AudioClient.PlayerState.PLAYING, queueState.hasNext,
        player::skipToPrev, player::togglePause, player::skipToNext
    )
    if (expanded) {
      Text(playPosition.currentPos.formatDurationFromSeconds())
      Text(playPosition.duration.formatDurationFromSeconds())
      if (playPosition.duration > 0L) {
        ExtraControls(value, playPosition.duration, {
          log.dtrace("value: $it")
          value = it
          dragging = true
        }, onValueChangeFinished = {
          log.dtrace("on value change ")
          player.seekTo(value)
          dragging = false
        })
      }

    }
  }

}


private val log = danbroid.logging.getLog("danbroid.audioservice.app.ui.controls")
