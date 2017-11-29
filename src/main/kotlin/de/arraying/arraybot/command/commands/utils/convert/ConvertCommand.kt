package de.arraying.arraybot.command.commands.utils.convert

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.UDatatypes
import de.arraying.kotys.JSON
import net.dv8tion.jda.core.Permission
import okhttp3.OkHttpClient
import okhttp3.Request
import java.awt.Color

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
class ConvertCommand: DefaultCommand("convert",
        CommandCategory.UTILS,
        Permission.MESSAGE_WRITE) {

    /**
     * When the command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        if(args.size < 2) {
            Message.COMMANDS_CONVERT_PROVIDE_FROM.send(channel).queue()
            return
        }
        val from = args[1]
        when(from.toLowerCase()) {
            "rgb" -> {
                if(args.size < 3) {
                    Message.COMMANDS_CONVERT_PROVIDE_VALUE.send(channel).queue()
                    return
                }
                val value = args[2]
                if(!value.startsWith("#")) {
                    Message.COMMANDS_CONVERT_VALUE_INVALID.send(channel).queue()
                    return
                }
                try {
                    val colour = Color.decode(value)
                    channel.sendMessage("R: ${colour.red}, G: ${colour.green}, B: ${colour.blue}").queue()
                } catch(exception: NumberFormatException) {
                    Message.COMMANDS_CONVERT_VALUE_INVALID.send(channel).queue()
                }
            }
            "hex" -> {
                if(args.size < 3) {
                    Message.COMMANDS_CONVERT_PROVIDE_VALUE.send(channel).queue()
                    return
                }
                val value = args[2]
                val value2 = if(args.size < 4) null else args[3]
                val value3 = if(args.size < 5) null else args[4]
                if(value2 == null
                        || value3 == null) {
                    Message.COMMANDS_CONVERT_PROVIDE_VALUE.send(channel).queue()
                    return
                }
                if(UDatatypes.isInt(value)
                        && UDatatypes.isInt(value2)
                        && UDatatypes.isInt(value3)) {
                    val r = value.toInt()
                    val g = value2.toInt()
                    val b = value3.toInt()
                    val colour = Color(r, g, b)
                    channel.sendMessage("#${Integer.toHexString(colour.rgb).toUpperCase().substring(2)}").queue()
                } else {
                    Message.COMMANDS_CONVERT_VALUE_INVALID.send(channel).queue()
                }
            }
            else -> {
                if(args.size < 3) {
                    Message.COMMANDS_CONVERT_PROVIDE_TO.send(channel).queue()
                    return
                }
                val to = args[2].toUpperCase()
                val amount = if(args.size < 4) null else args[3]
                val request = Request.Builder()
                        .url("https://api.fixer.io/latest?base=$from")
                        .build()
                val response = OkHttpClient().newCall(request).execute()
                val json = JSON(response.body()!!.string())
                if(json.has("error")) {
                    Message.COMMANDS_CONVERT_MONEY_INVALID.send(channel).queue()
                    return
                }
                val multiplier: Double = if(amount != null) {
                    val result: Double? = UDatatypes.toDouble(amount)
                    if(result != null) {
                        result
                    } else {
                        Message.COMMANDS_CONVERT_MONEY_NUMBER.send(channel).queue()
                        return
                    }
                } else {
                    1.0
                }
                if(!json.json("rates").has(to)) {
                    Message.COMMANDS_CONVERT_MONEY_INVALID.send(channel).queue()
                    return
                }
                val value = multiplier * json.json("rates").decimal(to)
                Message.COMMANDS_CONVERT_MONEY_RESULT.send(channel, value.toString()).queue()
            }
        }
    }

}