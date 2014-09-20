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

package com.blogspot.jabelarminecraft.magicbeans.entities;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * @author jabelar
 *
 */
public class EntityCowMagicBeans extends EntityCow implements IEntityMagicBeans
{

	/**
	 * @param parWorld
	 */
	public EntityCowMagicBeans(World parWorld) 
	{
		super(parWorld);
		// DEBUG
		System.out.println("EntityCowMagicBeans constructor");
	}
	
    @Override
	protected Item getDropItem()
    {
        return Items.leather;
    }
    
    @Override
	public void onUpdate()
    {
    	super.onUpdate();
    	if (getLeashed() && !worldObj.isRemote)
    	{
    		// chance mysterious stranger will appear
    		if (rand.nextFloat() < (1.0F / (30 * 20)))
    		{
        		// DEBUG
        		System.out.println("A mysterious stranger appears");
    		}
    	}
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    @Override
	protected void dropFewItems(boolean parRecentlyHit, int parLootingLevel)
    {
        int j = rand.nextInt(3) + rand.nextInt(1 + parLootingLevel);
        int k;

        for (k = 0; k < j; ++k)
        {
            dropItem(Items.leather, 1);
        }

        j = rand.nextInt(3) + 1 + rand.nextInt(1 + parLootingLevel);

        for (k = 0; k < j; ++k)
        {
            if (isBurning())
            {
                dropItem(Items.cooked_beef, 1);
            }
            else
            {
                dropItem(Items.beef, 1);
            }
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @Override
	public boolean interact(EntityPlayer parPlayer)
    {
        ItemStack itemstack = parPlayer.inventory.getCurrentItem();

        if (itemstack != null && itemstack.getItem() == Items.bucket && !parPlayer.capabilities.isCreativeMode)
        {
            if (itemstack.stackSize-- == 1)
            {
                parPlayer.inventory.setInventorySlotContents(parPlayer.inventory.currentItem, new ItemStack(Items.milk_bucket));
            }
            else if (!parPlayer.inventory.addItemStackToInventory(new ItemStack(Items.milk_bucket)))
            {
                parPlayer.dropPlayerItemWithRandomChoice(new ItemStack(Items.milk_bucket, 1, 0), false);
            }

            return true;
        }
        else
        {
            return super.interact(parPlayer);
        }
    }
    
    @Override
	public boolean canDespawn()
    {
    	return false;
    }
    
    @Override
	public int getMaxSpawnedInChunk()
    {
    	return 1;
    }

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#setupAI()
	 */
	@Override
	public void setupAI() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#clearAITasks()
	 */
	@Override
	public void clearAITasks() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#initExtProps()
	 */
	@Override
	public void initExtProps() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#getExtProps()
	 */
	@Override
	public NBTTagCompound getExtProps() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#setExtProps(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void setExtProps(NBTTagCompound parCompound) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#getExtPropsToBuffer(io.netty.buffer.ByteBufOutputStream)
	 */
	@Override
	public void getExtPropsToBuffer(ByteBufOutputStream parBBOS) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#setExtPropsFromBuffer(io.netty.buffer.ByteBufInputStream)
	 */
	@Override
	public void setExtPropsFromBuffer(ByteBufInputStream parBBIS) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#setScaleFactor(float)
	 */
	@Override
	public void setScaleFactor(float parScaleFactor) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#getScaleFactor()
	 */
	@Override
	public float getScaleFactor() {
		// TODO Auto-generated method stub
		return 1.0F;
	}

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#sendEntitySyncPacket()
	 */
	@Override
	public void sendEntitySyncPacket() {
		// TODO Auto-generated method stub
		
	}

}
