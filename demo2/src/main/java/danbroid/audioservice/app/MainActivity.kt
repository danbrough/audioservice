package danbroid.audioservice.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import danbroid.audioservice.app.ui.theme.DemoTheme
import danbroid.media.client.AudioClientModel
import danbroid.media.client.AudioClientModelFactory

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent {
      ProvideWindowInsets {
        DemoTheme(darkTheme = false) {
          val navController = rememberNavController()
          //val scaffoldState = rememberScaffoldState()
          TestScaffold(this@MainActivity.title.toString(), navController)
        }
      }
    }
  }
}

@Composable
fun audioClientModel() = viewModel<AudioClientModel>(factory = AudioClientModelFactory(LocalContext.current))



