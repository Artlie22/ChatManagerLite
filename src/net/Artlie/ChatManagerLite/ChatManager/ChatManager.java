package net.Artlie.ChatManagerLite.ChatManager;

import org.bukkit.entity.Player;

public class ChatManager {
	Caps anticaps;

	public ChatManager() {
		this.anticaps = new Caps();
	}

	public Blocked canTalk(Player player, String message) {
		String anticapsblocked = this.anticaps.isBlocked(player, message);
		if (anticapsblocked != null) {
			return new Blocked(Boolean.valueOf(true), anticapsblocked);
		}
		return new Blocked(Boolean.valueOf(false));
	}

	public class Blocked {
		public Boolean BLOCKED;
		public String[] MESSAGE;

		public Blocked(Boolean b) {
			this.BLOCKED = b;
			this.MESSAGE = null;
		}

		public Blocked(Boolean b, String s) {
			this.BLOCKED = b;
			this.MESSAGE = new String[] { s };
		}

		public Blocked(Boolean b, String[] s) {
			this.BLOCKED = b;
			this.MESSAGE = s;
		}
	}
}
