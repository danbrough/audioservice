package danbroid.audioservice.app.ui.menu

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.RemoveFromQueue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.google.accompanist.insets.statusBarsHeight
import danbroid.audio.library.AudioClientViewModel
import danbroid.audio.menu.Menu
import danbroid.audio.menu.MenuDSL
import danbroid.audio.ui.AppIcon
import danbroid.audioservice.app.Routes
import danbroid.audioservice.app.log
import danbroid.audioservice.app.ui.theme.DemoTheme

@Composable
fun IconImage(
  imageUrl: Any,
  contentDescription: String?,
  modifier: Modifier = Modifier,
) {

  AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
      .data(imageUrl)
      .crossfade(true)
      .build(),
    placeholder = painterResource(danbroid.audioservice.app.R.drawable.ic_launcher_foreground),
    contentDescription = stringResource(danbroid.audioservice.app.R.string.app_name),
    contentScale = ContentScale.FillBounds,
    modifier = modifier.clip(RoundedCornerShape(8.dp))
  )
/*  Image(
    painter = rememberImagePainter(imageUrl,null) {
      //crossfade(true)

      //TODO transformations(RoundedCornersTransformation(8.dp.value))
    },
    contentDescription = contentDescription,
    modifier = modifier,
    contentScale = ContentScale.FillBounds,
  )*/
}

@Composable
fun MenuListIcon(_icon: Any?, title: String = "") {
  val imageModifier =
    Modifier
      .size(52.dp)
      .padding(4.dp)
  // .padding(start = 8.dp, top = 0.dp, bottom = 0.dp)

  var icon = _icon

  if (icon is AppIcon)
    icon = AppIcon.lookup(icon)

  val iconTint = MaterialTheme.colors.primary

  icon?.also {
    when (it) {
      is Bitmap ->
        /*        DemoImage(
                    imageUrl = it,
                    title,
                    modifier = imageModifier
                )*/
        Image(
          it.asImageBitmap(),
          title,
          modifier = imageModifier.clip(RoundedCornerShape(8.dp)),
          contentScale = ContentScale.FillBounds
        )
      /*       Icon(
                 it.asImageBitmap(),
                 title,
                 modifier = imageModifier.clip(RoundedCornerShape(8.dp)),
                 tint = Color.Unspecified
             )*/
      is String ->
        IconImage(
          imageUrl = it,
          title,
          modifier = imageModifier
        )

      is ImageVector ->
        Icon(
          it,
          title,
          modifier = imageModifier,
          tint = iconTint
        )

      is Int ->
        Icon(
          painterResource(it),
          title,
          modifier = imageModifier,
          tint = iconTint,
        )

      else ->
        IconImage(
          imageUrl = it.toString(),
          title,
          modifier = imageModifier
        )
    }
  } ?: Icon(
    Icons.Filled.Audiotrack,
    contentDescription = null,
    tint = iconTint,
    modifier = imageModifier
  )
}

@Composable
private fun MenuListItemRow(
  modifier: Modifier,
  title: String,
  subTitle: String,
  _icon: Any?
) {
  Row(
    modifier = modifier
      .height(62.dp)
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {

    Spacer(Modifier.width(4.dp))
    MenuListIcon(_icon, title)
    /*  Image(
          Icons.Filled.Audiotrack, "",
          contentScale = ContentScale.Fit,
          //modifier = Modifier.size(42.dp),
          colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
      )*/
    Column(
      Modifier
        .padding(start = 16.dp)
        .fillMaxWidth(),
      verticalArrangement = Arrangement.Bottom
    ) {
      Text(title, style = MaterialTheme.typography.subtitle1)
      Text(
        subTitle,
        overflow = TextOverflow.Ellipsis, maxLines = 2,
        modifier = Modifier.alpha(ContentAlpha.medium),
        style = MaterialTheme.typography.body2
      )
    }
  }
}

@Composable
fun MenuListItemImpl(
  modifier: Modifier,
  title: String,
  subTitle: String,
  _icon: Any?,
  highLighted: Boolean = false,
) {
  if (highLighted) {
    Box {
      MenuListItemRow(modifier, title, subTitle, _icon)
      Box(
        Modifier
          .height(62.dp)
          .fillMaxWidth()
          .background(MaterialTheme.colors.secondaryVariant.copy(alpha = 0.2f))
      ) {

      }
    }
  } else
    MenuListItemRow(modifier, title, subTitle, _icon)
  Divider()
}

@Preview(showBackground = true)
@Composable
private fun MenuItemPreview() {
  DemoTheme {
    Column {

      ListItem(
        secondaryText = { Text("This is the secondary text") },
        icon = { MenuListIcon(AppIcon.SETTINGS, "Settings") }
      ) {
        Text("This is the primary text")
      }
      Divider()

      MenuListItemImpl(Modifier, "The title", "The Subtitle", Icons.Default.QueueMusic)
      Divider()
      MenuListItemImpl(
        Modifier,
        "The title2",
        "The Subtitle which is a lot longer and so will probably over flow the text display thingy still typing ",
        Icons.Default.RemoveFromQueue,
        highLighted = true
      )
      Divider()
    }
  }
}

@MenuDSL
inline fun LazyListScope.menu(
  id: String = "_${MenuContext.NEXT_ID++}",
  sticky: Boolean = false,
  highLighted: Boolean = false,
  clickable: Boolean = true,
  crossinline onCreate: @Composable Menu.() -> Unit
) {

  val menu = Menu(id, "Untitled")
  log.trace("menuID: ${menu.id}")

  val itemContent: @Composable LazyItemScope.() -> Unit = {
    menu.onCreate()
    val context = LocalMenuContext.current
    val itemModifier = if (clickable) Modifier.clickable(true, "Click label", onClick = {
      onClicked(menu, context.navHostController, context.audioClientModel)
    }) else Modifier

    if (sticky) {
      Card(elevation = 1.dp) {
        MenuListItemImpl(
          itemModifier,
          menu.title,
          menu.subTitle,
          menu.icon,
          highLighted = highLighted
        )
      }
    } else MenuListItemImpl(
      itemModifier,
      menu.title,
      menu.subTitle,
      menu.icon,
      highLighted = highLighted
    )
  }

  if (sticky)
    stickyHeader(menu.id, null, itemContent)
  else
    item(menu.id, null, itemContent)
}


fun onClicked(
  menu: Menu,
  navController: NavHostController,
  audioClientModel: AudioClientViewModel
) {
  log.debug("clicked: $menu")
  menu.onClicked?.also {
    it.invoke()
    return
  }

  navController.findDestination(menu.id)?.also {
    navController.navigate(menu.id)
    return
  }

  if (navController.graph.hasDeepLink(menu.id.toUri())) {
    navController.navigate(menu.id.toUri())
  } else if (menu.isBrowsable) {
    navController.navigate(Routes.menuRoute(menu.id)) //, menuNavOptions)
  } else if (menu.isPlayable) {
    audioClientModel.play(menu.id)
  }

  /*val menuNavOptions: NavOptionsBuilder.() -> Unit = {
    anim {
      enter = R.anim.menu_enter
      exit = R.anim.menu_exit
      popEnter = R.anim.menu_pop_enter
      popExit = R.anim.menu_pop_exit
    }
  }*/
}


@SuppressLint("ComposableNaming")
@MenuDSL
@Composable
fun menuScreen(block: LazyListScope.() -> Unit) {
  Column {
    Spacer(
      Modifier
        .fillMaxWidth()
        .statusBarsHeight()
        .background(MaterialTheme.colors.primary))
    LazyColumn {
      block()
    }
  }
}
