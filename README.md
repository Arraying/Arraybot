# **Arraybot**


###A simple bot with a lot of features.
Parameters in () are required; [] are optional.


##Moderation commands



* //clear (#)- Clears # amount of messages. Values are from 1-50.
Example: //clear 19

* //demote (@user) [@user] - Demotes the mentioned user(s).
Add as many users as required.
The bot can only demote those who are lower roles than it.
Example: //demote @Arraying @ipr0james

* //kick (@user) [@user] - Kicks the mentioned user(s).
Add as many users as required.
The bot can only kick those who are lower roles than it.
Example: //kick @LuketehPanda

* //ban (@user) [@user] - Bans the mentioned user(s).
Add as many users as required.
The bot can only ban those who are lower roles than it.
Example: //ban @DumplingsWithToads @potato



##Utility commands



* //broadcast (message) - Broadcasts the message to all users.
This will send the users of the guild a PM.
It will also show in the channel you executed the command.
Example: //broadcast Our server has released! mc.crap.server!

* //search (query) - Searches for guilds/users/channels/roles that contain the query.
This is mainly used to obtain the ID of them.
You will need the role "Arraybot Search".
Example: //search Arraybot

* //msg (id) - Messages a text channel/user using IDs.
This is mainly used to message other guilds.
You will need the role "Arraybot Message".
Example: //msg 115134128717955080 OMG, I'm a fan!

* //guildinfo - Displays information about the guild.
This will display some guild info.
It also displays guild roles.
Example: //guildinfo

* //userinfo [@mention] - This will show info about the mentioned user(s) or yourself.
This will display the ID as well.
It also displays roles.
Example: //userinfo @Amber

* //createt (name) - Create a text channel.
The name specified will be the name of the channel.
Spaces are replaced with underscores.
Example: //createt bot spam

* //createv (name) - Create a voice channel.
The name specified will be the name of the channel.
Spaces are not replaced.
Example: //createv Scrabble

* //checkthis - Checks if the guild is registered in the database.
If not, it will add the guild.
There are no parameters.
Example: //checkthis



##Fun commands



* //say (message) - Make Arraybot say something.
The bot will repeat the message.
It requires the role "Arraybot Say".
Example: //say I'm supid!

* //8ball (question) - Ask the 8Ball for answers.
There are 5 answers, they are chosen randomly.
Only yes and no questions work well with this.
Example: //8ball will I have a beard?

* //skin (name) - Displays the Minecraft skin of (name).
If the user is not found, it will display steve.
Make sure to have embeded images enabled.
Example: //skin Arraying

* //stealskin (name) - Steal the Minecraft skin of (name).
If the user is not found, it will display steve.
The bot will PM you the skin which you can save and upload.
Example: //stealskin Arraying

* //rand (a) (z) - Create a random number between (a) and (z).
Use this to check how well the bot is responding.
Note: No number is truly random. Only humans can do that.
Example: //rand 21 69

* //1v1 (name) - 1v1 someone called (name).
The person does not have to be a discord user or mentioned.
The winner will be announced in chat.
Example: //1v1 ChuckNorris

* //cat - Displays a random cat.
Meow. Meow. MEOW!
Meooooooooowwww!!!!!!???
Example: //cat



##Customisation commands



* //prefix [new prefix] - Shows the current prefix or sets a new one.
All commands work with //, too.
The prefix cannot contain spaces (yet).
Example: //prefix ~

* //joinleavemsg - Toggles the messages for join or leave.
This is per guild.
You cannot enable/disable one of them.
Example: //joinleavemsg

* //joinmsg [message] - Shows or sets the join message.
The current placeholders are: {user} && {guild}
Seperate multiple messages with a semi-colon.
Example: //joinmsg Welcome to *{guild}*, **{user}**!

* //leavemsg [message] - Shows or sets the leave message.
The current placeholders are: {user} && {guild}
Seperate multiple messages with a semi-colon.
Example: //leavemsg Say goodbye to *{guild}*, **{user}**!

* //autorole - Toggles the auto-role function for newcomers.
This is per guild.
The role is set with a different command.
Example: //autorole

* //setrole (role name) - Sets the auto-role role for newcomers.
This role can have spaces.
If there are multiple roles with the same name, it'll apply both.
Example: //setrole User

* //abc (id) - Sets the channel for Arraybot. Use //search for the ID.
Join/leave messages will be displayed here.
By default this is the defaul channel.
Example: //abc 215520148373635074



##Custom commands
* //createcmd (name) <response> - Create a custom command.
The command will have no prefix, so if you want it to be //hi set the name to //hi.
The the name can only be one word.
Use underscores in names as space placeholders.
You can have multiple replies by seperating them with a semi-colon.
You can set the bot to tag a user by adding "[user]", then the user ID, then "[/user]".
You can only tag one user.
You can set the bot to tag a role by adding "[role]", then the role ID, then "[/role]".
You can only set one role.
It is possible to have one role and one user tag per reply.
The role tag will be FIRST in the message, independent to where you have put it.
The user tag will be SECOND in the message, independent to where you have put it.
The current placeholders are: {user} & {guild}
Example: //createcmd //hi Hello.;Hey, {user}!

* //deletecmd (name) - Deletes the custom command.
Make sure to put the _ instead of space!
Deleted commands cannot be recovered.
Example: //deletecmd hi

* //customcmds - List all custom commands.
This applies for the current guild only, not globally.
Spaces are repalced with an _ here.
Example: //customcmds



