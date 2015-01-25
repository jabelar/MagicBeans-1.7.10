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
import net.minecraft.entity.player.EntityPlayer;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.entities.EntityGiant;

/**
 * @author jabelar
 *
 */
public class EntityGiantAINearestAttackableTarget2 extends EntityAITarget {
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

	public EntityGiantAINearestAttackableTarget2(EntityCreature parCreature, Class parTargetClass) {
		this(parCreature, parTargetClass, (IEntitySelector)null);
	}

    public EntityGiantAINearestAttackableTarget2(EntityCreature parCreature, Class parTargetClass, final IEntitySelector parEntitySelector) {
        super(parCreature, true, false);
        taskOwner = parCreature;
        targetClass = parTargetClass;
        theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(parCreature);
        setMutexBits(1);
        targetEntitySelector = new IEntitySelector() {
            /**
             * Return whether the specified entity is applicable to this filter.
             */
            @Override
			public boolean isEntityApplicable(Entity parEntity) {
                return !(parEntity instanceof EntityLivingBase) ? false : (parEntitySelector != null && !parEntitySelector.isEntityApplicable(parEntity) ? false : EntityGiantAINearestAttackableTarget2.this.isSuitableTarget((EntityLivingBase)parEntity, false));
            }
        };
    }

	/**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
	public boolean shouldExecute() {    	
    	if (!MagicBeans.configGiantIsHostile) {
			return false;
		}
        double d0 = getTargetDistance();
        
        List list = taskOwner.worldObj.selectEntitiesWithinAABB(targetClass, taskOwner.boundingBox.expand(d0, 4.0D, d0), targetEntitySelector);
        Collections.sort(list, theNearestAttackableTargetSorter);

        if (list.isEmpty()) {
            return false;
        } else {
            targetEntity = (EntityLivingBase)list.get(0);
            if (targetEntity instanceof EntityPlayer) {
            	return !((EntityPlayer)targetEntity).capabilities.isCreativeMode;
            }
            return true;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
	public void startExecuting() {
    	// DEBUG
    	System.out.println("Start executing attack AI");
    	
        taskOwner.setAttackTarget(targetEntity);
        if (taskOwner instanceof EntityGiant) {
        	EntityGiant theGiant = (EntityGiant)taskOwner;
            theGiant.setIsAttacking(true);
        }
        super.startExecuting();
    }
    
    @Override
	public boolean continueExecuting() {
    	if (!MagicBeans.configGiantIsHostile) {
			return false;
		}
    	
    	if (targetEntity.isDead) { // dead
        	// DEBUG
        	System.out.println("Stop executing attack AI");

        	if (taskOwner instanceof EntityGiant) {
            	EntityGiant theGiant = (EntityGiant)taskOwner;
                theGiant.setIsAttacking(false);
            }
        	return false;
    	} else {
        	if (taskOwner instanceof EntityGiant)
            {
            	EntityGiant theGiant = (EntityGiant)taskOwner;
            	if (theGiant.ticksExisted%200 == 0) { // every 10 seconds
        			if (theGiant.worldObj.rand.nextInt(10)<8) { // 80% chance
        				if (!theGiant.isInWater()) {
        					// DEBUG
        					System.out.println("Giant jump attack!");
        					// setJumping(true);
        					theGiant.setSpecialAttackTimer(20);
        					theGiant.setIsAttacking(false);
        					return false;
        				}
        			}
            	}
            }
    		return true;
    	}
    }
}