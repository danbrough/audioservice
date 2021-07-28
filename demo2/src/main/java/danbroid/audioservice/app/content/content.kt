package danbroid.audioservice.app.content

import danbroid.audioservice.app.R
import danbroid.audioservice.app.Routes
import danbroid.audioservice.app.audioClientModel
import danbroid.audioservice.app.menu.MenuItem
import danbroid.audioservice.app.menu.menu
import danbroid.audioservice.app.rnz.RNZLibrary
import danbroid.audioservice.app.rnz.rnz
import danbroid.audioservice.app.ui.AppIcon
import danbroid.audioservice.app.ui.menu.MenuModel
import danbroid.demo.content.ipfs_gateway
import danbroid.demo.content.somaFM
import danbroid.demo.content.testTracks
import danbroid.util.format.uriEncode
import java.util.*

internal val log = danbroid.logging.getLog("danbroid.audioservice.app.content")

const val URI_PREFIX = "audiodemo:/"
const val URI_CONTENT = "$URI_PREFIX/content"

const val URI_SETTINGS = "$URI_PREFIX/settings"
const val URI_BROWSER = "$URI_PREFIX/browser"
const val URI_PLAYLIST = "$URI_PREFIX/playlist"


suspend fun demoMenu(builder: MenuModel.DemoMenuBuilder, rootTitle: String): MenuModel.DemoMenuBuilder = builder.apply {
  id = URI_CONTENT
  title = rootTitle

  menu {
    id = "somafm://poptron"
    title = "PopTron"
    isPlayable = true
    iconURI = AppIcon.RADIO
  }

  menu {
    id = RNZLibrary.getProgrammeURI(context.rnz.rnzNewsProgrammeID())
    title = "RNZ News"
    subtitle = "Latest RNZ News Bulletin"
    isPlayable = true
    iconURI = AppIcon.RNZ_NEWS
  }

  menu {
    id = URI_PLAYLIST
    title = context.getString(R.string.playlist)
    subtitle = context.audioClientModel().client.queueState.value.size.let { size ->
      if (size > 0)
        "Size: $size"
      else
        "Empty"
    }
    iconURI = AppIcon.PLAYLIST
  }

  menu {
    title = context.getString(R.string.test)
    subtitle = "Menu with Children"
    iconURI = AppIcon.SAVINGS

    menu {
      title = "Child 1"
      subtitle = "First Child"
      iconURI = AppIcon.PANORAMA
    }

    menu {
      id = URI_PLAYLIST

      title = context.getString(R.string.playlist)
      subtitle = context.audioClientModel().client.queueState.value.size.let { size ->
        if (size > 0)
          "Size: $size"
        else
          "Empty"
      }
      iconURI = AppIcon.PLAYLIST
    }

    menu {
      title = "Child 12"
      subtitle = "Click for more"

      menu {
        title = "Child 21"
        subtitle = "Click for more"

        menu {
          title = "Child 211"
          subtitle = "Click for more"
          menu {
            title = "Child 211"
            subtitle = "Click for more"
          }
        }
      }

      menu {
        title = "Child 22"
        subtitle = "Second Child"
      }

      menu {
        id = Routes.SETTINGS
        title = "Navigate by ROUTE.SETTINGS to Settings"
        subtitle = "Click to update this subtitle"
        onClicked = {

          update(it.copy(subTitle = "The date is ${Date()}"))
        }
      }
    }

    menu {
      id = Routes.SETTINGS
      iconURI = AppIcon.SETTINGS
      title = "Navigate by ROUTE.SETTINGS to Settings"
    }
  }

  menu {
    id = URI_BROWSER
    iconURI = AppIcon.BROWSER
    title = "Web Browser"
  }

  menu {
    id = "$URI_CONTENT/soma"
    title = "Soma FM"
    subtitle = "Over 30 unique channels of listener-supported, commercial-free, underground/alternative radio broadcasting to the world"
    iconURI = "$ipfs_gateway/ipns/audienz.danbrough.org/media/somafm.png"
    isBrowsable = true
    buildChildren = {
      context.somaFM.channels().map {
        MenuItem(
            id = "somafm://${it.id.uriEncode()}",
            title = it.title,
            subTitle = it.description,
            iconURI = it.image,
            isPlayable = true)

      }
    }

  }


/*  context.rnz.loadProgramme(2582485L).toMenuItem().also {
    menu {
      id = it.id
      title = it.title
      subtitle = it.subTitle
      iconURI = it.iconURI
      isPlayable = it.isPlayable
    }
  }

  context.rnz.rnzNews().toMenuItem().also {
    menu {
      id = it.id
      title = it.title
      subtitle = it.subTitle
      iconURI = AppIcon.RNZ_NEWS
      isPlayable = it.isPlayable
    }
  }*/

  menu {
    id = URI_SETTINGS
    iconURI = AppIcon.SETTINGS
    title = "Settings with a really long title here to test with."
    subtitle = "Subtitle 2"
  }

  testTracks.testData.forEach { audioTrack ->
    menu {
      id = audioTrack.id
      title = audioTrack.title
      subtitle = audioTrack.subTitle
      isPlayable = true
      iconURI = audioTrack.iconURI
    }
  }


}
