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

package com.blogspot.jabelarminecraft.magicbeans.entities;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;

public class EntityGoldenGoose extends EntityAnimal implements IEntityMagicBeans
{
    public float rotationFloat1;
    public float destPos;
    public float rotationFloat2;
    public float rotationFloat3;
    public float rotationIncrementFactor = 1.0F;
    /** The time until the next egg is spawned. */
    public int timeUntilNextEgg;

    public EntityGoldenGoose(World par1World)
    {
        super(par1World);
        setSize(0.3F, 0.7F);
        timeUntilNextEgg = rand.nextInt(600) + 600;
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.4D));
        tasks.addTask(2, new EntityAIMate(this, 1.0D));
        tasks.addTask(3, new EntityAITempt(this, 1.0D, Items.wheat_seeds, false));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        tasks.addTask(5, new EntityAIWander(this, 1.0D));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(7, new EntityAILookIdle(this));
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    @Override
	public boolean isAIEnabled()
    {
        return true;
    }

    @Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
	public void onLivingUpdate()
    {
        super.onLivingUpdate();
        rotationFloat3 = rotationFloat1;
        rotationFloat2 = destPos;
        destPos = (float)(destPos + (onGround ? -1 : 4) * 0.3D);

        if (destPos < 0.0F)
        {
            destPos = 0.0F;
        }

        if (destPos > 1.0F)
        {
            destPos = 1.0F;
        }

        if (!onGround && rotationIncrementFactor < 1.0F)
        {
            rotationIncrementFactor = 1.0F;
        }

        rotationIncrementFactor = (float)(rotationIncrementFactor * 0.9D);

        if (!onGround && motionY < 0.0D)
        {
            motionY *= 0.6D;
        }

        rotationFloat1 += rotationIncrementFactor * 2.0F;
        
        // if on server and egg timer is up, lay egg
        if (!worldObj.isRemote)
        {
        	// DEBUG
        	// System.out.println("Time until next egg ="+timeUntilNextEgg);
            if (!isChild() && --timeUntilNextEgg <= 0)
            {

                playSound("mob.chicken.plop", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                dropItem(MagicBeans.itemGoldenEgg, 1);
                // DEBUG
                System.out.println("Laid golden egg");
                timeUntilNextEgg = rand.nextInt(600) + 600;
            }
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    @Override
	protected void fall(float par1) {}

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
	protected String getLivingSound()
    {
        return "mob.chicken.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
	protected String getHurtSound()
    {
        return "mob.chicken.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
	protected String getDeathSound()
    {
        return "mob.chicken.hurt";
    }

    @Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
    {
        playSound("mob.chicken.step", 0.15F, 1.0F);
    }

    @Override
	protected Item getDropItem()
    {
        return Items.feather;
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    @Override
	protected void dropFewItems(boolean par1, int par2)
    {
        int j = rand.nextInt(3) + rand.nextInt(1 + par2);

        for (int k = 0; k < j; ++k)
        {
            dropItem(Items.feather, 1);
        }

        if (isBurning())
        {
            dropItem(Items.cooked_chicken, 1);
        }
        else
        {
            dropItem(Items.chicken, 1);
        }
    }

    @Override
	public EntityChicken createChild(EntityAgeable par1EntityAgeable)
    {
        return new EntityChicken(worldObj);
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    @Override
	public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return par1ItemStack != null && par1ItemStack.getItem() instanceof ItemSeeds;
    }

	@Override
	public void setupAI() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearAITasks() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initExtProps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NBTTagCompound getExtProps() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setExtProps(NBTTagCompound parCompound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getExtPropsToBuffer(ByteBufOutputStream parBBOS) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExtPropsFromBuffer(ByteBufInputStream parBBIS) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setScaleFactor(float parScaleFactor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getScaleFactor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void sendEntitySyncPacket() {
		// TODO Auto-generated method stub
		
	}
}