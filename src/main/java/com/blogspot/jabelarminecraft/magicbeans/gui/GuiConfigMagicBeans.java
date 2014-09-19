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
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;

import cpw.mods.fml.client.config.GuiConfig;

// Thanks to minalien tutoral at http://minalien.com/minecraft-forge-feature-spotlight-config-guis/
public class GuiConfigMagicBeans extends GuiConfig 
{
    public GuiConfigMagicBeans(GuiScreen parent) 
    {
        super(parent,
                new ConfigElement(MagicBeans.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                MagicBeans.MODID, false, false, GuiConfig.getAbridgedConfigPath(MagicBeans.config.toString()));
    }
}