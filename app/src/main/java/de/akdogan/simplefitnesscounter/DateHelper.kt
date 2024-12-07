package de.akdogan.simplefitnesscounter

import android.util.Log
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateHelper {

    fun getNumberOfDaysInCurrentWeek(startDayOfWeek: DayOfWeek): Int {
        val todayInt = nowZoned().dayOfWeek.value
        val startDayInt = startDayOfWeek.value
        Log.d("Arif", "Today: $todayInt -- startDay: $startDayInt")
        val offSet = if (todayInt < startDayInt) {
            7 - (startDayInt - todayInt)
        } else {
            todayInt - startDayInt
        }
        return offSet + 1
    }

    /**
     * Returns the start of a day in the past.
     * @param days Number of days to go into the past from today. Example: If today is Wednesday, days 2 will return
     * the start of the last monday
     * @return start of the specified day in epoch seconds
     */
    fun getStartOfPastDay(days: Int): Long {
        val zdtStartOfBefore = nowZoned().toLocalDate().atStartOfDay(getSystemZone()).minusDays(days.toLong() - 1)
        Log.d("Arif", "getStartOfPastDay: days = $days -- startOfDay = $zdtStartOfBefore")
        return zdtStartOfBefore.toEpochSecond()
    }

    fun getStartOfPastDayLocal(days: Int): LocalDateTime {
        val zdtStartOfBefore = nowZoned().toLocalDate().atStartOfDay(getSystemZone()).minusDays(days.toLong() - 1)
        Log.d("Arif", "getStartOfPastDay: days = $days -- startOfDay = $zdtStartOfBefore")
        return zdtStartOfBefore.toLocalDateTime()
    }

    fun nowZoned(): ZonedDateTime {
        return ZonedDateTime.now()
    }

    fun getNow(): LocalDateTime {
        return nowZoned().toLocalDateTime()
    }

    private fun getSystemZone(): ZoneId? {
        return ZoneId.systemDefault()
    }

    val dateDisplayFormat = DateTimeFormatter.ofPattern("")
}

fun LocalDateTime.toUiFormat(): String {
    return this.format(DateTimeFormatter.ofPattern("E dd.MM"))
}
//
//enum class DayOfWeekDomain(val identifier: Int) {
//    MONDAY(1),
//    TUESDAY(2),
//    WEDNESDAY(3),
//    THURSDAY(4),
//    FRIDAY(5),
//    SATURDAY(6),
//    SUNDAY(7);
//
//    companion object {
//        fun fromInt(
//            @IntRange(from = 1, to = 7)
//            isoStartDayOfWeek: Int
//        ): DayOfWeekDomain {
//            return DayOfWeekDomain.entries.first { it.identifier == isoStartDayOfWeek }
//        }
//    }
//}