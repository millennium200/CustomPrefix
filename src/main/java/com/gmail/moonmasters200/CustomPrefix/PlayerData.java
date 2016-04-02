package com.gmail.moonmasters200.CustomPrefix;

/** Utility object to assist with checking and setting prefix */
public class PlayerData implements Cloneable
{
  String playerName;
  String prefix;
  String noColorPrefix;
  boolean status;
  
  public PlayerData() {}
  
  public PlayerData(PlayerData playerData)
  {
    this.playerName = playerData.getPlayerName();
    this.prefix = playerData.getPrefix();
    this.noColorPrefix = playerData.getNoColorPrefix();
    this.status = playerData.getStatus();
  }
  
  public void setPlayerName(String playerName)
  {
    this.playerName = playerName;
  }
  
  public String getPlayerName()
  {
    return playerName;
  }
  
  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }
  
  public String getPrefix()
  {
    return prefix;
  }
  
  public void setNoColorPrefix(String noColorPrefix)
  {
    this.noColorPrefix = noColorPrefix;
  }
  
  public String getNoColorPrefix()
  {
    return noColorPrefix;
  }
  
  public void setStatus(boolean status)
  {
    this.status = status;
  }
  
  public boolean getStatus()
  {
    return status;
  }
  
  public Object clone() throws CloneNotSupportedException
  {
    return (PlayerData)super.clone();
  }

}
