package de.arraying.arraybot.utils

import net.dv8tion.jda.core.Region
import net.dv8tion.jda.core.entities.Guild
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * Credits to this duration parser code goes to @AlphartDev on GitHub.
 */
object UtilsTime {

    private val timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?"
            + "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?"
            + "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?"
            + "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE)

    /**
     * Gets the duration of the punishments.
     */
    fun parseDuration(durationStr: String): Long {
        val matcher = timePattern.matcher(durationStr)
        var years = 0
        var months = 0
        var weeks = 0
        var days = 0
        var hours = 0
        var minutes = 0
        var seconds = 0
        var found = false
        while(matcher.find()) {
            if(matcher.group() == null 
                    || matcher.group().isEmpty()) {
                continue
            }
            for(i in 0..matcher.groupCount() - 1) {
                if(matcher.group(i) != null 
                        && !matcher.group(i).isEmpty()) {
                    found = true
                    break
                }
            }
            if(found) {
                if(matcher.group(1) != null
                        && !matcher.group(1).isEmpty()) {
                    years = Integer.parseInt(matcher.group(1))
                }
                if(matcher.group(2) != null
                        && !matcher.group(2).isEmpty()) {
                    months = Integer.parseInt(matcher.group(2))
                }
                if(matcher.group(3) != null
                        && !matcher.group(3).isEmpty()) {
                    weeks = Integer.parseInt(matcher.group(3))
                }
                if(matcher.group(4) != null
                        && !matcher.group(4).isEmpty()) {
                    days = Integer.parseInt(matcher.group(4))
                }
                if(matcher.group(5) != null
                        && !matcher.group(5).isEmpty()) {
                    hours = Integer.parseInt(matcher.group(5))
                }
                if(matcher.group(6) != null
                        && !matcher.group(6).isEmpty()) {
                    minutes = Integer.parseInt(matcher.group(6))
                }
                if(matcher.group(7) != null
                        && !matcher.group(7).isEmpty()) {
                    seconds = Integer.parseInt(matcher.group(7))
                }
                break
            }
        }
        if(!found) {
            return -1
        }
        val calendar = GregorianCalendar()
        if(years > 0) {
            calendar.add(Calendar.YEAR, years)
        }
        if(months > 0) {
            calendar.add(Calendar.MONTH, months)
        }
        if(weeks > 0) {
            calendar.add(Calendar.WEEK_OF_YEAR, weeks)
        }
        if(days > 0) {
            calendar.add(Calendar.DAY_OF_MONTH, days)
        }
        if(hours > 0) {
            calendar.add(Calendar.HOUR_OF_DAY, hours)
        }
        if(minutes > 0) {
            calendar.add(Calendar.MINUTE, minutes)
        }
        if(seconds > 0) {
            calendar.add(Calendar.SECOND, seconds)
        }
        return calendar.timeInMillis
    }


    /**
     * Gets a displayable time string.
     */
    fun getDisplayableTime(guild: Guild, time: Long = System.currentTimeMillis()): String {
        val timezone: TimeZone = when(guild.region) {
            Region.AMSTERDAM -> TimeZone.getTimeZone("Europe/Amsterdam")
            Region.BRAZIL -> TimeZone.getTimeZone("Brazil/Acre")
            Region.EU_CENTRAL -> TimeZone.getTimeZone("Europe/Berlin")
            Region.EU_WEST -> TimeZone.getTimeZone("Europe/London")
            Region.FRANKFURT -> TimeZone.getTimeZone("Europe/Berlin")
            Region.LONDON -> TimeZone.getTimeZone("Europe/London")
            Region.SINGAPORE -> TimeZone.getTimeZone("Asia/Singapore")
            Region.SYDNEY -> TimeZone.getTimeZone("Australia/Sydney")
            Region.US_CENTRAL -> TimeZone.getTimeZone("America/Chicago")
            Region.US_EAST -> TimeZone.getTimeZone("America/New_York")
            Region.US_SOUTH -> TimeZone.getTimeZone("America/Chicago")
            Region.US_WEST -> TimeZone.getTimeZone("America/Los_Angeles")
            Region.VIP_AMSTERDAM -> TimeZone.getTimeZone("Europe/Amsterdam")
            Region.VIP_BRAZIL -> TimeZone.getTimeZone("Brazil/Acre")
            Region.VIP_EU_CENTRAL -> TimeZone.getTimeZone("Europe/Berlin")
            Region.VIP_EU_WEST -> TimeZone.getTimeZone("Europe/London")
            Region.VIP_FRANKFURT -> TimeZone.getTimeZone("Europe/Berlin")
            Region.VIP_LONDON -> TimeZone.getTimeZone("Europe/London")
            Region.VIP_SINGAPORE -> TimeZone.getTimeZone("Asia/Singapore")
            Region.VIP_SYDNEY -> TimeZone.getTimeZone("Australia/Sydney")
            Region.VIP_US_CENTRAL -> TimeZone.getTimeZone("America/Chicago")
            Region.VIP_US_EAST -> TimeZone.getTimeZone("America/New_York")
            Region.VIP_US_SOUTH -> TimeZone.getTimeZone("America/Chicago")
            Region.VIP_US_WEST -> TimeZone.getTimeZone("America/Los_Angeles")
            else -> TimeZone.getTimeZone("Europe/Berlin")
        }
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy kk:mm:ss")
        simpleDateFormat.timeZone = timezone
        val calendar = GregorianCalendar.getInstance()
        calendar.timeInMillis = time
        return simpleDateFormat.format(calendar.time)
    }

}