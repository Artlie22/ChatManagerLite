package net.Artlie.ChatManagerLite.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import net.Artlie.ChatManagerLite.Main;
import net.Artlie.ChatManagerLite.Updater;

public class PlayerJoinListener implements Listener {

	@EventHandler
	public void join(PlayerLoginEvent e) {
		if (Main.instance.config.getBoolean("Auto Update")) {
			new Updater(e.getPlayer(), 45455, "https://www.spigotmc.org/resources/chatmanagerlite.45455/");
		}
	}

}
