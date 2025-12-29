package net.me.daemon;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.me.daemon.particles.api.register.DaemonParticleRegistry;
import net.me.daemon.particles.api.register.DaemonShaderRegistry;
import net.me.daemon.particles.api.type.defaultparticles.SmokeParticleType;
import net.me.daemon.particles.client.ClientParticleHandeler;
import net.me.daemon.particles.util.ClientPayloadReg;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

import java.io.IOException;

public class MainClient implements ClientModInitializer {
    SmokeParticleType smoke = null;

    @Override
    public void onInitializeClient() {
        ClientPayloadReg.register();
        ClientParticleHandeler.register();
        DaemonShaderRegistry.init();

        // Create your particle type (concrete subclass)
        ClientTickEvents.START_CLIENT_TICK.register(client -> {

            try {
                // particle types
                smoke = new SmokeParticleType(Identifier.of(Main.MOD_ID, "smoke"));

                DaemonParticleRegistry.register(smoke);

                // shaders
                DaemonShaderRegistry.register(
                        Identifier.of(Main.MOD_ID, "smoke"),
                        VertexFormats.POSITION_TEXTURE_COLOR
                );

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

