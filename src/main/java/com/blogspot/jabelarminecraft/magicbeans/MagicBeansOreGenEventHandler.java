package com.blogspot.jabelarminecraft.magicbeans;

import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MagicBeansOreGenEventHandler 
{
    @SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=true)
    public void onEvent(OreGenEvent event)
    {
        
    }
    
    @SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=true)
    public void onEvent(GenerateMinable event)
    {
        
    }
    
    @SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=true)
    public void onEvent(OreGenEvent.Post event)
    {
        
    }
    
    @SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=true)
    public void onEvent(OreGenEvent.Pre event)
    {
        
    }
    
}
