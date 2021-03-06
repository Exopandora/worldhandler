package exopandora.worldhandler.event;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

import exopandora.worldhandler.config.Config;
import exopandora.worldhandler.util.ActionHelper;
import exopandora.worldhandler.util.BlockHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class KeyHandler
{
	public static final KeyBinding KEY_WORLD_HANDLER = new KeyBinding("key.worldhandler", GLFW.GLFW_KEY_V, "key.categories.worldhandler");
	public static final KeyBinding KEY_WORLD_HANDLER_POS1 = new KeyBinding("key.worldhandler.pos1", GLFW.GLFW_KEY_O, "key.categories.worldhandler");
	public static final KeyBinding KEY_WORLD_HANDLER_POS2 = new KeyBinding("key.worldhandler.pos2", GLFW.GLFW_KEY_P, "key.categories.worldhandler");
	
	@SubscribeEvent
	public static void keyInputEvent(KeyInputEvent event)
	{
		if(Minecraft.getInstance() != null && Minecraft.getInstance().screen == null)
		{
			if(KEY_WORLD_HANDLER.consumeClick())
			{
				ActionHelper.displayGui();
			}
			else if(Config.getSettings().shortcutKeys() && KEY_WORLD_HANDLER_POS1.consumeClick())
			{
				BlockHelper.setPos1(BlockHelper.getFocusedBlockPos());
			}
			else if(Config.getSettings().shortcutKeys() && KEY_WORLD_HANDLER_POS2.consumeClick())
			{
				BlockHelper.setPos2(BlockHelper.getFocusedBlockPos());
			}
		}
	}
	
	public static void updatePosKeys()
	{
		boolean isRegistered = KeyHandler.arePosKeysRegistered();
		
		if(Config.getSettings().shortcutKeys() && !isRegistered)
		{
			ClientRegistry.registerKeyBinding(KEY_WORLD_HANDLER_POS1);
			ClientRegistry.registerKeyBinding(KEY_WORLD_HANDLER_POS2);
		}
		else if(!Config.getSettings().shortcutKeys() && isRegistered)
		{
			KeyHandler.removePosKeys();
		}
	}
	
	public static boolean arePosKeysRegistered()
	{
		return ArrayUtils.contains(Minecraft.getInstance().options.keyMappings, KEY_WORLD_HANDLER_POS1) || ArrayUtils.contains(Minecraft.getInstance().options.keyMappings, KEY_WORLD_HANDLER_POS2);
	}
	
	public static void removePosKeys()
	{
		Minecraft.getInstance().options.keyMappings = ArrayUtils.removeElements(Minecraft.getInstance().options.keyMappings, KEY_WORLD_HANDLER_POS1, KEY_WORLD_HANDLER_POS2);
	}
}
