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
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * @author jabelar
 *
 */
public class MessageGiveItemToServer implements IMessage 
{
    
    private static ItemStack itemToGive;
    private String itemName;
    private EntityCowMagicBeans entityCowMagicBeans;
    private static int entityID;

    public MessageGiveItemToServer() 
    { 
    	// need this constructor
    }

    public MessageGiveItemToServer(ItemStack parItemStack, EntityCowMagicBeans parCowMagicBeans) 
    {
        itemToGive = parItemStack;
        entityCowMagicBeans = parCowMagicBeans;
        // DEBUG
        System.out.println("MessageGiveItemToServer constructor");
    }

    @Override
    public void fromBytes(ByteBuf buf) 
    {
    	itemName = ByteBufUtils.readUTF8String(buf); // this class is very useful in general for writing more complex objects
    	entityID = ByteBufUtils.readVarInt(buf, 4);
    	// DEBUG
    	System.out.println("fromBytes = "+itemName+" from Entity ID = "+entityID);
    	itemToGive = GameRegistry.findItemStack(MagicBeans.MODID, itemName, 1);
    }

    @Override
    public void toBytes(ByteBuf buf) 
    {
    	itemName = GameRegistry.findUniqueIdentifierFor(itemToGive.getItem()).name;
    	entityID = entityCowMagicBeans.getEntityId();
    	ByteBufUtils.writeUTF8String(buf, itemName);
    	ByteBufUtils.writeVarInt(buf, entityID, 4);
        // DEBUG
        System.out.println("toBytes encoded = "+itemName+" from Entity ID ="+entityID);
    }

    public static class Handler implements IMessageHandler<MessageGiveItemToServer, IMessage> 
    {
        
        @Override
        public IMessage onMessage(MessageGiveItemToServer message, MessageContext ctx) 
        {
        	EntityPlayer thePlayer = MagicBeans.proxy.getPlayerEntityFromContext(ctx);
        	// DEBUG
            System.out.println(String.format("Received %s from %s", message.itemName, thePlayer.getDisplayName()));
            thePlayer.inventory.addItemStackToInventory(itemToGive);
            Entity theEntity = MagicBeansUtilities.getEntityByID(entityID, thePlayer.worldObj);
            theEntity.setDead();
            return null; // no response in this case
        }
    }
 }
