package net.me.daemon.particles.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.me.daemon.Main;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static class ParticleData {
        public Vec3d position, velocity, gravity;
        public Identifier id;
        public int lifetime;
        public List<Identifier> textures = new ArrayList<>();
    }







    public static ParticleData loadParticleFromJSON(Identifier identifier) throws IOException {
        ParticleData data = new ParticleData();
        data.id = identifier;


        // Automatically build the JSON path in assets
        Identifier jsonIdentifier = Identifier.of(
                identifier.getNamespace(),
                "particles/" + identifier.getPath() + ".json"
        );


        try (InputStream stream = MinecraftClient.getInstance().getResourceManager().open(jsonIdentifier);
             InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
             JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

             // lifetime
            data.lifetime = json.get("lifetime").getAsInt();

            // textures
            JsonArray textures = json.get("textures").getAsJsonArray();
            for (JsonElement texture : textures) {
                data.textures.add(Identifier.of(identifier.getNamespace(), "textures/particle/" + texture.getAsString()));
            }

            // velocity
            data.velocity = getVector3d(json, "velocity");

            // position
            data.position = getVector3d(json, "position");

            // gravity
            data.gravity = getVector3d(json, "gravity");

        }

        return data;
    }


    private static Vec3d getVector3d(JsonObject json, String name) {
        JsonObject  Vector = json.get(name).getAsJsonObject();
        Vec3d vec3 = new Vec3d(
                Vector.get("x").getAsFloat(),
                Vector.get("y").getAsFloat(),
                Vector.get("z").getAsFloat()
        );
        return vec3;
    }
}
