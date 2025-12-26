package net.me.daemon.particles.server;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.me.daemon.particles.util.ParticlePayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class ServerParticleHandeler {

    public static void spawnParticle(Identifier identifier, Vec3d position) {
        ServerTickEvents.START_WORLD_TICK.register(serverWorld -> {
            serverWorld.getPlayers().forEach(serverPlayerEntity -> {
                ServerPlayNetworking.send(serverPlayerEntity, new ParticlePayload(identifier, position));
            });
        });
    }
}
