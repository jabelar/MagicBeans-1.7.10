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

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.networking.MessageSyncEntityToClient;

/**
 * @author jabelar
 *
 */
public class MagicBeansUtilities 
{
	public static String stringToRainbow(String parString)
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
		return outputString;
	}

	public static String stringToGolden(String parString, int parShineLocation)
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
		return outputString;
	}

	public static Entity getEntityByID(int entityID, World world)        
	{         
	    for(Object o: world.getLoadedEntityList())                
	    {                        
	        if(((Entity)o).getEntityId() == entityID)                        
	        {                                
	            System.out.println("Found the entity");                                
	            return ((Entity)o);                        
	        }                
	    }                
	    return null;        
	} 

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#sendEntitySyncPacket()
	 */
    public static void sendEntitySyncPacket(IEntityMagicBeans parEntity) 
    {
    	Entity theEntity = (Entity)parEntity;
        if (!theEntity.worldObj.isRemote)
        {
        	// DEBUG
        	System.out.println("sendEntitySyncPacket from server");
            MagicBeans.network.sendToAll(new MessageSyncEntityToClient(theEntity.getEntityId(), parEntity.getSyncDataCompound()));           
        }
    }
}
