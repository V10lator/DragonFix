package com.modcrafting.dragonfix;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftEnderDragon;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class DragonFix extends org.bukkit.plugin.java.JavaPlugin implements org.bukkit.event.Listener {
	public void onEnable() {
		for(World w: getServer().getWorlds())
		   	for(Chunk c: w.getLoadedChunks())
		   		chunkLoad(new ChunkLoadEvent(c, false));
		getServer().getPluginManager().registerEvents(this, this);
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
				((CraftEnderDragon)e).getHandle().X = false;
	}
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void chunkUnload(ChunkUnloadEvent event) {
		for(Entity e: event.getChunk().getEntities())
			if(e instanceof EnderDragon)
				((CraftEnderDragon)e).getHandle().X = true;
	}
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void dragonSpawn(CreatureSpawnEvent event) {
		if(event.getEntity() instanceof EnderDragon)
			((CraftEnderDragon)event.getEntity()).getHandle().X = false;
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void dragonExplode(EntityExplodeEvent event)
	{
		if(event.getEntity() instanceof EnderDragon)
			event.blockList().clear();
	}
}