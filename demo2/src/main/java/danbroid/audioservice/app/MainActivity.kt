package danbroid.audioservice.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import danbroid.audioservice.app.ui.theme.BrewAppTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      ProvideWindowInsets {
        BrewAppTheme (darkTheme = false) {
          val navController = rememberNavController()

          Column {
            Spacer(Modifier.height(100.dp))

            Text(
                "Hello world!", style = MaterialTheme.typography.body1,
            )
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

@Preview
@Composable
fun Preview1() {
  BrewAppTheme {
    Column {
      Text(
          "Hello world!", style = MaterialTheme.typography.body1,
          color = MaterialTheme.colors.primaryVariant,
      )
      Button({
        log.debug("pressed it!")
      }) {
        Text("Press me!", style = TextStyle.Default)
      }
    }
  }
}


