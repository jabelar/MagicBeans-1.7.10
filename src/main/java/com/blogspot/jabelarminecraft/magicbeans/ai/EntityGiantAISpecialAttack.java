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
import net.minecraft.entity.ai.EntityAITarget;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.entities.EntityGiant;

/**
 * @author jabelar
 *
 */
public class EntityGiantAISpecialAttack extends EntityAITarget
{
    /** The entity that this task belongs to */
    protected EntityCreature taskOwner;
 
    public EntityGiantAISpecialAttack(EntityCreature parCreature)
    {
        super(parCreature, true, false);
        taskOwner = parCreature;
        setMutexBits(1);
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
    	else // giant is hostile
    	{
        	if (taskOwner instanceof EntityGiant) 
        	{
            	return (((EntityGiant)taskOwner).getSpecialAttackTimer() == 20); // timer just started
        	}
        	else // not a giant
        	{
        		return false;
        	}
    	}    	
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
	public void startExecuting()
    {
    	// DEBUG
    	System.out.println("Start executing special attack AI");
//		((EntityGiant) taskOwner).setSpecialAttackTimer(20);
    }
    
    @Override
	public boolean continueExecuting()
    {
    	if (!MagicBeans.configGiantIsHostile)
		{
			return false;
		}
    	else // giant is hostile
    	{
        	EntityGiant theGiant = (EntityGiant)taskOwner;
        	
        	// decrement counter
//        	theGiant.setSpecialAttackTimer(theGiant.getSpecialAttackTimer()-1);
        	if (theGiant.getSpecialAttackTimer() <= 0)
        	{
        		// DEBUG
        		System.out.println("Stopping special attack AI");
        		theGiant.getSpecialAttack().doGiantAttack(Math.round(MagicBeans.configGiantAttackDamage*3));
        		return false;
        	}
        	else // timer still active
        	{
        		return true;
        	}
    	}    	
    }
}