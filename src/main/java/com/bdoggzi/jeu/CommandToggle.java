package com.bdoggzi.jeu;

public class CommandToggle {
    private String command;
    private boolean enabled;

    public CommandToggle(String command, boolean enabled) {
        this.command = command;
        this.enabled = enabled;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }
}