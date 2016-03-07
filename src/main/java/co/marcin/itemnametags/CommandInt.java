package co.marcin.itemnametags;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandInt implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(Utils.fixColors("&8[&6ItemNameTags&8] &7Marcin (CTRL) Wieczorek 2016"));
			sender.sendMessage(Utils.fixColors("&8[&6ItemNameTags&8] &7http://marcin.co/"));
			sender.sendMessage(Utils.fixColors("&8[&6ItemNameTags&8] &4* &7/int give &a[&7player&a]"));
		}
		else {
			if(args[0].equalsIgnoreCase("give")) {
				if(!Permission.GIVE.has(sender)) {
					Message.NOPERMISSIONS.send(sender);
					return true;
				}

				Player receiver;
				if(args.length == 2) {
					receiver = Bukkit.getPlayer(args[1]);
				}
				else {
					if(!(sender instanceof Player)) {
						Message.CONSOLESENDER.send(sender);
						return true;
					}

					receiver = (Player) sender;
				}

				if(receiver == null) {
					Message.INVALIDPLAYER.send(sender);
					return true;
				}

				ItemStack tagItemStack = ItemNameTags.getInstance().getTagItemStack();

				if(Utils.isFull(receiver.getInventory())) {
					receiver.getWorld().dropItemNaturally(receiver.getLocation(), tagItemStack);
				}
				else {
					receiver.getInventory().addItem(tagItemStack);
				}
			}
			else {
				Message.UNKNOWNCOMMAND.send(sender);
			}
		}

		return true;
	}
}
