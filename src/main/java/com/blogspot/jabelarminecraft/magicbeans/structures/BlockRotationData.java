package com.blogspot.jabelarminecraft.magicbeans.structures;

/**
Copyright (C) <2014> <coolAlias>

This file is part of coolAlias' Structure Generation Tool; as such,
you can redistribute it and/or modify it under the terms of the GNU
General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class BlockRotationData
{
	/** Valid rotation types. Each type is handled like vanilla blocks of this kind. */
	public static enum Rotation {ANVIL, DOOR, GENERIC, PISTON_CONTAINER, QUARTZ, RAIL, REPEATER,
		SIGNPOST, SKULL, STAIRS, TRAPDOOR, VINE, WALL_MOUNTED, LEVER, WOOD};
	
	/** A mapping of block ids to rotation type for handling rotation. Allows custom blocks to be added. */
	private static final Map<Integer, Rotation> blockRotationData = new HashMap<Integer, Rotation>();
	
	/**
	 * Returns the rotation type for the block id given, or null if no type is registered
	 */
	public static final Rotation getBlockRotationType(int blockId) {
		return blockRotationData.get(blockId);
	}
	
	/**
	 * Maps a block id to a specified rotation type. Allows custom blocks to rotate with structure.
	 * @param blockID a valid block id, 0 to 4095 (4096 total)
	 * @param rotationType types predefined by enumerated type ROTATION
	 * @return false if a rotation type has already been specified for the given blockID
	 */
	public static final boolean registerCustomBlockRotation(int blockID, Rotation rotationType) {
		return registerCustomBlockRotation(blockID, rotationType, false);
	}
	
	/**
	 * Maps a block id to a specified rotation type. Allows custom blocks to rotate with structure.
	 * @param blockID a valid block id, 0 to 4095 (4096 total)
	 * @param rotationType types predefined by enumerated type ROTATION
	 * @param override if true, will override the previously set rotation data for specified blockID
	 * @return false if a rotation type has already been specified for the given blockID
	 */
	public static final boolean registerCustomBlockRotation(int blockID, Rotation rotationType, boolean override)
	{
//		if (Block.blocksList[blockID] == null || blockID < 0 || blockID > 4095) {
//			throw new IllegalArgumentException("[STRUCTURE GEN API] Error setting custom block rotation for block ID " + blockID + (Block.blocksList[blockID] == null ? "; block was not found in Block.blocksList. Please register your block." : "Valid ids are (0-4095)"));
//		}
	
		if (blockRotationData.containsKey(blockID)) {
			LogHelper.log(Level.WARNING, "Block " + blockID + " already has a rotation type." + (override ? " Overriding previous data." : ""));
			if (override) blockRotationData.remove(blockID);
			else return false;
		}
	
		blockRotationData.put(blockID, rotationType);
	
		return true;
	}
	
	/** Set rotation data for vanilla blocks */
	static
	{
		blockRotationData.put(Block.getIdFromBlock(Blocks.anvil), Rotation.ANVIL);
	
		blockRotationData.put(Block.getIdFromBlock(Blocks.iron_door), Rotation.DOOR);
		blockRotationData.put(Block.getIdFromBlock(Blocks.wooden_door), Rotation.DOOR);
	
		blockRotationData.put(Block.getIdFromBlock(Blocks.bed), Rotation.GENERIC);
		blockRotationData.put(Block.getIdFromBlock(Blocks.cocoa), Rotation.GENERIC);
		blockRotationData.put(Block.getIdFromBlock(Blocks.fence_gate), Rotation.GENERIC);
		blockRotationData.put(Block.getIdFromBlock(Blocks.pumpkin), Rotation.GENERIC);
		blockRotationData.put(Block.getIdFromBlock(Blocks.lit_pumpkin), Rotation.GENERIC);
		blockRotationData.put(Block.getIdFromBlock(Blocks.end_portal), Rotation.GENERIC);
		blockRotationData.put(Block.getIdFromBlock(Blocks.tripwire_hook), Rotation.GENERIC);
	
		blockRotationData.put(Block.getIdFromBlock(Blocks.chest), Rotation.PISTON_CONTAINER);
		blockRotationData.put(Block.getIdFromBlock(Blocks.trapped_chest), Rotation.PISTON_CONTAINER);
		blockRotationData.put(Block.getIdFromBlock(Blocks.dispenser), Rotation.PISTON_CONTAINER);
		blockRotationData.put(Block.getIdFromBlock(Blocks.dropper), Rotation.PISTON_CONTAINER);
		blockRotationData.put(Block.getIdFromBlock(Blocks.ender_chest), Rotation.PISTON_CONTAINER);
		blockRotationData.put(Block.getIdFromBlock(Blocks.lit_furnace), Rotation.PISTON_CONTAINER);
		blockRotationData.put(Block.getIdFromBlock(Blocks.furnace), Rotation.PISTON_CONTAINER);
		blockRotationData.put(Block.getIdFromBlock(Blocks.hopper), Rotation.PISTON_CONTAINER);
		blockRotationData.put(Block.getIdFromBlock(Blocks.ladder), Rotation.PISTON_CONTAINER);
		blockRotationData.put(Block.getIdFromBlock(Blocks.wall_sign), Rotation.PISTON_CONTAINER);
		blockRotationData.put(Block.getIdFromBlock(Blocks.piston), Rotation.PISTON_CONTAINER);
		blockRotationData.put(Block.getIdFromBlock(Blocks.piston_extension), Rotation.PISTON_CONTAINER);
		blockRotationData.put(Block.getIdFromBlock(Blocks.piston_head), Rotation.PISTON_CONTAINER);
		blockRotationData.put(Block.getIdFromBlock(Blocks.sticky_piston), Rotation.PISTON_CONTAINER);
		blockRotationData.put(Block.getIdFromBlock(Blocks.activator_rail), Rotation.PISTON_CONTAINER);
		blockRotationData.put(Block.getIdFromBlock(Blocks.detector_rail), Rotation.PISTON_CONTAINER);
		// blockRotationData.put(Block.railPowered.blockID, Rotation.PISTON_CONTAINER);
	
		blockRotationData.put(Block.getIdFromBlock(Blocks.quartz_block), Rotation.QUARTZ); // was netherQuartz
	
		blockRotationData.put(Block.getIdFromBlock(Blocks.rail), Rotation.RAIL);
	
		blockRotationData.put(Block.getIdFromBlock(Blocks.powered_comparator), Rotation.REPEATER);
		blockRotationData.put(Block.getIdFromBlock(Blocks.unpowered_comparator), Rotation.REPEATER);
		blockRotationData.put(Block.getIdFromBlock(Blocks.powered_repeater), Rotation.REPEATER);
		blockRotationData.put(Block.getIdFromBlock(Blocks.unpowered_repeater), Rotation.REPEATER);
	
		blockRotationData.put(Block.getIdFromBlock(Blocks.standing_sign), Rotation.SIGNPOST);
	
		blockRotationData.put(Block.getIdFromBlock(Blocks.skull), Rotation.SKULL);
	
		blockRotationData.put(Block.getIdFromBlock(Blocks.brick_stairs), Rotation.STAIRS);
		blockRotationData.put(Block.getIdFromBlock(Blocks.stone_stairs), Rotation.STAIRS);
		blockRotationData.put(Block.getIdFromBlock(Blocks.nether_brick_stairs), Rotation.STAIRS);
		blockRotationData.put(Block.getIdFromBlock(Blocks.quartz_stairs), Rotation.STAIRS);
		blockRotationData.put(Block.getIdFromBlock(Blocks.sandstone_stairs), Rotation.STAIRS);
		blockRotationData.put(Block.getIdFromBlock(Blocks.stone_brick_stairs), Rotation.STAIRS);
		blockRotationData.put(Block.getIdFromBlock(Blocks.birch_stairs), Rotation.STAIRS);
		blockRotationData.put(Block.getIdFromBlock(Blocks.jungle_stairs), Rotation.STAIRS);
		blockRotationData.put(Block.getIdFromBlock(Blocks.oak_stairs), Rotation.STAIRS);
		blockRotationData.put(Block.getIdFromBlock(Blocks.spruce_stairs), Rotation.STAIRS);
	
		blockRotationData.put(Block.getIdFromBlock(Blocks.trapdoor), Rotation.TRAPDOOR);
	
		blockRotationData.put(Block.getIdFromBlock(Blocks.vine), Rotation.VINE);
	
		blockRotationData.put(Block.getIdFromBlock(Blocks.lever), Rotation.LEVER);
	
		blockRotationData.put(Block.getIdFromBlock(Blocks.stone_button), Rotation.WALL_MOUNTED);
		blockRotationData.put(Block.getIdFromBlock(Blocks.wooden_button), Rotation.WALL_MOUNTED);
		blockRotationData.put(Block.getIdFromBlock(Blocks.redstone_torch), Rotation.WALL_MOUNTED);
		blockRotationData.put(Block.getIdFromBlock(Blocks.unlit_redstone_torch), Rotation.WALL_MOUNTED);
		blockRotationData.put(Block.getIdFromBlock(Blocks.torch), Rotation.WALL_MOUNTED);
	
		blockRotationData.put(Block.getIdFromBlock(Blocks.log), Rotation.WOOD); // supposed to be "wood"
	}
}