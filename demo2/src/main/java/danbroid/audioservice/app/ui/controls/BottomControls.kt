package danbroid.audioservice.app.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.media3.common.util.UnstableApi
import com.google.accompanist.insets.LocalWindowInsets
import danbroid.audio.client.AudioClient
import danbroid.audio.formatDurationFromSeconds
import danbroid.audio.library.AudioClientViewModel
import danbroid.audio.library.R
import danbroid.audio.service.AudioService
import danbroid.audioservice.app.log
import danbroid.audioservice.app.ui.theme.DemoTheme
import danbroid.audioservice.app.ui.theme.LightThemeColors


@Composable
private fun PlayerButton(
  imageVector: ImageVector,
  contentDescription: String = "",
  modifier: Modifier = Modifier.size(42.dp),
  onClicked: () -> Unit = {}
) {
  IconButton(onClicked, modifier = modifier) {
    Icon(
      imageVector = imageVector,
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
  skipToPrev: () -> Unit = {}, togglePlay: () -> Unit = {}, skipToNext: () -> Unit = {}
) {


  ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
    val (button1, button2, button3, text1, text2, upArrow, menu) = createRefs()

    val buttonsEnd = createAbsoluteRightBarrier(button1, button2, button3)
    val buttonModifier = Modifier.size(38.dp)
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
      PlayerButton(
        Icons.Default.SkipPrevious,
        stringResource(R.string.lbl_skip_prev),
        button1Modifier,
        skipToPrev
      )


    if (isPlaying)
      PlayerButton(Icons.Default.Pause, stringResource(R.string.pause), button2Modifier, togglePlay)
    else
      PlayerButton(
        Icons.Default.PlayArrow,
        stringResource(R.string.play),
        button2Modifier,
        togglePlay
      )

    if (hasNext)
      PlayerButton(
        Icons.Default.SkipNext,
        stringResource(R.string.lbl_skip_next),
        button3Modifier,
        skipToNext
      )

    Icon(
      painterResource(if (expanded) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up),
      stringResource(R.string.swipe_up),
      tint = LocalContentColor.current.copy(alpha = 0.2f),
      modifier = Modifier
        .size(28.dp)
        .constrainAs(upArrow) {
          //end.linkTo(parent.end, margin = 4.dp)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
          top.linkTo(parent.top)
        }
    )

    Text(
      title ?: "",
      style = MaterialTheme.typography.subtitle1,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,

      modifier = Modifier.constrainAs(text1) {
        top.linkTo(parent.top)
        linkTo(buttonsEnd, parent.end, startMargin = 0.dp, endMargin = 4.dp, bias = 0F)
        width = Dimension.fillToConstraints
      }
    )

    Text(
      subTitle ?: "",
      style = MaterialTheme.typography.caption,
      maxLines = 2,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier.constrainAs(text2) {
        linkTo(buttonsEnd, parent.end, startMargin = 0.dp, endMargin = 4.dp, bias = 0F)
        top.linkTo(text1.bottom)
        width = Dimension.fillToConstraints

      }
    )

    var menuExpanded by remember { mutableStateOf(false) }

    val navRight =
      with(LocalDensity.current) { LocalWindowInsets.current.navigationBars.right.toDp() }

    Box(modifier = Modifier
      .constrainAs(menu) {
        end.linkTo(parent.end, margin = navRight)
      }
      .wrapContentHeight(Alignment.Bottom)) {
      IconButton({ menuExpanded = true }) {
        Icon(imageVector = Icons.Default.MoreVert, "")
      }

      DropdownMenu(
        expanded = menuExpanded,
        offset = DpOffset(0.dp, 0.dp),
        onDismissRequest = {
          log.trace("onDismissRequest")
          menuExpanded = false
        },
      ) {
        DropdownMenuItem({
          log.trace("clicked item1")
          menuExpanded = false
        }) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Cast, "", tint = MaterialTheme.colors.primary)
            Text(
              "Cast to TV",
              modifier = Modifier.padding(8.dp),
              style = MaterialTheme.typography.subtitle2
            )
          }
        }
        DropdownMenuItem({
          log.trace("clicked item2")
          menuExpanded = false
        }) {
          Icon(Icons.Default.Info, "", tint = MaterialTheme.colors.primary)
          Text(
            "Item 2 with a very long title",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.subtitle2
          )
        }
      }
    }
  }
}

@Preview
@Composable()
private fun BottomControlsPreview() {
  DemoTheme {
    CompositionLocalProvider(LocalContentColor provides contentColorFor(MaterialTheme.colors.primary)) {
      Column(
        Modifier
          .background(MaterialTheme.colors.primary)
          .width(400.dp)
      ) {
        BottomControls(
          "The Title which is quote long here as you can see",
          "The Subtitle which is really long and so will have ellipsis",
          true,
          true,
          true,
          true
        )
      }
    }
  }
}

