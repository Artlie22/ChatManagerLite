package net.Artlie.ChatManagerLite.Hooks.GroupManager;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class GMHook implements Listener {
	private static GroupManager groupManager;
	private Plugin plugin;

	public GMHook(final Plugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(final PluginEnableEvent event) {
		final PluginManager pluginManager = plugin.getServer().getPluginManager();
		final Plugin GMplugin = pluginManager.getPlugin("GroupManager");

		if (GMplugin != null && GMplugin.isEnabled()) {
			groupManager = (GroupManager) GMplugin;

		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginDisable(PluginDisableEvent event) {
		if (groupManager != null) {
			if (event.getPlugin().getDescription().getName().equals("GroupManager")) {
				groupManager = null;
			}
		}
	}

	public static String getPrefix(final Player base) {
		final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(base);
		if (handler == null) {
			return null;
		}
		return handler.getUserPrefix(base.getName());
	}

	public static String getSuffix(final Player base) {
		final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(base);
		if (handler == null) {
			return null;
		}
		return handler.getUserSuffix(base.getName());
	}

	public boolean hasPermission(final Player base, final String node) {
		final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(base);
		if (handler == null) {
			return false;
		}
		return handler.has(base, node);
	}
}