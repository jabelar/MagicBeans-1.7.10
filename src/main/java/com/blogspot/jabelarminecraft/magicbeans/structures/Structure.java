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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Structure
{
	protected String theName;
	
	protected World theWorld;
	
	protected int dimX;
	protected int dimY;
	protected int dimZ;
	
	protected int startX;
	protected int startY;
	protected int startZ;

	String[][][] blockNameArray = null;
	int[][][] blockMetaArray = null;

	BufferedReader readIn;

	public Structure(String parName)
	{
		theName = parName;
		readArrays(theName);
	}
	
	public String getName()
	{
		return theName;
	}
	
	public int getDimX()
	{
		return dimX;
	}
				
	public int getDimY()
	{
		return dimY;
	}
				
	public int getDimZ()
	{
		return dimZ;
	}
				
	public String[][][] getBlockNameArray()
	{
		return blockNameArray;
	}
	
	public int[][][] getBlockMetaArray()
	{
		return blockMetaArray;
	}
				
	protected void readArrays(String parName)
	{
	    try 
	    {
	    	System.out.println("Reading file = "+parName+".txt");
			readIn = new BufferedReader(new FileReader(parName+".txt"));
		    dimX = Integer.valueOf(readIn.readLine());
		    dimY = Integer.valueOf(readIn.readLine());
		    dimZ = Integer.valueOf(readIn.readLine());
		    blockNameArray = new String[dimX][dimY][dimZ];
		    blockMetaArray = new int[dimX][dimY][dimZ];
		    System.out.println("Dimensions of structure = "+dimX+", "+dimY+", "+dimZ);
		    for (int indY = 0; indY < dimY; indY++) // Y first to organize in vertical layers
		    {
		    	for (int indX = 0; indX < dimX; indX++)
		    	{
		    		for (int indZ = 0; indZ < dimZ; indZ++)
		    		{
		    			blockNameArray[indX][indY][indZ] = readIn.readLine();
		    			blockMetaArray[indX][indY][indZ] = Integer.valueOf(readIn.readLine());
		    		}
		    	}
		    }
		} 
	    catch (FileNotFoundException e) 
	    {
			e.printStackTrace();
		} 
	    catch (IOException e) 
	    {
			e.printStackTrace();
		}
	    
	    try 
	    {
			readIn.close();
		} 
	    catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void generate(Entity parEntity, int parOffsetX, int parOffsetY, int parOffsetZ) 
	{
		Entity theEntity = parEntity;
		theWorld = theEntity.worldObj;

		startX = (int) theEntity.posX;
		startY = (int) theEntity.posY;
		startZ = (int) theEntity.posZ;
	    
	    for (int indY = 0; indY < dimY; indY++) // Y first to organize in vertical layers
	    {
	    	for (int indX = 0; indX < dimX; indX++)
	    	{
	    		for (int indZ = 0; indZ < dimZ; indZ++)
	    		{
	    			if (blockMetaArray[indX][indY][indZ]==0)
	    			{
	    				String blockName = blockNameArray[indX][indY][indZ];
	    				if (!(blockName.equals("minecraft:tripwire"))) // tripwire/string needs to be placed after other blocks
	    				{
							theWorld.setBlock(startX+parOffsetX+indX, startY+parOffsetY+indY, startZ+parOffsetZ+indZ, 
									Block.getBlockFromName(blockName), 0, 2);
	    				}
	    			}	    			
	    		}
	    	}
	    }
	    // best to place metadata blocks after non-metadata blocks as they need to attach, etc.
	    for (int indY = 0; indY < dimY; indY++) // Y first to organize in vertical layers
	    {
	    	for (int indX = 0; indX < dimX; indX++)
	    	{
	    		for (int indZ = 0; indZ < dimZ; indZ++)
	    		{
	    			if (!(blockMetaArray[indX][indY][indZ]==0))
	    			{
						theWorld.setBlock(startX+parOffsetX+indX, startY+parOffsetY+indY, startZ+parOffsetZ+indZ, 
								Block.getBlockFromName(blockNameArray[indX][indY][indZ]), blockMetaArray[indX][indY][indZ], 2);
	    			}	    			
	    		}
	    	}
	    }
	    // some blocks with 0 metadata, like string/tripwire, require other blocks to be placed already, so do them again as last pass.
	    for (int indY = 0; indY < dimY; indY++) // Y first to organize in vertical layers
	    {
	    	for (int indX = 0; indX < dimX; indX++)
	    	{
	    		for (int indZ = 0; indZ < dimZ; indZ++)
	    		{
    				String blockName = blockNameArray[indX][indY][indZ];
    				if (blockName.equals("minecraft:tripwire"))
    				{
						theWorld.setBlock(startX+parOffsetX+indX, startY+parOffsetY+indY, startZ+parOffsetZ+indZ, 
								Block.getBlockFromName(blockName), 0, 2);
    				}	    			
	    		}
	    	}
	    }		
	}
	
	/**
	 * @param tileEntityMagicBeanStalk
	 * @param parOffsetX
	 * @param parOffsetY
	 * @param parOffsetZ
	 */
	public void generate(TileEntity parEntity, int parOffsetX, int parOffsetY, int parOffsetZ) 
	{
		TileEntity theEntity = parEntity;
		theWorld = theEntity.getWorldObj();

		startX = theEntity.xCoord;
		startY = theEntity.yCoord;
		startZ = theEntity.zCoord;
	    
	    for (int indY = 0; indY < dimY; indY++) // Y first to organize in vertical layers
	    {
	    	for (int indX = 0; indX < dimX; indX++)
	    	{
	    		for (int indZ = 0; indZ < dimZ; indZ++)
	    		{
	    			if (blockMetaArray[indX][indY][indZ]==0)
	    			{
	    				String blockName = blockNameArray[indX][indY][indZ];
	    				if (!(blockName.equals("minecraft:tripwire"))) // tripwire/string needs to be placed after other blocks
	    				{
							theWorld.setBlock(startX+parOffsetX+indX, startY+parOffsetY+indY, startZ+parOffsetZ+indZ, 
									Block.getBlockFromName(blockName), 0, 2);
	    				}
	    			}	    			
	    		}
	    	}
	    }
	    // best to place metadata blocks after non-metadata blocks as they need to attach, etc.
	    for (int indY = 0; indY < dimY; indY++) // Y first to organize in vertical layers
	    {
	    	for (int indX = 0; indX < dimX; indX++)
	    	{
	    		for (int indZ = 0; indZ < dimZ; indZ++)
	    		{
	    			if (!(blockMetaArray[indX][indY][indZ]==0))
	    			{
						theWorld.setBlock(startX+parOffsetX+indX, startY+parOffsetY+indY, startZ+parOffsetZ+indZ, 
								Block.getBlockFromName(blockNameArray[indX][indY][indZ]), blockMetaArray[indX][indY][indZ], 2);
	    			}	    			
	    		}
	    	}
	    }
	    // some blocks with 0 metadata, like string/tripwire, require other blocks to be placed already, so do them again as last pass.
	    for (int indY = 0; indY < dimY; indY++) // Y first to organize in vertical layers
	    {
	    	for (int indX = 0; indX < dimX; indX++)
	    	{
	    		for (int indZ = 0; indZ < dimZ; indZ++)
	    		{
    				String blockName = blockNameArray[indX][indY][indZ];
    				if (blockName.equals("minecraft:tripwire"))
    				{
						theWorld.setBlock(startX+parOffsetX+indX, startY+parOffsetY+indY, startZ+parOffsetZ+indZ, 
								Block.getBlockFromName(blockName), 0, 2);
    				}	    			
	    		}
	    	}
	    }		
	}

}
