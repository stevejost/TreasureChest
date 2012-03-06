package com.mtihc.minecraft.treasurechest.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mtihc.minecraft.core2.BukkitCommand;
import com.mtihc.minecraft.treasurechest.Permission;
import com.mtihc.minecraft.treasurechest.TreasureChestPlugin;

public class SetForgetCommand extends BukkitCommand {

	private TreasureChestPlugin plugin;
	
	public SetForgetCommand(TreasureChestPlugin plugin, BukkitCommand parent, String name, List<String> aliases) {
		super(parent, name, "<days> <hours> <min> <sec>", "Define after how long this chest can be accessed again, per player", aliases);
		this.plugin = plugin;
		setPermission(Permission.SET.getNode());
		setPermissionMessage(ChatColor.RED + "You don't have permission for that command.");
	}

	@Override
	protected boolean onCommand(CommandSender sender, String label,
			String[] args) {

		if(!(sender instanceof Player)) {
			sender.sendMessage("Command must be executed by a player, in game.");
			return false;
		}

		if(!testPermission(sender)) {
			return false;
		}
		
		int days, hours, minutes, seconds;
		try {
			days = Integer.parseInt(args[0]);
			hours = Integer.parseInt(args[1]);
			minutes = Integer.parseInt(args[2]);
			seconds = Integer.parseInt(args[3]);
		} catch(Exception e) {
			sender.sendMessage(ChatColor.RED + "Expected days, hours, minutes, seconds.");
			sender.sendMessage(getUsage());
			return false;
		}
		
		if(args.length > 4) {
			sender.sendMessage(ChatColor.RED + "Too many arguments.");
			sender.sendMessage(getUsage());
			return false;
		}
		
		Player player = (Player) sender;
		Chest chest = plugin.getTargetedChestBlock(player);
		
		if(chest == null || !plugin.getChests().hasChest(chest.getBlock())) {
			sender.sendMessage(ChatColor.RED + "You're not looking at a treasure chest");
			return false;
		}
		
		long secsIn = seconds + (minutes * 60) + (hours * 3600) + (days * 86400);
		int realDays = (int) (secsIn / 86400);
		int remainder = (int) (secsIn % 86400);
		int realHours = remainder / 3600;
		remainder = remainder % 3600;
		int realMinutes = remainder / 60;
		remainder = remainder % 60;
		int realSeconds = remainder;
		
		String chestName = plugin.getChests().getChestNameFormatter().getChestName(chest.getBlock());
		plugin.getChests().setForgetTime(chestName, secsIn * 1000);
		if(days + hours + minutes + seconds == 0) {
			sender.sendMessage(ChatColor.GOLD + "Cleared forget time");
		}else {
			sender.sendMessage(ChatColor.GOLD + "Changed forget time to " + ChatColor.WHITE + realDays + " days, " + realHours + " hours, " + realMinutes + " minutes, and " + realSeconds + " seconds");
		}
		
		
		
		return true;
	}
	


}
