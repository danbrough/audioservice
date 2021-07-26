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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.media2.common.MediaMetadata
import com.google.accompanist.insets.statusBarsPadding
import danbroid.audio.client.AudioClient
import danbroid.audio.service.AudioService
import danbroid.audioservice.app.DemoAudioClientModel
import danbroid.audioservice.app.R
import danbroid.audioservice.app.audioClientModel
import danbroid.audioservice.app.ui.theme.DemoTheme
import danbroid.audioservice.app.ui.theme.LightThemeColors
import danbroid.demo.formatDurationFromSeconds


@Composable
private fun PlayerButton(imageVector: ImageVector, contentDescription: String = "", modifier: Modifier = Modifier.size(42.dp), onClicked: () -> Unit = {}) {
  IconButton(onClicked, modifier = modifier) {
    Icon(imageVector = imageVector,
        contentDescription = contentDescription,
        tint = MaterialTheme.colors.secondary,
        modifier = modifier
    )
  }
}


@Composable
private fun BottomControls(
    title: String? = "title", subTitle: String? = "subTitle",
    hasPrevious: Boolean, isPlaying: Boolean, hasNext: Boolean, expanded: Boolean,
    skipToPrev: () -> Unit = {}, togglePlay: () -> Unit = {}, skipToNext: () -> Unit = {}) {


  ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
    val (button1, button2, button3, text1, text2, upArrow) = createRefs()

    val buttonsEnd = createAbsoluteRightBarrier(button1,button2,button3)
    val buttonModifier = Modifier.size(46.dp)
    val button1Modifier = buttonModifier.constrainAs(button1) {
      start.linkTo(parent.start)
      top.linkTo(parent.top)
    }
    val button2Modifier = buttonModifier.constrainAs(button2) {
      start.linkTo(button1.end)
      top.linkTo(button1.top)
    }
    val button3Modifier = buttonModifier.constrainAs(button3) {
      start.linkTo(button2.end)
      top.linkTo(button2.top)
    }
    if (hasPrevious)
      PlayerButton(Icons.Default.SkipPrevious, stringResource(R.string.lbl_skip_prev), button1Modifier, skipToPrev)


    if (isPlaying)
      PlayerButton(Icons.Default.Pause, stringResource(R.string.pause), button2Modifier, togglePlay)
    else
      PlayerButton(Icons.Default.PlayArrow, stringResource(R.string.play), button2Modifier, togglePlay)

    if (hasNext)
      PlayerButton(Icons.Default.SkipNext, stringResource(R.string.lbl_skip_next), button3Modifier, skipToNext)

    Text(
        title ?: "",
        style = MaterialTheme.typography.subtitle1,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,

        modifier = Modifier.constrainAs(text1) {
          top.linkTo(parent.top)
          linkTo(buttonsEnd, parent.end, startMargin = 0.dp, endMargin = 0.dp, bias = 0F)
          width = Dimension.fillToConstraints
        }
    )

    Text(
        subTitle ?: "",
        style = MaterialTheme.typography.subtitle2,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.constrainAs(text2) {
          linkTo(buttonsEnd, parent.end, startMargin = 0.dp, endMargin = 0.dp, bias = 0F)
          top.linkTo(text1.bottom)
          width = Dimension.fillToConstraints

        }
    )

    Icon(
        painterResource(if (expanded) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up),
        stringResource(R.string.swipe_up),
        tint = MaterialTheme.colors.secondary,
        modifier = Modifier.size(28.dp).constrainAs(upArrow) {
          end.linkTo(parent.end, margin = 4.dp)
          top.linkTo(parent.top)
        }
    )
  }
}

@Preview
@Composable()
private fun BottomControlsPreview() {
  DemoTheme {
    CompositionLocalProvider(LocalContentColor provides contentColorFor(MaterialTheme.colors.primary)) {
      Column(Modifier.background(MaterialTheme.colors.primary).width(400.dp)) {
        BottomControls("The Title which is quote long here as you can see", "The Subtitle which is really long and so will have ellipsis", true, true, true, true)
      }
    }
  }
}

@Composable
fun ExtraControls(value: Float, durationInSeconds: Float, onValueChange: (Float) -> Unit, onValueChangeFinished: () -> Unit = {}) {

  DemoTheme(colors = LightThemeColors.copy(primary = LightThemeColors.onPrimary, onPrimary = LightThemeColors.primary)) {
    Row(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
      Text(value.formatDurationFromSeconds())
      Text(durationInSeconds.formatDurationFromSeconds())
    }
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
    var value by remember { mutableStateOf(120f) }
    ExtraControls(value, 600f, {
      value = it
    })
  }
}

@Composable
fun BottomControls(expanded: Boolean = false, audioClientModel: DemoAudioClientModel = audioClientModel()) {
  log.ddebug("BottomControls() expanded: $expanded")

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

  val modifier = if (expanded) Modifier.statusBarsPadding() else Modifier

  Column(modifier = modifier.fillMaxWidth()) {

    BottomControls(
        title, subTitle,
        queueState.hasPrevious, playerState == AudioClient.PlayerState.PLAYING, queueState.hasNext, expanded,
        player::skipToPrev, player::togglePause, player::skipToNext
    )

    if (false)
      currentItem?.metadata?.extras?.also { extras ->
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(20.dp)) {
          val colorModifier = Modifier.size(12.dp)
          listOf(
              AudioService.MEDIA_METADATA_KEY_DARK_COLOR,
              AudioService.MEDIA_METADATA_KEY_LIGHT_COLOR,
              AudioService.MEDIA_METADATA_KEY_DARK_MUTED_COLOR,
              AudioService.MEDIA_METADATA_KEY_LIGHT_MUTED_COLOR,
              AudioService.MEDIA_METADATA_KEY_DOMINANT_COLOR,
              AudioService.MEDIA_METADATA_KEY_VIBRANT_COLOR,
          ).forEach {
            Box(modifier = colorModifier.background(Color(extras.getInt(it, android.graphics.Color.TRANSPARENT))))
          }
        }
      }


    if (expanded) {
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
