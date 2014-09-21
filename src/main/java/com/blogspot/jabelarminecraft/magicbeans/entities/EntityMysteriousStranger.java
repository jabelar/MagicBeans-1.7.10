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
*/

package com.blogspot.jabelarminecraft.magicbeans.entities;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

/**
 * @author jabelar
 *
 */
public class EntityMysteriousStranger extends EntityCreature
{

	/**
	 * @param parWorld
	 */
	public EntityMysteriousStranger(World parWorld) 
	{
		super(parWorld);
		// TODO Auto-generated constructor stub
	}

	// you don't have to call this as it is called automatically during EntityLiving subclass creation
	@Override
	protected void applyEntityAttributes()
	{
	    super.applyEntityAttributes(); 

	    // standard attributes registered to EntityLivingBase
	   getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
	   getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.0D); // doesnt' move
	   getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.8D);
	   getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);

	    // need to register any additional attributes
//	   getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
//	   getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
	}
}
