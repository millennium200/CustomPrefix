package com.gmail.moonmasters200.CustomPrefix;

import org.bukkit.Bukkit;
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
      SetAndReset actions = new SetAndReset();
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
        /** Reloading the config */
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
        /** Resetting your prefix */
        else if (args[0].equalsIgnoreCase("reset"))
        {
          if (!(sender instanceof Player))
          {
            return false;
          }
          Player player = (Player) sender;
          if (!(player.hasPermission("millenium.prefix.use")))
          {
            player.sendMessage("You do not have enough swag.");
            return true;
          }
          String playerName = player.toString();

          actions.resetprefix(playerName);
          return true;
        }
        return false;
      }
      
      /** Two arguments - check <username>, set <prefix>, reset [playername] */
      else if (args.length == 2)
      {
        /** Checking someone's prefix */
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
        /** Setting a prefix */
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
          
          // Get the second argument and store as new prefix.
          String playerNewPrefix = args[1];
          
          // Get the length of the new prefix
          int prefixLength = playerNewPrefix.length();
          
          // Prefixes need to be 14 characters or shorter
          if (prefixLength > 14)
          {
            sender.sendMessage(ChatColor.RED + "[Warning:]"
                + " " + ChatColor.AQUA + "That prefix is too long.");
            return true;
          }
          
          // This checks whether a prefix is alphanumeric
          char testLetter;
          for (int n = 0; n < prefixLength;)
          {
            testLetter = playerNewPrefix.charAt(n);
            n++;
            if (Character.isLetter(testLetter) || Character.isDigit(testLetter) ||
                testLetter == '&')
            {
              continue;
            }
            else
            {
              sender.sendMessage("Prefixes need to be alphanumeric.");
              return true;
            }
          }
          
          // This code checks each color used
          // Default color is &5
          // Not allowed colors are &4, &d, and &c, &k formatting not allowed either
          int locationColor; char color;
          char lastLetter = playerNewPrefix.charAt(prefixLength - 1);
          if (lastLetter == 'k')
          {
            sender.sendMessage("Prefixes can not be formatted like that.");
            return true;
          }
          else if (lastLetter == 'd' || lastLetter == 'c' || lastLetter == '4')
          {
            sender.sendMessage("Prefixes can not be red or pink.");
          }
          
          // Loop to check
          String[] prefixArray = new String[prefixLength];
          for (int i = 0; i < prefixLength; i++)
          {
            if (playerNewPrefix.charAt(i) == '&')
            {
              /** In this if statement, we inspect the '&''s */
              locationColor = i+1;
              color = playerNewPrefix.charAt(locationColor);
              if (color == 'd' || color == '4' || color == 'c')
              {
                sender.sendMessage("You can't use that color.");
                return true;
              } else if (color == 'k')
              {
                sender.sendMessage("You can't format your prefix like that.");
                return true;
              }
              prefixArray[i] = "null";
            }
            else if (playerNewPrefix.charAt(i) != '&')
            {
              /** This sets up a system so that we can bannedwords */
              /** This following if statement is very necessary, because
               *  without it, there would be an error from trying to get the char before
               *  the prefix started. ie. prefix = prefix, looking for a value before p.
               */
              if ((i == 0))
              {
                prefixArray[i] = Character.toString(playerNewPrefix.charAt(i));
              }
              else
              {
                if (playerNewPrefix.charAt(i-1) != '&')
                {
                  prefixArray[i] = Character.toString(playerNewPrefix.charAt(i));
                }
                else
                {
                  prefixArray[i] = "null";
                }
              }
            }
          }
          
          /** Start of checking prefixes with bannedwords */
          // This checks for inappropriate tags, staff tags, derogatory, etc
          // The bannedwords are stored in the config.yml
          StringBuilder modifiedString = new StringBuilder();
          for(int i = 0; i < prefixArray.length; i++)
          {
            if(prefixArray[i].equalsIgnoreCase("null"))
            {
              continue;
            }
            modifiedString.append(prefixArray[i]);
          }
          String prefixWithoutAmpersands = modifiedString.toString();
          
          // Pulls the string list from the config.yml of bannedwords into an array
          String[] bannedWords = getConfig().getStringList("bannedwords").toArray(new
              String[getConfig().getStringList("bannedwords").size()]);
          String prefixLowerCase = prefixWithoutAmpersands.toLowerCase();
          
          // This check checks whether the prefix is longer than the specified length in
          // the config.yml and is different from way above cause it does not include &'s
          if (prefixWithoutAmpersands.length() > getConfig().getInt("maxPrefixLength"))
          {
            sender.sendMessage(ChatColor.RED + "[WARNING:] " + ChatColor.AQUA + "That prefix is too long.");
            return true;
          }
          
          // Checks whether prefixLowerCase (without ampersands) contains any bannedwords
          for (int i = 0; i < bannedWords.length; i++)
          {
            if (prefixLowerCase.contains(bannedWords[i]))
            {
              sender.sendMessage("You cannot use \"" + ChatColor.RED + ChatColor.ITALIC + ChatColor.BOLD +
                  "" + bannedWords[i] + ChatColor.RESET + "\" in your prefix.");
              return true;
            }
            i++;
          }
          /** End of checking prefix with bannedwords */
          
          /** Get variables, set prefix */
          String sendPrefixesTo = getConfig().getString("sendPrefixesTo");
          String playerName = player.getName().toString();
          actions.setprefix(playerName, playerNewPrefix);
          
          /** This sets the prefix someone set inside of the config file to check for abuse */
          getConfig().set("prefixes." + player.getName(), prefixWithoutAmpersands);
          saveConfig();
          
          /** Alerts player that they set their prefix */
          player.sendMessage(announcePrefix + ChatColor.GREEN + "You set your prefix to " + 
          ChatColor.RESET + ChatColor.BOLD + playerNewPrefix);
          
          /** Alert chosen staff, broadcast to server */
          Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mail send " 
          + sendPrefixesTo + " Player, " + 
              player.getName() + " has set their prefix to " + prefixWithoutAmpersands + ".");
          Bukkit.broadcastMessage(announcePrefix + ChatColor.AQUA + 
              " " + player.getName() + " has set their " + ChatColor.BOLD + "prefix" + 
              ChatColor.AQUA + " using /prefix!");
          return true;
        }
        /** Resetting another's prefix */
        else if (args[0].equalsIgnoreCase("reset"))
        {
          String playerName = args[1].toString();
          if (sender instanceof Player)
          {
            Player player = (Player) sender;
            if (!(player.hasPermission("millenium.prefix.resetothers")))
            {
              player.sendMessage("You do not have enough swag.");
              return true;
            }
          }
          actions.resetprefix(playerName);
          return true;
        }
        return false;
      }
      
      /** Three arguments - set <prefix> [playername] */
      else if (args.length == 3)
      {
        /** Setting another's prefix */
        if (args[0].equalsIgnoreCase("set"))
        {
          /** Get variables */
          String playerName = args[2].toString();
          String sendPrefixesTo = getConfig().getString("sendPrefixesTo");
          if (sender instanceof Player)
          {
            Player player = (Player) sender;
            if (!(player.hasPermission("millenium.prefix.setothers")))
            {
              player.sendMessage("You don't have enough swag.");
              return true;
            }
          }
          
          // Get the second argument and store as new prefix.
          String playerNewPrefix = args[1];
          
          // Get the length of the new prefix
          int prefixLength = playerNewPrefix.length();
          
          // Prefixes need to be 14 characters or shorter
          if (prefixLength > 14)
          {
            sender.sendMessage(ChatColor.RED + "[Warning:]"
                + " " + ChatColor.AQUA + "That prefix is too long.");
            return true;
          }
          
          // This checks whether a prefix is alphanumeric
          char testLetter;
          for (int n = 0; n < prefixLength;)
          {
            testLetter = playerNewPrefix.charAt(n);
            n++;
            if (Character.isLetter(testLetter) || Character.isDigit(testLetter) ||
                testLetter == '&')
            {
              continue;
            }
            else
            {
              sender.sendMessage("Prefixes need to be alphanumeric.");
              return true;
            }
          }
          
          // This code checks each color used
          // Default color is &5
          // Not allowed colors are &4, &d, and &c, &k formatting not allowed either
          int locationColor; char color;
          char lastLetter = playerNewPrefix.charAt(prefixLength - 1);
          if (lastLetter == 'k')
          {
            sender.sendMessage("Prefixes can not be formatted like that.");
            return true;
          }
          else if (lastLetter == 'd' || lastLetter == 'c' || lastLetter == '4')
          {
            sender.sendMessage("Prefixes can not be red or pink.");
          }
          
          // Loop to check
          String[] prefixArray = new String[prefixLength];
          for (int i = 0; i < prefixLength; i++)
          {
            if (playerNewPrefix.charAt(i) == '&')
            {
              /** In this if statement, we inspect the '&''s */
              locationColor = i+1;
              color = playerNewPrefix.charAt(locationColor);
              if (color == 'd' || color == '4' || color == 'c')
              {
                sender.sendMessage("You can't use that color.");
                return true;
              } else if (color == 'k')
              {
                sender.sendMessage("You can't format your prefix like that.");
                return true;
              }
              prefixArray[i] = "null";
            }
            else if (playerNewPrefix.charAt(i) != '&')
            {
              /** This sets up a system so that we can bannedwords */
              /** This following if statement is very necessary, because
               *  without it, there would be an error from trying to get the char before
               *  the prefix started. ie. prefix = prefix, looking for a value before p.
               */
              if ((i == 0))
              {
                prefixArray[i] = Character.toString(playerNewPrefix.charAt(i));
              }
              else
              {
                if (playerNewPrefix.charAt(i-1) != '&')
                {
                  prefixArray[i] = Character.toString(playerNewPrefix.charAt(i));
                }
                else
                {
                  prefixArray[i] = "null";
                }
              }
            }
          }
          
          /** Start of checking prefixes with bannedwords */
          // This checks for inappropriate tags, staff tags, derogatory, etc
          // The bannedwords are stored in the config.yml
          StringBuilder modifiedString = new StringBuilder();
          for(int i = 0; i < prefixArray.length; i++)
          {
            if(prefixArray[i].equalsIgnoreCase("null"))
            {
              continue;
            }
            modifiedString.append(prefixArray[i]);
          }
          String prefixWithoutAmpersands = modifiedString.toString();
          
          // Pulls the string list from the config.yml of bannedwords into an array
          String[] bannedWords = getConfig().getStringList("bannedwords").toArray(new
              String[getConfig().getStringList("bannedwords").size()]);
          String prefixLowerCase = prefixWithoutAmpersands.toLowerCase();
          
          // This check checks whether the prefix is longer than the specified length in
          // the config.yml and is different from way above cause it does not include &'s
          if (prefixWithoutAmpersands.length() > getConfig().getInt("maxPrefixLength"))
          {
            sender.sendMessage(ChatColor.RED + "[WARNING:] " + ChatColor.AQUA + "That prefix is too long.");
            return true;
          }
          
          // Checks whether prefixLowerCase (without ampersands) contains any bannedwords
          for (int i = 0; i < bannedWords.length; i++)
          {
            if (prefixLowerCase.contains(bannedWords[i]))
            {
              sender.sendMessage("You cannot use \"" + ChatColor.RED + ChatColor.ITALIC + ChatColor.BOLD +
                  "" + bannedWords[i] + ChatColor.RESET + "\" in your prefix.");
              return true;
            }
            i++;
          }
          /** End of checking prefix with bannedwords */
          
          actions.setprefix(playerName, playerNewPrefix);
          
          /** This sets the prefix someone set inside of the config file to check for abuse */
          getConfig().set("prefixes." + playerName, prefixWithoutAmpersands);
          saveConfig();
          
          /** Alerts sender that prefix was set */
          sender.sendMessage(announcePrefix + ChatColor.GREEN + playerName +
              "'s prefix was set to " + ChatColor.RESET + ChatColor.BOLD + playerNewPrefix);
          
          /** Alerts staff about prefix */
          Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mail send " 
          + sendPrefixesTo + " Player, " + sender.toString() + "has set " + playerName + "'s prefix"
              + " to " + prefixWithoutAmpersands + ".");
          return true;
        }
        return false;
      }
      return false;
    }
    return false;
  }
}
