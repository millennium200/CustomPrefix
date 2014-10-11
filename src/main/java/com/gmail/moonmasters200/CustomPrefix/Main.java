package com.gmail.moonmasters200.CustomPrefix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{

  String prefix = "" + ChatColor.BOLD + "Prefix" + ChatColor.GRAY + ChatColor.BOLD + ">> ";
  String error = "" + ChatColor.BOLD + "ERROR" + ChatColor.GRAY + ChatColor.BOLD + ">> ";

  public void onEnable()
  {
    this.saveDefaultConfig();
  }
  
  @ SuppressWarnings({ "deprecation" })
  public boolean onCommand(CommandSender sender, Command cmd, String StringLabel, String[] args)
  {

    if (cmd.getName().equalsIgnoreCase("prefix") && (args.length == 2))
    {
      
      if (!(sender instanceof Player))
      {
        sender.sendMessage("Only players can set prefixes for themselves.");
        return true;
      }
      
      Player player = (Player) sender;
      
      if (!player.hasPermission("millenium.prefix.use"))
      {
        player.sendMessage("You don't have enough swag.");
        return true;
      }
      
      if (args[0].equalsIgnoreCase("set"))
      {
        
        /**
         * Here, we'll do many different checks on the prefix, a user is trying to use.
         * Some things we'll check for include: length, staff tags, racist tags,
         * staff colors, and bad symbols that don't work in prefixes.
         */
        
        String playerNewPrefix = args[1];
        
        int prefixLength = playerNewPrefix.length();
        
        if (prefixLength > 14)
        {
          player.sendMessage(ChatColor.RED + "[WARNING:] " + ChatColor.AQUA + "Your prefix is too long.");
          return true;
        }
        
        /** This is a very important test to determine if the prefix the user
         * enters is alphanumeric (with &'s and -'s) or not.
         */
        int n = 0;
        char testLetter;
        while (n < (prefixLength)) {
          testLetter = playerNewPrefix.charAt(n);
          n++;
          if (Character.isLetter(testLetter) || Character.isDigit(testLetter) || (testLetter == '&'))
          {
            continue;
          } else
          {
            player.sendMessage("Your prefix needs to be alphanumeric!");
            return true;
          }
        }
        
        /** This code is in progress to check each color used */
        /** Default color will be the &5 SWAG-VIP color */
        /** Not allowed colors include red and pink, &k formatting isn't allowed either */
        
        // Create variables for testing
        int locationColor;
        char color;
        /** This code does various checks on color */
        char lastLetter = playerNewPrefix.charAt(prefixLength - 1);
        if (lastLetter == '&')
        {
          player.sendMessage("You have to specify a color with a letter after the '&'");
          return true;
        }
        
        /** Loop to check that &4, &d, and &k aren't used in prefix */
        String[] prefixArray = new String[prefixLength];
        for(int i = 0; i < prefixLength; i++)
        {
          if (playerNewPrefix.charAt(i) == '&')
          {
            /** In this if statement, we inspect the '&''s */
            locationColor = i+1;
            color = playerNewPrefix.charAt(locationColor);
            if (color == 'd' || color == '4' || color == 'c')
            {
              player.sendMessage("You can't use that color.");
              return true;
            } else if (color == 'k')
            {
              player.sendMessage("You can't format your prefix like that.");
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
        
        /** Below that, we should do tests to see staff prefixes, racist / derogatory prefixes
         *  inappropriate prefixes, etc.  A config would work best to add more.
         */
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
        // The below is just a placeholder.
        player.sendMessage("Your prefix without ampersands is: " + prefixWithoutAmpersands);
        
        String[] bannedWords = getConfig().getStringList("bannedwords").toArray(new
            String[getConfig().getStringList("bannedwords").size()]);
        //String[] bannedWords = (String[]) getConfig().getStringList("bannedwords").toArray();
        String prefixLowerCase = prefixWithoutAmpersands.toLowerCase();
        player.sendMessage("Your prefix in lowercase is: " + prefixLowerCase);
        
        /** This is different from the check way above, because this does not include &'s. */
        if (prefixWithoutAmpersands.length() > getConfig().getInt("maxPrefixLength"))
        {
          player.sendMessage(ChatColor.RED + "[WARNING:] " + ChatColor.AQUA + "Your prefix is too long.");
          return true;
        }
        
        /** This checks if the prefixLowerCase (without ampersands) contains any bannedwords */
        /** Set bannedwords in the config.yml */
        for(int i = 0; size < bannedWords.length; size++)
        {
          if (prefixLowerCase.contains(bannedWords[i]))
          {
            player.sendMessage("You cannot use \"" + ChatColor.RED + ChatColor.ITALIC + ChatColor.BOLD +
                "" + bannedWords[i] + ChatColor.RESET + "\" in your prefix.");
            return true;
          }
        }
        
        /** testing */
        /** Can't store &'s in the config.yml, so setting them here for now */
        String startingBracket = getConfig().getString("startingBracket");//"&8&l[&5&l"; // 
        String closingBracket = getConfig().getString("closingBracket");//"&8&l] &5&l"; // 
        
        /** This sends the command through PermissionsEX to set the prefix */
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() +
          " prefix " + "\"" + startingBracket + playerNewPrefix + closingBracket + "\"");
        player.sendMessage(this.prefix + ChatColor.GREEN + "You set your prefix to " + ChatColor.RESET + ChatColor.BOLD + playerNewPrefix);
        
        /** This sets the prefix someone set inside of the config file to check for abuse */
        getConfig().set("prefixes." + player.getName(), prefixWithoutAmpersands);
        saveConfig();
        
        /** This sends a message to who you specify a message regarding prefixes. */
        /** Default is millenium200, specify this in config.yml */
        String sendPrefixesTo = getConfig().getString("sendPrefixesTo");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mail send " + sendPrefixesTo + " Player, " + 
            player.getName() + " has set their prefix to " + prefixWithoutAmpersands + ".");
        
        /** This broadcasts to the server, that someone set their prefix */
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Swag-Prefixes]" + ChatColor.AQUA + 
            " " + player.getName() + " has set their " + ChatColor.BOLD + "prefix" + 
            ChatColor.AQUA + " using /prefix!");
        return true;
      }
    }
    else if ((cmd.getName().equalsIgnoreCase("resetprefix") || (cmd.getName().equalsIgnoreCase("prefixreset"))))
    {
      if (!(sender instanceof Player))
      {
        sender.sendMessage("Only players can reset prefixes.");
        return true;
      }
      
      Player player = (Player) sender;
      
      if (!player.hasPermission("millenium.prefix.use"))
      {
        player.sendMessage("You don't have enough swag.");
        return true;
      }
      
      /** This sends a command through PermissionsEX to reset their prefix */
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() +
        " prefix \"\"");
      player.sendMessage(ChatColor.BOLD + "You have reset your prefix!");
      
      /** This sets the prefix stored for someone to "prefixwasreset" */
      getConfig().set("prefixes." + player.getName(), "prefixwasreset");
      saveConfig();
      
      return true;
    }
    else if (cmd.getName().equalsIgnoreCase("prefixconfigreload"))
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
    return false;

  }
}
