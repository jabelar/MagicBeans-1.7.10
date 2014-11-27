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

package com.blogspot.jabelarminecraft.magicbeans.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.VersionChecker;
import com.blogspot.jabelarminecraft.magicbeans.entities.EntityCowMagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.entities.EntityGiant;
import com.blogspot.jabelarminecraft.magicbeans.entities.EntityGoldenEggThrown;
import com.blogspot.jabelarminecraft.magicbeans.entities.EntityGoldenGoose;
import com.blogspot.jabelarminecraft.magicbeans.entities.EntityMysteriousStranger;
import com.blogspot.jabelarminecraft.magicbeans.models.ModelGiant;
import com.blogspot.jabelarminecraft.magicbeans.models.ModelGoldenGoose;
import com.blogspot.jabelarminecraft.magicbeans.renderers.RenderCowMagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.renderers.RenderGiant;
import com.blogspot.jabelarminecraft.magicbeans.renderers.RenderGoldenEggThrown;
import com.blogspot.jabelarminecraft.magicbeans.renderers.RenderGoldenGoose;
import com.blogspot.jabelarminecraft.magicbeans.renderers.RenderMysteriousStranger;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;


public class ClientProxy extends CommonProxy 
{

	/*
	 * Fields related to key binding
	 */
	public static KeyBinding[] keyBindings;
	
	/*
	 * For rendering a sphere, need ids for call lists for outside and inside
	 */
	public static int sphereIdOutside;
	public static int sphereIdInside;
	
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
		// registerClientPacketHandler();
		registerKeyBindings();

    	// create sphere call list
    	createSphereCallList();
	}
	
	@Override
	public void fmlLifeCycleEvent(FMLPostInitializationEvent event)
	{
		// DEBUG
        System.out.println("on Client side");

        // do common stuff
		super.fmlLifeCycleEvent(event);

		// do client-specific stuff
		MagicBeans.versionChecker = new VersionChecker();
		Thread versionCheckThread = new Thread(MagicBeans.versionChecker, "Version Check");
		versionCheckThread.start();
	}

	/*
	 * Registers key bindings
	 */
	public void registerKeyBindings() 
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

	/**
	 * Registers the entity renderers
	 */
	public void registerRenderers() 
    {
		// the float parameter passed to the Render class is the shadow size for the entity
      
	    RenderingRegistry.registerEntityRenderingHandler(EntityGoldenGoose.class, new RenderGoldenGoose(new ModelGoldenGoose(), 0.5F)); // 0.5F is shadow size 
	    RenderingRegistry.registerEntityRenderingHandler(EntityGoldenEggThrown.class, new RenderGoldenEggThrown(MagicBeans.itemGoldenEgg)); 
	    RenderingRegistry.registerEntityRenderingHandler(EntityCowMagicBeans.class, new RenderCowMagicBeans(new ModelCow(), 0.5F)); 
	    RenderingRegistry.registerEntityRenderingHandler(EntityMysteriousStranger.class, new RenderMysteriousStranger(new ModelVillager(0.0F), 0.5F));    
    	RenderingRegistry.registerEntityRenderingHandler(EntityGiant.class, new RenderGiant(new ModelGiant(0.0F), 0.5F));    
    }
	
	/*	 
	 * Thanks to CoolAlias for this tip!
	 */
	/**
	 * Returns a side-appropriate EntityPlayer for use during message handling
	 */
    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx) 
    {
        // Note that if you simply return 'Minecraft.getMinecraft().thePlayer',
        // your packets will not work because you will be getting a client
        // player even when you are on the server! Sounds absurd, but it's true.

        // Solution is to double-check side before returning the player:
        return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntityFromContext(ctx));
    }
    
    /*
     * For rendering a sphere, need to make the call list
     * Must be called after pre-init, otherwise Minecraft.getMinecraft() will fail will null pointer exception
     */
    public void createSphereCallList()
    {
        Sphere sphere = new Sphere();
       //GLU_POINT will render it as dots.
       //GLU_LINE will render as wireframe
       //GLU_SILHOUETTE will render as ?shadowed? wireframe
       //GLU_FILL as a solid.
        sphere.setDrawStyle(GLU.GLU_FILL);
       //GLU_SMOOTH will try to smoothly apply lighting
       //GLU_FLAT will have a solid brightness per face, and will not shade.
       //GLU_NONE will be completely solid, and probably will have no depth to it's appearance.
        sphere.setNormals(GLU.GLU_SMOOTH);
       //GLU_INSIDE will render as if you are inside the sphere, making it appear inside out.(Similar to how ender portals are rendered)
        sphere.setOrientation(GLU.GLU_OUTSIDE);
        sphereIdOutside = GL11.glGenLists(1);
       //Create a new list to hold our sphere data.
        GL11.glNewList(sphereIdOutside, GL11.GL_COMPILE);
       //binds the texture 
       ResourceLocation rL = new ResourceLocation(MagicBeans.MODID+":textures/entities/sphere.png");
       Minecraft.getMinecraft().getTextureManager().bindTexture(rL);
       //The drawing the sphere is automatically doing is getting added to our list. Careful, the last 2 variables
       //control the detail, but have a massive impact on performance. 32x32 is a good balance on my machine.s
       sphere.draw(0.5F, 32, 32);
       GL11.glEndList();

       //GLU_INSIDE will render as if you are inside the sphere, making it appear inside out.(Similar to how ender portals are rendered)
       sphere.setOrientation(GLU.GLU_INSIDE);
       sphereIdInside = GL11.glGenLists(1);
       //Create a new list to hold our sphere data.
       GL11.glNewList(sphereIdInside, GL11.GL_COMPILE);
       Minecraft.getMinecraft().getTextureManager().bindTexture(rL);
       //The drawing the sphere is automatically doing is getting added to our list. Careful, the last 2 variables
       //control the detail, but have a massive impact on performance. 32x32 is a good balance on my machine.s
       sphere.draw(0.5F, 32, 32);
       GL11.glEndList();
    }
}