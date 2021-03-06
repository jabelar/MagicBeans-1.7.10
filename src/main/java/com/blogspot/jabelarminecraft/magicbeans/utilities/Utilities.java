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

package com.blogspot.jabelarminecraft.magicbeans.utilities;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.util.BlockSnapshot;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.entities.IModEntity;
import com.blogspot.jabelarminecraft.magicbeans.networking.MessageSyncEntityToClient;
import com.blogspot.jabelarminecraft.magicbeans.networking.MessageSyncEntityToServer;

/**
 * @author jabelar
 *
 */
public class Utilities 
{
	/*
	 * Text Utilities
	 */
	
	public static String stringToRainbow(String parString, boolean parReturnToBlack)
	{
		int stringLength = parString.length();
		if (stringLength < 1)
		{
			return "";
		}
		String outputString = "";
		EnumChatFormatting[] colorChar = 
			{
			EnumChatFormatting.RED,
			EnumChatFormatting.GOLD,
			EnumChatFormatting.YELLOW,
			EnumChatFormatting.GREEN,
			EnumChatFormatting.AQUA,
			EnumChatFormatting.BLUE,
			EnumChatFormatting.LIGHT_PURPLE,
			EnumChatFormatting.DARK_PURPLE
			};
		for (int i = 0; i < stringLength; i++)
		{
			outputString = outputString+colorChar[i%8]+parString.substring(i, i+1);
		}
		// return color to a common one after (most chat is white, but for other GUI might want black)
		if (parReturnToBlack)
		{
			return outputString+EnumChatFormatting.BLACK;
		}
		return outputString+EnumChatFormatting.WHITE;
	}

	// by default return to white (for chat formatting).
	public static String stringToRainbow(String parString)
	{
		return stringToRainbow(parString, false);
	}
	
	public static String stringToGolden(String parString, int parShineLocation, boolean parReturnToBlack)
	{
		int stringLength = parString.length();
		if (stringLength < 1)
		{
			return "";
		}
		String outputString = "";
		for (int i = 0; i < stringLength; i++)
		{
			if ((i+parShineLocation+Minecraft.getSystemTime()/20)%88==0)
			{
				outputString = outputString+EnumChatFormatting.WHITE+parString.substring(i, i+1);				
			}
			else if ((i+parShineLocation+Minecraft.getSystemTime()/20)%88==1)
			{
				outputString = outputString+EnumChatFormatting.YELLOW+parString.substring(i, i+1);				
			}
			else if ((i+parShineLocation+Minecraft.getSystemTime()/20)%88==87)
			{
				outputString = outputString+EnumChatFormatting.YELLOW+parString.substring(i, i+1);				
			}
			else
			{
				outputString = outputString+EnumChatFormatting.GOLD+parString.substring(i, i+1);								
			}
		}
		// return color to a common one after (most chat is white, but for other GUI might want black)
		if (parReturnToBlack)
		{
			return outputString+EnumChatFormatting.BLACK;
		}
		return outputString+EnumChatFormatting.WHITE;
	}

	// by default return to white (for chat formatting).
	public static String stringToGolden(String parString, int parShineLocation)
	{
		return stringToGolden(parString, parShineLocation, false);
	}

	public static Entity getEntityByID(int entityID, World world)        
	{         
	    for(Object o: world.getLoadedEntityList())                
	    {                        
	        if(((Entity)o).getEntityId() == entityID)                        
	        {   
	        	// DEBUG
	            // System.out.println("Found the entity");                                
	            return ((Entity)o);                        
	        }                
	    }                
	    return null;        
	} 

	/**
	* Based on code from http://pages.cs.wisc.edu/~ltorrey/cs302/examples/PigLatinTranslator.java
	* Method to translate a sentence word by word.
	* @param s The sentence in English
	* @return The pig latin version
	*/
	public static String toPigLatin(String s) 
	{
		String latin = "";
	    int i = 0;
	    while (i<s.length()) 
	    {
	    	// Take care of punctuation and spaces
	    	while (i<s.length() && !isLetter(s.charAt(i))) 
	    	{
	    		latin = latin + s.charAt(i);
	    		i++;
	    	}

	    	// If there aren't any words left, stop.
	    	if (i>=s.length()) break;

	    	// Otherwise we're at the beginning of a word.
	    	int begin = i;
	    	while (i<s.length() && isLetter(s.charAt(i))) 
	    	{
	    		i++;
	    	}

	    	// Now we're at the end of a word, so translate it.
	    	int end = i;
	    	latin = latin + pigWord(s.substring(begin, end));
	    }
	    return latin;
	}

