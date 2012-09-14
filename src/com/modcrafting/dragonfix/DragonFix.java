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
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DragonFix extends JavaPlugin implements Listener {
	public void onEnable(){
		try {
		    Method a = net.minecraft.server.EntityTypes.class.getDeclaredMethod("a", new Class[] {Class.class, String.class, int.class});
		    a.setAccessible(true);
		    a.invoke(a, NewDragon.class, "NewDragon", 63);
		    a.setAccessible(false);
		    for(World w: getServer().getWorlds())
		    	for(Chunk c: w.getLoadedChunks())
		    		chunkLoad(new ChunkLoadEvent(c, false));
		    
		} catch (Exception e) {
		    e.printStackTrace();
		    this.setEnabled(false);
		}
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
	
	private void replaceEntity(EnderDragon e, boolean tc) {
		EntityEnderDragon d = ((CraftEnderDragon)e).getHandle();
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
}
