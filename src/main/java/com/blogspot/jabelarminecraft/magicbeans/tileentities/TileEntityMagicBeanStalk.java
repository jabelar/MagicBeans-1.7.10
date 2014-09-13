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

public class TileEntityMagicBeanStalk extends TileEntity
{
	public static boolean hasSpawnedCastleClientSide = false;
	public static boolean hasSpawnedCastleServerSide = false;
	protected int ticksExisted = 0 ;
	protected int maxStalkHeight = 70;
	

    @Override
	public void readFromNBT(NBTTagCompound parTagCompound)
    {
    	super.readFromNBT(parTagCompound);
        ticksExisted = parTagCompound.getInteger("ticksExisted");
    }

    @Override
	public void writeToNBT(NBTTagCompound parTagCompound)
    {
    	super.writeToNBT(parTagCompound);
        parTagCompound.setInteger("ticksExisted", ticksExisted);
    }
	
	@Override
	public void updateEntity()
	{
		if (worldObj.isRemote && hasSpawnedCastleClientSide)
		{
			return;
		}
		if (!worldObj.isRemote && hasSpawnedCastleServerSide)
		{
			return;
		}
		
		++ticksExisted;
		markDirty();
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, Math.min(7,  ticksExisted / 21), 2);
		if (ticksExisted >= 10 * 20) // 10 seconds
		{
			// DEBUG
			System.out.println("updateEntity() beanstalk tile entity, isRemote = "+worldObj.isRemote);
			
			// check if higher than clouds
			if (yCoord < maxStalkHeight)
			{
	    		// check if can build next growing position
	    	    if(worldObj.isAirBlock(xCoord, yCoord + 1, zCoord) && !worldObj.isRemote)
	    	    {
	    	    	// DEBUG
	    	    	System.out.println("Space above so adding more bean stalk");
	    	        worldObj.setBlock(xCoord, yCoord + 1, zCoord, MagicBeans.blockMagicBeanStalk);	    	        
	    	    }   		
 			}
			else // fully grown
			{
				if (!hasSpawnedCastleClientSide && worldObj.isRemote)
				{
					// DEBUG
					System.out.println("Look up!");
					MagicBeans.structureCastleTalia.generate(this, 0, -2, 0);
					hasSpawnedCastleClientSide = true;
				}
				if (!hasSpawnedCastleServerSide && !worldObj.isRemote)
				{
					// DEBUG
					System.out.println("Look up!");
					MagicBeans.structureCastleTalia.generate(this, 0, -2, 0);
					hasSpawnedCastleServerSide = true;
				}
			}
			
   	        // add vines all around
    	    if(worldObj.isAirBlock(xCoord+1, yCoord , zCoord))
    	    {
    	        worldObj.setBlock(xCoord+1, yCoord, zCoord, MagicBeans.blockMagicBeansVine, 2, 2);	    	        
    	    }   		
    	    if(worldObj.isAirBlock(xCoord-1, yCoord , zCoord))
    	    {
    	        worldObj.setBlock(xCoord-1, yCoord, zCoord, MagicBeans.blockMagicBeansVine, 8, 2);	    	        
    	    }   		
    	    if(worldObj.isAirBlock(xCoord, yCoord , zCoord+1))
    	    {
    	        worldObj.setBlock(xCoord, yCoord, zCoord+1, MagicBeans.blockMagicBeansVine, 4, 2);	    	        
    	    }   		
    	    if(worldObj.isAirBlock(xCoord, yCoord , zCoord-1))
    	    {
    	        worldObj.setBlock(xCoord, yCoord, zCoord-1, MagicBeans.blockMagicBeansVine, 1, 2);	
    	    }   		
		}
	}
}
