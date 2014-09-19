package com.gmail.moonmasters200.CustomPrefix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
				return true;
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
				
				/** Check for racist / staff / inappropriate words */
				//TODO Add an array check if the string contains any of the words there
				File file = new File ("bannedwords.txt");
				Scanner in = null;
        try {
          in = new Scanner(file);
        } catch (FileNotFoundException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
				int i=0;  String uselessString;
				while(in.hasNextLine()){
				  uselessString = in.nextLine();
				  i++;
				}
				in.nextLine();
				int arrayLength = i;
				
				
				i = 0;
        String bannedwords[] = new String[arrayLength];
				while(in.hasNextLine()){
				  // Retrieves words from the bannedwords.txt file and puts them in
				  // array: bannedwords.
				  bannedwords[i] = in.nextLine();
				  i++;
				}
				
								
				/** This code is in progress to check each color used */
				/** Default color will be the &5 SWAG-VIP color */
				/** Not allowed colors include red and pink, &k formatting isn't allowed either */
				int locationAmpersand;
				int lastLocation = 0;
				locationAmpersand = playerNewPrefix.indexOf('&');
				int locationColor = locationAmpersand + 1;
				char color = prefix.charAt(locationColor);
				while(!(locationAmpersand  == -1)) {
					  locationAmpersand = playerNewPrefix.indexOf('&', lastLocation);
					  locationColor = locationAmpersand + 1;
		        color = prefix.charAt(locationColor);
					  if(color == 'd' || color == '4' || color == 'c') {
		          player.sendMessage("You can't use that color.");
		          return true;
		        }
		        if(color == 'k') {
		          player.sendMessage("You can't format your prefix like that.");
		          return true;
		        }
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