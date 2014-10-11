package com.gmail.moonmasters200.CustomPrefix;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SetAndReset extends JavaPlugin {

  public void setprefix (String playerName, String playerNewPrefix)
  {
    String startingBracket = getConfig().getString("startingBracket");
    String closingBracket = getConfig().getString("closingBracket");
    
    String prefix = startingBracket + playerNewPrefix + closingBracket;
    
    if (getServer().getPluginManager().getPlugin("PermissionsEx") != null)
    {
      Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " +
          playerName + " prefix " + "\"" + prefix + "\"");
    }
    else if (getServer().getPluginManager().getPlugin("GroupManager") != null)
    {
      Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "manuaddv " +
          playerName + " prefix \"" + prefix + "\"");
    }
    
    else
    {
      /** Case that no plugin was found */
      OfflinePlayer[] list = Bukkit.getOfflinePlayers();
      for (int n = 0; n < list.length; n++) {
        Player player = (Player) list[n];
        if (player.hasPermission("millenium.prefix.admin"))
        {
          Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
              "mail send " + player.toString() + " Permissions Plugin was not"
                  + " found for CustomPrefix!");
        }
      }
    }
  }
  
  public void resetprefix (String playerName)
  {
    if (getServer().getPluginManager().getPlugin("PermissionsEx") != null)
    {
      Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " +
          playerName + " prefix \"\"");
    }
    else if (getServer().getPluginManager().getPlugin("GroupManager") != null)
    {
      Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "manudelv " +
          playerName + " prefix");
    }
  }

  
}
