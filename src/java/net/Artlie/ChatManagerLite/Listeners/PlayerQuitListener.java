package net.Artlie.ChatManagerLite.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.Artlie.ChatManagerLite.Main;
import net.Artlie.ChatManagerLite.Utils;

public class PlayerQuitListener implements Listener {

	@SuppressWarnings("static-access")
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		String format = Utils.color(Main.instance.config.getString("Chat.leave"));
		format = format.replace("%playername%", e.getPlayer().getName());
		format = format.replace("%displayname%", e.getPlayer().getDisplayName());
		if (Main.instance.getServer().getPluginManager().isPluginEnabled("Vault")) {
			if (Main.instance.hooks.getBoolean("Vault")) {
				format = format.replace("%vault_prefix%", Main.instance.getChat().getPlayerPrefix(e.getPlayer()));
				format = format.replace("%vault_suffix%", Main.instance.getChat().getPlayerSuffix(e.getPlayer()));
				format = format.replace("%vault_prefix_name%",
						Main.getChat().getPlayerPrefix(e.getPlayer()) + " " + e.getPlayer().getDisplayName());
				format = format.replace("%vault_name_suffix%",
						e.getPlayer().getDisplayName() + " " + Main.getChat().getPlayerSuffix(e.getPlayer()));
				format = format.replace("%vault_prefix_name_suffix%", Main.getChat().getPlayerPrefix(e.getPlayer())
						+ " " + e.getPlayer().getDisplayName() + " " + Main.getChat().getPlayerSuffix(e.getPlayer()));
			}
		}
		e.setQuitMessage(format);
	}

}
