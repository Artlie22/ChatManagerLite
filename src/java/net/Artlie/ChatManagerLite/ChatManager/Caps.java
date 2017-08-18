package net.Artlie.ChatManagerLite.ChatManager;

import org.bukkit.entity.Player;

import net.Artlie.ChatManagerLite.Main;
import net.Artlie.ChatManagerLite.Permissions;
import net.Artlie.ChatManagerLite.Utils;

public class Caps {
	public String isBlocked(Player player, String message) {
		if (player.hasPermission(Permissions.getPermission(Permissions.BYPASS))) {
			return null;
		}
		if (message.length() < Main.instance.config.getInt("Chat.Caps.Count")) {
			return null;
		}
		double uppercasepercent = getUppercasePercentage(message);
		if (uppercasepercent > 10.0D * Main.instance.config.getDouble("Chat.Caps.Count")) {
			return Utils.color(Main.instance.config.getString("Chat.Caps.Message"));
		}
		return null;
	}

	private double getUppercasePercentage(String string) {
		double upperCase = 0.0D;
		for (int i = 0; i < string.length(); i++) {
			if (isUpperCase(string.substring(i, i + 1))) {
				upperCase += 1.0D;
			}
		}
		return upperCase / string.length() * 100.0D;
	}

	private boolean isUpperCase(String string) {
		return "ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(string);
	}
}
