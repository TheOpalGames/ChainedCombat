package net.theopalgames.chainedcombat;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public final class PlayerWrapper {
	@Getter
	private final Player player;
	
	@Getter
	private boolean chatAlerts;
	@Getter
	private boolean dingAlerts;
	
	@Getter
	@Setter
	private Combo combo;
	
	public PlayerWrapper init() {
		initSettings();
		return this;
	}
	
	private void initSettings() {
		FileConfiguration playerData = ChainedCombatPlugin.getInstance().getPlayerData();
		ConfigurationSection ourData = playerData.getConfigurationSection(player.getUniqueId().toString());
		
		chatAlerts = ourData.getBoolean("chatAlerts");
		dingAlerts = ourData.getBoolean("dingAlerts");
	}
}
