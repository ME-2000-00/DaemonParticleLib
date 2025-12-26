package net.me.daemon.particles.api.register;

import net.me.daemon.particles.api.type.DaemonParticleType;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DaemonParticleRegistry {

    private static final Map<Identifier ,DaemonParticleType> TYPES = new HashMap<>();

    /// this will return the identifier of the particle for later usage
    /// needs to be called on server and client
    public static void register(DaemonParticleType type) throws IOException {
        TYPES.put(type.id, type);
    }

    public static DaemonParticleType get(Identifier identifier) {
        return TYPES.get(identifier);
    }
}
