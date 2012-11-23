package me.DDoS.Quicksign.util;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import me.DDoS.Quicksign.QuickSign;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author DDoS
 */
public class QSConfig {

    private final YamlConfiguration config = new YamlConfiguration();
    //
    public static boolean chatSigns;
    //
    public static int maxReach;
    //
    public static boolean useWG;
    public static boolean useLogBlock;

    public void setupConfig(QuickSign plugin) {

        File configDir = new File("plugins/QuickSign");

        if (!configDir.exists()) {

            configDir.mkdir();

        }

        File configFile = new File("plugins/QuickSign/config.yml");

        if (!configFile.exists()) {

            try {

                configFile.createNewFile();

            } catch (Exception e) {

            	plugin.logi("[QuickSign] Error when creating config file.");

            }
        }

        if (getConfig()) {

            checkConfig();
            loadData();
            convertProperties(plugin);
            return;

        } else {
            
            loadDefaults();
            convertProperties(plugin);
            
        }
        
        System.out.println("[QuickSign] Configuration loaded.");
        
    }

    private boolean getConfig() {

        try {

            config.load("plugins/QuickSign/config.yml");
            return true;

        } catch (Exception ex) {

        	System.out.println("[QuickSign] Couldn't load config: " + ex.getMessage());
            return false;

        }
    }

    private void checkConfig() {

        Set<String> keys = config.getKeys(false);

        if (!keys.contains("useWorldGuard")) {
            config.set("useWorldGuard", true);
        }
        
        if (!keys.contains("useLogBock")) {
            config.set("useLogBock", true);
        }
        
        if (!keys.contains("maxReach")) {
        	config.set("maxReach", 20);
        }

        if (!keys.contains("chatSigns")) {
            config.set("chatSigns", true);
        }
        
        try {
        
            config.save("plugins/QuickSign/config.yml");
        
        } catch (IOException ex) {

        	System.out.println("[QuickSign] Couldn't save config: " + ex.getMessage());

        }
    }

    private void loadData() {

        useWG = config.getBoolean("useWorldGuard", true);
        useLogBlock = config.getBoolean("useLogBock", true);
        maxReach = config.getInt("maxReach", 20);
        chatSigns = config.getBoolean("chatSigns", true);

    }
    
    private void loadDefaults() {
        
        useWG = true;
        useLogBlock = true;
        maxReach = 20;
        chatSigns = true;
        
        System.out.println("[QuickSign] Loaded defaults.");
        
    }
    
    private void convertProperties(QuickSign plugin) {

        if (!useWG) {
        	System.out.println("[QuickSign] WorldGuard support disabled by config.");
        }

        if (!useLogBlock) {
            plugin.setConsumer(null);
            System.out.println("[QuickSign] LogBlock support disabled by config.");
        }
    }
}