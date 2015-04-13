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

package com.blogspot.jabelarminecraft.magicbeans.structures;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.ModWorldData;
import com.blogspot.jabelarminecraft.magicbeans.utilities.Utilities;

public class Structure implements IStructure
{
    protected String theStructureName;
    
    protected World theWorld;
    protected Entity theEntity;
    protected TileEntity theTileEntity;
    
    protected int dimX;
    protected int dimY;
    protected int dimZ;
    protected int totalVolume;
    
    protected int startX;
    protected int startY;
    protected int startZ;
    
    protected int cloudMarginX = 15;
    protected int cloudMarginZ = 15;
    
    public boolean shouldGenerate = false;
    public boolean finishedGeneratingCloud = false; // cloud generation, this is unique to this mod
    public boolean finishedGeneratingBasic = false; // basic block generation
    public boolean finishedGeneratingMeta = false; // blocks with metadata generation
    public boolean finishedGeneratingSpecial = false; // special blocks like tripwire
    public boolean finishedPopulatingItems = false; // items into inventories and such
    public boolean finishedPopulatingEntities = false; // default entities that inhabit structure
    protected int ticksGenerating = 0;

    String[][][] blockNameArray = null;
    int[][][] blockMetaArray = null;

    BufferedReader readIn;
    
    StructureSparseArrayElement[] theSparseArrayBasic = new StructureSparseArrayElement[64 * 64 * 64];
    StructureSparseArrayElement[] theSparseArrayMeta = new StructureSparseArrayElement[64 * 64 * 64];
    StructureSparseArrayElement[] theSparseArraySpecial = new StructureSparseArrayElement[64 * 64 * 64];
    int numSparseElementsBasic = 0;
    int numSparseElementsMeta = 0;
    int numSparseElementsSpecial = 0;

    public Structure(String parName)
    {
        theStructureName = parName;
        // Remember to put following in the init handling of common proxy *after* the blocks are registered
        //  readArrays(theStructureName);
        //  makeSparseArray();
    }
    
    @Override
    public String getName()
    {
        return theStructureName;
    }
    
    @Override
    public int getDimX()
    {
        return dimX;
    }
                
    @Override
    public int getDimY()
    {
        return dimY;
    }
                
    @Override
    public int getDimZ()
    {
        return dimZ;
    }
                
    @Override
    public String[][][] getBlockNameArray()
    {
        return blockNameArray;
    }
    
    @Override
    public int[][][] getBlockMetaArray()
    {
        return blockMetaArray;
    }
                
