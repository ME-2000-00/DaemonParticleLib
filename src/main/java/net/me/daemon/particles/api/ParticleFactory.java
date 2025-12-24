package net.me.daemon.particles.api;

import net.me.daemon.particles.client.ClientParticle;
import net.me.daemon.particles.networking.ParticleData;
import net.minecraft.util.math.Vec3d;

public interface ParticleFactory<T extends ParticleData> {

    ClientParticle<T> create(Vec3d pos, T data);

}
