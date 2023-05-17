package danbroid.audio.service

import android.app.Notification
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.media.AudioAttributesCompat
import androidx.media2.common.MediaItem
import androidx.media2.common.MediaMetadata
import androidx.media2.common.SessionPlayer
import androidx.media2.common.UriMediaItem
import androidx.media2.session.MediaSession
import androidx.media2.session.MediaSessionService
import androidx.media2.session.SessionCommand
import androidx.media2.session.SessionCommandGroup
import androidx.media2.session.SessionResult
import androidx.palette.graphics.Palette
import androidx.versionedparcelable.ParcelUtils
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerLibraryInfo
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.ext.media2.SessionPlayerConnector
import com.google.android.exoplayer2.metadata.Metadata
import com.google.android.exoplayer2.metadata.flac.VorbisComment
import com.google.android.exoplayer2.metadata.icy.IcyInfo
import com.google.android.exoplayer2.metadata.id3.TextInformationFrame
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.common.util.concurrent.ListenableFuture
import danbroid.audio.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class AudioService : MediaSessionService() {

  companion object {
    const val PACKAGE = "danbroid.media.service"
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


  lateinit var exoPlayer: SimpleExoPlayer
  lateinit var session: MediaSession

  private lateinit var callbackExecutor: Executor
  private lateinit var notificationManager: PlayerNotificationManager
  private val iconUtils = IconUtils(this)

  private val lifecycleScope = CoroutineScope(Dispatchers.IO + SupervisorJob())


  override fun onCreate() {
    log.info("onCreate() hashCode:${hashCode()}")
    super.onCreate()


    log.info("ExoPlayerLibraryInfo.VERSION_SLASHY = ${ExoPlayerLibraryInfo.VERSION_SLASHY}")

    callbackExecutor = ContextCompat.getMainExecutor(this)


    /**
     * SessionPlayerConnector​(Player player)
    Creates an instance using DefaultMediaItemConverter to convert between ExoPlayer and media2 MediaItems and DefaultControlDispatcher to dispatch player commands.
    SessionPlayerConnector​(Player player, MediaItemConverter mediaItemConverter)
     */


    createExternalExoPlayer()
    if (BuildConfig.DEBUG) log.debug("created player: $exoPlayer")


    val sessionPlayer = SessionPlayerConnector(exoPlayer)


    sessionPlayer.setAudioAttributes(
      AudioAttributesCompat.Builder()
        .setUsage(AudioAttributesCompat.USAGE_MEDIA)
        .setContentType(AudioAttributesCompat.CONTENT_TYPE_MUSIC)
        .build()
    )

    session =
      MediaSession.Builder(this, sessionPlayer)
        .setSessionCallback(callbackExecutor, SessionCallback())
        // .setId("danbroid.media.session")
        .build()


//    addSession(session)


    sessionPlayer.registerPlayerCallback(callbackExecutor, object : SessionPlayer.PlayerCallback() {

      val debug = false

      /*      override fun onCurrentMediaItemChanged(player: SessionPlayer, item: MediaItem) {
              log.warn("onCurrentMediaItemChanged() $item")
            }*/

      override fun onCurrentMediaItemChanged(player: SessionPlayer, item: MediaItem?) {
        item ?: return
        if (debug) log.debug("onCurrentMediaItemChanged() $item")
        //  loadIcon(item)
      }

      override fun onBufferingStateChanged(
        player: SessionPlayer,
        item: MediaItem?,
        buffState: Int
      ) {
        super.onBufferingStateChanged(player, item, buffState)
        if (debug) log.debug("onBufferingStateChanged() endPosition:${item?.endPosition}")
      }

      override fun onPlaybackCompleted(player: SessionPlayer) {
        if (debug) log.debug("onPlaybackCompleted()")
      }

      override fun onPlayerStateChanged(player: SessionPlayer, playerState: Int) {
        if (debug) log.debug("onPlayerStateChanged() $playerState = ${playerState.playerState} endPosition: ${player.currentMediaItem?.endPosition}")
      }

      override fun onPlaylistMetadataChanged(player: SessionPlayer, metadata: MediaMetadata?) {
        if (debug) log.debug("onPlaylistMetadataChanged() $metadata position: ${player.currentPosition} endPosition:${player.currentMediaItem?.endPosition} duration:${player.duration}")
      }

      override fun onTrackSelected(player: SessionPlayer, trackInfo: SessionPlayer.TrackInfo) {
        if (debug) log.debug("onTrackSelected() ${trackInfo.format}")
      }

      override fun onTrackDeselected(player: SessionPlayer, trackInfo: SessionPlayer.TrackInfo) {
        if (debug) log.debug("onTrackDeselected() $trackInfo")
      }

      override fun onTracksChanged(
        player: SessionPlayer,
        tracks: MutableList<SessionPlayer.TrackInfo>
      ) {
        if (debug) log.debug("onTracksChanged()")
      }
    })
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    if (BuildConfig.DEBUG) log.warn("onStartCommand() hashCode:${hashCode()}")
    super.onStartCommand(intent, flags, startId)
    return START_NOT_STICKY
  }

  override fun onDestroy() {
    super.onDestroy()
    log.info("onDestroy() hashCode:${hashCode()}")
    lifecycleScope.cancel("Service destroyed")
  }

  fun createExternalExoPlayer(): ExoPlayer {


    /*
        val ffmpegRenderersFactory = object : RenderersFactory {
          override fun createRenderers(
            eventHandler: Handler,
            videoRendererEventListener: VideoRendererEventListener,
            audioRendererEventListener: AudioRendererEventListener,
            textRendererOutput: TextOutput,
            metadataRendererOutput: MetadataOutput
          ) = arrayOf(FfmpegAudioRenderer(eventHandler, audioRendererEventListener).also {
          })
        }
    */


    val defaultRenderersFactory =
      DefaultRenderersFactory(this).setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
    val renderersFactory = defaultRenderersFactory

    val bwMeter = DefaultBandwidthMeter.Builder(this)
      .setResetOnNetworkTypeChange(true)
      .build().also {
        it.addEventListener(
          Handler(Looper.getMainLooper()),
          object : BandwidthMeter.EventListener {

            override fun onBandwidthSample(
              elapsedMs: Int,
              bytesTransferred: Long,
              bitrateEstimate: Long
            ) {
              log.warn("onBandwidth() $bytesTransferred bitrate:$bitrateEstimate")
            }

          })
      }

    exoPlayer = SimpleExoPlayer.Builder(this, renderersFactory)
      .setHandleAudioBecomingNoisy(true)
      .setBandwidthMeter(bwMeter)
      .build()



    notificationManager =
      createNotificationManager(this, notificationListener = ServiceNotificationListener())

    notificationManager.setPlayer(exoPlayer)

    exoPlayer.addAnalyticsListener(ExoAnalyticsListener())
    exoPlayer.addListener(object : Player.Listener {


      override fun onTracksChanged(
        trackGroups: TrackGroupArray,
        trackSelections: TrackSelectionArray
      ) {
        if (BuildConfig.DEBUG) log.trace("onTracksChanged()")
      }

      override fun onMetadata(metadata: Metadata) {
        (0 until metadata.length()).forEach {
          if (BuildConfig.DEBUG) log.trace("onMetadata() ${metadata[it]}")
        }
      }

      override fun onMediaMetadataChanged(mediaMetadata: com.google.android.exoplayer2.MediaMetadata) {
        if (BuildConfig.DEBUG) {
          log.trace("onMediaMetadataChanged() $mediaMetadata")
          log.trace("MediaMetadata: title:${mediaMetadata.title}")
          log.trace("MediaMetadata: artist:${mediaMetadata.artist}")
          log.trace("MediaMetadata: albumArtist:${mediaMetadata.albumArtist}")
          log.trace("MediaMetadata: year:${mediaMetadata.year}")

          mediaMetadata.extras?.keySet()?.forEach {
            log.trace("MediaMetadata: EXTRA KEY:${it}")
          }
        }

      }

      override fun onStaticMetadataChanged(metadataList: MutableList<Metadata>) {
        if (BuildConfig.DEBUG)
          log.trace("onStaticMetadataChanged() $metadataList")
      }
    })


    return exoPlayer
  }

  override fun startForegroundService(service: Intent): ComponentName? {
    log.info("startForegroundService(): $service")
    return super.startForegroundService(service)
  }


  override fun onUpdateNotification(session: MediaSession): MediaNotification? {
    if (BuildConfig.DEBUG) log.trace("onUpdateNotification()")
    //val notification = super.onUpdateNotification(session)

    return null
  }

  override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
    if (BuildConfig.DEBUG) log.trace("onGetSession() controllerInfo: $controllerInfo")

    return session
  }

  private fun loadIcon(mediaItem: MediaItem) {
    log.debug("loadIcon() $mediaItem")

    lifecycleScope.launch {
      fun updateMetadata(bitmap: BitmapDrawable) {
        if (BuildConfig.DEBUG) log.trace("updateMetadata()")

        val builder = MediaMetadata.Builder(mediaItem.metadata!!)

        val extras = mediaItem.metadata?.extras ?: bundleOf().also {
          builder.setExtras(it)
        }

        if (!extras.containsKey(MEDIA_METADATA_KEY_LIGHT_COLOR)) {
          if (BuildConfig.DEBUG) log.trace("generating palette............................................")

          val palette = Palette.from(bitmap.bitmap).generate()

          extras.putInt(
            MEDIA_METADATA_KEY_LIGHT_COLOR,
            palette.getLightVibrantColor(Color.TRANSPARENT)
          )
          extras.putInt(
            MEDIA_METADATA_KEY_DARK_COLOR,
            palette.getDarkVibrantColor(Color.TRANSPARENT)
          )
          extras.putInt(
            MEDIA_METADATA_KEY_LIGHT_MUTED_COLOR,
            palette.getLightMutedColor(Color.TRANSPARENT)
          )
          extras.putInt(
            MEDIA_METADATA_KEY_DARK_MUTED_COLOR,
            palette.getDarkMutedColor(Color.TRANSPARENT)
          )
          extras.putInt(
            MEDIA_METADATA_KEY_DOMINANT_COLOR,
            palette.getDominantColor(Color.TRANSPARENT)
          )
          extras.putInt(
            MEDIA_METADATA_KEY_VIBRANT_COLOR,
            palette.getVibrantColor(Color.TRANSPARENT)
          )
        }

        //extras.putParcelable(METADATA_EXTRAS_KEY_CACHED_ICON, bitmap)
        builder.putBitmap(MediaMetadata.METADATA_KEY_DISPLAY_ICON, bitmap.bitmap)
        mediaItem.metadata = builder.build()
      }

      mediaItem.metadata?.also {
        if (!it.containsKey(MediaMetadata.METADATA_KEY_DISPLAY_ICON))
          iconUtils.loadIcon(it)?.also {
            updateMetadata(it)
          }
      }
    }
  }

  inner class ServiceNotificationListener() : PlayerNotificationManager.NotificationListener {
    var foreground = false

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
      log.warn("onNotificationCancelled() byUser:$dismissedByUser")
      if (dismissedByUser) {
        log.warn("SHOULD STOP PLAYBACK")
      } else {
        if (foreground) {
          log.warn("stopping foreground ..")
          stopForeground(true)
          foreground = false
        }
      }
    }


    override fun onNotificationPosted(
      notificationId: Int,
      notification: Notification,
      ongoing: Boolean
    ) {
      log.warn("onNotificationPosted() ongoing:$ongoing mediaItem:${session.player.currentMediaItem?.metadata?.mediaId}")
      if (ongoing) {
        if (!foreground) {
          log.warn("starting foreground ..")
          // startForegroundService(Intent(applicationContext, javaClass))
          ContextCompat.startForegroundService(
            applicationContext,
            Intent(applicationContext, javaClass)
          )
          /*startForegroundService(
               Intent(applicationContext, javaClass)
           )*/
          startForeground(notificationId, notification)
          foreground = true
        }
      } else {
        if (foreground) {
          log.warn("stopping foreground ..")
          stopForeground(false)
          foreground = false
        }
      }
      //etStyle(androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle())
      // notificationManager.setColorized(true)
      if (BuildConfig.DEBUG) log.trace("metadata: ${session.player.currentMediaItem?.metadata.toDebugString()}")
      val extras = session.player.currentMediaItem?.metadata?.extras


      var dominantColor = extras.getColor(
        MEDIA_METADATA_KEY_DARK_MUTED_COLOR,
        MEDIA_METADATA_KEY_DOMINANT_COLOR,
        MEDIA_METADATA_KEY_DARK_COLOR,
        noColor = Color.TRANSPARENT
      )

      if (dominantColor == Color.TRANSPARENT) dominantColor =
        Config.Notifications.notificationColour

      if (BuildConfig.DEBUG) log.trace("dominantColor: $dominantColor duration:${session.player.currentMediaItem.duration}")
      notificationManager.setUseChronometer(session.player.currentMediaItem.duration.let { it > 0L && it != Long.MAX_VALUE })
      notificationManager.setColor(dominantColor)
    }
  }

  inner class SessionCallback : MediaSession.SessionCallback() {


    override fun onSetMediaUri(
      session: MediaSession,
      controller: MediaSession.ControllerInfo,
      uri: Uri,
      extras: Bundle?
    ): Int {
      log.debug("onSetMediaUri() $uri")

      extras ?: error("Extras not present")

      val metadata =
        ParcelUtils.getVersionedParcelable<MediaMetadata?>(extras, ACTION_ARG_MEDIA_ITEM)

      metadata ?: error("ACTION_ARG_MEDIA_ITEM not present in extras")

      val item = UriMediaItem.Builder(uri).setStartPosition(0L)
        .setEndPosition(-1L)
        .setMetadata(metadata)
        .build()

      loadIcon(item)
      log.trace("adding playlist item ...")
      session.player.addPlaylistItem(0, item).then {
        log.trace("added item to playlist: $it")
        session.player.prepare().then {
          log.trace("got $it calling play:")
          session.player.play().then {
            log.trace("play received $it")
          }
        }
      }
      return SessionResult.RESULT_SUCCESS
    }

    override fun onCreateMediaItem(
      session: MediaSession,
      controller: MediaSession.ControllerInfo,
      mediaId: String
    ): MediaItem? {
      log.error("onCreateMediaItem() $mediaId")
      return super.onCreateMediaItem(session, controller, mediaId)
    }

    override fun onCommandRequest(
      session: MediaSession,
      controller: MediaSession.ControllerInfo,
      command: SessionCommand
    ): Int {
      //log.debug("onCommandRequest() ${command.commandCode}:${command.customAction}:extras:${command.customExtras}")
      if (session.player.playerState == SessionPlayer.PLAYER_STATE_ERROR) {
        log.info("in the error state suppresion reason: ${exoPlayer.playbackSuppressionReason} error: ${exoPlayer.playerError}")
        //session.player.prepare()
        exoPlayer.prepare()
      }

      if (command.commandCode == SessionCommand.COMMAND_CODE_PLAYER_PAUSE) {
        if (BuildConfig.DEBUG) log.trace("PAUSING IT DUDE!")
      }
      return super.onCommandRequest(session, controller, command)
    }


    override fun onConnect(session: MediaSession, controller: MediaSession.ControllerInfo) =
      super.onConnect(session, controller)!!.let {
        SessionCommandGroup.Builder(it)
          .addCommand(SessionCommand(ACTION_ADD_TO_PLAYLIST, null))
          .build()
      }

    override fun onPostConnect(session: MediaSession, controller: MediaSession.ControllerInfo) {
      log.info("onPostConnect() session:$session controller:$controller")
      log.debug("controller uid: ${controller.uid} package: ${controller.packageName}")
      super.onPostConnect(session, controller)
    }

    override fun onCustomCommand(
      session: MediaSession,
      controller: MediaSession.ControllerInfo,
      customCommand: SessionCommand,
      args: Bundle?
    ): SessionResult {
      log.debug("onCustomCommand(): ${customCommand.commandCode}:${customCommand.customAction}:extras:${customCommand.customExtras.toDebugString()} args: ${args.toDebugString()}")

      if (customCommand.customAction == ACTION_ADD_TO_PLAYLIST) {
        val metadata =
          ParcelUtils.getVersionedParcelable<MediaMetadata>(args!!, ACTION_ARG_MEDIA_ITEM)!!
        log.trace("found metadata $metadata")
        val uri = metadata.getString(MediaMetadata.METADATA_KEY_MEDIA_URI)!!.toUri()
        //val uri = metadata.getString(MediaMetadata.METADATA_KEY_MEDIA_ID)!!.toUri()


        val mediaItem = UriMediaItem.Builder(uri)
          .setStartPosition(0).setEndPosition(-1L)
          .setMetadata(metadata)
          .build()

        loadIcon(mediaItem)

        log.trace("adding playlist item.. with uri: ${mediaItem.uri}")
        session.player.addPlaylistItem(Int.MAX_VALUE, mediaItem).then {
          log.trace("addToPlaylistItem returned ${it.successful}")
        }


        return SessionResult(SessionResult.RESULT_SUCCESS, null)
      }

      return super.onCustomCommand(session, controller, customCommand, args)
    }
  }


  inner class ExoAnalyticsListener : AnalyticsListener {

    override fun onBandwidthEstimate(
      eventTime: AnalyticsListener.EventTime,
      totalLoadTimeMs: Int,
      totalBytesLoaded: Long,
      bitrateEstimate: Long
    ) {
      log.error("ANALYTICS: loadTime: $totalLoadTimeMs totalBytesLoaded:$totalBytesLoaded bitrateEstimate:$bitrateEstimate")
    }

    override fun onMetadata(eventTime: AnalyticsListener.EventTime, metadata: Metadata) {
      log.trace("ANALYTICS: metadata $metadata pos:${exoPlayer.currentPosition} duration:${exoPlayer.duration}")
      var title: String? = null
      var album: String? = null
//      val currentMetadata = player.currentMediaItem!!.metadata!!

      (0 until metadata.length()).forEach {
        val entry = metadata.get(it)

        when (entry) {
          is VorbisComment -> {
            log.trace("ANALYTICS:VORBIS COMMENT: ${entry.key}:=<${entry.value}>")
            when (entry.key) {
              "Title" -> {
                title = entry.value
              }
            }
          }

          is TextInformationFrame -> {
            log.trace("ANALYTICS: ID3 COMMENT: ${entry.id}:=<${entry.value}>")
            when (entry.id) {
              "TIT2" -> {
                title = entry.value
              }

              "TALB" -> {
                album = entry.value
                log.trace("album: $album")
              }
            }
          }

          is IcyInfo -> {
            log.trace("ANALYTICS: IcyInfo title:${entry.title} ${entry.url}")
            title = entry.title
          }
        }
      }


      if (BuildConfig.DEBUG) {
        log.trace("playlistMetadata: ${session.player.playlistMetadata}")
        log.trace("currentItem.metadata: ${session.player.currentMediaItem?.metadata}")
        log.trace("title: $title")
      }

      if (title != "")
        MediaMetadata.Builder(session.player.currentMediaItem!!.metadata!!)
          .putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE, title)
          .build().also {
            session.player.currentMediaItem!!.metadata = it
          }


      /*  val oldMetadata = session.player.playlistMetadata?.also {
          log.dtrace("using playlistMetadata")
        } ?: session.player.currentMediaItem?.metadata?.also {
          log.dtrace("using currentMediaItem metadata")
        }!!

        val oldTitle = oldMetadata.getString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE)
        if (oldTitle != title && title != null) {
          MediaMetadata.Builder(oldMetadata)
              .putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE, title)
              .build().also {
                log.dtrace("updating metadata with new title: $title")
                session.player.updatePlaylistMetadata(it)
              }
        }*/

    }

    /*    override fun onTracksChanged(
            eventTime: AnalyticsListener.EventTime,
            trackGroups: TrackGroupArray,
            trackSelections: TrackSelectionArray
        ) {
          log.dwarn("ANALYTICS onTracksChanged()")
          for (n in 0 until trackGroups.length) {
            for (m in 0 until trackGroups[n].length) {
              trackGroups[n].getFormat(m).also { format ->
                val metadata = format.metadata
                val currentMetadata = player.currentMediaItem?.metadata
                log.dtrace("ANALYTICS:bitrate: ${format.bitrate}")
                log.dtrace("ANALYTICS:trackMetadata:$n cls:${format::class.java} ${metadata}")
                log.dtrace("ANALYTICS:currentMetadata: ${currentMetadata}")

                var title: String? = null

                if (metadata != null) {
                  (0 until metadata.length()).forEach {
                    val entry = metadata.get(it)
                    log.dtrace("ANALYTICS: entry: ${entry} cls: ${entry::class.java}")
                    when (entry) {
                      is VorbisComment -> {
                        log.dtrace("ANALYTICS:VORBIS COMMENT: ${entry.key}:=<${entry.value}>")
                        when (entry.key) {
                          "Title" -> {
                            title = entry.value
                          }
                        }
                      }
                      is TextInformationFrame -> {
                        log.dtrace("ANALYTICS: ID3 COMMENT: ${entry.id}:=<${entry.value}>")
                        when (entry.id) {
                          "TIT2" -> {
                            title = entry.value
                          }
                          "TALB" -> {
                            val album = entry.value
                            log.dinfo("album: $album")
                          }
                        }
                      }
                    }
                  }

                  if (currentMetadata != null && title != null) {
                    val oldTitle =
                        currentMetadata.getString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE)
                    if (oldTitle != title) {
                      val newMetadata = MediaMetadata.Builder(currentMetadata).also {
                        it.putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE, title)
                      }.build()
                      log.derror("ANALYTICS: updating currentMediaItem metadata with title:$title")

                    //  player.currentMediaItem?.metadata = newMetadata
                      player.updatePlaylistMetadata(newMetadata)
                    }
                  }
                }
              }
            }

          }

        }
        */

  }

  private fun <T> ListenableFuture<T>.then(job: (T) -> Unit) =
    addListener({
      job.invoke(get())
    }, callbackExecutor)
}

private fun Bundle?.toDebugString() = if (BuildConfig.DEBUG) this?.let { data ->
  data.keySet().joinToString(",") {
    "$it:${data[it]}"
  }.let {
    "Bundle<$it>"
  }
} ?: "null"
else this.toString()


fun MediaMetadata?.toDebugString(): String = if (BuildConfig.DEBUG) this.run {
  if (this != null) {
    keySet().joinToString {
      it
    }.let { "MediaMetadata<${mediaId}:$it:extras:${extras.toDebugString()}>" }
  } else "MediaMetadata<null>"
} else "Metadata"


