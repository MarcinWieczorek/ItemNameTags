package co.marcin.itemnametags;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public enum Message {
	NOPERMISSIONS,
	CONSOLESENDER,
	INVALIDPLAYER,
	UNKNOWNCOMMAND;

	public void send(CommandSender sender) {
		String message = ItemNameTags.getInstance().getConfig().getString("messages." + getPath());
		sender.sendMessage(Utils.fixColors("&8[&6ItemNameTags&8] " + message));
	}

	private String getPath() {
		return StringUtils.replace(name().toLowerCase(), "_", ".");
	}
}
