package  danbroid.audioservice.app.ui

import android.content.res.Configuration
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import danbroid.audioservice.app.ui.theme.BrewAppTheme


@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
fun Test() {
  BrewAppTheme {
    Text("HEllo world!", style = MaterialTheme.typography.body1)
  }
}