package danbroid.audioservice.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import danbroid.audio.library.AudioClientViewModel
import danbroid.audio.library.BackButtonHandler
import danbroid.audio.library.audioClientModel
import danbroid.audioservice.app.ui.controls.BottomControls
import kotlinx.coroutines.launch

@Composable
fun MainScaffold(
    navController: NavHostController,
    bottomSheetScaffoldState: BottomSheetScaffoldState
) {
  val coroutineScope = rememberCoroutineScope()
  val audioClientModel: AudioClientViewModel = audioClientModel()
  val insets = LocalWindowInsets.current


  val navBottom = with(LocalDensity.current) { insets.navigationBars.bottom.toDp() }
  log.trace("NAV BAR BOTTOM: ${navBottom}")
  val sheetHeight = 50.dp

  BottomSheetScaffold(
      modifier = Modifier,
/*      topBar = {
        Row {
          Text(title)
        }
      },*/
      scaffoldState = bottomSheetScaffoldState,
      sheetBackgroundColor = MaterialTheme.colors.primary,
      sheetContent = {
        Box(Modifier.fillMaxWidth().fillMaxHeight()) {
          BottomControls(expanded = bottomSheetScaffoldState.bottomSheetState.isExpanded, audioClientModel)
        }

      },
      //sheetPeekHeight = if (bottomSheetScaffoldState.bottomSheetState.isExpanded) 0.dp else 56.dp
      sheetPeekHeight = sheetHeight + navBottom
  ) {
    DemoNavGraph(Modifier.navigationBarsPadding(end = false).padding(bottom = sheetHeight), navController, audioClientModel)
  }

  BackButtonHandler(bottomSheetScaffoldState.bottomSheetState.isExpanded) {
    coroutineScope.launch {
      bottomSheetScaffoldState.bottomSheetState.collapse()
    }
  }
}



