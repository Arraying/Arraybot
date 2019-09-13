package de.arraying.arraybot.util

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
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
class CustomEmbedBuilder {

    companion object {

        /**
         * The maximum length for a title.
         */
        const val TITLE_MAX_LENGTH = 256

        /**
         * The maximum length for an embed value.
         */
        const val TEXT_MAX_LENGTH = 1024

        /**
         * The overall maximum length of the embed.
         */
        const val MAX_LENGTH = 6000

    }

    private val embedBuilder = EmbedBuilder()
    private val lengthPlaceholder = "[...]"
    private var totalLength = 0

    /**
     * When the wrong field adding method is invoked.
     */
    fun addField(field: MessageEmbed.Field?): CustomEmbedBuilder {
        embedBuilder.addField(field)
        return this
    }

    /**
     * Adds a field to the embed.
     */
    fun addField(name: String?, value: String?, inline: Boolean): CustomEmbedBuilder {
        val localName = if (name == null) getCorrectTitle("null") else getCorrectTitle(name)
        val localValue = if (value == null) getCorrectText("null") else getCorrectText(value)
        embedBuilder.addField(localName, localValue, inline)
        return this
    }

    fun addBlankField(inline: Boolean): CustomEmbedBuilder {
        embedBuilder.addBlankField(inline)
        return this
    }

    /**
     * Sets the author.
     */
    fun setAuthor(name: String?, url: String?, iconUrl: String?): CustomEmbedBuilder {
        val localName = if (name == null) getCorrectTitle("null") else getCorrectTitle(name)
        embedBuilder.setAuthor(localName, url, iconUrl)
        return this
    }

    /**
     * Sets the colour.
     */
    fun setColor(color: Color?): CustomEmbedBuilder {
        val localColour = color ?: Color.RED
        embedBuilder.setColor(localColour)
        return this
    }

    /**
     * Sets the description.
     */
    fun setDescription(description: CharSequence?): CustomEmbedBuilder {
        val localDescription = if (description == null) getCorrectText("null") else getCorrectText(description.toString())
        embedBuilder.setDescription(localDescription)
        return this
    }

    /**
     * Sets the title
     */
    fun setTitle(title: String?): CustomEmbedBuilder {
        return setTitle(title, null)
    }

    /**
     * Sets the title.
     */
    fun setTitle(title: String?, url: String?): CustomEmbedBuilder {
        val localTitle = if (title == null) getCorrectTitle("null") else getCorrectTitle(title)
        embedBuilder.setTitle(localTitle, url)
        return this
    }

    /**
     * Sets the icon.
     */
    fun setThumbnail(icon: String): CustomEmbedBuilder {
        embedBuilder.setThumbnail(icon)
        return this
    }

    /**
     * Sets the footer.
     */
    fun setFooter(text: String?, iconUrl: String?): CustomEmbedBuilder {
        val localText = if (text == null) getCorrectText("null") else getCorrectText(text)
        embedBuilder.setFooter(localText, iconUrl)
        return this
    }

    /**
     * Builds this embed builder.
     */
    fun build(): MessageEmbed {
        return embedBuilder.build()
    }

    /**
     * Substrings the title so it cannot be bigger than the character limit.
     */
    private fun getCorrectTitle(title: String): String {
        var localTitle =
                if (title.length > TITLE_MAX_LENGTH)
                    title.substring(0, TITLE_MAX_LENGTH - lengthPlaceholder.length) + lengthPlaceholder
                else
                    title
        if (totalLength + localTitle.length > MAX_LENGTH) {
            localTitle = localTitle.substring(0, MAX_LENGTH - lengthPlaceholder.length) + lengthPlaceholder
        }
        totalLength += localTitle.length
        if (totalLength >= MAX_LENGTH) {
            val difference = totalLength - MAX_LENGTH
            localTitle = localTitle.substring(0, difference)
        }
        return localTitle
    }

    /**
     * Substrings the value so it cannot be bigger than the character limit.
     */
    private fun getCorrectText(value: String): String {
        var localValue =
                if (value.length > TEXT_MAX_LENGTH)
                    value.substring(0, TEXT_MAX_LENGTH - lengthPlaceholder.length) + lengthPlaceholder
                else
                    value
        if (totalLength + localValue.length > MAX_LENGTH) {
            localValue = localValue.substring(0, MAX_LENGTH - lengthPlaceholder.length) + lengthPlaceholder
        }
        totalLength += localValue.length
        if (totalLength >= MAX_LENGTH) {
            val difference = totalLength - MAX_LENGTH
            localValue = localValue.substring(0, difference)
        }
        return localValue
    }

}