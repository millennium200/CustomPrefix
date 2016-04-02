package com.gmail.moonmasters200.CustomPrefix;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class ContentHandler
{
  private PlayerData playerData = new PlayerData();
  private CommandSender sender;
  
  private Character[] bannedColors;
  private String[] bannedWords;
  
  private short errorCode = -1;
  private short TOO_LONG = 0;
  private short NOT_ALPHANUMERIC = 1;
  private short INVALID_FORMATTING = 2;
  private short INVALID_COLOR = 3;
  private short TOO_LONG_2 = 4;
  private short CONTAINS_BANNED_WORD = 5;
  
  public PlayerData check(FileConfiguration config, CommandSender sender, String playerName, String prefix)
  {
    playerData.setPlayerName(playerName);
    playerData.setPrefix(prefix);
    this.sender = sender;
    
    String noColorPrefix = "";
    String prefixLowerCase;
    
    // Check if prefix is too long
    if (prefix.length() > 14)
    {
      errorCode = TOO_LONG;
      alertUser();
      playerData.setStatus(false);
      return playerData;
    }
    else if (prefix.length() > config.getInt("maxPrefixLength"))
    {
      errorCode = TOO_LONG_2;
      alertUser();
      playerData.setStatus(false);
      return playerData;
    }
    
    // Check if prefix is not alphanumeric
    char testLetter;
    for (int i = 0; i < prefix.length();)
    {
      testLetter = prefix.charAt(i);
      i++;
      if (Character.isLetter(testLetter) || Character.isDigit(testLetter)) continue;
      else if (testLetter == '&' && i != prefix.length()) continue;
      else
      {
        errorCode = NOT_ALPHANUMERIC;
        alertUser();
        playerData.setStatus(false);
        return playerData;
      }
    }
    
    // Check if invalid formatting or invalid color
    bannedColors = config.getCharacterList("bannedcolors").toArray(new
        Character[config.getCharacterList("bannedcolors").size()]);
    //int location; char character;
    for (int i = 0; i < prefix.length(); i++)
    {
      if (prefix.charAt(i) == '&')
      {
        for (int j = 0; j < bannedColors.length; j++)
        {
          if (prefix.charAt(i+1) == 'k')
          {
            errorCode = INVALID_FORMATTING;
            alertUser();
            playerData.setStatus(false);
            return playerData;
          }
          if (prefix.charAt(i+1) == bannedColors[j].charValue())
          {
            errorCode = INVALID_COLOR;
            alertUser();
            playerData.setStatus(false);
            return playerData;
          }
        }
      }
      else
      {
        if (i == 0 || prefix.charAt(i - 1) != '&')
        {
          noColorPrefix += prefix.charAt(i);
        }
      }
    }
    
    playerData.setNoColorPrefix(noColorPrefix);
    
    // Check if prefix contains any banned words
    if (config.getStringList("bannedwords").size() == 0)
    {
      return playerData;
    }
    
    bannedWords = config.getStringList("bannedwords").toArray(new 
        String[config.getStringList("bannedwords").size()]);
    prefixLowerCase = noColorPrefix.toLowerCase();
    
    for (int i = 0; i < bannedWords.length; i++)
    {
      if (prefixLowerCase.contains(bannedWords[i]))
      {
        errorCode = CONTAINS_BANNED_WORD;
        alertUser();
        playerData.setStatus(false);
        return playerData;
      }
    }
    
    // Everything is okay
    playerData.setStatus(true);
    return playerData;
  }
  
  private void alertUser()
  {
    String warning = ChatColor.RED + "WARNING> " + " " + ChatColor.DARK_RED;
    if (errorCode == TOO_LONG || errorCode == TOO_LONG_2)
    {
      sender.sendMessage(warning + "That prefix is too long.");
    }
    else if (errorCode == NOT_ALPHANUMERIC)
    {
      sender.sendMessage(warning + "Prefixes must be alphanumeric.");
    }
    else if (errorCode == INVALID_FORMATTING)
    {
      sender.sendMessage(warning + "Prefixes cannot be formatted like that.");
    }
    else if (errorCode == INVALID_COLOR)
    {
      sender.sendMessage(warning + "Prefixes cannot contain that color.");
    }
    else if (errorCode == CONTAINS_BANNED_WORD)
    {
      sender.sendMessage(warning + "That prefix contains a banned word.");
    }
    return;
  }
  
}
