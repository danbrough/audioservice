package danbroid.audio.service


import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.media2.common.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.NotificationUtil
import danbroid.audio.R


object Config {

  var PROJECT_VERSION = "0.0.0"

  object Notifications {

    var notificationID = 1438293

    @DrawableRes
    var statusBarIcon = R.drawable.ic_audio_track

    @ColorInt
    var notificationColour = 0

    @ColorInt
    var notificationIconTint = 0

    @DrawableRes
    var defaultNotificationIcon = R.drawable.ic_audio_track

    var notificationIconWidth = 256

    var notificationIconHeight = 256
  }


}

class NotificationListener(val service: AudioService) :
  PlayerNotificationManager.NotificationListener {
  var serviceForeground = false


  override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
    log.trace("onNotificationCancelled() byUser:$dismissedByUser")
    if (dismissedByUser) {
      log.trace("SHOULD STOP PLAYBACK")
    } else {
      if (serviceForeground) {
        log.trace("stopping foreground ..")
        service.stopForeground(true)
        serviceForeground = false
      }
    }
  }

  override fun onNotificationPosted(
    notificationId: Int,
    notification: Notification,
    ongoing: Boolean
  ) {
    //log.warn("onNotificationPosted() ongoing:$ongoing")
    if (ongoing) {
      if (!serviceForeground) {
        log.trace("starting foreground ..")
        ContextCompat.startForegroundService(
          service.applicationContext,
          Intent(service.applicationContext, service.javaClass)
        )
        service.startForeground(notificationId, notification)
        serviceForeground = true
      }
    } else {
      if (serviceForeground) {
        log.trace("stopping foreground ..")
        service.stopForeground(false)
        serviceForeground = false
      }
    }
  }
}

fun createNotificationManager(
  service: AudioService,
  notificationID: Int = Config.Notifications.notificationID,
  notificationListener: PlayerNotificationManager.NotificationListener = NotificationListener(
    service
  )
): PlayerNotificationManager {
  log.trace("createNotificationManager()")

  NotificationUtil.createNotificationChannel(
    service.applicationContext,
    service.getString(R.string.notification_channel_id),
    R.string.notification_channel_name,
    R.string.notification_description,
    NotificationUtil.IMPORTANCE_LOW
  )

  return PlayerNotificationManager.Builder(
    service, notificationID,
    service.getString(R.string.notification_channel_id),
    PlayerDescriptionAdapter(service)
  )
    .setNotificationListener(notificationListener)
    .setSmallIconResourceId(Config.Notifications.statusBarIcon)
    .build().also {
      //it.setUseChronometer(true)
      if (Config.Notifications.notificationColour != 0) {
        it.setColor(Config.Notifications.notificationColour)
      }
      it.setUseNextActionInCompactView(true)
      it.setUsePreviousActionInCompactView(true)
      it.setColorized(true)
    }

  /*  return object : PlayerNotificationManager(
        service,
        service.getString(R.string.notification_channel_id),
        notificationID,
        PlayerDescriptionAdapter(service),
        notificationListener
    ) {
      override fun createNotification(
          player: Player,
          builder: NotificationCompat.Builder?,
          ongoing: Boolean,
          largeIcon: Bitmap?
      ): NotificationCompat.Builder? {
        return super.createNotification(player, builder, ongoing, largeIcon)?.apply {
          setUsesChronometer(player.isCurrentWindowSeekable)
          setShowWhen(player.isCurrentWindowSeekable)
        }
      }

      override fun getActions(player: Player) = super.getActions(player).also {
        if (!player.hasPrevious()) it.remove(ACTION_PREVIOUS)
      }

    }.apply {
      setSmallIcon(Config.Notifications.statusBarIcon)
      setUseNextActionInCompactView(true)
      setUsePreviousActionInCompactView(true)

      if (Config.Notifications.notificationColour != 0) {
        setColor(Config.Notifications.notificationColour)
      }

      setUsePlayPauseActions(true)
      setControlDispatcher(DefaultControlDispatcher(0L, 0L))
      //setUseChronometer(true)
      setColorized(true)
      setUseStopAction(false)
    }*/
}


private class PlayerDescriptionAdapter(val service: AudioService) :
  PlayerNotificationManager.MediaDescriptionAdapter {

  val context: Context = service

  val currentMetadata: MediaMetadata?
    get() = service.session.player.currentMediaItem?.metadata

  override fun createCurrentContentIntent(player: Player) =
    service.packageManager?.getLaunchIntentForPackage(service.packageName)?.let { sessionIntent ->
      PendingIntent.getActivity(service, 0, sessionIntent, 0)
    }

  override fun getCurrentContentTitle(player: Player): CharSequence {
    return currentMetadata?.let {
      it.getText(MediaMetadata.METADATA_KEY_DISPLAY_TITLE)
        ?: it.getText(MediaMetadata.METADATA_KEY_TITLE)
    } ?: context.getString(R.string.untitled)
  }

  override fun getCurrentContentText(player: Player): CharSequence? {
    return currentMetadata?.let {
      it.getText(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE)
        ?: it.getText(MediaMetadata.METADATA_KEY_DISPLAY_DESCRIPTION)
    }
  }
  /*
    override fun getCurrentSubText(player: Player): CharSequence? {
      return super.getCurrentSubText(player)
    }*/


  override fun getCurrentLargeIcon(
    player: Player,
    callback: PlayerNotificationManager.BitmapCallback
  ) = currentMetadata?.getBitmap(MediaMetadata.METADATA_KEY_DISPLAY_ICON)
}