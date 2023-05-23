package danbroid.audio.service

import android.widget.Toast
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.common.util.concurrent.MoreExecutors
import danbroid.audio.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.concurrent.Executors

@UnstableApi
class AudioService : MediaSessionService() {

  companion object {
    private const val PACKAGE = "danbroid.audio.service"
    const val ACTION_ADD_TO_PLAYLIST = "$PACKAGE.ACTION_ADD_TO_PLAYLIST"
    const val ACTION_ARG_MEDIA_ITEM = "$PACKAGE.MEDIA_ITEM"

    const val MEDIA_METADATA_KEY_BITRATE =
      "$PACKAGE.MEDIA_METADATA_KEY_BITRATE"
    const val MEDIA_METADATA_KEY_LIGHT_COLOR =
      "$PACKAGE.MEDIA_METADATA_KEY_LIGHT_COLOR"
    const val MEDIA_METADATA_KEY_DARK_COLOR =
      "$PACKAGE.MEDIA_METADATA_KEY_DARK_COLOR"
    const val MEDIA_METADATA_KEY_LIGHT_MUTED_COLOR =
      "$PACKAGE.MEDIA_METADATA_KEY_LIGHT_MUTED_COLOR"
    const val MEDIA_METADATA_KEY_DARK_MUTED_COLOR =
      "$PACKAGE.MEDIA_METADATA_KEY_DARK_MUTED_COLOR"
    const val MEDIA_METADATA_KEY_DOMINANT_COLOR =
      "$PACKAGE.MEDIA_METADATA_KEY_DOMINANT_COLOR"
    const val MEDIA_METADATA_KEY_VIBRANT_COLOR =
      "$PACKAGE.MEDIA_METADATA_KEY_VIBRANT_COLOR"
  }

  private val serviceJob = SupervisorJob()

  private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

  private val executorService by lazy {
    MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor())
  }

  private val playerListener = PlayerEventListener()

  private val exoPlayer: Player by lazy {

    val audioAttributes = AudioAttributes.Builder()
      .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
      .setUsage(C.USAGE_MEDIA)
      .build()

    ExoPlayer.Builder(this).build().apply {
      setAudioAttributes(audioAttributes, true)
      setHandleAudioBecomingNoisy(true)
      addListener(playerListener)
      addAnalyticsListener(EventLogger(PACKAGE))
    }
  }


  private lateinit var session: MediaSession

  override fun onCreate() {
    log.info("onCreate()")
    super.onCreate()
  }

  override fun onDestroy() {
    log.info("onDestroy()")
    if (::session.isInitialized) {
      log.info("releasing exoPlayer")
      exoPlayer.release()
      session.release()
    }

    super.onDestroy()
  }

  override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
    if (!::session.isInitialized) {
      log.debug("creating media session..")
      session = MediaSession.Builder(this, exoPlayer).build()
    }
    return session
  }

  private inner class PlayerEventListener : Player.Listener {

    override fun onEvents(player: Player, events: Player.Events) {
      if (events.contains(Player.EVENT_POSITION_DISCONTINUITY)
        || events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)
        || events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED)
      ) {
        /* TODO
        currentMediaItemIndex = player.currentMediaItemIndex
        saveRecentSongToStorage()
         */
      }
    }

    override fun onPlayerError(error: PlaybackException) {
      var message: Int = R.string.error_generic
      log.error("Player error: ${error.errorCodeName} (${error.errorCode}),error")
      if (error.errorCode == PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS
        || error.errorCode == PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND
      ) {
        message = R.string.error_media_not_found
      }

      Toast.makeText(
        applicationContext,
        message,
        Toast.LENGTH_LONG
      ).show()
    }
  }
}

