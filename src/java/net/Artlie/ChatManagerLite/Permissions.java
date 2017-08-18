package net.Artlie.ChatManagerLite;

public enum Permissions {

	ALL, COLOR, MSG, MSG_REPLY, MUTE_BYPASS, BYPASS, CORE, CLEARCHAT, MUTECHAT, MUTE, NICK, NICK_OTHER, NICK_RESET, NICK_RESET_OTHER, NICK_CHECK, NICK_CHECK_OTHER;

	public static String getPermission(Permissions type) {
		String permission = "";

		if (type == CLEARCHAT) {
			permission = "chatmanagerlite.clearchat";
		} else if (type == MUTE) {
			permission = "chatmanagerlite.mute";
		} else if (type == NICK) {
			permission = "chatmanagerlite.nick";
		} else if (type == NICK_OTHER) {
			permission = "chatmanagerlite.nick.other";
		} else if (type == NICK_RESET) {
			permission = "chatmanagerlite.nick.reset";
		} else if (type == NICK_RESET_OTHER) {
			permission = "chatmanagerlite.nick.reset.other";
		} else if (type == NICK_CHECK) {
			permission = "chatmanagerlite.nick.check";
		} else if (type == NICK_CHECK_OTHER) {
			permission = "chatmanagerlite.nick.check_other";
		} else if (type == CORE) {
			permission = "chatmanagerlite.core";
		} else if (type == ALL) {
			permission = "chatmanagerlite.*";
		} else if (type == MUTECHAT) {
			permission = "chatmanagerlite.mutechat";
		} else if (type == BYPASS) {
			permission = "chatmanagerlite.bypass";
		} else if (type == COLOR) {
			permission = "chatmanagerlite.color";
		} else if (type == MUTE_BYPASS) {
			permission = "chatmanagerlite.mute.bypass";
		} else if (type == MSG) {
			permission = "chatmanagerlite.msg";
		} else if (type == MSG_REPLY) {
			permission = "chatmanagerlite.msg.reply";
		}

		return permission;
	}

}
