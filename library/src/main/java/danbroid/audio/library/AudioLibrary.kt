package danbroid.audio.library

import androidx.media3.common.MediaItem

interface AudioLibrary {
  suspend fun loadItem(mediaID: String): MediaItem?
}


object RootAudioLibrary : AudioLibrary {

  private val libraries = mutableListOf<AudioLibrary>()

  fun register(vararg libs: AudioLibrary) {
    libraries.addAll(libs)
  }

  override suspend fun loadItem(mediaID: String): MediaItem? =
    libraries.firstNotNullOfOrNull {
      it.loadItem(mediaID)
    }


}