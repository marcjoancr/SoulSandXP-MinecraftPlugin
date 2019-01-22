package soulsand;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

public class PlayerListener implements Listener {

    private final SoulSandPlugin plugin;

    PlayerListener(final SoulSandPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        int xp = player.getLevel();
        if (event.getClickedBlock() == null) return;
        Block block = event.getClickedBlock();
        if (block.getType() != Material.SOUL_SAND) return;
        if (!player.isSneaking()) return;
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (player.getLevel() <= 0) return;
            if (plugin.xp_blocks.containsKey(block.getLocation())) {
                xp += plugin.xp_blocks.get(block.getLocation());
            }
            player.sendMessage(ChatColor.YELLOW + "You have stored " + player.getLevel() + ". Now the block has " + LevelCommission(xp) + " levels.");
            plugin.xp_blocks.put(block.getLocation(), xp);
            player.setLevel(0);
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!plugin.xp_blocks.containsKey(block.getLocation())) return;
            xp = plugin.xp_blocks.get(block.getLocation());
            if (event.getHand() != EquipmentSlot.HAND) return;
            player.sendMessage(ChatColor.YELLOW + "This block has " + LevelCommission(xp) + " levels.");
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (block.getType() == Material.SOUL_SAND) {
            if (plugin.xp_blocks.containsKey(block.getLocation())) {
                int xp = plugin.xp_blocks.get(block.getLocation());
                event.setExpToDrop(ExtraXP(xp));
                xp = LevelCommission(xp);
                player.setLevel(xp + player.getLevel());
                player.sendMessage(ChatColor.GOLD + "You earned " + xp + " levels.");
                event.setDropItems(false);
                plugin.xp_blocks.remove(block.getLocation());
            }
        }
    }

    private int LevelCommission(int level) {
        if (level <= 10) {
            return (int) (level*0.80);
        } else if (level <= 30) {
            return (int) (level*0.85);
        } else {
            return (int) (level*0.90);
        }
    }

    private int ExtraXP(int xp) {
        if (xp <= 10) {
            return (int) (xp*0.20);
        } else if (xp <= 30) {
            return (int) (xp*0.25);
        } else {
            return (int) (xp*0.30);
        }
    }

}
