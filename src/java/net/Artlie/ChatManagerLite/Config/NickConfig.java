package net.Artlie.ChatManagerLite.Config;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.Artlie.ChatManagerLite.Main;

public class NickConfig implements Listener {
	@SuppressWarnings("unused")
	private static Main plugin;
	private static FileConfiguration database;
	private static File dFile;

	@SuppressWarnings("static-access")
	public NickConfig(Main plugin) {
		this.plugin = plugin;
	}

	public static void setupFiles(Main plugin) {
		if (plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdirs();
		}
		dFile = new File(plugin.getDataFolder(), "nicks.yml");
		if (!dFile.exists()) {
			try {
				dFile.createNewFile();
			} catch (IOException ex) {
				Bukkit.getLogger().warning("Cannot create 'nicks.yml'.");
			}
		}
		database = YamlConfiguration.loadConfiguration(dFile);
	}

	public static FileConfiguration getData() {
		return database;
	}

	public static void save() {
		try {
			database.save(dFile);
		} catch (IOException ex) {
			Bukkit.getLogger().warning("Cannot save 'nicks.yml'.");
		}
	}

	public static void setNick(Player player, String nick) {
		database.set(player.getUniqueId().toString(), nick);
		save();
		player.setDisplayName(ChatColor.translateAlternateColorCodes('&', nick) + ChatColor.WHITE);
	}

	public static void resetNick(Player player) {
		database.set(player.getUniqueId().toString(), null);
		save();
		player.setDisplayName(null);
	}

	public static boolean hasNick(Player player) {
		return database.contains(player.getUniqueId().toString());
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if (database.contains(player.getUniqueId().toString())) {
			player.setDisplayName(
					ChatColor.translateAlternateColorCodes('&', database.getString(player.getUniqueId().toString())));
		}
	}
}
