package danbroid.audioservice.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import danbroid.audioservice.app.ui.theme.AudioDemoTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      ProvideWindowInsets {
        AudioDemoTheme(darkTheme = false) {
          val navController = rememberNavController()

          Spacer(Modifier.height(100.dp))
          Column {
            Text("Hello world!")
            Button({
              log.debug("pressed it!")
            }) {
              Text("Press me!", style = TextStyle.Default)
            }
          }

        }
      }
    }

  }
}


