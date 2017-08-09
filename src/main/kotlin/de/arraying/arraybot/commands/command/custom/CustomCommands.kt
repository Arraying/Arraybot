package de.arraying.arraybot.commands.command.custom

import com.besaba.revonline.pastebinapi.impl.factory.PastebinFactory
import com.besaba.revonline.pastebinapi.paste.PasteExpire
import com.besaba.revonline.pastebinapi.paste.PasteVisiblity
import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.cache.Cache
import de.arraying.arraybot.cache.storage.CustomCommandDataStorage
import de.arraying.arraybot.cache.storage.DataStorage
import de.arraying.arraybot.commands.command.custom.entities.CustomCommandPermission
import de.arraying.arraybot.commands.command.custom.entities.CustomCommandSyntax
import de.arraying.arraybot.commands.command.custom.entities.CustomCommandTypes
import de.arraying.arraybot.commands.command.custom.parameters.CustomCommandParameter
import de.arraying.arraybot.core.iface.ICustomCommandType
import de.arraying.arraybot.core.language.Messages
import net.dv8tion.jda.core.entities.TextChannel
import org.apache.commons.io.IOUtils
import org.json.JSONObject
import java.net.URL
import java.nio.charset.Charset
import java.util.regex.Pattern

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
object CustomCommands {

    internal val storage = DataStorage<CustomCommandDataStorage>()
    internal val types = LinkedHashMap<CustomCommandTypes, ICustomCommandType>()
    internal val parameters = LinkedHashMap<String, CustomCommandParameter>()
    private val pastebinfactory = PastebinFactory()
    private val pastebin = pastebinfactory.createPastebin(Arraybot.instance.configuration.keyPastebin)
    private val urlRegex = Pattern.compile("^(http(s)?://pastebin\\.com/raw/[0-9a-zA-Z]+)$")

    /**
     * Exports a custom command to Pastebin.
     */
    fun export(command: CustomCommand): String {
        val commandObject = JSONObject()
                .put("name", command.name)
                .put("syntax", command.syntax.toString())
                .put("permission", command.permission.value)
                .put("type", command.type.toString())
                .put("value", command.value)
        if(command.description != null) {
            commandObject.put("description", command.description)
        } else {
            commandObject.put("description", JSONObject.NULL)
        }
        val pasteBuilder = pastebinfactory.createPaste()
        pasteBuilder.setTitle("Custom ICommand Export")
        pasteBuilder.setRaw(commandObject.toString())
        pasteBuilder.setMachineFriendlyLanguage("json")
        pasteBuilder.setVisiblity(PasteVisiblity.Unlisted)
        pasteBuilder.setExpire(PasteExpire.Never)
        val paste = pasteBuilder.build()
        val response = pastebin.post(paste)
        if(response.hasError()) {
            throw IllegalStateException("Pastebin returned an error: ${response.error}.")
        }
        return response.get()
    }

    /**
     * Imports a custom command from Pastebin.
     */
    fun import(channel: TextChannel, url: String) {
        val matcher = urlRegex.matcher(url)
        if(!matcher.find()) {
            Messages.COMMAND_CUSTOM_IMPORT_URL.send(channel).queue()
            return
        }
        val urlObject  = URL(url)
        try {
            val commandObject = JSONObject(IOUtils.toString(urlObject, Charset.forName("utf-8")))
            if(!commandObject.has("name")
                    || commandObject.getString("name") == null
                    || !commandObject.has("syntax")
                    || commandObject.getString("syntax") == null
                    || !commandObject.has("permission")
                    || commandObject.getString("permission") == null
                    || !commandObject.has("type")
                    || commandObject.getString("type") == null
                    || !commandObject.has("description")
                    || !commandObject.has("value")
                    || commandObject.getString("value") == null) {
                Messages.COMMAND_CUSTOM_IMPORT_INVALID.send(channel).queue()
                return
            }
            val cache = Cache.guilds[channel.guild.idLong]!!
            val name = commandObject.getString("name".toLowerCase())
            val description: String? = if(!commandObject.isNull("description")) {
                            commandObject.getString("description")
                        } else {
                            null
                        }
            if(cache.customCommands.containsKey(name)) {
                Messages.COMMAND_CUSTOM_CREATE_EXISTS.send(channel).queue()
                return
            }
            Arraybot.instance.managerSql.createCustomCommand(channel.guild.idLong,
                    name,
                    CustomCommandSyntax.fromString(commandObject.getString("syntax")),
                    CustomCommandPermission(commandObject.getString("permission")),
                    CustomCommandTypes.fromString(commandObject.getString("type")),
                    description,
                    commandObject.getString("value"))
            Messages.COMMAND_CUSTOM_IMPORT.send(channel).queue()
        } catch(exception: Exception) {
            Messages.COMMAND_CUSTOM_IMPORT_INVALID.send(channel).queue()
        }
    }

}