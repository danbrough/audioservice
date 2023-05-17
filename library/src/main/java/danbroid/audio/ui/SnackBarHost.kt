package danbroid.audio.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SnackBarHost(state: SnackbarHostState, modifier: Modifier) {
  // reuse default SnackbarHost to have default animation and timing handling

  SnackbarHost(state, modifier = modifier) { data ->

    // custom snackbar with the custom border
    Snackbar(
      modifier = Modifier.padding(8.dp),
      //backgroundColor = MaterialTheme.colors.primaryVariant,
      content = {
        Text(
          text = data.message,
          style = MaterialTheme.typography.body2
        )
      },
      action = {
        data.actionLabel?.let {
          TextButton(onClick = {
            log.info("ok")
            state.currentSnackbarData?.performAction()
          }) {

            Text(
              text = state.currentSnackbarData?.actionLabel
                ?: "",
              color = MaterialTheme.colors.secondary
            )
          }

        }


      }

      //modifier = Modifier.border(2.dp, MaterialTheme.colors.secondary),
      //snackbarData = data
    )
  }
}

val log = danbroid.logging.getLog("danbroid.audio.ui")
