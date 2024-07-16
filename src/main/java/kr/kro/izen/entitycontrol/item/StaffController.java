package kr.kro.izen.entitycontrol.item;

import kr.kro.izen.entitycontrol.effect.SummonMagic;
import kr.kro.izen.entitycontrol.entity.EntityHandler;
import kr.kro.izen.entitycontrol.sound.SoundController;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

public class StaffController implements Listener {
    private final EntityHandler entityHandler;
    private final SoundController soundController;
    private final SummonMagic summonMagic;

    public StaffController() {
        this.entityHandler = new EntityHandler();
        this.soundController = new SoundController();
        this.summonMagic = new SummonMagic();
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        LivingEntity target = (LivingEntity) player.getTargetEntity(30);

        Location loc = getTargetBlock(player).getLocation().add(0, 1, 0);
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        Action action = event.getAction();

        if (mainHand.getItemMeta() == null && mainHand.getItemMeta().displayName() == null) return;
        if (!mainHand.getItemMeta().getDisplayName().equalsIgnoreCase("§f스태프")) return;
        if (player.isSneaking()) {
            if (action.isRightClick()) {
                summonMagic.applyEffect(loc);
                entityHandler.summonVindicator(loc);
                soundController.playSound(player, loc, 1);
            }
            else if (action.isLeftClick()) {
                entityHandler.surround(target);
            }
        }
        else {
            if (action.isRightClick()) {
                entityHandler.move(loc, 1.4);
            } else if (action.isLeftClick()) {
                entityHandler.attack(target);
            }
        }
    }

    @EventHandler
    public void entityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType() != EntityType.VINDICATOR) return;
        EntityHandler.vindicatorMap.remove(entity.getUniqueId());
    }

    private Block getTargetBlock(Player player) {
        BlockIterator iter = new BlockIterator(player, 30);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() != Material.AIR) {
                break;
            }
        }
        return lastBlock.getType() == Material.AIR ? null : lastBlock;
    }
}