@Composable
fun ExtraControls(
  value: Float,
  durationInSeconds: Float,
  onValueChange: (Float) -> Unit,
  onValueChangeFinished: () -> Unit = {}
) {

  DemoTheme(
    colors = LightThemeColors.copy(
      primary = LightThemeColors.onPrimary,
      onPrimary = LightThemeColors.primary
    )
  ) {
    Row(
      Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
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

@Preview(showBackground = true)
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
@OptIn(UnstableApi::class)
fun BottomControls(expanded: Boolean = false, audioClientModel: AudioClientViewModel) {
  log.debug("BottomControls() expanded: $expanded")

  val player = audioClientModel.client
  val playerState by player.playState.collectAsState()
  val queueState by player.queueState.collectAsState()
  val currentItem by player.currentItem.collectAsState()

  val title = currentItem?.mediaMetadata?.title ?: ""
  val subTitle = currentItem?.mediaMetadata?.subtitle ?: ""


  val modifier = if (expanded) Modifier.padding(bottom = 50.dp) else Modifier

  Column(modifier = modifier.fillMaxWidth()) {

    BottomControls(
      title.toString(),
      subTitle.toString(),
      queueState.hasPrevious,
      playerState == AudioClient.PlayerState.PLAYING,
      queueState.hasNext,
      expanded,
      player::skipToPrev,
      player::togglePause,
      player::skipToNext
    )

    if (expanded) {
      val playPosition by player.playPosition.collectAsState()
      var dragging by remember { mutableStateOf(false) }
      var value by remember { mutableStateOf(playPosition.currentPos) }

      if (!dragging)
        value = playPosition.currentPos

      if (playPosition.duration > 0L) {
        ExtraControls(value, playPosition.duration, {
          log.trace("value: $it")
          value = it
          dragging = true
        }, onValueChangeFinished = {
          log.trace("on value change ")
          player.seekTo(value)
          dragging = false
        })
      }

      currentItem?.mediaMetadata?.extras?.also { extras ->
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(20.dp)) {
          val colorModifier = Modifier.size(24.dp)
          listOf(
            AudioService.MEDIA_METADATA_KEY_DARK_COLOR,
            AudioService.MEDIA_METADATA_KEY_LIGHT_COLOR,
            AudioService.MEDIA_METADATA_KEY_DARK_MUTED_COLOR,
            AudioService.MEDIA_METADATA_KEY_LIGHT_MUTED_COLOR,
            AudioService.MEDIA_METADATA_KEY_DOMINANT_COLOR,
            AudioService.MEDIA_METADATA_KEY_VIBRANT_COLOR,
          ).forEach {
            Box(
              modifier = colorModifier.background(
                Color(
                  extras.getInt(
                    it,
                    android.graphics.Color.TRANSPARENT
                  )
                )
              )
            )
          }
        }
      }

      var menuExpanded by remember { mutableStateOf(false) }
      Box(
        modifier = Modifier
          .fillMaxSize()
          .wrapContentSize(Alignment.TopStart)
      ) {
        Icon(imageVector = Icons.Default.MoreVert, "", modifier = Modifier.clickable {
          menuExpanded = true
        })
        DropdownMenu(
          expanded = menuExpanded,
          onDismissRequest = {
            log.trace("onDismissRequest")
            menuExpanded = false
          },
          modifier = Modifier.background(Color.Red)
        ) {
          DropdownMenuItem({
            log.trace("clicked item1")
            menuExpanded = false
          }) {
            Row {
              Icon(Icons.Default.Cast, "")
              Text("Cast to TV")
            }
          }
          DropdownMenuItem({
            log.trace("clicked item2")
            menuExpanded = false
          }) {
            Text("Item 2")
          }
        }

      }
    }
  }
}

@Composable
fun DropdownDemo() {
  var expanded by remember { mutableStateOf(false) }
  val items = listOf("A", "B", "C", "D", "E", "F")
  val disabledValue = "B"
  var selectedIndex by remember { mutableStateOf(0) }
  Box(
    modifier = Modifier
      .fillMaxSize()
      .wrapContentSize(Alignment.TopStart)
  ) {
    Text(
      items[selectedIndex],
      modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = { expanded = true })
        .background(
          Color.Gray
        )
    )
    DropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Color.Red
        )
    ) {
      items.forEachIndexed { index, s ->
        DropdownMenuItem(onClick = {
          selectedIndex = index
          expanded = false
        }) {
          val disabledText = if (s == disabledValue) {
            " (Disabled)"
          } else {
            ""
          }
          Text(text = s + disabledText)
        }
      }
    }
  }
}



