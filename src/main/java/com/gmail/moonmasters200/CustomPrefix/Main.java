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
    //getConfig().options.copyDefaults(true);
    //saveConfig();
  }

  @
  SuppressWarnings({ "deprecation" })
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

        if (prefixLength > 8)
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

        /** Check for racist / staff / inappropriate words */
        /** This code vvv is not working.  Need to find a new way to check */

        /**
         * //TODO Add an array check if the string contains any of the words there
        File file = new File("/CustomPrefix/bannedwords.txt");

          Scanner in = null;
          try
          { in = new Scanner(file);
          }
          catch (FileNotFoundException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          int i = 0;@
          SuppressWarnings("unused")
          String uselessString;
          while ( in.hasNextLine())
          {
            uselessString = in .nextLine();
            i++;
          }
          int arrayLength = i;

          i = 0;
          String bannedwords[] = new String[arrayLength];
          bannedwords[0] = "admin"; // Set that in the first slot as a test
          while ( in .hasNextLine())
          {
            // Retrieves words from the bannedwords.txt file and puts them in
            // array: bannedwords.
            bannedwords[i] = in .nextLine();
            i++;
          }

          i = 0;
          String prefixLowerCase = playerNewPrefix.toLowerCase();
          while (i < arrayLength)
          {
            if (prefixLowerCase.contains(bannedwords[i]))
            {
              player.sendMessage("You cannot use \"" + ChatColor.RED + ChatColor.ITALIC + ChatColor.BOLD +
                "" + bannedwords[i] + ChatColor.RESET + "\" in your prefix.");
              i++;
              return true;
            }
          } */

        /** This code is in progress to check each color used */
        /** Default color will be the &5 SWAG-VIP color */
        /** Not allowed colors include red and pink, &k formatting isn't allowed either */
        
        int locationAmpersand;
        int lastLocation = 0;
        locationAmpersand = playerNewPrefix.indexOf('&');
        int locationColor = locationAmpersand + 1;
        char color = playerNewPrefix.charAt(locationColor);
        if (color == 'd' || color == '4' || color == 'c')
        {
          player.sendMessage("You can't use that color.");
          return true;
        } else if (color == 'k' )
        {
          player.sendMessage("You can't format your prefix like that.");
          return true;
        }
        while (!(locationAmpersand == -1))
        {
          locationAmpersand = playerNewPrefix.indexOf('&', lastLocation);
          locationColor = locationAmpersand + 1;
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
          lastLocation = locationAmpersand;
        }
        

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() +
          " prefix " + "\"&8&l[&5&l" + playerNewPrefix + "&8&l] &5&l\"");
        player.sendMessage(this.prefix + ChatColor.GREEN + "You set your prefix to " + ChatColor.RESET + ChatColor.BOLD + playerNewPrefix);

        for (Player p: Bukkit.getOnlinePlayers())
        {
          p.sendMessage(ChatColor.GOLD + "[Swag-Prefixes]" + ChatColor.AQUA + " " + player.getName() + " has set their " + ChatColor.BOLD + "prefix" + ChatColor.AQUA + " using /prefix!");
        }
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

      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() +
        " prefix \"\"");
      player.sendMessage(ChatColor.BOLD + "You have reset your prefix!");
      return true;
    }

    return false;

  }

}