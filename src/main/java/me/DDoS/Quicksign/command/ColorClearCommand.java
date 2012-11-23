package me.DDoS.Quicksign.command;

import me.DDoS.Quicksign.QuickSign;
import me.DDoS.Quicksign.util.QSUtil;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class ColorClearCommand extends QSCommand {

    private final int line;
    private final String[] backups;

    public ColorClearCommand(QuickSign plugin, Sign sign, int line) {

        super (plugin, sign);
        this.line = line;
        backups = new String[1];

    }

    @Override
    public boolean run(Player player) {

        if (line < 0 || line > 3) {

            QSUtil.tell(player, "Invalid line.");
            return false;

        }

        int i = 0;
        
//        for (Sign sign : signs) {

            backups[i] = sign.getLine(line);
            sign.setLine(line, ChatColor.stripColor(sign.getLine(line)));
            sign.update();
            logChange(player, sign);
            i++;

//        }

        QSUtil.tell(player, "Edit successful.");
        return true;

    }

    @Override
    public void undo(Player player) {

        int i = 0;

//        for (Sign sign : signs) {

            sign.setLine(line, backups[i]);
            sign.update();
            logChange(player, sign);
            i++;

//        }
        
        QSUtil.tell(player, "Undo successful.");
        
    }

    @Override
    public void redo(Player player) {

//        for (Sign sign : signs) {

            sign.setLine(line, ChatColor.stripColor(sign.getLine(line)));
            sign.update();
            logChange(player, sign);

//        }

        QSUtil.tell(player, "Redo successful.");

    }
}