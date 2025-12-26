package net.me.daemon.particles.util;

import io.netty.buffer.ByteBuf;
import net.me.daemon.Main;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public record ParticlePayload(Identifier id, Vec3d position) implements CustomPayload {


    public static final Id<ParticlePayload> ID =
            new CustomPayload.Id<>(Identifier.of(Main.MOD_ID, "particle_payload"));

    public static final PacketCodec<PacketByteBuf, ParticlePayload> CODEC =
            PacketCodec.of(
                    ParticlePayload::write,
                    ParticlePayload::read
            );

    private static ParticlePayload read(PacketByteBuf packetByteBuf) {
        Identifier id = packetByteBuf.readIdentifier();
        Vec3d pos = packetByteBuf.readVec3d();
        return new ParticlePayload(id, pos);
    }

    private static void write(ParticlePayload payload, PacketByteBuf buf) {
        buf.writeIdentifier(payload.id());
        buf.writeVec3d(payload.position());
    }


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
