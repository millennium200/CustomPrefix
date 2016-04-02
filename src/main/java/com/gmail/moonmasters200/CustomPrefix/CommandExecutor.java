package com.gmail.moonmasters200.CustomPrefix;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class CommandExecutor
{
  
  public void setPrefix (FileConfiguration config, PlayerData playerData)
  {
    String startingBracket = config.getString("startingBracket");
    String closingBracket = config.getString("closingBracket");
    String prefix = startingBracket + playerData.getPrefix() + closingBracket;
    
    String setPrefixCommand = config.getString("setprefixcommand");
    String temp = setPrefixCommand.replace("{PREFIX}", prefix);
    setPrefixCommand = temp.replace("{PLAYER}", playerData.getPlayerName());
    
    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), setPrefixCommand);
    
  }
  
  public void resetPrefix (FileConfiguration config, PlayerData playerData)
  {
    String resetPrefixCommand = config.getString("resetprefixcommand");
    String result = resetPrefixCommand.replace("{PLAYER}", playerData.getPlayerName());
    
    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), result);
    
  }
  
}
