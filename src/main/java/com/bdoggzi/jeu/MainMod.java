package com.bdoggzi.jeu;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.client.Minecraft;
import java.util.HashMap;
import java.util.Map;

@Mod(modid = MainMod.MODID, name = MainMod.NAME, version = MainMod.VERSION)
public class MainMod {
    public static final String MODID = "jeu";
    public static final String NAME = "Just Enough Updates";
    public static final String VERSION = "0.1-BETA";

    // Keybinding
    private static final KeyBinding openGuiKey = new KeyBinding("Opens The GUI", Keyboard.KEY_RSHIFT, "Just Enough Updates");

    // Map to store command toggles
    private static final Map<String, CommandToggle> commandToggles = new HashMap<String, CommandToggle>();

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // Register the keybinding and event bus on the client side
        if (event.getSide() == Side.CLIENT) {
            // Register keybinding
            ClientRegistry.registerKeyBinding(openGuiKey);
            // Register the event handler for key inputs
            MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
            // Register the event handler for chat messages
            MinecraftForge.EVENT_BUS.register(new ChatHandler());
            // Initialize command toggles
            commandToggles.put("p", new CommandToggle("p", true));
            commandToggles.put("pme", new CommandToggle("pme", true));
            commandToggles.put("warp", new CommandToggle("warp", true));

        }
    }

    @SideOnly(Side.CLIENT)
    public static class KeyInputHandler {
        @SubscribeEvent
        public void onKeyInput(InputEvent.KeyInputEvent event) {
            if (openGuiKey.isPressed()) {
                Minecraft.getMinecraft().displayGuiScreen(new Gui(commandToggles));
            }
        }
    }

    // Getter method for command toggles
    public static Map<String, CommandToggle> getCommandToggles() {
        return commandToggles;
    }
}
