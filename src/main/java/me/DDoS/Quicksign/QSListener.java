package me.DDoS.Quicksign;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QSListener implements Listener {

	public QuickSign qs;
	public QSListener(QuickSign plugin){
		this.qs = plugin;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		org.bukkit.entity.Player player = event.getPlayer();
		if(qs.isUsing(player))
		    qs.removeSession(player);
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event) {
		org.bukkit.entity.Player player = event.getPlayer();
		if(qs.isUsing(player))
			qs.removeSession(player);
	}
}
