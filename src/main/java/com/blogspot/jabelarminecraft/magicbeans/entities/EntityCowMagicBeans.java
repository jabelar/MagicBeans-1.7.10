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

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.MagicBeansWorldData;
import com.blogspot.jabelarminecraft.magicbeans.gui.GuiFamilyCow;
import com.blogspot.jabelarminecraft.magicbeans.utilities.MagicBeansUtilities;

/**
 * @author jabelar
 *
 */
public class EntityCowMagicBeans extends EntityCow implements IEntityMagicBeans
{
    public NBTTagCompound syncDataCompound = new NBTTagCompound();
    
	/**
	 * @param parWorld
	 */
	public EntityCowMagicBeans(World parWorld) 
	{
		super(parWorld);
		// DEBUG
		System.out.println("EntityCowMagicBeans constructor");
		
		initSyncDataCompound();
	}
	
    
    @Override
	public void onUpdate()
    {
    	super.onUpdate();
    	if (getLeashed() && !worldObj.isRemote && !getHasSpawnedMysteriousStranger())
    	{
    		// chance mysterious stranger will appear
    		if (!getHasSpawnedMysteriousStranger() && (rand.nextFloat() < (1.0F / (30 * 20))))
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
		                
		                // check to ensure there is open area for stranger to spawn, not underground
		                if (worldObj.canBlockSeeTheSky(MathHelper.floor_double(spawnX), MathHelper.floor_double(spawnY), MathHelper.floor_double(spawnZ)))
		                {
		                	entityToSpawn.setLocationAndAngles(spawnX, spawnY, spawnZ, 
			                      MathHelper.wrapAngleTo180_float(rand.nextFloat()
			                      * 360.0F), 0.0F);
			                worldObj.spawnEntityInWorld(entityToSpawn);
			                entityToSpawn.playLivingSound();
			                ((EntityMysteriousStranger)entityToSpawn).setCowSummonedBy(this);
			                ((EntityMysteriousStranger)entityToSpawn).setPlayerSummonedBy(playerLeashedTo);
			                setHasSpawnedMysteriousStranger(true);
		                }
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

    @Override
	protected Item getDropItem()
    {
        return Items.leather;
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

        dropItem(getDropItem(), 1);
 
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
    	// check if have already spawned castle
    	if (!MagicBeansWorldData.get(worldObj).getHasCastleSpwaned())
    	{
	    	// Family cow doesn't provide milk (that's why your mother wants you to sell it)
	    	// don't open gui if holding items, e.g. wheat that should incite mating instead
	    	// also don't open gui if already gone through gui to get a lead
	    	if (parPlayer.getCurrentEquippedItem() == null || parPlayer.getCurrentEquippedItem().getItem() == Items.bucket)
	    	{
				collideWithNearbyEntities();
				if (parPlayer.worldObj.isRemote)
				{
					Minecraft.getMinecraft().displayGuiScreen(new GuiFamilyCow());
				}
	    	}
	    	else // act like normal cow
	    	{
	    		super.interact(parPlayer);
	    	}
    	}
    	else // act like normal cow
    	{
    		super.interact(parPlayer);
    	}
		return false;
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

    @Override
    public void readEntityFromNBT(NBTTagCompound parCompound)
    {
    	super.readEntityFromNBT(parCompound);
    	syncDataCompound = (NBTTagCompound) parCompound.getTag("syncDataCompound");
        // DEBUG
        // System.out.println("EntityCowMagicBeans readEntityFromNBT");
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound parCompound)
    {
    	super.writeEntityToNBT(parCompound);
    	parCompound.setTag("syncDataCompound", syncDataCompound);
        // DEBUG
        // System.out.println("EntityCowMagicBeans writeEntityToNBT");
    }
    
	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#initExtProps()
	 */
	@Override
	public void initSyncDataCompound() 
	{
		// don't use setters because it might be too early to send sync packet
        syncDataCompound.setFloat("scaleFactor", 1.0F);
        syncDataCompound.setBoolean("hasSpawnedMysteriousStranger", false);
	}

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#getExtProps()
	 */
    @Override
    public NBTTagCompound getSyncDataCompound()
    {
        return syncDataCompound;
    }

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#setExtProps(net.minecraft.nbt.NBTTagCompound)
	 */
    @Override
    public void setSyncDataCompound(NBTTagCompound parCompound) 
    {
        syncDataCompound = parCompound;
        
        // probably need to be careful sync'ing here as this is called by
        // sync process itself -- don't create infinite loop
    }

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#setScaleFactor(float)
	 */
    @Override
    public void setScaleFactor(float parScaleFactor)
    {
        syncDataCompound.setFloat("scaleFactor", Math.abs(parScaleFactor));
       
        // don't forget to sync client and server
        sendEntitySyncPacket();
    }

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#getScaleFactor()
	 */
    @Override
    public float getScaleFactor()
    {
        return syncDataCompound.getFloat("scaleFactor");
    }

    public boolean getHasSpawnedMysteriousStranger()
    {
        return syncDataCompound.getBoolean("hasSpawnedMysteriousStranger");
    }
    
    public void setHasSpawnedMysteriousStranger(boolean parHasSpawnedMysteriousStranger)
    {
        syncDataCompound.setBoolean("hasSpawnedMysteriousStranger", parHasSpawnedMysteriousStranger);
       
        // don't forget to sync client and server
        sendEntitySyncPacket();
    }

	/* (non-Javadoc)
	 * @see com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans#sendEntitySyncPacket()
	 */
	@Override
	public void sendEntitySyncPacket()
	{
		MagicBeansUtilities.sendEntitySyncPacketToClient(this);
	}

}
