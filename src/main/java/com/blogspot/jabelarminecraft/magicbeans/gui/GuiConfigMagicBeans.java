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

package com.blogspot.jabelarminecraft.magicbeans.gui;

/**
 * @author jabelar
 *
 */
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiMessageDialog;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.Event.Result;

// Thanks to minalien tutoral at http://minalien.com/minecraft-forge-feature-spotlight-config-guis/
public class GuiConfigMagicBeans extends GuiConfig 
{
    public GuiConfigMagicBeans(GuiScreen parent) 
    {
        super(parent,
                new ConfigElement(MagicBeans.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                MagicBeans.MODID, false, false, GuiConfig.getAbridgedConfigPath(MagicBeans.config.toString()));
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 2000)
        {
        	// DEBUG
        	System.out.println("Pressed DONE button");
            boolean flag = true;
            try
            {
                if ((configID != null || this.parentScreen == null || !(this.parentScreen instanceof GuiConfig)) 
                        && (this.entryList.hasChangedEntry(true)))
                {
                	// DEBUG
                	System.out.println("Saving config elements");
                    boolean requiresMcRestart = this.entryList.saveConfigElements();

                    if (Loader.isModLoaded(modID))
                    {
                        ConfigChangedEvent event = new OnConfigChangedEvent(modID, configID, isWorldRunning, requiresMcRestart);
                        FMLCommonHandler.instance().bus().post(event);
                        if (!event.getResult().equals(Result.DENY))
                            FMLCommonHandler.instance().bus().post(new PostConfigChangedEvent(modID, configID, isWorldRunning, requiresMcRestart));
                        
                        if (requiresMcRestart)
                        {
                            flag = false;
                            mc.displayGuiScreen(new GuiMessageDialog(parentScreen, "fml.configgui.gameRestartTitle", 
                                    new ChatComponentText(I18n.format("fml.configgui.gameRestartRequired")), "fml.configgui.confirmRestartMessage"));
                        }
                        
                        if (this.parentScreen instanceof GuiConfig)
                            ((GuiConfig) this.parentScreen).needsRefresh = true;
                    }
                }
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
            
            if (flag)
                this.mc.displayGuiScreen(this.parentScreen);
        }
    }

}