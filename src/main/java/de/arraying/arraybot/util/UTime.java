package de.arraying.arraybot.util;

import net.dv8tion.jda.api.entities.Guild;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright 2017 Arraying, AlphartDev.
 *
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class UTime {

    /**
     * The pattern to use when figuring out the time.
     * Credits to @AlphartDev on GitHub for this pattern.
     */
    private static final Pattern TIME_PATTERN = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?" +
                "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?" +
                "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?" +
                "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?" +
                "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?" +
                "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?" +
                "(?:([0-9]+)\\s*(?:s[a-z]*)?)?",
            Pattern.CASE_INSENSITIVE);

    /**
     * Parses the duration of the punishment.
     * Credits to @AlphartDev on GitHub for this method.
     * @param duration The duration as a string.
     * @return The time in milliseconds when this duration is met.
     */
    public static long parseDuration(String duration) {
        Matcher matcher = TIME_PATTERN.matcher(duration);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        boolean found = false;
        while(matcher.find()) {
            if(matcher.group() == null
                    || matcher.group().isEmpty()) {
                continue;
            }
            for(int i = 0; i < matcher.groupCount(); i++) {
                if(matcher.group(i) != null
                        && !matcher.group(i).isEmpty()) {
                    found = true;
                    break;
                }
            }
            if(found) {
                if(matcher.group(1) != null
                        && !matcher.group(1).isEmpty()) {
                    years = Integer.parseInt(matcher.group(1));
                }
                if(matcher.group(2) != null
                        && !matcher.group(2).isEmpty()) {
                    months = Integer.parseInt(matcher.group(2));
                }
                if(matcher.group(3) != null
                        && !matcher.group(3).isEmpty()) {
                    weeks = Integer.parseInt(matcher.group(3));
                }
                if(matcher.group(4) != null
                        && !matcher.group(4).isEmpty()) {
                    days = Integer.parseInt(matcher.group(4));
                }
                if(matcher.group(5) != null
                        && !matcher.group(5).isEmpty()) {
                    hours = Integer.parseInt(matcher.group(5));
                }
                if(matcher.group(6) != null
                        && !matcher.group(6).isEmpty()) {
                    minutes = Integer.parseInt(matcher.group(6));
                }
                if(matcher.group(7) != null
                        && !matcher.group(7).isEmpty()) {
                    seconds = Integer.parseInt(matcher.group(7));
                }
                break;
            }
        }
        if(!found) {
            return -1;
        }
        GregorianCalendar calendar = new GregorianCalendar();
        if(years > 0) {
            calendar.add(Calendar.YEAR, years);
        }
        if(months > 0) {
            calendar.add(Calendar.MONTH, months);
        }
        if(weeks > 0) {
            calendar.add(Calendar.WEEK_OF_YEAR, weeks);
        }
        if(days > 0) {
            calendar.add(Calendar.DAY_OF_MONTH, days);
        }
        if(hours > 0) {
            calendar.add(Calendar.HOUR_OF_DAY, hours);
        }
        if(minutes > 0) {
            calendar.add(Calendar.MINUTE, minutes);
        }
        if(seconds > 0) {
            calendar.add(Calendar.SECOND, seconds);
        }
        return calendar.getTimeInMillis();
    }

    /**
     * Gets a displayable time string.
     * @param guild The guild where the string is taken from.
     * @param time The time, in milliseconds, to use.
     */
    public static String getDisplayableTime(Guild guild, long time) {
        TimeZone timeZone;
        switch(guild.getRegion()) {
            case AMSTERDAM:
                timeZone = TimeZone.getTimeZone("Europe/Amsterdam");
                break;
            case BRAZIL:
                timeZone = TimeZone.getTimeZone("Brazil/Acre");
                break;
            case EU_CENTRAL:
            case FRANKFURT:
                timeZone = TimeZone.getTimeZone("Europe/Berlin");
                break;
            case EU_WEST:
            case LONDON:
                timeZone = TimeZone.getTimeZone("Europe/London");
                break;
            case HONG_KONG:
                timeZone = TimeZone.getTimeZone("Asia/Hong_Kong");
                break;
            case RUSSIA:
                timeZone = TimeZone.getTimeZone("Europe/Moscow");
                break;
            case SINGAPORE:
                timeZone = TimeZone.getTimeZone("Asia/Singapore");
                break;
            case SYDNEY:
                timeZone = TimeZone.getTimeZone("Australia/Sydney");
                break;
            case US_CENTRAL:
            case US_SOUTH:
                timeZone = TimeZone.getTimeZone("America/Chicago");
                break;
            case US_EAST:
                timeZone = TimeZone.getTimeZone("America/New_York");
                break;
            case US_WEST:
                timeZone = TimeZone.getTimeZone("America/Los_Angeles");
                break;
            case VIP_AMSTERDAM:
                timeZone = TimeZone.getTimeZone("Europe/Amsterdam");
                break;
            case VIP_BRAZIL:
                timeZone = TimeZone.getTimeZone("Brazil/Acre");
                break;
            case VIP_EU_CENTRAL:
            case VIP_FRANKFURT:
                timeZone = TimeZone.getTimeZone("Europe/Berlin");
                break;
            case VIP_EU_WEST:
            case VIP_LONDON:
                timeZone = TimeZone.getTimeZone("Europe/London");
                break;
            case VIP_SINGAPORE:
                timeZone = TimeZone.getTimeZone("Asia/Singapore");
                break;
            case VIP_SYDNEY:
                timeZone = TimeZone.getTimeZone("Australia/Sydney");
                break;
            case VIP_US_CENTRAL:
            case VIP_US_SOUTH:
                timeZone = TimeZone.getTimeZone("America/Chicago");
                break;
            case VIP_US_EAST:
                timeZone = TimeZone.getTimeZone("America/New_York");
                break;
            case VIP_US_WEST:
                timeZone = TimeZone.getTimeZone("America/Los_Angeles");
                break;
            default:
                timeZone = TimeZone.getTimeZone("Europe/Berlin");
                break;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
        simpleDateFormat.setTimeZone(timeZone);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(time);
        return simpleDateFormat.format(calendar.getTime());
    }

}
