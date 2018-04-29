package me.tomthedeveloper.buildbattle.stats;

import me.tomthedeveloper.buildbattle.User;
import me.tomthedeveloper.buildbattle.Main;
import me.tomthedeveloper.buildbattle.handlers.ConfigurationManager;
import me.tomthedeveloper.buildbattle.handlers.UserManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

/**
 * Created by Tom on 17/06/2015.
 */
public class FileStats {

    private FileConfiguration config;

    public FileStats() {
        config = ConfigurationManager.getConfig("STATS");
    }


    public void saveStat(Player player, String stat) {
        User user = UserManager.getUser(player.getUniqueId());
        config.set(player.getUniqueId().toString() + "." + stat, user.getInt(stat));
        try {
            config.save(ConfigurationManager.getFile("STATS"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void loadStat(Player player, String stat) {
        User user = UserManager.getUser(player.getUniqueId());
        if(config.contains(player.getUniqueId().toString() + "." + stat))
            user.setInt(stat, config.getInt(player.getUniqueId().toString() + "." + stat));
        else user.setInt(stat, 0);
    }


}