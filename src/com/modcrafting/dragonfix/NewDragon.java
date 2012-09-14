package com.modcrafting.dragonfix;

import net.minecraft.server.EntityEnderDragon;
import net.minecraft.server.World;

public class NewDragon extends EntityEnderDragon {
	
	public NewDragon(World world) {
		super(world);
		X = false;
	}
}