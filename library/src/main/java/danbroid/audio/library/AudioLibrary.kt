package danbroid.audio.library

import androidx.media2.common.MediaItem

interface AudioLibrary {
  suspend fun loadItem(mediaID: String): MediaItem?
}

private val log = danbroid.logging.getLog(AudioLibrary::class)


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