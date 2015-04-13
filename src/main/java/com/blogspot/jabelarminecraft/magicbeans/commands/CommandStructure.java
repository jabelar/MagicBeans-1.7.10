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

package com.blogspot.jabelarminecraft.magicbeans.commands;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.structures.Structure;

public class CommandStructure implements ICommand
{
    private final List aliases;
    World theWorld;
    Entity thePlayer;
    
    int dimX;
    int dimY;
    int dimZ;

    String[][][] blockNameArray = null;
    int[][][] blockMetaArray = null;

    BufferedReader readIn;
    
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
        return "structure <int> <int> <int>"; // use the ints if offset required
    }

    @Override
    public List getCommandAliases() 
    {
        return this.aliases;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] argString) 
    {
        theWorld = sender.getEntityWorld();
        thePlayer = sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName());

        
        if (theWorld.isRemote)
        {
            System.out.println("Not processing on Client side");
        }
        else
        {
            System.out.println("Processing on Server side");

            if(argString.length==0)
            {
                Structure theStructure = MagicBeans.structureCastle;
                theStructure.generate(thePlayer, 0, -2, 0);
            }
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
