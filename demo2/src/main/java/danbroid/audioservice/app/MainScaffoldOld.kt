package danbroid.audioservice.app

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import danbroid.audio.library.AudioClientViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScaffoldOld(
  navController: NavHostController,
  audioClientModel: AudioClientViewModel
) {
  /*
    val coroutineScope = rememberCoroutineScope()
    val insets = LocalWindowInsets.current

    val scaffoldState = rememberBottomSheetScaffoldState()

    val navBottom = with(LocalDensity.current) { insets.navigationBars.bottom.toDp() }
    log.trace("NAV BAR BOTTOM: ${navBottom}")
    val sheetHeight = 50.dp

    BottomSheetScaffold(
      modifier = Modifier,
      */
  /*      topBar = {
              Row {
                Text(title)
              }
            },*//*

    scaffoldState = scaffoldState,
    sheetBackgroundColor = MaterialTheme.colors.primary,
    sheetContent = {
      Box(
        Modifier
          .fillMaxWidth()
          .fillMaxHeight()) {
        BottomControls(expanded = scaffoldState.bottomSheetState.isExpanded, audioClientModel)
      }

    },
    //sheetPeekHeight = if (bottomSheetScaffoldState.bottomSheetState.isExpanded) 0.dp else 56.dp
    sheetPeekHeight = sheetHeight + navBottom
  ) {
    DemoNavGraphOld(
      Modifier
        .navigationBarsPadding(end = false)
        .padding(bottom = sheetHeight),
      navController,
      audioClientModel
    )

    BackButtonHandler(scaffoldState.bottomSheetState.isExpanded) {
      coroutineScope.launch {
        scaffoldState.bottomSheetState.collapse()
      }
    }
  }
*/


}



