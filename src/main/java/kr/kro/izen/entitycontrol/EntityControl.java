package kr.kro.izen.entitycontrol;

import kr.kro.izen.entitycontrol.entity.EntityHandler;
import kr.kro.izen.entitycontrol.item.StaffController;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public final class EntityControl extends JavaPlugin {

    public static EntityControl plugin;

    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getPluginManager().registerEvents(new StaffController(), this);
    }

    @Override
    public void onDisable() {
        for (Entity entity : EntityHandler.vindicatorMap.values()) {
            entity.remove();
        }
    }
}
