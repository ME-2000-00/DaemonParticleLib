package net.me.daemon.particles.api;

import net.me.daemon.particles.networking.ParticleData;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public final class ParticleTypeRegistry {

    private static final Map<Identifier, ParticleType<?>> TYPES = new HashMap<>();

    private ParticleTypeRegistry() {}

    public static <T extends ParticleData> void register(ParticleType<T> type) {
        Identifier id = type.id();

        if (TYPES.containsKey(id)) {
            throw new IllegalStateException(
                    "Duplicate particle type id: " + id
            );
        }

        TYPES.put(id, type);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ParticleData> ParticleType<T> get(Identifier id) {
        ParticleType<?> type = TYPES.get(id);

        if (type == null) {
            throw new IllegalStateException(
                    "Unknown particle type: " + id
            );
        }

        return (ParticleType<T>) type;
    }

    public static boolean contains(Identifier id) {
        return TYPES.containsKey(id);
    }
}
