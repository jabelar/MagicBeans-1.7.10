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
