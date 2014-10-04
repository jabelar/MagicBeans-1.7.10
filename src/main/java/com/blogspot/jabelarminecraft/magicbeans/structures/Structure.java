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
import java.io.IOException;
import java.io.InputStreamReader;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;

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
	
	public boolean shouldGenerate = false;
	public boolean finishedGeneratingBasic = false; // basic block generation
	public boolean finishedGeneratingMeta = false; // blocks with metadata generation
	public boolean finishedGeneratingSpecial = false; // special blocks like tripwire
	protected int ticksGenerating = 0;

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
			readIn = new BufferedReader(new InputStreamReader(getClass().getClassLoader()
					.getResourceAsStream("assets/magicbeans/structures/"+parName+".txt"), "UTF-8"));
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
		if (theWorld.isRemote)
		{
			return;
		}

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

		// DEBUG
		System.out.println("Generating castle in the clouds. IsRemote = "+theWorld.isRemote);

		if (theWorld.isRemote)
		{
			return;
		}

		startX = theEntity.xCoord;
		startY = theEntity.yCoord;
		startZ = theEntity.zCoord;
		
		// generate the cloud
		generateCloud(theWorld, startX, startY, startZ, 75);
	    
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
	public void generateTick(TileEntity parEntity, int parOffsetX, int parOffsetY, int parOffsetZ) 
	{
		// exit if generating not started or if finished
		if (!shouldGenerate || finishedGeneratingSpecial)
		{
			return;
		}
		
		TileEntity theEntity = parEntity;
		theWorld = theEntity.getWorldObj();

		if (theWorld.isRemote)
		{
			return;
		}

		startX = theEntity.xCoord;
		startY = theEntity.yCoord;
		startZ = theEntity.zCoord;
		
		int totalVolume = dimX * dimY * dimZ;
		
		// generate the cloud
//		generateCloud(theWorld, startX, startY, startZ, 75);
		
		if (!finishedGeneratingBasic)
		{
			int indY = ticksGenerating/(dimX*dimZ);
			int indX = (ticksGenerating-indY*dimX*dimZ)/dimZ;

			for (int indZ = 0; indZ < dimZ; indZ++)
			{
				// DEBUG
				System.out.println("Generating basic blocks at "+indY+", "+indX+", "+indZ);

				if (blockMetaArray[indX][indY][indZ]==0) // check for basic block
				{
					String blockName = blockNameArray[indX][indY][indZ];
					if (!(blockName.equals("minecraft:tripwire"))) // tripwire/string needs to be placed after other blocks
					{
						theWorld.setBlock(startX+parOffsetX+indX, startY+parOffsetY+indY, startZ+parOffsetZ+indZ, 
								Block.getBlockFromName(blockName), 0, 2);
					}
				}
			}
			
			ticksGenerating += dimZ;
			if (ticksGenerating >= totalVolume)
			{
				// DEBUG
				System.out.println("Finishing generation basic blocks with dimX = "+dimX+" dimY = "+dimY+" dimZ = "+dimZ);
				finishedGeneratingBasic = true;
				ticksGenerating = 0;
			}
		}
		else if (!finishedGeneratingMeta)
		{
			int indY = ticksGenerating/(dimX*dimZ);
			int indX = (ticksGenerating-indY*dimX*dimZ)/dimZ;
			
			for (int indZ = 0; indZ < dimZ; indZ++)
			{
				// DEBUG
				System.out.println("Generating meta blocks at "+indY+", "+indX+", "+indZ);
	
				if (!(blockMetaArray[indX][indY][indZ]==0))
				{
					theWorld.setBlock(startX+parOffsetX+indX, startY+parOffsetY+indY, startZ+parOffsetZ+indZ, 
							Block.getBlockFromName(blockNameArray[indX][indY][indZ]), blockMetaArray[indX][indY][indZ], 2);
				}	
			}
			
			ticksGenerating += dimZ;
			if (ticksGenerating >= totalVolume)
			{
				// DEBUG
				System.out.println("Finishing generation meta blocks with dimX = "+dimX+" dimY = "+dimY+" dimZ = "+dimZ);
				finishedGeneratingMeta = true;
				ticksGenerating = 0;
			}
		}
		else if (!finishedGeneratingSpecial)
		{
			int indY = ticksGenerating/(dimX*dimZ);
			int indX = (ticksGenerating-indY*dimX*dimZ)/dimZ;

			for (int indZ = 0; indZ < dimZ; indZ++)
			{
				// DEBUG
				System.out.println("Generating special blocks at "+indY+", "+indX+", "+indZ);
	
				String blockName = blockNameArray[indX][indY][indZ];
				if (blockName.equals("minecraft:tripwire"))
				{
					theWorld.setBlock(startX+parOffsetX+indX, startY+parOffsetY+indY, startZ+parOffsetZ+indZ, 
							Block.getBlockFromName(blockName), 0, 2);
				}	    	
			}
			
			ticksGenerating += dimZ;
			if (ticksGenerating >= totalVolume)
			{
				// DEBUG
				System.out.println("Finishing generation special blocks with dimX = "+dimX+" dimY = "+dimY+" dimZ = "+dimZ);
				finishedGeneratingSpecial = true;
				ticksGenerating = 0;
			}
		}
		
		
//	    // best to place metadata blocks after non-metadata blocks as they need to attach, etc.
//	    for (int indY = 0; indY < dimY; indY++) // Y first to organize in vertical layers
//	    {
//	    	for (int indX = 0; indX < dimX; indX++)
//	    	{
//	    		for (int indZ = 0; indZ < dimZ; indZ++)
//	    		{
//	    			if (!(blockMetaArray[indX][indY][indZ]==0))
//	    			{
//						theWorld.setBlock(startX+parOffsetX+indX, startY+parOffsetY+indY, startZ+parOffsetZ+indZ, 
//								Block.getBlockFromName(blockNameArray[indX][indY][indZ]), blockMetaArray[indX][indY][indZ], 2);
//	    			}	    			
//	    		}
//	    	}
//	    }
//	    // some blocks with 0 metadata, like string/tripwire, require other blocks to be placed already, so do them again as last pass.
//	    for (int indY = 0; indY < dimY; indY++) // Y first to organize in vertical layers
//	    {
//	    	for (int indX = 0; indX < dimX; indX++)
//	    	{
//	    		for (int indZ = 0; indZ < dimZ; indZ++)
//	    		{
//    				String blockName = blockNameArray[indX][indY][indZ];
//    				if (blockName.equals("minecraft:tripwire"))
//    				{
//						theWorld.setBlock(startX+parOffsetX+indX, startY+parOffsetY+indY, startZ+parOffsetZ+indZ, 
//								Block.getBlockFromName(blockName), 0, 2);
//    				}	    			
//	    		}
//	    	}
//	    }		
	}
	
	public void generateCloud(World parWorld, int parX, int parY, int parZ, int parCloudSize) 
	{	
		// DEBUG
		System.out.println("Generating cloud");
		
		if (parWorld.isRemote)
		{
			return;
		}

		for (int indX = parX-parCloudSize/2; indX < parX+parCloudSize/2; indX++)
		{
			for (int indZ = parZ-parCloudSize/2; indZ < parZ+parCloudSize/2; indZ++)
			{
				parWorld.setBlockToAir(indX, parY-1, indZ);
				parWorld.setBlock(indX, parY-1, indZ, MagicBeans.blockCloud, 0, 2);
			}
		}
	}

}
