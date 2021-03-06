package exopandora.worldhandler.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nullable;

import exopandora.worldhandler.Main;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@OnlyIn(Dist.CLIENT)
public class RegistryHelper
{
	private static final Map<IForgeRegistry<?>, Function<? extends ForgeRegistryEntry<?>, String>> FORGE = new HashMap<IForgeRegistry<?>, Function<? extends ForgeRegistryEntry<?>, String>>();
	
	static
	{
		registerRegistry(ForgeRegistries.BLOCKS, Block::getDescriptionId);
		registerRegistry(ForgeRegistries.ITEMS, Item::getDescriptionId);
		registerRegistry(ForgeRegistries.POTIONS, Effect::getDescriptionId);
		registerRegistry(ForgeRegistries.BIOMES, biome ->
		{
			MutableRegistry<Biome> registry = DynamicRegistries.builtin().registryOrThrow(Registry.BIOME_REGISTRY);
			ResourceLocation resource = registry.getKey(biome);
			String key = "biome." + biome.getRegistryName().getNamespace() + "." + resource.getPath();
			
			return LanguageMap.getInstance().has(key) ? key : resource.toString();
		});
		registerRegistry(ForgeRegistries.ENCHANTMENTS, Enchantment::getDescriptionId);
		registerRegistry(ForgeRegistries.ENTITIES, EntityType::getDescriptionId);
		registerRegistry(ForgeRegistries.STAT_TYPES, stat -> "stat." + stat.toString().replace(':', '.'));
	}
	
	private static <T extends ForgeRegistryEntry<T>> void registerRegistry(IForgeRegistry<T> registry, Function<T, String> mapper)
	{
		FORGE.put(registry, mapper);
	}
	
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T extends ForgeRegistryEntry<T>> String translate(ResourceLocation resource)
	{
		for(IForgeRegistry<?> registry : FORGE.keySet())
		{
			if(registry.containsKey(resource))
			{
				return ((Function<T, String>) FORGE.get(registry)).apply((T) registry.getValue(resource));
			}
		}
		
		return null;
	}
	
	public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistry<T> registry, String name, T entry)
	{
		register(registry, Main.MODID, name, entry);
	}
	
	public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistry<T> registry, String modid, String name, T entry)
	{
		registry.register(entry.setRegistryName(new ResourceLocation(modid, name)));
	}
}
