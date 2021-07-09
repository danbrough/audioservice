package danbroid.audioservice.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import danbroid.audioservice.app.ui.theme.AudioDemoTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      ProvideWindowInsets {
        AudioDemoTheme {
          Text("Hello world!")
        }
      }
    }

  }
}


