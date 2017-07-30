package de.arraying.arraybot.core.managers

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.cache.Cache
import de.arraying.arraybot.cache.entities.*
import de.arraying.arraybot.commands.command.custom.CustomCommand
import de.arraying.arraybot.commands.command.custom.entities.CustomCommandPermission
import de.arraying.arraybot.commands.command.custom.entities.CustomCommandSyntax
import de.arraying.arraybot.commands.command.custom.entities.CustomCommandTypes
import de.arraying.arraybot.core.iface.ICache
import de.arraying.arraybot.language.Language
import de.arraying.arraybot.misc.ArraybotException
import de.arraying.arraybot.misc.SQLQuery
import de.arraying.arraybot.utils.Utils
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import net.dv8tion.jda.core.Permission
import java.math.BigInteger
import java.security.SecureRandom
import kotlin.concurrent.thread

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
class ManagerSQL {

    private val arraybot = Arraybot.instance
    private val dataSource: HikariDataSource
    private val host = arraybot.configuration.mySQLHost
    private val database = arraybot.configuration.mySQLDatabase
    private val username = arraybot.configuration.mySQLUsername
    private val password = arraybot.configuration.mySQLPassword

    enum class Table(val tableName: String) {
        GUILDS(Arraybot.instance.configuration.mySQLGuildsTable),
        COMMANDS(Arraybot.instance.configuration.mySQLCommandsTable),
        PUNISHMENTS(Arraybot.instance.configuration.mySQLPunishmentsTable),
        DISABLED(Arraybot.instance.configuration.mySQLDisabledTable),
        ANNOUNCER(Arraybot.instance.configuration.mySQLAnnouncerTable),
        ANNOUNCEMENTS(Arraybot.instance.configuration.mySQLAnnouncementsTable),
        MOD(Arraybot.instance.configuration.mySQLModTable),
        FILTER(Arraybot.instance.configuration.mySQLFilterTable),
        LOGS(Arraybot.instance.configuration.mySQLLogsTable),
        AUTOROLE(Arraybot.instance.configuration.mySQLAutoroleTable),
        BLACKLIST(Arraybot.instance.configuration.mySQLBlacklistTable),
        STATISTICS(Arraybot.instance.configuration.mySQLStatisticsTable),
        FILTER_BYPASS(Arraybot.instance.configuration.mySQLFilterBypassTable);
    }

    /**
     * Initialises some HTTP request things and connects to the DB.
     */
    init {
        try {
            val hikariConfig = HikariConfig()
            hikariConfig.jdbcUrl = "jdbc:mysql://$host:3306/$database?useSSL=false&characterEncoding=utf-8"
            hikariConfig.username = username
            hikariConfig.password = password
            hikariConfig.maximumPoolSize = 10
            hikariConfig.minimumIdle = 3
            hikariConfig.leakDetectionThreshold = 3000
            dataSource = HikariDataSource(hikariConfig)
            readyDatabase()
            loadCache()
        } catch(exception: Exception) {
            throw ArraybotException(exception.message)
        }
    }

    /**
     * Checks if the guild is synced.
     */
    fun checkSync(guild: Long) {
        if(!Utils.isCached(guild)) {
            addGuild(guild)
        } else if(Cache.guilds[guild]!!.announcer == null
                || Cache.guilds[guild]!!.mod == null
                || Cache.guilds[guild]!!.logs == null
                || Cache.guilds[guild]!!.autorole == null) {
            createSubModules(Cache.guilds[guild]!!)
        }
    }

    /**
     * Executes a guild update.
     */
    fun updateGuildTable(id: Long, field: String, value: String?) {
        launch(CommonPool) {
            SQLQuery("UPDATE ${Table.GUILDS.tableName} SET `$field`=? WHERE `id`=?", dataSource)
                    .with(value, id)
                    .update()
        }
    }

    /**
     * Executes a custom command update.
     */
    fun updateCommandsTable(id: Long, name: String, field: String, value: String?) {
        launch(CommonPool) {
            SQLQuery("UPDATE ${Table.COMMANDS.tableName} SET `$field`=? WHERE `id`=? AND `name`=?", dataSource)
                    .with(value, id, name)
                    .update()
        }
    }

