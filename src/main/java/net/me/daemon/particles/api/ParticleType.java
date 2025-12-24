package net.me.daemon.particles.api;

import io.netty.buffer.ByteBuf;
import net.me.daemon.particles.networking.ParticleData;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Identifier;

public interface ParticleType<T extends ParticleData> {

    Identifier id();

    PacketCodec<ByteBuf, T> codec();
}
