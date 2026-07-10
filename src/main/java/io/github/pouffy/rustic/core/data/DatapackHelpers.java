package io.github.pouffy.rustic.core.data;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class DatapackHelpers {

    private static void sendPackets(ServerPlayer player, CustomPacketPayload[] packets) {
        if (!player.connection.getConnection().isMemoryConnection()) {
            for (CustomPacketPayload packet : packets) {
                PacketDistributor.sendToPlayer(player, packet);
            }
        }
    }

    public static void syncPackets(OnDatapackSyncEvent event, CustomPacketPayload... packets) {
        // send to single player
        ServerPlayer targetedPlayer = event.getPlayer();
        if (targetedPlayer != null) {
            sendPackets(targetedPlayer, packets);
        } else {
            // send to all players
            for (ServerPlayer player : event.getPlayerList().getPlayers()) {
                sendPackets(player, packets);
            }
        }
    }
}
