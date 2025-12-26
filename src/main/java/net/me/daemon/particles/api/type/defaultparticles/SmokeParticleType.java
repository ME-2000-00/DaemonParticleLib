package net.me.daemon.particles.api.type.defaultparticles;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.me.daemon.Main;
import net.me.daemon.particles.api.type.DaemonParticleType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.io.IOException;

public class SmokeParticleType extends DaemonParticleType {

    public SmokeParticleType(Identifier id) throws IOException {
        super(id);
    }

    @Override
    public void tick(WorldRenderContext context) {
//            update_uniforms(worldRenderContext, Identifier.of(Main.MOD_ID, "smoke"));
        matrixStack = context.matrixStack();
        matrixStack.push();
    }

    @Override
    public void render(WorldRenderContext context) {
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder buffer = tes.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        // billboarding
        // matrixStack.multiply(context.camera().getRotation());

        Matrix4f mat = matrixStack.peek().getPositionMatrix();
        Vec3d camPos = context.camera().getPos();

        // TODO: find out why fps drop a lot when your too close to the particle
        double distSq = camPos.squaredDistanceTo(position);

        if (distSq < 0.25) return; // too close (0.5 blocks)
        if (distSq > 256) return;  // too far (16 blocks)

        mat.translate(
                (float) (position.x - camPos.x) + 0.5f,
                (float) (position.y - camPos.y) + 0.5f,
                (float) (position.z - camPos.z) + 0.5f
        );

        Vec3d topLeft     = new Vec3d(-0.5,  0.5, 0);
        Vec3d topRight    = new Vec3d( 0.5,  0.5, 0);
        Vec3d bottomRight = new Vec3d( 0.5, -0.5, 0);
        Vec3d bottomLeft  = new Vec3d(-0.5, -0.5, 0);

        buffer.vertex(mat, (float) topLeft.x, (float) topLeft.y, (float) topLeft.z).color(255,0,0,255);
        buffer.vertex(mat, (float) topRight.x, (float) topRight.y, (float) topRight.z).color(255,0,0,255);
        buffer.vertex(mat, (float) bottomRight.x, (float) bottomRight.y, (float) bottomRight.z).color(255,0,0,255);
        buffer.vertex(mat, (float) bottomLeft.x, (float) bottomLeft.y, (float) bottomLeft.z).color(255,0,0,255);


        // We'll get to this bit in the next section.
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        matrixStack.pop();

    }
}
