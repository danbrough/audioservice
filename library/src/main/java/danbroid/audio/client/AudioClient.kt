package danbroid.audio.client

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
import androidx.versionedparcelable.ParcelUtils
import com.google.common.util.concurrent.ListenableFuture
import danbroid.audio.service.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


open class AudioClient(context: Context) {

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

  data class QueueState(
      val hasPrevious: Boolean,
      val hasNext: Boolean,
      val size: Int,
      val position: Int
  )

  private val _playPosition = MutableStateFlow(PlayPosition.NO_POSITION)
  val playPosition: StateFlow<PlayPosition> = _playPosition

  private val _queueState = MutableStateFlow(QueueState(false, false, 0, -1))
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

  private val _playList = MutableStateFlow<List<MediaItem>>(emptyList())
  val playlist: StateFlow<List<MediaItem>> = _playList


  protected val controllerCallback = ControllerCallback()

  protected val mainExecutor =
      ContextCompat.getMainExecutor(context)//Executors.newSingleThreadExecutor()
  // protected val mainExecutor = java.util.concurrent.Executors.newSingleThreadExecutor()

  val mediaController: MediaBrowser = run {


    log.info("starting service ..")
    context.startService(Intent(context, AudioService::class.java))


    val sessionManager = MediaSessionManager.getInstance(context)
    val serviceToken = sessionManager.sessionServiceTokens.first {
      it.serviceName == AudioService::class.qualifiedName
    }

    log.dtrace("serviceToken: $serviceToken.")


    MediaBrowser.Builder(context)
        .setControllerCallback(mainExecutor, controllerCallback)
        .setSessionToken(serviceToken)
        .build()
  }

  val playlistIndex: Int = mediaController.currentMediaItemIndex
  var seeking = false

  fun seekTo(seconds: Float) {
    log.trace("seekTo() $seconds")
    seeking = true
    mediaController.seekTo((seconds * 1000L).toLong()).then {
      seeking = false
      updatePosition()
    }
  }

  fun play() = mediaController.play()

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

  private fun <T> ListenableFuture<T>.then(job: (T) -> Unit) =
      addListener({
        job.invoke(get())
      }, mainExecutor)


