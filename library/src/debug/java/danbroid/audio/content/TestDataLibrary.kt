package danbroid.audio.content

import androidx.core.net.toUri
import androidx.media2.common.MediaItem
import androidx.media2.common.UriMediaItem
import danbroid.audio.LibraryIcon
import danbroid.audio.library.AudioLibrary
import danbroid.audio.menu.Menu


//const val ipfs_gateway = "https://cloudflare-ipfs.com"
const val ipfs_gateway = "https://h1.danbrough.org"

val testTracks = testData {

  menu {
    id = "http://sohoradioculture.doughunt.co.uk:8000/320mp3"
    title = "Soho NYC"
    subTitle = "Soho radio from NYC"
    iconUrl =
      "${ipfs_gateway}/ipns/k51qzi5uqu5dkfj7jtuefs73phqahshpa4nl7whcjntlq8v1yqi06fv6zjy3d3/media/soho_nyc.png"
  }

  menu {
    id = "http://sohoradiomusic.doughunt.co.uk:8000/320mp3"
    title = "Soho UK"
    subTitle = "Soho radio from London"
    iconUrl =
      "${ipfs_gateway}/ipns/k51qzi5uqu5dkfj7jtuefs73phqahshpa4nl7whcjntlq8v1yqi06fv6zjy3d3/media/soho_uk.png"
  }

  menu {
    id = "http://colostreaming.com:8094"
    title = "Radio SHE"
    subTitle = "Some cheesy rock station from florida"
    iconUrl =
      "${ipfs_gateway}/ipns/k51qzi5uqu5dkfj7jtuefs73phqahshpa4nl7whcjntlq8v1yqi06fv6zjy3d3/media/RadioSHE.png"
  }

  menu {
    id = "http://curiosity.shoutca.st:9073/stream"
    title = "NZ Metal"
    subTitle = "Its heavy metal!!!"
    iconUrl = "https://h1.danbrough.org/nzrp/metalradio.png"
  }

  menu {
    title = "U80s"
    id = "http://ice4.somafm.com/u80s-256-mp3"
    subTitle = "Underground 80's SOMAFM"
    iconUrl = "https://api.somafm.com/img/u80s-120.png"
  }

  menu {
    title = "RNZ"
    id = "http://radionz-ice.streamguys.com/National_aac128"
    subTitle = "National Radio New Zealand"
    icon = LibraryIcon.SOMAFM
  }

  menu {
    title = "The Rock"
    id = "http://livestream.mediaworks.nz/radio_origin/rock_128kbps/playlist.m3u8"
    subTitle = "the rock"
    iconUrl = "${ipfs_gateway}/ipns/audienz.danbrough.org/media/the_rock.png"
  }

  menu {
    title = "NTS1"
    id = "https://stream-relay-geo.ntslive.net/stream"
    subTitle = "NTS Live 1"
    iconUrl = "${ipfs_gateway}/ipns/audienz.danbrough.org/media/nts.png"
  }


  menu {
    title = "NTS2"
    id = "https://stream-relay-geo.ntslive.net/stream2"
    subTitle = "NTS Live 2"
    iconUrl = "${ipfs_gateway}/ipns/audienz.danbrough.org/media/nts.png"
  }

  menu {
    title = "Opus Test"
    id = "https://h1.danbrough.org/audienz/demos/improv/improv1.opus"
    subTitle = "Improv1 - Dan Brough"
  }

  menu {
    title = "Flac Test"
    id = "https://h1.danbrough.org/audienz/demos/improv/improv2.flac"
    subTitle = "Improv2 - Dan Brough"
  }

  menu {
    title = "MP3 Test"
    id = "https://h1.danbrough.org/audienz/demos/improv/improv3.mp3"
    subTitle = "Improv3 - Dan Brough"
  }

  menu {
    title = "Ogg Test"
    id = "https://h1.danbrough.org/audienz/demos/improv/improv4.ogg"
    subTitle = "Improv4 - Dan Brough"
  }

  menu {
    title = "Local ogg file that wont play for you and has a really long title"
    subTitle = "Local ogg file that wont play for you and has a really long subtitle"
    id = "http://192.168.1.2/music/Electric%20Youth/Innerworld/02%20-%20Runaway.ogg"
  }

  menu {
    title = "Local opus"
    subTitle = "Local opus file that wont play for you"
    id = "http://192.168.1.2/music/Wilco/2011%20The%20Whole%20Love/11%20-%20Whole%20Love.opus"
    iconUrl = "https://h1.danbrough.org/ipns/kitty.danbrough.org/cat6.jpg"
  }

  menu {
    title = "Short OGA"
    id = "https://h1.danbrough.org/audienz/demos/test.oga"
    subTitle = "A short OGA file"
  }

  menu {
    title = "Another Short OGA"
    id = "https://h1.danbrough.org/audienz/demos/complete.oga"
    subTitle = "A short OGA file"
  }
}


class TestDataLibrary(private val tracks: List<Menu> = testTracks) : AudioLibrary {
  override suspend fun loadItem(mediaID: String): MediaItem? =
    tracks.firstOrNull {
      it.id == mediaID
    }?.let {
      UriMediaItem.Builder(mediaID.toUri())
        .setStartPosition(0L).setEndPosition(-1L)
        .setMetadata(it.toMediaMetadata().build()).build()
    }.also {
      log.info("Found mediaID: $mediaID ${it != null}")
    }
}

