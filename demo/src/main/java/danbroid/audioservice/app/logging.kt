package danbroid.audioservice.app


import danbroid.audio.BuildConfig
import klog.KMessageFormatters
import klog.Level
import klog.colored
import klog.klog


val log = klog("AUDIO_DEMO") {
  level = if (BuildConfig.DEBUG) Level.TRACE else Level.INFO
  messageFormatter =
    if (BuildConfig.DEBUG) KMessageFormatters.verbose.colored else KMessageFormatters.simple
}