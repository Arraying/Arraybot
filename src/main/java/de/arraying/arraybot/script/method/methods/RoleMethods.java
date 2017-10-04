package de.arraying.arraybot.script.method.methods;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.script.method.templates.EntityCollectionMethods;
import de.arraying.arraybot.util.UZeus;
import de.arraying.zeus.backend.annotations.ZeusMethod;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.exceptions.PermissionException;

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
public final class RoleMethods extends EntityCollectionMethods<Role> {

    /**
     * Creates a new method collection object.
     * @param environment The command environment.
     */
    public RoleMethods(CommandEnvironment environment) {
        super(environment, "roles");
    }

    /**
     * Adds a role to a user.
     * @param user The user ID.
     * @param role The role ID.
     */
    @ZeusMethod
    public void role_add(Long user, Long role) {
        manageRole(user, role, true);
    }

    /**
     * Removes a role from a user.
     * @param user The user ID.
     * @param role The role ID.
     */
    @ZeusMethod
    public void role_remove(Long user, Long role) {
        manageRole(user, role, false);
    }

    /**
     * Gets all roles by name.
     * @param name The name.
     * @param ignoreCase Whether to ignore the case.
     * @return The key.
     */
    @ZeusMethod
    public String roles_find(String name, Boolean ignoreCase) {
        return internalNew(environment.getGuild().getRolesByName(name, ignoreCase));
    }

    /**
     * Gets all roles for the specified user.
     * @param id The user.
     * @return The key or "invalid" if the user is invalid.
     */
    @ZeusMethod
    public String roles_user(Long id) {
        Member user = environment.getGuild().getMemberById(id);
        if(user == null) {
            return "invalid";
        }
        return internalNew(user.getRoles());
    }

    /**
     * Gets the size of the roles.
     * @param key The key.
     * @return The number of roles, or -1 if the key is invalid.
     */
    @ZeusMethod
    public Integer roles_length(String key) {
        return length(key);
    }

    /**
     * Gets a role from the collection of roles.
     * @param key The key.
     * @param index The index.
     * @return The role ID or -1 if the key/index is invalid.
     */
    @ZeusMethod
    public Long roles_get(String key, Integer index) {
        return get(key, index);
    }

    /**
     * Manages the a role.
     * @param user The user.
     * @param role The role.
     * @param add True = add role, false = remove role.
     */
    private void manageRole(Long user, Long role, Boolean add) {
        try {
            Guild guild = environment.getGuild();
            Member member = guild.getMemberById(user);
            Role roleObject = guild.getRoleById(role);
            if(member == null
                    || roleObject == null) {
                return;
            }
            if(add) {
                guild.getController().addSingleRoleToMember(member, roleObject).queue();
            } else {
                guild.getController().removeSingleRoleFromMember(member, roleObject).queue();
            }
        } catch(PermissionException | IllegalArgumentException exception) {
            UZeus.errorInChannel(environment.getChannel(), exception);
        }
    }

}
