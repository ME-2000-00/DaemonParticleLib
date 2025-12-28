package net.me.daemon.particles.server;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.me.daemon.particles.util.ParticlePayload;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class ServerParticleHandeler implements ModInitializer{

    private static List<QueParticle> QUE = new ArrayList<>();

    public static void spawnParticle(ServerWorld serverWorld, Identifier identifier, Vec3d position) {

        if (serverWorld.getPlayers().isEmpty()) {
//            System.out.println("saving particle to que");
            QUE.add(new QueParticle(serverWorld, identifier, position));
            return;
        }

        serverWorld.getPlayers().forEach(serverPlayerEntity -> {
//            System.out.println("sending out particle to: " + serverPlayerEntity.getName());

            ServerPlayNetworking.send(serverPlayerEntity, new ParticlePayload(identifier, position));
        });
    }

    //TODO: que logic
    @Override
    public void onInitialize() {
        ServerPlayerEvents.JOIN.register((serverPlayerEntity -> {
//            System.out.println("sending out que to: " + serverPlayerEntity.getName());
//            System.out.println("que size: " + QUE.size());

            QUE.forEach(queParticle -> {
                ServerPlayNetworking.send(serverPlayerEntity, new ParticlePayload(queParticle.identifier, queParticle.position));
            });
        }));
    }

    // data class
    private record QueParticle(ServerWorld serverWorld, Identifier identifier, Vec3d position) {};
}
