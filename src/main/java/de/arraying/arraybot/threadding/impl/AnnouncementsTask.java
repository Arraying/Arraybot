package de.arraying.arraybot.threadding.impl;

import de.arraying.arraybot.Arraybot;
import de.arraying.arraybot.data.database.categories.AnnouncementEntry;
import de.arraying.arraybot.data.database.categories.AnnouncementIdsEntry;
import de.arraying.arraybot.data.database.categories.GuildEntry;
import de.arraying.arraybot.data.database.core.Category;
import de.arraying.arraybot.data.database.core.EntryField;
import de.arraying.arraybot.threadding.AbstractWatcher;
import de.arraying.arraybot.util.UDatatypes;
import de.arraying.arraybot.util.UDefaults;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Copyright 2018 Arraying
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
public final class AnnouncementsTask extends AbstractWatcher {

    /**
     * All active announcement tasks.
     */
    private static final Map<Long, AnnouncementsTask> tasks = new ConcurrentHashMap<>();

    private final long guild;

    /**
     * Creates a new announcement task.
     * @param guild The guild ID.
     * @param interval The interval in minutes.
     */
    private AnnouncementsTask(long guild, int interval) {
        super("Announcements-" + guild, (int) TimeUnit.MINUTES.toMillis(interval));
        this.guild = guild;
    }

    /**
     * Sets the interval.
     * @param interval The interval in minutes.
     */
    public void setInterval(int interval) {
        setWaitDuration((int) TimeUnit.MINUTES.toMillis(interval));
    }

    /**
     * Gets the announcement task for the guild.
     * @param guild The guild ID.
     * @return The announcement task, possibly null.
     */
    public static synchronized AnnouncementsTask getTask(long guild) {
        return tasks.get(guild);
    }

    /**
     * Adds a task.
     * @param guild The guild ID.
     */
    public static synchronized void addTask(long guild) {
        if(tasks.containsKey(guild)) {
            return;
        }
        GuildEntry entry = (GuildEntry) Category.GUILD.getEntry();
        EntryField intervalEntry = entry.getField(GuildEntry.Fields.ANNOUNCEMENT_INTERVAL);
        int interval = Integer.valueOf(entry.fetch(intervalEntry, guild, null));
        AnnouncementsTask task = new AnnouncementsTask(guild, interval);
        tasks.put(guild, task);
        task.create();
        Arraybot.INSTANCE.getLogger().info("Enabled announcements for the guild {}.", guild);
    }

    /**
     * Removes a task.
     * @param guild The guild ID.
     */
    public static synchronized void stopTask(long guild) {
        AnnouncementsTask task = tasks.get(guild);
        if(task != null) {
            task.interrupt();
            tasks.remove(guild);
            Arraybot.INSTANCE.getLogger().info("Disabled announcements for the guild {}.", guild);
        }
    }

    /**
     * When the task is executed.
     */
    @Override
    public void onTask() {
        GuildEntry guildEntry = (GuildEntry) Category.GUILD.getEntry();
        long channelId = Long.valueOf(guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.ANNOUNCEMENT_CHANNEL), guild, null));
        TextChannel channel = Arraybot.INSTANCE.getBotManager().getShardManager().getTextChannelById(channelId);
        if(channel == null
                || channel.getGuild().getIdLong() != guild
                || !channel.getGuild().getSelfMember().hasPermission(channel, Permission.MESSAGE_WRITE)) {
            stopTask(guild);
            return;
        }
        AnnouncementIdsEntry announcementIdsEntry = (AnnouncementIdsEntry) Category.ANNOUNCEMENT_IDS.getEntry();
        Set<String> idsRaw = announcementIdsEntry.values(guild);
        if(idsRaw.isEmpty()) {
            return;
        }
        TreeSet<Integer> ids = new TreeSet<>();
        for(String id : idsRaw) {
            if(UDatatypes.isInt(id)) {
                ids.add(Integer.valueOf(id));
            }
        }
        if(ids.isEmpty()) {
            return;
        }
        int min = Collections.min(ids);
        int max = Collections.max(ids);
        int last = Integer.valueOf(guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.ANNOUNCEMENT_LAST_ID), guild, null));
        int id;
        if(last == UDefaults.DEFAULT_ID
                || last >= max) {
            id = min;
        } else {
            id = ids.stream()
                    .filter(integer -> integer > last)
                    .findFirst()
                    .orElse(min);
        }
        guildEntry.push(guildEntry.getField(GuildEntry.Fields.ANNOUNCEMENT_LAST_ID), guild, null, id);
        AnnouncementEntry announcementEntry = (AnnouncementEntry) Category.ANNOUNCEMENT.getEntry();
        String text = announcementEntry.fetch(announcementEntry.getField(AnnouncementEntry.Fields.ANNOUNCEMENT), guild, id);
        if(text.equals(UDefaults.DEFAULT_STRING)) {
            return;
        }
        channel.sendMessage(text).queue();
    }

}
