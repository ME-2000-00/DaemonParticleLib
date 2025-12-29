package net.me.daemon.particles.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.me.daemon.particles.api.register.DaemonParticleRegistry;
import net.me.daemon.particles.api.type.DaemonParticleType;
import net.me.daemon.particles.util.ParticlePayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class ClientParticleHandeler {

    // TODO: FIX, ITS ALWAYS EMPTY FOR SOME REASON
    // line 44 might be the issue, if not then look at server side
    private static List<DaemonParticleType> PARTICLES = new ArrayList<>();


    public static void register() {
        WorldRenderEvents.END.register(worldRenderContext -> {
            // TODO: there is an issue where there are to may particles even tho only one was added
//            System.out.println("[CLIENT]: particle array size: " + PARTICLES.size());

            if (!PARTICLES.isEmpty()) {
                // iterating through the client particles
                PARTICLES.forEach(daemonParticleType -> {
                    daemonParticleType.TrueUpdate(worldRenderContext);
                    daemonParticleType.TrueRender(worldRenderContext);
                });
                // deleting if markedForDeath
                PARTICLES.removeIf(DaemonParticleType::markedForDeath);
            }

        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ClientPlayNetworking.registerReceiver(ParticlePayload.ID, ((particlePayload, context) -> {
                context.client().execute(() -> {
                    // should be safe for now,
                    // in earlier version had issue that particle id was null and made client crash

//                    System.out.println("[CLIENT]: recv particle data");
                    addParticle(particlePayload.id(), particlePayload.position());
                });
            }));
        });
    }






    public static void addParticle(Identifier identifier, Vec3d position) {
        DaemonParticleType particleType = DaemonParticleRegistry.get(identifier);
        particleType.setPosition(position.add(particleType.position));
//        System.out.println("[CLIENT]: added particle: ");
//        System.out.println("[CLIENT]: " + particleType.id.getPath() + "\n");
//        System.out.println("[CLIENT]: " + particleType.position.x + "\n");
//        System.out.println("[CLIENT]: " + particleType.position.y + "\n");
//        System.out.println("[CLIENT]: " + particleType.position.z + "\n");


        PARTICLES.add(particleType);
    }

































}
