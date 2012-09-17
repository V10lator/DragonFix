package com.modcrafting.dragonfix;

import java.lang.reflect.Method;

import net.minecraft.server.EntityEnderDragon;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftEnderDragon;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import TheEnd.DragonTravel.XemDragon;

import de.V10lator.RideThaDragon.V10Dragon;

public class DragonFix extends JavaPlugin implements Listener {
	private boolean rtd, dt = false;
	
	public void onEnable() {
		try {
		    Method a = net.minecraft.server.EntityTypes.class.getDeclaredMethod("a", new Class[] {Class.class, String.class, int.class});
		    a.setAccessible(true);
		    a.invoke(a, NewDragon.class, "NewDragon", 63);
		    a.setAccessible(false);
		    for(World w: getServer().getWorlds())
		    	for(Chunk c: w.getLoadedChunks())
		    		chunkLoad(new ChunkLoadEvent(c, false));
		    getServer().getPluginManager().registerEvents(this, this);
		} catch (Exception e) {
		    e.printStackTrace();
		    this.setEnabled(false);
		}
		Plugin p = getServer().getPluginManager().getPlugin("RideThaDragon");
		if(p != null && p.isEnabled())
			rtd = true;
		p = getServer().getPluginManager().getPlugin("DragonTravel");
		if(p != null && p.isEnabled())
			dt = true;
	}
	
	public void onDisable() {
		for(World w: getServer().getWorlds())
	    	for(Chunk c: w.getLoadedChunks())
	    		chunkUnload(new ChunkUnloadEvent(c));
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void chunkLoad(ChunkLoadEvent event) {
		for(Entity e: event.getChunk().getEntities())
			if(e instanceof EnderDragon)
				replaceEntity((EnderDragon)e, true);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void chunkUnload(ChunkUnloadEvent event) {
		for(Entity e: event.getChunk().getEntities())
			if(e instanceof EnderDragon)
				replaceEntity((EnderDragon)e, false);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void dragonSpawn(CreatureSpawnEvent event) {
		Entity e = event.getEntity();
		if(e instanceof EnderDragon && !(((CraftEnderDragon)e).getHandle() instanceof NewDragon))
			replaceEntity((EnderDragon)e, true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void dragonExplode(EntityExplodeEvent event)
	{
		if(event.getEntity() instanceof EnderDragon)
			event.blockList().clear();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void pluginEnabled(PluginEnableEvent event)
	{
		String p = event.getPlugin().getName();
		if(p.equals("RideThaDragon"))
			rtd = true;
		else if(p.equals("DragonTravel"))
			dt = true;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void pluginDisabled(PluginDisableEvent event)
	{
		String p = event.getPlugin().getName();
		if(p.equals("RideThaDragon"))
			rtd = false;
		else if(p.equals("DragonTravel"))
			dt = false;
	}
	
	private void replaceEntity(EnderDragon e, boolean tc) {
		EntityEnderDragon d = ((CraftEnderDragon)e).getHandle();
		if(isPluginDragon(d))
			return;
		if(tc)
			d = new NewDragon(d.world);
		else
			d = new EntityEnderDragon(d.world);
		d.world.addEntity(d, SpawnReason.CUSTOM);
		EnderDragon nd = (EnderDragon)d.getBukkitEntity();
		nd.teleport(e.getLocation());
		nd.setHealth(e.getHealth());
		nd.setLastDamage(e.getLastDamage());
		nd.setLastDamageCause(e.getLastDamageCause());
		nd.setPassenger(e.getPassenger());
		if(e.getTicksLived() > 0)
			nd.setTicksLived(e.getTicksLived());
		nd.addPotionEffects(e.getActivePotionEffects());
		e.remove();
	}
	
	private boolean isPluginDragon(EntityEnderDragon d)
	{
		//We check for RideThaDragon (V10Dragon) and DragonTravel (XemDragon) for now.
		return (rtd && d instanceof V10Dragon) || (dt && d instanceof XemDragon);
	}
}
