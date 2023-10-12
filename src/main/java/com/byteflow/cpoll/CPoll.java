package com.byteflow.cpoll;

import com.byteflow.cpoll.commands.PollCommands;
import com.byteflow.cpoll.components.CreateConfigFile;
import com.byteflow.cpoll.storage.VoteData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CPoll extends JavaPlugin {
    private static CPoll cPoll_instance;
    FileConfiguration configuration = getConfig();
    @Override
    public void onEnable() {
        VoteData voteData = new VoteData();
        voteData.addStatus(0, true);
        voteData.addStatus(1, false);
        voteData.saveStatusToFile("cpoll_data.json");

        saveDefaultConfig();
        cPoll_instance = this;
        CreateConfigFile.setDefaultValues(configuration);
        initPlugin();

        Objects.requireNonNull(getCommand("poll")).setExecutor(new PollCommands());
        Objects.requireNonNull(getCommand("cpollreload")).setExecutor(new PollCommands());
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[ CPOLL closing ]");
    }

    public static CPoll getInstance(){
        return cPoll_instance;
    };
    private void initPlugin(){
        Bukkit.getLogger().info("\n\n" +
                "░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░\n" +
                "░░░░░░░░░┌───────────────────┐░░░░░░░░\n" +
                "░░░░░░░░░│──▄▀▀▀▄▄▄▄▄▄▄▀▀▀▄──│░░░░░░░░\n" +
                "░░░░░░░░░│──█▒▒░░░░░░░░░▒▒█──│░░░░░░░░\n" +
                "░░░░░░░░░│───█░░█░░░░░█░░█───│░░░░░░░░\n" +
                "░░░░░░░░░│▄▄──█░░░▀█▀░░░█──▄▄│░░░░░░░░\n" +
                "░░░░░░░░░█░░█─▀▄░░░░░░░▄▀─█░░█░░░░░░░░\n" +
                "░░░░░░░░░█▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀█░░░░░░░░\n" +
                "░░░░░░░░░█░░░░╔═╗░░░░░░░░░░░░█░░░░░░░░\n" +
                "░░░░░░░░░█░░░░║╔╬═╦═╦╗╔╗░░░░░█░░░░░░░░\n" +
                "░░░░░░░░░█░░░░║╚╣╬║╬║╚╣╚╗░░░░█░░░░░░░░\n" +
                "░░░░░░░░░█░░░░╚═╣╔╩═╩═╩═╝░░░░█░░░░░░░░\n" +
                "░░░░░░░░░█░░░░░░╚╝░░░░░░░░░░░█░░░░░░░░\n" +
                "░░░░░░░░░█▄▄▄▄[v-1.1.23 ]▄▄▄▄█░░░░░░░░\n" +
                "░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░\n" +
                "\n");
        Bukkit.getLogger().info("[ CPOLL Loaded successfully ]");
    }
}
