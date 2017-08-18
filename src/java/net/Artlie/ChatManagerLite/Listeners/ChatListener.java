package net.Artlie.ChatManagerLite.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.wasteofplastic.askyblock.ASkyBlockAPI;

import net.Artlie.ChatManagerLite.Main;
import net.Artlie.ChatManagerLite.Permissions;
import net.Artlie.ChatManagerLite.Utils;
import net.Artlie.ChatManagerLite.ChatManager.ChatManager;
import net.Artlie.ChatManagerLite.Hooks.GroupManager.GMHook;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import us.talabrek.ultimateskyblock.api.uSkyBlockAPI;

public class ChatListener implements Listener {

	PluginManager pm = Main.instance.getServer().getPluginManager();

	@SuppressWarnings({ "static-access", "deprecation" })
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (Main.instance.config.getBoolean("Chat.enabled")) {
			String format = Main.instance.config.getString("Chat.format");
			format = format.replace("%playername%", e.getPlayer().getName());
			format = format.replace("%displayname%", e.getPlayer().getDisplayName());
			if (Main.instance.getServer().getPluginManager().isPluginEnabled("GroupManager")) {
				if (Main.instance.hooks.getBoolean("GroupManager")) {
					try {
						format = format.replace("%gm_prefix%", GMHook.getPrefix(e.getPlayer()));
						format = format.replace("%gm_suffix%", GMHook.getSuffix(e.getPlayer()));
					} catch (Exception ex) {
						Bukkit.getLogger().warning("Could not hook into GroupManager");
					}
				}
			}
			if (Main.instance.getServer().getPluginManager().isPluginEnabled("PermissionsEx")) {
				if (Main.instance.hooks.getBoolean("PermissionsEx")) {
					try {
						PermissionUser user = PermissionsEx.getUser(e.getPlayer());
						String pexprefix = user.getPrefix();
						String pexsuffix = user.getSuffix();
						format = format.replace("%pex_prefix%", pexprefix);
						format = format.replace("%pex_suffix%", pexsuffix);
					} catch (Exception ex) {
						Bukkit.getLogger().warning("Could not hook into PermissionsEx");
					}
				}
			}
			if (pm.isPluginEnabled("Factions")) {
				if (Main.instance.hooks.getBoolean("Factions")) {
					try {
						Object player1 = e.getPlayer();
						Object player2 = e.getPlayer();
						Object player = e.getPlayer();
						com.massivecraft.factions.entity.MPlayer uplayer1 = com.massivecraft.factions.entity.MPlayer
								.get(player1);
						com.massivecraft.factions.entity.MPlayer uplayer2 = com.massivecraft.factions.entity.MPlayer
								.get(player2);
						com.massivecraft.factions.entity.MPlayer uplayer = com.massivecraft.factions.entity.MPlayer
								.get(player);
						com.massivecraft.factions.entity.Faction faction = uplayer.getFaction();
						com.massivecraft.factions.Rel rel = uplayer1.getRelationTo(uplayer2.getFaction());
						format = format.replace("%factions_name%", faction.getName());
						format = format.replace("%factions_rel%", rel.toString());
						format = format.replace("LEADER", Main.instance.config.getString("Factions.Leader")
								.replace("%factions_name%", faction.getName()).replace("%factions_rel%", rel.toString())
								.replace("%factions_colors%", faction.getColorTo(uplayer).toString()));
						format = format.replace("OFFICER", Main.instance.config.getString("Factions.Officer")
								.replace("%factions_name%", faction.getName()).replace("%factions_rel%", rel.toString())
								.replace("%factions_colors%", faction.getColorTo(uplayer).toString()));
						format = format.replace("MEMBER", Main.instance.config.getString("Factions.Member")
								.replace("%factions_name%", faction.getName()).replace("%factions_rel%", rel.toString())
								.replace("%factions_colors%", faction.getColorTo(uplayer).toString()));
						format = format.replace("RECRUIT", Main.instance.config.getString("Factions.Recruit")
								.replace("%factions_name%", faction.getName()).replace("%factions_rel%", rel.toString())
								.replace("%factions_colors%", faction.getColorTo(uplayer).toString()));
						format = format.replace(com.massivecraft.factions.entity.FactionColl.get().getNone().getName(),
								Main.instance.config.getString("Factions.Wilderness")
										.replace("%factions_name%", faction.getName())
										.replace("%factions_rel%", rel.toString())
										.replace("%factions_colors%", faction.getColorTo(uplayer).toString()));
						format = format.replace("%factions_colors%", faction.getColorTo(uplayer).toString());
					} catch (Exception ex) {
						Bukkit.getLogger().warning("Could not hook into Factions!");
					}
				}
			}
			if (Main.instance.getServer().getPluginManager().isPluginEnabled("ASkyBlock")) {
				if (Main.instance.hooks.getBoolean("ASkyBlock")) {
					try {
						format = format.replace("%askyblock_islevel%",
								ASkyBlockAPI.getInstance().getIslandLevel(e.getPlayer().getUniqueId()) + "");
						format = format.replace("%askyblock_isname%",
								ASkyBlockAPI.getInstance().getIslandName(e.getPlayer().getUniqueId()));
						format = format.replace("%askyblock_isworld%",
								ASkyBlockAPI.getInstance().getIslandWorld().getName());
						format = format.replace("%askyblock_ishomeloc%",
								ASkyBlockAPI.getInstance().getHomeLocation(e.getPlayer().getUniqueId()) + "");
					} catch (Exception ex) {
						Bukkit.getLogger().warning("Could not hook into aSkyBlock");
					}
				}
			}
			if (Main.instance.getServer().getPluginManager().isPluginEnabled("Vault")) {
				if (Main.instance.hooks.getBoolean("Vault")) {
					try {
						format = format.replace("%vault_prefix%",
								Main.instance.getChat().getPlayerPrefix(e.getPlayer()));
						format = format.replace("%vault_suffix%",
								Main.instance.getChat().getPlayerSuffix(e.getPlayer()));
						format = format.replace("%vault_prefix_name%",
								Main.getChat().getPlayerPrefix(e.getPlayer()) + " " + e.getPlayer().getDisplayName());
						format = format.replace("%vault_name_suffix%",
								e.getPlayer().getDisplayName() + " " + Main.getChat().getPlayerSuffix(e.getPlayer()));
						format = format.replace("%vault_prefix_name_suffix%",
								Main.getChat().getPlayerPrefix(e.getPlayer()) + " " + e.getPlayer().getDisplayName()
										+ " " + Main.getChat().getPlayerSuffix(e.getPlayer()));

					} catch (Exception ex) {
						Bukkit.getLogger().warning("Could not hook into Vault");
					}
				}
			}
			if (Main.instance.getServer().getPluginManager().isPluginEnabled("uSkyBlock")) {
				if (Main.instance.hooks.getBoolean("uSkyBlock")) {
					try {
						Plugin plugin = Bukkit.getPluginManager().getPlugin("uSkyBlock");
						uSkyBlockAPI usb = (uSkyBlockAPI) plugin;
						format = format.replace("%uskyblock_islevel%", usb.getIslandLevel(e.getPlayer()) + "");
						format = format.replace("%uskyblock_rank%", usb.getIslandRank(e.getPlayer()) + "");
						format = format.replace("%uskyblock_info%", usb.getIslandInfo(e.getPlayer()) + "");
					} catch (Exception ex) {
						Bukkit.getLogger().warning("Could not hook into uSkyBlock");
					}
				}
			}
			format = ChatColor.translateAlternateColorCodes('&', format);
			if (!e.getPlayer().hasPermission(Permissions.getPermission(Permissions.COLOR))) {
				e.setFormat(format + "%2$s");
			} else {
				e.setFormat(format + "%2$s");
				if (e.getMessage().contains("&")) {
					e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
				}
			}
		}
	}

	@EventHandler
	public void Mute(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (Main.instance.mute.contains(p.getName())) {
			Utils.sendMessage(e.getPlayer(), Utils.color("&cYou are muted"));
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void MuteAll(AsyncPlayerChatEvent e) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!p.hasPermission(Permissions.getPermission(Permissions.MUTE_BYPASS))) {
				if (!p.isOp()) {
					if (Main.instance.mute2.contains(p)) {
						Utils.sendMessage(e.getPlayer(), Utils.color("&cChat is muted"));
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void Caps(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		ChatManager.Blocked blocked = Main.instance.getChatManager().canTalk(player, e.getMessage());
		if (blocked.BLOCKED.booleanValue()) {
			String[] arrayOfString;
			int j = (arrayOfString = blocked.MESSAGE).length;
			for (int i = 0; i < j; i++) {
				String message = arrayOfString[i];
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
			}
			e.setCancelled(true);
			return;
		}
	}

	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (Main.instance.config.getBoolean("Chat.Swear.enable")) {
			for (String word : e.getMessage().split(" ")) {
				if (Main.instance.config.getStringList("Chat.Swear.Words").contains(word)) {
					Utils.sendMessage(e.getPlayer(), Main.instance.config.getString("Chat.Swear.Message"));
					e.setCancelled(true);
					return;
				}
			}
		}
	}

	@EventHandler
	public void onChat1(AsyncPlayerChatEvent e) {
		String[] str1 = e.getMessage().split(" ");
		for (String list : Main.instance.config.getStringList("Chat.Swear.Words")) {
			for (String str2 : str1) {
				if (str2.equalsIgnoreCase(list)) {
					Utils.sendMessage(e.getPlayer(), Main.instance.config.getString("Chat.Swear.Message"));
					e.setCancelled(true);
					return;
				}
			}
		}
	}

}
