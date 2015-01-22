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

package com.blogspot.jabelarminecraft.magicbeans.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.utilities.MagicBeansUtilities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author jabelar
 *
 */
public class ItemGoldenGooseMeat extends ItemFood
{

    public ItemGoldenGooseMeat(int p_i45341_1_, float p_i45341_2_, boolean p_i45341_3_)
    {
        super(p_i45341_1_, p_i45341_2_, p_i45341_3_);
        setAlwaysEdible();
        setUnlocalizedName("golden_goose_meat").
        setTextureName(MagicBeans.MODID+":golden_goose_meat");
    }

    @Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack parItemStack)
    {
        return true;
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
	protected void onFoodEaten(ItemStack parItemStack, World parWorld, EntityPlayer parPlayer)
    {
        if (!parWorld.isRemote)
        {
            parPlayer.addPotionEffect(new PotionEffect(Potion.field_76444_x.id, 2400, 0)); // absorption
            parPlayer.addPotionEffect(new PotionEffect(Potion.heal.id, 2400, 0)); // instant health
            parPlayer.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 2400, 0)); // strength
        }
    }
    
    @Override
    // Doing this override means that there is no localization for language
    // unless you specifically check for localization here and convert
	public String getItemStackDisplayName(ItemStack par1ItemStack)
    {
    	return MagicBeansUtilities.stringToGolden("Golden Goose Meat", 8);
	}  

}