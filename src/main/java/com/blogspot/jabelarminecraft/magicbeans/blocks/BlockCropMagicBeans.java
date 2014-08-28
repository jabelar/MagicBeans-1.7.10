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

package com.blogspot.jabelarminecraft.magicbeans.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCropMagicBeans extends BlockBush implements IGrowable
{
    @SideOnly(Side.CLIENT)
    protected IIcon[] iconArray;
    
    protected boolean isFullyGrown = false;

    public BlockCropMagicBeans()
    {
    	super();
        // Basic block setup
        setTickRandomly(true);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setCreativeTab((CreativeTabs)null);
        setHardness(0.0F);
        setStepSound(soundTypeGrass);
        disableStats();
    }

    public BlockCropMagicBeans(Material parMaterial) 
    {
		super(parMaterial);
        // Basic block setup
        setTickRandomly(true);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setCreativeTab((CreativeTabs)null);
        setHardness(0.0F);
        setStepSound(soundTypeGrass);
        disableStats();
	}

	/**
     * is the block grass, dirt or farmland
     */
    @Override
    protected boolean canPlaceBlockOn(Block parBlockToTest)
    {
        return parBlockToTest == Blocks.farmland;
    }
    
    public boolean isFullyGrown()
    {
    	return isFullyGrown;
    }

    public void incrementGrowStage(World parWorld, int parX, int parY, int parZ)
    {
    	if (!isFullyGrown)
    	{
            int growStage = parWorld.getBlockMetadata(parX, parY, parZ) + MathHelper
                    .getRandomIntegerInRange(parWorld.rand, 2, 5);

            if (growStage >= 7) // only 8 growth stages
            {
                growStage = 7;
                isFullyGrown = true;
            }

            parWorld.setBlockMetadataWithNotify(parX, parY, parZ, growStage, 2);
       	}
    	else // fully grown so create the stalk above
    	{
    		// check if air above
    	    if(parWorld.isAirBlock(parX, parY + 1, parZ))
    	    {
    	        parWorld.setBlock(parX, parY + 1, parZ, MagicBeans.blockMagicBeanStalk);
    	    }
    		
    	}
    }
    

    /**
     * The type of render function that is called for this block
     */
    @Override
     public int getRenderType()
    {
        return 1; // Cross like flowers
    }

    @Override
    // checks if finished growing (a grow stage of 7 is final stage)
    public boolean func_149851_a(World parWorld, int parX, int parY, int parZ, 
          boolean p_149851_5_)
    {
        return parWorld.getBlockMetadata(parX, parY, parZ) != 7;
    }

    /*
     * (non-Javadoc)
     * @see net.minecraft.block.IGrowable#func_149852_a(net.minecraft.world.World, 
     * java.util.Random, int, int, int)
     */
    @Override
    // basically an canBoneMealSpeedGrowth() method
    public boolean func_149852_a(World p_149852_1_, Random parRand, int parX, 
          int parY, int parZ)
    {
        return true;
    }

    /*
     * (non-Javadoc)
     * @see net.minecraft.block.IGrowable#func_149853_b(net.minecraft.world.World, 
     * java.util.Random, int, int, int)
     */
    @Override
    public void func_149853_b(World parWorld, Random parRand, int parX, int parY, int parZ)
    {
        incrementGrowStage(parWorld, parX, parY, parZ);
    }
}