	/**
	* Method to test whether a character is a letter or not.
	* @param c The character to test
	* @return True if it's a letter
	*/
	private static boolean isLetter(char c) 
	{
		return ( (c >='A' && c <='Z') || (c >='a' && c <='z') );
	}

	/**
	* Method to translate one word into pig latin.
	* @param word The word in english
	* @return The pig latin version
	*/
	private static String pigWord(String word) 
	{
		int split = firstVowel(word);
		return word.substring(split)+"-"+word.substring(0, split)+"ay";
	}

	/**
	* Method to find the index of the first vowel in a word.
	* @param word The word to search
	* @return The index of the first vowel
	*/
	private static int firstVowel(String word) 
	{
		word = word.toLowerCase();
	    for (int i=0; i<word.length(); i++)
	    {
	    	if (word.charAt(i)=='a' || word.charAt(i)=='e' ||
	    	      word.charAt(i)=='i' || word.charAt(i)=='o' ||
	              word.charAt(i)=='u')
	    	{
	    		return i;
	    	}
	    }
	    	return 0;
	}
	  
	/*
	 * Networking packet utilities
	 */
	
    public static void sendEntitySyncPacketToClient(IModEntity parEntity) 
    {
    	Entity theEntity = (Entity)parEntity;
        if (!theEntity.worldObj.isRemote)
        {
        	// DEBUG
        	System.out.println("sendEntitySyncPacket from server");
            MagicBeans.network.sendToAll(new MessageSyncEntityToClient(theEntity.getEntityId(), parEntity.getSyncDataCompound()));           
        }
    }

    public static void sendEntitySyncPacketToServer(IModEntity parEntity) 
    {
    	Entity theEntity = (Entity)parEntity;
        if (theEntity.worldObj.isRemote)
        {
        	// DEBUG
        	System.out.println("sendEntitySyncPacket from client");
            MagicBeans.network.sendToServer(new MessageSyncEntityToServer(theEntity.getEntityId(), parEntity.getSyncDataCompound()));           
        }
    }
    
    /**
     * Sets the block ID and metadata at a given location. Args: X, Y, Z, new block ID, new metadata, flags. Flag 1 will
     * cause a block update. Flag 2 will send the change to clients (you almost always want parChunk). Flag 4 prevents the
     * block from being re-rendered, if parChunk is a client world. Flags can be added together.
     */
    public static boolean setBlockFast(World parWorld, int parX, int parY, int parZ, Block parBlock, int parMetaData, int parFlag)
    {
        // Make sure position is within valid range
        if (parX >= -30000000 && parZ >= -30000000 && parX < 30000000 && parZ < 30000000)
        {
            if (parY < 0)
            {
                return false;
            }
            else if (parY >= 256)
            {
                return false;
            }
            else
            {
                Chunk chunk = parWorld.getChunkFromChunkCoords(parX >> 4, parZ >> 4);
                Block existingBlock = null;
                BlockSnapshot blockSnapshot = null;

                if ((parFlag & 1) != 0)
                {
                    existingBlock = chunk.getBlock(parX & 15, parY, parZ & 15);
                }

                if (parWorld.captureBlockSnapshots && !parWorld.isRemote)
                {
                    blockSnapshot = BlockSnapshot.getBlockSnapshot(parWorld, parX, parY, parZ, parFlag);
                    parWorld.capturedBlockSnapshots.add(blockSnapshot);
                }

                boolean setBlockSuceeded = setBlockInChunkFast(chunk, parX & 15, parY, parZ & 15, parBlock, parMetaData);

                if (!setBlockSuceeded && blockSnapshot != null)
                {
                    parWorld.capturedBlockSnapshots.remove(blockSnapshot);
                    blockSnapshot = null;
                }

//                parWorld.theProfiler.startSection("checkLight");
//                parWorld.func_147451_t(parX, parY, parZ);
//                parWorld.theProfiler.endSection();

                if (setBlockSuceeded && blockSnapshot == null) // Don't notify clients or update physics while capturing blockstates
                {
                    // Modularize client and physic updates
                    parWorld.markAndNotifyBlock(parX, parY, parZ, chunk, existingBlock, parBlock, parFlag);
                }

                return setBlockSuceeded;
            }
        }
        else
        {
            return false;
        }
    }

