package com.byteflow.cpoll.commands;

import com.byteflow.cpoll.CPoll;
import com.byteflow.cpoll.components.MessageBroadcast;
import com.byteflow.cpoll.components.MessageComponent;
import com.byteflow.cpoll.components.PlaySound;
import com.byteflow.cpoll.storage.VoteData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PollCommands implements CommandExecutor {
    public Map<String, Integer> responses; // map of answer choices to number of responses
    public Set<String> votedPlayers;
    FileConfiguration configuration;
    long delay;
    boolean isNewVote;
    private static BossBar bossBar;

    public PollCommands(){
        this.responses = new HashMap<>();
        this.votedPlayers = new HashSet<>();
        this.isNewVote = false;
    }
    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        configuration = CPoll.getInstance().getConfig();

        VoteData voteData = new VoteData();
        voteData.loadVoteSatus("cpoll_data.json");

        if(!(sender instanceof Player)){
            MessageComponent.send(sender, configuration.getString("command-sender-error"));
            return true;
        }

        Player player = (Player) sender;

        if(command.getName().equalsIgnoreCase("poll")){
            String[] input = Arrays.copyOfRange(args, 0, args.length);
            String joinedInput = String.join(" ", input);

            String question = joinedInput.replaceAll("^<(.+?)>.+", "$1");

            if(joinedInput.startsWith("/")){
                if(voteData.getStatus().get(1).equals(false)){
                    MessageComponent.send(sender, configuration.getString("vote-party-end-message"));
                }else{
                    if(voteData.getStatus().get(0).equals(false)){
                        if(votedPlayers.contains(player.getName())){
                            MessageComponent.send(sender, configuration.getString("one-time-vote-message"));
                        }else{
                            String playerChoice = joinedInput.replaceAll("/", "");
                            if(responses.containsKey(playerChoice)){
                                int currentCount = responses.get(playerChoice);
                                responses.put(playerChoice, currentCount + 1);

                                MessageComponent.send(sender, configuration.getString("vote-success-message"));
                                votedPlayers.add(player.getName());
                            }else{
                                MessageComponent.send(sender, configuration.getString("choice-error-message"));
                            }
                        }
                    }else{
                        MessageComponent.send(sender, configuration.getString("vote-party-end-message"));
                    }
                }
            }else{
                //check for permission
                if(player.hasPermission("CPoll.poll")){
                    if (voteData.getStatus().get(0).equals(true)) {
                        if (args.length < 1) {
                            MessageComponent.send(sender, configuration.getString("invalid-command-error"));
                            MessageComponent.send(sender, configuration.getString("usage-text"));
                            return true;
                        }

                        try {
                            delay = Long.parseLong(input[input.length - 1]);

                            List<String> choices = new ArrayList<>();
                            Matcher matcher = Pattern.compile("\\{(.*?)}").matcher(joinedInput);
                            while (matcher.find()) {
                                choices.add(matcher.group(1).replaceAll("&[a-f0-9klmnor]", ""));
                            }

                            if (choices.isEmpty()) {
                                MessageComponent.send(sender, configuration.getString("invalid-command-error"));
                                MessageComponent.send(sender, configuration.getString("usage-text"));
                                return true;
                            } else {
                                bossBar = Bukkit.createBossBar("Poll Started", BarColor.YELLOW, BarStyle.SOLID);
                                bossBar.setVisible(true);

                                PlaySound.playStartSound(Sound.ENTITY_ITEM_PICKUP);

                                ComponentBuilder component = new ComponentBuilder(ChatColor.translateAlternateColorCodes(
                                        '&', configuration.getString("question-tag") + " &r: " + question + "\n")).append(" ");

                                responses = new HashMap<>();
                                votedPlayers = new HashSet<>();

                                for (String selection : choices) {
                                    responses.put(selection, 0);

                                    component
                                            .append(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&l[ &r&a" + selection + " &r&l]"))
                                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/poll /" + selection))
                                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                    new ComponentBuilder(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes(
                                                            '&', "&bClick to vote for [ ")
                                                            + net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&',
                                                            "&6" + selection)
                                                            + net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&',
                                                            "&b ] &r&f or do (&6/poll /" + selection + ") &f&o(Answers are case sensitive)."))
                                                            .create()))
                                            .append("  ");
                                }
                                MessageBroadcast.broadCastComponent(component);

                                long totalTime = delay * 1000L; // Convert seconds to milliseconds
                                long startTime = System.currentTimeMillis();

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        long currentTime = System.currentTimeMillis();
                                        long elapsedTime = currentTime - startTime;
                                        long remainingTime = totalTime - elapsedTime;

                                        double progress = (double) remainingTime / totalTime;

                                        if (remainingTime <= 0) {
                                            // Timer has expired, stop the countdown
                                            this.cancel();

                                            AtomicReference<StringBuilder> message = new AtomicReference<>(new StringBuilder());
                                            message.get().append(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&',
                                                            configuration.getString("question-tag")+" &r: "+question))
                                                    .append("\n")
                                                    .append(org.bukkit.ChatColor.translateAlternateColorCodes('&',
                                                            configuration.getString("answer-tag")+"\n"));

                                            for(String response : responses.keySet()){
                                                message.get()
                                                        .append("&r&a&l ")
                                                        .append(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', response))
                                                        .append(" : &r&b&l").append(responses.get(response)).append(responses.get(response) > 1 ? " Votes" : " Vote")
                                                        .append("\n&r");
                                            }

                                            PlaySound.playEndSound(Sound.ENTITY_PLAYER_LEVELUP);
                                            MessageBroadcast.broadcast(String.valueOf(message));

                                            voteData.addStatus(0, true);
                                            voteData.addStatus(1, false);
                                            voteData.saveStatusToFile("cpoll_data.json");

                                            return;
                                        }

                                        // Create the boss bar
                                        BossBar bossBar = Bukkit.createBossBar(org.bukkit.ChatColor.translateAlternateColorCodes('&',
                                                configuration.getString("boss-bar-title")+ " &r: "+question), BarColor.YELLOW, BarStyle.SOLID);
                                        bossBar.setProgress(progress);

                                        // Show the boss bar to the player
                                        for (Player playerInstance : Bukkit.getOnlinePlayers()) {
                                            bossBar.addPlayer(playerInstance);
                                        }
                                        // Remove the boss bar after a short delay
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                //after
                                                for (Player playerInstance : Bukkit.getOnlinePlayers()) {
                                                    bossBar.removePlayer(playerInstance);
                                                }
                                            }
                                        }.runTaskLater(CPoll.getInstance(), 20); // Change 'plugin' to your instance of JavaPlugin
                                    }
                                }.runTaskTimer(CPoll.getInstance(), 0, 20);

                                voteData.addStatus(0, false);
                                voteData.addStatus(1, true);
                                voteData.saveStatusToFile("cpoll_data.json");
                            }
                        } catch (NumberFormatException nfe) {
                            MessageComponent.send(sender, configuration.getString("delay-value-error"));
                            MessageComponent.send(sender, configuration.getString("usage-text"));
                            return true;
                        }
                    }
                }else {
                    MessageComponent.send(sender, configuration.getString("permission-missing"));
                }
            }
        }
        else if(command.getName().equals("cpollreload")){
            CPoll.getInstance().reloadConfig();
            MessageComponent.send(sender, configuration.getString("plugin-reload"));
        }
        return false;
    }
}
