package danbroid.audioservice.app.content

import androidx.activity.ComponentActivity
import danbroid.audioservice.app.R
import danbroid.audioservice.app.Routes
import danbroid.audioservice.app.menu.MenuBuilder
import danbroid.audioservice.app.menu.MenuBuilderContext
import danbroid.audioservice.app.rnz.rnz
import danbroid.audioservice.app.rnz.toMenuItem
import danbroid.audioservice.app.ui.AppIcon
import danbroid.demo.content.ipfs_gateway
import danbroid.demo.content.somaFM
import danbroid.demo.content.testTracks
import danbroid.media.client.audioClientModel
import danbroid.util.format.uriEncode
import kotlinx.coroutines.runBlocking

internal val log = danbroid.logging.getLog("danbroid.audioservice.app.content")

const val URI_PREFIX = "audiodemo:/"
const val URI_CONTENT = "$URI_PREFIX/content"

const val URI_SETTINGS = "$URI_PREFIX/settings"
const val URI_BROWSER = "$URI_PREFIX/browser"

suspend fun MenuBuilderContext.demoMenu(rootTitle: String): MenuBuilder = MenuBuilder(this).apply {
  id = URI_CONTENT
  title = rootTitle

  menu {
    title = "Test"
    onClicked = {
      val client = (context.context as ComponentActivity).audioClientModel().client
      runBlocking {
        context.context.somaFM.loadItem("somafm://poptron")
      }?.also {
        client.test(it.metadata!!)
      }

    }
    iconURI = AppIcon.RADIO
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

    context.context.somaFM.channels().forEach {
      menu {
        id = "somafm://${it.id.uriEncode()}"
        title = it.title
        subtitle = it.description
        iconURI = it.image
        isPlayable = true
      }
    }
  }


  context.context.rnz.loadProgramme(2582485L).toMenuItem().also {
    menu {
      id = it.id
      title = it.title
      subtitle = it.subTitle
      iconURI = it.iconURI
      isPlayable = it.isPlayable
    }
  }

  context.context.rnz.rnzNews().toMenuItem().also {
    menu {
      id = it.id
      title = it.title
      subtitle = it.subTitle
      iconURI = AppIcon.RNZ_NEWS
      isPlayable = it.isPlayable
    }
  }

  menu {
    id = URI_SETTINGS
    iconURI = AppIcon.SETTINGS
    title = context.getString(R.string.settings)
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