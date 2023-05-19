package danbroid.audio.service

import danbroid.audio.BuildConfig
import klog.KMessageFormatters
import klog.Level
import klog.colored
import klog.klog

private object Log

internal val log = klog("AUDIO_SERVICE") {
  level = if (BuildConfig.DEBUG) Level.TRACE else Level.INFO
  messageFormatter =
    if (BuildConfig.DEBUG) KMessageFormatters.verbose.colored else KMessageFormatters.simple
}