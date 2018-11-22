package net.theopalgames.chainedcombat;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.google.common.base.Preconditions;

public final class CombatHandler implements Listener {
	private boolean enabled = false;
	
	public synchronized void enable() {
		Preconditions.checkState(!enabled, "Enabling ChainedCombat while it is already enabled!");
		enabled = true;
	}
	
	public synchronized void disable() {
		Preconditions.checkState(enabled, "Disabling ChainedCombat while it is already disabled!");
		enabled = false;
	}
	
	@EventHandler
	public void onCombat(EntityDamageByEntityEvent event)
	{
		if (!enabled)
			return;
		
		if (event.getDamager() instanceof Player)
		{
			Player attacker = (Player)event.getDamager();
			Entity damaged = event.getEntity();
			int combo = 0;
			double extra = 0.0;
			boolean effective = true;

			if (event.getDamage() > 1.0 || event.getCause().equals(DamageCause.ENTITY_SWEEP_ATTACK))
			{
				try
				{
					extra = event.getDamage() * 0.2 * combos.get(attacker.getName());
				}
				catch (Exception e) 
				{
					combos.put(attacker.getName(), 0);
				}
				if (damaged instanceof Player)
					event.setDamage(event.getDamage() + extra / 4.0);
				else
					event.setDamage(event.getDamage() + extra);
				combo = combos.get(attacker.getName());
				combos.remove(attacker.getName());
				combos.put(attacker.getName(), combo + 1);
				combotimer.put(attacker.getName(), System.currentTimeMillis()/1000);
			}
			else
			{
				effective = false;
			}

			if (combomsg.get(getUUID(attacker)))
			{
				if (combo >= 1 && damaged instanceof Player)
					attacker.sendMessage("\u00A7a(+"+Math.round(event.getDamage()*10)/10.0+") " + (combo+1) + " hit chain, +" + Math.round(extra*10)/40.0 + " (x"+ (1+combo*0.5/10.0) + ")");
				else if (combo >= 1)
					attacker.sendMessage("\u00A7a(+"+Math.round(event.getDamage()*10)/10.0+") " + (combo+1) + " hit chain, +" + Math.round(extra*10)/10.0 + " (x"+ (1+combo*2.0/10.0) + ")");
				else
					attacker.sendMessage("\u00A7a(+"+Math.round(event.getDamage()*10)/10.0+") Hit! (x1.0)");
			}

			double pitch = 0.5;
			for (int i = 0; i < combo; i++)
			{
				pitch *= 1.0594630943;
			}

			if (combosound.get(getUUID(attacker)))
			{
				if (effective)
					attacker.playSound(attacker.getLocation(), Sound.BLOCK_NOTE_BELL, 1000, (float) pitch);
				else
					attacker.playSound(attacker.getLocation(), Sound.BLOCK_NOTE_SNARE, 1000, (float) pitch);
			}

			if (damaged instanceof Player)
			{
				Player damagedplayer = (Player) damaged;
				if (event.getDamager().getType().equals(EntityType.ENDER_PEARL))
				{
					event.setDamage(0);
					return;
				}
				try
				{
					if (combomsg.get(getUUID(damagedplayer)))
					{
						if (combo >= 1)
							damaged.sendMessage("\u00A7c(-"+Math.round(event.getDamage()*10)/10.0+") Hit by a "+(combo+1)+"-hit chain! -" + (int)Math.round(extra*10)/10.0 + "!");
						else
							damaged.sendMessage("\u00A7c(-"+Math.round(event.getDamage()*10)/10.0+") Hit!");
					}
					if (combosound.get(getUUID(damagedplayer)))
					{
						((Player) damaged).playSound(damaged.getLocation(), Sound.BLOCK_NOTE_GUITAR, 1000, (float) pitch);
					}
				}
				catch (Exception e) {};
			}
		}
		else
		{
			Entity damaged = event.getEntity();
			if (damaged instanceof Player)
				if (combomsg.get(getUUID((Player)damaged)))
					damaged.sendMessage("\u00A7c(-"+Math.round(event.getDamage()*10)/10.0+") Hit!");
		}
	}
}
