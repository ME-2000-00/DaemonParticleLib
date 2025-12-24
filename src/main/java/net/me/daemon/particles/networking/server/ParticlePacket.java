package net.me.daemon.particles.networking.server;

import net.me.daemon.Main;
import net.me.daemon.particles.api.ParticleType;
import net.me.daemon.particles.api.ParticleTypeRegistry;
import net.me.daemon.particles.networking.ParticleData;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public record ParticlePacket(
        Identifier typeId,
        Vec3d position,
        ParticleData data
) implements CustomPayload {

    public static final Id<ParticlePacket> ID =
            new CustomPayload.Id<>(Identifier.of(Main.MOD_ID, "particle_packet"));

    // PacketCodec for CustomPayload registration
    public static final PacketCodec<PacketByteBuf, ParticlePacket> CODEC =
            PacketCodec.of(ParticlePacket::write, ParticlePacket::read);

    // -------------------------
    // Write packet to buffer
    // -------------------------
    private static void write(ParticlePacket packet, PacketByteBuf buf) {
        // 1️⃣ Write type identifier
        buf.writeIdentifier(packet.typeId());

        // 2️⃣ Write position
        buf.writeDouble(packet.position().x);
        buf.writeDouble(packet.position().y);
        buf.writeDouble(packet.position().z);

        // 3️⃣ Write particle data using the ParticleType's codec
        ParticleType<ParticleData> type = (ParticleType<ParticleData>) ParticleTypeRegistry.get(packet.typeId());
        type.codec().encode(buf, packet.data());
    }

    // -------------------------
    // Read packet from buffer
    // -------------------------
    private static ParticlePacket read(PacketByteBuf buf) {
        // 1️⃣ Read type identifier
        Identifier typeId = buf.readIdentifier();

        // 2️⃣ Read position
        Vec3d pos = new Vec3d(
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble()
        );

        // 3️⃣ Read particle data using the ParticleType's codec
        ParticleType<ParticleData> type = (ParticleType<ParticleData>) ParticleTypeRegistry.get(typeId);
        ParticleData data = type.codec().decode(buf);

        return new ParticlePacket(typeId, pos, data);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
