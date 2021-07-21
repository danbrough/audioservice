package danbroid.audioservice.app

import danbroid.logging.AndroidLog
import danbroid.logging.LogConfig

val log = LogConfig.let {
  val demoLog = AndroidLog("AUDIO_DEMO")
  val clientLog = AndroidLog("AUDIO_CLIENT")
  val serviceLog = AndroidLog("AUDIO_SERVICE")

  it.COLOURED = true
  it.DEBUG = BuildConfig.DEBUG
  it.DETAILED = true
  demoLog.debug("created demo log")

  it.GET_LOG = {
    when{
      it.startsWith("danbroid.media.client") -> clientLog
      it.startsWith("danbroid.media.service") -> serviceLog
      else -> demoLog
    }
  }
  demoLog
}


object Logging {
  val jlog = log
}