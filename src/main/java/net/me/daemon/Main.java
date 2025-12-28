package net.me.daemon;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.me.daemon.particles.server.ServerParticleHandeler;
import net.me.daemon.particles.util.ParticlePayload;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.io.IOException;

public class Main implements ModInitializer {
    public static final String MOD_ID = "daemon";

    @Override
    public void onInitialize() {
        ModCommands.register();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerWorld serverWorld = handler.player.getServerWorld();
            ServerParticleHandeler.spawnParticle(serverWorld ,Identifier.of(Main.MOD_ID, "smoke"), new Vec3d(0, 60 ,0));
        });

    }
}

