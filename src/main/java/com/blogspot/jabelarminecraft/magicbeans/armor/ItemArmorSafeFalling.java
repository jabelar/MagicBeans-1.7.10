/**
    Copyright (C) 2015 by jabelar

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

package com.blogspot.jabelarminecraft.magicbeans.armor;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;

/**
 * @author jabelar
 *
 */
public class ItemArmorSafeFalling extends ItemArmor
{
	// Got ideas from http://bedrockminer.jimdo.com/modding-tutorials/basic-modding/custom-armor/
	
	public String textureName;

	public ItemArmorSafeFalling(String parUnlocalizedName, ArmorMaterial parMaterial, String parTextureName, int parArmorType) 
	{
	    super(parMaterial, 0, parArmorType);
	    textureName = parTextureName;
	    setUnlocalizedName(parUnlocalizedName);
	    setTextureName(MagicBeans.MODID + ":" + parUnlocalizedName);
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
	    return MagicBeans.MODID + ":armor/" + textureName + "_" + (armorType == 2 ? "2" : "1") + ".png";
	}
}
