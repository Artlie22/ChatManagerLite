package net.Artlie.ChatManagerLite.Listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.Artlie.ChatManagerLite.Main;
import net.Artlie.ChatManagerLite.Utils;

public class PlayerCommandProccessListener implements Listener {

	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (Main.instance.mute.contains(p.getName())) {
			List<String> commands = Main.instance.config.getStringList("Chat.mute.commands");
			for (String command : commands) {
				if (e.getMessage().startsWith("/" + command + " ")) {
					e.getPlayer().sendMessage(Utils.color("&cYou are muted"));
					e.setCancelled(true);
					return;
				}
			}
		}
	}

	@EventHandler
	public void onCommandPreprocesss(PlayerCommandPreprocessEvent e) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!p.isOp()) {
				if (Main.instance.mute2.contains(p)) {
					List<String> commands = Main.instance.config.getStringList("Chat.mute.commands");
					for (String command : commands) {
						if (e.getMessage().startsWith("/" + command + " ")) {
							e.getPlayer().sendMessage(Utils.color("&cYou are muted"));
							e.setCancelled(true);
							return;
						}
					}
				}
			}
		}
	}

}
