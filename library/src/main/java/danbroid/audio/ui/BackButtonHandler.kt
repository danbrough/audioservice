package danbroid.audio.ui

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember

@Composable
fun BackButtonHandler(
    enabled: Boolean = true,
    onBackPressed: () -> Unit
) {
  val dispatcherOwner = LocalOnBackPressedDispatcherOwner.current ?: return
  val backCallback = remember {
    object : OnBackPressedCallback(enabled) {
      override fun handleOnBackPressed() {
        onBackPressed.invoke()
      }
    }
  }
  backCallback.isEnabled = enabled
  DisposableEffect(dispatcherOwner) {
    dispatcherOwner.onBackPressedDispatcher.addCallback(backCallback)
    onDispose {
      backCallback.remove()
    }
  }
}