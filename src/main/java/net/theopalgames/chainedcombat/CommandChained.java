package net.theopalgames.chainedcombat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class CommandChained implements CommandExecutor {
	private boolean infoCommand;
	
	@NonNull
	private final String chatOn;
	@NonNull
	private final String chatOff;
	
	@NonNull
	private final String guitarOn;
	@NonNull
	private final String guitarOff;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		execute(sender, args);
		return true; // Get rid of extra Bukkit usage messages.
	}
	
	private void execute(CommandSender sender, String[] args) {
		
	}
}
