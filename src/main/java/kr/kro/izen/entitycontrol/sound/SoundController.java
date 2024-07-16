package kr.kro.izen.entitycontrol.sound;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundController {

    private final ProtocolManager protocolManager;

    public SoundController() {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public void playSound(Player player, Location location, float volume) {

        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.NAMED_SOUND_EFFECT);

        packet.getSoundCategories()
                .write(0, EnumWrappers.SoundCategory.MASTER);

        packet.getSoundEffects()
                .write(0, Sound.ENTITY_LIGHTNING_BOLT_THUNDER);

        packet.getIntegers()
                .write(0, location.getBlockX() * 8)
                .write(1, location.getBlockY() * 8)
                .write(2, location.getBlockZ() * 8);

        packet.getFloat()
                .write(0, volume)
                .write(1, 1F);

        packet.getLongs()
                .write(0, 0L);

        protocolManager.sendServerPacket(player, packet);
    }
}
