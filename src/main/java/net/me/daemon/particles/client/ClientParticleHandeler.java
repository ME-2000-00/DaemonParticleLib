package net.me.daemon.particles.client;

import com.sun.jna.platform.win32.OaIdl;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.me.daemon.particles.api.register.DaemonParticleRegistry;
import net.me.daemon.particles.api.type.DaemonParticleType;
import net.me.daemon.particles.util.ParticlePayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientParticleHandeler {

    // TODO: FIX, ITS ALWAYS EMPTY FOR SOME REASON
    // line 44 might be the issue, if not then look at server side
    private static List<DaemonParticleType> PARTICLES = new ArrayList<>();


    public static void register() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(worldRenderContext -> {
            // TODO: there is an issue where there are to may particles even tho only one was added
            System.out.println("[CLIENT]: particle array size: " + PARTICLES.size());

            PARTICLES.forEach(daemonParticleType -> {

                daemonParticleType.tick(worldRenderContext);
                daemonParticleType.render(worldRenderContext);
            });
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ClientPlayNetworking.registerReceiver(ParticlePayload.ID, ((particlePayload, context) -> {
                context.client().execute(() -> {
                    // TODO: IF ONLY TEMPORARY NEEDS A PROPER FIX IN THE FUTURE
                    // if statement prevents particletypes that are null from being used since the server sends a payload before the json has been loaded

                    System.out.println("[CLIENT]: recv particle data");
                    addParticle(particlePayload.id(), particlePayload.position());
                });
            }));
        });
    }






    public static void addParticle(Identifier identifier, Vec3d position) {
        DaemonParticleType particleType = DaemonParticleRegistry.get(identifier);
        particleType.setPosition(position);
        System.out.println("[CLIENT]: added particle: ");
        System.out.println("[CLIENT]: " + particleType.id.getPath() + "\n");
        System.out.println("[CLIENT]: " + particleType.position.x + "\n");
        System.out.println("[CLIENT]: " + particleType.position.y + "\n");
        System.out.println("[CLIENT]: " + particleType.position.z + "\n");


        PARTICLES.add(particleType);
    }
}
