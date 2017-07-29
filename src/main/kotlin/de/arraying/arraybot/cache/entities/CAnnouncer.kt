package de.arraying.arraybot.cache.entities

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.iface.ICache
import de.arraying.arraybot.managers.ManagerSQL

/**
 * Copyright 2017 Arraying
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
class CAnnouncer(val id: Long,
                 joinAnnouncer: Boolean,
                 joinChannel: Long,
                 joinMessage: String?,
                 leaveAnnouncer: Boolean,
                 leaveChannel: Long,
                 leaveMessage: String?,
                 statusAnnouncer: Boolean,
                 statusChannel: Long,
                 announcementAnnouncer: Boolean,
                 announcementChannel: Long,
                 announcementCount: Long):
        ICache {
    
    private val arraybot = Arraybot.instance
    val announcements = HashMap<Long, CAnnouncement>()

    /**
     * Whether or not join messages should be announced.
     */
    var joinAnnouncer = joinAnnouncer
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.ANNOUNCER, "join_announcer", value)
        }

    /**
     * The channel to send join messages in (ID).
     */
    var joinChannel = joinChannel
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.ANNOUNCER, "join_channel", value)
        }

    /**
     * The join message.
     */
    var joinMessage = joinMessage
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.ANNOUNCER, "join_message", value)
        }

    /**
     * Whether or not leave messages should be announced.
     */
    var leaveAnnouncer = leaveAnnouncer
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.ANNOUNCER, "leave_announcer", value)
        }

    /**
     * The channel to send leave messages in (ID).
     */
    var leaveChannel = leaveChannel
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.ANNOUNCER, "leave_channel", value)
        }

    /**
     * The leave message.
     */
    var leaveMessage = leaveMessage
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.ANNOUNCER, "leave_message", value)
        }

    /**
     * Whether or not Minecraft status changes should be announced.
     */
    var statusAnnouncer = statusAnnouncer
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.ANNOUNCER, "status_announcer", value)
        }

    /**
     * The channel to send Minecraft status announcements in (ID).
     */
    var statusChannel = statusChannel
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.ANNOUNCER, "status_channel", value)
        }

    /**
     * Whether or not announcements should be announced.
     */
    var announcementAnnouncer = announcementAnnouncer
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.ANNOUNCER, "announcement_announcer", value)
        }

    /**
     * The channel to send announcements in (ID).
     */
    var announcementChannel = announcementChannel
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.ANNOUNCER, "announcement_channel", value)
        }

    /**
     * The ID of the latest announcement, used to create unique announcement IDs for management.
     */
    var announcementCount = announcementCount
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.ANNOUNCER, "announcement_count", value)
        }
    
}