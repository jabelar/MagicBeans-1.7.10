package com.blogspot.jabelarminecraft.magicbeans.structures;

public class StructureCastle extends Structure
{
	
	public static final int[][][] blockArray =
	{
	    { // y = 1
	        { WOOD, WOOD, DOOR, WOOD, WOOD},
	        { WOOD,  AIR,  AIR,CHEST, WOOD},
	        { WOOD,  AIR,  AIR,  AIR, WOOD},
	        { WOOD,  BED,  BED,  AIR, WOOD},
	        { WOOD, WOOD, WOOD, WOOD, WOOD}
	    },
	    { // y = 2
	        { WOOD, WOOD, DOOR, WOOD, WOOD},
	        { WOOD,TORCH,  AIR,  AIR, WOOD},
	        {GLASS,  AIR,  AIR,  AIR,GLASS},
	        { WOOD,  AIR,  AIR,  AIR, WOOD},
	        { WOOD, WOOD,GLASS, WOOD, WOOD}
	    },
	    { // y = 3
	        { WOOD, WOOD, WOOD, WOOD, WOOD},
	        { WOOD, WOOD, WOOD, WOOD, WOOD},
	        { WOOD, WOOD, WOOD, WOOD, WOOD},
	        { WOOD, WOOD, WOOD, WOOD, WOOD},
	        { WOOD, WOOD, WOOD, WOOD, WOOD}
	    }
	};
	
	public StructureCastle()
	{
		super(blockArray);
	}
}
