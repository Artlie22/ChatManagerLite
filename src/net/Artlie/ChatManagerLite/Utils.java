package net.Artlie.ChatManagerLite;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Utils {

	static String noPermissions = Utils.color("&bChatManagerPlus &3>> &aYou don't have Permissions!!");

	public static void sendMessage(CommandSender sender, String msg) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	public static void sendMessage(Player p, String msg) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	public static void sendConsoleMessage(ConsoleCommandSender con, String msg) {
		con.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	public static void broadcastMessage(String msg) {
		Bukkit.broadcastMessage(color(msg));
	}

	public static void log(Level level, String msg) {
		System.out.println(msg);
	}

	public static String color(String s) {
		s = ChatColor.translateAlternateColorCodes('&', s);

		return s;
	}

}