package net.theopalgames.chainedcombat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public final class ToggleCommandHandler implements Listener {
	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		PlayerWrapper pw = PlayerWrapper.get(event.getPlayer());
		String message = event.getMessage().substring(1); // Skip the slash
		FileConfiguration config = ChainedCombatPlugin.getInstance().getConfig();
		
		if (message.equalsIgnoreCase(config.getString("enable-combomsg-command"))) {
			pw.setChatAlerts(true);
			pw.saveSettings();
		} else if (message.equalsIgnoreCase(config.getString("disable-combomsg-command"))) {
			pw.setChatAlerts(false);
			pw.saveSettings();
		} else if (message.equalsIgnoreCase(config.getString("enable-combosound-command"))) {
			pw.setDingAlerts(true);
			pw.saveSettings();
		} else if (message.equalsIgnoreCase(config.getString("disable-combosound-command"))) {
			pw.setDingAlerts(false);
			pw.saveSettings();
		}
	}
}
