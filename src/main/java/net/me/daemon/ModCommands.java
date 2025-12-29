package net.me.daemon;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.me.daemon.particles.server.ServerParticleHandeler;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class ModCommands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("daemon")
                    .executes(ModCommands::test)
            );
        });
    }

    private static int test(CommandContext<ServerCommandSource> context) {
        ServerWorld serverWorld = context.getSource().getWorld();
        ServerParticleHandeler.spawnParticle(serverWorld , Identifier.of(Main.MOD_ID, "smoke"), context.getSource().getPlayer().getPos());

            return 1;
    }

}