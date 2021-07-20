package danbroid.audioservice.app.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import danbroid.audioservice.app.menu.MenuItem
import danbroid.audioservice.app.ui.components.DemoImage
import danbroid.audioservice.app.ui.theme.DemoTheme


@Composable
fun MenuScreen(
    title: String,
    menus: List<MenuItem>,
    menuItemClicked: (MenuItem) -> Unit = {},
) {
  log.dtrace("MenuScreen()")


/*  val audioClientModel = viewModel<AudioClientModel>(factory = AudioClientModelFactory(LocalContext.current))
  log.ddebug("audioClient: ${audioClientModel.client}")*/
  Column {
    val imageModifier = Modifier.size(52.dp)
    LazyColumn {
      items(menus, { it.id }) { menu ->

        MenuListItem(menu, { menuItemClicked.invoke(menu) })
        /* ListItem(
             icon = {
               menu.imageURI?.also {
                 DemoImage(
                     imageUrl = it,
                     menu.title,
                     modifier = imageModifier.background(Color.Green),
                 )
               } ?: Icon(
                   Icons.Filled.Audiotrack,
                   contentDescription = null,
                   modifier = imageModifier,
                   tint = MaterialTheme.colors.primary
               )
             },
             text = { Text(menu.title) },
             secondaryText = { Text(menu.subTitle, overflow = TextOverflow.Ellipsis, maxLines = 2) },
             modifier = Modifier.clickable {
               menuItemClicked.invoke(menu)
             }
         )*/
      }
    }
  }
}


@Composable
fun MenuListItem(menuItem: MenuItem, onClicked: () -> Unit) {
  Row(modifier = Modifier.height(62.dp).fillMaxWidth().clickable { onClicked() }, verticalAlignment = Alignment.CenterVertically) {
    //Spacer(Modifier.width(4.dp))

    val imageModifier = Modifier.size(52.dp).padding(start = 4.dp)
    menuItem.icon?.also {
      when (it) {
        is String -> DemoImage(
            imageUrl = it,
            menuItem.title,
            modifier = imageModifier
        )
        is ImageVector ->
          Icon(
              it,
              menuItem.title,
              modifier = imageModifier,
              tint = MaterialTheme.colors.primary,
          )
        is Int ->
          Icon(
              painterResource(it),
              menuItem.title,
              modifier = imageModifier,
              tint = MaterialTheme.colors.primary,
          )
        else -> error("Unknown icon type: $it")
      }
    } ?: Icon(
        Icons.Filled.Audiotrack,
        contentDescription = null,
        tint = MaterialTheme.colors.primary,
        modifier = Modifier.size(52.dp).padding(start = 4.dp)
    )
    /*  Image(
          Icons.Filled.Audiotrack, "",
          contentScale = ContentScale.Fit,
          //modifier = Modifier.size(42.dp),
          colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
      )*/
    Column(Modifier.padding(start = 16.dp).fillMaxWidth(), verticalArrangement = Arrangement.Bottom) {
      Text(menuItem.title, style = MaterialTheme.typography.subtitle1)
      Text(
          menuItem.subTitle,
          overflow = TextOverflow.Ellipsis, maxLines = 2,
          modifier = Modifier.alpha(ContentAlpha.medium),
          style = MaterialTheme.typography.body2
      )
    }
  }
  Divider()
}

@Preview
@Composable
private fun ItemTest() {
  DemoTheme {
    LazyColumn {
      item("1") {
        Row(modifier = Modifier.height(62.dp), verticalAlignment = Alignment.CenterVertically) {
          //Spacer(Modifier.width(4.dp))
          Icon(
              Icons.Filled.Audiotrack,
              contentDescription = null,
              tint = MaterialTheme.colors.primary,
              modifier = Modifier.size(52.dp).padding(start = 4.dp)
          )
          /*  Image(
                Icons.Filled.Audiotrack, "",
                contentScale = ContentScale.Fit,
                //modifier = Modifier.size(42.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
            )*/
          Column(Modifier.padding(start = 16.dp), verticalArrangement = Arrangement.Bottom) {
            Text("Item 1", style = MaterialTheme.typography.subtitle1)
            Text("Subtitle 1", overflow = TextOverflow.Ellipsis, maxLines = 2,
                modifier = Modifier.alpha(0.6f),
                style = MaterialTheme.typography.body2)
          }
        }
        Divider()        /*       ListItem(
                   icon = {
                     *//*  Icon(
                    Icons.Filled.Audiotrack,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )*//*
              Image(
                  Icons.Filled.Audiotrack, "",
                  contentScale = ContentScale.Fit,
                  //modifier = Modifier.size(42.dp),
                  colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
              )
            },
            text = { Text("Item 1") },
            secondaryText = { Text("Subtitle 1", overflow = TextOverflow.Ellipsis, maxLines = 2) },
            modifier = Modifier.height(72.dp)
        )*/
      }
      item {
        Divider()
      }
      item("2") {
        ListItem(
            icon = {
              Icon(
                  Icons.Filled.Dashboard,
                  contentDescription = null,
                  tint = MaterialTheme.colors.primary,
              )
            },
            text = { Text("Item 2") },
            secondaryText = { Text("Subtitle 2", overflow = TextOverflow.Ellipsis, maxLines = 2) },
        )
      }
    }
  }
}


private val log = danbroid.logging.getLog("danbroid.audioservice.app.ui.menu")
