package danbroid.audioservice.app.ui.settings

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import danbroid.audioservice.app.ui.controls.BottomControls
import danbroid.audioservice.app.ui.theme.DemoTheme

@Composable
fun SettingsScreen() {
  Text("Settings", style = MaterialTheme.typography.subtitle1)
  BottomControls()

}

@Preview
@Composable
private fun SettingsSceenPreview() {
  DemoTheme {
    SettingsScreen()
  }
}

private val log = danbroid.logging.getLog("danbroid.audioservice.app.ui.settings")
