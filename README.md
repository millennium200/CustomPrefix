CustomPrefix
============
Prefix plugin for Bukkit

Enables owners to give admins or donors permission to change their own prefix* 
with the command /prefix set {prefix}.

* You must have a permissions plugin / prefix plugin which allows you to set
  a user's prefix through a command for this plugin to work.

Bukkit Link: http://dev.bukkit.org/bukkit-plugins/customprefix/

The config.yml is used to store a list of bannedwords to block in prefixes, the max length
of the prefix, and the user prefix changes should be sent to through /mail

Commands:
 - /prefix check {playername}
 - /prefix set {prefix} [username]
 - /prefix reset [username]
 - /prefix reloadconfig

Permissions:
 - millenium.prefix.use
 - millenium.prefix.admin
 - millenium.prefix.setothers
 - millenium.prefix.check
 - millenium.prefix.resetothers

