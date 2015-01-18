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

package com.blogspot.jabelarminecraft.magicbeans.blocks;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author jabelar
 *
 */
public class BlockCloud extends Block
{

    public BlockCloud()
    {
        super(MagicBeans.materialCloud);
        // DEBUG
        System.out.println("BlockCloud constructor");
        // override default values of Block, where appropriate
        setBlockName("magicbeanscloud");
        setBlockTextureName("magicbeans:cloud");
        setCreativeTab(CreativeTabs.tabBlock);
        stepSound = soundTypeSnow;
        blockParticleGravity = 1.0F;
        slipperiness = 0.6F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        opaque = isOpaqueCube();
        lightOpacity = 20; // cast a light shadow
        canBlockGrass = !getMaterial().getCanBlockGrass();
        setBlockUnbreakable();
        setTickRandomly(false);
        setLightLevel(0.5F); // redstone light has light value of 1.0F
        useNeighborBrightness = false;
    }

//
//    @Override
//	public MapColor getMapColor(int p_149728_1_)
//    {
//        return getMaterial().getMaterialMapColor();
//    }
//
//    /**
//     * Indicate if a material is a normal solid opaque cube
//     */
//    @Override
//	@SideOnly(Side.CLIENT)
//    public boolean isBlockNormalCube()
//    {
//        return blockMaterial.blocksMovement() && renderAsNormalBlock();
//    }
//
//    @Override
//	public boolean isNormalCube()
//    {
//        return blockMaterial.isOpaque() && renderAsNormalBlock() && !canProvidePower();
//    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
	public boolean renderAsNormalBlock()
    {
        return true;
    }

    @Override
	public boolean getBlocksMovement(IBlockAccess p_149655_1_, int p_149655_2_, int p_149655_3_, int p_149655_4_)
    {
        return !blockMaterial.blocksMovement();
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
	public int getRenderType()
    {
        return 0;
    }

//    /**
//     * Sets how many hits it takes to break a block.
//     */
//    @Override
//	public Block setHardness(float parHardness)
//    {
//        blockHardness = parHardness;
//
//        if (blockResistance < parHardness * 5.0F)
//        {
//            blockResistance = parHardness * 5.0F;
//        }
//
//        return this;
//    }
//
//    /**
//     * This method will set the hardness of the block to -1, making it indestructible
//     */
//    @Override
//	public Block setBlockUnbreakable()
//    {
//        setHardness(-1.0F);
//        return this;
//    }
//
//    /**
//     * Returns the block hardness at a location. Args: world, x, y, z
//     */
//    @Override
//	public float getBlockHardness(World p_149712_1_, int p_149712_2_, int p_149712_3_, int p_149712_4_)
//    {
//        return blockHardness;
//    }
//
//
//    @Override
//	@Deprecated //Forge: New Metadata sensitive version.
//    public boolean hasTileEntity()
//    {
//        return hasTileEntity(0);
//    }
//
//    /**
//     * How bright to render this block based on the light its receiving. Args: iBlockAccess, x, y, z
//     */
//    @Override
//	@SideOnly(Side.CLIENT)
//    public int getMixedBrightnessForBlock(IBlockAccess parBlockAccess, int parX, int parY, int parZ)
//    {
//        Block block = parBlockAccess.getBlock(parX, parY, parZ);
//        int l = parBlockAccess.getLightBrightnessForSkyBlocks(parX, parY, parZ, block.getLightValue(parBlockAccess, parX, parY, parZ));
//        return l;
//    }
//
//    /**
//     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
//     * coordinates.  Args: blockAccess, x, y, z, side
//     */
//    @Override
//	@SideOnly(Side.CLIENT)
//    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_)
//    {
//    	return true;
//    }

    /**
     * Returns true if the given side of this block type should be rendered (if it's solid or not), if the adjacent
     * block is at the given coordinates. Args: blockAccess, x, y, z, side
     */
    @Override
	public boolean isBlockSolid(IBlockAccess p_149747_1_, int p_149747_2_, int p_149747_3_, int p_149747_4_, int p_149747_5_)
    {
        return getMaterial().isSolid();
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
	public boolean isOpaqueCube()
    {
        return getMaterial().isOpaque();
    }

 //    /**
//     * Returns how much this block can resist explosions from the passed in entity.
//     */
//    @Override
//	public float getExplosionResistance(Entity p_149638_1_)
//    {
//        return blockResistance / 5.0F;
//    }

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    @Override
	@SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        return 0xFFFFFF; // white
    }

//    /**
//     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
//     * when first determining what to render.
//     */
//    @Override
//	@SideOnly(Side.CLIENT)
//    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
//    {
//        return 0x000000;
//    }

    /**
     * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2 = total immobility
     * and stop pistons
     */
    @Override
	public int getMobilityFlag()
    {
        return getMaterial().getMaterialMobility();
    }

    /**
     * Checks if the block is a solid face on the given side, used by placement logic.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @param side The side to check
     * @return True if the block is solid on the specified side.
     */
    @Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
    {
        return (side == ForgeDirection.UP); // can only build on top (cloud is generated not built)
    }

    /**
     * Determines if a new block can be replace the space occupied by this one,
     * Used in the player's placement code to make the block act like water, and lava.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block is replaceable by another block
     */
    @Override
	public boolean isReplaceable(IBlockAccess world, int x, int y, int z)
    {
        return getMaterial().isReplaceable();
    }

    /**
     * Chance that fire will spread and consume this block.
     * 300 being a 100% chance, 0, being a 0% chance.
     *
     * @param world The current world
     * @param x The blocks X position
     * @param y The blocks Y position
     * @param z The blocks Z position
     * @param face The face that the fire is coming from
     * @return A number ranging from 0 to 300 relating used to determine if the block will be consumed by fire
     */
    @Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face)
    {
        return 0;
    }

