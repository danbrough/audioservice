package danbroid.audioservice.app.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import danbroid.audioservice.app.ui.components.DemoImage
import danbroid.audioservice.app.ui.theme.DemoTheme
import danbroid.demo.content.testTracks
import danbroid.media.client.AudioClientModel
import danbroid.media.client.AudioClientModelFactory

@ExperimentalMaterialApi
@Composable
fun Home(navBackStackEntry: NavBackStackEntry? = null) {
  log.info("Home()")
  val audioClientModel = viewModel<AudioClientModel>(factory = AudioClientModelFactory(LocalContext.current))
  log.ddebug("audioClient: ${audioClientModel.client}")
  Column {
    Text("Home Page")
    LazyColumn {
      items(testTracks.testData, { it.id }) { testTrack ->
        Row {


          ListItem(
              icon = {
                testTrack.imageURI?.also {
                  DemoImage(imageUrl = it, testTrack.title)
                } ?: Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colors.primary
                )
              },
              text = { Text(testTrack.title) },
              secondaryText = { Text(testTrack.subtitle) },
          )

        }
        Divider()

      }
    }
  }

}

@ExperimentalMaterialApi
@Preview
@Composable
fun HomePreview(){
  DemoTheme {
    Home()
  }
}

private val log = danbroid.logging.getLog("danbroid.audioservice.app.ui.home")
