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

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.utilities.MagicBeansUtilities;

/**
 * @author jabelar
 *
 */
public class EntityCowMagicBeans extends EntityCow implements IEntityMagicBeans, IExtendedEntityProperties
{
    public NBTTagCompound extPropsCompound = new NBTTagCompound();

	/**
	 * @param parWorld
	 */
	public EntityCowMagicBeans(World parWorld) 
	{
		super(parWorld);
		// DEBUG
		System.out.println("EntityCowMagicBeans constructor");
		
		// initExtProps();
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
        		Entity entityLeashedTo = getLeashedToEntity();
        		if (entityLeashedTo instanceof EntityPlayer)
        		{
        			EntityPlayer playerLeashedTo = (EntityPlayer) entityLeashedTo;
        			Vec3 playerLookVector = playerLeashedTo.getLookVec();
        			playerLeashedTo.addChatMessage(new ChatComponentText(MagicBeansUtilities.stringToRainbow("A mysterious stranger appears!")));
		            String entityToSpawnNameFull = MagicBeans.MODID+".Mysterious Stranger";
		            if (EntityList.stringToClassMapping.containsKey(entityToSpawnNameFull))
		            {
		                EntityLiving entityToSpawn = (EntityLiving) EntityList
		                      .createEntityByName(entityToSpawnNameFull, worldObj);
		                double spawnX = playerLeashedTo.posX+5*playerLookVector.xCoord;
		                double spawnZ = playerLeashedTo.posZ+5*playerLookVector.zCoord;
		                double spawnY = worldObj.getHeightValue((int)spawnX, (int)spawnZ);
		                entityToSpawn.setLocationAndAngles(spawnX, spawnY, spawnZ, 
		                      MathHelper.wrapAngleTo180_float(rand.nextFloat()
		                      * 360.0F), 0.0F);
		                worldObj.spawnEntityInWorld(entityToSpawn);
		                entityToSpawn.playLivingSound();
		                ((EntityMysteriousStranger)entityToSpawn).setCowSummonedBy(this);
		                ((EntityMysteriousStranger)entityToSpawn).setPlayerSummonedBy(playerLeashedTo);
		            }
		            else
		            {
		                //DEBUG
		                System.out.println("Entity not found "+entityToSpawnNameFull);
		            }
        		}
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

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
	public void writeEntityToNBT(NBTTagCompound parCompound)
    {
        super.writeEntityToNBT(parCompound);
        // DEBUG
        System.out.println("EntityCowMagicBeans writeEntityToNBT");
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
	public void readEntityFromNBT(NBTTagCompound parCompound)
    {
        super.readEntityFromNBT(parCompound);
        // DEBUG
        System.out.println("EntityCowMagicBeans readEntityFromNBT");
    }
    
    /*
     * (non-Javadoc)
     * @see net.minecraftforge.common.IExtendedEntityProperties#saveNBTData(net.minecraft.nbt.NBTTagCompound)
     */
   	@Override
	public void saveNBTData(NBTTagCompound parCompound) 
	{
		// DEBUG
		System.out.println("Extended properties saveNBTData(), Entity = "+getEntityId()+", client side = "+worldObj.isRemote);
		
		// good idea to keep your extended properties in a sub-compound to avoid conflicts with other
		// possible extended properties, even from other mods (like if a mod extends all EntityAnimal)
		parCompound.setTag(MagicBeans.EXT_PROPS_NAME, getExtProps()); // set as a sub-compound
	}

   	/*
   	 * @Override(non-Javadoc)
   	 * @see net.minecraftforge.common.IExtendedEntityProperties#loadNBTData(net.minecraft.nbt.NBTTagCompound)
   	 */
	@Override
	public void loadNBTData(NBTTagCompound parCompound) 
	{
		// DEBUG
		System.out.println("Extended properties loadNBTData(), Entity = "+getEntityId()+", client side = "+worldObj.isRemote);

		// Get the sub-compound
		setExtProps((NBTTagCompound) parCompound.getTag(MagicBeans.EXT_PROPS_NAME));
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.common.IExtendedEntityProperties#init(net.minecraft.entity.Entity, net.minecraft.world.World)
	 */
	@Override
	public void init(Entity entity, World world) 
	{
		// DEBUG
		System.out.println("Extended properties init(), Entity = "+getEntityId()+", client side = "+worldObj.isRemote);
	}

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#initExtProps()
	 */
	@Override
	public void initExtProps() 
	{
        extPropsCompound.setFloat("scaleFactor", 1.0F);
	}

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#getExtProps()
	 */
    @Override
    public NBTTagCompound getExtProps()
    {
        return extPropsCompound;
    }

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#setExtProps(net.minecraft.nbt.NBTTagCompound)
	 */
    @Override
    public void setExtProps(NBTTagCompound parCompound) 
    {
        extPropsCompound = parCompound;
        
        // probably need to be careful sync'ing here as this is called by
        // sync process itself -- don't create infinite loop
    }

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#getExtPropsToBuffer(io.netty.buffer.ByteBufOutputStream)
	 */
    @Override
    // no need to return the buffer because the buffer is operated on directly
    public void getExtPropsToBuffer(ByteBufOutputStream parBBOS) 
    {
        try {
            parBBOS.writeFloat(extPropsCompound.getFloat("scaleFactor"));
        } catch (IOException e) { e.printStackTrace(); }        
    }

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#setExtPropsFromBuffer(io.netty.buffer.ByteBufInputStream)
	 */
    @Override
    // no need to return anything because the extended properties tag is updated directly
    public void setExtPropsFromBuffer(ByteBufInputStream parBBIS) 
    {
        try {
            extPropsCompound.setFloat("scaleFactor", parBBIS.readFloat());
        } catch (IOException e) { e.printStackTrace(); }
    }

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#setScaleFactor(float)
	 */
    @Override
    public void setScaleFactor(float parScaleFactor)
    {
        extPropsCompound.setFloat("scaleFactor", Math.abs(parScaleFactor));
       
        // don't forget to sync client and server
        sendEntitySyncPacket();
    }

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#getScaleFactor()
	 */
    @Override
    public float getScaleFactor()
    {
        return extPropsCompound.getFloat("scaleFactor");
    }

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#sendEntitySyncPacket()
	 */
	@Override
	public void sendEntitySyncPacket()
	{
		MagicBeansUtilities.sendEntitySyncPacket(this);
	}

}
