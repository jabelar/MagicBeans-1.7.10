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

package com.blogspot.jabelarminecraft.magicbeans.structures;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.MathHelper;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;

/**
 * @author jabelar
 *
 */
public class StructureCastleJaden extends Structure
{

	public boolean hasSpawnedCastle = false;

	public StructureCastleJaden()
	{
		super("castleJaden");
	}
	
	@Override
	public void populateItems() 
	{
        // DEBUG
        System.out.println("Finished populating items in structure.");
		finishedPopulatingItems = true;
	}

	@Override
	public void customizeTileEntity(Block theBlock, int theMetadata, int parX,
			int parY, int parZ) 
	{
		if (theBlock == Blocks.dispenser)
		{
			TileEntityDispenser theTileEntity = (TileEntityDispenser) theWorld.getTileEntity(parX, parY, parZ);
			int inventorySize = theTileEntity.getSizeInventory();
			for (int i=0; i < inventorySize; i++)
			{
				theTileEntity.setInventorySlotContents(i, new ItemStack(Items.arrow, 5));
			}
		}
		if (theBlock == Blocks.brewing_stand)
		{
			TileEntityBrewingStand theTileEntity = (TileEntityBrewingStand) theWorld.getTileEntity(parX, parY, parZ);
			
			// got potion damage values from http://minecraft.gamepedia.com/Potion#Data_value_table
			for (int slot = 0; slot<theTileEntity.getSizeInventory(); slot++)
			{
				int chanceOfPotionType = theWorld.rand.nextInt(10);
				if (chanceOfPotionType <= 3) // randomize potion
				{
					// fire resistance
					theTileEntity.setInventorySlotContents(slot, new ItemStack(Items.potionitem, 1, 8259));
				}
				else if (chanceOfPotionType <= 8) 
				{
					// regeneration
					theTileEntity.setInventorySlotContents(slot, new ItemStack(Items.potionitem, 1, 8257));
				}
				else
				{
					// water breathing
					theTileEntity.setInventorySlotContents(slot, new ItemStack(Items.potionitem, 1, 8269));
				}
			}
		}
	}

	@Override
	public void populateEntities()
	{
		if (!theWorld.isRemote)
		{
			String entityToSpawnName = "Golden Goose";
	        String entityToSpawnNameFull = MagicBeans.MODID+"."+entityToSpawnName;
	        if (EntityList.stringToClassMapping.containsKey(entityToSpawnNameFull))
	        {
	            EntityLiving entityToSpawn = (EntityLiving) EntityList
	                  .createEntityByName(entityToSpawnNameFull, theWorld);
	            entityToSpawn.setLocationAndAngles(startX+19, startY+10, startZ+22, 
	                  MathHelper.wrapAngleTo180_float(theWorld.rand.nextFloat()
	                  * 360.0F), 0.0F);
	            ((EntityAgeable)entityToSpawn).setGrowingAge(0);
	            theWorld.spawnEntityInWorld(entityToSpawn);
	            entityToSpawn.playLivingSound();
//	            // DEBUG
//	            System.out.println("Populating golden goose at "+entityToSpawn.posX+", "+entityToSpawn.posY+", "+entityToSpawn.posZ);
	        }
	        else
	        {
	            //DEBUG
	            System.out.println("Entity not found "+entityToSpawnName);
	        }

			entityToSpawnName = "Giant";
	        entityToSpawnNameFull = MagicBeans.MODID+"."+entityToSpawnName;
	        if (EntityList.stringToClassMapping.containsKey(entityToSpawnNameFull))
	        {
	            EntityLiving entityToSpawn = (EntityLiving) EntityList
	                  .createEntityByName(entityToSpawnNameFull, theWorld);
	            entityToSpawn.setLocationAndAngles(startX+19, startY+9, startZ+32, 
	                  MathHelper.wrapAngleTo180_float(theWorld.rand.nextFloat()
	                  * 360.0F), 0.0F);
	            theWorld.spawnEntityInWorld(entityToSpawn);
	            entityToSpawn.playLivingSound();
//	            // DEBUG
//	            System.out.println("Populating giant at "+entityToSpawn.posX+", "+entityToSpawn.posY+", "+entityToSpawn.posZ);
	        }
	        else
	        {
	            //DEBUG
	            System.out.println("Entity not found "+entityToSpawnName);
	        }

	        // DEBUG
	        System.out.println("Finished populating entities in structure.");
	        finishedPopulatingEntities = true;
		}
	}
}
