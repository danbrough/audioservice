package danbroid.media.client

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.media2.common.MediaItem
import androidx.media2.common.MediaMetadata
import androidx.media2.common.SessionPlayer
import androidx.media2.common.SubtitleData
import androidx.media2.session.*
import com.google.common.util.concurrent.ListenableFuture
import danbroid.media.service.AudioService
import danbroid.media.service.buffState
import danbroid.media.service.playerState
import danbroid.media.service.toDebugString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class AudioClient(context: Context) {

  enum class PlayerState {
    IDLE, PAUSED, PLAYING, ERROR;
  }

  enum class BufferingState {
    UNKNOWN, BUFFERING_AND_PLAYABLE, BUFFERING_AND_STARVED, BUFFERING_COMPLETE;
  }

  data class PlayPosition(val currentPos: Float, val duration: Float) {
    companion object {
      val NO_POSITION = PlayPosition(-1f, -1f)
    }
  }

  data class QueueState(val hasPrevious: Boolean, val hasNext: Boolean, val playState: PlayerState, val size: Int)

  private val _playPosition = MutableStateFlow(PlayPosition.NO_POSITION)
  val playPosition: StateFlow<PlayPosition> = _playPosition

  private val _queueState = MutableStateFlow(QueueState(false, false, PlayerState.IDLE, 0))
  val queueState: StateFlow<QueueState> = _queueState

  private val _bufferingState = MutableStateFlow(BufferingState.UNKNOWN)
  val bufferingState: StateFlow<BufferingState> = _bufferingState

  private val _playState = MutableStateFlow(PlayerState.IDLE)
  val playState: StateFlow<PlayerState> = _playState

  private val _connected = MutableStateFlow(false)
  val connected: StateFlow<Boolean> = _connected

  private val _currentItem = MutableStateFlow<MediaItem?>(null)
  val currentItem: StateFlow<MediaItem?> = _currentItem

  private val _metadata = MutableStateFlow<MediaMetadata?>(null)
  val metadata: StateFlow<MediaMetadata?> = _metadata

  protected val controllerCallback = ControllerCallback()

  protected val mainExecutor = ContextCompat.getMainExecutor(context)//Executors.newSingleThreadExecutor()
  // protected val mainExecutor = java.util.concurrent.Executors.newSingleThreadExecutor()

  val mediaController: MediaBrowser = run {


    log.debug("starting service ..")
    context.startService(Intent(context, AudioService::class.java))


    val sessionManager = MediaSessionManager.getInstance(context)
    val serviceToken = sessionManager.sessionServiceTokens.first {
      it.serviceName == AudioService::class.qualifiedName
    }

    log.derror("serviceToken: $serviceToken.")


    MediaBrowser.Builder(context)
        .setControllerCallback(mainExecutor, controllerCallback)
        .setSessionToken(serviceToken)
        .build()
  }


  fun playUri(uri: String) {
    log.info("playUri() $uri")

    mediaController.playlist?.indexOfFirst {
      it.metadata?.mediaId == uri
    }?.also {
      if (it != -1) {
        if (it != mediaController.currentMediaItemIndex) {
          log.dtrace("skipping to existing item $it")

          mediaController.skipToPlaylistItem(it).then {
            if (it.resultCode == SessionResult.RESULT_SUCCESS) {
              log.trace("calling play")
              mediaController.play()
            } else {
              log.error("failed to skip to existing playlist item: ${it.customCommandResult}")
            }
          }
        } else {
          mediaController.play()
        }
        return
      }
    }

    log.dinfo("adding to playlist")


    mediaController.addPlaylistItem(Integer.MAX_VALUE, uri).then {
      log.ddebug("result: $it code: ${it.resultCode} item:${it.mediaItem}")
      if (mediaController.playerState != SessionPlayer.PLAYER_STATE_PLAYING) {
        mediaController.play()
      }
    }
  }

  var seeking = false

  fun seekTo(seconds: Float) {
    log.trace("seekTo() $seconds")
    seeking = true
    mediaController.seekTo((seconds * 1000L).toLong()).then {
      seeking = false
      updatePosition()
    }
  }

  fun togglePause() {
    log.debug("togglePause() state: ${mediaController.playerState.playerState}")
    if (mediaController.playerState == SessionPlayer.PLAYER_STATE_PLAYING) {
      mediaController.pause()
    } else {
      mediaController.play()
    }
  }

  fun skipToNext() {
    mediaController.skipToNextPlaylistItem()
  }

  fun skipToPrev() {
    mediaController.skipToPreviousPlaylistItem()
  }

  private fun <T> ListenableFuture<T>.then(job: (T) -> Unit) =
      addListener({
        job.invoke(get())
      }, mainExecutor)


  fun test(item: MediaMetadata) {
    log.dinfo("test()")

    val metadata = mediaController.playlistMetadata
    log.trace("metadata: ${metadata.toDebugString()}")
    val playlist = mediaController.playlist ?: mutableListOf()
    playlist.forEach {
      log.error("playlist item: $it")
    }

    mediaController.sendCustomCommand(SessionCommand("test", null), bundleOf("count" to 3)).then {
      log.debug("cmd sent")
    }
/*
    mediaController.setMediaUri(item.mediaId!!.toUri(), bundleOf().also {
      ParcelUtils.putVersionedParcelable(it, "item", item)
    }).then {
      log.debug("finished setting media uri")
    }
*/


    /*  mediaController.sendCustomCommand(SessionCommand("test", bundleOf("id" to "thang")), bundleOf("count" to 3)).then {
        log.debug("cmd sent")
      }*/
  }

  private val handler = Handler(Looper.getMainLooper())
  private val updatePositionJob: Runnable = Runnable {
    updatePosition()
  }


  private fun updatePosition() {
    val position = mediaController.currentPosition
    val duration = mediaController.duration
    log.dtrace("updatePosition():${hashCode()} $position:$duration seeking:$seeking")

    handler.removeCallbacks(updatePositionJob)
    if (seeking) return
    _playPosition.value = PlayPosition(position / 1000f, duration / 1000f)

    if (duration > 0 && mediaController.playerState == SessionPlayer.PLAYER_STATE_PLAYING)
      handler.postDelayed(updatePositionJob, 1000L)

  }

  protected inner class ControllerCallback : MediaBrowser.BrowserCallback() {

    override fun onPlaybackInfoChanged(
        controller: MediaController,
        info: MediaController.PlaybackInfo
    ) {
      log.info("onPlaybackInfoChanged(): $info")
    }

    override fun onPlaybackCompleted(controller: MediaController) {
      log.info("onPlaybackCompleted()")
    }

    override fun onPlaylistMetadataChanged(controller: MediaController, metadata: MediaMetadata?) {
      log.debug("onPlaylistMetadataChanged() metadata: ${metadata.toDebugString()}")
      _metadata.value = metadata
    }

    override fun onPlaylistChanged(
        controller: MediaController,
        list: MutableList<MediaItem>?,
        metadata: MediaMetadata?
    ) {
      val state = controller.playerState

      log.info("onPlaylistChanged() size:${list?.size ?: "null"} state:${state.playerState} prev:${controller.previousMediaItemIndex} next:${controller.nextMediaItemIndex} duration:${controller.currentMediaItem?.duration}")
      log.info("metadata: ${metadata.toDebugString()}")
      log.ddebug("duration: ${metadata?.getLong(MediaMetadata.METADATA_KEY_DURATION)}")
      _queueState.value = _queueState.value.copy(
          hasPrevious = controller.previousMediaItemIndex != -1,
          hasNext = controller.nextMediaItemIndex != -1,
          size = list?.size ?: 0
      )
    }

    override fun onTrackSelected(controller: MediaController, trackInfo: SessionPlayer.TrackInfo) {
      log.info("onTrackSelected() ${controller.currentMediaItem?.metadata}")
    }

    override fun onCurrentMediaItemChanged(controller: MediaController, item: MediaItem?) {
      log.info("onCurrentMediaItemChanged(): $item currentPos: ${controller.currentPosition} duration:${controller.duration} ")

      log.dtrace("keys: ${item?.metadata?.keySet()?.joinToString(",")}")
      log.dtrace("extra keys: ${item?.metadata?.extras?.keySet()?.joinToString(",")}")

      updatePosition()

      _currentItem.value = item
      _metadata.value = item?.metadata
      _queueState.value = _queueState.value.copy(
          hasPrevious = controller.previousMediaItemIndex != -1,
          hasNext = controller.nextMediaItemIndex != -1
      )
    }

    override fun onBufferingStateChanged(controller: MediaController, item: MediaItem, state: Int) {
      log.info("onBufferingStateChanged() ${state.buffState} duration: ${item.duration}")

      _bufferingState.value = when (state) {
        SessionPlayer.BUFFERING_STATE_UNKNOWN -> BufferingState.UNKNOWN
        SessionPlayer.BUFFERING_STATE_BUFFERING_AND_PLAYABLE -> BufferingState.BUFFERING_AND_PLAYABLE
        SessionPlayer.BUFFERING_STATE_BUFFERING_AND_STARVED -> BufferingState.BUFFERING_AND_STARVED
        SessionPlayer.BUFFERING_STATE_COMPLETE -> BufferingState.BUFFERING_COMPLETE
        else -> error("Unknown buffering state: $state")
      }
    }


    override fun onPlayerStateChanged(controller: MediaController, state: Int) {
      log.debug("onPlayerStateChanged() state:$state = ${state.playerState}  pos:${controller.currentPosition} duration:${controller.duration}")

      _playState.value = when (state) {
        SessionPlayer.PLAYER_STATE_IDLE -> PlayerState.IDLE
        SessionPlayer.PLAYER_STATE_PLAYING -> PlayerState.PLAYING
        SessionPlayer.PLAYER_STATE_ERROR -> PlayerState.ERROR
        SessionPlayer.PLAYER_STATE_PAUSED -> PlayerState.PAUSED
        else -> error("Unknown player state: $state")
      }


      updatePosition()
    }


    override fun onSubtitleData(
        controller: MediaController,
        item: MediaItem,
        track: SessionPlayer.TrackInfo,
        data: SubtitleData
    ) {
      log.info("onSubtitleData() $track data: $data")
    }

    override fun onTracksChanged(
        controller: MediaController,
        tracks: MutableList<SessionPlayer.TrackInfo>
    ) {
      val state = controller.playerState
      log.info("onTracksChanged() tracks:${tracks} state:${state.playerState} prev:${controller.previousMediaItemIndex} next:${controller.nextMediaItemIndex}")

    }

    override fun onConnected(controller: MediaController, allowedCommands: SessionCommandGroup) {
      log.info("onConnected()")
      _connected.value = true
      _currentItem.value = controller.currentMediaItem
      _metadata.value = controller.currentMediaItem?.metadata

    }

    override fun onDisconnected(controller: MediaController) {
      log.info("onDisconnected()")
      _connected.value = false
    }


  }

  fun close() {
    log.info("close()")
    handler.removeCallbacksAndMessages(null)
    mediaController.close()
  }

  fun clearPlaylist() {
    log.trace("clearPlaylist()")
    if (!mediaController.playlist.isNullOrEmpty()) {
      mediaController.removePlaylistItem(0).then {
        clearPlaylist()
      }
    }
  }

}


val MediaItem?.duration: Long
  get() = this?.metadata?.getLong(MediaMetadata.METADATA_KEY_DURATION) ?: 0L


private val log = danbroid.logging.getLog(AudioClient::class)