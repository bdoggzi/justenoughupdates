package com.bdoggzi.jeu;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.bdoggzi.jeu.BlacklistManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Map;

public class ChatHandler {
    private int ticks = 0;
    private boolean triggerWarp = false;

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (event == null || event.message == null) {
            return; // Exit method if event or message is null
        }

        String message = event.message.getUnformattedText();
        // Retrieve command toggles map
        Map<String, CommandToggle> commandToggles = MainMod.getCommandToggles();

        if (commandToggles.containsKey("warp") && commandToggles.get("warp").isEnabled() && message.contains("Party") && message.contains("!warp")) {
            String[] parts = message.split("f:");
            if (parts.length > 1) {
                String playerNamePart = parts[0];
                String[] playerNameParts = playerNamePart.split(" ");
                String playerName = playerNameParts[playerNameParts.length - 1].trim();
                if (BlacklistManager.isPlayerBlacklisted(playerName)) {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/message " + playerName + " " + playerName + " Is blacklisted");
                } else {
                    triggerWarp = true; // Set the flag to true when the condition is met
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/pc Warping in 1 second");
                }
            }
        }
        if (commandToggles.containsKey("pme") && commandToggles.get("pme").isEnabled() && message.contains("From") && message.contains("!pme")) {
            // Split the message by ":" to extract the player's name
            String[] parts = message.split(":");
            if (parts.length > 1) {
                // The first part contains "From [RANK] PlayerName", extract the PlayerName
                String playerNamePart = parts[0];
                String[] playerNameParts = playerNamePart.split(" ");
                if (playerNameParts.length > 2) {
                    String playerName = playerNameParts[playerNameParts.length - 1];
                    if (BlacklistManager.isPlayerBlacklisted(playerName)) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/message " + playerName + " " + playerName + " Is blacklisted");
                    } else {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/party invite " + playerName);
                    }
                }
            }
        }
        if (commandToggles.containsKey("p") && commandToggles.get("p").isEnabled() && message.contains("Party") && message.contains("!p ")) {
            String[] parts = message.split("f:");
            if (parts.length > 1) {
                String playerNamePart = parts[0];
                String[] playerNameParts = playerNamePart.split(" ");
                String playerName = playerNameParts[playerNameParts.length - 1].trim();
                if (BlacklistManager.isPlayerBlacklisted(playerName)) {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/message " + playerName + " " + playerName + " Is blacklisted");
                } else {
                    String[] parts1 = message.split("!p");
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/party invite" + parts1[1]);
                }
            }

        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (triggerWarp) {
            ticks++;
            if (ticks >= 20) { // Approximately 5 seconds if 20 ticks = 1 second
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/p warp");
                triggerWarp = false; // Reset the trigger
                ticks = 0; // Reset the tick counter
            }
        }
    }
}