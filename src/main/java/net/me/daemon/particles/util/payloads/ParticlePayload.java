package net.me.daemon.particles.util.payloads;

import net.me.daemon.Main;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public record ParticlePayload(Identifier particle, Vec3d position) implements CustomPayload {


    public static final Id<ParticlePayload> ID =
            new Id<>(Identifier.of(Main.MOD_ID, "particle_payload"));

    public static final PacketCodec<PacketByteBuf, ParticlePayload> CODEC =
            PacketCodec.of(
                    ParticlePayload::write,
                    ParticlePayload::read
            );

    private static ParticlePayload read(PacketByteBuf packetByteBuf) {
        Identifier particle = packetByteBuf.readIdentifier();
        Vec3d pos = packetByteBuf.readVec3d();
        return new ParticlePayload(particle, pos);
    }

    private static void write(ParticlePayload payload, PacketByteBuf buf) {
        buf.writeIdentifier(payload.particle());
        buf.writeVec3d(payload.position());
    }


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
