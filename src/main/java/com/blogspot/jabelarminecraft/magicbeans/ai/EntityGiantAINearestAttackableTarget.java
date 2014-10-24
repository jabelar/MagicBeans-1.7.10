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
import java.util.Comparator;
import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author jabelar
 *
 */
public class EntityGiantAINearestAttackableTarget extends EntityAITarget
{
    private final Class targetClass;
    private final int targetChance;
    /** Instance of EntityAINearestAttackableTargetSorter. */
    private final EntityGiantAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
    /**
     * This filter is applied to the Entity search.  Only matching entities will be targetted.  (null -> no
     * restrictions)
     */
    private final IEntitySelector targetEntitySelector;
    private EntityLivingBase targetEntity;

    public EntityGiantAINearestAttackableTarget(EntityCreature parCreatureExecutingAI, Class parClassToTarget, int parChanceToTarget, boolean parUseSight, boolean parOnlyTargetIfInReach, final IEntitySelector p_i1665_6_)
    {
        super(parCreatureExecutingAI, parUseSight, parOnlyTargetIfInReach);
        targetClass = parClassToTarget;
        targetChance = parChanceToTarget;
        theNearestAttackableTargetSorter = new EntityGiantAINearestAttackableTarget.Sorter(parCreatureExecutingAI);
        setMutexBits(1);
        targetEntitySelector = new IEntitySelector()
        {
            /**
             * Return whether the specified entity is applicable to this filter.
             */
            @Override
			public boolean isEntityApplicable(Entity parEntity)
            {
                return !(parEntity instanceof EntityLivingBase) ? false : (p_i1665_6_ != null && !p_i1665_6_.isEntityApplicable(parEntity) ? false : EntityGiantAINearestAttackableTarget.this.isSuitableTarget((EntityLivingBase)parEntity, false));
            }
        };
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
	public boolean shouldExecute()
    {
        if (targetChance > 0 && taskOwner.getRNG().nextInt(targetChance) != 0)
        {
            return false;
        }
        else
        {
            double d0 = getTargetDistance();
            List list = taskOwner.worldObj.selectEntitiesWithinAABB(targetClass, taskOwner.boundingBox.expand(d0, 4.0D, d0), targetEntitySelector);
            Collections.sort(list, theNearestAttackableTargetSorter);

            if (list.isEmpty())
            {
                return false;
            }
            else
            {
                targetEntity = (EntityLivingBase)list.get(0);
                // DEBUG
                System.out.println("Found target, is player = "+(targetEntity instanceof EntityPlayer));
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
    	// DEBUG
    	System.out.println("EntityGiantAINearestAttackableTarget startExecuting");
        taskOwner.setAttackTarget(targetEntity);
        super.startExecuting();
    }

    public static class Sorter implements Comparator
        {
            private final Entity theEntity;

            public Sorter(Entity parEntity)
            {
                theEntity = parEntity;
            }

            public int compare(Entity parEntity1, Entity parEntity2)
            {
                double d0 = theEntity.getDistanceSqToEntity(parEntity1);
                double d1 = theEntity.getDistanceSqToEntity(parEntity2);
                return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
            }

            @Override
			public int compare(Object parObject1, Object parObject2)
            {
                return compare((Entity)parObject1, (Entity)parObject2);
            }
        }
}