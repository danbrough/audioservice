package danbroid.audioservice.app

import danbroid.logging.AndroidLog
import danbroid.logging.LogConfig

val log = LogConfig.let {
  val log = AndroidLog("AUDIO_DEMO")
  it.GET_LOG = {
    log
  }
  it.COLOURED = true
  it.DEBUG = BuildConfig.DEBUG
  it.DETAILED = true
  log.debug("created log")
  log
}
