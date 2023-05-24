package danbroid.audio.library

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import com.google.common.util.concurrent.ListenableFuture
import danbroid.audio.client.AudioClient
import danbroid.audio.log

@UnstableApi
open class AudioClientViewModel(context: Context) : ViewModel() {

  private val _client = lazy {
    //log.info("starting audio service ..")
    //context.startService(Intent(context, AudioService::class.java))
    AudioClient(context, viewModelScope)
  }

  val client: AudioClient by _client

  override fun onCleared() {
    super.onCleared()
    log.info("onCleared()")
    if (_client.isInitialized()) {
      log.debug("closing client..")
      _client.value.close()
    }
  }

  fun play(mediaID: String) {
    log.info("play(): $mediaID")
    val controller = client.mediaController
    controller.addMediaItem(MediaItem.Builder().setMediaId("").build())
    /*    controller.currentMediaItemIndex

        controller.playlistMetad
        val n = controller.medi
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
          } else {
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
        }*/
  }

  private val mainExecutor = ContextCompat.getMainExecutor(context)

  protected fun <T> ListenableFuture<T>.then(job: (T) -> Unit) =
    addListener({
      job.invoke(get())
    }, mainExecutor)

  companion object {
    class AudioClientViewModelFactory(val context: Context) :
      ViewModelProvider.NewInstanceFactory() {
      override fun <T : ViewModel> create(modelClass: Class<T>): T =
        modelClass.getDeclaredConstructor(Context::class.java).newInstance(context) as T
    }
  }
}

@Composable
fun audioClientModel(): AudioClientViewModel = LocalContext.current.audioClientModel()

fun Context.audioClientModel(): AudioClientViewModel {
  val activity = this as ComponentActivity
  return activity.viewModels<AudioClientViewModel> {
    AudioClientViewModel.Companion.AudioClientViewModelFactory(activity)
  }.value
}


