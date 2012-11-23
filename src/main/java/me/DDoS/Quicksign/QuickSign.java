package me.DDoS.Quicksign;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;
import java.util.HashMap;
import java.util.Map;
import me.DDoS.Quicksign.permission.Permissions;
import me.DDoS.Quicksign.permission.PermissionsHandler;
import me.DDoS.Quicksign.permission.Permission;
import me.DDoS.Quicksign.session.EditSession;
import me.DDoS.Quicksign.session.StandardEditSession;
import me.DDoS.Quicksign.sign.SignGenerator;
import me.DDoS.Quicksign.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author DDoS 
 */
public class QuickSign extends JavaPlugin {
	
	public QSUtil qs = new QSUtil(this);
    //
    private Permissions permissions;
    //
    private final SignGenerator signGenerator = new SignGenerator(this);
    //
    private final Map<Player, EditSession> sessions = new HashMap<Player, EditSession>();
    //
    private Consumer consumer;
    //
    private final BlackList blackList = new BlackList(this);

    @Override
    public void onEnable() {

        permissions = new PermissionsHandler(this).getPermissions();
        
        getServer().getPluginManager().registerEvents(new QSListener(this), this);
        getServer().getPluginCommand("qs").setExecutor(this);

        checkForWorldGuard();
        checkForLogBlock();

        new QSConfig().setupConfig(this);
        
        logi("[QuickSign] Plugin enabled. v" + getDescription().getVersion() + ", by DDoS");

    }

    @Override
    public void onDisable() {

    	sessions.clear();
        logi("[QuickSign] Plugin disabled. v" + getDescription().getVersion() + ", by DDoS");

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (cmd.getName().equalsIgnoreCase("qs")
                && hasPermissions(sender, Permission.USE)) {

            if (args.length == 0) {

                qs.showHelp(sender);
                return true;

            }
            
            if (args.length >= 1 && args[0].equalsIgnoreCase("help")) {
            	
            	qs.showHelp(sender);
            	return true;
            	
            }

            if (args.length >= 1 && args[0].equalsIgnoreCase("rc")) {

                if (hasPermissions(sender, Permission.RC)) {

                    new QSConfig().setupConfig(this);
                    QSUtil.tell(sender, "Configuration reloaded.");
                    return true;

                } else {

                    QSUtil.tell(sender, ChatColor.RED + "You don't have permission for this command.");
                    return true;

                }
            }
            
            if (args.length >= 1 && args[0].equalsIgnoreCase("colors")) {
            	qs.showColors(sender);
            	return true;
            }
            
            if (!(sender instanceof Player)) {

                QSUtil.tell(sender, "This command can only be used in-game.");
                return true;

            }

            Player player = (Player) sender;
            if(!isUsing(player)){
            	sessions.put(player, new StandardEditSession(player, this));
            }

            if (args.length >= 3 && args[0].equalsIgnoreCase("fs")) {

                if (hasPermissions(player, Permission.FS)) {

                    signGenerator.createSign(player, args[1], QSUtil.mergeToString(args, 2));
                    return true;

                } else {

                    player.sendMessage(ChatColor.RED + "You don't have permission for this command.");
                    return true;

                }
            } else {

                if (!getSession(player).handleCommand(args)) {

                    QSUtil.tell(player, "Your command was not recognized or is invalid.");

                }

                return true;

            }
        }

        QSUtil.tell(sender, ChatColor.RED + "You don't have permission for this command.");
        return true;

    }

    public boolean isUsing(Player player) {
		return sessions.containsKey(player);
	}

	public EditSession getSession(Player player) {
		return sessions.get(player);
	}
	
	public void removeSession(Player player) {
        sessions.remove(player);
    }

	public BlackList getBlackList() {

        return blackList;

    }

    public Consumer getConsumer() {

        return consumer;

    }

    public void setConsumer(Consumer consumer) {

        this.consumer = consumer;

    }

    private void checkForWorldGuard() {

        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin != null && plugin instanceof WorldGuardPlugin) {

            logi("[QuickSign] WorldGuard detected. Features enabled.");

        } else {

            logi("[QuickSign] No WorldGuard detected. Features disabled.");

        }
    }

    private void checkForLogBlock() {

        PluginManager pm = getServer().getPluginManager();
        Plugin plugin = pm.getPlugin("LogBlock");

        if (plugin != null && plugin instanceof LogBlock) {

            logi("[QuickSign] LogBlock detected. Features enabled.");
            consumer = ((LogBlock) plugin).getConsumer();
            return;

        } else {

            logi("[QuickSign] No LogBlock detected. Features disabled.");

        }
    }

    public boolean hasPermissions(CommandSender sender, Permission permission) {
    	if(sender instanceof Player){
    		Player player = (Player) sender;
    		if(permissions.hasPermission(player, Permission.MASTER.getNodeString()))
    			return true;
    		else
    			return permissions.hasPermission(player, permission.getNodeString());
    	} else
    		return true;
    }
    
    public void logi(String log) {
    	getLogger().info(log);
    }

	public WorldGuardPlugin getWorldGuard() {
		Plugin wg = Bukkit.getPluginManager().getPlugin("WorldGuard");

		if (wg == null || !(wg instanceof WorldGuardPlugin)) {
			return null; 
		}
		return (WorldGuardPlugin) wg;
	}
}