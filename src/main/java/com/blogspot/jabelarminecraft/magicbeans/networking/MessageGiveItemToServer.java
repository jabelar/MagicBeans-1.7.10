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

package com.blogspot.jabelarminecraft.magicbeans.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.entities.EntityCowMagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.utilities.MagicBeansUtilities;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 * @author jabelar
 *
 */
public class MessageGiveItemToServer implements IMessage 
{
    
    private static ItemStack itemToGive = new ItemStack(MagicBeans.magicBeans, 1);
    private EntityCowMagicBeans entityCowMagicBeans;
    private static int entityID;

    public MessageGiveItemToServer() 
    { 
    	// need this constructor
    }

    public MessageGiveItemToServer(EntityCowMagicBeans parCowMagicBeans) 
    {
        entityCowMagicBeans = parCowMagicBeans;
        // DEBUG
        System.out.println("MessageGiveItemToServer constructor");
    }

    @Override
    public void fromBytes(ByteBuf buf) 
    {
    	entityID = ByteBufUtils.readVarInt(buf, 4);
    }

    @Override
    public void toBytes(ByteBuf buf) 
    {
    	entityID = entityCowMagicBeans.getEntityId();
    	ByteBufUtils.writeVarInt(buf, entityID, 4);
    }

    public static class Handler implements IMessageHandler<MessageGiveItemToServer, IMessage> 
    {
        
        @Override
        public IMessage onMessage(MessageGiveItemToServer message, MessageContext ctx) 
        {
        	// DEBUG
        	System.out.println("Message received");
        	EntityPlayer thePlayer = MagicBeans.proxy.getPlayerEntityFromContext(ctx);
            thePlayer.inventory.addItemStackToInventory(itemToGive);
            Entity theEntity = MagicBeansUtilities.getEntityByID(entityID, thePlayer.worldObj);
            theEntity.setDead();
            return null; // no response in this case
        }
    }
 }
