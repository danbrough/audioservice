package danbroid.audio.ui

import android.app.Activity
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import danbroid.audio.library.R
import kotlinx.coroutines.launch

@Composable
fun QuitOnBackHandler(snackbarHostState: SnackbarHostState) {
  var lastBackPressed by remember { mutableStateOf(0L) }
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val msg = stringResource(R.string.msg_press_back_to_quit)

  BackButtonHandler {
    log.dtrace("BackBUttonHandler()")
    val now = System.currentTimeMillis()
    if (now - lastBackPressed < 3000L) {
      (context as Activity).finish()
    } else {
      lastBackPressed = now
      log.dtrace("showing snack bar")
      scope.launch {
        snackbarHostState.showSnackbar(
            msg,
            duration = SnackbarDuration.Short
        )
      }
    }
  }
}
