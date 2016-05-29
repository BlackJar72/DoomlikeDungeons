package jaredbgreat.dldungeons.pieces.chests;

/**
 * The three types of loot for chests:
 * 
 * <ol>
 * <li>GEAR: armor / weapons / tools that are useful combat or dungeoneering</li>
 * <li>HEAL: health items (usually food)</li>
 * <li>LOOT: Treasure items (or junk at low levels)</li>
 * </ol>
 * 
 * This also includes RANDOM, selecting any of the above three randomly. * 
 * 
 * @author Jared Blackburn *
 */
public enum LootType {
	GEAR,
	HEAL,
	LOOT,
	RANDOM;
}
