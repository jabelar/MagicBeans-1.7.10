package com.blogspot.jabelarminecraft.magicbeans.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;

public class ItemMagicBeans extends ItemSeedFoodMagicBeans 
{
    public ItemMagicBeans() 
    {
        super(1, 0.3F, MagicBeans.blockMagicBeanStalk, Blocks.farmland);
        setUnlocalizedName("magicbeans");
        setTextureName("magicbeans:magicbeans");
        setCreativeTab(CreativeTabs.tabFood);
    }
}