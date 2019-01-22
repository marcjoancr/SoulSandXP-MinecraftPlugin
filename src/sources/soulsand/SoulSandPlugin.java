package soulsand;

import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author Marc Joan (Twaia), contact: marcjoancr@gmail.com
 */

public class SoulSandPlugin extends JavaPlugin {

    public HashMap<Location, Integer> xp_blocks = new HashMap<Location, Integer>();

    @Override
    public void onDisable() {
        int count = 0;
        for (final Map.Entry<Location, Integer> entry : xp_blocks.entrySet()) {
            getConfig().set("SoulSands." + count + ".location", entry.getKey());
            getConfig().set("SoulSands." + count + ".exp", entry.getValue());
            count++;
        }
        saveConfig();
        getLogger().info("Good Bye World!");
    }

    @Override
    public void onEnable() {

        getLogger().info("Hello World!");

        saveDefaultConfig();
        if (getConfig().contains("SoulSands")) {
            for (final String loop : getConfig().getConfigurationSection("SoulSands").getKeys(false)) {
                final Location location = (Location) getConfig().get("SoulSands." + loop + ".location");
                final int exp = getConfig().getInt("SoulSands." + loop + ".exp");

                xp_blocks.put(location, exp);
            }
            getConfig().set("SoulSands", null);
            saveConfig();
        }

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);

    }

}

