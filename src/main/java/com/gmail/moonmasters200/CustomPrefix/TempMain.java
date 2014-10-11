package com.gmail.moonmasters200.CustomPrefix;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TempMain extends JavaPlugin {
  
  String announcePrefix = "" + ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + ChatColor.BOLD +
      "CustomPrefix" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + "";
  
  public void onEnable()
  {
    this.saveDefaultConfig();
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String StringLabel, String[] args)
  {
    if (cmd.getName().equalsIgnoreCase("prefix"))
    {
      /** No arguments */
      if (args.length == 0)
      {
        sender.sendMessage(announcePrefix + "/prefix check <playername>");
        sender.sendMessage(announcePrefix + "/prefix set <prefix> [username]");
        sender.sendMessage(announcePrefix + "/prefix reset [username]");
        sender.sendMessage(announcePrefix + "/prefix reloadconfig");
        return true;
      }
      
      /** One argument - reloadconfig and reset */
      else if (args.length == 1)
      {
        if (args[0].equalsIgnoreCase("reloadconfig"))
        {
          if(sender instanceof Player)
          {
            Player player = (Player) sender;
            if (!player.hasPermission("millenium.prefix.reload"))
            {
              player.sendMessage("You don't have enough swag.");
              return true;
            }
          }
          this.reloadConfig();
          sender.sendMessage("CustomPrefix configuration reloaded.");
          return true;
        }
        else if (args[0].equalsIgnoreCase("reset"))
        {
          // TODO Add code for resetting of prefix
          return true;
        }
        return false;
      }
      
      /** Two arguments - check <username>, set <prefix>, reset [playername] */
      else if (args.length == 2)
      {
        if (args[0].equalsIgnoreCase("check"))
        {
          if (sender instanceof Player)
          {
            Player player = (Player) sender;
            if (!(player.hasPermission("millenium.prefix.check")))
            {
              player.sendMessage("You don't have enough swag");
              return true;
            }
          }
          String playerName = args[1].toString();
          // If not found, alert sender
          if (getConfig().get("prefixes." + playerName).toString() == null)
          {
            sender.sendMessage("Prefix not found.");
            return true;
          }
          String playerPrefix = getConfig().get("prefixes." + playerName).toString();
          sender.sendMessage("The prefix of " + playerName + " is " + playerPrefix);
          return true;
        }
        else if (args[0].equalsIgnoreCase("set"))
        {
          // Check whether console sender
          if (!(sender instanceof Player))
          {
            sender.sendMessage(announcePrefix + "Only players can set their"
                + " own prefixes.");
            return true;
          }
          Player player = (Player) sender;
          
          // Check permissions
          if (!(player.hasPermission("millenium.prefix.use")))
          {
            player.sendMessage("You don't have enough swag.");
            return true;
          }
          
          
          
          
          //TODO Add code to set one's prefixes
          return true;
        }
        else if (args[0].equalsIgnoreCase("reset"))
        {
          //TODO Add code to reset others' prefixes
          return true;
        }
        return false;
      }
      
      /** Three arguments - set <prefix> [playername] */
      else if (args.length == 3)
      {
        if (args[0].equalsIgnoreCase("set"))
        {
          //TODO Add code to set others' prefixes.
          return true;
        }
        return false;
      }
      return false;      
    }
    
    
    return false;
  }
  
  
}
