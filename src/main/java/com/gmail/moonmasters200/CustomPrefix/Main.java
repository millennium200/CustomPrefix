package com.gmail.moonmasters200.CustomPrefix;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
  
  private FileConfiguration prefixesConfig = null;
  private File prefixesConfigFile = null;
  
  private String announcePrefix = "" + ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + ChatColor.BOLD +
      "CustomPrefix" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " ";
  private String noperms = ChatColor.translateAlternateColorCodes('&', "&cError: &4You don't have enough swag to do this.");
  
  public void onEnable()
  {
    this.saveDefaultConfig();
    this.saveDefaultPrefixesConfig();
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String StringLabel, String[] args)
  {
    PlayerData playerData = new PlayerData();
    ContentHandler handler = new ContentHandler();
    CommandExecutor executor = new CommandExecutor();
    
    if (cmd.getName().equalsIgnoreCase("prefix"))
    {
      /** No arguments */
      if (args.length == 0)
      {
        sender.sendMessage(announcePrefix + "CustomPrefix plugin developed by millenium200");
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
              player.sendMessage(noperms);
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
            sender.sendMessage(announcePrefix + "Only players can reset their"
                + " own prefixes.");
            return true;
          }
          Player player = (Player) sender;
          if (!(player.hasPermission("millenium.prefix.use")))
          {
            player.sendMessage("You do not have enough swag.");
            return true;
          }
          String playerName = player.getName();
          playerData.setPlayerName(playerName);
          
          executor.resetPrefix(getConfig(), playerData);
          sender.sendMessage("Your prefix was reset.");
          
          /** This sets the prefix someone set inside of the config file to check for abuse */
          getPrefixesConfig().set("prefixes." + playerName, "thisprefixwasreset");
          savePrefixesConfig();
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
              player.sendMessage(noperms);
              return true;
            }
          }
          String playerName = args[1].toString();
          if (getConfig().getString("prefixes." + playerName) == null)
          {
            sender.sendMessage("Prefix not found.");
            return true;
          }
          String playerPrefix = getConfig().get("prefixes." + playerName).toString() ;
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
            player.sendMessage(noperms);
            return true;
          }
          
          // Get the second argument and store as new prefix & store playerName
          String playerName = player.getName();
          String playerNewPrefix = args[1];
          
          playerData = handler.check(getConfig(), sender, playerName, playerNewPrefix);
          if (playerData.getStatus() == false) return true;
          
          /** Get variables, set prefix */
          String sendPrefixesTo = getConfig().getString("sendPrefixesTo");
          executor.setPrefix(getConfig(), playerData);
          
          /** This sets the prefix someone set inside of the config file to check for abuse */
          getPrefixesConfig().set("prefixes." + playerName, playerData.getNoColorPrefix());
          savePrefixesConfig();
          
          /** Alerts player that they set their prefix */
          player.sendMessage(announcePrefix + ChatColor.GREEN + "You set your prefix to " + 
          ChatColor.RESET + ChatColor.BOLD + playerNewPrefix);
          
          /** Alert chosen staff, broadcast to server */
          Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mail send " 
          + sendPrefixesTo + " Player, " + 
              playerName + " has set their prefix to " + playerData.getNoColorPrefix() + ".");
          Bukkit.broadcastMessage(announcePrefix + ChatColor.AQUA + 
              "" + playerName + " has set their " + ChatColor.BOLD + "prefix" + 
              ChatColor.AQUA + " using /prefix!");
          return true;
        }
        /** Resetting another's prefix */
        else if (args[0].equalsIgnoreCase("reset"))
        {
          String playerName = args[1].toString();
          playerData.setPlayerName(playerName);
          if (sender instanceof Player)
          {
            Player player = (Player) sender;
            if (!(player.hasPermission("millenium.prefix.resetothers") 
                || (player.getName().equals(playerName)
                && player.hasPermission("millenium.prefix.use"))))
            {
              player.sendMessage("You do not have enough swag.");
              return true;
            }
          }
          executor.resetPrefix(getConfig(), playerData);
          sender.sendMessage(announcePrefix + "Prefix reset");
          
          if (sender instanceof Player)
          {
            Player player = (Player) sender;
            if (player.getName().equals(playerName))
            {
              getPrefixesConfig().set("prefixes." + playerName, "thisprefixwasreset");
              savePrefixesConfig();
              return true;
            }
          }
          getPrefixesConfig().set("prefixes." + playerName, "anadminresetthis");
          savePrefixesConfig();
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
            if (!(player.hasPermission("millenium.prefix.setothers")
                || (player.getName().equals(playerName)
                    && player.hasPermission("millenium.prefix.use"))))
            {
              player.sendMessage(noperms);
              return true;
            }
          }
          
          // Get the second argument and store as new prefix.
          String playerNewPrefix = args[1];
          
          playerData = handler.check(getConfig(), sender, playerName, playerNewPrefix);
          if (playerData.getStatus() == false) return true;
          
          executor.setPrefix(getConfig(), playerData);
          
          /** This sets the prefix someone set inside of the config file to check for abuse */
          getPrefixesConfig().set("prefixes." + playerName, playerData.getNoColorPrefix());
          savePrefixesConfig();
          
          if (sender instanceof Player)
          {
            Player player = (Player) sender;
            if (player.getName().equals(playerName))
            {
              /** Alerts player that they set their prefix */
              player.sendMessage(announcePrefix + ChatColor.GREEN + "You set your prefix to " + 
              ChatColor.RESET + ChatColor.BOLD + playerNewPrefix);
              
              /** Alert chosen staff, broadcast to server */
              Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mail send " 
              + sendPrefixesTo + " Player, " + 
                  playerName + " has set their prefix to " + playerData.getNoColorPrefix() + ".");
              Bukkit.broadcastMessage(announcePrefix + ChatColor.AQUA + 
                  "" + playerName + " has set their " + ChatColor.BOLD + "prefix" + 
                  ChatColor.AQUA + " using /prefix!");
              return true;
            }
          }
          
          /** Alerts sender that prefix was set */
          sender.sendMessage(announcePrefix + ChatColor.GREEN + playerName +
              "'s prefix was set to " + ChatColor.RESET + ChatColor.BOLD + playerNewPrefix);
          
          /** Alerts staff about prefix */
          Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mail send " 
          + sendPrefixesTo + " Player, " + sender.toString() + "has set " + playerName + "'s prefix"
              + " to " + playerData.getNoColorPrefix() + ".");
          return true;
        }
        return false;
      }
      return false;
    }
    return false;
  }
  
  public void reloadPrefixesConfig()
  {
    if (prefixesConfigFile == null)
    {
      prefixesConfigFile = new File(getDataFolder(), "prefixes.yml");
    }
    prefixesConfig = YamlConfiguration.loadConfiguration(prefixesConfigFile);
    
    // Look for defaults
    Reader defConfigStream;
    try {
      defConfigStream = new InputStreamReader(this.getResource("prefixes.yml"), "UTF-8");
      if (defConfigStream != null)
      {
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        prefixesConfig.setDefaults(defConfig);
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
  
  public FileConfiguration getPrefixesConfig()
  {
    if (prefixesConfig == null)
    {
      reloadPrefixesConfig();
    }
    return prefixesConfig;
  }
  
  public void savePrefixesConfig()
  {
    if (prefixesConfig == null || prefixesConfigFile == null)
    {
      return;
    }
    try {
      getPrefixesConfig().save(prefixesConfigFile);
    } catch(Throwable t)
    {
      getLogger().log(Level.SEVERE, "Could not save config to " + prefixesConfigFile, t);
    }
  }
  
  public void saveDefaultPrefixesConfig()
  {
    if (prefixesConfigFile == null)
    {
      prefixesConfigFile = new File(getDataFolder(), "prefixes.yml");
    }
    if (!prefixesConfigFile.exists())
    {
      this.saveResource("prefixes.yml", false);
    }
  }
  
}
