package com.byteflow.cpoll.components;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageComponent {
    public static void send(CommandSender sender, String message){
        send(sender, message, "&r");
    }
    public static void send(CommandSender sender, String message, String prefix){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+message));
    }
}
