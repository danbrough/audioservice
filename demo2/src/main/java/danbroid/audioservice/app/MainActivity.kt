package danbroid.audioservice.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import danbroid.audio.library.AudioClientViewModel
import danbroid.audio.library.RootAudioLibrary
import danbroid.audioservice.app.rnz.rnz
import danbroid.audioservice.app.ui.theme.DemoTheme
import danbroid.demo.content.TestDataLibrary
import danbroid.demo.content.somaFM

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)

    RootAudioLibrary.register(
        TestDataLibrary(),
        somaFM,
        rnz
    )


    setContent {
      ProvideWindowInsets {
        DemoTheme(darkTheme = false) {
          log.derror("MAIN ACTIVITY SET CONTENT")
          val navController = rememberNavController()
          //val scaffoldState = rememberScaffoldState()
          //activity.onBackPressedDispatcher.addCallback(activity, onBackPressedCallback)
          val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

          MainScaffold(navController, bottomSheetScaffoldState)
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





