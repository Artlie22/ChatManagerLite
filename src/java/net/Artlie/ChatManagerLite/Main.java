package net.Artlie.ChatManagerLite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.Artlie.ChatManagerLite.ChatManager.ChatManager;
import net.Artlie.ChatManagerLite.Config.ConfigManager;
import net.Artlie.ChatManagerLite.Config.NickConfig;
import net.Artlie.ChatManagerLite.Hooks.GroupManager.GMHook;
import net.Artlie.ChatManagerLite.Listeners.ChatListener;
import net.Artlie.ChatManagerLite.Listeners.PlayerCommandProccessListener;
import net.Artlie.ChatManagerLite.Listeners.PlayerJoinListener;
import net.Artlie.ChatManagerLite.Listeners.PlayerLoginListener;
import net.Artlie.ChatManagerLite.Listeners.PlayerQuitListener;
import net.milkbowl.vault.chat.Chat;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

	public ConfigManager config = new ConfigManager(this, "config.yml", "config.yml");
	public ConfigManager hooks = new ConfigManager(this, "hooks.yml", "hooks.yml");
	private ChatManager chatmanager;
	public static Main instance;
	private static Chat chat = null;

	public List<String> mute = new ArrayList<>();
	public ArrayList<Player> mute2 = new ArrayList<Player>();
	public HashMap<Player, Player> r = new HashMap<>();
	public Metrics metrics;

	private boolean setupChat() {
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
		chat = rsp.getProvider();
		return chat != null;
	}

	public static Chat getChat() {
		return chat;
	}

	@Override
	public void onEnable() {
		instance = this;
		this.chatmanager = new ChatManager();
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerCommandProccessListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerLoginListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
		getServer().getPluginManager().registerEvents(new NickConfig(this), this);
		NickConfig.setupFiles(this);
		if (getServer().getPluginManager().isPluginEnabled("Vault")) {
			setupChat();
		} else {
			Utils.log(Level.WARNING, "ChatManagerLite >> Vault not found!! Install vault to use this Plugin");
			getServer().getPluginManager().disablePlugin(this);
		}
		if (hooks.getBoolean("GroupManager")) {
			if (getServer().getPluginManager().isPluginEnabled("GroupManager")) {
				getServer().getPluginManager().registerEvents(new GMHook(this), this);
			} else {
				Utils.log(Level.WARNING, "ChatManagerLite >> GroupManager not found!! Disable the GroupManager Hook");
				getServer().getPluginManager().disablePlugin(this);
			}
		}
		if (getConfig().getBoolean("Auto Update")) {
			new Updater(this, 45455, "https://www.spigotmc.org/resources/chatmanagerlite.45455/");
		}

		metrics = new Metrics(this);
		this.getLogger().info("Metrics loaded.");
		Utils.log(Level.INFO, "ChatManagerLite >> Plugin enabled");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String message = "";
		for (int i = 1; i < args.length; i++) {
			message = message + args[i] + " ";
		}
		String message2 = "";
		for (int i = 1; i < args.length; i++) {
			message2 = message2 + args[i] + " ";
		}
		if (cmd.getName().equalsIgnoreCase("clearchat")) {
			if (!sender.hasPermission(Permissions.getPermission(Permissions.CLEARCHAT))) {
				Utils.sendMessage(sender, Utils.noPermissions);
				return true;
			}
			for (Player all : Bukkit.getOnlinePlayers()) {
				for (int i = 0; i < 100; i++) {
					all.sendMessage(" ");
				}
				Utils.broadcastMessage(this.getConfig().getString("Chat.clear")
						.replace("%playername%", sender.getName()).replace("&", "ยง"));
			}

		}
		if (cmd.getName().equalsIgnoreCase("msg")) {
			if (!(sender instanceof Player)) {
				Utils.sendMessage(sender, "Only players can run this command");
				return true;
			}
			if (!sender.hasPermission(Permissions.getPermission(Permissions.MSG))) {
				Utils.sendMessage(sender, Utils.noPermissions);
				return true;
			}
			if (args.length == 0) {
				Utils.sendMessage(sender, "&cPlease specify a player!");
				return true;
			}
			Player target = Bukkit.getServer().getPlayer(args[0]);
			if (target == null) {
				Utils.sendMessage(sender, "&cPlayer " + args[0] + " is not online!");
				return true;
			}
			if (target == sender) {
				Utils.sendMessage(sender, "&cYou cant send private message to your self!");
				return true;
			}
			if (sender.hasPermission(Permissions.getPermission(Permissions.COLOR))) {
				Utils.sendMessage(sender, Utils.color("&7[&6Me -> &c" + target.getDisplayName() + "&7]:&r " + message));
				Utils.sendMessage(target,
						Utils.color("&7[&c" + ((Player) sender).getDisplayName() + " &6-> Me&7]:&r " + message));
				if (!this.r.containsKey(sender)) {
					this.r.put((Player) sender, target);
				} else {
					this.r.replace((Player) sender, target);
				}
				if (!this.r.containsKey(target)) {
					this.r.put(target, (Player) sender);
				} else {
					this.r.replace(target, (Player) sender);
				}
				return true;
			} else {
				sender.sendMessage("ง7[ง6Me -> งc" + target.getName() + "ง7]:งr " + message);
				target.sendMessage("ง7[งc" + sender.getName() + " ง6-> Meง7]:งr " + message);
				if (!this.r.containsKey(sender)) {
					this.r.put((Player) sender, target);
				} else {
					this.r.replace((Player) sender, target);
				}
				if (!this.r.containsKey(target)) {
					this.r.put(target, (Player) sender);
				} else {
					this.r.replace(target, (Player) sender);
				}
				return true;
			}

		}
		if (cmd.getName().equalsIgnoreCase("reply")) {
			if (!sender.hasPermission(Permissions.getPermission(Permissions.MSG_REPLY))) {
				Utils.sendMessage(sender, Utils.noPermissions);
				return true;
			}
			Player player = (Player) sender;
			Player target = r.get(player);
			if (target == null) {
				Utils.sendMessage(sender, "&cThis player is offline");
				return true;
			}
			if (sender.hasPermission(Permissions.getPermission(Permissions.COLOR))) {
				Utils.sendMessage(player, "&7[&6Me" + " &7-> &r" + target.getDisplayName() + "&7]:&r " + message2);
				Utils.sendMessage(target, "&7[&6" + player.getDisplayName() + " &7-> &6Me&7]:&r " + message2);
			} else {
				player.sendMessage("ง7[ง6Me" + " ง7-> งr" + target.getDisplayName() + " ง7]:งr " + message2);
				target.sendMessage("ง7[ง6" + player.getDisplayName() + " ง7-> ง6Meง7]:งr " + message2);
			}
		}
		if (cmd.getName().equalsIgnoreCase("mute")) {
			if (!sender.hasPermission(Permissions.getPermission(Permissions.MUTE))) {
				Utils.sendMessage(sender, Utils.noPermissions);
				return true;
			}
			if (args.length == 0) {
				Utils.sendMessage(sender, "ง7Your arguments are too short!");
				return true;
			}
			if (args.length == 1) {
				Player target = Bukkit.getServer().getPlayer(args[0]);
				if (target == null) {
					Utils.sendMessage(sender, "ง7The player wasn't found!");
					return true;
				}
				if (!this.mute.contains(target.getName())) {
					this.mute.add(target.getName());
					Utils.sendMessage(sender, "ง7You muted ง8" + target.getName() + "ง7!");
					Utils.sendMessage(target, "ง7You're muted!");
				} else if (this.mute.contains(target.getName())) {
					this.mute.remove(target.getName());
					Utils.sendMessage(sender, "ง7You un-muted ง8" + target.getName() + "ง7!");
					Utils.sendMessage(target, "ง7You're un-muted!");
				}
			}
		}
		if (cmd.getName().equalsIgnoreCase("mutechat")) {
			if (!sender.hasPermission(Permissions.getPermission(Permissions.MUTECHAT))) {
				Utils.sendMessage(sender, Utils.noPermissions);
				return true;
			}
			for (Player all : Bukkit.getOnlinePlayers()) {
				if (!this.mute2.contains(all)) {
					this.mute2.add(all);
					Utils.sendMessage(sender, "ง7You muted ง8the chatง7!");
					Utils.sendMessage(all, "ง7Chat muted!");
				} else if (this.mute2.contains(all)) {
					this.mute2.remove(all);
					Utils.sendMessage(sender, "ง7You un-muted ง8the chatง7!");
					Utils.sendMessage(all, "ง7Chat un-muted!");
				}
			}
		}
		if (cmd.getName().equalsIgnoreCase("nick")) {
			if (!(sender instanceof Player)) {
				ConsoleCommandSender con = (ConsoleCommandSender) sender;
				Utils.sendConsoleMessage(con, "Only players can run this command");
				return true;
			}
			if (!sender.hasPermission(Permissions.getPermission(Permissions.NICK))) {
				Utils.sendMessage(sender, Utils.noPermissions);
				return true;
			}
			Player player = (Player) sender;
			if (args.length == 0) {
				Utils.sendMessage(sender, "&6Help Menu:");
				Utils.sendMessage(sender, "&7/nick set <nick> [player] - Set the nick of you/a player.");
				Utils.sendMessage(sender, "&7/nick check [player] - Check if you/a player has a nick.");
				Utils.sendMessage(sender, "&7/nick reset [player] - Reset your/a players nick.");
			} else if (args[0].equalsIgnoreCase("set")) {
				if (args.length == 1) {
					Utils.sendMessage(sender, "&cCorrect usage: /nick set <nick> [player]");
					return true;
				}
				if (args.length == 2) {
					if ((player.hasPermission(Permissions.getPermission(Permissions.NICK))) || (player.isOp())) {
						NickConfig.setNick(player, args[1]);
						Utils.sendMessage(sender, "&aYou have changed your display name to "
								+ ChatColor.translateAlternateColorCodes('&', args[1]));
					}
				} else if ((args.length == 3)
						&& ((player.hasPermission(Permissions.getPermission(Permissions.NICK_OTHER)))
								|| (player.isOp()))) {
					Player c = Bukkit.getPlayer(args[2]);
					if (c != null) {
						NickConfig.setNick(c, args[1]);
						Utils.sendMessage(sender, "&aYou have changed &f" + c.getName() + " &anick to "
								+ ChatColor.translateAlternateColorCodes('&', args[1]));
					} else {
						Utils.sendMessage(sender, "&cCannot find player " + args[2] + "!");
					}
				}
			} else if (args[0].equalsIgnoreCase("check")) {
				if (args.length == 1) {
					if (player.hasPermission(Permissions.getPermission(Permissions.NICK_CHECK))) {
						if (NickConfig.hasNick(player)) {
							Utils.sendMessage(sender,
									"&aYour active nick is " + ChatColor.translateAlternateColorCodes('&',
											NickConfig.getData().getString(player.getUniqueId().toString())));
						} else {
							Utils.sendMessage(sender, ChatColor.GREEN + "You do not have a active nick.");
						}
					}
				} else if ((args.length == 2)
						&& (player.hasPermission(Permissions.getPermission(Permissions.NICK_CHECK_OTHER)))) {
					Player c = Bukkit.getPlayer(args[1]);
					if (c != null) {
						if (NickConfig.hasNick(c)) {
							Utils.sendMessage(sender,
									"&f" + c.getName() + " &7active nick is " + ChatColor.translateAlternateColorCodes(
											'&', NickConfig.getData().getString(c.getUniqueId().toString())));
						} else {
							Utils.sendMessage(sender, "&c" + c.getName() + " does not have a active nick.");
						}
					} else {
						Utils.sendMessage(sender, "&cCannot find player " + args[1] + "!");
					}
				}
			} else if (args[0].equalsIgnoreCase("reset")) {
				if (args.length == 1) {
					if (player.hasPermission(Permissions.getPermission(Permissions.NICK_RESET))) {
						if (NickConfig.hasNick(player)) {
							NickConfig.resetNick(player);
							Utils.sendMessage(sender, "&aReseted you nick.");
						} else {
							Utils.sendMessage(sender, "&cYou do not have nick.");
						}
					}
				} else if ((args.length == 2)
						&& (player.hasPermission(Permissions.getPermission(Permissions.NICK_RESET_OTHER)))) {
					Player c = Bukkit.getPlayer(args[1]);
					if (c != null) {
						if (NickConfig.hasNick(c)) {
							NickConfig.resetNick(c);
							Utils.sendMessage(sender, "&aReseted &7" + c.getName() + " &anick.");
						} else {
							Utils.sendMessage(sender, "&c" + c.getName() + " does not have a nick.");
						}
					} else {
						Utils.sendMessage(sender, "&cCannot find player " + args[1] + "!");
					}
				}
			} else {
				Utils.sendMessage(sender, "&bChatManagerLite &3>> &aPlease use one of the availalbe options!");
				Utils.sendMessage(sender, "&6[&eset reset check&6]");
			}
		}
		if (cmd.getName().equalsIgnoreCase("chatmanagerlite")) {
			if (!sender.hasPermission(Permissions.getPermission(Permissions.CORE))) {
				Utils.sendMessage(sender, Utils.noPermissions);
				return true;
			}
			if (args.length == 0) {
				Utils.sendMessage(sender, Utils.color("&c-------- &6[&4ChatManagerLite&6]&c --------"));
				Utils.sendMessage(sender, Utils.color("&8Commands&e:"));
				Utils.sendMessage(sender, Utils.color("&7/chatmanagerlite reload"));
				Utils.sendMessage(sender, Utils.color("&7/chatmanagerlite update"));
				Utils.sendMessage(sender, Utils.color("&7/chatmanagerlite help"));
				Utils.sendMessage(sender, Utils.color("&7/clearchat"));
				Utils.sendMessage(sender, Utils.color("&7/mutechat"));
				Utils.sendMessage(sender, Utils.color("&7/mute"));
				Utils.sendMessage(sender, Utils.color("&7/nick"));
				Utils.sendMessage(sender, Utils.color("&7/msg"));
				Utils.sendMessage(sender, Utils.color("&7/reply"));
				Utils.sendMessage(sender, Utils.color("&c-------- &6[&4ChatManagerLite&6]&c --------"));
				return true;
			}
			if (args[0].equalsIgnoreCase("help")) {
				Utils.sendMessage(sender, Utils.color("&c-------- &6[&4ChatManagerLite&6]&c --------"));
				Utils.sendMessage(sender, Utils.color("&8Commands&e:"));
				Utils.sendMessage(sender, Utils.color("&7/chatmanagerlite reload"));
				Utils.sendMessage(sender, Utils.color("&7/chatmanagerlite update"));
				Utils.sendMessage(sender, Utils.color("&7/chatmanagerlite help"));
				Utils.sendMessage(sender, Utils.color("&7/clearchat"));
				Utils.sendMessage(sender, Utils.color("&7/mutechat"));
				Utils.sendMessage(sender, Utils.color("&7/mute"));
				Utils.sendMessage(sender, Utils.color("&7/nick"));
				Utils.sendMessage(sender, Utils.color("&7/msg"));
				Utils.sendMessage(sender, Utils.color("&7/reply"));
				Utils.sendMessage(sender, Utils.color("&c-------- &6[&4ChatManagerLite&6]&c --------"));
				return true;
			} else if (args[0].equalsIgnoreCase("reload")) {
				config.reload();
				hooks.reload();
				Utils.sendMessage(sender, Utils.color("&4Config reloaded"));
				return true;
			} else if (args[0].equalsIgnoreCase("update")) {
				if (!(sender instanceof Player)) {
					new Updater(this, 45455, "https://www.spigotmc.org/resources/chatmanagerlite.45455/");
					return true;
				}
				Player updater = (Player) sender;
				new Updater(updater, 45455, "https://www.spigotmc.org/resources/chatmanagerlite.45455/");
				return true;
			} else {
				Utils.sendMessage(sender,
						Utils.color("&bChatManagerLite &3>> &aPlease use one of the availalbe options!"));
				Utils.sendMessage(sender, Utils.color("&6[&ehelp update reload&6]"));
				return true;
			}

		}
		return true;
	}

	public ChatManager getChatManager() {
		return this.chatmanager;
	}

}
