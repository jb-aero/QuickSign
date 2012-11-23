package me.DDoS.Quicksign.command;

import me.DDoS.Quicksign.QuickSign;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public abstract class QSCommand {

    protected final QuickSign plugin;
    protected final Sign sign;

    public QSCommand(QuickSign plugin, Sign sign) {

        this.plugin = plugin;
        this.sign = sign;

    }

    public abstract boolean run(Player player);

    public abstract void undo(Player player);

    public abstract void redo(Player player);

    protected void logChange(Player player, Sign sign) {

        if (plugin.getConsumer() != null) {

            plugin.getConsumer().queueSignPlace(player.getName(), sign);

        }
    }
}