    /**
     * Executes a punishment update.
     */
    fun updatePunishmentTable(id: Long, punishmentId: Long, field: String, value: Any?) {
        launch(CommonPool) {
            SQLQuery("UPDATE ${Table.PUNISHMENTS.tableName} SET `$field`=? WHERE `id`=? AND `punishment_id`=?", dataSource)
                    .with(value, id, punishmentId)
                    .update()
        }
    }

    /**
     * Executes a module update.
     */
    fun updateModuleTable(id: Long, table: Table, field: String, value: Any?) {
        launch(CommonPool) {
            SQLQuery("UPDATE ${table.tableName} SET `$field`=? WHERE `id`=?", dataSource)
                    .with(value, id)
                    .update()
        }
    }

    /**
     * Adds a guild to the database.
     */
    fun addGuild(id: Long): CGuild {
        val prefix = arraybot.configuration.botPrefix
        val secureRandom = SecureRandom()
        val password = BigInteger(120, secureRandom).toString(32)
        val language = arraybot.configuration.botDefaultLanguage
        SQLQuery("INSERT INTO ${Table.GUILDS.tableName}(`id`, `prefix`, `password`, `language`) VALUES (?, ?, ?, ?);", dataSource)
                .with(id, prefix, password, language)
                .update()
        val guild = CGuild(id, prefix, password, language)
        Cache.guilds.put(guild.id, guild)
        createSubModules(guild)
        arraybot.logger.info("The guild $id has been added.")
        return guild
    }

    /**
     * Removes the guild and its data from the database.
     */
    fun removeGuild(id: Long) {
        Table.values()
                .filter {
                    it != Table.BLACKLIST
                            && it != Table.STATISTICS
                } .forEach {
                    thread {
                        SQLQuery("DELETE FROM ${it.tableName} WHERE `id`=?", dataSource)
                                .with(id)
                                .update()
                    }
                }
    }

    /**
     * Creates a custom command.
     */
    fun createCustomCommand(id: Long, name: String) {
        createCustomCommand(id,
                name,
                CustomCommandSyntax.EQUALS,
                CustomCommandPermission(Permission.MESSAGE_WRITE.toString()),
                CustomCommandTypes.MESSAGE,
                null,
                Language.get(id, "misc.customcommand.message"))
    }

    /**
     * Creates a custom command.
     */
    fun createCustomCommand(id: Long, name: String, syntax: CustomCommandSyntax, permission: CustomCommandPermission,
                            type: CustomCommandTypes, description: String?, value: String) {
        val command = CustomCommand(id, name, syntax, permission,
                type, description, value)
        SQLQuery("INSERT INTO ${Table.COMMANDS.tableName}(`id`, `name`, `syntax`, `permission`, `type`, `description`, `value`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)", dataSource)
                .with(id, name, command.syntax.toString(), command.permission.value, command.type.toString(), command.description, command.value)
                .update()
        val guild = getGuild(id)?: return
        guild.customCommands.put(name, command)
    }

    /**
     * Deletes a custom command.
     */
    fun deleteCustomCommand(id: Long, name: String) {
        val guild = getGuild(id)?: return
        guild.customCommands.remove(name)
        SQLQuery("DELETE FROM ${Table.COMMANDS.tableName} WHERE `id`=? AND `name`=?", dataSource)
                .with(id, name)
                .update()
    }


