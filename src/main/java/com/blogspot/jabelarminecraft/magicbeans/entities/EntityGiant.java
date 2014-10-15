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
import net.minecraft.command.IEntitySelector;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.ai.EntityGiantAINearestAttackableTarget;
import com.blogspot.jabelarminecraft.magicbeans.ai.EntityGiantAISeePlayer;
import com.blogspot.jabelarminecraft.magicbeans.particles.EntityParticleFXMysterious;
import com.blogspot.jabelarminecraft.magicbeans.utilities.MagicBeansUtilities;

/**
 * @author jabelar
 *
 */
public class EntityGiant extends EntityCreature implements IEntityMagicBeans, IExtendedEntityProperties
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
    protected EntityAIBase aiNearestAttackableTarget = new EntityGiantAINearestAttackableTarget(this, EntityPlayer.class, 2, true, false, (IEntitySelector)null);
    protected EntityAIBase aiSeePlayer = new EntityGiantAISeePlayer(this, 16.0D);

    // fields related to being attacked
    protected Entity entityAttackedBy = null;
    protected boolean wasDamageDoneOutsideResistancePeriod = true;
    protected DamageSource damageSource = null;
    protected float damageAmount = 0;
    
    // fields related to attacking
    protected Entity entityAttacked = null;
    protected float attackDamage = 1.0F;
    protected int knockback = 0;
	protected boolean wasDamageDone = false;
    
	/**
	 * @param parWorld
	 */
	public EntityGiant(World parWorld) 
	{
		super(parWorld);
		
		initExtProps();
		setupAI();
		setSize(1.0F, 4.5F);
	}

	// you don't have to call this as it is called automatically during EntityLiving subclass creation
	@Override
	protected void applyEntityAttributes()
	{
	    super.applyEntityAttributes(); 

	    // standard attributes registered to EntityLivingBase
	   getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
	   getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D); 
	   getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0D); // can't knock back
	   getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);

	    // need to register any additional attributes
	   getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
	   getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
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
		collideWithNearbyEntities();;
		if (parPlayer.worldObj.isRemote)
		{
			// Minecraft.getMinecraft().displayGuiScreen(new GuiMysteriousStranger(this));
		}
		return false;
	}
	
    /**
     * Returns true if the newer Entity AI code should be run
     */
    @Override
	public boolean isAIEnabled()
    {
        return true;
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
        // targetTasks.addTask(1, aiNearestAttackableTarget);
        targetTasks.addTask(1, aiSeePlayer);
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
	
    @Override
	public boolean attackEntityAsMob(Entity parEntity)
    {
    	//DEBUG
    	// System.out.println("EntityGiant attackEntityAsMob");
    	
    	entityAttacked = parEntity;
        attackDamage = (float)getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        knockback = 0;

        if (entityAttacked instanceof EntityLivingBase)
        {
            attackDamage += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase)entityAttacked);
            knockback += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase)entityAttacked);
        }

        wasDamageDone = entityAttacked.attackEntityFrom(DamageSource.causeMobDamage(this), attackDamage);
        // DEBUG
        // System.out.println("Damage was done ="+wasDamageDone+", damage amount ="+attackDamage);
        if (wasDamageDone)
        {
            if (knockback > 0)
            {
            	entityAttacked.addVelocity(-MathHelper.sin(rotationYaw * (float)Math.PI / 180.0F) * knockback * 0.5F, 0.1D, MathHelper.cos(rotationYaw * (float)Math.PI / 180.0F) * knockback * 0.5F);
                motionX *= 0.6D;
                motionZ *= 0.6D;
            }

            int fireModifier = EnchantmentHelper.getFireAspectModifier(this);

            if (fireModifier > 0)
            {
            	entityAttacked.setFire(fireModifier * 4);
            }

            if (entityAttacked instanceof EntityLivingBase)
            {
                EnchantmentHelper.func_151384_a((EntityLivingBase)entityAttacked, this);
            }

            EnchantmentHelper.func_151385_b(this, entityAttacked);
        }

        return wasDamageDone;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
	public boolean attackEntityFrom(DamageSource parDamageSource, float parDamageAmount)
    {
    	damageSource = parDamageSource;
    	damageAmount = parDamageAmount;
        entityAttackedBy = damageSource.getEntity();

        // DEBUG
    	// System.out.println("EntityGiant attackEntityFrom()");
        if (ForgeHooks.onLivingAttack(this, damageSource, damageAmount)) return false;

        if (conditionsPreventDamage(damageSource))
        {
        	return false;
        }

        wasDamageDoneOutsideResistancePeriod = processDamage(damageSource, damageAmount);
               
        updateEntityState();
                
        // process death
        if (getHealth() <= 0.0F)
        {
        	onDeath(damageSource);
        }
        
        playHurtOrDeathSound();

		return wasDamageDoneOutsideResistancePeriod;
    }

	private void updateEntityState() 
	{
        // flail limbs when attacked
        limbSwingAmount = 1.5F;

        entityAge = 0; // reset despawn counter if you've been attacked by something

        if (entityAttackedBy != null)
        {
        	// DEBUG
        	// System.out.println("Attacked by an entity");
            if (entityAttackedBy instanceof EntityLivingBase)
            {
                setRevengeTarget((EntityLivingBase)entityAttackedBy);
            }

            if (entityAttackedBy instanceof EntityPlayer)
            {
            	// DEBUG
            	// System.out.println("Attacked by a player");
                recentlyHit = 100;
                attackingPlayer = (EntityPlayer)entityAttackedBy;
            }
            else if (entityAttackedBy instanceof EntityTameable)
            {
                EntityTameable entityTameable = (EntityTameable)entityAttackedBy;

                if (entityTameable.isTamed())
                {
                    recentlyHit = 100;
                    attackingPlayer = null;
                }
            }
        }

        if (wasDamageDoneOutsideResistancePeriod)
        {
            worldObj.setEntityState(this, (byte)2);

            if (damageSource != DamageSource.drown)
            {
                setBeenAttacked();
            }

            if (entityAttackedBy != null)
            {
                double d1 = entityAttackedBy.posX - posX;
                double d0;

                for (d0 = entityAttackedBy.posZ - posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
                {
                    d1 = (Math.random() - Math.random()) * 0.01D;
                }

                attackedAtYaw = (float)(Math.atan2(d0, d1) * 180.0D / Math.PI) - rotationYaw;
                knockBack(entityAttackedBy, damageAmount, d1, d0);
            }
            else // non-entity was damage source
            {
            	// DEBUG
            	// System.out.println("Damage was done by something other than an entity");
                attackedAtYaw = (int)(Math.random() * 2.0D) * 180;
            }
        }
        
        if (riddenByEntity != entityAttackedBy && ridingEntity != entityAttackedBy)
          {
              if (entityAttackedBy != this)
              {
                  entityToAttack = entityAttackedBy;
              }
          }
	}

	/**
	 * @param wasDamageDoneOutsideResistancePeriod
	 */
	private void playHurtOrDeathSound() 
	{
        String soundName;

        if (getHealth() <= 0.0F)
        {
            soundName = getDeathSound();
        }
        else
        {
        	soundName = getHurtSound();
        }

        if (wasDamageDoneOutsideResistancePeriod && soundName != null)
        {
            playSound(soundName, getSoundVolume(), getSoundPitch());
        }
	}

	/**
	 * @param parDamageSource
	 * @param parDamageAmount
	 * @return
	 */
	private boolean processDamage(DamageSource parDamageSource,
			float parDamageAmount) 
	{       
        // Check if helmet protects from anvil or falling block, and damage helmet
        if ((damageSource == DamageSource.anvil || damageSource == DamageSource.fallingBlock) && getEquipmentInSlot(4) != null)
        {
            getEquipmentInSlot(4).damageItem((int)(damageAmount * 4.0F + rand.nextFloat() * damageAmount * 2.0F), this);
            damageAmount *= 0.75F;
        }

        wasDamageDoneOutsideResistancePeriod = true;

        // Check for hurt resistance
        if (hurtResistantTime > maxHurtResistantTime / 2.0F)
        {
        	// DEBUG
        	// System.out.println("Reduced damage done, damage amount ="+damageAmount+". health remaining ="+getHealth());
            if (damageAmount <= lastDamage)
            {
                return false;
            }

            damageEntity(damageSource, damageAmount - lastDamage);
            lastDamage = damageAmount;
            wasDamageDoneOutsideResistancePeriod = false;
        }
        else // do normal damage
        {
            lastDamage = damageAmount;
            prevHealth = getHealth();
            hurtResistantTime = maxHurtResistantTime;
            damageEntity(damageSource, damageAmount);
            hurtTime = maxHurtTime = 10;
        	// DEBUG
        	// System.out.println("Normal damage done, damage amount ="+damageAmount+". health remaining ="+getHealth());
        }
        
		return wasDamageDoneOutsideResistancePeriod;
	}

	/**
	 * @param parDamageSource
	 * @return
	 */
	private boolean conditionsPreventDamage(DamageSource parDamageSource) 
	{
        // Only process damage on server side
        if (worldObj.isRemote)
        {
            return true;
        }

        // Check for invlunerability
        if (isEntityInvulnerable())
        {
            return true;
        }

        // Check if already dead
        if (getHealth() <= 0.0F)
        {
            return true;
        }

        // Check if damage is from fire but resistant
        if (damageSource.isFireDamage() && isPotionActive(Potion.fireResistance))
        {
            return true;
        }
        
		return false;
	}
    
    @Override
	public boolean allowLeashing()
    {
    	return false;
    }


    @Override
	public float getEyeHeight()
    {
        return height * 0.85F * getScaleFactor();
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
	public void writeEntityToNBT(NBTTagCompound parCompound)
    {
        super.writeEntityToNBT(parCompound);
        // DEBUG
        System.out.println("EntityGiant writeEntityToNBT");
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
	public void readEntityFromNBT(NBTTagCompound parCompound)
    {
        super.readEntityFromNBT(parCompound);
        // DEBUG
        System.out.println("EntityGiant readEntityFromNBT");
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
        try 
        {
            parBBOS.writeFloat(extPropsCompound.getFloat("scaleFactor"));
        } 
        catch (IOException e) 
        { 
        	e.printStackTrace(); 
        }        
    }

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#setExtPropsFromBuffer(io.netty.buffer.ByteBufInputStream)
	 */
    @Override
    // no need to return anything because the extended properties tag is updated directly
    public void setExtPropsFromBuffer(ByteBufInputStream parBBIS) 
    {
        try 
        {
            extPropsCompound.setFloat("scaleFactor", parBBIS.readFloat());
        } 
        catch (IOException e) 
        { 
        	e.printStackTrace(); 
        }
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
