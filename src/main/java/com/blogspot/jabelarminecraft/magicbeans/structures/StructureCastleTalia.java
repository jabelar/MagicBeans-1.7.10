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
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.MathHelper;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;

/**
 * @author jabelar
 *
 */
public class StructureCastleTalia extends Structure
{
	public StructureCastleTalia()
	{
		super("taliaCastle");
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
		if (theBlock == Blocks.chest)
		{
			TileEntityChest theTileEntity = (TileEntityChest) theWorld.getTileEntity(parX, parY, parZ);
			int inventorySize = theTileEntity.getSizeInventory();
			for (int i=0; i < inventorySize; i++)
			{
				theTileEntity.setInventorySlotContents(i, new ItemStack(Items.emerald, 5));
			}
		}
		if (theBlock == Blocks.furnace)
		{
			TileEntityFurnace theTileEntity = (TileEntityFurnace) theWorld.getTileEntity(parX, parY, parZ);
			int inventorySize = theTileEntity.getSizeInventory();
			for (int i=0; i < inventorySize; i++)
			{
				theTileEntity.setInventorySlotContents(i, new ItemStack(Items.gold_ingot, 5));
			}
		}
		if (theBlock == Blocks.dispenser)
		{
			TileEntityDispenser theTileEntity = (TileEntityDispenser) theWorld.getTileEntity(parX, parY, parZ);
			int inventorySize = theTileEntity.getSizeInventory();
			for (int i=0; i < inventorySize; i++)
			{
				theTileEntity.setInventorySlotContents(i, new ItemStack(Items.arrow, theTileEntity.getInventoryStackLimit()));
			}
		}
		if (theBlock == Blocks.brewing_stand)
		{
			TileEntityBrewingStand theTileEntity = (TileEntityBrewingStand) theWorld.getTileEntity(parX, parY, parZ);
			int inventorySize = theTileEntity.getSizeInventory();
			for (int i=0; i < inventorySize; i++)
			{
				theTileEntity.setInventorySlotContents(i, new ItemStack(Items.bone, 5));
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
	            entityToSpawn.setLocationAndAngles(startX-3, startY+2, startZ-3, 
	                  MathHelper.wrapAngleTo180_float(theWorld.rand.nextFloat()
	                  * 360.0F), 0.0F);
	            theWorld.spawnEntityInWorld(entityToSpawn);
	            entityToSpawn.playLivingSound();
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
	            entityToSpawn.setLocationAndAngles(startX-10, startY+2, startZ-10, 
	                  MathHelper.wrapAngleTo180_float(theWorld.rand.nextFloat()
	                  * 360.0F), 0.0F);
	            theWorld.spawnEntityInWorld(entityToSpawn);
	            entityToSpawn.playLivingSound();
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
