package co.marcin.itemnametags;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Utils {
	public static String fixColors(String msg) {
		if(msg == null) {
			return null;
		}

		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public static boolean isFull(Inventory inventory) {
		for(ItemStack itemStack : inventory.getContents()) {
			if(itemStack == null || itemStack.getType() == Material.AIR) {
				return false;
			}
		}

		return true;
	}
}
