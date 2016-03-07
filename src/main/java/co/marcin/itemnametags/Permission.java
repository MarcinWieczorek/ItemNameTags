package co.marcin.itemnametags;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public enum Permission {
	GIVE,
	USE,
	INFINITE;

	public boolean has(CommandSender sender) {
		return sender.hasPermission("itemnametags." + getPath());
	}

	private String getPath() {
		return StringUtils.replace(name().toLowerCase(), "_", ".");
	}
}
