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

package com.blogspot.jabelarminecraft.magicbeans.materials;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

/**
 * @author jabelar
 *
 */
public class MaterialCloud extends Material
{
    /** Bool defining if the block can burn or not. */
    protected boolean canBurn = false;
    /**
     * Determines whether blocks with this material can be "overwritten" by other blocks when placed - eg snow, vines
     * and tall grass.
     */
    protected boolean replaceable = false;
    /** Indicates if the material is translucent */
    protected boolean isTranslucent = true;
    /**
     * Determines if the material can be harvested without a tool (or with the wrong tool)
     */
    protected boolean requiresNoTool = true;
    /**
     * Mobility information flag. 0 indicates that this block is normal, 1 indicates that it can't push other blocks, 2
     * indicates that it can't be pushed.
     */
    protected int mobilityFlag;
    protected boolean isAdventureModeExempt;

	public MaterialCloud() 
	{
		super(MapColor.snowColor);
		setAdventureModeExempt();
		setNoPushMobility();
		setRequiresTool();
		setTranslucent(true);
	}

    /**
     * Returns if blocks of these materials are liquids.
     */
    @Override
	public boolean isLiquid()
    {
        return false;
    }

    @Override
	public boolean isSolid()
    {
        return true;
    }

    /**
     * Will prevent grass from growing on dirt underneath and kill any grass below it if it returns true
     */
    @Override
	public boolean getCanBlockGrass()
    {
        return true;
    }

    /**
     * Returns if this material is considered solid or not
     */
    @Override
	public boolean blocksMovement()
    {
        return true;
    }

    /**
     * Marks the material as translucent
     */
    private Material setTranslucent(boolean parIsTranslucent)
    {
        isTranslucent = parIsTranslucent;
        return this;
    }

    /**
     * Makes blocks with this material require the correct tool to be harvested.
     */
    @Override
	protected Material setRequiresTool()
    {
        requiresNoTool = false;
        return this;
    }

    /**
     * Set the canBurn bool to True and return the current object.
     */
    @Override
	protected Material setBurning()
    {
        canBurn = true;
        return this;
    }

    /**
     * Returns if the block can burn or not.
     */
    @Override
	public boolean getCanBurn()
    {
        return canBurn;
    }

    /**
     * Sets {@link #replaceable} to true.
     */
    @Override
	public Material setReplaceable()
    {
        replaceable = true;
        return this;
    }

    /**
     * Returns whether the material can be replaced by other blocks when placed - eg snow, vines and tall grass.
     */
    @Override
	public boolean isReplaceable()
    {
        return replaceable;
    }

    /**
     * Indicate if the material is opaque
     */
    @Override
	public boolean isOpaque()
    {
        return isTranslucent ? false : blocksMovement();
    }

    /**
     * Returns true if the material can be harvested without a tool (or with the wrong tool)
     */
    @Override
	public boolean isToolNotRequired()
    {
        return requiresNoTool;
    }

    /**
     * Returns the mobility information of the material, 0 = free, 1 = can't push but can move over, 2 = total
     * immobility and stop pistons.
     */
    @Override
	public int getMaterialMobility()
    {
        return mobilityFlag;
    }

    /**
     * This type of material can't be pushed, but pistons can move over it.
     */
    @Override
	protected Material setNoPushMobility()
    {
        mobilityFlag = 1;
        return this; // allows chaining
    }

    /**
     * This type of material can't be pushed, and pistons are blocked to move.
     */
    @Override
	protected Material setImmovableMobility()
    {
        mobilityFlag = 2;
        return this; // allows chaining
    }

    /**
     * @see #isAdventureModeExempt()
     */
    @Override
	protected Material setAdventureModeExempt()
    {
        isAdventureModeExempt = true;
        return this; // allows chaining
    }

    /**
     * Returns true if blocks with this material can always be mined in adventure mode.
     */
    @Override
	public boolean isAdventureModeExempt()
    {
        return isAdventureModeExempt;
    }
}
