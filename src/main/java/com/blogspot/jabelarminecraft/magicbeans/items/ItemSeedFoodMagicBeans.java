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

package com.blogspot.jabelarminecraft.magicbeans.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemSeedFoodMagicBeans extends ItemFood implements IPlantable
{
    private final Block theBlockPlant;
    /**
     * Block ID of the soil this seed food should be planted on.
     */
    private final Block soilId;

    public ItemSeedFoodMagicBeans(int parHealAmount, float parSaturationModifier, 
          Block parBlockPlant, Block parSoilBlock)
    {
        super(parHealAmount, parSaturationModifier, false);
        theBlockPlant = parBlockPlant;
        soilId = parSoilBlock;
    }

    @Override
    public boolean onItemUse(ItemStack parItemStack, EntityPlayer parPlayer, 
          World parWorld, int parX, int parY, int parZ, int par7, float par8, 
          float par9, float par10)
    {
        // not sure what this parameter is for, copied this from potato
        if (par7 != 1)
        {
            return false;
        }
        // check if player can edit the block on ground and block where
        // plant will grow.  Note that the canPlayerEdit class doesn't seem to 
        // be affected by the position parameters and really just checks player 
        // and item capability to edit
        else if (parPlayer.canPlayerEdit(parX, parY+1, parZ, par7, parItemStack))
        {
            // check that the soil is a type that can sustain the plant
            // and check that there is air above to give plant room to grow
            if (parWorld.getBlock(parX, parY, parZ).canSustainPlant(parWorld, 
                  parX, parY, parZ, ForgeDirection.UP, this) && parWorld
                  .isAirBlock(parX, parY+1, parZ))
            {
                // place the plant block
            	if (theBlockPlant==null)
            	{
            		System.out.println("The plant block is null!");
            	}
                parWorld.setBlock(parX, parY+1, parZ, theBlockPlant);
                // decrement the seed item stack                
                --parItemStack.stackSize;
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
    {
        return EnumPlantType.Crop;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z)
    {
        return theBlockPlant;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
    {
        return 0;
    }

    public Block getSoilId()
    {
        return soilId;
     }
}