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

package com.blogspot.jabelarminecraft.magicbeans;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.world.World;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.InputEvent.MouseInputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MagicBeansFMLEventHandler 
{
	
	/*
	 * Common events
	 */

	// events in the cpw.mods.fml.common.event package are actually handled with
	// @EventHandler annotation in the main mod class or the proxies.
	
	/*
	 * Game input events
	 */

	private final boolean haveOutputWorldDataDebugMessages = false;

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(InputEvent event)
	{
		
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(KeyInputEvent event)
	{

	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(MouseInputEvent event)
	{

	}
	
	/*
	 * Player events
	 */

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(PlayerEvent event)
	{
		
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(ItemCraftedEvent event)
	{
		
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(ItemPickupEvent event)
	{
		
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(ItemSmeltedEvent event)
	{
		
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(PlayerChangedDimensionEvent event)
	{
		
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(PlayerLoggedInEvent event)
	{
		if (event.player.getDisplayName().equals("MistMaestro"))
		{
			// DEBUG
			System.out.println("Welcome Master!");
		}
		
		// DEBUG
		System.out.println("MagicBeansWorldData hasCastleSpawned ="+MagicBeansWorldData.get(event.player.worldObj).getHasCastleSpwaned()+
				", familyCowHasGivenLead ="+MagicBeansWorldData.get(event.player.worldObj).getFamilyCowHasGivenLead());
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(PlayerLoggedOutEvent event)
	{
		// DEBUG
		System.out.println("Player logged out");
		
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(PlayerRespawnEvent event)
	{
		// DEBUG
		System.out.println("The memories of past existences are but glints of light.");
		
	}

	/*
	 * Tick events
	 */

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(ClientTickEvent event) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{

	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(PlayerTickEvent event)
	{
		
		EntityPlayer thePlayer = event.player;
		World world = thePlayer.worldObj;
		
		if (!MagicBeans.haveWarnedVersionOutOfDate && world.isRemote && !MagicBeans.versionChecker.isLatestVersion())
		{
			ClickEvent versionCheckChatClickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, "http://jabelarminecraft.blogspot.com");
			ChatStyle clickableChatStyle = new ChatStyle().setChatClickEvent(versionCheckChatClickEvent);
			ChatComponentText versionWarningChatComponent = new ChatComponentText("Your Magic Beans Mod is not latest version!  Click here to update.");
			versionWarningChatComponent.setChatStyle(clickableChatStyle);
			thePlayer.addChatMessage(versionWarningChatComponent);
			MagicBeans.haveWarnedVersionOutOfDate = true;
		}
		
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(RenderTickEvent event)
	{
		
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(ServerTickEvent event)
	{
		
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(OnConfigChangedEvent eventArgs) 
	{
		// DEBUG
		System.out.println("OnConfigChangedEvent");
		if(eventArgs.modID.equals(MagicBeans.MODID))
		{
			System.out.println("Syncing config for mod ="+eventArgs.modID);
			MagicBeans.config.save();
			MagicBeans.proxy.syncConfig();
	    }
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(PostConfigChangedEvent eventArgs) 
	{
		// useful for doing something if another mod's config has changed
		// if(eventArgs.modID.equals(MagicBeans.MODID))
		// {
		//		// do whatever here
		// }
	}
}
