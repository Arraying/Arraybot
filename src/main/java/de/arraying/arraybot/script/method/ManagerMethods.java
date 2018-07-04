package de.arraying.arraybot.script.method;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.script.entity.ScriptRole;
import de.arraying.arraybot.script.entity.ScriptUser;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.managers.GuildController;

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
public final class ManagerMethods {

    private final CommandEnvironment environment;

    /**
     * Creates a manager environment.
     * @param environment The environment.
     */
    public ManagerMethods(CommandEnvironment environment) {
        this.environment = environment;
    }

    /**
     * Adds a role to the user.
     * @param user The user.
     * @param role The role.
     * @return True if successful, false otherwise.
     */
    public boolean addRole(ScriptUser user, ScriptRole role) {
        return manageRole(user, role, true);
    }

    /**
     * Removes a role from the user.
     * @param user The user.
     * @param role The role.
     * @return True if successful, false otherwise.
     */
    public boolean removeRole(ScriptUser user, ScriptRole role) {
        return manageRole(user, role, false);
    }

    /**
     * Kicks a user.
     * @param user The user.
     * @return True if successful, false otherwise.
     */
    public boolean kick(ScriptUser user) {
        try {
            environment.getGuild().getController().kick(user.getID()).complete();
            return true;
        } catch(Exception exception) {
            return false;
        }
    }

    /**
     * Bans a user.
     * @param user The user.
     * @return True if successful, false otherwise.
     */
    public boolean ban(ScriptUser user) {
        try {
            environment.getGuild().getController().ban(user.getID(), 0).complete();
            return true;
        } catch(Exception exception) {
            return false;
        }
    }

    /**
     * Change a user's nickname.
     * @param user The user.
     * @param nickname The new nickname.
     * @return True if successful, false otherwise.
     */
    public boolean nickname(ScriptUser user, String nickname) {
        try {
            Member member = environment.getGuild().getMemberById(user.getID());
            environment.getGuild().getController().setNickname(member, nickname).complete();
            return true;
        } catch(Exception exception) {
            return false;
        }
    }

    /**
     * Manages the roles of a user.
     * @param user The user.
     * @param role The role.
     * @param add True = add, false = remove.
     * @return True if successful, false otherwise.
     */
    private synchronized boolean manageRole(ScriptUser user, ScriptRole role, boolean add) {
        try {
            GuildController controller = environment.getGuild().getController();
            Member member = environment.getGuild().getMemberById(user.getID());
            Role rank = environment.getGuild().getRoleById(role.getID());
            if(add) {
                controller.addSingleRoleToMember(member, rank).complete();
            } else {
                controller.removeSingleRoleFromMember(member, rank).complete();
            }
            return true;
        } catch(Exception exception) {
            return false;
        }
    }

}
