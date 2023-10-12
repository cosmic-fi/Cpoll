package com.byteflow.cpoll.components;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlaySound {
    static float volume = 1.0f;
    static float pitch = 1.0f;
    public static void playStartSound(Sound startSound){
        play(startSound);
    }
    public static void playEndSound(Sound endSound){
        play(endSound);
    }

    private static void play(Sound sound){
        for(Player player: Bukkit.getOnlinePlayers()){
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }
}
