package net.theopalgames.chainedcombat;

import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public final class Combo {
	private final Player player;
	@Setter
	private long time;
	@Setter
	private double damage;
	@Getter
	@Setter
	private int count;
	
	public void update() {
		
	}
}
