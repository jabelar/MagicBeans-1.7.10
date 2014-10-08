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
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.tileentities.TileEntityMagicBeanStalk;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMagicBeanStalk extends BlockCropMagicBeans implements ITileEntityProvider
{

    public BlockMagicBeanStalk()
    {
    	super(Material.wood); // should chop like wood, block movement
    	// DEBUG
    	System.out.println("BlockMagicBeanStalk constructor()");

    	// Basic block setup
        setBlockName("magicbeanstalk");
        setBlockTextureName("magicbeans:magicbeanstalk_stage_0");
    	setBlockBounds(0.5F-0.125F, 0.0F, 0.5F-0.125F, 0.5F+0.125F, 1.0F, 0.5F+0.125F);
    }
    
    // identifies what food (ItemFood or ItemSeedFood type) is harvested from this
    @Override
	public Item getItemDropped(int parMetadata, Random parRand, int parFortune)
    {
        return MagicBeans.magicBeans;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random parRand)
    {
        return 0; // will make this a configurable quantity
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconReg)
    {
    	// DEBUG
    	System.out.println("Registering icons for Magic Bean Stalk");
	    iconArray = new IIcon[8];
	    // seems that crops like to have 8 growth icons, but okay to 
	    // repeat actual texture if you want
	    iconArray[0] = iconReg.registerIcon("magicbeans:magicbeanstalk_stage_0");
	    iconArray[1] = iconReg.registerIcon("magicbeans:magicbeanstalk_stage_0");
	    iconArray[2] = iconReg.registerIcon("magicbeans:magicbeanstalk_stage_1");
	    iconArray[3] = iconReg.registerIcon("magicbeans:magicbeanstalk_stage_1");
	    iconArray[4] = iconReg.registerIcon("magicbeans:magicbeanstalk_stage_2");
	    iconArray[5] = iconReg.registerIcon("magicbeans:magicbeanstalk_stage_2");
	    iconArray[6] = iconReg.registerIcon("magicbeans:magicbeanstalk_stage_3");
	    iconArray[7] = iconReg.registerIcon("magicbeans:magicbeanstalk_stage_3");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
	public IIcon getIcon(int parSide, int parMetadata)
    {
    	blockIcon = iconArray[parMetadata];
		return blockIcon;
    }
    
    @Override
	public boolean canPlaceBlockAt(World parWorld, int parX, int parY, int parZ) 
    {
        Block block = parWorld.getBlock(parX, parY - 1, parZ);
        return block.canSustainPlant(parWorld, parX, parY - 1, parZ, ForgeDirection.UP, this) || block == this;
    }
    
    @Override
	public String getHarvestTool(int metadata)
    {
        return null; // anything can harvest this block. should change to hatchet later
    }
    
    @Override
	public void incrementGrowStage(World parWorld, int parX, int parY, int parZ)
    {
		// DEBUG
		System.out.println("BlockMagicBeans incrementGrowStage growing");
        int growStage = parWorld.getBlockMetadata(parX, parY, parZ) + MathHelper
                .getRandomIntegerInRange(parWorld.rand, 2, 5);

        if (growStage >= 7) // only 8 growth stages
        {
            growStage = 7;
        }

        parWorld.setBlockMetadataWithNotify(parX, parY, parZ, growStage, 2);
    }
    
    /**
     * is the block grass, dirt or farmland
     */
    // can plant on itself as this allows the bean stalk to grow very tall
    @Override
	protected boolean canPlaceBlockOn(Block parBlock)
    {
        return parBlock == MagicBeans.blockMagicBeanStalk || parBlock == Blocks.grass || parBlock == Blocks.dirt || parBlock == Blocks.farmland;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType()
    {
        return 1; // you can see list of all possible types in renderBlockByRenderType() method
    }

    /**
     * Determines if this block can support the passed in plant, allowing it to be planted and grow.
     * Some examples:
     *   Reeds check if its a reed, or if its sand/dirt/grass and adjacent to water
     *   Cacti checks if its a cacti, or if its sand
     *   Nether types check for soul sand
     *   Crops check for tilled soil
     *   Caves check if it's a solid surface
     *   Plains check if its grass or dirt
     *   Water check if its still water
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z position
     * @param direction The direction relative to the given position the plant wants to be, typically its UP
     * @param plantable The plant that wants to check
     * @return True to allow the plant to be planted/stay.
     */
    @Override
	public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable)
    {
        if (world.getBlock(x, y + 1, z) == MagicBeans.blockMagicBeanStalk)
        {
        	return true;
        }
        else
        {
        	return super.canSustainPlant(world, x, y, z, direction, plantable);
        }
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World parWorld, int parX, int parY, int parZ)
    {
        return AxisAlignedBB.getBoundingBox(parX + minX, parY + minY, parZ + minZ, parX + maxX, parY + maxY, parZ + maxZ);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
	public void updateTick(World parWorld, int parX, int parY, int parZ, Random parRand)
    {
    	super.updateTick(parWorld, parX, parY, parZ, parRand);
    }

	@Override
	public TileEntity createNewTileEntity(World parWorld, int parMetadata) 
	{
		return new TileEntityMagicBeanStalk();
	}

	@Override
	public boolean isLadder(IBlockAccess parWord, int parX, int parY, int parZ, EntityLivingBase parEntity)
	{
		return true;
	}
}