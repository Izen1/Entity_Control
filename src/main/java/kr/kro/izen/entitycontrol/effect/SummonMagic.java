package kr.kro.izen.entitycontrol.effect;

import com.google.common.util.concurrent.AtomicDouble;
import dev.lone.itemsadder.api.CustomStack;
import kr.kro.izen.entitycontrol.EntityControl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;

import java.util.HashSet;
import java.util.Set;

public class SummonMagic {

    private final CustomStack magicCircle = CustomStack.getInstance("summoner:magic_circle");
    private ItemDisplay itemDisplay;
    private Set<ItemDisplay> displaySet = new HashSet<>();

    public void applyEffect(Location location) {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        itemStack.editMeta(meta -> {
            meta.setCustomModelData(4004);
        });
        World world = location.getWorld();
        location.setYaw(90);
        location.setPitch(-90);

        AtomicDouble offset = new AtomicDouble();
        itemDisplay = world.spawn(location, ItemDisplay.class, display -> {
            displaySet.add(display);
            display.setItemStack(new ItemStack(itemStack));
            Transformation transformation = display.getTransformation();
            transformation.getScale().set(5F);


            Bukkit.getScheduler().runTaskTimer(EntityControl.plugin, task -> {
                if (!itemDisplay.isValid()) {
                    task.cancel();
                    return;
                }

                double angle = offset.getAndAdd(15);
                float radian = (float) Math.toRadians(angle);

                transformation.getLeftRotation().setAngleAxis(radian, 0,0, 1);

                display.setTransformation(transformation);
            }, 0L, 1L);
        });

        Bukkit.getScheduler().runTaskLater(EntityControl.plugin, this::removeMagic , 5 * 20L);
    }

    public void removeMagic() {
        if (displaySet.isEmpty()) return;
        for (ItemDisplay display : displaySet) {
            display.remove();
        }
        displaySet.clear();
    }
}
