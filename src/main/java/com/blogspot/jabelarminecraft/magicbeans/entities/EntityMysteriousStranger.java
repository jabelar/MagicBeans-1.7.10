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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.gui.GuiMysteriousStranger;
import com.blogspot.jabelarminecraft.magicbeans.particles.EntityParticleFXMysterious;
import com.blogspot.jabelarminecraft.magicbeans.utilities.MagicBeansUtilities;

/**
 * @author jabelar
 *
 */
public class EntityMysteriousStranger extends EntityCreature implements IEntityMagicBeans, IExtendedEntityProperties
{
    private NBTTagCompound extPropsCompound = new NBTTagCompound();

	/**
	 * @param parWorld
	 */
	public EntityMysteriousStranger(World parWorld) 
	{
		super(parWorld);
		
		// initExtProps();
		setupAI();
	}

	// you don't have to call this as it is called automatically during EntityLiving subclass creation
	@Override
	protected void applyEntityAttributes()
	{
	    super.applyEntityAttributes(); 

	    // standard attributes registered to EntityLivingBase
	   getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
	   getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.0D); // doesnt' move
	   getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.8D);
	   getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);

	    // need to register any additional attributes
//	   getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
//	   getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
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
			Minecraft.getMinecraft().displayGuiScreen(new GuiMysteriousStranger(this));
		}
		return false;
		
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
        System.out.println("EntityMysteriousStranger writeEntityToNBT");
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
	public void readEntityFromNBT(NBTTagCompound parCompound)
    {
        super.readEntityFromNBT(parCompound);
        // DEBUG
        System.out.println("EntityMysteriousStranger readEntityFromNBT");
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
        extPropsCompound.setInteger("cowSummonedById", -1);
        extPropsCompound.setInteger("playerSummonedById", -1);		
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
            parBBOS.writeInt(extPropsCompound.getInteger("cowSummonedById"));
            parBBOS.writeInt(extPropsCompound.getInteger("playerSummonedById"));
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
            extPropsCompound.setInteger("cowSummonedById", parBBIS.readInt());
            extPropsCompound.setInteger("playerSummonedById", parBBIS.readInt());
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
	
	public EntityCowMagicBeans getCowSummonedBy()
	{
		int cowSummonedById = extPropsCompound.getInteger("cowSummonedById");

		// DEBUG
		System.out.println("EntityMysteriousStranger getCowSummonedBy = "+cowSummonedById+", on world.isRemote = "+worldObj.isRemote);
		return (EntityCowMagicBeans) MagicBeansUtilities.getEntityByID(cowSummonedById, worldObj);
	}
	
	public void setCowSummonedBy(EntityCowMagicBeans parCowMagicBeans)
	{
		int cowSummonedById = parCowMagicBeans.getEntityId();
		
		// DEBUG
		System.out.println("EntityMysteriousStranger setCowSummonedBy = "+cowSummonedById+", on world.isRemote = "+worldObj.isRemote);

		extPropsCompound.setInteger("cowSummonedById", cowSummonedById);
	       
        // don't forget to sync client and server
        sendEntitySyncPacket();
	}

	public EntityPlayer getPlayerSummonedBy() 
	{
		int playerSummonedById = extPropsCompound.getInteger("playerSummonedById");

		// DEBUG
		System.out.println("EntityMysteriousStranger getPlayerSummonedBy = "+playerSummonedById+", on world.isRemote = "+worldObj.isRemote);
		return (EntityPlayer) MagicBeansUtilities.getEntityByID(playerSummonedById, worldObj);
	}

	public void setPlayerSummonedBy(EntityPlayer parPlayerSummonedBy) 
	{
		int playerSummonedById = parPlayerSummonedBy.getEntityId();
		
		// DEBUG
		System.out.println("EntityMysteriousStranger setPlayerSummonedBy = "+playerSummonedById+", on world.isRemote = "+worldObj.isRemote);

		extPropsCompound.setInteger("playerSummonedById", playerSummonedById);
	       
        // don't forget to sync client and server
        sendEntitySyncPacket();
	}
	
	@Override
	public void sendEntitySyncPacket()
	{
		MagicBeansUtilities.sendEntitySyncPacket(this);
	}
}
