/**
    Copyright (C) 2014 by jabelar

    This file is part of jabelar's Minecraft Forge modding examples; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.

	If you're interested in licensing the code under different terms you can
	contact the author at julian_abelar@hotmail.com 
*/

package com.blogspot.jabelarminecraft.magicbeans.entities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;

public class ExtendedPropertiesMagicBeans implements IExtendedEntityProperties 
{
	protected Entity theEntity;
	protected World theWorld;
	
	@Override
	public void saveNBTData(NBTTagCompound parCompound) 
	{
		// DEBUG
		System.out.println("ExtendedPropertiesMagicBeans saveNBTData(), Entity = "+theEntity.getEntityId()+", client side = "+theWorld.isRemote);
		
		// good idea to keep your extended properties in a sub-compound to avoid conflicts with other
		// possible extended properties, even from other mods (like if a mod extends all EntityAnimal)
		parCompound.setTag(MagicBeans.EXT_PROPS_NAME, ((IEntityMagicBeans)theEntity).getExtProps()); // set as a sub-compound
	}

	@Override
	public void loadNBTData(NBTTagCompound parCompound) 
	{
		// DEBUG
		System.out.println("ExtendedPropertiesMagicBeans loadNBTData(), Entity = "+theEntity.getEntityId()+", client side = "+theWorld.isRemote);

		// Get the sub-compound
		((IEntityMagicBeans)theEntity).setExtProps((NBTTagCompound) parCompound.getTag(MagicBeans.EXT_PROPS_NAME));
	}

	@Override
	public void init(Entity entity, World world) 
	{
		// DEBUG
		System.out.println("ExtendedPropertiesMagicBeans init(), Entity = "+entity.getEntityId()+", client side = "+world.isRemote);

		theEntity = entity;
	    theWorld = world;
	}
  
}
