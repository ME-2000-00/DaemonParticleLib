package net.me.daemon.particles.util;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ClientPayloadReg {
    public static void register() {
        PayloadTypeRegistry.playS2C().register(ParticlePayload.ID, ParticlePayload.CODEC);

    }
}
