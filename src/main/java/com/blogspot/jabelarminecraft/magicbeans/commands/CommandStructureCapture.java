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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
	public static String[][][] blockNameArray ;
    public static int[][][] blockMetaArray ;
    int startX ;
    int startY ;
    int startZ ;
    int endX ;
    int endY ;
    int endZ ;
    static int dimX ;
    static int dimY ;
    static int dimZ ;
    static int signX ;
    static int signY ;
    static int signZ ;
    
   		
	
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
		return "capture <int> <int> <int> <int> <int> <int> <text>"; // use "structure <text>"; later when passing name of structure
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

			if(argString.length != 7)
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
		    if (endX < startX)
		    {
		    	int temp = startX;
		    	startX = endX;
		    	endX = temp;
		    }
		    if (endY < startY)
		    {
		    	int temp = startY;
		    	startY = endY;
		    	endY = temp;
		    }
		    if (endZ < startZ)
		    {
		    	int temp = startZ;
		    	startZ = endZ;
		    	endZ = temp;
		    }
			if(dimX*dimY*dimZ > 64*64*64)
		    {
		    	sender.addChatMessage(new ChatComponentText("Capture area too big"));
		    	return;
		    }
		    
		    sender.addChatMessage(new ChatComponentText("Capturing Structure from "+startX+", "+startY+", "+
		         startZ+" to "+endX+", "+endY+", "+endZ));
		    blockNameArray = new String[dimX][dimY][dimZ];
		    blockMetaArray = new int[dimX][dimY][dimZ];

		    for (int indY = 0; indY < dimY; indY++) // Y first to organize in vertical layers
		    {
		    	for (int indX = 0; indX < dimX; indX++)
		    	{
		    		for (int indZ = 0; indZ < dimZ; indZ++)
		    		{
		    			blockNameArray[indX][indY][indZ] = Block.blockRegistry.getNameForObject(theWorld.getBlock(startX+indX, startY+indY, 
		    					startZ+indZ));
		    			blockMetaArray[indX][indY][indZ] = theWorld.getBlockMetadata(startX+indX, startY+indY, 
		    					startZ+indZ);		    			
		    		}
		    	}
		    }

		    writeFileNameArray(argString[6]);
		}
	}

	protected void printNameArray()
	{
		System.out.println("// Block Name Array");
		System.out.println("{");
	    for (int indY = 0; indY < dimY; indY++) // Y first to organize in layers
	    {
	    	System.out.println("    {   // Layer ="+indY);
	    	for (int indX = 0; indX < dimX; indX++)
	    	{
	    		String row = "";
	    		for (int indZ = 0; indZ < dimZ; indZ++)
	    		{
	    			if (indZ < dimZ-1) // not last element in row
	    			{
		    			row = row+blockNameArray[indX][indY][indZ]+", ";	    				
	    			}
	    			else // last element in row
	    			{
		    			row = row+blockNameArray[indX][indY][indZ];	    				
	    			}
	    		}
	    		if (indX < dimX-1) // not last element in column
	    		{
		    		System.out.println("        { "+row+" },");
	    		}
	    		else // last element in column
	    		{
		    		System.out.println("        { "+row+" }");
	    		}
	    	}
    		if (indY < dimY-1) // not last layer
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
	    for (int indY = 0; indY < dimY; indY++) // Y first to organize in layers
	    {
	    	System.out.println("    {   // Layer ="+indY);
	    	for (int indX = 0; indX < dimX; indX++)
	    	{
	    		String row = "";
	    		for (int indZ = 0; indZ < dimZ; indZ++)
	    		{
	    			if (indZ < dimZ-1) // not last element in row
	    			{
		    			row = row+blockMetaArray[indX][indY][indZ]+", ";	    				
	    			}
	    			else // last element in row
	    			{
		    			row = row+blockMetaArray[indX][indY][indZ];	    				
	    			}
	    		}
	    		if (indX < dimX-1) // not last element in column
	    		{
		    		System.out.println("        { "+row+" },");
	    		}
	    		else // last element in column
	    		{
		    		System.out.println("        { "+row+" }");
	    		}
	    	}
    		if (indY < dimY-1) // not last layer
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
	
	protected void writeFileNameArray(String fileName)
	{
		File file = new File(fileName+".txt");
		PrintWriter printOut = null;
		try 
		{
			printOut = new PrintWriter(new FileWriter(file));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		// print dimensions to the file
		printOut.println(dimX);
		printOut.println(dimY);
		printOut.println(dimZ);

		// Write each string in the array on a separate line
	    for (int indY = 0; indY < dimY; indY++) // Y first to organize in layers
	    {
	    	for (int indX = 0; indX < dimX; indX++)
	    	{
	    		for (int indZ = 0; indZ < dimZ; indZ++)
	    		{
	    			printOut.println(blockNameArray[indX][indY][indZ]);
	    			printOut.println(blockMetaArray[indX][indY][indZ]);
	    		}
	    	}
	    }
	    printOut.close();
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
