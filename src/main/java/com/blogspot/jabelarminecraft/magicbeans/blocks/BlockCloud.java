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

import static net.minecraftforge.common.util.ForgeDirection.UP;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.util.RotationHelper;
import net.minecraftforge.event.ForgeEventFactory;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author jabelar
 *
 */
public class BlockCloud extends Block
{
	protected static Material materialCloud = Material.rock;

    public BlockCloud()
    {
        super(materialCloud);
        // override default values of Block, where appropriate
        setCreativeTab(CreativeTabs.tabBlock);
        stepSound = soundTypeStone;
        blockParticleGravity = 1.0F;
        slipperiness = 0.6F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        opaque = isOpaqueCube();
        lightOpacity = isOpaqueCube() ? 255 : 0;
        canBlockGrass = !materialCloud.getCanBlockGrass();
    }

    @Override
	public boolean func_149730_j()
    {
        return opaque;
    }

    @Override
	public int getLightOpacity()
    {
        return lightOpacity;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public boolean getCanBlockGrass()
    {
        return canBlockGrass;
    }

    /**
     * Gets the light value of the specified block coords. Args: x, y, z
     */
    @Override
	public int getLightValue()
    {
        return lightValue;
    }

    /**
     * Should block use the brightest neighbor light value as its own
     */
    @Override
	public boolean getUseNeighborBrightness()
    {
        return useNeighborBrightness;
    }

    @Override
	public Material getMaterial()
    {
        return blockMaterial;
    }

    @Override
	public MapColor getMapColor(int p_149728_1_)
    {
        return getMaterial().getMaterialMapColor();
    }

    /**
     * Indicate if a material is a normal solid opaque cube
     */
    @Override
	@SideOnly(Side.CLIENT)
    public boolean isBlockNormalCube()
    {
        return blockMaterial.blocksMovement() && renderAsNormalBlock();
    }

    @Override
	public boolean isNormalCube()
    {
        return blockMaterial.isOpaque() && renderAsNormalBlock() && !canProvidePower();
    }

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

    /**
     * Sets how many hits it takes to break a block.
     */
    @Override
	public Block setHardness(float parHardness)
    {
        blockHardness = parHardness;

        if (blockResistance < parHardness * 5.0F)
        {
            blockResistance = parHardness * 5.0F;
        }

        return this;
    }

    /**
     * This method will set the hardness of the block to -1, making it indestructible
     */
    @Override
	public Block setBlockUnbreakable()
    {
        setHardness(-1.0F);
        return this;
    }

    /**
     * Returns the block hardness at a location. Args: world, x, y, z
     */
    @Override
	public float getBlockHardness(World p_149712_1_, int p_149712_2_, int p_149712_3_, int p_149712_4_)
    {
        return blockHardness;
    }

    /**
     * Sets whether this block type will receive random update ticks
     */
    @Override
	public Block setTickRandomly(boolean p_149675_1_)
    {
        needsRandomTick = p_149675_1_;
        return this;
    }

    /**
     * Returns whether or not this block is of a type that needs random ticking. Called for ref-counting purposes by
     * ExtendedBlockStorage in order to broadly cull a chunk from the random chunk update list for efficiency's sake.
     */
    @Override
	public boolean getTickRandomly()
    {
        return needsRandomTick;
    }

    @Override
	@Deprecated //Forge: New Metadata sensitive version.
    public boolean hasTileEntity()
    {
        return hasTileEntity(0);
    }

    /**
     * How bright to render this block based on the light its receiving. Args: iBlockAccess, x, y, z
     */
    @Override
	@SideOnly(Side.CLIENT)
    public int getMixedBrightnessForBlock(IBlockAccess p_149677_1_, int p_149677_2_, int p_149677_3_, int p_149677_4_)
    {
        Block block = p_149677_1_.getBlock(p_149677_2_, p_149677_3_, p_149677_4_);
        int l = p_149677_1_.getLightBrightnessForSkyBlocks(p_149677_2_, p_149677_3_, p_149677_4_, block.getLightValue(p_149677_1_, p_149677_2_, p_149677_3_, p_149677_4_));

        if (l == 0 && block instanceof BlockSlab)
        {
            --p_149677_3_;
            block = p_149677_1_.getBlock(p_149677_2_, p_149677_3_, p_149677_4_);
            return p_149677_1_.getLightBrightnessForSkyBlocks(p_149677_2_, p_149677_3_, p_149677_4_, block.getLightValue(p_149677_1_, p_149677_2_, p_149677_3_, p_149677_4_));
        }
        else
        {
            return l;
        }
    }

    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: blockAccess, x, y, z, side
     */
    @Override
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_)
    {
        return p_149646_5_ == 0 && minY > 0.0D ? true : (p_149646_5_ == 1 && maxY < 1.0D ? true : (p_149646_5_ == 2 && minZ > 0.0D ? true : (p_149646_5_ == 3 && maxZ < 1.0D ? true : (p_149646_5_ == 4 && minX > 0.0D ? true : (p_149646_5_ == 5 && maxX < 1.0D ? true : !p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_).isOpaqueCube())))));
    }

    /**
     * Returns true if the given side of this block type should be rendered (if it's solid or not), if the adjacent
     * block is at the given coordinates. Args: blockAccess, x, y, z, side
     */
    @Override
	public boolean isBlockSolid(IBlockAccess p_149747_1_, int p_149747_2_, int p_149747_3_, int p_149747_4_, int p_149747_5_)
    {
        return true;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess p_149673_1_, int p_149673_2_, int p_149673_3_, int p_149673_4_, int p_149673_5_)
    {
        return getIcon(p_149673_5_, p_149673_1_.getBlockMetadata(p_149673_2_, p_149673_3_, p_149673_4_));
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int parSide, int parMetadata)
    {
        return blockIcon;
    }

    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    @Override
	public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_, int p_149743_3_, int p_149743_4_, AxisAlignedBB p_149743_5_, List p_149743_6_, Entity p_149743_7_)
    {
        AxisAlignedBB axisalignedbb1 = getCollisionBoundingBoxFromPool(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_);

        if (axisalignedbb1 != null && p_149743_5_.intersectsWith(axisalignedbb1))
        {
            p_149743_6_.add(axisalignedbb1);
        }
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return AxisAlignedBB.getBoundingBox(p_149668_2_ + minX, p_149668_3_ + minY, p_149668_4_ + minZ, p_149668_2_ + maxX, p_149668_3_ + maxY, p_149668_4_ + maxZ);
    }

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    @Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_, int p_149633_4_)
    {
        return AxisAlignedBB.getBoundingBox(p_149633_2_ + minX, p_149633_3_ + minY, p_149633_4_ + minZ, p_149633_2_ + maxX, p_149633_3_ + maxY, p_149633_4_ + maxZ);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
	public boolean isOpaqueCube()
    {
        return true;
    }

    /**
     * Returns whether this block is collideable based on the arguments passed in n@param par1 block metaData n@param
     * par2 whether the player right-clicked while holding a boat
     */
    @Override
	public boolean canCollideCheck(int parMetadata, boolean parRightClickWhileHoldingBoat)
    {
        return isCollidable();
    }

    /**
     * Returns if this block is collidable (only used by Fire). Args: x, y, z
     */
    @Override
	public boolean isCollidable()
    {
        return true;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {}

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World p_149734_1_, int p_149734_2_, int p_149734_3_, int p_149734_4_, Random p_149734_5_) {}

    /**
     * Called right before the block is destroyed by a player.  Args: world, x, y, z, metaData
     */
    @Override
	public void onBlockDestroyedByPlayer(World p_149664_1_, int p_149664_2_, int p_149664_3_, int p_149664_4_, int p_149664_5_) {}

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    @Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_) {}

    /**
     * How many world ticks before ticking
     */
    @Override
	public int tickRate(World p_149738_1_)
    {
        return 10;
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {}

    @Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
    {
        if (hasTileEntity(p_149749_6_))
        {
            p_149749_1_.removeTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);
        }
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
	public int quantityDropped(Random p_149745_1_)
    {
        return 1;
    }

    @Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return null;
    }

    /**
     * Gets the hardness of block at the given coordinates in the given world, relative to the ability of the given
     * EntityPlayer.
     */
    @Override
	public float getPlayerRelativeBlockHardness(EntityPlayer p_149737_1_, World p_149737_2_, int p_149737_3_, int p_149737_4_, int p_149737_5_)
    {
        return ForgeHooks.blockStrength(this, p_149737_1_, p_149737_2_, p_149737_3_, p_149737_4_, p_149737_5_);
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    @Override
	public void dropBlockAsItemWithChance(World parWorld, int parX, int parY, int parZ, int parMetaData, float parDropChance, int parFortune)
    {
        if (!parWorld.isRemote)
        {
            ArrayList<ItemStack> items = getDrops(parWorld, parX, parY, parZ, parMetaData, parFortune);
            parDropChance = ForgeEventFactory.fireBlockHarvesting(items, parWorld, this, parX, parY, parZ, parMetaData, parFortune, parDropChance, false, harvesters.get());

            for (ItemStack item : items)
            {
                if (parWorld.rand.nextFloat() <= parDropChance)
                {
                    dropBlockAsItem(parWorld, parX, parY, parZ, item);
                }
            }
        }
    }

    /**
     * Spawns EntityItem in the world for the given ItemStack if the world is not remote.
     */
    @Override
	protected void dropBlockAsItem(World p_149642_1_, int p_149642_2_, int p_149642_3_, int p_149642_4_, ItemStack p_149642_5_)
    {
        if (!p_149642_1_.isRemote && p_149642_1_.getGameRules().getGameRuleBooleanValue("doTileDrops"))
        {
            if (captureDrops.get())
            {
                capturedDrops.get().add(p_149642_5_);
                return;
            }
            float f = 0.7F;
            double d0 = p_149642_1_.rand.nextFloat() * f + (1.0F - f) * 0.5D;
            double d1 = p_149642_1_.rand.nextFloat() * f + (1.0F - f) * 0.5D;
            double d2 = p_149642_1_.rand.nextFloat() * f + (1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(p_149642_1_, p_149642_2_ + d0, p_149642_3_ + d1, p_149642_4_ + d2, p_149642_5_);
            entityitem.delayBeforeCanPickup = 10;
            p_149642_1_.spawnEntityInWorld(entityitem);
        }
    }

    /**
     * called by spawner, ore, redstoneOre blocks
     */
    @Override
	public void dropXpOnBlockBreak(World p_149657_1_, int p_149657_2_, int p_149657_3_, int p_149657_4_, int p_149657_5_)
    {
        if (!p_149657_1_.isRemote)
        {
            while (p_149657_5_ > 0)
            {
                int i1 = EntityXPOrb.getXPSplit(p_149657_5_);
                p_149657_5_ -= i1;
                p_149657_1_.spawnEntityInWorld(new EntityXPOrb(p_149657_1_, p_149657_2_ + 0.5D, p_149657_3_ + 0.5D, p_149657_4_ + 0.5D, i1));
            }
        }
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    @Override
	public int damageDropped(int p_149692_1_)
    {
        return 0;
    }

    /**
     * Returns how much this block can resist explosions from the passed in entity.
     */
    @Override
	public float getExplosionResistance(Entity p_149638_1_)
    {
        return blockResistance / 5.0F;
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
    @Override
	public void onBlockDestroyedByExplosion(World p_149723_1_, int p_149723_2_, int p_149723_3_, int p_149723_4_, Explosion p_149723_5_) {}

    @Override
	public boolean canReplace(World p_149705_1_, int p_149705_2_, int p_149705_3_, int p_149705_4_, int p_149705_5_, ItemStack p_149705_6_)
    {
        return canPlaceBlockOnSide(p_149705_1_, p_149705_2_, p_149705_3_, p_149705_4_, p_149705_5_);
    }

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    @Override
	@SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 0;
    }

    /**
     * checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    @Override
	public boolean canPlaceBlockOnSide(World p_149707_1_, int p_149707_2_, int p_149707_3_, int p_149707_4_, int p_149707_5_)
    {
        return canPlaceBlockAt(p_149707_1_, p_149707_2_, p_149707_3_, p_149707_4_);
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
	public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_)
    {
        return p_149742_1_.getBlock(p_149742_2_, p_149742_3_, p_149742_4_).isReplaceable(p_149742_1_, p_149742_2_, p_149742_3_, p_149742_4_);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        return false;
    }

    /**
     * Called whenever an entity is walking on top of this block. Args: world, x, y, z, entity
     */
    @Override
	public void onEntityWalking(World p_149724_1_, int p_149724_2_, int p_149724_3_, int p_149724_4_, Entity p_149724_5_) {}

    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    @Override
	public int onBlockPlaced(World p_149660_1_, int p_149660_2_, int p_149660_3_, int p_149660_4_, int p_149660_5_, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_)
    {
        return p_149660_9_;
    }

    /**
     * Called when a player hits the block. Args: world, x, y, z, player
     */
    @Override
	public void onBlockClicked(World p_149699_1_, int p_149699_2_, int p_149699_3_, int p_149699_4_, EntityPlayer p_149699_5_) {}

    /**
     * Can add to the passed in vector for a movement vector to be applied to the entity. Args: x, y, z, entity, vec3d
     */
    @Override
	public void velocityToAddToEntity(World p_149640_1_, int p_149640_2_, int p_149640_3_, int p_149640_4_, Entity p_149640_5_, Vec3 p_149640_6_) {}

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_) {}

    @Override
	@SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        return 0xFFFFFF; // white
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    @Override
	@SideOnly(Side.CLIENT)
    public int getRenderColor(int p_149741_1_)
    {
        return 0x000000;
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    @Override
	@SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
    {
        return 0x000000;
    }

    @Override
	public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_)
    {
        return 0;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    @Override
	public boolean canProvidePower()
    {
        return false;
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    @Override
	public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_) {}

    @Override
	public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_, int p_149748_5_)
    {
        return 0;
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    @Override
	public void setBlockBoundsForItemRender() {}

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    @Override
	public void harvestBlock(World p_149636_1_, EntityPlayer p_149636_2_, int p_149636_3_, int p_149636_4_, int p_149636_5_, int p_149636_6_)
    {
        p_149636_2_.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
        p_149636_2_.addExhaustion(0.025F);

        if (canSilkHarvest(p_149636_1_, p_149636_2_, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_) && EnchantmentHelper.getSilkTouchModifier(p_149636_2_))
        {
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            ItemStack itemstack = createStackedBlock(p_149636_6_);

            if (itemstack != null)
            {
                items.add(itemstack);
            }

            ForgeEventFactory.fireBlockHarvesting(items, p_149636_1_, this, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_, 0, 1.0f, true, p_149636_2_);
            for (ItemStack is : items)
            {
                dropBlockAsItem(p_149636_1_, p_149636_3_, p_149636_4_, p_149636_5_, is);
            }
        }
        else
        {
            harvesters.set(p_149636_2_);
            int i1 = EnchantmentHelper.getFortuneModifier(p_149636_2_);
            dropBlockAsItem(p_149636_1_, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_, i1);
            harvesters.set(null);
        }
    }

    /**
     * Return true if a player with Silk Touch can harvest this block directly, and not its normal drops.
     */
    @Override
	protected boolean canSilkHarvest()
    {
        return false;
    }

    /**
     * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage
     * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
     */
    @Override
	protected ItemStack createStackedBlock(int p_149644_1_)
    {
        int j = 0;
        Item item = Item.getItemFromBlock(this);

        if (item != null && item.getHasSubtypes())
        {
            j = p_149644_1_;
        }

        return new ItemStack(item, 1, j);
    }

    /**
     * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i' (inclusive).
     */
    @Override
	public int quantityDroppedWithBonus(int p_149679_1_, Random p_149679_2_)
    {
        return quantityDropped(p_149679_2_);
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    @Override
	public boolean canBlockStay(World p_149718_1_, int p_149718_2_, int p_149718_3_, int p_149718_4_)
    {
        return true;
    }

    /**
     * Called when the block is placed in the world.
     */
    @Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {}

    /**
     * Called after a block is placed
     */
    @Override
	public void onPostBlockPlaced(World p_149714_1_, int p_149714_2_, int p_149714_3_, int p_149714_4_, int p_149714_5_) {}

    /**
     * Gets the localized name of this block. Used for the statistics page.
     */
    @Override
	public String getLocalizedName()
    {
        return StatCollector.translateToLocal(getUnlocalizedName() + ".name");
    }

    /**
     * Returns the unlocalized name of the block with "tile." appended to the front.
     */
    @Override
	public String getUnlocalizedName()
    {
        return super.getUnlocalizedName();
    }

    @Override
	public boolean onBlockEventReceived(World p_149696_1_, int p_149696_2_, int p_149696_3_, int p_149696_4_, int p_149696_5_, int p_149696_6_)
    {
        return false;
    }

    /**
     * Return the state of blocks statistics flags - if the block is counted for mined and placed.
     */
    @Override
	public boolean getEnableStats()
    {
        return enableStats;
    }

    /**
     * Disable statistics for the block, the block will not count for mined or placed.
     */
    @Override
	protected Block disableStats()
    {
        enableStats = false;
        return this;
    }

    /**
     * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2 = total immobility
     * and stop pistons
     */
    @Override
	public int getMobilityFlag()
    {
        return blockMaterial.getMaterialMobility();
    }

    /**
     * Returns the default ambient occlusion value based on block opacity
     */
    @Override
	@SideOnly(Side.CLIENT)
    public float getAmbientOcclusionLightValue()
    {
        return isBlockNormalCube() ? 0.2F : 1.0F;
    }

    /**
     * Block's chance to react to an entity falling on it.
     */
    @Override
	public void onFallenUpon(World p_149746_1_, int p_149746_2_, int p_149746_3_, int p_149746_4_, Entity p_149746_5_, float p_149746_6_) {}

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @Override
	@SideOnly(Side.CLIENT)
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_)
    {
        return Item.getItemFromBlock(this);
    }

    /**
     * Get the block's damage value (for use with pick block).
     */
    @Override
	public int getDamageValue(World p_149643_1_, int p_149643_2_, int p_149643_3_, int p_149643_4_)
    {
        return damageDropped(p_149643_1_.getBlockMetadata(p_149643_2_, p_149643_3_, p_149643_4_));
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_)
    {
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
    }

    @Override
	public Block setCreativeTab(CreativeTabs p_149647_1_)
    {
        super.setCreativeTab(p_149647_1_);
        return this;
    }

    /**
     * Called when the block is attempted to be harvested
     */
    @Override
	public void onBlockHarvested(World p_149681_1_, int p_149681_2_, int p_149681_3_, int p_149681_4_, int p_149681_5_, EntityPlayer p_149681_6_) {}

    /**
     * Returns the CreativeTab to display the given block on.
     */
    @Override
	@SideOnly(Side.CLIENT)
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return super.getCreativeTabToDisplayOn();
    }

    /**
     * Called on server worlds only when the block is about to be replaced by a different block or the same block with a
     * different metadata value. Args: world, x, y, z, old metadata
     */
    @Override
	public void onBlockPreDestroy(World p_149725_1_, int p_149725_2_, int p_149725_3_, int p_149725_4_, int p_149725_5_) {}

    /**
     * currently only used by BlockCauldron to incrament meta-data during rain
     */
    @Override
	public void fillWithRain(World p_149639_1_, int p_149639_2_, int p_149639_3_, int p_149639_4_) {}

    /**
     * Returns true only if block is flowerPot
     */
    @Override
	@SideOnly(Side.CLIENT)
    public boolean isFlowerPot()
    {
        return false;
    }

    @Override
    // something related to block update schedule functionality
	public boolean func_149698_L()
    {
        return true;
    }

    /**
     * Return whether this block can drop from an explosion.
     */
    @Override
	public boolean canDropFromExplosion(Explosion p_149659_1_)
    {
        return false;
    }

    @Override
	public boolean isAssociatedBlock(Block p_149667_1_)
    {
        return this == p_149667_1_;
    }

    public static boolean isEqualTo(Block p_149680_0_, Block p_149680_1_)
    {
        return p_149680_0_ != null && p_149680_1_ != null ? (p_149680_0_ == p_149680_1_ ? true : p_149680_0_.isAssociatedBlock(p_149680_1_)) : false;
    }

    /**
     * If this returns true, then comparators facing away from this block will use the value from
     * getComparatorInputOverride instead of the actual redstone signal strength.
     */
    @Override
	public boolean hasComparatorInputOverride()
    {
        return false;
    }

    /**
     * If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
     * strength when this block inputs to a comparator.
     */
    @Override
	public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_, int p_149736_3_, int p_149736_4_, int p_149736_5_)
    {
        return 0;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public IIcon func_149735_b(int p_149735_1_, int p_149735_2_)
    {
        return getIcon(p_149735_1_, p_149735_2_);
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        blockIcon = p_149651_1_.registerIcon(getTextureName());
    }

    /**
     * Gets the icon name of the ItemBlock corresponding to this block. Used by hoppers.
     */
    @Override
	@SideOnly(Side.CLIENT)
    public String getItemIconName()
    {
        return null;
    }

    /* ======================================== FORGE START =====================================*/
    //For ForgeInternal use Only!
    protected ThreadLocal<EntityPlayer> harvesters = new ThreadLocal();
    private final ThreadLocal<Integer> silk_check_meta = new ThreadLocal(); 
    /**
     * Get a light value for the block at the specified coordinates, normal ranges are between 0 and 15
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return The light value
     */
    @Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);
        if (block != this)
        {
            return block.getLightValue(world, x, y, z);
        }
        /**
         * Gets the light value of the specified block coords. Args: x, y, z
         */
        return getLightValue();
    }

    /**
     * Checks if a player or entity can use this block to 'climb' like a ladder.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @param entity The entity trying to use the ladder, CAN be null.
     * @return True if the block should act like a ladder
     */
    @Override
	public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity)
    {
        return false;
    }

    /**
     * Return true if the block is a normal, solid cube.  This
     * determines indirect power state, entity ejection from blocks, and a few
     * others.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block is a full cube
     */
    @Override
	public boolean isNormalCube(IBlockAccess world, int x, int y, int z)
    {
        return true;
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
        int meta = world.getBlockMetadata(x, y, z);
        // TODO
        // should only be solid from top

        return isNormalCube(world, x, y, z);
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
        return blockMaterial.isReplaceable();
    }

    /**
     * Determines if this block should set fire and deal fire damage
     * to entities coming into contact with it.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block should deal damage
     */
    @Override
	public boolean isBurning(IBlockAccess world, int x, int y, int z)
    {
        return false;
    }

    /**
     * Determines this block should be treated as an air block
     * by the rest of the code. This method is primarily
     * useful for creating pure logic-blocks that will be invisible
     * to the player and otherwise interact as air would.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block considered air
     */
    @Override
	public boolean isAir(IBlockAccess world, int x, int y, int z)
    {
        return getMaterial() == Material.air;
    }

    /**
     * Determines if the player can harvest this block, obtaining it's drops when the block is destroyed.
     *
     * @param player The player damaging the block, may be null
     * @param meta The block's current metadata
     * @return True to spawn the drops
     */
    @Override
	public boolean canHarvestBlock(EntityPlayer player, int meta)
    {
        return ForgeHooks.canHarvestBlock(this, player, meta);
    }

    /**
     * Called when a player removes a block.  This is responsible for
     * actually destroying the block, and the block is intact at time of call.
     * This is called regardless of whether the player can harvest the block or
     * not.
     *
     * Return true if the block is actually destroyed.
     *
     * Note: When used in multiplayer, this is called on both client and
     * server sides!
     *
     * @param world The current world
     * @param player The player damaging the block, may be null
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @param willHarvest True if Block.harvestBlock will be called after this, if the return in true. 
     *        Can be useful to delay the destruction of tile entities till after harvestBlock
     * @return True if the block is actually destroyed.
     */
    @Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        return removedByPlayer(world, player, x, y, z);
    }

    @Override
	@Deprecated
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        return world.setBlockToAir(x, y, z);
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
        return Blocks.fire.getFlammability(this);
    }

    /**
     * Called when fire is updating, checks if a block face can catch fire.
     *
     *
     * @param world The current world
     * @param x The blocks X position
     * @param y The blocks Y position
     * @param z The blocks Z position
     * @param face The face that the fire is coming from
     * @return True if the face can be on fire, false otherwise.
     */
    @Override
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face)
    {
        return getFlammability(world, x, y, z, face) > 0;
    }

    /**
     * Called when fire is updating on a neighbor block.
     * The higher the number returned, the faster fire will spread around this block.
     *
     * @param world The current world
     * @param x The blocks X position
     * @param y The blocks Y position
     * @param z The blocks Z position
     * @param face The face that the fire is coming from
     * @return A number that is used to determine the speed of fire growth around the block
     */
    @Override
	public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face)
    {
        return Blocks.fire.getEncouragement(this);
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
        if (this == Blocks.netherrack && side == UP)
        {
            return true;
        }
        if ((world.provider instanceof WorldProviderEnd) && this == Blocks.bedrock && side == UP)
        {
            return true;
        }
        return false;
    }

    private final boolean isTileProvider = this instanceof ITileEntityProvider;
    /**
     * Called throughout the code as a replacement for block instanceof BlockContainer
     * Moving this to the Block base class allows for mods that wish to extend vanilla
     * blocks, and also want to have a tile entity on that block, may.
     *
     * Return true from this function to specify this block has a tile entity.
     *
     * @param metadata Metadata of the current block
     * @return True if block has a tile entity, false otherwise
     */
    @Override
	public boolean hasTileEntity(int metadata)
    {
        return isTileProvider;
    }

    /**
     * Called throughout the code as a replacement for ITileEntityProvider.createNewTileEntity
     * Return the same thing you would from that function.
     * This will fall back to ITileEntityProvider.createNewTileEntity(World) if this block is a ITileEntityProvider
     *
     * @param metadata The Metadata of the current block
     * @return A instance of a class extending TileEntity
     */
    @Override
	public TileEntity createTileEntity(World world, int metadata)
    {
        if (isTileProvider)
        {
            return ((ITileEntityProvider)this).createNewTileEntity(world, metadata);
        }
        return null;
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
        return quantityDroppedWithBonus(fortune, random);
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

        int count = quantityDropped(metadata, fortune, world.rand);
        for(int i = 0; i < count; i++)
        {
            Item item = getItemDropped(metadata, world.rand, fortune);
            if (item != null)
            {
                ret.add(new ItemStack(item, 1, damageDropped(metadata)));
            }
        }
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
        silk_check_meta.set(metadata);;
        boolean ret = canSilkHarvest();
        silk_check_meta.set(null);
        return ret;
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
     * Determines if this block is classified as a Bed, Allowing
     * players to sleep in it, though the block has to specifically
     * perform the sleeping functionality in it's activated event.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param player The player or camera entity, null in some cases.
     * @return True to treat this as a bed
     */
    @Override
	public boolean isBed(IBlockAccess world, int x, int y, int z, EntityLivingBase player)
    {
        return false;
    }

    /**
     * Called when a leaf should start its decay process.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     */
    @Override
	public void beginLeavesDecay(World world, int x, int y, int z){}

    /**
     * Determines if this block can prevent leaves connected to it from decaying.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @return true if the presence this block can prevent leaves from decaying.
     */
    @Override
	public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z)
    {
        return false;
    }

    /**
     * Determines if this block is considered a leaf block, used to apply the leaf decay and generation system.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @return true if this block is considered leaves.
     */
    @Override
	public boolean isLeaves(IBlockAccess world, int x, int y, int z)
    {
        return false;
    }

    /**
     * Used during tree growth to determine if newly generated leaves can replace this block.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @return true if this block can be replaced by growing leaves.
     */
    @Override
	public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z)
    {
        return false;
    }

    /**
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @return  true if the block is wood (logs)
     */
    @Override
	public boolean isWood(IBlockAccess world, int x, int y, int z)
    {
         return false;
    }

    /**
     * Determines if the current block is replaceable by Ore veins during world generation.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param target The generic target block the gen is looking for, Standards define stone
     *      for overworld generation, and neatherack for the nether.
     * @return True to allow this block to be replaced by a ore
     */
    @Override
	public boolean isReplaceableOreGen(World world, int x, int y, int z, Block target)
    {
        return false;
    }

    /**
     * Location sensitive version of getExplosionRestance
     *
     * @param par1Entity The entity that caused the explosion
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param explosionX Explosion source X Position
     * @param explosionY Explosion source X Position
     * @param explosionZ Explosion source X Position
     * @return The amount of the explosion absorbed.
     */
    @Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
    {
        /**
         * Returns how much this block can resist explosions from the passed in entity.
         */
        return getExplosionResistance(par1Entity);
    }

    /**
     * Called when the block is destroyed by an explosion.
     * Useful for allowing the block to take into account tile entities,
     * metadata, etc. when exploded, before it is removed.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param Explosion The explosion instance affecting the block
     */
    @Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion)
    {
        world.setBlockToAir(x, y, z);
        onBlockDestroyedByExplosion(world, x, y, z, explosion);
    }

    /**
     * Determine if this block can make a redstone connection on the side provided,
     * Useful to control which sides are inputs and outputs for redstone wires.
     *
     * Side:
     *  -1: UP
     *   0: NORTH
     *   1: EAST
     *   2: SOUTH
     *   3: WEST
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param side The side that is trying to make the connection
     * @return True to make the connection
     */
    @Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
        /**
         * Can this block provide power. Only wire currently seems to have this change based on its state.
         */
        return false;
    }

    /**
     * Determines if a torch can be placed on the top surface of this block.
     * Useful for creating your own block that torches can be on, such as fences.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @return True to allow the torch to be placed
     */
    @Override
	public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
    {
    	return false;
    }

    /**
     * Called when a user uses the creative pick block button on this block
     *
     * @param target The full target the player is looking at
     * @return A ItemStack to add to the player's inventory, Null if nothing should be added.
     */
    @Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        Item item = getItem(world, x, y, z);

        if (item == null)
        {
            return null;
        }

        Block block = item instanceof ItemBlock && !isFlowerPot() ? Block.getBlockFromItem(item) : this;
        return new ItemStack(item, 1, block.getDamageValue(world, x, y, z));
    }

    /**
     * Used by getTopSolidOrLiquidBlock while placing biome decorations, villages, etc
     * Also used to determine if the player can spawn on this block.
     *
     * @return False to disallow spawning
     */
    @Override
	public boolean isFoliage(IBlockAccess world, int x, int y, int z)
    {
        return false;
    }

    /**
     * Spawn a digging particle effect in the world, this is a wrapper
     * around EffectRenderer.addBlockHitEffects to allow the block more
     * control over the particles. Useful when you have entirely different
     * texture sheets for different sides/locations in the world.
     *
     * @param world The current world
     * @param target The target the player is looking at {x/y/z/side/sub}
     * @param effectRenderer A reference to the current effect renderer.
     * @return True to prevent vanilla digging particles form spawning.
     */
    @Override
	@SideOnly(Side.CLIENT)
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer)
    {
        return false;
    }

    /**
     * Spawn particles for when the block is destroyed. Due to the nature
     * of how this is invoked, the x/y/z locations are not always guaranteed
     * to host your block. So be sure to do proper sanity checks before assuming
     * that the location is this block.
     *
     * @param world The current world
     * @param x X position to spawn the particle
     * @param y Y position to spawn the particle
     * @param z Z position to spawn the particle
     * @param meta The metadata for the block before it was destroyed.
     * @param effectRenderer A reference to the current effect renderer.
     * @return True to prevent vanilla break particles from spawning.
     */
    @Override
	@SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
    {
        return false;
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
        return false;
    }

    /**
     * Called when a plant grows on this block, only implemented for saplings using the WorldGen*Trees classes right now.
     * Modder may implement this for custom plants.
     * This does not use ForgeDirection, because large/huge trees can be located in non-representable direction,
     * so the source location is specified.
     * Currently this just changes the block to dirt if it was grass.
     *
     * Note: This happens DURING the generation, the generation may not be complete when this is called.
     *
     * @param world Current world
     * @param x Soil X
     * @param y Soil Y
     * @param z Soil Z
     * @param sourceX Plant growth location X
     * @param sourceY Plant growth location Y
     * @param sourceZ Plant growth location Z
     */
    @Override
	public void onPlantGrow(World world, int x, int y, int z, int sourceX, int sourceY, int sourceZ)
    {
    }

    /**
     * Checks if this soil is fertile, typically this means that growth rates
     * of plants on this soil will be slightly sped up.
     * Only vanilla case is tilledField when it is within range of water.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z position
     * @return True if the soil should be considered fertile.
     */
    @Override
	public boolean isFertile(World world, int x, int y, int z)
    {
        return false;
    }

    /**
     * Location aware and overrideable version of the lightOpacity array,
     * return the number to subtract from the light value when it passes through this block.
     *
     * This is not guaranteed to have the tile entity in place before this is called, so it is
     * Recommended that you have your tile entity call relight after being placed if you
     * rely on it for light info.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z position
     * @return The amount of light to block, 0 for air, 255 for fully opaque.
     */
    @Override
	public int getLightOpacity(IBlockAccess world, int x, int y, int z)
    {
        return getLightOpacity();
    }

    /**
     * Determines if this block is can be destroyed by the specified entities normal behavior.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z position
     * @return True to allow the ender dragon to destroy this block
     */
    @Override
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity)
    {
        return false;
    }

    /**
     * Determines if this block can be used as the base of a beacon.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z position
     * @param beaconX Beacons X Position
     * @param beaconY Beacons Y Position
     * @param beaconZ Beacons Z Position
     * @return True, to support the beacon, and make it active with this block.
     */
    @Override
	public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ)
    {
        return false;
    }

    /**
     * Rotate the block. For vanilla blocks this rotates around the axis passed in (generally, it should be the "face" that was hit).
     * Note: for mod blocks, this is up to the block and modder to decide. It is not mandated that it be a rotation around the
     * face, but could be a rotation to orient *to* that face, or a visiting of possible rotations.
     * The method should return true if the rotation was successful though.
     *
     * @param worldObj The world
     * @param x X position
     * @param y Y position
     * @param z Z position
     * @param axis The axis to rotate around
     * @return True if the rotation was successful, False if the rotation failed, or is not possible
     */
    @Override
	public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis)
    {
        return RotationHelper.rotateVanillaBlock(this, worldObj, x, y, z, axis);
    }

    /**
     * Get the rotations that can apply to the block at the specified coordinates. Null means no rotations are possible.
     * Note, this is up to the block to decide. It may not be accurate or representative.
     * @param worldObj The world
     * @param x X position
     * @param y Y position
     * @param z Z position
     * @return An array of valid axes to rotate around, or null for none or unknown
     */
    @Override
	public ForgeDirection[] getValidRotations(World worldObj, int x, int y, int z)
    {
        return RotationHelper.getValidVanillaBlockRotations(this);
    }

    /**
     * Determines the amount of enchanting power this block can provide to an enchanting table.
     * @param world The World
     * @param x X position
     * @param y Y position
     * @param z Z position
     * @return The amount of enchanting power this block produces.
     */
    @Override
	public float getEnchantPowerBonus(World world, int x, int y, int z)
    {
        return 0;
    }

    /**
     * Common way to recolour a block with an external tool
     * @param world The world
     * @param x X
     * @param y Y
     * @param z Z
     * @param side The side hit with the colouring tool
     * @param colour The colour to change to
     * @return If the recolouring was successful
     */
    @Override
	public boolean recolourBlock(World world, int x, int y, int z, ForgeDirection side, int colour)
    {
        return false;
    }

    /**
     * Gathers how much experience this block drops when broken.
     * 
     * @param world The world
     * @param metadata
     * @param fortune
     * @return Amount of XP from breaking this block.
     */
    @Override
	public int getExpDrop(IBlockAccess world, int metadata, int fortune)
    {
        return 0;
    }

    /**
     * Called when a tile entity on a side of this block changes is created or is destroyed.
     * @param world The world
     * @param x The x position of this block instance
     * @param y The y position of this block instance
     * @param z The z position of this block instance
     * @param tileX The x position of the tile that changed
     * @param tileY The y position of the tile that changed
     * @param tileZ The z position of the tile that changed
     */
    @Override
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ)
    {
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
        return isNormalCube();
    }

    /**
     * If this block should be notified of weak changes.
     * Weak changes are changes 1 block away through a solid block.
     * Similar to comparators.
     * 
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @param side The side to check
     * @return true To be notified of changes
     */
    @Override
	public boolean getWeakChanges(IBlockAccess world, int x, int y, int z)
    {
        return false;
    }

    private final String[] harvestTool = new String[16];
    private final int[] harvestLevel = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    /**
     * Sets or removes the tool and level required to harvest this block.
     * 
     * @param toolClass Class
     * @param level Harvest level:
     *     Wood:    0
     *     Stone:   1
     *     Iron:    2
     *     Diamond: 3
     *     Gold:    0
     */
    @Override
	public void setHarvestLevel(String toolClass, int level)
    {
        for (int m = 0; m < 16; m++)
        {
            setHarvestLevel(toolClass, level, m);
        }
    }

    /**
     * Sets or removes the tool and level required to harvest this block.
     * 
     * @param toolClass Class
     * @param level Harvest level:
     *     Wood:    0
     *     Stone:   1
     *     Iron:    2
     *     Diamond: 3
     *     Gold:    0
     * @param metadata The specific metadata to set
     */
    @Override
	public void setHarvestLevel(String toolClass, int level, int metadata)
    {
        harvestTool[metadata] = toolClass;
        harvestLevel[metadata] = level;
    }

    /**
     * Queries the class of tool required to harvest this block, if null is returned 
     * we assume that anything can harvest this block.
     * 
     * @param metadata
     * @return
     */
    @Override
	public String getHarvestTool(int metadata)
    {
        return harvestTool[metadata];
    }

    /**
     * Queries the harvest level of this item stack for the specifred tool class,
     * Returns -1 if this tool is not of the specified type
     * 
     * @param stack This item stack instance
     * @return Harvest level, or -1 if not the specified tool type.
     */
    @Override
	public int getHarvestLevel(int metadata)
    {
        return harvestLevel[metadata];
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
        if ("pickaxe".equals(type) && (this == Blocks.redstone_ore || this == Blocks.lit_redstone_ore || this == Blocks.obsidian))
            return false;
        if (harvestTool[metadata] == null) return false;
        return harvestTool[metadata].equals(type);
    }
}
