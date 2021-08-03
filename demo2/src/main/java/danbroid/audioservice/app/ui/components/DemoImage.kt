package danbroid.audioservice.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter


@Composable
fun DemoImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {

  Image(
      painter = rememberImagePainter(imageUrl),
      contentDescription = contentDescription,
      modifier = modifier,
      contentScale = ContentScale.FillBounds,
  )
}

private val log = danbroid.logging.getLog("danbroid.audioservice.app.ui.components")

