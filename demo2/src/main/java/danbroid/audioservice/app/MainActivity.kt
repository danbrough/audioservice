package danbroid.audioservice.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import danbroid.audio.library.AudioClientViewModel
import danbroid.audio.library.RootAudioLibrary
import danbroid.audioservice.app.rnz.RNZLibrary
import danbroid.audioservice.app.ui.theme.DemoTheme
import danbroid.demo.content.SomaFMLibrary
import danbroid.demo.content.TestDataLibrary
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)

    RootAudioLibrary.register(
        TestDataLibrary(),
        SomaFMLibrary.getInstance(this),
        RNZLibrary.getInstance(this)
    )


    setContent {
      ProvideWindowInsets {
        DemoTheme(darkTheme = false) {
          log.derror("MAIN ACTIVITY SET CONTENT")
          val navController = rememberNavController()
          //val scaffoldState = rememberScaffoldState()
          //activity.onBackPressedDispatcher.addCallback(activity, onBackPressedCallback)
          val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
          val scope = rememberCoroutineScope()

          val onBackPressedCallback = object : OnBackPressedCallback(bottomSheetScaffoldState.bottomSheetState.isExpanded) {
            override fun handleOnBackPressed() {
              log.dwarn("COLLAPSING SCAFFOLD!!!!!!!!!!")
              remove()
              scope.launch {
                bottomSheetScaffoldState.bottomSheetState.collapse()
              }
            }
          }

          onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

          TestScaffold(navController, bottomSheetScaffoldState)
        }
      }
    }
  }
}


@Composable
fun audioClientModel(): DemoAudioClientModel = LocalContext.current.audioClientModel()

fun Context.audioClientModel(): DemoAudioClientModel {
  val activity = this as ComponentActivity
  return activity.viewModels<DemoAudioClientModel> {
    AudioClientViewModel.Companion.AudioClientViewModelFactory(activity)
  }.value
}





