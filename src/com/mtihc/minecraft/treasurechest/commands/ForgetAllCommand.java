package com.mtihc.minecraft.treasurechest.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mtihc.minecraft.core2.BukkitCommand;
import com.mtihc.minecraft.treasurechest.Permission;
import com.mtihc.minecraft.treasurechest.TreasureChestPlugin;
import com.mtihc.minecraft.treasurechest.persistance.TreasureChest;

public class ForgetAllCommand extends BukkitCommand {

	private TreasureChestPlugin plugin;

	public ForgetAllCommand(TreasureChestPlugin plugin, BukkitCommand parent, String name, List<String> aliases) {
		super(parent, name, "", "As if nobody ever found the chest", aliases);
		this.plugin = plugin;
		setPermission(Permission.FORGET_ALL.getNode());
		setPermissionMessage(ChatColor.RED + "You don't have permission to make a chest forget that anybody found it.");
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
		
		if(args != null && args.length > 0) {
			sender.sendMessage(ChatColor.RED + "Expected no arguments");
			sender.sendMessage(getUsage());
			return false;
		}
		
		Player player = (Player) sender;
		Chest chest = plugin.getTargetedChestBlock(player);
		
		if(chest == null || !plugin.getChests().hasChest(chest.getBlock())) {
			sender.sendMessage(ChatColor.RED + "You're not looking at a treasure chest");
			return false;
		}
		
		String chestName = plugin.getChestName(chest.getBlock());
		TreasureChest tchest = plugin.getChests().getChest(chestName);
		if(tchest.isLinkedChest()) {
			plugin.getMemory().forgetChest(tchest.getLinkedChest());
		}
		plugin.getMemory().forgetChest(chestName);
		sender.sendMessage(ChatColor.GOLD + "Treasure chest is as good as new :)");
		
		
		
		
		
		return true;
	}
	

	
}