    public static boolean setBlockInChunkFast(Chunk parChunk, int parX, int parY, int parZ, Block parBlock, int parMetaData)
    {
        int mapKey = parZ << 4 | parX;

        if (parY >= parChunk.precipitationHeightMap[mapKey] - 1)
        {
            parChunk.precipitationHeightMap[mapKey] = -999;
        }

        int currentHeight = parChunk.heightMap[mapKey];
        Block existingBlock = parChunk.getBlock(parX, parY, parZ);
        int existingMetaData = parChunk.getBlockMetadata(parX, parY, parZ);

        if (existingBlock == parBlock && existingMetaData == parMetaData)
        {
            return false;
        }
        else
        {
            ExtendedBlockStorage extendedblockstorage = parChunk.getBlockStorageArray()[parY >> 4];
            boolean flag = false;

            if (extendedblockstorage == null)
            {
                if (parBlock == Blocks.air)
                {
                    return false;
                }

                extendedblockstorage = parChunk.getBlockStorageArray()[parY >> 4] = new ExtendedBlockStorage(parY >> 4 << 4, !parChunk.worldObj.provider.hasNoSky);
                flag = parY >= currentHeight;
            }

            int worldPosX = parChunk.xPosition * 16 + parX;
            int worldPosZ = parChunk.zPosition * 16 + parZ;

//            int existingLightOpacity = existingBlock.getLightOpacity(parChunk.worldObj, worldPosX, parY, worldPosZ);

            if (!parChunk.worldObj.isRemote)
            {
                existingBlock.onBlockPreDestroy(parChunk.worldObj, worldPosX, parY, worldPosZ, existingMetaData);
            }

            extendedblockstorage.setExtBlockID(parX, parY & 15, parZ, parBlock);
            extendedblockstorage.setExtBlockMetadata(parX, parY & 15, parZ, parMetaData); // This line duplicates the one below, so breakBlock fires with valid worldstate

            if (!parChunk.worldObj.isRemote)
            {
                existingBlock.breakBlock(parChunk.worldObj, worldPosX, parY, worldPosZ, existingBlock, existingMetaData);
                // After breakBlock a phantom TE might have been created with incorrect meta. This attempts to kill that phantom TE so the normal one can be create properly later
                TileEntity te = parChunk.getTileEntityUnsafe(parX & 0x0F, parY, parZ & 0x0F);
                if (te != null && te.shouldRefresh(existingBlock, parChunk.getBlock(parX & 0x0F, parY, parZ & 0x0F), existingMetaData, parChunk.getBlockMetadata(parX & 0x0F, parY, parZ & 0x0F), parChunk.worldObj, worldPosX, parY, worldPosZ))
                {
                    parChunk.removeTileEntity(parX & 0x0F, parY, parZ & 0x0F);
                }
            }
            else if (existingBlock.hasTileEntity(existingMetaData))
            {
                TileEntity te = parChunk.getTileEntityUnsafe(parX & 0x0F, parY, parZ & 0x0F);
                if (te != null && te.shouldRefresh(existingBlock, parBlock, existingMetaData, parMetaData, parChunk.worldObj, worldPosX, parY, worldPosZ))
                {
                    parChunk.worldObj.removeTileEntity(worldPosX, parY, worldPosZ);
                }
            }

            if (extendedblockstorage.getBlockByExtId(parX, parY & 15, parZ) != parBlock)
            {
                return false;
            }
            else
            {
                extendedblockstorage.setExtBlockMetadata(parX, parY & 15, parZ, parMetaData);

//                if (flag)
//                {
//                    parChunk.generateSkylightMap();
//                }
//                else
//                {
//                    int newLightOpacity = parBlock.getLightOpacity(parChunk.worldObj, worldPosX, parY, worldPosZ);
//
//                    if (newLightOpacity > 0)
//                    {
//                        if (parY >= currentHeight)
//                        {
//                            parChunk.relightBlock(parX, parY + 1, parZ);
//                        }
//                    }
//                    else if (parY == currentHeight - 1)
//                    {
//                        parChunk.relightBlock(parX, parY, parZ);
//                    }
//
//                    if (newLightOpacity != existingLightOpacity && (newLightOpacity < existingLightOpacity || parChunk.getSavedLightValue(EnumSkyBlock.Sky, parX, parY, parZ) > 0 || parChunk.getSavedLightValue(EnumSkyBlock.Block, parX, parY, parZ) > 0))
//                    {
//                        parChunk.propagateSkylightOcclusion(parX, parZ);
//                    }
//                }

                TileEntity tileentity;

                if (!parChunk.worldObj.isRemote)
                {
                    parBlock.onBlockAdded(parChunk.worldObj, worldPosX, parY, worldPosZ);
                }

                if (parBlock.hasTileEntity(parMetaData))
                {
                    tileentity = parChunk.getBlockTileEntityInChunk(parX, parY, parZ);

                    if (tileentity != null)
                    {
                        tileentity.updateContainingBlockInfo();
                        tileentity.blockMetadata = parMetaData;
                    }
                }

                parChunk.isModified = true;
                return true;
            }
        }
    }
}
