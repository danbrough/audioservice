package danbroid.audioservice.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import danbroid.audio.content.TestDataLibrary
import danbroid.audio.content.rnz
import danbroid.audio.content.somaFM
import danbroid.audio.library.RootAudioLibrary
import danbroid.audio.library.audioClientModel
import danbroid.audioservice.app.ui.theme.DemoTheme


class MainActivity : ComponentActivity() {


  override fun onCreate(savedInstanceState: Bundle?) {
    log.info("onCreate()")
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    RootAudioLibrary.register(
        TestDataLibrary(),
        somaFM,
        rnz
    )

    setContent {
      DemoTheme(darkTheme = false) {
        if (BuildConfig.DEBUG) log.error("MAIN ACTIVITY SET CONTENT")
        val navController = rememberNavController()
        //val scaffoldState = rememberScaffoldState()
        //activity.onBackPressedDispatcher.addCallback(activity, onBackPressedCallback)

        val audioClientViewModel = audioClientModel()
        if (BuildConfig.DEBUG) log.error("MAIN ACTIVITY SET CONTENT")
        MainScaffold(navController, audioClientViewModel)
      }

    }
  }
}







