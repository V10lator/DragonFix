package com.modcrafting.dragonfix;

import java.lang.reflect.Method;

import org.bukkit.plugin.java.JavaPlugin;

public class DragonFix extends JavaPlugin{
	public void onEnable(){
		try {
		    @SuppressWarnings("rawtypes")
		    Class[] args = new Class[3];
		    args[0] = Class.class;
		    args[1] = String.class;
		    args[2] = int.class;
		    Method a = net.minecraft.server.EntityTypes.class.getDeclaredMethod("a", args);
		    a.setAccessible(true);
		    a.invoke(a, NewDragon.class, "EnderDragon", 63);
		} catch (Exception e) {
		    e.printStackTrace();
		    this.setEnabled(false);
		}
	}
}
