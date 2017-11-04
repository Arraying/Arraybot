package de.arraying.arraybot.script.method.methods;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.language.Message;
import de.arraying.arraybot.script.method.templates.EntityCollectionMethods;
import de.arraying.arraybot.util.Limits;
import de.arraying.arraybot.util.UZeus;
import de.arraying.zeus.backend.annotations.ZeusMethod;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.GuildUnavailableException;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.ArrayList;
import java.util.List;

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
@SuppressWarnings("unused")
public final class UserMethods extends EntityCollectionMethods<User> {

    /**
     * Creates a new method collection object.
     * @param environment The command environment.
     */
    public UserMethods(CommandEnvironment environment) {
        super(environment, "users");
    }

    /**
     * Bans a user.
     * @param id The ID of the user.
     * @param reason The reason.
     */
    @ZeusMethod
    public void user_ban(Long id, String reason) {
        punish(id, reason, true);
    }

    /**
     * Kicks a user.
     * @param id The ID of the user.
     * @param reason The reason.
     */
    @ZeusMethod
    public void user_kick(Long id, String reason) {
        punish(id, reason, false);
    }

    /**
     * Whether or not a user has a role.
     * @param id The ID of the user.
     * @param role The role ID.
     * @return True if they do, false if they don't or the user/role ID is invalid.
     */
    @ZeusMethod
    public Boolean user_has_role(Long id, Long role) {
        Member member = environment.getGuild().getMemberById(id);
        return member != null && member.getRoles().stream().anyMatch(r -> r.getIdLong() == role);
    }

    /**
     * Whether or not an ID is a user.
     * @param id The ID.
     * @return True if it is, false otherwise,
     */
    @ZeusMethod
    public Boolean is_user(Long id) {
        User user = environment.getGuild().getJDA().getUserById(id);
        return user != null && environment.getGuild().isMember(user);
    }

    /**
     * Gets all users by name.
     * @param name The name.
     * @param ignoreCase Whether or not to ignore the case.
     * @return A key.
     */
    @ZeusMethod
    public String users_find(String name, Boolean ignoreCase) {
        List<User> users = new ArrayList<>();
        for(Member member : environment.getGuild().getMembersByEffectiveName(name, ignoreCase)) {
            users.add(member.getUser());
        }
        return internalNew(users);
    }

    /**
     * Gets the size of the roles.
     * @param key The key.
     * @return The size or -1 if the key is invalid.
     */
    @ZeusMethod
    public Integer users_length(String key) {
        return length(key);
    }

    /**
     * Gets a user from the collection of users.
     * @param key The key.
     * @param index The index.
     * @return The user ID, or -1 if the key/index are invalid.
     */
    @ZeusMethod
    public Long users_get(String key, Integer index) {
        return get(key, index);
    }

    /**
     * Punishes a user.
     * @param id The ID of the user.
     * @param reason The punishment reason.
     * @param ban True = ban, false = kick.
     */
    private void punish(Long id, String reason, Boolean ban) {
        Member member = environment.getGuild().getMemberById(id);
        TextChannel channel = environment.getChannel();
        if(member == null) {
            return;
        }
        if(reason.length() > Limits.REASON.getLimit()) {
            Message.ZEUS_ERROR_BAN_REASON_LENGTH.send(channel, Limits.REASON.asString()).queue();
            return;
        }
        try {
            if(ban) {
                member.getGuild().getController().ban(String.valueOf(id), 0, reason).queue();
            } else {
                member.getGuild().getController().kick(String.valueOf(id), reason).queue();
            }
        } catch(PermissionException | IllegalArgumentException | GuildUnavailableException exception) {
            UZeus.errorInChannel(environment.getChannel(), exception);
        }
    }

}
