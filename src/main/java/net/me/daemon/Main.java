package net.me.daemon;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.me.daemon.particles.server.ServerParticleHandeler;
import net.me.daemon.particles.util.ParticlePayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.io.IOException;

public class Main implements ModInitializer {
    public static final String MOD_ID = "daemon";

    @Override
    public void onInitialize() {
        ServerWorldEvents.LOAD.register((minecraftServer, serverWorld) -> {
            ServerParticleHandeler.spawnParticle(Identifier.of(Main.MOD_ID, "smoke"), new Vec3d(0, 60 ,0));
        });
    }
}

