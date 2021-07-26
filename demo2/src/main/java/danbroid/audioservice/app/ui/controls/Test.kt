package danbroid.audioservice.app.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
@Preview(showBackground = true)
fun Test() {

  ConstraintLayout(Modifier.background(Color.DarkGray).width(300.dp).height(100.dp)) {

    val (button, text) = createRefs()

    val farEnd = this.createGuidelineFromAbsoluteRight(0F)

    Text("B1", color = Color.White,
        style = MaterialTheme.typography.h4,
        modifier = Modifier.background(Color.Blue).constrainAs(button) {
          start.linkTo(parent.start, margin = 2.dp)
          top.linkTo(parent.top)
          bottom.linkTo(parent.bottom)
        }
    )

    Text("SubTitle goes here and its long so while require ellipsis",
        color = Color.Yellow,
        style = MaterialTheme.typography.h5,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.constrainAs(text) {
          top.linkTo(parent.top)
          start.linkTo(button.end)
          end.linkTo(parent.end)
          width = Dimension.fillToConstraints
        }
    )

  }
}