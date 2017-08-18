package net.Artlie.ChatManagerLite;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.net.HttpURLConnection;

import java.net.URL;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Updater {

	public Updater(final JavaPlugin paramPlugin, final int paramPluginID, final String paramPluginURL) {

		paramPlugin.getServer().getScheduler().runTaskTimerAsynchronously(paramPlugin, new Runnable() {

			public void run() {

				final JavaPlugin PLUGIN = paramPlugin;

				final int PLUGIN_ID = paramPluginID;

				final String PLUGIN_URL = paramPluginURL;

				final String BASE_URL = "http://www.spigotmc.org/api/general.php";

				final String localVersion = PLUGIN.getDescription().getVersion();

				String onlineVersion;

				PLUGIN.getLogger().info("Checking for Updates ... ");

				try {

					HttpURLConnection con = (HttpURLConnection) new URL(BASE_URL).openConnection();

					con.setDoOutput(true);

					con.setRequestMethod("POST");

					con.getOutputStream()

							.write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource="

									+ PLUGIN_ID).getBytes("UTF-8"));

					onlineVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();

				} catch (Exception ex) {

					PLUGIN.getLogger().warning("Failed to check for an update on spigot.");

					PLUGIN.getLogger().warning("Either spigot or you are offline or are slow to respond.");

					return;

				}

				switch (versionCompare(localVersion, onlineVersion)) {

				case 0:

					PLUGIN.getLogger().info("You are running the newest stable build.");

					break;

				case 1:

					PLUGIN.getLogger().info("Your version is newer than the last stable build.");

					PLUGIN.getLogger()

							.info("Stable Version: " + onlineVersion + ". You are running version: " + localVersion);

					PLUGIN.getLogger().info("If you are experiencing issues please fall back to last stable build.");

					break;

				case -1:

					PLUGIN.getLogger().warning("New stable version availiable!");

					PLUGIN.getLogger()

							.warning("Stable Version: " + onlineVersion + ". You are running version: " + localVersion);

					PLUGIN.getLogger().warning("Update at: " + PLUGIN_URL);
					break;

				default:

					PLUGIN.getLogger().warning("Failed to check for an update on spigot.");

					PLUGIN.getLogger().warning("The versions are misbehaving.");

				}

			}

		}, 0L, 432000L);

	}

	public Updater(final Player paramPlayer, final int paramPluginID, final String paramPluginURL) {

		Main.instance.getServer().getScheduler().runTaskTimerAsynchronously(Main.instance, new Runnable() {

			public void run() {

				final JavaPlugin PLUGIN = Main.instance;

				final int PLUGIN_ID = paramPluginID;

				final String BASE_URL = "http://www.spigotmc.org/api/general.php";

				final String localVersion = PLUGIN.getDescription().getVersion();

				final Player p = paramPlayer;

				String onlineVersion;

				try {

					HttpURLConnection con = (HttpURLConnection) new URL(BASE_URL).openConnection();

					con.setDoOutput(true);

					con.setRequestMethod("POST");

					con.getOutputStream()

							.write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource="

									+ PLUGIN_ID).getBytes("UTF-8"));

					onlineVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();

				} catch (Exception ex) {

					p.sendMessage("");
					p.sendMessage("");
					p.sendMessage(color("&7-------- [&aChatManagerPlus&7] --------"));
					p.sendMessage(color("&3&l>&c&l> &4&lAn Error Occurred &c&l<&3&l<"));
					p.sendMessage(color("&7Failed to check for an update on spigot."));
					p.sendMessage(color("&7Either spigot or you are offline or are slow to respond."));
					p.sendMessage(color("&7-------- [&aChatManagerPlus&7] --------"));
					p.sendMessage("");
					p.sendMessage("");

					return;

				}

				switch (versionCompare(localVersion, onlineVersion)) {

				case 0:

					p.sendMessage("");
					p.sendMessage("");
					p.sendMessage(color("&7-------- [&aChatManagerPlus&7] --------"));
					p.sendMessage(color("&3&l>&c&l> &a&lUpdated &c&l<&3&l<"));
					p.sendMessage(color("&8Your version is: &f" + localVersion));
					p.sendMessage(color("&7Stable version is: &f" + onlineVersion));
					p.sendMessage("");
					p.sendMessage(color("&7You are running the newest stable build."));
					p.sendMessage(color("&7-------- [&aChatManagerPlus&7] --------"));
					p.sendMessage("");
					p.sendMessage("");

					break;

				case 1:

					p.sendMessage("");
					p.sendMessage("");
					p.sendMessage(color("&7-------- [&aChatManagerPlus&7] --------"));
					p.sendMessage(color("&3&l>&c&l> &4&lAn Error Occurred &c&l<&3&l<"));
					p.sendMessage(color("&8Your version is: &f" + localVersion));
					p.sendMessage(color("&7Stable version is: &f" + onlineVersion));
					p.sendMessage("");
					p.sendMessage(color("&7If you are experiencing issues"));
					p.sendMessage(color("&7please fall back to last stable build."));
					p.sendMessage(color("&7-------- [&aChatManagerPlus&7] --------"));
					p.sendMessage("");
					p.sendMessage("");

					break;

				case -1:

					TextComponent message = new TextComponent("Click me!");
					message.setClickEvent(
							new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/"));
					message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new ComponentBuilder("Update ChatManagerPlus!").create()));
					message.setColor(ChatColor.GRAY);
					p.sendMessage("");
					p.sendMessage("");
					p.sendMessage(color("&7-------- [&aChatManagerPlus&7] --------"));
					p.sendMessage(color("&3&l>&c&l> &4&lAn Error Occurred &c&l<&3&l<"));
					p.sendMessage(color("&8Your version is: &f" + localVersion));
					p.sendMessage(color("&7Stable version is: &f" + onlineVersion));
					p.sendMessage("");
					p.sendMessage(color("&7New stable version availiable!"));
					p.spigot().sendMessage(message);
					p.sendMessage(color("&7-------- [&aChatManagerPlus&7] --------"));
					p.sendMessage("");
					p.sendMessage("");
					break;

				default:

					p.sendMessage("");
					p.sendMessage("");
					p.sendMessage(color("&7-------- [&aChatManagerPlus&7] --------"));
					p.sendMessage(color("&3&l>&c&l> &4&lAn Error Occurred &c&l<&3&l<"));
					p.sendMessage(color("&7Failed to check for an update on spigot."));
					p.sendMessage(color("&7The versions are misbehaving."));
					p.sendMessage(color("&7-------- [&aChatManagerPlus&7] --------"));
					p.sendMessage("");
					p.sendMessage("");

				}

			}

		}, 0L, 432000L);

	}

	private String color(String s) {
		s = ChatColor.translateAlternateColorCodes('&', s);
		return s;
	}

	public static int versionCompare(String paramVersion1, String paramVersion2) {

		String[] vals1 = paramVersion1.split("\\.");

		String[] vals2 = paramVersion2.split("\\.");

		int i = 0;

		// set index to first non-equal ordinal or length of shortest version

		// string

		while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {

			i++;

		}

		// compare first non-equal ordinal number

		if (i < vals1.length && i < vals2.length) {

			int diff = String.valueOf(vals1[i]).compareTo(String.valueOf(vals2[i]));

			return Integer.signum(diff);

		}

		// the strings are equal or one string is a substring of the other

		// e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"

		return Integer.signum(vals1.length - vals2.length);

	}

}