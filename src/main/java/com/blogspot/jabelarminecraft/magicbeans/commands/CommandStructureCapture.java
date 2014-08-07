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

package com.blogspot.jabelarminecraft.magicbeans.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

/**
 * @author jabelar
 *
 */
public class CommandStructureCapture implements ICommand
{
	private final List aliases;
	
	World theWorld;
	Entity thePlayer;
	public int[][][] blockIdArray ;
	public String[][][] blockNameArray ;
    public int[][][] blockMetaArray ;
    int startX ;
    int startY ;
    int startZ ;
    int endX ;
    int endY ;
    int endZ ;
    int dimX ;
    int dimY ;
    int dimZ ;
    int signX ;
    int signY ;
    int signZ ;
    
    
	// TODO
	// ultimately need to pass structures by name to make more generic
	
	public CommandStructureCapture()
	{
		    aliases = new ArrayList();
		    aliases.add("capture");
		    aliases.add("capt");
	}
	
	@Override
	public int compareTo(Object o) 
	{
		return 0;
	}

	@Override
	public String getCommandName() 
	{
		return "capture";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) 
	{
		return "capture <int> <int> <int> <int> <int> <int>"; // use "structure <text>"; later when passing name of structure
	}

	@Override
	public List getCommandAliases() 
	{
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] argString) 
	{
		theWorld = sender.getEntityWorld();
		
		if (theWorld.isRemote)
		{
			System.out.println("Not processing on Client side");
		}
		else
		{
			System.out.println("Processing on Server side");

			if(argString.length != 6)
		    {
		    	sender.addChatMessage(new ChatComponentText("Invalid argument"));
		    	return;
		    }
			
		    thePlayer = sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName());
		    startX = Integer.parseInt(argString[0]);
		    startY = Integer.parseInt(argString[1]);
		    startZ = Integer.parseInt(argString[2]);
		    endX = Integer.parseInt(argString[3]);
		    endY = Integer.parseInt(argString[4]);
		    endZ = Integer.parseInt(argString[5]);
		    dimX = Math.abs(endX - startX);
		    dimY = Math.abs(endY - startY);
		    dimZ = Math.abs(endZ - startZ);
		    signX = dimX / (endX - startX);
		    signY = dimY / (endY - startY);
		    signZ = dimZ / (endZ - startZ);
			if(dimX*dimY*dimZ > 64*64*64)
		    {
		    	sender.addChatMessage(new ChatComponentText("Capture area too big"));
		    	return;
		    }
		    
		    sender.addChatMessage(new ChatComponentText("Capturing Structure from "+startX+", "+startY+", "+
		         startZ+" to "+endX+", "+endY+", "+endZ));
		    blockIdArray = new int[dimX][dimY][dimZ];
		    blockNameArray = new String[dimX][dimY][dimZ];
		    blockMetaArray = new int[dimX][dimY][dimZ];

		    for (int i = 0; i < dimY; i++) // Y first to organize in vertical layers
		    {
		    	for (int j = 0; j < dimX; j++)
		    	{
		    		for (int k = 0; k < dimZ; k++)
		    		{
//		    			blockIdArray[j][i][k] = Block.getIdFromBlock(theWorld.getBlock(startX+signX*j, startY+signY*i, 
//		    					startZ+signZ*k));
		    			blockNameArray[j][i][k] = Block.blockRegistry.getNameForObject(theWorld.getBlock(startX+signX*j, startY+signY*i, 
		    					startZ+signZ*k));
		    			blockMetaArray[j][i][k] = theWorld.getBlockMetadata(startX+signX*j, startY+signY*i, 
		    					startZ+signZ*k);		    			
		    		}
		    	}
		    }

//		    printIdArray();
		    printNameArray();
		    printMetaArray();
		}
	}
	
	protected void printIdArray()
	{
		System.out.println("// Block ID Array");
		System.out.println("{");
	    for (int i = 0; i < dimY; i++) // Y first to organize in layers
	    {
	    	System.out.println("    {   // Layer ="+i);
	    	for (int j = 0; j < dimX; j++)
	    	{
	    		String row = "";
	    		for (int k = 0; k < dimZ; k++)
	    		{
	    			if (k < dimZ-1) // not last element in row
	    			{
		    			row = row+blockIdArray[j][i][k]+", ";	    				
	    			}
	    			else // last element in row
	    			{
		    			row = row+blockIdArray[j][i][k];	    				
	    			}
	    		}
	    		if (j < dimX-1) // not last element in column
	    		{
		    		System.out.println("        { "+row+" },");
	    		}
	    		else // last element in column
	    		{
		    		System.out.println("        { "+row+" }");
	    		}
	    	}
    		if (i < dimY-1) // not last layer
    		{
	    		System.out.println("    },");
    		}
    		else // last layer
    		{
	    		System.out.println("    }");
    		}
	    }	
		System.out.println("};");
	}

	protected void printNameArray()
	{
		System.out.println("// Block Name Array");
		System.out.println("{");
	    for (int i = 0; i < dimY; i++) // Y first to organize in layers
	    {
	    	System.out.println("    {   // Layer ="+i);
	    	for (int j = 0; j < dimX; j++)
	    	{
	    		String row = "";
	    		for (int k = 0; k < dimZ; k++)
	    		{
	    			if (k < dimZ-1) // not last element in row
	    			{
		    			row = row+blockNameArray[j][i][k]+", ";	    				
	    			}
	    			else // last element in row
	    			{
		    			row = row+blockNameArray[j][i][k];	    				
	    			}
	    		}
	    		if (j < dimX-1) // not last element in column
	    		{
		    		System.out.println("        { "+row+" },");
	    		}
	    		else // last element in column
	    		{
		    		System.out.println("        { "+row+" }");
	    		}
	    	}
    		if (i < dimY-1) // not last layer
    		{
	    		System.out.println("    },");
    		}
    		else // last layer
    		{
	    		System.out.println("    }");
    		}
	    }	
		System.out.println("};");
	}

	protected void printMetaArray()
	{
		System.out.println("// Metadata Array");
		System.out.println("{");
	    for (int i = 0; i < dimY; i++) // Y first to organize in layers
	    {
	    	System.out.println("    {   // Layer ="+i);
	    	for (int j = 0; j < dimX; j++)
	    	{
	    		String row = "";
	    		for (int k = 0; k < dimZ; k++)
	    		{
	    			if (k < dimZ-1) // not last element in row
	    			{
		    			row = row+blockMetaArray[j][i][k]+", ";	    				
	    			}
	    			else // last element in row
	    			{
		    			row = row+blockMetaArray[j][i][k];	    				
	    			}
	    		}
	    		if (j < dimX-1) // not last element in column
	    		{
		    		System.out.println("        { "+row+" },");
	    		}
	    		else // last element in column
	    		{
		    		System.out.println("        { "+row+" }");
	    		}
	    	}
    		if (i < dimY-1) // not last layer
    		{
	    		System.out.println("    },");
    		}
    		else // last layer
    		{
	    		System.out.println("    }");
    		}
	    }	
		System.out.println("};");
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender var1) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] var1, int var2) {
		// TODO Auto-generated method stub
		return false;
	}

}
