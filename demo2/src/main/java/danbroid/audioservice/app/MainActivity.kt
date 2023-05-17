package danbroid.audioservice.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import danbroid.audio.content.TestDataLibrary
import danbroid.audio.content.rnz
import danbroid.audio.content.somaFM
import danbroid.audio.library.RootAudioLibrary
import danbroid.audio.library.audioClientModel
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
      DemoTheme(darkTheme = false) {
        //val scaffoldState = rememberScaffoldState()
        //activity.onBackPressedDispatcher.addCallback(activity, onBackPressedCallback)

        val audioClientViewModel = audioClientModel()
        MainScaffold(audioClientViewModel)
      }

    }
  }
}







