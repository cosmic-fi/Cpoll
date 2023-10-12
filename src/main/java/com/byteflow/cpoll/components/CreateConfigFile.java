package com.byteflow.cpoll.components;

import com.byteflow.cpoll.CPoll;
import org.bukkit.configuration.file.FileConfiguration;

public class CreateConfigFile {
    public static void setDefaultValues(FileConfiguration configuration){
        configuration.set("boss-bar-title", "&3[ &6&lCPOLL &3]");
        configuration.set("command-sender-error", "&3[&6&lCPOLL&3] &r&f- &eOnly players can use this command.");
        configuration.set("invalid-command-error", "&3[&6&lCPOLL&3] &r&f- &eInvalid command format");
        configuration.set("usage-text", "&e[Usage] &f: /poll <question> {choice} {choice} ... delay(seconds)");
        configuration.set("delay-value-error", "&3[&6&lCPOLL&3] &r&f- &ePoll delay value was not provided!");
        configuration.set("max-delay-value-message", "&3[&6&lCPOLL&3] &r&f- &eMax delay timer is 60 (seconds).");
        configuration.set("one-time-vote-message", "&3[&6&lCPOLL&3] &r&f- &eSorry but you can only vote once :D");
        configuration.set("plugin-reload", "&3[&6&lCPOLL&3] &r&f- &eConfig file reloaded successfully!");
        configuration.set("permission-missing", "&3[&6&lCPOLL&3] &r&f- &e&lYou don't have the necessary permission to use this command!");
        configuration.set("vote-success-message", "&3[&6&lCPOLL&3] &r&f- &aYou have voted successfully, sit back and wait for the results :D.");
        configuration.set("choice-error-message", "&3[&6&lCPOLL&3] &r&f- &eYou have enter and invalid choice, please try again. &f&o(Answers are case sensitive) \n&e[Usage] &f: /poll /answer");
        configuration.set("vote-party-end-message", "&3[&6&lCPOLL&3] &r&f- &eWhopsy! Poll party has ended, maybe wait again next time :)");
        configuration.set("question-tag", "&5&l[Question]");
        configuration.set("answer-tag", "&e&l( Answers )");
        CPoll.getInstance().saveConfig();
    }
}
