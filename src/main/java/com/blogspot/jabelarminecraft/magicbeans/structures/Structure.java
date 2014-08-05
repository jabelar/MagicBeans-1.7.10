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

package com.blogspot.jabelarminecraft.magicbeans.structures;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class Structure
{
	/** The block array map of the structure */
	private int[][][] blockArray;
	/** The associated metadata for the blocks in the structure */
	private int[][][] metaDataArray;
	
	/** Stores the direction this structure faces. Default is NORTH.*/
	public final static int DIR_NORTH = 0;
	public final static int DIR_EAST = 1;
	public final static int DIR_SOUTH = 2;
	public final static int DIR_WEST = 4;
	private int facing = DIR_NORTH;
	
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
	
	/** create constants for easier referencing of metadata values in the array */
	/** see http://minecraft.gamepedia.com/Data_values#Data for metadata values for blocks */
	/** some can be added together bitwise */
	// No metadata
	protected final static int MNONE = 0;
	// Torches and Redstone Torches
	protected final static int MTCHE = 1; // Torch East
	protected final static int MTCHW = 2; // Torch West
	protected final static int MTCHN = 3; // Torch North
	protected final static int MTCHS = 4; // Torch South
	protected final static int MTRCH = 5; // free standing
	// Slabs
	protected final static int MSLBN = 0; // Slab normal
	protected final static int MSLBU = 8; // Slab upside-down
    // Beds, foot
	protected final static int MBDFS = 0; // Bed foot to South
	protected final static int MBDFW = 1; // Bed foot to West
	protected final static int MBDFN = 2; // Bed foot to North
	protected final static int MBDFE = 3; // Bed foot to East
    // Beds, head
	protected final static int MBDHS = 8; // Bed head to South
	protected final static int MBDHW = 9; // Bed head to West
	protected final static int MBDHN = 10; // Bed head to North
	protected final static int MBDHE = 11; // Bed head to East
    // Stairs
	protected final static int MSTAE = 0; // Stairs ascending to East
	protected final static int MSTAW = 1; // Stairs ascending to West
	protected final static int MSTAS = 2; // Stairs ascending to South
	protected final static int MSTAN = 3; // Stairs ascending to North
	// Door, top
	protected final static int MDRTR = 8; // Door top, hinge right
	protected final static int MDRTL = 9; // Door top, hinge left
	// Door, bottom
	protected final static int MDRBW = 0; // Door bottom, facing West
	protected final static int MDRBN = 1; // Door bottom, facing North
	protected final static int MDRBE = 2; // Door bottom, facing East
	protected final static int MDRBS = 3; // Door bottom, facing South	

	public Structure() 
	{
	}
	
	public Structure(int[][][] parBlockArray, int[][][] parMetaDataArray)
	{
		setBlockArray(parBlockArray);
		setMetaDataArray(parMetaDataArray);
	}
	
	/**
	 * @param parMetaDataArray
	 */
	protected void setMetaDataArray(int[][][] parMetaDataArray) 
	{
		metaDataArray = parMetaDataArray;		
	}

	public void generateStructure(World parWorld, int parFacing, int parOriginX, 
			int parOriginY, int parOriginZ, int parOffsetX, int parOffsetY, int parOffsetZ) 
	{
		if (parWorld.isRemote)
		{
			return ; // do nothing if on client side
		}
		
		setFacing(parFacing);
		setOrigin(parOriginX, parOriginY, parOriginZ);
		setOffset(parOffsetX, parOffsetY, parOffsetZ);

		switch (getFacing())
		{
		case DIR_NORTH:
		{
			//DEBUG
			System.out.println("Structure facing North");
			generateFacingNorth(parWorld);
			break;
		}
		case DIR_EAST:
		{
			//DEBUG
			System.out.println("Structure facing East");
			generateFacingEast(parWorld);
			break;
		}
		case DIR_SOUTH:
		{
			//DEBUG
			System.out.println("Structure facing South");
			generateFacingSouth(parWorld);
			break;
		}
		case DIR_WEST:
		{
			//DEBUG
			System.out.println("Structure facing West");
			generateFacingWest(parWorld);
			break;
		}
		default:
			break;
			
		}
	}
	
	protected void generateFacingNorth(World parWorld)
	{
		// create blocks
		for (int y=0; y < getArrayHeight(); y++)
		{
			for (int z=0; z < getArrayDepth(); z++)
			{
				for (int x=0; x < getArrayWidth(); x++)
				{
					parWorld.setBlock(getOriginX()-getOffsetX()+x, getOriginY()-getOffsetY()+y, getOriginZ()-getOffsetZ()+z, 
							Block.getBlockById(blockArray[y][z][x]), metaDataArray[y][z][x], 2);
				}
			}
		}				
	}

	/** East is 90 degrees clockwise, means swap X and Z and use -Z */
	protected void generateFacingEast(World parWorld)
	{
		// create blocks
		for (int y=0; y < getArrayHeight(); y++)
		{
			for (int z=0; z < getArrayDepth(); z++)
			{
				for (int x=0; x < getArrayWidth(); x++)
				{
					parWorld.setBlock(getOriginX()+getOffsetZ()-z, getOriginY()-getOffsetY()+y, getOriginZ()-getOffsetX()+x, 
							Block.getBlockById(blockArray[y][z][x]), metaDataArray[y][z][x], 2);
				}
			}
		}				
	}

	/** West is 90 degrees anticlockwise, means swap X and Z and use -X */
	protected void generateFacingWest(World parWorld)
	{
		// create blocks
		for (int y=0; y < getArrayHeight(); y++)
		{
			for (int z=0; z < getArrayDepth(); z++)
			{
				for (int x=0; x < getArrayWidth(); x++)
				{
					parWorld.setBlock(getOriginX()-getOffsetZ()+z, getOriginY()-getOffsetY()+y, getOriginZ()+getOffsetX()-x, 
							Block.getBlockById(blockArray[y][z][x]), metaDataArray[y][z][x], 2);
				}
			}
		}				
	}
	
	protected void generateFacingSouth(World parWorld)
	{
		// create blocks
		for (int y=0; y < getArrayHeight(); y++)
		{
			for (int z=0; z < getArrayDepth(); z++)
			{
				for (int x=0; x < getArrayWidth(); x++)
				{
					parWorld.setBlock(getOriginX()+getOffsetX()-x, getOriginY()-getOffsetY()+y, getOriginZ()-getOffsetZ()+z, 
							Block.getBlockById(blockArray[y][z][x]), metaDataArray[y][z][x], 2);
				}
			}
		}				
	}



	private int getFacing() 
	{
		return facing;
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

	public final void setFacing(int parFacing) 
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