    /**
     * Currently only called by fire when it is on top of this block.
     * Returning true will prevent the fire from naturally dying during updating.
     * Also prevents firing from dying from rain.
     *
     * @param world The current world
     * @param x The blocks X position
     * @param y The blocks Y position
     * @param z The blocks Z position
     * @param metadata The blocks current metadata
     * @param side The face that the fire is coming from
     * @return True if this block sustains fire, meaning it will never go out.
     */
    @Override
	public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side)
    {
        return false;
    }

    /**
     * Metadata and fortune sensitive version, this replaces the old (int meta, Random rand)
     * version in 1.1.
     *
     * @param meta Blocks Metadata
     * @param fortune Current item fortune level
     * @param random Random number generator
     * @return The number of items to drop
     */
    @Override
	public int quantityDropped(int meta, int fortune, Random random)
    {
        /**
         * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i' (inclusive).
         */
        return 0;
    }

    /**
     * This returns a complete list of items dropped from this block.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param metadata Current metadata
     * @param fortune Breakers fortune level
     * @return A ArrayList containing all items this block drops
     */
    @Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        return ret;
    }

    /**
     * Return true from this function if the player with silk touch can harvest this block directly, and not it's normal drops.
     *
     * @param world The world
     * @param player The player doing the harvesting
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param metadata The metadata
     * @return True if the block can be directly harvested using silk touch
     */
    @Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
    {
    	return false;
    }

    /**
     * Determines if a specified mob type can spawn on this block, returning false will
     * prevent any mob from spawning on the block.
     *
     * @param type The Mob Category Type
     * @param world The current world
     * @param x The X Position
     * @param y The Y Position
     * @param z The Z Position
     * @return True to allow a mob of the specified category to spawn, false to prevent it.
     */
    @Override
	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z)
    {
    	// TODO
    	// probably want to limit by creature type
         return true;
    }

    /**
     * Called to determine whether to allow the a block to handle its own indirect power rather than using the default rules.
     * @param world The world
     * @param x The x position of this block instance
     * @param y The y position of this block instance
     * @param z The z position of this block instance
     * @param side The INPUT side of the block to be powered - ie the opposite of this block's output side
     * @return Whether Block#isProvidingWeakPower should be called when determining indirect power
     */
    @Override
	public boolean shouldCheckWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
        return false;
    }

    /**
     * Checks if the specified tool type is efficient on this block, 
     * meaning that it digs at full speed.
     * 
     * @param type
     * @param metadata
     * @return
     */
    @Override
	public boolean isToolEffective(String type, int metadata)
    {
        return false;
    }
}
