package danbroid.audioservice.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import danbroid.audioservice.app.ui.theme.DemoTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      ProvideWindowInsets {
        DemoTheme(darkTheme = false) {
          val navController = rememberNavController()
          val scaffoldState = rememberScaffoldState()
          MainScaffold(Modifier, this@MainActivity.title.toString(), navController, scaffoldState)
        }
      }
    }
  }
}



