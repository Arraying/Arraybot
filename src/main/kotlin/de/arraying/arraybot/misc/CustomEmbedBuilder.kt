package de.arraying.arraybot.misc

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageEmbed
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
class CustomEmbedBuilder : EmbedBuilder() {

    companion object {

        /**
         * The maximum length for a title.
         */
        val TITLE_MAX_LENGTH = 256

        /**
         * The maximum length for an embed value.
         */
        val TEXT_MAX_LENGTH = 2048

        /**
         * The overall maximum length of the embed.
         */
        val MAX_LENGTH = 4000

    }

    private val lengthPlaceholder = "[...]"
    var totalLength = 0

    /**
     * When the wrong field adding method is invoked.
     */
    override fun addField(field: MessageEmbed.Field?): CustomEmbedBuilder {
        return this
    }

    /**
     * Adds a filed to the embed.
     */
    override fun addField(name: String?, value: String?, inline: Boolean): CustomEmbedBuilder {
        val localName = if(name == null) getCorrectTitle("null") else getCorrectTitle(name)
        val localValue = if(value == null) getCorrectText("null") else getCorrectText(value)
        super.addField(localName, localValue, inline)
        return this
    }

    /**
     * When the description is appended to.
     */
    override fun appendDescription(description: CharSequence?): CustomEmbedBuilder {
        return this
    }

    /**
     * Sets the author.
     */
    override fun setAuthor(name: String?, url: String?, iconUrl: String?): CustomEmbedBuilder {
        val localName = if(name == null) getCorrectTitle("null") else getCorrectTitle(name)
        super.setAuthor(localName, url, iconUrl)
        return this
    }

    /**
     * Sets the colour.
     */
    override fun setColor(color: Color?): CustomEmbedBuilder {
        val localColour = color ?: Color.RED
        super.setColor(localColour)
        return this
    }

    /**
     * Sets the description.
     */
    override fun setDescription(description: CharSequence?): CustomEmbedBuilder {
        val localDescription = if(description == null) getCorrectText("null") else getCorrectText(description.toString())
        super.setDescription(localDescription)
        return this
    }

    /**
     * Sets the title
     */
    override fun setTitle(title: String?): CustomEmbedBuilder {
        return setTitle(title, null)
    }

    /**
     * Sets the title.
     */
    override fun setTitle(title: String?, url: String?): CustomEmbedBuilder {
        val localTitle = if(title == null) getCorrectTitle("null") else getCorrectTitle(title)
        super.setTitle(localTitle, url)
        return this
    }

    /**
     * Sets the footer.
     */
    override fun setFooter(text: String?, iconUrl: String?): CustomEmbedBuilder {
        val localText = if(text == null) getCorrectText("null") else getCorrectText(text)
        super.setFooter(localText, iconUrl)
        return this
    }

    /**
     * Substrings the title so it cannot be bigger than the character limit.
     */
    private fun getCorrectTitle(title: String): String {
        var localTitle =
                if(title.length > TITLE_MAX_LENGTH)
                    title.substring(0, TITLE_MAX_LENGTH -lengthPlaceholder.length) + lengthPlaceholder
                else
                    title
        if(totalLength + localTitle.length > MAX_LENGTH) {
            localTitle = localTitle.substring(0, MAX_LENGTH -lengthPlaceholder.length) + lengthPlaceholder
        }
        totalLength += localTitle.length
        if(totalLength >= MAX_LENGTH) {
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
                if(value.length > TEXT_MAX_LENGTH)
                    value.substring(0, TEXT_MAX_LENGTH -lengthPlaceholder.length) + lengthPlaceholder
                else
                    value
        if(totalLength + localValue.length > MAX_LENGTH) {
            localValue = localValue.substring(0, MAX_LENGTH -lengthPlaceholder.length) + lengthPlaceholder
        }
        totalLength += localValue.length
        if(totalLength >= MAX_LENGTH) {
            val difference = totalLength - MAX_LENGTH
            localValue = localValue.substring(0, difference)
        }
        return localValue
    }

}