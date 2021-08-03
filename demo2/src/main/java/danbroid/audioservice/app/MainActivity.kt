package danbroid.audioservice.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import danbroid.audio.content.somaFM
import danbroid.audio.library.RootAudioLibrary
import danbroid.audioservice.app.content.TestDataLibrary
import danbroid.audioservice.app.rnz.rnz
import danbroid.audioservice.app.ui.theme.DemoTheme

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

          val audioClientViewModel = danbroid.audio.library.audioClientModel()
          MainScaffold(navController, audioClientViewModel)
        }
      }
    }
  }
}







