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
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.blogspot.jabelarminecraft.magicbeans.gui.GuiMysteriousStranger;
import com.blogspot.jabelarminecraft.magicbeans.particles.EntityParticleFXMysterious;

/**
 * @author jabelar
 *
 */
public class EntityMysteriousStranger extends EntityCreature implements IEntityMagicBeans
{

	/**
	 * @param parWorld
	 */
	public EntityMysteriousStranger(World parWorld) 
	{
		super(parWorld);
		// TODO Auto-generated constructor stub
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
//		if (!worldObj.isRemote && ticksExisted%40==0)
//		{
//			for (int var3 = 0; var3 < 7; ++var3)
//			{
//			double var4 = rand.nextGaussian() * 0.02D;
//			double var6 = rand.nextGaussian() * 0.02D;
//			double var8 = rand.nextGaussian() * 0.02D;
//			worldObj.spawnParticle("happyVillager", posX + rand.nextFloat() * width * 2.0F - width, posY + 0.5D + rand.nextFloat() * height, posZ + rand.nextFloat() * width * 2.0F - width, var4, var6, var8);
//			}
//		}
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
		if (parPlayer.worldObj.isRemote)
		{
			Minecraft.getMinecraft().displayGuiScreen(new GuiMysteriousStranger());
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
