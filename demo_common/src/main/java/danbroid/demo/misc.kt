package danbroid.demo

import kotlin.math.floor

fun Number.formatDurationFromSeconds(): String {
  var totalSeconds = this.toLong()
  val hours = floor(totalSeconds/(60*60.0)).toLong()
  totalSeconds -= hours*60*60
  val mins = floor(totalSeconds/60.0).toLong()
  totalSeconds -= mins*60
  val seconds = totalSeconds

  return "%d:%02d:%02d".format(hours, mins, seconds)
}