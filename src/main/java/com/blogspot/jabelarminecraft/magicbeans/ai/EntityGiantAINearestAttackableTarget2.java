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

import java.util.Collections;
import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;

/**
 * @author jabelar
 *
 */
public class EntityGiantAINearestAttackableTarget2 extends EntityAITarget
{
    /** The entity that this task belongs to */
    protected EntityCreature taskOwner;
    /** If true, EntityAI targets must be able to be seen (cannot be blocked by walls) to be suitable targets. */
    protected boolean shouldCheckSight;
    protected final Class targetClass;
    /** Instance of EntityAINearestAttackableTargetSorter. */
    protected final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
    /**
     * This filter is applied to the Entity search.  Only matching entities will be targetted.  (null -> no
     * restrictions)
     */
    protected final IEntitySelector targetEntitySelector;
    protected EntityLivingBase targetEntity;

	public EntityGiantAINearestAttackableTarget2(EntityCreature parCreature, Class parTargetClass)
	{
		this(parCreature, parTargetClass, (IEntitySelector)null);
	}

    public EntityGiantAINearestAttackableTarget2(EntityCreature parCreature, Class parTargetClass, final IEntitySelector parEntitySelector)
    {
        super(parCreature, true, false);
        taskOwner = parCreature;
        targetClass = parTargetClass;
        theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(parCreature);
        setMutexBits(1);
        targetEntitySelector = new IEntitySelector()
        {
            /**
             * Return whether the specified entity is applicable to this filter.
             */
            @Override
			public boolean isEntityApplicable(Entity parEntity)
            {
                return !(parEntity instanceof EntityLivingBase) ? false : (parEntitySelector != null && !parEntitySelector.isEntityApplicable(parEntity) ? false : EntityGiantAINearestAttackableTarget2.this.isSuitableTarget((EntityLivingBase)parEntity, false));
            }
        };
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
    	
    	// similar to regular nearest target AI, except chance is always 100%

//        if (targetChance > 0 && taskOwner.getRNG().nextInt(targetChance) != 0)
//        {
//            return false;
//        }
//        else
        {
            double d0 = getTargetDistance();
            // DEBUG
            if (taskOwner == null) {
            	System.out.println("task owner is null");
            }
            if (targetEntitySelector == null) {
            	System.out.println("entity selector is null");
            }
            
            List list = taskOwner.worldObj.selectEntitiesWithinAABB(targetClass, taskOwner.boundingBox.expand(d0, 4.0D, d0), targetEntitySelector);
            Collections.sort(list, theNearestAttackableTargetSorter);

            if (list.isEmpty())
            {
                return false;
            }
            else
            {
                targetEntity = (EntityLivingBase)list.get(0);
                return true;
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
	public void startExecuting()
    {
        taskOwner.setAttackTarget(targetEntity);
        super.startExecuting();
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