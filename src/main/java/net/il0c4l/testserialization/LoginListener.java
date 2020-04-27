package net.il0c4l.testserialization;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.util.UUID;

public class LoginListener implements Listener {

    private final Main plugin;

    public LoginListener(final Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        UUID uuid = e.getPlayer().getUniqueId();
        if(!plugin.entryExists(uuid)){
            plugin.getEntries().add(new Entry(uuid));
        }
    }
}
