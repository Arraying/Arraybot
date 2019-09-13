package de.arraying.arraybot.punishment.punishments;

import de.arraying.arraybot.punishment.Punishment;
import de.arraying.arraybot.util.objects.Pair;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.exceptions.PermissionException;

/**
 * Copyright 2017 Arraying
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
public final class KickPunishment implements Punishment {

    /**
     * Kicks the user.
     * @param guild The guild where the punishment is to occur.
     * @param punishedId The ID of the punished user.
     * @param reason The reason for the punishment.
     * @return A pair of success and whether the punishment needs to be revoked.
     */
    @Override
    public Pair<Boolean, Boolean> punish(Guild guild, long punishedId, String reason) {
        try {
            guild.kick(String.valueOf(punishedId), reason).queue();
            return new Pair<>(true, false);
        } catch(PermissionException | IllegalArgumentException exception) {
            return new Pair<>(false, false);
        }
    }

    /**
     * Revokes the punishment.
     * @param guild The guild where the punishment occurred.
     * @param punishedId The ID of the punishment.
     * @return True if the revocation was successful, false otherwise.
     */
    @Override
    public boolean revoke(Guild guild, long punishedId) {
        return true;
    }

}