    @Override
    public void readArrays(String parName)
    {
        try 
        {
            System.out.println("Reading file = "+parName+".txt");
            readIn = new BufferedReader(new InputStreamReader(getClass().getClassLoader()
                    .getResourceAsStream("assets/magicbeans/structures/"+parName+".txt"), "UTF-8"));
            dimX = Integer.valueOf(readIn.readLine());
            dimY = Integer.valueOf(readIn.readLine());
            dimZ = Integer.valueOf(readIn.readLine());
            blockNameArray = new String[dimX][dimY][dimZ];
            blockMetaArray = new int[dimX][dimY][dimZ];
            System.out.println("Dimensions of structure = "+dimX+", "+dimY+", "+dimZ);
            for (int indY = 0; indY < dimY; indY++) // Y first to organize in vertical layers
            {
                for (int indX = 0; indX < dimX; indX++)
                {
                    for (int indZ = 0; indZ < dimZ; indZ++)
                    {
                        blockNameArray[indX][indY][indZ] = readIn.readLine();
                        blockMetaArray[indX][indY][indZ] = Integer.valueOf(readIn.readLine());
                        // DEBUG
                        if (indX + indY + indZ == 0) System.out.println("Block name for index 0, 0, 0 is "+blockNameArray[indX][indY][indZ]);
                    }
                }
            }
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        try 
        {
            readIn.close();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void makeSparseArrays()
    {
        // DEBUG
        System.out.println("Starting to make sparse array for basic blocks");
        for (int indY=0; indY < dimY; indY++)
        {
            for (int indX=0; indX < dimX; indX++)
            {
                for (int indZ=0; indZ < dimZ; indZ++) 
                {
                    if (!blockNameArray[indX][indY][indZ].equals("minecraft:air"))
                    {
                        if (blockMetaArray[indX][indY][indZ] == 0 
                                && !blockNameArray[indX]indY][indZ].equals("minecraft:tripwire"))
                        {
                            theSparseArrayBasic[numSparseElementsBasic] = 
                                    new StructureSparseArrayElement(
                                          Block.getBlockFromName(blockNameArray[indX][indY][indZ]),
                                          0,
                                          indX,
                                          indY,
                                          indZ
                                          );
                            numSparseElementsBasic++;
                        }
                        else if (blockMetaArray[indX][indY][indZ] > 0)
                        {
                            theSparseArrayMeta[numSparseElementsMeta] = 
                                    new StructureSparseArrayElement(
                                          Block.getBlockFromName(blockNameArray[indX][indY][indZ]),
                                          blockMetaArray[indX][indY][indZ],
                                          indX,
                                          indY,
                                          indZ
                                          );
                            numSparseElementsMeta++;
                        }
                        else // must be trip wire
                        {
                            theSparseArraySpecial[numSparseElementsSpecial] = 
                                    new StructureSparseArrayElement(
                                          Block.getBlockFromName(blockNameArray[indX][indY][indZ]),
                                          blockMetaArray[indX][indY][indZ],
                                          indX,
                                          indY,
                                          indZ
                                          );
                            numSparseElementsSpecial++;
                        }
                    }
                }
            }
        }
        // DEBUG
        System.out.println("Finished making sparse array for basic blocks, with number of elements = "+numSparseElementsBasic);
        System.out.println("Finished making sparse array for meta blocks, with number of elements = "+numSparseElementsMeta);
        System.out.println("Finished making sparse array for special blocks, with number of elements = "+numSparseElementsSpecial);
    }
    @Override
    public void generateSparse(TileEntity parEntity, int parOffsetX, int parOffsetY, int parOffsetZ) 
    {
        // exit if generating not started
        if (!shouldGenerate)
        {
            return;
        }
        
        theTileEntity = parEntity;
        theWorld = theTileEntity.getWorldObj();

        if (theWorld.isRemote)
        {
            return;
        }

        // exit if finished
        if (ModWorldData.get(theWorld).getHasCastleSpawned())
        {
            // DEBUG
            System.out.println("Castle has already spawned");
            return;
        }

        // DEBUG
        System.out.println("Starting to generate with sparse array");
        
        startX = theTileEntity.xCoord-9; // +parOffsetX;
        startY = theTileEntity.yCoord-3; // +parOffsetY;
        startZ = theTileEntity.zCoord-12; // +parOffsetZ;

        totalVolume = dimX * dimY * dimZ;
        
        if (!finishedGeneratingBasic)
        {
            for (int index = 0; index < numSparseElementsBasic; index++)
            {
                Block theBlock = theSparseArrayMeta[index].theBlock;
                theWorld.setBlock(
                        startX+theSparseArrayBasic[index].posX, 
                        startY+theSparseArrayBasic[index].posY, 
                        startZ+theSparseArrayBasic[index].posZ, 
                        theBlock, 
                        0, 
                        2
                        );
                if (theBlock.hasTileEntity(0))
                {
                    customizeTileEntity(
                            theBlock, 
                            0, 
                            startX+theSparseArrayMeta[index].posX, 
                            startY+theSparseArrayMeta[index].posY, 
                            startZ+theSparseArrayMeta[index].posZ
                            );
                }
            }
            finishedGeneratingBasic = true;
        }
        else if (!finishedGeneratingMeta)
        {
            for (int index = 0; index < numSparseElementsMeta; index++)
            {
                Block theBlock = theSparseArrayMeta[index].theBlock;
                int theMetaData = theSparseArrayMeta[index].theMetaData;
                theWorld.setBlock(
                        startX+theSparseArrayMeta[index].posX, 
                        startY+theSparseArrayMeta[index].posY, 
                        startZ+theSparseArrayMeta[index].posZ, 
                        theBlock, 
                        theMetaData, 
                        2
                        );
               if (theBlock.hasTileEntity(theMetaData))
               {
                   customizeTileEntity(
                           theBlock, 
                           theMetaData, 
                           startX+theSparseArrayMeta[index].posX, 
                           startY+theSparseArrayMeta[index].posY, 
                           startZ+theSparseArrayMeta[index].posZ
                           );
               }
            }
            finishedGeneratingMeta = true;
        }
        else if (!finishedGeneratingSpecial)
        {
//            // DEBUG
//            System.out.println("Generating special blocks");
            for (int index = 0; index < numSparseElementsSpecial; index++)
            {
                Block theBlock = theSparseArrayMeta[index].theBlock;
                int theMetaData = theSparseArrayMeta[index].theMetaData;
               theWorld.setBlock(
                       startX+theSparseArraySpecial[index].posX, 
                       startY+theSparseArraySpecial[index].posY, 
                       startZ+theSparseArraySpecial[index].posZ, 
                       theBlock, 
                       theMetaData, 
                       2
                       );
               if (theBlock.hasTileEntity(theMetaData))
               {
                   customizeTileEntity(
                           theBlock, 
                           theMetaData, 
                           startX+theSparseArraySpecial[index].posX, 
                           startY+theSparseArraySpecial[index].posY, 
                           startZ+theSparseArraySpecial[index].posZ
                           );
               }
            }
            finishedGeneratingSpecial = true;
        }
        else if (!finishedPopulatingItems)
        {
//            // DEBUG
//            System.out.println("Populating items");
            populateItems();
        }
        else if (!finishedPopulatingEntities)
        {
//            // DEBUG
//            System.out.println("Populating Entities");
            populateEntities();
        }
        else
        {
            // DEBUG
            System.out.println("Structure setting MagicBeansWorldData hasCastleBeenSpawned to true");
            ModWorldData.get(theWorld).setHasCastleSpawned(true);
            theWorld.getClosestPlayer(startX, startY, startZ, -1).addChatMessage(new ChatComponentText(Utilities.stringToRainbow("Look up! Something happened at the top of the bean stalk.")));
        }
    }
    
    @Override
    public void generateTick(TileEntity parEntity, int parOffsetX, int parOffsetY, int parOffsetZ)
    {
        // exit if generating not started
        if (!shouldGenerate)
        {
            return;
        }
        
        theTileEntity = parEntity;
        theWorld = theTileEntity.getWorldObj();

        if (theWorld.isRemote)
        {
            return;
        }

        // exit if finished
        if (ModWorldData.get(theWorld).getHasCastleSpawned())
        {
            // DEBUG
            System.out.println("Castle has already spawned");
            return;
        }

        startX = theTileEntity.xCoord-9; // +parOffsetX;
        startY = theTileEntity.yCoord-3; // +parOffsetY;
        startZ = theTileEntity.zCoord-12; // +parOffsetZ;

        totalVolume = dimX * dimY * dimZ;
        
        if (!finishedGeneratingBasic)
        {
//            // DEBUG
//            System.out.println("Generating basic blocks");
            generateBasicBlocksTick();
        }
        else if (!finishedGeneratingMeta)
        {
//            // DEBUG
//            System.out.println("Generating metadata blocks");
            generateMetaBlocksTick();
        }
        else if (!finishedGeneratingSpecial)
        {
//            // DEBUG
//            System.out.println("Generating special blocks");
            generateSpecialBlocksTick();
        }
        else if (!finishedPopulatingItems)
        {
//            // DEBUG
//            System.out.println("Populating items");
            populateItems();
        }
        else if (!finishedPopulatingEntities)
        {
//            // DEBUG
//            System.out.println("Populating Entities");
            populateEntities();
        }
        else
        {
            // DEBUG
            System.out.println("Structure setting MagicBeansWorldData hasCastleBeenSpawned to true");
            ModWorldData.get(theWorld).setHasCastleSpawned(true);
        }
    }
    
    @Override
    public void generateBasicBlocksTick() 
    {
        int indY = ticksGenerating/(dimX*dimZ);

        for (int indX = 0; indX < dimX; indX++)
        {
            for (int indZ = 0; indZ < dimZ; indZ++)
            {
                Block theBlock = Block.getBlockFromName(blockNameArray[indX][indY][indZ]);
                int theMetaData = blockMetaArray[indX][indY][indZ];
                if (theMetaData == 0) // check for basic block
                {
                    String blockName = blockNameArray[indX][indY][indZ];
                    if (!(blockName.equals("minecraft:tripwire"))) // tripwire/string needs to be placed after other blocks
                    {
                        theWorld.setBlock(startX+indX, startY+indY, startZ+indZ, 
                                theBlock, 0, 2);
                        if (theBlock.hasTileEntity(theMetaData))
                        {
                            customizeTileEntity(theBlock, theMetaData, startX+indX, startY+indY, startZ+indZ);
                        }
                    }
                }
            }
        }
        
        ticksGenerating += dimX * dimZ;
        if (ticksGenerating >= totalVolume)
        {
            // DEBUG
            System.out.println("Finishing generation basic blocks with dimX = "+dimX+" dimY = "+dimY+" dimZ = "+dimZ);
            finishedGeneratingBasic = true;
            ticksGenerating = 0;
        }
    }

    @Override
    public void generateMetaBlocksTick() 
    {
        int indY = ticksGenerating/(dimX*dimZ);

        for (int indX = 0; indX < dimX; indX++)
        {
            for (int indZ = 0; indZ < dimZ; indZ++)
            {
                Block theBlock = Block.getBlockFromName(blockNameArray[indX][indY][indZ]);
                int theMetaData = blockMetaArray[indX][indY][indZ];
                if (!(theMetaData == 0))
                {
                    if (theBlock == Blocks.lava) // in Jaden's castle there was issue with lava so making them all sources
                    {
                        theMetaData = 0;
                    }
                    theWorld.setBlock(startX+indX, startY+indY, startZ+indZ, 
                            theBlock, theMetaData, 2);
                    if (theBlock.hasTileEntity(theMetaData))
                    {
                        customizeTileEntity(theBlock, theMetaData, startX+indX, startY+indY, startZ+indZ);
                    }
                }    
            }
        }
        
        ticksGenerating += dimX * dimZ;
        if (ticksGenerating >= totalVolume)
        {
            // DEBUG
            System.out.println("Finishing generation meta blocks with dimX = "+dimX+" dimY = "+dimY+" dimZ = "+dimZ);
            finishedGeneratingMeta = true;
            ticksGenerating = 0;
        }
    }

    /**
     * In this method you can do additional processing for a tile entity
     * such as putting contents into the inventory.
     */
    @Override
    public void customizeTileEntity(Block theBlock, int theMetaData, int parX,
            int parY, int parZ) 
    {
        
    }

    @Override
    public void generateSpecialBlocksTick() 
    {
        int indY = ticksGenerating/(dimX*dimZ);

        for (int indX = 0; indX < dimX; indX++)
        {
            for (int indZ = 0; indZ < dimZ; indZ++)
            {
                String blockName = blockNameArray[indX][indY][indZ];
                if (blockName.equals("minecraft:tripwire"))
                {
                    theWorld.setBlock(startX+indX, startY+indY, startZ+indZ, 
                            Block.getBlockFromName(blockName), 0, 2);
                }            
            }
        }
        
        ticksGenerating += dimX * dimZ;
        if (ticksGenerating >= totalVolume)
        {
            // DEBUG
            System.out.println("Finishing generation special blocks with dimX = "+dimX+" dimY = "+dimY+" dimZ = "+dimZ);
            finishedGeneratingSpecial = true;
            ticksGenerating = 0;
        }
    }

    @Override
    public void populateItems()
    {
        // DEBUG
        System.out.println("Finished populating items in structure.");
        finishedPopulatingItems = true;
    }
    
    @Override
    public void populateEntities()
    {
        finishedPopulatingEntities = true;
    }

    public void generate(Entity parEntity, int parOffsetX, int parOffsetY, int parOffsetZ) 
    {
        Entity theEntity = parEntity;
        theWorld = theEntity.worldObj;
        if (theWorld.isRemote)
        {
            return;
        }

        startX = (int) theEntity.posX;
        startY = (int) theEntity.posY;
        startZ = (int) theEntity.posZ;
        
        for (int indY = 0; indY < dimY; indY++) // Y first to organize in vertical layers
        {
            for (int indX = 0; indX < dimX; indX++)
            {
                for (int indZ = 0; indZ < dimZ; indZ++)
                {
                    if (blockMetaArray[indX][indY][indZ]==0)
                    {
                        String blockName = blockNameArray[indX][indY][indZ];
                        if (!(blockName.equals("minecraft:tripwire"))) // tripwire/string needs to be placed after other blocks
                        {
                            theWorld.setBlock(startX+parOffsetX+indX, startY+parOffsetY+indY, startZ+parOffsetZ+indZ, 
                                    Block.getBlockFromName(blockName), 0, 2);
                        }
                    }                    
                }
            }
        }
        // best to place metadata blocks after non-metadata blocks as they need to attach, etc.
        for (int indY = 0; indY < dimY; indY++) // Y first to organize in vertical layers
        {
            for (int indX = 0; indX < dimX; indX++)
            {
                for (int indZ = 0; indZ < dimZ; indZ++)
                {
                    if (!(blockMetaArray[indX][indY][indZ]==0))
                    {
                        theWorld.setBlock(startX+parOffsetX+indX, startY+parOffsetY+indY, startZ+parOffsetZ+indZ, 
                                Block.getBlockFromName(blockNameArray[indX][indY][indZ]), blockMetaArray[indX][indY][indZ], 2);
                    }                    
                }
            }
        }
        // some blocks with 0 metadata, like string/tripwire, require other blocks to be placed already, so do them again as last pass.
        for (int indY = 0; indY < dimY; indY++) // Y first to organize in vertical layers
        {
            for (int indX = 0; indX < dimX; indX++)
            {
                for (int indZ = 0; indZ < dimZ; indZ++)
                {
                    String blockName = blockNameArray[indX][indY][indZ];
                    if (blockName.equals("minecraft:tripwire"))
                    {
                        theWorld.setBlock(startX+parOffsetX+indX, startY+parOffsetY+indY, startZ+parOffsetZ+indZ, 
                                Block.getBlockFromName(blockName), 0, 2);
                    }                    
                }
            }
        }        
    }
    
    /**
     * @param tileEntityMagicBeanStalk
     * @param parOffsetX
     * @param parOffsetY
     * @param parOffsetZ
     */
    public void generate(TileEntity parEntity, int parOffsetX, int parOffsetY, int parOffsetZ, boolean parSparse) 
    {
        if (parSparse)
        {
            generateSparse(parEntity, parOffsetZ, parOffsetZ, parOffsetZ);
        }
        else
        {
            TileEntity theEntity = parEntity;
            theWorld = theEntity.getWorldObj();
    
            if (theWorld.isRemote)
            {
                return;
            }
    
            startX = theEntity.xCoord+parOffsetX;
            startY = theEntity.yCoord+parOffsetY;
            startZ = theEntity.zCoord+parOffsetZ;
                    
            for (int indY = 0; indY < dimY; indY++) // Y first to organize in vertical layers
            {
                for (int indX = 0; indX < dimX; indX++)
                {
                    for (int indZ = 0; indZ < dimZ; indZ++)
                    {
                        if (blockMetaArray[indX][indY][indZ]==0)
                        {
                            String blockName = blockNameArray[indX][indY][indZ];
                            if (!(blockName.equals("minecraft:tripwire"))) // tripwire/string needs to be placed after other blocks
                            {
                                theWorld.setBlock(startX+indX, startY+indY, startZ+indZ, 
                                        Block.getBlockFromName(blockName), 0, 2);
                            }
                        }                    
                    }
                }
            }
            // best to place metadata blocks after non-metadata blocks as they need to attach, etc.
            for (int indY = 0; indY < dimY; indY++) // Y first to organize in vertical layers
            {
                for (int indX = 0; indX < dimX; indX++)
                {
                    for (int indZ = 0; indZ < dimZ; indZ++)
                    {
                        if (!(blockMetaArray[indX][indY][indZ]==0))
                        {
                            theWorld.setBlock(startX+indX, startY+indY, startZ+indZ, 
                                    Block.getBlockFromName(blockNameArray[indX][indY][indZ]), blockMetaArray[indX][indY][indZ], 2);
                        }                    
                    }
                }
            }
            // some blocks with 0 metadata, like string/tripwire, require other blocks to be placed already, so do them again as last pass.
            for (int indY = 0; indY < dimY; indY++) // Y first to organize in vertical layers
            {
                for (int indX = 0; indX < dimX; indX++)
                {
                    for (int indZ = 0; indZ < dimZ; indZ++)
                    {
                        String blockName = blockNameArray[indX][indY][indZ];
                        if (blockName.equals("minecraft:tripwire"))
                        {
                            theWorld.setBlock(startX+indX, startY+indY, startZ+indZ, 
                                    Block.getBlockFromName(blockName), 0, 2);
                        }                    
                    }
                }
            }        
        }
    }
}
