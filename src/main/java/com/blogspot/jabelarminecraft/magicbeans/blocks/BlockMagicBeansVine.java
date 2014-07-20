package com.blogspot.jabelarminecraft.magicbeans.blocks;

import net.minecraft.block.BlockVine;
import net.minecraft.world.World;

public class BlockMagicBeansVine extends BlockVine
{
	public BlockMagicBeansVine()
	{
		super();
    	// Basic block setup
        setBlockName("magicbeansvine");
        setBlockTextureName("magicbeans:vine");

	}

    /**
     * checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    @Override
	public boolean canPlaceBlockOnSide(World parWorld, int parX, int parY, int parZ, int parSide)
    {
    	// DEBUG
    	System.out.println("canPlaceBlockOnSide() check for side ="+parSide);
    	return true;
//        switch (parSide)
//        {
//            case 1:
//            {
//            	return true;
//            }
//            case 2:
//            {
//            	if (parWorld.getBlock(parX, parY, parZ + 1)==MagicBeans.blockMagicBeanStalk)
//            	{
//            		return true;
//            	}
//            	else
//            	{
//            		return false;
//            	}
//            }
//            case 3:
//            {
//            	if (parWorld.getBlock(parX, parY, parZ - 1)==MagicBeans.blockMagicBeanStalk)
//            	{
//            		return true;
//            	}
//            	else
//            	{
//            		return false;
//            	}
//            }
//            case 4:
//            {
//            	if (parWorld.getBlock(parX + 1, parY, parZ)==MagicBeans.blockMagicBeanStalk)
//            	{
//            		return true;
//            	}
//            	else
//            	{
//            		return false;
//            	}
//            }
//            case 5:
//            {
//            	if (parWorld.getBlock(parX - 1, parY, parZ)==MagicBeans.blockMagicBeanStalk)
//            	{
//            		return true;
//            	}
//            	else
//            	{
//            		return false;
//            	}
//            }
//            default:
//                return false;
//        }
    }

}
