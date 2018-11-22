package net.theopalgames.chainedcombat;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChainedCombatPlugin extends JavaPlugin {
	private final CombatHandler handler = new CombatHandler();
	
	private FileConfiguration config;
	
	@Override
	public void onLoad() {
		saveDefaultConfig();
		config = getConfig();
		
		getCommand("chained").setExecutor(new CommandChained(config.getBoolean("enable-info-command"), config.getString("enable-combomsg-command"), config.getString("disable-combomsg-command"), config.getString("enable-combosound-command"), config.getString("disable-combosound-command")));
		
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
