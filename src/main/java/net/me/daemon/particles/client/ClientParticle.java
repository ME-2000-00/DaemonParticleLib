package net.me.daemon.particles.client;

import net.me.daemon.particles.networking.ParticleData;
import net.minecraft.util.math.Vec3d;

import java.awt.image.renderable.RenderContext;

public abstract class ClientParticle<T extends ParticleData> {

    protected Vec3d position;
    protected Vec3d velocity;
    protected int age;
    protected int lifetime;

    protected ClientParticle(Vec3d position) {
        this.position = position;
    }

    public abstract void tick();
    public abstract void render(RenderContext ctx, float tickDelta);
}
