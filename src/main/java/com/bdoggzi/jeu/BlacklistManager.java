package com.bdoggzi.jeu;

import java.util.HashSet;
import java.util.Set;

public class BlacklistManager {
    private static final Set<String> blacklist = new HashSet<String>();

    public static boolean isPlayerBlacklisted(String playerName) {
        return blacklist.contains(playerName);
    }

    public static void addToBlacklist(String playerName) {
        blacklist.add(playerName);
    }

    public static void removeFromBlacklist(String playerName) {
        blacklist.remove(playerName);
    }

    public static Set<String> getBlacklist() {
        return blacklist;
    }
}