package net.theopalgames.chainedcombat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class CommandChained implements CommandExecutor {
	private static final String NO_PERMISSION = ChatColor.RED + "The chains pull you all around. Chained to the chains, you realize you should not be using this command.";
	
	private boolean infoCommand;
	
	@NonNull
	private String chatOn;
	@NonNull
	private String chatOff;
	
	@NonNull
	private String dingOn;
	@NonNull
	private String dingOff;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		execute(sender, args);
		return true; // Get rid of extra Bukkit usage messages.
	}
	
	private void execute(CommandSender sender, String[] args) {
		if (sender instanceof ProxiedCommandSender)
			sender = ((ProxiedCommandSender) sender).getCallee();
		
		
		if (!infoCommand && !sender.hasPermission("chained.command")) {
			sender.sendMessage(NO_PERMISSION);
			return;
		}
		
		if (args.length == 0)
			noArgs(sender);
		else if (args[1] == "reload")
			reloadCommand(sender);
	}
	
	private void noArgs(CommandSender sender) {
		sender.sendMessage(ChatColor.AQUA + "This server is running ChainedCombat from The Opal Games.");
		
		if (sender instanceof Player) {
			if (chatOn.startsWith("chained ") && chatOff.startsWith("chained "))
				sender.sendMessage(ChatColor.AQUA + "/chained alerts <on|off>");
			
			if (dingOn.startsWith("chained ") && dingOff.startsWith("chained "))
				sender.sendMessage(ChatColor.AQUA + "/chained sounds <on|off>");
		}
		
		if (sender.hasPermission("chained.reload"))
			sender.sendMessage(ChatColor.AQUA + "/chained reload");
	}
	
	private void reloadCommand(CommandSender sender) {
		if (!sender.hasPermission("chained.reload")) {
			sender.sendMessage(NO_PERMISSION);
			return;
		}
		
		ChainedCombatPlugin.getInstance().reloadConfig();
		ChainedCombatPlugin.getInstance().reloadPlayerData();
	}
}
