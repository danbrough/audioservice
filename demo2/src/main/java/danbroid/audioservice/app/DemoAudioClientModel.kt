package danbroid.audioservice.app

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.media2.common.SessionPlayer
import danbroid.audio.library.AudioClientViewModel
import danbroid.audio.library.RootAudioLibrary
import danbroid.media.service.playerState
import danbroid.media.service.successfull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DemoAudioClientModel(context: Context) : AudioClientViewModel(context) {


  fun play(mediaID: String) {
    log.info("test(): $mediaID")
    val controller = client.mediaController
    val existingIndex = controller.playlist?.indexOfFirst {
      it.metadata?.mediaId == mediaID
    } ?: -1

    if (existingIndex != -1) {
      log.warn("already in playlist at $existingIndex")
      if (existingIndex != controller.currentMediaItemIndex) {
        controller.skipToPlaylistItem(existingIndex).then {
          log.debug("skipTO $existingIndex successfull: ${it.successfull}")
          client.playIfNotPlaying()
        }
      } else  {
        client.playIfNotPlaying()
      }
      return
    }

    viewModelScope.launch(Dispatchers.IO) {
      val mediaItem = RootAudioLibrary.loadItem(mediaID) ?: run {
        log.error("Failed to find $mediaID in library")
        return@launch
      }
      withContext(Dispatchers.Main) {
        client.addToPlaylist(mediaItem).then {
          log.info("addToPlaylist returned ${it.successfull}  playlistIndex: ${controller.currentMediaItemIndex} playlistSize: ${controller.playlist?.size} playerState:${controller.playerState.playerState}")
          if (controller.playerState == SessionPlayer.PLAYER_STATE_IDLE || controller.playerState == SessionPlayer.PLAYER_STATE_ERROR) {
            log.debug("calling prepare ..")
            controller.prepare().then {
              log.debug("prepare(): success: ${it.successfull} calling play ..")
              controller.play().then {
                log.debug("play() success:${it.successfull}")
              }
            }
          }
        }
      }
    }
  }
}



