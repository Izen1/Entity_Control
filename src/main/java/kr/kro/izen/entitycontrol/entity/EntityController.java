package kr.kro.izen.entitycontrol.entity;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public interface EntityController {
    void summonVindicator(Location location);
    void move(Location location, double speed);
    void attack(LivingEntity target);
    void surround(LivingEntity target);
}
