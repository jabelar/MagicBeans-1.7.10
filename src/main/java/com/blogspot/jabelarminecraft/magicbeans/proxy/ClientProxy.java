package com.blogspot.jabelarminecraft.magicbeans.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;

import org.lwjgl.input.Keyboard;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.entities.EntityGoldenEggThrown;
import com.blogspot.jabelarminecraft.magicbeans.entities.EntityGoldenGoose;
import com.blogspot.jabelarminecraft.magicbeans.models.ModelGoldenGoose;
import com.blogspot.jabelarminecraft.magicbeans.renderers.RenderGoldenEggThrown;
import com.blogspot.jabelarminecraft.magicbeans.renderers.RenderGoldenGoose;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


public class ClientProxy extends CommonProxy 
{

	/*
	 * Fields related to key binding
	 */
	public static KeyBinding[] keyBindings;
	
	@Override
	public void fmlLifeCycleEvent(FMLPreInitializationEvent event)
	{
		// DEBUG
        System.out.println("on Client side");
        
		// do common stuff
		super.fmlLifeCycleEvent(event);

        // do client-specific stuff
        registerRenderers();
	}
	
	@Override
	public void fmlLifeCycleEvent(FMLInitializationEvent event)
	{
		// DEBUG
        System.out.println("on Client side");

        // do common stuff
		super.fmlLifeCycleEvent(event);

		// do client-specific stuff
		registerClientPacketHandler();
		registerKeyBindings();
	}
	
	private void registerKeyBindings() 
	{		
		// declare an array of key bindings
		keyBindings = new KeyBinding[2]; 
		
		// instantiate the key bindings
		keyBindings[0] = new KeyBinding("key.structure.desc", Keyboard.KEY_P, "key.magicbeans.category");
		keyBindings[1] = new KeyBinding("key.hud.desc", Keyboard.KEY_H, "key.magicbeans.category");
		
		// register all the key bindings
		for (int i = 0; i < keyBindings.length; ++i) 
		{
			ClientRegistry.registerKeyBinding(keyBindings[i]);
		}
	}

	private void registerClientPacketHandler() 
	{
		MagicBeans.channel.register(new ClientPacketHandler());	
	}
	
	@Override
	public void fmlLifeCycleEvent(FMLPostInitializationEvent event)
	{
		// DEBUG
        System.out.println("on Client side");

        // do common stuff
		super.fmlLifeCycleEvent(event);

		// do client-specific stuff
	}

	public void registerRenderers() 
    {
		// the float parameter passed to the Render class is the shadow size for the entity
      
	    RenderingRegistry.registerEntityRenderingHandler(EntityGoldenGoose.class, new RenderGoldenGoose(new ModelGoldenGoose(), 0.5F)); // 0.5F is shadow size 
	    RenderingRegistry.registerEntityRenderingHandler(EntityGoldenEggThrown.class, new RenderGoldenEggThrown(MagicBeans.itemGoldenEgg)); // 0.5F is shadow size 
    }
	
    @Override
    public void sendMessageToPlayer(ChatComponentText msg) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(msg);
    }
}