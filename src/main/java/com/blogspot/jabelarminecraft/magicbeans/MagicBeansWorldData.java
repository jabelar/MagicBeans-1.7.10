package com.blogspot.jabelarminecraft.magicbeans;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class MagicBeansWorldData extends WorldSavedData 
{

	private static final String IDENTIFIER = MagicBeans.MODID;
	
	private boolean hasCastleSpawned = false;
	
	public MagicBeansWorldData() 
	{
		this(IDENTIFIER);
	}
	
	public MagicBeansWorldData(String parIdentifier) 
	{
		super(parIdentifier);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		hasCastleSpawned = nbt.getBoolean("hasCastleSpawned");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setBoolean("hasCastleSpawned", hasCastleSpawned);
	}
	
	public boolean getHasCastleSpwaned() 
	{
		return hasCastleSpawned;
	}
	
	public void setHasCastleSpawned(boolean parHasCastleSpawned) 
	{
		// DEBUG
		System.out.println("World Data setHasCastleSpawned = "+parHasCastleSpawned);
		if (!hasCastleSpawned) 
		{
			hasCastleSpawned = true;
			markDirty();
			// new PacketWorldData(this).sendToAll(); shouldn't need to send packet as this field is only used on server side
		}
	}
		
	public static MagicBeansWorldData get(World world) 
	{
		MagicBeansWorldData data = (MagicBeansWorldData)world.loadItemData(MagicBeansWorldData.class, IDENTIFIER);
		if (data == null) {
			data = new MagicBeansWorldData();
			world.setItemData(IDENTIFIER, data);
		}
		return data;
	}
}