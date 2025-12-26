package net.me.daemon.particles.api.register;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class DaemonShaderRegistry {

    private static final Map<Identifier , ShaderProgram> TYPES = new HashMap<>();

    /// needs to be called on server and client
    public static void register(Identifier identifier, VertexFormat format) {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
                .registerReloadListener(new SimpleSynchronousResourceReloadListener() {

                    @Override
                    public Identifier getFabricId() {
                        return identifier;
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        try {
                            ShaderProgram p = new ShaderProgram(
                                    MinecraftClient.getInstance().getResourceManager(),
                                    identifier.getPath(),
                                    format
                            );

                            TYPES.put(identifier, p);

                        } catch (Exception e) {
                            System.err.println("Could not load solar shader!");
                            e.printStackTrace();
                        }
                    }
                });
    }

    public static ShaderProgram get(Identifier identifier) {
        return TYPES.get(identifier);
    }
}
