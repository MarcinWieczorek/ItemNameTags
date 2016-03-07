package co.marcin.itemnametags;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemNameTags extends JavaPlugin {
	private static ItemNameTags instance;
	private final Set<Material> materialList = new HashSet<>();
	private final List<String> tagLore = new ArrayList<>();

	@Override
	public void onEnable() {
		instance = this;

		if(!new File(getDataFolder(), "config.yml").exists()) {
			info("Creating default config");
			saveDefaultConfig();
		}

		getCommand("itemnametags").setExecutor(new CommandInt());

		materialList.addAll(loadMaterials(getConfig().getStringList("applicable")));

		info("Applicable materials (" + materialList.size() + "):");

		int i = 0;
		while(i < materialList.size()) {
			int endIndex = i + 5 > materialList.size() ? materialList.size() : i + 5;
			info(StringUtils.join(materialList.toArray(), ", ", i, endIndex));
			i += 5;
		}

		new InventoryListener();
		setupMetrics();

		List<String> lore = getConfig().getStringList("item.lore");
		for(String loreString : lore) {
			tagLore.add(Utils.fixColors(loreString));
		}

		info("Marcin (CTRL) Wieczorek (http://marcin.co/)");
		info("v" + getDescription().getVersion() + " Enabled");
	}

	@Override
	public void onDisable() {
		info("v" + getDescription().getVersion() + " Disabled");
	}

	public static void info(String message) {
		Bukkit.getLogger().info("[ItemNameTags] " + message);
	}

	public ItemStack getTagItemStack() {
		ItemStack itemStack = new ItemStack(Material.NAME_TAG, 1);

		ItemMeta meta = Bukkit.getItemFactory().getItemMeta(Material.NAME_TAG);
		meta.setDisplayName(Utils.fixColors(getConfig().getString("item.name")));
		meta.setLore(new ArrayList<>(tagLore));
		itemStack.setItemMeta(meta);

		return itemStack;
	}

	public static ItemNameTags getInstance() {
		return instance;
	}

	private List<Material> loadMaterials(List<String> stringList) {
		List<Material> materialList = new ArrayList<>();

		for(String string : stringList) {
			boolean start = StringUtils.startsWith(string, "*");
			boolean end = StringUtils.endsWith(string, "*");

			if(start || end) {
				String match = string;

				if(start) {
					match = match.substring(1);
				}

				if(end) {
					match = match.substring(0, match.length() - 1);
				}

				for(Material m : Material.values()) {
					boolean ends = StringUtils.endsWith(m.name(), match);
					boolean starts = StringUtils.startsWith(m.name(), match);

					if(start && end && !(starts || ends)) {
						if(StringUtils.contains(m.name(), match)) {
							materialList.add(m);
							continue;
						}
					}

					if(!end && ends) {
						materialList.add(m);
						continue;
					}

					if(!start && starts) {
						materialList.add(m);
					}
				}
			}
			else {
				try {
					materialList.add(Material.valueOf(string));
				}
				catch(IllegalArgumentException e) {
					info("Invalid material: " + string);
				}
			}
		}

		return materialList;
	}

	public boolean isTagItemStack(ItemStack itemStack) {
		return itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore() && itemStack.getItemMeta().getLore().equals(tagLore);
	}

	public boolean isApplicable(ItemStack itemStack) {
		return itemStack != null && materialList.contains(itemStack.getType());
	}

	private void setupMetrics() {
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
			info("Metrics started");
		}
		catch(IOException e) {
			info("Failed to update stats!");
			info(e.getMessage());
		}
	}
}
