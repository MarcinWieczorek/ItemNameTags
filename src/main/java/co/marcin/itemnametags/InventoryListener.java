package co.marcin.itemnametags;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryListener implements Listener {
	private final ItemNameTags plugin = ItemNameTags.getInstance();

	public InventoryListener() {
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		ItemStack cursor = event.getCursor();
		ItemStack current = event.getCurrentItem();
		Player player = (Player) event.getWhoClicked();

		if(!plugin.isTagItemStack(cursor)) {
			return;
		}

		if(!Permission.USE.has(player)) {
			return;
		}

		if(!plugin.isApplicable(current)) {
			return;
		}

		event.setCancelled(true);

		ItemMeta meta = current.getItemMeta();
		if(meta == null) {
			meta = Bukkit.getItemFactory().getItemMeta(current.getType());
		}

		meta.setDisplayName(cursor.getItemMeta().getDisplayName());
		current.setItemMeta(meta);

		if(!Permission.INFINITE.has(player)) {
			if(cursor.getAmount() == 1) {
				event.setCursor(null);
			}
			else {
				cursor.setAmount(cursor.getAmount() - 1);
			}
		}
	}
}
