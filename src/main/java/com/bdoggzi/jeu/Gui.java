package com.bdoggzi.jeu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Mouse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Gui extends GuiScreen {
    private Map<String, CommandToggle> commandToggles;
    private GuiTextField blacklistTextField;
    private int scrollOffset = 0;
    private final int guiWidth = 250;
    private final int guiHeight = 250;
    private final int maxDisplay = 20; // Maximum number of names to display at once

    public Gui(Map<String, CommandToggle> commandToggles) {
        this.commandToggles = commandToggles;
    }

    @Override
    public void initGui() {
        int guiX = (width - guiWidth) / 2;
        int guiY = (height - guiHeight) / 2;

        // Initialize your GUI components here
        blacklistTextField = new GuiTextField(10, fontRendererObj, guiX + 10, guiY + 220, 120, 20);
        buttonList.add(new GuiButton(4, guiX + 140, guiY + 220, 100, 20, "Add Blacklist"));
        buttonList.add(new GuiButton(5, guiX + 140, guiY + 245, 100, 20, "Remove Blacklist"));
        // Assuming commandToggles is already populated with "p", "pme", and "warp" keys
        buttonList.add(new GuiButton(1, guiX + 140, guiY + 290, 100, 20, getButtonLabel("p")));
        buttonList.add(new GuiButton(2, guiX + 140, guiY + 315, 100, 20, getButtonLabel("pme")));
        buttonList.add(new GuiButton(3, guiX + 140, guiY + 340, 100, 20, getButtonLabel("warp")));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        switch (button.id) {
            case 1: // Toggle !p
                CommandToggle toggleP = commandToggles.get("p");
                if (toggleP != null) {
                    toggleP.toggle();
                }
                break;
            case 2: // Toggle !pme
                CommandToggle togglePme = commandToggles.get("pme");
                if (togglePme != null) {
                    togglePme.toggle();
                }
                break;
            case 3: // Toggle !warp
                CommandToggle toggleWarp = commandToggles.get("warp");
                if (toggleWarp != null) {
                    toggleWarp.toggle();
                }
                break;
            case 4: // Add to Blacklist
                String playerNameToAdd = blacklistTextField.getText();
                if (playerNameToAdd != null && !playerNameToAdd.trim().isEmpty()) {
                    BlacklistManager.addToBlacklist(playerNameToAdd.trim());
                    blacklistTextField.setText(""); // Clear the text field after adding
                }
                break;
            case 5: // Remove from Blacklist
                String playerNameToRemove = blacklistTextField.getText();
                if (playerNameToRemove != null && !playerNameToRemove.trim().isEmpty()) {
                    BlacklistManager.removeFromBlacklist(playerNameToRemove.trim());
                    blacklistTextField.setText(""); // Clear the text field after removing
                }
                break;
        }
        for (GuiButton guiButton : (List<GuiButton>) buttonList) {
            if (guiButton.id == 1) {
                guiButton.displayString = getButtonLabel("p");
            } else if (guiButton.id == 2) {
                guiButton.displayString = getButtonLabel("pme");
            } else if (guiButton.id == 3) {
                guiButton.displayString = getButtonLabel("warp");
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        blacklistTextField.drawTextBox();

        // Draw the blacklist entries based on scrollOffset
        List<String> blacklist = new ArrayList<String>(BlacklistManager.getBlacklist());
        int startY = 20; // Starting Y position for listing names
        for (int i = scrollOffset; i < Math.min(scrollOffset + maxDisplay, blacklist.size()); i++) {
            drawString(fontRendererObj, blacklist.get(i), width / 2 - guiWidth / 4, startY + (i - scrollOffset) * 10, 0xFFFFFFFF);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        blacklistTextField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (blacklistTextField.isFocused()) {
            blacklistTextField.textboxKeyTyped(typedChar, keyCode);
        }
        // Additional logic for handling other key events, if necessary
    }

    private String getButtonLabel(String commandKey) {
        CommandToggle commandToggle = commandToggles.get(commandKey);
        if (commandToggle != null && commandToggle.isEnabled()) {
            return commandKey.toUpperCase() + " ON";
        } else {
            return commandKey.toUpperCase() + " OFF";
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) { // Scroll down
            if (scrollOffset < BlacklistManager.getBlacklist().size() - maxDisplay) {
                scrollOffset++;
            }
        } else if (dWheel > 0) { // Scroll up
            if (scrollOffset > 0) {
                scrollOffset--;
            }
        }
    }
}
