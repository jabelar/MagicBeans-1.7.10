package com.blogspot.jabelarminecraft.magicbeans.entities;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityGoldenEggThrown extends EntityThrowable
{  

    public EntityGoldenEggThrown(World par1World)
    {
        super(par1World);
    }

    public EntityGoldenEggThrown(World par1World, EntityLivingBase par2EntityLivingBase)
    {
        super(par1World, par2EntityLivingBase);
        // DEBUG
        System.out.println("Constructing on client side ="+worldObj.isRemote);
        System.out.println("Constructor: Thrown egg entity position ="+posX+", "+posY+", "+posZ);
        System.out.println("Constructor: Thrown egg entity motion ="+motionX+", "+motionY+", "+motionZ);
        
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    @Override
    protected void onImpact(MovingObjectPosition par1MovingObjectPosition)
    {
        if (par1MovingObjectPosition.entityHit != null)
        {
            par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 0.0F);
        }

        if (!worldObj.isRemote) // never spawn entity on client side
        {
    		// need to put in randomness here
    	    // should sometimes give gold ingot
            // should sometimes (very rarely) give baby golden goose
        	int dropItem = MathHelper.getRandomIntegerInRange(rand, 0, 8);
        	if (dropItem == 0) // 1in8 chance of spawning entity, like chicken egg
        	{
                EntityAnimal entityToSpawn = (EntityAnimal) EntityList.createEntityByName("magicbeans.Golden Goose", worldObj);
                // entityToSpawn.setGrowingAge(-24000);
                entityToSpawn.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                worldObj.spawnEntityInWorld(entityToSpawn);
        	}
        	else
        	{
                dropItem(Items.gold_nugget, 1);
                // DEBUG
                System.out.println("Good as gold");
        	}
        }

        for (int j = 0; j < 8; ++j)
        {
            worldObj.spawnParticle("snowballpoof", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        }

        if (!worldObj.isRemote)
        {
            setDead();
        }
    }
}