package io.github.pouffy.agrestic.core.fluid.transfer;

import io.github.pouffy.agrestic.Agrestic;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public record FluidTransferSyncPacket(Set<Item> containerItems) implements CustomPacketPayload {
    public static final Type<FluidTransferSyncPacket> TYPE = new Type<>(Agrestic.location("fluid_container_transfer_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidTransferSyncPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public FluidTransferSyncPacket decode(RegistryFriendlyByteBuf buffer) {
            int size = buffer.readVarInt();
            List<Item> builder = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                builder.add(BuiltInRegistries.ITEM.get(buffer.readResourceLocation()));
            }
            return new FluidTransferSyncPacket(Set.copyOf(builder));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, FluidTransferSyncPacket packet) {
            buffer.writeVarInt(packet.containerItems().size());
            for (Item item : packet.containerItems()) {
                buffer.writeResourceLocation(BuiltInRegistries.ITEM.getKey(item));
            }
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        FluidContainerTransferManager.INSTANCE.setContainerItems(this.containerItems);
    }
}
