package net.theopalgames.chainedcombat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
					extra = event.getDamage() * 0.2 * PlayerWrapper.get(attacker).getCombo().getCount();
				}
				catch (Exception e) 
				{
					PlayerWrapper.get(attacker).setCombo(new Combo(attacker, 0, 0, 0));
				}
				if (damaged instanceof Player)
					event.setDamage(event.getDamage() + extra / 4.0);
				else
					event.setDamage(event.getDamage() + extra);
				
				Combo theCombo = PlayerWrapper.get(attacker).getCombo();
				
				combo = theCombo.getCount();
				
				theCombo.setCount(combo+1);
				theCombo.setTime(System.currentTimeMillis()/1000);
			}
			else
			{
				effective = false;
			}

			if (PlayerWrapper.get(attacker).isChatAlerts())
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

			if (PlayerWrapper.get(attacker).isDingAlerts())
			{
				if (effective)
					attacker.playSound(attacker.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1000, (float) pitch);
				else
					attacker.playSound(attacker.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1000, (float) pitch);
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
					if (PlayerWrapper.get(damagedplayer).isChatAlerts())
					{
						if (combo >= 1)
							damaged.sendMessage("\u00A7c(-"+Math.round(event.getDamage()*10)/10.0+") Hit by a "+(combo+1)+"-hit chain! -" + (int)Math.round(extra*10)/10.0 + "!");
						else
							damaged.sendMessage("\u00A7c(-"+Math.round(event.getDamage()*10)/10.0+") Hit!");
					}
					if (PlayerWrapper.get(damagedplayer).isDingAlerts())
					{
						damagedplayer.playSound(damaged.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1000, (float) pitch);
					}
				}
				catch (Exception e) {};
			}
		}
		else
		{
			Entity damaged = event.getEntity();
			if (damaged instanceof Player)
				if (PlayerWrapper.get((Player) damaged).isChatAlerts())
					damaged.sendMessage("\u00A7c(-"+Math.round(event.getDamage()*10)/10.0+") Hit!");
		}
	}
	
	@EventHandler
	public void onDamaged(EntityDamageEvent event)
	{
		if (!enabled)
			return;
		
		Entity damaged = event.getEntity();
		if (damaged instanceof LivingEntity)
		{
			LivingEntity e = (LivingEntity) damaged;
			if (event.getFinalDamage() >= e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
			{
				e.getWorld().playSound(e.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1f, 1f);
				e.getWorld().spawnParticle(Particle.END_ROD, e.getLocation().add(0, 1, 0), 50, 0, 0, 0, 0.075);		
			}

		}
		if (event.getCause().equals(DamageCause.ENTITY_ATTACK) || event.getCause().equals(DamageCause.ENTITY_SWEEP_ATTACK) || event.getCause().equals(DamageCause.ENTITY_EXPLOSION) || event.getCause().equals(DamageCause.PROJECTILE))
			return;
		if (damaged instanceof Player)
		{

			if (PlayerWrapper.get((Player) damaged).isChatAlerts())
				damaged.sendMessage("\u00A7c(-"+Math.round(event.getDamage()*10)/10.0+") Ouch!");
		}	
	}
	
	public void updatePlayers() {
		try
		{
			for (Player player: Bukkit.getOnlinePlayers())
			{
				PlayerWrapper pw = PlayerWrapper.get(player);
				
				//Mention.getGlobal(player);
				if (pw.hasCombo())
					if (System.currentTimeMillis()/1000 - 2 > pw.getCombo().getTime())
					{
						pw.removeCombo();
						
						if (pw.isChatAlerts())
							player.sendMessage(ChatColor.GOLD + "Your combo chain has been reset");
						
						if (pw.isDingAlerts())
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 1000, 1);;
					}

			}
		}
		catch (Exception e)
		{

		}
	}
}
