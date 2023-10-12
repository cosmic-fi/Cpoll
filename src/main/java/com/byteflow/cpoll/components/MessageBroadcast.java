package com.byteflow.cpoll.components;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageBroadcast {
    public static void broadcast(String message){
        String formattedMessage = ChatColor.translateAlternateColorCodes('&', message);
        Bukkit.broadcastMessage(formattedMessage);
    }
    public static void broadCastComponent(ComponentBuilder component){
        for (Player players : Bukkit.getServer().getOnlinePlayers()){
            players.spigot().sendMessage(component.create());
        }
    }
}
