package exopandora.worldhandler.gui.category;

import exopandora.worldhandler.main.Main;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Categories
{
	public static final Category MAIN;
	public static final Category ENTITIES;
	public static final Category ITEMS;
	public static final Category BLOCKS;
	public static final Category WORLD;
	public static final Category PLAYER;
	public static final Category SCOREBOARD;
	
	static
	{
		MAIN = Categories.getRegisteredCategory("main");
		ENTITIES = Categories.getRegisteredCategory("entities");
		ITEMS = Categories.getRegisteredCategory("items");
		BLOCKS = Categories.getRegisteredCategory("blocks");
		WORLD = Categories.getRegisteredCategory("world");
		PLAYER = Categories.getRegisteredCategory("player");
		SCOREBOARD = Categories.getRegisteredCategory("scoreboard");
	}
	
	private static Category getRegisteredCategory(String name)
	{
		Category category = Category.REGISTRY.getObject(new ResourceLocation(Main.MODID, name));
		
		if(category == null)
		{
			throw new IllegalStateException("Invalid Category requested: " + name);
		}
		
		return category;
	}
}
