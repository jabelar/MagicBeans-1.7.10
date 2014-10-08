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

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.blogspot.jabelarminecraft.magicbeans.entities.EntityGiant;


public class EntityGiantAISeePlayer extends EntityAIBase
{
    private final EntityGiant theGiant;
    private EntityPlayer thePlayer;
    private final World worldObject;
    private final double minPlayerDistance;

    public EntityGiantAISeePlayer(EntityGiant parEntityGiant, double parMinPlayerDistance)
    {
        theGiant = parEntityGiant;
        worldObject = parEntityGiant.worldObj;
        minPlayerDistance = parMinPlayerDistance;
        // setMutexBits(2);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
	public boolean shouldExecute()
    {
        thePlayer = worldObject.getClosestPlayerToEntity(theGiant, minPlayerDistance);
        return thePlayer != null && theGiant.canEntityBeSeen(thePlayer) ? true : false;
   }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
	public boolean continueExecuting()
    {
        return !thePlayer.isEntityAlive() ? false : (theGiant.getDistanceSqToEntity(thePlayer) <= minPlayerDistance * minPlayerDistance);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
	public void startExecuting()
    {
    	// target the player
        theGiant.setAttackTarget(thePlayer);
    }

    /**
     * Resets the task
     */
    @Override
	public void resetTask()
    {
        theGiant.setAttackTarget(null);
        thePlayer = null;
    }

    /**
     * Updates the task
     */
    @Override
	public void updateTask()
    {
        theGiant.getLookHelper().setLookPosition(thePlayer.posX, thePlayer.posY + thePlayer.getEyeHeight(), thePlayer.posZ, 10.0F, theGiant.getVerticalFaceSpeed());
    }

}