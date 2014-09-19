package com.gmail.moonmasters200.CustomPrefix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	String prefix = "Prefix" + ChatColor.GRAY + ">> ";
	String error = "ERROR" + ChatColor.GRAY + ">> ";
	
	public void onEnable() {
		//getConfig().options.copyDefaults(true);
		//saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String StringLabel, String[] args) {
				
		Player player = (Player) sender;
		if ((cmd.getName().equalsIgnoreCase("prefix")) && (args.length == 2)) {
			
			if (!(sender instanceof Player)) {
				sender.sendMessage("Only players can set prefixes for themselves.");
				return false;
			}
			
			
			if (!player.hasPermission("millenium.prefix.use")) {
				player.sendMessage("You don't have enough swag.");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("reset")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() +
						" prefix \"\"");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("set")) {
				
				/**
				 * Here, we'll do many different checks on the prefix, a user is trying to use.
				 * Some things we'll check for include: length, staff tags, racist tags,
				 * staff colors, and bad symbols that don't work in prefixes.
				 */
				
				String playerNewPrefix = args[1];
				
				int prefixLength = playerNewPrefix.length();
				
				if (prefixLength > 8) {
					player.sendMessage("Your prefix is too long.");
					return false;
				}
				
				//if (playerNewPrefix.toLowerCase().contains(Array[])) {
				//	player.sendMessage("Your prefix can't be that.");
				//	return false;
				//}
				
				int firstAmpersand = playerNewPrefix.indexOf('&');
				char color;
				
				if(!(firstAmpersand == -1)) {
					color = 'a';
					
					if(color == 'd' || color == '4' || color == 'c') {
						player.sendMessage("You can't use that color.");
						return true;
					}
					if(color == 'k') {
						player.sendMessage("You can't format your prefix like that.");
						return true;
					}
				}
				
				/** This code is in progress to check each color used */
				/** Default color will be the &5 SWAG-VIP color */
				int locationAmpersand;
				int lastLocation;
				locationAmpersand = playerNewPrefix.indexOf('&');
				lastLocation = locationAmpersand;
				while(locationAmpersand  == -1) {
					locationAmpersand = playerNewPrefix.indexOf('&', lastLocation);
					
					
					lastLocation = locationAmpersand;
				}
				
				
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() +
						" prefix " + "\"&8&l[&5&l" + args[1] + "&8&l]\""); 
				player.sendMessage(this.prefix + ChatColor.GREEN + "You set your prefix to " + ChatColor.RESET
						+ args[1]);
			}
		}
		return false;
		
		
	}

}
