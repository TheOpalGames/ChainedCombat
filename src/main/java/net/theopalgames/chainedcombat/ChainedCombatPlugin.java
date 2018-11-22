package net.theopalgames.chainedcombat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChainedCombatPlugin extends JavaPlugin {
	private final CombatHandler handler = new CombatHandler();
	
	@Override
	public void onLoad() {
		Bukkit.getPluginManager().registerEvents(handler, this);
		getLogger().info("ChainedCombat has been loaded!");
	}
	
	@Override
	public void onEnable() {
		handler.enable();
		getLogger().info("ChainedCombat has been enabled!");
	}
	
	@Override
	public void onDisable() {
		handler.disable();
		getLogger().info("ChainedCombat has been disabled!");
	}
}
