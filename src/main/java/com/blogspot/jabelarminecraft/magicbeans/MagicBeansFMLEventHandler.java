package com.blogspot.jabelarminecraft.magicbeans;

import net.minecraft.client.settings.KeyBinding;

import com.blogspot.jabelarminecraft.magicbeans.proxy.ClientProxy;

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

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(InputEvent event)
	{
		
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(KeyInputEvent event)
	{
		// DEBUG
		System.out.println("Key Input Event");

		// make local copy of key binding array
		KeyBinding[] keyBindings = ClientProxy.keyBindings;
		
		// check each enumerated key binding type for pressed and take appropriate action
		if (keyBindings[0].isPressed()) 
		{
			// DEBUG
			System.out.println("Key binding ="+keyBindings[0].getKeyDescription());
			// do stuff for this key binding here
			// remember you may need to send packet to server
		}
		if (keyBindings[1].isPressed()) 
		{
			// DEBUG
			System.out.println("Key binding ="+keyBindings[1].getKeyDescription());
			// do stuff for this key binding here
			// remember you may need to send packet to server
		}
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
		if (event.player.getDisplayName()=="MistMaestro")
		{
			// DEBUG
			System.out.println("Welcome Master!");
		}
		
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
		
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(RenderTickEvent event)
	{
		
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(ServerTickEvent event)
	{
		
	}

}
