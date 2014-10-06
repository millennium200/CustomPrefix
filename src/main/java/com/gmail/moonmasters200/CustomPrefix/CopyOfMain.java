package com.gmail.moonmasters200.CustomPrefix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CopyOfMain extends JavaPlugin
{
  
  String prefix = "" + ChatColor.BOLD + "Prefix" + ChatColor.GRAY + ChatColor.BOLD + ">> ";
  String error = "" + ChatColor.BOLD + "ERROR" + ChatColor.GRAY + ChatColor.BOLD + ">> ";

  public void onEnable()
  {
    this.saveDefaultConfig();
  }
  
  @ SuppressWarnings({ })
  public boolean onCommand(CommandSender sender, Command cmd, String StringLabel, String[] args)
  {
    if (cmd.getName().equalsIgnoreCase("prefix"))
    {
      /** /prefix check <username> command */
      if (args[0].equalsIgnoreCase("check"))
      {
        if (args.length == 2)
        {
          // Check permissions
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
        }
        else
        {
          return false;
        }
      }
      
      /** /prefix set <prefix> [username] command */
      else if (args[0].equalsIgnoreCase("set"))
      {
        // Check permissions
        if (args.length == 2)
        {
          if (sender instanceof Player)
          {
            Player player = (Player) sender;
            if (!(player.hasPermission("millenium.prefix.use")))
            {
              player.sendMessage("You don't have enough swag.");
              return true;
            }
          }
        }
        else if (args.length == 3)
        {
          if (sender instanceof Player)
          {
            Player player = (Player) sender;
            if (!(player.hasPermission("millenium.prefix.setothers")))
            {
              player.sendMessage("You don't have enough swag.");
              return true;
            }
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
        
        // TODO Misc checks on who / what did the command and args
        
        String sendPrefixesTo = getConfig().getString("sendPrefixesTo");
        
        if (!(sender instanceof Player))
        {
          if (args.length != 3)
          {
            return false;
          }
          String playerName = args[2].toString();
          
          setprefix(playerName, playerNewPrefix);
          
          /** This sets the prefix someone set inside of the config file to check for abuse */
          getConfig().set("prefixes." + playerName, prefixWithoutAmpersands);
          saveConfig();
          
          sender.sendMessage(this.prefix + ChatColor.GREEN + playerName +
              "'s prefix was set to " + ChatColor.RESET + ChatColor.BOLD + playerNewPrefix);
          return true;

        }
        else
        {
          if (args.length == 2)
          {
            Player player = (Player) sender;
            // playerName & playerNewPrefix strings
            String playerName = player.getName().toString();
            setprefix(playerName, playerNewPrefix);
            
            /** This sets the prefix someone set inside of the config file to check for abuse */
            getConfig().set("prefixes." + player.getName(), prefixWithoutAmpersands);
            saveConfig();
            
            player.sendMessage(this.prefix + ChatColor.GREEN + "You set your prefix to " + 
            ChatColor.RESET + ChatColor.BOLD + playerNewPrefix);
            
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mail send " 
            + sendPrefixesTo + " Player, " + 
                player.getName() + " has set their prefix to " + prefixWithoutAmpersands + ".");
            
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Swag-Prefixes]" + ChatColor.AQUA + 
                " " + player.getName() + " has set their " + ChatColor.BOLD + "prefix" + 
                ChatColor.AQUA + " using /prefix!");
            return true;
          }
          else if (args.length == 3)
          {
            Player player = (Player) sender;
            if (!(player.hasPermission("millenium.prefix.setothers")))
            {
              player.sendMessage("You do not have enough swag.");
              return true;
            }
            // playerName & playerNewPrefix strings
            String playerName = args[3].toString();
            setprefix(playerName, playerNewPrefix);
            
            /** This sets the prefix someone set inside of the config file to check for abuse */
            getConfig().set("prefixes." + playerName, prefixWithoutAmpersands);
            saveConfig();
            
            player.sendMessage(this.prefix + ChatColor.GREEN + "You set " + playerName +
                "'s prefix to " + ChatColor.RESET + ChatColor.BOLD + playerNewPrefix);
            
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mail send " 
            + sendPrefixesTo + " Player, " + player.getName().toString() + " has set " + 
                playerName + "'s prefix to " + prefixWithoutAmpersands + ".");
            return true;
          }
          else
          {
            return false;
          }
        }
      }
      
      /** /prefix reset [username] command */
      else if (args[0].equalsIgnoreCase("reset"))
      {
        String playerName;
        
        // If no name is specified, ie. player resets their own prefix.
        if (args.length == 1)
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
          playerName = player.toString();
        }
        
        // If /prefix reset <playername>
        else if (args.length == 2)
        {
          playerName = args[1].toString();
          if (sender instanceof Player)
          {
            Player player = (Player) sender;
            if (!(player.hasPermission("millenium.prefix.resetothers")))
            {
              player.sendMessage("You do not have enough swag.");
              return true;
            }
          }
        }
        else
        {
          return false;
        }
        
        setprefix(playerName, "");
      }
      
      /** /prefix reloadconfig command */
      else if (args[0].equalsIgnoreCase("reloadconfig"))
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
      
      /** /prefix addbannedword <word> */
      else if (args[0].equalsIgnoreCase("addbannedword"))
      {
        sender.sendMessage("This still needs to be added.");
        return true;
      }
      return false;
    }
    return false;
    
    
  }
  
  public void setprefix (String playerName, String playerNewPrefix)
  {
    
    // Can't put &'s in the config.yml, so storing the brackets here */
    String startingBracket = "&8&l[&5&l"; // getConfig().getString("startingBracket");
    String closingBracket = "&8&l] &5&l"; // getConfig().getString("closingBracket");
    
    if (getServer().getPluginManager().getPlugin("PermissionsEx") != null)
    {
      if (playerNewPrefix.equalsIgnoreCase(""))
      {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " +
            playerName + " prefix \"\"");
      }
      else
      {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " +
            playerName + " prefix " + "\"" + startingBracket + playerNewPrefix + 
            closingBracket + "\"");
      }
    }
    else if (getServer().getPluginManager().getPlugin("GroupManager") != null)
    {
      if (playerNewPrefix.equalsIgnoreCase(""))
      {
        // Resets prefix
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "manudelv " +
            playerName + " prefix");
      }
      else
      {
        // Sets the prefix
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "manuaddv " +
            playerName + " prefix \"" + prefix + "\"");
      }
    }
    
    else if (getServer().getPluginManager().getPlugin("bPermissions") != null)
    {
      if (playerNewPrefix.equalsIgnoreCase(""))
      {
        // Resets prefix
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "manudelv " +
            playerName + " prefix");
      }
      else
      {
        // Sets the prefix
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "manuaddv " +
            playerName + " prefix \"" + prefix + "\"");
      }
    }
    else if (getServer().getPluginManager().getPlugin("zPermissions") != null)
    {
      if (playerNewPrefix.equalsIgnoreCase(""))
      {
        // Resets prefix
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "manudelv " +
            playerName + " prefix");
      }
      else
      {
        // Sets the prefix
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "manuaddv " +
            playerName + " prefix \"" + prefix + "\"");
      }
    }
    else
    {
      Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "manuaddv " +
          playerName + " prefix \"" + prefix + "\"");
    }
  }

  
}