    /**
     * Adds a punishment.
     */
    fun addPunishment(id: Long, punishmentId: Long, user: Long, userString: String, type: String,
                      staff: Long, staffString: String, expiration: Long, revoked: Boolean, reason: String): CPunishment? {
        val punishment = CPunishment(id,
                punishmentId,
                user,
                userString,
                type,
                staff,
                staffString,
                expiration,
                revoked,
                reason)
        SQLQuery("INSERT INTO ${Table.PUNISHMENTS.tableName}(`id`, `punishment_id`, `user`, `user_string`, `type`, " +
                    "`staff`, `staff_string`, `expiration`, `revoked`, `reason`) "+
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", dataSource)
                .with(id, punishmentId, user, userString, type, staff, staffString, expiration, revoked, reason)
                .update()
        val guild = getGuild(id)?: return null
        guild.punishments.put(punishmentId, punishment)
        guild.mod?.punishmentCount = punishmentId
        return punishment
    }

    /**
     * Adds a disabled command.
     */
    fun addDisabledCommand(id: Long, disabled: String): String? {
        val guild = getGuild(id)?: return null
        guild.disabled.add(disabled)
        SQLQuery("INSERT INTO ${Table.DISABLED.tableName}(`id`, `command`) VALUES (?, ?)", dataSource)
                .with(id, disabled)
                .update()
        return disabled
    }

    /**
     * Removes a disabled command.
     */
    fun removeDisabledCommand(id: Long, disabled: String) {
        val guild = getGuild(id)?: return
        guild.disabled.remove(disabled)
        SQLQuery("DELETE FROM ${Table.DISABLED.tableName} WHERE `id`=? AND `command`=?", dataSource)
                .with(id, disabled)
                .update()
    }


    /**
     * Adds an announcement.
     */
    fun addAnnouncement(id: Long, announcementId: Long, announcement: String): CAnnouncement? {
        val announcementObject = CAnnouncement(id,
                announcementId,
                announcement)
        SQLQuery("INSERT INTO ${Table.ANNOUNCEMENTS.tableName}(`id`, `announcement_id`, `announcement`) VALUES (?, ?, ?)", dataSource)
                .with(id, announcementId, announcement)
                .update()
        val guild = getGuild(id)?: return null
        guild.announcer?.announcements?.put(announcementId, announcementObject)
        guild.announcer?.announcementCount = announcementId
        return announcementObject
    }

    /**
     * Removes an announcement.
     */
    fun removeAnnouncement(id: Long, announcementId: Long) {
        val guild = getGuild(id)?: return
        guild.announcer?.announcements?.remove(announcementId)
        SQLQuery("DELETE FROM ${Table.ANNOUNCEMENTS.tableName} WHERE `id`=? AND `punishment_id`=?", dataSource)
                .with(id, announcementId)
                .update()
    }


    /**
     * Adds a filtered phrase.
     */
    fun addChatFilterPhrase(id: Long, phrase: String): String? {
        val guild = getGuild(id)?: return null
        guild.mod?.filtered?.add(phrase)
        SQLQuery("INSERT INTO ${Table.FILTER.tableName}(`id`, `phrase`) VALUES (?, ?)", dataSource)
                .with(id, phrase)
                .update()
        return phrase
    }

    /**
     * Removes a filtered phrase.
     */
    fun removeChatFilterPhrase(id: Long, phrase: String) {
        val guild = getGuild(id)?: return
        guild.mod?.filtered?.remove(phrase)
        SQLQuery("DELETE FROM ${Table.FILTER.tableName} WHERE `id`=? and `phrase`=?", dataSource)
                .with(id, phrase)
                .update()
    }

    /**
     * Adds a chat filter bypass.
     */
    fun addChatFilterBypass(id: Long, bypassId: Long, type: String, value: Long): CBypass? {
        val filterBypassObject = CBypass(id, bypassId, type, value)
        SQLQuery("INSERT INTO ${Table.FILTER_BYPASS.tableName} (`id`, `bypass_id`, `type`, `value`) VALUES (?, ?, ?, ?)", dataSource)
                .with(id, bypassId, type, value)
                .update()
        val guild = getGuild(id)?: return null
        guild.mod?.filterBypasses?.put(bypassId, filterBypassObject)
        guild.mod?.bypassCount = bypassId
        return filterBypassObject
    }

    /**
     * Removes a chat filter bypass.
     */
    fun removeChatFilterBypass(id: Long, bypassId: Long) {
        val guild = getGuild(id)?: return
        guild.mod?.filterBypasses?.remove(bypassId)
        SQLQuery("DELETE FROM ${Table.FILTER_BYPASS.tableName} WHERE `id`=? AND `bypass_id`=?", dataSource)
                .with(id, bypassId)
                .update()
    }

    /**
     * Logs the statistics.
     */
    fun logStatistics() {
        val version = arraybot.configuration.botVersion + if(arraybot.configuration.botBeta) "_BETA" else ""
        val shards = arraybot.configuration.botShards
        var guilds = 0
        var users = 0
        var channels = 0
        val messages = Cache.messagesSent
        val commands = Cache.commandsRun
        for(shard in Arraybot.instance.managerBot.shards.values) {
            guilds += shard.guilds.size
            users += shard.users.size
            channels += shard.textChannels.size + shard.voiceChannels.size
        }
        SQLQuery("INSERT INTO ${Table.STATISTICS.tableName}(`version`, `shards`, `guilds`, `users`, `channels`, `messages`, `commands`) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)", dataSource)
                .with(version, shards, guilds, users, channels, messages, commands)
                .update()
        arraybot.logger.info("Logged the statistics.")
    }

    /**
     * Checks if any modules are null, and initializes them if they are.
     */
    private fun createSubModules(guild: CGuild) {
        val id = guild.id
        if(!Utils.isCached(id)) {
            return
        }
        if(guild.announcer == null) {
            Cache.guilds[id]!!.announcer = createModule(id, Table.ANNOUNCER, CAnnouncer::class.java) as CAnnouncer
        }
        if(guild.mod == null) {
            Cache.guilds[id]!!.mod = createModule(id, Table.MOD, CMod::class.java) as CMod
        }
        if(guild.logs == null) {
            Cache.guilds[id]!!.logs = createModule(id, Table.LOGS, CLogs::class.java) as CLogs
        }
        if(guild.autorole == null) {
            Cache.guilds[id]!!.autorole = createModule(id, Table.AUTOROLE, CAutorole::class.java) as CAutorole
        }
    }

    /**
     * Creates a default module.
     */
    private fun createModule(id: Long, table: Table, type: Class<out ICache>): Any {
        SQLQuery("INSERT INTO ${table.tableName}(`id`) VALUES (?)", dataSource)
                .with(id)
                .update()
        return SQLQuery("SELECT * FROM ${table.tableName} WHERE `id`=?", dataSource)
                .with(id)
                .select(type)[0]
    }

    /**
     * Gets a cached guild, can be null.
     */
    private fun getGuild(id: Long): CGuild? {
        return Cache.guilds[id]
    }

    /**
     * Readies the database by setting up tables.
     */
    private fun readyDatabase() {
        SQLQuery("CREATE TABLE IF NOT EXISTS `${Table.GUILDS.tableName}`(" +
                        "`id` BIGINT, " +
                        "`prefix` TEXT, " +
                        "`password` TEXT, " +
                        "`language` TEXT, " +
                        "PRIMARY KEY (id));",
                dataSource)
                .update()
        SQLQuery("CREATE TABLE IF NOT EXISTS  `${Table.COMMANDS.tableName}`(" + "`id` BIGINT, " +
                        "`name` TEXT, " +
                        "`syntax` TEXT, " +
                        "`permission` TEXT, " +
                        "`type` TEXT, " +
                        "`description` TEXT, "+
                        "`value` TEXT);",
                    dataSource)
                .update()
        SQLQuery("CREATE TABLE IF NOT EXISTS `${Table.FILTER.tableName}`(" +
                           "`id` BIGINT, " +
                           "`phrase` TEXT);",
                    dataSource)
                .update()
        SQLQuery("CREATE TABLE IF NOT EXISTS `${Table.PUNISHMENTS.tableName}`(" +
                            "`id` BIGINT, " +
                            "`punishment_id` BIGINT, " +
                            "`user` BIGINT, " +
                            "`user_string` TEXT, " +
                            "`type` TEXT, " +
                            "`staff` BIGINT, " +
                            "`staff_string` TEXT, " +
                            "`expiration` BIGINT, " +
                            "`revoked` BOOLEAN DEFAULT FALSE, " +
                            "`reason` TEXT);",
                    dataSource)
                .update()
        SQLQuery("CREATE TABLE IF NOT EXISTS `${Table.DISABLED.tableName}`(" +
                            "`id` BIGINT, " +
                            "`command` TEXT);",
                    dataSource)
                .update()
        SQLQuery("CREATE TABLE IF NOT EXISTS `${Table.BLACKLIST.tableName}`(" +
                            "`id` BIGINT, " +
                            "`reason` TEXT, " +
                            "PRIMARY KEY (id));",
                    dataSource)
                .update()
        SQLQuery("CREATE TABLE IF NOT EXISTS `${Table.STATISTICS.tableName}`(" +
                            "`id` BIGINT AUTO_INCREMENT, " +
                            "`version` TEXT, " +
                            "`shards` INT, " +
                            "`guilds` BIGINT, " +
                            "`users` BIGINT, " +
                            "`channels` BIGINT, "+
                            "`messages` BIGINT, "+
                            "`commands` BIGINT, "+
                            "PRIMARY KEY (id));",
                    dataSource)
                .update()
        SQLQuery("CREATE TABLE IF NOT EXISTS `${Table.ANNOUNCER.tableName}`(" +
                            "`id` BIGINT, " +
                            "`join_announcer` BOOLEAN DEFAULT FALSE, " +
                            "`join_channel` BIGINT DEFAULT 0, " +
                            "`join_message` TEXT, " +
                            "`leave_announcer` BOOLEAN DEFAULT FALSE, " +
                            "`leave_channel` BIGINT DEFAULT 0, " +
                            "`leave_message` TEXT, " +
                            "`status_announcer` BOOLEAN DEFAULT FALSE, " +
                            "`status_channel` BIGINT DEFAULT 0, " +
                            "`announcement_announcer` BOOLEAN DEFAULT FALSE, " +
                            "`announcement_channel` BIGINT DEFAULT 0, " +
                            "`announcement_count` BIGINT DEFAULT 0, " +
                            "PRIMARY KEY (id));",
                    dataSource)
                .update()
        SQLQuery("CREATE TABLE IF NOT EXISTS `${Table.ANNOUNCEMENTS.tableName}`(" +
                            "`id` BIGINT, " +
                            "`announcement_id` BIGINT, " +
                            "`announcement` TEXT);",
                    dataSource)
                .update()
        SQLQuery("CREATE TABLE IF NOT EXISTS `${Table.AUTOROLE.tableName}`(" +
                            "`id` BIGINT, " +
                            "`enabled` BOOLEAN DEFAULT FALSE, " +
                            "`message` BOOLEAN DEFAULT FALSE, " +
                            "`action` BIGINT DEFAULT 0, " +
                            "PRIMARY KEY(id));",
                    dataSource)
                .update()
        SQLQuery("CREATE TABLE IF NOT EXISTS `${Table.LOGS.tableName}`(" +
                            "`id` BIGINT, " +
                            "`user_enabled` BOOLEAN DEFAULT FALSE, " +
                            "`user_channel` BIGINT DEFAULT 0, " +
                            "`message_enabled` BOOLEAN DEFAULT FALSE, " +
                            "`message_channel` BIGINT DEFAULT 0, " +
                            "`guild_enabled` BOOLEAN DEFAULT FALSE, " +
                            "`guild_channel` BIGINT DEFAULT 0, " +
                            "`mod_enabled` BOOLEAN DEFAULT FALSE, " +
                            "`mod_channel` BIGINT DEFAULT 0, " +
                            "PRIMARY KEY(id));",
                    dataSource)
                .update()
        SQLQuery("CREATE TABLE IF NOT EXISTS `${Table.MOD.tableName}`(" +
                            "`id` BIGINT, " +
                            "`punishment_count` BIGINT DEFAULT 0, " +
                            "`bypass_count` BIGINT DEFAULT 0, " +
                            "`filter_enabled` BOOLEAN DEFAULT FALSE, " +
                            "`filter_regex` BOOLEAN DEFAULT FALSE, " +
                            "`filter_silent` BOOLEAN DEFAULT FALSE, " +
                            "`filter_private` BOOLEAN DEFAULT FALSE, " +
                            "`filter_message` TEXT, " +
                            "`mute_role` BIGINT DEFAULT 0, " +
                            "`mute_permission` TEXT, " +
                            "PRIMARY KEY(id));",
                    dataSource)
                .update()
        SQLQuery("CREATE TABLE IF NOT EXISTS `${Table.FILTER_BYPASS.tableName}`(" +
                            "`id` BIGINT, " +
                            "`bypass_id` BIGINT, " +
                            "`type` TEXT, " +
                            "`value` BIGINT);",
                    dataSource)
                .update()
        arraybot.logger.info("Created all required MySQL tables.")
    }

    /**
     * Synchronizes the cache with the database content.
     */
    private fun loadCache() {
        arraybot.logger.info("Starting the caching process...")
        cacheGuilds()
        val caches = listOf(ManagerSQL::cacheCustomCommands,
                ManagerSQL::cachePunishments,
                ManagerSQL::cacheDisabledCommands,
                ManagerSQL::cacheRelatedAnnouncements,
                ManagerSQL::cacheRelatedModeration,
                ManagerSQL::cacheLogSettings,
                ManagerSQL::cacheAutoroleSettings,
                ManagerSQL::cacheBlacklist,
                ManagerSQL::cacheStatistics)
                .map {
                    launch(CommonPool) {
                        it.invoke(this@ManagerSQL)
                    }
                }
        runBlocking {
            caches.forEach {
                it.join()
            }
            arraybot.logger.info("Checking for uninitialized modules...")
            for(guild in Cache.guilds.values) {
                createSubModules(guild)
            }
            arraybot.logger.info("Checked for uninitialized modules.")
            arraybot.logger.info("Finished the caching process.")
        }
    }

    /**
     * Caches in all guilds in a blocking fashion.
     */
    private fun cacheGuilds() {
        arraybot.logger.info("Caching guilds...")
        SQLQuery("SELECT * FROM ${Table.GUILDS.tableName}", dataSource).select(CGuild::class.java)
                .filterIsInstance<CGuild>()
                .forEach {
                    Cache.guilds.put(it.id, it)
                }
        arraybot.logger.info("Finished caching guilds.")
    }

    /**
     * Coroutine method to cache custom commands.
     */
    private fun cacheCustomCommands() {
        arraybot.logger.info("Caching custom commands...")
        SQLQuery("SELECT * FROM ${Table.COMMANDS.tableName}", dataSource).select {
            while(it.next()) {
                val id = it.getLong("id")
                val command = CustomCommand(id,
                        it.getString("name"),
                        CustomCommandSyntax.fromString(it.getString("syntax")),
                        CustomCommandPermission(it.getString("permission")),
                        CustomCommandTypes.fromString(it.getString("type")),
                        it.getString("description"),
                        it.getString("value"))
                val guild = getGuild(id)
                guild?.customCommands?.put(command.name, command)
            }
        }
        arraybot.logger.info("Finished caching custom commands.")
    }

    /**
     * Coroutine method to cache punishments.
     */
    private fun cachePunishments() {
        arraybot.logger.info("Caching punishments...")
        SQLQuery("SELECT * FROM ${Table.PUNISHMENTS.tableName}", dataSource).select(CPunishment::class.java)
                .filterIsInstance<CPunishment>()
                .forEach {
                    val guild = getGuild(it.id)?: return
                    guild.punishments.put(it.punishmentId, it)
                }
        arraybot.logger.info("Finished caching punishments.")
    }

    /**
     * Coroutine method to cache disabled commands.
     */
    private fun cacheDisabledCommands() {
        arraybot.logger.info("Caching disabled commands...")
        SQLQuery("SELECT * FROM ${Table.DISABLED.tableName}", dataSource).select(CValue::class.java)
                .filterIsInstance<CValue>()
                .forEach {
                    val guild = getGuild(it.id)?: return
                    guild.disabled.add(it.value)
                }
        arraybot.logger.info("Finished caching disabled commands.")
    }

    /**
     * Coroutine method to cache announcer settings and announcements.
     */
    private fun cacheRelatedAnnouncements() {
        cacheAnnouncerSettings()
        cacheAnnouncements()
    }

    /**
     * Coroutine method to cache announcer settings.
     */
    private fun cacheAnnouncerSettings() {
        arraybot.logger.info("Caching announcer settings...")
        SQLQuery("SELECT * FROM ${Table.ANNOUNCER.tableName}", dataSource).select(CAnnouncer::class.java)
                .filterIsInstance<CAnnouncer>()
                .forEach {
                    val guild = getGuild(it.id)?: return
                    guild.announcer = it
                }
        arraybot.logger.info("Finished caching announcer settings...")
    }


    /**
     * Coroutine method to cache announcements.
     */
    private fun cacheAnnouncements() {
        arraybot.logger.info("Caching announcements...")
        SQLQuery("SELECT * FROM ${Table.ANNOUNCEMENTS.tableName}", dataSource).select(CAnnouncement::class.java)
                .filterIsInstance<CAnnouncement>()
                .forEach {
                    val guild = getGuild(it.id)?: return
                    guild.announcer?.announcements?.put(it.announcementId, it)
                }
        arraybot.logger.info("Finished caching announcements.")
    }

    /**
     * Coroutine method to cache mod settings and filtered phrases.
     */
    private fun cacheRelatedModeration() {
        cacheModSettings()
        cacheFilteredPhrases()
        cacheFilterBypasses()
    }

    /**
     * Coroutine method to cache mod settings.
     */
    private fun cacheModSettings() {
        arraybot.logger.info("Caching mod settings...")
        SQLQuery("SELECT * FROM ${Table.MOD.tableName}", dataSource).select(CMod::class.java)
                .filterIsInstance<CMod>()
                .forEach {
                    val guild = getGuild(it.id)?: return
                    guild.mod = it
                }
        arraybot.logger.info("Finished caching mod settings.")
    }

    /**
     * Coroutine method to cache filtered phrases.
     */
    private fun cacheFilteredPhrases() {
        arraybot.logger.info("Caching filtered phrases...")
        SQLQuery("SELECT * FROM ${Table.FILTER.tableName}", dataSource).select(CValue::class.java)
                .filterIsInstance<CValue>()
                .forEach {
                    val guild = getGuild(it.id)?: return
                    guild.mod?.filtered?.add(it.value)
                }
        arraybot.logger.info("Finished caching filtered phrases.")
    }

    /**
     * Coroutine method to cache filter bypasses.
     */
    private fun cacheFilterBypasses() {
        arraybot.logger.info("Caching filter bypasses...")
        SQLQuery("SELECT * FROM ${Table.FILTER_BYPASS.tableName}", dataSource).select(CBypass::class.java)
                .filterIsInstance<CBypass>()
                .forEach {
                    val guild = getGuild(it.id)?: return
                    guild.mod?.filterBypasses?.put(it.bypassId, it)
                }
        arraybot.logger.info("Finished caching filter bypasses.")
    }


    /**
     * Coroutine method to cache log settings.
     */
    private fun cacheLogSettings() {
        arraybot.logger.info("Caching log settings...")
        SQLQuery("SELECT * FROM ${Table.LOGS.tableName}", dataSource).select(CLogs::class.java)
                .filterIsInstance<CLogs>()
                .forEach {
                    val guild = getGuild(it.id)?: return
                    guild.logs = it
                }
        arraybot.logger.info("Finished caching log settings.")
    }

    /**
     * Coroutine method to cache autorole settings.
     */
    private fun cacheAutoroleSettings() {
        arraybot.logger.info("Caching autorole settings...")
        SQLQuery("SELECT * FROM ${Table.AUTOROLE.tableName}", dataSource).select(CAutorole::class.java)
                .filterIsInstance<CAutorole>()
                .forEach {
                    val guild = getGuild(it.id)?: return
                    guild.autorole = it
                }
        arraybot.logger.info("Finished caching autorole settings.")
    }

    /**
     * Coroutine method to cache blacklist.
     */
    private fun cacheBlacklist() {
        arraybot.logger.info("Caching blacklist...")
        SQLQuery("SELECT * FROM ${Table.BLACKLIST.tableName}", dataSource).select(CValue::class.java)
                .filterIsInstance<CValue>()
                .forEach {
                    Cache.blacklist.put(it.id, it.value)
                }
        arraybot.logger.info("Finished caching blacklist.")
    }

    /**
     * Coroutine method to cache statistics.
     */
    private fun cacheStatistics() {
        arraybot.logger.info("Caching statistics...")
        val statisticsArray = SQLQuery("SELECT * FROM ${Table.STATISTICS.tableName} ORDER BY id DESC", dataSource).select(CStatistics::class.java)
        if(statisticsArray.isNotEmpty()
                && statisticsArray[0] is CStatistics) {
            val statistics = statisticsArray[0] as CStatistics
            Cache.messagesSent = statistics.messages
            Cache.commandsRun = statistics.commands
        }
        arraybot.logger.info("Finished caching statistics.")
    }

}