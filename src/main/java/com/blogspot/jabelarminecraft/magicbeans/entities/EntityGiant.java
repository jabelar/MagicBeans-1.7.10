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

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.blogspot.jabelarminecraft.magicbeans.particles.EntityParticleFXMysterious;
import com.blogspot.jabelarminecraft.magicbeans.utilities.MagicBeansUtilities;

/**
 * @author jabelar
 *
 */
public class EntityGiant extends EntityCreature implements IEntityMagicBeans
{
    private NBTTagCompound extPropsCompound = new NBTTagCompound();

    // good to have instances of AI so task list can be modified, including in sub-classes
    protected EntityAIBase aiSwimming = new EntityAISwimming(this);
    protected EntityAIBase aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, true);
    protected EntityAIBase aiMoveTowardsRestriction = new EntityAIMoveTowardsRestriction(this, 1.0D);
    protected EntityAIBase aiMoveThroughVillage = new EntityAIMoveThroughVillage(this, 1.0D, false);
    protected EntityAIBase aiWander = new EntityAIWander(this, 1.0D);
    protected EntityAIBase aiWatchClosest = new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F);
    protected EntityAIBase aiLookIdle = new EntityAILookIdle(this);
    protected EntityAIBase aiHurtByTarget = new EntityAIHurtByTarget(this, true);
    protected EntityAIBase aiNearestAttackableTarget = new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true);

	/**
	 * @param parWorld
	 */
	public EntityGiant(World parWorld) 
	{
		super(parWorld);
		
		initExtProps();
		setupAI();
		setSize(1.5F, 4.5F);
	}

	// you don't have to call this as it is called automatically during EntityLiving subclass creation
	@Override
	protected void applyEntityAttributes()
	{
	    super.applyEntityAttributes(); 

	    // standard attributes registered to EntityLivingBase
	   getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
	   getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(1.0D); 
	   getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0D); // can't knock back
	   getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);

	    // need to register any additional attributes
	   getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
	   getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8.0D);
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (!worldObj.isRemote && rand.nextFloat()<0.1F)
		{
			double var4 = rand.nextGaussian() * 0.02D;
			double var6 = rand.nextGaussian() * 0.02D;
			double var8 = rand.nextGaussian() * 0.02D;
			EntityFX particleMysterious = new EntityParticleFXMysterious(worldObj, posX + rand.nextFloat() * width * 2.0F - width, posY + 0.5D + rand.nextFloat() * height, posZ + rand.nextFloat() * width * 2.0F - width, var4, var6, var8);
			Minecraft.getMinecraft().effectRenderer.addEffect(particleMysterious);
		}
	}
	
	@Override
	public boolean interact(EntityPlayer parPlayer)
	{
		this.collideWithNearbyEntities();;
		if (parPlayer.worldObj.isRemote)
		{
			// Minecraft.getMinecraft().displayGuiScreen(new GuiMysteriousStranger(this));
		}
		return false;
		
	}

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#setupAI()
	 */
	@Override
	public void setupAI() 
	{
        getNavigator().setBreakDoors(true);
        clearAITasks();
        tasks.addTask(0, aiSwimming);
        tasks.addTask(1, aiAttackOnCollide);
        tasks.addTask(2, aiMoveTowardsRestriction);
        tasks.addTask(3, aiMoveThroughVillage);
        tasks.addTask(4, aiWander);
        tasks.addTask(5, aiWatchClosest);
        tasks.addTask(6, aiLookIdle);
        targetTasks.addTask(0, aiHurtByTarget);
        targetTasks.addTask(1, aiNearestAttackableTarget);
	}

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#clearAITasks()
	 */
	@Override
	public void clearAITasks() 
	{
        tasks.taskEntries.clear();
        targetTasks.taskEntries.clear();
	}

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#initExtProps()
	 */
	@Override
	public void initExtProps() 
	{
        extPropsCompound.setFloat("scaleFactor", 2.25F);
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
	
	@Override
	public void sendEntitySyncPacket()
	{
		MagicBeansUtilities.sendEntitySyncPacket(this);
	}
}
