package uhc;

import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.Player;
import org.bukkit.attribute.Attribute;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
    private static Main instance;
    private static NamespacedKey key;
    
    public void onEnable() {
        Main.instance = this;
        Main.key = new NamespacedKey((Plugin)Main.instance, "key");
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
    }
    
    public void onDisable() {
    }
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final boolean hasPlayed = player.hasPlayedBefore();
        if (hasPlayed) {
            player.setHealth((double)player.getPersistentDataContainer().get(Main.key, PersistentDataType.DOUBLE));
        }
        else {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40.0); // change these two values to maximum desired health, vanilla is 20.0
            player.setHealth(40.0); // this one too
        }
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        player.getPersistentDataContainer().set(Main.key, PersistentDataType.DOUBLE, player.getHealth());
    }
    
    @EventHandler (priority = EventPriority.HIGH)
    public void onDeath(final PlayerDeathEvent event) throws InterruptedException {
        final Player player = event.getEntity();
        player.spigot().respawn();
        this.getServer().dispatchCommand((CommandSender)this.getServer().getConsoleSender(), "tempban " + player.getUniqueId() + " 32h"); // change tempban time, or replace tempban with "ban" and remove the last part
    }
}