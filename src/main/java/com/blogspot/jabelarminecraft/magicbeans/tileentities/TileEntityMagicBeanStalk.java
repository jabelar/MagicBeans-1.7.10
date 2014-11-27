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

package com.blogspot.jabelarminecraft.magicbeans.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.MagicBeansWorldData;

public class TileEntityMagicBeanStalk extends TileEntity
{
//	public boolean hasSpawnedCastle = false;
	protected int ticksExisted = 0 ;	

    @Override
	public void readFromNBT(NBTTagCompound parTagCompound)
    {
    	super.readFromNBT(parTagCompound);
        ticksExisted = parTagCompound.getInteger("ticksExisted");
//        hasSpawnedCastle = parTagCompound.getBoolean("hasSpawnedCastle");
    }

    @Override
	public void writeToNBT(NBTTagCompound parTagCompound)
    {
    	super.writeToNBT(parTagCompound);
        parTagCompound.setInteger("ticksExisted", ticksExisted);
//        parTagCompound.setBoolean("hasSpawnedCastle", hasSpawnedCastle);
    }
    	
	@Override
	public void updateEntity()
	{
		if (worldObj.isRemote || MagicBeansWorldData.get(worldObj).getHasCastleSpwaned())
		{
			return;
		}
		
		++ticksExisted;
		markDirty();
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, Math.min(7,  ticksExisted / MagicBeans.configTicksPerGrowStage), 2);
		if (ticksExisted >= MagicBeans.configTicksPerGrowStage * 9) 
		{
			// check if higher than clouds
			if (yCoord < MagicBeans.configMaxStalkHeight)
			{
	    		// check if can build next growing position
	    	    if(worldObj.isAirBlock(xCoord, yCoord + 1, zCoord))
	    	    {
	    	    	// DEBUG
	    	    	// System.out.println("Beanstalk still growing, hasSpawnedCastle = "+hasSpawnedCastle);
	    	        worldObj.setBlock(xCoord, yCoord + 1, zCoord, MagicBeans.blockMagicBeanStalk);	    	        
	    	    }   		
 			}
			else // fully grown
			{
				MagicBeans.structureCastleTalia.shouldGenerate = true;
				MagicBeans.structureCastleTalia.generateTick(this, 5, -2, 5);
				// MagicBeansWorldData.get(worldObj).setHasCastleSpawned(MagicBeans.structureCastleTalia.finishedPopulatingEntities);
			}
		}
	}
}
