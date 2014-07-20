package com.blogspot.jabelarminecraft.magicbeans.structures;

/**
Copyright (C) <2013> <coolAlias>

This file is part of coolAlias' Structure Generation Tool; as such,
you can redistribute it and/or modify it under the terms of the GNU
General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class Structure
{
	/** The block array map of the structure */
	private int[][][] blockArray;
	
	/** Stores the direction this structure faces. Default is EAST.*/
	private ForgeDirection facing = ForgeDirection.EAST;
	
	/** Origin representing location in chunk of the 0, 0, 0 block	 */
	private int originX = 0, originY = 0, originZ = 0;
	
	/** Amount to offset structure's location relative to orign 
	 * i.e if you want to control the position of the door as origin.
	*/
	private int offsetX = 0, offsetY = 0, offsetZ = 0;

	/** create constants for easier referencing of blocks in the array */
	protected final static int AIR = b(Blocks.air);
	protected final static int WOOD = b(Blocks.log);
	protected final static int DOOR = b(Blocks.wooden_door);
	protected final static int CHEST = b(Blocks.chest);
	protected final static int BED = b(Blocks.bed);
	protected final static int GLASS = b(Blocks.glass);
	protected final static int TORCH = b(Blocks.torch);

	public Structure() 
	{
	}
	
	public Structure(int[][][] parBlockArray)
	{
		setBlockArray(parBlockArray);
	}
	
	public void generateStructure(World parWorld, ForgeDirection parFacing, int parOriginX, 
			int parOriginY, int parOriginZ, int parOffsetX, int parOffsetY, int parOffsetZ) 
	{
		if (parWorld.isRemote)
		{
			return ; // do nothing if on client side
		}
		
		setFacing(parFacing);
		setOrigin(parOriginX, parOriginY, parOriginZ);
		setOffset(parOffsetX, parOffsetY, parOffsetZ);
		
		// create blocks
		for (int y=0; y < getArrayHeight(); y++)
		{
			for (int z=0; z < getArrayDepth(); z++)
			{
				for (int x=0; x < getArrayWidth(); x++)
				{
					parWorld.setBlock(getOriginX()-getOffsetX()+x, getOriginY()-getOffsetY()+y, getOriginZ()-getOffsetZ()+z, Block.getBlockById(blockArray[y][z][x]));
				}
			}
		}		
	}
	
	public final void setOrigin(int parOriginX, int parOriginY,int parOriginZ) 
	{
		originX = parOriginX;
		originY = parOriginY;
		originZ = parOriginZ;		
	}

	public final void setBlockArray(int[][][] parBlockArray)
	{
		blockArray = parBlockArray;
	}

	public final void setFacing(ForgeDirection parFacing) 
	{
		facing = parFacing;
	}


	public final int getOriginX() 
	{
		return originX;
	}

	public final int getOriginY() 
	{
		return originY;
	}

	public final int getOriginZ() 
	{
		return originZ;
	}

	public final int getOffsetX() 
	{
		return offsetX;
	}

	public final int getOffsetY() 
	{
		return offsetY;
	}

	public final int getOffsetZ() 
	{
		return offsetZ;
	}

	public final void setOffset(int x, int y, int z)
	{
		offsetX = x;
		offsetZ = z;
		offsetY = y;
	}
	
	/* 
	 * Helper functions
	 */
	
	/** save typing with helper function */
	protected static int b(Block parBlock)
	{
		return Block.getIdFromBlock(parBlock);
	}

	// since array is "top view", the first index is actually the layer which indicates height
	public final int getArrayHeight()
	{
		return blockArray.length;
	}
	
	public final int getArrayDepth()
	{
		// assumes non-jagged array
		return blockArray[0].length;
	}

	public final int getArrayWidth()
	{
		// assumes non-jagged array
		return blockArray[0][0].length;
	}
}