package danbroid.audioservice.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import danbroid.audio.library.RootAudioLibrary
import danbroid.audioservice.app.content.TestDataLibrary
import danbroid.audioservice.app.rnz.rnz
import danbroid.audioservice.app.ui.theme.DemoTheme
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







