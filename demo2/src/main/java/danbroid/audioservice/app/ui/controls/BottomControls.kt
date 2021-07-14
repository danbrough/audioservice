package danbroid.audioservice.app.ui.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import danbroid.audioservice.app.ui.theme.DemoTheme
import kotlinx.coroutines.launch

@Composable
fun BottomControls() {
  val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(

  )
  val coroutineScope = rememberCoroutineScope()
  BottomSheetScaffold(
      modifier = Modifier.padding(bottom = 56.dp),
      scaffoldState = bottomSheetScaffoldState,
      sheetBackgroundColor = MaterialTheme.colors.primary,
      sheetContent = {
        Box(
            Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
          Text(text = "Hello from sheet",
              style = MaterialTheme.typography.subtitle2)
        }
      },
      sheetPeekHeight = if (bottomSheetScaffoldState.bottomSheetState.isExpanded) 0.dp else 56.dp
  ) {
    Button(onClick = {
      coroutineScope.launch {

        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
          bottomSheetScaffoldState.bottomSheetState.expand()
        } else {
          bottomSheetScaffoldState.bottomSheetState.collapse()
        }
      }
    }) {
      Text(text = "Expand/Collapse Bottom Sheet")
    }
  }
}

@Preview
@Composable
private fun BottomControlsPreview() {
  DemoTheme {
    BottomControls()
  }
}

private val log = danbroid.logging.getLog("danbroid.audioservice.app.ui.controls")
