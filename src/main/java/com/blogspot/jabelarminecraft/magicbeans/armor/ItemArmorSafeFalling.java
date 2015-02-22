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
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.utilities.MagicBeansUtilities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
	    
	    // DEBUG
	    // System.out.println("Constructor for ItemArmorSafeFalling");
	    
	    textureName = parTextureName;
	    setUnlocalizedName(parUnlocalizedName);
	    setTextureName(MagicBeans.MODID + ":" + parUnlocalizedName);
	}
	
    @Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack parItemStack)
    {
        return true;
    }
    
    @Override
    public boolean onEntityItemUpdate(EntityItem parEntityItem)
    {
    	// DEBUG
    	// System.out.println("There is boots of safe falling entity item at "+parEntityItem.posX+", "+parEntityItem.posY+", "+parEntityItem.posZ);
		return false; // false to allow other EntityItem update code to be processed    	
    }

    /**
     * Return an item rarity from EnumRarity
     */
    @Override
	public EnumRarity getRarity(ItemStack parItemStack)
    {
        return EnumRarity.epic;
    }
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
	    return MagicBeans.MODID + ":armor/" + textureName + "_" + (armorType == 2 ? "2" : "1") + ".png";
	}

	@Override
	public String getItemStackDisplayName(ItemStack parItemStack)
	{
		if (armorType == 3) // boots
		{
			return MagicBeansUtilities.stringToRainbow("Boots of Safe Falling", true);
		}
		else if (armorType == 2) // leggings
		{
			return MagicBeansUtilities.stringToRainbow("Leggings of Safe Falling", true);
		}
		else if (armorType == 1) // chestplate
		{
			return MagicBeansUtilities.stringToRainbow("Chestplate of Safe Falling", true);
		}
		else // helmet
		{
			return MagicBeansUtilities.stringToRainbow("Helmet of Safe Falling", true);
		}
	}
}
