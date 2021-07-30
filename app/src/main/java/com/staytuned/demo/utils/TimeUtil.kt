package com.staytuned.demo.utils

class TimeUtil {
    companion object {
        /**
         * Function to convert milliseconds time to Timer Format
         * Hours:Minutes:Seconds
         */
        fun milliSecondsToTimer(ms: Long): String {

            val milliseconds = if (ms < 0) 0 else ms

            var finalTimerString = ""
            val secondsString: String

            // Convert total duration into time
            val hours = (milliseconds / (1000 * 60 * 60)).toInt()
            val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
            val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
            // Add hours if there
            if (hours > 0) {
                finalTimerString = "$hours:"
            }

            // Prepending 0 to seconds if it is one digit
            secondsString = if (seconds < 10) {
                "0$seconds"
            } else {
                "" + seconds
            }

            finalTimerString = "$finalTimerString$minutes:$secondsString"

            // return timer string
            return finalTimerString
        }

        fun milliSecondsToTimerVerbose(ms: Long): String {

            val milliseconds = if (ms < 0) 0 else ms

            var finalTimerString = ""
            var secondsString: String

            // Convert total duration into time
            val hours = (milliseconds / (1000 * 60 * 60)).toInt()
            val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
            val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
            // Add hours if there
            if (hours > 0) {
                finalTimerString = "$hours h"
            }

            finalTimerString = "$finalTimerString $minutes mn"

            return finalTimerString
        }
    }
}