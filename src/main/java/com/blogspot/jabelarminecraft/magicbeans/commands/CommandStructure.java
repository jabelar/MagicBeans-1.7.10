package com.blogspot.jabelarminecraft.magicbeans.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;

public class CommandStructure implements ICommand
{
	private final List aliases;
	
	// TODO
	// ultimately need to pass structures by name to make more generic
	
	public CommandStructure()
	{
		    aliases = new ArrayList();
		    aliases.add("structure");
		    aliases.add("struct");
	}
	
	@Override
	public int compareTo(Object o) 
	{
		return 0;
	}

	@Override
	public String getCommandName() 
	{
		return "structure";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) 
	{
		return "structure"; // use "structure <text>"; later when passing name of structure
	}

	@Override
	public List getCommandAliases() 
	{
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] argString) 
	{
		World world = sender.getEntityWorld();
		
		if (world.isRemote)
		{
			System.out.println("Not processing on Client side");
		}
		else
		{
			System.out.println("Processing on Server side");

//			if(argString.length == 0)
//		    {
//		    	sender.addChatMessage(new ChatComponentText("Invalid argument"));
//		    	return;
//		    }
			
		    sender.addChatMessage(new ChatComponentText("Generating Structure"));
		    Entity entityPlayer = sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName());
		    int playerDirection = MathHelper.floor_double((entityPlayer.rotationYaw * 4F) / 360f + 0.5D) &3;
		    switch (playerDirection)
		    {
		    case 0:
		    {
			    MagicBeans.structureCastle.generateStructure(world, ForgeDirection.EAST, sender.getPlayerCoordinates().posX, sender.getPlayerCoordinates().posY, sender.getPlayerCoordinates().posZ+2, MagicBeans.structureCastle.getArrayDepth()/2, 0, 0);		    		    	
		    }
			default:
				break;
		    }
		    MagicBeans.structureCastle.generateStructure(world, ForgeDirection.EAST, sender.getPlayerCoordinates().posX, sender.getPlayerCoordinates().posY, sender.getPlayerCoordinates().posZ+2, MagicBeans.structureCastle.getArrayDepth()/2, 0, 0);		    
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender var1) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] var1, int var2) {
		// TODO Auto-generated method stub
		return false;
	}

}
