package net.theopalgames.chainedcombat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;

import lombok.Getter;
import lombok.SneakyThrows;

public final class ChainedCombatPlugin extends JavaPlugin {
	@Getter
	private static ChainedCombatPlugin instance;
	
	private final CombatHandler handler = new CombatHandler();
	
	private FileConfiguration config;
	@Getter
	private YamlConfiguration playerData;
	
	private File playerDataFile;
	
	{
		Preconditions.checkState(instance == null, "Two ChainedCombat plugins?");
		instance = this;
	}
	
	@Override
	public void onLoad() {
		loadConfig();
		
		getCommand("chained").setExecutor(new CommandChained(config.getBoolean("enable-info-command"), config.getString("enable-combomsg-command"), config.getString("disable-combomsg-command"), config.getString("enable-combosound-command"), config.getString("disable-combosound-command")));
		
		Bukkit.getPluginManager().registerEvents(handler, this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, handler::updatePlayers, 1, 1);
		
		getLogger().info("ChainedCombat has been loaded!");
	}
	
	@SneakyThrows // I hate checked exceptions.
	private void loadConfig() {
		saveDefaultConfig();
		config = getConfig();
		
		File playerFile = new File(getDataFolder(), "players.yml"); // YML = Yummypie Markup Language
		if (!playerFile.exists()) {
			playerFile.createNewFile();
			
			try (
				InputStream in = getResource("players.yml");
				FileOutputStream out = new FileOutputStream(playerFile);
			) {
				ByteStreams.copy(in, out);
			}
		}
		
		playerDataFile = playerFile;
		reloadPlayerData();
	}
	
	public void reloadPlayerData() {
		playerData = YamlConfiguration.loadConfiguration(playerDataFile);
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
