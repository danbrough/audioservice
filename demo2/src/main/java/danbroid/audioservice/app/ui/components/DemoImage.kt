package danbroid.audioservice.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import danbroid.audioservice.app.R


@Composable
fun DemoImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 10.dp
) {

  Image(
      painter = rememberCoilPainter(
          request = imageUrl,
          previewPlaceholder = R.drawable.ic_launcher_foreground
      ),
      contentDescription = contentDescription,
      modifier = modifier.clip(RoundedCornerShape(8.dp)),
      contentScale = ContentScale.FillBounds,

  )
}

private val log = danbroid.logging.getLog("danbroid.audioservice.app.ui.components")
