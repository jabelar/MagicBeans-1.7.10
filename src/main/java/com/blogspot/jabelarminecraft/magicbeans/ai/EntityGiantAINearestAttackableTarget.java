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

package com.blogspot.jabelarminecraft.magicbeans.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;

/**
 * @author jabelar
 *
 */
public class EntityGiantAINearestAttackableTarget extends EntityAINearestAttackableTarget
{
    public EntityGiantAINearestAttackableTarget(EntityCreature parCreatureExecutingAI, Class parClassToTarget, int parChanceToTarget, boolean parUseSight, boolean parOnlyTargetIfInReach)
    {
        super(parCreatureExecutingAI, parClassToTarget, parChanceToTarget, parUseSight, parOnlyTargetIfInReach);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
	public boolean shouldExecute()
    {
    	if (!MagicBeans.configGiantIsHostile)
    	{
    		return false;
    	}
    	return super.shouldExecute();
    }
    
    @Override
	public boolean continueExecuting()
    {
    	if (!MagicBeans.configGiantIsHostile)
    	{
    		return false;
    	}
    	return super.continueExecuting();
    }
}