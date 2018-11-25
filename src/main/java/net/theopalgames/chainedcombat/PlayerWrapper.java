package net.theopalgames.chainedcombat;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public final class PlayerWrapper {
	private static final Map<Player,PlayerWrapper> map = new HashMap<>();
	
	public static PlayerWrapper get(Player player) {
		return map.get(player);
	}
	
	@Getter
	private final Player player;
	
	@Getter
	@Setter
	private boolean chatAlerts;
	@Getter
	@Setter
	private boolean dingAlerts;
	
	@Getter
	@Setter
	private Combo combo;
	
	public void init() {
		map.put(player, this);
		initSettings();
	}
	
	private void initSettings() {
		ConfigurationSection ourData = getOurData(false);
		
		if (ourData == null)
			ourData = ChainedCombatPlugin.getInstance().getConfig().getConfigurationSection("default-settings");
		
		chatAlerts = ourData.getBoolean("chatAlerts");
		dingAlerts = ourData.getBoolean("dingAlerts");
	}
	
	public boolean hasCombo() {
		return combo != null;
	}
	
	public void removeCombo() {
		combo = null;
	}
	
	public void saveSettings() {
		ConfigurationSection ourData = getOurData(false);
		
		ourData.set("chatAlerts", chatAlerts);
		ourData.set("dingAlerts", dingAlerts);
	}
	
	private ConfigurationSection getOurData(boolean write) {
		FileConfiguration playerData = ChainedCombatPlugin.getInstance().getPlayerData();
		ConfigurationSection ourData = playerData.getConfigurationSection(player.getUniqueId().toString().replace("-", ""));
		
		if (ourData != null)
			return ourData;
		
		return write ? playerData.createSection(player.getUniqueId().toString().replace("-", "")) : ChainedCombatPlugin.getInstance().getConfig().getConfigurationSection("default-settings"); 
	}
}
