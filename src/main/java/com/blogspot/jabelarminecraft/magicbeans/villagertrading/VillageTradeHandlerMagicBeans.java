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

package com.blogspot.jabelarminecraft.magicbeans.villagertrading;

import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;

import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;

/**
 * @author jabelar
 *
 */
public class VillageTradeHandlerMagicBeans implements IVillageTradeHandler
{

    @Override
	public void manipulateTradesForVillager(EntityVillager parVillager, MerchantRecipeList parRecipeList, Random parRand) 
    {
    	switch (parVillager.getProfession())
    	{
    	case 5:
    		parRecipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 2), new ItemStack(MagicBeans.magicBeans, 1)));
    		break;
   	    default:
	    	break;
    	}
	}

}
