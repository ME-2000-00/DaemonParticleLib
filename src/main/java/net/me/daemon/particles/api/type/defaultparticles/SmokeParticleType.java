package net.me.daemon.particles.api.type.defaultparticles;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.me.daemon.Main;
import net.me.daemon.particles.api.register.DaemonShaderRegistry;
import net.me.daemon.particles.api.type.DaemonParticleType;
import net.me.daemon.particles.util.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
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
        update_uniforms(context, Identifier.of(Main.MOD_ID, "smoke"));
        set_texture(0);

//        set_screen_texture(0);
        matrixStack = context.matrixStack();
        matrixStack.push();

    }

    @Override
    public void render(WorldRenderContext context) {
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder buffer = tes.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        Vec3d camPos = context.camera().getPos();


        // TODO: find out why fps drop a lot when your too close to the particle
        double distSq = camPos.squaredDistanceTo(position);

        matrixStack.translate(
                (float) (position.x - camPos.x) + 0.5f,
                (float) (position.y - camPos.y) + 0.5f,
                (float) (position.z - camPos.z) + 0.5f
        );

        // billboarding
        matrixStack.multiply(context.camera().getRotation());
        matrixStack.scale(3,3,3); // scale the particle

        Matrix4f mat = matrixStack.peek().getPositionMatrix();

        Vec3d topLeft     = new Vec3d( -0.5, 0.5, 0);
        Vec3d topRight    = new Vec3d( 0.5,  0.5, 0);
        Vec3d bottomRight = new Vec3d( 0.5, -0.5, 0);
        Vec3d bottomLeft  = new Vec3d( -0.5,-0.5, 0);

        buffer.vertex(mat, (float) topLeft.x, (float) topLeft.y, (float) topLeft.z)
                .color(255, 0, 0, 255)
                .texture(0f, 0f); // top-left UV
        buffer.vertex(mat, (float) topRight.x, (float) topRight.y, (float) topRight.z)
                .color(255, 0, 0, 255)
                .texture(1f, 0f); // top-right UV
        buffer.vertex(mat, (float) bottomRight.x, (float) bottomRight.y, (float) bottomRight.z)
                .color(255, 0, 0, 255)
                .texture(1f, 1f); // bottom-right UV
        buffer.vertex(mat, (float) bottomLeft.x, (float) bottomLeft.y, (float) bottomLeft.z)
                .color(255, 0, 0, 255)
                .texture(0f, 1f); // bottom-left UV



        // We'll get to this bit in the next section.
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        // USE CUSTOM SHADER
        ShaderProgram shader = DaemonShaderRegistry.get(Identifier.of(Main.MOD_ID, "smoke"));
        shader.bind();

        BufferRenderer.draw(buffer.end());

        shader.unbind();

        matrixStack.pop();

    }





























}
