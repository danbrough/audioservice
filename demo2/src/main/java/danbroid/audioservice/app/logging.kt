package danbroid.audioservice.app


import klog.KLogWriters
import klog.KMessageFormatters
import klog.Level
import klog.colored
import klog.klog
/*
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
      it.startsWith("danbroid.audio.client") -> clientLog
      it.startsWith("danbroid.audio.service") -> serviceLog
      else -> demoLog
    }
  }
  demoLog
}*/


val log = klog("AUDIO_DEMO"){
  level = Level.TRACE
  messageFormatter = KMessageFormatters.verbose.colored
  writer = KLogWriters.stdOut
}