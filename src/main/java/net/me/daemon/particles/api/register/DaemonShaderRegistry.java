package net.me.daemon.particles.api.register;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.me.daemon.Main;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class DaemonShaderRegistry {

    private static final Map<Identifier, VertexFormat> DEFINITIONS = new HashMap<>();
    private static final Map<Identifier, ShaderProgram> SHADERS = new HashMap<>();


    public static void register(Identifier identifier, VertexFormat format) {
        DEFINITIONS.put(identifier, format);
    }

    public static void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
                .registerReloadListener(new SimpleSynchronousResourceReloadListener() {

                    @Override
                    public Identifier getFabricId() {
                        return Identifier.of(Main.MOD_ID, "shaders");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        SHADERS.clear();

                        DEFINITIONS.forEach((id, format) -> {
                            try {
                                ShaderProgram program = new ShaderProgram(
                                        MinecraftClient.getInstance().getResourceManager(),
                                        id.getPath(),
                                        format
                                );
                                SHADERS.put(id, program);
                            } catch (Exception e) {
                                System.err.println("Failed to load shader: " + id);
                                e.printStackTrace();
                            }
                        });
                    }
                });
    }

    public static ShaderProgram get(Identifier identifier) {
        return SHADERS.get(identifier);
    }






































}