  fun addToPlaylist(item: MediaItem): ListenableFuture<SessionResult> {
    log.debug("addToPlaylist(): $item")
    val args = bundleOf()
    ParcelUtils.putVersionedParcelable(args, AudioService.ACTION_ARG_MEDIA_ITEM, item.metadata)
    return mediaController.sendCustomCommand(
        SessionCommand(
            AudioService.ACTION_ADD_TO_PLAYLIST,
            null
        ), args
    )
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

  fun playIfNotPlaying() {
    log.trace("playIfNotPlaying()")
    if (mediaController.playerState != SessionPlayer.PLAYER_STATE_PLAYING)
      mediaController.play()
  }

  protected inner class ControllerCallback : MediaBrowser.BrowserCallback() {

    override fun onPlaybackInfoChanged(
        controller: MediaController,
        info: MediaController.PlaybackInfo
    ) {
      log.trace("onPlaybackInfoChanged(): $info")
    }

    override fun onPlaybackCompleted(controller: MediaController) {
      log.trace("onPlaybackCompleted()")
    }

    override fun onPlaylistMetadataChanged(controller: MediaController, metadata: MediaMetadata?) {
      log.trace("onPlaylistMetadataChanged() metadata: ${metadata.toDebugString()}")
      _metadata.value = metadata
    }

    override fun onPlaylistChanged(
        controller: MediaController,
        list: MutableList<MediaItem>?,
        metadata: MediaMetadata?
    ) {
      val state = controller.playerState

      log.trace("onPlaylistChanged() size:${list?.size ?: "null"} state:${state.playerState} prev:${controller.previousMediaItemIndex} next:${controller.nextMediaItemIndex} duration:${controller.currentMediaItem?.duration}")
      log.dtrace("metadata: ${metadata.toDebugString()}")
      log.dtrace("duration: ${metadata?.getLong(MediaMetadata.METADATA_KEY_DURATION)}")
      _queueState.value = _queueState.value.copy(
          hasPrevious = controller.previousMediaItemIndex != -1,
          hasNext = controller.nextMediaItemIndex != -1,
          size = list?.size ?: 0,
          position = controller.currentMediaItemIndex
      )
      _playList.value = list ?: emptyList()
    }

    override fun onTrackSelected(controller: MediaController, trackInfo: SessionPlayer.TrackInfo) {
      log.trace("onTrackSelected() ${controller.currentMediaItem?.metadata}")
    }

    override fun onCurrentMediaItemChanged(controller: MediaController, item: MediaItem?) {
      log.trace("onCurrentMediaItemChanged(): $item currentPos: ${controller.currentPosition} duration:${controller.duration} ")

      log.dtrace("keys: ${item?.metadata?.keySet()?.joinToString(",")}")
      log.dtrace("extra keys: ${item?.metadata?.extras?.keySet()?.joinToString(",")}")

      updatePosition()

      _currentItem.value = item
      _metadata.value = item?.metadata
      _queueState.value = _queueState.value.copy(
          hasPrevious = controller.previousMediaItemIndex != -1,
          hasNext = controller.nextMediaItemIndex != -1,
          position = controller.currentMediaItemIndex
      )
    }

    override fun onBufferingStateChanged(controller: MediaController, item: MediaItem, state: Int) {
      log.trace("onBufferingStateChanged() ${state.buffState} duration: ${item.duration}")

      _bufferingState.value = when (state) {
        SessionPlayer.BUFFERING_STATE_UNKNOWN -> BufferingState.UNKNOWN
        SessionPlayer.BUFFERING_STATE_BUFFERING_AND_PLAYABLE -> BufferingState.BUFFERING_AND_PLAYABLE
        SessionPlayer.BUFFERING_STATE_BUFFERING_AND_STARVED -> BufferingState.BUFFERING_AND_STARVED
        SessionPlayer.BUFFERING_STATE_COMPLETE -> BufferingState.BUFFERING_COMPLETE
        else -> error("Unknown buffering state: $state")
      }
    }


    override fun onPlayerStateChanged(controller: MediaController, state: Int) {
      log.trace("onPlayerStateChanged() state:$state = ${state.playerState}  pos:${controller.currentPosition} duration:${controller.duration}")

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
      log.trace("onSubtitleData() $track data: $data")
    }

    override fun onTracksChanged(
        controller: MediaController,
        tracks: MutableList<SessionPlayer.TrackInfo>
    ) {
      val state = controller.playerState
      log.trace("onTracksChanged() tracks:${tracks} state:${state.playerState} prev:${controller.previousMediaItemIndex} next:${controller.nextMediaItemIndex}")
    }

    override fun onConnected(controller: MediaController, allowedCommands: SessionCommandGroup) {
      log.info("onConnected()")
      _connected.value = true
      _currentItem.value = controller.currentMediaItem
      _metadata.value = controller.currentMediaItem?.metadata
      val playlist = controller.playlist ?: emptyList()
      _playList.value = playlist
      _queueState.value = QueueState(
          hasPrevious = controller.previousMediaItemIndex != -1,
          hasNext = controller.nextMediaItemIndex != -1,
          size = playlist.size,
          position = controller.currentMediaItemIndex
      )

      _playState.value = when (controller.playerState) {
        SessionPlayer.PLAYER_STATE_IDLE -> PlayerState.IDLE
        SessionPlayer.PLAYER_STATE_PLAYING -> PlayerState.PLAYING
        SessionPlayer.PLAYER_STATE_ERROR -> PlayerState.ERROR
        SessionPlayer.PLAYER_STATE_PAUSED -> PlayerState.PAUSED
        else -> error("Unknown player state: ${controller.playerState}")
      }

    }

    override fun onDisconnected(controller: MediaController) {
      log.info("onDisconnected()")
      _connected.value = false
    }
  }


}


private val log = danbroid.logging.getLog(AudioClient::class)