package kr.kro.izen.entitycontrol.entity;

import kr.kro.izen.entitycontrol.EntityControl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Frog;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vindicator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityHandler implements EntityController{
    private UUID summonedVindicatorUUID;
    public boolean isSurround;
    public static Map<UUID, Vindicator> vindicatorMap = new HashMap<>();

    public EntityHandler() {
    }

    @Override
    public void summonVindicator(Location location) {
        Bukkit.getScheduler().runTaskLater(EntityControl.plugin, task -> {
            World world = location.getWorld();
            Vindicator vindicator = world.spawn(location, Vindicator.class);
            this.summonedVindicatorUUID = vindicator.getUniqueId();
            vindicatorMap.put(summonedVindicatorUUID, vindicator);
        }, 3 * 20L);
    }

    public UUID getSummonedFrogUUID() {
        return summonedVindicatorUUID;
    }

    @Override
    public void move(Location location, double speed) {
        setIsSurround(false);
        if (vindicatorMap.isEmpty()) return;
        for (Vindicator vindicator : vindicatorMap.values()) {
            vindicator.setAI(true);
            vindicator.setTarget(null);
            Bukkit.getScheduler().runTaskTimer(EntityControl.plugin, task -> {
                vindicator.getPathfinder().moveTo(location, speed);
                if (vindicator.getLocation().distance(location) < 1.5) {
                    vindicator.setAI(false);
                    task.cancel();
                }
            }, 0, 1);
        }
    }

    @Override
    public void attack(LivingEntity target) {
        setIsSurround(false);
        if (vindicatorMap.isEmpty()) return;
        for (Vindicator vindicator : vindicatorMap.values()) {
            vindicator.setAI(true);
            vindicator.setTarget(target);
        }
    }

    @Override
    public void surround(LivingEntity target) {
        if (vindicatorMap.isEmpty()) return;

        setIsSurround(true);
        int radius = 5;

        Bukkit.getScheduler().runTaskTimer(EntityControl.plugin, task -> {
            if (!getIsSurround()) {
                task.cancel();
                return;
            }

            Location center = target.getLocation();
            int i = 0;
            for (Vindicator vindicator : vindicatorMap.values()) {
                if (vindicator == null) return;

                double angle = 360.0 / vindicatorMap.size();
                double theta = i * angle;
                double x = center.getX() + radius * Math.cos(Math.toRadians(theta));
                double z = center.getZ() + radius * Math.sin(Math.toRadians(theta));
                Location loc = new Location(center.getWorld(), x, center.getY(), z);

                vindicator.setAI(true);
                vindicator.getPathfinder().moveTo(loc, 1.4);
                vindicator.lookAt(target);

                i++;
            }
        }, 0, 5);
    }

    public void setIsSurround(boolean b) {
        isSurround = b;
    }

    public boolean getIsSurround() {
        return isSurround;
    }
}
