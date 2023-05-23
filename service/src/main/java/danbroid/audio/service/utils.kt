package danbroid.audio.service

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.media3.common.Player
import androidx.media3.session.SessionResult
import danbroid.audio.service.util.httpSupport
import okhttp3.CacheControl
import java.util.concurrent.TimeUnit


suspend fun parsePlaylistURL(context: Context, url: String): String? =
  context.httpSupport.requestString(url, CacheControl.Builder().maxStale(1, TimeUnit.DAYS).build())
    .let {
      it.lines().firstNotNullOfOrNull { line ->
        val i = line.indexOf('=')
        if (line.startsWith("File") && i > 0) {
          line.substring(i + 1).trim()
        } else if (line.startsWith("http")) {
          line.trimEnd()
        } else null
      }
    }

/*@Throws(IOException::class)
private fun processPlaylistURL(
    url: String,
    maxStale: Int = 60 * 60 * 12
): String? {
  // log.trace("processUrl() $url")

  context.httpSupport.cacheRequest(
      url,
      CacheControl.Builder().maxStale(1, TimeUnit.DAYS).build()
  ).use {
    it.body?.string()?.lines()?.forEach { line ->
      val i = line.indexOf('=');
      if (line.startsWith("File") && i > 0) {
        return line.substring(i + 1).trim()
      } else if (line.startsWith("http")) {
        return line.trimEnd()
      }
    }
  }
  throw IOException("Failed to parse playlist url: $url")
}*/

/*
TODO
@SessionPlayer.PlayerState
val Int.playerState: String
  get() = when (this) {
    SessionPlayer.PLAYER_STATE_IDLE -> "PLAYER_STATE_IDLE"
    SessionPlayer.PLAYER_STATE_PAUSED -> "PLAYER_STATE_PAUSED"
    SessionPlayer.PLAYER_STATE_PLAYING -> "PLAYER_STATE_PLAYING"
    SessionPlayer.PLAYER_STATE_ERROR -> "PLAYER_STATE_ERROR"
    else -> "ERROR_INVALID_PLAYER_STATE: $this"
  }
*/


val @Player.State
Int.exoPlayerState: String
  get() = when (this) {
    Player.STATE_IDLE -> "STATE_IDLE"
    Player.STATE_BUFFERING -> "STATE_BUFFERING"
    Player.STATE_ENDED -> "STATE_ENDED"
    Player.STATE_READY -> "STATE_READY"
    else -> "ERROR_INVALID_STATE: $this"
  }

/*TODO @Player.BuffState
val Int.buffState: String
  get() = when (this) {
    SessionPlayer.BUFFERING_STATE_UNKNOWN -> "BUFFERING_STATE_UNKNOWN"
    SessionPlayer.BUFFERING_STATE_BUFFERING_AND_PLAYABLE -> "BUFFERING_STATE_BUFFERING_AND_PLAYABLE"
    SessionPlayer.BUFFERING_STATE_BUFFERING_AND_STARVED -> "BUFFERING_STATE_BUFFERING_AND_STARVED"
    SessionPlayer.BUFFERING_STATE_COMPLETE -> "BUFFERING_STATE_COMPLETE"
    else -> "ERROR_INVALID_BUFF_STATE: $this"
  }*/


val @Player.PlayWhenReadyChangeReason
Int.playWhenReadyChangeReason: String
  get() = when (this) {
    Player.PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST -> "PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST"
    Player.PLAY_WHEN_READY_CHANGE_REASON_AUDIO_FOCUS_LOSS -> "PLAY_WHEN_READY_CHANGE_REASON_AUDIO_FOCUS_LOSS"
    Player.PLAY_WHEN_READY_CHANGE_REASON_AUDIO_BECOMING_NOISY -> "PLAY_WHEN_READY_CHANGE_REASON_AUDIO_BECOMING_NOISY"
    Player.PLAY_WHEN_READY_CHANGE_REASON_REMOTE -> "PLAY_WHEN_READY_CHANGE_REASON_REMOTE"
    Player.PLAY_WHEN_READY_CHANGE_REASON_END_OF_MEDIA_ITEM -> "PLAY_WHEN_READY_CHANGE_REASON_END_OF_MEDIA_ITEM"
    else -> "ERROR: Invalid PlayWhenReadyChangeReason: $this"
  }

val SessionResult.successfull: Boolean
  get() = resultCode == SessionResult.RESULT_SUCCESS

/*TODO val Player.PlayerResult.successfull: Boolean
  @SuppressLint("RestrictedApi")
  get() = resultCode == SessionPlayer.PlayerResult.RESULT_SUCCESS*/

/*
TODO
val MediaItem?.duration: Long
  get() = this?.metadata?.getLong(MediaMetadata.METADATA_KEY_DURATION) ?: 0L
*/

@ColorInt
fun Bundle?.getColor(vararg keys: String, noColor: Int = Color.TRANSPARENT): Int {
  if (this == null) return noColor
  keys.forEach {
    if (containsKey(it))
      getInt(it).also {
        if (it != noColor)
          return it
      }
  }
  return noColor
}
