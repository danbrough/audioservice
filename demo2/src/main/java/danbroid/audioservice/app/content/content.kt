package danbroid.audioservice.app.content

import danbroid.audioservice.app.R
import danbroid.audioservice.app.Routes
import danbroid.audioservice.app.menu.MenuBuilder
import danbroid.audioservice.app.menu.MenuBuilderContext
import danbroid.demo.content.ipfs_gateway
import danbroid.demo.content.somaFM
import danbroid.demo.content.testTracks
import danbroid.util.format.uriEncode

internal val log = danbroid.logging.getLog("danbroid.audioservice.app.content")

const val URI_PREFIX = "audiodemo:/"
const val URI_CONTENT = "$URI_PREFIX/content"

const val URI_SETTINGS = "$URI_PREFIX/settings"

suspend fun MenuBuilderContext.demoMenu(rootTitle: String): MenuBuilder = MenuBuilder(this).apply {
  id = URI_CONTENT
  title = rootTitle

  menu {
    title = context.getString(R.string.test)
    subtitle = "Menu with Children"

    menu {
      title = "Child 1"
      subtitle = "First Child"
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
      title = "Navigate by ROUTE.SETTINGS to Settings"
    }
  }

  menu {
    id = "$URI_CONTENT/soma"
    title = "Soma FM"
    subtitle = "Over 30 unique channels of listener-supported, commercial-free, underground/alternative radio broadcasting to the world"
    iconURI = "$ipfs_gateway/ipns/audienz.danbrough.org/media/somafm.png"
    log.dwarn("SOMA MENU..")

    context.context.somaFM.channels().forEach {
      menu {
        id = "somafm://${it.id.uriEncode()}"        //id = "$URI_CONTENT/soma/${it.id.uriEncode()}"
        title = it.title
        subtitle = it.description
        iconURI = it.image
        isPlayable = true
      }
    }
  }

  menu {
    id = URI_SETTINGS
    title = context.getString(R.string.settings)
    subtitle = "Subtitle 2"
  }

  testTracks.testData.forEach { audioTrack ->
    menu {
      id = audioTrack.id
      title = audioTrack.title
      subtitle = audioTrack.subtitle
      isPlayable = true
      iconURI = audioTrack.imageURI
    }
  }
}